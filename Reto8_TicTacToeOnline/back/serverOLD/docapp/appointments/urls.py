from django.conf.urls import patterns, url, include
from docapp.appointments.views import *
from rest_framework import routers

#routers for the rest framework
router = routers.DefaultRouter()

router.register(r'appointments', AppointmentsViewSet)
router.register(r'doctor_legal_profiles', DoctorLegalProfileViewSet)
router.register(r'icd_10', ICD_10_DiseaseViewSet)

urlpatterns = [

    url(r'^', include(router.urls)),

]