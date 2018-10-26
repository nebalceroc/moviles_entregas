from django.conf.urls import include, url
from django.contrib import admin
from django.conf import settings
from django.conf.urls.static import static
from django.contrib.auth import views as auth_views
from docapp.appointments import urls as appoinments_urls
from docapp.appointments import views as appoinments_views
from docapp.account import urls as account_urls
from docapp.account import views as account_views
from docapp.help import urls as help_urls
from docapp.chat import urls as chat_urls
from docapp.account.views import *

from rest_framework.routers import DefaultRouter

router = DefaultRouter()

router.register(r'devices', DeviceViewSet)


urlpatterns = [
	url(r'^', include(router.urls)),
    url(r'^admin/', include(admin.site.urls)),
    url(r'^appointments/', include(appoinments_urls)),
    url(r'^api_test/', account_views.api_test, name='api_test'),
    url(r'^$', appoinments_views.index, name='home'),
    url(r'^account/', include(account_urls)),
    url(r'^help/', include(help_urls)),
    url(r'^chat/', include(chat_urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
]+ static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
