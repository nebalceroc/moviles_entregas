from rest_framework import serializers
from docapp.chat.models import *

class ChatMessageSerializer(serializers.ModelSerializer):
    class Meta:
        model = ChatMessage
        fields = ('id', 'sender','receiver','message','read','time_sent')