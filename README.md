# Steps to manually install the Surf-Booking app

To begin, clone this repository by running:
```
git clone https://github.com/LiorAtari/Surf-App.git
```

build the docker file using the following command:
```
cd application
docker build -t lioratari/surf-booking:<version> .
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
docker push lioratari/surf-booking:<version>
helm push surf-booking-chart.tgz
```
To deploy the application manually, run:
```
helm install surf-app oci://registry-1.docker.io/lioratari/surf-booking-chart --version <desired version> --set postgresql.host=<DB IP> --set postgresql.database=<DB name> --set postgresql.password=<DB password> --set image.tag=<version>
```
Alternativly, you can create a custom values.yaml file as shows below and run:  
```
surf-app oci://registry-1.docker.io/lioratari/surf-booking-chart --version <desired version> -f values.yaml
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

