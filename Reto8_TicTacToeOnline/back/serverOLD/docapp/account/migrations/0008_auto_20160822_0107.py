# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-08-22 01:07
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0007_auto_20160822_0005'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'2ed65a61-c413-4f81-a26f-72e01fb3d830', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'4fde01ea-aa29-4c11-a728-6e97cd48dc1b', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 8, 24, 1, 7, 29, 768625)),
        ),
    ]
