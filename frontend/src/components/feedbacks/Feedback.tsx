import styled from 'styled-components';

import Button from 'components/@commons/Button';
import FlexBox from 'components/@commons/FlexBox';
import UiViewer from 'components/@commons/UiViewer';
import { FeedbackCustomHookType, FeedbackType } from 'types/feedback';

const Feedback = ({ feedbackInfo, levellogId, onClickDeleteButton }: FeedbackProps) => {
  const handleClickDeleteButton = () => {
    onClickDeleteButton({ feedbackInfo, levellogId });
  };

  return (
    <S.Container>
      <S.Header>
        <h3>{feedbackInfo.from.nickname}의 피드백</h3>
        <Button onClick={handleClickDeleteButton}>삭제하기</Button>
      </S.Header>
      <FlexBox gap={1.5}>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>학습 측면에서 좋은 점과 부족한 점은?</h3>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.study} />
          </S.Content>
        </FlexBox>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</h3>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.speak} />
          </S.Content>
        </FlexBox>
        <FlexBox flexFlow={'column'} gap={1.25}>
          <h3>기타 피드백 (위 2 질문 외에 다른 피드백을 주세요.)</h3>
          <S.Content>
            <UiViewer content={feedbackInfo.feedback.etc} />
          </S.Content>
        </FlexBox>
      </FlexBox>
    </S.Container>
  );
};

interface FeedbackProps {
  feedbackInfo: FeedbackType;
  levellogId: string;
  onClickDeleteButton: ({
    feedbackInfo,
    levellogId,
  }: Pick<FeedbackCustomHookType, 'levellogId' | 'feedbackInfo'>) => Promise<void>;
}

const S = {
  Container: styled.div`
    overflow: hidden;
    width: 46.75rem;
    min-width: 46.75rem;
    min-height: 35.25rem;
    padding: 1.25rem 1.5625rem 1.25rem 1.5rem;
    background-color: ${(props) => props.theme.default.WHITE};
    border-radius: 8px;
    @media (max-width: 560px) {
      min-width: 15rem;
    }
  `,

  Header: styled.header`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.875rem;
  `,

  Content: styled.div`
    width: 43.75rem;
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
