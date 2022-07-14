import { BrowserRouter } from 'react-router-dom';

import { UserProvider } from '../src/context';

import GlobalStyles from '../src/styles/GlobalStyle';

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
        <GlobalStyles />
        <Story />
      </UserProvider>
    </BrowserRouter>
  ),
];
