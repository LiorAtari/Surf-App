# Kubernetes
Below are two options to deploy a Kubernetes cluster -  
1. Using Azure AKS
2. Using Kind (Kubernetes in Docker container)
### AKS cluster
To login to Azure and set the AKS cluster as your Kubernetes context, run the following (Replace <> with your values)
```
az login

az aks install-cli

az aks get-credentials --resource-group <resource group name> --name <AKS cluster name>

kubectl config get-contexts
```
 


## Kind cluster
### Make sure you have these installed:
1. Docker
2. Kubectl
3. Kind
You can use these files as examples - [multi-node-cluster.yaml](multi-node-cluster.yaml), [namespaces.yaml](namespaces.yaml)
```
kubectl apply -f multi-node-cluster.yaml
    
kubectl apply -f namespaces.yaml
```



## Deploy ingress 
Example [ingress.yaml](ingress.yaml)
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
    
helm repo update
    
helm install my-ingress ingress-nginx/ingress-nginx -n (namespace)

kubectl apply -f ingress.yaml -n (namespace)
```

### If you want to add a suffix
1. change namespaces name to accept variables -> (namespace){{ .Values.namespaces.suffix }}
2. add the value to the values.yaml
```
namespaces:
  suffix: ".liorns"
```
You can add whatever suffix you like 
    
