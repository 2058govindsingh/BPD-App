import React, { useEffect, useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Home from "./components/Home/Home";
import Login from "./components/Login/Login";
import Signup from "./components/Signup/Signup";
// import Signout from "./components/Signout.js";

import { auth } from "./firebase";

import "./App.css";
import ButtonAppBar from "./components/Home/AppBar";

function App() {
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    auth.onAuthStateChanged((user) => {
      if (user) {
        console.log(userData)
        setUserData(user);
      } else setUserData(null);
    });
  }, []);
  return (
    <div className="App">
      <Router>
        <ButtonAppBar/>
        <Routes>
          <Route path="/" element={userData !== null ? <Home /> : <Login/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/signup" element={<Signup />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
