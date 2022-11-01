module.exports = {
  presets: [
    '@babel/env',
    '@babel/preset-typescript',
    ['@babel/preset-react', { runtime: 'automatic' }],
  ],
  plugins: ['babel-plugin-styled-components'],
};
