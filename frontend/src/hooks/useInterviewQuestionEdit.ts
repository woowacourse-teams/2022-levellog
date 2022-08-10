import { useEffect, useRef, useState } from 'react';

import { InterviewQuestionApiType, interviewQuestionType } from 'types/interviewQuestion';

const useInterviewQuestionEdit = ({
  interviewQuestionInfo,
  onClickDeleteInterviewQuestionButton,
  onClickEditInterviewQuestionButton,
}: useInterviewQuestionEditProps) => {
  const [isEditInterviewQuestion, setIsEditInterviewQuestion] = useState(false);
  const interviewQuestionEditRef = useRef<HTMLInputElement>(null);

  const handleToggleEditInterviewQuestion = () => {
    setIsEditInterviewQuestion((prev) => !prev);
  };

  const handleClickDeleteInterviewQuestionButton = async () => {
    onClickDeleteInterviewQuestionButton({ interviewQuestionId: interviewQuestionInfo.id });
  };

  const handleClickEditCompleteInterviewQuestionButton = async () => {
    setIsEditInterviewQuestion((prev) => !prev);
    if (interviewQuestionEditRef.current) {
      onClickEditInterviewQuestionButton({
        interviewQuestionId: interviewQuestionInfo.id,
        interviewQuestion: interviewQuestionEditRef.current.value,
      });
    }
  };

  useEffect(() => {
    if (isEditInterviewQuestion && interviewQuestionEditRef.current) {
      interviewQuestionEditRef.current.value = interviewQuestionInfo.interviewQuestion;
      interviewQuestionEditRef.current.focus();
    }
  }, [isEditInterviewQuestion]);

  return {
    isEditInterviewQuestion,
    interviewQuestionEditRef,
    handleToggleEditInterviewQuestion,
    handleClickDeleteInterviewQuestionButton,
    handleClickEditCompleteInterviewQuestionButton,
  };
};

interface useInterviewQuestionEditProps {
  interviewQuestionInfo: interviewQuestionType;
  onClickDeleteInterviewQuestionButton: ({
    interviewQuestionId,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId'>) => Promise<void>;
  onClickEditInterviewQuestionButton: ({
    interviewQuestionId,
    interviewQuestion,
  }: Pick<InterviewQuestionApiType, 'interviewQuestionId' | 'interviewQuestion'>) => Promise<void>;
}

export default useInterviewQuestionEdit;
