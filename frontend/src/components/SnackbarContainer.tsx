import styled from 'styled-components';

import SnackbarPortal from 'portal/SnackbarPortal';

const SnackbarContainer = ({ children }: SnackbarContainerProps) => {
  return (
    <SnackbarPortal>
      <SnackbarContainerStyle>{children}</SnackbarContainerStyle>
    </SnackbarPortal>
  );
};

interface SnackbarContainerProps {
  children: JSX.Element[];
}

const SnackbarContainerStyle = styled.div`
  position: fixed;
  bottom: 1rem;
  display: flex;
  flex-direction: column-reverse;
  gap: 1rem;
  z-index: 20;
  width: 100%;
`;

export default SnackbarContainer;
