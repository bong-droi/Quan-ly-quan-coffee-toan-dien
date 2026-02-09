from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.db.models import Sum, Count, Q
from django.utils import timezone
from datetime import datetime, date
from .models import Salary, BaseSalary
from .forms import SalaryForm, BaseSalaryForm, SalaryFilterForm, CalculateSalaryForm
from django.contrib.auth import get_user_model
from Shifts.models import Shift, AssignedShift, ShiftRegistration

User = get_user_model()

# ============ TÍNH LƯƠNG TỰ ĐỘNG ============

@login_required
def calculate_salary(request):
    """Tính lương tự động cho tất cả nhân viên"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền tính lương")
        return redirect('/')
    
    # Khởi tạo form ban đầu
    form = None
    
    if request.method == 'POST':
        form = CalculateSalaryForm(request.POST)
        if form.is_valid():
            month = int(form.cleaned_data['month'])
            year = int(form.cleaned_data['year'])
            
            # Kiểm tra đã tính lương tháng này chưa
            existing = Salary.objects.filter(month=month, year=year)
            if existing.exists():
                messages.warning(request, f"Lương tháng {month}/{year} đã được tính trước đó!")
                return redirect('salary_list')
            
            # Lấy tất cả nhân viên active
            staff_list = User.objects.filter(role='staff', is_active=True)
            
            created_count = 0
            error_count = 0
            errors = []
            
            for staff in staff_list:
                try:
                    # Lấy các ca đã được phân công trong tháng
                    assigned_shifts = AssignedShift.objects.filter(
                        user=staff,
                        date__year=year,
                        date__month=month
                    ).select_related('shift')
                    
                    # Lấy các ca đã đăng ký và được phân công
                    registered_shifts = ShiftRegistration.objects.filter(
                        user=staff,
                        date__year=year,
                        date__month=month,
                        status='assigned'
                    ).select_related('shift')
                    
                    # Gộp tất cả ca
                    total_shifts = assigned_shifts.count() + registered_shifts.count()
                    
                    if total_shifts == 0:
                        continue  # Không có ca thì không tính lương
                    
                    # Tính tổng giờ từ ca đã phân công
                    total_hours = 0
                    
                    # Tính từ assigned_shifts
                    for assigned_shift in assigned_shifts:
                        start_datetime = datetime.combine(assigned_shift.date, assigned_shift.shift.start_time)
                        end_datetime = datetime.combine(assigned_shift.date, assigned_shift.shift.end_time)
                        duration = (end_datetime - start_datetime).total_seconds() / 3600
                        total_hours += duration
                    
                    # Tính từ registered_shifts đã được assigned
                    for reg_shift in registered_shifts:
                        start_datetime = datetime.combine(reg_shift.date, reg_shift.shift.start_time)
                        end_datetime = datetime.combine(reg_shift.date, reg_shift.shift.end_time)
                        duration = (end_datetime - start_datetime).total_seconds() / 3600
                        total_hours += duration
                    
                    # Lấy cấu hình lương cơ bản
                    base_salary_config = BaseSalary.objects.filter(
                        staff_type='parttime'
                    ).first()
                    
                    if not base_salary_config:
                        # Tạo cấu hình mặc định nếu chưa có
                        base_salary_config = BaseSalary.objects.create(
                            staff_type='parttime',
                            hourly_rate=20000,  # 20k/giờ
                            shift_rate=150000  # 150k/ca
                        )
                    
                    # Tính lương theo ca HOẶC theo giờ (ưu tiên cao hơn)
                    if base_salary_config.shift_rate > 0:
                        base_salary = total_shifts * base_salary_config.shift_rate
                    else:
                        base_salary = total_hours * base_salary_config.hourly_rate
                    
                    # Kiểm tra xem nhân viên đã có bảng lương tháng này chưa
                    if Salary.objects.filter(employee=staff, month=month, year=year).exists():
                        error_count += 1
                        errors.append(f"Nhân viên {staff.username} đã có bảng lương tháng {month}/{year}")
                        continue
                    
                    # Tạo bảng lương
                    salary = Salary.objects.create(
                        employee=staff,
                        month=month,
                        year=year,
                        total_shifts=total_shifts,
                        total_hours=round(total_hours, 2),
                        base_salary=base_salary,
                        bonus=0,
                        deduction=0,
                        status='calculated'
                    )
                    
                    created_count += 1
                    
                except Exception as e:
                    error_count += 1
                    errors.append(f"Lỗi tính lương cho {staff.username}: {str(e)}")
                    print(f"Lỗi tính lương cho {staff.username}: {str(e)}")
            
            if created_count > 0:
                messages.success(request, f"✅ Đã tính lương cho {created_count} nhân viên tháng {month}/{year}")
            else:
                messages.info(request, "Không có nhân viên nào có ca làm việc trong tháng này")
            
            if error_count > 0:
                messages.warning(request, f"Có {error_count} lỗi khi tính lương")
                if errors:
                    # Hiển thị 3 lỗi đầu tiên
                    for i, error in enumerate(errors[:3]):
                        messages.warning(request, f"• {error}")
                    if len(errors) > 3:
                        messages.warning(request, f"... và {len(errors) - 3} lỗi khác")
            
            return redirect('salary_list')
    else:
        # GET request - tạo form mới
        form = CalculateSalaryForm()
    
    # GET request - hiển thị form
    current_year = datetime.now().year
    current_month = datetime.now().month
    years = list(range(current_year - 2, current_year + 3))
    
    context = {
        'form': form,
        'months': list(range(1, 13)),
        'years': years,
        'current_year': current_year,
        'current_month': current_month,
    }
    return render(request, 'salary/calculate_salary.html', context)

# ============ QUẢN LÝ LƯƠNG ============

@login_required
def salary_list(request):
    """Danh sách bảng lương"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền xem bảng lương")
        return redirect('/')
    
    form = SalaryFilterForm(request.GET or None)
    salaries = Salary.objects.all().select_related('employee')
    
    if form.is_valid():
        month = form.cleaned_data.get('month')
        year = form.cleaned_data.get('year')
        employee = form.cleaned_data.get('employee')
        
        if month:
            salaries = salaries.filter(month=month)
        if year:
            salaries = salaries.filter(year=year)
        if employee:
            salaries = salaries.filter(employee=employee)
    
    # Tính tổng
    total_salary = salaries.aggregate(total=Sum('total_salary'))['total'] or 0
    total_employees = salaries.values('employee').distinct().count()
    
    context = {
        'salaries': salaries,
        'total_salary': total_salary,
        'total_employees': total_employees,
        'form': form,
    }
    return render(request, 'salary/salary_list.html', context)


