import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import useSnackbar from 'hooks/utils/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

const useTeamsCondition = () => {
  const { showSnackbar } = useSnackbar();
  const navigate = useNavigate();

  const accessToken = localStorage.getItem('accessToken');

  const [teamsCondition, setTeamsCondition] = useState({
    open: true,
    close: false,
    my: false,
  });

  const handleClickOpenTeamsButton = () => {
    if (teamsCondition.open) return;
    setTeamsCondition({ open: true, close: false, my: false });
  };

  const handleClickCloseTeamsButton = () => {
    if (teamsCondition.close) return;
    setTeamsCondition({ open: false, close: true, my: false });
  };

  const handleClickMyTeamsButton = () => {
    if (teamsCondition.my) return;
    setTeamsCondition({ open: false, close: false, my: true });
  };

  const handleClickTeamAddButton = () => {
    if (accessToken) {
      navigate(ROUTES_PATH.INTERVIEW_TEAMS_ADD);
      return;
    }

    showSnackbar({ message: MESSAGE.NEED_LOGIN_SERVICE });
  };

  return {
    teamsCondition,
    handleClickOpenTeamsButton,
    handleClickCloseTeamsButton,
    handleClickMyTeamsButton,
    handleClickTeamAddButton,
  };
};

export default useTeamsCondition;
