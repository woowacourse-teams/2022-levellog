import Login from 'pages/Login';
import FeedbackAdd from 'pages/feedback/FeedbackAdd';
import FeedbackEdit from 'pages/feedback/FeedbackEdit';
import Feedbacks from 'pages/feedback/Feedbacks';
import InterviewQuestions from 'pages/interviewQuestion/InterviewQuestions';
import LevellogAdd from 'pages/levellogs/LevellogAdd';
import LevellogEdit from 'pages/levellogs/LevellogEdit';
import PreQuestionAdd from 'pages/preQuestion/PreQuestionAdd';
import PreQuestionEdit from 'pages/preQuestion/PreQuestionEdit';
import Error from 'pages/status/Error';
import NotFound from 'pages/status/NotFound';
import InterviewDetail from 'pages/teams/InterviewDetail';
import InterviewTeamAdd from 'pages/teams/InterviewTeamAdd';
import InterviewTeamEdit from 'pages/teams/InterviewTeamEdit';
import InterviewTeams from 'pages/teams/InterviewTeams';

import { REQUIRE_AUTH, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import Auth from 'routes/Auth';
import AuthLogin from 'routes/AuthLogin';
import TeamStatus from 'routes/TeamStatus';

// 인증&인가가 잘못되었음
// AUTHOR 인증&인가가 추가되어야 하며 , ~edit페이지 URI에 writerId가 추가되어야함

export const routes = [
  {
    element: <AuthLogin needLogin={true} />,
    children: [
      {
        path: ROUTES_PATH.FEEDBACK_ROUTE,
        element: (
          <Auth requireAuth={REQUIRE_AUTH.IN_TEAM}>
            <Feedbacks />
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
          //author로 변경
          <Auth requireAuth={REQUIRE_AUTH.NOT_ME}>
            <PreQuestionEdit />
          </Auth>
        ),
      },
      {
        path: ROUTES_PATH.FEEDBACK_ADD,
        element: (
          <Auth requireAuth={REQUIRE_AUTH.NOT_ME}>
            <TeamStatus allowedStatuses={[TEAM_STATUS.READY, TEAM_STATUS.IN_PROGRESS]}>
              <FeedbackAdd />
            </TeamStatus>
          </Auth>
        ),
      },
      {
        path: ROUTES_PATH.LEVELLOG_ADD_ROUTE,
        element: (
          <Auth requireAuth={REQUIRE_AUTH.IN_TEAM}>
            <TeamStatus allowedStatuses={[TEAM_STATUS.READY]}>
              <LevellogAdd />
            </TeamStatus>
          </Auth>
        ),
      },
      {
        path: ROUTES_PATH.LEVELLOG_EDIT,
        element: (
          //author로 변경
          <Auth requireAuth={REQUIRE_AUTH.ME}>
            <TeamStatus allowedStatuses={[TEAM_STATUS.READY]}>
              <LevellogEdit />
            </TeamStatus>
          </Auth>
        ),
      },
      {
        path: ROUTES_PATH.INTERVIEW_TEAMS_EDIT,
        element: (
          <Auth requireAuth={REQUIRE_AUTH.HOST}>
            <TeamStatus allowedStatuses={[TEAM_STATUS.READY]}>
              <InterviewTeamEdit />
            </TeamStatus>
          </Auth>
        ),
      },
      {
        path: ROUTES_PATH.FEEDBACK_EDIT,
        element: (
          <Auth requireAuth={REQUIRE_AUTH.AUTHOR}>
            <TeamStatus allowedStatuses={[TEAM_STATUS.IN_PROGRESS]}>
              <FeedbackEdit />
            </TeamStatus>
          </Auth>
        ),
      },
      {
        path: ROUTES_PATH.INTERVIEW_QUESTION,
        element: <InterviewQuestions />,
      },
    ],
  },
  {
    element: <AuthLogin needLogin={false} />,
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
        path: ROUTES_PATH.ERROR,
        element: <Error />,
      },
      {
        path: '*',
        element: <NotFound />,
      },
    ],
  },
];
