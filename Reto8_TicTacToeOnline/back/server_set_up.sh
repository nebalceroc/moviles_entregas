#uwsgi --ini /etc/uwsgi.ini &
uwsgi --socket mysite.sock --wsgi-file test.py
