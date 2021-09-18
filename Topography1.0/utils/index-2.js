// Extends EditorUi to update I/O action states based on availability of backend
(function () {
    var editorUiInit = EditorUi.prototype.init;
    var editui = null
    EditorUi.prototype.init = function () {
        sessionStorage.removeItem('viewTitle')
        editorUiInit.apply(this, arguments);
        this.actions.get('export').setEnabled(false);
        editui = this
        var graph = this.editor.graph
        // Updates action states which require a backend
        if (!Editor.useLocalStorage) {
            // console.log(OPEN_URL)
            this.actions.get('open').setEnabled(true || Graph.fileSupport);
            this.actions.get('import').setEnabled(true);
            this.actions.get('save').setEnabled(true || Graph.fileSupport);
            this.actions.get('saveAs').setEnabled(true || Graph.fileSupport);
            this.actions.get('export').setEnabled(true);
        }
    }

    mxResources.loadDefaultBundle = false;
    var bundle = mxResources.getDefaultBundle(RESOURCE_BASE, mxLanguage) ||
        mxResources.getSpecialBundle(RESOURCE_BASE, mxLanguage);
    mxUtils.getAll([bundle, STYLE_PATH + '/default.xml'], function (xhr) {
            // Adds bundle text to resources
            mxResources.parse(xhr[0].getText());
            // Configures the default graph theme
            var themes = new Object();
            themes[Graph.prototype.defaultThemeName] = xhr[1].getDocumentElement();
            // 实例化 EditorUi
            // 如果缓存不为空，则读取缓存数据渲染
            var newEditorUi = new EditorUi(new Editor(urlParams['chrome'] == '0', themes));
            // console.log(newEditorUi)
            // console.log(urlParams['chrome'] == '0')
            // console.log(new Editor(urlParams['chrome'] == '0', themes))
            window.newEditorUi = newEditorUi
            // 接受主系统传递过来的值
            window.addEventListener("message", function (event) {
                var data = event.data
                // console.log(data)
                // console.log('父-子')
                switch (data.cmd) {
                    case 'sendData':
                        initEditor(newEditorUi, data)
                        break
                    case 'nodeBindObject':
                        nodeBindObject(newEditorUi, JSON.parse(data.params.data))
                        break
                    case 'nodeBindView':
                        nodeBindView(newEditorUi, data.params.data)
                        break
                    case 'nodeBindUploadFile':
                        nodeBindUploadFile(newEditorUi, data.params.data)
                        break
                    case 'bindView':
                        var $viewTitle = document.querySelector('.view-title');
                        var $viewBindInput = document.querySelector('.view-bind-input');
                        $viewTitle.value = data.params.data
                        $viewBindInput.value = data.params.data

                        sessionStorage.setItem('viewTitle', data.params.data)
                        sessionStorage.getItem('viewTitle')
                        break
                    case 'viewBindViewType':
                        var $viewType = document.querySelector('.view-type');
                        $viewType.value = data.params.data.chooseLabel

                        sessionStorage.setItem('viewType', data.params.data.chooseLabel)
                        break
                }
            }, false);

        },
        function () {
            document.body.innerHTML =
                '<center style="margin-top:10%;">Error loading resource files. Please check browser console.</center>';
        });

    // 初始化视图
    function initEditor(newEditorUi, data) {
        var $viewTitle = document.querySelector('.view-title');
        var $viewBindInput = document.querySelector('.view-bind-input');
        if (data.params.name) {
            sessionStorage.setItem('viewTitle', data.params.name)
            $viewTitle.value = data.params.name
            $viewBindInput.value = data.params.name
        } else {
            sessionStorage.setItem('viewTitle', '')
            $viewTitle.value = ''
            $viewBindInput.value = ''
        }
        // 读取pictureType
        var $pictureType = document.querySelector('.pictureType');
        if (data.params.pictureType) {
            sessionStorage.setItem('pictureType', data.params.pictureType)
            $pictureType.value = data.params.pictureType
        } else {
            sessionStorage.setItem('pictureType', '')
            $pictureType.value = ''
        }

        // if (data.params.pictureType) {
        //     sessionStorage.setItem('pictureType', data.params.pictureType)
        // } else {
        //     sessionStorage.setItem('pictureType', 0)
        // }
        if (data.params.data) {
            var xmlData = newEditorUi.editor.graph.zapGremlins(mxUtils.trim(JSON.parse(data.params.data)));
            newEditorUi.editor.graph.model.beginUpdate();
            try {
                newEditorUi.editor.setGraphXml(mxUtils.parseXml(xmlData).documentElement);
            } catch (e) {
                error = e;
            } finally {
                newEditorUi.editor.graph.model.endUpdate();
            }
        } else {
            var xml = '<mxGraphModel dx="1314" dy="723" grid="1" gridSize="6" guides="1" tooltips="1" connect="0" arrows="1" fold="1" page="1" pageScale="1" pageWidth="1880" pageHeight="940" scale="1" background="#ffffff"><root><mxCell id="0" style="pagetype=0"/><mxCell id="1" parent="0"/></root></mxGraphModel>'
            var xmlData = newEditorUi.editor.graph.zapGremlins(mxUtils.trim(xml));
            newEditorUi.editor.graph.model.beginUpdate();
            try {
                newEditorUi.editor.setGraphXml(mxUtils.parseXml(xmlData).documentElement);
            } catch (e) {
                error = e;
            } finally {
                newEditorUi.editor.graph.model.endUpdate();
            }
        }
        // 获取渲染后的页面数据
        var content = data.params.data
        var xmlDoc = mxUtils.parseXml(content);
        var codec = new mxCodec(xmlDoc);
        var graph = newEditorUi.editor.graph
        var code = codec.decode(xmlDoc.documentElement);
        // 读取背景图
        if (code.backgroundImage) {
            var img = new mxImage(code.backgroundImage, code.cells[0].imageWidth, code.cells[0].imageHeight);
            graph.setBackgroundImage(img);
            graph.view.validateBackgroundImage();
        } else {
            graph.setBackgroundImage();
            graph.view.validateBackgroundImage();
        }
        // 读取缩放比列
        if (code.scale) {
            graph.zoomTo(code.scale)
        }
        // 设置初始位置
        newEditorUi.editor.graph.zoomTo(1)
        newEditorUi.resetScrollbars()
        // 读取网格颜色
        if (code.gridColor) {
            graph.view.gridColor = code.gridColor
            graph.view.validateBackground();

        } else {
            graph.view.gridColor = 'rgba(224, 224, 224, 1)'
            graph.view.validateBackground();
        }
        // 渲染完毕刷新format表单
        newEditorUi.format.refresh()
    }

    // 节点绑定对象
    function nodeBindObject(newEditorUi, data) {
        var graph = newEditorUi.editor.graph;
        let state = graph.view.getState(graph.getSelectionCells()[0]);
        graph.getModel().beginUpdate();
        try {
            Object.keys(data).forEach(function (item) {
                graph.setCellStyles(item, data[item], graph.getSelectionCells());
            })
        } finally {
            graph.getModel().endUpdate();
        }
        var $nodeBindObject = document.getElementById('attrConInput-bandNode')

        if (data.name) {
            $nodeBindObject.value = data.name
        } else {
            $nodeBindObject.value = ''
        }
    }

    // 节点绑定视图
    function nodeBindView(newEditorUi, data) {
        var graph = newEditorUi.editor.graph;
        Object.keys(data).forEach(function (item) {
            graph.setCellStyles(item, data[item], graph.getSelectionCells());
        })
        if (data.viewName) {
            let pictureTypeOptionData = ['默认', '温度图', '市电图']
            var $nodeBindView = document.querySelector('.node-bind-view');
            $nodeBindView.value = data.viewName + ' - ' + pictureTypeOptionData[data.pictureType]
        }
    }
    // 节点绑定上传的文件
    function nodeBindUploadFile(newEditorUi, data) {
        console.log(data)
        var graph = newEditorUi.editor.graph;
        if (data.url) {
            graph.setCellStyles('uploadFileUrl', data.url, graph.getSelectionCells());
        }
        if (data.name) {
            graph.setCellStyles('uploadFileName', data.name, graph.getSelectionCells());
            var $nodeBindFile = document.querySelector('.nodeBindUploadFile');
            $nodeBindFile.value = data.name
        }
    }
})();
