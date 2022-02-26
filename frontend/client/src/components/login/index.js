import React, { useState } from "react";
import { authenticate, register} from "../../api";
import Footer from "../footer";
import Header from "../header"
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

  // Toggle Login vs Sign Up (on-screen)
  // $('.message a').click(function(){
  //     $('form').animate({height: "toggle", opacity: "toggle"}, 500);
  //     $(".error").hide();
  //     $('.input-login').val('');
  //     $('.input-register').val('');
  // }); // remove the JQUERY

  // $('#btn-login').click(function(){ $(".error").show(); }); // remove the JQUERY

  // function toggleError(condition){
  //   if(condition){
  //       document.getElementById("login-error").style.display = "block";
  //       document.getElementsByClassName("form")[0].style.padding = "45px 45px 16px 45px";
  //   } else{
  //       document.getElementById("login-error").style.display = "none";
  //       document.getElementsByClassName("form")[0].style.padding = "45px 45px 60px 45px";
  //   }
  // }

  return (
    <html> 
        <Header/>
        <div id="background-img"></div>
        <div id="background-overlay"></div>
        <div id="container-login">
            <img id="logo" src={j3Logo} alt=""/>
            <p id="slogan"> a jira-inspired to-do list</p>
            <div id="container-login-inner">
                <div class="form">
                    <form class="register-form" onSubmit={onSubmitRegister}>
                        <input class="input-register" type="text" placeholder="Username" onChange={onUsernameChange} value={username}/>
                        <input class="input-register" type="password" placeholder="Password" onChange={onPasswordChange} value={password}/>
                        <button type="submit">Create</button>
                        <p class="message">Already registered? <a href="#">Sign In</a></p>
                    </form>
                    <form class="login-form" onSubmit={onSubmitLogin}>
                        <input class="input-login" type="text" placeholder="Username" onChange={onUsernameChange} value={username}/>
                        <input class="input-login" type="password" placeholder="Password" onChange={onPasswordChange} value={password}/>
                        <button id="btn-login" type="submit">Login</button>
                        <p class="message">Not registered? <a href="#">Create an account</a></p>
                    </form>
                </div>
                  <div id="login-error" class="error alert alert-danger"> 
                      <i class="fa fa-exclamation-circle" aria-hidden="true"></i> <span> Invalid username or password </span>
                  </div>
            </div>
        </div>
        <Footer/>
    </html>
  );
}

export default Login;
