# RSO: Image metadata microservice

## Prerequisites

```bash
docker run -d --name pg-vreme2 -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=vreme -p 5432:5432 postgres:14
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar vreme-api-1.0.0-SNAPSHOT.jar
```
Available at: localhost:8084/v1/images

## Docker commands
```bash
docker build -t novaslika .   
docker images
docker run novaslika    
docker tag novaslika prporso/novaslika   
docker push prporso/novaslika  
```
```bash
docker network ls  
docker network rm rso
docker network create rso
docker run -d --name pg-vreme -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=vreme -p 5432:5432 --network rso postgres:13
docker inspect pg-vreme
docker run -p 8084:8084 --network rso -e KUMULUZEE_DATASOURCES0_CONNECTIONURL=jdbc:postgresql://pg-vreme:5432/vreme prporso/vreme:2022-11-14-12-45-13
```

## Kubernetes
```bash
kubectl version
kubectl --help
kubectl get nodes
kubectl create -f vreme-deployment.yaml 
kubectl apply -f vreme-deployment.yaml 
kubectl get services 
kubectl get deployments
kubectl get pods
kubectl logs vreme-deployment-6f59c5d96c-rjz46
kubectl delete pod vreme-deployment-6f59c5d96c-rjz46
```

Kubernetes secrets configuration: https://kubernetes.io/docs/tasks/configmap-secret/managing-secret-using-kubectl/

```bash
kubectl create secret generic pg-pass --from-literal=password=mypassword
kubectl get secrets
kubectl describe secret pg-pass
```


