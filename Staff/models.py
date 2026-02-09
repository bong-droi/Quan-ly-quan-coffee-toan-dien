from django.db import models
from django.conf import settings
from django.contrib.auth import get_user_model
from Shifts.models import Shift  # Import model Shift từ app Shifts

User = get_user_model()

class BaseSalary(models.Model):
    """Cấu hình lương cơ bản"""
    STAFF_TYPE_CHOICES = [
        ('fulltime', 'Toàn thời gian'),
        ('parttime', 'Bán thời gian'),
        ('trainee', 'Thực tập sinh'),
    ]
    
    staff_type = models.CharField(
        max_length=20,
        choices=STAFF_TYPE_CHOICES,
        unique=True,
        verbose_name="Loại nhân viên"
    )
    
    hourly_rate = models.DecimalField(
        max_digits=10,
        decimal_places=2,
        default=0,
        verbose_name="Lương theo giờ (₫)"
    )
    
    shift_rate = models.DecimalField(
        max_digits=10,
        decimal_places=2,
        default=0,
        verbose_name="Lương theo ca (₫)"
    )
    
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        verbose_name = "Lương cơ bản"
        verbose_name_plural = "Lương cơ bản"
    
    def __str__(self):
        return f"{self.get_staff_type_display()} - {self.hourly_rate}₫/giờ"


class Salary(models.Model):
    """Lương tháng của nhân viên"""
    STATUS_CHOICES = [
        ('calculated', 'Đã tính'),
        ('pending', 'Chờ thanh toán'),
        ('paid', 'Đã thanh toán'),
        ('cancelled', 'Đã hủy'),
    ]
    
    employee = models.ForeignKey(
        User,
        on_delete=models.CASCADE,
        related_name='salaries',  # Giữ nguyên
        verbose_name="Nhân viên"
    )
    
    month = models.IntegerField(verbose_name="Tháng")
    year = models.IntegerField(verbose_name="Năm")
    
    # Thống kê
    total_shifts = models.IntegerField(default=0, verbose_name="Tổng số ca")
    total_hours = models.DecimalField(
        max_digits=6,
        decimal_places=2,
        default=0,
        verbose_name="Tổng giờ làm"
    )
    
    # Lương
    base_salary = models.DecimalField(
        max_digits=12,
        decimal_places=2,
        default=0,
        verbose_name="Lương cơ bản (₫)"
    )
    
    bonus = models.DecimalField(
        max_digits=12,
        decimal_places=2,
        default=0,
        verbose_name="Thưởng (₫)"
    )
    
    deduction = models.DecimalField(
        max_digits=12,
        decimal_places=2,
        default=0,
        verbose_name="Khấu trừ (₫)"
    )
    
    total_salary = models.DecimalField(
        max_digits=12,
        decimal_places=2,
        default=0,
        verbose_name="Tổng lương (₫)"
    )
    
    status = models.CharField(
        max_length=20,
        choices=STATUS_CHOICES,
        default='calculated',
        verbose_name="Trạng thái"
    )
    
    paid_date = models.DateField(
        null=True,
        blank=True,
        verbose_name="Ngày thanh toán"
    )
    
    notes = models.TextField(
        blank=True,
        verbose_name="Ghi chú"
    )
    
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    class Meta:
        verbose_name = "Bảng lương"
        verbose_name_plural = "Bảng lương"
        unique_together = ['employee', 'month', 'year']
        ordering = ['-year', '-month', 'employee']
    
    def __str__(self):
        return f"{self.employee.username} - {self.month}/{self.year} - {self.total_salary}₫"
    
    def save(self, *args, **kwargs):
        # Tự động tính tổng lương
        self.total_salary = self.base_salary + self.bonus - self.deduction
        super().save(*args, **kwargs)
    
    @property
    def month_year(self):
        return f"{self.month:02d}/{self.year}"
    
    def mark_as_paid(self):
        """Đánh dấu đã thanh toán"""
        from django.utils import timezone
        self.status = 'paid'
        self.paid_date = timezone.now().date()
        self.save()
        
        