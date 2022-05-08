import React from 'react'
import { Link } from 'react-router-dom'
import './submenu.css'

const Submenu = () => {
    return (
        <div className='sub-menu'>
            <div className='sub-menu-items'>
                <Link className='sub-item' to="/studies">스터디구인란</Link>
                <Link className='sub-item' to="/questions">QNA</Link>
                <Link className='sub-item' to="/chats">자유게시판</Link>
            </div>
        </div>
    )
}

export default Submenu