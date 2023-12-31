<img src="https://github.com/LiorAtari/Surf-App/blob/main/project_diagram.png?raw=true">

## Infrastructure 

- INF-01 - Kubernetes cluster establishment [link](https://github.com/LiorAtari/Surf-App/tree/main/infrastructure/Kubernetes)
- INF-02 - Jenkins server establishment
- INF-04 - ArgoCD solution establishment
- INF-05 - Prometheus and Grafana solutions establishment
- INF-06 - Create all needed namespaces and deploy an ingress
- INF-07 - PostgreSQL database establishment

## Application 

- APP-01 - The application is written in Python3
- APP-02 - Docker-application
- APP-03 - features unit-tests
- APP-04 - Database usage
- APP-05 - Web-frontend
- APP-06 - HELM-deployment

## Pipeline 

- PIPE-01 - feature-branch Jenkins CI-pipeline
- PIPE-02 - main-branch Jenkins CI-pipeline
- PIPE-03 - CD pipeline for each environment

## Bonuses

- BON-01 - Integration of  generative AI tool through an API. [link](https://github.com/LiorAtari/Surf-App/blob/main/AI_API.md)
- BON-02 - Implement a cluster-portal, which is a web-entry with links to all the CI/CD and monitoring services. 


# Steps to manually install the Surf-app application

To begin, clone this repository by running:
```
git clone https://github.com/LiorAtari/Surf-App.git
```

build the docker file using the following command:
```
cd application
docker build -t <your-docker-repo-name>:<version> .
```

To run the unit-tests, use:
```
pytest test_app.py
```

To build the Helm package, run:
```
helm package surf-booking-chart
```

```
docker login
docker push <your-docker-repo-name>:<version>
helm push surf-booking-chart.tgz
```
To deploy the application manually, run (replace <> in URL with your repository name):
```
helm install surf-app oci://registry-1.docker.io/<your-repo>/surf-booking-chart --version <desired version> --set postgresql.host=<DB IP> --set postgresql.database=<DB name> --set postgresql.password=<DB password> --set image.tag=<version>
```
Alternatively, you can create a custom values.yaml file as shown below and run (replace <> in URL with your repository name):  
```
surf-app oci://registry-1.docker.io/<your-repo>/surf-booking-chart --version <desired version> -f values.yaml
```
values.yaml
```
---
image:
  tag: "<version>"
postgresql:
  host: "<host>"
  password: "<password>"
  database: "<database>"
```

