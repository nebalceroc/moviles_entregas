# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-10-05 12:19
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0023_auto_20161005_1218'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'd7656b0f-a9b4-49ab-919e-cf35cf594ebe', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'28b33b1d-da80-4863-a7a6-07abba22757e', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 10, 7, 12, 19, 28, 411846)),
        ),
    ]
