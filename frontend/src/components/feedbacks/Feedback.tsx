import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { TEAM_STATUS } from 'constants/constants';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import Image from 'components/@commons/Image';
import UiViewer from 'components/@commons/UiViewer';
import { FeedbackType } from 'types/feedback';

const Feedback = ({ loginUserId, feedbackInfo, teamId, levellogId, teamStatus }: FeedbackProps) => {
  console.log('Feedback', feedbackInfo.from.id, loginUserId);
  return (
    <S.Container>
      <S.Header>
        <FlexBox alignItems={'center'} gap={0.375}>
          <Image src={feedbackInfo.from.profileUrl} sizes={'MEDIUM'} />
          <S.AuthorText>{feedbackInfo.from.nickname}의 피드백</S.AuthorText>
        </FlexBox>
        {teamStatus === TEAM_STATUS.IN_PROGRESS && feedbackInfo.from.id === loginUserId && (
          <Link
            to={`/teams/${teamId}/levellogs/${levellogId}/feedbacks/${feedbackInfo.id}/writer/${feedbackInfo.from.id}/edit`}
          >
            <Button>수정하기</Button>
          </Link>
        )}
      </S.Header>
      <S.Box>
        <S.FeedbackBox>
          <S.Question>학습 측면에서 좋은 점과 부족한 점은?</S.Question>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.study} />
          </S.Content>
        </S.FeedbackBox>
        <S.FeedbackBox>
          <S.Question>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</S.Question>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.speak} />
          </S.Content>
        </S.FeedbackBox>
        <S.FeedbackBox>
          <S.Question>기타 피드백 (위 2 질문 외에 다른 피드백을 주세요.)</S.Question>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.etc} />
          </S.Content>
        </S.FeedbackBox>
      </S.Box>
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
    margin: auto;
    width: 100%;
    max-width: 71rem;
    min-width: 18.75rem;
    margin-bottom: 3.125rem;
    border: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
    border-radius: 0.5rem;
    box-shadow: 0 0 1rem ${(props) => props.theme.new_default.GRAY};
  `,

  Header: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 0.375rem;
    padding: 0.875rem;
    width: 100%;
    border-bottom: 0.0625rem solid ${(props) => props.theme.new_default.GRAY};
  `,

  AuthorText: styled.p`
    font-size: 2rem;
    font-weight: 300;
  `,

  Box: styled.div`
    display: flex;
    flex-direction: column;
    gap: 1.25rem;
    padding: 0.875rem;
  `,

  FeedbackBox: styled.div`
    display: flex;
    flex-direction: column;
    gap: 0.875rem;
    width: 100%;
  `,

  Question: styled.h2`
    font-size: 1.25rem;
    font-weight: 300;
    color: ${(props) => props.theme.new_default.DARK_BLACK};
  `,

  Content: styled.div`
    width: 100%;
    min-height: 6.25rem;
    padding: 0 1rem;
    border: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    word-spacing: 0.0625rem;
    line-height: 1.875rem;
  `,
};

export default Feedback;
