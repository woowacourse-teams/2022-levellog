import styled from 'styled-components';

import useFeedbackAddPage from 'hooks/useFeedbackAddPage';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import WriterDocument from 'components/WriterDocument';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import InterviewQuestion from 'components/interviewQuestion/InterviewQuestion';

const FeedbackAdd = () => {
  const {
    state: {
      levellogInfo,
      feedbackWriterRole,
      interviewQuestionInfos,
      preQuestion,
      whichContentShow,
    },
    ref: { feedbackRef, interviewQuestionRef, interviewQuestionContentRef },
    handler: {
      onClickDeleteInterviewQuestionButton,
      onSubmitEditInterviewQuestion,
      handleClickFeedbackAddButton,
      handleSubmitInterviewQuestion,
      handleClickLevellogTag,
      handleClickPreQuestionTag,
    },
  } = useFeedbackAddPage();

  return (
    <S.Container>
      <ContentHeader
        title={`${levellogInfo.author && levellogInfo.author.nickname}의 레벨 인터뷰 피드백`}
      >
        <Button onClick={handleClickFeedbackAddButton}>등록하기</Button>
      </ContentHeader>
      <S.Content>
        <S.LeftContent>
          <FlexBox alignItems={'center'} gap={1}>
            {whichContentShow.levellog && <S.LevellogTitle>레벨로그</S.LevellogTitle>}
            {whichContentShow.preQuestion && <S.LevellogTitle>사전질문</S.LevellogTitle>}
            <S.RoleContent>나의 역할: {feedbackWriterRole}</S.RoleContent>
          </FlexBox>
          <WriterDocument
            levellogInfo={levellogInfo}
            preQuestion={preQuestion}
            whichContentShow={whichContentShow}
            handleClickLevellogTag={handleClickLevellogTag}
            handleClickPreQuestionTag={handleClickPreQuestionTag}
          />
        </S.LeftContent>
        <S.RightContent>
          <InterviewQuestion
            interviewQuestionInfos={interviewQuestionInfos}
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

  LevellogTitle: styled.h2`
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
