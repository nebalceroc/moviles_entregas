from rest_framework import permissions


class UserOwnerPermissions(permissions.BasePermission):
    """
    Handles permissions for users.  The basic rules are

     - owner may GET, PUT, POST, DELETE
     - anyone can create
    """

    def has_object_permission(self, request, view, user):
    	if not request.user.is_authenticated():
            if view.action == 'create':
                return True
        else:
	        # check if user is owner
	        return request.user == user


class UserProfileOwnerPermissions(permissions.BasePermission):

    def has_object_permission(self, request, view, user_profile):
    	print view.action
    	if not request.user.is_authenticated():
            if view.action == 'create':
                return True
        else:
	        # check if user is owner
	        return request.user == user_profile.user


class DeviceOwnerPermissions(permissions.BasePermission):

    def has_object_permission(self, request, view, device):
        print view.action
        if not request.user.is_authenticated():
            if view.action == 'create':
                return True
        else:
            # check if user is owner
            return request.user == device.user