import styled from 'styled-components';

import SnackbarPortal from 'portal/SnackbarPortal';

const SnackbarContainer = ({ children }: any) => {
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
  position: relative;
  top: -7.5rem;
`;

export default SnackbarContainer;
