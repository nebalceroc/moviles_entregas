FROM python:3.5
MAINTAINER Nicolas E. Balcero C. <nicolas.balcero.ceron@gmail.cmo>

# Install extra packages
RUN apt-get update && apt-get install -y curl git sudo
#RUN pip3 install virtualenv
#RUN virtualenv django
#RUN cd django
#RUN source bin/activate

RUN pip3 install --upgrade pip

RUN pip3 install mysqlclient
RUN pip3 install Django==2.1.2
RUN pip3 install django-filter==0.13.0
RUN pip3 install djangorestframework==3.9.0
RUN pip3 install drf-extensions==0.4.0
RUN pip3 install Markdown==3.0.1
RUN pip3 install wheel==0.32.2
RUN pip3 install Pillow==5.3.0
RUN pip3 install pyfcm==1.4.5
RUN pip3 install uwsgi==2.0.17.1


ENV NAME tictactoe
ENV USER nb
ENV PASSWORD 12345
ENV HOST mysql

# Volumes avalaible
# VOLUME /usr/src

COPY server /usr/src/server
ADD ./uwsgi.ini /etc/uwsgi.ini

EXPOSE 8085:8085
WORKDIR /usr/src/server

#RUN pip install -r /usr/src/gems-django/requeriments.txt

#FROM nginx:1.15

# Copy the default nginx.conf provided by tiangolo/node-frontend
#COPY ./robots_core_panel/robots-core-panel/nginx.conf /etc/nginx/conf.d/default.conf


CMD ["bash", "./server_set_up.sh"]
