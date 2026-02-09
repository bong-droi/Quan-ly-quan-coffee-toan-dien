# admin.py
from django.contrib import admin
from .models import Shift, ShiftRegistration, AssignedShift

@admin.register(Shift)
class ShiftAdmin(admin.ModelAdmin):
    list_display = ['name', 'date', 'start_time', 'end_time', 'capacity', 'registered', 'assigned']
    list_filter = ['date']
    search_fields = ['name']
    ordering = ['date', 'start_time']
    
    def registered(self, obj):
        return obj.get_registered_count()
    registered.short_description = 'Đã đăng ký'
    
    def assigned(self, obj):
        return f"{obj.get_assigned_count()}/{obj.capacity}"
    assigned.short_description = 'Đã phân công'

@admin.register(ShiftRegistration)
class ShiftRegistrationAdmin(admin.ModelAdmin):
    list_display = ['user', 'shift', 'date', 'status', 'registered_at']
    list_filter = ['status', 'date', 'shift__date']
    search_fields = ['user__username', 'shift__name']
    ordering = ['-registered_at']
    raw_id_fields = ['user', 'shift']

@admin.register(AssignedShift)
class AssignedShiftAdmin(admin.ModelAdmin):
    list_display = ['user', 'shift', 'date', 'assigned_at', 'status']
    list_filter = ['date', 'assigned_at']
    search_fields = ['user__username', 'shift__name']
    ordering = ['-date', 'shift__start_time']
    raw_id_fields = ['user', 'shift']
    date_hierarchy = 'date'
    
    def status(self, obj):
        from django.utils import timezone
        if obj.date < timezone.now().date():
            return "✓ Đã xong"
        elif obj.date == timezone.now().date():
            return "⚡ Hôm nay"
        else:
            return "⏰ Sắp tới"
    status.short_description = 'Trạng thái'