from datetime import timedelta
from django.contrib.auth.models import User
from django.db import models
import datetime
import os
import uuid
from django.db.models.lookups import GreaterThan
from django.utils import timezone
from django.conf import settings
from django.db.models.signals import post_save
from django.dispatch import receiver
from rest_framework.authtoken.models import Token


@receiver(post_save, sender=settings.AUTH_USER_MODEL)
def create_auth_token(sender, instance=None, created=False, **kwargs):
    if created:
        Token.objects.create(user=instance)

#-------------------------------------------------------------------------------
# get_all_users
#-------------------------------------------------------------------------------
def get_all_users():
    """
    This method is used to return all users except super users
    """
    
    return User.objects.all().exclude(is_superuser=True)

#-------------------------------------------------------------------------------
# UserProfile
#-------------------------------------------------------------------------------
class UserProfile(models.Model):
    user = models.ForeignKey(User)
    id_tag = models.CharField(max_length=50,default="no_id")
    avatar = models.ImageField(upload_to='user_uploads/avatar/', default='avatar/no_avatar.png')
    birthday = models.DateTimeField(default=timezone.now)
    age = models.IntegerField(default=0)
    nationality = models.CharField(max_length=50)
    member_since = models.DateTimeField(default=timezone.now)
    key = models.CharField(max_length=50, default=uuid.uuid4().__str__())
    key_expires = models.DateTimeField(default=datetime.datetime.now() + \
                                       timedelta(days=2))
    is_verified = models.BooleanField(default=False)
    lat = models.CharField(max_length=30, default="0")
    lon = models.CharField(max_length=30, default="0")
    role = models.CharField(max_length=10, default="none")

    class Meta:
        verbose_name = "User Profile"
        verbose_name_plural = "Users Profiles"
        
    def __unicode__(self):
        return "Profile - %s" % (self.user.username)



#-------------------------------------------------------------------------------
# Device
#-------------------------------------------------------------------------------
class Device(models.Model) :
    ANDROID = 1
    IPHONE = 2
    CHROME = 3
    OTHER = 4

    DEVICE_CHOICES = ( (ANDROID, 'Android'), (IPHONE, 'iPhone') , (CHROME,'Chrome'), (OTHER,'Others'))

    fcm_token = models.CharField(max_length = 250, default='')
    device_type = models.SmallIntegerField(choices = DEVICE_CHOICES,default=1)
    user = models.ForeignKey(User)
    
#-------------------------------------------------------------------------------
# DoctorRequeriment
#-------------------------------------------------------------------------------
class DoctorRequeriments(models.Model):
    country = models.CharField(max_length=50, default="")
    document_name = models.CharField(max_length=50, default="")

#-------------------------------------------------------------------------------
# ModelPasswordResetRequest
#-------------------------------------------------------------------------------
class ModelPasswordResetRequest(models.Model):
    """
    This model class is used to store forgot password requests
    """  
    
    user = models.ForeignKey(User)
    key = models.CharField(max_length=50, default=uuid.uuid4().__str__())    