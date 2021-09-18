/**
 * Copyright (c) 2006-2012, JGraph Ltd
 */
Format = function (editorUi, container) {
    this.editorUi = editorUi;
    this.container = container;
};

/**
 * Returns information about the current selection.
 */
Format.prototype.labelIndex = 0;

/**
 * Returns information about the current selection.
 */
Format.prototype.currentIndex = 0;

/**
 * Returns information about the current selection.
 */
Format.prototype.showCloseButton = true;

/**
 * Background color for inactive tabs.
 */
Format.prototype.inactiveTabBackgroundColor = '#f6f6f6';

/**
 * Background color for inactive tabs.
 */
Format.prototype.roundableShapes = ['label', 'rectangle', 'internalStorage', 'corner',
    'parallelogram', 'swimlane', 'triangle', 'trapezoid',
    'ext', 'step', 'tee', 'process', 'link',
    'rhombus', 'offPageConnector', 'loopLimit', 'hexagon',
    'manualInput', 'curlyBracket', 'singleArrow', 'callout',
    'doubleArrow', 'flexArrow', 'card', 'umlLifeline'
];

/**
 * Adds the label menu items to the given menu and parent.
 */
Format.prototype.init = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    this.update = mxUtils.bind(this, function (sender, evt) {
        this.clearSelectionState();
        this.refresh();
    });

    graph.getSelectionModel().addListener(mxEvent.CHANGE, this.update);
    graph.addListener(mxEvent.EDITING_STARTED, this.update);
    graph.addListener(mxEvent.EDITING_STOPPED, this.update);
    graph.getModel().addListener(mxEvent.CHANGE, this.update);
    graph.addListener(mxEvent.ROOT, mxUtils.bind(this, function () {
        this.refresh();
    }));

    editor.addListener('autosaveChanged', mxUtils.bind(this, function () {
        this.refresh();
    }));

    this.refresh();
};

/**
 * Returns information about the current selection.
 */
Format.prototype.clearSelectionState = function () {
    this.selectionState = null;
};

/**
 * Returns information about the current selection.
 */
Format.prototype.getSelectionState = function () {
    if (this.selectionState == null) {
        this.selectionState = this.createSelectionState();
    }

    return this.selectionState;
};

/**
 * Returns information about the current selection.
 */
Format.prototype.createSelectionState = function () {
    var cells = this.editorUi.editor.graph.getSelectionCells();
    var result = this.initSelectionState();

    for (var i = 0; i < cells.length; i++) {
        this.updateSelectionStateForCell(result, cells[i], cells);
    }

    return result;
};

/**
 * Returns information about the current selection.
 */
Format.prototype.initSelectionState = function () {
    return {
        vertices: [],
        edges: [],
        x: null,
        y: null,
        width: null,
        height: null,
        style: {},
        containsImage: false,
        containsLabel: false,
        fill: true,
        glass: true,
        rounded: true,
        comic: true,
        autoSize: false,
        image: true,
        shadow: true,
        lineJumps: true,
        flow: true
    };
};

/**
 * Returns information about the current selection.
 */
Format.prototype.updateSelectionStateForCell = function (result, cell, cells) {
    var graph = this.editorUi.editor.graph;

    if (graph.getModel().isVertex(cell)) {
        result.vertices.push(cell);
        var geo = graph.getCellGeometry(cell);

        if (geo != null) {
            if (geo.width > 0) {
                if (result.width == null) {
                    result.width = geo.width;
                } else if (result.width != geo.width) {
                    result.width = '';
                }
            } else {
                result.containsLabel = true;
            }

            if (geo.height > 0) {
                if (result.height == null) {
                    result.height = geo.height;
                } else if (result.height != geo.height) {
                    result.height = '';
                }
            } else {
                result.containsLabel = true;
            }

            if (!geo.relative || geo.offset != null) {
                var x = (geo.relative) ? geo.offset.x : geo.x;
                var y = (geo.relative) ? geo.offset.y : geo.y;

                if (result.x == null) {
                    result.x = x;
                } else if (result.x != x) {
                    result.x = '';
                }

                if (result.y == null) {
                    result.y = y;
                } else if (result.y != y) {
                    result.y = '';
                }
            }
        }
    } else if (graph.getModel().isEdge(cell)) {
        result.edges.push(cell);
    }

    var state = graph.view.getState(cell);

    if (state != null) {
        result.autoSize = result.autoSize || this.isAutoSizeState(state);
        result.glass = result.glass && this.isGlassState(state);
        result.rounded = result.rounded && this.isRoundedState(state);
        result.lineJumps = result.lineJumps && this.isLineJumpState(state);
        result.comic = result.comic && this.isComicState(state);
        result.image = result.image && this.isImageState(state);
        result.shadow = result.shadow && this.isShadowState(state);
        result.fill = result.fill && this.isFillState(state);
        result.flow = result.flow && this.isFlow(state);
        var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);
        result.containsImage = result.containsImage || shape == 'image';

        for (var key in state.style) {
            var value = state.style[key];
            if (value != null) {
                if (result.style[key] == null) {
                    result.style[key] = value;
                } else if (result.style[key] != value) {
                    result.style[key] = '';
                }
            }
        }
    }

    // flow(graph)

};

/**
 * Returns information about the current selection.
 */
Format.prototype.isFillState = function (state) {
    return state.view.graph.model.isVertex(state.cell) ||
        mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null) == 'arrow' ||
        mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null) == 'filledEdge' ||
        mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null) == 'flexArrow';
};

/**
 * Returns information about the current selection.
 */
Format.prototype.isGlassState = function (state) { // 玻璃效果
    if (state.style.shape === 'label') {
        return false
    } else {
        var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);
        return (shape == 'label' || shape == 'rectangle' || shape == 'internalStorage' ||
            shape == 'ext' || shape == 'umlLifeline' || shape == 'swimlane' ||
            shape == 'process');
    }
};

/**
 * Returns information about the current selection.
 */
Format.prototype.isRoundedState = function (state) { // 圆角
    if (state.style.shape === 'label' || state.style.shape === 'text') {
        return false
    } else {
        return (state.shape != null) ? state.shape.isRoundable() :
            mxUtils.indexOf(this.roundableShapes, mxUtils.getValue(state.style,
                mxConstants.STYLE_SHAPE, null)) >= 0;
    }
};

/**
 * Returns information about the current selection.
 */
Format.prototype.isLineJumpState = function (state) {
    var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);
    var curved = mxUtils.getValue(state.style, mxConstants.STYLE_CURVED, false);

    return !curved && (shape == 'connector' || shape == 'filledEdge');
};

/**
 * Returns information about the current selection.
 */
Format.prototype.isComicState = function (state) { // 手绘
    var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);

    return mxUtils.indexOf(['rectangle', 'internalStorage', 'corner', 'parallelogram', 'note', 'collate',
        'swimlane', 'triangle', 'trapezoid', 'ext', 'step', 'tee', 'process', 'link', 'rhombus',
        'offPageConnector', 'loopLimit', 'hexagon', 'manualInput', 'singleArrow', 'doubleArrow',
        'flexArrow', 'filledEdge', 'card', 'umlLifeline', 'connector', 'folder', 'component', 'sortShape',
        'cross', 'umlFrame', 'cube', 'isoCube', 'isoRectangle', 'partialRectangle'
    ], shape) >= 0;
};
Format.prototype.isFlow = function (state) {
    var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);
    return mxUtils.indexOf(['rectangle', 'internalStorage', 'corner', 'parallelogram', 'note', 'collate',
        'swimlane', 'triangle', 'trapezoid', 'ext', 'step', 'tee', 'process', 'link', 'rhombus',
        'offPageConnector', 'loopLimit', 'hexagon', 'manualInput', 'singleArrow', 'doubleArrow',
        'flexArrow', 'filledEdge', 'card', 'umlLifeline', 'connector', 'folder', 'component', 'sortShape',
        'cross', 'umlFrame', 'cube', 'isoCube', 'isoRectangle', 'partialRectangle'
    ], shape) >= 0;
};
/**
 * Returns information about the current selection.
 */
Format.prototype.isAutoSizeState = function (state) {
    return mxUtils.getValue(state.style, mxConstants.STYLE_AUTOSIZE, null) == '1';
};

/**
 * Returns information about the current selection.
 */
Format.prototype.isImageState = function (state) {
    var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);

    return (shape == 'label' || shape == 'image');
};

/**
 * Returns information about the current selection.
 */
Format.prototype.isShadowState = function (state) { // 阴影

    var shape = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);
    // console.log(shape)
    return (shape != 'image' && shape != 'label' && shape != 'line' && shape != 'ellipse' && shape != 'text');
};

/**
 * Adds the label menu items to the given menu and parent.
 */
Format.prototype.clear = function () {
    this.container.innerHTML = '';
    // Destroy existing panels
    if (this.panels != null) {
        for (var i = 0; i < this.panels.length; i++) {
            this.panels[i].destroy();
        }
    }

    this.panels = [];
};

/**
 * Adds the label menu items to the given menu and parent.
 */
