import { Suspense } from 'react';

import styled from 'styled-components';

import useFeedbackAdd from 'hooks/feedback/useFeedbackAdd';
import useRole from 'hooks/useRole';
import useContentTag from 'hooks/utils/useContentTag';
import usePreventGoBack from 'hooks/utils/usePreventGoBack';

import Loading from 'pages/status/Loading';

import BottomBar from 'components/@commons/BottomBar';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import ToolTip from 'components/@commons/ToolTip';
import WriterDocument from 'components/WriterDocument';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import InterviewQuestion from 'components/interviewQuestion/InterviewQuestion';

const FeedbackAdd = () => {
  const { feedbackRef, handleClickFeedbackAddButton } = useFeedbackAdd();
  const { whichContentShow, handleClickLevellogTag, handleClickPreQuestionTag } = useContentTag();
  const { authorInfo, feedbackWriterRole } = useRole();

  usePreventGoBack();

  return (
    <>
      <ContentHeader
        imageUrl={authorInfo?.profileUrl}
        title={`${authorInfo?.nickname}에 대한 레벨 인터뷰 피드백`}
      />
      <S.Container>
        <S.Content>
          <S.LeftContent>
            <FlexBox alignItems={'center'} gap={1}>
              {whichContentShow.levellog && <S.LevellogTitle>{'레벨로그'}</S.LevellogTitle>}
              {whichContentShow.preQuestion && <S.LevellogTitle>{'사전질문'}</S.LevellogTitle>}
              {feedbackWriterRole === 'OBSERVER' && <S.RoleContent>{'옵저버'}</S.RoleContent>}
              {feedbackWriterRole === 'INTERVIEWER' && <S.RoleContent>{'인터뷰어'}</S.RoleContent>}
              {feedbackWriterRole === 'INTERVIEWEE' && <S.RoleContent>{'인터뷰이'}</S.RoleContent>}
            </FlexBox>
            <Suspense fallback={<Loading />}>
              <WriterDocument
                whichContentShow={whichContentShow}
                handleClickLevellogTag={handleClickLevellogTag}
                handleClickPreQuestionTag={handleClickPreQuestionTag}
              />
            </Suspense>
          </S.LeftContent>
          <S.RightContent>
            <S.QuestionContent>
              <FlexBox gap={1}>
                <S.QuestionTitle>{'인터뷰에서 받은 질문'}</S.QuestionTitle>
                <ToolTip
                  toolTipText={`질문 텍스트를 클릭하면 수정가능합니다. 질문 수정 후 엔터를 눌러 반영해주세요.`}
                />
              </FlexBox>
              <InterviewQuestion />
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
      </S.Container>
    </>
  );
};

const S = {
  Container: styled.div`
    display: flex;
    overflow: auto;
    flex-direction: column;
    height: calc(100vh - 14.375rem);
    @media (min-width: 1620px) {
      padding: 0 calc((100vw - 100rem) / 2);
    }
    @media (max-width: 1620px) {
      padding: 0 1.25rem;
    }
    @media (max-width: 520px) {
      height: max-content;
    }
  `,

  Content: styled.div`
    display: flex;
    gap: 1rem;
    width: 100%;
    height: calc(100vh - 18.75rem);
    @media (max-width: 1024px) {
      gap: 1.875rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
      height: max-content;
    }
  `,

  LeftContent: styled.div`
    position: relative;
    width: 50%;
    height: inherit;
    @media (max-width: 520px) {
      width: 100%;
      height: max-content;
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
    overflow-x: hidden;
    overflow-y: auto;
    flex-direction: column;
    gap: 3.125rem;
    width: 50%;
    height: inherit;
    @media (max-width: 520px) {
      width: 100%;
      height: max-content;
    }
  `,

  QuestionContent: styled.div``,

  QuestionTitle: styled.h2`
    margin-bottom: 1.25rem;
    font-size: 1.875rem;
  `,

  FeedbackContent: styled.div``,
};

export default FeedbackAdd;
