# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-09-10 00:12
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0018_auto_20160907_1447'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'0f7c81e1-0bce-450a-a0a3-6eabacad8075', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'25a72394-1e1c-451c-8e0a-874a1179e492', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 9, 12, 0, 12, 21, 533240)),
        ),
    ]
