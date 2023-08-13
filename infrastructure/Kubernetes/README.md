# Kubernetes

## AKS cluster

```
    $ az login (make sure you are logged in with Microsoft edge)
    
    $ az aks install-cli
    
    $ az aks get-credentials --resource-group (for example- "rg-final-project-westEurope") --name (for examle- "aks-final-project-westEurope")
    
    $ kubectl config get-contexts
```
 


## Kind cluster

### make sure you have these installed:
1. Docker
2. Kubectl
3. Kind


    $kubectl apply -f multi-node-cluster.yaml -> [config example](multi-node-cluster.yaml)
    
    $ kubectl apply -f namespaces.yaml -> [config namespaces](namespaces.yaml)




## Deploy ingress 

```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
    
helm repo update
    
helm install my-ingress ingress-nginx/ingress-nginx -n (namespace)

kubectl apply -f ingress.yaml -n (namespace) -> [ingress file](ingress.yaml)
```

### if you want to add suffix
1. change namespaces name to accept variables -> (namespace){{ .Values.namespaces.suffix }}
2. add valus yaml -> namespaces: suffix: ".liorns"
- you can add whatever suffix you like 
    
