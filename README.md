# mServices


#start mysql containter
docker run -p3306:3306 --name mysqldb -e MYSQL_ROOT_PASSWORD=test -d mysql:latest 


#open bash to running container
docker run -i -t mysql:latest /bin/bash


#connect to docker mysql from another docker mysqlclient
docker run -it --link mysqldb:mysql --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -ptest'

#start springboot app
mvn clean package && java -jar target/demo-0.0.1-SNAPSHOT.jar

#connect to docker mysql from docker-compose
docker-compose exec mysql sh -c 'exec mysql -uroot -ptest'


#build springboot docker image
./mvnw install dockerfile:build