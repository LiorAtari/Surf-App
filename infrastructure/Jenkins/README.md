# Jenkins

 Setup Jenkins on Kubernetes cluster with Dynamic Agents / Pods

## Prerequisites
1. Kubernetes cluster
2. Helm client  
3. **Make sure your Kubernetes version is 1.24 and up**

### Adding the Jenkins repository to Helm  
```
helm repo add jenkins https://charts.jenkins.io
helm repo update
```
### Create a service account for Jenkins in the cluster  
You can use the following [serviceAccount.yaml](https://github.com/LiorAtari/Surf-App/blob/main/infrastructure/Jenkins/serviceAccount.yaml),
[jenkins-values.yaml](https://github.com/LiorAtari/Surf-App/blob/main/infrastructure/Jenkins/jenkins-values.yaml) files as an example
```
kubectl apply -f serviceAccount.yaml -n cicd 
helm install jenkins -n cicd -f jenkins-values.yaml jenkins/jenkins  
```
Get the admin password from the pod by running:  
```
kubectl exec --namespace cicd -it svc/jenkins -c jenkins -- /bin/cat /run/secrets/additional/chart-admin-password  
```
Use the following command to port-forward traffic to "localhost:8080":  
```
kubectl -n cicd port-forward <pod_name> 8080:8080
```
### Create a token for Jenkins to access the cluster
```
kubectl create token jenkins -n cicd
```
### Install Jenkins plugins using the "jenkins-values.yaml" file:

To install a set of plugins when Jenkins is deployed, 
1. find the names of the plugins you want to install (Google)
2. Place their names under the "installPlugins" line.  
  
Example - 
If you want to install the Kubernetes plugin when Jenkins deploys, your file would look as follows:

```
installPlugins:
    - kubernetes:3937.vd7b_82db_e347b_
```
3. Create a pipeline within Jenkins and build it on Kubernetes (Use this code as an [example](https://github.com/LiorAtari/Surf-App/blob/main/infrastructure/Jenkins/pipelineExemple))
4. Open Powershell and run:  
```
kubectl get pods -n cicd --watch
```  
- You will see the Dynamic agent pods deploy while the pipeline is running, and destroyed when the job is done

## Steps to customize your Jenkins Logo

Get your avatar logo in GitLab and upload it into the Jenkins repo  
after you uploaded, right-click on the image and select "copy image address"  
Install Simple Theme Plugin in Jenkins  
Go to Manage Jenkins → Configure System → Theme → Extra CSS  
paste this code and change the URL to the one you uploaded to GitLab (Replace <> below with the URL to your logo)  
```
/* Custom Jenkins Logo /
.logo {
/ Hide the default logo */
display: none;
}
.logo::before {
/* Set the custom logo image /
content: url("<URL-to-your-logo>");
/ Add any additional styles for the logo, if needed /
display: inline-block;
width: 50px; / Adjust the width to fit your logo's dimensions /
height: 50px; / Adjust the height to fit your logo's dimensions */
}
/* Custom Jenkins Name */
#jenkins-home-link {
/* Hide the default text */
display: none;
}
```

## Jenkins pipeline email on failure: 
1. In Jenkins go to Manage Jenkins > System and make these changes: 
- System Admin e-mail address: you can change it to be your email or a no-reply address
-  E-mail Notification: if you are facing any issues with inserting a password check [this](https://support.google.com/accounts/answer/185833#zippy=), you might need to create 2FA (Two-Factor Authentication)

2. In Jenkins create a new job > multibranch pipeline
3. In configuration insert the branch source (in this case: https://github.com/LiorAtari/Surf-App.git)
4. if you gave your Jenkinsfile a different name, specify it under Build Configuration > Script Path
5. you can choose the intervals you want under "Scan Multibranch Pipeline Triggers" (in this case - 1 minute)

### Insert this to your Jenkinsfile, this will trigger when the Jenkins jobs failed

```
    $ stages {
        stage('Hello') {
            steps {
                echo "Hello world"
                    }
            }
        }
        post{
            failure{
                mail to: "naivetechblog@gmail.com",
                subject: "jenkins build:${currentBuild.currentResult}: ${env.JOB_NAME}",
                body: "${currentBuild.currentResult}: Job ${env.JOB_NAME}\nMore Info can be found here: ${env.BUILD_URL}"
        }
    }
```
