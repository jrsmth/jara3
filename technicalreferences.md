# Jara3 Technical References

<br>

## Project Tooling
* Main Development Languages
    * [Java X]()
    * JavaScript X
* Build Tools:
    * Maven X
    * NPM X
* Frameworks:
    * Spring Boot X
    * Node JS X
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

## Frequently Used Commands
* 

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

<br>

### This and That

<br>

 

