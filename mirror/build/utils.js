'use strict'
const path = require('path')
const config = require('../config')
// const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const devMode = process.env.NODE_ENV !== 'production'

exports.assetsPath = function (_path) {
  const assetsSubDirectory = !devMode ?
    config.build.assetsSubDirectory :
    config.dev.assetsSubDirectory
  return path.posix.join(assetsSubDirectory, _path)
}

exports.cssLoaders = function (options) {
  options = options || {}

  const cssLoader = {
    loader: 'css-loader',
    options: {
      minimize: !devMode,
      sourceMap: options.sourceMap
    }
  }

  // generate loader string to be used with extract text plugin
  function generateLoaders(loader, loaderOptions) {
    const loaders = []

    // MiniCssExtractPlugin抽取css文件个数过多，增加了过多请求，暂不使用
    // if (options.extract) {
    //     loaders.push({
    //       loader:MiniCssExtractPlugin.loader,
    //       options: {
    //         publicPath: '../',
    //         hmr: devMode, // 仅dev环境启用HMR功能
    //       }
    //     })
    // } else {
    //     loaders.push('vue-style-loader')
    // }

    loaders.push('vue-style-loader')

    loaders.push(cssLoader)

    if (loader) {
      loaders.push({
        loader: loader + '-loader',
        options: Object.assign({}, loaderOptions, {
          sourceMap: options.sourceMap
        })
      })
    }

    return loaders

  }

  // https://vue-loader.vuejs.org/en/configurations/extract-css.html
  return {
    css: generateLoaders(),
    postcss: generateLoaders(),
    less: generateLoaders('less'),
    sass: generateLoaders('sass', {
      indentedSyntax: true
    }),
    // scss: generateLoaders('sass'),
    scss: generateLoaders('sass').concat({
      loader: 'sass-resources-loader',
      options: {
        resources: path.resolve(__dirname, '../src/assets/css/rule.scss')
      }
    }),
    stylus: generateLoaders('stylus'),
    styl: generateLoaders('stylus')
  }
}

// Generate loaders for standalone style files (outside of .vue)
exports.styleLoaders = function (options) {
  const output = []
  const loaders = exports.cssLoaders(options)
  
  for (const extension in loaders) {
    const loader = loaders[extension]
    output.push({
      test: new RegExp('\\.' + extension + '$'),
      use: loader
    })
  }
  return output
}
