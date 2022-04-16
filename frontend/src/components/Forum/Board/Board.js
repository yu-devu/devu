import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './board.css';
import { Link } from 'react-router-dom';
import ReactHtmlParser from 'react-html-parser';
import Posts from './Posts';
import Pagination from './Pagination';

const url = 'http://54.180.29.69:8080';

const Board = () => {
  const [currentPage, setCurrentPage] = useState(0);
  const [postsPerPage] = useState(10);
  const [postData, setPostData] = useState([
    {
      id: '',
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
  }, []);

  const fetchData = async () => {
    const res = await axios.get(url + `/community/chats`, {
      params: {
        page: currentPage,
      },
    });

    const _postData = await res.data.map(
      (rowData) => (
        setLastIdx(lastIdx + 1),
        {
          id: rowData.id,
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

  const indexOfLastPost = (currentPage + 1) * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = postData.slice(indexOfFirstPost, indexOfLastPost);

  const paginate = pageNumber => {
    setCurrentPage(pageNumber);
  };

  return (
    <div>
      {/* 게시판 */}
      <div className="content">
        <div className="article-list">
          <Posts postData={currentPosts} />
          <Pagination postsPerPage={postsPerPage} totalPosts={postData.length} paginate={paginate} />
        </div>
        <Link to="write">
          <button className="btn-write">글 쓰기</button>
        </Link>
      </div>
    </div>
  );
};

export default Board;
