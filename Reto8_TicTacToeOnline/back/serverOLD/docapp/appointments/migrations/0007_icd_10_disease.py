# -*- coding: utf-8 -*-
# Generated by Django 1.9.5 on 2016-10-22 22:31
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('appointments', '0006_auto_20160910_0012'),
    ]

    operations = [
        migrations.CreateModel(
            name='ICD_10_Disease',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('classification', models.IntegerField(default=0)),
                ('place', models.CharField(default=b'', max_length=1)),
                ('reg_type', models.CharField(default=b'', max_length=1)),
                ('ch_number', models.IntegerField(default=0)),
                ('code3', models.CharField(default=b'', max_length=3)),
                ('code6dot', models.CharField(default=b'', max_length=7)),
                ('code6dot2', models.CharField(default=b'', max_length=7)),
                ('code6', models.CharField(default=b'', max_length=6)),
                ('title', models.CharField(default=b'', max_length=200)),
                ('m1', models.CharField(default=b'', max_length=5)),
                ('m2', models.CharField(default=b'', max_length=5)),
                ('m3', models.CharField(default=b'', max_length=5)),
                ('m4', models.CharField(default=b'', max_length=5)),
                ('m5', models.CharField(default=b'', max_length=5)),
            ],
            options={
                'verbose_name': 'ICD_10_Disease',
                'verbose_name_plural': 'ICD_10_Disease',
            },
        ),
    ]
