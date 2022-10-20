import { useEffect, useRef, useState } from 'react';

import { InterviewQuestionApiType, InterviewQuestionInfoType } from 'types/interviewQuestion';

const useInterviewQuestionEdit = ({
  interviewQuestionInfo,
  onClickDeleteInterviewQuestionButton,
  onSubmitEditInterviewQuestion,
}: useInterviewQuestionEditProps) => {
  const [isEditInterviewQuestion, setIsEditInterviewQuestion] = useState(false);
  const interviewQuestionEditRef = useRef<HTMLInputElement>(null);

  const handleClickEditInterviewQuestionButton = () => {
    setIsEditInterviewQuestion((prev) => !prev);
  };

  const handleClickDeleteInterviewQuestionButton = () => {
    onClickDeleteInterviewQuestionButton({ interviewQuestionId: interviewQuestionInfo.id });
  };

  const handleSubmitEditCompleteInterviewQuestion = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsEditInterviewQuestion((prev) => !prev);
    if (interviewQuestionEditRef.current) {
      onSubmitEditInterviewQuestion({
        interviewQuestionId: interviewQuestionInfo.id,
        interviewQuestion: interviewQuestionEditRef.current.value,
      });
    }
  };

  useEffect(() => {
    if (isEditInterviewQuestion && interviewQuestionEditRef.current) {
      interviewQuestionEditRef.current.value = interviewQuestionInfo.content;
      interviewQuestionEditRef.current.focus();
    }
  }, [isEditInterviewQuestion]);

  return {
    isEditInterviewQuestion,
    interviewQuestionEditRef,
    handleClickEditInterviewQuestionButton,
    handleClickDeleteInterviewQuestionButton,
    handleSubmitEditCompleteInterviewQuestion,
  };
};

interface useInterviewQuestionEditProps {
  interviewQuestionInfo: InterviewQuestionInfoType;
  onClickDeleteInterviewQuestionButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => Promise<void>;
  onSubmitEditInterviewQuestion: ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => Promise<void>;
}

export default useInterviewQuestionEdit;