Format.prototype.refresh = function () {
    // Performance tweak: No refresh needed if not visible
    if (this.container.style.width == '0px') {
        return;
    }
    this.clear();
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var sect = graph.getSelectionCells()
    var ss = graph.view.getState(graph.getSelectionCell())

    var div = document.createElement('div');
    div.style.whiteSpace = 'nowrap';
    div.style.color = 'rgb(112, 112, 112)';
    div.style.textAlign = 'left';
    div.style.cursor = 'default';

    var label = document.createElement('div');
    label.style.border = '1px solid #e0e0e0';
    label.style.background = '#f6f6f6'
    label.style.borderWidth = '0px 0px 1px 0px';
    label.style.textAlign = 'center';
    label.style.fontWeight = 'bold';
    label.style.overflow = 'hidden';
    label.style.display = (mxClient.IS_QUIRKS) ? 'inline' : 'inline-block';
    label.style.paddingTop = '8px';
    label.style.height = (mxClient.IS_QUIRKS) ? '34px' : '25px';
    label.style.width = '100%';
    label.style.verticalAlign = 'middle'
    // 关闭面板按钮
    var closeFormattingPanel = document.createElement('a')
    closeFormattingPanel.classList.add('closeFormattingPanel')
    label.appendChild(closeFormattingPanel)
    closeFormattingPanel.setAttribute('title', '关闭面板(ctrl+Shift+P)')
    closeFormattingPanel.addEventListener('click', function (e) {
        ui.toggleFormatPanel()
    })
    this.container.appendChild(div);

    if (graph.isSelectionEmpty()) {
        mxUtils.write(label, '设置');

        div.appendChild(label);

        var a2 = document.createElement("div")
        var text2 = document.createTextNode("视图标题")

        var span = document.createElement("span")
        // <span style="color:red;">*</span>
        span.style = "color:red;"
        span.appendChild(document.createTextNode("*"))
        a2.appendChild(span)
        a2.appendChild(text2)
        // span.className = 'textareaSpan'
        var input = document.createElement("input")
        input.classList.add('eleInput')
        input.classList.add('view-title')
        input.id = 'ViewTitleInput'
        input.style.width = '156px'
        input.style.height = '24px'
        input.setAttribute('autocomplete', 'off')
        a2.appendChild(input)
        div.appendChild(a2)
        a2.style.marginTop = "10px"
        a2.style.marginBottom = "10px"
        a2.style.marginLeft = "10px"
        input.style.marginLeft = "10px"
        input.value = sessionStorage.getItem('viewTitle') || ''
        input.addEventListener('input', function (e) {
            // 值改变存到session里面去
            sessionStorage.setItem('viewTitle', e.target.value)
        })


        // 视图对象资源池列表绑定
        var a3 = document.createElement("div")
        a3.style.marginLeft = "20px"
        var text3 = document.createTextNode("资源池")

        var span = document.createElement("span")
        // <span style="color:red;">*</span>
        span.style = "color:red;"
        span.appendChild(document.createTextNode("*"))
        a3.appendChild(span)
        a3.appendChild(text3)
        idcTypeSelect = document.createElement('select')
        idcTypeSelect.name = 'idcTypeName'
        idcTypeSelect.className = "pictureTypeSelest idcType"
        // 设置option参数列表
        // 添加option
        var idcTypeDefaultOption = document.createElement('option')
        idcTypeDefaultOption.value = ""
        idcTypeDefaultOption.innerText = "请选择"
        idcTypeSelect.appendChild(idcTypeDefaultOption)
        for (var index = 0; index < idcTypeOptionData.length; index++) {
            var idcTypeOption = document.createElement('option')
            idcTypeOption.value = idcTypeOptionData[index].value
            idcTypeOption.innerText = idcTypeOptionData[index].name
            idcTypeSelect.appendChild(idcTypeOption)
            // if (sessionStorage.getItem('idcType') == idcTypeOption.value) {
            //     idcTypeOption.selected = true
            // }
        }
        idcTypeSelect.value = sessionStorage.getItem('idcType')
        //有值选择默认值sendProjectNameData
        a3.appendChild(idcTypeSelect)
        div.appendChild(a3)
        sessionStorage.setItem('idcType', idcTypeSelect.value)

        idcTypeSelect.value = sessionStorage.getItem('idcType')
        idcTypeSelect.addEventListener('change', function (e) {
            // 值改变存到session里面去
            sessionStorage.setItem('idcType', e.target.value)
            // 获取项目名称列表
            window.parent.postMessage({
                cmd: 'getProjectNameList',
                params: {
                    success: true,
                    idcType: e.target.value
                }
            }, '*');
        })

        // 视图对象pod列表绑定
        var a4 = document.createElement("div")
        a4.style.marginLeft = "35.45px"
        a4.style.marginTop = "10px"
        var text4 = document.createTextNode("POD")
        a4.appendChild(text4)
        podTypeSelect = document.createElement('select')
        podTypeSelect.name = 'podTypeName'
        podTypeSelect.className = "pictureTypeSelest pod"
        // 设置option参数列表
        var podDefaultOption = document.createElement('option')
        podDefaultOption.value = ""
        podDefaultOption.innerText = "请选择"
        podTypeSelect.appendChild(podDefaultOption)
        for (var index = 0; index < podOptionData.length; index++) {
            var podOption = document.createElement('option')
            podOption.value = podOptionData[index].name
            podOption.innerText = podOptionData[index].name

            //有值选择默认值
            // if (sessionStorage.getItem('pod')) {
            //     podOption.selected = true
            // }
            podTypeSelect.appendChild(podOption)
        }
        podTypeSelect.value = sessionStorage.getItem('pod')
        a4.appendChild(podTypeSelect)
        div.appendChild(a4)
        sessionStorage.setItem('pod', podTypeSelect.value)
        podTypeSelect.addEventListener('change', function (e) {
            // 值改变存到session里面去
            sessionStorage.setItem('pod', e.target.value)
        })

        //
        var a6 = document.createElement("div")
        a6.style.marginLeft = "15.45px"
        a6.style.marginTop = "10px"
        var text6 = document.createTextNode("项目名称")
        a6.appendChild(text6)
        projectNameSelect = document.createElement('select')
        projectNameSelect.name = 'projectName'
        projectNameSelect.className = "pictureTypeSelest projectName"
        // 设置option参数列表
        // 添加option
        var projectNameDefaultOption = document.createElement('option')
        projectNameDefaultOption.value = ""
        projectNameDefaultOption.innerText = "请选择"
        projectNameSelect.appendChild(projectNameDefaultOption)
        // for (var index = 0; index < projectNameOptionData.length; index++) {
        //     var projectNameOption = document.createElement('option')
        //     projectNameOption.value = projectNameOptionData[index].project_name
        //     projectNameOption.innerText = projectNameOptionData[index].project_name
        //     projectNameSelect.appendChild(projectNameOption)
        //     // if (sessionStorage.getItem('idcType') == idcTypeOption.value) {
        //     //     idcTypeOption.selected = true
        //     // }
        // }
        window.parent.postMessage({
            cmd: 'getProjectNameList',
            params: {
                success: true,
                idcType: sessionStorage.getItem('idcType')
            }
        }, '*');
        //有值选择默认值
        a6.appendChild(projectNameSelect)
        div.appendChild(a6)
        // sessionStorage.setItem('projectName', projectNameSelect.value)
        projectNameSelect.addEventListener('change', function (e) {
            // 值改变存到session里面去
            sessionStorage.setItem('projectName', e.target.value)
        })

        //添加业务系统下拉
        var a5 = document.createElement("div")
        a5.style.marginLeft = "10px"
        a5.style.marginTop = "10px"
        a5.className = 'bizSystemDiv'
        var text5 = document.createTextNode("业务系统")
        var span = document.createElement("span")
        // <span style="color:red;">*</span>
        span.style = "color:red;"
        span.appendChild(document.createTextNode("*"))
        a5.appendChild(span)
        a5.appendChild(text5)
        bizSystemSelect = document.createElement('select')
        bizSystemSelect.name = 'bizSystemName'
        bizSystemSelect.className = "pictureTypeSelest bizSystem"
        bizSystemSelect.placeholder = "请选择"
        // 设置option参数列表
        var bizSysDefaultOption = document.createElement('option')
        bizSysDefaultOption.value = ""
        bizSysDefaultOption.innerText = "请选择"
        bizSystemSelect.appendChild(bizSysDefaultOption)
        for (var index = 0; index < bizSystemOptionData.length; index++) {
            var bizSystemOption = document.createElement('option')
            bizSystemOption.value = bizSystemOptionData[index].name
            bizSystemOption.innerText = bizSystemOptionData[index].name

            //有值选择默认值
            // if (sessionStorage.getItem('bizSystem')) {
            //     bizSystemOption.selected = true
            // }
            bizSystemSelect.appendChild(bizSystemOption)
        }
        bizSystemSelect.value = sessionStorage.getItem('bizSystem')
        a5.appendChild(bizSystemSelect)
        div.appendChild(a5)
        sessionStorage.setItem('bizSystem', bizSystemSelect.value)
        bizSystemSelect.addEventListener('change', function (e) {
            // 值改变存到session里面去
            sessionStorage.setItem('bizSystem', e.target.value)
        })
        if (sessionStorage.getItem('pictureType') != '1') {
            a5.style.display = 'flex'
        } else {
            a5.style.display = 'none'
        }

        // var input2 = document.createElement("input")
        // var button2 = document.createElement("button")
        // a3.appendChild(input2)
        // a3.appendChild(button2)
        // div.appendChild(a3)
        // a3.style.marginTop = "5px"
        // input2.style.marginLeft = "10px"
        // input2.style.width = "101px"
        // a3.style.marginLeft = "10px"
        // input2.setAttribute("readonly", "true")
        // input2.value = sessionStorage.getItem('viewTitle') || ''
        // input2.classList.add('eleInput')
        // input2.classList.add('view-bind-input')
        // input2.style.height = '24px'
        // input2.style.borderRadius = "4px 0  0 4px"
        // button2.classList.add('eleButton')
        // button2.style.width = "56px"
        // button2.style.height = "24px"
        // button2.style.marginLeft = "-1px"
        // button2.style.padding = "0px  10px"
        // button2.innerText = "绑定"
        // button2.style.borderRadius = "0 4px 4px 0"
        // button2.style.fontSize = "10px"
        // button2.addEventListener('click', function (e) {
        //   // 向父vue页面发送信息
        //   window.parent.postMessage({
        //     cmd: 'showViewBind',
        //     params: {
        //       success: true
        //     }
        //   }, '*');
        // })
        // 视图类型
        var pictureTypeDiv = document.createElement('div')
        pictureTypeP = document.createElement('p')
        pictureTypeP.style.margin = '20px 0 0 0 '
        pictureTypeP.innerText = '视图类型'
        pictureTypeDiv.className = "pictureTypeDiv"
        pictureTypeP.className = "pictureTypeP"
        // 添加P标签
        pictureTypeDiv.appendChild(pictureTypeP)
        //添加选择框
        pictureTypeSelest = document.createElement('select')
        pictureTypeSelest.name = 'pictureTypename'
        pictureTypeSelest.className = "pictureTypeSelest pictureType"
        // 设置option参数列表
        // 添加option
        for (var index = 0; index < pictureTypeOptionData.length; index++) {
            var pictureTypeOption = document.createElement('option')
            pictureTypeOption.value = pictureTypeOptionData[index].id
            pictureTypeOption.innerText = pictureTypeOptionData[index].label

            //有值选择默认值
            if (sessionStorage.getItem('pictureType') && sessionStorage.getItem('pictureType') == index) {
                pictureTypeOption.selected = true
            }
            pictureTypeSelest.appendChild(pictureTypeOption)
        }
        pictureTypeSelest.value = sessionStorage.getItem('pictureType') || 1

        pictureTypeDiv.appendChild(pictureTypeSelest)
        div.appendChild(pictureTypeDiv)

        //添加select事件
        pictureTypeSelest.addEventListener('change', function (e) {
            // 向父vue页面发送信息
            window.parent.postMessage({
                cmd: 'switchPageType',
                params: {
                    success: true,
                    pictureType: e.target.value
                }
            }, '*');
            var $bizSystemDiv = document.querySelector('.bizSystemDiv');
            if (e.target.value == '1') {
                $bizSystemDiv.style.display = 'none'
            } else {
                $bizSystemDiv.style.display = 'flex'
            }
        })

        // // 视图类型
        // var divViewType = document.createElement("div")
        // var textViewType = document.createTextNode("视图类型")
        // divViewType.appendChild(textViewType)
        // var inputViewType = document.createElement("input")
        // var buttonViewType = document.createElement("button")
        // divViewType.appendChild(inputViewType)
        // divViewType.appendChild(buttonViewType)
        // div.appendChild(divViewType)
        // divViewType.className = "pictureTypeDiv"
        // // divViewType.style.marginTop = "5px"
        // inputViewType.style.marginLeft = "10px"
        // inputViewType.style.width = "101px"
        // divViewType.style.paddingTop = "10px"
        // inputViewType.setAttribute("readonly", "true")
        // inputViewType.value = sessionStorage.getItem('viewType') || ''
        // inputViewType.classList.add('eleInput')
        // inputViewType.classList.add('view-type')
        // inputViewType.style.height = '24px'
        // inputViewType.style.borderRadius = "4px 0  0 4px"
        // buttonViewType.classList.add('eleButton')
        // buttonViewType.style.width = "56px"
        // buttonViewType.style.height = "24px"
        // buttonViewType.style.marginLeft = "-1px"
        // buttonViewType.style.padding = "0px  10px"
        // buttonViewType.innerText = "绑定"
        // buttonViewType.style.borderRadius = "0 4px 4px 0"
        // buttonViewType.style.fontSize = "10px"
        // buttonViewType.addEventListener('click', function (e) {
        //   // 向父vue页面发送信息
        //   window.parent.postMessage({
        //     cmd: 'showViewTypeBind',
        //     params: {
        //       success: true
        //     }
        //   }, '*');
        // })

        // 页面类型
        var pageTypeDiv = document.createElement("div")
        var pageTypeText = document.createTextNode("页面类型")
        var pageTypep = document.createElement("p")
        // 创建实例单选按钮
        var pageTypeRadioShow1 = document.createElement('p')
        var pageTypeRadioShow2 = document.createElement('p')

        //设置节点
        var pageTypeRadioType1 = document.createElement('input')
        var pageTypeRadioType1Text = document.createElement('p')
        var pageTypeRadioType2 = document.createElement('input')
        var pageTypeRadioType2Text = document.createElement('p')
        pageTypeRadioType1Text.innerText = '普通页面'
        pageTypeRadioType2Text.innerText = '模板'
        pageTypeRadioType1.setAttribute('type', 'radio')
        pageTypeRadioType2.setAttribute('type', 'radio')
        pageTypeRadioType1.name = 'pageType'
        pageTypeRadioType2.name = 'pageType'
        pageTypeRadioType1.value = 0
        pageTypeRadioType2.value = 1
        //设置状态
        pageTypeRadioType1Text.style.marginRight = '25px'
        pageTypep.style.marginRight = '12px'
        pageTypeRadioType1.style.display = 'none'
        pageTypeRadioType2.style.display = 'none'

        // 装载节点
        pageTypep.appendChild(pageTypeText)
        pageTypeDiv.appendChild(pageTypep)
        pageTypeDiv.appendChild(pageTypeRadioType1)
        pageTypeDiv.appendChild(pageTypeRadioShow1)

        pageTypeDiv.appendChild(pageTypeRadioType1Text)
        pageTypeDiv.appendChild(pageTypeRadioType2)
        pageTypeDiv.appendChild(pageTypeRadioShow2)
        pageTypeDiv.appendChild(pageTypeRadioType2Text)

        pageTypeDiv.classList = 'pageTypeDiv'
        pageTypep.classList = 'pageTypep'
        pageTypeRadioType1Text.classList = 'pageTypep'
        pageTypeRadioType2Text.classList = 'pageTypep'
        div.appendChild(pageTypeDiv)

        var cells = graph.getModel().cells
        var state = graph.view.getState(cells[0])

        if (state && state.style.pagetype === 0) {
            pageTypeRadioShow1.className = 'radioChecked'
            pageTypeRadioShow2.className = 'radioUnChecked'
            pageTypeRadioType1.checked = true
        } else if (state && state.style.pagetype === 1) {
            pageTypeRadioShow1.className = 'radioUnChecked'
            pageTypeRadioShow2.className = 'radioChecked'
            pageTypeRadioType2.checked = true
        } else {
            graph.getModel().setStyle(cells[0], 'pagetype=0');
            pageTypeRadioType1.checked = true
            pageTypeRadioType2.checked = false
            pageTypeRadioShow1.className = 'radioChecked'
            pageTypeRadioShow2.className = 'radioUnChecked'
        }
        // 监听show实列事件
        pageTypeRadioShow1.addEventListener('click', function (e) {
            pageTypeRadioShow1.className = 'radioChecked'
            pageTypeRadioShow2.className = 'radioUnChecked'
            pageTypeRadioType1.click()
        })
        pageTypeRadioShow2.addEventListener('click', function (e) {
            pageTypeRadioShow1.className = 'radioUnChecked'
            pageTypeRadioShow2.className = 'radioChecked'
            pageTypeRadioType2.click()
        })
        // 监听radio事件
        pageTypeRadioType1.addEventListener('change', function (e) {
            graph.getModel().setStyle(cells[0], 'pagetype=0');
            // 向父vue页面发送信息
            window.parent.postMessage({
                cmd: 'pagetype',
                params: {
                    success: true,
                    pagetype: e.target.value
                }
            }, '*');
        })
        pageTypeRadioType2.addEventListener('change', function (e) {
            graph.getModel().setStyle(cells[0], 'pagetype=1');
            // 向父vue页面发送信息
            window.parent.postMessage({
                cmd: 'pagetype',
                params: {
                    success: true,
                    pagetype: e.target.value
                }
            }, '*');
        })
        // bgcolor
        bgcolor = document.createElement('div')
        bgcolor.id = 'bgcolor'
        bgcolor.style.display = 'inline-block'
        bgcolor.style.width = '48px'
        bgcolor.style.height = '24px'
        // gridColor
        gridColor = document.createElement('div')
        gridColor.id = 'gridColor'
        gridColor.style.display = 'inline-block'
        gridColor.style.width = '48px'
        gridColor.style.height = '24px'
        gridColor.style.marginLeft = '10px'

        div.appendChild(bgcolor)
        div.appendChild(gridColor)

        new ewPlugins('colorpicker', {
            el: "#bgcolor",
            alpha: true, //是否开启透明度
            size: 'mini', //颜色box类型
            predefineColor: [
                '#ff4500',
                '#ff8c00',
                '#ffd700',
                '#90ee90',
                '#00ced1',
                '#1e90ff',
                '#c71585',
            ], //预定义颜色
            defaultColor: graph.background,
            disabled: false, //是否禁止打开颜色选择器
            openPickerAni: 'opacity', //打开颜色选择器动画
            sure: function (color) {
                graph.background = color;
                graph.view.validateBackground();
            }, //点击确认按钮事件回调
            clear: function () {
                graph.background = '#fff';
                graph.view.validateBackground();
            } //点击清空按钮事件回调
        })

        new ewPlugins('colorpicker', {
            el: "#gridColor",
            alpha: true, //是否开启透明度
            size: 'mini', //颜色box类型
            predefineColor: [
                '#ff4500',
                '#ff8c00',
                '#ffd700',
                '#90ee90',
                '#00ced1',
                '#1e90ff',
                '#c71585',
            ], //预定义颜色
            defaultColor: graph.view.gridColor,
            disabled: false, //是否禁止打开颜色选择器
            openPickerAni: 'opacity', //打开颜色选择器动画
            sure: function (color) {
                graph.view.gridColor = color;
                graph.view.validateBackground();
            }, //点击确认按钮事件回调
            clear: function () {
                graph.view.gridColor = 'rgba(224,224,224,1)';
                graph.view.validateBackground();
            } //点击清空按钮事件回调
        })

        div.className = 'setup'
        this.panels.push(new DiagramFormatPanel(this, ui, div));

    }
    // else if (graph.isEditing()) { // 文本框子
    //   mxUtils.write(label, mxResources.get('text'));
    //   label.style.borderBottom = '1px solid #e0e0e0';
    //   label.style.background = '#f6f6f6'
    //   div.appendChild(label);
    //   div.className = 'texttable'
    //   this.panels.push(new TextFormatPanel(this, ui, div,0));
    // }
    else {
        var containsLabel = this.getSelectionState().containsLabel;
        var currentLabel = null;
        var currentPanel = null;
        var addClickHandler = mxUtils.bind(this, function (elt, panel, index) {
            var clickHandler = mxUtils.bind(this, function (evt) {
                if (currentLabel != elt) {
                    if (containsLabel) {
                        this.labelIndex = index;
                    } else {
                        this.currentIndex = index;
                    }

                    if (currentLabel != null) {
                        currentLabel.style.backgroundColor = this.inactiveTabBackgroundColor;
                        currentLabel.style.borderBottomWidth = '1px';
                    }

                    currentLabel = elt;
                    currentLabel.style.backgroundColor = '#ffffff';
                    currentLabel.style.borderBottomWidth = '0px';

                    // console.log(currentPanel, panel);

                    if (currentPanel != panel) {
                        if (currentPanel != null) {
                            currentPanel.style.display = 'none';
                        }

                        currentPanel = panel;
                        currentPanel.style.display = '';
                    }
                }
            });
            mxEvent.addListener(elt, 'click', clickHandler);

            if (index == ((containsLabel) ? this.labelIndex : this.currentIndex)) {
                // Invokes handler directly as a workaround for no click on DIV in KHTML.
                clickHandler();
            }
        });

        var idx = 0;

        label.style.backgroundColor = this.inactiveTabBackgroundColor;
        label.style.borderLeftWidth = '1px';
        label.style.width = (containsLabel) ? '50%' : '33.3%';
        label.style.width = (containsLabel) ? '50%' : '33.3%';
        var label2 = label.cloneNode(false);
        var label3 = label2.cloneNode(false);

        // Workaround for ignored background in IE
        label2.style.backgroundColor = this.inactiveTabBackgroundColor;
        label3.style.backgroundColor = this.inactiveTabBackgroundColor;

        // console.log(ss);

        // Style 位置
        if (containsLabel) {
            label2.style.borderLeftWidth = '0px';
        } else if (ss && sect.length === 1 && (ss.style.type === 'jintaitupian' || ss.style.type === 'dongtu')) {
            label.style.borderLeftWidth = '0px';
            mxUtils.write(label, '样式');
            div.appendChild(getStyleTitle(ss))
            div.appendChild(label);
            var stylePanel = div.cloneNode(false);
            stylePanel.style.display = 'none';
            this.panels.push(new styleFormatPanelLabels(this, ui, stylePanel));

            this.container.appendChild(stylePanel);
            addClickHandler(label, stylePanel, idx++);
        } else {
            label.style.borderLeftWidth = '0px';
            mxUtils.write(label, mxResources.get('style'));
            div.appendChild(label);
            var stylePanel = div.cloneNode(false);
            stylePanel.style.display = 'none';
            this.panels.push(new TextFormatPanel(this, ui, stylePanel, 0));
            this.panels.push(new StyleFormatPanel(this, ui, stylePanel));


            new ArrangePanel(this, ui, stylePanel, 0)

            this.container.appendChild(stylePanel);
            addClickHandler(label, stylePanel, idx++);


            if (ss && ss.style && ss.style.shape != "line") {
                // 字体颜色
                new ewPlugins('colorpicker', {
                    el: "#fontColor",
                    alpha: true, //是否开启透明度
                    size: 'mini', //颜色box类型
                    predefineColor: [
                        '#ff4500',
                        '#ff8c00',
                        '#ffd700',
                        '#90ee90',
                        '#00ced1',
                        '#1e90ff',
                        '#c71585',
                    ], //预定义颜色
                    defaultColor: ss.style.fontColor || '',
                    disabled: false, //是否禁止打开颜色选择器
                    openPickerAni: 'opacity', //打开颜色选择器动画
                    sure: function (color) {
                        graph.setCellStyles('fontColor', color, graph.getSelectionCells());
                        var style = graph.getStylesheet().getDefaultVertexStyle();
                        style[mxConstants.STYLE_FONTCOLOR] = color;
                    }, //点击确认按钮事件回调
                    clear: function () {
                        graph.setCellStyles('fontColor', 'none', graph.getSelectionCells());
                        var style = graph.getStylesheet().getDefaultVertexStyle();
                        style[mxConstants.STYLE_FONTCOLOR] = '#000000';
                    } //点击清空按钮事件回调
                })
            }

            var state = this.getSelectionState()
            // console.log(ss.style.shape)
            if (sect.length <= 1) {
                if (ss && ss.style && (ss.style.shape == 'text' || ss.style.shape == 'label')) {
                    // 文本填充色
                    new ewPlugins('colorpicker', {
                        el: "#fillColor",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.fillColor || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('fillColor', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('fillColor', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                    //  文本边框颜色strokeColor
                    new ewPlugins('colorpicker', {
                        el: "#strokeColor",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.strokeColor || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('strokeColor', color, graph.getSelectionCells());
                            var style = graph.getStylesheet().getDefaultEdgeStyle();
                            style[mxConstants.STYLE_STROKECOLOR] = color;
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('strokeColor', 'none', graph.getSelectionCells());
                            var style = graph.getStylesheet().getDefaultEdgeStyle();
                            style[mxConstants.STYLE_STROKECOLOR] = 'none';
                        } //点击清空按钮事件回调
                    })
                } else if (ss && ss.style && (ss.style.shape == 'connector' || ss.style.shape == "line" || ss.style.shape == 'ellipse')) {
                    new ewPlugins('colorpicker', {
                        el: "#xian",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.strokeColor || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('strokeColor', color, graph.getSelectionCells());
                            var style = graph.getStylesheet().getDefaultEdgeStyle();
                            style[mxConstants.STYLE_STROKECOLOR] = color;
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('strokeColor', 'none', graph.getSelectionCells());
                            var style = graph.getStylesheet().getDefaultEdgeStyle();
                            style[mxConstants.STYLE_STROKECOLOR] = 'none';
                        } //点击清空按钮事件回调
                    })
                } else if (ss && ss.style && (ss.style.shape === "image")) {
                    // label背景色
                    new ewPlugins('colorpicker', {
                        el: "#labelBackgroundColor",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.labelBackgroundColor || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('labelBackgroundColor', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('labelBackgroundColor', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                    // 元件背景色
                    new ewPlugins('colorpicker', {
                        el: "#imageBackground",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.imageBackground || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('imageBackground', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('imageBackground', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                    // 元件边框色
                    new ewPlugins('colorpicker', {
                        el: "#imageBorder",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.imageBorder || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('imageBorder', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('imageBorder', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                }
            } else {
                if (state && state.style && (state.style.shape === 'image')) {
                    new ewPlugins('colorpicker', {
                        el: "#fontBg",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.labelBackgroundColor || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('labelBackgroundColor', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('labelBackgroundColor', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                    new ewPlugins('colorpicker', {
                        el: "#beiJing",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.imageBackground || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('imageBackground', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('imageBackground', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                    new ewPlugins('colorpicker', {
                        el: "#bianKuang",
                        alpha: true, //是否开启透明度
                        size: 'mini', //颜色box类型
                        predefineColor: [
                            '#ff4500',
                            '#ff8c00',
                            '#ffd700',
                            '#90ee90',
                            '#00ced1',
                            '#1e90ff',
                            '#c71585',
                        ], //预定义颜色
                        defaultColor: ss.style.imageBorder || '',
                        disabled: false, //是否禁止打开颜色选择器
                        openPickerAni: 'opacity', //打开颜色选择器动画
                        sure: function (color) {
                            graph.setCellStyles('imageBorder', color, graph.getSelectionCells());
                        }, //点击确认按钮事件回调
                        clear: function () {
                            graph.setCellStyles('imageBorder', 'none', graph.getSelectionCells());
                        } //点击清空按钮事件回调
                    })
                } else {
                    if (ss && ss.style) {
                        new ewPlugins('colorpicker', {
                            el: "#tianChong",
                            alpha: true, //是否开启透明度
                            size: 'mini', //颜色box类型
                            predefineColor: [
                                '#ff4500',
                                '#ff8c00',
                                '#ffd700',
                                '#90ee90',
                                '#00ced1',
                                '#1e90ff',
                                '#c71585',
                            ], //预定义颜色
                            defaultColor: ss.style.fillColor || '',
                            disabled: false, //是否禁止打开颜色选择器
                            openPickerAni: 'opacity', //打开颜色选择器动画
                            sure: function (color) {
                                graph.setCellStyles('imageBackground', color, graph.getSelectionCells());
                                graph.setCellStyles('fillColor', color, graph.getSelectionCells());
                            }, //点击确认按钮事件回调
                            clear: function () {
                                graph.setCellStyles('imageBackground', 'none', graph.getSelectionCells());
                                graph.setCellStyles('fillColor', 'none', graph.getSelectionCells());
                            } //点击清空按钮事件回调
                        })
                        new ewPlugins('colorpicker', {
                            el: "#lianXian",
                            alpha: true, //是否开启透明度
                            size: 'mini', //颜色box类型
                            predefineColor: [
                                '#ff4500',
                                '#ff8c00',
                                '#ffd700',
                                '#90ee90',
                                '#00ced1',
                                '#1e90ff',
                                '#c71585',
                            ], //预定义颜色
                            defaultColor: ss.style.strokeColor || '',
                            disabled: false, //是否禁止打开颜色选择器
                            openPickerAni: 'opacity', //打开颜色选择器动画
                            sure: function (color) {
                                graph.setCellStyles('strokeColor', color, graph.getSelectionCells());
                                var style = graph.getStylesheet().getDefaultEdgeStyle();
                                style[mxConstants.STYLE_STROKECOLOR] = color;
                            }, //点击确认按钮事件回调
                            clear: function () {
                                graph.setCellStyles('strokeColor', 'none', graph.getSelectionCells());
                                var style = graph.getStylesheet().getDefaultEdgeStyle();
                                style[mxConstants.STYLE_STROKECOLOR] = 'none';
                            } //点击清空按钮事件回调
                        })
                    }
                }
            }
        }

        // Text
        // mxUtils.write(label2, mxResources.get('text'));
        mxUtils.write(label2, '属性');
        div.appendChild(label2);
        var textPanel = div.cloneNode(false);
        textPanel.style.display = 'none';
        this.panels.push(new TextFormatPanel(this, ui, textPanel, 1));
        this.container.appendChild(textPanel);
        // Arrange
        mxUtils.write(label3, mxResources.get('arrange'));
        div.appendChild(label3);

        var arrangePanel = div.cloneNode(false);
        arrangePanel.style.display = 'none';
        this.panels.push(new ArrangePanel(this, ui, arrangePanel, 1));

        this.container.appendChild(arrangePanel);
        // var cn = this.container.lastElementChild
        // cn.style.display = 'none'
        // console.log(this.container);

        addClickHandler(label2, textPanel, idx++);
        addClickHandler(label3, arrangePanel, idx++);
    }
};

// 公共的获取标题头的方法
getStyleTitle = function (ss) {
    var styleTitle = document.createElement('div')
    styleTitle.style.height = '33px'
    styleTitle.style.background = '#f6f6f6'
    styleTitle.style.paddingLeft = '10px'
    styleTitle.style.textAlign = 'left'
    styleTitle.style.fontWeight = 'bold'
    styleTitle.style.lineHeight = '33px'
    styleTitle.style.borderBottom = '1px solid rgb(224, 224, 224)'
    switch (ss.style.type) {
        case 'jintaitupian':
            styleTitle.innerText = '静态图片'
            break
        case 'dongtu':
            styleTitle.innerText = '动态图片'
            break
    }

    return styleTitle
}

/**
 * Base class for format panels.
 */
BaseFormatPanel = function (format, editorUi, container) {
    this.format = format;
    this.editorUi = editorUi;
    this.container = container;
    this.listeners = [];
};


/**
 *
 */
BaseFormatPanel.prototype.buttonBackgroundColor = 'white';

/**
 * Adds the given color option.
 */
BaseFormatPanel.prototype.getSelectionState = function () {
    var graph = this.editorUi.editor.graph;
    var cells = graph.getSelectionCells();
    var shape = null;

    for (var i = 0; i < cells.length; i++) {
        var state = graph.view.getState(cells[i]);

        if (state != null) {
            var tmp = mxUtils.getValue(state.style, mxConstants.STYLE_SHAPE, null);

            if (tmp != null) {
                if (shape == null) {
                    shape = tmp;
                } else if (shape != tmp) {
                    return null;
                }
            }

        }
    }

    return shape;
};

/**
 * Install input handler.
 */
BaseFormatPanel.prototype.installInputHandler = function (input, key, defaultValue, min, max, unit, textEditFallback, isFloat) {
    unit = (unit != null) ? unit : '';
    isFloat = (isFloat != null) ? isFloat : false;

    var ui = this.editorUi;
    var graph = ui.editor.graph;

    min = (min != null) ? min : 1;
    max = (max != null) ? max : 999;

    var selState = null;
    var updating = false;

    var update = mxUtils.bind(this, function (evt) {
        var value = (isFloat) ? parseFloat(input.value) : parseInt(input.value);

        // Special case: angle mod 360
        if (!isNaN(value) && key == mxConstants.STYLE_ROTATION) {
            // Workaround for decimal rounding errors in floats is to
            // use integer and round all numbers to two decimal point
            value = mxUtils.mod(Math.round(value * 100), 36000) / 100;
        }

        value = Math.min(max, Math.max(min, (isNaN(value)) ? defaultValue : value));

        if (graph.cellEditor.isContentEditing() && textEditFallback) {
            if (!updating) {
                updating = true;

                if (selState != null) {
                    graph.cellEditor.restoreSelection(selState);
                    selState = null;
                }

                textEditFallback(value);
                input.value = value + unit;

                // Restore focus and selection in input
                updating = false;
            }
        } else if (value != mxUtils.getValue(this.format.getSelectionState().style, key, defaultValue)) {
            if (graph.isEditing()) {
                graph.stopEditing(true);
            }

            graph.getModel().beginUpdate();
            try {
                graph.setCellStyles(key, value, graph.getSelectionCells());

                // Handles special case for fontSize where HTML labels are parsed and updated
                if (key == mxConstants.STYLE_FONTSIZE) {
                    graph.updateLabelElements(graph.getSelectionCells(), function (elt) {
                        elt.style.fontSize = value + 'px';
                        elt.removeAttribute('size');
                    });
                }

                ui.fireEvent(new mxEventObject('styleChanged', 'keys', [key],
                    'values', [value], 'cells', graph.getSelectionCells()));
            } finally {
                graph.getModel().endUpdate();
            }
        }

        input.value = value + unit;
        mxEvent.consume(evt);
    });

    if (textEditFallback && graph.cellEditor.isContentEditing()) {
        // KNOWN: Arrow up/down clear selection text in quirks/IE 8
        // Text size via arrow button limits to 16 in IE11. Why?
        mxEvent.addListener(input, 'mousedown', function () {
            if (document.activeElement == graph.cellEditor.textarea) {
                selState = graph.cellEditor.saveSelection();
            }
        });

        mxEvent.addListener(input, 'touchstart', function () {
            if (document.activeElement == graph.cellEditor.textarea) {
                selState = graph.cellEditor.saveSelection();
            }
        });
    }

    mxEvent.addListener(input, 'change', update);
    mxEvent.addListener(input, 'blur', update);

    return update;
};

/**
 * Adds the given option.
 */
BaseFormatPanel.prototype.createPanel = function () {
    var div = document.createElement('div');
    div.style.padding = '12px 0px 12px 10px';
    div.style.borderBottom = '1px solid #e0e0e0';

    return div;
};

/**
 * Adds the given option.
 */
BaseFormatPanel.prototype.createTitle = function (title) {
    var div = document.createElement('div');
    div.style.padding = '0px 0px 6px 0px';
    div.style.whiteSpace = 'nowrap';
    div.style.overflow = 'hidden';
    div.style.width = '200px';
    div.style.fontWeight = 'bold';
    mxUtils.write(div, title);

    return div;
};

/**
 *
 */
BaseFormatPanel.prototype.createStepper = function (input, update, step, height, disableFocus, defaultValue) {
    step = (step != null) ? step : 1;
    height = (height != null) ? height : 8;

    if (mxClient.IS_QUIRKS) {
        height = height - 2;
    } else if (mxClient.IS_MT || document.documentMode >= 8) {
        height = height + 1;
    }

    var stepper = document.createElement('div');
    mxUtils.setPrefixedStyle(stepper.style, 'borderRadius', '3px');
    stepper.style.border = '1px solid #e0e0e0';
    stepper.style.position = 'absolute';

    var up = document.createElement('div');
    up.style.borderBottom = '1px solid #e0e0e0';
    up.style.position = 'relative';
    up.style.height = height + 'px';
    up.style.width = '10px';
    up.className = 'geBtnUp';
    stepper.appendChild(up);

    var down = up.cloneNode(false);
    down.style.border = 'none';
    down.style.height = height + 'px';
    down.className = 'geBtnDown';
    stepper.appendChild(down);

    mxEvent.addListener(down, 'click', function (evt) {
        if (input.value == '') {
            input.value = defaultValue || '2';
        }

        var val = parseInt(input.value);

        if (!isNaN(val)) {
            input.value = val - step;

            if (update != null) {
                update(evt);
            }
        }

        mxEvent.consume(evt);
    });

    mxEvent.addListener(up, 'click', function (evt) {
        if (input.value == '') {
            input.value = defaultValue || '0';
        }

        var val = parseInt(input.value);

        if (!isNaN(val)) {
            input.value = val + step;

            if (update != null) {
                update(evt);
            }
        }

        mxEvent.consume(evt);
    });

    mxEvent.addListener(up, 'click', function (evt) {
        if (input.value == '') {
            input.value = defaultValue || '0';
        }

        var val = parseInt(input.value);

        if (!isNaN(val)) {
            input.value = val + step;

            if (update != null) {
                update(evt);
            }
        }

        mxEvent.consume(evt);
    });

    // Disables transfer of focus to DIV but also :active CSS
    // so it's only used for fontSize where the focus should
    // stay on the selected text, but not for any other input.
    if (disableFocus) {
        var currentSelection = null;

        mxEvent.addGestureListeners(stepper,
            function (evt) {
                // Workaround for lost current selection in page because of focus in IE
                if (mxClient.IS_QUIRKS || document.documentMode == 8) {
                    currentSelection = document.selection.createRange();
                }

                mxEvent.consume(evt);
            },
            null,
            function (evt) {
                // Workaround for lost current selection in page because of focus in IE
                if (currentSelection != null) {
                    try {
                        currentSelection.select();
                    } catch (e) {
                        // ignore
                    }

                    currentSelection = null;
                    mxEvent.consume(evt);
                }
            }
        );
    }

    return stepper;
};


BaseFormatPanel.prototype.createStepper_personal = function (input, update, step, height, disableFocus, defaultValue) {
    step = (step != null) ? step : 1;
    height = (height != null) ? height : 8;

    if (mxClient.IS_QUIRKS) {
        height = height - 2;
    } else if (mxClient.IS_MT || document.documentMode >= 8) {
        height = height + 1;
    }

    var stepper = document.createElement('div');
    mxUtils.setPrefixedStyle(stepper.style, 'borderRadius', '3px');
    stepper.style.border = '1px solid #e0e0e0';
    stepper.style.position = 'absolute';
    stepper.className = 'stepperDiv'


    var up = document.createElement('div');
    up.className = 'topButton';
    up.style.float = "left"


    var down = document.createElement('div');
    down.className = 'dowmButton';
    down.style.float = "right"

    stepper.appendChild(up); // 上下箭头
    stepper.appendChild(down);

    mxEvent.addListener(down, 'click', function (evt) {
        if (input.value == '') {
            input.value = defaultValue || '2';
        }

        var val = parseInt(input.value);

        if (!isNaN(val)) {
            input.value = val - step;

            if (update != null) {
                update(evt);
            }
        }

        mxEvent.consume(evt);
    });

    mxEvent.addListener(up, 'click', function (evt) {
        if (input.value == '') {
            input.value = defaultValue || '0';
        }

        var val = parseInt(input.value);

        if (!isNaN(val)) {
            input.value = val + step;

            if (update != null) {
                update(evt);
            }
        }

        mxEvent.consume(evt);
    });

    mxEvent.addListener(up, 'click', function (evt) {
        if (input.value == '') {
            input.value = defaultValue || '0';
        }

        var val = parseInt(input.value);

        if (!isNaN(val)) {
            input.value = val + step;

            if (update != null) {
                update(evt);
            }
        }

        mxEvent.consume(evt);
    });

    // Disables transfer of focus to DIV but also :active CSS
    // so it's only used for fontSize where the focus should
    // stay on the selected text, but not for any other input.
    if (disableFocus) {
        var currentSelection = null;

        mxEvent.addGestureListeners(stepper,
            function (evt) {
                // Workaround for lost current selection in page because of focus in IE
                if (mxClient.IS_QUIRKS || document.documentMode == 8) {
                    currentSelection = document.selection.createRange();
                }

                mxEvent.consume(evt);
            },
            null,
            function (evt) {
                // Workaround for lost current selection in page because of focus in IE
                if (currentSelection != null) {
                    try {
                        currentSelection.select();
                    } catch (e) {
                        // ignore
                    }

                    currentSelection = null;
                    mxEvent.consume(evt);
                }
            }
        );
    }

    return stepper;
};
/**
 * Adds the given option.
 */
BaseFormatPanel.prototype.createOption = function (label, isCheckedFn, setCheckedFn, listener) {
    var div = document.createElement('div');
    div.style.padding = '6px 0px 1px 0px';
    div.style.whiteSpace = 'nowrap';
    div.style.overflow = 'hidden';
    div.style.width = '200px';
    div.style.height = (mxClient.IS_QUIRKS) ? '27px' : '18px';

    var cb = document.createElement('input');
    cb.setAttribute('type', 'checkbox');
    cb.style.margin = '0px 6px 0px 0px';
    div.appendChild(cb);

    var span = document.createElement('span');
    mxUtils.write(span, label);
    div.appendChild(span);

    var applying = false;
    var value = isCheckedFn();

    var apply = function (newValue) {
        if (!applying) {
            applying = true;

            if (newValue) {
                cb.setAttribute('checked', 'checked');
                cb.defaultChecked = true;
                cb.checked = true;
            } else {
                cb.removeAttribute('checked');
                cb.defaultChecked = false;
                cb.checked = false;
            }

            if (value != newValue) {
                value = newValue;

                // Checks if the color value needs to be updated in the model
                if (isCheckedFn() != value) {
                    setCheckedFn(value);
                }
            }

            applying = false;
        }
    };

    mxEvent.addListener(div, 'click', function (evt) {
        if (cb.getAttribute('disabled') != 'disabled') {
            // Toggles checkbox state for click on label
            var source = mxEvent.getSource(evt);

            if (source == div || source == span) {
                cb.checked = !cb.checked;
            }

            apply(cb.checked);
        }
    });

    apply(value);

    if (listener != null) {
        listener.install(apply);
        this.listeners.push(listener);
    }

    return div;
};

/**
 * The string 'null' means use null in values.
 */
BaseFormatPanel.prototype.createCellOption = function (label, key, defaultValue, enabledValue, disabledValue, fn, action, stopEditing) {
    enabledValue = (enabledValue != null) ? ((enabledValue == 'null') ? null : enabledValue) : '1';
    disabledValue = (disabledValue != null) ? ((disabledValue == 'null') ? null : disabledValue) : '0';

    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    return this.createOption(label, function () {
        // Seems to be null sometimes, not sure why...
        var state = graph.view.getState(graph.getSelectionCell());

        if (state != null) {
            return mxUtils.getValue(state.style, key, defaultValue) != disabledValue;
        }

        return null;
    }, function (checked) {

        if (stopEditing) {
            graph.stopEditing();
        }

        if (action != null) {
            action.funct();
        } else {
            graph.getModel().beginUpdate();
            try {
                var value = (checked) ? enabledValue : disabledValue;

                graph.setCellStyles(key, value, graph.getSelectionCells());

                if (fn != null) {
                    fn(graph.getSelectionCells(), value);
                }

                ui.fireEvent(new mxEventObject('styleChanged', 'keys', [key],
                    'values', [value], 'cells', graph.getSelectionCells()));

            } finally {
                graph.getModel().endUpdate();
            }

            var state = graph.view.getState(graph.getSelectionCell());
            if (key === 'flow' && checked) {
                state.shape.node.getElementsByTagName('path')[1].setAttribute('class', 'flow')
            } else if (key === 'flow' && !checked) {
                state.shape.node.getElementsByTagName('path')[1].setAttribute('class', '')
            }
        }
    }, {
        install: function (apply) {
            this.listener = function () {
                // Seems to be null sometimes, not sure why...
                var state = graph.view.getState(graph.getSelectionCell());

                if (state != null) {
                    apply(mxUtils.getValue(state.style, key, defaultValue) != disabledValue);
                }
            };

            graph.getModel().addListener(mxEvent.CHANGE, this.listener);
        },
        destroy: function () {
            graph.getModel().removeListener(this.listener);
        }
    });
};

/**
 * Adds the given color option.
 */
BaseFormatPanel.prototype.createColorOption = function (label, getColorFn, setColorFn, defaultColor, listener, callbackFn, hideCheckbox) {
    var div = document.createElement('div');
    div.style.padding = '6px 0px 1px 0px';
    div.style.whiteSpace = 'nowrap';
    div.style.overflow = 'hidden';
    div.style.width = '200px';
    div.style.height = (mxClient.IS_QUIRKS) ? '27px' : '18px';


    var cb = document.createElement('input');
    cb.setAttribute('type', 'checkbox');
    cb.style.margin = '0px 6px 0px 0px';

    if (!hideCheckbox) {
        div.appendChild(cb);
    }

    var span = document.createElement('span');
    mxUtils.write(span, label);
    div.appendChild(span);

    var applying = false;
    var value = getColorFn();

    var btn = null;

    var apply = function (color, disableUpdate, forceUpdate) {
        if (!applying) {
            applying = true;
            btn.innerHTML = '<div style="width:' + ((mxClient.IS_QUIRKS) ? '30' : '36') +
                'px;height:12px;margin:3px;border:1px solid black;background-color:' +
                ((color != null && color != mxConstants.NONE) ? color : defaultColor) + ';"></div>';

            // Fine-tuning in Firefox, quirks mode and IE8 standards
            if (mxClient.IS_QUIRKS || document.documentMode == 8) {
                btn.firstChild.style.margin = '0px';
            }

            if (color != null && color != mxConstants.NONE) {
                cb.setAttribute('checked', 'checked');
                cb.defaultChecked = true;
                cb.checked = true;
            } else {
                cb.removeAttribute('checked');
                cb.defaultChecked = false;
                cb.checked = false;
            }

            btn.style.display = (cb.checked || hideCheckbox) ? '' : 'none';

            if (callbackFn != null) {
                callbackFn(color);
            }

            if (!disableUpdate) {
                value = color;

                // Checks if the color value needs to be updated in the model
                if (forceUpdate || hideCheckbox || getColorFn() != value) {
                    setColorFn(value);
                }
            }

            applying = false;
        }
    };

    btn = mxUtils.button('', mxUtils.bind(this, function (evt) {
        this.editorUi.pickColor(value, function (color) {
            apply(color, null, true);
        });
        mxEvent.consume(evt);
    }));

    btn.style.position = 'absolute';
    btn.style.marginTop = '-4px';
    btn.style.right = (mxClient.IS_QUIRKS) ? '0px' : '20px';
    btn.style.height = '22px';
    btn.className = 'geColorBtn';
    btn.style.display = (cb.checked || hideCheckbox) ? '' : 'none';
    div.appendChild(btn);

    mxEvent.addListener(div, 'click', function (evt) {
        var source = mxEvent.getSource(evt);

        if (source == cb || source.nodeName != 'INPUT') {
            // Toggles checkbox state for click on label
            if (source != cb) {
                cb.checked = !cb.checked;
            }

            // Overrides default value with current value to make it easier
            // to restore previous value if the checkbox is clicked twice
            if (!cb.checked && value != null && value != mxConstants.NONE &&
                defaultColor != mxConstants.NONE) {
                defaultColor = value;
            }

            apply((cb.checked) ? defaultColor : mxConstants.NONE);
        }
    });

    apply(value, true);

    if (listener != null) {
        listener.install(apply);
        this.listeners.push(listener);
    }

    return div;
};

/**
 *
 */
BaseFormatPanel.prototype.createCellColorOption = function (label, colorKey, defaultColor, callbackFn, setStyleFn) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    return this.createColorOption(label, function () {
        // Seems to be null sometimes, not sure why...
        var state = graph.view.getState(graph.getSelectionCell());

        if (state != null) {
            return mxUtils.getValue(state.style, colorKey, null);
        }

        return null;
    }, function (color) {

        graph.getModel().beginUpdate();
        try {
            if (setStyleFn != null) {
                setStyleFn(color);
            }
            graph.setCellStyles(colorKey, color, graph.getSelectionCells());
            ui.fireEvent(new mxEventObject('styleChanged', 'keys', [colorKey],
                'values', [color], 'cells', graph.getSelectionCells()));
        } finally {
            graph.getModel().endUpdate();
        }
    }, defaultColor || mxConstants.NONE, {
        install: function (apply) {
            this.listener = function () {
                // Seems to be null sometimes, not sure why...
                var state = graph.view.getState(graph.getSelectionCell());

                if (state != null) {
                    apply(mxUtils.getValue(state.style, colorKey, null));
                }
            };

            graph.getModel().addListener(mxEvent.CHANGE, this.listener);
        },
        destroy: function () {
            graph.getModel().removeListener(this.listener);
        }
    }, callbackFn);
};

/**
 *
 */
BaseFormatPanel.prototype.addArrow = function (elt, height) {
    height = (height != null) ? height : 10;

    var arrow = document.createElement('div');
    arrow.style.display = (mxClient.IS_QUIRKS) ? 'inline' : 'inline-block';
    arrow.style.padding = '6px';
    arrow.style.paddingRight = '4px';

    var m = (10 - height);

    if (m == 2) {
        arrow.style.paddingTop = 6 + 'px';
    } else if (m > 0) {
        arrow.style.paddingTop = (6 - m) + 'px';
    } else {
        arrow.style.marginTop = '-2px';
    }

    arrow.style.height = height + 'px';
    arrow.style.borderLeft = '1px solid #a0a0a0';
    arrow.innerHTML = '<img border="0" src="' + ((mxClient.IS_SVG) ? 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAHBJREFUeNpidHB2ZyAGsACxDRBPIKCuA6TwCBB/h2rABu4A8SYmKCcXiP/iUFgAxL9gCi8A8SwsirZCMQMTkmANEH9E4v+CmsaArvAdyNFI/FlQ92EoBIE+qCRIUz168DBgsU4OqhinQpgHMABAgAEALY4XLIsJ20oAAAAASUVORK5CYII=' :
        IMAGE_PATH + '/dropdown.png') + '" style="margin-bottom:4px;">';
    mxUtils.setOpacity(arrow, 70);

    var symbol = elt.getElementsByTagName('div')[0];

    if (symbol != null) {
        symbol.style.paddingRight = '6px';
        symbol.style.marginLeft = '4px';
        symbol.style.marginTop = '-1px';
        symbol.style.display = (mxClient.IS_QUIRKS) ? 'inline' : 'inline-block';
        mxUtils.setOpacity(symbol, 60);
    }

    mxUtils.setOpacity(elt, 100);
    elt.style.border = '1px solid #a0a0a0';
    elt.style.backgroundColor = this.buttonBackgroundColor;
    elt.style.backgroundImage = 'none';
    elt.style.width = 'auto';
    elt.className += ' geColorBtn';
    mxUtils.setPrefixedStyle(elt.style, 'borderRadius', '3px');

    elt.appendChild(arrow);

    return symbol;
};

/**
 *
 */
BaseFormatPanel.prototype.addUnitInput = function (container, unit, right, width, update, step, marginTop, disableFocus) { // 大小input
    marginTop = (marginTop != null) ? marginTop : 0;

    var input = document.createElement('input');
    input.style.position = 'absolute';
    input.style.textAlign = 'center';
    input.style.marginTop = '-2px';
    input.style.right = (right + 12) + 'px';
    input.style.width = width + 'px';
    container.appendChild(input);

    var stepper = this.createStepper(input, update, step, null, disableFocus);
    stepper.style.marginTop = (marginTop - 2) + 'px';
    stepper.style.right = right + 'px';
    container.appendChild(stepper);

    return input;
};
BaseFormatPanel.prototype.addUnitInput_personalAngle = function (container, unit, right, width, update, step, marginTop, disableFocus) { // 角度input
    marginTop = (marginTop != null) ? marginTop : 0;

    var input = document.createElement('input');
    input.style.position = 'absolute';
    input.style.textAlign = 'center';
    input.style.marginTop = '-2px';
    input.style.borderRadius = '3px 0 0 3px';
    input.style.height = '22px';
    input.style.right = 77 + 'px';
    input.style.width = width + 'px';
    container.appendChild(input);

    var stepper = this.createStepper_personal(input, update, step, null, disableFocus);
    stepper.style.marginTop = (marginTop - 2) + 'px';
    stepper.style.right = right + 'px';
    container.appendChild(stepper);

    return input;
};

BaseFormatPanel.prototype.addUnitInput_personalWidth = function (container, unit, right, width, update, step, marginTop, disableFocus) { // 大小input
    marginTop = (marginTop != null) ? marginTop : 0;

    var input = document.createElement('input');
    input.style.position = 'absolute';
    input.style.textAlign = 'center';
    input.style.marginTop = '0px';
    input.style.right = (right + 12) + 'px';
    input.style.width = width + 'px';
    input.style.height = 22 + 'px';
    input.style.borderRadius = '3px 0 0 3px';
    container.appendChild(input);

    var stepper = this.createStepper_personal(input, update, step, null, disableFocus);
    stepper.style.marginTop = (marginTop - 2) + 'px';
    stepper.style.right = 47 + 'px';
    container.appendChild(stepper);
    stepper.style.display = "block"
    var span = document.createElement('span');
    span.innerText = '宽'
    span.style.position = 'absolute';
    span.style.right = '26px'
    span.style.top = '15px'
    container.appendChild(span);


    return input;
};
BaseFormatPanel.prototype.addUnitInput_personalHeight = function (container, unit, right, width, update, step, marginTop, disableFocus) { // 大小input
    marginTop = (marginTop != null) ? marginTop : 0;

    var input = document.createElement('input');
    input.style.position = 'absolute';
    input.style.textAlign = 'center';
    input.style.marginTop = '-2px';
    input.style.borderRadius = '3px 0 0 3px';
    input.style.right = (right + 12) + 'px';
    input.style.width = width + 'px';
    input.style.height = 22 + 'px';
    container.appendChild(input);

    var stepper = this.createStepper_personal(input, update, step, null, disableFocus);
    stepper.style.marginTop = (marginTop - 2) + 'px';
    stepper.style.right = 47 + 'px';
    stepper.style.top = 50 + 'px';
    container.appendChild(stepper);
    stepper.style.display = "block"

    var span = document.createElement('span');
    span.innerText = '高'
    span.style.position = 'absolute';
    span.style.right = '26px'
    span.style.top = '53px'
    container.appendChild(span);

    return input;
};

/**
 *
 */
BaseFormatPanel.prototype.createRelativeOption = function (label, key, width, handler, init) {
    width = (width != null) ? width : 44;

    var graph = this.editorUi.editor.graph;
    var div = this.createPanel();
    div.style.paddingTop = '10px';
    div.style.paddingBottom = '10px';
    mxUtils.write(div, label);
    div.style.fontWeight = 'bold';

    var update = mxUtils.bind(this, function (evt) {
        if (handler != null) {
            handler(input);
        } else {
            var value = parseInt(input.value);
            value = Math.min(100, Math.max(0, (isNaN(value)) ? 100 : value));
            var state = graph.view.getState(graph.getSelectionCell());

            if (state != null && value != mxUtils.getValue(state.style, key, 100)) {
                // Removes entry in style (assumes 100 is default for relative values)
                if (value == 100) {
                    value = null;
                }

                graph.setCellStyles(key, value, graph.getSelectionCells());
                this.editorUi.fireEvent(new mxEventObject('styleChanged', 'keys', [key],
                    'values', [value], 'cells', graph.getSelectionCells()));
            }

            input.value = ((value != null) ? value : '100') + ' %';
        }

        mxEvent.consume(evt);
    });

    var input = this.addUnitInput(div, '%', 20, width, update, 10, -15, handler != null);

    if (key != null) {
        var listener = mxUtils.bind(this, function (sender, evt, force) {
            if (force || input != document.activeElement) {
                var ss = this.format.getSelectionState();
                var tmp = parseInt(mxUtils.getValue(ss.style, key, 100));
                input.value = (isNaN(tmp)) ? '' : tmp + ' %';
            }
        });

        mxEvent.addListener(input, 'keydown', function (e) {
            if (e.keyCode == 13) {
                graph.container.focus();
                mxEvent.consume(e);
            } else if (e.keyCode == 27) {
                listener(null, null, true);
                graph.container.focus();
                mxEvent.consume(e);
            }
        });

        graph.getModel().addListener(mxEvent.CHANGE, listener);
        this.listeners.push({
            destroy: function () {
                graph.getModel().removeListener(listener);
            }
        });
        listener();
    }

    mxEvent.addListener(input, 'blur', update);
    mxEvent.addListener(input, 'change', update);

    if (init != null) {
        init(input);
    }

    return div;
};

/**
 *
 */
BaseFormatPanel.prototype.addLabel = function (div, title, right, width) {
    width = (width != null) ? width : 61;

    var label = document.createElement('div');
    mxUtils.write(label, title);
    label.style.position = 'absolute';
    label.style.right = right + 'px';
    label.style.width = width + 'px';
    label.style.marginTop = '16px';
    label.style.textAlign = 'center';
    div.appendChild(label);
};

/**
 *
 */
BaseFormatPanel.prototype.addKeyHandler = function (input, listener) {
    mxEvent.addListener(input, 'keydown', mxUtils.bind(this, function (e) {
        if (e.keyCode == 13) {
            this.editorUi.editor.graph.container.focus();
            mxEvent.consume(e);
        } else if (e.keyCode == 27) {
            if (listener != null) {
                listener(null, null, true);
            }

            this.editorUi.editor.graph.container.focus();
            mxEvent.consume(e);
        }
    }));
};

/**
 *
 */
BaseFormatPanel.prototype.styleButtons = function (elts) {
    for (var i = 0; i < elts.length; i++) {
        mxUtils.setPrefixedStyle(elts[i].style, 'borderRadius', '3px');
        mxUtils.setOpacity(elts[i], 100);
        elts[i].style.border = '1px solid #a0a0a0';
        elts[i].style.padding = '4px';
        elts[i].style.paddingTop = '3px';
        elts[i].style.paddingRight = '1px';
        elts[i].style.margin = '1px';
        elts[i].style.width = '24px';
        elts[i].style.height = '20px';
        elts[i].className += ' geColorBtn';
    }
};

/**
 * Adds the label menu items to the given menu and parent.
 */
BaseFormatPanel.prototype.destroy = function () {
    if (this.listeners != null) {
        for (var i = 0; i < this.listeners.length; i++) {
            this.listeners[i].destroy();
        }

        this.listeners = null;
    }
};

// 公共的styleLabel方法
styleFormatPanelLabels = function (format, editorUi, container) {
    BaseFormatPanel.call(this, format, editorUi, container);
    this.init()
};

mxUtils.extend(styleFormatPanelLabels, BaseFormatPanel);


styleFormatPanelLabels.prototype.init = function () {
    var ui = this.editorUi
    var graph = this.editorUi.editor.graph;
    var ss = this.format.getSelectionState();
    // console.log(ss);
    switch (ss.style.type) {
        case 'jintaitupian':
            this.container.appendChild(this.staticPicture())
            break
        case 'dongtu':
            this.container.appendChild(this.staticPicture())
            break
    }
}


// 静态图片的方法
styleFormatPanelLabels.prototype.staticPicture = function () {
    var ui = this.editorUi
    var graph = this.editorUi.editor.graph;
    var ss = this.format.getSelectionState();

    var baseDiv = document.createElement('div')
    baseDiv.style.padding = '12px 0 0 10px'
    baseDiv.appendChild(this.importPicture())
    baseDiv.appendChild(this.transparency(15))
    return baseDiv
}
// 公共的导图静态图片的方法
styleFormatPanelLabels.prototype.importPicture = function (martop) {
    var ui = this.editorUi
    var graph = this.editorUi.editor.graph;
    var ss = this.format.getSelectionState();

    var fileDiv = document.createElement('div')

    fileDiv.style.marginTop = martop ? martop + 'px' : ''

    // 显示input
    var fileInputText = document.createElement('input')
    fileInputText.style.height = '24px'
    fileInputText.className = 'eleInput'
    fileInputText.style.width = '88px'
    fileInputText.value = ss.style.cellImageName || ''
    fileInputText.style.borderRadius = '4px 0 0 4px'
    fileInputText.style.padding = '0'
    fileInputText.setAttribute('readonly', true)

    // label
    var labelP = document.createElement('p')
    labelP.innerText = '图片'
    labelP.style.width = '55px'
    labelP.style.height = '24px'
    labelP.style.margin = '0'
    labelP.style.lineHeight = '24px'
    labelP.style.display = 'inline-block'
    labelP.style.verticalAlign = 'middle';
    // 导入按钮
    var fileButton = document.createElement('button')
    fileButton.className = 'eleButton'
    fileButton.style.height = '24px'
    fileButton.style.position = 'relative'
    fileButton.style.top = '0px'
    fileButton.style.left = '-2px'
    fileButton.style.borderRadius = '0 4px 4px 0'
    fileButton.style.lineHeight = '14px'
    fileButton.innerText = '导入'
    // 删除按钮
    var fileButton2 = document.createElement('button')
    fileButton2.style.position = 'relative'
    fileButton2.style.height = '24px'
    fileButton2.style.width = '24px'
    fileButton2.style.left = '-1px'
    fileButton2.style.borderRadius = '0px'
    fileButton2.style.borderLeft = '0px'
    if (ss.style.cellImageName) {
        fileButton2.className = 'deleteBtn'
    }
    // 监听导入事件
    fileButton.addEventListener('click', function (e) {
        // 转发事件
        fileImage.click()
    })
    // 监听删除事件
    fileButton2.addEventListener('click', function (e) {
        graph.getModel().beginUpdate();
        try {
            graph.setCellStyles('image', 'stencils/editor/base/jintaitupian_base.svg', graph.getSelectionCells());
            graph.setCellStyles('cellImageName', '', graph.getSelectionCells());
        } finally {
            graph.getModel().endUpdate();
        }
        fileButton2.classList.remove('deleteBtn')
    })
    // 上传方法
    var fileImage = document.createElement('input')
    fileImage.id = 'file'
    fileImage.setAttribute('type', 'file')
    fileImage.setAttribute('title', '')
    fileImage.style.width = '100%'
    fileImage.style.display = 'none'
    fileImage.addEventListener('change', function (e) {
        var x = new FileReader;
        fileInputText.value = e.target.files[0].name
        if (e.target.files[0].name) {
            fileButton2.className = 'deleteBtn'
        }
        x.readAsDataURL(e.target.files[0])
        x.onloadend = function (ev) {
            graph.getModel().beginUpdate();
            try {
                graph.setCellStyles('image', x.result.replace(';', ''), graph.getSelectionCells());
                graph.setCellStyles('cellImageName', e.target.files[0].name, graph.getSelectionCells());
                fileImage.value = ''
            } finally {
                graph.getModel().endUpdate();
            }
        }
    })
    fileDiv.appendChild(labelP)
    fileDiv.appendChild(fileInputText)
    fileDiv.appendChild(fileButton2)
    fileDiv.appendChild(fileButton)
    fileDiv.appendChild(fileImage)
    // label提示信息
    var bgWarningTitle = document.createElement('p')
    bgWarningTitle.innerText = '图片建议不大于500K !'
    bgWarningTitle.style.margin = '8px 0 0 0 '
    bgWarningTitle.style.color = '#bbbbbb'
    bgWarningTitle.className = 'bgWarningTitle'
    bgWarningTitle.style.paddingLeft = '65px'
    fileDiv.appendChild(bgWarningTitle)

    return fileDiv
}
// 公共的透明度的方法
styleFormatPanelLabels.prototype.transparency = function (martop) {
    var opacityPanel = this._createInputOption(mxResources.get('opacity'), mxConstants.STYLE_OPACITY, 60);
    opacityPanel.style.marginTop = martop ? martop + 'px' : ''
    return opacityPanel
}
// 公共的创建input方法
styleFormatPanelLabels.prototype._privateInput = function (container, width) {

    var input = document.createElement('input');
    input.style.width = width + 'px'
    input.style.height = 24 + 'px'
    input.className = 'eleInput';
    container.appendChild(input);

    return input;
};
// 公共的创建input选择框方法
styleFormatPanelLabels.prototype._createInputOption = function (label, key, width, handler, init) {
    width = (width != null) ? width : 50;

    var graph = this.editorUi.editor.graph;
    var div = document.createElement('div')

    // label
    var labelP = document.createElement('p')
    labelP.innerText = label
    labelP.style.width = '55px'
    labelP.style.height = '24px'
    labelP.style.margin = '0'
    labelP.style.lineHeight = '24px'
    labelP.style.display = 'inline-block'
    labelP.style.verticalAlign = 'middle';
    div.appendChild(labelP)

    var update = mxUtils.bind(this, function (evt) {
        if (handler != null) {
            handler(input);
        } else {
            var value = parseInt(input.value);
            value = Math.min(100, Math.max(0, (isNaN(value)) ? 100 : value));
            var state = graph.view.getState(graph.getSelectionCell());

            if (state != null && value != mxUtils.getValue(state.style, key, 100)) {
                // Removes entry in style (assumes 100 is default for relative values)
                if (value == 100) {
                    value = null;
                }

                graph.setCellStyles(key, value, graph.getSelectionCells());
                this.editorUi.fireEvent(new mxEventObject('styleChanged', 'keys', [key],
                    'values', [value], 'cells', graph.getSelectionCells()));
            }

            input.value = ((value != null) ? value : '100') + ' %';
        }

        mxEvent.consume(evt);
    });

    var input = this._privateInput(div, width);

    if (key != null) {
        var listener = mxUtils.bind(this, function (sender, evt, force) {
            if (force || input != document.activeElement) {
                var ss = this.format.getSelectionState();
                var tmp = parseInt(mxUtils.getValue(ss.style, key, 100));
                input.value = (isNaN(tmp)) ? '' : tmp + ' %';
            }
        });

        mxEvent.addListener(input, 'keydown', function (e) {
            if (e.keyCode == 13) {
                graph.container.focus();
                mxEvent.consume(e);
            } else if (e.keyCode == 27) {
                listener(null, null, true);
                graph.container.focus();
                mxEvent.consume(e);
            }
        });

        graph.getModel().addListener(mxEvent.CHANGE, listener);
        this.listeners.push({
            destroy: function () {
                graph.getModel().removeListener(listener);
            }
        });
        listener();
    }

    mxEvent.addListener(input, 'blur', update);
    mxEvent.addListener(input, 'change', update);

    if (init != null) {
        init(input);
    }

    return div;
};
/**
 * Adds the label menu items to the given menu and parent.
 */
ArrangePanel = function (format, editorUi, container, type) {
    BaseFormatPanel.call(this, format, editorUi, container);
    if (type) {
        this.init();
    } else {
        this.init1();
    }

};

mxUtils.extend(ArrangePanel, BaseFormatPanel);

//临时方法
ArrangePanel.prototype.init1 = function () {
    var ui = this.editorUi
    var graph = this.editorUi.editor.graph;
    var ss = this.format.getSelectionState();
    // this.container.appendChild(this.addLayerOps(this.createPanel())); // 隐藏移至最前 移至最后
    // Special case that adds two panels
    // this.addGeometry(this.container);
    // this.addEdgeGeometry(this.container);
    var addGeometryDiv = document.createElement('div')
    // var addEdgeGeometryDiv = document.createElement('div')

    this.container.appendChild(this.addGeometry(addGeometryDiv));
    // this.container.appendChild(this.addEdgeGeometry(addEdgeGeometryDiv));

    if (ss.style.shape == 'connector') {
        return false
    } else {
        if (!ss.containsLabel || ss.edges.length == 0) {
            this.container.appendChild(this.addAngle(this.createPanel()));
        }
    }


    // if (!ss.containsLabel && ss.edges.length == 0) {
    //   this.container.appendChild(this.addFlip(this.createPanel()));  // 隐藏翻转(水平翻转,垂直翻转)
    // }

    if (ss.vertices.length > 1) {
        // this.container.appendChild(this.addAlign(this.createPanel())); // 隐藏对齐
        // this.container.appendChild(this.addDistribute(this.createPanel())); // 隐藏等级分布
    }

    this.container.appendChild(this.addGroupOps(this.createPanel()));
};
/**
 * Adds the label menu items to the given menu and parent.
 */



ArrangePanel.prototype.init = function () {

    var ui = this.editorUi
    var graph = this.editorUi.editor.graph;
    var ss = this.format.getSelectionState();
    this.container.appendChild(this.mutualPanel());
    this.container.appendChild(this.mutualPanel(0));
    this.container.appendChild(this.mutualPanel(1));
    this.container.appendChild(this.mutualPanel(2));

    this.container.appendChild(this.mutualPanelSwich())
};
ArrangePanel.prototype.mutualPanel = function (actionType) {
    var ui = this.editorUi
    var graph = this.editorUi.editor.graph;
    var ss = this.format.getSelectionState();

    var sect = graph.getSelectionCells()
    var actionTypeTitle
    var mutualDiv = document.createElement('div')
    mutualDiv.className = 'panel_Div'
    mutualDiv.style.marginLeft = '5px'

    if (actionType === undefined) {
        // 1.0 事件
        var oneDiv = document.createElement('div')
        oneDiv.style.marginTop = '5px'
        var tableDivLeft1 = document.createElement('div')
        var tableDivRight1 = document.createElement('div')
        tableDivLeft1.className = 'tableDivLeft'
        tableDivRight1.className = 'tableDivRight'
        var tableTitle1 = document.createElement('p')
        tableTitle1.innerText = "动作"
        tableDivLeft1.appendChild(tableTitle1)
        tableDivRight1.innerText = "事件"
        tableDivRight1.style.lineHeight = "24px"
        oneDiv.appendChild(tableDivLeft1)
        oneDiv.appendChild(tableDivRight1)
        mutualDiv.appendChild(oneDiv)
    } else {
        // 1.1.0 鼠标单击
        var actionChange = 'actionChange' + actionType
        var actionTypeTi = 'actionType' + actionType
        switch (actionType) {
            case 0:
                actionTypeTitle = '鼠标单击'
                break
            case 1:
                actionTypeTitle = '鼠标双击'
                break
            case 2:
                actionTypeTitle = '鼠标移动'
                break
        }
        var twoDiv = document.createElement('div')
        var tableDivLeft2 = document.createElement('div')
        var tableDivRight2 = document.createElement('div')
        tableDivLeft2.className = 'tableDivLeft tableDivTwo'
        tableDivRight2.className = 'tableDivRight tableDivTwo'
        var tableTitle3 = document.createElement('p')
        tableTitle3.innerText = actionTypeTitle
        tableDivLeft2.appendChild(tableTitle3)
        twoDiv.appendChild(tableDivLeft2)
        twoDiv.appendChild(tableDivRight2)
        mutualDiv.appendChild(twoDiv)
        // 1.1.1 添加选择框
        var actionSelect = document.createElement('select')
        actionSelect.name = "eventName" + actionType
        actionSelect.className = "pictureTypeSelest"
        actionSelect.style.margin = "0"
        // 1.1.2 设置option参数列表
        var actionOpationData = ['删除绑定', '组态页面切换', '读取文件']
        // 1.1.3 添加 option
        for (var i = 0; i < actionOpationData.length; i++) {
            var actionOpation = document.createElement('option')
            actionOpation.value = i
            actionOpation.innerText = actionOpationData[i]

            if (sect.length === 1 && ss.style[actionTypeTi] === actionType && ss.style[actionChange] === i) {
                //  eventSelect.style.cursor = 'default'
                actionOpation.selected = true
            }
            actionSelect.appendChild(actionOpation)
        }
        tableDivRight2.appendChild(actionSelect)
        var _this = this

        // 重置选择栏参数
        actionSelect.onmousedown = function () {
            this.sindex = this.selectedIndex;
            this.selectedIndex = -1
        }
        actionSelect.onmouseout = function () {
            var index = actionSelect.selectedIndex;
            if (index == -1) {
                this.selectedIndex = this.sindex
            }
        }
        // 1.1.4 添加select事件
        actionSelect.addEventListener('change', function (e) {
            if (sect[0].edge || sect.length >= 2) {
                return false
            } else {
                // change事件发生时主动失去焦点
                this.blur()
                graph.getModel().beginUpdate();
                try {
                    // 将当前动作存到session里
                    sessionStorage.setItem('switchType', actionType)
                    // 设置当前字段名
                    var viewNameTi = 'viewName' + actionType
                    var viewIdTi = 'viewId' + actionType
                    var pictureTypeTi = 'pictureType' + actionType
                    var uploadFileNameTi = 'uploadFileName' + actionType
                    var uploadFileUrlTi = 'uploadFileUrl' + actionType
                    // 根据当前字段名解除绑定
                    if (e.target.value == 0) {
                        graph.setCellStyles(uploadFileNameTi, '', graph.getSelectionCells());
                        graph.setCellStyles(uploadFileUrlTi, '', graph.getSelectionCells());
                        graph.setCellStyles(viewIdTi, '', graph.getSelectionCells());
                        graph.setCellStyles(viewNameTi, '', graph.getSelectionCells());
                        graph.setCellStyles(pictureTypeTi, '', graph.getSelectionCells());
                        graph.setCellStyles('actionType' + actionType, '', graph.getSelectionCells());
                        graph.setCellStyles('actionChange' + actionType, '', graph.getSelectionCells());
                    } else if (e.target.value == 1) {
                        graph.setCellStyles(uploadFileNameTi, '', graph.getSelectionCells());
                        graph.setCellStyles(uploadFileUrlTi, '', graph.getSelectionCells());
                        graph.setCellStyles('actionType' + actionType, actionType, graph.getSelectionCells());
                        graph.setCellStyles('actionChange' + actionType, e.target.value, graph.getSelectionCells());
                        _this.format.refresh()
                    } else if (e.target.value == 2) {
                        graph.setCellStyles(viewIdTi, '', graph.getSelectionCells());
                        graph.setCellStyles(viewNameTi, '', graph.getSelectionCells());
                        graph.setCellStyles(pictureTypeTi, '', graph.getSelectionCells());
                        graph.setCellStyles('actionType' + actionType, actionType, graph.getSelectionCells());
                        graph.setCellStyles('actionChange' + actionType, e.target.value, graph.getSelectionCells());
                        _this.format.refresh()
                    }
                } finally {
                    graph.getModel().endUpdate();
                }
            }
        })
    }
    return mutualDiv

}
ArrangePanel.prototype.mutualPanelSwich = function () {

    var ui = this.editorUi
    var ss = this.format.getSelectionState();
    var graph = ui.editor.graph;
    var sect = graph.getSelectionCells()
    // 读取当前动作session值
    var sessionSwitchType = sessionStorage.getItem('switchType') || 0
    // 方法 ＋ 动作
    var actionChange = 'actionChange' + sessionSwitchType
    var actionType = 'actionType' + sessionSwitchType
    // 读取input字段名
    var viewNameTi = 'viewName' + sessionSwitchType
    var uploadFileNameTi = 'uploadFileName' + sessionSwitchType


    var div = document.createElement("div")
    div.className = 'panel_Div'
    div.style.padding = '0';
    div.style.borderBottom = 'none';

    var testDiv = document.createElement("div")
    if (sessionSwitchType == '0') {
        testDiv.innerText = "鼠标单击"
    } else if (sessionSwitchType == '1') {
        testDiv.innerText = "鼠标双击"
    } else if (sessionSwitchType == '2') {
        testDiv.innerText = "鼠标移动"
    }
    testDiv.style.width = "240px"
    testDiv.style.height = "24px"
    testDiv.style.lineHeight = "24px"
    testDiv.style.marginTop = "10px"
    testDiv.style.fontSize = "12px"
    testDiv.style.paddingLeft = "10px"

    var switchTypeDiv = document.createElement("div")
    var switchTypeDiv2 = document.createElement("div")
    var switchTypeText = document.createTextNode("切换到")
    var switchTypep = document.createElement("p")
    // 创建实例单选按钮
    var switchTypeRadioShow1 = document.createElement('p')
    var switchTypeRadioShow2 = document.createElement('p')

    //设置节点
    var switchTypeRadioType1 = document.createElement('input')
    var switchTypeRadioType1Text = document.createElement('p')
    var switchTypeRadioType2 = document.createElement('input')
    var switchTypeRadioType2Text = document.createElement('p')
    switchTypeRadioType1Text.innerText = '监控对象'
    switchTypeRadioType2Text.innerText = '指定文件'
    switchTypeRadioType1.setAttribute('type', 'radio')
    switchTypeRadioType2.setAttribute('type', 'radio')
    switchTypeRadioType1.name = 'pageType' + sessionSwitchType
    switchTypeRadioType2.name = 'pageType' + sessionSwitchType
    switchTypeRadioType1.value = 0
    switchTypeRadioType2.value = 1
    //设置状态
    switchTypeRadioType1Text.style.marginRight = '25px'
    switchTypep.style.marginRight = '12px'
    switchTypeRadioType1.style.display = 'none'
    switchTypeRadioType2.style.display = 'none'

    // 装载节点
    switchTypep.appendChild(switchTypeText)
    switchTypeDiv.appendChild(switchTypep)
    switchTypeDiv.appendChild(switchTypeRadioType1)
    switchTypeDiv.appendChild(switchTypeRadioShow1)
    switchTypeDiv.appendChild(switchTypeRadioType1Text)
    switchTypeDiv2.appendChild(switchTypeRadioType2)
    switchTypeDiv2.appendChild(switchTypeRadioShow2)
    switchTypeDiv2.appendChild(switchTypeRadioType2Text)

    switchTypeDiv.classList = 'switchTypeDiv'
    switchTypeDiv2.classList = 'switchTypeDiv2'
    switchTypep.classList = 'switchTypeP'
    switchTypeRadioType1Text.classList = 'switchTypeP'
    switchTypeRadioType2Text.classList = 'switchTypeP'

    switchTypeRadioShow1.className = 'radioUnChecked'
    switchTypeRadioShow2.className = 'radioUnChecked'


    // 监控对象
    var a8 = document.createElement("div")
    var input8 = document.createElement("input")
    var button8 = document.createElement("button")
    a8.appendChild(input8)
    a8.appendChild(button8)
    input8.style.marginLeft = "70px"
    input8.style.width = "100px"
    input8.classList.add('eleInput')
    input8.classList.add('node-bind-view')
    input8.style.height = "25px"
    input8.style.borderRadius = "4px 0 0 4px"
    input8.setAttribute("readonly", "true")
    button8.className = "eleButton"
    button8.style.width = "56px"
    button8.style.marginLeft = "-1px"
    button8.style.padding = "0 10px"
    button8.innerText = "选择"
    button8.classList.add('monitor-button8')
    button8.style.fontSize = "10px"
    button8.style.borderRadius = "0 4px 4px 0"

    ss.style[viewNameTi] ? input8.value = ss.style[viewNameTi] : ss.style.viewName0 || null


    button8.addEventListener('click', function (e) {
        if (sect[0].edge || sect.length >= 2) {
            return false
        } else {
            // 节点绑定画布
            window.parent.postMessage({
                cmd: 'showNodeViewBind',
                params: {
                    success: true,
                    datas: ss.style
                }
            }, '*')
        }
    })

    // 导入文件
    var a9 = document.createElement("div")
    var input9 = document.createElement("input")
    var button9 = document.createElement("button")
    a9.appendChild(input9)
    a9.appendChild(button9)
    ss.style[uploadFileNameTi] ? input9.value = ss.style[uploadFileNameTi] : ss.style.uploadFileName0 || null
    input9.style.marginLeft = "70px"
    input9.style.width = "100px"
    input9.classList.add('eleInput')
    input9.style.height = "25px"
    input9.style.borderRadius = "4px 0 0 4px"
    input9.classList.add('nodeBindUploadFile')
    input9.setAttribute("readonly", "true")
    button9.className = "eleButton"
    button9.classList.add('monitor-button9')

    button9.style.width = "56px"
    button9.style.marginLeft = "-1px"
    button9.style.padding = "0 10px"
    button9.innerText = "导入"
    button9.style.fontSize = "10px"
    button9.style.borderRadius = "0 4px 4px 0"
    button9.addEventListener('click', function (e) {
        if (sect[0].edge || sect.length >= 2) {
            return false
        } else {
            // 上传文件
            window.parent.postMessage({
                cmd: 'uploadFiles',
                params: {
                    success: true
                }
            }, '*')
        }
    })

    // 监听show实列事件
    switchTypeRadioShow1.addEventListener('click', function (e) {
        if (sect[0].edge || sect.length >= 2 || ss.style[actionChange] != 1) {
            return false
        } else {
            graph.getModel().beginUpdate();
            try {
                switchTypeRadioShow1.className = 'radioChecked'
                switchTypeRadioShow2.className = 'radioUnChecked'
                input9.value = ''
            } finally {
                graph.getModel().endUpdate();
            }
        }
    })
    switchTypeRadioShow2.addEventListener('click', function (e) {
        if (sect[0].edge || sect.length >= 2 || ss.style[actionChange] != 2) {
            return false
        } else {
            graph.getModel().beginUpdate();
            try {
                switchTypeRadioShow1.className = 'radioUnChecked'
                switchTypeRadioShow2.className = 'radioChecked'
                input8.value = ''
            } finally {
                graph.getModel().endUpdate();
            }
        }
    })

    // 如果当前方法为切换页面则不可以点击选择文件
    if (ss && sect.length === 1 && ss.style[actionChange] == 1) {

        switchTypeRadioShow1.click()
        button8.removeAttribute("disabled")
        button9.setAttribute("disabled", true)
        button8.style.cursor = 'pointer'
        button9.style.cursor = 'no-drop'
        switchTypeRadioShow1.style.cursor = 'pointer'
        switchTypeRadioShow2.style.cursor = 'no-drop'
    } // 如果当前方法为选择文件则不可以点击切换页面
    else if (ss && sect.length === 1 && ss.style[actionChange] == 2) {

        switchTypeRadioShow2.click()
        button8.setAttribute("disabled", true)
        button9.removeAttribute("disabled")
        button8.style.cursor = 'no-drop'
        button9.style.cursor = 'pointer'
        switchTypeRadioShow1.style.cursor = 'no-drop'
        switchTypeRadioShow2.style.cursor = 'pointer'
    } // 如果当前方法为删除绑定则两个都不可以点击
    else if (sect[0].edge || sect.length >= 2 || ss.style[actionChange] == undefined) {
        switchTypeRadioShow1.style.cursor = 'no-drop'
        switchTypeRadioShow2.style.cursor = 'no-drop'
        switchTypeRadioShow1.className = 'radioUnChecked'
        switchTypeRadioShow2.className = 'radioUnChecked'
        button8.setAttribute("disabled", true)
        button9.setAttribute("disabled", true)
        button8.style.cursor = 'no-drop'
        button9.style.cursor = 'no-drop'
    }
    //  else {
    //   switchTypeRadioShow1.className = 'radioChecked' // 默认选中
    //   switchTypeRadioShow1.click()
    //   button9.setAttribute("disabled", true)
    //   button9.style.cursor = 'no-drop'
    // }

    div.appendChild(testDiv)
    div.appendChild(switchTypeDiv)
    div.appendChild(a8)
    div.appendChild(switchTypeDiv2)
    div.appendChild(a9)

    return div
}
/**
 *
 */
ArrangePanel.prototype.addLayerOps = function (div) {
    var ui = this.editorUi;

    var btn = mxUtils.button(mxResources.get('toFront'), function (evt) {
        ui.actions.get('toFront').funct();
    })

    btn.setAttribute('title', mxResources.get('toFront') + ' (' + this.editorUi.actions.get('toFront').shortcut + ')');
    btn.style.width = '100px';
    btn.style.marginRight = '2px';
    div.appendChild(btn);

    var btn = mxUtils.button(mxResources.get('toBack'), function (evt) {
        ui.actions.get('toBack').funct();
    })

    btn.setAttribute('title', mxResources.get('toBack') + ' (' + this.editorUi.actions.get('toBack').shortcut + ')');
    btn.style.width = '100px';
    div.appendChild(btn);

    return div;
};

/**
 *
 */
ArrangePanel.prototype.addGroupOps = function (div) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var cell = graph.getSelectionCell();
    var ss = this.format.getSelectionState();
    var count = 0;
    var btn = null;

    div.style.paddingTop = '8px';
    div.style.paddingBottom = '6px';
    div.style.display = 'none';

    if (graph.getSelectionCount() > 1) {
        btn = mxUtils.button(mxResources.get('group'), function (evt) {
            ui.actions.get('group').funct();
        })

        btn.setAttribute('title', mxResources.get('group') + ' (' + this.editorUi.actions.get('group').shortcut + ')');
        btn.style.width = '202px';
        btn.style.marginBottom = '2px';
        div.appendChild(btn);
        count++;
    } else if (graph.getSelectionCount() == 1 && !graph.getModel().isEdge(cell) && !graph.isSwimlane(cell) &&
        graph.getModel().getChildCount(cell) > 0) {
        btn = mxUtils.button(mxResources.get('ungroup'), function (evt) {
            ui.actions.get('ungroup').funct();
        })

        btn.setAttribute('title', mxResources.get('ungroup') + ' (' +
            this.editorUi.actions.get('ungroup').shortcut + ')');
        btn.style.width = '202px';
        btn.style.marginBottom = '2px';
        div.appendChild(btn);
        count++;
    }

    if (ss.vertices.length > 0) {
        if (count > 0) {
            mxUtils.br(div);
            count = 0;
        }

        var btn = mxUtils.button(mxResources.get('copySize'), function (evt) {
            ui.actions.get('copySize').funct();
        });

        btn.setAttribute('title', mxResources.get('copySize') + ' (' +
            this.editorUi.actions.get('copySize').shortcut + ')');
        btn.style.width = '202px';
        btn.style.marginBottom = '2px';

        div.appendChild(btn);
        count++;

        if (ui.copiedSize != null) {
            var btn2 = mxUtils.button(mxResources.get('pasteSize'), function (evt) {
                ui.actions.get('pasteSize').funct();
            });

            btn2.setAttribute('title', mxResources.get('pasteSize') + ' (' +
                this.editorUi.actions.get('pasteSize').shortcut + ')');

            div.appendChild(btn2);
            count++;

            btn.style.width = '100px';
            btn.style.marginBottom = '2px';
            btn2.style.width = '100px';
            btn2.style.marginBottom = '2px';
        }
    }

    if (graph.getSelectionCount() == 1 && graph.getModel().isVertex(cell) &&
        graph.getModel().isVertex(graph.getModel().getParent(cell))) {
        if (count > 0) {
            mxUtils.br(div);
        }

        btn = mxUtils.button(mxResources.get('removeFromGroup'), function (evt) {
            ui.actions.get('removeFromGroup').funct();
        })

        btn.setAttribute('title', mxResources.get('removeFromGroup'));
        btn.style.width = '202px';
        btn.style.marginBottom = '2px';
        div.appendChild(btn);
        count++;
    } else if (graph.getSelectionCount() > 0) {
        if (count > 0) {
            mxUtils.br(div);
        }

        btn = mxUtils.button(mxResources.get('clearWaypoints'), mxUtils.bind(this, function (evt) {
            this.editorUi.actions.get('clearWaypoints').funct();
        }));

        btn.setAttribute('title', mxResources.get('clearWaypoints') + ' (' + this.editorUi.actions.get('clearWaypoints').shortcut + ')');
        btn.style.width = '202px';
        btn.style.marginBottom = '2px';
        div.appendChild(btn);

        count++;
    }

    if (graph.getSelectionCount() == 1) {
        if (count > 0) {
            mxUtils.br(div);
        }

        btn = mxUtils.button(mxResources.get('editData'), mxUtils.bind(this, function (evt) {
            this.editorUi.actions.get('editData').funct();
        }));

        btn.setAttribute('title', mxResources.get('editData') + ' (' + this.editorUi.actions.get('editData').shortcut + ')');
        btn.style.width = '100px';
        btn.style.marginBottom = '2px';
        div.appendChild(btn);
        count++;

        btn = mxUtils.button(mxResources.get('editLink'), mxUtils.bind(this, function (evt) {
            this.editorUi.actions.get('editLink').funct();
        }));

        btn.setAttribute('title', mxResources.get('editLink'));
        btn.style.width = '100px';
        btn.style.marginLeft = '2px';
        btn.style.marginBottom = '2px';
        div.appendChild(btn);
        count++;
    }

    if (count == 0) {
        div.style.display = 'none';
    }

    return div;
};

/**
 *
 */
ArrangePanel.prototype.addAlign = function (div) {
    var graph = this.editorUi.editor.graph;
    div.style.paddingTop = '6px';
    div.style.paddingBottom = '12px';
    div.appendChild(this.createTitle(mxResources.get('align')));

    var stylePanel = document.createElement('div');
    stylePanel.style.position = 'relative';
    stylePanel.style.paddingLeft = '0px';
    stylePanel.style.borderWidth = '0px';
    stylePanel.className = 'geToolbarContainer';

    if (mxClient.IS_QUIRKS) {
        div.style.height = '60px';
    }

    var left = this.editorUi.toolbar.addButton('geSprite-alignleft', mxResources.get('left'),
        function () {
            graph.alignCells(mxConstants.ALIGN_LEFT);
        }, stylePanel);
    var center = this.editorUi.toolbar.addButton('geSprite-aligncenter', mxResources.get('center'),
        function () {
            graph.alignCells(mxConstants.ALIGN_CENTER);
        }, stylePanel);
    var right = this.editorUi.toolbar.addButton('geSprite-alignright', mxResources.get('right'),
        function () {
            graph.alignCells(mxConstants.ALIGN_RIGHT);
        }, stylePanel);

    var top = this.editorUi.toolbar.addButton('geSprite-aligntop', mxResources.get('top'),
        function () {
            graph.alignCells(mxConstants.ALIGN_TOP);
        }, stylePanel);
    var middle = this.editorUi.toolbar.addButton('geSprite-alignmiddle', mxResources.get('middle'),
        function () {
            graph.alignCells(mxConstants.ALIGN_MIDDLE);
        }, stylePanel);
    var bottom = this.editorUi.toolbar.addButton('geSprite-alignbottom', mxResources.get('bottom'),
        function () {
            graph.alignCells(mxConstants.ALIGN_BOTTOM);
        }, stylePanel);

    this.styleButtons([left, center, right, top, middle, bottom]);
    right.style.marginRight = '6px';
    div.appendChild(stylePanel);

    return div;
};

/**
 *
 */
// ArrangePanel.prototype.addFlip = function (div) {  // 隐藏翻转
//   var ui = this.editorUi;
//   var editor = ui.editor;
//   var graph = editor.graph;
//   div.style.paddingTop = '6px';
//   div.style.paddingBottom = '10px';

//   var span = document.createElement('div');
//   span.style.marginTop = '2px';
//   span.style.marginBottom = '8px';
//   span.style.fontWeight = 'bold';
//   mxUtils.write(span, mxResources.get('flip'));
//   div.appendChild(span);

//   var btn = mxUtils.button(mxResources.get('horizontal'), function (evt) {
//     graph.toggleCellStyles(mxConstants.STYLE_FLIPH, false);
//   })

//   btn.setAttribute('title', mxResources.get('horizontal'));
//   btn.style.width = '100px';
//   btn.style.marginRight = '2px';
//   div.appendChild(btn);

//   var btn = mxUtils.button(mxResources.get('vertical'), function (evt) {
//     graph.toggleCellStyles(mxConstants.STYLE_FLIPV, false);
//   })

//   btn.setAttribute('title', mxResources.get('vertical'));
//   btn.style.width = '100px';
//   div.appendChild(btn);

//   return div;
// };

/**
 *
 */
ArrangePanel.prototype.addDistribute = function (div) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    div.style.paddingTop = '6px';
    div.style.paddingBottom = '12px';

    div.appendChild(this.createTitle(mxResources.get('distribute')));

    var btn = mxUtils.button(mxResources.get('horizontal'), function (evt) {
        graph.distributeCells(true);
    })

    btn.setAttribute('title', mxResources.get('horizontal'));
    btn.style.width = '100px';
    btn.style.marginRight = '2px';
    div.appendChild(btn);

    var btn = mxUtils.button(mxResources.get('vertical'), function (evt) {
        graph.distributeCells(false);
    })

    btn.setAttribute('title', mxResources.get('vertical'));
    btn.style.width = '100px';
    div.appendChild(btn);

    return div;
};

/**
 *
 */
ArrangePanel.prototype.addAngle = function (div) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();

    div.style.paddingBottom = '8px';

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.width = '70px';
    span.style.marginTop = '3px';
    span.style.fontWeight = 'bold';


    var input = null;
    var update = null;
    var btn = null;

    if (ss.edges.length == 0) {
        mxUtils.write(span, mxResources.get('angle'));
        div.appendChild(span);

        input = this.addUnitInput_personalAngle(div, '°', 26, 44, function () { // 角度
            update.apply(this, arguments);
        });

        mxUtils.br(div);
        div.style.paddingTop = '10px';
    } else {
        div.style.paddingTop = '8px';
    }

    if (!ss.containsLabel) {
        var label = mxResources.get('reverse');

        if (ss.vertices.length > 0 && ss.edges.length > 0) {
            label = mxResources.get('turn') + ' / ' + label;
        } else if (ss.vertices.length > 0) {
            label = mxResources.get('turn');
        }

        btn = mxUtils.button(label, function (evt) {
            ui.actions.get('turn').funct();
        })

        btn.setAttribute('title', label + ' (' + this.editorUi.actions.get('turn').shortcut + ')');
        btn.style.width = '202px';
        div.appendChild(btn);

        if (input != null) {
            btn.style.marginTop = '20px';
        }
    }

    if (input != null) {
        var listener = mxUtils.bind(this, function (sender, evt, force) {
            if (force || document.activeElement != input) {
                ss = this.format.getSelectionState();
                var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_ROTATION, 0));
                input.value = (isNaN(tmp)) ? '' : tmp + '°';
            }
        });

        update = this.installInputHandler(input, mxConstants.STYLE_ROTATION, 0, 0, 360, '°', null, true);
        this.addKeyHandler(input, listener);

        graph.getModel().addListener(mxEvent.CHANGE, listener);
        this.listeners.push({
            destroy: function () {
                graph.getModel().removeListener(listener);
            }
        });
        listener();
    }

    return div;
};

/**
 *
 */
ArrangePanel.prototype.addGeometry = function (container) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var rect = this.format.getSelectionState();

    var div = this.createPanel();
    div.style.paddingBottom = '8px';
    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.width = '50px';
    span.style.marginTop = '0px';
    span.style.fontWeight = 'bold';
    span.style.top = "16px"
    mxUtils.write(span, mxResources.get('size'));
    div.appendChild(span);
    div.style.height = "100px"
    div.style.position = "relative"


    var widthUpdate, heightUpdate, leftUpdate, topUpdate;
    var width = this.addUnitInput_personalWidth(div, 'pt', 84, 44, function () { // 宽 container, unit, right, width, update, step, marginTop, disableFocus)
        widthUpdate.apply(this, arguments);
    });
    width.className = 'widthDiv'
    var height = this.addUnitInput_personalHeight(div, 'pt', 84, 44, function () { // 高 container, unit, right, width, update, step, marginTop, disableFocus)
        heightUpdate.apply(this, arguments);
    });
    height.className = 'heightDiv'
    var autosizeBtn = document.createElement('div');
    autosizeBtn.className = 'geSprite geSprite-fit';
    autosizeBtn.setAttribute('title', mxResources.get('autosize') + ' (' + this.editorUi.actions.get('autosize').shortcut + ')');
    autosizeBtn.style.position = 'relative';
    autosizeBtn.style.cursor = 'pointer';
    autosizeBtn.style.marginTop = '-3px';
    autosizeBtn.style.border = '0px';
    autosizeBtn.style.left = '52px';
    mxUtils.setOpacity(autosizeBtn, 50);

    mxEvent.addListener(autosizeBtn, 'mouseenter', function () {
        mxUtils.setOpacity(autosizeBtn, 100);
    });

    mxEvent.addListener(autosizeBtn, 'mouseleave', function () {
        mxUtils.setOpacity(autosizeBtn, 50);
    });

    mxEvent.addListener(autosizeBtn, 'click', function () {
        ui.actions.get('autosize').funct();
    });

    // div.appendChild(autosizeBtn);  // 隐藏自动调整按钮
    // this.addLabel(div, mxResources.get('width'), 84); // 大小的高
    // this.addLabel(div, mxResources.get('height'), 20); // 大小的宽
    // console.log(div)
    mxUtils.br(div);

    var wrapper = document.createElement('div');
    wrapper.style.paddingTop = '8px';
    wrapper.style.paddingRight = '20px';
    wrapper.style.whiteSpace = 'nowrap';
    wrapper.style.textAlign = 'right'; // 限制比例
    wrapper.style.position = 'absolute'
    wrapper.style.bottom = '10px'
    wrapper.style.right = '7px'

    var opt = this.createCellOption(mxResources.get('constrainProportions'),
        mxConstants.STYLE_ASPECT, null, 'fixed', 'null');
    opt.style.width = '100%';
    wrapper.appendChild(opt);
    div.appendChild(wrapper);
    var constrainCheckbox = opt.getElementsByTagName('input')[0];
    this.addKeyHandler(width, listener);
    this.addKeyHandler(height, listener);

    widthUpdate = this.addGeometryHandler(width, function (geo, value) {
        if (geo.width > 0) {
            var value = Math.max(1, value);

            if (constrainCheckbox.checked) {
                geo.height = Math.round((geo.height * value * 100) / geo.width) / 100;
            }

            geo.width = value;
        }
    });
    heightUpdate = this.addGeometryHandler(height, function (geo, value) {
        if (geo.height > 0) {
            var value = Math.max(1, value);

            if (constrainCheckbox.checked) {
                geo.width = Math.round((geo.width * value * 100) / geo.height) / 100;
            }

            geo.height = value;
        }
    });

    container.appendChild(div);

    var div2 = this.createPanel();
    div2.style.paddingBottom = '30px';

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.width = '70px';
    span.style.marginTop = '0px';
    span.style.fontWeight = 'bold';
    mxUtils.write(span, mxResources.get('position'));
    div2.appendChild(span);

    var left = this.addUnitInput(div2, 'pt', 84, 44, function () {
        leftUpdate.apply(this, arguments);
    });
    var top = this.addUnitInput(div2, 'pt', 20, 44, function () {
        topUpdate.apply(this, arguments);
    });

    mxUtils.br(div2);
    this.addLabel(div2, mxResources.get('left'), 84);
    this.addLabel(div2, mxResources.get('top'), 20);

    var listener = mxUtils.bind(this, function (sender, evt, force) {
        rect = this.format.getSelectionState();

        if (!rect.containsLabel && rect.vertices.length == graph.getSelectionCount() &&
            rect.width != null && rect.height != null) {
            div.style.display = '';

            if (force || document.activeElement != width) {
                width.value = rect.width + ((rect.width == '') ? '' : ' pt');
            }

            if (force || document.activeElement != height) {
                height.value = rect.height + ((rect.height == '') ? '' : ' pt');
            }
        } else {
            div.style.display = 'none';
        }

        if (rect.vertices.length == graph.getSelectionCount() &&
            rect.x != null && rect.y != null) {
            div2.style.display = '';

            if (force || document.activeElement != left) {
                left.value = rect.x + ((rect.x == '') ? '' : ' pt');
            }

            if (force || document.activeElement != top) {
                top.value = rect.y + ((rect.y == '') ? '' : ' pt');
            }
        } else {
            div2.style.display = 'none';
        }
    });

    this.addKeyHandler(left, listener);
    this.addKeyHandler(top, listener);

    graph.getModel().addListener(mxEvent.CHANGE, listener);
    this.listeners.push({
        destroy: function () {
            graph.getModel().removeListener(listener);
        }
    });
    listener();

    leftUpdate = this.addGeometryHandler(left, function (geo, value) {
        if (geo.relative) {
            geo.offset.x = value;
        } else {
            geo.x = value;
        }
    });
    topUpdate = this.addGeometryHandler(top, function (geo, value) {
        if (geo.relative) {
            geo.offset.y = value;
        } else {
            geo.y = value;
        }
    });

    // container.appendChild(div2); // 隐藏图形位置
    return container
};

/**
 *
 */
ArrangePanel.prototype.addGeometryHandler = function (input, fn) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var initialValue = null;

    function update(evt) {
        if (input.value != '') {
            var value = parseFloat(input.value);

            if (value != initialValue) {
                graph.getModel().beginUpdate();
                try {
                    var cells = graph.getSelectionCells();

                    for (var i = 0; i < cells.length; i++) {
                        if (graph.getModel().isVertex(cells[i])) {
                            var geo = graph.getCellGeometry(cells[i]);

                            if (geo != null) {
                                geo = geo.clone();
                                fn(geo, value);

                                graph.getModel().setGeometry(cells[i], geo);
                            }
                        }
                    }
                } finally {
                    graph.getModel().endUpdate();
                }

                initialValue = value;
                input.value = value + ' pt';
            } else if (isNaN(value)) {
                input.value = initialValue + ' pt';
            }
        }

        mxEvent.consume(evt);
    };

    mxEvent.addListener(input, 'blur', update);
    mxEvent.addListener(input, 'change', update);
    mxEvent.addListener(input, 'focus', function () {
        initialValue = input.value;
    });

    return update;
};

ArrangePanel.prototype.addEdgeGeometryHandler = function (input, fn) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var initialValue = null;

    function update(evt) {
        if (input.value != '') {
            var value = parseFloat(input.value);

            if (isNaN(value)) {
                input.value = initialValue + ' pt';
            } else if (value != initialValue) {
                graph.getModel().beginUpdate();
                try {
                    var cells = graph.getSelectionCells();

                    for (var i = 0; i < cells.length; i++) {
                        if (graph.getModel().isEdge(cells[i])) {
                            var geo = graph.getCellGeometry(cells[i]);

                            if (geo != null) {
                                geo = geo.clone();
                                fn(geo, value);

                                graph.getModel().setGeometry(cells[i], geo);
                            }
                        }
                    }
                } finally {
                    graph.getModel().endUpdate();
                }

                initialValue = value;
                input.value = value + ' pt';
            }
        }

        mxEvent.consume(evt);
    };

    mxEvent.addListener(input, 'blur', update);
    mxEvent.addListener(input, 'change', update);
    mxEvent.addListener(input, 'focus', function () {
        initialValue = input.value;
    });

    return update;
};

/**
 *
 */
ArrangePanel.prototype.addEdgeGeometry = function (container) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var rect = this.format.getSelectionState();

    var div = this.createPanel();

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.width = '70px';
    span.style.marginTop = '0px';
    span.style.fontWeight = 'bold';
    mxUtils.write(span, mxResources.get('width'));
    div.appendChild(span);

    var widthUpdate, xtUpdate, ytUpdate, xsUpdate, ysUpdate;
    var width = this.addUnitInput(div, 'pt', 20, 44, function () {
        widthUpdate.apply(this, arguments);
    });

    mxUtils.br(div);
    this.addKeyHandler(width, listener);

    function widthUpdate(evt) {
        // Maximum stroke width is 999
        var value = parseInt(width.value);
        value = Math.min(999, Math.max(1, (isNaN(value)) ? 1 : value));

        if (value != mxUtils.getValue(rect.style, 'width', mxCellRenderer.defaultShapes['flexArrow'].prototype.defaultWidth)) {
            graph.setCellStyles('width', value, graph.getSelectionCells());
            ui.fireEvent(new mxEventObject('styleChanged', 'keys', ['width'],
                'values', [value], 'cells', graph.getSelectionCells()));
        }
        width.value = value + ' pt';
        mxEvent.consume(evt);
    };

    mxEvent.addListener(width, 'blur', widthUpdate);
    mxEvent.addListener(width, 'change', widthUpdate);

    container.appendChild(div);

    var divs = this.createPanel();
    divs.style.paddingBottom = '30px';

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.width = '70px';
    span.style.marginTop = '0px';
    span.style.fontWeight = 'bold';
    mxUtils.write(span, 'Start');
    divs.appendChild(span);
    divs.className = 'start'
    var xs = this.addUnitInput(divs, 'pt', 84, 44, function () {
        xsUpdate.apply(this, arguments);
    });
    var ys = this.addUnitInput(divs, 'pt', 20, 44, function () {
        ysUpdate.apply(this, arguments);
    });

    mxUtils.br(divs);
    this.addLabel(divs, mxResources.get('left'), 84);
    this.addLabel(divs, mxResources.get('top'), 20);
    container.appendChild(divs);
    this.addKeyHandler(xs, listener);
    this.addKeyHandler(ys, listener);

    var divt = this.createPanel();
    divt.style.paddingBottom = '30px';

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.width = '70px';
    span.style.marginTop = '0px';
    span.style.fontWeight = 'bold';
    mxUtils.write(span, 'End');
    divt.appendChild(span);
    divt.className = 'end'
    var xt = this.addUnitInput(divt, 'pt', 84, 44, function () {
        xtUpdate.apply(this, arguments);
    });
    var yt = this.addUnitInput(divt, 'pt', 20, 44, function () {
        ytUpdate.apply(this, arguments);
    });

    mxUtils.br(divt);
    this.addLabel(divt, mxResources.get('left'), 84);
    this.addLabel(divt, mxResources.get('top'), 20);
    container.appendChild(divt);
    this.addKeyHandler(xt, listener);
    this.addKeyHandler(yt, listener);

    var listener = mxUtils.bind(this, function (sender, evt, force) {
        rect = this.format.getSelectionState();
        var cell = graph.getSelectionCell();

        if (rect.style.shape == 'link' || rect.style.shape == 'flexArrow') {
            div.style.display = '';

            if (force || document.activeElement != width) {
                var value = mxUtils.getValue(rect.style, 'width',
                    mxCellRenderer.defaultShapes['flexArrow'].prototype.defaultWidth);
                width.value = value + ' pt';
            }
        } else {
            div.style.display = 'none';
        }

        if (graph.getSelectionCount() == 1 && graph.model.isEdge(cell)) {
            var geo = graph.model.getGeometry(cell);

            if (geo.sourcePoint != null && graph.model.getTerminal(cell, true) == null) {
                xs.value = geo.sourcePoint.x;
                ys.value = geo.sourcePoint.y;
            } else {
                divs.style.display = 'none';
            }

            if (geo.targetPoint != null && graph.model.getTerminal(cell, false) == null) {
                xt.value = geo.targetPoint.x;
                yt.value = geo.targetPoint.y;
            } else {
                divt.style.display = 'none';
            }
        } else {
            divs.style.display = 'none';
            divt.style.display = 'none';
        }
    });

    xsUpdate = this.addEdgeGeometryHandler(xs, function (geo, value) {
        geo.sourcePoint.x = value;
    });

    ysUpdate = this.addEdgeGeometryHandler(ys, function (geo, value) {
        geo.sourcePoint.y = value;
    });

    xtUpdate = this.addEdgeGeometryHandler(xt, function (geo, value) {
        geo.targetPoint.x = value;
    });

    ytUpdate = this.addEdgeGeometryHandler(yt, function (geo, value) {
        geo.targetPoint.y = value;
    });

    graph.getModel().addListener(mxEvent.CHANGE, listener);
    this.listeners.push({
        destroy: function () {
            graph.getModel().removeListener(listener);
        }
    });
    listener();
    return container
};

/**
 * Adds the label menu items to the given menu and parent.
 */
TextFormatPanel = function (format, editorUi, container, type) {
    BaseFormatPanel.call(this, format, editorUi, container);
    if (type) {
        this.init1();
    } else {
        this.init2();
    }
};

mxUtils.extend(TextFormatPanel, BaseFormatPanel);

/**
 * Adds the label menu items to the given menu and parent.
 */
TextFormatPanel.prototype.init1 = function () {
    this.container.style.borderBottom = 'none';
    // this.addFont(this.container);
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();
    // add jinsu 添加一个绑定类型
    // if (ss.edges.length <= 0) {
    this.container.appendChild(this.bindType())
    this.container.appendChild(this.addAttributeContainer())
    // }
    // if (ss.edges.length <= 0) {
    //   this.container.appendChild(this._getDeviceName())
    // }
    // 注：需要兼容之前的画布
    // if (ss.style && ss.style.image) {
    //   if (ss.style.image.indexOf('equipment') >= 0 || ss.style.image.indexOf('simulationDiya') >= 0 || ss.style.image.indexOf('simulationPeiDian') >= 0) {
    //     this.container.appendChild(this._getPrecinctId())
    //   }
    // }
};
// 临时方法
TextFormatPanel.prototype.init2 = function () {
    this.container.style.borderBottom = 'none';
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();
    if (ss.style.shape == 'connector' || ss.style.shape == 'line') {
        return false
    } else {
        this.addFont(this.container);
    }
};

TextFormatPanel.prototype._getDeviceName = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var sect = graph.getSelectionCells()
    var ss = this.format.getSelectionState();
    var baseDiv = document.createElement('div')
    baseDiv.style.marginTop = '15px'
    baseDiv.style.width = '100%'
    baseDiv.style.paddingLeft = '10px'
    var labelP = document.createElement('p')
    labelP.innerText = '设备名称'
    labelP.style.width = '60px'
    labelP.style.height = '24px'
    labelP.style.margin = '0'
    labelP.style.lineHeight = '24px'
    labelP.style.display = 'inline-block'
    labelP.style.verticalAlign = 'middle';
    var input = document.createElement('input')
    input.className = 'eleInput'
    input.style.height = '24px'
    input.style.width = '152px'

    if (ss && sect.length > 1) {
        input.setAttribute("disabled", true)
        input.style.cursor = 'no-drop'
    } else {
        input.removeAttribute("disabled")
        input.style.cursor = 'default'
    }
    baseDiv.appendChild(labelP)
    baseDiv.appendChild(input)
    sect[0] ? input.value = sect[0].value : input.value = ''
    input.addEventListener('blur', _setPrecinctId)
    input.addEventListener('change', _setPrecinctId)

    function _setPrecinctId(e) {
        graph.getModel().beginUpdate();
        try {
            graph.cellLabelChanged(sect[0], e.target.value)
        } finally {
            graph.getModel().endUpdate();
        }
    }

    return baseDiv
}

TextFormatPanel.prototype._getPrecinctId = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var sect = graph.getSelectionCells()
    var ss = this.format.getSelectionState();
    var baseDiv = document.createElement('div')
    baseDiv.style.marginTop = '15px'
    baseDiv.style.width = '100%'
    baseDiv.style.paddingLeft = '10px'
    var labelP = document.createElement('p')
    labelP.innerText = '设备编码'
    labelP.style.width = '60px'
    labelP.style.height = '24px'
    labelP.style.margin = '0'
    labelP.style.lineHeight = '24px'
    labelP.style.display = 'inline-block'
    labelP.style.verticalAlign = 'top';
    var formItem = document.createElement('div')
    formItem.classList.add('form-item__content')
    formItem.style.display = 'inline-block'
    formItem.style.width = '100%'
    var input = document.createElement('input')
    input.className = 'eleInput'
    input.style.height = '24px'
    input.style.width = '152px'
    formItem.appendChild(input)
    if (ss && sect.length > 1) {
        input.setAttribute("disabled", true)
        input.style.cursor = 'no-drop'
    } else {
        input.removeAttribute("disabled")
        input.style.cursor = 'default'
    }
    var tooltip = document.createElement('div')
    tooltip.style.marginTop = '10px'
    tooltip.style.width = '152px'
    tooltip.style.whiteSpace = 'pre-line'
    tooltip.style.wordBreak = 'break-all'
    tooltip.style.wordWrap = 'break-word'

    var icon = document.createElement('span')
    icon.classList.add('equipment_tooltip')

    var txt = document.createElement('div')
    txt.classList.add('equipment_txt')

    txt.innerHTML = "设备编码格式参考《中国移动动环命名及编码指导意见》中的命名格式要求"
    tooltip.appendChild(icon)
    tooltip.appendChild(txt)
    formItem.appendChild(tooltip)

    baseDiv.appendChild(labelP)
    baseDiv.appendChild(formItem)
    input.value = ss.style.precinctId || ''
    input.addEventListener('blur', _setPrecinctId)
    input.addEventListener('change', _setPrecinctId)

    function _setPrecinctId(e) {
        graph.getModel().beginUpdate();
        try {
            graph.setCellStyles('precinctId', e.target.value, graph.getSelectionCells());
        } finally {
            graph.getModel().endUpdate();
        }
    }

    return baseDiv
}
TextFormatPanel.prototype.bindType = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var sect = graph.getSelectionCells()
    var ss = this.format.getSelectionState();
    var baseDiv = document.createElement('div')
    baseDiv.style.marginTop = '15px'
    baseDiv.style.width = '100%'
    baseDiv.style.paddingLeft = '10px'

    if (ss.style.image) {
        let iconBindDiv = document.createElement('div')
        iconBindDiv.className = 'part_Div'
        iconBindDiv.style.height = '48px'
        let iconBindLableP = document.createElement('P')
        iconBindLableP.className = 'lable_Title'
        iconBindLableP.innerText = '绑定图标'
        iconBindLableP.style.margin = '0px'
        let iconRightDiv = document.createElement('div')
        // ipRightDiv.style.paddingLeft = '20px'
        iconRightDiv.className = 'panel_container'
        iconRightDiv.style.height = '60px'
        iconRightDiv.style.width = '160px'
        let iconSelect = document.createElement('select')
        iconSelect.name = 'iconName'
        iconSelect.className = "pictureTypeSelest icon"
        // 设置option参数列表
        // 添加option
        let bandObjIconData = {
            'access_switch': {name: '接入交换机', image: 'stencils/editor/switch/access_switch.svg'},
            'agg_switch': {name: '聚合交换机', image: 'stencils/editor/switch/agg_switch.svg'},
            'core_switch': {name: '核心交换机', image: 'stencils/editor/switch/core_switch.svg'},
            'CE12800_switch': {name: 'CE12800交换机', image: 'stencils/editor/switch/CE12800_switch.svg'},
            'normal_switch': {name: '通用交换机', image: 'stencils/editor/switch/normal_switch.svg'},
            'stack_switch': {name: '堆叠交换机', image: 'stencils/editor/switch/stack_switch.svg'},
            'stack_CE_switch': {name: '堆叠CE交换机', image: 'stencils/editor/switch/stack_CE_switch.svg'},
            'stack_TOR_switch': {name: '堆叠TOR交换机', image: 'stencils/editor/switch/stack_TOR_switch.svg'},
            'TOR_switch': {name: 'TOR交换机', image: 'stencils/editor/switch/TOR_switch.svg'},
            'vm_switch': {name: 'vm交换机', image: 'stencils/editor/switch/vm_switch.svg'},
            'a_firewall': {name: '防火墙A', image: 'stencils/editor/firewall/a_firewall.svg'},
            'b_firewall': {name: '防火墙B', image: 'stencils/editor/firewall/b_firewall.svg'},
            'core_router': {name: '核心路由器', image: 'stencils/editor/router/core_router.svg'},
            'normal_f5': {name: '负载均衡', image: 'stencils/editor/loadBalance/normal_f5.svg'},
            'normal_service': {name: '通用服务器', image: 'stencils/editor/service/normal_service.svg'},
            'normal_internet': {name: 'Internet', image: 'stencils/editor/internet/normal_internet.svg'},
            'ip_internet': {name: 'IP', image: 'stencils/editor/internet/ip_internet.svg'},
            'LAN_internet': {name: 'LAN', image: 'stencils/editor/internet/LAN_internet.svg'},
            'SAN_internet': {name: 'SAN', image: 'stencils/editor/internet/SAN_internet.svg'},
            'WAN_internet': {name: 'WAN', image: 'stencils/editor/internet/WAN_internet.svg'},
            'unknow_device': {name: '未知设备', image: 'stencils/editor/switch/unknow_device.svg'}
        }
        for (var key in bandObjIconData) {
            var iconOption = document.createElement('option')
            iconOption.value = key
            iconOption.innerText = bandObjIconData[key].name
            iconSelect.appendChild(iconOption)
            if (ss.style.type == key.substring(0, key.lastIndexOf('_')) && ss.style.image == bandObjIconData[key].image) {
                iconOption.selected = true
            }
            // if (sessionStorage.getItem('idcType') == idcTypeOption.value) {
            //     idcTypeOption.selected = true
            // }
        }
        iconSelect.addEventListener('change', function (e) {
            graph.getModel().beginUpdate();
            try {
                var type = e.target.value.substring(0, e.target.value.lastIndexOf('_'))
                var image = bandObjIconData[e.target.value].image
                graph.setCellStyles('type', type, graph.getSelectionCells());
                graph.setCellStyles('image', image, graph.getSelectionCells());
            } finally {
                graph.getModel().endUpdate();
            }
        })
        // let ipAextArea = document.createElement('textarea')
        // ipAextArea.className = 'ipArea'
        // ipAextArea.style.height = '60px'
        // ipAextArea.style.width = '156px'
        // ipAextArea.style.color = 'rgb(112, 112, 112)'
        // ipAextArea.style.fontSize = '12px'
        // ipAextArea.value = ipList.join(',')
        iconRightDiv.appendChild(iconSelect)
        iconBindDiv.appendChild(iconBindLableP)
        iconBindDiv.appendChild(iconRightDiv)
        baseDiv.appendChild(iconBindDiv)
    }
    //
    var labelP = document.createElement('p')
    labelP.innerText = '绑定类型'
    labelP.style.width = '60px'
    labelP.style.height = '24px'
    labelP.style.margin = '0'
    labelP.style.lineHeight = '24px'
    labelP.style.display = 'inline-block'
    labelP.style.verticalAlign = 'top';
    baseDiv.appendChild(labelP);
    let nodeBandRadioData = ['告警', '监控']
    //创建radio
    let nodeBandDataDiv = document.createElement('div')
    for (let index = 0; index < nodeBandRadioData.length; index++) {
        let nodeBandRadioDiv = document.createElement('div')
        let nodeBandRadio = document.createElement('input')
        let nodeBandRadioLabel = document.createElement('p')
        // show单选
        let nodeBandRadioShow = document.createElement('p')
        nodeBandRadioShow.classList.add('radioUnChecked', 'nodeBandRadioShow')
        // 单选label
        nodeBandRadioLabel.innerText = nodeBandRadioData[index]
        nodeBandRadioLabel.style.display = 'inline-block'
        nodeBandRadioLabel.style.margin = '0'
        nodeBandRadio.setAttribute('type', 'radio')
        nodeBandRadio.name = 'bandTypeRadio'
        nodeBandRadio.value = index
        nodeBandRadio.style.display = 'none'
        if (sect.length >= 2 || (ss.image && index != 0)) {
            nodeBandRadioShow.style.cursor = 'no-drop'
        }
        nodeBandDataDiv.appendChild(nodeBandRadioDiv)
        nodeBandRadio.addEventListener('click', function (e) {
            // graph.getModel().refresh()
            graph.getModel().beginUpdate();
            try {
                if (ss.style.bandType != index) {
                    graph.setCellStyles('bandTypeTemp', index, graph.getSelectionCells());
                    graph.setCellStyles('isBind', null, graph.getSelectionCells());
                } else {
                    graph.setCellStyles('bandTypeTemp', null, graph.getSelectionCells());
                    graph.setCellStyles('isBind', true, graph.getSelectionCells());
                }
                // graph.setCellStyles('isBind', null, graph.getSelectionCells());
                // graph.setCellStyles('name', null, graph.getSelectionCells());
                // graph.setCellStyles('unit', null, graph.getSelectionCells());
                // graph.setCellStyles('valueView', null, graph.getSelectionCells());
                // graph.setCellStyles('colorView', null, graph.getSelectionCells());
                // graph.setCellStyles('sillList', null, graph.getSelectionCells());
                // graph.setCellStyles('countType', null, graph.getSelectionCells());
                // graph.setCellStyles('alertLevelList', null, graph.getSelectionCells());
                // graph.setCellStyles('deviceList', null, graph.getSelectionCells());
                // graph.setCellStyles('itemList', null, graph.getSelectionCells());
            } finally {
                graph.getModel().endUpdate();
            }
        })
        nodeBandRadioShow.addEventListener('click', () => {
            if (sect.length != 1 || (ss.image && index != 0)) {
                return
            } else {
                nodeBandRadio.click()
                nodeBandRadio.value = index
                nodeBandRadio.checked = true
                let showNodes = document.getElementsByClassName("nodeBandRadioShow");
                for (let item = 0; item < showNodes.length; item++) {
                    let element = showNodes[item];
                    if (item === index) {
                        element.classList.remove('radioUnChecked')
                        element.classList.add('radioChecked')
                    } else {
                        element.classList.add('radioUnChecked')
                        element.classList.remove('radioChecked')
                    }
                }
            }
        })
        if (sect.length === 1 && index === ss.style.bandType) {
            nodeBandRadio.checked = true
            nodeBandRadioShow.classList.remove('radioUnChecked')
            nodeBandRadioShow.classList.add('radioChecked')
        }
        nodeBandRadioDiv.appendChild(nodeBandRadio)
        nodeBandRadioDiv.appendChild(nodeBandRadioShow)

        nodeBandRadioDiv.appendChild(nodeBandRadioLabel)
        nodeBandRadioDiv.style.margin = '0 0 20px 0px'
    }


    // if (ss.style.bandType == undefined) {
            //     nodeBandRadio.click()
            //     nodeBandRadio.value = index
            //     nodeBandRadio.checked = true
            //     nodeBandRadioShow.classList.remove('radioUnChecked')
            //     nodeBandRadioShow.classList.add('radioChecked')
            // }
    nodeBandDataDiv.style.float = 'right'
    nodeBandDataDiv.style.marginRight = '115px'
    nodeBandDataDiv.style.paddingTop = '5px';
    // nodeBandlDiv.appendChild(nodeBandDataDiv)
    baseDiv.appendChild(nodeBandDataDiv)
    return baseDiv
}
TextFormatPanel.prototype.addAttributeContainer = function (container) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();

    var sect = graph.getSelectionCells()
    var container = document.createElement('div')
    container.style.float = 'left'
    container.className = 'panel_Div'
    let attrDiv = document.createElement('div')
    attrDiv.className = 'part_Div'
    let attrLableP = document.createElement('P')
    attrLableP.className = 'lable_Title'
    attrLableP.innerText = '绑定对象'
    let attrConRightDiv = document.createElement('div')
    // attrConRightDiv.style.paddingLeft = '20px'
    attrConRightDiv.className = 'panel_container'

    // let attrConInput = document.createElement('input')
    // attrConInput.setAttribute('readonly', 'true')
    // attrConInput.style.height = '24px'
    // attrConInput.style.width = '116px'
    // attrConInput.style.borderRadius = '4px 0 0 4px'
    //
    // attrConInput.className = 'eleInput'
    // attrConInput.id = 'attrConInput-bandNode'
    let attrConButton = document.createElement('button')
    attrConButton.innerText = '选择'
    attrConButton.style.height = '24px'
    attrConButton.style.width = '56px'
    attrConButton.style.borderRadius = '4px 4px 4px 4px'
    attrConButton.style.position = 'relative'
    attrConButton.style.left = '-1px'
    // 添加绑定对象回显div
    let bindDataDiv = document.createElement('div')
    bindDataDiv.id = 'bindDataDiv-bandNode'
    if (sect.length != 1) {
        // attrConInput.value = '请选择单一节点'
        attrConButton.setAttribute('disable', true)
        attrConButton.style.cursor = 'no-drop'
    } else {
        if (ss.style.isBind) {
            if (ss.style.deviceList && ss.style.deviceList.length != 0) {
                let deviceListObj = JSON.parse(ss.style.deviceList)
                let ipList = []
                for (let i = 0; i < deviceListObj.length; i++) {
                    ipList.push(deviceListObj[i].idcType + ':' + deviceListObj[i].ip)
                }
                let ipBindDiv = document.createElement('div')
                ipBindDiv.className = 'part_Div'
                ipBindDiv.style.height = '60px'
                let ipBindLableP = document.createElement('P')
                ipBindLableP.className = 'lable_Title'
                ipBindLableP.innerText = '绑定IP'
                let ipRightDiv = document.createElement('div')
                // ipRightDiv.style.paddingLeft = '20px'
                ipRightDiv.className = 'panel_container'
                ipRightDiv.style.height = '60px'
                ipRightDiv.style.width = '160px'
                let ipAextArea = document.createElement('textarea')
                ipAextArea.className = 'ipArea'
                ipAextArea.style.height = '60px'
                ipAextArea.style.width = '156px'
                ipAextArea.style.color = 'rgb(112, 112, 112)'
                ipAextArea.style.fontSize = '12px'
                ipAextArea.value = ipList.join(',')
                ipRightDiv.appendChild(ipAextArea)
                ipBindDiv.appendChild(ipBindLableP)
                ipBindDiv.appendChild(ipRightDiv)
                bindDataDiv.appendChild(ipBindDiv)
            }
            if (ss.style.itemList && ss.style.itemList.length != 0) {
                let itemListObj = JSON.parse(ss.style.itemList)
                let keyList = []
                for (let i = 0; i < itemListObj.length; i++) {
                    keyList.push(itemListObj[i].key)
                }
                let itemBindDiv = document.createElement('div')
                itemBindDiv.className = 'part_Div'
                itemBindDiv.style.height = '60px'
                let itemBindLableP = document.createElement('P')
                itemBindLableP.className = 'lable_Title'
                itemBindLableP.innerText = '绑定指标'
                let itemRightDiv = document.createElement('div')
                // ipRightDiv.style.paddingLeft = '20px'
                itemRightDiv.className = 'panel_container'
                itemRightDiv.style.height = '60px'
                itemRightDiv.style.width = '160px'
                let itemTextArea = document.createElement('textarea')
                itemTextArea.className = 'itemArea'
                itemTextArea.style.height = '60px'
                itemTextArea.style.width = '156px'
                itemTextArea.style.color = 'rgb(112, 112, 112)'
                itemTextArea.style.fontSize = '12px'
                itemTextArea.value = keyList.join(',')
                itemRightDiv.appendChild(itemTextArea)
                itemBindDiv.appendChild(itemBindLableP)
                itemBindDiv.appendChild(itemRightDiv)
                bindDataDiv.appendChild(itemBindDiv)
            }
            if (ss.style.name) {
                let name = JSON.parse(ss.style.name)
                let nameBindDiv = document.createElement('div')
                nameBindDiv.className = 'part_Div'
                let nameBindLableP = document.createElement('P')
                nameBindLableP.className = 'lable_Title'
                nameBindLableP.innerText = '对象名称'
                let nameRightDiv = document.createElement('div')
                nameRightDiv.className = 'panel_container'
                let nameSpan = document.createElement('span')
                nameSpan.innerText = name
                nameRightDiv.appendChild(nameSpan)
                nameBindDiv.appendChild(nameBindLableP)
                nameBindDiv.appendChild(nameRightDiv)
                bindDataDiv.appendChild(nameBindDiv)
            }
            if (ss.style.bandType == 1) {
                if (ss.style.countType) {
                    let countType = JSON.parse(ss.style.countType)
                    if (countType && countType != '') {
                        let countTypeBindDiv = document.createElement('div')
                        countTypeBindDiv.className = 'part_Div'
                        let countTypeBindLableP = document.createElement('P')
                        countTypeBindLableP.className = 'lable_Title'
                        countTypeBindLableP.innerText = '计算方式'
                        let countTypeRightDiv = document.createElement('div')
                        countTypeRightDiv.className = 'panel_container'
                        let countTypeSpan = document.createElement('span')
                        if (countType == 'max') {
                            countType = '最大'
                        } else if (countType == 'min') {
                            countType = '最小'
                        } else if (countType == 'avg') {
                            countType = '平均'
                        } else if (countType == 'sum') {
                            countType = '求和'
                        }
                        countTypeSpan.innerText = countType
                        countTypeRightDiv.appendChild(countTypeSpan)
                        countTypeBindDiv.appendChild(countTypeBindLableP)
                        countTypeBindDiv.appendChild(countTypeRightDiv)
                        bindDataDiv.appendChild(countTypeBindDiv)
                    }
                }
                if (ss.style.sillList) {
                    let sillListObj = JSON.parse(ss.style.sillList)
                    if (sillListObj.length > 0) {
                        let triggerNumBindDiv = document.createElement('div')
                        triggerNumBindDiv.className = 'part_Div'
                        let triggerBindLableP = document.createElement('P')
                        triggerBindLableP.className = 'lable_Title'
                        triggerBindLableP.innerText = '阀值配置数'
                        triggerBindLableP.style.width = '60px'
                        let triggerRightDiv = document.createElement('div')
                        triggerRightDiv.className = 'panel_container'
                        let triggerSpan = document.createElement('span')
                        triggerSpan.innerText = sillListObj.length
                        triggerRightDiv.appendChild(triggerSpan)
                        triggerNumBindDiv.appendChild(triggerBindLableP)
                        triggerNumBindDiv.appendChild(triggerRightDiv)
                        bindDataDiv.appendChild(triggerNumBindDiv)
                    }
                }
            } else if (ss.style.bandType == 0 && ss.style.alertLevelList) {
                let alertLevelListObj = JSON.parse(ss.style.alertLevelList)
                let alertLevelBindDiv = document.createElement('div')
                alertLevelBindDiv.className = 'part_Div'
                let alertLevelBindLableP = document.createElement('P')
                alertLevelBindLableP.className = 'lable_Title'
                alertLevelBindLableP.innerText = '告警范围'
                let alertLevelRightDiv = document.createElement('div')
                alertLevelRightDiv.className = 'panel_container'
                let alertLevelSpan = document.createElement('span')
                let alertLevelDescList = []
                alertLevelListObj.forEach((item) => {
                    let desc = ''
                    if (item == '2') {
                        desc = '低'
                    } else if (item == '3') {
                        desc = '中'
                    } else if (item == '4') {
                        desc = '高'
                    } else if (item == '5') {
                        desc = '重大'
                    }
                    alertLevelDescList.push(desc)
                })
                alertLevelSpan.innerText = alertLevelDescList.join(',')
                alertLevelRightDiv.appendChild(alertLevelSpan)
                alertLevelBindDiv.appendChild(alertLevelBindLableP)
                alertLevelBindDiv.appendChild(alertLevelRightDiv)
                bindDataDiv.appendChild(alertLevelBindDiv)
            }
            if (ss.style.valueView) {
                let valueView = JSON.parse(ss.style.valueView)
                let valueViewBindDiv = document.createElement('div')
                valueViewBindDiv.className = 'part_Div'
                let valueViewBindLableP = document.createElement('P')
                valueViewBindLableP.className = 'lable_Title'
                valueViewBindLableP.innerText = ss.style.bandType == 0 ? '告警值显示' : '监控值显示'
                valueViewBindLableP.style.width = '60px'
                let valueViewRightDiv = document.createElement('div')
                valueViewRightDiv.className = 'panel_container'
                let valueViewSpan = document.createElement('span')
                valueViewSpan.innerText = valueView ? '是' : '否'
                valueViewRightDiv.appendChild(valueViewSpan)
                valueViewBindDiv.appendChild(valueViewBindLableP)
                valueViewBindDiv.appendChild(valueViewRightDiv)
                bindDataDiv.appendChild(valueViewBindDiv)
            }
            if (ss.style.colorView) {
                let colorView = JSON.parse(ss.style.colorView)
                let colorViewBindDiv = document.createElement('div')
                colorViewBindDiv.className = 'part_Div'
                let colorViewBindLableP = document.createElement('P')
                colorViewBindLableP.className = 'lable_Title'
                colorViewBindLableP.innerText = ss.style.bandType == 0 ? '异常动态显示' : '阀值异常显示'
                colorViewBindLableP.style.width = '70px'
                let colorViewRightDiv = document.createElement('div')
                colorViewRightDiv.className = 'panel_container'
                let colorViewSpan = document.createElement('span')
                colorViewSpan.innerText = colorView ? '是' : '否'
                colorViewRightDiv.appendChild(colorViewSpan)
                colorViewBindDiv.appendChild(colorViewBindLableP)
                colorViewBindDiv.appendChild(colorViewRightDiv)
                bindDataDiv.appendChild(colorViewBindDiv)
            }
        }
        attrConButton.removeAttribute('disable')
        attrConButton.style.cursor = 'pointer'
        // attrConInput.value = ss.style.name ? ss.style.name : '请选择监控对象'

    }
    attrConButton.addEventListener('click', function (e) {
        //绑定对象
        if (sect.length != 1) {
            return
        } else {
            let data = {}
            if (ss.style.isBind == null) {
                data = {bandType: ss.style.bandTypeTemp}
            } else {
                data = ss.style
            }
            window.parent.postMessage({
                cmd: 'showNodeBind',
                params: {
                    success: true,
                    datas: data
                }
            }, '*')
        }
    })
// 装载节点
// attrConRightDiv.appendChild(attrConInput)
    attrConRightDiv.appendChild(attrConButton)
    attrDiv.appendChild(attrLableP)
    attrDiv.appendChild(attrConRightDiv)
//

//总父盒子
    container.appendChild(attrDiv)
    container.appendChild(bindDataDiv)
    return container
}
/**
 * Adds the label menu items to the given menu and parent.
 */
TextFormatPanel.prototype.addFont = function (container) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();

    var title = this.createTitle(mxResources.get('font'));
    title.style.paddingLeft = '18px';
    title.style.paddingTop = '10px';
    title.style.paddingBottom = '6px';
    container.appendChild(title);


    var stylePanel = this.createPanel();
    stylePanel.style.paddingTop = '2px';
    stylePanel.style.paddingBottom = '2px';
    stylePanel.style.position = 'relative';
    stylePanel.style.marginLeft = '-2px';
    stylePanel.style.borderWidth = '0px';
    stylePanel.className = 'geToolbarContainer';


    if (mxClient.IS_QUIRKS) {
        stylePanel.style.display = 'block';
    }

    if (graph.cellEditor.isContentEditing()) {
        var cssPanel = stylePanel.cloneNode();

        var cssMenu = this.editorUi.toolbar.addMenu(mxResources.get('style'),
            mxResources.get('style'), true, 'formatBlock', cssPanel);
        cssMenu.style.color = 'rgb(112, 112, 112)';
        cssMenu.style.whiteSpace = 'nowrap';
        cssMenu.style.overflow = 'hidden';
        cssMenu.style.margin = '0px';
        this.addArrow(cssMenu);
        cssMenu.style.width = '192px';
        cssMenu.style.height = '15px';

        var arrow = cssMenu.getElementsByTagName('div')[0];
        arrow.style.cssFloat = 'right';
        container.appendChild(cssPanel);

        // Workaround for offset in FF
        if (mxClient.IS_FF) {
            cssMenu.getElementsByTagName('div')[0].style.marginTop = '-18px';
        }
    }

    container.appendChild(stylePanel);

    var colorPanel = this.createPanel();
    colorPanel.style.marginTop = '8px';
    colorPanel.style.borderTop = '1px solid #c0c0c0';
    colorPanel.style.paddingTop = '6px';
    colorPanel.style.paddingBottom = '6px';

    var fontMenu = this.editorUi.toolbar.addMenu('Helvetica', mxResources.get('fontFamily'), true, 'fontFamily', stylePanel);
    fontMenu.style.color = 'rgb(112, 112, 112)';
    fontMenu.style.whiteSpace = 'nowrap';
    fontMenu.style.overflow = 'hidden';
    fontMenu.style.margin = '0px';

    this.addArrow(fontMenu);
    fontMenu.style.width = '192px';
    fontMenu.style.height = '15px';


    // Workaround for offset in FF
    if (mxClient.IS_FF) {
        fontMenu.getElementsByTagName('div')[0].style.marginTop = '-18px';
    }

    var stylePanel2 = stylePanel.cloneNode(false);
    stylePanel2.style.marginLeft = '-3px';
    var fontStyleItems = this.editorUi.toolbar.addItems(['bold', 'italic', 'underline'], stylePanel2, true);
    fontStyleItems[0].setAttribute('title', mxResources.get('bold') + ' (' + this.editorUi.actions.get('bold').shortcut + ')');
    fontStyleItems[1].setAttribute('title', mxResources.get('italic') + ' (' + this.editorUi.actions.get('italic').shortcut + ')');
    fontStyleItems[2].setAttribute('title', mxResources.get('underline') + ' (' + this.editorUi.actions.get('underline').shortcut + ')');

    var verticalItem = this.editorUi.toolbar.addItems(['vertical'], stylePanel2, true)[0];

    if (mxClient.IS_QUIRKS) {
        mxUtils.br(container);
    }

    container.appendChild(stylePanel2);

    this.styleButtons(fontStyleItems);
    this.styleButtons([verticalItem]);

    var stylePanel3 = stylePanel.cloneNode(false);
    stylePanel3.style.marginLeft = '-3px';
    stylePanel3.style.paddingBottom = '0px';

    // Helper function to return a wrapper function does not pass any arguments
    var callFn = function (fn) {
        return function () {
            return fn();
        };
    };

    var left = this.editorUi.toolbar.addButton('geSprite-left', mxResources.get('left'),
        (graph.cellEditor.isContentEditing()) ?
            function () {
                document.execCommand('justifyleft', false, null);
            } : callFn(this.editorUi.menus.createStyleChangeFunction([mxConstants.STYLE_ALIGN], [mxConstants.ALIGN_LEFT])), stylePanel3);
    var center = this.editorUi.toolbar.addButton('geSprite-center', mxResources.get('center'),
        (graph.cellEditor.isContentEditing()) ?
            function () {
                document.execCommand('justifycenter', false, null);
            } : callFn(this.editorUi.menus.createStyleChangeFunction([mxConstants.STYLE_ALIGN], [mxConstants.ALIGN_CENTER])), stylePanel3);
    var right = this.editorUi.toolbar.addButton('geSprite-right', mxResources.get('right'),
        (graph.cellEditor.isContentEditing()) ?
            function () {
                document.execCommand('justifyright', false, null);
            } : callFn(this.editorUi.menus.createStyleChangeFunction([mxConstants.STYLE_ALIGN], [mxConstants.ALIGN_RIGHT])), stylePanel3);

    this.styleButtons([left, center, right]);

    if (graph.cellEditor.isContentEditing()) {
        var clear = this.editorUi.toolbar.addButton('geSprite-removeformat', mxResources.get('removeFormat'),
            function () {
                document.execCommand('removeformat', false, null);
            }, stylePanel2);
        this.styleButtons([clear]);
    }

    var top = this.editorUi.toolbar.addButton('geSprite-top', mxResources.get('top'),
        callFn(this.editorUi.menus.createStyleChangeFunction([mxConstants.STYLE_VERTICAL_ALIGN], [mxConstants.ALIGN_TOP])), stylePanel3);
    var middle = this.editorUi.toolbar.addButton('geSprite-middle', mxResources.get('middle'),
        callFn(this.editorUi.menus.createStyleChangeFunction([mxConstants.STYLE_VERTICAL_ALIGN], [mxConstants.ALIGN_MIDDLE])), stylePanel3);
    var bottom = this.editorUi.toolbar.addButton('geSprite-bottom', mxResources.get('bottom'),
        callFn(this.editorUi.menus.createStyleChangeFunction([mxConstants.STYLE_VERTICAL_ALIGN], [mxConstants.ALIGN_BOTTOM])), stylePanel3);

    this.styleButtons([top, middle, bottom]);

    if (mxClient.IS_QUIRKS) {
        mxUtils.br(container);
    }

    container.appendChild(stylePanel3);

    // Hack for updating UI state below based on current text selection
    // currentTable is the current selected DOM table updated below
    var sub, sup, full, tableWrapper, currentTable, tableCell, tableRow;

    if (graph.cellEditor.isContentEditing()) {
        top.style.display = 'none';
        middle.style.display = 'none';
        bottom.style.display = 'none';
        verticalItem.style.display = 'none';

        full = this.editorUi.toolbar.addButton('geSprite-justifyfull', null,
            function () {
                document.execCommand('justifyfull', false, null);
            }, stylePanel3);
        this.styleButtons([full,
            sub = this.editorUi.toolbar.addButton('geSprite-subscript',
                mxResources.get('subscript') + ' (' + Editor.ctrlKey + '+,)',
                function () {
                    document.execCommand('subscript', false, null);
                }, stylePanel3), sup = this.editorUi.toolbar.addButton('geSprite-superscript',
                mxResources.get('superscript') + ' (' + Editor.ctrlKey + '+.)',
                function () {
                    document.execCommand('superscript', false, null);
                }, stylePanel3)
        ]);
        full.style.marginRight = '9px';

        var tmp = stylePanel3.cloneNode(false);
        tmp.style.paddingTop = '4px';
        var btns = [this.editorUi.toolbar.addButton('geSprite-orderedlist', mxResources.get('numberedList'),
            function () {
                document.execCommand('insertorderedlist', false, null);
            }, tmp),
            this.editorUi.toolbar.addButton('geSprite-unorderedlist', mxResources.get('bulletedList'),
                function () {
                    document.execCommand('insertunorderedlist', false, null);
                }, tmp),
            this.editorUi.toolbar.addButton('geSprite-outdent', mxResources.get('decreaseIndent'),
                function () {
                    document.execCommand('outdent', false, null);
                }, tmp),
            this.editorUi.toolbar.addButton('geSprite-indent', mxResources.get('increaseIndent'),
                function () {
                    document.execCommand('indent', false, null);
                }, tmp),
            this.editorUi.toolbar.addButton('geSprite-code', mxResources.get('html'),
                function () {
                    graph.cellEditor.toggleViewMode();
                }, tmp)
        ];
        this.styleButtons(btns);
        btns[btns.length - 1].style.marginLeft = '9px';

        if (mxClient.IS_QUIRKS) {
            mxUtils.br(container);
            tmp.style.height = '40';
        }

        container.appendChild(tmp);
    } else {
        fontStyleItems[2].style.marginRight = '9px';
        right.style.marginRight = '9px';
    }

    // Label position
    var stylePanel4 = stylePanel.cloneNode(false);
    stylePanel4.style.marginLeft = '0px';
    stylePanel4.style.paddingTop = '8px';
    stylePanel4.style.paddingBottom = '4px';
    stylePanel4.style.fontWeight = 'normal';

    mxUtils.write(stylePanel4, mxResources.get('position'));

    // Adds label position options
    var positionSelect = document.createElement('select');
    positionSelect.style.position = 'absolute';
    positionSelect.style.right = '20px';
    positionSelect.style.width = '97px';
    positionSelect.style.marginTop = '-2px';

    var directions = ['topLeft', 'top', 'topRight', 'left', 'center', 'right', 'bottomLeft', 'bottom', 'bottomRight'];
    var lset = {
        'topLeft': [mxConstants.ALIGN_LEFT, mxConstants.ALIGN_TOP, mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_BOTTOM],
        'top': [mxConstants.ALIGN_CENTER, mxConstants.ALIGN_TOP, mxConstants.ALIGN_CENTER, mxConstants.ALIGN_BOTTOM],
        'topRight': [mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_TOP, mxConstants.ALIGN_LEFT, mxConstants.ALIGN_BOTTOM],
        'left': [mxConstants.ALIGN_LEFT, mxConstants.ALIGN_MIDDLE, mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_MIDDLE],
        'center': [mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE, mxConstants.ALIGN_CENTER, mxConstants.ALIGN_MIDDLE],
        'right': [mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_MIDDLE, mxConstants.ALIGN_LEFT, mxConstants.ALIGN_MIDDLE],
        'bottomLeft': [mxConstants.ALIGN_LEFT, mxConstants.ALIGN_BOTTOM, mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_TOP],
        'bottom': [mxConstants.ALIGN_CENTER, mxConstants.ALIGN_BOTTOM, mxConstants.ALIGN_CENTER, mxConstants.ALIGN_TOP],
        'bottomRight': [mxConstants.ALIGN_RIGHT, mxConstants.ALIGN_BOTTOM, mxConstants.ALIGN_LEFT, mxConstants.ALIGN_TOP]
    };

    for (var i = 0; i < directions.length; i++) {
        var positionOption = document.createElement('option');
        positionOption.setAttribute('value', directions[i]);
        mxUtils.write(positionOption, mxResources.get(directions[i]));
        positionSelect.appendChild(positionOption);
    }

    stylePanel4.appendChild(positionSelect);

    // Writing direction
    var stylePanel5 = stylePanel.cloneNode(false);
    stylePanel5.style.marginLeft = '0px';
    stylePanel5.style.paddingTop = '4px';
    stylePanel5.style.paddingBottom = '4px';
    stylePanel5.style.fontWeight = 'normal';


    mxUtils.write(stylePanel5, mxResources.get('writingDirection'));

    // Adds writing direction options
    // LATER: Handle reselect of same option in all selects (change event
    // is not fired for same option so have opened state on click) and
    // handle multiple different styles for current selection
    var dirSelect = document.createElement('select');
    dirSelect.style.position = 'absolute';
    dirSelect.style.right = '20px';
    dirSelect.style.width = '97px';
    dirSelect.style.marginTop = '-2px';


    // NOTE: For automatic we use the value null since automatic
    // requires the text to be non formatted and non-wrapped
    var dirs = ['automatic', 'leftToRight', 'rightToLeft'];
    var dirSet = {
        'automatic': null,
        'leftToRight': mxConstants.TEXT_DIRECTION_LTR,
        'rightToLeft': mxConstants.TEXT_DIRECTION_RTL
    };

    for (var i = 0; i < dirs.length; i++) {
        var dirOption = document.createElement('option');
        dirOption.setAttribute('value', dirs[i]);
        mxUtils.write(dirOption, mxResources.get(dirs[i]));
        dirSelect.appendChild(dirOption);
    }

    stylePanel5.appendChild(dirSelect);


    // stylePanel5.style.background ='red'

    if (!graph.isEditing()) {
        container.appendChild(stylePanel4);

        mxEvent.addListener(positionSelect, 'change', function (evt) {
            graph.getModel().beginUpdate();
            try {
                var vals = lset[positionSelect.value];

                if (vals != null) {
                    graph.setCellStyles(mxConstants.STYLE_LABEL_POSITION, vals[0], graph.getSelectionCells());
                    graph.setCellStyles(mxConstants.STYLE_VERTICAL_LABEL_POSITION, vals[1], graph.getSelectionCells());
                    graph.setCellStyles(mxConstants.STYLE_ALIGN, vals[2], graph.getSelectionCells());
                    graph.setCellStyles(mxConstants.STYLE_VERTICAL_ALIGN, vals[3], graph.getSelectionCells());
                }
            } finally {
                graph.getModel().endUpdate();
            }

            mxEvent.consume(evt);
        });

        // LATER: Update dir in text editor while editing and update style with label
        // NOTE: The tricky part is handling and passing on the auto value
        container.appendChild(stylePanel5);
        // container.style.background = 'red'

        mxEvent.addListener(dirSelect, 'change', function (evt) {
            graph.setCellStyles(mxConstants.STYLE_TEXT_DIRECTION, dirSet[dirSelect.value], graph.getSelectionCells());
            mxEvent.consume(evt);
        });
    }

    // Font size
    var input = document.createElement('input');
    input.style.textAlign = 'center';
    input.style.marginTop = '4px';

    if (!mxClient.IS_QUIRKS) {
        input.style.position = 'absolute';
        input.style.right = '32px';
    }

    input.style.width = '46px';
    input.style.height = (mxClient.IS_QUIRKS) ? '21px' : '19px';
    stylePanel2.appendChild(input);
    // Workaround for font size 4 if no text is selected is update font size below
    // after first character was entered (as the font element is lazy created)
    var pendingFontSize = null;

    var inputUpdate = this.installInputHandler(input, mxConstants.STYLE_FONTSIZE, Menus.prototype.defaultFontSize, 1, 999, ' pt',
        function (fontSize) {
            // IE does not support containsNode
            // KNOWN: Fixes font size issues but bypasses undo
            if (window.getSelection && !mxClient.IS_IE && !mxClient.IS_IE11) {
                var selection = window.getSelection();
                var container = (selection.rangeCount > 0) ? selection.getRangeAt(0).commonAncestorContainer :
                    graph.cellEditor.textarea;

                function updateSize(elt, ignoreContains) {
                    if (elt != graph.cellEditor.textarea && graph.cellEditor.textarea.contains(elt) &&
                        (ignoreContains || selection.containsNode(elt, true))) {
                        if (elt.nodeName == 'FONT') {
                            elt.removeAttribute('size');
                            elt.style.fontSize = fontSize + 'px';
                        } else {
                            var css = mxUtils.getCurrentStyle(elt);

                            if (css.fontSize != fontSize + 'px') {
                                if (mxUtils.getCurrentStyle(elt.parentNode).fontSize != fontSize + 'px') {
                                    elt.style.fontSize = fontSize + 'px';
                                } else {
                                    elt.style.fontSize = '';
                                }
                            }
                        }
                    }
                };

                // Wraps text node or mixed selection with leading text in a font element
                if (container == graph.cellEditor.textarea ||
                    container.nodeType != mxConstants.NODETYPE_ELEMENT) {
                    document.execCommand('fontSize', false, '1');
                }

                if (container != graph.cellEditor.textarea) {
                    container = container.parentNode;
                }

                if (container.nodeType == mxConstants.NODETYPE_ELEMENT) {
                    var elts = container.getElementsByTagName('*');
                    updateSize(container);

                    for (var i = 0; i < elts.length; i++) {
                        updateSize(elts[i]);
                    }
                }

                input.value = fontSize + ' pt';
            } else if (window.getSelection || document.selection) {
                // Checks selection
                var par = null;

                if (document.selection) {
                    par = document.selection.createRange().parentElement();
                } else {
                    var selection = window.getSelection();

                    if (selection.rangeCount > 0) {
                        par = selection.getRangeAt(0).commonAncestorContainer;
                    }
                }

                // Node.contains does not work for text nodes in IE11
                function isOrContains(container, node) {
                    while (node != null) {
                        if (node === container) {
                            return true;
                        }

                        node = node.parentNode;
                    }

                    return false;
                };

                if (par != null && isOrContains(graph.cellEditor.textarea, par)) {
                    pendingFontSize = fontSize;

                    // Workaround for can't set font size in px is to change font size afterwards
                    document.execCommand('fontSize', false, '4');
                    var elts = graph.cellEditor.textarea.getElementsByTagName('font');

                    for (var i = 0; i < elts.length; i++) {
                        if (elts[i].getAttribute('size') == '4') {
                            elts[i].removeAttribute('size');
                            elts[i].style.fontSize = pendingFontSize + 'px';

                            // Overrides fontSize in input with the one just assigned as a workaround
                            // for potential fontSize values of parent elements that don't match
                            window.setTimeout(function () {
                                input.value = pendingFontSize + ' pt';
                                pendingFontSize = null;
                            }, 0);

                            break;
                        }
                    }
                }
            }
        }, true);

    var stepper = this.createStepper(input, inputUpdate, 1, 10, true, Menus.prototype.defaultFontSize);
    stepper.style.display = input.style.display;
    stepper.style.marginTop = '4px';

    if (!mxClient.IS_QUIRKS) { // true
        stepper.style.right = '20px';
    }

    stylePanel2.appendChild(stepper);

    var arrow = fontMenu.getElementsByTagName('div')[0];
    arrow.style.cssFloat = 'right';

    var bgColorApply = null;
    var currentBgColor = '#ffffff';

    var fontColorApply = null;
    var currentFontColor = '#000000';

    var bgPanel = (graph.cellEditor.isContentEditing()) ? this.createColorOption(mxResources.get('backgroundColor'), function () { // 隐藏文字背景颜色
        return currentBgColor;
    }, function (color) {
        document.execCommand('backcolor', false, (color != mxConstants.NONE) ? color : 'transparent');
    }, '#ffffff', {
        install: function (apply) {
            bgColorApply = apply;
        },
        destroy: function () {
            bgColorApply = null;
        }
    }, null, true) : this.createCellColorOption(mxResources.get('backgroundColor'), mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, '#ffffff', null, function (color) {
        graph.updateLabelElements(graph.getSelectionCells(), function (elt) {
            elt.style.backgroundColor = null;
        });
    });
    bgPanel.style.fontWeight = 'bold';

    var borderPanel = this.createCellColorOption(mxResources.get('borderColor'), mxConstants.STYLE_LABEL_BORDERCOLOR, '#000000'); // 隐藏文字边框颜色
    borderPanel.style.fontWeight = 'bold';

    var panel = (graph.cellEditor.isContentEditing()) ? this.createColorOption(mxResources.get('fontColor'), function () { // 字体颜色
        return currentFontColor;
    }, function (color) {
        document.execCommand('forecolor', false, (color != mxConstants.NONE) ? color : 'transparent');
    }, '#000000', {
        install: function (apply) {
            fontColorApply = apply;
        },
        destroy: function () {
            fontColorApply = null;
        }
    }, null, true) : this.createCellColorOption(mxResources.get('fontColor'), mxConstants.STYLE_FONTCOLOR, '#000000', function (color) {
        if (color == null || color == mxConstants.NONE) {
            bgPanel.style.display = 'none';
        } else {
            bgPanel.style.display = '';
        }

        borderPanel.style.display = bgPanel.style.display;
    }, function (color) {
        if (color == null || color == mxConstants.NONE) {
            graph.setCellStyles(mxConstants.STYLE_NOLABEL, '1', graph.getSelectionCells());
        } else {
            graph.setCellStyles(mxConstants.STYLE_NOLABEL, null, graph.getSelectionCells());
        }

        graph.updateLabelElements(graph.getSelectionCells(), function (elt) {
            elt.removeAttribute('color');
            elt.style.color = null;
        });
    });
    panel.style.fontWeight = 'bold';


    // colorPanel.appendChild(panel); // 隐藏字体颜色
    // colorPanel.appendChild(bgPanel); // 隐藏文字背景颜色

    // if (!graph.cellEditor.isContentEditing()) {
    //   colorPanel.appendChild(borderPanel); // 隐藏文字边框颜色
    // }

    // colorPanel.style.background= 'red'
    // container.appendChild(colorPanel); // 隐藏文字颜色

    var extraPanel = this.createPanel();
    extraPanel.style.paddingTop = '2px';
    extraPanel.style.paddingBottom = '4px';

    // LATER: Fix toggle using '' instead of 'null'
    var wwOpt = this.createCellOption(mxResources.get('wordWrap'), mxConstants.STYLE_WHITE_SPACE, null, 'wrap', 'null', null, null, true); // 隐藏文字自动换行
    wwOpt.style.fontWeight = 'bold';

    // Word wrap in edge labels only supported via labelWidth style
    if (!ss.containsLabel && !ss.autoSize && ss.edges.length == 0) { // 隐藏文字自动换行
        extraPanel.appendChild(wwOpt);
    }

    // Delegates switch of style to formattedText action as it also convertes newlines
    // var htmlOpt = this.createCellOption(mxResources.get('formattedText'), 'html', '0', // 隐藏已格式化文本
    //   null, null, null, ui.actions.get('formattedText'));
    // htmlOpt.style.fontWeight = 'bold';
    // extraPanel.appendChild(htmlOpt);

    // var spacingPanel = this.createPanel();  // 把sapcingPanel  间距 全部隐藏掉
    // spacingPanel.style.paddingTop = '10px';
    // spacingPanel.style.paddingBottom = '28px';
    // spacingPanel.style.fontWeight = 'normal';
    // spacingPanel.style.background = 'pink';

    // var span = document.createElement('div');
    // span.style.position = 'absolute';
    // span.style.width = '70px';
    // span.style.background = 'green';
    // span.style.marginTop = '0px';
    // span.style.fontWeight = 'bold';
    // mxUtils.write(span, mxResources.get('spacing'));
    // spacingPanel.appendChild(span);

    // var topUpdate, globalUpdate, leftUpdate, bottomUpdate, rightUpdate;
    // var topSpacing = this.addUnitInput(spacingPanel, 'pt', 91, 44, function () {
    //   topUpdate.apply(this, arguments);
    // });
    // var globalSpacing = this.addUnitInput(spacingPanel, 'pt', 20, 44, function () {
    //   globalUpdate.apply(this, arguments);
    // });

    // mxUtils.br(spacingPanel);
    // this.addLabel(spacingPanel, mxResources.get('top'), 91);
    // this.addLabel(spacingPanel, mxResources.get('global'), 20);
    // mxUtils.br(spacingPanel);
    // mxUtils.br(spacingPanel);

    // var leftSpacing = this.addUnitInput(spacingPanel, 'pt', 162, 44, function () {
    //   leftUpdate.apply(this, arguments);
    // });
    // var bottomSpacing = this.addUnitInput(spacingPanel, 'pt', 91, 44, function () {
    //   bottomUpdate.apply(this, arguments);
    // });
    // var rightSpacing = this.addUnitInput(spacingPanel, 'pt', 20, 44, function () {
    //   rightUpdate.apply(this, arguments);
    // });

    // mxUtils.br(spacingPanel);
    // this.addLabel(spacingPanel, mxResources.get('left'), 162);
    // this.addLabel(spacingPanel, mxResources.get('bottom'), 91);
    // this.addLabel(spacingPanel, mxResources.get('right'), 20);

    if (!graph.cellEditor.isContentEditing()) {
        container.appendChild(extraPanel);
        // container.appendChild(this.createRelativeOption(mxResources.get('opacity'), mxConstants.STYLE_TEXT_OPACITY)); // 隐藏文本透明度
        // container.appendChild(spacingPanel); // 隐藏间距
    } else {
        var selState = null;
        var lineHeightInput = null;

        // container.appendChild(this.createRelativeOption(mxResources.get('lineheight'), null, null, function (input) { // 隐藏线高
        //   var value = (input.value == '') ? 120 : parseInt(input.value);
        //   value = Math.max(0, (isNaN(value)) ? 120 : value);

        //   if (selState != null) {
        //     graph.cellEditor.restoreSelection(selState);
        //     selState = null;
        //   }

        //   var selectedElement = graph.getSelectedElement();
        //   var node = selectedElement;

        //   while (node != null && node.nodeType != mxConstants.NODETYPE_ELEMENT) {
        //     node = node.parentNode;
        //   }

        //   if (node != null && node == graph.cellEditor.textarea && graph.cellEditor.textarea.firstChild != null) {
        //     if (graph.cellEditor.textarea.firstChild.nodeName != 'P') {
        //       graph.cellEditor.textarea.innerHTML = '<p>' + graph.cellEditor.textarea.innerHTML + '</p>';
        //     }

        //     node = graph.cellEditor.textarea.firstChild;
        //   }

        //   if (node != null && node != graph.cellEditor.textarea && graph.cellEditor.textarea.contains(node)) {
        //     node.style.lineHeight = value + '%';
        //   }

        //   input.value = value + ' %';
        // }, function (input) {
        //   // Used in CSS handler to update current value
        //   lineHeightInput = input;

        //   // KNOWN: Arrow up/down clear selection text in quirks/IE 8
        //   // Text size via arrow button limits to 16 in IE11. Why?
        //   mxEvent.addListener(input, 'mousedown', function () {
        //     if (document.activeElement == graph.cellEditor.textarea) {
        //       selState = graph.cellEditor.saveSelection();
        //     }
        //   });

        //   mxEvent.addListener(input, 'touchstart', function () {
        //     if (document.activeElement == graph.cellEditor.textarea) {
        //       selState = graph.cellEditor.saveSelection();
        //     }
        //   });

        //   input.value = '120 %';
        // }));

        var insertPanel = stylePanel.cloneNode(false);
        insertPanel.style.paddingLeft = '0px';
        var insertBtns = this.editorUi.toolbar.addItems(['link', 'image'], insertPanel, true);

        var btns = [
            this.editorUi.toolbar.addButton('geSprite-horizontalrule', mxResources.get('insertHorizontalRule'),
                function () {
                    document.execCommand('inserthorizontalrule', false);
                }, insertPanel),
            this.editorUi.toolbar.addMenuFunctionInContainer(insertPanel, 'geSprite-table', mxResources.get('table'), false, mxUtils.bind(this, function (menu) {
                this.editorUi.menus.addInsertTableItem(menu);
            }))
        ];
        this.styleButtons(insertBtns);
        this.styleButtons(btns);

        var wrapper2 = this.createPanel();
        wrapper2.style.paddingTop = '10px';
        wrapper2.style.paddingBottom = '10px';
        wrapper2.appendChild(this.createTitle(mxResources.get('insert')));
        wrapper2.appendChild(insertPanel);
        // container.appendChild(wrapper2); // 隐藏文本框的插入

        if (mxClient.IS_QUIRKS) {
            wrapper2.style.height = '70';
        }

        var tablePanel = stylePanel.cloneNode(false);
        tablePanel.style.paddingLeft = '0px';


        var btns = [
            this.editorUi.toolbar.addButton('geSprite-insertcolumnbefore', mxResources.get('insertColumnBefore'),
                mxUtils.bind(this, function () {
                    try {
                        if (currentTable != null) {
                            graph.selectNode(graph.insertColumn(currentTable, (tableCell != null) ? tableCell.cellIndex : 0));
                        }
                    } catch (e) {
                        this.editorUi.handleError(e);
                    }
                }), tablePanel),
            this.editorUi.toolbar.addButton('geSprite-insertcolumnafter', mxResources.get('insertColumnAfter'),
                mxUtils.bind(this, function () {
                    try {
                        if (currentTable != null) {
                            graph.selectNode(graph.insertColumn(currentTable, (tableCell != null) ? tableCell.cellIndex + 1 : -1));
                        }
                    } catch (e) {
                        this.editorUi.handleError(e);
                    }
                }), tablePanel),
            this.editorUi.toolbar.addButton('geSprite-deletecolumn', mxResources.get('deleteColumn'),
                mxUtils.bind(this, function () {
                    try {
                        if (currentTable != null && tableCell != null) {
                            graph.deleteColumn(currentTable, tableCell.cellIndex);
                        }
                    } catch (e) {
                        this.editorUi.handleError(e);
                    }
                }), tablePanel),
            this.editorUi.toolbar.addButton('geSprite-insertrowbefore', mxResources.get('insertRowBefore'),
                mxUtils.bind(this, function () {
                    try {
                        if (currentTable != null && tableRow != null) {
                            graph.selectNode(graph.insertRow(currentTable, tableRow.sectionRowIndex));
                        }
                    } catch (e) {
                        this.editorUi.handleError(e);
                    }
                }), tablePanel),
            this.editorUi.toolbar.addButton('geSprite-insertrowafter', mxResources.get('insertRowAfter'),
                mxUtils.bind(this, function () {
                    try {
                        if (currentTable != null && tableRow != null) {
                            graph.selectNode(graph.insertRow(currentTable, tableRow.sectionRowIndex + 1));
                        }
                    } catch (e) {
                        this.editorUi.handleError(e);
                    }
                }), tablePanel),
            this.editorUi.toolbar.addButton('geSprite-deleterow', mxResources.get('deleteRow'),
                mxUtils.bind(this, function () {
                    try {
                        if (currentTable != null && tableRow != null) {
                            graph.deleteRow(currentTable, tableRow.sectionRowIndex);
                        }
                    } catch (e) {
                        this.editorUi.handleError(e);
                    }
                }), tablePanel)
        ];
        this.styleButtons(btns);
        btns[2].style.marginRight = '9px';

        var wrapper3 = this.createPanel();
        wrapper3.style.paddingTop = '10px';
        wrapper3.style.paddingBottom = '10px';
        wrapper3.appendChild(this.createTitle(mxResources.get('table')));
        wrapper3.appendChild(tablePanel);

        if (mxClient.IS_QUIRKS) {
            mxUtils.br(container);
            wrapper3.style.height = '70';
        }

        var tablePanel2 = stylePanel.cloneNode(false);
        tablePanel2.style.paddingLeft = '0px';
        // tablePanel2.style.background = 'red';

        var btns = [
            this.editorUi.toolbar.addButton('geSprite-strokecolor', mxResources.get('borderColor'),
                mxUtils.bind(this, function () {
                    if (currentTable != null) {
                        // Converts rgb(r,g,b) values
                        var color = currentTable.style.borderColor.replace(
                            /\brgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/g,
                            function ($0, $1, $2, $3) {
                                return "#" + ("0" + Number($1).toString(16)).substr(-2) + ("0" + Number($2).toString(16)).substr(-2) + ("0" + Number($3).toString(16)).substr(-2);
                            });
                        this.editorUi.pickColor(color, function (newColor) {
                            if (newColor == null || newColor == mxConstants.NONE) {
                                currentTable.removeAttribute('border');
                                currentTable.style.border = '';
                                currentTable.style.borderCollapse = '';
                            } else {
                                currentTable.setAttribute('border', '1');
                                currentTable.style.border = '1px solid ' + newColor;
                                currentTable.style.borderCollapse = 'collapse';
                            }
                        });
                    }
                }), tablePanel2),
            this.editorUi.toolbar.addButton('geSprite-fillcolor', mxResources.get('backgroundColor'),
                mxUtils.bind(this, function () {
                    // Converts rgb(r,g,b) values
                    if (currentTable != null) {
                        var color = currentTable.style.backgroundColor.replace(
                            /\brgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/g,
                            function ($0, $1, $2, $3) {
                                return "#" + ("0" + Number($1).toString(16)).substr(-2) + ("0" + Number($2).toString(16)).substr(-2) + ("0" + Number($3).toString(16)).substr(-2);
                            });
                        this.editorUi.pickColor(color, function (newColor) {
                            if (newColor == null || newColor == mxConstants.NONE) {
                                currentTable.style.backgroundColor = '';
                            } else {
                                currentTable.style.backgroundColor = newColor;
                            }
                        });
                    }
                }), tablePanel2),
            this.editorUi.toolbar.addButton('geSprite-fit', mxResources.get('spacing'),
                function () {
                    if (currentTable != null) {
                        var value = currentTable.getAttribute('cellPadding') || 0;

                        var dlg = new FilenameDialog(ui, value, mxResources.get('apply'), mxUtils.bind(this, function (newValue) {
                            if (newValue != null && newValue.length > 0) {
                                currentTable.setAttribute('cellPadding', newValue);
                            } else {
                                currentTable.removeAttribute('cellPadding');
                            }
                        }), mxResources.get('spacing'));
                        ui.showDialog(dlg.container, 300, 80, true, true);
                        dlg.init();
                    }
                }, tablePanel2),
            this.editorUi.toolbar.addButton('geSprite-left', mxResources.get('left'),
                function () {
                    if (currentTable != null) {
                        currentTable.setAttribute('align', 'left');
                    }
                }, tablePanel2),
            this.editorUi.toolbar.addButton('geSprite-center', mxResources.get('center'),
                function () {
                    if (currentTable != null) {
                        currentTable.setAttribute('align', 'center');
                    }
                }, tablePanel2),
            this.editorUi.toolbar.addButton('geSprite-right', mxResources.get('right'),
                function () {
                    if (currentTable != null) {
                        currentTable.setAttribute('align', 'right');
                    }
                }, tablePanel2)
        ];
        this.styleButtons(btns);
        btns[2].style.marginRight = '9px';

        if (mxClient.IS_QUIRKS) {
            mxUtils.br(wrapper3);
            mxUtils.br(wrapper3);
        }

        wrapper3.appendChild(tablePanel2);
        // wrapper3.style.background = 'red';
        container.appendChild(wrapper3);

        tableWrapper = wrapper3;
    }

    function setSelected(elt, selected) {
        if (mxClient.IS_IE && (mxClient.IS_QUIRKS || document.documentMode < 10)) {
            elt.style.filter = (selected) ? 'progid:DXImageTransform.Microsoft.Gradient(' +
                'StartColorStr=\'#c5ecff\', EndColorStr=\'#87d4fb\', GradientType=0)' : '';
        } else {
            elt.style.backgroundImage = (selected) ? 'linear-gradient(#c5ecff 0px,#87d4fb 100%)' : '';
        }
    };

    var listener = mxUtils.bind(this, function (sender, evt, force) {
        ss = this.format.getSelectionState();
        var fontStyle = mxUtils.getValue(ss.style, mxConstants.STYLE_FONTSTYLE, 0);
        setSelected(fontStyleItems[0], (fontStyle & mxConstants.FONT_BOLD) == mxConstants.FONT_BOLD);
        setSelected(fontStyleItems[1], (fontStyle & mxConstants.FONT_ITALIC) == mxConstants.FONT_ITALIC);
        setSelected(fontStyleItems[2], (fontStyle & mxConstants.FONT_UNDERLINE) == mxConstants.FONT_UNDERLINE);
        fontMenu.firstChild.nodeValue = mxUtils.htmlEntities(mxUtils.getValue(ss.style, mxConstants.STYLE_FONTFAMILY, Menus.prototype.defaultFont));

        setSelected(verticalItem, mxUtils.getValue(ss.style, mxConstants.STYLE_HORIZONTAL, '1') == '0');

        if (force || document.activeElement != input) {
            var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_FONTSIZE, Menus.prototype.defaultFontSize));
            input.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        var align = mxUtils.getValue(ss.style, mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
        setSelected(left, align == mxConstants.ALIGN_LEFT);
        setSelected(center, align == mxConstants.ALIGN_CENTER);
        setSelected(right, align == mxConstants.ALIGN_RIGHT);

        var valign = mxUtils.getValue(ss.style, mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
        setSelected(top, valign == mxConstants.ALIGN_TOP);
        setSelected(middle, valign == mxConstants.ALIGN_MIDDLE);
        setSelected(bottom, valign == mxConstants.ALIGN_BOTTOM);

        var pos = mxUtils.getValue(ss.style, mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
        var vpos = mxUtils.getValue(ss.style, mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);

        if (pos == mxConstants.ALIGN_LEFT && vpos == mxConstants.ALIGN_TOP) {
            positionSelect.value = 'topLeft';
        } else if (pos == mxConstants.ALIGN_CENTER && vpos == mxConstants.ALIGN_TOP) {
            positionSelect.value = 'top';
        } else if (pos == mxConstants.ALIGN_RIGHT && vpos == mxConstants.ALIGN_TOP) {
            positionSelect.value = 'topRight';
        } else if (pos == mxConstants.ALIGN_LEFT && vpos == mxConstants.ALIGN_BOTTOM) {
            positionSelect.value = 'bottomLeft';
        } else if (pos == mxConstants.ALIGN_CENTER && vpos == mxConstants.ALIGN_BOTTOM) {
            positionSelect.value = 'bottom';
        } else if (pos == mxConstants.ALIGN_RIGHT && vpos == mxConstants.ALIGN_BOTTOM) {
            positionSelect.value = 'bottomRight';
        } else if (pos == mxConstants.ALIGN_LEFT) {
            positionSelect.value = 'left';
        } else if (pos == mxConstants.ALIGN_RIGHT) {
            positionSelect.value = 'right';
        } else {
            positionSelect.value = 'center';
        }

        var dir = mxUtils.getValue(ss.style, mxConstants.STYLE_TEXT_DIRECTION, mxConstants.DEFAULT_TEXT_DIRECTION);

        if (dir == mxConstants.TEXT_DIRECTION_RTL) {
            dirSelect.value = 'rightToLeft';
        } else if (dir == mxConstants.TEXT_DIRECTION_LTR) {
            dirSelect.value = 'leftToRight';
        } else if (dir == mxConstants.TEXT_DIRECTION_AUTO) {
            dirSelect.value = 'automatic';
        }

        // if (force || document.activeElement != globalSpacing) { // 隐藏掉间距
        //   var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_SPACING, 2));
        //   globalSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        // }

        // if (force || document.activeElement != topSpacing) {
        //   var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_SPACING_TOP, 0));
        //   topSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        // }

        // if (force || document.activeElement != rightSpacing) {
        //   var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_SPACING_RIGHT, 0));
        //   rightSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        // }

        // if (force || document.activeElement != bottomSpacing) {
        //   var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_SPACING_BOTTOM, 0));
        //   bottomSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        // }

        // if (force || document.activeElement != leftSpacing) {
        //   var tmp = parseFloat(mxUtils.getValue(ss.style, mxConstants.STYLE_SPACING_LEFT, 0));
        //   leftSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        // }
    });

    // globalUpdate = this.installInputHandler(globalSpacing, mxConstants.STYLE_SPACING, 2, -999, 999, ' pt'); // 隐藏间距
    // topUpdate = this.installInputHandler(topSpacing, mxConstants.STYLE_SPACING_TOP, 0, -999, 999, ' pt');
    // rightUpdate = this.installInputHandler(rightSpacing, mxConstants.STYLE_SPACING_RIGHT, 0, -999, 999, ' pt');
    // bottomUpdate = this.installInputHandler(bottomSpacing, mxConstants.STYLE_SPACING_BOTTOM, 0, -999, 999, ' pt');
    // leftUpdate = this.installInputHandler(leftSpacing, mxConstants.STYLE_SPACING_LEFT, 0, -999, 999, ' pt');

    // this.addKeyHandler(input, listener);  // 隐藏间距
    // this.addKeyHandler(globalSpacing, listener);
    // this.addKeyHandler(topSpacing, listener);
    // this.addKeyHandler(rightSpacing, listener);
    // this.addKeyHandler(bottomSpacing, listener);
    // this.addKeyHandler(leftSpacing, listener);

    graph.getModel().addListener(mxEvent.CHANGE, listener);
    this.listeners.push({
        destroy: function () {
            graph.getModel().removeListener(listener);
        }
    });
    listener();

    if (graph.cellEditor.isContentEditing()) {
        var updating = false;

        var updateCssHandler = function () {
            if (!updating) {
                updating = true;

                window.setTimeout(function () {
                    var selectedElement = graph.getSelectedElement();
                    var node = selectedElement;

                    while (node != null && node.nodeType != mxConstants.NODETYPE_ELEMENT) {
                        node = node.parentNode;
                    }

                    if (node != null) {
                        // Workaround for commonAncestor on range in IE11 returning parent of common ancestor
                        if (node == graph.cellEditor.textarea && graph.cellEditor.textarea.children.length == 1 &&
                            graph.cellEditor.textarea.firstChild.nodeType == mxConstants.NODETYPE_ELEMENT) {
                            node = graph.cellEditor.textarea.firstChild;
                        }

                        function getRelativeLineHeight(fontSize, lineHeight, elt) {
                            if (elt.style.lineHeight.substring(elt.style.lineHeight.length - 1) == '%') {
                                return parseInt(elt.style.lineHeight) / 100;
                            } else {
                                return (lineHeight.substring(lineHeight.length - 2) == 'px') ?
                                    parseFloat(lineHeight) / fontSize : parseInt(lineHeight);
                            }
                        };

                        function getAbsoluteFontSize(fontSize) {
                            if (fontSize.substring(fontSize.length - 2) == 'px') {
                                return parseFloat(fontSize);
                            } else {
                                return mxConstants.DEFAULT_FONTSIZE;
                            }
                        }

                        //var realCss = mxUtils.getCurrentStyle(selectedElement);
                        var css = mxUtils.getCurrentStyle(node);
                        var fontSize = getAbsoluteFontSize(css.fontSize);
                        var lineHeight = getRelativeLineHeight(fontSize, css.lineHeight, node);

                        // Finds common font size
                        var elts = node.getElementsByTagName('*');

                        // IE does not support containsNode
                        if (elts.length > 0 && window.getSelection && !mxClient.IS_IE && !mxClient.IS_IE11) {
                            var selection = window.getSelection();

                            for (var i = 0; i < elts.length; i++) {
                                if (selection.containsNode(elts[i], true)) {
                                    temp = mxUtils.getCurrentStyle(elts[i]);
                                    fontSize = Math.max(getAbsoluteFontSize(temp.fontSize), fontSize);
                                    var lh = getRelativeLineHeight(fontSize, temp.lineHeight, elts[i]);

                                    if (lh != lineHeight || isNaN(lh)) {
                                        lineHeight = '';
                                    }
                                }
                            }
                        }

                        if (css != null) {
                            setSelected(fontStyleItems[0], css.fontWeight == 'bold' || graph.getParentByName(node, 'B', graph.cellEditor.textarea) != null);
                            setSelected(fontStyleItems[1], css.fontStyle == 'italic' || graph.getParentByName(node, 'I', graph.cellEditor.textarea) != null);
                            setSelected(fontStyleItems[2], graph.getParentByName(node, 'U', graph.cellEditor.textarea) != null);
                            setSelected(left, css.textAlign == 'left');
                            setSelected(center, css.textAlign == 'center');
                            setSelected(right, css.textAlign == 'right');
                            setSelected(full, css.textAlign == 'justify');
                            setSelected(sup, graph.getParentByName(node, 'SUP', graph.cellEditor.textarea) != null);
                            setSelected(sub, graph.getParentByName(node, 'SUB', graph.cellEditor.textarea) != null);

                            currentTable = graph.getParentByName(node, 'TABLE', graph.cellEditor.textarea);
                            tableRow = (currentTable == null) ? null : graph.getParentByName(node, 'TR', currentTable);
                            tableCell = (currentTable == null) ? null : graph.getParentByName(node, 'TD', currentTable);
                            tableWrapper.style.display = (currentTable != null) ? '' : 'none';

                            if (document.activeElement != input) {
                                if (node.nodeName == 'FONT' && node.getAttribute('size') == '4' &&
                                    pendingFontSize != null) {
                                    node.removeAttribute('size');
                                    node.style.fontSize = pendingFontSize + ' pt';
                                    pendingFontSize = null;
                                } else {
                                    input.value = (isNaN(fontSize)) ? '' : fontSize + ' pt';
                                }

                                var lh = parseFloat(lineHeight);

                                // if (!isNaN(lh)) {
                                //   lineHeightInput.value = Math.round(lh * 100) + ' %';
                                // } else {
                                //   lineHeightInput.value = '100 %';
                                // }
                            }

                            // Converts rgb(r,g,b) values
                            var color = css.color.replace(
                                /\brgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/g,
                                function ($0, $1, $2, $3) {
                                    return "#" + ("0" + Number($1).toString(16)).substr(-2) + ("0" + Number($2).toString(16)).substr(-2) + ("0" + Number($3).toString(16)).substr(-2);
                                });
                            var color2 = css.backgroundColor.replace(
                                /\brgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/g,
                                function ($0, $1, $2, $3) {
                                    return "#" + ("0" + Number($1).toString(16)).substr(-2) + ("0" + Number($2).toString(16)).substr(-2) + ("0" + Number($3).toString(16)).substr(-2);
                                });

                            // Updates the color picker for the current font
                            if (fontColorApply != null) {
                                if (color.charAt(0) == '#') {
                                    currentFontColor = color;
                                } else {
                                    currentFontColor = '#000000';
                                }

                                fontColorApply(currentFontColor, true);
                            }

                            if (bgColorApply != null) {
                                if (color2.charAt(0) == '#') {
                                    currentBgColor = color2;
                                } else {
                                    currentBgColor = null;
                                }

                                bgColorApply(currentBgColor, true);
                            }

                            // Workaround for firstChild is null or not an object
                            // in the log which seems to be IE8- only / 29.01.15
                            if (fontMenu.firstChild != null) {
                                // Strips leading and trailing quotes
                                var ff = css.fontFamily;

                                if (ff.charAt(0) == '\'') {
                                    ff = ff.substring(1);
                                }

                                if (ff.charAt(ff.length - 1) == '\'') {
                                    ff = ff.substring(0, ff.length - 1);
                                }

                                if (ff.charAt(0) == '"') {
                                    ff = ff.substring(1);
                                }

                                if (ff.charAt(ff.length - 1) == '"') {
                                    ff = ff.substring(0, ff.length - 1);
                                }

                                fontMenu.firstChild.nodeValue = ff;
                            }
                        }
                    }

                    updating = false;
                }, 0);
            }
        };

        mxEvent.addListener(graph.cellEditor.textarea, 'input', updateCssHandler)
        mxEvent.addListener(graph.cellEditor.textarea, 'touchend', updateCssHandler);
        mxEvent.addListener(graph.cellEditor.textarea, 'mouseup', updateCssHandler);
        mxEvent.addListener(graph.cellEditor.textarea, 'keyup', updateCssHandler);
        this.listeners.push({
            destroy: function () {
                // No need to remove listener since textarea is destroyed after edit
            }
        });
        updateCssHandler();
    }

    return container;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel = function (format, editorUi, container) {
    BaseFormatPanel.call(this, format, editorUi, container);
    this.init();
};

mxUtils.extend(StyleFormatPanel, BaseFormatPanel);

/**
 *
 */
StyleFormatPanel.prototype.defaultStrokeColor = 'black';

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.init = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();

    if (ss.containsImage && ss.vertices.length == 1 && ss.style.shape == 'image' &&
        ss.style.image != null && ss.style.image.substring(0, 19) == 'data:image/svg+xml;') {
        this.container.appendChild(this.addSvgStyles(this.createPanel()));
    }


    this.container.appendChild(this.addFill(this.createPanel()));

    this.container.appendChild(this.addStroke(this.createPanel()));
    // this.container.appendChild(this.addLineJumps(this.createPanel()));
    // var opacityPanel = this.createRelativeOption(mxResources.get('opacity'), mxConstants.STYLE_OPACITY, 41);
    // opacityPanel.style.paddingTop = '8px';
    // opacityPanel.style.paddingBottom = '8px';
    // opacityPanel.style.background = 'green';

    // this.container.appendChild(opacityPanel); // 隐藏元件的透明度
    this.container.appendChild(this.addEffects(this.createPanel()));
    // var opsPanel = this.addEditOps(this.createPanel());

    // if (opsPanel.firstChild != null) {
    //   mxUtils.br(opsPanel);
    // }
    // this.container.appendChild(opsPanel);

    // this.container.appendChild(this.addStyleOps(opsPanel));

    this.container.appendChild(this.showWarningColor());

};
StyleFormatPanel.prototype.showWarningColor = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();

    var warningDiv = document.createElement("div")
    if (ss.vertices.length > 0 && ss.style.warning == 'true') {
        var warningText = document.createTextNode("显示告警色")
        var warningSpan = document.createElement("span")
        var warningInput = document.createElement("input")
        warningSpan.appendChild(warningText)
        warningDiv.appendChild(warningInput)
        warningDiv.appendChild(warningSpan)
        warningInput.setAttribute('type', 'checkbox')
        warningSpan.style.fontWeight = 'bolder'
        if (ss.style.isShowAlarm == undefined) {
            warningInput.checked = false
        } else if (ss.style.isShowAlarm === 'true') {
            warningInput.checked = true
        } else if (ss.style.isShowAlarm === 'false') {
            warningInput.checked = false
        }

        warningDiv.style.marginTop = '8px'
        warningDiv.style.paddingLeft = '8px'
        warningDiv.style.marginBottom = '8px'
        this.container.appendChild(warningDiv)

        warningInput.addEventListener('change', function (e) {
            graph.setCellStyles('isShowAlarm', e.target.checked, graph.getSelectionCells());
        })
    }
    return warningDiv
}


/**
 * Use browser for parsing CSS.
 */
StyleFormatPanel.prototype.getCssRules = function (css) {
    var doc = document.implementation.createHTMLDocument('');
    var styleElement = document.createElement('style');

    mxUtils.setTextContent(styleElement, css);
    doc.body.appendChild(styleElement);

    return styleElement.sheet.cssRules;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.addSvgStyles = function (container) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var ss = this.format.getSelectionState();
    container.style.paddingTop = '6px';
    container.style.paddingBottom = '6px';
    container.style.fontWeight = 'bold';
    container.style.display = 'none';

    try {
        var exp = ss.style.editableCssRules;

        if (exp != null) {
            var regex = new RegExp(exp);

            var data = ss.style.image.substring(ss.style.image.indexOf(',') + 1);
            var xml = (window.atob) ? atob(data) : Base64.decode(data, true);
            var svg = mxUtils.parseXml(xml);

            if (svg != null) {
                var styles = svg.getElementsByTagName('style');

                for (var i = 0; i < styles.length; i++) {
                    var rules = this.getCssRules(mxUtils.getTextContent(styles[i]));

                    for (var j = 0; j < rules.length; j++) {
                        this.addSvgRule(container, rules[j], svg, styles[i], rules, j, regex);
                    }
                }
            }
        }
    } catch (e) {
        // ignore
    }

    return container;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.addSvgRule = function (container, rule, svg, styleElem, rules, ruleIndex, regex) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;

    if (regex.test(rule.selectorText)) {
        function rgb2hex(rgb) {
            rgb = rgb.match(/^rgba?[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?/i);

            return (rgb && rgb.length === 4) ? "#" +
                ("0" + parseInt(rgb[1], 10).toString(16)).slice(-2) +
                ("0" + parseInt(rgb[2], 10).toString(16)).slice(-2) +
                ("0" + parseInt(rgb[3], 10).toString(16)).slice(-2) : '';
        };

        var addStyleRule = mxUtils.bind(this, function (rule, key, label) {
            if (rule.style[key] != '') {
                var option = this.createColorOption(label + ' ' + rule.selectorText, function () {
                    return rgb2hex(rule.style[key]);
                }, function (color) {
                    rules[ruleIndex].style[key] = color;
                    var cssTxt = '';

                    for (var i = 0; i < rules.length; i++) {
                        cssTxt += rules[i].cssText + ' ';
                    }

                    styleElem.textContent = cssTxt;
                    var xml = mxUtils.getXml(svg.documentElement);

                    graph.setCellStyles(mxConstants.STYLE_IMAGE, 'data:image/svg+xml,' +
                        ((window.btoa) ? btoa(xml) : Base64.encode(xml, true)),
                        graph.getSelectionCells());
                }, '#ffffff', {
                    install: function (apply) {
                        // ignore
                    },
                    destroy: function () {
                        // ignore
                    }
                });

                container.appendChild(option);

                // Shows container if rules are added
                container.style.display = '';
            }
        });

        addStyleRule(rule, 'fill', mxResources.get('fill'));
        addStyleRule(rule, 'stroke', mxResources.get('line'));
    }
};

/**
 * Adds the label menu items to the given menu and parent.
 * 样式编辑
 */
StyleFormatPanel.prototype.addEditOps = function (div) {
    var ss = this.format.getSelectionState();
    var btn = null;

    // if (this.editorUi.editor.graph.getSelectionCount() == 1) {
    //   btn = mxUtils.button(mxResources.get('editStyle'), mxUtils.bind(this, function (evt) {
    //     this.editorUi.actions.get('editStyle').funct();
    //   }));
    //
    //   btn.setAttribute('title', mxResources.get('editStyle') + ' (' + this.editorUi.actions.get('editStyle').shortcut + ')');
    //   btn.style.width = '202px';
    //   btn.style.marginBottom = '2px';
    //
    //   div.appendChild(btn);
    // }
    // btn = mxUtils.button(mxResources.get('setBindNode'), mxUtils.bind(this, function (evt) {
    //   this.editorUi.actions.get('setBindNode').funct();
    // }));

    // btn.setAttribute('title', mxResources.get('setBindNode') + ' (' + this.editorUi.actions.get('setBindNode').shortcut + ')');
    // btn.style.width = '202px';
    // btn.style.marginBottom = '2px';

    // div.appendChild(btn);

    if (ss.vertices.length === 1 && ss.image) {
        // var btn2 = mxUtils.button(mxResources.get('editImage'), mxUtils.bind(this, function (evt) {
        //   this.editorUi.actions.get('image').funct();
        // }));

        var btn2 = mxUtils.button(mxResources.get('editImage'));
        btn2.setAttribute('title', mxResources.get('editImage'));
        btn2.style.marginBottom = '2px';
        if (btn == null) {
            btn2.style.width = '202px';
        } else {
            btn.style.width = '100px';
            btn2.style.width = '100px';
            btn2.style.marginLeft = '2px';
        }
        div.appendChild(btn2);
        div.style.height = '30px'
        //添加自定义上传节点图片按钮
        var graph = this.editorUi.editor.graph;
        var fileImage = document.createElement('input')
        fileImage.id = 'filecell'
        fileImage.setAttribute('type', 'file')
        fileImage.setAttribute('title', '')
        fileImage.style.cursor = 'pointer'
        fileImage.style.height = '23px'
        fileImage.style.width = '202px'
        fileImage.addEventListener('change', function (e) {
            var x = new FileReader;
            x.readAsDataURL(e.target.files[0])
            x.onloadend = function () {
                var style = ss.vertices[0].style.split(';')
                var newstyle = []
                style.forEach(function (val, index) {
                    if (val.indexOf('image=') >= 0) {
                        var imagestyle = style[index]
                        var imagearr = imagestyle.split('=')
                        imagearr[1] = x.result.replace(';', '')
                        val = imagearr.join('=')
                        newstyle.push(val)
                    } else {
                        newstyle.push(val)
                    }
                })
                newstyle = newstyle.join(';')
                graph.getModel().beginUpdate();
                try {
                    graph.getModel().setStyle(ss.vertices[0], newstyle)
                } finally {
                    graph.getModel().endUpdate();
                }
            }
        })
        var fileDiv = document.createElement('div')
        fileDiv.appendChild(fileImage)
        fileDiv.style.position = 'relative'
        fileDiv.style.top = '-26px'
        fileDiv.style.left = '0'
        fileDiv.style.opacity = '0'
        fileDiv.style.height = '23px'
        fileDiv.style.width = '202px'
        div.appendChild(fileDiv)

        return div;
    }
    div.style.display = 'none'
    return div;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.addFill = function (container) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var ss = this.format.getSelectionState();
    var sect = graph.getSelectionCells()
    container.style.paddingTop = '6px';
    container.style.paddingBottom = '6px';

    // Adds gradient direction option
    var gradientSelect = document.createElement('select');
    gradientSelect.style.position = 'absolute';
    gradientSelect.style.marginTop = '-2px';
    gradientSelect.style.right = (mxClient.IS_QUIRKS) ? '52px' : '72px';
    gradientSelect.style.width = '70px';


    if (ss && ss.style && ss.style.shape != "line") {
        container.appendChild(this.createColorPicker('fontColor'));
    }


    // Stops events from bubbling to color option event handler
    mxEvent.addListener(gradientSelect, 'click', function (evt) {

        mxEvent.consume(evt);
    });

    var gradientPanel = this.createCellColorOption(mxResources.get('gradient'), mxConstants.STYLE_GRADIENTCOLOR, '#ffffff', function (color) {
        if (color == null || color == mxConstants.NONE) {
            gradientSelect.style.display = 'none';
        } else {
            gradientSelect.style.display = '';
        }
    });

    var fillKey = (ss.style.shape == 'image') ? mxConstants.STYLE_IMAGE_BACKGROUND : mxConstants.STYLE_FILLCOLOR;
    var label = (ss.style.shape == 'image') ? mxResources.get('background') : mxResources.get('fill');

    var fillPanel = this.createCellColorOption(label, fillKey, '#ffffff');
    fillPanel.style.fontWeight = 'bold';

    var tmpColor = mxUtils.getValue(ss.style, fillKey, null);
    gradientPanel.style.display = (tmpColor != null && tmpColor != mxConstants.NONE &&
        ss.fill && ss.style.shape != 'image') ? '' : 'none';

    var directions = [mxConstants.DIRECTION_NORTH, mxConstants.DIRECTION_EAST,
        mxConstants.DIRECTION_SOUTH, mxConstants.DIRECTION_WEST
    ];

    for (var i = 0; i < directions.length; i++) {
        var gradientOption = document.createElement('option');
        gradientOption.setAttribute('value', directions[i]);
        mxUtils.write(gradientOption, mxResources.get(directions[i]));
        gradientSelect.appendChild(gradientOption);
    }

    gradientPanel.appendChild(gradientSelect);

    var listener = mxUtils.bind(this, function () {
        ss = this.format.getSelectionState();
        var value = mxUtils.getValue(ss.style, mxConstants.STYLE_GRADIENT_DIRECTION, mxConstants.DIRECTION_SOUTH);

        // Handles empty string which is not allowed as a value
        if (value == '') {
            value = mxConstants.DIRECTION_SOUTH;
        }

        gradientSelect.value = value;

        container.style.display = (ss.fill) ? '' : 'none';

        var fillColor = mxUtils.getValue(ss.style, mxConstants.STYLE_FILLCOLOR, null);

        if (!ss.fill || ss.containsImage || fillColor == null || fillColor == mxConstants.NONE || ss.style.shape == 'filledEdge') {
            gradientPanel.style.display = 'none';
        } else {
            gradientPanel.style.display = '';
        }
    });

    graph.getModel().addListener(mxEvent.CHANGE, listener);
    this.listeners.push({
        destroy: function () {
            graph.getModel().removeListener(listener);
        }
    });
    listener();

    mxEvent.addListener(gradientSelect, 'change', function (evt) {
        graph.setCellStyles(mxConstants.STYLE_GRADIENT_DIRECTION, gradientSelect.value, graph.getSelectionCells());
        mxEvent.consume(evt);
    });

    // if(sect.length>1) {
    //   container.appendChild(fillPanel); // 单选隐藏文本填充,多选显示
    // }
    if (sect.length <= 1) {
        if (ss.style.shape === 'text' || ss.style.shape === 'label') {
            container.appendChild(this.createColorPicker('fillColor'));
        } else if (ss.style.shape == 'image') {
            container.appendChild(this.createColorPicker('labelBackgroundColor'));
            container.appendChild(this.createColorPicker('imageBackground'));
        } else if (ss.style.shape == 'line') {
            container.style.display = 'none'
        }
    } else {
        if (ss.style.shape === 'image') {
            container.appendChild(this.createColorPicker('fontBg'));
            container.appendChild(this.createColorPicker('beiJing'));
        } else {
            container.appendChild(this.createColorPicker('tianChong'));
        }
    }
    // container.appendChild(gradientPanel);  // 隐藏渐变

    // Adds custom colors
    var custom = this.getCustomColors();

    for (var i = 0; i < custom.length; i++) {

        container.appendChild(this.createCellColorOption(custom[i].title, custom[i].key, custom[i].defaultValue));
    }

    return container;
};
// 创建选择颜色的div
StyleFormatPanel.prototype.createColorPicker = function (id) {

    var div = document.createElement('div')
    var colordiv = document.createElement('div')
    colordiv.id = id
    colordiv.style.display = 'inline-block'
    colordiv.style.width = '48px'
    colordiv.style.height = '24px'
    colordiv.style.lineHeight = '24px'
    colordiv.style.marginTop = '5px'
    colordiv.style.marginBottom = '5px'
    colordiv.style.marginLeft = '10px'
    var bigDiv = document.createElement('div')
    var colorP = document.createElement('p')

    colorP.style.margin = '0 100px 0 0'
    colorP.style.fontWeight = 'bolder'
    colorP.style.display = 'inline-block'

    if (id === 'tianChong' || id === 'bianKuang' || id === 'lianXian' || id === 'xian') {
        colorP.style.margin = '0 124px 0 0'
    }

    switch (id) {
        case 'imageBackground':
            colorP.innerText = '元件背景'
            break
        case 'labelBackgroundColor':
            colorP.innerText = '字体背景'
            break
        case 'fontColor':
            colorP.innerText = '字体颜色'
            break
        case 'imageBorder':
            colorP.innerText = '元件边框'
            break
        case 'fillColor': // 文本框填充色
            colorP.innerText = '文本填充'
            break
        case 'strokeColor': // 文本框颜色
            colorP.innerText = '文本边框'
            break
        case 'beiJing': // 元件背景颜色
            colorP.innerText = '元件背景'
            break
        case 'fontBg': // 元件字体背景
            colorP.innerText = '字体背景'
            break
        case 'tianChong': // 文本填充颜色
            colorP.innerText = '填充'
            break
        case 'bianKuang':
            colorP.innerText = '边框'
            break
        case 'lianXian':
            colorP.innerText = '线型'
            break
        case 'xian':
            colorP.innerText = '线型'
            break
    }

    bigDiv.appendChild(colorP)
    bigDiv.appendChild(colordiv)
    div.appendChild(bigDiv)

    return div
}
/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.getCustomColors = function () {
    var ss = this.format.getSelectionState();
    var result = [];

    if (ss.style.shape == 'swimlane') {
        result.push({
            title: mxResources.get('laneColor'),
            key: 'swimlaneFillColor',
            defaultValue: '#ffffff'
        });
    }

    return result;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.addStroke = function (container) { // 边框和线条
    var ui = this.editorUi;
    var graph = ui.editor.graph;
    var ss = this.format.getSelectionState();
    var sect = graph.getSelectionCells()
    container.style.paddingTop = '4px';
    container.style.paddingBottom = '4px';
    container.style.whiteSpace = 'normal';


    var colorPanel = document.createElement('div'); // 边框
    colorPanel.style.fontWeight = 'bold';

    // Adds gradient direction option
    var styleSelect = document.createElement('select');
    styleSelect.style.position = 'absolute';
    styleSelect.style.marginTop = '-2px';
    styleSelect.style.right = '72px';
    styleSelect.style.width = '80px';
    styleSelect.style.background = 'green';
    styleSelect.className = 'lineclass2'
    var styles = ['sharp', 'rounded', 'curved'];

    for (var i = 0; i < styles.length; i++) {
        var styleOption = document.createElement('option');
        styleOption.setAttribute('value', styles[i]);
        mxUtils.write(styleOption, mxResources.get(styles[i]));
        styleSelect.appendChild(styleOption);
    }

    mxEvent.addListener(styleSelect, 'change', function (evt) {
        graph.getModel().beginUpdate();
        try {
            var keys = [mxConstants.STYLE_ROUNDED, mxConstants.STYLE_CURVED];
            // Default for rounded is 1
            var values = ['0', null];

            if (styleSelect.value == 'rounded') {
                values = ['1', null];
            } else if (styleSelect.value == 'curved') {
                values = [null, '1'];
            }

            for (var i = 0; i < keys.length; i++) {
                graph.setCellStyles(keys[i], values[i], graph.getSelectionCells());
            }

            ui.fireEvent(new mxEventObject('styleChanged', 'keys', keys,
                'values', values, 'cells', graph.getSelectionCells()));
        } finally {
            graph.getModel().endUpdate();
        }

        mxEvent.consume(evt);
    });

    // Stops events from bubbling to color option event handler
    mxEvent.addListener(styleSelect, 'click', function (evt) {
        mxEvent.consume(evt);
    });

    var strokeKey = (ss.style.shape == 'image') ? mxConstants.STYLE_IMAGE_BORDER : mxConstants.STYLE_STROKECOLOR;
    var label = (ss.style.shape == 'image') ? mxResources.get('border') : mxResources.get('line');

    var lineColor = this.createCellColorOption(label, strokeKey, '#000000');

    lineColor.appendChild(styleSelect);
    // colorPanel.appendChild(lineColor); // 隐藏线颜色
    if (sect.length <= 1) {
        if (ss.style.shape === 'text' || ss.style.shape === 'label') {
            container.appendChild(this.createColorPicker('strokeColor'));
        } else if (ss.style.shape == 'connector' || ss.style.shape == 'line' || ss.style.shape == 'ellipse') {
            container.appendChild(this.createColorPicker('xian'));
        } else if (ss.style.shape == 'image') {
            container.appendChild(this.createColorPicker('imageBorder'));
        }
    } else {
        if (ss.style.shape === 'image') {
            container.appendChild(this.createColorPicker('bianKuang'));
        } else {
            container.appendChild(this.createColorPicker('lianXian'));
        }
    }

    // Used if only edges selected
    var stylePanel = colorPanel.cloneNode(false);
    stylePanel.style.fontWeight = 'normal';
    stylePanel.style.whiteSpace = 'nowrap';
    stylePanel.style.position = 'relative';
    stylePanel.style.paddingLeft = '10px'
    stylePanel.style.marginBottom = '2px';
    stylePanel.style.marginTop = '2px';
    stylePanel.className = 'geToolbarContainer line3';

    var addItem = mxUtils.bind(this, function (menu, width, cssName, keys, values) {
        var item = this.editorUi.menus.styleChange(menu, '', keys, values, 'geIcon', null);

        var pat = document.createElement('div');
        pat.style.width = width + 'px';
        pat.style.height = '1px';
        pat.style.borderBottom = '1px ' + cssName + ' ' + this.defaultStrokeColor;
        pat.style.paddingTop = '6px';


        item.firstChild.firstChild.style.padding = '0px 4px 0px 4px';
        item.firstChild.firstChild.style.width = width + 'px';
        item.firstChild.firstChild.appendChild(pat);

        return item;
    });


    var pattern = this.editorUi.toolbar.addMenuFunctionInContainer(stylePanel, 'geSprite-orthogonal', mxResources.get('pattern'), false, mxUtils.bind(this, function (menu) {
        addItem(menu, 75, 'solid', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], [null, null]).setAttribute('title', mxResources.get('solid'));
        addItem(menu, 75, 'dashed', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', null]).setAttribute('title', mxResources.get('dashed'));
        addItem(menu, 75, 'dotted', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', '1 1']).setAttribute('title', mxResources.get('dotted') + ' (1)');
        addItem(menu, 75, 'dotted', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', '1 2']).setAttribute('title', mxResources.get('dotted') + ' (2)');
        addItem(menu, 75, 'dotted', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', '1 4']).setAttribute('title', mxResources.get('dotted') + ' (3)');
    }));

    // Used for mixed selection (vertices and edges)
    var altStylePanel = stylePanel.cloneNode(false);

    var edgeShape = this.editorUi.toolbar.addMenuFunctionInContainer(altStylePanel, 'geSprite-connection', mxResources.get('connection'), false, mxUtils.bind(this, function (menu) {
        this.editorUi.menus.styleChange(menu, '', [mxConstants.STYLE_SHAPE, mxConstants.STYLE_STARTSIZE, mxConstants.STYLE_ENDSIZE, 'width'], [null, null, null, null], 'geIcon geSprite geSprite-connection', null, true).setAttribute('title', mxResources.get('line'));
        this.editorUi.menus.styleChange(menu, '', [mxConstants.STYLE_SHAPE, mxConstants.STYLE_STARTSIZE, mxConstants.STYLE_ENDSIZE, 'width'], ['link', null, null, null], 'geIcon geSprite geSprite-linkedge', null, true).setAttribute('title', mxResources.get('link'));
        this.editorUi.menus.styleChange(menu, '', [mxConstants.STYLE_SHAPE, mxConstants.STYLE_STARTSIZE, mxConstants.STYLE_ENDSIZE, 'width'], ['flexArrow', null, null, null], 'geIcon geSprite geSprite-arrow', null, true).setAttribute('title', mxResources.get('arrow'));
        this.editorUi.menus.styleChange(menu, '', [mxConstants.STYLE_SHAPE, mxConstants.STYLE_STARTSIZE, mxConstants.STYLE_ENDSIZE, 'width'], ['arrow', null, null, null], 'geIcon geSprite geSprite-simplearrow', null, true).setAttribute('title', mxResources.get('simpleArrow'));
    }));

    var altPattern = this.editorUi.toolbar.addMenuFunctionInContainer(altStylePanel, 'geSprite-orthogonal', mxResources.get('pattern'), false, mxUtils.bind(this, function (menu) {
        addItem(menu, 33, 'solid', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], [null, null]).setAttribute('title', mxResources.get('solid'));
        addItem(menu, 33, 'dashed', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', null]).setAttribute('title', mxResources.get('dashed'));
        addItem(menu, 33, 'dotted', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', '1 1']).setAttribute('title', mxResources.get('dotted') + ' (1)');
        addItem(menu, 33, 'dotted', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', '1 2']).setAttribute('title', mxResources.get('dotted') + ' (2)');
        addItem(menu, 33, 'dotted', [mxConstants.STYLE_DASHED, mxConstants.STYLE_DASH_PATTERN], ['1', '1 4']).setAttribute('title', mxResources.get('dotted') + ' (3)');
    }));

    var stylePanel2 = stylePanel.cloneNode(false);

    // Stroke width
    var input = document.createElement('input');
    input.style.textAlign = 'center';
    input.style.marginTop = '2px';
    input.style.width = '41px';
    input.setAttribute('title', mxResources.get('linewidth'));

    stylePanel.appendChild(input);

    var altInput = input.cloneNode(true);
    altStylePanel.appendChild(altInput);

    function update(evt) {
        // Maximum stroke width is 999
        var value = parseInt(input.value);
        value = Math.min(999, Math.max(1, (isNaN(value)) ? 1 : value));

        if (value != mxUtils.getValue(ss.style, mxConstants.STYLE_STROKEWIDTH, 1)) {
            graph.setCellStyles(mxConstants.STYLE_STROKEWIDTH, value, graph.getSelectionCells());
            ui.fireEvent(new mxEventObject('styleChanged', 'keys', [mxConstants.STYLE_STROKEWIDTH],
                'values', [value], 'cells', graph.getSelectionCells()));
        }

        input.value = value + ' pt';
        mxEvent.consume(evt);
    };

    function altUpdate(evt) {
        // Maximum stroke width is 999
        var value = parseInt(altInput.value);
        value = Math.min(999, Math.max(1, (isNaN(value)) ? 1 : value));

        if (value != mxUtils.getValue(ss.style, mxConstants.STYLE_STROKEWIDTH, 1)) {
            graph.setCellStyles(mxConstants.STYLE_STROKEWIDTH, value, graph.getSelectionCells());
            ui.fireEvent(new mxEventObject('styleChanged', 'keys', [mxConstants.STYLE_STROKEWIDTH],
                'values', [value], 'cells', graph.getSelectionCells()));
        }

        altInput.value = value + ' pt';
        mxEvent.consume(evt);
    };

    var stepper = this.createStepper(input, update, 1, 9);
    stepper.style.display = input.style.display;
    stepper.style.marginTop = '2px';
    stylePanel.appendChild(stepper);

    var altStepper = this.createStepper(altInput, altUpdate, 1, 9);
    altStepper.style.display = altInput.style.display;
    altStepper.style.marginTop = '2px';
    altStylePanel.appendChild(altStepper);

    if (!mxClient.IS_QUIRKS) {
        input.style.position = 'absolute';
        input.style.right = '32px';
        input.style.height = '15px';
        stepper.style.right = '20px';

        altInput.style.position = 'absolute';
        altInput.style.right = '32px';
        altInput.style.height = '15px';
        altStepper.style.right = '20px';
    } else {
        input.style.height = '17px';
        altInput.style.height = '17px';
    }

    mxEvent.addListener(input, 'blur', update);
    mxEvent.addListener(input, 'change', update);

    mxEvent.addListener(altInput, 'blur', altUpdate);
    mxEvent.addListener(altInput, 'change', altUpdate);

    if (mxClient.IS_QUIRKS) {
        mxUtils.br(stylePanel2);
        mxUtils.br(stylePanel2);
    }

    var edgeStyle = this.editorUi.toolbar.addMenuFunctionInContainer(stylePanel2, 'geSprite-orthogonal', mxResources.get('waypoints'), false, mxUtils.bind(this, function (menu) {
        if (ss.style.shape != 'arrow') {
            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], [null, null, null], 'geIcon geSprite geSprite-straight', null, true).setAttribute('title', mxResources.get('straight'));
            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['orthogonalEdgeStyle', null, null], 'geIcon geSprite geSprite-orthogonal', null, true).setAttribute('title', mxResources.get('orthogonal'));
            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['elbowEdgeStyle', null, null, null], 'geIcon geSprite geSprite-horizontalelbow', null, true).setAttribute('title', mxResources.get('simple'));
            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['elbowEdgeStyle', 'vertical', null, null], 'geIcon geSprite geSprite-verticalelbow', null, true).setAttribute('title', mxResources.get('simple'));
            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['isometricEdgeStyle', null, null, null], 'geIcon geSprite geSprite-horizontalisometric', null, true).setAttribute('title', mxResources.get('isometric'));
            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['isometricEdgeStyle', 'vertical', null, null], 'geIcon geSprite geSprite-verticalisometric', null, true).setAttribute('title', mxResources.get('isometric'));

            if (ss.style.shape == 'connector') {
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['orthogonalEdgeStyle', '1', null], 'geIcon geSprite geSprite-curved', null, true).setAttribute('title', mxResources.get('curved'));
            }

            this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['entityRelationEdgeStyle', null, null], 'geIcon geSprite geSprite-entity', null, true).setAttribute('title', mxResources.get('entityRelation'));
        }
    }));

    var lineStart = this.editorUi.toolbar.addMenuFunctionInContainer(stylePanel2, 'geSprite-startclassic', mxResources.get('linestart'), false, mxUtils.bind(this, function (menu) {
        if (ss.style.shape == 'connector' || ss.style.shape == 'flexArrow' || ss.style.shape == 'filledEdge') {
            var item = this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.NONE, 0], 'geIcon', null, false);
            item.setAttribute('title', mxResources.get('none'));
            item.firstChild.firstChild.innerHTML = '<font style="font-size:10px;">' + mxUtils.htmlEntities(mxResources.get('none')) + '</font>';

            if (ss.style.shape == 'connector' || ss.style.shape == 'filledEdge') {
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_CLASSIC, 1], 'geIcon geSprite geSprite-startclassic', null, false).setAttribute('title', mxResources.get('classic'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_CLASSIC_THIN, 1], 'geIcon geSprite geSprite-startclassicthin', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_OPEN, 0], 'geIcon geSprite geSprite-startopen', null, false).setAttribute('title', mxResources.get('openArrow'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_OPEN_THIN, 0], 'geIcon geSprite geSprite-startopenthin', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['openAsync', 0], 'geIcon geSprite geSprite-startopenasync', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_BLOCK, 1], 'geIcon geSprite geSprite-startblock', null, false).setAttribute('title', mxResources.get('block'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_BLOCK_THIN, 1], 'geIcon geSprite geSprite-startblockthin', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['async', 1], 'geIcon geSprite geSprite-startasync', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_OVAL, 1], 'geIcon geSprite geSprite-startoval', null, false).setAttribute('title', mxResources.get('oval'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_DIAMOND, 1], 'geIcon geSprite geSprite-startdiamond', null, false).setAttribute('title', mxResources.get('diamond'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_DIAMOND_THIN, 1], 'geIcon geSprite geSprite-startthindiamond', null, false).setAttribute('title', mxResources.get('diamondThin'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_CLASSIC, 0], 'geIcon geSprite geSprite-startclassictrans', null, false).setAttribute('title', mxResources.get('classic'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_CLASSIC_THIN, 0], 'geIcon geSprite geSprite-startclassicthintrans', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_BLOCK, 0], 'geIcon geSprite geSprite-startblocktrans', null, false).setAttribute('title', mxResources.get('block'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_BLOCK_THIN, 0], 'geIcon geSprite geSprite-startblockthintrans', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['async', 0], 'geIcon geSprite geSprite-startasynctrans', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_OVAL, 0], 'geIcon geSprite geSprite-startovaltrans', null, false).setAttribute('title', mxResources.get('oval'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_DIAMOND, 0], 'geIcon geSprite geSprite-startdiamondtrans', null, false).setAttribute('title', mxResources.get('diamond'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], [mxConstants.ARROW_DIAMOND_THIN, 0], 'geIcon geSprite geSprite-startthindiamondtrans', null, false).setAttribute('title', mxResources.get('diamondThin'));

                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['dash', 0], 'geIcon geSprite geSprite-startdash', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['cross', 0], 'geIcon geSprite geSprite-startcross', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['circlePlus', 0], 'geIcon geSprite geSprite-startcircleplus', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['circle', 1], 'geIcon geSprite geSprite-startcircle', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['ERone', 0], 'geIcon geSprite geSprite-starterone', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['ERmandOne', 0], 'geIcon geSprite geSprite-starteronetoone', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['ERmany', 0], 'geIcon geSprite geSprite-startermany', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['ERoneToMany', 0], 'geIcon geSprite geSprite-starteronetomany', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['ERzeroToOne', 1], 'geIcon geSprite geSprite-starteroneopt', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW, 'startFill'], ['ERzeroToMany', 1], 'geIcon geSprite geSprite-startermanyopt', null, false);
            } else {
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_STARTARROW], [mxConstants.ARROW_BLOCK], 'geIcon geSprite geSprite-startblocktrans', null, false).setAttribute('title', mxResources.get('block'));
            }
        }
    }));

    var lineEnd = this.editorUi.toolbar.addMenuFunctionInContainer(stylePanel2, 'geSprite-endclassic', mxResources.get('lineend'), false, mxUtils.bind(this, function (menu) {
        if (ss.style.shape == 'connector' || ss.style.shape == 'flexArrow' || ss.style.shape == 'filledEdge') {
            var item = this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.NONE, 0], 'geIcon', null, false);
            item.setAttribute('title', mxResources.get('none'));
            item.firstChild.firstChild.innerHTML = '<font style="font-size:10px;">' + mxUtils.htmlEntities(mxResources.get('none')) + '</font>';

            if (ss.style.shape == 'connector' || ss.style.shape == 'filledEdge') {
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_CLASSIC, 1], 'geIcon geSprite geSprite-endclassic', null, false).setAttribute('title', mxResources.get('classic'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_CLASSIC_THIN, 1], 'geIcon geSprite geSprite-endclassicthin', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_OPEN, 0], 'geIcon geSprite geSprite-endopen', null, false).setAttribute('title', mxResources.get('openArrow'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_OPEN_THIN, 0], 'geIcon geSprite geSprite-endopenthin', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['openAsync', 0], 'geIcon geSprite geSprite-endopenasync', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_BLOCK, 1], 'geIcon geSprite geSprite-endblock', null, false).setAttribute('title', mxResources.get('block'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_BLOCK_THIN, 1], 'geIcon geSprite geSprite-endblockthin', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['async', 1], 'geIcon geSprite geSprite-endasync', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_OVAL, 1], 'geIcon geSprite geSprite-endoval', null, false).setAttribute('title', mxResources.get('oval'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_DIAMOND, 1], 'geIcon geSprite geSprite-enddiamond', null, false).setAttribute('title', mxResources.get('diamond'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_DIAMOND_THIN, 1], 'geIcon geSprite geSprite-endthindiamond', null, false).setAttribute('title', mxResources.get('diamondThin'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_CLASSIC, 0], 'geIcon geSprite geSprite-endclassictrans', null, false).setAttribute('title', mxResources.get('classic'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_CLASSIC_THIN, 0], 'geIcon geSprite geSprite-endclassicthintrans', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_BLOCK, 0], 'geIcon geSprite geSprite-endblocktrans', null, false).setAttribute('title', mxResources.get('block'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_BLOCK_THIN, 0], 'geIcon geSprite geSprite-endblockthintrans', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['async', 0], 'geIcon geSprite geSprite-endasynctrans', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_OVAL, 0], 'geIcon geSprite geSprite-endovaltrans', null, false).setAttribute('title', mxResources.get('oval'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_DIAMOND, 0], 'geIcon geSprite geSprite-enddiamondtrans', null, false).setAttribute('title', mxResources.get('diamond'));
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], [mxConstants.ARROW_DIAMOND_THIN, 0], 'geIcon geSprite geSprite-endthindiamondtrans', null, false).setAttribute('title', mxResources.get('diamondThin'));

                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['dash', 0], 'geIcon geSprite geSprite-enddash', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['cross', 0], 'geIcon geSprite geSprite-endcross', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['circlePlus', 0], 'geIcon geSprite geSprite-endcircleplus', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['circle', 1], 'geIcon geSprite geSprite-endcircle', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['ERone', 0], 'geIcon geSprite geSprite-enderone', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['ERmandOne', 0], 'geIcon geSprite geSprite-enderonetoone', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['ERmany', 0], 'geIcon geSprite geSprite-endermany', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['ERoneToMany', 0], 'geIcon geSprite geSprite-enderonetomany', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['ERzeroToOne', 1], 'geIcon geSprite geSprite-enderoneopt', null, false);
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW, 'endFill'], ['ERzeroToMany', 1], 'geIcon geSprite geSprite-endermanyopt', null, false);
            } else {
                this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_ENDARROW], [mxConstants.ARROW_BLOCK], 'geIcon geSprite geSprite-endblocktrans', null, false).setAttribute('title', mxResources.get('block'));
            }
        }
    }));

    this.addArrow(edgeShape, 8);
    this.addArrow(edgeStyle);
    this.addArrow(lineStart);
    this.addArrow(lineEnd);

    var symbol = this.addArrow(pattern, 9);
    symbol.className = 'geIcon';
    symbol.style.width = '84px';

    var altSymbol = this.addArrow(altPattern, 9);
    altSymbol.className = 'geIcon';
    altSymbol.style.width = '22px';

    var solid = document.createElement('div');
    solid.style.width = '85px';
    solid.style.height = '1px';
    solid.style.borderBottom = '1px solid ' + this.defaultStrokeColor;
    solid.style.marginBottom = '9px';
    symbol.appendChild(solid);

    var altSolid = document.createElement('div');
    altSolid.style.width = '23px';
    altSolid.style.height = '1px';
    altSolid.style.borderBottom = '1px solid ' + this.defaultStrokeColor;
    altSolid.style.marginBottom = '9px';
    altSymbol.appendChild(altSolid);

    pattern.style.height = '15px';
    altPattern.style.height = '15px';
    edgeShape.style.height = '15px';
    edgeStyle.style.height = '17px';
    lineStart.style.marginLeft = '3px';
    lineStart.style.height = '17px';
    lineEnd.style.marginLeft = '3px';
    lineEnd.style.height = '17px';

    container.appendChild(colorPanel);
    container.appendChild(altStylePanel);
    container.appendChild(stylePanel);

    var arrowPanel = stylePanel.cloneNode(false);
    arrowPanel.style.paddingBottom = '6px';
    arrowPanel.style.paddingTop = '4px';
    arrowPanel.style.fontWeight = 'normal';

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.marginLeft = '3px';
    span.style.marginBottom = '12px';
    span.style.marginTop = '2px';
    span.style.fontWeight = 'normal';
    span.style.width = '76px';

    mxUtils.write(span, mxResources.get('lineend'));
    arrowPanel.appendChild(span);

    var endSpacingUpdate, endSizeUpdate;
    var endSpacing = this.addUnitInput(arrowPanel, 'pt', 74, 33, function () {
        endSpacingUpdate.apply(this, arguments);
    });
    var endSize = this.addUnitInput(arrowPanel, 'pt', 20, 33, function () {
        endSizeUpdate.apply(this, arguments);
    });

    mxUtils.br(arrowPanel);

    var spacer = document.createElement('div');
    spacer.style.height = '8px';
    arrowPanel.appendChild(spacer);

    span = span.cloneNode(false);
    mxUtils.write(span, mxResources.get('linestart'));
    arrowPanel.appendChild(span);

    var startSpacingUpdate, startSizeUpdate;
    var startSpacing = this.addUnitInput(arrowPanel, 'pt', 74, 33, function () {
        startSpacingUpdate.apply(this, arguments);
    });
    var startSize = this.addUnitInput(arrowPanel, 'pt', 20, 33, function () {
        startSizeUpdate.apply(this, arguments);
    });

    mxUtils.br(arrowPanel);
    // this.addLabel(arrowPanel, mxResources.get('spacing'), 74, 50);
    // this.addLabel(arrowPanel, mxResources.get('size'), 20, 50);
    mxUtils.br(arrowPanel);

    var perimeterPanel = colorPanel.cloneNode(false);
    perimeterPanel.style.fontWeight = 'normal';
    perimeterPanel.style.position = 'relative';
    perimeterPanel.style.paddingLeft = '16px'
    perimeterPanel.style.marginBottom = '2px';
    perimeterPanel.style.marginTop = '6px';
    perimeterPanel.style.borderWidth = '0px';
    perimeterPanel.style.paddingBottom = '18px';

    var span = document.createElement('div');
    span.style.position = 'absolute';
    span.style.marginLeft = '3px';
    span.style.marginBottom = '12px';
    span.style.marginTop = '1px';
    span.style.fontWeight = 'normal';
    span.style.width = '120px';
    mxUtils.write(span, mxResources.get('perimeter'));
    perimeterPanel.appendChild(span);

    var perimeterUpdate;
    var perimeterSpacing = this.addUnitInput(perimeterPanel, 'pt', 20, 41, function () {
        perimeterUpdate.apply(this, arguments);
    });

    if (ss.edges.length == graph.getSelectionCount()) {
        container.appendChild(stylePanel2);

        if (mxClient.IS_QUIRKS) {
            mxUtils.br(container);
            mxUtils.br(container);
        }

        container.appendChild(arrowPanel);
    } else if (ss.vertices.length == graph.getSelectionCount()) {
        if (mxClient.IS_QUIRKS) {
            mxUtils.br(container);
        }

        // container.appendChild(perimeterPanel); 周长
    }

    var listener = mxUtils.bind(this, function (sender, evt, force) {
        ss = this.format.getSelectionState();
        var color = mxUtils.getValue(ss.style, strokeKey, null);

        if (force || document.activeElement != input) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_STROKEWIDTH, 1));
            input.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        if (force || document.activeElement != altInput) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_STROKEWIDTH, 1));
            altInput.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        styleSelect.style.visibility = (ss.style.shape == 'connector' || ss.style.shape == 'filledEdge') ? '' : 'hidden';

        if (mxUtils.getValue(ss.style, mxConstants.STYLE_CURVED, null) == '1') {
            styleSelect.value = 'curved';
        } else if (mxUtils.getValue(ss.style, mxConstants.STYLE_ROUNDED, null) == '1') {
            styleSelect.value = 'rounded';
        }

        if (mxUtils.getValue(ss.style, mxConstants.STYLE_DASHED, null) == '1') {
            if (mxUtils.getValue(ss.style, mxConstants.STYLE_DASH_PATTERN, null) == null) {
                solid.style.borderBottom = '1px dashed ' + this.defaultStrokeColor;
            } else {
                solid.style.borderBottom = '1px dotted ' + this.defaultStrokeColor;
            }
        } else {
            solid.style.borderBottom = '1px solid ' + this.defaultStrokeColor;
        }

        altSolid.style.borderBottom = solid.style.borderBottom;

        // Updates toolbar icon for edge style
        var edgeStyleDiv = edgeStyle.getElementsByTagName('div')[0];
        var es = mxUtils.getValue(ss.style, mxConstants.STYLE_EDGE, null);

        if (mxUtils.getValue(ss.style, mxConstants.STYLE_NOEDGESTYLE, null) == '1') {
            es = null;
        }

        if (es == 'orthogonalEdgeStyle' && mxUtils.getValue(ss.style, mxConstants.STYLE_CURVED, null) == '1') {
            edgeStyleDiv.className = 'geSprite geSprite-curved';
        } else if (es == 'straight' || es == 'none' || es == null) {
            edgeStyleDiv.className = 'geSprite geSprite-straight';
        } else if (es == 'entityRelationEdgeStyle') {
            edgeStyleDiv.className = 'geSprite geSprite-entity';
        } else if (es == 'elbowEdgeStyle') {
            edgeStyleDiv.className = 'geSprite ' + ((mxUtils.getValue(ss.style,
                mxConstants.STYLE_ELBOW, null) == 'vertical') ?
                'geSprite-verticalelbow' : 'geSprite-horizontalelbow');
        } else if (es == 'isometricEdgeStyle') {
            edgeStyleDiv.className = 'geSprite ' + ((mxUtils.getValue(ss.style,
                mxConstants.STYLE_ELBOW, null) == 'vertical') ?
                'geSprite-verticalisometric' : 'geSprite-horizontalisometric');
        } else {
            edgeStyleDiv.className = 'geSprite geSprite-orthogonal';
        }

        // Updates icon for edge shape
        var edgeShapeDiv = edgeShape.getElementsByTagName('div')[0];

        if (ss.style.shape == 'link') {
            edgeShapeDiv.className = 'geSprite geSprite-linkedge';
        } else if (ss.style.shape == 'flexArrow') {
            edgeShapeDiv.className = 'geSprite geSprite-arrow';
        } else if (ss.style.shape == 'arrow') {
            edgeShapeDiv.className = 'geSprite geSprite-simplearrow';
        } else {
            edgeShapeDiv.className = 'geSprite geSprite-connection';
        }

        if (ss.edges.length == graph.getSelectionCount()) {
            altStylePanel.style.display = '';
            stylePanel.style.display = 'none';
        } else {
            altStylePanel.style.display = 'none';
            stylePanel.style.display = '';
        }

        function updateArrow(marker, fill, elt, prefix) {
            var markerDiv = elt.getElementsByTagName('div')[0];

            markerDiv.className = ui.getCssClassForMarker(prefix, ss.style.shape, marker, fill);

            if (markerDiv.className == 'geSprite geSprite-noarrow') {
                markerDiv.innerHTML = mxUtils.htmlEntities(mxResources.get('none'));
                markerDiv.style.backgroundImage = 'none';
                markerDiv.style.verticalAlign = 'top';
                markerDiv.style.marginTop = '5px';
                markerDiv.style.fontSize = '10px';
                markerDiv.style.filter = 'none';
                markerDiv.style.color = this.defaultStrokeColor;
                markerDiv.nextSibling.style.marginTop = '0px';
            }

            return markerDiv;
        };

        var sourceDiv = updateArrow(mxUtils.getValue(ss.style, mxConstants.STYLE_STARTARROW, null),
            mxUtils.getValue(ss.style, 'startFill', '1'), lineStart, 'start');
        var targetDiv = updateArrow(mxUtils.getValue(ss.style, mxConstants.STYLE_ENDARROW, null),
            mxUtils.getValue(ss.style, 'endFill', '1'), lineEnd, 'end');

        // Special cases for markers
        if (ss.style.shape == 'arrow') {
            sourceDiv.className = 'geSprite geSprite-noarrow';
            targetDiv.className = 'geSprite geSprite-endblocktrans';
        } else if (ss.style.shape == 'link') {
            sourceDiv.className = 'geSprite geSprite-noarrow';
            targetDiv.className = 'geSprite geSprite-noarrow';
        }

        mxUtils.setOpacity(edgeStyle, (ss.style.shape == 'arrow') ? 30 : 100);

        if (ss.style.shape != 'connector' && ss.style.shape != 'flexArrow' && ss.style.shape != 'filledEdge') {
            mxUtils.setOpacity(lineStart, 30);
            mxUtils.setOpacity(lineEnd, 30);
        } else {
            mxUtils.setOpacity(lineStart, 100);
            mxUtils.setOpacity(lineEnd, 100);
        }

        if (force || document.activeElement != startSize) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_MARKERSIZE));
            startSize.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        if (force || document.activeElement != startSpacing) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_SOURCE_PERIMETER_SPACING, 0));
            startSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        if (force || document.activeElement != endSize) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_ENDSIZE, mxConstants.DEFAULT_MARKERSIZE));
            endSize.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        if (force || document.activeElement != startSpacing) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_TARGET_PERIMETER_SPACING, 0));
            endSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }

        if (force || document.activeElement != perimeterSpacing) {
            var tmp = parseInt(mxUtils.getValue(ss.style, mxConstants.STYLE_PERIMETER_SPACING, 0));
            perimeterSpacing.value = (isNaN(tmp)) ? '' : tmp + ' pt';
        }
    });

    startSizeUpdate = this.installInputHandler(startSize, mxConstants.STYLE_STARTSIZE, mxConstants.DEFAULT_MARKERSIZE, 0, 999, ' pt');
    startSpacingUpdate = this.installInputHandler(startSpacing, mxConstants.STYLE_SOURCE_PERIMETER_SPACING, 0, -999, 999, ' pt');
    endSizeUpdate = this.installInputHandler(endSize, mxConstants.STYLE_ENDSIZE, mxConstants.DEFAULT_MARKERSIZE, 0, 999, ' pt');
    endSpacingUpdate = this.installInputHandler(endSpacing, mxConstants.STYLE_TARGET_PERIMETER_SPACING, 0, -999, 999, ' pt');
    perimeterUpdate = this.installInputHandler(perimeterSpacing, mxConstants.STYLE_PERIMETER_SPACING, 0, 0, 999, ' pt');

    this.addKeyHandler(input, listener);
    this.addKeyHandler(startSize, listener);
    this.addKeyHandler(startSpacing, listener);
    this.addKeyHandler(endSize, listener);
    this.addKeyHandler(endSpacing, listener);
    this.addKeyHandler(perimeterSpacing, listener);

    graph.getModel().addListener(mxEvent.CHANGE, listener);
    this.listeners.push({
        destroy: function () {
            graph.getModel().removeListener(listener);
        }
    });
    listener();

    return container;
};

