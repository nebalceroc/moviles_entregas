from docapp.account.libs.alert import get_alert

#-------------------------------------------------------------------------------
# alerts
#-------------------------------------------------------------------------------
def alerts(request):
    """
    This context processor method is used to add a global alert object to the 
    context. 
    """
    
    return {'alert' : get_alert(request)} 
