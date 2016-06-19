#! /bin/bash

sh deleteAllDocker.sh

docker run --name mysql -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=admin1234 hallow/db:linux

echo -e "\n\t*****************************\n\t* Lancement du Docker Mysql *\n\t*****************************\n"

docker run --name mywidfly --link mysql:db -d -p 8080:8080 -p 8787:8787 -p 9990:9990  hallow/wildfly:linux

echo -e "\n\t*******************************\n\t* Lancement du Docker Wildfly *\n\t*******************************\n"


