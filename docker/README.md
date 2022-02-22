```shell
sudo docker build -t yiluxiangbei/kg-python:v1 -f Dockerfile.python .
sudo docker run -it yiluxiangbei/kg-python:v1 bash

sudo docker push yiluxiangbei/kg-python:v1

docker rmi `docker images | grep none | awk '{print $3}'`
```