# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-10-22 22:31
from __future__ import unicode_literals

import datetime
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('account', '0026_auto_20161012_0727'),
    ]

    operations = [
        migrations.AlterField(
            model_name='modelpasswordresetrequest',
            name='key',
            field=models.CharField(default=b'c844d21a-78ce-4fcd-8d00-3bc7fc29d57f', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key',
            field=models.CharField(default=b'0df65e36-9598-4d57-a302-09dcdd4475b2', max_length=50),
        ),
        migrations.AlterField(
            model_name='userprofile',
            name='key_expires',
            field=models.DateTimeField(default=datetime.datetime(2016, 10, 24, 22, 31, 1, 754830)),
        ),
    ]
