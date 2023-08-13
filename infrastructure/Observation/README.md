# Observation

### Installation guide for Prometheus & Grafana

```
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm install prometheus prometheus-community/prometheus -n observation
```

To get the admin password for the Grafana server - 
```
kubectl get secret --namespace observation my-release-grafana -o jsonpath="{.data.admin-password}" | base64 --decode
```
