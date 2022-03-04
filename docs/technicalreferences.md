# Jara3 Technical References

<br>

## Project Tooling
* Main Development Languages
    * Java [v15](https://www.oracle.com/java/technologies/downloads/#jdk15)
    * JavaScript
* Build Tools:
    * Maven [v3.6.3](https://maven.apache.org/install.html)
    * NPM [v8.4.0](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
* Frameworks:
    * Spring Boot [v2.6.3](https://start.spring.io)
    * Node JS [v14.15.1](https://nodejs.org/en/download/)
        * React JS [v17.0.1](https://reactjs.org/docs/create-a-new-react-app.html)
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
* Change the port that your React app runs on ([source](https://scriptverse.academy/tutorials/reactjs-change-port-number.html))
    * ``` export PORT=8702 ```
    * ``` npm start ```
* Base64: encode and decode
    * ``` echo -n amFyYTMtZGV2 | base64 --d ```
    * ``` echo -n jara3-dev | base64 ```
* GKE login
    * ``` gcloud auth login ```
    * ``` gcloud container clusters get-credentials $CLUSTER --zone $ZONE --project $PROJECT ```
* Manually update an image on GCR
    * ``` docker build -t jrsmiffy/jara3 -f Dockerfile . ```
    * ``` docker tag jara3/userservice eu.gcr.io/jrsmiffy/jara3/userservice:latest ```
	* ``` docker push eu.gcr.io/jrsmiffy/jara3/userservice:latest ```

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
        * ~~deploy to dev environment~~ (prod environment only)
    * main
        * unit tests
        * build images and push to registry
        * deploy to production environment

inspired by: https://nvie.com/posts/a-successful-git-branching-model/


### Jira Process
TLDR - stop using Jira as a source of documentation, instead keep a good technicalreferences.md <br>
TLDR 2 - any documentation that you do want to add to the ticket, keep it in the description, as it gets preserved when exported. Use the comments to update on progress.
* When working on a ticket, keep a stringent checklist of what exactly needs to be done to complete it (in the description).
* Use the comments to evidence that the work has been completed, with accompanying media (screen shots, etc).
* Stop using Jira tickets to document how to do specific tasks; you will eventually lose access to those tickets and, retrospectively, the information is hard/impossible to pull out in bulk.
    * Instead, consistently add knowledge to techincalreferences.md - plus build up a great bank of private TINTK.
    * You can still put commands, links and details in the Jira comments (there's still some use in that). You can (and should) still keep a top-notch Jira but don't go overboard and invest the same effort into keeping personal documentation.

### Creating a React Frontend that is able to talk with Eureka
* React is a JavaScript library, not a framework. Hence the frontend application is really a Node.js application, as we needed an express server backend in order to communicate with Eureka and perform REST calls.
* During my research I came across many references to the use of Zuul as an API gateway, alongside Spring Eureka when setting up a polyglot microservices architecture for the cloud. I also came across the Netflix suggestion of using a spring boot sidecar alongside your non-JVM application when trying to register with Eureka. I also came across references to Ingress controllers and using NGINX as a reverse proxy. All of these things are new to me and maybe the official way of doing things. However I believe the solution I have gone for is sufficient and if there is a better way of doing things, I hope to learn it in the future.
* The solution I have gone for is inspired by two tutorials that I found:
    1. [Registering Node.js with Eureka](https://medium.com/@zilayhuda/register-nodejs-service-with-netflix-eureka-and-use-zuul-for-routing-service-part-1-e50fc49d1219)
        * https://github.com/zilehuda/eureka-zuul-nodejs-microservices
    2. [Packaging React inside Node.js](https://medium.com/geekculture/build-and-deploy-a-web-application-with-react-and-node-js-express-bce2c3cfec32)
        * https://github.com/leandroercoli/NodeReact
* With the first one, I pinched the ```eureka-helper.js``` file, which I then used to register the app with Eureka when Express server is created. It leaverages the [eureka-js-client](https://www.google.com/search?client=safari&rls=en&q=eureka-js-client&ie=UTF-8&oe=UTF-8).
* The second one allowed me to have a React client running inside my Node.js application that was then able to make use of the register information provided by Eureka in order to make calls to the backend micro-service (i.e the user-service).
* Steps to create the frontend:
    * ``` mkdir frontend```
    * ``` npm init -y ```
    * ``` npm install ```
    * ``` npm i express cors dotenv express-validator cross-fetch```
    * Add the ```scripts``` to ```package.json```
    * *optional*: Create .gitignore and .env 
    * Create ```/models``` with ```server.js```, ```eureka-helper.js``` and ```eureka-registry.js```
        * ```eureka-helper.js```: registers us to the Eureka server and fetches the registry containing the paths to the other services.
        * ```eureka-registry.js```: gets updated by ```eureka-helper.js``` to store and paths for the other services and exposes them to the client application through the controller.
    * Create ```app.js```
        * serves as the entry point to the application - spins us the Express server and registers us with Eureka.
    * Add the ```/controllers``` and ```/routes```
        * These controllers and routes are responsible for making the actual REST call. The flow is as follows:
            * Inside the Client React app, the a Component (such as Login) has a JS function that calls the index.js for the API's (also inside the Client).
            * The Client's ```/api/index.js``` calls the routes, which have been globally exported in the Node.js wrapper application.
            * The routes lead to the controller, which is responsible for making the actual call to Eureka, using the URL provided by ```eureka-registry.js```. It thens forwards the response back the Component that requested the call.
    * *optional*: Add ```/middleware``` for enhancing the REST call - for example, adding validation to input form
    * Create the React application (in ```/frontend```)
        * ``` npx create-react-app client ```
        * ``` cd client ```
        * ``` npm install ```
        * ``` npm install react-bootstrap bootstrap@5.1.3 ```
    * Replace ```App.js```.
    * Add the ```/src/api/index.js``` to make API calls to the Node.js controller.
    * Add the Components
* Notes:
    * How to Run: ``` node start ```
        * If you make a change to the React app, ``` npm run build ; node start ```

### Release Process
* Store the latest version of Jira and Miro in https://github.com/JRSmiffy/jara3/tree/main/docs

### Coding Standards
* When refactoring (at the end of TDD) or when reviewing code as part of a pull request, ensure the following standards are upheld:
    * TDV-inspired coding standards:
        * Code in comments should be removed
        * Add appropriate exception handling and error handling
        * Remove unused endpoints
        * Organise package structure
        * Ensure variables are named according to their purpose
        * Add descriptive code comments
        * Add logging in code - remove System.out.println
        * Remove any copy/pasted code and refactor
        * Remove any hardcoded values
        * Make variables final - including in method parameters if they are not changing
* Set aside 15 mins to go through these when you review a PR.

### Test-Driven Design
* TDD is a must when creating methods, especially in Java-land.
* Don't test private methods, as a rule.
* It is worth the initial investment to get efficient code and comprehensive tests.

### Overcoming Obstacles
* **spring-data-jpa dependency issue**
    * make sure you the ```spring-boot-starter-data-jpa``` dependency and not the ```spring-data-jpa``` when connecting to a database, using a JPA repository.
        * I kept facing a ```Consider defining a bean named 'entityManagerFactory' in your configuration``` exception with the latter dependency.
        * source: [stack overflow](https://stackoverflow.com/questions/41170661/spring-data-jpa-consider-defining-a-bean-named-entitymanagerfactory-in-your/41178250)
* **Minified React Error #130**
    * change the version of ```"react-bootstrap": "^2.1.2" ``` to ``` "react-bootstrap": "^1.5.2" ``` in ```client/package.json``` and run ```npm install``` in ```/client```
    * when you get errors in the client (visible in the browser js console), you can view more detailed logs in the terminal, if you run the client on its own
        * ``` cd frontend/client ```
        * ``` export PORT=9000 ```
        * ``` npm start ```
* **Using JQuery in React**
    * [stack overflow](https://stackoverflow.com/questions/38518278/how-to-use-jquery-with-reactjs)
        * ``` npm install jquery --save ```
        * ``` import $ from 'jquery'; ```


<br>

 

