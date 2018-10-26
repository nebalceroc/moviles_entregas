from django.db import models
from docapp.account.models import UserProfile
from docapp.appointments.models import Appointment
from django.utils import timezone
from django.contrib.auth.models import User

class Wthdrawal(models.Model):
	user = models.ForeignKey(User,default = None)
	time_created = models.DateTimeField(default = timezone.now)
	value = models.IntegerField(default=0)

class AppointmentPayment(models.Model):
	time_created = models.DateTimeField(default = timezone.now)
	value = models.IntegerField(default=0)
	appointment = models.ForeignKey(Appointment)
