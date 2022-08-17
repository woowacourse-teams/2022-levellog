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
  @media (min-width: 1620px) {
    padding: 0 calc((100vw - 1600px) / 2);
    padding-bottom: 200px;
  }
  @media (min-width: 1187.5px) and (max-width: 1620px) {
    padding: 0 calc((100vw - 1187.5px) / 2);
    padding-bottom: 200px;
  }
  @media (min-width: 775px) and (max-width: 1207.5px) {
    padding: 0 calc((100vw - 775px) / 2);
    padding-bottom: 200px;
  }
  @media (min-width: 560px) and (max-width: 800px) {
    padding: 0 calc((100vw - 362.5px) / 2);
    padding-bottom: 200px;
  }
  @media (max-width: 560px) {
    padding: 0 1.25rem;
    padding-bottom: 200px;
  }
`;

export default App;
