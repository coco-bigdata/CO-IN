```shell
sudo docker build -t yiluxiangbei/kg-python:v1 -f Dockerfile.python .
sudo docker run -it yiluxiangbei/kg-python:v1 bash

sudo docker push yiluxiangbei/kg-python:v1

docker rmi `docker images | grep none | awk '{print $3}'`

cd backend
mvn clean package
mvn clean package -DskipTests

cd docker
sudo docker-compose up kg-java
sudo docker-compose up -d kg-java
sudo docker-compose down
sudo docker-compose up
sudo docker-compose up -d

sudo docker-compose build
sudo docker-compose build kg-python
sudo docker-compose build kg-java

sudo docker-compose up neo4jv3
sudo docker-compose up -d neo4jv3
sudo docker-compose stop neo4jv3

chmod 777 neo4j/logs
docker-compose logs -f

sudo docker-compose up mysql80
sudo docker-compose up -d mysql80
sudo docker-compose stop mysql80

sudo docker-compose up redis
sudo docker-compose up -d redis
sudo docker-compose stop redis

sudo docker-compose up kg-python
sudo docker-compose up -d kg-python
sudo docker-compose stop kg-python

sudo docker-compose up kg-java
sudo docker-compose up -d kg-java
sudo docker-compose stop kg-java

mysql -h127.0.0.1 -uroot -p -P3310
root
create database kg default character set utf8mb4 collate utf8mb4_unicode_ci;

mysql -h127.0.0.1 -uroot -p -P3310 kg < ../backend/web/src/main/resources/SQLScripts/graph_layout.sql
mysql -h127.0.0.1 -uroot -p -P3310 kg < ../backend/web/src/main/resources/SQLScripts/layout.sql
mysql -h127.0.0.1 -uroot -p -P3310 kg < ../backend/web/src/main/resources/SQLScripts/project.sql
mysql -h127.0.0.1 -uroot -p -P3310 kg < ../backend/web/src/main/resources/SQLScripts/t_user.sql

sudo docker network create --subnet=172.20.0.0/16 docker-kg
```