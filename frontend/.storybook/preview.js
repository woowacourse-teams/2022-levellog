import { BrowserRouter } from 'react-router-dom';

import { UserProvider } from 'contexts';
import { ThemeProvider } from 'styled-components';

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
