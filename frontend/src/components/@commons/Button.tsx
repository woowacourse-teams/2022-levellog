import styled from 'styled-components';

const Button = ({ children, onClick, color, ...props }: ButtonProps) => {
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

const ButtonStyle = styled.button`
  width: fit-content;
  cursor: pointer;
  background-color: none;
  border: 2px solid ${(props) => props.theme.default.BLACK};
  font-size: 22px;
  font-weight: 500;
`;

export default Button;
