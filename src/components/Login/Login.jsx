import "./Login.scss";
import { TextField, Box, Button } from "@mui/material";
import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { saveToken } from "../Authen/Auth";
import { apost } from "../../net/Axios";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isRequesting, setRequesting] = useState(false);
  const navigate = useNavigate();

  const fetchUser = async (jwt) => {
    if (jwt) {
      try {
        const response = await axios.get("https://jewelrysalesystem-backend.onrender.com/api/this-info", {
          headers: {
            'Authorization': `Bearer ${jwt}`
          }
        });
        console.log(response.data);
        return response.data.roleID;  // Return roleID directly
      } catch (error) {
        console.error("Error fetching user:", error);
        return null;
      }
    } else {
      console.error("No token found");
      return null;
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
      if (roleID === 1) {
        navigate("/staff");
      } else if (roleID === 2) {
        navigate("/manager");
      } else if (roleID === 3) {
        navigate("/admin");
      } else {
        navigate("/homepage");
      }
    } catch (e) {
      console.error(e);
      console.error("Login error");
      toast.error("Wrong email or password!")
    } finally {
      setRequesting(false);
    }
  };

  return (
    <div className="login">
      <ToastContainer/>
      <Box className="login__form">
        <h2>Login</h2>
        <TextField
          required
          className="login-input"
          id="outlined-required"
          label="Email"
          defaultValue=""
          onChange={(e) => setEmail(e.target.value)}
        />
        <TextField
          required
          className="login-input"
          id="outlined-required"
          type="password"
          label="Password"
          defaultValue=""
          onChange={(e) => setPassword(e.target.value)}
        />
        <a href="/forget-form">forget password?</a>
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

        <Button
          variant="contained"
          href="#contained-buttons"
          className="btn-register"
          component={Link}
          to="/register"
        >
          Register
        </Button>
      </Box>
    </div>
  );
}
