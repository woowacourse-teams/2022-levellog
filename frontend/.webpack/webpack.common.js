const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const Dotenv = require('dotenv-webpack');
const CopyWebpackPlugin = require('copy-webpack-plugin');

require('dotenv').config();

module.exports = {
  entry: '../src/index.tsx',
  module: {
    rules: [
      {
        test: /\.(png|jpe?g|svg|webp|woff)$/,
        type: 'asset/resource',
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  resolve: {
    modules: [path.resolve(__dirname, '../src'), 'node_modules'],
    extensions: ['.tsx', '.ts', '.js'],
  },
  output: {
    path: path.resolve(__dirname, '../dist'),
    filename: '[name].[contenthash].js',
    publicPath: '/',
  },
  plugins: [
    new Dotenv(),
    new HtmlWebpackPlugin({ template: 'public/index.html' }),
    new CopyWebpackPlugin({
      patterns: [{ from: './public', to: './public' }],
    }),
    new CleanWebpackPlugin(),
  ],
};
