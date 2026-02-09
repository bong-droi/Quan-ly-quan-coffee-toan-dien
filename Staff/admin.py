from django.contrib import admin
from .models import BaseSalary, Salary  # Chỉ import model mới

@admin.register(BaseSalary)
class BaseSalaryAdmin(admin.ModelAdmin):
    list_display = ['staff_type', 'hourly_rate', 'shift_rate', 'created_at']
    list_filter = ['staff_type']
    search_fields = ['staff_type']
    readonly_fields = ['created_at', 'updated_at']
    
    fieldsets = (
        ('Thông tin cấu hình', {
            'fields': ('staff_type', 'hourly_rate', 'shift_rate')
        }),
        ('Thông tin hệ thống', {
            'fields': ('created_at', 'updated_at'),
            'classes': ('collapse',)
        }),
    )


@admin.register(Salary)
class SalaryAdmin(admin.ModelAdmin):
    list_display = ['employee', 'month_year', 'total_hours', 'total_shifts', 'total_salary', 'status', 'paid_date']
    list_filter = ['status', 'month', 'year', 'paid_date']
    search_fields = ['employee__username', 'employee__first_name', 'employee__last_name', 'notes']
    readonly_fields = ['total_salary', 'created_at', 'updated_at']
    list_per_page = 20
    
    fieldsets = (
        ('Thông tin nhân viên', {
            'fields': ('employee', 'month', 'year')
        }),
        ('Thống kê công việc', {
            'fields': ('total_shifts', 'total_hours')
        }),
        ('Thông tin lương', {
            'fields': ('base_salary', 'bonus', 'deduction', 'total_salary')
        }),
        ('Trạng thái', {
            'fields': ('status', 'paid_date', 'notes')
        }),
        ('Thông tin hệ thống', {
            'fields': ('created_at', 'updated_at'),
            'classes': ('collapse',)
        }),
    )
    
    def month_year(self, obj):
        return f"{obj.month:02d}/{obj.year}"
    month_year.short_description = "Tháng/Năm"
    month_year.admin_order_field = ['year', 'month']
    
    def get_queryset(self, request):
        qs = super().get_queryset(request)
        return qs.select_related('employee')
    
    def mark_as_paid(self, request, queryset):
        """Đánh dấu đã thanh toán"""
        count = queryset.count()
        for salary in queryset:
            salary.mark_as_paid()
        self.message_user(request, f"✅ Đã đánh dấu đã thanh toán cho {count} bảng lương")
    mark_as_paid.short_description = "Đánh dấu đã thanh toán"
    
    def calculate_salary_action(self, request, queryset):
        """Tính toán lại lương"""
        count = queryset.count()
        for salary in queryset:
            # Tính lại tổng lương
            salary.total_salary = salary.base_salary + salary.bonus - salary.deduction
            salary.save()
        self.message_user(request, f"✅ Đã tính toán lại lương cho {count} bảng lương")
    calculate_salary_action.short_description = "Tính toán lại lương"
    
    actions = ['mark_as_paid', 'calculate_salary_action']