-- on crée la table et c'est cette table qu'on va utiliser --
create DATABASE DBApp 
default character set utf8
DEFAULT collate utf8_general_ci;
USE DBApp;

-- on fait du ménage pour être sur de pas avoir de conflit--
drop table if exists USERS;
drop table if exists PASSWORDS;
drop table if exists SPORTS_EVENTS;
drop table if exists SUBSCRIPTIONS;
drop table if exists MESSAGES;

-- la table des users--
CREATE TABLE USERS(
	USER_ID bigint(8) primary key,
	USERNAME varchar(32) not null,
	EMAIL varchar(32) not null unique,
	BRITHDATE date,
	PROFILE_PICTURE varchar(50) not null,
	DESCRIPTION varchar(2048),
	UNREAD_MSG boolean
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle du password --
CREATE TABLE PASSWORDS(
	EMAIL varchar(32) not null unique,
	foreign key(EMAIL) references USERS(EMAIL),
	primary key(EMAIL),
	PASSWD varchar(131),
	ROLE varchar(100)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle des événements --
CREATE TABLE SPORTS_EVENTS(
	EVT_ID bigint(8) primary key,
	EVT_NAME varchar(32) not null,
	SPORT varchar(32)not null,
	EVT_DATE date not null,
	EVT_TIME time not null,
	ADRESS varchar(64) not null,
	LNG float(8) not null,
	LAT float(8) not null,
	DESCRIPTION varchar(2048),
	EVT_LEVEL int(1),
	MAX_SUBSCRIBERS int(4),
	EVALUATION int(1)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle des inscriptions, pour se créer un compte--
CREATE TABLE SUBSCRIPTIONS(
	USER_ID bigint(8),
	EVT_ID bigint(8),
	foreign key(USER_ID) references USERS(USER_ID),
	foreign key(EVT_ID) references SPORTS_EVENTS(EVT_ID),
	constraint pk_SUBSCRIPTIONS primary key (USER_ID,EVT_ID)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle des messages --
CREATE TABLE MESSAGES(
	SENDER bigint(8),
	RECEIVER bigint(8),
	foreign key(SENDER) references USERS(USER_ID),
	foreign key(RECEIVER) references USERS(USER_ID),
	MSG_ID bigint(10) primary key,
	MSG_TEXT varchar(2048) not null,
	MSG_TIME time not null,
	MSG_DATE date not null
)CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE INVITATIONS(
	USER_ID bigint(8),
	EVT_ID bigint(8),
	foreign key(USER_ID) references USERS(USER_ID),
	foreign key(EVT_ID) references SPORTS_EVENTS(EVT_ID),
	constraint pk_INVITATIONS primary key (USER_ID,EVT_ID)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- on ajoute un foreign key pour dire qui est l'user qui a crée l'event--
alter table SPORTS_EVENTS ADD EVT_OWNER bigint(8);
alter table SPORTS_EVENTS add foreign key(EVT_OWNER) references USERS(USER_ID);


create DATABASE DBAppTest 
default character set utf8
DEFAULT COLLATE utf8_general_ci;
USE DBAppTest;

-- on fait du ménage pour être sur de pas avoir de conflit--
drop table if exists USERS;
drop table if exists PASSWORDS;
drop table if exists SPORTS_EVENTS;
drop table if exists SUBSCRIPTIONS;
drop table if exists MESSAGES;

-- la table des users--
CREATE TABLE USERS(
	USER_ID bigint(8) primary key,
	USERNAME varchar(32) not null,
	EMAIL varchar(32) not null unique,
	BRITHDATE date,
	PROFILE_PICTURE varchar(50) not null,
	DESCRIPTION varchar(2048),
	UNREAD_MSG boolean
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle du password --
CREATE TABLE PASSWORDS(
	EMAIL varchar(32) not null unique,
	foreign key(EMAIL) references USERS(EMAIL),
	primary key(EMAIL),
	PASSWD varchar(131),
	ROLE varchar(100)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle des événements --
CREATE TABLE SPORTS_EVENTS(
	EVT_ID bigint(8) primary key,
	EVT_NAME varchar(32) not null,
	SPORT varchar(32)not null,
	EVT_DATE date not null,
	EVT_TIME time not null,
	ADRESS varchar(64) not null,
	LNG float(8) not null,
	LAT float(8) not null,
	DESCRIPTION varchar(2048),
	EVT_LEVEL int(1),
	MAX_SUBSCRIBERS int(4),
	EVALUATION int(1)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle des inscriptions, pour se créer un compte--
CREATE TABLE SUBSCRIPTIONS(
	USER_ID bigint(8),
	EVT_ID bigint(8),
	foreign key(USER_ID) references USERS(USER_ID),
	foreign key(EVT_ID) references SPORTS_EVENTS(EVT_ID),
	constraint pk_SUBSCRIPTIONS primary key (USER_ID,EVT_ID)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- celle des messages --
CREATE TABLE MESSAGES(
	SENDER bigint(8),
	RECEIVER bigint(8),
	foreign key(SENDER) references USERS(USER_ID),
	foreign key(RECEIVER) references USERS(USER_ID),
	MSG_ID bigint(10) primary key,
	MSG_TEXT varchar(2048) not null,
	MSG_TIME time not null,
	MSG_DATE date not null
)CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE INVITATIONS(
	USER_ID bigint(8),
	EVT_ID bigint(8),
	foreign key(USER_ID) references USERS(USER_ID),
	foreign key(EVT_ID) references SPORTS_EVENTS(EVT_ID),
	constraint pk_INVITATIONS primary key (USER_ID,EVT_ID)
)CHARACTER SET utf8 COLLATE utf8_general_ci;

-- on ajoute un foreign key pour dire qui est l'user qui a crée l'event--
alter table SPORTS_EVENTS ADD EVT_OWNER bigint(8);
alter table SPORTS_EVENTS add foreign key(EVT_OWNER) references USERS(USER_ID);
