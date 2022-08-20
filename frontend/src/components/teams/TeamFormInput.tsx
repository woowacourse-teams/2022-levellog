import React, { useState } from 'react';

import styled from 'styled-components';

import { MESSAGE } from 'constants/constants';
import { interviewTitleValidate } from 'constants/validate';

import Input from 'components/@commons/Input';

const TeamFormInput = ({
  label,
  type,
  errorText,
  validate,
  inputRef,
  onChange,
  onBlur,
  children,
  ...props
}: TeamFormInputProps) => {
  const [isErrorText, setIsErrorText] = useState(false);

  const handleOnBlurInput = (e: FocusEvent) => {
    const input = e.target as HTMLInputElement;
    if (validate) {
      setIsErrorText(validate({ text: input.value }));
    }
  };

  return (
    <S.InputContainer>
      <S.Label>{label}</S.Label>
      {children}
      <S.Input type={type} inputRef={inputRef} onChange={onChange} onBlur={handleOnBlurInput} />
      <S.ErrorBox>{isErrorText && <S.ErrorText>{errorText}</S.ErrorText>}</S.ErrorBox>
    </S.InputContainer>
  );
};

interface TeamFormInputProps {
  label: string;
  type?: string;
  errorText?: string;
  validate?: ([props]: any) => boolean;
  inputRef?: (el: HTMLInputElement) => HTMLInputElement;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur?: (e: FocusEvent) => void;
  children?: JSX.Element;
  [props: string]: any;
}

const S = {
  InputContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 100%;
  `,

  Label: styled.label`
    margin-bottom: 1rem;
    font-size: 1rem;
  `,

  Input: styled(Input)`
    width: calc(100% - 2rem);
    height: 1.125rem;
    padding: 1rem;
    border: 1px solid ${(props) => props.theme.new_default.LIGHT_GRAY};
  `,

  ErrorBox: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 100%;
    margin-top: 0.5rem;
  `,

  ErrorText: styled.div`
    color: ${(props) => props.theme.new_default.RED};
  `,
};

export default TeamFormInput;
