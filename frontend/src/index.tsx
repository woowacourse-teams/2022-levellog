import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import { ThemeProvider } from 'styled-components';

import { theme } from './styles/theme';

import App from './App';
import { UserProvider } from './contexts/userContext';

const main = () => {
  if (!process.env.NODE_ENV) {
    const { worker } = require('./mocks/browser');
    // worker.start();
  }

  const root = ReactDOM.createRoot(document.getElementById('root'));
  root.render(
    <BrowserRouter>
      <UserProvider>
        <ThemeProvider theme={theme}>
          <App />
        </ThemeProvider>
      </UserProvider>
    </BrowserRouter>,
  );
};
main();
