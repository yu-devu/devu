import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./register.css";

const url = "localhost:8080"

const Register = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [authkey, setAuthkey] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [checkPassword, setCheckPassword] = useState("");

  const handleEmail = (e) => {
    setEmail(e.target.value);
  }
  const handleAuthkey = (e) => {
    setAuthkey(e.target.value);
  }
  const handleUsername = (e) => {
    setUsername(e.target.value);
  }
  const handlePassword = (e) => {
    setPassword(e.target.value)
  }
  const handleCheckPassword = (e) => {
    setCheckPassword(e.target.value);
  }

  const handleAuthorize = async (email) => {
    try {
      const res = await axios.post(url + `/email`, email)
        .then((res) => {
          if (res.status === 200) {
            console.log("authkey 전송 완료!");
          } else {
            console.log("authkey 전송 실패..");
          }
        });
    } catch (err) {
      console.log(err);
    }
  }

  const checkAuthkey = async (authkey) => {
    try {
      const res = await axios.post(url + `/key`, authkey)
        .then((res) => {
          if (res.status === 200) {
            console.log("인증확인 완료!");
          } else {
            console.log("인증확인 실패..");
          }
        });
    } catch (err) {
      console.log(err);
    }
  }

  const handleSignUp = async (email, username, password) => {
    try {
      const data = {
        eamil: email,
        username: username,
        password: password,
      };

      const res = await axios.post(url + `/signup`, JSON.stringify(data), {
        headers: {
          "Content-Type": 'application/json',
        }
      })
        .then((res) => {
          if (res.status === 200) {
            // bootstrap이나 antd등 라이브러리 사용해서 회원가입이 성공했다는 창을 보여줘야 함
            // message.success("")
            console.log("회원가입에 성공했습니다!");
            navigate.go(0);
          } else {
            // message.error("")
            console.log("회원가입에 실패했습니다..");
          }
        });
    } catch (err) {
      console.log(err);
    }

  }

  return (
    <div className="container-register">
      <h1 className="register">회원가입</h1>
      <div className="input-container">
        <div className="register-email">
          <input
            className="register-input"
            id="email"
            name="email"
            value={email}
            onChange={handleEmail}
            placeholder="이메일"
          />
          <button
            className="btn-validate"
            onClick={handleAuthorize}
          >인증하기</button>
        </div>
        <div className="register-email">
          <input
            className="register-input"
            id="authkey"
            name="authkey"
            value={authkey}
            onChange={handleAuthkey}
            placeholder="인증번호"
          />
          <button
            className="btn-validate"
            onClick={checkAuthkey}
          >인증확인</button>
        </div>
        <input
          className="register-input"
          id="username"
          name="username"
          value={username}
          onChange={handleUsername}
          placeholder="사용자 이름"
        />
        <input
          className="register-input"
          id="password"
          name="password"
          value={password}
          onChange={handlePassword}
          type="password"
          placeholder="비밀번호"
        />
        <input
          className="register-input"
          id="password"
          name="password"
          value={checkPassword}
          onChange={handleCheckPassword}
          type="password"
          placeholder="비밀번호 확인"
        />

        <button onClick={handleSignUp} className="btn-register">회원가입</button>
      </div>
    </div>
  );
};

export default Register;
