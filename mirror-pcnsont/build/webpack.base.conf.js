'use strict'
const path = require('path')
const utils = require('./utils')
const config = require('../config')
const vueLoaderConfig = require('./vue-loader.conf')
const HappyPack = require('happypack')

var webpack = require('webpack')

function resolve(dir) {
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
      ? config.build.assetsPublicPath : config.dev.assetsPublicPath
  },
  externals: {
    'vue': 'Vue',
    'vue-router': 'VueRouter',
    'element-ui': 'ELEMENT',
    'echarts': 'echarts',
    'moment': 'moment'
  },
  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
      'src': resolve('src'),
      'assets': resolve('src/assets'),
      'static': resolve('static')
    }
  },
  module: {
    rules: [
      // {
      //   test: /\.(js|vue)$/,
      //   loader: 'eslint-loader',
      //   enforce: 'pre',
      //   include: [resolve('src'), resolve('test'),resolve('/node_modules/element-ui/src'),resolve('/node_modules/element-ui/packages')],
      //   options: {
      //     formatter: require('eslint-friendly-formatter')
      //   }
      // },

      // {
      //   test: /\.sass$/,
      //   include: [resolve('src'), resolve('test')],
      //   use: ['style-loader', 'css-loader', 'sass-loader']
      // },
      // {
      //   test: /\.vue$/,
      //   loader: 'vue-loader',
      //   options: vueLoaderConfig
      // },
      // {
      //   test: /\.js$/,
      //   loader: 'babel-loader',
      //   include: [resolve('src'), resolve('test'), resolve('/node_modules/element-ui/src'), resolve('/node_modules/element-ui/packages')],
      //   // options: {
      //   //   presets: ['es2015']
      //   // }
      // },

      {
        test: /\.sass$/,
        include: [resolve('src'), resolve('test')],
        use: ['happypack/loader?id=style']
      },
      {
        test: /\.vue$/,
        use: ['happypack/loader?id=vue']
      },
      {
        test: /\.js$/,
        // use: ['babel-loader?cacheDirectory'] 之前是使用这种方式直接使用 loader
        // 现在用下面的方式替换成 happypack/loader，并使用 id 指定创建的 HappyPack 插件
        use: ['happypack/loader?id=babel'],
        include: [resolve('src'), resolve('test'), resolve('/node_modules/element-ui/src'), resolve('/node_modules/element-ui/packages')],
        // 排除 node_modules 目录下的文件
        exclude: /node_modules/
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('img/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('media/[name].[hash:7].[ext]')
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
        }
      }

    ]
  },
  plugins: [
    new webpack.ProvidePlugin({
      $: 'jquery',
      jQuery: 'jquery'
    }),
    new HappyPack({
      /*
       * 必须配置
       */
      // id 标识符，要和 rules 中指定的 id 对应起来
      id: 'babel',
      // 需要使用的 loader，用法和 rules 中 Loader 配置一样
      // 可以直接是字符串，也可以是对象形式
      loaders: ['babel-loader?cacheDirectory']
    }),
    new HappyPack({
      id: 'vue',
      loaders: [{
        loader: 'vue-loader',
        options: vueLoaderConfig
      }]
    }),
    new HappyPack({
      id: 'style',
      loaders: ['style-loader', 'css-loader', 'sass-loader']
    })
  ]
}
