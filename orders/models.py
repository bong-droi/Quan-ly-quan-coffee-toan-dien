# orders/models.py - CẬP NHẬT ĐẦY ĐỦ
from django.db import models
from django.conf import settings
from menu.models import MenuItem

class Order(models.Model):
    staff = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.SET_NULL, null=True)
    customer = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.SET_NULL, null=True, blank=True, related_name='customer_orders')
    created_at = models.DateTimeField(auto_now_add=True)
    
    ORDER_TYPE_CHOICES = (
        ("online", "Online"),
        ("offline", "Offline"),
    )
    order_type = models.CharField(max_length=10, choices=ORDER_TYPE_CHOICES, default="offline")
    delivery_address = models.CharField(max_length=255, blank=True, default="")
    table_number = models.CharField(max_length=20, blank=True, default="")
    phone_number = models.CharField(max_length=20, blank=True, default="")

    STATUS_CHOICES = (
        ("processing", "Đang xử lý"),
        ("processed", "Đã xử lý"),
        ("canceled", "Đã hủy"),
    )
    status = models.CharField(max_length=12, choices=STATUS_CHOICES, default="processing")
    completed_at = models.DateTimeField(null=True, blank=True)
    canceled_at = models.DateTimeField(null=True, blank=True)
    cancel_reason = models.TextField(blank=True, default="")
    
    # THÊM CÁC TRƯỜNG BỊ THIẾU
    PAYMENT_METHOD_CHOICES = (
        ("cash", "Tiền mặt"),
        ("card", "Thẻ"),
        ("transfer", "Chuyển khoản"),
        ("momo", "Momo"),
        ("zalopay", "ZaloPay"),
    )
    payment_method = models.CharField(
        max_length=10, 
        choices=PAYMENT_METHOD_CHOICES, 
        default="cash"
    )
    
    customer_count = models.PositiveIntegerField(default=1)
    discount = models.DecimalField(
        max_digits=10, 
        decimal_places=2, 
        default=0,
        help_text="Số tiền giảm giá (VNĐ)"
    )
    
    # ← THÊM MỚI: Lưu tổng tiền để query nhanh hơn
    total_amount = models.DecimalField(
        max_digits=12, 
        decimal_places=2, 
        default=0,
        help_text="Tổng tiền của đơn hàng (tự động tính)"
    )

    class Meta:
        ordering = ['-created_at']
        indexes = [
            models.Index(fields=['-created_at', 'status']),
            models.Index(fields=['status', 'total_amount']),
        ]

    def __str__(self):
        return f"Order #{self.id} - {self.staff.username if self.staff else 'N/A'}"

    def calculate_total(self):
        """Tính tổng tiền từ các OrderItem"""
        return sum(item.menu_item.price * item.quantity for item in self.items.all())

    def total_price(self):
        """Backward compatibility - trả về total_amount nếu có, không thì tính lại"""
        if self.total_amount and self.total_amount > 0:
            return self.total_amount
        return self.calculate_total()
    
    def final_amount(self):
        """Tính tổng tiền sau khi trừ giảm giá"""
        total = self.total_amount or self.calculate_total()
        return max(total - self.discount, 0)  # Đảm bảo không âm

    def save(self, *args, **kwargs):
        """Auto-update total_amount trước khi save"""
        # Nếu order đã có ID (đã tồn tại), tính lại total
        if self.pk:
            self.total_amount = self.calculate_total()
        super().save(*args, **kwargs)


class OrderItem(models.Model):
    order = models.ForeignKey(Order, related_name="items", on_delete=models.CASCADE)
    menu_item = models.ForeignKey(MenuItem, on_delete=models.CASCADE)
    quantity = models.PositiveIntegerField(default=1)

    def __str__(self):
        return f"{self.menu_item.name} x {self.quantity}"
    
    def save(self, *args, **kwargs):
        """Sau khi save OrderItem, update lại total_amount của Order"""
        super().save(*args, **kwargs)
        # Update total_amount của Order
        self.order.total_amount = self.order.calculate_total()
        self.order.save(update_fields=['total_amount'])
    
    def delete(self, *args, **kwargs):
        """Sau khi xóa OrderItem, update lại total_amount của Order"""
        order = self.order
        super().delete(*args, **kwargs)
        # Update total_amount của Order
        order.total_amount = order.calculate_total()
        order.save(update_fields=['total_amount'])