import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useContentTag from 'hooks/useContentTag';
import useFeedback from 'hooks/useFeedback';
import useInterviewQuestion from 'hooks/useInterviewQuestion';
import useLevellog from 'hooks/useLevellog';
import usePreQuestion from 'hooks/usePreQuestion';
import useRole from 'hooks/useRole';

import Loading from 'pages/status/Loading';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

import BottomBar from 'components/@commons/BottomBar';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import WriterDocument from 'components/WriterDocument';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import InterviewQuestion from 'components/interviewQuestion/InterviewQuestion';

const FeedbackEdit = () => {
  const { preQuestion, getPreQuestion } = usePreQuestion();
  const { levellogInfo, getLevellog } = useLevellog();
  const { whichContentShow, handleClickLevellogTag, handleClickPreQuestionTag } = useContentTag();
  const { getFeedbackOnRef, feedbackRef, onClickFeedbackEditButton } = useFeedback();
  const { feedbackWriterRole, getWriterInfo } = useRole();
  const { teamId, levellogId, feedbackId } = useParams();
  const navigate = useNavigate();
  const {
    interviewQuestionInfos,
    interviewQuestionRef,
    interviewQuestionContentRef,
    getInterviewQuestion,
    onClickDeleteInterviewQuestionButton,
    onSubmitEditInterviewQuestion,
    handleSubmitInterviewQuestion,
  } = useInterviewQuestion();

  const handleClickFeedbackEditButton = () => {
    if (
      typeof teamId === 'string' &&
      typeof feedbackId === 'string' &&
      typeof levellogId === 'string'
    ) {
      onClickFeedbackEditButton({ teamId, levellogId, feedbackId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.ERROR);
  };

  const init = async () => {
    if (
      typeof teamId === 'string' &&
      typeof feedbackId === 'string' &&
      typeof levellogId === 'string'
    ) {
      const res = await getLevellog({ teamId, levellogId });
      getFeedbackOnRef({ levellogId, feedbackId });
      getWriterInfo({ teamId, participantId: res!.author.id });
      getInterviewQuestion();
      getPreQuestion({ levellogId });

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.ERROR);
  };

  useEffect(() => {
    init();
  }, []);

  if (Object.keys(levellogInfo).length === 0) return <Loading />;
  if (!feedbackWriterRole) return <Loading />;
  if (!levellogId || !feedbackId) return <Loading />;

  return (
    <>
      <ContentHeader
        imageUrl={levellogInfo.author.profileUrl}
        title={`${levellogInfo.author.nickname}의 인터뷰 피드백`}
      ></ContentHeader>
      <S.Container>
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
          buttonText={'수정하기'}
          handleClickRightButton={handleClickFeedbackEditButton}
        ></BottomBar>
      </S.Container>
    </>
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
  `,

  Content: styled.div`
    display: flex;
    gap: 1rem;
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
    display: flex;
    align-items: center;
    height: 1.875rem;
    padding: 0.625rem 0.9375rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    border-radius: 1.25rem;
    margin-bottom: 1.25rem;
    background-color: ${(props) => props.theme.new_default.DARK_BLACK};
    color: ${(props) => props.theme.new_default.WHITE};
    font-weight: 700;
  `,

  LevellogTitle: styled.h2`
    margin-bottom: 1.25rem;
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
    margin-bottom: 1.25rem;
    font-size: 1.875rem;
  `,

  FeedbackContent: styled.div``,
};

export default FeedbackEdit;
