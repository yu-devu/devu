import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './register.css';

const Register = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [authkey, setAuthkey] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [checkPassword, setCheckPassword] = useState('');
  const [passwordAvailability, setPasswordAvailability] = useState(false);
  const [showValidate, setShowValidate] = useState(false);
  const [showInformation, setShowInformation] = useState(false);
  const [clickAuthkey, setClickAuthkey] = useState(false);
  const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/

  const handleEmail = (e) => setEmail(e.target.value);
  const handleAuthkey = (e) => setAuthkey(e.target.value);
  const handleUsername = (e) => setUsername(e.target.value);

  const handlePassword = (e) => {
    var regExp = /^(?=.*\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{8,10}$/;
    setPassword(e.target.value);
    setPasswordAvailability(regExp.test(e.target.value));
  };
  const handleCheckPassword = (e) => setCheckPassword(e.target.value);

  const showValidateInput = () => setShowValidate(true);
  const showInformationInput = () => setShowInformation(true);
  const handleAuthorize = async () => {
    if (email.includes('@ynu.ac.kr') || email.includes('@yu.ac.kr')) {
      const formData = new FormData();
      formData.append('email', email);
      await axios
        .post(process.env.REACT_APP_DB_HOST + `/email`, formData)
        .then((res) => {
          if (res.data.error) {
            alert(res.data.error);
          }
          setClickAuthkey(true);
          showValidateInput();
          console.log(res);
        })
        .catch((res) => alert(JSON.parse(res.request.response).error));
    } else alert('이메일 형식을 확인해주세요!');
  };

  const checkAuthkey = async () => {
    const formData = new FormData();
    formData.append('postKey', authkey);
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/key`, formData)
      .then(() => {
        alert('인증확인 완료!');
        showInformationInput();
      })
      .catch(() => alert('인증확인 실패..'));
  };

  const handleSignUp = async () => {
    if (password === checkPassword) {
      if (passwordAvailability == true) {
        const data = {
          email: email,
          username: username,
          password: password,
        };
        console.log(JSON.stringify(data));
        await axios
          .post(process.env.REACT_APP_DB_HOST + '/signup', data, {
            headers: {
              'Content-Type': 'application/json',
            },
          })
          .then(() => alert('회원가입에 성공했습니다!'))
          .catch(() => console.log('회원가입 실패..'));
      } else alert('비밀번호를 양식에 맞게 입력해주세요');
    } else alert('비밀번호를 확인해주세요.');
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
          {!clickAuthkey ? (
            <button className="btn-validate" onClick={() => handleAuthorize()}>
              인증하기
            </button>
          ) : (
            <button className="btn-validate" onClick={() => handleAuthorize()}>
              재전송
            </button>
          )}
        </div>
        {showValidate ? (
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
        ) : null}
        {showInformation ? (
          <div>
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
            {password && !passwordAvailability ? (
              <p>영문+숫자 조합으로 8~10자리로 생성하여야 합니다.</p>
            ) : null}
            <input
              className="register-input"
              id="checkPassword"
              name="checkPassword"
              value={checkPassword}
              onChange={(e) => handleCheckPassword(e)}
              type="password"
              placeholder="비밀번호 확인"
            />
            {checkPassword && password !== checkPassword ? (
              <p>비밀번호가 일치하지 않습니다.</p>
            ) : null}
            <button onClick={() => handleSignUp()} className="btn-register">
              회원가입
            </button>
          </div>
        ) : null}
      </div>
    </div>
  );
};

export default Register;
