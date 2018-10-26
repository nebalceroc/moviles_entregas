# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-08-21 23:48
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'5f701e78-6545-4aae-b7cf-9d6d6cede2b1', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'cfe8e261-be8c-411a-b1eb-c5e8bf50f071', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 8, 23, 23, 48, 24, 447750)),
        ),
    ]
