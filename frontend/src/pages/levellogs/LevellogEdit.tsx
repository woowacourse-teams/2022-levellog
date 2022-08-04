import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import SubTitleLabel from 'components/@commons/Label';
import UiEditor from 'components/@commons/UiEditor';

const LevellogEdit = () => {
  const { levellogRef, getLevellogOnRef, onClickLevellogEditButton } = useLevellog();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  const handleClickEditButton = () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onClickLevellogEditButton({ teamId, levellogId });

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellogOnRef({ teamId, levellogId });

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <>
      <ContentHeader title={'레벨로그 수정'}>
        <Button onClick={handleClickEditButton}>{'수정하기'}</Button>
      </ContentHeader>
      <S.Content>
        <SubTitleLabel>Level Log</SubTitleLabel>
        <UiEditor
          needToolbar={true}
          autoFocus={true}
          height={'50rem'}
          contentRef={levellogRef}
          initialEditType={'markdown'}
        />
      </S.Content>
    </>
  );
};

const S = {
  Content: styled.main`
    display: flex;
    flex-direction: column;
  `,
};

export default LevellogEdit;
