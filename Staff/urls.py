from django.urls import path
from . import views


urlpatterns = [
    # Quản lý lương cơ bản (Owner)
    path('base-salary/', views.base_salary_list, name='base_salary_list'),
    path('base-salary/<int:pk>/update/', views.base_salary_update, name='base_salary_update'),
    
    # Tính toán lương tự động (Owner)
    path('calculate/', views.calculate_salary, name='calculate_salary'),
    
    # Quản lý bảng lương (Owner)
    path('', views.salary_list, name='salary_list'),
    path('create/', views.salary_create, name='salary_create'),
    path('<int:pk>/', views.salary_detail, name='salary_detail'),
    path('<int:pk>/update/', views.salary_update, name='salary_update'),
    path('<int:pk>/pay/', views.make_payment, name='make_payment'),
    
    # Nhân viên xem lương
    path('my-salary/', views.my_salary, name='my_salary'),
    path('my-salary/<int:pk>/', views.my_salary_detail, name='my_salary_detail'),
]