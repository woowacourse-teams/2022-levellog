import { ROUTES_PATH } from '../constants/constants';
import FeedbackAdd from '../pages/FeedbackAdd';
import FeedbackList from '../pages/FeedbackList';
import Home from '../pages/Home';
import LevellogAdd from '../pages/LevellogAdd';
import Login from '../pages/Login';

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
    path: ROUTES_PATH.LEVELLOG_ADD,
    element: <LevellogAdd />,
  },
];
