from django.urls import path, include
from django.contrib.auth import views as auth_views
from . import views
from Reports.views import dashboard, revenue_report
from django.contrib import admin
from Shifts.views import shift_list
urlpatterns = [
    # ============ AUTH & USER ============
    path("login/", views.login_view, name="login"),
    path("logout/", views.logout_view, name="logout"),
    path("register/", views.register_view, name="register"),
    
    # ============ CUSTOMER ============
    path("customer/", views.customer_frameset, name="customer_frameset"),  # <-- Đổi từ customer_home
    path("customer/header/", views.customer_header, name="customer_header"),  # <-- Thêm mới
    path("customer/menu/", views.customer_menu, name="customer_menu"),  # <-- Thêm mới
    path("customer/order/", views.customer_order_ui, name="customer_order_ui"),
                
    # ============ STAFF ============
    path('staff/frameset/', views.staff_frameset, name='staff_frameset'),
    path('staff/header/', views.staff_header, name='staff_header'),
    # path("dashboard/staff/", views.staff_dashboard, name="staff_dashboard"),
    path("staff/order-ui/", views.staff_order_ui, name="staff_order_ui"),
    path("staff/order-tai-quay/", views.staff_order_tai_quay, name="staff_order_tai_quay"),

    # ============ OWNER ============
    path("owner/", views.owner_frameset, name="owner_frameset"),
    path("owner/header/", views.owner_header, name="owner_header"),
    path("owner/home/", views.owner_home, name="owner_home"),

    # ============ USER MANAGEMENT ============
    path("users/", views.user_list, name="user_list"),
    path("users/create/", views.user_create, name="user_create"),
    path("users/<int:pk>/", views.user_detail, name="user_detail"),
    path("users/<int:pk>/edit/", views.user_edit, name="user_edit"),
    path("users/<int:pk>/role/", views.user_role, name="user_role"),
    path("users/<int:pk>/password/", views.user_password, name="user_password"),
    path("users/<int:pk>/delete/", views.user_delete, name="user_delete"),
    path("users/<int:pk>/salary/", views.user_edit_salary, name="user_edit_salary"),
    path("staff/<int:staff_id>/edit/", views.edit_staff, name="edit_staff"),

    # ============ PASSWORD CHANGE ============
    path('password_change/', auth_views.PasswordChangeView.as_view(
        template_name='accounts/password_change.html'), name='password_change'),
    path('password_change/done/', auth_views.PasswordChangeDoneView.as_view(
        template_name='accounts/password_change_done.html'), name='password_change_done'),
    
    # ============ REPORTS ============
    path('reports/', dashboard, name='report_dashboard'),
    path('reports/revenue/', revenue_report, name='revenue_report'),
    # path('shifts/', views.shift_list, name='shift_list'),

    # path('shifts/', include('Shifts.urls')),
    path('shifts/', include('Shifts.urls')),
    
]