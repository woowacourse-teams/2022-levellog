import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import BottomBar from 'components/@commons/BottomBar';
import UiEditor from 'components/@commons/UiEditor';

const LevellogEdit = () => {
  const { levellogRef, getLevellogOnRef, onClickLevellogEditButton } = useLevellog();
  const { showSnackbar } = useSnackbar();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  const handleClickLevellogEditButton = () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onClickLevellogEditButton({ teamId, levellogId });

      return;
    }
    showSnackbar({ message: MESSAGE.WRONG_ACCESS });
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellogOnRef({ teamId, levellogId });

      return;
    }
    showSnackbar({ message: MESSAGE.WRONG_ACCESS });
    navigate(ROUTES_PATH.ERROR);
  }, []);

  return (
    <S.Container>
      <UiEditor
        needToolbar={true}
        autoFocus={true}
        height={'50rem'}
        contentRef={levellogRef}
        initialEditType={'markdown'}
      />
      <BottomBar
        buttonText={'작성하기'}
        handleClickRightButton={handleClickLevellogEditButton}
      ></BottomBar>
    </S.Container>
  );
};

const S = {
  Container: styled.main`
    @media (min-width: 1620px) {
      padding: 1.25rem calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 1.25rem 1.25rem;
    }
  `,
};

export default LevellogEdit;
