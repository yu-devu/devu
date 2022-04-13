import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar/Navbar';
import Home from './components/Home/Home';
import Register from './components/Register/Register';
import Board from './components/Forum/Board/Board';
import Write from './components/Forum/Board/Write';
import PostView from './components/Forum/Board/PostView';

function App() {
  return (
    <>
      <Router>
        <Navbar />
        <Routes>
          <Route path="/" exact element={<Home />}></Route>
          <Route path="/register" exact element={<Register />}></Route>
          <Route path="/board" exact element={<Board />}></Route>
          <Route path="/board/write" exact element={<Write />}></Route>
          <Route path="/postDetail/:no" exact element={<PostView />}></Route>
        </Routes>
      </Router>
    </>
  );
}

export default App;
