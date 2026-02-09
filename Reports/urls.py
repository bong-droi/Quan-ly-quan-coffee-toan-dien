# Reports/urls.py
from django.urls import path
from . import views

app_name = 'reports'

urlpatterns = [
    path('', views.dashboard, name='dashboard'),
    path('revenue/', views.revenue_report, name='revenue_report'),
    path('bill/<str:bill_id>/', views.bill_detail, name='bill_detail'),
]