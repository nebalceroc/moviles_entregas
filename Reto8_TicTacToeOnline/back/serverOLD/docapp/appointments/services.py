from math import radians, cos, sin, asin, sqrt
from models import *
from serializers import AppointmentSerializer
from pyfcm import FCMNotification
from django.http.response import JsonResponse
from rest_framework import viewsets, status
from docapp.account.models import UserProfile, Device



   


#-------------------------------------------------------------------------------
# distance between two points
#-------------------------------------------------------------------------------
def haversine(lon1, lat1, lon2, lat2):
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371
    return c * r

#-------------------------------------------------------------------------------
# notify appointment change
#-------------------------------------------------------------------------------
def notify_appointment_status_change(_id, _status):
    appointment = Appointment.objects.get(pk=_id)
    doctor = User.objects.get(pk=appointment.doctor.id)
    doctor_profile = UserProfile.objects.get(user=doctor)
    serializer = AppointmentSerializer(appointment)
    device = Device.objects.get(user=appointment.patient)
    push_service = FCMNotification(api_key="AIzaSyCW_zgK5QCnZOhAr-Rue5gPPtd2yMytPUI")
    registration_id = device.fcm_token
    message_title = "Actualizacion cita medica"
    if _status == 2:
        message_body = "Ya hay un doctor dispuesto a atenderte!"
    elif _status == 3:
        message_body = "Ya se registro el diagnostico de tu cita."
    data_message = serializer.data
    data_message['nkind']="APPOINTMENT_STATUS_UPDATE"
    data_message['doctor_fname']=doctor.first_name
    data_message['doctor_lname']=doctor.last_name
    data_message['doctor_lat']=doctor_profile.lat
    data_message['doctor_lon']=doctor_profile.lon
    data_message['doctor_email']=doctor.email
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
    print JsonResponse(result,status=status.HTTP_200_OK, safe=False)