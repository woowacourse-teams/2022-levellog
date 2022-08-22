import styled from 'styled-components';

const Snackbar = ({ children }: SnackbarProps) => {
  return (
    <SnackbarStyle>
      <SnackbarText>{children}</SnackbarText>
    </SnackbarStyle>
  );
};

const SnackbarStyle = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin: auto;
  width: calc(100% - 2.5rem);
  max-width: 71rem;
  height: 6.25rem;
  border-radius: 0.875rem;
  background-color: ${(props) => props.theme.new_default.DARK_GRAY};
`;

const SnackbarText = styled.p`
  font-size: 1.875rem;
  color: ${(props) => props.theme.new_default.WHITE};
`;

interface SnackbarProps {
  children: string;
}

export default Snackbar;
