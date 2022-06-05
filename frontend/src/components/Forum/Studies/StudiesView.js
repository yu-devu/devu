import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useLocation, Link, useNavigate } from 'react-router-dom';
import './studiesView.css';
import Submenu from '../Submenu';
import ab from '../../../img/a.png';
import share from '../../../img/share.png';
import warning from '../../../img/warning.png';
import hit from '../../../img/hit.png';
import like from '../../../img/like.png';
import imgComment from '../../../img/comment.png';
import more from '../../../img/more.png';
import FooterGray from '../../Home/FooterGray';

const StudiesView = () => {
  let now = new Date();
  let hours = now.getHours() - 9;
  let minutes = now.getMinutes();
  let seconds = now.getSeconds();
  let year = now.getFullYear();
  let month = now.getMonth() + 1;
  let date = now.getDate();

  const navigate = useNavigate();
  const location = useLocation();
  const [postData, setPostData] = useState([]);
  const [isLike, setLike] = useState(false);
  const username = localStorage.getItem('username');
  const [comment, setComment] = useState('');
  const [modifycomment, setModifyComment] = useState('');
  const [dropdown, setDropdown] = useState(false);
  const [commentModifyMode, handleCommentModifyMode] = useState(0);
  const onChangeComment = (e) => {
    setComment(e.target.value);
  };
  const onChangeModifyComment = (e) => {
    setModifyComment(e.target.value);
  };

  // const moreButton = document.getElementById('btn-more');

  // moreButton.addEventListener('click', () => {
  //   const dropdown = document.querySelector('more-submenu');
  //   dropdown.style.display = 'block';
  // });

  let pathname = location.pathname;
  let [a, b, postId] = pathname.split('/');
  var comment_num;
  // useLocation으로 pathname을 추출한 후, '/'를 기준으로 parameter를 분리함

  useEffect(() => {
    fetchData();
    window.scrollTo(0, 0);
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
      date: res.data.createAt.substr(0, 10),
      hours: Number(res.data.createAt.substr(11, 2)) + 9,
      minutes: Number(res.data.createAt.substr(14, 2)),
      seconds: Number(res.data.createAt.substr(17, 2)),
      tags: res.data.tags,
      studyStatus: res.data.studyStatus,
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

  const handlePostDelete = async () => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/community/study/${postId}`)
        .then(() => {
          console.log('삭제 성공!');
          navigate(-1);
        })
        .catch((res) => console.log(res));
    } else {
      alert('취소하였습니다!');
    }
  };

  const handleCommentDelete = async (id) => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/api/comments/${id}`)
        .then(() => {
          console.log('삭제 성공!');
          navigate(0);
        })
        .catch((res) => console.log(res));
    } else {
      alert('취소하였습니다!');
    }
  };

  const handleStatus = async () => {
    const data = {
      postId: postData.id,
      username: postData.username,
    };
    await axios
      .post(
        process.env.REACT_APP_DB_HOST + `/api/change/study_status`,
        JSON.stringify(data),
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )
      .then(() => {
        navigate(0);
      })
      .catch((res) => {
        console.log(res);
      });
  };

  const handleCommentModify = async (id) => {
    const data = {
      contents: modifycomment,
    };

    await axios
      .patch(
        process.env.REACT_APP_DB_HOST + `/api/comments/${id}`,
        JSON.stringify(data),
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `${localStorage.getItem('accessToken')}`,
            // 'X-AUTH-ACCESS-TOKEN': `${localStorage.getItem('accessToken')}`,
          },
        }
      )
      .then(() => {
        navigate(0);
      })
      .catch((res) => {
        console.log(res);
      });
  };

  console.log(postData.hours + ':' + postData.minutes + ':' + postData.seconds);

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
          navigate(0);
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
          <div className="studies-view">
            <div className="studies-detail-top">
              <div className="studies-contents-all">
                <div className="studies-detail-top">
                  <div className="studies-profile">
                    <img className="studies-photo" src={ab} alt="" />
                  </div>
                  <div className="studies-owner">{postData.username}</div>
                  <div className="studies-date">
                    {postData.date} {postData.hours}:{postData.minutes}:
                    {postData.seconds}
                  </div>
                </div>
                <div className="studies-top">
                  <div className="studies-status">
                    {postData.studyStatus === 'ACTIVE' ? '모집중' : '모집완료'}
                  </div>
                  <div className="studies-title">{postData.title}</div>
                </div>
                <div className="studies-content">{postData.content}</div>
              </div>
              <div className="studies-sidebar">
                {postData.username === username ? (
                  <button
                    className="studies-sidebar-status"
                    onClick={() => handleStatus()}
                  >
                    {postData.studyStatus === 'ACTIVE' ? '모집중' : '모집완료'}
                  </button>
                ) : (
                  <div className="studies-sidebar-status">
                    {postData.studyStatus === 'ACTIVE' ? '모집중' : '모집완료'}
                  </div>
                )}
                <div className="studies-sidebar-item">
                  <img className="img-detail-hit" src={hit} alt="" />
                  <h8 className="detail-sidebar-text">{postData.hit}</h8>
                </div>
                <div className="studies-sidebar-btn">
                  <img className="img-detail-like" src={like} alt="" />
                  <button
                    className="detail-sidebar-btn"
                    onClick={() => {
                      handleLike();
                    }}
                  >
                    {postData.like}
                  </button>
                </div>
                <div className="studies-sidebar-btn">
                  <img className="img-detail-like" src={share} alt="" />
                  <button className="detail-sidebar-btn">공유</button>
                </div>
                <div className="studies-sidebar-btn">
                  <img className="img-detail-like" src={warning} alt="" />
                  <button className="detail-sidebar-btn">신고</button>
                </div>
              </div>
            </div>
            <div className="studies-content-bottom">
              <div className="studies-tags">
                {postData.tags &&
                  postData.tags.map((tag) => (
                    <div className="studies-tag">{tag}</div>
                  ))}
              </div>
              {postData.username === username ? (
                <div className="studies-btns">
                  <Link
                    className="btn-modify"
                    to={`/studiesDetail/${postId}/modify`}
                  >
                    수정
                  </Link>
                  <button
                    className="btn-delete-post"
                    onClick={() => {
                      handlePostDelete();
                    }}
                  >
                    삭제
                  </button>
                </div>
              ) : null}
            </div>
            <div className="studies-detail-bottom">
              <div className="studies-write-comments">
                <input
                  className='comment'
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
                <div className="studies-comments-all">
                  <div className="number-comments">
                    {/* <h6 className="number-comments-text">개의 답글</h6> */}
                  </div>
                  <div div className="studies-comments">
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
                              <div className="comment-top">
                                <div className="comment-owner">
                                  {comment.username}
                                </div>
                                {comment.username === username ? (
                                  <button className="btn-more"
                                    onClick={() => {
                                      if (dropdown) setDropdown(false);
                                      else setDropdown(true);
                                    }}
                                    onBlur={() => {
                                      setDropdown(false)
                                    }}>
                                    <img
                                      className="img-more"
                                      alt=""
                                      src={more}
                                    />
                                    {dropdown && (
                                      <ul className='more-submenu'>
                                        <button
                                          onClick={() => {
                                            handleCommentModifyMode(
                                              comment.commentId
                                            );
                                          }}
                                        >
                                          수정
                                        </button>
                                        <button
                                          onClick={() => {
                                            handleCommentDelete(
                                              comment.commentId
                                            );
                                          }}
                                        >
                                          삭제
                                        </button>
                                      </ul>
                                    )}
                                  </button>
                                ) : null}
                              </div>
                            </div>

                            {comment.commentId === commentModifyMode ? (
                              <div className="questions-write-comments">
                                <input
                                  id="comment"
                                  name="comment"
                                  defaultValue={comment.contents}
                                  //   value={modifycomment}
                                  onChange={(e) => onChangeModifyComment(e)}
                                />

                                <button
                                  className="btn-comment"
                                  onClick={() => {
                                    handleCommentModify(comment.commentId);
                                  }}
                                >
                                  수정하기
                                </button>
                              </div>
                            ) : (
                              <div className="comment-content">
                                {comment.contents}
                              </div>
                            )}

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

export default StudiesView;
