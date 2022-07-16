import { UserProvider } from '../src/contexts';
import GlobalStyles from '../src/styles/GlobalStyle';
import { theme } from '../src/styles/theme';

import { BrowserRouter } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';

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
