import { BrowserRouter } from 'react-router-dom';

import { ThemeProvider } from 'styled-components';

import { UserProvider } from '/contexts/userContext';
import GlobalStyles from 'styles/GlobalStyle';
import { theme } from 'styles/theme';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

export const decorators = [
  (Story) => (
    <BrowserRouter>
      <UserProvider>
        <ThemeProvider theme={theme}>
          <GlobalStyles />
          <Story />
        </ThemeProvider>
      </UserProvider>
    </BrowserRouter>
  ),
];
