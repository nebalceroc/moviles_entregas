import os
import json
from django.contrib.auth import authenticate
from django.contrib.auth import login
from django.contrib.auth import logout
from django.contrib.auth.decorators import login_required
from django.core.paginator import EmptyPage
from django.core.paginator import PageNotAnInteger
from django.core.paginator import Paginator
from django.core.urlresolvers import reverse
from django.http.response import HttpResponseRedirect, JsonResponse, HttpResponseBadRequest, HttpResponse
from django.shortcuts import render_to_response
from django.template.context import RequestContext
from django.utils.html import escape
from django.utils.safestring import mark_safe
from django.utils.html import format_html
from django.contrib.auth.models import User
from docapp.account.forms import *
from docapp.account.models import *
from docapp.account.services import *
from docapp.account.strings import *
from docapp.account.libs.alert import set_alert
from docapp.account.serializers import *
from docapp.account.models import UserProfile, Device
from docapp.account.permissions import *
from docapp.appointments.permissions import *
from docapp.appointments.models import *
from rest_framework import viewsets, status
from django.shortcuts import get_object_or_404
from rest_framework.response import Response
from rest_framework.decorators import authentication_classes, permission_classes, api_view, detail_route, list_route
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated
from django.http import JsonResponse
import requests as req
from pyfcm import FCMNotification


@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, UserOwnerPermissions))
class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    #serializer_class = UserSerializer

    @list_route(methods=['get'], permission_classes=(IsAuthenticated, UserOwnerPermissions,))
    def get_auth_user_json(self, request):
        serializer = UserSerializer(request.user, context={'request': request})
        print JsonResponse(serializer.data)
        return JsonResponse(serializer.data)

    @detail_route(methods=['get'], permission_classes=(IsAuthenticated, ContactPermissions))
    def get_doctor_user_json(self, request, pk=None):
        queryset = User.objects.get(id=pk)
        serializer = UserSerializer(queryset)
        return JsonResponse(serializer.data)

    @detail_route(permission_classes=(permissions.AllowAny,))
    def test_fcm(self, request, pk=None):
        device = Device.objects.get(pk=pk)
        #r = req.get("https://fcm.googleapis.com/fcm/send",headers={"Content-Type":"application/json", "c":"key=AIzaSyBeZnwF7-7SyZNl4owJ_bGR3BLT3ll6_kQ"},data={"data":{"cause":"test"},"to":str(device.registration_id)})
        #device.send_message({'notification':'my test message'})
        push_service = FCMNotification(api_key="AIzaSyBeZnwF7-7SyZNl4owJ_bGR3BLT3ll6_kQ")
        registration_id = device.fcm_token
        message_title = "DocAPp update"
        message_body = "Test update"
        result = push_service.notify_single_device(registration_id=registration_id, message_title=message_title, message_body=message_body)
        return JsonResponse(result,status=status.HTTP_201_CREATED, safe=False)

    @list_route(methods=['post', 'put'], permission_classes=(permissions.AllowAny,))
    def register_user(self, request):
        serializer = UserSerializer(data=request.data,context={'request': request})
        if serializer.is_valid():            
            user = User.objects.create(
                username=request.data['username'],
                email=request.data['email'],
                first_name=request.data['fname'],
                last_name=request.data['lname']
            )
            user.set_password(request.data['password'])
            user.save()
            user = get_user_by_username(request.data['username'])
            profile = UserProfile(user=user,role=request.data['role'].strip(),nationality=request.data['nationality'].strip(),birthday=request.data['birthday'].strip())
            profile.save()
            if request.data['role'].strip() == 'Doctor':
                legal_profile = DoctorLegalProfile(doctor=user)
                legal_profile.save()
                
            device = Device(user=user,fcm_token=request.data['fcm_token'].strip(),device_type=int(request.data['device_type']))
            device.save()
            user_s = UserSerializer(user, context={'request': request})
            return JsonResponse(user_s.data, status=status.HTTP_201_CREATED)
        else:
            return JsonResponse(serializer._errors, status=status.HTTP_400_BAD_REQUEST)

    def list(self, request):
        return HttpResponseBadRequest()


