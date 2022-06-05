import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import './questionsView.css';
import Submenu from '../Submenu';
import ab from '../../../img/a.png';
import share from '../../../img/share.png';
import warning from '../../../img/warning.png';
import hit from '../../../img/hit.png';
import like from '../../../img/like.png';
import imgComment from '../../../img/comment.png';
import FooterGray from '../../Home/FooterGray';

const QuestionsView = () => {
  let now = new Date();
  let seconds = now.getSeconds();
  let hours = now.getHours() - 9;
  let minutes = now.getMinutes();
  let year = now.getFullYear();
  let month = now.getMonth() + 1;
  let date = now.getDate();

  const navigate = useNavigate();
  const location = useLocation();
  const [postData, setPostData] = useState([]);
  const [isLike, setLike] = useState(false);
  const username = localStorage.getItem('username');
  const [comment, setComment] = useState('');
  const onChangeComment = (e) => {
    setComment(e.target.value);
  };

  let pathname = location.pathname;
  let [a, b, postId] = pathname.split('/');
  var comment_num;
  // useLocation으로 pathname을 추출한 후, '/'를 기준으로 parameter를 분리함

  useEffect(() => {
    fetchData();
    // console.log(location);
    // console.log(isLike);
  }, [location, isLike]);

  const fetchData = async () => {
    const res = await axios.get(
      process.env.REACT_APP_DB_HOST + `/community/questions/${postId}`
    );
    // console.log(res.data);
    const _postData = {
      id: res.data.id,
      title: res.data.title,
      content: res.data.content,
      hit: res.data.hit,
      like: res.data.like,
      username: res.data.username,
      date: res.data.createAt.substr(0, 10),
      time: res.data.createAt.substr(11, 8),
      tags: res.data.tags,
      questionsStatus: res.data.questionsStatus,
      comments: res.data.comments,
    };
    setPostData(_postData);
    comment_num = res.data.comments.length;
  };

  const handleLike = async () => {
    const data = {
      username: username,
      postId: postData.id,
    };
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/api/like`, JSON.stringify(data), {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then((res) => {
        console.log('res.data', res.data);
        if (res.data.liked) setLike(true);
        else setLike(false);
      })
      .catch((res) => {
        console.log(res);
      });
  };

  const handleDelete = async () => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/community/question/${postId}`)
        .then(() => {
          console.log('삭제 성공!');
          navigate(-1);
        })
        .catch((res) => console.log(res));
    } else {
      alert('취소하였습니다!');
    }
  };

  const handleComment = async () => {
    if (comment !== '') {
      const data = {
        username: username,
        postId: postId,
        contents: comment,
        // parent: parent,
        // group: group,
      };

      await axios
        .post(
          process.env.REACT_APP_DB_HOST + `/api/comments`,
          JSON.stringify(data),
          {
            headers: {
              'Content-Type': 'application/json',
            },
          }
        )
        .then((res) => {
          console.log(res);
        })
        .catch((res) => {
          console.log(res);
        });
    } else {
      alert('댓글을 작성해주세요!');
    }
  };

  return (
    <div>
      <Submenu />
      <div>
        {postData ? (
          <div className="questions-view">
            <div className="questions-detail-top">
              <div className="questions-contents-all">
                <div className="questions-detail-top">
                  <div className="questions-profile">
                    <img className="questions-photo" src={ab} alt="" />
                  </div>
                  <div className="questions-owner">{postData.username}</div>
                  <div className="questions-date">
                    {postData.date} {postData.time}
                  </div>
                </div>
                <div className="questions-top">
                  <div className="questions-status">
                    {postData.questionsStatus === 'SOLVED' ? '해결' : '미해결'}
                  </div>
                  <div className="questions-title">{postData.title}</div>
                </div>
                <div className="questions-content">{postData.content}</div>
              </div>
              <div className="questions-sidebar">
                <div className="questions-sidebar-status">
                  {postData.questionsStatus === 'SOLVED' ? '해결' : '미해결'}
                </div>
                <div className="questions-sidebar-item">
                  <img className="img-detail-hit" src={hit} alt="" />
                  <h8 className="detail-sidebar-text">{postData.hit}</h8>
                </div>
                <div className="questions-sidebar-btn">
                  <img className="img-detail-like" src={like} alt="" />
                  <button className="detail-sidebar-btn">
                    {postData.like}
                  </button>
                </div>
                <div className="questions-sidebar-btn">
                  <img className="img-detail-like" src={share} alt="" />
                  <button className="detail-sidebar-btn">공유</button>
                </div>
                <div className="questions-sidebar-btn">
                  <img className="img-detail-like" src={warning} alt="" />
                  <button className="detail-sidebar-btn">신고</button>
                </div>
              </div>
            </div>
            <div className="questions-content-bottom">
              <div className="questions-tags">
                {/* {postData.tags} */}
                {postData.tags &&
                  postData.tags.map((tag) => (
                    <div className="questions-tag">{tag}</div>
                  ))}
              </div>
              {postData.username === username ? (
                <div className="questions-btns">
                  <Link
                    className="btn-modify"
                    to={`/questionsDetail/${postId}/modify`}
                  >
                    수정
                  </Link>
                  <button
                    className="btn-delete-post"
                    onClick={() => {
                      handleDelete();
                    }}
                  >
                    삭제
                  </button>
                </div>
              ) : null}
            </div>
            <div className="questions-detail-bottom">
              <div className="questions-write-comments">
                <input
                  id="comment"
                  name="comment"
                  value={comment}
                  onChange={(e) => onChangeComment(e)}
                  placeholder="댓글을 달아주세요."
                />
                <button
                  className="btn-comment"
                  onClick={() => {
                    handleComment();
                  }}
                >
                  댓글달기
                </button>
              </div>
              {postData.comments ? (
                <div className="questions-comments-all">
                  <div className="number-comments">
                    {/* <h6 className="number-comments-text">개의 답글</h6> */}
                  </div>
                  <div className="questions-comments">
                    {postData.comments &&
                      postData.comments.map((comment) => (
                        <div className="container-comments">
                          <div className="comment-detail">
                            <div className="comments-top">
                              <div>
                                <img
                                  className="comment-photo"
                                  src={ab}
                                  alt=""
                                />
                              </div>
                              <div className="comment-owner">학생1</div>
                            </div>
                            <hr className="comment-line" />
                            <div className="comment-content">
                              {comment.contents}
                            </div>
                            <div className="comment-date">
                              {comment.createAt.slice(0, 4) == year
                                ? comment.createAt.slice(5, 7) == month &&
                                  comment.createAt.slice(8, 10) == date
                                  ? comment.createAt.slice(11, 13) == hours
                                    ? comment.createAt.slice(14, 16) == minutes
                                      ? seconds -
                                        comment.createAt.slice(17, 19) +
                                        '초 전'
                                      : minutes -
                                          comment.createAt.slice(14, 16) ==
                                          1 &&
                                        seconds < comment.createAt.slice(17, 19)
                                      ? 60 -
                                        comment.createAt.slice(17, 19) +
                                        seconds +
                                        '초 전'
                                      : minutes -
                                        comment.createAt.slice(14, 16) +
                                        '분 전'
                                    : hours -
                                      comment.createAt.slice(11, 13) +
                                      '시간 전'
                                  : comment.createAt.slice(5, 7) +
                                    '.' +
                                    comment.createAt.slice(8, 10)
                                : comment.createAt.slice(2, 4) +
                                  '.' +
                                  comment.createAt.slice(5, 7) +
                                  '.' +
                                  comment.createAt.slice(8, 10)}
                            </div>
                            <div className="comments-options">
                              <div className="comment-comment">
                                <img
                                  className="img-comment-comment"
                                  src={imgComment}
                                  alt=""
                                />
                                0
                              </div>
                              <div className="comment-like">
                                <img
                                  className="img-comment-like"
                                  src={like}
                                  alt=""
                                  onClick={() => {
                                    handleLike();
                                  }}
                                />
                                0
                              </div>
                            </div>
                          </div>
                        </div>
                      ))}
                  </div>
                </div>
              ) : null}
              <FooterGray />
            </div>
          </div>
        ) : (
          '해당 게시글을 찾을 수 없습니다.'
        )}
      </div>
    </div>
  );
};

export default QuestionsView;
