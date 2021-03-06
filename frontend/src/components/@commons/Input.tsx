import styled from 'styled-components';

const Input = ({ width = '300px', height = '40px', inputRef, ...props }: InputProps) => {
  return <InputStyle width={width} height={height} ref={inputRef} {...props} />;
};

interface InputProps {
  width?: string;
  height?: string;
  inputRef?: React.Ref<HTMLInputElement>;
}

export const InputStyle = styled.input`
  width: ${(props) => props.width};
  height: ${(props) => props.height};
  font-size: 1.2rem;
  background-color: ${(props) => props.theme.WHITE};
  border-style: none;
  border-radius: 5px;
  box-sizing: border-box;
`;

export default Input;
