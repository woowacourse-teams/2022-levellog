const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
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
