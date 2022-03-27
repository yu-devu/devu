import React, { useState } from "react";
import "./register.css";

const Register = () => {
    return (
        <div className="container-register">
            <h1 className="register">회원가입</h1>
            <div className="input-container">
                <input
                    className="register-input"
                    id="id"
                    name="id"
                    placeholder="아이디"
                />
                <input
                    className="register-input"
                    id="nickname"
                    name="nickname"
                    placeholder="비밀번호"
                />
                <div className="register-email">
                    <input
                        className="register-input"
                        id="email"
                        name="email"
                        placeholder="이메일"
                    />
                    <button className="btn-validate">인증하기</button>
                </div>
                <button className="btn-register">회원가입</button>
            </div>
        </div>
    );
};

export default Register;
