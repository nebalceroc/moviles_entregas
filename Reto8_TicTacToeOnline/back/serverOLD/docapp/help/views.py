from django.shortcuts import render_to_response
from django.template.context import RequestContext

from docapp.help.models import *
from docapp.help.serializers import *

from rest_framework import viewsets, status
from django.shortcuts import get_object_or_404
from django.http.response import HttpResponseRedirect, JsonResponse, HttpResponseBadRequest
from rest_framework.response import Response
from rest_framework.decorators import authentication_classes, permission_classes, api_view, detail_route, list_route
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from django.shortcuts import render_to_response
from django.template.context import RequestContext

@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, ))
class TicketViewSet(viewsets.ModelViewSet):
    serializer_class = TicketSerializer
    queryset = Ticket.objects.all()


