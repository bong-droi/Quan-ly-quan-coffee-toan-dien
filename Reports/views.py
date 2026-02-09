# Reports/views.py
# ✅ PHIÊN BẢN SỬA LỖI: Sử dụng Order từ app orders

from gc import get_count
from django.shortcuts import render, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.db.models import Sum, Count, Avg, Q
from django.utils import timezone
from datetime import datetime

# ✅ IMPORT ĐÚNG: Sử dụng Order từ app orders
from orders.models import Order, OrderItem

@login_required
def dashboard(request):
    """
    Dashboard báo cáo - Hiển thị thống kê tổng quan
    ✅ FIX: Sử dụng Order.objects thay vì Bill.objects
    """
    try:
        # ✅ Chỉ lấy đơn đã xử lý (processed)
        processed_orders = Order.objects.filter(status='processed')
        
        # Hôm nay
        today = timezone.now().date()
        
        # Query với aggregate (NHANH HƠN nhiều so với tính từng order)
        today_stats = processed_orders.filter(
            created_at__date=today
        ).aggregate(
            revenue=Sum('total_amount'),
            count=Count('id')
        )
        
        total_stats = processed_orders.aggregate(
            revenue=Sum('total_amount'),
            count=Count('id')
        )
        
        today_revenue = today_stats['revenue'] or 0
        today_bills_count = today_stats['count'] or 0
        total_revenue = total_stats['revenue'] or 0
        total_bills_count = total_stats['count'] or 0
        
        # ✅ THÊM: Debug info
        print(f"DEBUG - Dashboard:")
        print(f"  - Total processed orders: {processed_orders.count()}")
        print(f"  - Today orders: {today_bills_count}")
        print(f"  - Today revenue: {today_revenue}")
        print(f"  - Total revenue: {total_revenue}")
        
    except Exception as e:
        print(f"ERROR in dashboard: {str(e)}")
        today_revenue = 0
        today_bills_count = 0
        total_revenue = 0
        total_bills_count = 0
    
    context = {
        'today_revenue': today_revenue,
        'today_bills': today_bills_count,
        'total_revenue': total_revenue,
        'total_bills': total_bills_count,
        'today': timezone.now().date(),
    }
    
    return render(request, 'reports/dashboard.html', context)


@login_required
def revenue_report(request):
    """
    Báo cáo doanh thu - Query tối ưu với total_amount và select_related
    ✅ FIX: Sử dụng Order.objects thay vì Bill.objects
    """
    
    # Mặc định: hôm nay
    end_date = timezone.now().date()
    start_date = end_date
    
    # Lấy tham số từ URL
    start_date_str = request.GET.get('start_date')
    end_date_str = request.GET.get('end_date')
    
    if start_date_str:
        try:
            start_date = datetime.strptime(start_date_str, '%Y-%m-%d').date()
        except:
            start_date = timezone.now().date()
    
    if end_date_str:
        try:
            end_date = datetime.strptime(end_date_str, '%Y-%m-%d').date()
        except:
            end_date = timezone.now().date()
    
    if start_date > end_date:
        start_date, end_date = end_date, start_date
    
    try:
        # ✅ Query tối ưu: select_related để giảm số query
        orders = Order.objects.filter(
            created_at__date__gte=start_date,
            created_at__date__lte=end_date,
            status='processed'  # ✅ Chỉ lấy đơn đã xử lý
        ).select_related(
            'staff', 'customer'
        ).prefetch_related(
            'items__menu_item'
        ).order_by('-created_at')
        
        # ✅ Aggregate để tính tổng (NHANH nhất)
        stats = orders.aggregate(
            total_revenue=Sum('total_amount'),
            total_bills=Count('id'),
            avg_bill=Avg('total_amount')
        )
        
        # ✅ Thống kê theo loại đơn
        type_stats = {
            'offline': orders.filter(order_type='offline').aggregate(Sum('total_amount'))['total_amount__sum'] or 0,
            'online': orders.filter(order_type='online').aggregate(Sum('total_amount'))['total_amount__sum'] or 0,
        }
        
        # ✅ THÊM: Debug info
        print(f"DEBUG - Revenue Report:")
        print(f"  - Date range: {start_date} to {end_date}")
        print(f"  - Total orders: {orders.count()}")
        print(f"  - Total revenue: {stats['total_revenue']}")
        print(f"  - Offline: {type_stats['offline']}, Online: {type_stats['online']}")
        
        # ✅ Format dữ liệu cho template
        bills = []
        for order in orders:
            # Tính số lượng món
            item_count = order.items.aggregate(Sum('quantity'))['quantity__sum'] or 0
            
            bills.append({
                'bill_id': f"ORD-{order.id}",
                'formatted_created_at': order.created_at.strftime('%H:%M %d/%m/%Y'),
                'table_number': order.table_number or '-',
                'customer_count': item_count,
                'staff_name': (
                    order.staff.username if order.staff 
                    else (order.customer.username if order.customer else '-')
                ),
                'payment_method': order.order_type,  # offline/online
                'final_amount': order.total_amount,
            })
        
        context = {
            'bills': bills,
            'total_revenue': stats['total_revenue'] or 0,
            'total_bills': stats['total_bills'] or 0,
            'cash_amount': type_stats['offline'],
            'card_amount': type_stats['online'],
            'average_bill': stats['avg_bill'] or 0,
            'start_date': start_date,
            'end_date': end_date,
            'date_range': f"{start_date.strftime('%d/%m/%Y')} - {end_date.strftime('%d/%m/%Y')}",
        }
        
    except Exception as e:
        print(f"ERROR in revenue_report: {str(e)}")
        context = {
            'bills': [],
            'total_revenue': 0,
            'total_bills': 0,
            'cash_amount': 0,
            'card_amount': 0,
            'average_bill': 0,
            'start_date': start_date,
            'end_date': end_date,
            'date_range': f"{start_date.strftime('%d/%m/%Y')} - {end_date.strftime('%d/%m/%Y')}",
            'error': str(e)
        }
    
    return render(request, 'reports/revenue_report.html', context)

