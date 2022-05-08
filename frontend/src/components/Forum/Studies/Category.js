import React from 'react'
import { Link } from 'react-router-dom'
import './category.css'

const Category = () => {
    return (
        <div className='cat-menu'>
            <div className='cat-menu-items'>
                <Link className='cat-item' to="/studies">전체</Link>
                <Link className='cat-item' to="/questions">모집중</Link>
                <Link className='cat-item' to="/chats">모집완료</Link>
            </div>
        </div>
    )
}

export default Category