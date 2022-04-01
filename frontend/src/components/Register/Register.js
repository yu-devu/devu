import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './register.css';

const url = 'http://54.180.29.69:8080';

const Register = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [authkey, setAuthkey] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [checkPassword, setCheckPassword] = useState('');

  const handleEmail = (e) => setEmail(e.target.value);
  const handleAuthkey = (e) => setAuthkey(e.target.value);
  const handleUsername = (e) => setUsername(e.target.value);
  const handlePassword = (e) => setPassword(e.target.value);
  const handleCheckPassword = (e) => setCheckPassword(e.target.value);

  const handleAuthorize = async () => {
    const formData = new FormData();
    formData.append('email', email);
    await axios
      .post(url + `/email`, formData)
      .then(() => alert('authkey 전송 완료!'))
      .catch(() => alert('authkey 전송 실패..'));
  };

  const checkAuthkey = async () => {
    const formData = new FormData();
    formData.append('postKey', authkey);
    await axios
      .post(url + `/key`, formData)
      .then(() => alert('인증확인 완료!'))
      .catch(() => alert('인증확인 실패..'));
  };

  const handleSignUp = async () => {
    const data = {
      email: email,
      username: username,
      password: password,
    };
    console.log(JSON.stringify(data));

    await axios
      .post(url + `/signup`, JSON.stringify(data), {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then(() => {
        alert('회원가입에 성공했습니다!');
        // navigate.go(0);
      })
      .catch(() => alert('회원가입에 실패했습니다..'));
  };

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
            onChange={(e) => handleEmail(e)}
            placeholder="이메일"
          />
          <button className="btn-validate" onClick={() => handleAuthorize()}>
            인증하기
          </button>
        </div>
        <div className="register-email">
          <input
            className="register-input"
            id="authkey"
            name="authkey"
            value={authkey}
            onChange={(e) => handleAuthkey(e)}
            placeholder="인증번호"
          />
          <button className="btn-validate" onClick={() => checkAuthkey()}>
            인증확인
          </button>
        </div>
        <input
          className="register-input"
          id="username"
          name="username"
          value={username}
          onChange={(e) => handleUsername(e)}
          placeholder="사용자 이름"
        />
        <input
          className="register-input"
          id="password"
          name="password"
          value={password}
          onChange={(e) => handlePassword(e)}
          type="password"
          placeholder="비밀번호"
        />
        <input
          className="register-input"
          id="password"
          name="password"
          value={checkPassword}
          onChange={(e) => handleCheckPassword(e)}
          type="password"
          placeholder="비밀번호 확인"
        />

        <button className="btn-register" onClick={() => handleSignUp()}>
          회원가입
        </button>
      </div>
    </div>
  );
};

export default Register;
