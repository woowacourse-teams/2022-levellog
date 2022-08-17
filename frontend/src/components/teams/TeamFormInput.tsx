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
    border: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
    @media (min-width: 560px) and (max-width: 760px) {
      width: 27.5rem;
    }
    @media (max-width: 560px) {
      width: calc(100vw - 5rem);
    }
  `,
};

export default TeamFormInput;
