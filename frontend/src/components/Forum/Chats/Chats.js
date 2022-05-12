import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './chats.css';
import { Link } from 'react-router-dom';
import ReactPaginate from 'react-paginate'
import './pagination.css'

const Chats = () => {
  const [currentPage, setCurrentPage] = useState(0);
  const [postSize, setPostSize] = useState(0);
  const [postsPerPage] = useState(20);
  const [postData, setPostData] = useState([]);
  const [lastIdx, setLastIdx] = useState(0);
  // const pagesVisited = currentPage * postsPerPage

  useEffect(() => {
    fetchData();
    fetchPageSize();
  }, [currentPage]); // 글 목록 page 버튼 누를 때 마다 버튼 값 가져와서 setCurrentPage 하면 될 듯함.

  const fetchData = async () => {
    const res = await axios.get(process.env.REACT_APP_DB_HOST + `/community/chats`, {
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
          content: (rowData.content),
          hit: rowData.hit,
          like: rowData.like,
          username: rowData.username,
          tags: rowData.tags,
        }
      )
    );
    setPostData(_postData);
  };

  const fetchPageSize = async () => {
    const res = await axios.get(process.env.REACT_APP_DB_HOST + `/community/chats/size`);
    setPostSize(res.data);
  }

  const changePage = ({ selected }) => {
    setCurrentPage(selected)
  }

  return (
    <div>
      {/* 게시판 */}
      <div className="content">
        <div className="article-list">
          <ul className='list-group'>
            {postData.slice(0, 20).map(post => (
              <li key={post.id} className="list-group-item">
                <div className='title'>
                  <Link to={`/postDetail/${post.id}`}>{post.title}</Link>
                </div>
                <div className='owner'>
                  작성자 : {post.username} 조회수 : {post.hit} 좋아요 : {post.like}
                </div>

              </li>
            ))}
          </ul>
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
        <Link to="write">
          <button className="btn-write">글 쓰기</button>
        </Link>
      </div>
    </div>
  );
};

export default Chats;
