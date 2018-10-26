# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-08-22 00:05
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0006_auto_20160822_0004'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'00e39c67-27cd-413e-838f-3175876a8287', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'4313ba7b-07a3-4a19-9c79-99df8f79d19c', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 8, 24, 0, 5, 38, 610691)),
        ),
    ]
