import React from 'react';
import { Link } from 'react-router-dom';
import { ROUTES_PATH } from '../constants/constants';

const FeedbackAdd = () => {
  return (
    <div>
      <h2>피드백 등록 페이지</h2>
      <Link to={ROUTES_PATH.HOME}>
        <button>등록!</button>
      </Link>
    </div>
  );
};

export default FeedbackAdd;
