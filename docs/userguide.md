# Jara3 User Guide

<br>

## Run locally
* Run these commands in separate terminal windows:
    *  ``` make start_eureka_locally ```
    *  ``` make start_user_service_locally ```
    *  ``` make start_frontend_locally ```

<br>

## Set up MySQL locally
* ``` mysqld ```
* ``` mysql -u root ```
* ``` create database jara3_db; ```
* ``` create user 'jara3-dev'@'%' identified by 'password'; ```
* ``` grant all on jara3_db.* to 'jara3-dev'@'%'; ```
    
