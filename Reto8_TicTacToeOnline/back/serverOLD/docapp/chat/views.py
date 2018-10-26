from django.shortcuts import render
from services import *

from docapp.chat.models import *
from docapp.chat.serializers import *
from docapp.chat.models import *
from docapp.chat.permissions import *

from rest_framework import viewsets, status
from django.shortcuts import get_object_or_404
from django.http.response import HttpResponseRedirect, JsonResponse, HttpResponseBadRequest
from rest_framework.response import Response
from rest_framework.decorators import authentication_classes, permission_classes, api_view, detail_route, list_route
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from django.shortcuts import render_to_response
from django.template.context import RequestContext

from itertools import chain

# Create your views here.
@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, ChatMessageOwnerPermissions,))
class ChatMessageViewSet(viewsets.ModelViewSet):
    serializer_class = ChatMessageSerializer
    queryset = ChatMessage.objects.all()

    #@list_route(methods=['get'], permission_classes(IsAuthenticated,))
    #def get_doctor_data(self, request, id=0):
    #    serializer = us

    @list_route(methods=['get'], permission_classes=(IsAuthenticated, ))
    def get_messages(self, request):
        user=request.user
        messagess = ChatMessage.objects.filter(sender=request.user)
        messagesr = ChatMessage.objects.filter(receiver=request.user)
        all_messages = list(chain(messagess, messagesr))
        #json.dumps(qs)
        serializer = ChatMessageSerializer(all_messages,many=True,context={'request': request})
        return JsonResponse(serializer.data,safe=False)

    @list_route(methods=['post'], permission_classes=(IsAuthenticated, ))
    def send_message(self, request):
    	msg=request.data['message']
    	sender=request.user
    	receiver=User.objects.get(pk=int(request.data['receiver_id']))
    	new_msg = ChatMessage(message=msg,sender=sender,receiver=receiver)
        new_msg.save()
        serializer = ChatMessageSerializer(new_msg, context={'request': request})
        send_fcm_message(new_msg, request)
        return JsonResponse(serializer.data)