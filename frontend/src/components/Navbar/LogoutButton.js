import React from "react";
import axios from "axios";
import "./logoutButton.css";
import { useNavigate } from "react-router-dom";

const LogoutButton = () => {
  const navigate = useNavigate();
  const logout = async () => {
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/logout`)
      .then(() => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("username");
        window.location.reload(false);
      })
      .catch((e) => console.log(e));
  };

  return (
    <>
      <button
        className="btn-logout-nav"
        onClick={() => {
          logout();
          navigate("/");
        }}
      >
        로그아웃
      </button>
    </>
  );
};

export default LogoutButton;
