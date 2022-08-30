import { createGlobalStyle } from 'styled-components';

import NotoSans from 'assets/fonts/noto-sans-kr-v27-latin-regular.woff';

const GlobalStyles = createGlobalStyle` 
  #root {
    min-height: 100vh;
    height: max-content;
    width: 100%;
    margin: 0;
  }

  @font-face {
    font-family: "Font_Regular";
    src: local("Font_test"), url(${NotoSans}) format('otf'); 
  }

  html, body, div, span, applet, object, iframe,
  h1, h2, h3, h4, h5, h6, p, blockquote, pre,
  a, abbr, acronym, address, big, cite, code,
  del, dfn, em, img, ins, kbd, q, s, samp,
  small, strike, strong, sub, sup, tt, var,
  b, u, i, center,
  dl, dt, dd, ol, ul, li,
  fieldset, form, label, legend,
  table, caption, tbody, tfoot, thead, tr, th, td,
  article, aside, canvas, details, embed, 
  figure, figcaption, footer, header, hgroup, 
  menu, nav, output, ruby, section, summary,
  time, mark, audio, video {
    margin: 0;
    padding: 0;
    border: 0;
    vertical-align: baseline;
    box-sizing: border-box;
  }

  html {
    height: 100%;
    width: 100%;
  }

  body {
    height: 100%;
    width: 100%;
    background-color: #FFFFFF;
    font-family: 'Font_Regular';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    outline: 0;
    font-style: normal;
    ::-webkit-scrollbar {
      display: none
    };
  }

  p {
    word-break: break-all;
  }

  /* HTML5 display-role reset for older browsers */
  article, aside, details, figcaption, figure, 
  footer, header, hgroup, menu, nav, section {
    display: block;
  }
  body {
    line-height: 1;
  }
  ol, ul {
    list-style: none;
  }
  blockquote, q {
    quotes: none;
  }
  blockquote:before, blockquote:after,
  q:before, q:after {
    content: '';
    content: none;
  }
  table {
    border-collapse: collapse;
    border-spacing: 0;
  }
  
  input:focus {outline: none;}

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

  :disabled {
    cursor: inherit;
  }

  ::-webkit-scrollbar {
    display: block;
    width: 0.75rem;
  }
  ::-webkit-scrollbar-thumb {
    background-color: #3E3E3E;
    background-clip: padding-box;
    border: 0.125rem solid transparent;
    border-radius: 0.625rem;
  }
  ::-webkit-scrollbar-track {
    background-color: #FFFFFF;
  }
`;

export default GlobalStyles;
