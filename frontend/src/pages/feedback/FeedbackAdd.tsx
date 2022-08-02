import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useLevellog from 'hooks/useLevellog';

import { ROUTES_PATH } from 'constants/constants';
import { MESSAGE } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import LevellogReport from 'components/levellogs/LevellogReport';

const FeedbackAdd = () => {
  const { feedbackRef, onClickFeedbackAddButton } = useFeedback();
  const { levellog, getLevellog } = useLevellog();
  const { teamId, levellogId } = useParams();
  const navigate = useNavigate();

  const handleClickFeedbackAddButton = () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onClickFeedbackAddButton({ teamId, levellogId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellog({ teamId, levellogId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <FlexBox gap={1.875}>
      <S.Container>
        <ContentHeader title={'레벨로그 피드백'}>
          <Button onClick={handleClickFeedbackAddButton}>등록하기</Button>
        </ContentHeader>
        <S.Content>
          <LevellogReport levellog={levellog} />
          <FeedbackFormat feedbackRef={feedbackRef} />
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

  Content: styled.div`
    display: flex;
    overflow: auto;
    gap: 4.875rem;
    @media (max-width: 1024px) {
      gap: 1.875rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,
};

export default FeedbackAdd;
