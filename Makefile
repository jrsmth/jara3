

start_eureka_locally:
	cd eurekaserver ; ./mvnw spring-boot:run
start_user_service_locally:
	cd userservice ;  export EUREKA_URI=http://localhost:8761/eureka ; ./mvnw spring-boot:run 

start_jara3_locally:
	make start_eureka_locally & sleep 8
	make start_user_service_locally &
	# make start_xyz_service_locally