import styled from 'styled-components';

const InterviewQuestionButton = ({ handleClick, children }: InterviewQuestionButtonProps) => {
  return <S.Button onClick={handleClick}>{children}</S.Button>;
};

interface InterviewQuestionButtonProps {
  handleClick: () => void;
  children: JSX.Element;
}

const S = {
  Button: styled.button`
    width: 1.875rem;
    height: 1.875rem;
    border: none;
    border-radius: 0.9375rem;
    background-color: ${(props) => props.theme.default.GRAY};
  `,
};

export default InterviewQuestionButton;
