/*CREATE TABLE IF NOT EXISTS `icd_10` (
`classification` int(2) DEFAULT NULL,
`place` varchar(1) DEFAULT NULL,
`reg_type` varchar(1) DEFAULT NULL,
`ch_number` int(2) DEFAULT NULL,
`code3` varchar(3) DEFAULT NULL,
`code6dot` varchar(6) DEFAULT NULL,
`code6dot2` varchar(5) DEFAULT NULL,
`code6` varchar(4) DEFAULT NULL,
`title` varchar(185) DEFAULT NULL,
`m1` varchar(5) DEFAULT NULL,
`m2` varchar(5) DEFAULT NULL,
`m3` varchar(5) DEFAULT NULL,
`m4` varchar(5) DEFAULT NULL,
`m5` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;*/




#El archivo codes.txt se bajo de la pakina oficial de la organizacion mundial de la salud	

#into table icd_10
#load data infile '/home/nicolas/Downloads/codes.txt'
load data infile '/var/lib/mysql-files/codes.txt'
into table appointments_icd_10_disease
fields terminated by ';'
enclosed by ';'
lines terminated by '\n'
(classification,place,reg_type,ch_number,code3,code6dot,code6dot2,code6,title,m1,m2,m3,m4,m5);