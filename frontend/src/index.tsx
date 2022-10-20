import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';

import App from 'App';
import { ThemeProvider } from 'styled-components';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

import { SnackbarProvider } from 'contexts/snackbarContext';
import { TeamProvider } from 'contexts/teamContext';
import { UserProvider } from 'contexts/userContext';
import { theme } from 'styles/theme';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      suspense: true,
      retry: 1,
      retryDelay: 0,
      useErrorBoundary: false,
      staleTime: 0,
      cacheTime: 0,
    },
    mutations: {
      retry: 1,
      retryDelay: 0,
      cacheTime: 0,
    },
  },
});

const main = () => {
  root.render(
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <TeamProvider>
          <UserProvider>
            <SnackbarProvider>
              <ThemeProvider theme={theme}>
                <App />
              </ThemeProvider>
            </SnackbarProvider>
          </UserProvider>
        </TeamProvider>
      </QueryClientProvider>
    </BrowserRouter>,
  );
};

main();
