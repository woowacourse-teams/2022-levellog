import React from 'react';
import styled from 'styled-components';

const Input = ({ labelText, inputRef, ...rest }: InputProps) => {
  return (
    <>
      <label>{labelText}</label>
      <InputStyle ref={inputRef} {...rest} />
    </>
  );
};

interface InputProps {
  labelText: string;
  inputRef: React.Ref<HTMLInputElement>;
}

export const InputStyle = styled.input`
  width: 600px;
  height: 60px;
  font-size: 1.2rem;
`;

export default Input;
