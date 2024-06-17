import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { Link } from 'react-router-dom';
import logodiamond from '../asset/logo.png';

export default function Navbar() {
  return (
    <Box className="flex-grow w-screen">
      <AppBar position="static" className="bg-black shadow-none z-0">
        <Toolbar>
          <Typography
            className="flex items-center flex-grow text-2xl no-underline font-medium text-white"
            component={Link}
            to="/"
          >
            <img 
              src={logodiamond} 
              alt="logo" 
              className="w-12 h-12 pb-1 mb-[-10px] mr-2.5"
            />
            Sparkle 'n Shine
          </Typography>
          <Button color="inherit" component={Link} to="/login">Login</Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
