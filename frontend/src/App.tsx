import { useRoutes } from 'react-router-dom';

import styled from 'styled-components';

import Footer from 'components/Footer';
import Header from 'components/header/Header';
import { routes } from 'routes/Routes';
import GlobalStyles from 'styles/GlobalStyle';

const App = () => {
  const content = useRoutes(routes);

  return (
    <>
      <GlobalStyles />
      <Header />
      <PageContainer>{content}</PageContainer>
      <Footer />
    </>
  );
};

const PageContainer = styled.div``;

export default App;
