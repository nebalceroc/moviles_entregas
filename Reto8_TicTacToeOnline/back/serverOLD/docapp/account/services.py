from django.contrib.auth.models import User
from django.core.exceptions import ObjectDoesNotExist
from django.core.files import File
from django.core.urlresolvers import reverse
from django.utils import timezone
from docapp.account.models import *
from docapp.account.libs import send_email
from docapp.settings import BASE_URL
import json
import os




#-------------------------------------------------------------------------------
# get_user_dict
#-------------------------------------------------------------------------------
def get_user_dict(user):
    """
    This method returns a dictionary with user data.
    """
    
    return {'id' : user.id, 'first_name' : user.first_name, 
            'last_name' : user.last_name, 'email' : user.email, 
            'username' : user.username, 
            'combined' : '%s %s - (%s)' % (user.first_name, user.last_name, 
                                           user.username)}
#-------------------------------------------------------------------------------
# get_user_by_id
#-------------------------------------------------------------------------------
def get_user_by_id(userid):
    """
    This method is used to get a user by id.
    """
    
    try:
        return User.objects.get(pk=int(userid))
    except (ObjectDoesNotExist, TypeError, ValueError):
        return None
#-------------------------------------------------------------------------------
# get_users
#-------------------------------------------------------------------------------
def get_users(return_json=False):
    """
    This method is used to return a list of all users in the system, except
    super users
    """
    
    users = User.objects.all().exclude(is_superuser=True)
    
    if return_json:
        users = [get_user_dict(user) for user in users]
        return json.dumps(users)
    
    return users
#-------------------------------------------------------------------------------
# get_user_by_email
#-------------------------------------------------------------------------------
def get_user_by_email(email):
    """
    This method is used to fetch a user using the email id.
    """
    
    try:
        return User.objects.get(email=email)
    except(ObjectDoesNotExist, TypeError, ValueError):
        return None
#-------------------------------------------------------------------------------
# get_user_by_username
#-------------------------------------------------------------------------------
def get_user_by_username(username):
    """
    This method is used to fetch a user using the username.
    """
    
    try:
        return User.objects.get(username=username)
    except(ObjectDoesNotExist, TypeError, ValueError):
        return None    


#-------------------------------------------------------------------------------
# get_user_profile_by_email
#-------------------------------------------------------------------------------
def get_user_profile_by_email(email):
    """
    This method is used to return the user profile using the users email.
    """
    
    try:
        return ModelUserProfile.objects.get(user__email=email)
    except (ObjectDoesNotExist, TypeError, ValueError):
        return None
#-------------------------------------------------------------------------------
# expire_account
#-------------------------------------------------------------------------------
def expire_account(user):
    """
    This method is used to delete a users account.
    """
    
    user.delete()
#-------------------------------------------------------------------------------
# unlock_account
#-------------------------------------------------------------------------------
def unlock_account(user_profile):
    """
    This method is used to unlock a user account.
    """
    
    user_profile.user.is_active = True
    user_profile.user.save()
#-------------------------------------------------------------------------------
# lock_account
#-------------------------------------------------------------------------------
def lock_account(user):
    """
    This method is used to lock a user account
    """
    
    user.is_active = False
    user.save()
#-------------------------------------------------------------------------------
# create_user
#-------------------------------------------------------------------------------
def create_user(user_data):
    """
    This method is used to create a user. It first check whether a user exists.
    If yes, then the same user is returned else the user is created.
    """
    
    username = user_data.get('username')
    email = user_data.get('email')
    password = user_data.get('password')
    first_name =  user_data.get('first_name')
    last_name =  user_data.get('last_name')
    
    if not username or not password:
        return None
    
    try:
        user = User.objects.get(username=username)
    except ObjectDoesNotExist:
        user = User.objects.create_user(username, email, password)
    
        if first_name:
            user.first_name = first_name
    
        if last_name:
            user.last_name = last_name
            
        user.save()
    
    return user

def get_user_profile(user):

    try:
        user= UserProfile.objects.get(user=user)
        return UserProfile.objects.get(user=user)
    except:
        return None
	
#-------------------------------------------------------------------------------
# register_patient
#-------------------------------------------------------------------------------
#def register_patient(user_data):
#    """
#    This method is used to register a user.
#    """
#    
#    user = create_user(user_data)
#    if not user:
#        return None
#    profile = PatientProfile(user=user,age=user_data.get('age'),nationality=user_data.get('nationality'))
#    profile.save()
#    
#    return profile

#-------------------------------------------------------------------------------
# register_doctor
#-------------------------------------------------------------------------------
#def register_doctor(user_data):
#    """
#    This method is used to register a user.
#    """
#    
#    user = create_user(user_data)
#    if not user:
#        return None
#    profile = DoctorProfile(user=user,age=user_data.get('age'),nationality=user_data.get('nationality'))
#    profile.save()
#    
#    return profile

#-------------------------------------------------------------------------------
# verify_user_account
#-------------------------------------------------------------------------------
def verify_user_account(user_profile):
    """
    This method is used to verify a users account.
    """
    
    user_profile.is_verified = True
    user_profile.save()
    
    unlock_account(user_profile)   
