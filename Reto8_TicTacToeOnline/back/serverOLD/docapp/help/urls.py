from django.conf.urls import patterns, url
from docapp.help.views import *

from django.conf.urls import patterns, url, include
from docapp.help.views import *
from rest_framework import routers

#routers for the rest framework
router = routers.DefaultRouter()

router.register(r'^tickets', TicketViewSet)

urlpatterns = [

    url(r'^', include(router.urls)),

]