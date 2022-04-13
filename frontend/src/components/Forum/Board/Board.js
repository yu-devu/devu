import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './board.css';
import { Link } from 'react-router-dom';
import ReactHtmlParser from 'react-html-parser';

const url = 'http://54.180.29.69:8080';

const Board = () => {
  const [page, setPage] = useState(0);
  const [postData, setPostData] = useState([
    {
      title: '',
      content: '',
      username: '',
      hit: '',
      like: '',
    },
  ]);
  const [lastIdx, setLastIdx] = useState(0);

  useEffect(() => {
    fetchData();
  }, [page, postData]);

  const fetchData = async () => {
    const res = await axios.get(url + `/community/chats`, {
      params: {
        page: page,
      },
    });

    const _postData = await res.data.map(
      (rowData) => (
        setLastIdx(lastIdx + 1),
        {
          title: rowData.title,
          content: ReactHtmlParser(rowData.content),
          hit: rowData.hit,
          like: rowData.like,
          username: rowData.username,
        }
      )
    );
    setPostData(_postData);
  };

  return (
    <div>
      <div className="qna-header">
        <div className="container-board">
          <div className="best-board">
            <h2>답변을 기다리고 있습니다!</h2>
            <div className="row">
              <div className="board-best-item">
                <div className="qna-card">
                  <div className="type">답변을 기다리는 QnA</div>
                  <h4 className="title">
                    <a>질문 1</a>
                  </h4>
                  <p className="content">
                    <a className="content">내용 blablablabalbal</a>
                  </p>
                  <ul className="metadata">
                    <li className="metadata-item">
                      <span className="name">답변수</span>
                      <span className="value">0</span>
                    </li>
                    <li className="metadata-item">
                      <span className="name">추천수</span>
                      <span className="value">3</span>
                    </li>
                    <li className="metadata-item">
                      <span className="name">조회수</span>
                      <span className="value">20</span>
                    </li>
                  </ul>
                </div>
              </div>
              <div className="board-best-item">
                <div className="qna-card">
                  <div className="type">답변을 기다리는 QnA</div>
                  <h4 className="title">
                    <a>질문 2</a>
                  </h4>
                  <p className="content">
                    <a className="content">내용 blablablabalbal</a>
                  </p>
                  <ul className="metadata">
                    <li className="metadata-item">
                      <span className="name">답변수</span>
                      <span className="value">0</span>
                    </li>
                    <li className="metadata-item">
                      <span className="name">추천수</span>
                      <span className="value">3</span>
                    </li>
                    <li className="metadata-item">
                      <span className="name">조회수</span>
                      <span className="value">20</span>
                    </li>
                  </ul>
                </div>
              </div>
              <div className="board-best-item">
                <div className="qna-card">
                  <div className="type">답변을 기다리는 QnA</div>
                  <h4 className="title">
                    <a>질문 3</a>
                  </h4>
                  <p className="content">
                    <a className="content">내용 blablablabalbal</a>
                  </p>
                  <ul className="metadata">
                    <li className="metadata-item">
                      <span className="name">답변수</span>
                      <span className="value">0</span>
                    </li>
                    <li className="metadata-item">
                      <span className="name">추천수</span>
                      <span className="value">3</span>
                    </li>
                    <li className="metadata-item">
                      <span className="name">조회수</span>
                      <span className="value">20</span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 게시판 */}
      <div className="main-area">
        <div className="main-container container">
          <div className="qnas">
            <div className="content">
              <div className="content-top">
                <div className="filter-area">
                  <div className="orders">
                    <div className="order-item">
                      <div className="check-icon"></div>
                      <p className="active">최신순</p>
                    </div>
                    <div className="order-item">
                      <div className="check-icon"></div>
                      <p className="active">추천순</p>
                    </div>
                    <div className="order-item">
                      <div className="check-icon"></div>
                      <p className="active">조회순</p>
                    </div>
                  </div>
                </div>
              </div>
              <div className="article-list">
                <div className="search">
                  <div className="search-input">
                    <input
                      type="text"
                      className="input-search"
                      placeholder="검색어를 입력하세요"
                    />
                  </div>
                </div>
                {lastIdx !== 0 ? (
                  postData.map((rowData) => (
                    <tr className="article-item">
                      <td className="title">{rowData.title}</td>
                      <td className="item-body">
                        <span className="body-link">{rowData.content}</span>
                      </td>
                      <td className="item-user">
                        <span className="nickname">
                          작성자 : {rowData.username}
                        </span>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td>작성된 게시글이 없습니다.</td>
                  </tr>
                )}

                <ul className="pagination">
                  <li className="page-item">
                    <button className="page-link">1</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">2</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">3</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">4</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">5</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">6</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">7</button>
                  </li>
                  <li className="page-item">
                    <button className="page-link">8</button>
                  </li>
                </ul>
              </div>
              <Link to="write">
                <button className="btn-write">글 쓰기</button>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Board;