/**
 * Adds UI for configuring line jumps.
 */
// StyleFormatPanel.prototype.addLineJumps = function (container) {
//   var ss = this.format.getSelectionState();

//   if (Graph.lineJumpsEnabled && ss.edges.length > 0 &&
//     ss.vertices.length == 0 && ss.lineJumps) {
//     container.style.padding = '8px 0px 24px 18px';

//     var ui = this.editorUi;
//     var editor = ui.editor;
//     var graph = editor.graph;

//     var span = document.createElement('div');
//     span.style.position = 'absolute';
//     span.style.fontWeight = 'bold';
//     span.style.width = '80px';

//     mxUtils.write(span, mxResources.get('lineJumps'));
//     container.appendChild(span);

//     var styleSelect = document.createElement('select');
//     styleSelect.style.position = 'absolute';
//     styleSelect.style.marginTop = '-2px';
//     styleSelect.style.right = '76px';
//     styleSelect.style.width = '62px';
//     styleSelect.className = 'lineclass1'
//     var styles = ['none', 'arc', 'gap', 'sharp'];

//     for (var i = 0; i < styles.length; i++) {
//       var styleOption = document.createElement('option');
//       styleOption.setAttribute('value', styles[i]);
//       mxUtils.write(styleOption, mxResources.get(styles[i]));
//       styleSelect.appendChild(styleOption);
//     }

