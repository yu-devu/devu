import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';

function ChangePassword() {
    const navigate = useNavigate();
    const location = useLocation();

    const [password, setPassword] = useState('');
    const [checkPassword, setCheckPassword] = useState('');
    const [passwordAvailability, setPasswordAvailability] = useState(false);
    let pathname = location.pathname;
    let [a, b, email] = pathname.split('/'); // 추후에 바꿔야 함
    // console.log(email)

    // const [email, setEmail] = useState('');
    // setEmail(response);
    // console.log(email)

    const onChangePassword = (e) => {
        var regExp = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
        setPassword(e.target.value);
        setPasswordAvailability(regExp.test(e.target.value));
    }
    const onChangeCheckPassword = (e) => setCheckPassword(e.target.value);

    useEffect(() => {
        getChangePassword();
    }, []);

    const getChangePassword = async () => {
        await axios.get(process.env.REACT_APP_DB_HOST + `/api/change_password/${email}`)
            .then((res) => { console.log(res) });
    }

    const postChangePassword = async () => {
        if (password === checkPassword) {
            if (passwordAvailability == true) {
                const data = {
                    password: password,
                }

                await axios.post(process.env.REACT_APP_DB_HOST + `/api/change_password/${email}`, JSON.stringify(data), {
                    headers: {
                        'Content-Type': 'application/json',
                    },
                }).then((res) => {
                    console.log(res);
                    alert('비밀번호 변경 완료');
                    navigate('/');
                }
                ).catch((res) => {
                    // alert(JSON.parse(res.request.response).error);
                    alert("error");
                    console.log(res);
                });
            } else alert('비밀번호를 양식에 맞게 입력해주세요.');
        } else alert('비밀번호가 일치하지 않습니다.');
    }

    return (
        <>
            <p>변경할 비밀번호를 입력해주세요.</p>
            <div>
                <input
                    id="password"
                    name="password"
                    value={password}
                    type="password"
                    onChange={(e) => onChangePassword(e)}
                    placeholder="비밀번호"
                />
                {password && !passwordAvailability ? (
                    <p>특수문자, 문자, 숫자를 포함해 8자 이상 입력해주세요.</p>
                ) : null}
            </div>
            <div>
                <input
                    id="checkPassword"
                    name="checkPassword"
                    value={checkPassword}
                    type="password"
                    onChange={(e) => onChangeCheckPassword(e)}
                    placeholder="비밀번호 확인"
                />
                {checkPassword && password !== checkPassword ? (
                    <p>비밀번호가 일치하지 않습니다.</p>
                ) : null}
            </div>
            <div>
                <button onClick={() => { postChangePassword(); }}>비밀번호 변경</button>
            </div>
        </>
    );
}

export default ChangePassword;