# orders/views.py - ĐÃ SỬA (BẢN HOÀN CHỈNH)
from django.shortcuts import render, redirect, get_object_or_404
from django.contrib.auth.decorators import login_required
from django.http import JsonResponse
from django.views.decorators.http import require_http_methods
from django.utils import timezone
from django.db.models import Sum

from orders.forms import OrderItemForm
from .models import Order, OrderItem
from menu.models import MenuItem
import json

@login_required
def order_list(request):
    """Danh sách tất cả đơn hàng"""
    try:
        orders = Order.objects.select_related('staff', 'customer').prefetch_related('items__menu_item').order_by('-created_at')
        db_error = None
    except Exception as e:
        orders = []
        db_error = str(e)
    
    context = {
        'orders': orders,
        'db_error': db_error,
    }
    return render(request, 'orders/order_list.html', context)


@login_required
def order_create(request):
    """Tạo đơn hàng mới"""
    if request.method == 'POST':
        try:
            # Lấy dữ liệu từ form
            order_type = request.POST.get('order_type', 'offline')
            customer_count = int(request.POST.get('customer_count', 1))
            payment_method = request.POST.get('payment_method', 'cash')
            discount = float(request.POST.get('discount', 0))
            
            # Tạo đơn hàng
            order = Order.objects.create(
                staff=request.user,
                order_type=order_type,
                customer_count=customer_count,
                payment_method=payment_method,
                discount=discount,
                delivery_address=request.POST.get('delivery_address', ''),
                phone_number=request.POST.get('phone_number', ''),
                table_number=request.POST.get('table_number', ''),
                status='processing'
            )
            
            # Đưa người dùng đến trang thêm món ngay
            return redirect('order_detail', pk=order.id)
            
        except Exception as e:
            return render(request, 'orders/order_create.html', {
                'error': str(e),
                'form_data': request.POST
            })
    
    return render(request, 'orders/order_create.html')

@login_required
def order_detail(request, pk):
    """Chi tiết đơn hàng và thêm món"""
    order = get_object_or_404(Order.objects.prefetch_related('items__menu_item'), id=pk)
    
    # Tạo form thêm món
    form = OrderItemForm()
    
    if request.method == 'POST':
        try:
            menu_item_id = request.POST.get('menu_item')
            if not menu_item_id:
                raise ValueError('Vui lòng chọn món')
            
            # Lấy quantity, mặc định là 1 nếu không có hoặc không hợp lệ
            try:
                quantity = int(request.POST.get('quantity', 1))
                if quantity < 1:
                    quantity = 1
            except (ValueError, TypeError):
                quantity = 1
            
            menu_item = MenuItem.objects.get(id=menu_item_id)
            
            # Kiểm tra xem món đã có trong đơn chưa
            order_item, created = OrderItem.objects.get_or_create(
                order=order,
                menu_item=menu_item,
                defaults={'quantity': quantity}
            )
            
            if not created:
                # Nếu đã có, cộng thêm số lượng
                order_item.quantity += quantity
                order_item.save()
            
            # Lưu order để trigger auto-update total_amount
            order.save()
            
            return redirect('order_detail', pk=order.id)
            
        except MenuItem.DoesNotExist:
            error = 'Món không tồn tại'
        except ValueError as e:
            error = str(e)
        except Exception as e:
            error = f'Lỗi khi thêm món: {str(e)}'
        
        # Hiển thị lỗi
        context = {
            'order': order,
            'items': order.items.all(),
            'menu_items': MenuItem.objects.filter(is_available=True),
            'form': form,
            'error': error
        }
        return render(request, 'orders/order_detail.html', context)
    
    # Lấy danh sách menu để thêm món
    menu_items = MenuItem.objects.filter(is_available=True)
    items = order.items.all()
    
    context = {
        'order': order,
        'items': items,
        'menu_items': menu_items,
        'form': form,
    }
    return render(request, 'orders/order_detail.html', context)

