import { useState } from 'react';

const useTeamsCondition = () => {
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

  return {
    teamsCondition,
    handleClickOpenTeamsButton,
    handleClickCloseTeamsButton,
    handleClickMyTeamsButton,
  };
};

export default useTeamsCondition;
