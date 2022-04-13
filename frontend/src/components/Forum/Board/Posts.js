import React from 'react'
import { Link } from 'react-router-dom'
import './posts.css'

const Posts = ({ postData }) => {
    return <ul className='list-group'>
        {postData.map(post => (
            <li key={post.id} className="list-group-item">
                <div className='title'>
                    <Link to={`/postDetail/${post.id}`}>{post.title}</Link>
                </div>
                <div className='owner'>
                    작성자 : {post.username}
                </div>
            </li>
        ))}
    </ul>
}

export default Posts