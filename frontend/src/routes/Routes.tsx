import Login from 'pages/Login';
import FeedbackAdd from 'pages/feedback/FeedbackAdd';
import FeedbackEdit from 'pages/feedback/FeedbackEdit';
import FeedbackList from 'pages/feedback/FeedbackList';
import LevellogAdd from 'pages/levellogs/LevellogAdd';
import LevellogEdit from 'pages/levellogs/LevellogEdit';
import PreQuestionAdd from 'pages/preQuestion/PreQuestionAdd';
import PreQuestionEdit from 'pages/preQuestion/PreQuestionEdit';
import NotFound from 'pages/status/NotFound';
import InterviewDetail from 'pages/teams/InterviewDetail';
import InterviewTeamAdd from 'pages/teams/InterviewTeamAdd';
import InterviewTeamEdit from 'pages/teams/InterviewTeamEdit';
import InterviewTeams from 'pages/teams/InterviewTeams';

import { REQUIRE_AUTH, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import TeamStatus from './TeamStatus';
import { Auth } from 'routes/Auth';

export const routes = [
  {
    path: ROUTES_PATH.FEEDBACK_ROUTE,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.IN_TEAM}>
        <FeedbackList />
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.INTERVIEW_TEAMS_ADD,
    element: <InterviewTeamAdd />,
  },
  {
    path: ROUTES_PATH.PREQUESTION_ADD,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.NOT_ME}>
        <PreQuestionAdd />
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.PREQUESTION_EDIT,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.NOT_ME}>
        <PreQuestionEdit />
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.FEEDBACK_ADD,
    element: (
      // 제대로 되는지 백엔드 머지 후 테스트해야함
      <Auth requireAuth={REQUIRE_AUTH.NOT_ME}>
        <TeamStatus needStatus={TEAM_STATUS.READY || TEAM_STATUS.IN_PROGRESS}>
          <FeedbackAdd />
        </TeamStatus>
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.LEVELLOG_ADD_ROUTE,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.IN_TEAM}>
        <TeamStatus needStatus={TEAM_STATUS.READY}>
          <LevellogAdd />
        </TeamStatus>
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.LEVELLOG_EDIT,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.ME}>
        <TeamStatus needStatus={TEAM_STATUS.READY}>
          <LevellogEdit />
        </TeamStatus>
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.INTERVIEW_TEAMS_EDIT,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.HOST}>
        <TeamStatus needStatus={TEAM_STATUS.READY}>
          <InterviewTeamEdit />
        </TeamStatus>
      </Auth>
    ),
  },
  {
    path: ROUTES_PATH.FEEDBACK_EDIT,
    element: (
      <Auth requireAuth={REQUIRE_AUTH.ME}>
        <TeamStatus needStatus={TEAM_STATUS.IN_PROGRESS}>
          <FeedbackEdit />
        </TeamStatus>
      </Auth>
    ),
  },
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

  // {
  //   element: <AuthLogin needLogin={false} />,
  //   children: [
  //     {
  //       path: ROUTES_PATH.HOME,
  //       element: <InterviewTeams />,
  //     },
  //     {
  //       path: ROUTES_PATH.LOGIN,
  //       element: <Login />,
  //     },
  //     {
  //       path: ROUTES_PATH.INTERVIEW_TEAMS,
  //       element: <InterviewTeams />,
  //     },
  //     {
  //       path: ROUTES_PATH.INTERVIEW_TEAMS_DETAIL,
  //       element: <InterviewDetail />,
  //     },
  //     {
  //       path: '*',
  //       element: <NotFound />,
  //     },
  //   ],
  // },
];