//     mxEvent.addListener(styleSelect, 'change', function (evt) {
//       graph.getModel().beginUpdate();
//       try {
//         graph.setCellStyles('jumpStyle', styleSelect.value, graph.getSelectionCells());
//         ui.fireEvent(new mxEventObject('styleChanged', 'keys', ['jumpStyle'],
//           'values', [styleSelect.value], 'cells', graph.getSelectionCells()));
//       } finally {
//         graph.getModel().endUpdate();
//       }

//       mxEvent.consume(evt);
//     });

//     // Stops events from bubbling to color option event handler
//     mxEvent.addListener(styleSelect, 'click', function (evt) {
//       mxEvent.consume(evt);
//     });

//     container.appendChild(styleSelect);

//     var jumpSizeUpdate;

//     var jumpSize = this.addUnitInput(container, 'pt', 22, 33, function () {
//       jumpSizeUpdate.apply(this, arguments);
//     });

//     jumpSizeUpdate = this.installInputHandler(jumpSize, 'jumpSize',
//       Graph.defaultJumpSize, 0, 999, ' pt');

//     var listener = mxUtils.bind(this, function (sender, evt, force) {
//       ss = this.format.getSelectionState();
//       styleSelect.value = mxUtils.getValue(ss.style, 'jumpStyle', 'none');