@login_required
def salary_create(request):
    """Tạo bảng lương thủ công"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền tạo bảng lương")
        return redirect('salary_list')
    
    if request.method == 'POST':
        form = SalaryForm(request.POST)
        if form.is_valid():
            salary = form.save(commit=False)
            
            # Kiểm tra trùng
            if Salary.objects.filter(
                employee=salary.employee,
                month=salary.month,
                year=salary.year
            ).exists():
                messages.error(request, "Nhân viên này đã có bảng lương tháng này")
                return redirect('salary_create')
            
            salary.save()
            messages.success(request, "✅ Đã tạo bảng lương thành công")
            return redirect('salary_list')
    else:
        form = SalaryForm()
    
    context = {
        'form': form,
        'title': 'Tạo bảng lương thủ công'
    }
    return render(request, 'salary/salary_form.html', context)


@login_required 
def salary_update(request, pk):
    """Cập nhật bảng lương"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền chỉnh sửa bảng lương")
        return redirect('salary_list')
    
    salary = get_object_or_404(Salary, pk=pk)
    
    if request.method == 'POST':
        form = SalaryForm(request.POST, instance=salary)
        if form.is_valid():
            form.save()
            messages.success(request, "✅ Đã cập nhật bảng lương")
            return redirect('salary_list')
    else:
        form = SalaryForm(instance=salary)
    
    context = {
        'form': form,
        'title': 'Chỉnh sửa bảng lương',
        'salary': salary
    }
    return render(request, 'salary/salary_form.html', context)


