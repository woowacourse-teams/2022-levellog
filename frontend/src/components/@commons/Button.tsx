import React from 'react';
import styled from 'styled-components';

const Button = ({ children, onClick, ...props }: ButtonProps) => {
  return (
    <ButtonStyle onClick={onClick} color={color} {...props}>
      {children}
    </ButtonStyle>
  );
};

interface ButtonProps {
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  children: string;
  color?: string;
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
