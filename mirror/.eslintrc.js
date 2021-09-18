// https://eslint.org/docs/user-guide/configuring
module.exports = {
    globals: {
        _: true,
        $: true,
        echarts: true
    },
    root: true,
    parserOptions: {
        parser: "babel-eslint",
        sourceType: "module"
    },
    env: {
        browser: true,
        node: true,
        es6: true
    },
    // https://github.com/standard/standard/blob/master/docs/RULES-en.md
    extends: ["plugin:vue/base", "eslint:recommended"],
    plugins: ["vue", "html"],
    // add your custom rules here
    rules: {
        // .vue文件 已开启保存自动格式化，此处不再校验，避免某些情况冲突
        "vue/html-indent": [
            "off",
            4,
            {
                attribute: 1,
                baseIndent: 1,
                closeBracket: 0,
                alignAttributesVertically: true,
                ignores: []
            }
        ],
        // .vue文件 已开启保存自动格式化，此处不再校验，避免某些情况冲突
        "vue/script-indent": [
            "off",
            4,
            {
                baseIndent: 1,
                switchCase: 1,
                ignores: []
            }
        ],
        "vue/max-attributes-per-line": [
            "error",
            {
                singleline: 10,
                multiline: {
                    max: 3,
                    allowFirstLine: true
                }
            }
        ],
        // 标签末尾是否需要新一行
        "vue/html-closing-bracket-newline": [
            "error",
            {
                singleline: "never",
                multiline: "always"
            }
        ],
        // 没有内容时，组件必须自闭合
        "vue/html-self-closing": "off",
        "vue/name-property-casing": ["error", "PascalCase"],
        // allow async-await
        "generator-star-spacing": "off",
        // allow debugger during development
        "no-debugger": process.env.NODE_ENV === "production" ? "error" : "warn",
        // js缩进为4个空格 
        // .js文件 已开启保存自动格式化，此处不再校验，避免某些情况冲突
        indent: "off",
        // 对象展开时总是要添加逗号，一行时行末不需要逗号
        // 'comma-dangle': ['error', 'always-multiline'],
        // 禁止添加分号
        semi: ["error", "never"],
        quotes: ["error", 'single'], //引号类型
        // 定义函数时，函数括号之前不添加空格
        "space-before-function-paren": 0,
        // 关闭空行检测
        "no-multiple-empty-lines": "off",
        // 关闭模板字符串检测
        "no-template-curly-in-string": "off",
        "no-console": "off",

        // 以下为警告类型，后续逐步消除
        // 警告类型已基本消除，转为错误类型进行强校验   20200519
        // 重复定义
        "no-dupe-keys": "error",
        // 变量泄漏导致重复定义
        "no-redeclare": "error",
        // 检测未使用的变量，参数不检测
        "no-unused-vars": ["error", { "args": "none" }],
        // 关闭禁用无效标签
        "no-tabs": "error",
        // 空逻辑
        "no-empty": "error",
        // Do not access Object.prototype method 'hasOwnProperty' from target object
        "no-prototype-builtins": "error",
        // 转义通知
        "no-useless-escape": "error",
        // 不允许在试验条件不明确赋值运算符if，for，while，和do...while语句，如将比较运算符（如==）作为赋值运算符（例如=）输错
        "no-cond-assign": "error",
        // 该规则禁止词法声明（let，const，function和class在）case/default条款
        "no-case-declarations": "error",
        // 不允许可达代码后return，throw，continue，和break语句
        "no-unreachable": "error",
        // This rule is aimed at catching invalid whitespace that is not a normal tab and space. Some of these characters may cause issues in modern browsers and others will be a debugging issue to spot.
        "no-irregular-whitespace": "error",
        // 消除自我分配
        "no-self-assign": "error",
        // 不允许使用混合空格和制表符进行缩进
        "no-mixed-spaces-and-tabs": "error",
        // 消除一个案件无意中掉到另一个案件，如switch缺少break
        "no-fallthrough": "error",
        // 不建议在{}代码块内部声明变量或函数
        "no-inner-declarations": "error",

        // props属性必须定义类型
        "vue/require-prop-types": 0,
        // 禁止使用v-html，避免风险
        "vue/no-v-html": 0,
        // 禁止在计算属性中对属性修改
        "vue/no-side-effects-in-computed-properties": 0,
        // 模版中开始标签的反尖括号必须换行
        "vue/html-closing-bracket-newline": 0,

        // 以下为警告类型，后续逐步消除
        // 警告类型已基本消除，转为错误类型进行强校验   20200519
        "no-undef": "error",
        // 禁止在同一个元素上使用 v-if 和 v-for 指令，v-if应使用计算属性
        "vue/no-use-v-if-with-v-for": "error",
        // is defined but never used
        "vue/no-unused-vars": 2,
        //vue/require-v-for-key
        "vue/require-v-for-key": "error",
        "no-sparse-arrays": "error",
        "spaced-comment": ["error", "always", {
            "line": {
                "markers": ["/"],
                "exceptions": ["-", "+"]
            },
            "block": {
                "markers": ["!"],
                "exceptions": ["*"],
                "balanced": true
            }
        }]
    },
    // vue文件关闭eslint缩进校验，使用vue/script-indent缩进校验，避免冲突
    overrides: [
        {
            files: ["*.vue"],
            rules: {
                indent: "off"
            }
        }
    ]
};
