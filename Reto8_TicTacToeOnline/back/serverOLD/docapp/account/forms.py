from django import forms
from django.contrib.auth.models import User
from django.core.exceptions import ObjectDoesNotExist
from django.forms import CharField, IntegerField
#-------------------------------------------------------------------------------
# FormUserProfile
#-------------------------------------------------------------------------------
class FormUserProfile(forms.Form):
    """
    This form is used to manage information relating to the users profile
    """

    display_pic = \
    forms.ImageField(required=False)
    
    first_name = \
    forms.CharField(max_length=30,
                    widget=forms.TextInput(attrs={'class' : 'form-control'}))
    
    last_name = \
    forms.CharField(max_length=30, 
                    widget=forms.TextInput(attrs={'class' : 'form-control'}))
    email = \
    forms.EmailField(widget=forms.EmailInput(attrs={'class' : 'form-control',
                                                   'placeholder' : 'Main email'}))
    #channel_name = \
    #forms.CharField(max_length=30, 
    #                widget=forms.TextInput(attrs={'class' : 'form-control'}))
    bio = \
    forms.CharField(max_length=500, 
                    widget=forms.Textarea(attrs={'class' : 'form-control'}))
    
    def clean_email(self):
        """
        This method is used to check whether the email is unique
        """
        
        email = self.cleaned_data.get('email', '')
        
        if (email != ""):
            try:
                User.objects.get(email=email)
            except ObjectDoesNotExist:
                return email

            raise forms.ValidationError("Sorry, this email already exists.")
#-------------------------------------------------------------------------------
# FormResetPassword
#-------------------------------------------------------------------------------
class FormResetPassword(forms.Form):
    """
    This form class is used to manage registration.
    """    

    password = \
    CharField(label="Password", max_length=20,
                   widget=forms.PasswordInput(attrs={'class' : 'form-control',
                                                      'placeholder' : 'Enter new password'}))
    repeat_password = \
    CharField(max_length=20,
                  widget=forms.PasswordInput(attrs={'class' : 'form-control',
                                                    'placeholder' : 'Re-enter new password'}))

    #---------------------------------------------------------------------------
    # repeat_password
    #---------------------------------------------------------------------------
    def clean_repeat_password(self):
        """
        This method is used to check if both the password match
        """

        password = self.cleaned_data.get('password', '')
        repeat_password = self.cleaned_data.get('repeat_password', '')

        if not (password==repeat_password):
            raise forms.ValidationError("Sorry, your passwords do not match...")
        
        return repeat_password
