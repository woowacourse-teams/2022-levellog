import styled from 'styled-components';

import Input from 'components/@commons/Input';
import { FeedbackTitle } from 'components/@commons/Style';

const FeedbackForm = ({ feedbackRef }: FeedbackFormProps) => {
  return (
    <S.Container>
      <FeedbackTitle>Feedback</FeedbackTitle>
      <S.Content>
        <label>학습 측면에서 좋은 점과 부족한 점은?</label>
        <Input
          width="100%"
          height="18.75rem"
          inputRef={(el: HTMLInputElement) => (feedbackRef.current[0] = el)}
        />
        <label>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</label>
        <Input
          width="100%"
          height="18.75rem"
          inputRef={(el: HTMLInputElement) => (feedbackRef.current[1] = el)}
        />
        <label>기타 피드백 (위 2 질문 외에 다른 피드백도 주세요.)</label>
        <Input
          width="100%"
          height="12.5rem"
          inputRef={(el: HTMLInputElement) => (feedbackRef.current[2] = el)}
        />
      </S.Content>
    </S.Container>
  );
};

interface FeedbackFormProps {
  feedbackRef: React.MutableRefObject<HTMLInputElement[]>;
}

const S = {
  Container: styled.div`
    overflow: auto;
    width: 48rem;
    @media (max-width: 520px) {
      max-width: 22.875rem;
    }
  `,

  Content: styled.div`
    display: flex;
    flex-direction: column;
    align-content: space-between;
    gap: 1.5rem;
    width: 100%;
  `,
};
export default FeedbackForm;
