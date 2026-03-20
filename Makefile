# =============================================
# API Test Framework — Make Commands
# =============================================

# Default — prikaži pomoć
help:
	@echo "Available commands:"
	@echo "  make test-integration  → Run integration tests locally"
	@echo "  make test-mock         → Run mock tests locally"
	@echo "  make test-keycloak     → Run keycloak tests locally"
	@echo "  make docker-integration → Run integration tests in Docker"
	@echo "  make docker-mock        → Run mock tests in Docker"
	@echo "  make docker-keycloak    → Run keycloak tests in Docker"
	@echo "  make report             → Generate and open Allure report"
	@echo "  make clean              → Clean target folder"
	@echo "  make docker-clean       → Clean all Docker resources"

# =============================================
# LOCAL — bez Dockera
# =============================================
test-integration:
	mvn test -Pintegration

test-mock:
	mvn test -Pmock

test-keycloak:
	mvn test -Pkeycloak

# =============================================
# DOCKER
# =============================================
docker-integration:
	docker-compose up --build

docker-mock:
	docker-compose -f docker-compose-mock.yml up --build

docker-keycloak:
	docker-compose -f docker-compose-keycloak.yml up --build

# =============================================
# REPORTING
# =============================================
report:
	mvn allure:serve

report-generate:
	mvn allure:report

# =============================================
# CLEANUP
# =============================================
clean:
	mvn clean

docker-clean:
	docker-compose down --rmi all -v
	docker system prune -f

docker-stop:
	docker-compose down
	docker-compose -f docker-compose-mock.yml down
	docker-compose -f docker-compose-keycloak.yml down