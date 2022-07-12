import React from 'react';

import FeedbackList from '../pages/FeedbackList';
import FeedbackAdd from '../pages/FeedbackAdd';
import LevellogAdd from '../pages/LevellogAdd';
import Main from '../pages/Main';

import { ROUTES_PATH } from '../constants/constants';

export const routes = [
  {
    path: ROUTES_PATH.HOME,
    element: <Main />,
  },
  {
    path: ROUTES_PATH.FEEDBACK,
    element: <FeedbackList />,
  },
  {
    path: ROUTES_PATH.FEEDBACK_ADD,
    element: <FeedbackAdd />,
  },
  {
    path: ROUTES_PATH.LEVELLOG_ADD,
    element: <LevellogAdd />,
  },
];