@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, DeviceOwnerPermissions,))
class DeviceViewSet(viewsets.ModelViewSet):
    serializer_class = DeviceSerializer
    queryset = Device.objects.all()

    #@list_route(methods=['get'], permission_classes(IsAuthenticated,))
    #def get_doctor_data(self, request, id=0):
    #    serializer = us

    def list(self, request):
        return HttpResponseBadRequest()


@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated, UserProfileOwnerPermissions,))
class UserProfileViewSet(viewsets.ModelViewSet):
    serializer_class = UserProfileSerializer
    queryset = UserProfile.objects.all()

    @list_route(methods=['post'], permission_classes=(IsAuthenticated, ))
    def upload_avatar(self, request):
        profile = UserProfile.objects.get(user=request.user)
        if request.FILES.get('avatar',None) is not None:
            profile.avatar = request.FILES['avatar']

        profile.save()
        serializer = UserProfileSerializer(profile, context={'request': request})
        return JsonResponse(serializer.data)

    def list(self, request):
        return HttpResponseBadRequest()


@api_view(['PUT','POST'])
@authentication_classes((TokenAuthentication,))
@permission_classes((permissions.AllowAny,))
def register_user(request):
    #serializer = UserSerializer(request.user, context={'request': request})
    serializer = UserSerializer(data=request.data,context={'request': request})
    if serializer.is_valid():
        serializer.create(request.data);
        user = get_user_by_username(request.data['username'])
        profile = UserProfile(user=user,role=request.data['role'].strip())
        profile.save()
        user_s = UserSerializer(user, context={'request': request})
        return JsonResponse(user_s.data, status=status.HTTP_201_CREATED)
    else:
        return JsonResponse(serializer._errors, status=status.HTTP_400_BAD_REQUEST)


def api_test(request):
    return HttpResponse(request,status=status.HTTP_200_OK)


def view_register(request):
	return render_to_response('register.html',
							  {},
                              context_instance = RequestContext(request))

def view_patient_registration(request):

    if request.method == 'POST':
        base_form = FormRegistration(request.POST)
        
        if base_form.is_valid():
            user_profile = register_patient(base_form.cleaned_data)
        
            if not user_profile:
                set_alert(request, REGISTRATION_ERROR, ALERT_ERROR)
                return HttpResponseRedirect(reverse('register'))
            
            else:
                #send_registration_email(request, user_profile)
                
                msg = "You're now registered!"
                
                set_alert(request, msg, ALERT_SUCCESS)
            
        else:
            set_alert(request, FORM_ERROR, ALERT_ERROR)
    else:
        request.session['next_url'] = request.GET.get('next')
        
        base_form = FormRegistration()
    
    return render_to_response('patient_registration.html',
                              {'view_name' : 'view_register',
                               'base_form' : base_form},
                              context_instance = RequestContext(request))

def view_doctor_registration(request):
    
    if request.method == 'POST':
        base_form = FormRegistration(request.POST)
        
        if base_form.is_valid():
            user_profile = register_doctor(base_form.cleaned_data)
        
            if not user_profile:
                set_alert(request, REGISTRATION_ERROR, ALERT_ERROR)
                return HttpResponseRedirect(reverse('register'))
            
            else:
                #send_registration_email(request, user_profile)
                
                msg = "You're now registered!"
                
                set_alert(request, msg, ALERT_SUCCESS)
            
        else:
            set_alert(request, FORM_ERROR, ALERT_ERROR)
    else:
        request.session['next_url'] = request.GET.get('next')
        
        base_form = FormRegistration()
    
    return render_to_response('doctor_registration.html',
                              {'view_name' : 'view_register',
                               'base_form' : base_form},
                              context_instance = RequestContext(request))

#-------------------------------------------------------------------------------
# view_complete_registration
#-------------------------------------------------------------------------------
def view_complete_registration(request, key=None, email=None, next_url=None):
    """
    This method is used to complete the registration process. It first verifies
    the key and email sent via the link and then unlocks the users account, 
    else it redirects them to an error page.
    """
    
    user_profile = is_valid_registration_link(key, email)
    login_url = reverse('login', kwargs={'next_url' : next_url})
    registration_link = reverse('register')
    
    if user_profile:
        verify_user_account(user_profile)
        set_alert(request, REGISTRATION_VERIFIED, ALERT_SUCCESS)
        return HttpResponseRedirect(login_url)
    
    msg = "%s. Please <a href='%s'>click here</a> to re-register." \
    % (INVALID_REGISTRATION_LINK ,  registration_link)
    
    set_alert(request, msg, ALERT_ERROR)
    return HttpResponseRedirect(reverse('error'))
