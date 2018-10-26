# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-10-29 16:53
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0029_auto_20161028_2124'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'04895aa4-fed0-45e2-a6fe-4124d1d0f7ab', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'5a052d36-9c49-45d7-951e-1e587745584c', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 10, 31, 16, 53, 29, 240544)),
        ),
    ]