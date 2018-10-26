from rest_framework import permissions



class ContactPermissions(permissions.BasePermission):

    def has_object_permission(self, request, view, user):
        print view.action
        if not request.user.is_authenticated():
            return False
        else:
            # check if any appointment in common

            return request.user == user_profile.user

class AppointmentOwnerPermissions(permissions.BasePermission):
    """
    Handles permissions for appointments.  The basic rules are

     - owner may GET, PUT, POST, DELETE
     - anyone can create
     - auth doctors can patch
    """

    def has_object_permission(self, request, view, appointment):
        print view.action
        if not request.user.is_authenticated():
            if view.action == 'create':
                return True
        else:
            # check if user is owner
            return request.user == appointment.patient.user

class DoctorLegalProfileOwnerPermissions(permissions.BasePermission):

    def has_object_permission(self, request, view, profile):
        print view.action
        if not request.user.is_authenticated():
            if view.action == 'create':
                return True
        else:
            # check if user is owner
            return request.user == profile.doctor