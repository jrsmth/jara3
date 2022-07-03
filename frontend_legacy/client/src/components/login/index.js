import React, { useState } from "react";
import { authenticate, register} from "../../api";
import Footer from "../footer";
import Header from "../header"
import "../../css/login.css";
import j3Logo from '../../res/j3_logo.png';
import $ from 'jquery';

function Login({ onLoginSuccessful }) {
  const [username, setUsername] = useState(localStorage.getItem("username"));
  const [password, setPassword] = useState(localStorage.getItem("password"));

  const onUsernameChange = (event) => setUsername(event.target.value);
  const onPasswordChange = (event) => setPassword(event.target.value);

  const onSubmitLogin = async (event) => {
    event.preventDefault();
    const loginResult = await authenticate({ username, password });
    console.log(loginResult);
    
    var status = Object.keys(loginResult)[0];
    var response = Object.values(loginResult)[0];
    console.log(status);
    console.log(response);

    if (status === "CONFLICT"){
      toggleAlert(true, response);
      console.log(response);
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
    const registrationResult = await register({ username, password });
    console.log(registrationResult);
    
    var status = Object.keys(registrationResult)[0];
    var response = Object.values(registrationResult)[0];
    console.log(status);
    console.log(response);

    if (status === "CONFLICT"){
      toggleAlert(true, response);
      console.log(response);
    } 
    else {
      localStorage.setItem("username", username);
      localStorage.setItem("password", password);
      window.location.reload(false);
    }
  };

  const toggleForm = () => {
    $('form').animate({height: "toggle", opacity: "toggle"}, 500); // perform animation
    toggleAlert(false, null); // hide error message
    // reset form
    $('.input-login').val('');
    $('.input-register').val('');
    setUsername("");
    setPassword("");
  };

  const toggleAlert = (condition, message) => {
    if(condition){ // show alert
      document.getElementById("alert").style.display = "block";
      document.getElementById("form").style.padding = "45px 45px 16px 45px";
      document.getElementById("alert-message").innerHTML = message;
    } else{ // hide alert
      document.getElementById("alert").style.display = "none";
      document.getElementById("form").style.padding = "45px 45px 60px 45px";
    }
  };

  return (
    <html> 
        <Header/>
        <div id="background-img"></div>
        <div id="background-overlay"></div>
        <div id="container-login">
            <img id="logo" src={j3Logo} alt=""/>
            <p id="slogan"> a jira-inspired to-do list</p>
            <div id="container-login-inner">
                <div id="form">
                    <form id="form-register" onSubmit={onSubmitRegister}>
                        <input class="input-register" type="text" placeholder="Username" onChange={onUsernameChange} value={username}/>
                        <input class="input-register" type="password" placeholder="Password" onChange={onPasswordChange} value={password}/>
                        <button type="submit">Register</button>
                        <p class="message">Already registered? <a onClick={toggleForm}>Sign In</a></p>
                    </form>
                    <form id="form-login" onSubmit={onSubmitLogin}>
                        <input class="input-login" type="text" placeholder="Username" onChange={onUsernameChange} value={username}/>
                        <input class="input-login" type="password" placeholder="Password" onChange={onPasswordChange} value={password}/>
                        <button id="btn-login" type="submit">Sign In</button>
                        <p class="message">Not registered? <a onClick={toggleForm}>Register an account</a></p>
                    </form>
                </div>
                <div id="alert" class="error alert-danger"> 
                    <i class="fa fa-exclamation-circle" aria-hidden="true"></i> <span id="alert-message"> Invalid username or password </span>
                </div>
            </div>
        </div>
        <Footer/>
    </html>
  );
}

export default Login;
