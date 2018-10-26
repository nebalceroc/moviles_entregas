from rest_framework import serializers
from server.tictactoe.models import *

class GameSerializer(serializers.ModelSerializer):
    class Meta:
        model = Game
        fields = ('id','host','guest','state','turn')
