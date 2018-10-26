# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-09-10 00:12
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('appointments', '0005_doctorlegalprofile'),
    ]

    operations = [
        migrations.RenameField(
            model_name='doctorlegalprofile',
            old_name='verified',
            new_name='cmc_id_verified',
        ),
        migrations.AddField(
            model_name='doctorlegalprofile',
            name='degree_certificate_verified',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='doctorlegalprofile',
            name='diploma_verified',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='doctorlegalprofile',
            name='liability_policy_verified',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='doctorlegalprofile',
            name='rethus_register_verified',
            field=models.BooleanField(default=False),
        ),
        migrations.AddField(
            model_name='doctorlegalprofile',
            name='social_work_certificate_verified',
            field=models.BooleanField(default=False),
        ),
    ]