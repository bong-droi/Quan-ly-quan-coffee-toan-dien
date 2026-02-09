from django.db import models
from django.contrib.auth import get_user_model
from django.core.exceptions import ValidationError
from django.utils import timezone

User = get_user_model()

from datetime import date, datetime, timedelta

class Shift(models.Model):
    name = models.CharField(max_length=100, verbose_name="Tên ca")
    start_time = models.TimeField(verbose_name="Giờ bắt đầu")
    end_time = models.TimeField(verbose_name="Giờ kết thúc")
    date = models.DateField(verbose_name="Ngày làm việc")
    capacity = models.IntegerField(default=1, verbose_name="Sức chứa")
    
    class Meta:
        ordering = ['date', 'start_time']
    
    def __str__(self):
        return f"{self.name} ({self.date})"
    
    def get_duration_hours(self):
        from datetime import datetime
        start = datetime.combine(self.date, self.start_time)
        end = datetime.combine(self.date, self.end_time)
        duration = end - start
        return round(duration.total_seconds() / 3600, 1)
    
    def get_registered_count(self, date=None):
        if not date:
            date = self.date
        return self.registrations.filter(date=date).count()
    
    def get_assigned_count(self, date=None):
        if not date:
            date = self.date
        return self.assigned_shifts.filter(date=date).count()

    @property
    def formatted_date(self):
        return self.date.strftime('%d/%m/%Y') if self.date else None

    @property
    def full_time_info(self):
        return f"{self.name} - {self.formatted_date} ({self.start_time.strftime('%H:%M')}-{self.end_time.strftime('%H:%M')})"

class ShiftRegistration(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE)
    shift = models.ForeignKey(Shift, on_delete=models.CASCADE, related_name='registrations')
    date = models.DateField()
    status = models.CharField(max_length=20, choices=[
        ('registered', 'Đã đăng ký'),  # Thay pending bằng registered
        ('assigned', 'Đã phân công'),
        ('cancelled', 'Đã hủy'),
    ], default='registered')  # Mặc định là đã đăng ký
    
    registered_at = models.DateTimeField(auto_now_add=True, verbose_name="Thời gian đăng ký")
    
    class Meta:
        unique_together = ['user', 'shift', 'date']
        ordering = ['-registered_at']
    
    @property
    def is_assigned(self):
        """Property chỉ đọc - trả về True nếu status là 'assigned'"""
        return self.status == 'assigned'
    
    def __str__(self):
        return f"{self.user.username} - {self.shift.name} - {self.date}"
    
class AssignedShift(models.Model):
    """Ca đã được phân công (Owner phân công)"""
    user = models.ForeignKey(
        User,
        on_delete=models.CASCADE,
        related_name='assigned_shifts',
        verbose_name="Nhân viên"
    )
    shift = models.ForeignKey(
        Shift,
        on_delete=models.CASCADE,
        related_name='assigned_shifts',
        verbose_name="Ca"
    )
    date = models.DateField(
        verbose_name="Ngày làm việc",
        default=date.today
    )
    assigned_at = models.DateTimeField(
        auto_now_add=True,
        verbose_name="Thời gian phân công"
    )
    
    class Meta:
        db_table = 'shifts_assignedshift'
        verbose_name = "Ca đã phân công"
        verbose_name_plural = "Ca đã phân công"
        unique_together = ['user', 'shift', 'date']
        ordering = ['date', 'shift__start_time']
    
    def __str__(self):
        return f"{self.user.username} - {self.shift.name} - {self.date}"
 