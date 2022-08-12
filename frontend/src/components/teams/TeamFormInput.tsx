import styled from 'styled-components';

import Input from 'components/@commons/Input';

const TeamFormInput = ({ label, inputRef, children, ...props }: TeamFormInputProps) => {
  return (
    <S.InputContainer>
      <S.Label>{label}</S.Label>
      {children}
      <S.Input inputRef={inputRef} {...props} />
    </S.InputContainer>
  );
};

interface TeamFormInputProps {
  label: string;
  inputRef?: (el: HTMLInputElement) => HTMLInputElement;
  children?: JSX.Element;
  [props: string]: any;
}

const S = {
  InputContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
  `,

  Label: styled.label`
    margin-bottom: 1rem;
    font-size: 1rem;
  `,

  Input: styled(Input)`
    width: 40.625rem;
    height: 1.125rem;
    @media (max-width: 1024px) {
      width: 31.25rem;
    }
    @media (max-width: 560px) {
      width: 18.75rem;
    }
  `,
};

export default TeamFormInput;
