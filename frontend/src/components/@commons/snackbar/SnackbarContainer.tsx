import { useContext } from 'react';

import styled from 'styled-components';

import Snackbar from 'components/@commons/snackbar/Snackbar';
import { SnackbarContext } from 'contexts/snackbarContext';
import SnackbarPortal from 'portal/SnackbarPortal';

const SnackbarContainer = () => {
  const snackbars = useContext(SnackbarContext);

  return (
    <SnackbarPortal>
      <SnackbarContainerStyle>
        {snackbars.map((snackbar, index) => (
          <Snackbar key={index}>{snackbar}</Snackbar>
        ))}
      </SnackbarContainerStyle>
    </SnackbarPortal>
  );
};

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
