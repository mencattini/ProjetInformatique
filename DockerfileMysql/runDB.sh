#! /bin/bash
docker run -p 3306:3306 -d -e MYSQL_ROOT_PASSWORD=admin1234 hallow/db:linux
