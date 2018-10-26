# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-10-29 16:53
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('help', '0002_ticket_time_created'),
    ]

    operations = [
        migrations.AddField(
            model_name='ticket',
            name='title',
            field=models.TextField(default=b'empty', max_length=30),
        ),
        migrations.AlterField(
            model_name='ticket',
            name='kind',
            field=models.CharField(choices=[(b'0', b'general'), (b'1', b'appointment'), (b'2', b'payment'), (b'3', b'chat '), (b'4', b'user ')], default=b'0', max_length=1),
        ),
    ]