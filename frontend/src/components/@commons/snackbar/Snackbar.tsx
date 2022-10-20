import styled from 'styled-components';

const Snackbar = ({ children }: SnackbarProps) => {
  return (
    <SnackbarStyle>
      <SnackbarText aria-live={'polite'} aria-atomic={'true'} aria-label={children}>
        {children}
      </SnackbarText>
    </SnackbarStyle>
  );
};

const SnackbarStyle = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 auto;
  padding: 0.625rem;
  width: 20rem;
  max-width: 71rem;
  height: 3.75rem;
  border-radius: 0.875rem;
  background-color: ${(props) => props.theme.new_default.DARK_GRAY};
  animation: fade 2s linear;
  @keyframes fade {
    0%,
    100% {
      opacity: 0;
    }
    10%,
    90% {
      opacity: 1;
    }
  }
`;

const SnackbarText = styled.p`
  font-size: 0.875rem;
  color: ${(props) => props.theme.new_default.WHITE};
`;

interface SnackbarProps {
  children: string;
}

export default Snackbar;
