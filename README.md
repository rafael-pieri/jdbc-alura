[![Build Status](https://travis-ci.com/rafael-pieri/jdbc-alura.svg?branch=master)](https://travis-ci.com/rafael-pieri/jdbc-alura)

## Java and JDBC: Working with a Database
Java Database Connectivity (JDBC) is an application programming interface (API) for the programming language Java, 
which defines how a client may access a database. It is a Java-based data access technology used for Java database connectivity. 
It is part of the Java Standard Edition platform, from Oracle Corporation. It provides methods to query and update data in a database, 
and is oriented towards relational databases.

This repository contains some examples of how to work with a database using Java and JDBC. Below are listed the topics covered:
* Inserting and removing data from database;
* SQL Injection and Prepared Statement;
* Transaction: commit and rollback;
* Connection Pool and Datasources;
* Isolating data access and DAO (Data Access Object);
* Relationships, optimizing N+1 and ORM tools.

### How to run all tests successfully
Execute the following command to up a MySQL instance:

```docker run -d -p 3306:3306 --name mysql-jdbc-alura -e MYSQL_ROOT_PASSWORD=root rafaelpieri/jdbc-alura```

Once the database is up and running, execute the following command to run the automatic tests:
```mvn test```

#### How to build MySQL docker image locally
Go to docker/mysql folder and execute the following command:

```docker build -t mysql-jdbc-alura .```
