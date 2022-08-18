import styled from 'styled-components';

import BottomBar from 'components/@commons/BottomBar';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import WriterDocument from 'components/WriterDocument';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import InterviewQuestion from 'components/interviewQuestion/InterviewQuestion';

const FeedbackEdit = () => {
  // const {
  //   state: {
  //     levellogInfo,
  //     feedbackWriterRole,
  //     interviewQuestionInfos,
  //     preQuestion,
  //     whichContentShow,
  //   },
  //   ref: { feedbackRef, interviewQuestionRef, interviewQuestionContentRef },
  //   handler: {
  //     onClickDeleteInterviewQuestionButton,
  //     onSubmitEditInterviewQuestion,
  //     handleClickFeedbackAddButton,
  //     handleSubmitInterviewQuestion,
  //     handleClickLevellogTag,
  //     handleClickPreQuestionTag,
  //   },
  // } = useFeedbackAddPage();
  // if (Object.keys(levellogInfo).length === 0) return <></>;
  // if (!feedbackWriterRole) return <></>;
  // const { author } = levellogInfo;
  // return (
  //   <>
  //     <ContentHeader
  //       imageUrl={author.profileUrl}
  //       title={`${author.nickname}의 인터뷰 피드백`}
  //     ></ContentHeader>
  //     <S.Container>
  //       <S.Content>
  //         <S.LeftContent>
  //           <FlexBox alignItems={'center'} gap={1}>
  //             {whichContentShow.levellog && <S.LevellogTitle>레벨로그</S.LevellogTitle>}
  //             {whichContentShow.preQuestion && <S.LevellogTitle>사전질문</S.LevellogTitle>}
  //             {feedbackWriterRole === 'OBSERVER' && <S.RoleContent>{'옵저버'}</S.RoleContent>}
  //             {feedbackWriterRole === 'INTERVIEWER' && <S.RoleContent>{'인터뷰어'}</S.RoleContent>}
  //             {feedbackWriterRole === 'INTERVIEWEE' && <S.RoleContent>{'인터뷰이'}</S.RoleContent>}
  //           </FlexBox>
  //           <WriterDocument
  //             levellogInfo={levellogInfo}
  //             preQuestion={preQuestion}
  //             whichContentShow={whichContentShow}
  //             handleClickLevellogTag={handleClickLevellogTag}
  //             handleClickPreQuestionTag={handleClickPreQuestionTag}
  //           />
  //         </S.LeftContent>
  //         <S.RightContent>
  //           <S.QuestionContent>
  //             <S.QuestionTitle>인터뷰에서 받은 질문</S.QuestionTitle>
  //             <InterviewQuestion
  //               interviewQuestionInfos={interviewQuestionInfos}
  //               interviewQuestionRef={interviewQuestionRef}
  //               interviewQuestionContentRef={interviewQuestionContentRef}
  //               onClickDeleteInterviewQuestionButton={onClickDeleteInterviewQuestionButton}
  //               onSubmitEditInterviewQuestion={onSubmitEditInterviewQuestion}
  //               handleSubmitInterviewQuestion={handleSubmitInterviewQuestion}
  //             />
  //           </S.QuestionContent>
  //           <S.FeedbackContent>
  //             <FeedbackFormat feedbackRef={feedbackRef} />
  //           </S.FeedbackContent>
  //         </S.RightContent>
  //       </S.Content>
  //       <BottomBar
  //         buttonText={'작성하기'}
  //         handleClickRightButton={handleClickFeedbackAddButton}
  //       ></BottomBar>
  //     </S.Container>
  //   </>
  // );

  return <></>;
};

export default FeedbackEdit;