#-------------------------------------------------------------------------------
# generate_registration_link
#-------------------------------------------------------------------------------
def generate_registration_link(request, user_profile):
    """
    This method is used to generate a registration link.
    """
    
    next_url = request.session.get('next_url', '')
    kwargs = {'key' : user_profile.key, 'email' : user_profile.user.email,
              'next_url' : next_url if next_url else ''}
    
    registration_link = reverse('complete_registration', kwargs=kwargs)
    
    return "%s%s" % (BASE_URL, registration_link)     
#-------------------------------------------------------------------------------
# send_registration_email
#-------------------------------------------------------------------------------
def send_registration_email(request, user_profile):
    """
    This method is used to send an email to the user containing the link to 
    complete registration.
    """
    
    context_data = {'link' : generate_registration_link(request, user_profile)}
    
    send_email('registration-success', context_data, [user_profile.user.email])
#-------------------------------------------------------------------------------
# is_valid_registration_link
#-------------------------------------------------------------------------------
def is_valid_registration_link(key, email):
    """
    This method is used to verify the key and email provided in the registration
    link. It first checks whether the user profile exists, using the email 
    provided. It then check whether the profile has already been verified 
    and that the profile key is valid and has not expired. 
    """
    
    user_profile = get_user_profile_by_email(email)
    
    # check for valid profile
    if not user_profile:
        return False
    
    # check for a valid profile key
    if not user_profile.key==key:
        return False
    
    # check for a valid profile key expiration time
    if not user_profile.is_verified and user_profile.key_expires > timezone.now():
        return user_profile
        
    return False
#-------------------------------------------------------------------------------
# resend_registration_email
#-------------------------------------------------------------------------------
def resend_registration_email(request, email):
    """
    This method is used to resend the registration email to the user. 
    """
    
    user_profile = get_user_profile_by_email(email)
    
    if not user_profile.is_verified:
        send_registration_email(request, user_profile)
        return False
        
    return True
#-------------------------------------------------------------------------------
# create_password_reset_request
#-------------------------------------------------------------------------------
def create_password_reset_request(user):
    """
    This method is used to create a password reset request for a user.
    """
    
    try:
        reset_request = ModelPasswordResetRequest.objects.get(user=user)
    except ObjectDoesNotExist:
        reset_request = ModelPasswordResetRequest(user=user)
        reset_request.save()
    
    return reset_request.key
#-------------------------------------------------------------------------------
# get_password_reset_request_by_key
#-------------------------------------------------------------------------------
def get_password_reset_request_by_key(key):
    """
    This method is used to retrieve a password request user
    """

    try:
        reset_request = ModelPasswordResetRequest.objects.get(key=key)
    except ObjectDoesNotExist:
        return None
    
    return reset_request    
#-------------------------------------------------------------------------------
# get_reset_password_link
#-------------------------------------------------------------------------------
def get_reset_password_link(request_key):
    """
    This method is used to generate the password forgot link for a user.
    """
    
    kwargs = {'request_key' : request_key}
    
    return "%s%s" %(BASE_URL, reverse('reset_password', kwargs=kwargs))
#-------------------------------------------------------------------------------
# send_password_reset_request_email
#-------------------------------------------------------------------------------
def send_password_reset_request_email(user):
    """
    This method is used to send a password reset link to the user
    """
    
    request_key = create_password_reset_request(user)
    reset_link = get_reset_password_link(request_key)
    context_data = {'link' : reset_link}
    send_email('reset-password', context_data, [user.email])
#-------------------------------------------------------------------------------
# update_user_password
#-------------------------------------------------------------------------------
def update_user_password(user, password):
    """
    This method is used to update a users password
    """
    
    user.set_password(password)
    user.save()
#-------------------------------------------------------------------------------
# send_username_reminder_email
#-------------------------------------------------------------------------------
def send_username_reminder_email(user):
    """
    This method is used reminder a user of his username.
    """
    
    send_email('username-reminder', {'username' : user.username }, [user.email])
#-------------------------------------------------------------------------------
# update_user_profile
#-------------------------------------------------------------------------------
def update_user_profile(user_profile, data, FILES):
    """
    This method is used to update profile information for a user.
    """
    
    first_name = data.get('first_name')
    last_name = data.get('last_name')
    email = data.get('email')
    #channel_name = data.get('channel_name')
    bio = data.get('bio')
    #profile_pic = FILES.get('display_pic')
    
    
    if first_name:
        user_profile.user.first_name = first_name
        
    if last_name:
        user_profile.user.last_name = last_name
    user_profile.user.save()
    
    if email:
        user_profile.user.email=email
        user_profile.user.save()
    #if channel_name:
    #    user_profile.user.channel.name=channel_name
    #    user_profile.user.channel.save()
    if bio:
        user_profile.bio=bio
        user_profile.save()
        
#     if profile_pic:
#         filename = profile_pic.__str__()
#         
#         try:
#             old_profile_pic_url = user_profile.display_pic.path.__str__()
#         except ValueError:
#             old_profile_pic_url = None
#             
#         user_profile.display_pic.save(filename, File(profile_pic))
#         
#         if old_profile_pic_url:
#             os.remove(old_profile_pic_url)

    