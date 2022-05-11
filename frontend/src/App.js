import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar/Navbar';
import Home from './components/Home/Home';
import Register from './components/Register/Register';
import Write from './components/Forum/Chats/Write';
import PostView from './components/Forum/Chats/PostView';
import Modify from './components/Forum/Chats/Modify';
import ChangePassword from './components/Navbar/ChangePassword';
import Chats from './components/Forum/Chats/Chats';
import Studies from './components/Forum/Studies/Studies';
import StudiesView from './components/Forum/Studies/StudiesView';

function App() {
  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" exact element={<Home />}></Route>
          <Route path="/register" exact element={<Register />}></Route>
          <Route path="/chats" exact element={<Chats />}></Route>
          <Route path="/chats/write" exact element={<Write />}></Route>
          <Route path="/postDetail/:no" exact element={<PostView />}></Route>
          <Route path="/studiesDetail/:no" exact element={<StudiesView />}></Route>
          <Route path="/postDetail/:no/modify" exact element={<Modify />}></Route>
          <Route path="/change_password/*" element={<ChangePassword />}></Route>
          <Route path="/studies" exact element={<Studies />}></Route>
        </Routes>
      </Router>
    </>
  );
}

export default App;
