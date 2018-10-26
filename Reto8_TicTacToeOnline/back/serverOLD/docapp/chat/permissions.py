from rest_framework import permissions

class ChatMessageOwnerPermissions(permissions.BasePermission):

    def has_object_permission(self, request, view, message):
        print view.action
        if not request.user.is_authenticated():
            return False
        else:
            # check if user is owner
            return request.user == message.sender or request.user == message.receiver