### Learner Lab
https://awsacademy.instructure.com/courses/36197/modules/items/3082081

### RabbitMQ EC2
```bash
ssh -i key.pem ec2-user@54.149.172.209
```

### Install docker on EC2
https://serverfault.com/questions/836198/how-to-install-docker-on-aws-ec2-instance-with-ami-ce-ee-update

### Run RabbitMQ docker container

```
aKNlI4BwD#w74S#R9&KE
```

```
docker run --detach --rm --name rabbitmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER="admin-user" -e RABBITMQ_DEFAULT_PASS="aKNlI4BwD#w74S#R9&KE" rabbitmq:3.11-management
```

```
sudo service docker start
```

```
docker run -it --rm --name rabbitmq \
-p 5672:5672 \
-p 15672:15672 \
-e RABBITMQ_DEFAULT_USER=admin-user \
-e RABBITMQ_DEFAULT_PASS=admin-password \
rabbitmq:3.11-management
```

### Docker tips
https://typeofnan.dev/how-to-stop-all-docker-containers/





# Tomcat EC2

## Auto Start

* https://medium.com/@shrunk7byadagi/automatically-start-tomcat-on-instance-startup-reboot-in-amazon-ec2-ubuntu-instance-33849a9d9090

* https://serverfault.com/questions/345228/update-rc-d-on-amazon-linux

The Amazon Linux (which is essentially CentOS, which is essentially Red Hat) equivalent of update-rc.d is chkconfig. http://www.cyberciti.biz/faq/rhel5-update-rcd-command/

```
chkconfig --add tomcat
chkconfig tomcat on
```



```
cd /Users/sharonxia/github/cs6650_twinder

scp -i key.pem Assignment2/twinder_publisher_server_servlet/target/cs6650-server-1.0-SNAPSHOT.war ec2-user@35.89.5.25:/home/ec2-user/apache-tomcat/webapps/web-server.war
```



