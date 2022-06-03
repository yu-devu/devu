import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ReactPaginate from 'react-paginate';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import './questions.css';
import Submenu from '../Submenu';
import a from '../../../img/a.png';
import magnify from '../../../img/magnify.png';
import Footer from '../../Home/Footer';
import spring from '../../../img/spring.png';
import c from '../../../img/c.png';
import cpp from '../../../img/cpp.png';
import js from '../../../img/js.png';
import react from '../../../img/react.png';
import node_js from '../../../img/node_js.png';
import python from '../../../img/python.png';
import go from '../../../img/go.png';
import swift from '../../../img/swift.png';
import angular from '../../../img/angular.png';
import java from '../../../img/java.png';
import flutter from '../../../img/flutter.png';
import docker from '../../../img/docker.png';
import ruby from '../../../img/ruby.png';
import html from '../../../img/html.png';
import css from '../../../img/css.png';
import mysql from '../../../img/mysql.png';
import comment from '../../../img/comment.png';
import hit from '../../../img/hit.png';
import like from '../../../img/like.png';
import { Link } from 'react-router-dom';

const Questions = () => {
    const [currentPage, setCurrentPage] = useState(0);
    const [postSize, setPostSize] = useState(0);
    const [postsPerPage] = useState(20);
    const [postData, setPostData] = useState([]);
    const [lastIdx, setLastIdx] = useState(0);
    const [selectedTag, setSelectedTag] = useState([]);
    const [choiced, setChoiced] = useState(false);
    const [sentence, setSentence] = useState('');
    const [status, setStatus] = useState('');
    const [order, setOrder] = useState('');
    const onChangeSentence = (e) => {
        setSentence(e.target.value);
    };
    const username = localStorage.getItem('username');

    useEffect(() => {
        fetchData();
        fetchPageSize();
    }, [currentPage, selectedTag, status, order]);

    const fetchData = async () => {
        const res = await axios.get(
            process.env.REACT_APP_DB_HOST + `/community/questions`,
            {
                params: {
                    page: currentPage,
                    status: status,
                    order: order,
                    tags: selectedTag.join(','), // join(",")으로 해야 ?tags=REACT,SPRING으로 parameter 전송할 수 있음.
                    s: sentence,
                },
            }
        );

        const _postData = await res.data.map(
            (rowData) => (
                setLastIdx(lastIdx + 1),
                {
                    id: rowData.id,
                    title: rowData.title,
                    content: rowData.content,
                    hit: rowData.hit,
                    like: rowData.like,
                    username: rowData.username,
                    tags: rowData.tags,
                    questionsStatus: rowData.questionsStatus,
                    commentsSize: rowData.commentsSize,
                }
            )
        );
        setPostData(_postData);
        CKEditor.instances.textarea_id.getData();
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            fetchData();
        }
    };

    const handleTags = (tag) => {
        const _selectedTag = [...selectedTag];
        if (!_selectedTag.includes(tag)) {
            _selectedTag.push(tag);
        }
        setSelectedTag(_selectedTag);
    };

    const fetchPageSize = async () => {
        const res = await axios.get(
            process.env.REACT_APP_DB_HOST + `/community/questions/size`
        );
        setPostSize(res.data);
    };

    const changePage = ({ selected }) => {
        setCurrentPage(selected);
    };

    return (
        <div>
            <Submenu />
            <div className="all-questions">
                <div className="top-questions">
                    <h3 className="top-msg">쉽게 질문하고, 쉽게 대답하고</h3>
                    <h4 className="top-msg-gray">지식을 주고받아보세요!</h4>
                    {/* 상위 3개 게시물 */}
                    <div className="list-topcards">
                        {postData.slice(0, 3).map((top) => (
                            <li key={top.id} className="top-cards">
                                <div className="top-card">
                                    <div className="top-circle">
                                        <div className="top-profile">
                                            <img className="top-photo" src={a} alt="" />
                                        </div>
                                        <div className="top-detail">
                                            <div className="top-title">{top.title}</div>
                                            <div className="top-content">{top.content}</div>
                                            <div className="top-date">방금 전</div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </div>
                </div>
                <div className="body-questions">
                    <div className="cat-menu">
                        {status === "" ? <div className="cat-menu-items">
                            <p
                                className="cat-item-selected"
                                onClick={() => {
                                    setStatus('');
                                }}
                            >
                                전체
                            </p>
                            <p
                                className="cat-item"
                                onClick={() => {
                                    setStatus('SOLVED');
                                }}
                            >
                                해결
                            </p>
                            <p
                                className="cat-item"
                                onClick={() => {
                                    setStatus('UNSOLVED');
                                }}
                            >
                                미해결
                            </p>
                        </div> : status === "SOLVED" ? <div className="cat-menu-items">
                            <p
                                className="cat-item"
                                onClick={() => {
                                    setStatus('');
                                }}
                            >
                                전체
                            </p>
                            <p
                                className="cat-item-selected"
                                onClick={() => {
                                    setStatus('SOLVED');
                                }}
                            >
                                해결
                            </p>
                            <p
                                className="cat-item"
                                onClick={() => {
                                    setStatus('UNSOLVED');
                                }}
                            >
                                미해결
                            </p>
                        </div> : <div className="cat-menu-items">
                            <p
                                className="cat-item"
                                onClick={() => {
                                    setStatus('');
                                }}
                            >
                                전체
                            </p>
                            <p
                                className="cat-item"
                                onClick={() => {
                                    setStatus('SOLVED');
                                }}
                            >
                                해결
                            </p>
                            <p
                                className="cat-item-selected"
                                onClick={() => {
                                    setStatus('UNSOLVED');
                                }}
                            >
                                미해결
                            </p>
                        </div>}

                    </div>
                    <div className="search-and-write">
                        <div className="questions-search">
                            <input
                                type="text"
                                placeholder="맞춤 스터디그룹을 찾아보세요"
                                className="search-input"
                                onChange={(e) => {
                                    onChangeSentence(e);
                                }}
                                onKeyPress={handleKeyPress}
                            />
                            <button
                                className="btn-mag"
                                onClick={() => {
                                    fetchData();
                                }}
                            >
                                <img className="img-mag" src={magnify} alt="" />
                            </button>
                        </div>
                        {username ? ( // 로그인 했을 때 글쓰기 버튼 활성화
                            <Link to="write">
                                <button className="btn-questions-write">글쓰기</button>
                            </Link>
                        ) : null}
                    </div>
                    <div className="choicing">
                        <div className="choice-tag">
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Spring');
                                }}
                            >
                                {selectedTag.includes('Spring') ? (
                                    <img className="img-choiced" src={spring} alt="" />
                                ) : (
                                    <img className="img-choice" src={spring} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('C');
                                }}
                            >
                                {selectedTag.includes('C') ? (
                                    <img className="img-choiced" src={c} alt="" />
                                ) : (
                                    <img className="img-choice" src={c} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('CPP');
                                }}
                            >
                                {selectedTag.includes('CPP') ? (
                                    <img className="img-choiced" src={cpp} alt="" />
                                ) : (
                                    <img className="img-choice" src={cpp} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('JS');
                                }}
                            >
                                {selectedTag.includes('JS') ? (
                                    <img className="img-choiced" src={js} alt="" />
                                ) : (
                                    <img className="img-choice" src={js} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('React');
                                }}
                            >
                                {selectedTag.includes('React') ? (
                                    <img className="img-choiced" src={react} alt="" />
                                ) : (
                                    <img className="img-choice" src={react} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('NodeJS');
                                }}
                            >
                                {selectedTag.includes('NodeJS') ? (
                                    <img className="img-choiced" src={node_js} alt="" />
                                ) : (
                                    <img className="img-choice" src={node_js} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Python');
                                }}
                            >
                                {selectedTag.includes('Python') ? (
                                    <img className="img-choiced" src={python} alt="" />
                                ) : (
                                    <img className="img-choice" src={python} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Go');
                                }}
                            >
                                {selectedTag.includes('Go') ? (
                                    <img className="img-choiced" src={go} alt="" />
                                ) : (
                                    <img className="img-choice" src={go} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Swift');
                                }}
                            >
                                {selectedTag.includes('Swift') ? (
                                    <img className="img-choiced" src={swift} alt="" />
                                ) : (
                                    <img className="img-choice" src={swift} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Angular');
                                }}
                            >
                                {selectedTag.includes('Angular') ? (
                                    <img className="img-choiced" src={angular} alt="" />
                                ) : (
                                    <img className="img-choice" src={angular} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Java');
                                }}
                            >
                                {selectedTag.includes('Java') ? (
                                    <img className="img-choiced" src={java} alt="" />
                                ) : (
                                    <img className="img-choice" src={java} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Flutter');
                                }}
                            >
                                {selectedTag.includes('Flutter') ? (
                                    <img className="img-choiced" src={flutter} alt="" />
                                ) : (
                                    <img className="img-choice" src={flutter} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Docker');
                                }}
                            >
                                {selectedTag.includes('Docker') ? (
                                    <img className="img-choiced" src={docker} alt="" />
                                ) : (
                                    <img className="img-choice" src={docker} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('Ruby');
                                }}
                            >
                                {selectedTag.includes('Ruby') ? (
                                    <img className="img-choiced" src={ruby} alt="" />
                                ) : (
                                    <img className="img-choice" src={ruby} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('HTML');
                                }}
                            >
                                {selectedTag.includes('HTML') ? (
                                    <img className="img-choiced" src={html} alt="" />
                                ) : (
                                    <img className="img-choice" src={html} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('CSS');
                                }}
                            >
                                {selectedTag.includes('CSS') ? (
                                    <img className="img-choiced" src={css} alt="" />
                                ) : (
                                    <img className="img-choice" src={css} alt="" />
                                )}
                            </button>
                            <button
                                className="btn-choice"
                                onClick={() => {
                                    handleTags('MySQL');
                                }}
                            >
                                {selectedTag.includes('MySQL') ? (
                                    <img className="img-choiced" src={mysql} alt="" />
                                ) : (
                                    <img className="img-choice" src={mysql} alt="" />
                                )}
                            </button>
                        </div>
                        <div className="body-content">
                            <select
                                className="select-questions"
                                onChange={(e) => {
                                    setOrder(e.target.value);
                                }}
                            >
                                <option value="">최신순</option>
                                <option value="likes">인기순</option>
                                <option value="comments">댓글순</option>
                            </select>
                            <div className="questions-line"></div>
                            {/* 게시물 미리보기 */}
                            {postData.slice(0, 20).map((post) => (
                                <li key={post.id} className="list-questions">
                                    <div className="post-questions">
                                        <div className="post-header">
                                            <div className='post-status'>
                                                {post.questionsStatus === 'UNSOLVED' ? '미해결' : '해결'}
                                            </div>
                                            <div className="post-title">
                                                <Link to={`/questionsDetail/${post.id}`}>
                                                    {post.title}
                                                </Link>
                                            </div>
                                        </div>
                                        <div className="post-body">
                                            <div className="post-content">{post.content}</div>
                                            <div className="post-options">
                                                <div className="post-comment">
                                                    <div className="text-comment">
                                                        {post.commentsSize}
                                                    </div>
                                                    <img className="img-comment" src={comment} alt="" />
                                                </div>
                                                <div className="post-hit">
                                                    <div className="text-hit">{post.hit}</div>
                                                    <img className="img-hit" src={hit} alt="" />
                                                </div>
                                                <div className="post-like">
                                                    <div className="text-like">{post.like}</div>
                                                    <img className="img-like" src={like} alt="" />
                                                </div>
                                            </div>
                                        </div>
                                        <div className="tags">
                                            {post.tags.map((tag) => (
                                                <div className="post-tag">{tag}</div>
                                            ))}
                                        </div>
                                        <div className="post-tail">
                                            <div className="post-owner">{post.username}</div>
                                            <div className="post-date">1분 전</div>
                                        </div>
                                        <div className="questions-line"></div>
                                    </div>
                                </li>
                            ))}
                            <ReactPaginate
                                previousLabel={'<'}
                                nextLabel={'>'}
                                pageCount={Math.ceil(postSize / postsPerPage)} // 페이지 버튼 개수 출력하는 부분 -> 글 전체 개수 넘겨받아서 사용해야함
                                onPageChange={changePage}
                                containerClassName={'btn-pagination'}
                                previousLinkClassName={'btn-pagination-previous'}
                                nextLinkClassName={'btn-pagination-next'}
                                disabledClassName={'btn-pagination-disabled'}
                                activeClassName={'btn-pagination-active'}
                            />
                        </div>
                    </div>
                    <Footer />
                </div>
            </div>
        </div>
    );
};

export default Questions;
