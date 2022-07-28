const webpack = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'source-map',
  plugins: [
    new webpack.EnvironmentPlugin({
      SERVICE_URI: 'https://test.levellog.app',
      API_URI: 'https://dev.levellog.app/api',
      CLIENT_ID: '00230a7d7fa77d726d7e',
    }),
  ],
});
