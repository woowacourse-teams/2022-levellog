import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import usePreQuestion from 'hooks/usePreQuestion';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import UiEditor from 'components/@commons/UiEditor';
import LevellogReport from 'components/levellogs/LevellogReport';

const PreQuestionEdit = () => {
  const { levellogInfo, getLevellog } = useLevellog();
  const { preQuestionRef, getPreQuestionOnRef, onClickPreQuestionEditButton } = usePreQuestion();
  const { teamId, levellogId, preQuestionId } = useParams();
  const navigate = useNavigate();

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
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellog({ teamId, levellogId });
      getPreQuestionOnRef({ levellogId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.ERROR);
  }, []);

  return (
    <FlexBox gap={1.875}>
      <S.Container>
        <ContentHeader title={'사전질문 수정'}>
          <Button onClick={handleClickPreQuestionEditButton}>수정하기</Button>
        </ContentHeader>
        <S.Content>
          <LevellogReport levellogInfo={levellogInfo} />
          <S.UiEditorContainer>
            <S.Title>사전 질문</S.Title>
            <UiEditor
              needToolbar={true}
              autoFocus={true}
              height={'60rem'}
              contentRef={preQuestionRef}
              initialEditType={'markdown'}
            />
          </S.UiEditorContainer>
        </S.Content>
      </S.Container>
    </FlexBox>
  );
};

const S = {
  Container: styled.div`
    overflow: auto;
    width: 100%;
  `,

  UiEditorContainer: styled.div`
    overflow: auto;
    width: 48rem;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
  `,

  Title: styled.h2`
    margin-bottom: 1.875rem;
    font-size: 1.875rem;
  `,

  Content: styled.div`
    display: flex;
    overflow: auto;
    gap: 2.5rem;
    @media (max-width: 1024px) {
      gap: 1.25rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,
};

export default PreQuestionEdit;
