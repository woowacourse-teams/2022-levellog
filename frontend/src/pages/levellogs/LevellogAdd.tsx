import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import SubTitleLabel from 'components/@commons/Label';
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
    <>
      <ContentHeader title={'레벨로그 작성'}>
        <Button onClick={handleClickLevellogAddButton}>{'제출하기'}</Button>
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

export default LevellogAdd;
