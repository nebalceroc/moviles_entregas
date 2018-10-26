from django.core.mail.message import EmailMultiAlternatives
from django.template.context import Context

import os
import re
import subprocess

DEFAULT_FROM_EMAIL=""
#-------------------------------------------------------------------------------
# write_file_to_disk_in_chunks
#-------------------------------------------------------------------------------
def write_file_to_disk_in_chunks(filepath, infile, operation_flag='wb+'):
    """
    This method is used to write a file to disk in chunks
    """
    
    with open(filepath, operation_flag) as destination:
        for chunk in infile.chunks():
            destination.write(chunk)
#-------------------------------------------------------------------------------
# write_file_to_disk
#-------------------------------------------------------------------------------
def write_file_to_disk(filepath, content, operation_flag='wb+'):
    """
    This method is used to write a file to disk.
    """
    
    with open(filepath, operation_flag) as destination:
        destination.write(content)
#-------------------------------------------------------------------------------
# delete_file_from_disk
#-------------------------------------------------------------------------------
def delete_file_from_disk(filepath):
    """
    This method is used to delete a file from the operating system.
    """

    try:
        os.remove(filepath)
    except OSError:
        pass
#-------------------------------------------------------------------------------
# get_extension
#-------------------------------------------------------------------------------
def get_extension(filename, default='unknown'):
    """
    This method is used to return the extension of a filename
    """
    
    extension = filename.split('.')[-1]
    
    if not extension:
        extension = default

    return extension
#-------------------------------------------------------------------------------
# send_email
#-------------------------------------------------------------------------------
def send_email(email_tag, context_data, recipients, from_email='', 
               return_msg=False):

        return ""   
#-------------------------------------------------------------------------------
# create_post_tree
#-------------------------------------------------------------------------------
def create_post_tree(post_data):
    """
    This method is used to convert a django post/get dictionary to a multi level 
    element dictionary, where in html elements are grouped for example:
    
    {option[1][name] : 'nav'
     option[1][id] : '2'
     option[1][age] : '29'}
    
    will be turned into:
    
    {'option' : { 1 : {'name' : 'nav', 'id' : 2, 'age' : 29} } }
    """
    tree_data = {}
    for key, value in post_data.items():
        if '[' in key:
            
            list_result = re.split(r'\[(.*?)\]', key)
            list_result = [item for item in list_result if item]
            
#             last_element = list_result[len(list_result)-1]
            
            def insert_children_keys(data, list_keys):
                for x, y in enumerate(list_keys):
                    copy_list = list_keys
                    copy_list.pop(x)
                    
                    if len(copy_list) == 0:
                        data[y] = value
                    try:
                        data[y] = insert_children_keys(data[y], copy_list)
                    except:
                        data.setdefault(y, insert_children_keys({}, copy_list))
                
                return data
            
            insert_children_keys(tree_data, list_result)
            
        else:
            tree_data[key] = value
        
    return tree_data    

#-------------------------------------------------------------------------------
# get_stdout
#-------------------------------------------------------------------------------
def get_stdout(cmd):
    """
    This method is used to get output from the stdout of a shell. 
    """
    
    p = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, 
                         stderr=subprocess.STDOUT)
    stdout = []
    while True:
        line = p.stdout.readline()
        stdout.append(line)
        if line == '' and p.poll() != None:
            break
    return ''.join(stdout)