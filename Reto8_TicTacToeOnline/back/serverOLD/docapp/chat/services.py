from models import *
from serializers import *
from pyfcm import FCMNotification
from django.http.response import JsonResponse
from rest_framework import viewsets, status
from docapp.account.models import UserProfile, Device

#-------------------------------------------------------------------------------
# send message
#-------------------------------------------------------------------------------
def send_fcm_message(_msg,request):
    device = Device.objects.get(user=_msg.receiver)
    push_service = FCMNotification(api_key="AIzaSyCW_zgK5QCnZOhAr-Rue5gPPtd2yMytPUI")
    registration_id = device.fcm_token
    message_title = "Nuevo mensaje"
    message_body = "Has recibido un nuevo mensaje"
    serializer = ChatMessageSerializer(_msg, context={'request': request})
    data_message = serializer.data
    data_message['nkind']="CHAT_MESSAGE"
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
    print JsonResponse(result,status=status.HTTP_200_OK, safe=False)