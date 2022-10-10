import React, { useState } from 'react';

import styled from 'styled-components';

import Input from 'components/@commons/Input';

const TeamFormInput = ({
  label,
  type,
  value,
  errorText,
  validate,
  inputRef,
  onChange,
  children,
}: TeamFormInputProps) => {
  const [isCorrectValue, setIsCorrectValue] = useState(true);

  const handleOnBlurInput = (e: FocusEvent) => {
    const input = e.target as HTMLInputElement;
    if (validate) {
      setIsCorrectValue(validate({ value: input.value }));
    }
  };

  return (
    <S.InputContainer>
      <S.Label>{label}</S.Label>
      {children}
      <S.Input
        type={type}
        value={value}
        inputRef={inputRef}
        onChange={onChange}
        onBlur={handleOnBlurInput}
      />
      <S.ErrorBox>{isCorrectValue || <S.ErrorText>{errorText}</S.ErrorText>}</S.ErrorBox>
    </S.InputContainer>
  );
};

interface TeamFormInputProps {
  label: string;
  type?: string;
  value?: string;
  errorText?: string;
  validate?: ([props]: any) => boolean;
  inputRef?: (el: HTMLInputElement) => HTMLInputElement;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  children?: JSX.Element;
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
