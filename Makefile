
start_eureka_locally:
	cd eureka-server ; ./mvnw spring-boot:run
start_user_locally:
	# cd user-service ; ./mvnw clean test &
	cd user-service ; export EUREKA_URI=http://localhost:8761/eureka ; ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
start_web_app_locally:
	cd web-app ; ng serve

start_jara3_locally:
	make start_eureka_locally & sleep 8
	make start_user_locally &
	make start_web_app_locally &
stop_jara3_locally:
	# Eureka
	lsof -nti:8761 | xargs kill -9
	# User
	lsof -nti:8772 | xargs kill -9
	# Web App
	lsof -nti:4200 | xargs kill -9 