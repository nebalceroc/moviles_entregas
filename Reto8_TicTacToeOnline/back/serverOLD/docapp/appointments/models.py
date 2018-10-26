from django.db import models
from docapp.account.models import UserProfile
from django.utils import timezone
from django.contrib.auth.models import User


class ICD_10_Disease(models.Model):
	classification = models.IntegerField(default=0)
	place = models.CharField(max_length=1,default='')
	reg_type = models.CharField(max_length=1,default='')
	ch_number = models.IntegerField(default=0)
	code3 = models.CharField(max_length=3,default='')
	code6dot = models.CharField(max_length=7,default='')
	code6dot2 = models.CharField(max_length=7,default='')
	code6 = models.CharField(max_length=6,default='')
	title = models.CharField(max_length=200,default='')
	m1 = models.CharField(max_length=5,default='')
	m2 = models.CharField(max_length=5,default='')
	m3 = models.CharField(max_length=5,default='')
	m4 = models.CharField(max_length=5,default='')
	m5 = models.CharField(max_length=5,default='')

	class Meta:
		verbose_name = "ICD_10_Disease"
		verbose_name_plural = "ICD_10_Disease"

	def __unicode__(self):
		return "ICD_10_Disease - %s" % (self.id)

class Appointment(models.Model):
	patient = models.ForeignKey(User, related_name="patient")
	doctor = models.ForeignKey(User, related_name="doctor")

	statuses = (
        ('0', 'created'),
        ('1', 'paid'),
        ('2', 'assigned'),
        ('3', 'attended'),
        ('4', 'problematic'),
        )

	status = models.CharField(max_length=1, choices=statuses, default='0')
	time_created = models.DateTimeField(default = timezone.now)
	lon = models.CharField(max_length=30, default="0")
	lat = models.CharField(max_length=30, default="0")
	address = models.CharField(max_length = 50, default = "")
	city = models.CharField(max_length = 50, default = "")
	causes = models.TextField(default = "", max_length = 2500)
	diagnostic = models.TextField(default = "", max_length = 2500)
	#icd_tags = models.ManyToManyField(ICD_10_Disease)
	time_range1 = models.DateTimeField(default = timezone.now)
	time_range2 = models.DateTimeField(default = timezone.now)

	class Meta:
		verbose_name = "Appointment"
		verbose_name_plural = "Appointments"

	def __unicode__(self):
		return "Appointment - %s" % (self.id)

class UserMedicRegister(models.Model):
	patient = models.ForeignKey(User)
	reg_types = (
        ('0', 'disease'),
        ('1', 'surgery'),
		('2', 'alergy'),
		('3', 'other'),
        )
	reg_type = models.CharField(max_length=1, choices=reg_types, default='2')
	description = models.TextField(default = "", max_length = 1000)


def get_user_diploma_folder_path(instance, filename):
	return "user_uploads/documents/%s/diploma/%s" %(instance.doctor.username, filename)

def get_user_degree_folder_path(instance, filename):
	return "user_uploads/documents/%s/degree/%s" %(instance.doctor.username, filename)

def get_user_social_work_folder_path(instance, filename):
	return "user_uploads/documents/%s/social_work/%s" %(instance.doctor.username, filename)

def get_user_rethus_folder_path(instance, filename):
	return "user_uploads/documents/%s/rethus/%s" %(instance.doctor.username, filename)

def get_user_cmc_id_folder_path(instance, filename):
	return "user_uploads/documents/%s/cmc_id/%s" %(instance.doctor.username, filename)

def get_user_policy_folder_path(instance, filename):
	return "user_uploads/documents/%s/policy/%s" %(instance.doctor.username, filename)

class DoctorLegalProfile(models.Model):
	doctor = models.ForeignKey(User)
	doc_state = (
        (0, 'pending'),
        (1, 'sent'),
        (2, 'valid'),
        (3, 'invalid'),
        )
	diploma_state = models.IntegerField(choices=doc_state, default=0)
	diploma = models.ImageField(upload_to=get_user_diploma_folder_path, default='avatar/no_avatar.png')
	degree_certificate_state = models.IntegerField(choices=doc_state, default=0)
	degree_certificate = models.ImageField(upload_to=get_user_degree_folder_path, default='avatar/no_avatar.png')
	social_work_certificate_state = models.IntegerField(choices=doc_state, default=0)
	social_work_certificate = models.ImageField(upload_to=get_user_social_work_folder_path, default='avatar/no_avatar.png')
	rethus_register_state = models.IntegerField(choices=doc_state, default=0)
	rethus_register = models.ImageField(upload_to=get_user_rethus_folder_path, default='avatar/no_avatar.png')
	cmc_id_state = models.IntegerField(choices=doc_state, default=0)
	cmc_id = models.ImageField(upload_to=get_user_cmc_id_folder_path, default='avatar/no_avatar.png')
	liability_policy_state = models.IntegerField(choices=doc_state, default=0)
	liability_policy = models.ImageField(upload_to=get_user_policy_folder_path, default='avatar/no_avatar.png')

	class Meta:
		verbose_name = "Doctor Legal Profile"
		verbose_name_plural = "Doctors Legal Profiles"

	def __unicode__(seld):
		return "Legal Profiles - %s" % (self.doctor.username)