@login_required
def order_delete(request, pk):
    """Xóa đơn hàng"""
    order = get_object_or_404(Order, id=pk)
    
    if request.method == 'POST':
        order.delete()
        return redirect('order_list')
    
    return redirect('order_detail', pk=pk)


@login_required
def order_toggle_status(request, pk):
    """Chuyển đổi trạng thái đơn hàng"""
    order = get_object_or_404(Order, id=pk)
    
    if request.method == 'POST':
        if order.status == 'processing':
            order.status = 'processed'
            order.completed_at = timezone.now()
        elif order.status == 'processed':
            order.status = 'canceled'
            order.canceled_at = timezone.now()
        else:
            order.status = 'processing'
            order.completed_at = None
            order.canceled_at = None
        
        order.save()
    
    return redirect('order_detail', pk=pk)


@login_required
def order_invoice(request, pk):
    """In hóa đơn"""
    order = get_object_or_404(Order.objects.prefetch_related('items__menu_item'), id=pk)
    
    context = {
        'order': order,
        'items': order.items.all(),
    }
    return render(request, 'orders/invoice.html', context)


@login_required
def create_order_from_customer(request):
    """API tạo đơn hàng từ phía khách (mobile app)"""
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            
            order = Order.objects.create(
                customer=request.user,
                order_type=data.get('order_type', 'online'),
                delivery_address=data.get('delivery_address', ''),
                phone_number=data.get('phone_number', ''),
                table_number=data.get('table_number', ''),  # ✅ THÊM table_number
                status='processing'
            )
            
            # Thêm các món vào đơn
            items_data = data.get('items', [])
            if not items_data:
                order.delete()
                return JsonResponse({
                    'ok': False, 
                    'error': 'Đơn hàng phải có ít nhất 1 món'
                }, status=400)
            
            for item_data in items_data:
                try:
                    menu_item_id = item_data.get('menu_item_id')
                    # ✅ HỖ TRỢ CẢ 'qty' VÀ 'quantity'
                    quantity = item_data.get('quantity') or item_data.get('qty', 1)
                    
                    if not menu_item_id:
                        continue
                    
                    # Validate quantity
                    try:
                        quantity = int(quantity)
                        if quantity < 1:
                            quantity = 1
                    except (ValueError, TypeError):
                        quantity = 1
                    
                    menu_item = MenuItem.objects.get(id=menu_item_id)
                    OrderItem.objects.create(
                        order=order,
                        menu_item=menu_item,
                        quantity=quantity
                    )
                except MenuItem.DoesNotExist:
                    continue
                except Exception as e:
                    print(f"Error adding item: {e}")
                    continue
            
            # Kiểm tra xem có món nào được thêm không
            if order.items.count() == 0:
                order.delete()
                return JsonResponse({
                    'ok': False,
                    'error': 'Không thể thêm món vào đơn hàng'
                }, status=400)
            
            # Lưu lại để tính total_amount
            order.save()
            
            return JsonResponse({
                'ok': True,
                'order_id': order.id,
                'total_amount': float(order.total_amount)
            })
            
        except json.JSONDecodeError:
            return JsonResponse({
                'ok': False, 
                'error': 'Dữ liệu JSON không hợp lệ'
            }, status=400)
        except Exception as e:
            return JsonResponse({'ok': False, 'error': str(e)}, status=400)
    
    return JsonResponse({'ok': False, 'error': 'Method not allowed'}, status=405)


