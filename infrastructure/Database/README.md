# PostgreSQL installation guide using Helm
### NOTE:
if you attempted to install Postgresql before, you will need to delete the pvc  
which was created with Postgres before re-installing. To do so, use the following command:
```
kubectl delete pvc --namespace [namespace] [pvc_name]
```

To install Postgresql using helm, run the following commands:
```
helm repo add bitnami https://charts.bitnami.com/bitnami

helm repo update

helm install my-postgresql bitnami/postgresql --version 12.7.1
```
To get the "postgres" user's password, run the following command (Change the values in [] brackets to match your environment):
```
kubectl get secret --namespace [namespace name] [service name] -o jsonpath="{.data.postgres-password}"
```
Next, connect to the server and run the psql command
```
kubectl exec -it [server pod name] --namespace [namespace name] -- /opt/bitnami/scripts/postgresql/entrypoint.sh /bin/bash

psql 
```

#### To use custom values in the deployment of PostgreSQL:
1. Create a values.yaml file
2. insert the desired values into the file
3. run the following command:
    ```
    helm install postgresql -f values.yaml bitnami/postgresql --namespace [namespace]
    ```
For example -  

the [postgres-values.yaml](https://gitlab.com/sela-1090/students/lioratari/infrastructure_sg/database/-/blob/main/postgres-values.yaml?ref_type=heads) file can be used to deploy PostgreSQL the following values automatically:  
- Admin user's password set to "password"  
- A database called "surfdb"  
- Create a user called "postgresdb" with a password of "password"  
