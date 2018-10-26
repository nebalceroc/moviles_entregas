from django.conf.urls import patterns, url, include
from docapp.chat.views import *
from rest_framework import routers

#routers for the rest framework
router = routers.DefaultRouter()

router.register(r'^messages', ChatMessageViewSet)

urlpatterns = [

    url(r'^', include(router.urls)),

]