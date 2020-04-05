drop table articles if exists;

create table articles (

url VARCHAR(1000) not null primary key,
title VARCHAR(1000) NOT NULL,
language VARCHAR(2) NOT NULL,
sourcecountry VARCHAR(2) NOT NULL,
domain VARCHAR(2) NOT NULL,
seendate VARCHAR(20) NOT NULL

);