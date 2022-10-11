import { lazy, Suspense } from 'react';

import Copyright from 'pages/Copyright';
import Loading from 'pages/status/Loading';

import { REQUIRE_AUTH, ROUTES_PATH, TEAM_STATUS } from 'constants/constants';

import { MemberProvider } from 'contexts/memberContext';
import Auth from 'routes/Auth';
import AuthLogin from 'routes/AuthLogin';
import TeamStatus from 'routes/TeamStatus';

const Home = lazy(() => import('pages/Home'));
const Login = lazy(() => import('pages/Login'));
const FeedbackAdd = lazy(() => import('pages/feedback/FeedbackAdd'));
const FeedbackEdit = lazy(() => import('pages/feedback/FeedbackEdit'));
const Feedbacks = lazy(() => import('pages/feedback/Feedbacks'));
const InterviewQuestions = lazy(() => import('pages/interviewQuestion/InterviewQuestions'));
const LevellogAdd = lazy(() => import('pages/levellogs/LevellogAdd'));
const LevellogEdit = lazy(() => import('pages/levellogs/LevellogEdit'));
const PreQuestionAdd = lazy(() => import('pages/preQuestion/PreQuestionAdd'));
const PreQuestionEdit = lazy(() => import('pages/preQuestion/PreQuestionEdit'));
const Error = lazy(() => import('pages/status/Error'));
const NotFound = lazy(() => import('pages/status/NotFound'));
const InterviewDetail = lazy(() => import('pages/teams/InterviewDetail'));
const InterviewTeamAdd = lazy(() => import('pages/teams/InterviewTeamAdd'));
const InterviewTeamEdit = lazy(() => import('pages/teams/InterviewTeamEdit'));
const InterviewQuestionSearch = lazy(
  () => import('pages/interviewQuestion/InterviewQuestionSearch'),
);

export const routes = [
  {
    element: <AuthLogin needLogin={true} />,
    children: [
      {
        path: ROUTES_PATH.INTERVIEW_TEAMS_ADD,
        element: (
          <MemberProvider>
            <InterviewTeamAdd />
          </MemberProvider>
        ),
      },
      {
        path: ROUTES_PATH.INTERVIEW_TEAMS_EDIT,
        element: (
          <MemberProvider>
            <Auth requireAuth={REQUIRE_AUTH.HOST}>
              <TeamStatus allowedStatuses={[TEAM_STATUS.READY]}>
                <InterviewTeamEdit />
              </TeamStatus>
            </Auth>
          </MemberProvider>
        ),
      },
      {
        path: ROUTES_PATH.FEEDBACKS,
        element: (
          <Auth requireAuth={REQUIRE_AUTH.IN_TEAM}>
            <Feedbacks />
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
        path: ROUTES_PATH.LEVELLOG_ADD,
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
          <Auth requireAuth={REQUIRE_AUTH.AUTHOR}>
            <TeamStatus allowedStatuses={[TEAM_STATUS.READY]}>
              <LevellogEdit />
            </TeamStatus>
          </Auth>
        ),
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
          <Auth requireAuth={REQUIRE_AUTH.AUTHOR}>
            <PreQuestionEdit />
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
        element: <Home />,
      },
      {
        path: ROUTES_PATH.INTERVIEW_QUESTION_SEARCH,
        element: <InterviewQuestionSearch />,
      },
      {
        path: ROUTES_PATH.LOGIN,
        element: <Login />,
      },
      {
        path: ROUTES_PATH.INTERVIEW_TEAMS_DETAIL,
        element: (
          <Suspense fallback={<Loading />}>
            <InterviewDetail />
          </Suspense>
        ),
      },
      {
        path: ROUTES_PATH.ERROR,
        element: <Error />,
      },
      {
        path: '*',
        element: <NotFound />,
      },
      {
        path: ROUTES_PATH.COPYRIGHT,
        element: <Copyright />,
      },
    ],
  },
];
