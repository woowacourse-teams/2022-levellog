import styled from 'styled-components';

import Input from 'components/@commons/Input';
import { FeedbackContainer, FeedbackTitle } from 'components/@commons/Style';

const FeedbackForm = ({ feedbackRef, handleSubmitFeedbackForm }: any) => {
  return (
    <FeedbackContainer>
      <FeedbackTitle>Feedback</FeedbackTitle>
      <FormStyle onSubmit={handleSubmitFeedbackForm}>
        <label>학습 측면에서 좋은 점과 부족한 점은?</label>
        <Input
          width="100%"
          height="300px"
          inputRef={(el: HTMLInputElement) => (feedbackRef.current[0] = el)}
        />
        <label>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</label>
        <Input
          width="100%"
          height="300px"
          inputRef={(el: HTMLInputElement) => (feedbackRef.current[1] = el)}
        />
        <label>기타 피드백 (위 2 질문 외에 다른 피드백도 주세요.)</label>
        <Input
          width="100%"
          height="200px"
          inputRef={(el: HTMLInputElement) => (feedbackRef.current[2] = el)}
        />
      </FormStyle>
    </FeedbackContainer>
  );
};

const FormStyle = styled.form`
  display: flex;
  width: 100%;
  gap: 1.5rem;
  flex-direction: column;
  align-content: space-between;
`;

export default FeedbackForm;
