#-------------------------------------------------------------------------------
# Alert
#-------------------------------------------------------------------------------
class Alert():
    """
    This class is used to storing the state of an alert message, shown on the
    front end of a page.
    """
    
    def __init__(self, msg, alert_type, icon="warning"):
        self.msg = msg
        self.alert_type = alert_type
        self.icon = "warning"
#-------------------------------------------------------------------------------
# get_alert
#-------------------------------------------------------------------------------
def get_alert(request):
    """
    This method is a utility method for reading the alert response in a 
    session, if any, then reset the message or return None.
    """
    
    try:
        alert, request.session['alert'] = request.session['alert'], None
    except KeyError:
        request.session['alert'], alert = None, None
        
    return alert  
#-------------------------------------------------------------------------------
# set_alert
#-------------------------------------------------------------------------------
def set_alert(request, msg, alert_type):
    """
    This method is a used to set the alert response in the current session
    of the user.
    """
    
    request.session['alert'] = Alert(msg, alert_type)
    
    return request.session['alert']
#-------------------------------------------------------------------------------
