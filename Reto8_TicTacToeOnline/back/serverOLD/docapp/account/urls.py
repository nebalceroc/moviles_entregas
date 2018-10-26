from django.conf.urls import patterns, url, include
from docapp.account.views import *
from docapp.account.serializers import UserSerializer
from rest_framework import routers
from rest_framework.authtoken import views
from rest_framework_extensions.routers import NestedRouterMixin



#routers for the rest framework
router = routers.DefaultRouter()
router.register(r'user_profiles', UserProfileViewSet)
router.register(r'users', UserViewSet)

urlpatterns = [

    url(r'^', include(router.urls)),
    
    url(r'^api_token_auth/', views.obtain_auth_token),

    url(r'^register_user/', register_user),

    url(r'^register/?$', view_register, 
        name='register'),

    url(r'^patient-registration/?$', view_patient_registration, 
        name='patient-registration'),

    url(r'^doctor-registration/?$', view_doctor_registration, 
        name='doctor-registration'),

    url(r'^complete/registration/(?P<key>[\w\W]*)/(?P<email>[\w\W]*)/(?P<next_url>[\w\W]*)/?$', 
        view_complete_registration, 
        name='complete_registration'),

    url(r'^resend/registration/?$', 
        view_resend_registration_link, 
        name='resend_registration_link'),
     
    url(r'^login/(?P<next_url>[\w\W]*)/?$', 
        view_login, name='login'),

    url(r'^logout/?$', 
        view_logout, name='logout'),

    url(r'^profile/update/?$', view_profile_update, 
       name='profile_update'),
                       
    url(r'^forgot/password/?$', view_forgot_password, 
       name='forgot_password'),

    url(r'^forgot/username/?$', view_forgot_username, 
       name='forgot_username'),

    url(r'^reset/password/(?P<request_key>[\w\W]*)/??$', 
       view_reset_password, 
       name='reset_password'),
                                           
    url(r'^profile/?$', view_profile, 
       name='profile'),
    
    url(r'^terms-of-use/?$', view_terms_of_use, 
       name='terms-of-use'),
    
    url(r'^privacy-policy/?$', view_privacy_policy, 
       name='privacy-policy'),
    
]
