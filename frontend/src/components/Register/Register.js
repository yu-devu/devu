import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./register.css";
import FooterGray from "../Home/FooterGray";

const Register = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [authkey, setAuthkey] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [checkPassword, setCheckPassword] = useState("");
  const [passwordAvailability, setPasswordAvailability] = useState(false);
  const [showValidate, setShowValidate] = useState(false);
  const [showInformation, setShowInformation] = useState(false);
  const [clickAuthkey, setClickAuthkey] = useState(false);
  const [checkAuth, setCheckAuth] = useState(false);
  const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;

  const handleEmail = (e) => setEmail(e.target.value);
  const handleAuthkey = (e) => setAuthkey(e.target.value);
  const handleUsername = (e) => setUsername(e.target.value);

  const handlePassword = (e) => {
    var regExp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    setPassword(e.target.value);
    setPasswordAvailability(regExp.test(e.target.value));
  };
  const handleCheckPassword = (e) => setCheckPassword(e.target.value);

  const showValidateInput = () => setShowValidate(true);
  const showInformationInput = () => setShowInformation(true);
  const handleAuthorize = async () => {
    if (email.includes("@ynu.ac.kr") || email.includes("@yu.ac.kr")) {
      const formData = new FormData();
      formData.append("email", email);
      await axios
        .post(process.env.REACT_APP_DB_HOST + `/email`, formData)
        .then((res) => {
          if (res.data.error) alert(res.data.error);
          setClickAuthkey(true);
          showValidateInput();
        })
        .catch((e) => alert(JSON.parse(e.request.response).error));
    } else alert("이메일 형식을 확인해주세요!");
  };

  const checkAuthkey = async () => {
    const formData = new FormData();
    formData.append("postKey", authkey);
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/key`, formData)
      .then(() => {
        alert("인증확인 완료!");
        showInformationInput();
      })
      .catch(() => alert("인증확인 실패.."));
  };

  const handleSignUp = async () => {
    if (username !== "") {
      if (password === checkPassword) {
        if (passwordAvailability === true) {
          const data = {
            email: email,
            username: username,
            password: password,
          };
          await axios
            .post(process.env.REACT_APP_DB_HOST + "/signup", data, {
              headers: {
                "Content-Type": "application/json",
              },
            })
            .then(() => {
              alert("회원가입에 성공했습니다!");
              navigate("/registered");
              handleLogin();
            })
            .catch(() => console.log("회원가입 실패.."));
        } else alert("비밀번호를 양식에 맞게 입력해주세요");
      } else alert("비밀번호를 확인해주세요.");
    } else alert("사용자 이름을 입력해주세요.");
  };

  const handleLogin = async () => {
    const data = {
      email: email,
      password: password,
    };

    await axios
      .post(process.env.REACT_APP_DB_HOST + "/signin", JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
          // 'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
        },
      })
      .then((res) => {
        localStorage.setItem("username", res.data.username);
        localStorage.setItem("accessToken", res.data.accessToken);
        window.location.reload(false);
      })
      .catch(
        (e) => alert(JSON.parse(e.request.response).error) // 이메일, 비밀번호 오류 출력
      );
  };

  return (
    <div className="register-all">
      <div className="container-register">
        <h1 className="register">회원가입</h1>
        <div className="input-container">
          <h7 className="text-email">아이디(이메일)</h7>
          <div className="register-email">
            <input
              className="register-input-email"
              id="email"
              name="email"
              value={email}
              onChange={(e) => handleEmail(e)}
              placeholder="example@yu.ac.kr"
            />
            {!clickAuthkey ? (
              <button
                className="btn-validate"
                onClick={() => handleAuthorize()}
              >
                인증하기
              </button>
            ) : (
              <button
                className="btn-validate-clicked"
                onClick={() => handleAuthorize()}
              >
                재전송
              </button>
            )}
          </div>
          <div className="register-validate">
            <input
              className="register-input-email"
              id="authkey"
              name="authkey"
              value={authkey}
              onChange={(e) => handleAuthkey(e)}
              placeholder="인증번호"
            />
            {!clickAuthkey ? (
              <button className="btn-validate" onClick={() => checkAuthkey()}>
                인증확인
              </button>
            ) : (
              <button
                className="btn-validate-clicked"
                onClick={() => checkAuthkey()}
              >
                인증완료
              </button>
            )}
          </div>
          <h7 className="text-password">비밀번호</h7>
          <div className="register-info">
            <input
              className="register-input-password"
              id="password"
              name="password"
              value={password}
              onChange={(e) => handlePassword(e)}
              type="password"
              placeholder="특수문자, 영문, 숫자 포함 8자 이상"
            />
            {password && !passwordAvailability ? (
              <p className="register-text">
                특수문자, 문자, 숫자를 포함해 8자 이상 입력해주세요.
              </p>
            ) : null}
          </div>
          <h7 className="text-password-recheck">비밀번호확인</h7>
          <div className="register-info">
            <input
              className="register-input-password-recheck"
              id="checkPassword"
              name="checkPassword"
              value={checkPassword}
              onChange={(e) => handleCheckPassword(e)}
              type="password"
              placeholder="비밀번호와 동일"
            />
            {checkPassword && password !== checkPassword ? (
              <p className="register-text">비밀번호가 일치하지 않습니다.</p>
            ) : null}
          </div>
          <h7 className="text-name">이름</h7>
          <div className="register-info">
            <input
              className="register-input-name"
              id="username"
              name="username"
              value={username}
              onChange={(e) => handleUsername(e)}
              placeholder="이름"
            />
          </div>
          <button onClick={() => handleSignUp()} className="btn-register">
            가입하기
          </button>
        </div>
      </div>
      <FooterGray />
    </div>
  );
};

export default Register;
