from rest_framework import serializers
from docapp.appointments.models import *

class AppointmentSerializer(serializers.ModelSerializer):

	patient_fname = serializers.SerializerMethodField()

	patient_lname = serializers.SerializerMethodField()

	patient_email = serializers.SerializerMethodField()

	doctor_fname = serializers.SerializerMethodField()

	doctor_lname = serializers.SerializerMethodField()

	doctor_email = serializers.SerializerMethodField()

	doctor_lat = serializers.SerializerMethodField()

	doctor_lon = serializers.SerializerMethodField()

	def get_patient_fname(self, appointment):
		user = User.objects.get(pk=appointment.patient.id)
		return user.first_name

	def get_patient_lname(self, appointment):
		user = User.objects.get(pk=appointment.patient.id)
		return user.last_name

	def get_patient_email(self,appointment):
		user = User.objects.get(pk=appointment.patient.id)
		return user.email

	def get_doctor_fname(self, appointment):
		user = User.objects.get(pk=appointment.doctor.id)
		return user.first_name

	def get_doctor_lname(self, appointment):
		user = User.objects.get(pk=appointment.doctor.id)
		return user.last_name

	def get_doctor_email(self, appointment):
		user = User.objects.get(pk=appointment.doctor.id)
		return user.email

	def get_doctor_lat(self, appointment):
		user = User.objects.get(pk=appointment.doctor.id)
		profile = UserProfile.objects.get(user=user)
		return profile.lat

	def get_doctor_lon(self, appointment):
		user = User.objects.get(pk=appointment.doctor.id)
		profile = UserProfile.objects.get(user=user)
		return profile.lon


	class Meta:
		model = Appointment
		fields = ('id','patient','patient_fname','patient_lname','patient_email','doctor','doctor_fname','doctor_lname','doctor_email','doctor_lat','doctor_lon','status','time_created','city','lat','lon','address','causes','diagnostic','time_range1','time_range2')


class DoctorLegalProfileSerializer(serializers.ModelSerializer):
	class Meta:
		model = DoctorLegalProfile
		fields = ('diploma_state','degree_certificate_state','social_work_certificate_state','rethus_register_state','cmc_id_state','liability_policy_state')



class ICD_10_DiseaseSerializer(serializers.ModelSerializer):
	class Meta:
		model = ICD_10_Disease
		fields = ('classification','place','reg_type','ch_number','code3','code6dot','code6dot2','code6','title','m1','m2','m3','m4','m5')