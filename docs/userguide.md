# Jara3 User Guide

<br>
<br>

## Run locally
* Run these commands in separate terminal windows:
    *  ``` make start_eureka_local ```
    *  ``` make start_user_local ```
    *  ``` make start_web_app_local ```

<br>
<br>

## MySQL

### Set up local database
* ``` mysqld ```
* ``` mysql -u root ```
* ``` create database jara3_db; ```
* ``` create user 'jara3-dev'@'%' identified by 'password'; ```
* ``` grant all on jara3_db.* to 'jara3-dev'@'%'; ```

### View all local users
* ``` mysql -u root ```
* ``` use jara3_db; ```
* ``` select * from j3_user; ```
* ``` quit ```