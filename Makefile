
start_eureka_locally:
	cd eurekaserver ; ./mvnw spring-boot:run
start_user_service_locally:
	cd userservice ; ./mvnw clean install &
	cd userservice ; export EUREKA_URI=http://localhost:8761/eureka ; ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
start_frontend_locally:
	cd frontend ; npm run build ; export PORT=8702 ; node app

start_jara3_locally:
	make start_eureka_locally & sleep 8
	make start_user_service_locally &
	make start_frontend_locally &
stop_jara3_locally:
	lsof -nti:8761 | xargs kill -9
	lsof -nti:8772 | xargs kill -9
	lsof -nti:8702 | xargs kill -9 