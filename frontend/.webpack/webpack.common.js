const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
  entry: '../src/index.tsx',
  module: {
    rules: [
      {
        test: /\.(png|jpe?g|svg|webp|woff)$/,
        type: 'asset/resource',
        generator: {
          filename: '[hash][ext][query]',
        },
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
    new HtmlWebpackPlugin({
      template: path.join('public/index.html'),
      favicon: './src/assets/images/favicon.ico',
    }),
    new CleanWebpackPlugin(),
  ],
};
