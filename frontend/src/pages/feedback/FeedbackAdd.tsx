import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

import styled from 'styled-components';

import useFeedback from 'hooks/useFeedback';
import useLevellog from 'hooks/useLevellog';
import useRole from 'hooks/useRole';
import { useTeam } from 'hooks/useTeams';

import { ROUTES_PATH } from 'constants/constants';
import { MESSAGE } from 'constants/constants';

import Button from 'components/@commons/Button';
import ContentHeader from 'components/@commons/ContentHeader';
import FlexBox from 'components/@commons/FlexBox';
import FeedbackFormat from 'components/feedbacks/FeedbackFormat';
import LevellogReport from 'components/levellogs/LevellogReport';
import { InterviewTeamType } from 'types/team';

const FeedbackAdd = () => {
  const { feedbackRef, onClickFeedbackAddButton } = useFeedback();
  const { levellog, getLevellog } = useLevellog();
  const { teamId, levellogId } = useParams();
  const { getTeam } = useTeam();
  const { loginUserRole, getLoginUserRole } = useRole();
  const navigate = useNavigate();
  const [writer, setWriter] = useState('');

  const handleClickFeedbackAddButton = () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      onClickFeedbackAddButton({ teamId, levellogId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  //임시용
  const init = async () => {
    if (typeof teamId === 'string' && typeof levellogId === 'string') {
      getLevellog({ teamId, levellogId });
      const localTeam = await getTeam();
      const levellogWriter = (localTeam as InterviewTeamType).participants.filter(
        (participant) => String(participant.levellogId) === levellogId,
      )[0];
      setWriter(levellogWriter.nickname);
      getLoginUserRole({ teamId, participantId: levellogWriter.memberId });

      return;
    }
    alert(MESSAGE.WRONG_ACCESS);
    navigate(ROUTES_PATH.HOME);
  };

  useEffect(() => {
    init();
  }, []);

  if (!loginUserRole || !writer) return <div></div>;

  return (
    <FlexBox gap={1.875}>
      <S.Container>
        <ContentHeader title={`${writer}의 레벨 인터뷰 피드백`}>
          <Button onClick={handleClickFeedbackAddButton}>등록하기</Button>
        </ContentHeader>
        <S.Content>
          <S.RoleContent>나의 역할: {loginUserRole}</S.RoleContent>
          <LevellogReport levellog={levellog} />
          <FeedbackFormat feedbackRef={feedbackRef} />
        </S.Content>
      </S.Container>
    </FlexBox>
  );
};

const S = {
  Container: styled.div`
    overflow: auto;
    width: 100%;
  `,

  RoleContent: styled.div`
    position: absolute;
    top: 12.5rem;
    left: 17.5rem;
    font-weight: 700;
  `,

  Content: styled.div`
    display: flex;
    overflow: auto;
    gap: 4.875rem;
    @media (max-width: 1024px) {
      gap: 1.875rem;
    }
    @media (max-width: 520px) {
      flex-direction: column;
    }
  `,
};

export default FeedbackAdd;
