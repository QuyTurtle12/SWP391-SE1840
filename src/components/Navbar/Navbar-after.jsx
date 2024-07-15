import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import './Navbar.scss';
import { Link } from 'react-router-dom';
import logodiamond from '../asset/logo.png';


export default function NavbarHome() {
  return (
    <Box className='navbar'>
      <AppBar position="static" className='appbar'>
        <Toolbar>
          <Typography className='title flex' sx={{ flexGrow: 1 }} component={Link} to="/">
          <img className='logo' src={logodiamond}/>

            Sparkle 'n Shine
          </Typography>
          
        </Toolbar>
      </AppBar>
    </Box>
  );
}