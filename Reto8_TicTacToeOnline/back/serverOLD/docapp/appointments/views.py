import os
import json
from django.contrib.auth import authenticate
from django.contrib.auth import login
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.core.paginator import EmptyPage
from django.core.paginator import PageNotAnInteger
from django.core.paginator import Paginator
from django.core.urlresolvers import reverse
from django.http.response import HttpResponseRedirect, JsonResponse, HttpResponseBadRequest
from django.shortcuts import render_to_response
from django.template.context import RequestContext
from django.utils.html import escape
from django.utils.safestring import mark_safe
from django.utils.html import format_html
from django.contrib.auth.models import User
from docapp.appointments.models import *
from docapp.appointments.serializers import *
from docapp.appointments.models import Appointment
from docapp.appointments.permissions import *
from rest_framework import viewsets, status
from django.shortcuts import get_object_or_404
from rest_framework.response import Response
from rest_framework.decorators import authentication_classes, permission_classes, api_view, detail_route, list_route
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from django.shortcuts import render_to_response
from django.template.context import RequestContext

import django_filters
from rest_framework import filters
from rest_framework import generics
from services import haversine, notify_appointment_status_change

from itertools import chain

def index(request):
    return render_to_response("index.html",{},
                              context_instance = RequestContext(request))


@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, AppointmentOwnerPermissions))
class AppointmentsViewSet(viewsets.ModelViewSet):
    serializer_class = AppointmentSerializer
    queryset = Appointment.objects.all()

    @detail_route(methods=['patch'], permission_classes=(IsAuthenticated, ))
    def attend_appointment(self, request, pk=None):
        appointment = Appointment.objects.get(pk=pk)
        doctor = User.objects.get(pk=int(request.data['doctor']))
        appointment.doctor=doctor
        appointment.status=2
        appointment.save()
        serializer = AppointmentSerializer(appointment, context={'request': request})
        notify_appointment_status_change(pk,2)
        return JsonResponse(serializer.data)

    @detail_route(methods=['patch'], permission_classes=(IsAuthenticated, ))
    def register_diagnostic(self, request, pk=None):
        appointment = Appointment.objects.get(pk=pk)
        appointment.diagnostic = request.data['diagnostic']
        appointment.status=3
        appointment.save()
        serializer = AppointmentSerializer(appointment, context={'request': request})
        notify_appointment_status_change(pk,3)
        return JsonResponse(serializer.data)


    @list_route(methods=['get'], permission_classes=(IsAuthenticated,))
    def get_near_appointments(self, request):
    	all_appointments = Appointment.objects.all()	
    	profile = UserProfile.objects.get(user=request.user)
    	req_appointments = []
    	for appointment in all_appointments:
    		print "lat "+str(profile.lat) + " lon "+str(profile.lon)
    		print "alat "+appointment.lat + " lon "+appointment.lon
    		d = haversine(float(profile.lat),float(profile.lon),float(appointment.lat),float(appointment.lon))
    		print appointment.status
            #CAMBIAR AL IMPLEMENTAR PAGOS
    		if d <= 1000 and appointment.status == "0":
    			req_appointments.append(appointment)

    	none_qs = Appointment.objects.none()
    	qs = list(chain(none_qs, req_appointments))
    	#json.dumps(qs)
    	serializer = AppointmentSerializer(qs,many=True,context={'request': request})
    	return JsonResponse(serializer.data,safe=False)

    @list_route(methods=['get'], permission_classes=(IsAuthenticated,))
    def get_doctor_appointments(self, request):
    	all_appointments = Appointment.objects.all()	
    	profile = UserProfile.objects.get(user=request.user)
    	req_appointments = []
    	for appointment in all_appointments:
    		if appointment.doctor.id == request.user.id:
    			req_appointments.append(appointment)

    	none_qs = Appointment.objects.none()
    	qs = list(chain(none_qs, req_appointments))
    	#json.dumps(qs)
    	serializer = AppointmentSerializer(qs,many=True,context={'request': request})
    	return JsonResponse(serializer.data,safe=False)

    @list_route(methods=['get'], permission_classes=(IsAuthenticated,))
    def get_patient_appointments(self, request):
    	all_appointments = Appointment.objects.all()	
    	profile = UserProfile.objects.get(user=request.user)
    	req_appointments = []
    	for appointment in all_appointments:
    		if appointment.patient.id == request.user.id:
    			req_appointments.append(appointment)

    	none_qs = Appointment.objects.none()
    	qs = list(chain(none_qs, req_appointments))
    	#json.dumps(qs)
    	serializer = AppointmentSerializer(qs,many=True,context={'request': request})
    	return JsonResponse(serializer.data,safe=False)


    

@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, DoctorLegalProfileOwnerPermissions))
class DoctorLegalProfileViewSet(viewsets.ModelViewSet):
    serializer_class = DoctorLegalProfileSerializer
    queryset = DoctorLegalProfile.objects.all()

    @list_route(methods=['post'], permission_classes=(IsAuthenticated, ))
    def upload_doctor_legal_docs(self, request):
        profile = DoctorLegalProfile.objects.get(doctor=request.user)
        if request.FILES.get('diploma',None) is not None:
            profile.diploma = request.FILES['diploma']
            profile.diploma_state = 1

        if request.FILES.get('degree',None) is not None:
            profile.degree_certificate = request.FILES['degree']
            profile.degree_certificate_state = 1

        if request.FILES.get('social_work',None) is not None:
            profile.social_work_certificate = request.FILES['social_work']
            profile.social_work_certificate_state = 1

        if request.FILES.get('rethus',None) is not None:
            profile.rethus_register = request.FILES['rethus']
            profile.rethus_register_state = 1

        if request.FILES.get('cmc',None) is not None:
            profile.cmc_id = request.FILES['cmc']
            profile.cmc_id_state = 1

        if request.FILES.get('policy',None) is not None:
            profile.liability_policy = request.FILES['policy']
            profile.liability_policy_state = 1

        profile.save()
        serializer = DoctorLegalProfileSerializer(profile, context={'request': request})
        return JsonResponse(serializer.data)

    @list_route(methods=['get'], permission_classes=(IsAuthenticated, DoctorLegalProfileOwnerPermissions))
    def get_legal_profile(self, request):
        profile = DoctorLegalProfile.objects.get(doctor=request.user)
        serializer = DoctorLegalProfileSerializer(profile, context={'request': request})
        print serializer.data
        return JsonResponse(serializer.data)


#@authentication_classes((TokenAuthentication,))
@permission_classes((permissions.AllowAny, ))
class ICD_10_DiseaseViewSet(viewsets.ModelViewSet):
    serializer_class = ICD_10_DiseaseSerializer
    queryset = ICD_10_Disease.objects.all()

