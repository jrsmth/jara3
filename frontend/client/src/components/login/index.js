import React, { useState } from "react";
import { Container, Form, Button, Alert, Card } from "react-bootstrap";
import { login, test, authenticate, register} from "../../api";

function Login({ onLoginSuccessful }) {
  const [email, setEmail] = useState(localStorage.getItem("email"));
  const [password, setPassword] = useState(localStorage.getItem("password"));
  const [hasResponse, setHasResponse] = useState(false);
  const [response, setResponse] = useState("");
  const [toggleAction, setToggleAction] = useState("Register");
  const [toggleMessage, setToggleMessage] = useState("Don't have an account? Sign up here");

  const onEmailChange = (event) => setEmail(event.target.value);
  const onPasswordChange = (event) => setPassword(event.target.value);

  const onSubmitLogin = async (event) => {
    event.preventDefault();
    setHasResponse(false);
    const loginResult = await authenticate({ email, password });
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
      const { name, token } = loginResult;
      // Save user IDs on local storage
      localStorage.setItem("name", name);
      localStorage.setItem("token", token);
      localStorage.setItem("email", "");
      localStorage.setItem("password", "");
      onLoginSuccessful();
    }
  };

  const onSubmitRegister = async (event) => {
    event.preventDefault();
    setHasResponse(false);
    const registrationResult = await register({ email, password });
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
      localStorage.setItem("email", email);
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
        localStorage.setItem("email", "");
        localStorage.setItem("password", "");
        break;
      case "Register":
        setToggleAction("Login");
        setToggleMessage("Already have an account? Login here");
        break;   
    }

    // Reset form when toggled
    setEmail("");
    setPassword("");
    setHasResponse(false);
    setResponse("");

    console.log(localStorage.getItem("email"));
    console.log(localStorage.getItem("password"));
  };

  return (
    <Container>
      {toggleAction != "Login" && (
      <Card className="mt-5">
        <Card.Header as="h1">Login</Card.Header>
        <Card.Body>
          <Form className="w-100" onSubmit={onSubmitLogin}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email Address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                onChange={onEmailChange}
                value={email}
              />
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
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
        <Card.Header as="h1">Register</Card.Header>
        <Card.Body>
          <Form className="w-100" onSubmit={onSubmitRegister}>
            <Form.Group controlId="formBasicEmail">
              <Form.Label>Email Address</Form.Label>
              <Form.Control
                type="email"
                placeholder="Enter email"
                onChange={onEmailChange}
                value={email}
              />
              <Form.Text className="text-muted">
                We'll never share your email with anyone else.
              </Form.Text>
            </Form.Group>

            <Form.Group controlId="formBasicPassword">
              <Form.Label>Password</Form.Label>
              <Form.Control
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
  );
}

export default Login;
