const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'production',
  devtool: 'source-map',
  performance: {
    maxAssetSize: 400000,
    maxEntrypointSize: 400000,
  },
});
