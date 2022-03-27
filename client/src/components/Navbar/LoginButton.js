import React from "react";
import { Link } from "react-router-dom";
import "./loginButton.css";

const LoginButton = () => {
  return (
    <Link to="login">
      <button className="btn-login-nav">로그인</button>
    </Link>
  );
};

export default LoginButton;
