"use strict";
const path = require("path");
const utils = require("./utils");
const config = require("../config");
const vueLoaderConfig = require("./vue-loader.conf");
const VueLoaderPlugin = require("vue-loader/lib/plugin");
const HappyPack = require("happypack");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const TerserPlugin = require("terser-webpack-plugin");
const env = process.env.NODE_ENV;

var webpack = require("webpack");

function resolve(dir) {
    return path.join(__dirname, "..", dir);
}

// 分割缓存组
function getSplitGroups() {
    let chunkList = [
        "src_mirror_system",
        "src_cmdb",
        "src_mirror_alert",
        "src_mirror_business",
        "src_resource",
        "src_topo",
        "src_theme",
        "src_org",
        "src_services",
        "src_system_manage",
        "src_auto_operation",
        "src_settings",
        "node_echarts",
        "node_fullcalendar",
        "node_riophae",
        "node_jquery",
        "node_zrender",
        "node_asp-smart-layout",
        "node_moment",
        "node_vue-grid-layout",
        "node_quill",
        "node_codemirror|highcharts",
        "node_mxgraph",
        "node_lodash",
        "node_vant"
    ];
    let groups = {};
    chunkList.forEach((item, index) => {
        let moduleName = item.replace(/src_|node_/, "");
        let obj = {
            test: new RegExp(moduleName),
            name: item.replace(/\|/, "_"),
            reuseExistingChunk: true,
            enforce: true,
            priority: index
        };
        groups[moduleName.replace(/\|/, "_").replace(/\-/g, "")] = obj;
    });
    return groups;
}

module.exports = {
    mode: env || "development",
    entry: {
        main: "./src/main.js"
    },
    output: {
        path: config.build.assetsRoot,
        filename: "[name].js",
        publicPath:
            env === "production"
                ? config.build.assetsPublicPath
                : config.dev.assetsPublicPath
    },
    externals: {
        // 现网环境不支持引入外网cdn链接，暂无自有cdn服务
        // vue: "Vue",
        // "vue-router": "VueRouter",
        // "element-ui": "ELEMENT",
        // echarts: "echarts",
        // moment: "moment"
    },
    resolve: {
        extensions: [".js", ".vue", ".json"],
        alias: {
            vue$: "vue/dist/vue.esm.js",
            src: resolve("src"),
            assets: resolve("src/assets"),
            static: resolve("static")
        }
    },
    module: {
        rules: [
            {
                test: /\.(js|vue)$/,
                loader: "eslint-loader",
                enforce: "pre",
                include: [
                    resolve("src"),
                    resolve("test"),
                    resolve("/node_modules/element-ui/src"),
                    resolve("/node_modules/element-ui/packages")
                ],
                options: {
                    formatter: require("eslint-friendly-formatter")
                }
            },
            {
                test: /\.vue$/,
                loader: "vue-loader",
                options: vueLoaderConfig
            },
            {
                test: /\.sass\.styl(us)$/,
                include: [resolve("src"), resolve("test")],
                use: ["happypack/loader?id=style"]
            },
            {
                test: /\.js$/,
                // use: ['babel-loader?cacheDirectory'] 之前是使用这种方式直接使用 loader
                // 用下面的方式替换成 happypack/loader，并使用 id 指定创建的 HappyPack 插件
                use: ["happypack/loader?id=js"],
                include: [
                    resolve("src"),
                    resolve("test"),
                    resolve("/node_modules/element-ui/src"),
                    resolve("/node_modules/element-ui/packages")
                ],
                // 排除 node_modules 目录下的文件
                exclude: /node_modules/
            },
            {
                test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
                use: [
                    {
                        loader: "url-loader",
                        options: {
                            limit: 10000,
                            name: utils.assetsPath("img/[name].[hash:7].[ext]")
                        }
                    },
                    {
                        loader: "image-webpack-loader", // 压缩图片
                        options: {
                            bypassOnDebug: true
                        }
                    }
                ]
            },
            {
                test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
                loader: "url-loader",
                options: {
                    limit: 10000,
                    name: utils.assetsPath("media/[name].[hash:7].[ext]")
                }
            },
            {
                test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
                loader: "url-loader",
                options: {
                    limit: 10000,
                    name: utils.assetsPath("fonts/[name].[hash:7].[ext]")
                }
            }
        ]
    },
    plugins: [
        new VueLoaderPlugin(),
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            echarts: "echarts",
            // moment: "moment",
            _: "lodash",
        }),
        new HappyPack({
            /*
             * 必须配置
             */
            // id 标识符，要和 rules 中指定的 id 对应起来
            id: "js",
            // 需要使用的 loader，用法和 rules 中 Loader 配置一样
            // 可以直接是字符串，也可以是对象形式
            loaders: ["babel-loader?cacheDirectory"]
            // loaders: [{
            //     path: "babel-loader",
            //     cache: true,
            //     query: {
            //         plugins: ["syntax-dynamic-import"]
            //     }
            // }]
        }),
        new HappyPack({
            id: "style",
            loaders: [
                env === "production"
                    ? MiniCssExtractPlugin.loader
                    : "style-loader",
                "css-loader",
                "sass-loader",
                'vue-style-loader',
                'stylus-loader'
            ]
        })
    ],

    optimization: {
        minimizer: [
            new TerserPlugin({
                cache: true,
                parallel: true
            })
        ],
        /**
         * chunks
         * async 表示只从异步加载得模块（动态加载import()）里面进行拆分
         * initial 表示只从入口模块进行拆分
         * all 表示以上两者都包括
         */
        splitChunks: {
            // 自定义分割
            chunks: "async", //默认只作用于 async 异步模块，为`all`时对所有模块生效,`initial`对同步模块有效
            minSize: 30000, //模块大于30k，进行分割
            minChunks: 1, //最少被引用次数
            maxAsyncRequests: 6,
            maxInitialRequests: 4,
            automaticNameDelimiter: "~", //自动命名连接符
            cacheGroups: Object.assign(getSplitGroups(), {
                styles: {
                    name: "styles",
                    test: /\.css|\.scss|\.styl(us)?$/,
                    chunks: "all",
                    enforce: true
                },

                default: {
                    test: /[\\/]src[\\/]js[\\/]/,
                    minChunks: 2, //一般为非第三方公共模块
                    priority: -20,
                    reuseExistingChunk: true
                }
            })
        },
        runtimeChunk: {
            name: "manifest"
        }
    },
    performance: {
        hints: false // 关闭
        // hints: 'warning',
        // maxEntrypointSize: 1000000, //入口文件的最大体积，单位字节
        // maxAssetSize: 1000000, //生成文件的最大体积，单位字节
        // assetFilter: function(assetFilename) { //只给出js文件的性能提示
        //   return assetFilename.endsWith('.js');
        // }
    }
};
