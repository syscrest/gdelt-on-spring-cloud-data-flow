drop table articles if exists;

create table articles (

url VARCHAR(4000) not null primary key,
title TEXT NOT NULL,
language VARCHAR(100) NOT NULL,
sourcecountry VARCHAR(100) NOT NULL,
domain VARCHAR(1000) NOT NULL,
seendate VARCHAR(20) NOT NULL,
content TEXT NOT NULL

);