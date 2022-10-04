import Comments from '../Comments';
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
import like_color from '../../../img/like_color.png';
import imgComment from '../../../img/comment.png';
import more from '../../../img/more.png';
import FooterGray from '../../Home/FooterGray';
import { useMediaQuery } from 'react-responsive';
import LoadingSpinner from '../LoadingSpinner';
import '../loadingSpinner.css';

const QuestionsView = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [postData, setPostData] = useState([]);
  const [isLike, setLike] = useState(0);
  const username = localStorage.getItem('username');
  const [comment, setComment] = useState('');
  const [likePosts, setLikePosts] = useState([]);
  const isTabletOrMobile = useMediaQuery({ maxWidth: 1224 });
  const [loading, setLoading] = useState(true);

  let pathname = location.pathname;
  let [a, b, postId] = pathname.split('/');
  // useLocation으로 pathname을 추출한 후, '/'를 기준으로 parameter를 분리함

  useEffect(() => {
    fetchLikeData();
    handleGetLike();
  }, [isLike]);

  useEffect(() => {
    fetchData();
    window.scrollTo(0, 0);
    fetchLikeData();
  }, []);

  const fetchData = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/community/questions/${postId}`)
      .then((res) => {
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
          questionStatus: res.data.questionStatus,
          comments: res.data.comments,
        };
        setPostData(_postData);
        console.log(_postData.comments);
        setLoading(false);
      })
      .catch((e) => console.log(e));
  };

  const fetchLikeData = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/api/myLikes`)
      .then((res) => {
        const _likePosts = res.data.map((rowData) => rowData.id);
        setLikePosts(_likePosts);
      })
      .catch((e) => console.log(e));
  };

  const handlePostLike = async () => {
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
        console.log('res.data', res.data.liked);
        if (res.data.liked) setLike(like + 1);
        else setLike(like - 1);
      })
      .catch((e) => console.log(e));
  };

  const handleGetLike = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/api/like`, {
        params: {
          postId: postId,
        },
      })
      .then((res) => {
        setPostData({
          ...postData,
          like: res.data.likeSize,
        });
      })
      .catch((e) => console.log(e));
  };

  const handlePostDelete = async () => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/community/question/${postId}`)
        .then(() => navigate(-1))
        .catch((e) => console.log(e));
    } else alert('취소하였습니다!');
  };

  const handleStatus = async () => {
    const data = {
      postId: postData.id,
      username: postData.username,
    };
    await axios
      .post(
        process.env.REACT_APP_DB_HOST + `/api/change/question_status`,
        JSON.stringify(data),
        {
          headers: {
            'Content-Type': 'application/json',
          },
        }
      )
      .then(() => navigate(0))
      .catch((e) => console.log(e));
  };

  return (
    <div>
      <Submenu />
      {loading ? (
        <div>
          <LoadingSpinner />
        </div>
      ) : (
        <>
          {isTabletOrMobile ? (
            <div>
              {postData ? (
                <div className="questions-view">
                  {isTabletOrMobile ? (
                    <div className="questions-detail-top">
                      <div className="questions-contents-all">
                        <div className="questions-sidebar">
                          <div className="questions-sidebar-item">
                            <img className="img-detail-hit" src={hit} alt="" />
                            <h8 className="detail-sidebar-text">
                              {postData.hit}
                            </h8>
                          </div>
                          <div
                            className="questions-sidebar-btn"
                            onClick={() => handlePostLike()}
                          >
                            {likePosts.includes(postData.id) ? (
                              <img
                                className="img-detail-like"
                                src={like_color}
                                alt=""
                              />
                            ) : (
                              <img
                                className="img-detail-like"
                                src={like}
                                alt=""
                              />
                            )}
                            <button className="detail-sidebar-btn">
                              {postData.like}
                            </button>
                          </div>
                          <div className="questions-sidebar-btn">
                            <img
                              className="img-detail-like"
                              src={share}
                              alt=""
                            />
                            <button className="detail-sidebar-btn">공유</button>
                          </div>
                          <div className="questions-sidebar-btn">
                            <img
                              className="img-detail-like"
                              src={warning}
                              alt=""
                            />
                            <button className="detail-sidebar-btn">신고</button>
                          </div>
                        </div>
                        <div className="question-detail-top">
                          <div className="questions-profile">
                            <img className="questions-photo" src={ab} alt="" />
                          </div>
                          <div className="questions-owner">
                            {postData.username}
                          </div>
                          <div className="questions-date">
                            {postData.date} {postData.hours}:{postData.minutes}:
                            {postData.seconds}
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
                                  handlePostDelete();
                                }}
                              >
                                삭제
                              </button>
                            </div>
                          ) : null}
                        </div>
                        <div className="questions-top">
                          <div className="questions-status">
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </div>
                          <div className="questions-title">
                            {postData.title}
                          </div>
                        </div>
                        <div className="questions-content">
                          {postData.content}
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="questions-detail-top">
                      <div className="questions-contents-all">
                        <div className="questions-detail-top">
                          <div className="questions-profile">
                            <img className="questions-photo" src={ab} alt="" />
                          </div>
                          <div className="questions-owner">
                            {postData.username}
                          </div>
                          <div className="questions-date">
                            {postData.date} {postData.hours}:{postData.minutes}:
                            {postData.seconds}
                          </div>
                        </div>
                        <div className="questions-top">
                          <div className="questions-status">
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </div>
                          <div className="questions-title">
                            {postData.title}
                          </div>
                        </div>
                        <div className="questions-content">
                          {postData.content}
                        </div>
                      </div>
                      <div className="questions-sidebar">
                        {postData.username === username ? (
                          <button
                            className="questions-sidebar-status"
                            onClick={() => handleStatus()}
                          >
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </button>
                        ) : (
                          <div className="questions-sidebar-status">
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </div>
                        )}
                        <div className="questions-sidebar-item">
                          <img className="img-detail-hit" src={hit} alt="" />
                          <h8 className="detail-sidebar-text">
                            {postData.hit}
                          </h8>
                        </div>
                        <div
                          className="questions-sidebar-btn"
                          onClick={() => handlePostLike()}
                        >
                          <button className="detail-sidebar-btn">
                            {likePosts.includes(postData.id) ? (
                              <img
                                className="img-detail-like"
                                src={like_color}
                                alt=""
                              />
                            ) : (
                              <img
                                className="img-detail-like"
                                src={like}
                                alt=""
                              />
                            )}
                            {postData.like}
                          </button>
                        </div>
                        <div className="questions-sidebar-btn">
                          <img className="img-detail-like" src={share} alt="" />
                          <button className="detail-sidebar-btn">공유</button>
                        </div>
                        <div className="questions-sidebar-btn">
                          <img
                            className="img-detail-like"
                            src={warning}
                            alt=""
                          />
                          <button className="detail-sidebar-btn">신고</button>
                        </div>
                      </div>
                    </div>
                  )}
                  <div className="questions-content-bottom">
                    <div className="questions-tags">
                      {/* {postData.tags} */}
                      {postData.tags &&
                        postData.tags.map((tag) => (
                          <div className="questions-tag">{tag}</div>
                        ))}
                    </div>
                  </div>
                  <div className="questions-detail-bottom">
                    <Comments comments={postData.comments} />
                    {/* <FooterGray /> */}
                  </div>
                </div>
              ) : (
                '해당 게시글을 찾을 수 없습니다.'
              )}
            </div>
          ) : (
            <div>
              {postData ? (
                <div className="questions-view">
                  {isTabletOrMobile ? (
                    <div className="questions-detail-top">
                      <div className="questions-contents-all">
                        <div className="questions-sidebar">
                          <div className="questions-sidebar-item">
                            <img className="img-detail-hit" src={hit} alt="" />
                            <h8 className="detail-sidebar-text">
                              {postData.hit}
                            </h8>
                          </div>
                          <div
                            className="questions-sidebar-btn"
                            onClick={() => handlePostLike()}
                          >
                            {likePosts.includes(postData.id) ? (
                              <img
                                className="img-detail-like"
                                src={like_color}
                                alt=""
                              />
                            ) : (
                              <img
                                className="img-detail-like"
                                src={like}
                                alt=""
                              />
                            )}
                            <button className="detail-sidebar-btn">
                              {postData.like}
                            </button>
                          </div>
                          <div className="questions-sidebar-btn">
                            <img
                              className="img-detail-like"
                              src={share}
                              alt=""
                            />
                            <button className="detail-sidebar-btn">공유</button>
                          </div>
                          <div className="questions-sidebar-btn">
                            <img
                              className="img-detail-like"
                              src={warning}
                              alt=""
                            />
                            <button className="detail-sidebar-btn">신고</button>
                          </div>
                        </div>
                        <div className="question-detail-top">
                          <div className="questions-profile">
                            <img className="questions-photo" src={ab} alt="" />
                          </div>
                          <div className="questions-owner">
                            {postData.username}
                          </div>
                          <div className="questions-date">
                            {postData.date} {postData.hours}:{postData.minutes}:
                            {postData.seconds}
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
                                  handlePostDelete();
                                }}
                              >
                                삭제
                              </button>
                            </div>
                          ) : null}
                        </div>
                        <div className="questions-top">
                          <div className="questions-status">
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </div>
                          <div className="questions-title">
                            {postData.title}
                          </div>
                        </div>
                        <div className="questions-content">
                          {postData.content}
                        </div>
                      </div>
                    </div>
                  ) : (
                    <div className="questions-detail-top">
                      <div className="questions-contents-all">
                        <div className="questions-detail-top">
                          <div className="questions-profile">
                            <img className="questions-photo" src={ab} alt="" />
                          </div>
                          <div className="questions-owner">
                            {postData.username}
                          </div>
                          <div className="questions-date">
                            {postData.date} {postData.hours}:{postData.minutes}:
                            {postData.seconds}
                          </div>
                        </div>
                        <div className="questions-top">
                          <div className="questions-status">
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </div>
                          <div className="questions-title">
                            {postData.title}
                          </div>
                        </div>
                        <div className="questions-content">
                          {postData.content}
                        </div>
                      </div>
                      <div className="questions-sidebar">
                        {postData.username === username ? (
                          <button
                            className="questions-sidebar-status"
                            onClick={() => handleStatus()}
                          >
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </button>
                        ) : (
                          <div className="questions-sidebar-status">
                            {postData.questionStatus === 'SOLVED'
                              ? '해결'
                              : '미해결'}
                          </div>
                        )}
                        <div className="questions-sidebar-item">
                          <img className="img-detail-hit" src={hit} alt="" />
                          <h8 className="detail-sidebar-text">
                            {postData.hit}
                          </h8>
                        </div>
                        <div
                          className="questions-sidebar-btn"
                          onClick={() => handlePostLike()}
                        >
                          <button className="detail-sidebar-btn">
                            {likePosts.includes(postData.id) ? (
                              <img
                                className="img-detail-like"
                                src={like_color}
                                alt=""
                              />
                            ) : (
                              <img
                                className="img-detail-like"
                                src={like}
                                alt=""
                              />
                            )}
                            {postData.like}
                          </button>
                        </div>
                        <div className="questions-sidebar-btn">
                          <img className="img-detail-like" src={share} alt="" />
                          <button className="detail-sidebar-btn">공유</button>
                        </div>
                        <div className="questions-sidebar-btn">
                          <img
                            className="img-detail-like"
                            src={warning}
                            alt=""
                          />
                          <button className="detail-sidebar-btn">신고</button>
                        </div>
                      </div>
                    </div>
                  )}
                  <div className="questions-content-bottom">
                    <div className="questions-tags">
                      {/* {postData.tags} */}
                      {postData.tags &&
                        postData.tags.map((tag) => (
                          <div className="questions-tag">{tag}</div>
                        ))}
                    </div>
                    {postData.username === username ? (
                      <div className="studies-btns">
                        <Link
                          className="btn-modify"
                          to={`/questionsDetail/${postId}/modify`}
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
                  <div className="questions-detail-bottom">
                    <Comments comments={postData.comments} />
                    <FooterGray />
                  </div>
                </div>
              ) : (
                '해당 게시글을 찾을 수 없습니다.'
              )}
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default QuestionsView;
