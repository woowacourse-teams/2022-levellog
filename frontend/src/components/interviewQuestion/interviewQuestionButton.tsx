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
    width: 3.75rem;
    height: 1.875rem;
    border: none;
    border-radius: 0.625rem;
    background-color: ${(props) => props.theme.default.GRAY};
  `,
};

export default InterviewQuestionButton;
