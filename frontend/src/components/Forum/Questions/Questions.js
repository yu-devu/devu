import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ReactPaginate from 'react-paginate'
import { CKEditor } from '@ckeditor/ckeditor5-react';
import './questions.css'
import Submenu from '../Submenu'
import magnify from "../../../img/magnify.png"
import Footer from '../../Home/Footer'
import comment from "../../../img/comment.png"
import hit from "../../../img/hit.png"
import like from "../../../img/like.png"
import { Link } from 'react-router-dom';

const Questions = () => {
    const [currentPage, setCurrentPage] = useState(0);
    const [postSize, setPostSize] = useState(0);
    const [postsPerPage] = useState(20);
    const [postData, setPostData] = useState([]);
    const [lastIdx, setLastIdx] = useState(0);
    const [selectedTag, setSelectedTag] = useState([]);
    const [sentence, setSentence] = useState('');
    const [status, setStatus] = useState('');
    const [order, setOrder] = useState('');
    const onChangeSentence = (e) => { setSentence(e.target.value); }
    const username = localStorage.getItem('username');

    useEffect(() => {
        fetchData();
        fetchPageSize();
    }, [currentPage, selectedTag, status, order]);

    const fetchData = async () => {
        const res = await axios.get(process.env.REACT_APP_DB_HOST + `/community/questions`, {
            params: {
                page: currentPage,
                tags: selectedTag.join(","), // join(",")으로 해야 ?tags=REACT,SPRING으로 parameter 전송할 수 있음.
                s: sentence,
                status: status,
                order: order,
            },
        });

        const _postData = await res.data.map(
            (rowData) => (
                setLastIdx(lastIdx + 1),
                {
                    id: rowData.id,
                    title: rowData.title,
                    content: (rowData.content),
                    hit: rowData.hit,
                    like: rowData.like,
                    username: rowData.username,
                    questionsStatus: rowData.questionStatus,
                    commentsSize: rowData.commentsSize,
                }
            )
        );
        setPostData(_postData);
        CKEditor.instances.textarea_id.setData(postData.content);
        console.log(postData.content);
        CKEditor.instances.textarea_id.getData();
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            fetchData();
        }
    }

    const fetchPageSize = async () => {
        const res = await axios.get(process.env.REACT_APP_DB_HOST + `/community/questions/size`);
        setPostSize(res.data);
    }

    const changePage = ({ selected }) => {
        setCurrentPage(selected)
    }

    return (
        <div>
            <Submenu />
            <div className='all-question'>
                <div className='top-question'>
                    <h3 className='top-msg'>쉽게 질문하고, 쉽게 대답하고</h3>
                    <h4 className='top-msg-gray'>지식을 주고받아보세요!</h4>
                    {/* 상위 3개 게시물 */}
                    <div className='list-topcards'>
                        {postData.slice(0, 3).map(top => (
                            <li key={top.id} className="top-cards">
                                <div className='top-card'>
                                    <div className='top-circle-q'>
                                        <div className='top-detail'>
                                            <div className='top-title'>{top.title}</div>
                                            <div className='top-content'>{top.content}</div>
                                            <div className='top-date'>방금 전</div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </div>
                </div>
                <div className='body-question'>
                    <div className='cat-menu'>
                        <div className='cat-menu-items'>
                            <p className='cat-item' onClick={() => { setStatus(''); }}>전체</p>
                            <p className='cat-item' onClick={() => { setStatus('SOLVED') }}>해결</p>
                            <p className='cat-item' onClick={() => { setStatus('UNSOLVED') }}>미해결</p>
                        </div>
                    </div>
                    <div className='search-and-write'>
                        <div className='question-search'>
                            <input
                                type='text'
                                placeholder='궁금한 질문을 검색해보세요'
                                className='search-input'
                                onChange={(e) => { onChangeSentence(e); }}
                                onKeyPress={handleKeyPress}
                            />
                            <button className='btn-mag' onClick={() => { fetchData(); }}>
                                <img className='img-mag' src={magnify} alt="" />
                            </button>
                        </div>
                        {
                            username // 로그인 했을 때 글쓰기 버튼 활성화
                                ?
                                <Link to="write">
                                    <button className='btn-question-write'>글쓰기</button>
                                </Link>
                                : null
                        }
                    </div>
                    <div className='body-content-questions'>
                        <select className='select-question' onChange={(e) => { setOrder(e.target.value); }}>
                            <option value="">최신순</option>
                            <option value="likes">인기순</option>
                            <option value="comments">댓글순</option>
                        </select>
                        <div className='question-line'></div>
                        {/* 게시물 미리보기 */}
                        {postData.slice(0, 20).map(post => (
                            <li key={post.id} className="list-question">
                                <div className='post-question'>
                                    <div className='post-header'>
                                        <div className='post-status'>{post.questionsStatus === 'UNSOLVED' ? '미해결' : '해결'}</div>
                                        <div className='post-title'>
                                            <Link to={`/questionsDetail/${post.id}`}>{post.title}</Link>
                                        </div>
                                    </div>
                                    <div className='post-body'>
                                        <div className='post-content'>
                                            {post.content}
                                        </div>
                                        <div className='post-options'>
                                            <div className='post-comment'>
                                                <div className='text-comment'>
                                                    {post.commentsSize}
                                                </div>
                                                <img className="img-comment" src={comment} alt='' />
                                            </div>
                                            <div className='post-hit'>
                                                <div className='text-hit'>{post.hit}</div>
                                                <img className="img-hit" src={hit} alt='' />
                                            </div>
                                            <div className='post-like'>
                                                <div className='text-like'>{post.like}</div>
                                                <img className="img-like" src={like} alt='' />
                                            </div>
                                        </div>
                                    </div>
                                    <div className='post-tail'>
                                        <div className='post-owner'>{post.username}</div>
                                        <div className='post-date'>1분 전</div>
                                    </div>
                                    <div className='question-line'></div>
                                </div>
                            </li>
                        ))}
                        <ReactPaginate
                            previousLabel={"<"}
                            nextLabel={">"}
                            pageCount={Math.ceil(postSize / postsPerPage)} // 페이지 버튼 개수 출력하는 부분 -> 글 전체 개수 넘겨받아서 사용해야함
                            onPageChange={changePage}
                            containerClassName={"btn-pagination"}
                            previousLinkClassName={"btn-pagination-previous"}
                            nextLinkClassName={"btn-pagination-next"}
                            disabledClassName={"btn-pagination-disabled"}
                            activeClassName={"btn-pagination-active"}
                        />
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    )
}

export default Questions