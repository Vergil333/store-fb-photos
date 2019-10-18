# Store FB user and Photos

Tasks:
- create functional REST API by [this task](https://github.com/Vergil333/store-fb-photos/wiki/Task)
- achieve functionality from previous task via web application (Work in progress)


Spring boot Security (basic auth):  
user: user  
password: password


What I used:  
- Spring boot (security, web, thymeleaf, test, crud)  
- Lombok  
- FB JavaScript SDK  
- Hibernate (in-memory H2 database)

**H2 database:**  
link: https://localhost:8090/h2-console/    
user: SA  
password: {empty}  

**FB Javascript SDK** requires website to be secured (HTTPS) so you have to import
self-signed certificate into your browser.  
Cert location: /resources/localhost.p12  
Cert password: changeit