# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-09-06 12:43
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0016_auto_20160905_1510'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'df5dc683-93be-4ddc-bf4a-5f70593b3651', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'7a3f461b-25be-45f7-a1a2-a3abb2a31063', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 9, 8, 12, 43, 2, 235385)),
        ),
    ]