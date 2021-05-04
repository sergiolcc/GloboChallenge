from django.contrib import admin
from django.urls import path
from . import views
app_name = 'moviefilter'

urlpatterns = [
    path('', views.home),
    path('getdata', views.getdata),
    path('contact', views.contact),
    path('about', views.about)
    
]
