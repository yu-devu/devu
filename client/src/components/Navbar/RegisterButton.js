import React from "react";
import { Link } from "react-router-dom";
import "./registerButton.css";

function RegisterButton() {
  return (
    <Link to="register">
      <button className="btn-register-nav">회원가입</button>
    </Link>
  );
}

export default RegisterButton;
