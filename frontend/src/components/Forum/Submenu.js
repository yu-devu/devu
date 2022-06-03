import React from 'react'
import { Link, useLocation } from 'react-router-dom'
import './submenu.css'

const Submenu = () => {
    const location = useLocation();

    let pathname = location.pathname;
    let [a, forumname] = pathname.split('/');

    return (
        <div className='sub-menu'>
            <div className='sub-menu-items'>
                {forumname === 'studies' || forumname === 'studiesDetail' ? <div>
                    <Link className='sub-item-selected' to="/studies">스터디구인란</Link>
                    <Link className='sub-item' to="/questions">Q&A</Link>
                    <Link className='sub-item' to="/chats">자유게시판</Link>
                </div> : forumname === 'questions' || forumname === 'questionsDetail' ? <div>
                    <Link className='sub-item' to="/studies">스터디구인란</Link>
                    <Link className='sub-item-selected' to="/questions">Q&A</Link>
                    <Link className='sub-item' to="/chats">자유게시판</Link>
                </div> : <div>
                    <Link className='sub-item' to="/studies">스터디구인란</Link>
                    <Link className='sub-item' to="/questions">Q&A</Link>
                    <Link className='sub-item-selected' to="/chats">자유게시판</Link>
                </div>}
            </div>
        </div>
    )
}

export default Submenu