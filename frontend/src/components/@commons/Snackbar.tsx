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
  margin: 0 auto;
  width: calc(100% - 2.5rem);
  max-width: 71rem;
  height: 6.25rem;
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
  @media (max-width: 520px) {
    width: 20rem;
    height: 3.75rem;
  }
`;

const SnackbarText = styled.p`
  font-size: 1.875rem;
  color: ${(props) => props.theme.new_default.WHITE};
  @media (max-width: 520px) {
    font-size: 0.875rem;
  }
`;

interface SnackbarProps {
  children: string;
}

export default Snackbar;
