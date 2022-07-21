const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'source-map',
  devServer: {
    static: {
      // directory: path.join(__dirname, 'public'),
      directory: '../dist',
    },
    hot: true,
    compress: true,
    port: 3000,
    historyApiFallback: true,
  },
});
