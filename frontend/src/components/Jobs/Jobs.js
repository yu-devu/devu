import React, { useEffect, useState } from 'react'
import axios from 'axios';
import ReactPaginate from 'react-paginate';
import './jobs.css'
import magnify from '../../img/magnify.png';
import FooterGray from "../Home/FooterGray";
import logo from '../../img/logo_gray.png'

const Jobs = () => {
    const [postData, setPostData] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [postSize, setPostSize] = useState(0);
    const [postsPerPage] = useState(20);

    useEffect(() => {
        fetchData();
        fetchPageSize();
    }, [currentPage]);

    const fetchData = async () => {
        const res = await axios.get(
            process.env.REACT_APP_DB_HOST + `/api/position/all`,
            {
                params: {
                    page: currentPage,
                },
            }
        );

        const _postData = await res.data.map(
            (rowData) => (
                {
                    company: rowData.company,
                    title: rowData.title,
                    duration: rowData.duration,
                    link: rowData.link,
                }
            )
        );
        setPostData(_postData);
    };

    const fetchPageSize = async () => {
        const res = await axios.get(
            process.env.REACT_APP_DB_HOST + `/community/studies/size`
        );
        // setPostSize(res.data);
        setPostSize(2000); // 채용 정보 개수 받아오는 api가 없어서 임시로 설정함
    };

    const changePage = ({ selected }) => {
        setCurrentPage(selected);
    };

    return (
        <div>
            <div>
                <h1 className='text-jobs'>채용중인 포지션</h1>
                <div className='top-jobs'>
                    <select className='select1-jobs'>
                        <option>직무 전체</option>
                        <option>백엔드/서버 개발자</option>
                        <option>프론트엔드/웹퍼블리셔</option>
                        <option>SW 엔지니어</option>
                        <option>안드로이드 개발자</option>
                        <option>IOS 개발자</option>
                        <option>데이터 엔지니어</option>
                        <option>데이터 사이언티스트</option>
                    </select>
                    <div className='search-jobs'>
                        <input
                            type="text"
                            placeholder="채용 관련 정보를 찾아보세요."
                            className='search-jobs-input'
                            onChange={(e) => {
                            }}
                        />
                        <button className='btn-search-jobs'>
                            <img className='img-mag-jobs' src={magnify} alt="" />
                        </button>

                    </div>
                </div>
                <div className='middle-jobs'>
                    <div className='job-cards'>
                        {postData.slice(0, 20).map((post) => (
                            <div className='job-card'>
                                <div className='img-job'>
                                    <img className='job-logo' src={logo} />
                                </div>
                                <div className='top-job'>
                                    <div className='name-job'>{post.company}</div>
                                </div>
                                <div className='content-job'>
                                    <a className='job-link' target="_blank" href={post.link}>{post.title}</a>
                                </div>
                                <div className='date-job'>{post.duration}</div>
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
    )
}

export default Jobs