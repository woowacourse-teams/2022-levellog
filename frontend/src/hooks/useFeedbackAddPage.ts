import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import useFeedback from 'hooks/useFeedback';
import useInterviewQuestion from 'hooks/useInterviewQuestion';
import useLevellog from 'hooks/useLevellog';
import useRole from 'hooks/useRole';

import { MESSAGE, ROUTES_PATH } from 'constants/constants';

const useFeedbackAddPage = () => {
  const { levellogInfo, getLevellog } = useLevellog();
  const { feedbackRef, onClickFeedbackAddButton } = useFeedback();
  const { feedbackWriterRole, getWriterInfo } = useRole();
  const {
    interviewQuestionInfos,
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

  const init = async () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      const res = await getLevellog({ teamId, levellogId });
      await getWriterInfo({ teamId, participantId: res!.author.id });
      getInterviewQuestion();

      return;
    }

    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    init();
  }, []);

  return {
    state: { levellogInfo, feedbackWriterRole, interviewQuestionInfos },
    ref: { feedbackRef, interviewQuestionRef, interviewQuestionContentRef },
    handler: {
      onClickDeleteInterviewQuestionButton,
      onSubmitEditInterviewQuestion,
      handleClickFeedbackAddButton,
      handleSubmitInterviewQuestion,
    },
  };
};

export default useFeedbackAddPage;
