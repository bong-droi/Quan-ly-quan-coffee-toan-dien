from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.db.models import Count, Q, Sum, F
from django.utils import timezone
from datetime import date, datetime, timedelta
from .models import Shift, AssignedShift, ShiftRegistration, User
from .forms import (ShiftForm, ShiftRegistrationForm, AssignShiftForm,)
import calendar

# ============ QUẢN LÝ CA (OWNER) ============
@login_required
def shift_list(request):
    """Danh sách ca làm việc"""
    if request.user.role not in ['owner', 'staff']:
        messages.error(request, "Bạn không có quyền truy cập")
        return redirect('/accounts/login/')
    
    # Lấy tất cả ca
    shifts = Shift.objects.all().order_by('date', 'start_time')
    
    # Lọc theo ngày
    date_filter = request.GET.get('date')
    if date_filter:
        try:
            date_filter = datetime.strptime(date_filter, '%Y-%m-%d').date()
            shifts = shifts.filter(date=date_filter)
        except ValueError:
            date_filter = None
    
    # Tính tổng đăng ký
    total_registered = 0
    for shift in shifts:
        try:
            shift.registered_count = shift.get_registered_count(date_filter)
            total_registered += shift.registered_count
        except Exception as e:
            shift.registered_count = 0
    
    context = {
        'shifts': shifts,
        'date_filter': date_filter,
        'today': timezone.now().date(),
        'total_registered': total_registered,  # Thêm biến này
        'total_shifts': shifts.count(),        # Thêm biến này
    }
    return render(request, 'shifts/shifts_list.html', context)

@login_required
def shift_create(request):
    """Tạo ca làm việc mới"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền tạo ca")
        return redirect('shifts:shift_list')
    
    if request.method == 'POST':
        form = ShiftForm(request.POST)
        if form.is_valid():
            form.save()
            messages.success(request, "Tạo ca làm việc thành công")
            return redirect('shifts:shift_list')
    else:
        form = ShiftForm()
    
    return render(request, 'shifts/shift_form.html', {'form': form, 'title': 'Tạo ca mới'})

@login_required
def shift_update(request, pk):
    """Cập nhật ca làm việc"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền chỉnh sửa ca")
        return redirect('shifts:shift_list')
    
    shift = get_object_or_404(Shift, pk=pk)
    
    if request.method == 'POST':
        form = ShiftForm(request.POST, instance=shift)
        if form.is_valid():
            form.save()
            messages.success(request, "Cập nhật ca làm việc thành công")
            return redirect('shifts:shift_list')
    else:
        form = ShiftForm(instance=shift)
    
    return render(request, 'shifts/shift_form.html', {'form': form, 'title': 'Chỉnh sửa ca'})


