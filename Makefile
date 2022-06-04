
start_eureka_local:
	cd eureka-server ; ./mvnw spring-boot:run
start_user_local:
	cd user-service ; ./mvnw clean test &
	cd user-service ; export EUREKA_URI=http://localhost:8761/eureka ; ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
start_web_app_local:
	cd web-app ; npm run build ; export PORT=8702 ; node app

start_jara3_local:
	make start_eureka_local & sleep 8
	make start_user_local &
	make start_web_app_local &
stop_jara3_local:
	lsof -nti:8761 | xargs kill -9
	lsof -nti:8772 | xargs kill -9
	lsof -nti:8702 | xargs kill -9 