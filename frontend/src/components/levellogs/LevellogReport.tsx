import styled from 'styled-components';

import { FeedbackContainer, FeedbackTitle } from 'components/@commons/Style';

// 레벨로그 작성한 유저의 닉네임 가져오기
const LevellogReport = () => {
  return (
    <FeedbackContainer>
      <FeedbackTitle>레벨로그</FeedbackTitle>
      <LevellogContainer>
        <p>Virtual Dom이 하는 일은 무엇일까요?</p>
        <p>JSX는 무엇일까요?</p>
        <p>React에서 상태를 직접 변경하지 않고 setState() 를 사용하는 이유는 무엇일까요?</p>
        <p>
          React 17버전부터 이벤트들의 위임이 document에서 root element로 변경된 이유는 무엇일까요?
        </p>
        <p>React에서 key의 역할은 무엇일까요?</p>
        <p>React의 lifecycle은 어떻게 될까요?</p>
        <p>React에서 컴포넌트는 무엇일까요?</p>
        <p>React에서 속성은 불변 데이터일까요?</p>
        <p>제어 컴포넌트와 비제어 컴포넌트의 차이는 무엇일까요?</p>
      </LevellogContainer>
    </FeedbackContainer>
  );
};

const LevellogContainer = styled.div`
  overflow: hidden;
  width: 100%;
  min-height: 60.0625rem;
  padding: 1rem;
  background-color: ${(props) => props.theme.default.WHITE};
  border-radius: 0.5rem;
  @media (max-width: 520px) {
    max-width: 22.875rem;
    min-height: 0;
    flex-direction: column;
  }
`;

export default LevellogReport;
