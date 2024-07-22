import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { Link, useNavigate } from "react-router-dom";
import logodiamond from "../asset/logo.png";
import axios from "axios";

export default function Navbar() {
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

const handleLogout = async () => {
  try {
    await axios.post(
      "https://jewelrysalesystem-backend.onrender.com/api/auth/logout",
      {},
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    localStorage.removeItem("token");
    localStorage.removeItem("sessionActive");
    navigate("/login");
  } catch (error) {
    console.error("Error at logging out", error);
  }
};
  return (
    <Box className="flex-grow w-screen">
      <AppBar position="static" className="bg-black shadow-none z-0">
        <Toolbar>
          <Typography
            className="flex items-center flex-grow text-2xl no-underline font-medium text-white"
    
          >
            <img
              src={logodiamond}
              alt="logo"
              className="w-12 h-12 pb-1 mb-[-10px] mr-2.5"
            />
            Sparkle 'n Shine
          </Typography>
          {!token ? (
            <Button color="inherit" component={Link} to="/login">
              Login
            </Button>
          ) : (
            <Button color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
}
