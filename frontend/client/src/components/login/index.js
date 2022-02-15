import React, { useState } from "react";
import { Container, Form, Button, Alert, Card } from "react-bootstrap";
import { authenticate, register} from "../../api";
import "../../css/login.css";
import j3Logo from '../../res/j3_logo.png';

function Login({ onLoginSuccessful }) {
  const [username, setUsername] = useState(localStorage.getItem("username"));
  const [password, setPassword] = useState(localStorage.getItem("password"));
  const [hasResponse, setHasResponse] = useState(false);
  const [response, setResponse] = useState("");
  const [toggleAction, setToggleAction] = useState("Register");
  const [toggleMessage, setToggleMessage] = useState("Don't have an account? Sign up here");

  const onUsernameChange = (event) => setUsername(event.target.value);
  const onPasswordChange = (event) => setPassword(event.target.value);

  const onSubmitLogin = async (event) => {
    event.preventDefault();
    setHasResponse(false);
    const loginResult = await authenticate({ username, password });
    console.log(loginResult);
    
    var status = Object.keys(loginResult)[0];
    var response = Object.values(loginResult)[0];
    console.log(status);
    console.log(response);

    if (status === "CONFLICT"){
      setResponse(response);
      setHasResponse(true);
    } 
    else {
      // Save user IDs on local storage
      localStorage.setItem("name", response.username);
      localStorage.setItem("token", "A JWT token to keep the user logged in");
      localStorage.setItem("username", "");
      localStorage.setItem("password", "");
      onLoginSuccessful();
    }
  };

  const onSubmitRegister = async (event) => {
    event.preventDefault();
    setHasResponse(false);
    const registrationResult = await register({ username, password });
    console.log(registrationResult);
    
    var status = Object.keys(registrationResult)[0];
    var response = Object.values(registrationResult)[0];
    console.log(status);
    console.log(response);

    if (status === "CONFLICT"){
      setResponse(response);
    } 
    else {
      setResponse("Registration Successful");
      localStorage.setItem("username", username);
      localStorage.setItem("password", password);
      window.location.reload(false);
    }
    setHasResponse(true);
  };

  const onSubmitToggle = async (event) => {
    event.preventDefault();

    switch(toggleAction){
      case "Login":
        setToggleAction("Register");
        setToggleMessage("Don't have an account? Register here");
        localStorage.setItem("username", "");
        localStorage.setItem("password", "");
        break;
      case "Register":
        setToggleAction("Login");
        setToggleMessage("Already have an account? Login here");
        break;   
    }

    // Reset form when toggled
    setUsername("");
    setPassword("");
    setHasResponse(false);
    setResponse("");

    console.log(localStorage.getItem("username"));
    console.log(localStorage.getItem("password"));
  };

  return (
    <body>
      <div id="background-img"></div>
      <div id="background-overlay"></div>
      <div id="container-login">
        <img id="logo" src={j3Logo} alt=""/>
        <p id="slogan"> a jira-inspired to-do list</p>
        <Container id="container-login-inner"> 
          {toggleAction != "Login" && (
          <Card className="mt-5">
            <Card.Body>
              <Form className="w-100" onSubmit={onSubmitLogin}>
                <Form.Group controlId="formBasicUsername">
                  <Form.Control
                    className="input-login"
                    type="username"
                    placeholder="Username"
                    onChange={onUsernameChange}
                    value={username}
                  />
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                  <Form.Control
                    className="input-login"
                    type="password"
                    placeholder="Password"
                    onChange={onPasswordChange}
                    value={password}
                  />
                </Form.Group>
                {hasResponse && (
                  <Alert variant={"danger"}>
                    {response}
                  </Alert>
                )}
                <Button variant="primary" type="submit">
                  Login
                </Button>
              </Form>
            </Card.Body>
          </Card>
          )}

          {toggleAction != "Register" && (
          <Card className="mt-5">
            <Card.Body>
              <Form className="w-100" onSubmit={onSubmitRegister}>
                <Form.Group controlId="formBasicUsername">
                  <Form.Control
                    className="input-login"
                    type="username"
                    placeholder="Username"
                    onChange={onUsernameChange}
                    value={username}
                  />
                </Form.Group>

                <Form.Group controlId="formBasicPassword">
                  <Form.Control
                    className="input-login"
                    type="password"
                    placeholder="Password"
                    onChange={onPasswordChange}
                    value={password}
                  />
                </Form.Group>
                {hasResponse && (
                  <Alert variant={"danger"}>
                    {response}
                  </Alert>
                )}
                <Button variant="primary" type="submit">
                  Register
                </Button>
              </Form>
            </Card.Body>
          </Card>
          )}

          <Card className="mt-5">
            <Card.Body>
              <Form className="w-100" onSubmit={onSubmitToggle}>
                <Form.Group controlId="formBasicToggle">
                  <Form.Control
                    type="hidden"
                    value={toggleAction}
                  />
                </Form.Group>
                <Button variant="primary" type="submit">
                  {toggleMessage}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Container>
      </div>
    </body>
  );
}

export default Login;
