```shell
yum install python3
python3 KBQA/KBQA/KBQA_backend.py
sudo pip3 install flask
sudo pip3 install py2neo
sudo yum install python3-devel
sudo pip3 install pyahocorasick -i https://pypi.tuna.tsinghua.edu.cn/simple/

sudo docker-compose ps
sudo docker exec -it docker_neo4jv3_1 bash
ls neo4j/data/
ls neo4j/logs/
sudo chown -R 100:100 neo4j/logs/
sudo docker-compose stop neo4jv3
sudo docker-compose start neo4jv3
sudo docker-compose logs -f neo4jv3

sudo docker-compose ps

sudo docker-compose stop kg-python
sudo docker-compose start kg-python

sudo docker-compose logs -f

kg
mysql -h127.0.0.1 -P3310 -uroot -p
root
CREATE DATABASE IF NOT EXISTS `kg` DEFAULT CHARSET utf8mb4;

mysql -h127.0.0.1 -P3310 -uroot -p kg < backend/web/src/main/resources/SQLScripts/graph_layout.sql
mysql -h127.0.0.1 -P3310 -uroot -p kg < backend/web/src/main/resources/SQLScripts/layout.sql
mysql -h127.0.0.1 -P3310 -uroot -p kg < backend/web/src/main/resources/SQLScripts/project.sql
mysql -h127.0.0.1 -P3310 -uroot -p kg < backend/web/src/main/resources/SQLScripts/t_user.sql

http://localhost:8080/#/
test
123456

cd backend
mvn clean package
mvn clean package -DskipTests

ssh-keygen -t rsa -C '1097692918@qq.com'

jar -vtf example.jar|grep public.crt
   392 Mon Jan 03 17:35:22 CST 2022 BOOT-INF/classes/licence/public.crt
   
jar -xvf example.jar BOOT-INF/classes/licence/public.crt
 inflated: BOOT-INF/classes/licence/public.crt
 
jar -uvf example.jar BOOT-INF/classes/licence/public.crt 
adding: BOOT-INF/classes/licence/public.crt(in = 392) (out= 325)(deflated 17%)
```

```
公钥 = 【MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5hLd9FryBcXdlKKGICd3/axQ85V5QmB/0P7a5KhZr0vJaGX+7YRJt4NYpH1+pEob0TkFaFXzYZSZIZa3R63tS1pWpvKSWdSEy1Spb9qBS1FMp0j8vhQN1ydFv1Fh3Ds6vqBoGYyvqmkRLworLDUiRWuEQqxNcsNjx2HMJnhpdxwIDAQAB】
MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5hLd9FryBcXdlKKGICd3/axQ85V5QmB/0P7a5KhZr0vJaGX+7YRJt4NYpH1+pEob0TkFaFXzYZSZIZa3R63tS1pWpvKSWdSEy1Spb9qBS1FMp0j8vhQN1ydFv1Fh3Ds6vqBoGYyvqmkRLworLDUiRWuEQqxNcsNjx2HMJnhpdxwIDAQAB

私钥 = 【MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALmEt30WvIFxd2UooYgJ3f9rFDzlXlCYH/Q/trkqFmvS8loZf7thEm3g1ikfX6kShvROQVoVfNhlJkhlrdHre1LWlam8pJZ1ITLVKlv2oFLUUynSPy+FA3XJ0W/UWHcOzq+oGgZjK+qaREvCissNSJFa4RCrE1yw2PHYcwmeGl3HAgMBAAECgYEAgcFvzQ/v/OFtztUiVdIA8brlRspusxQTlXRSyyPC1tuOIrKfAmIcz7loUQ7ei5Sny4xIbUeGMJxesFhdwOthLxdcM54/lLW5ZCukUdUeLhjec1zTwCYZ9P1Ihrt63HOFRIVymak3f+eIWi4vmlNghSxyUGmXz1TYj/hI8wzWeZECQQDt7O5o+tZDGNPTzb4ppSyeZcRClcaLnvenig7Jyi3EOfv/gydANcUex5Fr8AnCkzAPIF0R3jU1vBbc0KKfnX1dAkEAx5yYjYQMJIFrJB6PYjhlwsOQi+cKFnphHnvaxPWP+LVAEduxjcKonvMmwqDq+6163omeXYEDn02pqeZoWMlxcwJAIpy7Oi5ziSNNfZyKs4hB63EmkgEz9w/TO15MNHLjIY7F6C/uP9sSqB2kPC2ZXeMHtMuifnzzBLQuJ0V6wvmoSQJBAKtfzLGi7vHgkuXdvuhq1yMR1+XlJAoMY5lSaI607ThwFGPApH265B4jT+HFWjldxaGNsYNBoqSAfuu5P1kLCfUCQQCJOv9FKXh5uWNO+Rcg4l7xwpnfCWtZKqrEWDy0cCfllfUPs0t8uRNZPKuroVGrZKJuvSGvQwM5g2pTAwRuctpM】
MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALmEt30WvIFxd2UooYgJ3f9rFDzlXlCYH/Q/trkqFmvS8loZf7thEm3g1ikfX6kShvROQVoVfNhlJkhlrdHre1LWlam8pJZ1ITLVKlv2oFLUUynSPy+FA3XJ0W/UWHcOzq+oGgZjK+qaREvCissNSJFa4RCrE1yw2PHYcwmeGl3HAgMBAAECgYEAgcFvzQ/v/OFtztUiVdIA8brlRspusxQTlXRSyyPC1tuOIrKfAmIcz7loUQ7ei5Sny4xIbUeGMJxesFhdwOthLxdcM54/lLW5ZCukUdUeLhjec1zTwCYZ9P1Ihrt63HOFRIVymak3f+eIWi4vmlNghSxyUGmXz1TYj/hI8wzWeZECQQDt7O5o+tZDGNPTzb4ppSyeZcRClcaLnvenig7Jyi3EOfv/gydANcUex5Fr8AnCkzAPIF0R3jU1vBbc0KKfnX1dAkEAx5yYjYQMJIFrJB6PYjhlwsOQi+cKFnphHnvaxPWP+LVAEduxjcKonvMmwqDq+6163omeXYEDn02pqeZoWMlxcwJAIpy7Oi5ziSNNfZyKs4hB63EmkgEz9w/TO15MNHLjIY7F6C/uP9sSqB2kPC2ZXeMHtMuifnzzBLQuJ0V6wvmoSQJBAKtfzLGi7vHgkuXdvuhq1yMR1+XlJAoMY5lSaI607ThwFGPApH265B4jT+HFWjldxaGNsYNBoqSAfuu5P1kLCfUCQQCJOv9FKXh5uWNO+Rcg4l7xwpnfCWtZKqrEWDy0cCfllfUPs0t8uRNZPKuroVGrZKJuvSGvQwM5g2pTAwRuctpM
```

```
docker-compose redis

java RSA license
java ssh-keygen
java rsa 公钥 私钥
java Data must not be longer than 128 bytes
java rsa PKCS8EncodedKeySpec
rsa 减少私钥长度

https://www.cnblogs.com/isyaya/p/11073149.html
https://blog.csdn.net/fenfenguai/article/details/88286428
```
