const { response } = require("express");
const fetch = require("cross-fetch");
const eurekaRegistry = require('../models/eureka-registry').eurekaRegistry;

const authenticate = async (req, res = response) => {
  console.log(eurekaRegistry.urlUserService);

  const { username, password } = req.body;
  console.log(username);
  console.log(password);
  await fetch(eurekaRegistry.urlUserService+"authenticate/"+username+"/"+password, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
  }).then(async function (resp) {
    const data = await resp.json();
    console.log(data);
    if (resp.status >= 400) {
      throw new Error("Bad response from server");
    }
    res.json(data);
  }).catch( function (err) {
    console.error(err);
  })

};

const register = async (req, res = response) => {
  console.log(req.body);
  const {username, password} = req.body;
  console.log(JSON.stringify({ "username": username, "password": password }));
  await fetch(eurekaRegistry.urlUserService+"register", {
      method: "POST",
      body: JSON.stringify({ "username": username, "password": password }),
      headers: {
        "Content-Type": "application/json",
      },
  }).then(async function (resp) {
    const data = await resp.json();
    console.log(data);
    if (resp.status >= 400) {
      throw new Error("Bad response from server");
    }
    res.json(data);
  }).catch( function (err) {
    console.error(err);
  })

};

module.exports = {
  authenticate,
  register
};
