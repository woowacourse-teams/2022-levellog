export const GITHUB_LOGIN_URL = `https://github.com/login/oauth/authorize?client_id=${process.env.CLIENT_ID}&redirect_uri=${process.env.SERVICE_URI}/login`;

export const ROUTES_PATH = Object.freeze({
  HOME: '/',
  FEEDBACK_ROUTE: '/teams/:teamId/levellogs/:levellogId/feedbacks',
  FEEDBACK_ADD: `/teams/:teamId/levellogs/:levellogId/feedbacks/add`,
  LOGIN: '/login',
  LEVELLOG: '/levellog',
  LEVELLOG_ADD: '/levellog/add',
  LEVELLOG_ADD_ROUTE: '/levellog/add/:teamId',
  LEVELLOG_MODIFY: '/levellog/modify/teams/:teamId/levellogs/:levellogId',
  INTERVIEW_TEAMS: '/interview/teams',
  INTERVIEW_TEAMS_DETAIL: `/interview/teams/:teamId`,
  INTERVIEW_TEAMS_ADD: `/interview/teams/add`,
  NOT_FOUND: '*',
});

export const SERVER_PATH = Object.freeze({
  FEEDBACKS: `${process.env.API_URI}/feedbacks`,
  LEVELLOGS: `${process.env.API_URI}/levellogs`,
});

export const MESSAGE = Object.freeze({
  WRONG_ACCESS: '잘못된 접근입니다',
  NEED_LOGIN: '로그인이 필요한 페이지입니다',
});