@login_required
def salary_detail(request, pk):
    """Chi tiết bảng lương"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền xem chi tiết lương")
        return redirect('salary_list')
    
    salary = get_object_or_404(Salary, pk=pk)
    
    # Lấy danh sách ca đã làm trong tháng (nếu có model Shift)
    try:
        from Shifts.models import Shift
        shifts = Shift.objects.filter(
            assigned_to=salary.employee,
            start_time__year=salary.year,
            start_time__month=salary.month,
            status='completed'
        ).order_by('start_time')
    except:
        shifts = None
    
    context = {
        'salary': salary,
        'shifts': shifts,
        'today': timezone.now().date(),
    }
    return render(request, 'salary/salary_detail.html', context)


@login_required
def make_payment(request, pk):
    """Đánh dấu đã thanh toán lương"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền thanh toán lương")
        return redirect('salary_list')
    
    salary = get_object_or_404(Salary, pk=pk)
    
    if salary.status == 'paid':
        messages.warning(request, "Lương này đã được thanh toán rồi")
        return redirect('salary_list')
    
    salary.status = 'paid'
    salary.paid_date = timezone.now().date()
    salary.save()
    
    messages.success(request, f"✅ Đã thanh toán lương cho {salary.employee.get_full_name()}")
    return redirect('salary_list')


# ============ QUẢN LÝ LƯƠNG CƠ BẢN ============

@login_required
def base_salary_list(request):
    """Danh sách cấu hình lương cơ bản"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền quản lý lương cơ bản")
        return redirect('/')
    
    base_salaries = BaseSalary.objects.all()
    
    context = {
        'base_salaries': base_salaries,
    }
    return render(request, 'salary/base_salary_list.html', context)


@login_required
def base_salary_update(request, pk):
    """Cập nhật lương cơ bản"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền chỉnh sửa lương cơ bản")
        return redirect('base_salary_list')
    
    base_salary = get_object_or_404(BaseSalary, pk=pk)
    
    if request.method == 'POST':
        form = BaseSalaryForm(request.POST, instance=base_salary)
        if form.is_valid():
            form.save()
            messages.success(request, "✅ Đã cập nhật lương cơ bản")
            return redirect('base_salary_list')
    else:
        form = BaseSalaryForm(instance=base_salary)
    
    context = {
        'form': form,
        'title': 'Chỉnh sửa lương cơ bản',
    }
    return render(request, 'salary/base_salary_form.html', context)


# ============ NHÂN VIÊN XEM LƯƠNG ============

@login_required
def my_salary(request):
    """Nhân viên xem lương của mình"""
    if request.user.role != 'staff':
        messages.error(request, "Chỉ nhân viên mới có thể xem lương của mình")
        return redirect('/')
    
    salaries = Salary.objects.filter(
        employee=request.user
    ).order_by('-year', '-month')
    
    # Tính tổng
    total_earned = salaries.filter(status='paid').aggregate(
        total=Sum('total_salary')
    )['total'] or 0
    
    paid_salaries = salaries.filter(status='paid').count()
    
    context = {
        'salaries': salaries,
        'total_earned': total_earned,
        'paid_salaries': paid_salaries,
    }
    return render(request, 'salary/my_salary.html', context)


@login_required
def my_salary_detail(request, pk):
    """Nhân viên xem chi tiết lương"""
    if request.user.role != 'staff':
        messages.error(request, "Chỉ nhân viên mới có thể xem chi tiết lương")
        return redirect('/')
    
    salary = get_object_or_404(Salary, pk=pk, employee=request.user)
    
    context = {
        'salary': salary,
    }
    return render(request, 'salary/my_salary_detail.html', context)