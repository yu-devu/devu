import React, { useEffect, useState } from "react";
import axios from "axios";
import ReactPaginate from "react-paginate";
import "./studies.css";
import Submenu from "../Submenu";
import a from "../../../img/a.png";
import magnify from "../../../img/magnify.png";
import Footer from "../../Home/Footer";
import spring from "../../../img/spring.png";
import c from "../../../img/c.png";
import cpp from "../../../img/cpp.png";
import js from "../../../img/js.png";
import react from "../../../img/react.png";
import node_js from "../../../img/node_js.png";
import python from "../../../img/python.png";
import go from "../../../img/go.png";
import swift from "../../../img/swift.png";
import angular from "../../../img/angular.png";
import java from "../../../img/java.png";
import flutter from "../../../img/flutter.png";
import docker from "../../../img/docker.png";
import ruby from "../../../img/ruby.png";
import html from "../../../img/html.png";
import css from "../../../img/css.png";
import mysql from "../../../img/mysql.png";
import comment from "../../../img/comment.png";
import hit from "../../../img/hit.png";
import like from "../../../img/like.png";
import like_color from "../../../img/like_color.png";
import { Link } from "react-router-dom";
import { useMediaQuery } from 'react-responsive'

const Studies = () => {
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
  const [isLike, setLike] = useState(1);
  const [lastIdx, setLastIdx] = useState(0);
  const [selectedTag, setSelectedTag] = useState([]);
  const [choiced, setChoiced] = useState(false);
  const [sentence, setSentence] = useState("");
  const [status, setStatus] = useState("");
  const [order, setOrder] = useState("");
  const isTabletOrMobile = useMediaQuery({ maxWidth: 1224 })
  const [likePosts, setLikePosts] = useState([]);
  const onChangeSentence = (e) => {
    setSentence(e.target.value);
  };
  const username = localStorage.getItem("username");

  useEffect(() => {
    fetchData();
    fetchPageSize();
    window.scrollTo(0, 0);
    fetchLikeData();
    setLike(1);
  }, [currentPage, selectedTag, status, order, isLike]);

  const fetchData = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/community/studies`, {
        params: {
          page: currentPage,
          status: status,
          order: order,
          tags: selectedTag.join(","), // join(",")으로 해야 ?tags=REACT,SPRING으로 parameter 전송할 수 있음.
          s: sentence,
        },
      })
      .then((res) => {
        const _postData = res.data.map(
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
              date: rowData.createAt.substr(0, 10),
              time: rowData.createAt.substr(11, 8),
              tags: rowData.tags,
              studyStatus: rowData.studyStatus,
              commentsSize: rowData.commentsSize,
            }
          )
        );
        setPostData(_postData);
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

  const handleLike = async (id) => {
    const data = {
      username: username,
      postId: id,
    };
    await axios
      .post(process.env.REACT_APP_DB_HOST + `/api/like`, JSON.stringify(data), {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then((res) => {
        if (res.data.liked) setLike(like + 1);
        else setLike(like - 1);
      })
      .catch((e) => console.log(e));
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      fetchData();
    }
  };

  const handleTags = (tag) => {
    const _selectedTag = [...selectedTag];
    if (!_selectedTag.includes(tag)) {
      _selectedTag.push(tag);
      setSelectedTag(_selectedTag);
    } else {
      setSelectedTag(_selectedTag.filter((index) => index != tag));
    }
  };

  const fetchPageSize = async () => {
    await axios
      .get(process.env.REACT_APP_DB_HOST + `/community/studies/size`)
      .then((res) => setPostSize(res.data))
      .catch((e) => console.log(e));
  };

  const changePage = (selected) => setCurrentPage(selected);
  return (
    <div>
      <Submenu />
      <div className="all-studies">
        <div className="top-studies">
          <h3 className="top-msg">따끈따끈한 구인란이에요!</h3>
          <h4 className="top-msg-gray">함께 성장할 스터디를 모집해보세요</h4>
          {/* 상위 3개 게시물 */}
          {isTabletOrMobile ? (<div className="list-topcards">
            {postData.slice(0, 1).map((top) => (
              <li key={top.id} className="top-cards">
                <div className="top-card">
                  <div className="top-circle">
                    <div className="top-profile">
                      <img className="top-photo" src={a} alt="" />
                    </div>
                    <div className="top-detail">
                      <div className="top-title">
                        <Link to={`/studiesDetail/${top.id}`}>
                          {top.title.length > 10
                            ? top.title.substr(0, 10) + "..."
                            : top.title}
                        </Link>
                      </div>
                      <div className="top-content">
                        {top.content.length > 20
                          ? top.title.substr(0, 20) + "..."
                          : top.content}
                      </div>
                      <div className="top-date">
                        {top.postYear == year
                          ? top.postMonth == month && top.postDay == date
                            ? top.postHour == hours
                              ? top.postMinute == minutes
                                ? seconds - top.postSecond + "초 전"
                                : minutes - top.postMinute == 1 &&
                                  seconds < top.postSecond
                                  ? 60 - top.postSecond + seconds + "초 전"
                                  : minutes - top.postMinute + "분 전"
                              : hours - top.postHour + "시간 전"
                            : top.postMonth + "." + top.postDay
                          : top.postYear.slice(2, 4) +
                          "." +
                          top.postMonth +
                          "." +
                          top.postDay}
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            ))}
          </div>) : (<div className="list-topcards">
            {postData.slice(0, 3).map((top) => (
              <li key={top.id} className="top-cards">
                <div className="top-card">
                  <div className="top-circle">
                    <div className="top-profile">
                      <img className="top-photo" src={a} alt="" />
                    </div>
                    <div className="top-detail">
                      <div className="top-title">
                        <Link to={`/studiesDetail/${top.id}`}>
                          {top.title.length > 10
                            ? top.title.substr(0, 10) + "..."
                            : top.title}
                        </Link>
                      </div>
                      <div className="top-content">
                        {top.content.length > 20
                          ? top.title.substr(0, 20) + "..."
                          : top.content}
                      </div>
                      <div className="top-date">
                        {top.postYear == year
                          ? top.postMonth == month && top.postDay == date
                            ? top.postHour == hours
                              ? top.postMinute == minutes
                                ? seconds - top.postSecond + "초 전"
                                : minutes - top.postMinute == 1 &&
                                  seconds < top.postSecond
                                  ? 60 - top.postSecond + seconds + "초 전"
                                  : minutes - top.postMinute + "분 전"
                              : hours - top.postHour + "시간 전"
                            : top.postMonth + "." + top.postDay
                          : top.postYear.slice(2, 4) +
                          "." +
                          top.postMonth +
                          "." +
                          top.postDay}
                      </div>
                    </div>
                  </div>
                </div>
              </li>
            ))}
          </div>)}
        </div>
        {isTabletOrMobile ? (<div className="body-studies">
          <div className="cat-menu">
            {status === "" ? (
              <div className="cat-menu-items">
                <p
                  className="cat-item-selected"
                  onClick={() => {
                    setStatus("");
                  }}
                >
                  전체
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("ACTIVE");
                  }}
                >
                  모집중
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("CLOSED");
                  }}
                >
                  모집완료
                </p>
              </div>
            ) : status === "ACTIVE" ? (
              <div className="cat-menu-items">
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("");
                  }}
                >
                  전체
                </p>
                <p
                  className="cat-item-selected"
                  onClick={() => {
                    setStatus("ACTIVE");
                  }}
                >
                  모집중
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("CLOSED");
                  }}
                >
                  모집완료
                </p>
              </div>
            ) : (
              <div className="cat-menu-items">
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("");
                  }}
                >
                  전체
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("ACTIVE");
                  }}
                >
                  모집중
                </p>
                <p
                  className="cat-item-selected"
                  onClick={() => {
                    setStatus("CLOSED");
                  }}
                >
                  모집완료
                </p>
              </div>
            )}
            {username ? ( // 로그인 했을 때 글쓰기 버튼 활성화
              <Link to="write">
                <button className="btn-studies-write">글쓰기</button>
              </Link>
            ) : null}
          </div>
          <div className="search-and-write">
            <div className="studies-search">
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
          </div>
          <div className="body-content">
            <select
              className="select-studies"
              onChange={(e) => {
                setOrder(e.target.value);
              }}
            >
              <option value="">최신순</option>
              <option value="likes">인기순</option>
              <option value="comments">댓글순</option>
            </select>
            {/* 게시물 미리보기 */}
            {postData.slice(0, postsPerPage).map((post) => (
              <li key={post.id} className="list-studies">
                <div className="post-studies">
                  <div className="post-header">
                    <div className="post-status">
                      {post.studyStatus === "ACTIVE" ? "모집중" : "모집완료"}
                    </div>
                    <div className="post-title">
                      <Link to={`/studiesDetail/${post.id}`}>
                        {post.title}
                      </Link>
                    </div>
                  </div>
                  <div className="post-body">
                    <div className="post-content">{post.content}</div>
                  </div>
                  <div className="tags">
                    {post.tags.map((tag) => (
                      <div className="post-tag">{tag}</div>
                    ))}
                  </div>
                  <div className="post-tail">
                    <div className="post-owner">{post.username}</div>
                    <div className="post-date">
                      {post.postYear == year
                        ? post.postMonth == month && post.postDay == date
                          ? post.postHour == hours
                            ? post.postMinute == minutes
                              ? seconds - post.postSecond + "초 전"
                              : minutes - post.postMinute == 1 &&
                                seconds < post.postSecond
                                ? 60 - post.postSecond + seconds + "초 전"
                                : minutes - post.postMinute + "분 전"
                            : hours - post.postHour + "시간 전"
                          : post.postMonth + "." + post.postDay
                        : post.postYear.slice(2, 4) +
                        "." +
                        post.postMonth +
                        "." +
                        post.postDay}
                    </div>
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
                  <div className="studies-line2"></div>
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
          {/* <Footer /> */}
        </div>) : (<div className="body-studies">
          <div className="cat-menu">
            {status === "" ? (
              <div className="cat-menu-items">
                <p
                  className="cat-item-selected"
                  onClick={() => {
                    setStatus("");
                  }}
                >
                  전체
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("ACTIVE");
                  }}
                >
                  모집중
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("CLOSED");
                  }}
                >
                  모집완료
                </p>
              </div>
            ) : status === "ACTIVE" ? (
              <div className="cat-menu-items">
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("");
                  }}
                >
                  전체
                </p>
                <p
                  className="cat-item-selected"
                  onClick={() => {
                    setStatus("ACTIVE");
                  }}
                >
                  모집중
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("CLOSED");
                  }}
                >
                  모집완료
                </p>
              </div>
            ) : (
              <div className="cat-menu-items">
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("");
                  }}
                >
                  전체
                </p>
                <p
                  className="cat-item"
                  onClick={() => {
                    setStatus("ACTIVE");
                  }}
                >
                  모집중
                </p>
                <p
                  className="cat-item-selected"
                  onClick={() => {
                    setStatus("CLOSED");
                  }}
                >
                  모집완료
                </p>
              </div>
            )}
          </div>
          <div className="search-and-write">
            <div className="studies-search">
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
                <button className="btn-studies-write">글쓰기</button>
              </Link>
            ) : null}
          </div>
          <div className="choicing">
            <div className="choice-tag">
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Spring");
                }}
              >
                {selectedTag.includes("Spring") ? (
                  <img className="img-choiced" src={spring} alt="" />
                ) : (
                  <img className="img-choice" src={spring} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("C");
                }}
              >
                {selectedTag.includes("C") ? (
                  <img className="img-choiced" src={c} alt="" />
                ) : (
                  <img className="img-choice" src={c} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("CPP");
                }}
              >
                {selectedTag.includes("CPP") ? (
                  <img className="img-choiced" src={cpp} alt="" />
                ) : (
                  <img className="img-choice" src={cpp} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("JS");
                }}
              >
                {selectedTag.includes("JS") ? (
                  <img className="img-choiced" src={js} alt="" />
                ) : (
                  <img className="img-choice" src={js} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("React");
                }}
              >
                {selectedTag.includes("React") ? (
                  <img className="img-choiced" src={react} alt="" />
                ) : (
                  <img className="img-choice" src={react} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("NodeJS");
                }}
              >
                {selectedTag.includes("NodeJS") ? (
                  <img className="img-choiced" src={node_js} alt="" />
                ) : (
                  <img className="img-choice" src={node_js} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Python");
                }}
              >
                {selectedTag.includes("Python") ? (
                  <img className="img-choiced" src={python} alt="" />
                ) : (
                  <img className="img-choice" src={python} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Go");
                }}
              >
                {selectedTag.includes("Go") ? (
                  <img className="img-choiced" src={go} alt="" />
                ) : (
                  <img className="img-choice" src={go} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Swift");
                }}
              >
                {selectedTag.includes("Swift") ? (
                  <img className="img-choiced" src={swift} alt="" />
                ) : (
                  <img className="img-choice" src={swift} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Angular");
                }}
              >
                {selectedTag.includes("Angular") ? (
                  <img className="img-choiced" src={angular} alt="" />
                ) : (
                  <img className="img-choice" src={angular} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Java");
                }}
              >
                {selectedTag.includes("Java") ? (
                  <img className="img-choiced" src={java} alt="" />
                ) : (
                  <img className="img-choice" src={java} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Flutter");
                }}
              >
                {selectedTag.includes("Flutter") ? (
                  <img className="img-choiced" src={flutter} alt="" />
                ) : (
                  <img className="img-choice" src={flutter} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Docker");
                }}
              >
                {selectedTag.includes("Docker") ? (
                  <img className="img-choiced" src={docker} alt="" />
                ) : (
                  <img className="img-choice" src={docker} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("Ruby");
                }}
              >
                {selectedTag.includes("Ruby") ? (
                  <img className="img-choiced" src={ruby} alt="" />
                ) : (
                  <img className="img-choice" src={ruby} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("HTML");
                }}
              >
                {selectedTag.includes("HTML") ? (
                  <img className="img-choiced" src={html} alt="" />
                ) : (
                  <img className="img-choice" src={html} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("CSS");
                }}
              >
                {selectedTag.includes("CSS") ? (
                  <img className="img-choiced" src={css} alt="" />
                ) : (
                  <img className="img-choice" src={css} alt="" />
                )}
              </button>
              <button
                className="btn-choice"
                onClick={() => {
                  handleTags("MySQL");
                }}
              >
                {selectedTag.includes("MySQL") ? (
                  <img className="img-choiced" src={mysql} alt="" />
                ) : (
                  <img className="img-choice" src={mysql} alt="" />
                )}
              </button>
            </div>
            <div className="body-content">
              <select
                className="select-studies"
                onChange={(e) => {
                  setOrder(e.target.value);
                }}
              >
                <option value="">최신순</option>
                <option value="likes">인기순</option>
                <option value="comments">댓글순</option>
              </select>
              <div className="studies-line"></div>
              {/* 게시물 미리보기 */}
              {postData.slice(0, postsPerPage).map((post) => (
                <li key={post.id} className="list-studies">
                  <div className="post-studies">
                    <div className="post-header">
                      <div className="post-status">
                        {post.studyStatus === "ACTIVE" ? "모집중" : "모집완료"}
                      </div>
                      <div className="post-title">
                        <Link to={`/studiesDetail/${post.id}`}>
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
                    <div className="tags">
                      {post.tags.map((tag) => (
                        <div className="post-tag">{tag}</div>
                      ))}
                    </div>
                    <div className="post-tail">
                      <div className="post-owner">{post.username}</div>
                      <div className="post-date">
                        {post.postYear == year
                          ? post.postMonth == month && post.postDay == date
                            ? post.postHour == hours
                              ? post.postMinute == minutes
                                ? seconds - post.postSecond + "초 전"
                                : minutes - post.postMinute == 1 &&
                                  seconds < post.postSecond
                                  ? 60 - post.postSecond + seconds + "초 전"
                                  : minutes - post.postMinute + "분 전"
                              : hours - post.postHour + "시간 전"
                            : post.postMonth + "." + post.postDay
                          : post.postYear.slice(2, 4) +
                          "." +
                          post.postMonth +
                          "." +
                          post.postDay}
                      </div>
                    </div>
                    <div className="studies-line2"></div>
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
          <Footer />
        </div>)}
      </div>
    </div>
  );
};

export default Studies;
