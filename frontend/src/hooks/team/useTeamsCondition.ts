import { useState } from 'react';

const useTeamsCondition = () => {
  const [teamsCondition, setTeamsCondition] = useState({
    open: true,
    close: false,
    my: false,
  });

  const handleClickFilterButtons = (e: React.MouseEvent<HTMLElement>) => {
    const eventTarget = e.target as HTMLElement;

    switch (eventTarget.innerText) {
      case '진행중인 인터뷰':
        if (teamsCondition.open) return;

        setTeamsCondition({ open: true, close: false, my: false });
        break;
      case '종료된 인터뷰':
        if (teamsCondition.close === true) return;

        setTeamsCondition({ open: false, close: true, my: false });
        break;
      case '나의 인터뷰':
        if (teamsCondition.my) return;

        setTeamsCondition({ open: false, close: false, my: true });
        break;
    }
  };

  return {
    teamsCondition,
    handleClickFilterButtons,
  };
};

export default useTeamsCondition;
