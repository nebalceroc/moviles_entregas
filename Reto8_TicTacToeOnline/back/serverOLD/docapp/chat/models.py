from __future__ import unicode_literals

from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User

class ChatMessage(models.Model):
	sender = models.ForeignKey(User, related_name="sender")
	receiver = models.ForeignKey(User, related_name="receiver")
	time_sent = models.DateTimeField(default = timezone.now)
	message	= models.TextField(default="empty", max_length = 500)
	read = models.BooleanField(default=False)
	