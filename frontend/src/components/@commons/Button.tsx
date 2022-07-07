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
  width: 200px;
  height: 40px;
  cursor: pointer;
`;

export default Button;
