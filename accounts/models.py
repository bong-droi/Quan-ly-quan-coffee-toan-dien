# accounts/models.py
from django.contrib.auth.models import AbstractUser
from django.db import models
from django.core.validators import MinValueValidator

class User(AbstractUser):
    ROLE_CHOICES = [
        ('owner', 'Chủ quán'),
        ('staff', 'Nhân viên'),
        ('customer', 'Khách hàng'),
    ]
    
    role = models.CharField(
        max_length=10,
        choices=ROLE_CHOICES,
        default='customer',
        verbose_name='Vai trò'
    )
    
    # Thêm các field lương nếu cần
    base_salary = models.DecimalField(
        max_digits=10,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)],
        verbose_name='Lương cơ bản'
    )
    
    hourly_rate = models.DecimalField(
        max_digits=10,
        decimal_places=2,
        default=0,
        validators=[MinValueValidator(0)],
        verbose_name='Lương theo giờ'
    )
    
    # Các field khác...
    
    def get_role_display(self):
        return dict(self.ROLE_CHOICES).get(self.role, self.role)
    
    def __str__(self):
        return f"{self.username} ({self.get_role_display()})"