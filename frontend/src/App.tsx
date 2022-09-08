import { useContext, useState, Suspense } from 'react';
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
    <Container>
      <GlobalStyles />
      <Header />
      <Suspense>
        <PageContainer>{content}</PageContainer>
        <SnackbarContainer>
          {snackbars.map((snackbar, index) => (
            <Snackbar key={index}>{snackbar}</Snackbar>
          ))}
        </SnackbarContainer>
        <Footer />
      </Suspense>
    </Container>
  );
};

const Container = styled.div`
  display: flex;
  flex-direction: column;
  min-height: inherit;
  height: inherit;
`;

const PageContainer = styled.div`
  flex: 1;
`;

export default App;
