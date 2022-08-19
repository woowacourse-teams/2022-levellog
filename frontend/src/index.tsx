import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import App from 'App';
import { ThemeProvider } from 'styled-components';

import { TeamProvider } from 'contexts/teamContext';
import { UserProvider } from 'contexts/userContext';
import { theme } from 'styles/theme';

const main = () => {
  const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
  root.render(
    <BrowserRouter>
      <TeamProvider>
        <UserProvider>
          <ThemeProvider theme={theme}>
            <App />
          </ThemeProvider>
        </UserProvider>
      </TeamProvider>
    </BrowserRouter>,
  );
};
main();
