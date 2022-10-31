import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import App from 'App';
import { ThemeProvider } from 'styled-components';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import ErrorBoundary from 'components/@commons/ErrorBoundary';
import { SnackbarProvider } from 'contexts/snackbarContext';
import { TeamProvider } from 'contexts/teamContext';
import { UserProvider } from 'contexts/userContext';
import { theme } from 'styles/theme';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      useErrorBoundary: true,
      retry: 0,
    },
    mutations: {
      useErrorBoundary: false,
      retry: 0,
    },
  },
});

const main = () => {
  root.render(
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <ErrorBoundary>
          <QueryClientProvider client={queryClient}>
            <TeamProvider>
              <UserProvider>
                <SnackbarProvider>
                  <App />
                </SnackbarProvider>
              </UserProvider>
            </TeamProvider>
          </QueryClientProvider>
        </ErrorBoundary>
      </ThemeProvider>
    </BrowserRouter>,
  );
};

main();
