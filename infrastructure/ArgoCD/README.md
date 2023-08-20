# Deploying and configuring an ArgoCD Helm chart 


### Deploy ArgoCD using default values
To deploy ArgoCD using default values, run the following commands in a terminal:
```
helm repo add argo https://argoproj.github.io/argo-helm
helm repo update
helm install argo argo/argo-cd --version <version> -n <namespace>
```
### Connect to your repository and deploy the application

1. Navigate to 'Settings -> Repositories -> Connect Repo' and fill in your repository details and connect
2. Once connected, Click the 3 dots near the repository and click "Create Application"  
3. Fill in your application details and click Create  
4. Once created, click the "Sync". A window will open. Click "Synchronize" and the application will deploy



