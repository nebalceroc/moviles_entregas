from django.db import models
import random

def random_start():
	return random.randint(1,2)

class Game(models.Model):
	def switch_turn(self):
		if self.turn == 1:
			self.turn = 2
		else:
			self.turn = 1

	host = models.TextField(default="1", max_length = 100)
	host_fcm_token = models.CharField(max_length = 250, default='')
	guest = models.TextField(default="empty", max_length = 100)
	guest_fcm_token = models.CharField(max_length = 250, default='')
	board = models.TextField(default="EEEEEEEEE", max_length = 9)
	state = models.TextField(default="empty", max_length = 100)
	turn = models.IntegerField(default=random_start)
	class Meta:
		ordering = ('state',)