#-------------------------------------------------------------------------------
# view_resend_registration_link
#-------------------------------------------------------------------------------
def view_resend_registration_link(request):
    """
    This view method is used to resend registration links to users.
    """
    
    if request.method == 'POST':
        base_form = FormResendRegistrationLink(request.POST)
        
        if base_form.is_valid():
            email = base_form.cleaned_data.get('email')
            verified = resend_registration_email(request, email)
            
            if not verified:
                msg = "%s %s" % (RESEND_REGISTRATION_LINK_MSG, email)
                set_alert(request, msg, ALERT_SUCCESS)
            else:
                set_alert(request, ACCOUNT_ALREADY_VERIFIED, ALERT_ERROR)
                
            return HttpResponseRedirect(reverse('resend_registration_link'))

        else:
            set_alert(request, FORM_ERROR, ALERT_ERROR)
    else:
        base_form = FormResendRegistrationLink()
    
    return render_to_response('resend_registration_link.html',
                              {'view_name' : 'view_login',
                               'base_form' : base_form},
                              context_instance = RequestContext(request))      
#-------------------------------------------------------------------------------
# view_login
#-------------------------------------------------------------------------------
def view_login(request, next_url=None):
    """
    This view method is used to handle login for a user.
    """
    
    #check if next url is part of the url or is a parameter
    if not next_url:
        next_url = request.GET.get('next')
        
    home_url = reverse('home')
    
    redirect_url = next_url if next_url else home_url
    
    if request.user.is_authenticated():
        return HttpResponseRedirect(home_url)
    
    if request.method == 'POST':
        
        base_form = FormLogin(request.POST)
        
        if base_form.is_valid():
            
            username = base_form.cleaned_data.get('username')
            password = base_form.cleaned_data.get('password')
            
            # check if user and password are matching
            user = authenticate(username=username, password=password)
            
            if user is not None:
                if user.is_active:
                    
                    login(request, user)
                    profile = get_user_profile(user)
                    role = get_user_role(user)
                    request.session['user_profile'] = get_user_profile(user)
                    request.session['user_role'] = role

                    if role == 'Patient':
						set_alert(request, 'Welcome back patient %s %s' % 
								  (user.first_name.capitalize(),
								   user.last_name.capitalize()), 
								  'success')

						return HttpResponseRedirect(redirect_url)
                    elif role == 'Doctor':
						set_alert(request, 'Welcome back doctor %s %s' % 
								  (user.first_name.capitalize(),
								   user.last_name.capitalize()), 
								  'success')

						return HttpResponseRedirect(redirect_url)
                
                else:
                    set_alert(request, DISABLED_ACCOUNT, ALERT_ERROR)
            else:
                set_alert(request, INVALID_USERNAME_PASSWORD, ALERT_ERROR)
        
        # if base form is not valid
        else:
            set_alert(request, INVALID_USERNAME_PASSWORD, ALERT_ERROR)
            
    else:
        
        # check if the user has asked for a particular url
        request.session['next_url'] = next_url
        base_form = FormLogin()
        
    return render_to_response('login.html',
                              {'view_name' : 'view_login',
                               'base_form' : base_form},
                              context_instance = RequestContext(request))      
#-------------------------------------------------------------------------------
# view_profile
#-------------------------------------------------------------------------------
@login_required
def view_profile(request):
    """
    This view method is used to display the profile for a user
    """
    
    return render_to_response('profile.html',
                              context_instance = RequestContext(request))
