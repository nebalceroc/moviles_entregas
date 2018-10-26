from django.db import models

class Game(models.Model):
	host = models.TextField(default="empty", max_length = 100)
    host_fcm_token = models.CharField(max_length = 250, default='')
	guest = models.TextField(default="empty", max_length = 100)
    guest_fcm_token = models.CharField(max_length = 250, default='')
	state = models.TextField(default="empty", max_length = 100)
    turn = models.CharField(
        max_length=1,
        choices=(
            ('G', 'guest'),
            ('H', 'host'),
        ),
        default='H',
    )
