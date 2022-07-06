import React from 'react';
import styled from 'styled-components';

const Feedback = () => {
  return (
    <FeedbackContent>
      <h3>학습 측면에서 좋은 점과 부족한 점은?</h3>
      <p></p>
      <h3>인터뷰, 말하기 측면에서 좋은 점과 개선할 부분은?</h3>
      <p></p>
      <h3>기타 피드백 (위 2 질문 외에 다른 피드백을 주새요.)</h3>
      <p></p>
    </FeedbackContent>
  );
};

const FeedbackContent = styled.div``;

export default Feedback;
