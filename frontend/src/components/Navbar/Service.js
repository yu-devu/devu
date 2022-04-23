import React from 'react'
import { Link } from "react-router-dom";
import './service.css'

const Service = () => {
    return (
        <Link to="service">
            <button className="btn-service-nav">고객 센터</button>
        </Link>
    )
}

export default Service