#-------------------------------------------------------------------------------
# view_profile_update
#-------------------------------------------------------------------------------
@login_required
def view_profile_update(request):
    """
    This view method is used update a users profile.
    """
    
    user_profile = request.session['user_profile']
    
    if request.method == 'POST':
        base_form = FormUserProfile(request.POST, request.FILES)
        
        if base_form.is_valid():
            
            update_user_profile(user_profile, base_form.cleaned_data, 
                                request.FILES)
            
            set_alert(request, PROFILE_UDPATE, ALERT_SUCCESS)
            
            return HttpResponseRedirect(reverse('profile'))
        
        set_alert(request, FORM_ERROR, ALERT_ERROR)    
    
    else:
        initial = {'first_name' : user_profile.user.first_name,
                   'last_name' : user_profile.user.last_name,
                  'email':user_profile.user.email,
                  'bio':user_profile.bio}
        
        base_form = FormUserProfile(initial=initial)
    
    return render_to_response('profile_update.html',
                              {'base_form' : base_form},
                              context_instance = RequestContext(request))
#-------------------------------------------------------------------------------
# view_forgot_password
#-------------------------------------------------------------------------------
def view_forgot_password(request):
    """
    This method is used to send a password reset request to a user.
    """
    
    if request.method == 'POST':
        base_form = FormForgotPassword(request.POST)
        
        if base_form.is_valid():
            username = base_form.cleaned_data.get('username')
            user = get_user_by_username(username)
            send_password_reset_request_email(user)
            set_alert(request, '%s %s' % (PASSWORD_RESET_MSG, user.email), 
                                'success')
        else:
            set_alert(request, INVALID_USERNAME, ALERT_ERROR)
    else:
        base_form = FormForgotPassword()
        
    return render_to_response('forgot_password.html',
                              {'base_form' : base_form},
                              context_instance = RequestContext(request))    
#-------------------------------------------------------------------------------
# reset_password
#-------------------------------------------------------------------------------
def view_reset_password(request, request_key=None):
    """
    This method is used to reset a users password/
    """        
    
    if request.method == 'POST':
        request_key = request.session.get('request_key')
        
        if not request_key:
            set_alert(request, INVALID_PASSWORD_RESET_REQUEST, ALERT_ERROR)
            return HttpResponseRedirect(reverse('forgot_password'))
        
        base_form = FormResetPassword(request.POST)
        
        if base_form.is_valid():
            update_user_password(request_key.user, 
                                 base_form.cleaned_data.get('password'))
            
            request_key.delete()
            
            set_alert(request, PASSWORD_RESET_SUCCESS, ALERT_SUCCESS )
            
            return HttpResponseRedirect(reverse('login', 
                                                kwargs={'next_url' : ''}))
        else:
            set_alert(request, FORM_ERROR, ALERT_ERROR )
    
    else:
        request_key = get_password_reset_request_by_key(request_key)
        
        if not request_key:
            set_alert(request, INVALID_PASSWORD_RESET_REQUEST, ALERT_ERROR)
            return HttpResponseRedirect(reverse('forgot_password'))
        
        request.session['request_key'] = request_key
        base_form = FormResetPassword()
        
    return render_to_response('reset_password.html',
                              {'base_form' : base_form},
                              context_instance = RequestContext(request))    
#-------------------------------------------------------------------------------
# view_forgot_username
#-------------------------------------------------------------------------------
def view_forgot_username(request):
    """
    This method is used to remind a user of his/her username
    """
    
    if request.method == 'POST':
        base_form = FormForgotUsername(request.POST)
        
        if base_form.is_valid():
            email = base_form.cleaned_data.get('email')
            user = get_user_by_email(email)
            send_username_reminder_email(user)
            msg = "%s %s" % (FORGOT_USERNAME_MSG, email)
            set_alert(request, msg, ALERT_SUCCESS)
        else:
            set_alert(request, FORM_ERROR, ALERT_ERROR )

    else:
        base_form = FormForgotUsername()

    return render_to_response('forgot_username.html',
                              {'base_form' : base_form},
                              context_instance = RequestContext(request))    
#-------------------------------------------------------------------------------
# view_logout
#-------------------------------------------------------------------------------
@login_required
def view_logout(request):
    """
    This method is used to logout a user from his session.
    """
    
    logout(request)
    set_alert(request, LOGOUT_MESSAGE, ALERT_SUCCESS)
    redirect_url = reverse('login', kwargs={'next_url' : ''})
    return HttpResponseRedirect(redirect_url)


def view_privacy_policy(request):
    return render_to_response('privacy_policy.html',
                              {},
                              context_instance = RequestContext(request))

def view_terms_of_use(request):
    return render_to_response('terms_of_use.html',
                              {},
                              context_instance = RequestContext(request))  