//       if (force || document.activeElement != jumpSize) {
//         var tmp = parseInt(mxUtils.getValue(ss.style, 'jumpSize', Graph.defaultJumpSize));
//         jumpSize.value = (isNaN(tmp)) ? '' : tmp + ' pt';
//       }
//     });

//     this.addKeyHandler(jumpSize, listener);

//     graph.getModel().addListener(mxEvent.CHANGE, listener);
//     this.listeners.push({
//       destroy: function () {
//         graph.getModel().removeListener(listener);
//       }
//     });
//     listener();
//   } else {
//     container.style.display = 'none';
//   }

//   return container;
// };

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.addEffects = function (div) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;
    var ss = this.format.getSelectionState();

    div.style.paddingTop = '0px';
    div.style.paddingBottom = '2px';

    var table = document.createElement('table');

    if (mxClient.IS_QUIRKS) {
        table.style.fontSize = '1em';
    }

    table.style.width = '100%';
    table.style.fontWeight = 'bold';
    table.style.paddingRight = '20px';
    var tbody = document.createElement('tbody');
    var row = document.createElement('tr');
    row.style.padding = '0px';
    var left = document.createElement('td');
    left.style.padding = '0px';
    left.style.width = '50%';
    left.setAttribute('valign', 'top');

    var right = left.cloneNode(true);
    right.style.paddingLeft = '8px';
    row.appendChild(left);
    row.appendChild(right);
    tbody.appendChild(row);
    table.appendChild(tbody);
    div.appendChild(table);

    var current = left;
    var count = 0;

    var addOption = mxUtils.bind(this, function (label, key, defaultValue) {
        var opt = this.createCellOption(label, key, defaultValue);
        opt.style.width = '100%';
        current.appendChild(opt);
        current = (current == left) ? right : left;
        count++;
    });

    var listener = mxUtils.bind(this, function (sender, evt, force) {
        ss = this.format.getSelectionState();

        left.innerHTML = '';
        right.innerHTML = '';
        current = left;

        if (ss.rounded) {
            addOption(mxResources.get('rounded'), mxConstants.STYLE_ROUNDED, 0);
        }

        if (ss.style.shape == 'swimlane') {
            addOption(mxResources.get('divider'), 'swimlaneLine', 1);
        }

        if (ss.shadow) {
            addOption(mxResources.get('shadow'), mxConstants.STYLE_SHADOW, 0);
        }

        if (ss.glass) {
            addOption(mxResources.get('glass'), mxConstants.STYLE_GLASS, 0);
        }

        if (ss.comic) {
            addOption(mxResources.get('comic'), 'comic', 0);
        }

        if (count == 0) {
            div.style.display = 'none';
        }

        if (ss.flow) {
            addOption('流动', 'flow', 0);
        }
    });

    graph.getModel().addListener(mxEvent.CHANGE, listener);
    this.listeners.push({
        destroy: function () {
            graph.getModel().removeListener(listener);
        }
    });
    listener();
    return div;
}

