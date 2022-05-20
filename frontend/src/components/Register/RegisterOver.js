import React from 'react'
import { Link } from 'react-router-dom'
import './registerOver.css'

const RegisterOver = () => {
    return (
        <div className='registerover-all'>
            <img />
            <h1>데뷰 회원가입이 완료되었습니다.</h1>
            <h2>지금 바로 다양한 서비스를 확인해보세요</h2>
            <div className='registerover-btns'>
                <Link className='btn-to-main' to="/">메인으로</Link>
                <button className='btn-to-login' >로그인</button>
            </div>
        </div>
    )
}

export default RegisterOver