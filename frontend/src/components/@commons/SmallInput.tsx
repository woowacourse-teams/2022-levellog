import React from 'react';
import styled from 'styled-components';
import { InputStyle } from './Input';

const SmallInput = ({ labelText, inputRef, ...props }: InputProps) => {
  return (
    <>
      <label>{labelText}</label>
      <SmallInputStyle ref={inputRef} {...props} />
    </>
  );
};

interface InputProps {
  labelText: string;
  inputRef: React.Ref<HTMLInputElement>;
}

export const SmallInputStyle = styled(InputStyle)`
  width: 100px;
  height: 20px;
`;

export default SmallInput;
