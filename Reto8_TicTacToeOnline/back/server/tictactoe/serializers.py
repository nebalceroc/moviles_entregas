from rest_framework import serializers
from tictactoe.models import *

class GameSerializer(serializers.ModelSerializer):
    class Meta:
        model = Game
        fields = ('id','host','host_fcm_token','guest','guest_fcm_token','board','state','turn')
