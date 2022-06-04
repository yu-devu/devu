import React from 'react'
import './jobs.css'
import magnify from '../../img/magnify.png';
import FooterGray from "../Home/FooterGray";

const Jobs = () => {
    return (
        <div>
            <div>
                <h1 className='text-jobs'>채용중인 포지션</h1>
                <div className='top-jobs'>
                    <select className='select1-jobs'>
                        <option>직군 전체</option>
                    </select>
                    <select className='select2-jobs'>
                        <option>직무 전체</option>
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
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                        <div className='job-card'>
                            <div className='img-job'></div>
                            <div className='top-job'>
                                <div className='location-job'>대구</div>
                                <div className='name-job'>기업명</div>
                            </div>
                            <div className='content-job'>구하는 개발자 포지션/정보란 (엔지니어)</div>
                            <div className='exp-job'>경력무관</div>
                            <div className='date-job'>채용 시 마감</div>
                        </div>
                    </div>
                </div>
            </div>
            <FooterGray />
        </div>
    )
}

export default Jobs