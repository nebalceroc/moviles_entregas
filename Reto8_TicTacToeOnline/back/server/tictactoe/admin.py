from django.contrib import admin
from tictactoe.models import Game

class GameAdmin(admin.ModelAdmin):
    pass

admin.site.register(Game, GameAdmin)
