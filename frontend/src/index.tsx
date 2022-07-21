import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import { ThemeProvider } from 'styled-components';

import { theme } from './styles/theme';

import App from './App';
import { UserProvider } from './contexts/userContext';
import { TeamProvider } from 'contexts/teamContext';

const main = () => {
  if (process.env.NODE_ENV === 'development') {
    const { worker } = require('./mocks/browser');
    worker.start();
  }

  const root = ReactDOM.createRoot(document.getElementById('root'));
  root.render(
    <BrowserRouter>
      <UserProvider>
        <TeamProvider>
          <ThemeProvider theme={theme}>
            <App />
          </ThemeProvider>
        </TeamProvider>
      </UserProvider>
    </BrowserRouter>,
  );
};
main();
