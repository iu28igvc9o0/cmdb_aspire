{
    "workbench.editor.enablePreview": false, //打开文件不覆盖
    "search.followSymlinks": false, //关闭rg.exe进程
    // "editor.minimap.enabled": false, //关闭快速预览
    "editor.lineNumbers": "on", //开启行数提示
    "editor.detectIndentation": false,
    "editor.quickSuggestions": {
        //开启自动显示建议
        "other": true,
        "comments": true,
        "strings": true
    }, //制表符符号eslint
    "javascript.format.insertSpaceBeforeFunctionParenthesis": false, // 方法括号之间插入空格
    "editor.formatOnSave": true, // 编辑器保存时自动格式化
    //保存自动修复
    "editor.codeActionsOnSave": {
        "source.fixAll.eslint": true
    },
    "editor.tabSize": 4,
    "vetur.format.options.tabSize": 4,
    "vetur.format.defaultFormatter.html": "js-beautify-html", //格式化.vue中html
    "vetur.format.defaultFormatter.js": "vscode-typescript", //让vue中的js按编辑器自带的ts格式进行格式化
    "prettier.semi": true, //去掉代码结尾的分号
    "prettier.singleQuote": true, //单引号
    "vetur.format.defaultFormatterOptions": {
        "js-beautify-html": {
            "wrap_attributes": "force-aligned" //属性强制折行对齐
        }
    },
    "eslint.validate": [
        "javascript",
        "javascriptreact",
        "html",
        "vue"
    ],
    //让.js文件中的js按编辑器自带的ts格式进行格式化
    "[javascript]": {
        "editor.defaultFormatter": "vscode.typescript-language-features"
    },
    "[markdown]": {
        "editor.defaultFormatter": "vscode.typescript-language-features"
    },
    // 默认使用 vetur 格式化 .vue
    "[vue]": {
        "editor.defaultFormatter": "octref.vetur"
    },
    "[html]": {
        "editor.defaultFormatter": "vscode.html-language-features"
    },
    "[jsonc]": {
        "editor.defaultFormatter": "vscode.json-language-features"
    },
    "vetur.format.scriptInitialIndent": true,
    "vetur.format.styleInitialIndent": true,
    "editor.suggest.snippetsPreventQuickSuggestions": false,
}