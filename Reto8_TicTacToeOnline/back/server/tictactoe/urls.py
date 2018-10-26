from django.urls import path
from django.conf.urls import include, url
from tictactoe.views import *
from rest_framework.routers import DefaultRouter

router = DefaultRouter()
router.register(r'games', GameViewSet)

urlpatterns = [
    url(r'^', include(router.urls)),
]
