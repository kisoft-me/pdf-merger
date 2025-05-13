DOCKER_REPO_NAME=kisoft/pdf-merger
DOCKER_TAG=latest

build:
	mvn clean install
	docker build --quiet -t $(DOCKER_REPO_NAME):$(DOCKER_TAG) .

push:
	docker push --quiet  $(DOCKER_REPO_NAME):$(DOCKER_TAG) 

run: build
	docker run -it -p 7000:7000 $(DOCKER_REPO_NAME):$(DOCKER_TAG)

ci: build push
