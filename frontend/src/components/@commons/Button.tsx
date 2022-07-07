import React, { Children } from 'react';
import styled from 'styled-components';

interface ButtonProps {
  children: string;
}

const Button = ({ children, ...rest }: ButtonProps) => {
  return <StyledButton {...rest}>{children}</StyledButton>;
};

const StyledButton = styled.button`
  width: 200px;
  height: 40px;
  cursor: pointer;
`;

export default Button;
