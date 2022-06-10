import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import ReactPaginate from 'react-paginate';
import './jobs.css';
import magnify from '../../img/magnify.png';
import FooterGray from '../Home/FooterGray';
// import logo from '../../img/logo_gray.png'
import Naver from '../../img/NaverLogo.png';
import Kakao from '../../img/KakaoLogo.png';
import Line from '../../img/LineLogo.png';
import Coupang from '../../img/CoupangLogo.png';
import Baemin from '../../img/BaeminLogo.png';

const Jobs = () => {
  const navigate = useNavigate();
  const [postData, setPostData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [postSize, setPostSize] = useState(0);
  const [postsPerPage] = useState(20);
  const [company, setCompany] = useState('all');

  useEffect(() => {
    fetchData();
  }, [currentPage, company]);

  const fetchData = async () => {
    if (company === 'all') {
      await axios
        .get(process.env.REACT_APP_DB_HOST + `/api/position/all`, {
          params: {
            page: currentPage,
          },
        })
        .then((res) => {
          setPostSize(res.data.size); // pagination 구현하기 위해 채용 정보의 총 개수를 불러와서 저장
          const _postData = res.data.positions.map((rowData) => ({
            company: rowData.company,
            title: rowData.title,
            duration: rowData.duration,
            link: rowData.link,
          }));
          setPostData(_postData);
        })
        .catch((e) => console.log(e));
    } else if (company === 'naver') {
      await axios
        .get(process.env.REACT_APP_DB_HOST + `/api/position/naver`, {
          params: {
            page: currentPage,
          },
        })
        .then((res) => {
          setPostSize(res.data.size); // pagination 구현하기 위해 채용 정보의 총 개수를 불러와서 저장
          const _postData = res.data.positions.map((rowData) => ({
            company: rowData.company,
            title: rowData.title,
            duration: rowData.duration,
            link: rowData.link,
          }));
          setPostData(_postData);
        })
        .catch((e) => console.log(e));
    } else if (company === 'kakao') {
      await axios
        .get(process.env.REACT_APP_DB_HOST + `/api/position/kakao`, {
          params: {
            page: currentPage,
          },
        })
        .then((res) => {
          setPostSize(res.data.size); // pagination 구현하기 위해 채용 정보의 총 개수를 불러와서 저장
          const _postData = res.data.positions.map((rowData) => ({
            company: rowData.company,
            title: rowData.title,
            duration: rowData.duration,
            link: rowData.link,
          }));
          setPostData(_postData);
        })
        .catch((e) => console.log(e));
    } else if (company === 'line') {
      await axios
        .get(process.env.REACT_APP_DB_HOST + `/api/position/line`, {
          params: {
            page: currentPage,
          },
        })
        .then((res) => {
          setPostSize(res.data.size); // pagination 구현하기 위해 채용 정보의 총 개수를 불러와서 저장
          const _postData = res.data.positions.map((rowData) => ({
            company: rowData.company,
            title: rowData.title,
            duration: rowData.duration,
            link: rowData.link,
          }));
          setPostData(_postData);
        })
        .catch((e) => console.log(e));
    } else if (company === 'coupang') {
      await axios
        .get(process.env.REACT_APP_DB_HOST + `/api/position/coupang`, {
          params: {
            page: currentPage,
          },
        })
        .then((res) => {
          setPostSize(res.data.size); // pagination 구현하기 위해 채용 정보의 총 개수를 불러와서 저장
          const _postData = res.data.positions.map((rowData) => ({
            company: rowData.company,
            title: rowData.title,
            duration: rowData.duration,
            link: rowData.link,
          }));
          setPostData(_postData);
        })
        .catch((e) => console.log(e));
    } else if (company === 'baemin') {
      await axios
        .get(process.env.REACT_APP_DB_HOST + `/api/position/baemin`, {
          params: {
            page: currentPage,
          },
        })
        .then((res) => {
          setPostSize(res.data.size); // pagination 구현하기 위해 채용 정보의 총 개수를 불러와서 저장
          const _postData = res.data.positions.map((rowData) => ({
            company: rowData.company,
            title: rowData.title,
            duration: rowData.duration,
            link: rowData.link,
          }));
          setPostData(_postData);
        })
        .catch((e) => console.log(e));
    }
  };

  const changePage = ({ selected }) => setCurrentPage(selected);
  return (
    <div>
      <div>
        <h1 className="text-jobs">채용중인 포지션</h1>
        <div className="top-jobs">
          <select
            className="select1-jobs"
            onChange={(e) => {
              setCompany(e.target.value);
              setCurrentPage(0);
            }}
            defaultValue="all"
          >
            {/* <option>직무 전체</option>
                        <option>백엔드/서버 개발자</option>
                        <option>프론트엔드/웹퍼블리셔</option>
                        <option>SW 엔지니어</option>
                        <option>안드로이드 개발자</option>
                        <option>IOS 개발자</option>
                        <option>데이터 엔지니어</option>
                        <option>데이터 사이언티스트</option> */}
            <option value="all">전체</option>
            <option value="naver">네이버</option>
            <option value="kakao">카카오</option>
            <option value="line">라인</option>
            <option value="coupang">쿠팡</option>
            <option value="baemin">배민</option>
          </select>
          <div className="search-jobs">
            <input
              type="text"
              placeholder="채용 관련 정보를 찾아보세요."
              className="search-jobs-input"
              onChange={(e) => {}}
            />
            <button className="btn-search-jobs">
              <img className="img-mag-jobs" src={magnify} alt="" />
            </button>
          </div>
        </div>
        <div className="middle-jobs">
          <div className="job-cards">
            {postData.slice(0, postsPerPage).map((post) => (
              <div className="job-card">
                <div className="img-job">
                  {
                    {
                      NAVER: <img className="job-logo" src={Naver} />,
                      KAKAO: <img className="job-logo" src={Kakao} />,
                      LINE: <img className="job-logo" src={Line} />,
                      COUPANG: <img className="job-logo" src={Coupang} />,
                      BAEMIN: <img className="job-logo" src={Baemin} />,
                    }[post.company]
                  }
                </div>
                {/* post.company 별로 회사 대표 이미지 불러올 수 있도록 변경해야 함 */}
                <div className="top-job">
                  <div className="name-job">{post.company}</div>
                </div>
                <div className="content-job">
                  <a className="job-link" target="_blank" href={post.link}>
                    {post.title}
                  </a>
                </div>
                <div className="date-job">{post.duration}</div>
              </div>
            ))}
          </div>
        </div>
      </div>
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
      <FooterGray />
    </div>
  );
};

export default Jobs;
