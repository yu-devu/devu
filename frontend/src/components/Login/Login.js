import React, { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import axios from "axios";
import "./login.css";

const Login = () => {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');

  const handleId = (e) => {
    setId(e.target.value);
  }
  const handlePassword = (e) => {
    setPassword(e.target.value)
  }

  // const users = [
  //   { id: hyerin, password: 1234 },
  //   { id: jeong, password: 3433 }
  // ]


  return (
    <div className="container-login">
      <h1 className="login">로그인</h1>
      <div className="input-container">
        <input
          className="login-input"
          id="id"
          name="id"
          value={id}
          onChange={handleId}
          placeholder="아이디" />
        <input
          className="login-input"
          id="password"
          name="password"
          value={password}
          onChange={handlePassword}
          type="password"
          placeholder="비밀번호"
        />
        <button className="btn-login">로그인</button>
      </div>
    </div>
  );
};

export default Login;
