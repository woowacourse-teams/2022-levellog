import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import BottomBar from 'components/@commons/BottomBar';
import UiEditor from 'components/@commons/UiEditor';

const LevellogAdd = () => {
  const { levellogRef, onClickLevellogAddButton } = useLevellog();
  const { teamId } = useParams();
  const navigate = useNavigate();

  const handleClickLevellogAddButton = () => {
    if (typeof teamId === 'string') {
      onClickLevellogAddButton({ teamId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

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
        handleClickRightButton={handleClickLevellogAddButton}
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

export default LevellogAdd;
