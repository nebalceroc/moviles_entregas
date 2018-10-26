from django.shortcuts import render

from tictactoe.models import Game
from tictactoe.serializers import *
from pyfcm import FCMNotification
from django.http.response import HttpResponseRedirect, JsonResponse, HttpResponseBadRequest, HttpResponse
from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import authentication_classes, permission_classes, api_view, detail_route, list_route



#-------------------------------------------------------------------------------
# send message fcm
#-------------------------------------------------------------------------------
# def send_fcm_join_message(_game,request):
#     #FCM API KEY
#     push_service = FCMNotification(api_key="AIzaSyCW_zgK5QCnZOhAr-Rue5gPPtd2yMytPUI")
#     registration_id = _game.host_fcm_token
#     message_title = "Jugador Encontrado"
#     message_body = _game.guest.toString()+" se ha unido al juego"
#     serializer = GameSerializer(_game, context={'request': request})
#     data_message = serializer.data
#     data_message['nkind']="JOIN"
#     result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
#     print JsonResponse(result,status=status.HTTP_200_OK, safe=False)

# def send_fcm_action_message(_game,request):
#     #FCM API KEY
#     push_service = FCMNotification(api_key="AIzaSyCW_zgK5QCnZOhAr-Rue5gPPtd2yMytPUI")
#     registration_id = _game.host_fcm_token
#     message_title = "Jugador Encontrado"
#     message_body = _game.guest.toString()+" se ha unido al juego"
#     serializer = GameSerializer(_game, context={'request': request})
#     data_message = serializer.data
#     data_message['nkind']="JOIN"
#     result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
#     print JsonResponse(result,status=status.HTTP_200_OK, safe=False)

class GameViewSet(viewsets.ModelViewSet):
    serializer_class = GameSerializer
    queryset = Game.objects.all()

    # Create your views here.
    @list_route(methods=['post'])
    def enter_game(self, request):
        guest=request.data['guest']
        game=Game.objects.get(pk=int(request.data['game_id']))
        if game.guest != "empty":
            game.guest = guest
            game.save()
            serializer = GameSerializer(game, context={'request': request})
            send_fcm_join_message(game, request)
            return JsonResponse(serializer.data)
        else:
            return JsonResponse({"error_msg":"Juego lleno"})

    @list_route(methods=['post'])
    def set_move(self, request):
        guest=request.data['guest']
        game=Game.objects.get(pk=int(request.data['game_id']))
        if game.guest != "empty":
            game.guest = guest
            game.save()
            serializer = GameSerializer(game, context={'request': request})
            send_fcm_join_message(game, request)
            return JsonResponse(serializer.data)
        else:
            return JsonResponse({"error_msg":"Juego lleno"})
