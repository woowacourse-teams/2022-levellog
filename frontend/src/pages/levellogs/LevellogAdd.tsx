import { useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import useSnackbar from 'hooks/useSnackbar';

import { MESSAGE } from 'constants/constants';

import BottomBar from 'components/@commons/BottomBar';
import UiEditor from 'components/@commons/markdownEditor/UiEditor';

const LevellogAdd = () => {
  const { levellogRef, onClickLevellogAddButton } = useLevellog();
  const { showSnackbar } = useSnackbar();
  const { teamId } = useParams();

  const handleClickLevellogAddButton = () => {
    if (typeof teamId === 'string') {
      onClickLevellogAddButton({ teamId });

      return;
    }
    showSnackbar({ message: MESSAGE.WRONG_ACCESS });
  };

  return (
    <S.Container>
      {/* <UiEditor
        needToolbar={true}
        autoFocus={true}
        contentRef={levellogRef}
        initialEditType={'markdown'}
      /> */}
      <BottomBar buttonText={'작성하기'} handleClickRightButton={handleClickLevellogAddButton} />
    </S.Container>
  );
};

const S = {
  Container: styled.main`
    height: calc(100vh - 14.375rem);
    @media (min-width: 1620px) {
      padding: 1.25rem calc((100vw - 100rem) / 2) 0 calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 1.25rem 1.25rem 0 1.25rem;
    }
  `,
};

export default LevellogAdd;
