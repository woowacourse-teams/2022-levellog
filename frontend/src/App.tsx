import React from 'react';
import { useRoutes } from 'react-router-dom';
import styled from 'styled-components';

import { routes } from './routes/Routes';

import GlobalStyles from './styles/GlobalStyle';

const App = () => {
  const content = useRoutes(routes);

  return (
    <>
      <GlobalStyles />
      <PageContainer>{content}</PageContainer>
    </>
  );
};

const PageContainer = styled.main`
  overflow: auto;
  overflow-x: hidden;
  width: 80%;
  margin: 5% 10% 0 10%;
`;

export default App;
