from django.contrib import admin
from django.urls import path
from tictactoe import urls
from django.conf.urls import include, url

urlpatterns = [
    path('api/', include(urls)),
    path('admin/', admin.site.urls),
]
