var path = require('path')
var utils = require('./utils')
var config = require('../config')
var vueLoaderConfig = require('./vue-loader.conf')
var webpack = require('webpack')
function resolve (dir) {
  return path.join(__dirname, '..', dir)
}

module.exports = {
  entry: {
    app: './src/main.js'
  },
  output: {
    path: config.build.assetsRoot,
    filename: '[name].js',
    publicPath: process.env.NODE_ENV === 'production'
      ? config.build.assetsPublicPath
      : config.dev.assetsPublicPath
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    modules: [
      resolve('src'),
      resolve('node_modules')
    ],
    alias: {
      'vue$': 'vue/dist/vue.common.js',
      'src': resolve('src'),
      'assets': resolve('src/assets'),
      'components': resolve('src/components')
    }
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader'
      },
      {
        test: /\.js$/,
        loader: 'babel-loader',
        include: [resolve('src'), resolve('test')],
        exclude: /node_modules/
      },
      //.less�ļ�����
      { 
        test: /\.less$/,
        loader: 'style-loader!css-loader!less-loader',
        include: [resolve('src'), resolve('test')],
        exclude: /node_modules/
      },
      //.css�ļ�����
      {
        test:/\.css$/,
        loader:'style-loader!css-loader!less-loader',
        include: [resolve('src'), resolve('test')],
        exclude: /node_modules/
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        query: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        query: {
          limit: 10000,
          name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
        }
      },
      {
          test: require.resolve('numbro'),
          loader: 'expose-loader?numbro'
        },
        {
          test: require.resolve('moment'),
          loader: 'expose-loader?moment'
        },
        {
          test: require.resolve('pikaday'),
          loader: 'expose-loader?Pikaday'
        },
        {
          test: require.resolve('zeroclipboard'),
          loader: 'expose-loader?ZeroClipboard'
        }
    ],
    loaders: [
              {
                  test: /\.js$/,
                  exclude: /(node_modules|bower_components)/,
                  loader: 'babel',
                  query: {
                      presets: ['es2015']
                  }
              }
          ]
  },
  plugins: [
    new webpack.ProvidePlugin({
        $: "jquery",
        jQuery: "jquery"
    }),
    new webpack.ProvidePlugin({
        echarts: "echarts"
    })
  ],
}