/**
 * Adds the label menu items to the given menu and parent.
 */
StyleFormatPanel.prototype.addStyleOps = function (div) {
    div.style.paddingTop = '10px';
    div.style.paddingBottom = '10px';

    var btn = mxUtils.button(mxResources.get('setAsDefaultStyle'), mxUtils.bind(this, function (evt) {
        this.editorUi.actions.get('setAsDefaultStyle').funct();
    }));

    btn.setAttribute('title', mxResources.get('setAsDefaultStyle') + ' (' + this.editorUi.actions.get('setAsDefaultStyle').shortcut + ')');
    btn.style.width = '202px';
    div.appendChild(btn);

    return div;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel = function (format, editorUi, container) { // 图表的方法
    BaseFormatPanel.call(this, format, editorUi, container);
    this.init();
};

mxUtils.extend(DiagramFormatPanel, BaseFormatPanel);

/**
 * Switch to disable page view.
 */
DiagramFormatPanel.showPageView = true;

/**
 * Specifies if the background image option should be shown. Default is true.
 */
DiagramFormatPanel.prototype.showBackgroundImageOption = true;

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.init = function () {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    this.container.appendChild(this.addView(this.createPanel()));
    if (graph.isEnabled()) {
        // this.container.appendChild(this.addOptions(this.createPanel()));
        // this.container.appendChild(this.addPaperSize(this.createPanel())); // 画布尺寸
        // this.container.appendChild(this.addStyleOps(this.createPanel()));
    }
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.addView = function (div) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    // Grid

    if (graph.isEnabled()) {

        var graph = this.editorUi.editor.graph;
        var cells = graph.getModel().cells

        div.appendChild(this.addPaperSize(this.createPanel())); // 画布尺寸

        // 背景颜色框
        var bgcolorDiv = document.createElement('div')
        // 添加label
        var bgcolorP = document.createElement('p')
        bgcolorP.innerText = '背景颜色'
        bgcolorP.style.margin = '0 10px 0 0'
        bgcolorP.style.display = 'inline-block'

        // 转载节点
        bgcolorDiv.appendChild(bgcolorP)
        bgcolorDiv.appendChild(bgcolor)
        div.appendChild(bgcolorDiv)


        // 背景图片框
        var fileDiv = document.createElement('div')
        // 显示input
        var fileInputText = document.createElement('input')
        fileInputText.style.height = '24px'
        fileInputText.className = 'eleInput'
        fileInputText.style.width = '88px'
        fileInputText.style.borderRadius = '4px 0 0 4px'
        fileInputText.style.padding = '0'
        fileInputText.setAttribute('readonly', true)

        // label
        var fileP = document.createElement('p')
        fileP.innerText = '背景图片'
        fileP.style.margin = '8px 10px 8px 0'
        fileP.style.display = 'inline-block'
        // 导入按钮
        var fileButton = document.createElement('button')
        fileButton.className = 'eleButton'
        fileButton.style.height = '24px'
        fileButton.style.position = 'relative'
        fileButton.style.top = '0px'
        fileButton.style.left = '-2px'
        fileButton.style.borderRadius = '0 4px 4px 0'
        fileButton.style.lineHeight = '14px'

        fileButton.innerText = '导入'
        // 删除按钮
        var fileButton2 = document.createElement('button')
        // fileButton2.className = 'deleteBtn'
        fileButton2.style.position = 'relative'
        fileButton2.style.height = '24px'
        fileButton2.style.width = '24px'
        fileButton2.style.left = '-1px'
        fileButton2.style.borderRadius = '0px'
        fileButton2.style.borderLeft = '0px'
        // 监听导入事件
        fileButton.addEventListener('click', function (e) {
            // 转发事件
            fileImage.click()
        })
        // 监听删除事件
        fileButton2.addEventListener('click', function (e) {
            graph.setBackgroundImage();
            graph.view.validateBackgroundImage();
            fileInputText.value = ''
            cells[0].imageHeight = ''
            cells[0].imageWidth = ''
            fileButton2.classList.remove('deleteBtn')
        })
        // 上传方法
        var fileImage = document.createElement('input')
        fileImage.id = 'file'
        fileImage.setAttribute('type', 'file')
        fileImage.setAttribute('title', '')
        fileImage.style.width = '100%'
        fileImage.style.display = 'none'
        fileImage.addEventListener('change', function (e) {
            var x = new FileReader;
            fileInputText.value = e.target.files[0].name
            if (e.target.files[0].name) {
                fileButton2.className = 'deleteBtn'
            }
            x.readAsDataURL(e.target.files[0])
            x.onloadend = function (e) {
                var imageHeight
                var imageWidth
                var image = new Image()
                image.src = x.result
                image.onload = function (e) {
                    imageHeight = e.path[0].height
                    imageWidth = e.path[0].width
                    cells[0].imageHeight = e.path[0].height
                    cells[0].imageWidth = e.path[0].width
                    var img = new mxImage(x.result, imageWidth, imageHeight);
                    ui.setBackgroundImage(img)
                    graph.view.validate();
                    fileImage.value = ''
                }
            }
        })
        // 转载节点
        fileDiv.appendChild(fileP)
        fileDiv.appendChild(fileInputText)
        fileDiv.appendChild(fileButton2)
        fileDiv.appendChild(fileButton)
        fileDiv.appendChild(fileImage)
        div.appendChild(fileDiv)

        // label提示信息
        var bgWarningTitle = document.createElement('p')
        bgWarningTitle.innerText = '图片建议不大于500K !'
        bgWarningTitle.style.margin = '0px 0 0 0 '
        bgWarningTitle.style.color = '#bbbbbb'
        bgWarningTitle.className = 'bgWarningTitle'
        bgWarningTitle.style.paddingLeft = '62px'
        div.appendChild(bgWarningTitle)

    }
    this.addGridOption(div); // 网格
    div.style.borderBottom = '0px'
    div.style.paddingBottom = '0px'

    return div;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.addOptions = function (div) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    div.appendChild(this.createTitle(mxResources.get('options')));

    if (graph.isEnabled()) {
        // Connection arrows
        div.appendChild(this.createOption(mxResources.get('connectionArrows'), function () {
            return graph.connectionArrowsEnabled;
        }, function (checked) {
            ui.actions.get('connectionArrows').funct();
        }, {
            install: function (apply) {
                this.listener = function () {
                    apply(graph.connectionArrowsEnabled);
                };

                ui.addListener('connectionArrowsChanged', this.listener);
            },
            destroy: function () {
                ui.removeListener(this.listener);
            }
        }));

        // Connection points
        div.appendChild(this.createOption(mxResources.get('connectionPoints'), function () {
            return graph.connectionHandler.isEnabled();
        }, function (checked) {
            ui.actions.get('connectionPoints').funct();
        }, {
            install: function (apply) {
                this.listener = function () {
                    apply(graph.connectionHandler.isEnabled());
                };

                ui.addListener('connectionPointsChanged', this.listener);
            },
            destroy: function () {
                ui.removeListener(this.listener);
            }
        }));

        // Guides
        div.appendChild(this.createOption(mxResources.get('guides'), function () {
            return graph.graphHandler.guidesEnabled;
        }, function (checked) {
            ui.actions.get('guides').funct();
        }, {
            install: function (apply) {
                this.listener = function () {
                    apply(graph.graphHandler.guidesEnabled);
                };

                ui.addListener('guidesEnabledChanged', this.listener);
            },
            destroy: function () {
                ui.removeListener(this.listener);
            }
        }));
    }

    return div;
};

/**
 *
 */
DiagramFormatPanel.prototype.addGridOption = function (container) {
    var ui = this.editorUi;
    var graph = ui.editor.graph;

    var input = document.createElement('input');
    input.style.width = '50px';
    input.style.height = '24px';
    input.style.padding = '0';
    input.style.borderRadius = '4px 0 0 4px'
    input.className = 'eleInput'
    input.value = graph.getGridSize() + ' pt';
    var stepper = this.createStepper(input, update);
    // input.style.display = (graph.isGridEnabled()) ? '' : 'none';
    stepper.style.display = input.style.display;

    mxEvent.addListener(input, 'keydown', function (e) {
        if (e.keyCode == 13) {
            graph.container.focus();
            mxEvent.consume(e);
        } else if (e.keyCode == 27) {
            input.value = graph.getGridSize();
            graph.container.focus();
            mxEvent.consume(e);
        }
    });

    function update(evt) {
        var value = parseInt(input.value);
        value = Math.max(1, (isNaN(value)) ? 10 : value);

        if (value != graph.getGridSize()) {
            graph.setGridSize(value)
        }

        input.value = value + ' pt';
        mxEvent.consume(evt);
    };

    mxEvent.addListener(input, 'blur', update);
    mxEvent.addListener(input, 'change', update);

    if (mxClient.IS_SVG) {
        stepper.style.marginTop = '-20px';
        stepper.style.right = '72px';
        // 网格框
        var gridDiv = document.createElement('div')
        gridDiv.style.height = '24px'
        gridDiv.style.width = '100%'
        gridDiv.style.margin = '8px 0 8px 0'
        // 网格labei
        var gridLabel = document.createElement('p')
        gridLabel.innerText = '网格'
        gridLabel.style.height = '100%'
        gridLabel.style.margin = '0 14px 0 6px'
        gridLabel.style.display = 'inline-block'
        // 网格切换按钮
        // 实体切换按钮
        var gridcheckedP = document.createElement('p')
        graph.isGridEnabled() ? gridcheckedP.className = 'checkBoxChecked' : gridcheckedP.className = 'checkBoxUnChecked'
        // 隐藏切换按钮
        var gridchecked = document.createElement('input')
        gridchecked.style.position = 'relative'
        gridchecked.style.top = '3px'
        gridchecked.style.display = 'none'
        gridchecked.name = 'gridchecked'
        gridchecked.type = 'checkbox'
        gridchecked.checked = graph.isGridEnabled()
        // 网格加减按钮
        var gridButton1 = document.createElement('button')
        gridButton1.className = 'topButton'
        var gridButton2 = document.createElement('button')
        gridButton2.className = 'dowmButton'
        // 监听按钮事件
        gridButton1.addEventListener('click', function (evt) {
            var value = parseInt(input.value);
            value = Math.max(1, (isNaN(value)) ? 10 : value);
            if ((value + 1) != graph.getGridSize()) {
                graph.setGridSize(value + 1)
            }
            input.value = (value + 1) + ' pt';
            mxEvent.consume(evt);
        })
        gridButton2.addEventListener('click', function (evt) {
            var value = parseInt(input.value);
            value = Math.max(1, (isNaN(value)) ? 10 : value);
            if ((value - 1) != graph.getGridSize() && value > 1) {
                graph.setGridSize(value - 1)
                input.value = (value - 1) + ' pt';
                mxEvent.consume(evt);
            }
        })
        // 监听网格事件
        gridcheckedP.addEventListener('click', function (e) {
            // 转发事件
            gridchecked.click()
        })
        gridchecked.addEventListener('click', function (e) {
            if (e.target.checked != graph.isGridEnabled()) {
                graph.setGridEnabled(e.target.checked)
                graph.view.validateBackground();
                graph.isGridEnabled() ? gridcheckedP.className = 'checkBoxChecked' : gridcheckedP.className = 'checkBoxUnChecked'
            }
        })
        // 转载节点
        gridDiv.appendChild(gridchecked);
        gridDiv.appendChild(gridcheckedP);
        gridDiv.appendChild(gridLabel);
        gridDiv.appendChild(input);
        gridDiv.appendChild(gridButton1);
        gridDiv.appendChild(gridButton2);
        gridDiv.appendChild(gridColor);
        container.appendChild(gridDiv);

        // var panel = this.createColorOption(mxResources.get('grid'), function () {
        //   var color = graph.view.gridColor;

        //   return (graph.isGridEnabled()) ? color : null;
        // },function (color) {
        //   if (color == mxConstants.NONE) {
        //     graph.setGridEnabled(false);
        //   } else {
        //     graph.setGridEnabled(true);
        //     ui.setGridColor(color);
        //   }

        //   input.style.display = (graph.isGridEnabled()) ? '' : 'none';
        //   stepper.style.display = input.style.display;
        //   ui.fireEvent(new mxEventObject('gridEnabledChanged'));
        // }, '#e0e0e0', {
        //   install: function (apply) {
        //     this.listener = function () {
        //       apply((graph.isGridEnabled()) ? graph.view.gridColor : null);
        //     };

        //     ui.addListener('gridColorChanged', this.listener);
        //     ui.addListener('gridEnabledChanged', this.listener);
        //   },
        //   destroy: function () {
        //     ui.removeListener(this.listener);
        //   }
        // });
        // panel.style.marginTop = '10px'
        // panel.appendChild(input);
        // panel.appendChild(stepper);
        // container.appendChild(panel);
    } else {
        // input.style.marginTop = '2px';
        // input.style.right = '32px';
        // stepper.style.marginTop = '2px';
        // stepper.style.right = '20px';

        // container.appendChild(input);
        // container.appendChild(stepper);

        // container.appendChild(this.createOption(mxResources.get('grid'), function () {
        //   return graph.isGridEnabled();
        // }, function (checked) {
        //   graph.setGridEnabled(checked);

        //   if (graph.isGridEnabled()) {
        //     graph.view.gridColor = '#e0e0e0';
        //   }

        //   ui.fireEvent(new mxEventObject('gridEnabledChanged'));
        // }, {
        //   install: function (apply) {
        //     this.listener = function () {
        //       input.style.display = (graph.isGridEnabled()) ? '' : 'none';
        //       stepper.style.display = input.style.display;

        //       apply(graph.isGridEnabled());
        //     };

        //     ui.addListener('gridEnabledChanged', this.listener);
        //   },
        //   destroy: function () {
        //     ui.removeListener(this.listener);
        //   }
        // }));
    }
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.addDocumentProperties = function (div) {
    // Hook for subclassers
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    div.appendChild(this.createTitle(mxResources.get('options')));

    return div;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.addPaperSize = function (div) {
    var ui = this.editorUi;
    var editor = ui.editor;
    var graph = editor.graph;

    // 宽度
    var PaperSizeWidthDiv = document.createElement('div')
    // pagersizeLabel
    var pagersizeLabel = document.createElement('p')
    pagersizeLabel.style.margin = '0'
    pagersizeLabel.style.display = 'inline-block'
    pagersizeLabel.style.height = '24px'
    pagersizeLabel.style.marginRight = '10px'
    pagersizeLabel.innerText = '画布尺寸'
    // 画布宽度
    var PaperSizeWidthButton = document.createElement('button')
    PaperSizeWidthButton.innerText = '宽度'
    PaperSizeWidthButton.style.borderRadius = '4px 0 0 4px';
    PaperSizeWidthButton.className = 'pagersizeButton'
    // 宽度input
    var PaperSizeWidthInput = document.createElement('input')
    PaperSizeWidthInput.value = graph.pageFormat.width + ' px'
    PaperSizeWidthInput.style.height = '20px'
    PaperSizeWidthInput.className = 'pagersizeInput'
    //宽度按钮
    var PaperSizeWidthButtonTop = document.createElement('button')
    PaperSizeWidthButtonTop.style.left = '-2px'
    PaperSizeWidthButtonTop.className = 'topButton'
    var PaperSizeWidthButtonDown = document.createElement('button')
    PaperSizeWidthButtonDown.style.left = '-3px'
    PaperSizeWidthButtonDown.className = 'dowmButton'

    // 高度
    var PaperSizeHeightDiv = document.createElement('div')
    PaperSizeHeightDiv.style.paddingLeft = '58px'
    // 画布高度
    var PaperSizeHeightButton = document.createElement('button')
    PaperSizeHeightButton.innerText = '高度'
    PaperSizeHeightButton.style.borderRadius = '4px 0 0 4px';
    PaperSizeHeightButton.className = 'pagersizeButton'
    // 高度input
    var PaperSizeHeightInput = document.createElement('input')
    PaperSizeHeightInput.value = graph.pageFormat.height + ' px'
    PaperSizeHeightInput.style.height = '20px'
    PaperSizeHeightInput.className = 'pagersizeInput'
    //高度按钮
    var PaperSizeHeightButtonTop = document.createElement('button')
    PaperSizeHeightButtonTop.style.left = '-2px'
    PaperSizeHeightButtonTop.className = 'topButton'
    var PaperSizeHeightButtonDown = document.createElement('button')
    PaperSizeHeightButtonDown.style.left = '-3px'
    PaperSizeHeightButtonDown.className = 'dowmButton'

    // 装载节点
    div.style.borderBottom = '0'
    div.style.padding = '0'
    div.style.margin = '0 0 10px 0'
    // 宽
    PaperSizeWidthDiv.appendChild(pagersizeLabel)
    PaperSizeWidthDiv.appendChild(PaperSizeWidthButton)
    PaperSizeWidthDiv.appendChild(PaperSizeWidthInput)
    PaperSizeWidthDiv.appendChild(PaperSizeWidthButtonTop)
    PaperSizeWidthDiv.appendChild(PaperSizeWidthButtonDown)
    // 高
    PaperSizeHeightDiv.appendChild(PaperSizeHeightButton)
    PaperSizeHeightDiv.appendChild(PaperSizeHeightInput)
    PaperSizeHeightDiv.appendChild(PaperSizeHeightButtonTop)
    PaperSizeHeightDiv.appendChild(PaperSizeHeightButtonDown)
    div.appendChild(PaperSizeWidthDiv);
    div.appendChild(PaperSizeHeightDiv);

    //监听页面改变方法

    PaperSizeWidthInput.addEventListener('change', setPaperSize)
    PaperSizeWidthInput.addEventListener('blur', setPaperSize)
    PaperSizeHeightInput.addEventListener('change', setPaperSize)
    PaperSizeHeightInput.addEventListener('blur', setPaperSize)

    PaperSizeWidthButtonTop.addEventListener('click', function (e) {
        var value = parseInt(PaperSizeWidthInput.value)
        if ((value + 1) !== graph.pageFormat.width) {
            PaperSizeWidthInput.value = (value + 1) + ' px'
            setPaperSize()
        }
    })
    PaperSizeWidthButtonDown.addEventListener('click', function (e) {
        var value = parseInt(PaperSizeWidthInput.value)
        if ((value - 1) !== graph.pageFormat.width && value > 1) {
            PaperSizeWidthInput.value = (value - 1) + ' px'
            setPaperSize()
        }
    })
    PaperSizeHeightButtonTop.addEventListener('click', function (e) {
        var value = parseInt(PaperSizeHeightInput.value)
        if ((value + 1) !== graph.pageFormat.height) {
            PaperSizeHeightInput.value = (value + 1) + ' px'
            setPaperSize()
        }
    })
    PaperSizeHeightButtonDown.addEventListener('click', function (e) {
        var value = parseInt(PaperSizeHeightInput.value)
        if ((value - 1) !== graph.pageFormat.height && value > 1) {
            PaperSizeHeightInput.value = (value - 1) + ' px'
            setPaperSize()
        }
    })

    function setPaperSize() {
        ui.setPageFormat({
            x: 0,
            y: 0,
            height: parseInt(PaperSizeHeightInput.value),
            width: parseInt(PaperSizeWidthInput.value)
        })
    }

    // var accessor = PageSetupDialog.addPageFormatPanel(div, 'formatpanel', graph.pageFormat, function (pageFormat) {
    //   if (graph.pageFormat == null || graph.pageFormat.width != pageFormat.width ||
    //     graph.pageFormat.height != pageFormat.height) {
    //     var change = new ChangePageSetup(ui, null, null, pageFormat);
    //     change.ignoreColor = true;
    //     change.ignoreImage = true;
    //     graph.model.execute(change);
    //     console.log(pageFormat);
    //   }
    // });

    // this.addKeyHandler(accessor.widthInput, function () {
    //   accessor.set(graph.pageFormat);
    // });
    // this.addKeyHandler(accessor.heightInput, function () {
    //   accessor.set(graph.pageFormat);
    // });

    // var listener = function () {
    //   accessor.set(graph.pageFormat);
    // };

    // ui.addListener('pageFormatChanged', listener);
    // this.listeners.push({
    //   destroy: function () {
    //     ui.removeListener(listener);
    //   }
    // });

    // graph.getModel().addListener(mxEvent.CHANGE, listener);
    // this.listeners.push({
    //   destroy: function () {
    //     graph.getModel().removeListener(listener);
    //   }
    // });

    return div;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.addStyleOps = function (div) {
    var btn = mxUtils.button(mxResources.get('editData'), mxUtils.bind(this, function (evt) {
        this.editorUi.actions.get('editData').funct();
    }));

    btn.setAttribute('title', mxResources.get('editData') + ' (' + this.editorUi.actions.get('editData').shortcut + ')');
    btn.style.width = '202px';
    btn.style.marginBottom = '2px';
    div.appendChild(btn);

    mxUtils.br(div);

    btn = mxUtils.button(mxResources.get('clearDefaultStyle'), mxUtils.bind(this, function (evt) {
        this.editorUi.actions.get('clearDefaultStyle').funct();
    }));

    btn.setAttribute('title', mxResources.get('clearDefaultStyle') + ' (' + this.editorUi.actions.get('clearDefaultStyle').shortcut + ')');
    btn.style.width = '202px';
    div.appendChild(btn);

    return div;
};

/**
 * Adds the label menu items to the given menu and parent.
 */
DiagramFormatPanel.prototype.destroy = function () {
    BaseFormatPanel.prototype.destroy.apply(this, arguments);

    if (this.gridEnabledListener) {
        this.editorUi.removeListener(this.gridEnabledListener);
        this.gridEnabledListener = null;
    }
};