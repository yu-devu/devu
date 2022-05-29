import React, { useState, useEffect } from "react";
import axios from "axios";
import { useLocation, Link, useNavigate } from "react-router-dom";
import "./studyView.css";
import Submenu from "../Submenu";
import ab from "../../../img/a.png"
import hit from "../../../img/hit.png"
import like from "../../../img/like.png"
import imgComment from "../../../img/comment.png"
import FooterGray from "../../Home/FooterGray";


const StudiesView = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [postData, setPostData] = useState([]);
    const [isLike, setLike] = useState(false);
    const username = localStorage.getItem("username");
    // const [comment, setComment] = useState('');
    // const onChangeComment = (e) => { setComment(e.target.value) }

    let pathname = location.pathname;
    let [a, b, postId] = pathname.split("/");
    var comment_num;
    // useLocation으로 pathname을 추출한 후, '/'를 기준으로 parameter를 분리함

    useEffect(() => {
        fetchData();
        // console.log(location);
        // console.log(isLike);
    }, [location, isLike]);

    const fetchData = async () => {
        const res = await axios.get(
            process.env.REACT_APP_DB_HOST + `/community/studies/${postId}`
        );
        // console.log(res.data);
        const _postData = {
            id: res.data.id,
            title: res.data.title,
            content: res.data.content,
            hit: res.data.hit,
            like: res.data.like,
            username: res.data.username,
            createAt: res.data.createAt,
            tags: res.data.tags,
            studyStatus: res.data.studyStatus,
            comments: res.data.comments,
        };
        setPostData(_postData);
        comment_num = res.data.comments.length
    };

    const handleLike = async () => {
        const data = {
            username: username,
            postId: postData.id,
        };
        await axios.post(process.env.REACT_APP_DB_HOST + `/api/like`, JSON.stringify(data), {
            headers: {
                'Content-Type': 'application/json',
            }
        }).then((res) => {
            console.log("res.data", res.data);
            if (res.data.liked) setLike(true);
            else setLike(false);
        })
            .catch((res) => { });
    };


    const handleDelete = async () => {
        await axios.delete(process.env.REACT_APP_DB_HOST + `/community/chat/${postId}`)
            .then(() => {
                console.log("삭제 성공!");
                navigate(-1);
            })
            .catch((res) => console.log(res));
    };

    return (
        <div>
            <Submenu />
            <div>
                {postData ? (
                    <div className="studies-view">
                        <div className="studies-contents-all">
                            <div className="studies-detail-top">
                                <div className="studies-profile">
                                    <img className="studies-photo" src={ab} alt="" />
                                </div>
                                <div className="studies-owner">
                                    {postData.username}
                                </div>
                                <div className="studies-date">{postData.createAt}</div>
                            </div>
                            <div className="studies-top">
                                <div className='studies-status'>{postData.studyStatus === 'ACTIVE' ? '모집중' : '모집완료'}</div>
                                <div className="studies-title">
                                    {postData.title}
                                </div>
                            </div>
                            <div className="studies-content">
                                {postData.content}
                            </div>
                            <div className="studies-tags">
                                {/* {postData.tags} */}
                                {postData.tags && postData.tags.map(tag => (
                                    <div className='studies-tag'>{tag}</div>
                                ))}
                            </div>
                            <div className="studies-options">
                                <div className="studies-hit">
                                    <img className="img-detail-hit" src={hit} alt="" />
                                    {postData.hit}</div>
                                <div className="studies-like">
                                    <img className="img-detail-like" src={like} alt="" />
                                    {postData.like}</div>
                            </div>
                        </div>
                        <div className="studies-detail-bottom">
                            <div className="studies-write-comments">
                                <input placeholder="댓글을 달아주세요."></input>
                                <button className="btn-comment">댓글달기</button>
                            </div>
                            {postData.comments ? <div className="studies-comments-all">
                                <div className="number-comments">
                                    <h6 className="number-comments-text">2 개의 답글</h6>
                                </div>
                                <div className="studies-comments">
                                    {postData.comments && postData.comments.map(comment => (
                                        <div className="container-comments">
                                            <div className="comment-detail">
                                                <div className="comments-top">
                                                    <div>
                                                        <img className="comment-photo" src={ab} alt="" />
                                                    </div>
                                                    <div className="comment-owner">학생1</div>
                                                </div>
                                                <hr className="comment-line" />
                                                <div className="comment-content">{comment.contents}</div>
                                                <div className="comment-date">5분 전</div>
                                                <div className="comments-options">
                                                    <div className="comment-comment">
                                                        <img className="img-comment-comment" src={imgComment} alt="" />0
                                                    </div>
                                                    <div className="comment-like">
                                                        <img className="img-comment-like" src={like} alt="" />0
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    ))}

                                </div>
                            </div> : <div></div>}
                            <FooterGray />
                        </div>
                    </div>
                ) : (
                    "해당 게시글을 찾을 수 없습니다."
                )
                }
            </div>

        </div>
    );
};

export default StudiesView;
