from django.db import models

class Game(models.Model):
	host = models.TextField(default="1", max_length = 100)
	host_fcm_token = models.CharField(max_length = 250, default='')
	guest = models.TextField(default="empty", max_length = 100)
	guest_fcm_token = models.CharField(max_length = 250, default='')
	board = models.TextField(default="EEEEEEEEE", max_length = 9)
	state = models.TextField(default="empty", max_length = 100)
	turn = models.IntegerField(default=1)
	class Meta:
		ordering = ('state',)
