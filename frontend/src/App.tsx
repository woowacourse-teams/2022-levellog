import { useContext, useState } from 'react';
import { useRoutes } from 'react-router-dom';

import styled from 'styled-components';

import Snackbar from 'components/@commons/snackbar/Snackbar';
import SnackbarContainer from 'components/@commons/snackbar/SnackbarContainer';
import Footer from 'components/Footer';
import Header from 'components/header/Header';
import { SnackbarContext } from 'contexts/snackbarContext';
import { routes } from 'routes/Routes';
import GlobalStyles from 'styles/GlobalStyle';

const App = () => {
  const content = useRoutes(routes);
  const [prevPathname, setPrevPathname] = useState('');
  const snackbars = useContext(SnackbarContext);

  if (location.pathname !== prevPathname) {
    window.scrollTo(0, 0);
    setPrevPathname(location.pathname);
  }

  return (
    <>
      <GlobalStyles />
      <Header />
      <PageContainer>{content}</PageContainer>
      <SnackbarContainer>
        {snackbars.map((snackbar, index) => (
          <Snackbar key={index}>{snackbar}</Snackbar>
        ))}
      </SnackbarContainer>
      <Footer />
    </>
  );
};

const PageContainer = styled.div`
  min-height: calc(100vh - 15rem);
`;

export default App;
