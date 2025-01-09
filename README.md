[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/ori1I0wD)

### Swagger

Após iniciar a aplicação é possível utilizar o swagger em: http://localhost:8080/swagger-ui/swagger-ui/index.html.

  ### Docker e Docker-compose
  ```bash
  $ mvn clan package #gerar o executável JAR
  $ docker-compose up mongo_db --build #subir o container com o banco mongo
  $ docker-compose up mongo_app --build #suvir o container com a aplicação
  ```
