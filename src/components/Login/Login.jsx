import "./Login.scss";
import { TextField, Box, Button } from "@mui/material";
import React, { useState } from "react";

import { useNavigate, Link } from "react-router-dom";
import { saveToken } from "../Authen/Auth";
import { apost } from "../../net/Axios";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isRequesting, setRequesting] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async () => {
    setRequesting(true);

    try {
      const response = await apost("/api/auth/login", {
        email: email,
        password: password,
      });

      console.log(response.data);
      const { jwt } = response.data;

      // Lưu token vào Auth
      saveToken(jwt);

      // Navigate to the Home page after successful login
      navigate("/homepage");
      window.location.reload();
    } catch (e) {
      console.error(e);
      console.error("Login error");
    } finally {
      setRequesting(false);
    }
  };

  return (
    <div className="login">
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
        <a href="/forget-form">Forgot password?</a>
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