# ✅ THÊM MỚI: API cho nhân viên đặt hàng tại quán
@login_required
def create_order_from_staff(request):
    """API tạo đơn hàng từ nhân viên (tại quán)"""
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            
            # Kiểm tra table_number bắt buộc cho đơn offline
            table_number = data.get('table_number', '').strip()
            if not table_number:
                return JsonResponse({
                    'ok': False, 
                    'error': 'Vui lòng chọn số bàn'
                }, status=400)
            
            order = Order.objects.create(
                staff=request.user,  # ✅ LƯU STAFF thay vì customer
                order_type='offline',  # ✅ Luôn là offline cho đơn tại quán
                table_number=table_number,  # ✅ LƯU SỐ BÀN
                status='processing'
            )
            
            # Thêm các món vào đơn
            items_data = data.get('items', [])
            if not items_data:
                order.delete()
                return JsonResponse({
                    'ok': False, 
                    'error': 'Đơn hàng phải có ít nhất 1 món'
                }, status=400)
            
            for item_data in items_data:
                try:
                    menu_item_id = item_data.get('menu_item_id')
                    quantity = item_data.get('quantity') or item_data.get('qty', 1)
                    
                    if not menu_item_id:
                        continue
                    
                    # Validate quantity
                    try:
                        quantity = int(quantity)
                        if quantity < 1:
                            quantity = 1
                    except (ValueError, TypeError):
                        quantity = 1
                    
                    menu_item = MenuItem.objects.get(id=menu_item_id)
                    OrderItem.objects.create(
                        order=order,
                        menu_item=menu_item,
                        quantity=quantity
                    )
                except MenuItem.DoesNotExist:
                    continue
                except Exception as e:
                    print(f"Error adding item: {e}")
                    continue
            
            # Kiểm tra xem có món nào được thêm không
            if order.items.count() == 0:
                order.delete()
                return JsonResponse({
                    'ok': False,
                    'error': 'Không thể thêm món vào đơn hàng'
                }, status=400)
            
            # Lưu lại để tính total_amount
            order.save()
            
            return JsonResponse({
                'ok': True,
                'order_id': order.id,
                'total_amount': float(order.total_amount),
                'table_number': table_number
            })
            
        except json.JSONDecodeError:
            return JsonResponse({
                'ok': False, 
                'error': 'Dữ liệu JSON không hợp lệ'
            }, status=400)
        except Exception as e:
            return JsonResponse({'ok': False, 'error': str(e)}, status=400)
    
    return JsonResponse({'ok': False, 'error': 'Method not allowed'}, status=405)


# ==================== API VIEWS ====================

def order_list_api(request):
    """API: Danh sách đơn hàng"""
    try:
        orders = Order.objects.select_related('staff', 'customer').prefetch_related('items__menu_item').order_by('-created_at')[:50]
        
        data = []
        for order in orders:
            data.append({
                'id': order.id,
                'staff': order.staff.username if order.staff else None,
                'customer': order.customer.username if order.customer else None,
                'order_type': order.order_type,
                'status': order.status,
                'total': float(order.total_amount),
                'created_at': order.created_at.strftime('%d/%m/%Y %H:%M'),
                'table_number': order.table_number,
                'delivery_address': order.delivery_address,
                'phone_number': order.phone_number,
            })
        
        return JsonResponse({'ok': True, 'orders': data})
        
    except Exception as e:
        return JsonResponse({'ok': False, 'error': str(e)}, status=400)


def order_detail_api(request, pk):
    """API: Chi tiết đơn hàng"""
    try:
        order = Order.objects.prefetch_related('items__menu_item').get(id=pk)
        
        items = []
        for item in order.items.all():
            items.append({
                'menu_item_id': item.menu_item.id,
                'name': item.menu_item.name,
                'price': float(item.menu_item.price),
                'qty': item.quantity,
                'quantity': item.quantity,
                'line': float(item.menu_item.price * item.quantity),
            })
        
        data = {
            'id': order.id,
            'staff': order.staff.username if order.staff else None,
            'customer': order.customer.username if order.customer else None,
            'order_type': order.order_type,
            'status': order.status,
            'total': float(order.total_amount),
            'total_amount': float(order.total_amount),
            'created_at': order.created_at.strftime('%d/%m/%Y %H:%M'),
            'completed_at': order.completed_at.strftime('%d/%m/%Y %H:%M') if order.completed_at else None,
            'canceled_at': order.canceled_at.strftime('%d/%m/%Y %H:%M') if order.canceled_at else None,
            'cancel_reason': order.cancel_reason,
            'table_number': order.table_number,
            'delivery_address': order.delivery_address,
            'phone_number': order.phone_number,
            'items': items,
        }
        
        return JsonResponse({'ok': True, 'order': data})
        
    except Order.DoesNotExist:
        return JsonResponse({'ok': False, 'error': 'Order not found'}, status=404)
    except Exception as e:
        return JsonResponse({'ok': False, 'error': str(e)}, status=400)


