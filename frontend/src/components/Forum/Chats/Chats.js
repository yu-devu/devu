import React, { useEffect, useState } from 'react';
import axios from 'axios';
import ReactPaginate from 'react-paginate';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import './chats.css';
import Submenu from '../Submenu';
import magnify from '../../../img/magnify.png';
import Footer from '../../Home/Footer';
import comment from '../../../img/comment.png';
import hit from '../../../img/hit.png';
import like from '../../../img/like.png';
import like_color from '../../../img/like_color.png';
import { Link } from 'react-router-dom';

const Chats = () => {
  let now = new Date();
  let hours = now.getHours() - 9;
  let minutes = now.getMinutes();
  let seconds = now.getSeconds();
  let year = now.getFullYear();
  let month = now.getMonth() + 1;
  let date = now.getDate();

  const [currentPage, setCurrentPage] = useState(0);
  const [postSize, setPostSize] = useState(0);
  const [postsPerPage] = useState(10);
  const [postData, setPostData] = useState([]);
  const [isLike, setLike] = useState(false);
  const [lastIdx, setLastIdx] = useState(0);
  const [selectedTag, setSelectedTag] = useState([]);
  const [sentence, setSentence] = useState('');
  const [status, setStatus] = useState('');
  const [order, setOrder] = useState('');
  const [likePosts, setLikePosts] = useState([]);
  const onChangeSentence = (e) => {
    setSentence(e.target.value);
  };
  const username = localStorage.getItem('username');

  useEffect(() => {
    fetchData();
    fetchPageSize();
    window.scrollTo(0, 0);
    fetchLikeData();
  }, [currentPage, selectedTag, status, order, isLike]);

  const fetchData = async () => {
    const res = await axios.get(
      process.env.REACT_APP_DB_HOST + `/community/chats`,
      {
        params: {
          page: currentPage,
          tags: selectedTag.join(','), // join(",")으로 해야 ?tags=REACT,SPRING으로 parameter 전송할 수 있음.
          s: sentence,
          status: status,
          order: order,
        },
      }
    );
    console.log(res);

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
          postYear: rowData.createAt.substr(0, 4),
          postMonth: rowData.createAt.substr(5, 2),
          postDay: rowData.createAt.substr(8, 2),
          postHour: rowData.createAt.substr(11, 2),
          postMinute: rowData.createAt.substr(14, 2),
          postSecond: rowData.createAt.substr(17, 2),
          commentsSize: rowData.commentsSize,
        }
      )
    );
    setPostData(_postData);
    CKEditor.instances.textarea_id.setData(postData.content);
    CKEditor.instances.textarea_id.getData();
  };

  const fetchLikeData = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/api/myLikes`)
      .then((res) => {
        const _likePosts = res.data.map((rowData) => rowData.id);
        setLikePosts(_likePosts);
      })
      .catch((err) => console.log(err));
  };

  const handleLike = async (id) => {
    const data = {
      username: username,
      postId: id,
    };
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/api/like`, JSON.stringify(data), {
        headers: {
          'Content-Type': 'application/json',
        },
      })
      .then((res) => {
        console.log('res.data', res.data.liked);
        if (res.data.liked) setLike(true);
        else setLike(false);
      })
      .catch((res) => {
        console.log(res);
      });
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      fetchData();
    }
  };

  const fetchPageSize = async () => {
    const res = await axios.get(
      process.env.REACT_APP_DB_HOST + `/community/chats/size`
    );
    setPostSize(res.data);
  };

  const changePage = ({ selected }) => {
    setCurrentPage(selected);
  };

  return (
    <div>
      <Submenu />
      <div className="all-chats">
        <div className="body-chats">
          <div className="search-and-write">
            <div className="chats-search">
              <input
                type="text"
                placeholder="궁금한 질문을 검색해보세요"
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
                <button className="btn-chats-write">글쓰기</button>
              </Link>
            ) : null}
          </div>
          <div className="body-content-chats">
            <select
              className="select-chats"
              onChange={(e) => {
                setOrder(e.target.value);
              }}
            >
              <option value="">최신순</option>
              <option value="likes">인기순</option>
              <option value="comments">댓글순</option>
            </select>
            <div className="chats-line"></div>
            {/* 게시물 미리보기 */}
            {postData.slice(0, postsPerPage).map((post) => (
              <li key={post.id} className="list-chats">
                <div className="post-chats">
                  <div className="post-header-chats">
                    <div className="post-title-chats">
                      <Link to={`/chatsDetail/${post.id}`}>{post.title}</Link>
                    </div>
                  </div>
                  <div className="post-body-chats">
                    <div className="post-content-chats">{post.content}</div>
                    <div className="post-options-chats">
                      <div className="post-comment-chats">
                        <div className="text-comment">{post.commentsSize}</div>
                        <img className="img-comment" src={comment} alt="" />
                      </div>
                      <div className="post-hit">
                        <div className="text-hit">{post.hit}</div>
                        <img className="img-hit" src={hit} alt="" />
                      </div>
                      <div
                        className="post-like"
                        onClick={() => handleLike(post.id)}
                      >
                        <div className="text-like">{post.like}</div>
                        {likePosts.includes(post.id) ? (
                          <img className="img-like" src={like_color} alt="" />
                        ) : (
                          <img className="img-like" src={like} alt="" />
                        )}
                      </div>
                    </div>
                  </div>
                  <div className="post-tail-chats">
                    <div className="post-owner">{post.username}</div>
                    <div className="post-date">
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
                    </div>
                  </div>
                  <div className="chats-line2"></div>
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
      </div>
      <Footer />
    </div>
  );
};

export default Chats;
