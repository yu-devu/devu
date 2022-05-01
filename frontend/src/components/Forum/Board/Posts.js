import React from 'react'
import { Link } from 'react-router-dom'
import './posts.css'

const Posts = ({ postData, pagesVisited, postsPerPage }) => {
    return (
        <ul className='list-group'>
            {postData.slice(pagesVisited, pagesVisited + postsPerPage).map(post => (
                <li key={post.id} className="list-group-item">
                    <div className='title'>
                        <Link to={`/postDetail/${post.id}`}>{post.title}</Link>
                    </div>
                    <div className='owner'>
                        작성자 : {post.username} 조회수 : {post.hit} 좋아요 : {post.like}
                    </div>

                </li>
            ))}
        </ul>
    );
}

export default Posts