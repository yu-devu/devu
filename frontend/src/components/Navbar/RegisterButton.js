import React from "react";
import { Link } from "react-router-dom";
import "./registerButton.css";

function RegisterButton() {
  const accessToken = localStorage.getItem("accessToken");
  return (
    <>
      {
        accessToken ? (
          <Link to="mypage" >
            <button className="btn-register-nav">마이페이지</button>
          </Link>
        ) : (<Link to="register">
          <button className="btn-register-nav">회원가입</button>
        </Link>)
      }
    </>
  );
}

export default RegisterButton;
