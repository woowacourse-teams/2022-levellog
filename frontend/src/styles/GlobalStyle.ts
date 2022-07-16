import { createGlobalStyle } from 'styled-components';

import NanumFont from 'assets/fonts/NanumSquare_ac.woff';

const GlobalStyles = createGlobalStyle` 
  #root {
    width: 100%;
    height: 100%;
    margin: 0;
    font-size: 62.5%;
  }

    @font-face {
      font-family: "Font_Regular";
      src: local("Font_test"), url(${NanumFont}) format('woff'); 
    }

  body {
    font-family: 'Font_Regular';
    box-sizing: border-box;
    margin: 0;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    margin: 0;
    padding: 0;
    border: 0;
    outline: 0;
    font-size: 100%;
    font-style: normal;
  }
  
  input:focus {outline: none;}

  code {
    font-family: source-code-pro, Menlo, Monaco, Consolas, 'Courier New', monospace;
  }

  button {
    cursor: pointer;
  }

  a {
    color: #000;
    text-decoration: none;
  }

  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
  }
`;

export default GlobalStyles;
