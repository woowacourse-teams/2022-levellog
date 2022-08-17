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

const PageContainer = styled.main`
  overflow: auto;
  overflow-x: hidden;
  box-sizing: border-box;
  width: 100%;
  min-height: 65vh;
  /* padding: 0 10rem;
  @media (max-width: 1024px) {
    padding: 0 5rem;
  }
  @media (max-width: 560px) {
    padding: 0 1.25rem;
  }
`;

export default App;
