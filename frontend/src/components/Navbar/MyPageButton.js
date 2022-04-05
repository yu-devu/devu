import React from 'react'
import { Link } from "react-router-dom";
import './myPageButton.css'

const MyPageButton = () => {
    return (
        <Link to="mypage">
            <button className="btn-mypage-nav">마이 페이지</button>
        </Link>
    )
}

export default MyPageButton