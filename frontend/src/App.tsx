import React from 'react';
import { useRoutes } from 'react-router-dom';
import Header from './components/Header';

import { routes } from './routes/Routes';

import GlobalStyles from './styles/GlobalStyle';

const App = () => {
  const content = useRoutes(routes);

  return (
    <>
      <GlobalStyles />
      <Header />
      {content}
    </>
  );
};

export default App;
