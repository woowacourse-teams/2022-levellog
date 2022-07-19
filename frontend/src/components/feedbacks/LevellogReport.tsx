import { FeedbackAddContainer } from '../@commons/Style';

const LevellogReport = () => {
  return (
    <FeedbackAddContainer>
      <h2>결의 레벨로그</h2>
      <ul>
        <li>Virtual Dom이 하는 일은 무엇일까요?</li>
        <li>JSX는 무엇일까요?</li>
        <li>React에서 상태를 직접 변경하지 않고 setState() 를 사용하는 이유는 무엇일까요?</li>
        <li>
          React 17버전부터 이벤트들의 위임이 document에서 root element로 변경된 이유는 무엇일까요?
        </li>
        <li>React에서 key의 역할은 무엇일까요?</li>
        <li>React의 lifecycle은 어떻게 될까요?</li>
        <li>React에서 컴포넌트는 무엇일까요?</li>
        <li>React에서 속성은 불변 데이터일까요?</li>
        <li>제어 컴포넌트와 비제어 컴포넌트의 차이는 무엇일까요?</li>
      </ul>
    </FeedbackAddContainer>
  );
};

export default LevellogReport;
