# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-10-05 12:20
from __future__ import unicode_literals

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('help', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='ticket',
            name='time_created',
            field=models.DateTimeField(default=django.utils.timezone.now),
        ),
    ]
