import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useInterviewQuestion from 'hooks/useInterviewQuestion';
import useLevellog from 'hooks/useLevellog';
import useRole from 'hooks/useRole';

import { ROUTES_PATH } from 'constants/constants';
import { MESSAGE } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import InterviewQuestion from 'components/interviewQuestion/InterviewQuestion';
import LevellogReport from 'components/levellogs/LevellogReport';

const FeedbackAdd = () => {
  const { feedbackRef, onClickFeedbackAddButton } = useFeedback();
  const { levellog, getLevellog } = useLevellog();
  const { levellogWriter, feedbackWriterRole, getWriterInfo } = useRole();
  const {
    interviewQuestionsInfo,
    interviewQuestionRef,
    interviewQuestionContentRef,
    getInterviewQuestion,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  } = useInterviewQuestion();
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
      getWriterInfo({ teamId, levellogId });
      getInterviewQuestion();

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  }, []);

  return (
    <S.Container>
      <ContentHeader title={`${levellogWriter}의 레벨 인터뷰 피드백`}>
        <Button onClick={handleClickFeedbackAddButton}>등록하기</Button>
      </ContentHeader>
      <S.Content>
        <S.LeftContent>
          <FlexBox alignItems={'center'} gap={1}>
            <S.FeedbackTitle>레벨로그</S.FeedbackTitle>
            <S.RoleContent>나의 역할: {feedbackWriterRole}</S.RoleContent>
          </FlexBox>
          <LevellogReport levellog={levellog} />
        </S.LeftContent>
        <S.RightContent>
          <InterviewQuestion
            interviewQuestionsInfo={interviewQuestionsInfo}
            interviewQuestionRef={interviewQuestionRef}
            interviewQuestionContentRef={interviewQuestionContentRef}
            onClickDeleteInterviewQuestionButton={onClickDeleteInterviewQuestionButton}
            onSubmitEditInterviewQuestion={onSubmitEditInterviewQuestion}
            handleSubmitInterviewQuestion={handleSubmitInterviewQuestion}
          />
          <FeedbackFormat feedbackRef={feedbackRef} />
        </S.RightContent>
      </S.Content>
    </S.Container>
  );
};

const S = {
  Container: styled.div`
    position: relative;
    width: 100%;
  `,

  Content: styled.div`
    display: flex;
    gap: 4.875rem;
    width: 100%;
    @media (max-width: 1024px) {
      gap: 1.875rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,

  LeftContent: styled.div`
    width: 50%;
    @media (max-width: 520px) {
      width: 100%;
    }
  `,

  FeedbackTitle: styled.h2`
    margin-bottom: 1.875rem;
    font-size: 1.875rem;
  `,

  RoleContent: styled.p`
    margin-bottom: 1.875rem;
    font-weight: 700;
  `,

  RightContent: styled.div`
    display: flex;
    flex-direction: column;
    gap: 3.125rem;
    width: 50%;
    @media (max-width: 520px) {
      width: 100%;
    }
  `,
};

export default FeedbackAdd;
