import Login from 'pages/Login';
import NotFound from 'pages/NotFound';
import FeedbackAdd from 'pages/feedback/FeedbackAdd';
import FeedbackEdit from 'pages/feedback/FeedbackEdit';
import FeedbackList from 'pages/feedback/FeedbackList';
import LevellogAdd from 'pages/levellogs/LevellogAdd';
import LevellogEdit from 'pages/levellogs/LevellogEdit';
import PreQuestionAdd from 'pages/preQuestion/PreQuestionAdd';
import PreQuestionEdit from 'pages/preQuestion/PreQuestionEdit';
import InterviewDetail from 'pages/teams/InterviewDetail';
import InterviewTeamAdd from 'pages/teams/InterviewTeamAdd';
import InterviewTeamEdit from 'pages/teams/InterviewTeamEdit';
import InterviewTeams from 'pages/teams/InterviewTeams';

import { ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import TeamStatus from './TeamStatus';
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
      {
        path: ROUTES_PATH.FEEDBACK_ADD,
        element: (
          // 제대로 되는지 백엔드 머지 후 테스트해야함
          <TeamStatus needStatus={TEAM_STATUS.READY || TEAM_STATUS.IN_PROGRESS}>
            <FeedbackAdd />
          </TeamStatus>
        ),
      },
      {
        path: ROUTES_PATH.LEVELLOG_ADD_ROUTE,
        element: (
          <TeamStatus needStatus={TEAM_STATUS.READY}>
            <LevellogAdd />
          </TeamStatus>
        ),
      },
      {
        path: ROUTES_PATH.LEVELLOG_EDIT,
        element: (
          <TeamStatus needStatus={TEAM_STATUS.READY}>
            <LevellogEdit />
          </TeamStatus>
        ),
      },
      {
        path: ROUTES_PATH.INTERVIEW_TEAMS_EDIT,
        element: (
          <TeamStatus needStatus={TEAM_STATUS.READY}>
            <InterviewTeamEdit />
          </TeamStatus>
        ),
      },
      {
        path: ROUTES_PATH.FEEDBACK_EDIT,
        element: (
          <TeamStatus needStatus={TEAM_STATUS.IN_PROGRESS}>
            <FeedbackEdit />
          </TeamStatus>
        ),
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
