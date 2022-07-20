import Home from 'pages/Home';
import Login from 'pages/Login';
import NotFound from 'pages/NotFound';
import FeedbackAdd from 'pages/feedback/FeedbackAdd';
import FeedbackList from 'pages/feedback/FeedbackList';
import LevellogAdd from 'pages/levellogs/LevellogAdd';
import InterviewDetail from 'pages/teams/InterviewDetail';
import InterviewTeams from 'pages/teams/InterviewTeams';

import { ROUTES_PATH } from 'constants/constants';

import RequireAuth from 'components/RequireAuth';

export const routes = [
  {
    path: ROUTES_PATH.HOME,
    element: <Home />,
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
    path: ROUTES_PATH.LOGIN,
    element: <Login />,
  },
  {
    path: ROUTES_PATH.LEVELLOG_ADD_DETAIL,
    element: (
      <RequireAuth>
        <LevellogAdd />,
      </RequireAuth>
    ),
  },
  {
    path: ROUTES_PATH.INTERVIEW_TEAMS,
    element: <InterviewTeams />,
  },
  {
    path: ROUTES_PATH.INTERVIEW_TEAMS_DETAIL,
    element: <InterviewDetail />,
  },
  {
    path: '*',
    element: <NotFound />,
  },
];
