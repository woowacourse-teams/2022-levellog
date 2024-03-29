import styled from 'styled-components';

const Input = ({
  width = '18.75rem',
  height = '2.5rem',
  placeholder,
  value,
  inputRef,
  onChange,
  ...props
}: InputProps) => {
  return (
    <InputStyle
      width={width}
      height={height}
      value={value}
      ref={inputRef}
      onChange={onChange}
      {...props}
    />
  );
};

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  width?: string;
  height?: string;
  placeholder?: string;
  value?: string | undefined;
  inputRef?: React.Ref<HTMLInputElement>;
}

export const InputStyle = styled.input`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  padding: 1rem;
  border-style: none;
  border-radius: 0.3125rem;
  background-color: ${(props) => props.theme.default.WHITE};
  font-size: 1.2rem;
  word-wrap: break-word;
`;

export default Input;
