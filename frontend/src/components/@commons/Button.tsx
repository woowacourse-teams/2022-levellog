import React from 'react';
import styled from 'styled-components';

const Button = ({ children, onClick, color = '#f1f1f1', ...props }: ButtonProps) => {
  return (
    <ButtonStyle onClick={onClick} color={color} {...props}>
      {children}
    </ButtonStyle>
  );
};

interface ButtonProps {
  onClick?: any;
  children: string;
  color?: string;
}

const ButtonStyle = styled.button`
  width: max-content;
  height: 40px;
  cursor: pointer;
  background-color: ${(props) => props.color};
  text-align: center;
  padding: 5px 10px;
  border-radius: 5px;
  border-style: none;
  font-size: 22px;
  font-weight: 500;
`;

export default Button;
