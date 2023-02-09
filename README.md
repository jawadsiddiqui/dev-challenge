## Operation Guide

### cURL commands against API EndPoints

#### /currency

curl --location --request GET 'http://localhost:8080/api/currencies' \
--header 'apiKey: 4FHm2zBjR3pnx60ygTOQye3l9P3biwe4'

#### /convert

curl --location --request GET 'http://localhost:8080/api/convert?amount=53&from=EUR&to=USD' \
--header 'apiKey: 4FHm2zBjR3pnx60ygTOQye3l9P3biwe4'

#### /rates
curl --location --request GET 'http://localhost:8080/api/rates?date=2023-02-09&base=EUR&target=USD' \
--header 'apiKey: 4FHm2zBjR3pnx60ygTOQye3l9P3biwe4'

#### /actuator
curl --location --request GET 'http://localhost:8080/actuator/health'
