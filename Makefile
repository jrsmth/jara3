

start_eureka_locally:
	cd eurekaserver ; ./mvnw spring-boot:run
start_user_service_locally:
	cd userservice ;  export EUREKA_URI=http://localhost:8761/eureka ; ./mvnw spring-boot:run 
start_frontend_locally:
	cd frontend ; node service.js

start_jara3_locally:
	make start_eureka_locally & sleep 8
	make start_user_service_locally &
	make start_frontend_locally &
	# make start_xyz_service_locally

end_frontend_locally:
	lsof -nti:8702 | xargs kill -9
	lsof -nti:8761 | xargs kill -9