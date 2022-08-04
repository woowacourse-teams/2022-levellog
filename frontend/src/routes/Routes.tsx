import Home from 'pages/Home';
import Login from 'pages/Login';
import NotFound from 'pages/NotFound';
import FeedbackAdd from 'pages/feedback/FeedbackAdd';
import FeedbackList from 'pages/feedback/FeedbackList';
import LevellogAdd from 'pages/levellogs/LevellogAdd';
import LevellogEdit from 'pages/levellogs/LevellogEdit';
import PreQuestionAdd from 'pages/prequestion/PreQuestionAdd';
import PreQuestionEdit from 'pages/prequestion/PreQuestionEdit';
import InterviewDetail from 'pages/teams/InterviewDetail';
import InterviewTeamAdd from 'pages/teams/InterviewTeamAdd';
import InterviewTeams from 'pages/teams/InterviewTeams';

import { ROUTES_PATH } from 'constants/constants';

import Auth from 'routes/Auth';

export const routes = [
  {
    element: <Auth needLogin={true} />,
    children: [
      {
        path: ROUTES_PATH.FEEDBACK_ROUTE,
        element: <FeedbackList />,
      },
      {
        path: ROUTES_PATH.FEEDBACK_ADD,
        element: <FeedbackAdd />,
      },
      {
        path: ROUTES_PATH.LEVELLOG_ADD_ROUTE,
        element: <LevellogAdd />,
      },
      {
        path: ROUTES_PATH.LEVELLOG_MODIFY,
        element: <LevellogEdit />,
      },
      {
        path: ROUTES_PATH.INTERVIEW_TEAMS_ADD,
        element: <InterviewTeamAdd />,
      },
      {
        path: ROUTES_PATH.PREQUESTION_ADD,
        element: <PreQuestionAdd />,
      },
      {
        path: ROUTES_PATH.PREQUESTION_EDIT,
        element: <PreQuestionEdit />,
      },
    ],
  },
  {
    element: <Auth needLogin={false} />,
    children: [
      {
        path: ROUTES_PATH.HOME,
        element: <InterviewTeams />,
      },
      {
        path: ROUTES_PATH.LOGIN,
        element: <Login />,
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
    ],
  },
];
