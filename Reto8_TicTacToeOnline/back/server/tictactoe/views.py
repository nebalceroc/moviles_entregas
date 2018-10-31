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
def send_fcm_join_message(_game,request):
    push_service = FCMNotification(api_key="AIzaSyBLJoll6rWObd-1GhG-jJD3kaSzmzAaSf8")
    registration_id = _game.host_fcm_token
    message_title = "Jugador Encontrado"
    message_body = _game.guest+" se ha unido al juego"
    serializer = GameSerializer(_game, context={'request': request})
    data_message = serializer.data
    data_message['nkind']="JOIN"
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
    print(JsonResponse(result,status=status.HTTP_200_OK, safe=False))

def send_fcm_action_message(_game,_player,_location,request):
    #FCM API KEY
    push_service = FCMNotification(api_key="AIzaSyBLJoll6rWObd-1GhG-jJD3kaSzmzAaSf8")
    if(str(_player)=='H'):
        registration_id = _game.guest_fcm_token
        print("HOST MOVED SENDING N TO:"+str(registration_id))
    if(str(_player)=='G'):
        registration_id = _game.host_fcm_token
        print("GUEST MOVED SENDING N TO:"+str(registration_id))

    message_title = "Jugador "+ str(_player) +" Movio"
    message_body = str(_player)+" tomo la casilla "+str(_location)
    serializer = GameSerializer(_game, context={'request': request})
    data_message = serializer.data
    data_message['nkind']="ACTION"
    result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body, data_message=data_message)
    print(result)
    print(JsonResponse(result,status=status.HTTP_200_OK, safe=False))
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
    def join_game(self, request):
        print(str(request.data))
        guest_fcm_token=request.data['guest_fcm_token']
        game=Game.objects.get(pk=int(request.data['game_id']))
        print(str(game.guest))
        if game.guest == "empty":
            game.guest = "2"
            game.guest_fcm_token = guest_fcm_token
            game.save()
            serializer = GameSerializer(game, context={'request': request})
            send_fcm_join_message(game, request)
            return JsonResponse(serializer.data)
        else:
            return JsonResponse({"error_msg":"Juego lleno"})

    @list_route(methods=['post'])
    def set_move(self, request):
        print(str(request.data))
        #import pdb; pdb.set_trace()
        player=request.data['player']
        location=request.data['location']
        game=Game.objects.get(pk=int(request.data['game_id']))
        if game.board[int(location)] == 'E':
            new = list(game.board)
            new[int(location)] = player
            game.board=''.join(new)
            game.switch_turn()
            game.save()
            serializer = GameSerializer(game, context={'request': request})
            send_fcm_action_message(game, player, location, request)
            return JsonResponse(serializer.data)
        else:
            return JsonResponse({"error_msg":"Movimiento Invalido"})
