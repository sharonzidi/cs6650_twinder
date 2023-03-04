### Learner Lab
https://awsacademy.instructure.com/courses/36197/modules/items/3082081

### RabbitMQ EC2
```
ssh -i key.pem ec2-user@54.149.172.209
```

### Install docker on EC2
https://serverfault.com/questions/836198/how-to-install-docker-on-aws-ec2-instance-with-ami-ce-ee-update

### Run RabbitMQ docker container

```
docker run --detach --rm --name rabbitmq -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin-user -e RABBITMQ_DEFAULT_PASS=admin-password rabbitmq:3.11-management
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