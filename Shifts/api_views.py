# api_views.py
from django.http import JsonResponse
from django.shortcuts import get_object_or_404
from django.contrib.auth.decorators import login_required
from .models import Shift, ShiftRegistration
from datetime import datetime

@login_required
def get_shift_registrations(request, shift_id):
    """API lấy danh sách nhân viên đã đăng ký ca"""
    if request.user.role != 'owner':
        return JsonResponse({'error': 'Unauthorized'}, status=403)
    
    shift = get_object_or_404(Shift, id=shift_id)
    date_str = request.GET.get('date')
    
    try:
        if date_str:
            date = datetime.strptime(date_str, '%Y-%m-%d').date()
        else:
            date = shift.date
    except:
        date = shift.date
    
    registrations = ShiftRegistration.objects.filter(
        shift=shift,
        date=date
    ).select_related('user')
    
    employees = []
    for reg in registrations:
        # Xử lý khi không có registered_at
        reg_time = "—"
        if hasattr(reg, 'registered_at') and reg.registered_at:
            reg_time = reg.registered_at.strftime('%H:%M %d/%m/%Y')
        
        employees.append({
            'id': reg.user.id,
            'username': reg.user.username,
            'full_name': reg.user.get_full_name() or reg.user.username,
            'email': reg.user.email,
            'registered_at': reg_time,
            'status': reg.status
        })
    
    return JsonResponse({
        'shift_id': shift.id,
        'shift_name': shift.name,
        'date': date.strftime('%d/%m/%Y'),
        'employees': employees,
        'count': len(employees)
    })