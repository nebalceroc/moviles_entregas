from django.db import models
from django.utils import timezone
from django.contrib.auth.models import User

class Ticket(models.Model):
	user = models.ForeignKey(User)
        title = models.TextField(default="empty", max_length = 30)
	message	= models.TextField(default="empty", max_length = 500)
	kind_options = (
        ('0', 'general'),
        ('1', 'appointment'),
        ('2', 'payment'),
        ('3', 'chat '),
        )
	
	kind = models.CharField(max_length=1, choices=kind_options, default='0')
	statuses = (
        ('0', 'created'),
        ('1', 'active'),
        ('2', 'solved'),
        ('3', 'problematic'),
        )
	
	status = models.CharField(max_length=1, choices=statuses, default='0')
	time_created = models.DateTimeField(default = timezone.now)

        class Meta:
                verbose_name = "Ticket"
                verbose_name_plural = "Tickets"

        def __unicode__(self):
                return "Ticket - %s" % (self.id)