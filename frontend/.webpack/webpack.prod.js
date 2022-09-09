const webpack = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  devtool: 'source-map',
  plugins: [
    new webpack.EnvironmentPlugin({
      SERVICE_URI: 'https://levellog.app',
      API_URI: 'https://prod.levellog.app/api',
      CLIENT_ID: 'fc4c9ab6e6d189931371',
    }),
  ],
});
