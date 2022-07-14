import React from 'react';
import styled from 'styled-components';

const Button = ({ children, onClick, ...rest }: ButtonProps) => {
  return (
    <StyledButton onClick={onClick} {...rest}>
      {children}
    </StyledButton>
  );
};

interface ButtonProps {
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  children: string;
}

const StyledButton = styled.button`
  width: fit-content;
  cursor: pointer;
  background-color: #b4b4b4;
  border-style: none;
  font-size: 22px;
  font-weight: 500;
`;

export default Button;
