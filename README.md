# store-fb-photos
Demo for RH Easy

Task can be found in Wiki page of this repository.

Spring boot Security:
user: user
password: password

Spring boot (security, web, thymeleaf, test, jpa)
Lombok
FB JavaScript SDK
Hibernate (in-memory H2 database)

Start Database:
docker run --name demo-database -e POSTGRES_USER=demo.user -e POSTGRES_DB=demo-database -e POSTGRES_PASSWORD=demo.password -p 5432:5432 -d postgres:9.5.4
docker start demo-database

Database login:
user: demo.user
password: demo.password