import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./login.css";

const url = "localhost:8080"

const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleEmail = (e) => {
    setEmail(e.target.value);
  }
  const handlePassword = (e) => {
    setPassword(e.target.value)
  }

  // const users = [
  //   { id: hyerin, password: 1234 },
  //   { id: jeong, password: 3433 }
  // ]

  const handleLogin = async (email, password) => {
    if (email === "" || password === "") {
      alert("아이디와 비밀번호를 입력해주세요.");
      return;
    }
    try {
      const res = await axios.get(url + `/signin`);
      localStorage.setItem("userId", res.data.id)
      navigate.go(0);
    } catch (err) {
      console.log(err);
    }
  }


  return (
    <div className="container-login">
      <h1 className="login">로그인</h1>
      <div className="input-container">
        <input
          className="login-input"
          id="email"
          name="email"
          value={email}
          onChange={handleEmail}
          placeholder="이메일" />
        <input
          className="login-input"
          id="password"
          name="password"
          value={password}
          onChange={handlePassword}
          type="password"
          placeholder="비밀번호"
        />
        <button
          className="btn-login"
          onclick={handleLogin}>로그인</button>
      </div>
    </div>
  );
};

export default Login;
