from django.urls import path
from . import views
from . import api_views
app_name = 'shifts'

urlpatterns = [
    # Quản lý ca (Owner)
    path('', views.shift_list, name='shift_list'),
    path('create/', views.shift_create, name='shift_create'),
    path('<int:pk>/', views.shift_detail, name='shift_detail'),
    path('<int:pk>/update/', views.shift_update, name='shift_update'),
    path('<int:pk>/delete/', views.shift_delete, name='shift_delete'),
    
    # Phân công ca (Owner)
    path('assign/', views.assign_shift, name='assign_shift'),
    path('assigned/<int:pk>/delete/', views.unassign_shift, name='unassign_shift'),
    
    # Đăng ký ca (Staff)
    path('register/<int:pk>/cancel/', views.shift_unregister, name='shift_unregister'),
    path('register/', views.shift_register, name='shift_register'),
    path('my-registrations/', views.my_registrations, name='my_registrations'),
    # Ca của tôi (Staff)
    path('my-shifts/', views.my_shifts, name='my_shifts'),

    # ... các URL hiện có ...
    path('api/shifts/<int:shift_id>/registrations/', api_views.get_shift_registrations, name='api_shift_registrations'),
    
    path('unregister/<int:reg_id>/', views.shift_unregister, name='shift_unregister'),

    path('my-dashboard/', views.shifts_dashboard, name='shifts_dashboard'),
    

]
