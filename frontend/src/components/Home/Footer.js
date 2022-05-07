import React from 'react'
import { footerItems } from "./FooterItems";
import './footer.css'
import { Link } from "react-router-dom";
import foot_logo from "../../img/logo_black.png"

const Footer = () => {
    return (
        <div className="footer">
            <ul className="footer-item">
                {footerItems.map((item) => {
                    return (
                        <li key={item.id} className={item.cName}>
                            <Link to={item.path}>{item.title}</Link>
                        </li>
                    );
                })}
            </ul>
            <hr className="line" />
            <div className="footer-intro">
                <img className="img-footer" alt="" src={foot_logo} />
                <h5>(주)데뷰   대표자 : 홍길동  | 사업자등록번호 : 123-456-789</h5>
                <h5>주소 : 경상북도 경산시 대학로 280</h5>
                <br></br>
                <h5 className="copyright">@DEVU.ALL.RIGHTS RESERVED</h5>
            </div>
        </div>
    )
}

export default Footer