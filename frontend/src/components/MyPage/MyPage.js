import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './myPage.css';
import { useNavigate, useLocation } from 'react-router-dom';
import a from '../../img/a.png';
import FooterGray from '../Home/FooterGray';

const MyPage = () => {
  useEffect(() => {
    fetchPostData();
    fetchLikeData();
  }, []);

  let now = new Date();
  let hours = now.getHours() - 9;
  let minutes = now.getMinutes();
  let seconds = now.getSeconds();
  let year = now.getFullYear();
  let month = now.getMonth() + 1;
  let date = now.getDate();

  const navigate = useNavigate();
  const [postData, setPostData] = useState([]);
  const [myPostStudy, setMyPostStudy] = useState([]);
  const [myPostQuestion, setMyPostQuestion] = useState([]);
  const [myPostChat, setMyPostChat] = useState([]);
  const [likeData, setLikeData] = useState([]);
  const [myLikeStudy, setMyLikeStudy] = useState([]);
  const [myLikeQuestion, setMyLikeQuestion] = useState([]);
  const [myLikeChat, setMyLikeChat] = useState([]);

  const [isPost, setIsPost] = useState(true); // 마이페이지 처음 렌더링 했을 때 작성한 글 먼저 보이도록 설정
  const [isPostStatus, setIsPostStatus] = useState('isStudy');
  const username = localStorage.getItem('username');

  //StduyFilter
  function filterStudy(element) {
    if (element.studyStatus !== null) {
      return true;
    }
  }
  //QuestionFilter
  function filterQuestion(element) {
    if (element.questionStatus !== null) {
      return true;
    }
  }
  //ChatFilter
  function filterChat(element) {
    if (element.questionStatus === null && element.studyStatus === null) {
      return true;
    }
  }

  const fetchPostData = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/api/myPosts`)
      .then((res) => {
        const _postData = res.data.map((rowData) => ({
          id: rowData.id,
          title: rowData.title,
          content: rowData.content,
          hit: rowData.hit,
          like: rowData.like,
          username: rowData.username,
          postYear: rowData.createAt.substr(0, 4),
          postMonth: rowData.createAt.substr(5, 2),
          postDay: rowData.createAt.substr(8, 2),
          postHour: rowData.createAt.substr(11, 2),
          postMinute: rowData.createAt.substr(14, 2),
          postSecond: rowData.createAt.substr(17, 2),
          date: rowData.createAt.substr(0, 10),
          time: rowData.createAt.substr(11, 8),
          tags: rowData.tags,
          studyStatus: rowData.studyStatus,
          questionStatus: rowData.questionStatus,
          commentsSize: rowData.commentsSize,
        }));
        setPostData(_postData);
        setMyPostStudy(_postData.filter(filterStudy));
        setMyPostQuestion(_postData.filter(filterQuestion));
        setMyPostChat(_postData.filter(filterChat));
      })
      .catch((e) => console.log(e));
  };

  const fetchLikeData = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/api/myLikes`)
      .then((res) => {
        const _likeData = res.data.map((rowData) => ({
          id: rowData.id,
          title: rowData.title,
          content: rowData.content,
          hit: rowData.hit,
          like: rowData.like,
          username: rowData.username,
          postYear: rowData.createAt.substr(0, 4),
          postMonth: rowData.createAt.substr(5, 2),
          postDay: rowData.createAt.substr(8, 2),
          postHour: rowData.createAt.substr(11, 2),
          postMinute: rowData.createAt.substr(14, 2),
          postSecond: rowData.createAt.substr(17, 2),
          date: rowData.createAt.substr(0, 10),
          time: rowData.createAt.substr(11, 8),
          tags: rowData.tags,
          studyStatus: rowData.studyStatus,
          questionStatus: rowData.questionStatus,
          commentsSize: rowData.commentsSize,
        }));
        setLikeData(_likeData);
        setMyLikeStudy(_likeData.filter(filterStudy));
        setMyLikeQuestion(_likeData.filter(filterQuestion));
        setMyLikeChat(_likeData.filter(filterChat));
      })
      .catch((e) => console.log(e));
  };

  const deleteAccount = async () => {
    if (window.confirm('정말 탈퇴하시겠습니까?')) {
      await axios
        .delete(process.env.REACT_APP_DB_HOST + `/api/user`)
        .then(() => {
          localStorage.removeItem('username');
          localStorage.removeItem('accessToken');
          alert('회원 탈퇴가 완료되었습니다.');
          navigate('/');
        })
        .catch((e) => console.log(e));
    } else alert('취소하였습니다!');
  };

  return (
    <div>
      <div className="mypage-top">
        <div className="profile-mypage">
          <img className="img-mypage" src={a} alt="" />
        </div>
        <div className="text-mypage">
          <h1>{username}님 안녕하세요</h1>
        </div>
        <div className='mypage-btns'>
          <button className="btn-change-info">정보변경</button>
          <button
            className="btn-delete-info"
            onClick={() => {
              deleteAccount();
            }}
          >
            회원탈퇴
          </button>
        </div>
      </div>
      <div className="tag-mypage">
        <button
          className="btn-mypage-tag"
          onClick={() => {
            {
              setIsPost(true);
              setIsPostStatus('isStudy');
            }
          }}
        >
          작성한글
        </button>
        <button
          className="btn-mypage-tag"
          onClick={() => {
            {
              setIsPost(false);
              setIsPostStatus('isStudy');
            }
          }}
        >
          좋아요
        </button>
      </div>
      <div className="submenu-mypage">
        {isPostStatus === 'isStudy' ? (
          <button
            className="btn-mypage-submenu-clicked"
            onClick={() => {
              setIsPostStatus('isStudy');
            }}
          >
            스터디구인란
          </button>
        ) : (
          <button
            className="btn-mypage-submenu"
            onClick={() => {
              setIsPostStatus('isStudy');
            }}
          >
            스터디구인란
          </button>
        )}
        {isPostStatus === 'isQuestion' ? (
          <button
            className="btn-mypage-submenu-clicked"
            onClick={() => {
              setIsPostStatus('isQuestion');
            }}
          >
            Q&A
          </button>
        ) : (
          <button
            className="btn-mypage-submenu"
            onClick={() => {
              setIsPostStatus('isQuestion');
            }}
          >
            Q&A
          </button>
        )}
        {isPostStatus === 'isChat' ? (
          <button
            className="btn-mypage-submenu-clicked"
            onClick={() => {
              setIsPostStatus('isChat');
            }}
          >
            자유게시판
          </button>
        ) : (
          <button
            className="btn-mypage-submenu"
            onClick={() => {
              setIsPostStatus('isChat');
            }}
          >
            자유게시판
          </button>
        )}
      </div>
      <hr className="line-mypage" />
      <table className="bottom-mypage">
        <tr className="cate-mypage">
          <th width="250">제목</th>
          <th width="150">게시일자</th>
          <th width="150">댓글</th>
          {
            {
              isStudy: <th width="150">상태</th>,
              isQuestion: <th width="150">상태</th>,
              isChat: null,
            }[isPostStatus]
          }
        </tr>
        {isPost // 작성한 글 / 좋아요한 글 구분
          ? {
            // 작성한 글에서 각 게시판 별로 구분
            isStudy: myPostStudy.slice(0, myPostStudy.length).map((post) => (
              <tr className="cate-mypage-detail">
                <td
                  className="cate-mypage-detail-title"
                  width="250"
                  onClick={() => {
                    navigate('/studiesDetail/' + post.id);
                  }}
                >
                  {post.title}
                </td>
                <td width="150">
                  {post.postYear == year
                    ? post.postMonth == month && post.postDay == date
                      ? post.postHour == hours
                        ? post.postMinute == minutes
                          ? seconds - post.postSecond + '초 전'
                          : minutes - post.postMinute == 1 &&
                            seconds < post.postSecond
                            ? 60 - post.postSecond + seconds + '초 전'
                            : minutes - post.postMinute + '분 전'
                        : hours - post.postHour + '시간 전'
                      : post.postMonth + '.' + post.postDay
                    : post.postYear.slice(2, 4) +
                    '.' +
                    post.postMonth +
                    '.' +
                    post.postDay}
                </td>
                <td width="150">{post.commentsSize}</td>
                <td width="150">{post.studyStatus}</td>
              </tr>
            )),
            isQuestion: myPostQuestion
              .slice(0, myPostQuestion.length)
              .map((post) => (
                <tr className="cate-mypage-detail">
                  <td
                    className="cate-mypage-detail-title"
                    width="250"
                    onClick={() => {
                      navigate('/questionsDetail/' + post.id);
                    }}
                  >
                    {post.title}
                  </td>
                  <td width="150">
                    {post.postYear == year
                      ? post.postMonth == month && post.postDay == date
                        ? post.postHour == hours
                          ? post.postMinute == minutes
                            ? seconds - post.postSecond + '초 전'
                            : minutes - post.postMinute == 1 &&
                              seconds < post.postSecond
                              ? 60 - post.postSecond + seconds + '초 전'
                              : minutes - post.postMinute + '분 전'
                          : hours - post.postHour + '시간 전'
                        : post.postMonth + '.' + post.postDay
                      : post.postYear.slice(2, 4) +
                      '.' +
                      post.postMonth +
                      '.' +
                      post.postDay}
                  </td>
                  <td width="150">{post.commentsSize}</td>
                  <td width="150">{post.questionStatus}</td>
                </tr>
              )),
            isChat: myPostChat.slice(0, myPostChat.length).map((post) => (
              <tr className="cate-mypage-detail">
                <td
                  className="cate-mypage-detail-title"
                  width="250"
                  onClick={() => {
                    navigate('/chatsDetail/' + post.id);
                  }}
                >
                  {post.title}
                </td>
                <td width="150">
                  {post.postYear == year
                    ? post.postMonth == month && post.postDay == date
                      ? post.postHour == hours
                        ? post.postMinute == minutes
                          ? seconds - post.postSecond + '초 전'
                          : minutes - post.postMinute == 1 &&
                            seconds < post.postSecond
                            ? 60 - post.postSecond + seconds + '초 전'
                            : minutes - post.postMinute + '분 전'
                        : hours - post.postHour + '시간 전'
                      : post.postMonth + '.' + post.postDay
                    : post.postYear.slice(2, 4) +
                    '.' +
                    post.postMonth +
                    '.' +
                    post.postDay}
                </td>
                <td width="150">{post.commentsSize}</td>
                <td width="150">{post.studyStatus}</td>
              </tr>
            )),
          }[isPostStatus]
          : {
            // 좋아요한 글에서 각 게시판 별로 구분
            isStudy: myLikeStudy.slice(0, myLikeStudy.length).map((post) => (
              <tr className="cate-mypage-detail">
                <td
                  className="cate-mypage-detail-title"
                  width="250"
                  onClick={() => {
                    navigate('/studiesDetail/' + post.id);
                  }}
                >
                  {post.title}
                </td>
                <td width="150">
                  {post.postYear == year
                    ? post.postMonth == month && post.postDay == date
                      ? post.postHour == hours
                        ? post.postMinute == minutes
                          ? seconds - post.postSecond + '초 전'
                          : minutes - post.postMinute == 1 &&
                            seconds < post.postSecond
                            ? 60 - post.postSecond + seconds + '초 전'
                            : minutes - post.postMinute + '분 전'
                        : hours - post.postHour + '시간 전'
                      : post.postMonth + '.' + post.postDay
                    : post.postYear.slice(2, 4) +
                    '.' +
                    post.postMonth +
                    '.' +
                    post.postDay}
                </td>
                <td width="150">{post.commentsSize}</td>
                <td width="150">{post.studyStatus}</td>
              </tr>
            )),
            isQuestion: myLikeQuestion
              .slice(0, myLikeQuestion.length)
              .map((post) => (
                <tr className="cate-mypage-detail">
                  <td
                    className="cate-mypage-detail-title"
                    width="250"
                    onClick={() => {
                      navigate('/questionsDetail/' + post.id);
                    }}
                  >
                    {post.title}
                  </td>
                  <td width="150">
                    {post.postYear == year
                      ? post.postMonth == month && post.postDay == date
                        ? post.postHour == hours
                          ? post.postMinute == minutes
                            ? seconds - post.postSecond + '초 전'
                            : minutes - post.postMinute == 1 &&
                              seconds < post.postSecond
                              ? 60 - post.postSecond + seconds + '초 전'
                              : minutes - post.postMinute + '분 전'
                          : hours - post.postHour + '시간 전'
                        : post.postMonth + '.' + post.postDay
                      : post.postYear.slice(2, 4) +
                      '.' +
                      post.postMonth +
                      '.' +
                      post.postDay}
                  </td>
                  <td width="150">{post.commentsSize}</td>
                  <td width="150">{post.questionStatus}</td>
                </tr>
              )),
            isChat: myLikeChat.slice(0, myLikeChat.length).map((post) => (
              <tr className="cate-mypage-detail">
                <td
                  className="cate-mypage-detail-title"
                  width="250"
                  onClick={() => {
                    navigate('/chatsDetail/' + post.id);
                  }}
                >
                  {post.title}
                </td>
                <td width="150">
                  {post.postYear == year
                    ? post.postMonth == month && post.postDay == date
                      ? post.postHour == hours
                        ? post.postMinute == minutes
                          ? seconds - post.postSecond + '초 전'
                          : minutes - post.postMinute == 1 &&
                            seconds < post.postSecond
                            ? 60 - post.postSecond + seconds + '초 전'
                            : minutes - post.postMinute + '분 전'
                        : hours - post.postHour + '시간 전'
                      : post.postMonth + '.' + post.postDay
                    : post.postYear.slice(2, 4) +
                    '.' +
                    post.postMonth +
                    '.' +
                    post.postDay}
                </td>
                <td width="150">{post.commentsSize}</td>
                <td width="150">{post.studyStatus}</td>
              </tr>
            )),
          }[isPostStatus]}
      </table>
      <FooterGray />
    </div>
  );
};

export default MyPage;
