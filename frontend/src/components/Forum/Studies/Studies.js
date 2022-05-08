import React from 'react'
import './studies.css'
import Submenu from './Submenu'
import a from "../../../img/a.png"
import b from "../../../img/b.png"
import c from "../../../img/c.png"
import Category from './Category'
import magnify from "../../../img/magnify.png"
import Footer from '../../Home/Footer'
import atom from "../../../img/atom.png"
import python from "../../../img/python.png"
import cp from "../../../img/cp.png"
import java from "../../../img/java.png"
import mysql from "../../../img/mysql.png"
import node_js from "../../../img/node_js.png"
import ruby from "../../../img/ruby.png"
import js from "../../../img/js.png"
import css from "../../../img/css.png"
import html from "../../../img/html.png"
import comment from "../../../img/comment.png"
import hit from "../../../img/hit.png"
import like from "../../../img/like.png"

const Studies = () => {
    return (
        <div>
            <Submenu />
            <div>
                <div className='top-studies'>
                    <h3 className='top-msg'>따끈따끈한 구인란이에요!</h3>
                    <h4 className='top-msg-gray'>함께 성장할 스터디를 모집해보세요</h4>
                    <div className='top-cards'>
                        <div className='top-card'>
                            <div className='top-circle'>
                                <div className='top-profile'>
                                    <img className="top-photo" src={a} />
                                </div>
                            </div>
                            <div className='top-detail'>
                                <div className='top-title'>스터디그룹 A</div>
                                <div className='top-people'>멤버 3명</div>
                                <div className='top-content'>프론트엔트 인터뷰 스터디 2분 모집합니다.</div>
                                <div className='top-date'>방금 전</div>
                            </div>
                        </div>
                        <div className='top-card'>
                            <div className='top-circle'>
                                <div className='top-profile'>
                                    <img className="top-photo" src={c} />
                                </div>
                            </div>
                            <div className='top-detail'>
                                <div className='top-title'>스터디그룹 A</div>
                                <div className='top-people'>멤버 3명</div>
                                <div className='top-content'>프론트엔트 인터뷰 스터디 2분 모집합니다.</div>
                                <div className='top-date'>방금 전</div>
                            </div>
                        </div>
                        <div className='top-card'>
                            <div className='top-circle'>
                                <div className='top-profile'>
                                    <img className="top-photo" src={b} />
                                </div>
                            </div>
                            <div className='top-detail'>
                                <div className='top-title'>스터디그룹 A</div>
                                <div className='top-people'>멤버 3명</div>
                                <div className='top-content'>프론트엔트 인터뷰 스터디 2분 모집합니다.</div>
                                <div className='top-date'>방금 전</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className='body-studies'>
                    <Category />
                    <div className='search-and-write'>
                        <div className='studies-search'>
                            <input type='text' placeholder='   맞춤 스터디그룹을 찾아보세요' className='search-input' />
                            <button className='btn-mag'>
                                <img className='img-mag' src={magnify} alt="" />
                            </button>
                        </div>
                        <button className='btn-studies-write'>글 쓰기</button>
                    </div>
                    <div className='choice-tag'>
                        <button className='btn-choice'>
                            <img className='img-choice' src={atom} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={python} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={ruby} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={js} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={mysql} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={cp} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={java} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={node_js} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={css} alt="" />
                        </button>
                        <button className='btn-choice'>
                            <img className='img-choice' src={html} alt="" />
                        </button>
                    </div>
                    <select className='select-studies'>
                        <option>최신순</option>
                        <option>인기순</option>
                        <option>조회순</option>
                    </select>
                    <div className='studies-line'></div>
                    <div className='post-studies'>
                        <div className='post-header'>
                            <div className='post-status'>모집중</div>
                            <div className='post-title'>WebGl 스터디 인원을 모집합니다.</div>
                        </div>
                        <div className='post-body'>
                            <div className='post-content'>본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                                본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                            </div>
                            <div className='post-comment'>
                                <img className="img-comment" src={comment} alt='' />
                                29</div>
                            <div className='post-hit'>
                                <img className="img-hit" src={hit} alt='' />
                                145</div>
                            <div className='post-like'>
                                <img className="img-like" src={like} alt='' />
                                123</div>
                        </div>
                        <div className='post-tag'>태그</div>
                        <div className='post-tail'>
                            <div className='post-owner'>홍길동</div>
                            <div className='post-date'>1분 전</div>
                        </div>
                        <div className='studies-line'></div>
                    </div>
                    <div className='post-studies'>
                        <div className='post-header'>
                            <div className='post-status'>모집중</div>
                            <div className='post-title'>WebGl 스터디 인원을 모집합니다.</div>
                        </div>
                        <div className='post-body'>
                            <div className='post-content'>본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                                본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                            </div>
                            <div className='post-comment'>
                                <img className="img-comment" src={comment} alt='' />
                                29</div>
                            <div className='post-hit'>
                                <img className="img-hit" src={hit} alt='' />
                                145</div>
                            <div className='post-like'>
                                <img className="img-like" src={like} alt='' />
                                123</div>
                        </div>
                        <div className='post-tag'>태그</div>
                        <div className='post-tail'>
                            <div className='post-owner'>홍길동</div>
                            <div className='post-date'>1분 전</div>
                        </div>
                        <div className='studies-line'></div>
                    </div>
                    <div className='post-studies'>
                        <div className='post-header'>
                            <div className='post-status'>모집중</div>
                            <div className='post-title'>WebGl 스터디 인원을 모집합니다.</div>
                        </div>
                        <div className='post-body'>
                            <div className='post-content'>본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                                본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                            </div>
                            <div className='post-comment'>
                                <img className="img-comment" src={comment} alt='' />
                                29</div>
                            <div className='post-hit'>
                                <img className="img-hit" src={hit} alt='' />
                                145</div>
                            <div className='post-like'>
                                <img className="img-like" src={like} alt='' />
                                123</div>
                        </div>
                        <div className='post-tag'>태그</div>
                        <div className='post-tail'>
                            <div className='post-owner'>홍길동</div>
                            <div className='post-date'>1분 전</div>
                        </div>
                        <div className='studies-line'></div>
                    </div>
                    <div className='post-studies'>
                        <div className='post-header'>
                            <div className='post-status'>모집중</div>
                            <div className='post-title'>WebGl 스터디 인원을 모집합니다.</div>
                        </div>
                        <div className='post-body'>
                            <div className='post-content'>본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                                본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                            </div>
                            <div className='post-comment'>
                                <img className="img-comment" src={comment} alt='' />
                                29</div>
                            <div className='post-hit'>
                                <img className="img-hit" src={hit} alt='' />
                                145</div>
                            <div className='post-like'>
                                <img className="img-like" src={like} alt='' />
                                123</div>
                        </div>
                        <div className='post-tag'>태그</div>
                        <div className='post-tail'>
                            <div className='post-owner'>홍길동</div>
                            <div className='post-date'>1분 전</div>
                        </div>
                        <div className='studies-line'></div>
                    </div>
                    <div className='post-studies'>
                        <div className='post-header'>
                            <div className='post-status'>모집중</div>
                            <div className='post-title'>WebGl 스터디 인원을 모집합니다.</div>
                        </div>
                        <div className='post-body'>
                            <div className='post-content'>본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                                본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용본문내용
                            </div>
                            <div className='post-comment'>
                                <img className="img-comment" src={comment} alt='' />
                                29</div>
                            <div className='post-hit'>
                                <img className="img-hit" src={hit} alt='' />
                                145</div>
                            <div className='post-like'>
                                <img className="img-like" src={like} alt='' />
                                123</div>
                        </div>
                        <div className='post-tag'>태그</div>
                        <div className='post-tail'>
                            <div className='post-owner'>홍길동</div>
                            <div className='post-date'>1분 전</div>
                        </div>
                        <div className='studies-line'></div>
                    </div>
                </div>
                <Footer />
            </div>
        </div>
    )
}

export default Studies