@require_http_methods(["POST"])
def order_complete_api(request, pk):
    """API: Hoàn thành đơn hàng"""
    try:
        order = Order.objects.get(id=pk)
        
        if order.status != 'processing':
            return JsonResponse({
                'ok': False, 
                'error': 'Chỉ có thể hoàn thành đơn đang xử lý'
            }, status=400)
        
        order.status = 'processed'
        order.completed_at = timezone.now()
        order.save()
        
        return JsonResponse({
            'ok': True,
            'message': 'Đơn hàng đã được hoàn thành',
            'order_id': order.id
        })
        
    except Order.DoesNotExist:
        return JsonResponse({'ok': False, 'error': 'Order not found'}, status=404)
    except Exception as e:
        return JsonResponse({'ok': False, 'error': str(e)}, status=400)


@require_http_methods(["POST"])
def order_cancel_api(request, pk):
    """API: Hủy đơn hàng"""
    try:
        order = Order.objects.get(id=pk)
        
        # ✅ HỖ TRỢ CẢ JSON VÀ FORM DATA
        if request.content_type == 'application/json':
            data = json.loads(request.body) if request.body else {}
        else:
            data = request.POST
        
        cancel_reason = data.get('reason', '')
        
        order.status = 'canceled'
        order.canceled_at = timezone.now()
        order.cancel_reason = cancel_reason
        order.save()
        
        return JsonResponse({
            'ok': True,
            'message': 'Đơn hàng đã được hủy',
            'order_id': order.id
        })
        
    except Order.DoesNotExist:
        return JsonResponse({'ok': False, 'error': 'Order not found'}, status=404)
    except Exception as e:
        return JsonResponse({'ok': False, 'error': str(e)}, status=400)


def my_orders_api(request):
    """API: Đơn hàng của tôi (cho khách hàng hoặc nhân viên)"""
    if not request.user.is_authenticated:
        return JsonResponse({'ok': False, 'error': 'Not authenticated'}, status=401)
    
    try:
        # ✅ LẤY CẢ ĐƠN CUSTOMER VÀ STAFF
        orders = Order.objects.filter(
            models.Q(customer=request.user) | models.Q(staff=request.user)
        ).prefetch_related('items__menu_item').order_by('-created_at')[:20]
        
        data = []
        for order in orders:
            items = []
            for item in order.items.all():
                items.append({
                    'name': item.menu_item.name,
                    'qty': item.quantity,
                    'quantity': item.quantity,
                    'price': float(item.menu_item.price),
                    'line': float(item.menu_item.price * item.quantity),
                })
            
            data.append({
                'id': order.id,
                'order_type': order.order_type,
                'status': order.status,
                'total_amount': float(order.total_amount),
                'created_at': order.created_at.strftime('%d/%m/%Y %H:%M'),
                'delivery_address': order.delivery_address,
                'phone_number': order.phone_number,
                'table_number': order.table_number,  # ✅ THÊM table_number
                'cancel_reason': order.cancel_reason,
                'items': items,
            })
        
        return JsonResponse({'ok': True, 'orders': data})
        
    except Exception as e:
        return JsonResponse({'ok': False, 'error': str(e)}, status=400)


# Import thêm models để dùng Q
from django.db import models