USE DBApp;

insert into USERS values(1,"Romain Mencattini","mencat1907@gmail.com","1993-07-19","mon_image.jpg","Bonjour, je m\'appelle Romain et je suis le meilleur sportif admin système au monde :)",False);
insert into USERS values(2,"Aurelien Coet","blup@gmail.com","1900-01-28","son_image.jpg","Bonjour, je ne suis pas aurélien, mais j\' ai quand même créer un user",False);
insert into USERS values(3,"Joel Beny","jesaispas@outlook.ch","2002-07-01","l\'image de joel","blablabla",False);

insert into PASSWORDS values
 ("mencat1907@gmail.com",
sha2('romain93',512),
"user");

insert into PASSWORDS values
 ("blup@gmail.com",
sha2('aurelien93',512),
"user");

insert into PASSWORDS values 
("jesaispas@outlook.ch",
sha2('joel',512),
"user");

insert into SPORTS_EVENTS values (123,"Basket-ball","Basket-Ball","2016-05-12","17:22:03","21c rue de Chêne 1200 Genève",12.3,12.3,"Une super rencontre vous attends",5,20,0,1);
insert into SPORTS_EVENTS values (1234,"Hokey","Hokey","2016-10-12","12:00:00","12 routes de jussy",23.3,3.3,"Blablabla",6,12,0,2);

insert into SUBSCRIPTIONS values(1,123);
insert into SUBSCRIPTIONS values(2,123);
insert into SUBSCRIPTIONS values(3,123);
insert into SUBSCRIPTIONS values(2,1234);



USE DBAppTest;

insert into USERS values(1,"Romain Mencattini","mencat1907@gmail.com","1993-07-19","mon_image.jpg","Bonjour, je m\'appelle Romain et je suis le meilleur sportif admin système au monde :)",False);
insert into USERS values(2,"Aurelien Coet","blup@gmail.com","1900-01-28","son_image.jpg","BOnjour, je ne suis pas aurélien, mais j\' ai quand même créer un user",False);
insert into USERS values(3,"Joel Beny","jesaispas@outlook.ch","2002-07-01","l\'image de joel","blablabla",False);
insert into USERS values(4,"Romain Mencattini","ahaha@gmail.com","1993-07-19",".jpg","Description",False);

insert into PASSWORDS values
 ("mencat1907@gmail.com",
sha2('romain93',512),
"user");

insert into PASSWORDS values
 ("blup@gmail.com",
sha2('aurelien93',512),
"user");

insert into PASSWORDS values 
("jesaispas@outlook.ch",
sha2('joel',512),
"user");

insert into PASSWORDS values
	("ahaha@gmail.com", sha2('roromain',512), "user");

insert into SPORTS_EVENTS values (123,"Basket-ball","Basket-Ball","2016-05-12","17:22:03","21c rue de Chêne 1200 Genève",12.3,12.3,"Une super rencontre vous attends",5,20,0,1);
insert into SPORTS_EVENTS values (1234,"Hokey","Hokey","2016-10-12","12:00:00","12 routes de jussy",23.3,23.3,"Blablabla",6,12,0,2);
insert into SPORTS_EVENTS values (12,"Basket-ball","BB","2016-05-12","17:22:03","21c route de chêne",175.56,-39.26,"salut",6,12,0,2);

insert into SUBSCRIPTIONS values(1,123);
insert into SUBSCRIPTIONS values(2,123);
insert into SUBSCRIPTIONS values(3,123);
insert into SUBSCRIPTIONS values(2,1234);
insert into SUBSCRIPTIONS values(4,123);
insert into SUBSCRIPTIONS values(4,1234);
