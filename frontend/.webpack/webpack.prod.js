const webpack = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const ForkTsCheckerWebpackPlugin = require('fork-ts-checker-webpack-plugin');

module.exports = merge(common, {
  mode: 'production',
  devtool: 'source-map',
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        use: 'ts-loader',
      },
    ],
  },
  plugins: [
    new webpack.EnvironmentPlugin({
      SERVICE_URI: 'https://levellog.app',
      API_URI: 'https://prod.levellog.app/api',
      CLIENT_ID: 'fc4c9ab6e6d189931371',
    }),
    new ForkTsCheckerWebpackPlugin(),
  ],
});
