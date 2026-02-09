# orders/forms.py - GIỮ NGUYÊN
from django import forms
from .models import Order, OrderItem
from menu.models import MenuItem

class OrderItemForm(forms.ModelForm):
    class Meta:
        model = OrderItem
        fields = ['menu_item', 'quantity']
        widgets = {
            'quantity': forms.NumberInput(attrs={'min': 1, 'value': 1}),
        }

class OrderForm(forms.ModelForm):
    class Meta:
        model = Order
        fields = ['order_type', 'delivery_address', 'table_number', 'phone_number', 
                  'customer_count', 'payment_method', 'discount']
        widgets = {
            'delivery_address': forms.TextInput(attrs={'placeholder': 'Nhập địa chỉ giao hàng'}),
            'table_number': forms.TextInput(attrs={'placeholder': 'Nhập số bàn'}),
            'phone_number': forms.TextInput(attrs={'placeholder': 'Nhập số điện thoại'}),
            'customer_count': forms.NumberInput(attrs={'min': 1}),
            'discount': forms.NumberInput(attrs={'min': 0}),
        }