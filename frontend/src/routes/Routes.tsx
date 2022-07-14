import React from 'react';
import { Routes, Route } from 'react-router-dom';

import FeedbackList from '../pages/FeedbackList';
import FeedbackAdd from '../pages/FeedbackAdd';
import Login from '../pages/Login';

import { ROUTES_PATH } from '../constants/constants';

export const routes = [
  {
    path: ROUTES_PATH.HOME,
    element: <FeedbackList />,
  },
  {
    path: ROUTES_PATH.FEEDBACK_ADD,
    element: <FeedbackAdd />,
  },
  {
    path: ROUTES_PATH.LOGIN,
    element: <Login />,
  },
];
