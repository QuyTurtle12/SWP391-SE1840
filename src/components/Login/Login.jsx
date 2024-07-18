import "./Login.scss";
import { TextField, Box, Button } from "@mui/material";
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { saveToken, getToken } from "../Authen/Auth"; // Ensure getToken and removeToken are defined in Auth
import { apost } from "../../net/Axios";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isRequesting, setRequesting] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const checkToken = async () => {
      const token = getToken(); // Retrieve the saved token
      if (token) {
        try {
          const roleID = await fetchUser(token); // Fetch the user role with the token
          navigateBasedOnRole(roleID); // Navigate based on the role
        } catch (error) {
          console.error("Error during token check:", error);
        }
      }
    };

    checkToken();
  }, [navigate]);

  const fetchUser = async (jwt) => {
    try {
      const response = await axios.get("http://localhost:8080/api/this-info", {
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      });
      console.log(response.data);
      return response.data.roleID; // Return roleID directly
    } catch (error) {
      console.error("Error fetching user:", error);
      return null;
    }
  };

  const navigateBasedOnRole = (roleID) => {
    if (roleID === 1) {
      navigate("/staff");
    } else if (roleID === 2) {
      navigate("/manager");
    } else if (roleID === 3) {
      navigate("/admin");
    } else {
      navigate("/homepage");
    }
  };

  const handleLogin = async () => {
    setRequesting(true);

    try {
      const response = await apost("/api/auth/login", {
        email: email,
        password: password,
      });

      console.log(response.data);
      const { jwt } = response.data;

      // Save token into Auth
      saveToken(jwt);

      // Fetch user information with the token
      const roleID = await fetchUser(jwt);
      console.log(roleID);

      // Navigate based on roleID
      navigateBasedOnRole(roleID);
    } catch (e) {
      console.error(e);
      console.error("Login error");
      toast.error("Wrong email or password!");
    } finally {
      setRequesting(false);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleLogin();
    }
  };

  return (
    <div className="login">
      <ToastContainer />
      <Box className="login__form">
        <h2>Login</h2>
        <TextField
          required
          className="login-input"
          id="outlined-required"
          label="Email"
          defaultValue=""
          onChange={(e) => setEmail(e.target.value)}
          onKeyPress={handleKeyPress}
        />
        <TextField
          required
          className="login-input"
          id="outlined-required"
          type="password"
          label="Password"
          defaultValue=""
          onChange={(e) => setPassword(e.target.value)}
          onKeyPress={handleKeyPress}
        />
        <a href="/forget-form">Forget password?</a>
        {!isRequesting ? (
          <Button
            variant="contained"
            href="#contained-buttons"
            className="btn-login"
            onClick={handleLogin}
          >
            Login
          </Button>
        ) : (
          <Button
            variant="contained"
            href="#contained-buttons"
            className="btn-login"
          >
            Logging in...
          </Button>
        )}
      </Box>
    </div>
  );
}
