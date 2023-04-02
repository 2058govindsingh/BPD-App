import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Link, useNavigate } from "react-router-dom";
import Login from '../Login/Login.js'
import MenuPopupState from './Menu.js';
import { auth } from "../../firebase";
import { getAuth, signOut } from "firebase/auth";


function ButtonAppBar() {

  const [userData, setUserData] = React.useState(null);
  const navigate = useNavigate();
  React.useEffect(() => {
    auth.onAuthStateChanged((user) => {
      console.log(user);
      if (user) {

        setUserData(user);
      } else setUserData(null);
    });
  }, []);


  function handleLogout() {


    const auth = getAuth();
    signOut(auth).then(() => {
      navigate("/login")
    })
  }


  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            {/* <MenuIcon /> */}
          </IconButton>
          <MenuPopupState />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            BPD
          </Typography>
          {/* <Button color="inherit" onClick={showLogin}>Login</Button> */}
          {/* <Button > Login</Button> */}
          {
            userData !==null ?
              <Button color="inherit" variant='contained' onClick={handleLogout}>Log Out</Button>
              :
              <Link to="/Login"><Button color="inherit" variant='contained'>Login</Button></Link>
          }
        </Toolbar>
      </AppBar>
    </Box>
  );
}
export default ButtonAppBar;