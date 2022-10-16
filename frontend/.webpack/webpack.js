const webpack = require('webpack');
const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');
const { ESBuildMinifyPlugin } = require('esbuild-loader');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'eval-source-map',
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        loader: 'esbuild-loader',
        options: {
          loader: 'tsx',
          target: 'es2015',
        },
      },
    ],
  },
  devServer: {
    static: {
      directory: '../dist',
    },
    hot: true,
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
  ],
});
