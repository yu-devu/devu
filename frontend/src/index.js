import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import axios from 'axios';

axios.defaults.withCredentials = true;
axios.defaults.headers.common['X-AUTH-ACCESS-TOKEN'] = localStorage.getItem('accessToken');

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);