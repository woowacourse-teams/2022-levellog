import styled from 'styled-components';

import useFeedbackAddPage from 'hooks/useFeedbackAddPage';

import BottomBar from 'components/@commons/BottomBar';
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

  if (Object.keys(levellogInfo).length === 0) return <></>;
  if (!feedbackWriterRole) return <></>;
  const { author } = levellogInfo;

  return (
    <>
      <ContentHeader
        imageUrl={author.profileUrl}
        title={`${author.nickname}의 인터뷰 피드백`}
      ></ContentHeader>
      <S.GridContainer>
        <S.Content>
          <S.LeftContent>
            <FlexBox alignItems={'center'} gap={1}>
              {whichContentShow.levellog && <S.LevellogTitle>레벨로그</S.LevellogTitle>}
              {whichContentShow.preQuestion && <S.LevellogTitle>사전질문</S.LevellogTitle>}
              {feedbackWriterRole === 'OBSERVER' && <S.RoleContent>{'옵저버'}</S.RoleContent>}
              {feedbackWriterRole === 'INTERVIEWER' && <S.RoleContent>{'인터뷰어'}</S.RoleContent>}
              {feedbackWriterRole === 'INTERVIEWEE' && <S.RoleContent>{'인터뷰이'}</S.RoleContent>}
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
            <S.QuestionContent>
              <S.QuestionTitle>인터뷰에서 받은 질문</S.QuestionTitle>
              <InterviewQuestion
                interviewQuestionInfos={interviewQuestionInfos}
                interviewQuestionRef={interviewQuestionRef}
                interviewQuestionContentRef={interviewQuestionContentRef}
                onClickDeleteInterviewQuestionButton={onClickDeleteInterviewQuestionButton}
                onSubmitEditInterviewQuestion={onSubmitEditInterviewQuestion}
                handleSubmitInterviewQuestion={handleSubmitInterviewQuestion}
              />
            </S.QuestionContent>
            <S.FeedbackContent>
              <FeedbackFormat feedbackRef={feedbackRef} />
            </S.FeedbackContent>
          </S.RightContent>
        </S.Content>
        <BottomBar
          buttonText={'작성하기'}
          handleClickRightButton={handleClickFeedbackAddButton}
        ></BottomBar>
      </S.GridContainer>
    </>
  );
};

const S = {
  GridContainer: styled.div`
    display: flex;
    overflow: auto;
    flex-direction: column;
    @media (min-width: 1620px) {
      padding: 20px calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 20px 1.25rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,

  Content: styled.div`
    display: flex;
    gap: 16px;
    width: 100%;
    @media (max-width: 1024px) {
      gap: 1.875rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,

  LeftContent: styled.div`
    position: relative;
    width: 50%;
    @media (max-width: 520px) {
      width: 100%;
    }
  `,

  RoleContent: styled.div`
    padding: 0.625rem 30px;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 1.25rem;
    margin-bottom: 20px;
    background-color: ${(props) => props.theme.new_default.DARK_BLACK};
    color: ${(props) => props.theme.new_default.WHITE};
    font-weight: 700;
  `,

  LevellogTitle: styled.h2`
    margin-bottom: 20px;
    font-size: 1.875rem;
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

  QuestionContent: styled.div``,

  QuestionTitle: styled.h2`
    margin-bottom: 20px;
    font-size: 1.875rem;
  `,

  FeedbackContent: styled.div``,
};

export default FeedbackAdd;
