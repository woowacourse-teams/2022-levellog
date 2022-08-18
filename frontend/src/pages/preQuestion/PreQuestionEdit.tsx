import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import usePreQuestion from 'hooks/usePreQuestion';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import BottomBar from 'components/@commons/BottomBar';
import UiEditor from 'components/@commons/UiEditor';
import LevellogReport from 'components/levellogs/LevellogReport';

const PreQuestionEdit = () => {
  const { levellogInfo, getLevellog } = useLevellog();
  const { preQuestionRef, getPreQuestionOnRef, onClickPreQuestionEditButton } = usePreQuestion();

  const navigate = useNavigate();
  const { teamId, levellogId, preQuestionId } = useParams();

  const handleClickPreQuestionEditButton = () => {
    if (
      typeof teamId === 'string' &&
      typeof levellogId === 'string' &&
      typeof preQuestionId === 'string'
    ) {
      onClickPreQuestionEditButton({ teamId, levellogId, preQuestionId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellog({ teamId, levellogId });
      getPreQuestionOnRef({ levellogId });

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <S.Container>
      <S.Content>
        <S.LeftContent>
          <LevellogReport levellogInfo={levellogInfo} />
        </S.LeftContent>
        <S.RightContent>
          <UiEditor
            needToolbar={true}
            autoFocus={true}
            height={'60rem'}
            contentRef={preQuestionRef}
            initialEditType={'markdown'}
          />
        </S.RightContent>
      </S.Content>
      <BottomBar
        buttonText={'작성하기'}
        handleClickRightButton={handleClickPreQuestionEditButton}
      ></BottomBar>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    overflow: auto;
    flex-direction: column;
    @media (min-width: 1620px) {
      padding: 1.25rem calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 1.25rem 1.25rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,

  Content: styled.div`
    display: flex;
    gap: 1rem;
  `,

  LeftContent: styled.div`
    width: 50%;
    @media (max-width: 520px) {
      width: 100%;
    }
  `,

  LevellogTitle: styled.h2`
    margin-bottom: 1.875rem;
    font-size: 1.875rem;
  `,

  RightContent: styled.div`
    width: 50%;
    @media (max-width: 520px) {
      width: 100%;
    }
  `,

  UiEditorContainer: styled.div`
    overflow: auto;
    width: 48rem;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
  `,
};

export default PreQuestionEdit;
