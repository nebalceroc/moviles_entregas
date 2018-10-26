from rest_framework import serializers
from docapp.help.models import *

class TicketSerializer(serializers.ModelSerializer):
    class Meta:
        model = Ticket
        fields = ('id','user', 'title','message','kind','status','time_created')