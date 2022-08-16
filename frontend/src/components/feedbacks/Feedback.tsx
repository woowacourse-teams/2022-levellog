import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import UiViewer from 'components/@commons/UiViewer';
import { FeedbackType } from 'types/feedback';

const Feedback = ({ loginUserId, feedbackInfo, teamId, levellogId, teamStatus }: FeedbackProps) => {
  return (
    <S.Container>
      <S.Header>
        <h3>{feedbackInfo.from.nickname}의 피드백</h3>
        {teamStatus === TEAM_STATUS.IN_PROGRESS && String(feedbackInfo.from.id) === loginUserId && (
          <Link
            to={`/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackInfo.id}/writer/${feedbackInfo.from.id}/edit`}
          >
            <Button>수정하기</Button>
          </Link>
        )}
      </S.Header>
      <FlexBox gap={1.5}>
        <S.FeedbackBox>
          <S.Title>학습 측면에서 좋은 점과 부족한 점은?</S.Title>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.study} />
          </S.Content>
        </S.FeedbackBox>
        <S.FeedbackBox>
          <S.Title>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</S.Title>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.speak} />
          </S.Content>
        </S.FeedbackBox>
        <S.FeedbackBox>
          <S.Title>기타 피드백 (위 2 질문 외에 다른 피드백을 주세요.)</S.Title>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.etc} />
          </S.Content>
        </S.FeedbackBox>
      </FlexBox>
    </S.Container>
  );
};

interface FeedbackProps {
  loginUserId: string;
  feedbackInfo: FeedbackType;
  teamId: string;
  levellogId: string;
  teamStatus: string;
}

const S = {
  Container: styled.div`
    width: 46.75rem;
    min-width: 46.75rem;
    min-height: 35.25rem;
    padding: 1.25rem 1.5625rem 1.25rem 1.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
    border-radius: 0.5rem;
    @media (max-width: 840px) {
      min-width: 15rem;
    }
  `,

  Header: styled.header`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.875rem;
  `,

  FeedbackBox: styled.div`
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
    width: 100%;
  `,

  Title: styled.h2`
    font-size: 1.25rem;
  `,

  Content: styled.div`
    width: 100%;
    min-height: 6.25rem;
    padding: 0 1rem;
    border-radius: 0.5rem;
    background-color: ${(props) => props.theme.default.LIGHT_GRAY};
    word-spacing: 0.0625rem;
    line-height: 1.875rem;
    @media (max-width: 560px) {
      width: 100%;
    }
  `,
};

export default Feedback;
