import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import App from 'App';
import { ThemeProvider } from 'styled-components';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { SnackbarProvider } from 'contexts/snackbarContext';
import { TeamProvider } from 'contexts/teamContext';
import { UserProvider } from 'contexts/userContext';
import { theme } from 'styles/theme';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
    },
  },
});

const main = () => {
  const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);
  root.render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <TeamProvider>
          <UserProvider>
            <SnackbarProvider>
              <ThemeProvider theme={theme}>
                <App />
              </ThemeProvider>
            </SnackbarProvider>
          </UserProvider>
        </TeamProvider>
      </BrowserRouter>
    </QueryClientProvider>,
  );
};

main();
