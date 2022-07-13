import React, { Children } from 'react';
import styled from 'styled-components';

const Button = ({ children, onClick, ...rest }: ButtonProps) => {
  return (
    <StyledButton onClick={onClick} {...rest}>
      {children}
    </StyledButton>
  );
};

interface ButtonProps {
  onClick?: any;
  children: string;
}

const StyledButton = styled.button`
  width: 100px;
  height: 40px;
  cursor: pointer;
  background-color: #f1f1f1;
  padding: 5px;
  border-radius: 5px;
  border-style: none;
  font-size: 22px;
  font-weight: 500;
`;

export default Button;