@login_required
def shift_delete(request, pk):
    """Xóa ca làm việc"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền xóa ca")
        return redirect('shifts:shift_list')
    
    shift = get_object_or_404(Shift, pk=pk)
    
    # Kiểm tra có nhân viên đã đăng ký chưa
    if shift.registrations.exists() or shift.assigned_shifts.exists():
        messages.error(request, "Không thể xóa ca đã có nhân viên đăng ký hoặc phân công")
        return redirect('shifts:shift_list')
    
    if request.method == 'POST':
        shift.delete()
        messages.success(request, "Xóa ca làm việc thành công")
        return redirect('shifts:shift_list')
    
    return render(request, 'shifts/shift_delete.html', {'shift': shift})


@login_required
def shift_detail(request, pk):
    """Chi tiết ca làm việc"""
    if request.user.role not in ['owner', 'staff']:
        messages.error(request, "Bạn không có quyền truy cập")
        return redirect('/accounts/login/')
    
    shift = get_object_or_404(Shift, pk=pk)
    today = timezone.now().date()
    
    # Xử lý date_filter
    date_filter = request.GET.get('date')
    if date_filter:
        try:
            date_filter = datetime.strptime(date_filter, '%Y-%m-%d').date()
        except ValueError:
            date_filter = shift.date
    else:
        date_filter = shift.date
    
    # Danh sách nhân viên đã đăng ký
    registrations = ShiftRegistration.objects.filter(
        shift=shift, 
        date=date_filter
    ).select_related('user')
    
    # Danh sách nhân viên đã được phân công
    assigned = AssignedShift.objects.filter(
        shift=shift, 
        date=date_filter
    ).select_related('user')
    
    # Danh sách ID của nhân viên đã phân công (dùng để check trong template)
    assigned_user_ids = [ass.user.id for ass in assigned]
    
    # Tính số chỗ còn lại
    remaining_slots = shift.capacity - assigned.count()
    
    context = {
        'shift': shift,
        'date_filter': date_filter,
        'today': today,
        'registrations': registrations,
        'assigned': assigned,
        'assigned_user_ids': assigned_user_ids,
        'remaining_slots': remaining_slots,
        'duration_hours': shift.get_duration_hours(),
    }
    return render(request, 'shifts/shift_detail.html', context)

# ============ ĐĂNG KÝ CA (STAFF) ============

@login_required
def shift_register(request):
    """Nhân viên đăng ký ca làm việc - Đăng ký trực tiếp không cần duyệt"""
    if request.user.role != 'staff':
        messages.error(request, "Chỉ nhân viên mới có thể đăng ký ca")
        return redirect('/accounts/dashboard/staff/')
    
    if request.method == 'POST':
        form = ShiftRegistrationForm(request.POST, user=request.user)
        if form.is_valid():
            try:
                registration = form.save(commit=False)
                registration.user = request.user
                
                # KIỂM TRA TRÙNG LẶP
                # 1. Kiểm tra đã đăng ký ca này chưa
                if ShiftRegistration.objects.filter(
                    user=request.user,
                    shift=registration.shift,
                    date=registration.date
                ).exists():
                    messages.warning(request, f"Bạn đã đăng ký ca {registration.shift.name} ngày {registration.date.strftime('%d/%m/%Y')} rồi!")
                    return redirect('shifts:shift_register')
                
                # 2. Kiểm tra đã được phân công ca này chưa
                if AssignedShift.objects.filter(
                    user=request.user,
                    shift=registration.shift,
                    date=registration.date
                ).exists():
                    messages.warning(request, f"Bạn đã được phân công ca {registration.shift.name} ngày {registration.date.strftime('%d/%m/%Y')} rồi!")
                    return redirect('shifts:shift_register')
                
                # LƯU ĐĂNG KÝ TRỰC TIẾP
                registration.status = 'registered'
                registration.save()
                
                messages.success(request, f"✅ Đã đăng ký ca {registration.shift.name} vào ngày {registration.date.strftime('%d/%m/%Y')}")
                return redirect('shifts:shift_register')
                
            except Exception as e:
                messages.error(request, f"Lỗi khi đăng ký: {str(e)}")
        else:
            for error in form.errors.values():
                messages.error(request, error)
    else:
        form = ShiftRegistrationForm(user=request.user)
    
    # Lấy danh sách ca có thể đăng ký (trong vòng 30 ngày tới)
    today = timezone.now().date()
    thirty_days_later = today + timedelta(days=30)
    
    # # ĐƠN GIẢN HÓA QUERY - KHÔNG DÙNG ANNOTATE PHỨC TẠP
    # available_shifts = Shift.objects.filter(
    #     date__range=[today, thirty_days_later]
    # ).order_by('date', 'start_time')
    
    all_shifts = Shift.objects.filter(
        date__range=[today, thirty_days_later]
    ).order_by('date', 'start_time')
    
    # Lấy danh sách ca đã đăng ký
    registrations = ShiftRegistration.objects.filter(
        user=request.user,
        date__gte=today
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    registrations = ShiftRegistration.objects.filter(
        user=request.user,
        date__gte=today
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    # Tạo danh sách ca đã đăng ký ID để kiểm tra nhanh
    registered_shift_ids = set(reg.shift.id for reg in registrations)
    
    # Tạo danh sách ca sẵn sàng để hiển thị (chưa đăng ký)
    available_shifts = []
    for shift in all_shifts:
        # Tính số lượng đã phân công
        assigned_count = AssignedShift.objects.filter(
            shift=shift,
            date=shift.date
        ).count()
        
        # Tính số lượng đã đăng ký
        registered_count = ShiftRegistration.objects.filter(
            shift=shift,
            date=shift.date
        ).count()
        
        # Kiểm tra còn chỗ không
        has_capacity = (assigned_count + registered_count) < shift.capacity
        
        # Kiểm tra user đã đăng ký chưa
        user_already_registered = shift.id in registered_shift_ids
        
        if has_capacity and not user_already_registered:
            available_shifts.append({
                'shift': shift,
                'assigned_count': assigned_count,
                'registered_count': registered_count,
                'available_slots': shift.capacity - (assigned_count + registered_count)
            })
    
    context = {
        'form': form,
        'registrations': registrations,
        'today': today,
        'available_shifts': available_shifts,  # Danh sách đã xử lý
        'registered': registrations,
    }
    return render(request, 'shifts/shift_register.html', context)

@login_required
def shift_unregister(request, reg_id):
    """Hủy đăng ký ca làm việc"""
    try:
        registration = ShiftRegistration.objects.get(id=reg_id, user=request.user)
        
        # Chỉ cho phép hủy đăng ký nếu chưa được phân công và chưa diễn ra
        today = timezone.now().date()
        if registration.date > today and not registration.is_assigned:
            registration.delete()
            messages.success(request, f"Đã hủy đăng ký ca {registration.shift.name} ngày {registration.date.strftime('%d/%m/%Y')}")
        else:
            messages.error(request, "Không thể hủy đăng ký ca đã được phân công hoặc đã diễn ra")
            
    except ShiftRegistration.DoesNotExist:
        messages.error(request, "Không tìm thấy đăng ký ca")
    
    return redirect('shifts:shift_register')

@login_required
def my_shifts(request):
    """Xem toàn bộ ca của tôi - cả đăng ký và phân công"""
    if request.user.role != 'staff':
        messages.error(request, "Chỉ nhân viên mới có thể xem ca làm việc")
        return redirect('/accounts/dashboard/staff/')
    
    today = timezone.now().date()
    
    # 1. Lấy tất cả ca ĐÃ PHÂN CÔNG
    assigned_shifts = AssignedShift.objects.filter(
        user=request.user,
        date__gte=today  # Chỉ lấy ca từ hôm nay trở đi
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    # 2. Lấy tất cả ca ĐÃ ĐĂNG KÝ
    registered_shifts = ShiftRegistration.objects.filter(
        user=request.user,
        date__gte=today
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    # 3. Gộp tất cả ca thành một danh sách
    all_shifts = []
    
    # Thêm ca đã phân công
    for shift in assigned_shifts:
        all_shifts.append({
            'id': shift.id,
            'type': 'assigned',
            'status': 'assigned',
            'date': shift.date,
            'shift': shift.shift,
            'assigned_at': shift.assigned_at
        })
    
    # Thêm ca đã đăng ký
    for shift in registered_shifts:
        all_shifts.append({
            'id': shift.id,
            'type': 'registered',
            'status': shift.status,
            'date': shift.date,
            'shift': shift.shift,
            'registered_at': shift.registered_at
        })
    
    # 4. Sắp xếp theo ngày (từ sớm nhất đến muộn nhất)
    all_shifts.sort(key=lambda x: x['date'])
    
    # 5. Tính tổng số ca
    total_shifts = len(all_shifts)
    
    # 6. Tính tổng số giờ
    total_hours = 0
    for shift in all_shifts:
        total_hours += shift['shift'].get_duration_hours()
    
    # 7. Lấy tháng và năm hiện tại
    month = today.month
    year = today.year
    
    # 8. Phân loại ca sắp tới (7 ngày)
    seven_days_later = today + timedelta(days=7)
    upcoming_shifts = [s for s in all_shifts if today <= s['date'] <= seven_days_later]
    
    context = {
        'all_shifts': all_shifts,
        'upcoming_shifts': upcoming_shifts,
        'total_shifts': total_shifts,
        'total_hours': round(total_hours, 1),
        'assigned_count': assigned_shifts.count(),
        'registered_count': registered_shifts.count(),
        'month': month,
        'year': year,
        'today': today,
    }
    
    # Sử dụng template my_shifts.html đã có
    return render(request, 'shifts/my_shifts.html', context)

@login_required
def my_registrations(request):
    """Xem ca đã đăng ký của tôi"""
    if request.user.role != 'staff':
        messages.error(request, "Chỉ nhân viên mới có thể xem ca đã đăng ký")
        return redirect('accounts:staff_dashboard')
    
    today = timezone.now().date()
    registrations = ShiftRegistration.objects.filter(
        user=request.user,
        date__gte=today
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    context = {
        'registrations': registrations,
        'today': today,
    }
    return render(request, 'shifts/my_registrations.html', context)

@login_required
def shifts_dashboard(request):
    """Dashboard tổng quan về ca làm việc"""
    if request.user.role != 'staff':
        messages.error(request, "Chỉ nhân viên mới có thể xem dashboard ca")
        return redirect('/accounts/dashboard/staff/')
    
    today = timezone.now().date()
    
    # Thống kê
    total_assigned = AssignedShift.objects.filter(
        user=request.user,
        date__gte=today
    ).count()
    
    total_registered = ShiftRegistration.objects.filter(
        user=request.user,
        date__gte=today,
        status='registered'
    ).count()
    
    total_shifts = total_assigned + total_registered
    
    # Tính tổng giờ
    assigned_hours = AssignedShift.objects.filter(
        user=request.user,
        date__gte=today
    ).aggregate(
        total_hours=Sum('shift__end_time__hour') - Sum('shift__start_time__hour')
    )['total_hours'] or 0
    
    registered_hours = ShiftRegistration.objects.filter(
        user=request.user,
        date__gte=today,
        status='registered'
    ).aggregate(
        total_hours=Sum('shift__end_time__hour') - Sum('shift__start_time__hour')
    )['total_hours'] or 0
    
    total_hours = assigned_hours + registered_hours
    
    # Ca sắp tới (7 ngày)
    seven_days_later = today + timedelta(days=7)
    
    # Ca đã phân công sắp tới
    assigned_upcoming = AssignedShift.objects.filter(
        user=request.user,
        date__range=[today, seven_days_later]
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    # Ca đã đăng ký sắp tới
    registered_upcoming = ShiftRegistration.objects.filter(
        user=request.user,
        date__range=[today, seven_days_later],
        status='registered'
    ).select_related('shift').order_by('date', 'shift__start_time')
    
    # Gộp và sắp xếp
    upcoming_shifts = []
    for shift in assigned_upcoming:
        upcoming_shifts.append({
            'id': shift.id,
            'type': 'assigned',
            'status': 'assigned',
            'date': shift.date,
            'shift': shift.shift
        })
    
    for shift in registered_upcoming:
        upcoming_shifts.append({
            'id': shift.id,
            'type': 'registered',
            'status': 'registered',
            'date': shift.date,
            'shift': shift.shift
        })
    
    # Sắp xếp theo ngày
    upcoming_shifts.sort(key=lambda x: x['date'])
    
    # Tạo lịch tháng hiện tại
    import calendar
    current_year = today.year
    current_month = today.month
    
    cal = calendar.monthcalendar(current_year, current_month)
    calendar_days = []
    
    # Lấy tất cả ca trong tháng
    month_start = date(current_year, current_month, 1)
    month_end = date(current_year, current_month, calendar.monthrange(current_year, current_month)[1])
    
    month_shifts_assigned = AssignedShift.objects.filter(
        user=request.user,
        date__range=[month_start, month_end]
    ).select_related('shift')
    
    month_shifts_registered = ShiftRegistration.objects.filter(
        user=request.user,
        date__range=[month_start, month_end],
        status='registered'
    ).select_related('shift')
    
    # Tạo dictionary để tra cứu nhanh
    shifts_by_date = {}
    for shift in month_shifts_assigned:
        if shift.date not in shifts_by_date:
            shifts_by_date[shift.date] = []
        shifts_by_date[shift.date].append({
            'name': shift.shift.name,
            'time': f"{shift.shift.start_time.strftime('%H:%M')}-{shift.shift.end_time.strftime('%H:%M')}",
            'type': 'assigned'
        })
    
    for shift in month_shifts_registered:
        if shift.date not in shifts_by_date:
            shifts_by_date[shift.date] = []
        shifts_by_date[shift.date].append({
            'name': shift.shift.name,
            'time': f"{shift.shift.start_time.strftime('%H:%M')}-{shift.shift.end_time.strftime('%H:%M')}",
            'type': 'registered'
        })
    
    # Tạo calendar days
    for week in cal:
        for day in week:
            if day == 0:
                calendar_days.append({'date': None, 'shifts': [], 'is_today': False, 'has_shift': False})
            else:
                day_date = date(current_year, current_month, day)
                is_today = (day_date == today)
                has_shift = (day_date in shifts_by_date)
                
                calendar_days.append({
                    'date': day_date,
                    'shifts': shifts_by_date.get(day_date, []),
                    'is_today': is_today,
                    'has_shift': has_shift
                })
    
    context = {
        'total_shifts': total_shifts,
        'assigned_count': total_assigned,
        'registered_count': total_registered,
        'total_hours': total_hours,
        'upcoming_shifts': upcoming_shifts[:10],  # Giới hạn 10 ca
        'current_month': current_month,
        'current_year': current_year,
        'calendar_days': calendar_days,
        'today': today,
    }
    
    return render(request, 'shifts/shifts_dashboard.html', context)

# ============ PHÂN CÔNG CA (OWNER) ============

@login_required
def assign_shift(request):
    """Phân công ca cho nhân viên"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền phân công ca")
        return redirect('shifts:shift_list')
    
    # Lấy tất cả nhân viên
    all_staff = User.objects.filter(role='staff', is_active=True).order_by('first_name')
    
    if request.method == 'POST':
        shift_id = request.POST.get('shift')
        employee_ids = request.POST.getlist('employees')  # Lấy danh sách nhân viên
        
        if not shift_id or not employee_ids:
            messages.error(request, "Vui lòng chọn ca và nhân viên")
            return redirect('shifts:assign_shift')
        
        try:
            shift = Shift.objects.get(id=shift_id)
            date_value = shift.date
            
            # Kiểm tra số lượng đã phân công
            currently_assigned = AssignedShift.objects.filter(
                shift=shift, 
                date=date_value
            ).count()
            
            available_slots = shift.capacity - currently_assigned
            
            if len(employee_ids) > available_slots:
                messages.error(request, f"Ca chỉ còn {available_slots} chỗ trống")
                return redirect('shifts:assign_shift')
            
            # Phân công cho từng nhân viên
            count = 0
            for emp_id in employee_ids:
                try:
                    employee = User.objects.get(id=emp_id, role='staff')
                    
                    # Kiểm tra đã phân công chưa
                    if not AssignedShift.objects.filter(
                        user=employee, 
                        shift=shift, 
                        date=date_value
                    ).exists():
                        AssignedShift.objects.create(
                            user=employee,
                            shift=shift,
                            date=date_value
                        )
                        count += 1
                except User.DoesNotExist:
                    continue
            
            messages.success(request, f"✅ Đã phân công {count} nhân viên vào ca {shift.name} ngày {date_value.strftime('%d/%m/%Y')}")
            return redirect('shifts:assign_shift')
            
        except Shift.DoesNotExist:
            messages.error(request, "❌ Ca không tồn tại")
            return redirect('shifts:assign_shift')
    
    # Lấy danh sách ca có sẵn (chỉ ca trong tương lai)
    today = timezone.now().date()
    shifts = Shift.objects.filter(date__gte=today).order_by('date', 'start_time')
    
    context = {
        'all_staff': all_staff,
        'shifts': shifts,
    }
    return render(request, 'shifts/assign_form.html', context)

@login_required
def unassign_shift(request, pk):
    """Hủy phân công ca"""
    if request.user.role != 'owner':
        messages.error(request, "Chỉ chủ quán mới có quyền hủy phân công")
        return redirect('shifts:shift_list')
    
    assigned = get_object_or_404(AssignedShift, pk=pk)
    
    # Không cho hủy nếu ca đã qua
    if assigned.date < timezone.now().date():
        messages.error(request, "Không thể hủy phân công ca đã qua")
        return redirect('shifts:shift_detail', pk=assigned.shift.pk)
    
    shift_name = assigned.shift.name
    assigned.delete()
    messages.success(request, f"Đã hủy phân công ca {shift_name}")
    return redirect('shifts:shift_detail', pk=assigned.shift.pk)