#-------------------------------------------------------------------------------
# FormForgotPassword
#-------------------------------------------------------------------------------
class FormForgotPassword(forms.Form):
    """
    This form class is used to display the form for forgot password
    """
    
    username = \
    forms.CharField(max_length=30,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Enter your username'}))
    
    def clean_username(self):
        """
        This method is used to check whether the username is unique
        """
        
        username = self.cleaned_data.get('username', '')
        
        try:
            User.objects.get(username=username)
        except ObjectDoesNotExist:
            raise forms.ValidationError("Oops! This username does not exist...")
        
        return username
#-------------------------------------------------------------------------------
# FormForgotUsername
#-------------------------------------------------------------------------------
class FormForgotUsername(forms.Form):
    """
    This form class is used to display the for forgot username.
    """
    
    email = \
    forms.EmailField(widget=forms.EmailInput(attrs={'class' : 'form-control',
                                                   'placeholder' : 'Enter your email'}))
    
    def clean_email(self):
        """
        This method is used to check whether the email exists
        """
        
        email = self.cleaned_data.get('email', '')
        
        try:
            User.objects.get(email=email)
        except ObjectDoesNotExist:
            raise forms.ValidationError("Oops! This email does not exist.")
        
        return email
#-------------------------------------------------------------------------------
# FormResendRegistrationLink
#-------------------------------------------------------------------------------
class FormResendRegistrationLink(forms.Form):
    """
    This form class is used to resend the registrationlink.
    """

    email = \
    forms.EmailField(widget=forms.EmailInput(attrs={'class' : 'form-control',
                                                   'placeholder' : 'Enter your email'}))
    
    def clean_email(self):
        """
        This method is used to check whether the email exists
        """
        
        email = self.cleaned_data.get('email', '')
        
        try:
            User.objects.get(email=email)
        except ObjectDoesNotExist:
            raise forms.ValidationError("Oops! This email does not exist.")
        
        return email
#-------------------------------------------------------------------------------
# FormLogin
#-------------------------------------------------------------------------------
class FormLogin(forms.Form):
    """
    This form class is used to manage login information.
    """

    username = \
    forms.CharField(max_length=30,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Enter your username'}))
    password = \
    forms.CharField(max_length=30,
                    widget=forms.PasswordInput(attrs={'class' : 'form-control',
                                                      'placeholder' : 'Enter you password'}))
#-------------------------------------------------------------------------------
# FormRegistration
#-------------------------------------------------------------------------------
class FormRegistration(forms.Form):
    """
    This form class is used to manage registration.
    """    

    first_name = \
    forms.CharField(max_length=30,
                    min_length=3,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Enter your first Name'}))

    last_name = \
    forms.CharField(max_length=30,
                    min_length=3,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Enter your last Name '}))
    username = \
    forms.CharField(max_length=30,
                    min_length=3,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Select a username'}))
    email = \
    forms.EmailField(widget=forms.EmailInput(attrs={'class' : 'form-control',
                                                   'placeholder' : 'Your email '}))
    
    age = forms.IntegerField(widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Enter your age','type':'number'}))
    nationality = \
    forms.CharField(max_length=50,
                    min_length=3,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Enter your nationality'}))
    password = CharField(label="Password",
                             max_length=20,
                             widget=forms.PasswordInput(attrs={'class' : 'form-control',
                                                      'placeholder' : 'Enter a password'}))
    repeat_password = \
    CharField(max_length=20,
                  widget=forms.PasswordInput(attrs={'class' : 'form-control',
                                                    'placeholder' : 'Re-enter password'}))
    channel_name=forms.CharField(required=False,max_length=30,
                    min_length=3,
                    widget=forms.TextInput(attrs={'class' : 'form-control',
                                                  'placeholder' : 'Write your channel\'s name (optional)'}))

    #---------------------------------------------------------------------------
    # repeat_password
    #---------------------------------------------------------------------------
    def clean_repeat_password(self):
        """
        This method is used to check if both the password match
        """

        password = self.cleaned_data.get('password', '')
        repeat_password = self.cleaned_data.get('repeat_password', '')

        if not (password==repeat_password):
            raise forms.ValidationError("Oops! Your passwords do not match...")

    # repeat_password
    #---------------------------------------------------------------------------
    def clean_age(self):
        """
        This method if the user is +18
        """

        age = self.cleaned_data.get('age', '')
        if age < 18:
            raise forms.ValidationError("You must be older than 18 to use this service.")
        else:
            return age
    #---------------------------------------------------------------------------
    # clean_username
    #---------------------------------------------------------------------------
    def clean_username(self):
        """
        This method is used to check whether the username is unique
        """
        
        username = self.cleaned_data.get('username', '')
        try:
            User.objects.get(username=username)
        except ObjectDoesNotExist:
            return username
        
        raise forms.ValidationError("Oops! This username already exists...")
    
  
    #---------------------------------------------------------------------------
    # clean_email
    #---------------------------------------------------------------------------
    def clean_email(self):
        """
        This method is used to check whether the email is unique
        """
        
        email = self.cleaned_data.get('email', '')
        
        if (email != ""):
            try:
                User.objects.get(email=email)
            except ObjectDoesNotExist:
                return email

            raise forms.ValidationError("Oops! This email already exists...")
