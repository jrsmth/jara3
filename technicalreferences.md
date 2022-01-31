# Jara3 Technical References

<br>

## Project Tooling
* Main Development Languages
    * Java [v15](https://www.oracle.com/java/technologies/downloads/#jdk15)
    * JavaScript X
* Build Tools:
    * Maven [v3.6.3](https://maven.apache.org/install.html)
    * NPM [v8.4.0](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
* Frameworks:
    * Spring Boot [v2.6.3](https://start.spring.io)
    * Node JS [v14.15.1](https://nodejs.org/en/download/)
    * React JS X
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
    * ...
    * ...

<br>

## Project Metadata
* Base Package Naming Convention
    * com.jrsmiffy.jara3

<br>

## Useful Commands
* Switch Java versions ([source](https://stackoverflow.com/questions/21964709/how-to-set-or-change-the-default-java-jdk-version-on-macos))
    * ``` /usr/libexec/java_home -V ```
    * ``` export JAVA_HOME=`/usr/libexec/java_home -v 17.0.1` ```
* Kill a service running on a particular $PORT
    * ``` lsof -nti:$PORT | xargs kill -9 ```
* Remove files that are still tracked after adding them to .gitignore ([source](https://stackoverflow.com/questions/11451535/gitignore-is-ignored-by-git))
    * ``` git rm -r --cached . ```
    * ``` git add . ```
    * ``` git commit -m "fixed untracked files" ```

<br>

## Further Documentation
### Git Branching Strategy
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
    * checkout a `feature` branch from `develop` (feature/ticket-reference)
    * build the feature (commit often)
    * create a PR - review and merge the `feature` branch into `develop`
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
        * ~~deploy to dev environment~~ (single environment only)
    * main
        * unit tests
        * build images and push to registry
        * deploy to production environment

inspired by: https://nvie.com/posts/a-successful-git-branching-model/


### This and That
* this and that

<br>

 

