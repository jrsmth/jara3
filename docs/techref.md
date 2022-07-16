# Jara3 Technical References

<br>

## Project Tooling
* Main Development Languages
    * Java [v17](https://www.oracle.com/java/technologies/downloads/#jdk17)
    * JavaScript
* Build Tools:
    * Maven [v3.6.3](https://maven.apache.org/install.html)
    * NPM [v8.4.0](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
    * Gradle
* Frameworks:
    * Spring Boot [v2.6.9](https://start.spring.io)
    * Node JS [v14.15.1](https://nodejs.org/en/download/)
        * Angular v12
* Kubernetes Version:
    * Client:
    * Server:
* Deployment Target:
    * GKE on GCP: details...
* Container Registry:
    * DockerHub (link)
    * GCR
* Logging
    * ...
    * ...
* Testing
    * Spock
    * JUnit 5
    * Geb, Selenium

<br>

## Project Metadata
* Base Package Naming Convention:
    * `com.jrsmiffy.jara3`

<br>

## Useful Commands
* Manage Java version with SDKMAN ([source](https://stackoverflow.com/questions/69875335/macos-how-to-install-java-17))
    * Install SDKMAN:
        * `curl -s "https://get.sdkman.io" | bash`
    * Install Java:
        * `sdk list java`
        * `sdk install java 17.0.3-tem`
    * Switch Java version
        * `sdk use java 17.0.3-tem`
    * Manage Java version manually (old)
        * [source](https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-macos)
    * Change Java version for IntelliJ Project 
        * [source](https://stackoverflow.com/questions/59180226/jdks-installed-with-sdkman-are-not-selectable-in-the-intellij-ide)
        * File > Project Structure (`cmd ;`)
* Kill a service running on a particular $PORT
    * ``` lsof -nti:$PORT | xargs kill -9 ```
* Remove files that are still tracked after adding them to .gitignore ([source](https://stackoverflow.com/questions/11451535/gitignore-is-ignored-by-git))
    * ``` git rm -r --cached . ```
    * ``` git add . ```
    * ``` git commit -m "fixed untracked files" ```
* Base64: encode and decode
    * ``` echo -n amFyYTMtZGV2 | base64 --d ```
    * ``` echo -n jara3-dev | base64 ```
* GKE login
    * ``` gcloud auth login ```
    * ``` gcloud container clusters get-credentials $CLUSTER --zone $ZONE --project $PROJECT ```
* Manually update an image on GCR
    * ``` docker build -t jrsmiffy/jara3 -f Dockerfile . ```
    * ``` docker tag jara3/user-service eu.gcr.io/jrsmiffy/jara3/user-service:latest ```
	* ``` docker push eu.gcr.io/jrsmiffy/jara3/user-service:latest ```

<br>

## Git Branching Strategy
* Overview
    * `master`
        * tracks production-ready state
    * `develop`
        * stages new features for deployment into production
    *  `feature` 
        * contains a stand-alone feature to be built (represents a jira-ticket)
    * `release`
        * take us from `develop` into `main` after final checks
    * `hot-fix`
        * contains a specific fix for a bug in production
* Flow
    * create a jira-ticket for a single feature or bug fix
        * example ticket: `JA3-101`
    * checkout a `feature` branch from `develop`
        * example branch: `[minor] JA3-101`
    * build the feature (commit often)
        * example commit: `[minor] JA3-101: updated docs`
    * create a PR
        * review and merge the `feature` branch into `develop`
    * after multiple features have been staged in `develop`, checkout a `release` branch (bump version and final checks)
    * raise a PR and merge the `release` branch into `main`
    * rebase `develop` from `main`
    * delete the redundant branches, rinse and repeat
        * for `hot-fix` branches: check out from `main`, bump version & fix and then merge them into `develop` and `main`
* GitHub Actions
    * feature
        * unit tests
    * develop
        * unit tests
    * main
        * unit tests
        * build images and push to registry
        * deploy to production environment

inspired by: https://nvie.com/posts/a-successful-git-branching-model/

<br>

## Jira Process
* All work *should* have an associated Jira ticket.
* Use Jira to evidence the work that has been done and to document the steps involved.
* Useful tips and tricks should be recorded in `techref.md` or TINTK.

<br>

## Release Process
* [release notes](../releasenotes.md)

<br>

## Coding Standards
* All functionality should be implemented following Top-Down TDD.
* As part of the TDD refactoring step or when reviewing a PR, check that coding standards are met (see TINTK).
* Set at least 15 mins aside to sit down and walk through a PR, before approving.


<br>

## Overcoming Obstacles
* **spring-data-jpa dependency issue**
    * make sure you the ```spring-boot-starter-data-jpa``` dependency and not the ```spring-data-jpa``` when connecting to a database, using a JPA repository.
        * I kept facing a ```Consider defining a bean named 'entityManagerFactory' in your configuration``` exception with the latter dependency.
        * source: [stack overflow](https://stackoverflow.com/questions/41170661/spring-data-jpa-consider-defining-a-bean-named-entitymanagerfactory-in-your/41178250)
* **Unit & Integration Tests Not Running with Maven**
    * Stick to using `JUnit5`.
        * Upgrading tests from `JUnit4` to `JUnit5` solved the issue of my `UserController` unit tests not working.
            * Watch out for accidental imports of `org.junit.test` (`JUnit4`) instead of `org.junit.juniper.api.test` (`JUnit5`).
    * Add the `maven-failsafe-plugin` to `pom.xml` to run integration tests.
        * When using the `*IT.java` naming convention, be sure to add `<include>**/*IT.java</include>` to the configuration.
        * `mvn clean test` will run unit tests only.
        * `mvn clean verify` will run both unit and integration tests.
        * source: [Geeky Hacker](https://www.geekyhacker.com/2020/07/11/run-integration-tests-with-maven-failsafe-plugin/)

