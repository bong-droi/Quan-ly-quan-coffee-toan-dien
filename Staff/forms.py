from django import forms
from .models import BaseSalary, Salary
from django.contrib.auth import get_user_model
from datetime import datetime

User = get_user_model()

class SalaryForm(forms.ModelForm):
    class Meta:
        model = Salary
        fields = [
            'employee', 'month', 'year', 
            'base_salary', 'bonus', 'deduction',
            'notes', 'status'
        ]
        widgets = {
            'month': forms.NumberInput(attrs={
                'class': 'form-control',
                'min': 1,
                'max': 12
            }),
            'year': forms.NumberInput(attrs={
                'class': 'form-control',
                'min': 2020,
                'max': 2100
            }),
            'base_salary': forms.NumberInput(attrs={
                'class': 'form-control',
                'step': '1000'
            }),
            'bonus': forms.NumberInput(attrs={
                'class': 'form-control',
                'step': '1000'
            }),
            'deduction': forms.NumberInput(attrs={
                'class': 'form-control',
                'step': '1000'
            }),
            'notes': forms.Textarea(attrs={
                'class': 'form-control',
                'rows': 3
            }),
            'status': forms.Select(attrs={
                'class': 'form-control'
            }),
        }
    
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # Chỉ hiển thị nhân viên staff
        self.fields['employee'].queryset = User.objects.filter(
            role='staff',
            is_active=True
        ).order_by('first_name')
        self.fields['employee'].widget.attrs.update({'class': 'form-control'})
        
        # Mặc định năm hiện tại
        if not self.instance.pk:
            self.fields['year'].initial = datetime.now().year
            self.fields['month'].initial = datetime.now().month


class BaseSalaryForm(forms.ModelForm):
    class Meta:
        model = BaseSalary
        fields = ['staff_type', 'hourly_rate', 'shift_rate']
        widgets = {
            'staff_type': forms.Select(attrs={'class': 'form-control'}),
            'hourly_rate': forms.NumberInput(attrs={
                'class': 'form-control',
                'step': '1000'
            }),
            'shift_rate': forms.NumberInput(attrs={
                'class': 'form-control',
                'step': '1000'
            }),
        }


class SalaryFilterForm(forms.Form):
    month = forms.ChoiceField(
        choices=[('', 'Tất cả tháng')] + [(str(i), f'Tháng {i}') for i in range(1, 13)],
        required=False,
        label="Tháng",
        widget=forms.Select(attrs={'class': 'form-control'})
    )
    
    year = forms.IntegerField(
        min_value=2020,
        max_value=2100,
        required=True,
        initial=datetime.now().year,
        label="Năm",
        widget=forms.NumberInput(attrs={
            'class': 'form-control',
            'min': 2020,
            'max': 2100
        })
    )
    
    employee = forms.ModelChoiceField(
        queryset=User.objects.filter(role='staff', is_active=True),
        required=False,
        label="Nhân viên",
        widget=forms.Select(attrs={'class': 'form-control'})
    )

class CalculateSalaryForm(forms.Form):
    """Form tính toán lương tự động"""
    month = forms.ChoiceField(
        choices=[('', '-- Chọn tháng --')] + [(str(i), f'Tháng {i}') for i in range(1, 13)],
        widget=forms.Select(attrs={
            'class': 'form-select',
            'id': 'monthSelect'
        }),
        label="Tháng",
        required=True
    )
    
    year = forms.ChoiceField(
        choices=[('', '-- Chọn năm --')] + [(str(i), f'Năm {i}') for i in range(2020, 2030)],
        widget=forms.Select(attrs={
            'class': 'form-select',
            'id': 'yearSelect'
        }),
        label="Năm",
        required=True
    )
    
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        # Set current month and year as default
        now = datetime.now()
        self.fields['month'].initial = str(now.month)
        self.fields['year'].initial = str(now.year)
    
    def clean(self):
        cleaned_data = super().clean()
        month = cleaned_data.get('month')
        year = cleaned_data.get('year')
        
        if month and year:
            # Check if salary already calculated for this month
            from .models import Salary
            if Salary.objects.filter(month=int(month), year=int(year)).exists():
                raise forms.ValidationError(
                    f"Lương tháng {month}/{year} đã được tính trước đó! "
                    f"Hãy kiểm tra trong danh sách lương."
                )
        
        return cleaned_data