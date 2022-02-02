const { response } = require("express");
const fetch = require("cross-fetch");
const eurekaRegistry = require('../models/eureka-registry').eurekaRegistry;

const login = async (req, res = response) => {
  const { email, password } = req.body;

  // Ideally search the user in a database,
  // throw an error if not found.
  if (password !== "1234") {
    return res.status(400).json({
      msg: "User / Password are incorrect",
    });
  }

  res.json({
    name: "Test User",
    token: "A JWT token to keep the user logged in.",
    msg: "Successful login",
  });
};

const test = async (req, res = response) => {
  console.log("HIT JRS");
  console.log(eurekaRegistry.urlUserService);

  await fetch(eurekaRegistry.urlUserService+"login?user=smith&pass=james", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
  }).then(async function (resp) {
    // resp.json();
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
  login,
  test
};
