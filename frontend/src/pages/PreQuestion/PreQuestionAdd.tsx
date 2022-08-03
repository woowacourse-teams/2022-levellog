import { useEffect, useRef } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useLevellog from 'hooks/useLevellog';
import usePreQuestion from 'hooks/usePreQuestion';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import { Editor } from '@toast-ui/react-editor';
import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import UiEditor from 'components/@commons/UiEditor';
import LevellogReport from 'components/levellogs/LevellogReport';

const PreQuestionAdd = () => {
  const { levellog, getLevellog } = useLevellog();
  const { preQuestionRef, onClickPreQuestionAddButton } = usePreQuestion();

  const navigate = useNavigate();
  const { teamId, levellogId } = useParams();

  const handleClickPreQuestionAddButton = () => {
    if (typeof levellogId === 'string') {
      onClickPreQuestionAddButton({ levellogId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellog({ teamId, levellogId });
    }
  }, []);

  return (
    <FlexBox gap={1.875}>
      <S.Container>
        <ContentHeader title={'사전질문'}>
          <Button onClick={handleClickPreQuestionAddButton}>작성하기</Button>
        </ContentHeader>
        <S.Content>
          <LevellogReport levellog={levellog} />
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

export default PreQuestionAdd;
