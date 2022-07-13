import { createGlobalStyle } from 'styled-components';

// 나중에 rem으로 단위 변경 시 사용 (#root)
// font-size: 62.5%;
const GlobalStyles = createGlobalStyle` 
  #root {
    width: 100%;
    height: 100%;
    margin: 0;
    font-family: 'Noto Sans KR';
  }

  body {
    margin: 0;
    font-family: -apple-system, BlinkMacSystemFont, 'Noto Sans KR', 'Segoe UI', 'Roboto', 'Oxygen',
      'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue', sans-serif;
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