@login_required
def bill_detail(request, bill_id):
    """
    Chi tiết hóa đơn với đầy đủ thông tin nhân viên
    """
    context = {}
    
    try:
        # ✅ Parse ID từ format "ORD-123" -> 123
        if bill_id.startswith('ORD-'):
            order_id = int(bill_id.replace('ORD-', ''))
        else:
            order_id = int(bill_id)
        
        # ✅ Lấy Order với tất cả thông tin liên quan
        order = get_object_or_404(
            Order.objects.select_related('staff', 'customer')
                        .prefetch_related('items__menu_item'),
            id=order_id
        )
        
        # ✅ Debug: Kiểm tra order
        print(f"DEBUG - Found order ID: {order.id}, Staff: {order.staff}")
        
        # ✅ Lấy thông tin nhân viên đầy đủ
        staff_info = {
            'username': order.staff.username if order.staff else '-',
            'full_name': '',
            'employee_id': '',
            'phone': ''
        }
        
        # ✅ Nếu staff có profile, lấy thêm thông tin
        if order.staff:
            # Thử lấy profile nếu có
            try:
                if hasattr(order.staff, 'profile'):
                    profile = order.staff.profile
                    staff_info['full_name'] = getattr(profile, 'full_name', '') or order.staff.username
                    staff_info['employee_id'] = getattr(profile, 'employee_id', '')
                    staff_info['phone'] = getattr(profile, 'phone', '')
                else:
                    # Nếu không có profile nhưng có staff
                    staff_info['full_name'] = order.staff.get_full_name() or order.staff.username
            except Exception as e:
                print(f"ERROR getting staff profile: {e}")
                staff_info['full_name'] = order.staff.username
        
        # ✅ Tạo danh sách items
        items = []
        total_items_count = 0
        
        for item in order.items.all():
            item_price = item.menu_item.price if item.menu_item else 0
            items.append({
                'product_name': item.menu_item.name if item.menu_item else 'Món không xác định',
                'product_price': item_price,
                'quantity': item.quantity,
                'total_price': item_price * item.quantity,
            })
            total_items_count += item.quantity
        
        # ✅ Tạo MockBillDetail
        class MockBillDetail:
            def __init__(self, data):
                self.product_name = data.get('product_name', '')
                self.product_price = data.get('product_price', 0)
                self.quantity = data.get('quantity', 0)
                self.total_price = data.get('total_price', 0)
            
            def __str__(self):
                return f"{self.product_name} x{self.quantity}"
        
        # ✅ Tạo MockBill
        class MockBill:
            def __init__(self, order, items_list, staff_info):
                self.bill_id = f"ORD-{order.id}"
                self.created_at = order.created_at
                self.staff_username = staff_info['username']
                self.staff_full_name = staff_info['full_name']
                self.staff_id = staff_info['employee_id']
                self.staff_phone = staff_info['phone']
                self.staff_name_display = staff_info['full_name'] if staff_info['full_name'] else staff_info['username']
                self.table_number = order.table_number or '-'
                self.customer_count = total_items_count
                self.payment_method = order.order_type
                self.total_amount = order.total_amount
                self.discount = getattr(order, 'discount', 0)
                self.final_amount = order.total_amount - getattr(order, 'discount', 0)
                self._items_list = items_list
                self.status = 'paid'  # Mặc định đã thanh toán
            
            def get_payment_method_display(self):
                payment_methods = {
                    'offline': 'Tại quán',
                    'online': 'Online',
                    'cash': 'Tiền mặt',
                    'card': 'Thẻ',
                }
                return payment_methods.get(self.payment_method, self.payment_method)
            
            @property
            def details(self):
                return [MockBillDetail(item) for item in self._items_list]
        
        # ✅ Tạo bill object
        bill = MockBill(order, items, staff_info)
        
        context = {
            'bill': bill,
            'order_time': order.created_at.strftime('%H:%M %d/%m/%Y'),
        }
        
        # ✅ Debug
        print(f"DEBUG - Bill Detail created successfully:")
        print(f"  - Bill ID: {bill.bill_id}")
        print(f"  - Staff: {bill.staff_name_display}")
        print(f"  - Items: {len(bill.details)}")
        print(f"  - Total: {bill.final_amount}")
        
    except ValueError as e:
        print(f"ERROR - Invalid bill_id format: {bill_id}")
        context = {
            'error': f"Mã hóa đơn không hợp lệ: {bill_id}",
            'bill': None
        }
    except Exception as e:
        print(f"ERROR in bill_detail: {str(e)}")
        import traceback
        traceback.print_exc()
        context = {
            'error': f"Lỗi khi tải hóa đơn: {str(e)}",
            'bill': None
        }
    
    # ✅ Thêm fallback để template không bị lỗi
    if 'bill' not in context or context['bill'] is None:
        context['bill'] = type('MockEmptyBill', (), {
            'bill_id': bill_id,
            'created_at': None,
            'staff_name_display': '-',
            'table_number': '-',
            'customer_count': 0,
            'payment_method': 'unknown',
            'total_amount': 0,
            'discount': 0,
            'final_amount': 0,
            'get_payment_method_display': lambda: 'Không xác định',
            'details': [],
        })()
    
    return render(request, 'reports/bill_detail.html', context)
