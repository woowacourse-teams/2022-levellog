import styled from 'styled-components';

const Button = ({
  children,
  type = 'button',
  disabled = false,
  onClick,
  color,
  ...props
}: ButtonProps) => {
  return (
    <ButtonStyle disabled={disabled} onClick={onClick} color={color} {...props}>
      {children}
    </ButtonStyle>
  );
};

interface ButtonProps {
  children: string;
  type?: string;
  disabled?: boolean;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  color?: string;
  [props: string]: any;
}

export const ButtonStyle = styled.button`
  width: fit-content;
  padding: 0.75rem 1.4375rem;
  border: none;
  border-radius: 0.625rem;
  background-color: ${(props) => props.theme.new_default.BLUE};
  font-size: 1.25rem;
  font-weight: 500;
  color: ${(props) => props.theme.new_default.WHITE};
  white-space: pre;
  :hover {
    opacity: 70%;
  }
`;

export default Button;
