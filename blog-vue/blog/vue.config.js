const compressionPlugin = require('compression-webpack-plugin')
module.exports = {
  transpileDependencies: ['vuetify'],
  configureWebpack: {
    plugins: [
      new compressionPlugin({
        test: /\.(js|css)(\?.*)?$/i,
        threshold: 10240,
        deleteOriginalAssets: false
      })
    ]
  },
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    disableHostCheck: true
  },
  productionSourceMap: false,
  css: {
    extract: true,
    sourceMap: false
  }
}
