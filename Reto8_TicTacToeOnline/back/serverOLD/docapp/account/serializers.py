from django.contrib.auth.models import User
from rest_framework import serializers
from docapp.account.models import *
from docapp.account.services import get_user_profile



class UserSerializer(serializers.ModelSerializer):

    role = serializers.SerializerMethodField()

    profile_instance_id = serializers.SerializerMethodField()

    lat = serializers.SerializerMethodField()

    lon = serializers.SerializerMethodField()

    device_id = serializers.SerializerMethodField()

    nationality = serializers.SerializerMethodField()

    birthday = serializers.SerializerMethodField()

    state = serializers.SerializerMethodField()

    def get_role(self, user):
    	user_profile_instance = UserProfile.objects.get(user= user)
        return user_profile_instance.role

    def get_profile_instance_id(self, user):
    	user_profile_instance = UserProfile.objects.get(user= user)
    	return user_profile_instance.id

    def get_lat(self, user):
        user_profile_instance = UserProfile.objects.get(user= user)
        return user_profile_instance.lat

    def get_lon(self, user):
        user_profile_instance = UserProfile.objects.get(user= user)
        return user_profile_instance.lon

    def get_device_id(self, user):
        device_id = Device.objects.get(user=user)
        return device_id.id

    def get_nationality(self, user):
        user_profile_instance = UserProfile.objects.get(user= user)
        return user_profile_instance.nationality

    def get_birthday(self, user):
        user_profile_instance = UserProfile.objects.get(user= user)
        return user_profile_instance.birthday

    def get_state(self, user):
        user_profile_instance = UserProfile.objects.get(user= user)
        #if user_profile_instance.role == 'Doctor':
            #doctor_profile = DoctorLegalProfile.objects.get(doctor= user)

        return "Valid"

    class Meta:
        model = User
        fields = ('id', 'role', 'profile_instance_id', 'username', 'email', 'first_name', 'last_name','lat','lon','device_id','nationality','birthday', 'state')


class UserProfileSerializer(serializers.ModelSerializer):
	class Meta:
		model = UserProfile
		fields = ('id', 'user','age','nationality','is_verified','lat','lon')

class DeviceSerializer(serializers.ModelSerializer):
    class Meta:
        model = Device
        fields = ('id', 'fcm_token','device_type')


