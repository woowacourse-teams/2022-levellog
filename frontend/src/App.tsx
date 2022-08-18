import { useState } from 'react';
import { useRoutes } from 'react-router-dom';

import styled from 'styled-components';

import Footer from 'components/Footer';
import Header from 'components/header/Header';
import { routes } from 'routes/Routes';
import GlobalStyles from 'styles/GlobalStyle';

const App = () => {
  const content = useRoutes(routes);
  const [prevPathname, setPrevPathname] = useState('');

  if (location.pathname !== prevPathname) {
    window.scrollTo(0, 0);
    setPrevPathname(location.pathname);
  }

  return (
    <>
      <GlobalStyles />
      <Header />
      <PageContainer>{content}</PageContainer>
      <Footer />
    </>
  );
};

const PageContainer = styled.div`
  min-height: calc(100vh - 15rem);
`;

export default App;
