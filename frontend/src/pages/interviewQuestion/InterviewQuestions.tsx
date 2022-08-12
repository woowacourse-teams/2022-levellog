import { useEffect } from 'react';

import styled from 'styled-components';

import useInterviewQuestion from 'hooks/useInterviewQuestion';

import ContentHeader from 'components/@commons/ContentHeader';
import { InterviewQuestionType } from 'types/interviewQuestion';

const InterviewQuestions = () => {
  const { interviewQuestionInfos, getInterviewQuestion } = useInterviewQuestion();

  useEffect(() => {
    getInterviewQuestion();
  }, []);

  return (
    <>
      <ContentHeader title={'내가 받은 인터뷰 질문'}></ContentHeader>
      <S.InterviewQuestionContent>
        {interviewQuestionInfos.length !== 0 &&
          interviewQuestionInfos.map((interviewQuestionInfo: InterviewQuestionType) => (
            <S.InterviewQuestionBox key={interviewQuestionInfo.id}>
              <S.InterviewQuestionText>
                {interviewQuestionInfo.interviewQuestion}
              </S.InterviewQuestionText>
            </S.InterviewQuestionBox>
          ))}
      </S.InterviewQuestionContent>
    </>
  );
};

const S = {
  InterviewQuestionContent: styled.div`
    display: flex;
    width: 100%;
    flex-direction: column;
    gap: 0.5rem;
  `,

  InterviewQuestionBox: styled.div`
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.GRAY};
  `,

  InterviewQuestionText: styled.p`
    line-height: 1.5rem;
    word-break: keep-all;
  `,
};

export default InterviewQuestions;