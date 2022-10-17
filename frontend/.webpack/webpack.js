const webpack = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'eval-source-map',
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        use: [
          {
            loader: 'babel-loader',
            options: {
              plugins: [require.resolve('react-refresh/babel')],
            },
          },
        ],
      },
    ],
  },
  devServer: {
    static: {
      directory: '../dist',
    },
    compress: true,
    port: 3000,
    historyApiFallback: true,
  },
  plugins: [
    new webpack.EnvironmentPlugin({
      SERVICE_URI: 'http://localhost:3000',
      API_URI: 'https://local.levellog.app/api',
      CLIENT_ID: '7a432a0c919a356b4efa',
    }),
    new ReactRefreshWebpackPlugin(),
  ],
});
