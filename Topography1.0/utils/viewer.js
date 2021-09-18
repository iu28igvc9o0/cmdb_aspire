// Extends EditorUi to update I/O action states based on availability of backend
(function () {
    var editorUiInit = EditorUi.prototype.init;

    EditorUi.prototype.init = function () {
        editorUiInit.apply(this, arguments);
        // Updates action states which require a backend
    };

    // Adds required resources (disables loading of fallback properties, this can only
    // be used if we know that all keys are defined in the language specific file)
    mxResources.loadDefaultBundle = false;
    var bundle = mxResources.getDefaultBundle(RESOURCE_BASE, mxLanguage) ||
        mxResources.getSpecialBundle(RESOURCE_BASE, mxLanguage);

    // Fixes possible asynchronous requests
    mxUtils.getAll([bundle, STYLE_PATH + '/default.xml'], function (xhr) {
            // Adds bundle text to resources
            mxResources.parse(xhr[0].getText());

            // Configures the default graph theme
            var themes = new Object();
            themes[Graph.prototype.defaultThemeName] = xhr[1].getDocumentElement();

            // Main
            window.newEditorUi = new EditorUi(new Editor(urlParams['chrome'] == '0', themes));
            newEditorUi.editor.graph.setEnabled(false);

            // graph
            var graph = newEditorUi.editor.graph
            var clickTime = null
            var sendViewPortData = null
            // 设置全局属性
            window._setFlowIndex = 0
            window.isEdgeID = {}
            window.isCellIDDown = {}
            window.isCellIDUp = {}

            // 鼠标单击点击事件
            graph.click = function (evt) {
                // clearTimeout(clickTime);
                // if (clickTime) {
                //     clearTimeout(clickTime)
                //     clickTime = null
                // } else {
                //     clickTime = setTimeout(function () {
                //         clearTimeout(clickTime)
                //         clickTime = null
                //         if (evt.evt.which == 1) {
                //             _setFlow(evt)
                //         }
                //         if (evt.state && evt.state.style.actionType0 === 0) {
                //             eventChange(evt.state, 0)
                //         } else {
                //             return false
                //         }
                //     }, 300);
                // }
            }
            // 鼠标双击事件
            graph.dblClick = function (evt, params) {
              //debugger;
                var state = graph.view.getState(params)
                if (state && state.cell.edge) {
                    if (state.style.isClick) {
                        state.style.isClick = null
                        var path = state.shape.node.getElementsByTagName('path')
                        for (let i = 1; i < path.length; i++) {
                            // path[i].removeAttribute('class', 'selected');
                            path[i].classList.remove('selected')
                        }
                    } else {
                        state.style.isClick = true
                        var path = state.shape.node.getElementsByTagName('path')
                        for (let i = 1; i < path.length; i++) {
                            // path[i].setAttribute('class', 'selected');
                            path[i].classList.add('selected')
                        }
                    }
                }
            }
            //右击菜单
            graph.popupMenuHandler.autoExpand = true;
            graph.popupMenuHandler.factoryMethod = function (menu, cell, evt) {
                var state = graph.view.getState(cell)
                if (state && state.style.isBind) {
                    menu.addItem('查看设备', null, function () {
                        addMenuItem(state, "deviceList")
                    });
                    menu.addItem('查看告警', null, function () {
                        addMenuItem(state, "AlertList")
                    });
                  menu.addItem('查看STP信息表', null, function () {
                    addMenuItem(state, "stpInfo")
                  });
                  menu.addItem('查看路由信息表', null, function () {
                    addMenuItem(state, "routeInfo")
                  });
                    // menu.addItem('添加节点', null, function () {
                    //     //console.log(evt);
                    //     createNewNode(graph, evt.clientX, evt.clientY, '新节点');
                    // }, submenu0);
                }
            }
            function addMenuItem(state, name) {
                if (state && state.style.isBind) {
                    window.parent.postMessage({
                        cmd: 'getChildView',
                        params: {
                            name: name,
                            deviceIp: JSON.parse(state.style.deviceList)[0].ip,
                            idcType: JSON.parse(state.style.deviceList)[0].idcType
                        }
                    }, '*');
                } else {
                    return false
                }
            }
            // //创建节点函数
            // function createNewNode(graph, x, y, text) {
            //     const parent = graph.getDefaultParent();
            //     var node = graph.insertVertex(parent, null, text, x, y, 80, 30);
            //     return node;
            // }
            // 鼠标移动事件
            graph.addMouseListener({
                mouseDown: function (sender, evt) {
                    if (evt.evt.which === 3) {
                        geDiagramContainerSc(1)
                    }
                },
                mouseUp: function (sender, evt) {
                },
                mouseMove: mxUtils.bind(this, function (sender, evt) {
                    // 鼠标移动
                    if (evt.state && evt.state.style.isBind) {
                        // eventChange(evt.state, 2)
                        // var overlay = new mxCellOverlay(evt.cell, tooltip);
                        addBindOjectTooltip(evt.state)
                    } else if (evt.state &&  evt.state.style.online) {
                        addOnlineObjTooltip(evt.state)
                    } else {
                        return false
                    }
                })
            })
            addOnlineObjTooltip = function (state) {
                var value = []
                value.push("源对象")
                // 添加源对象
                let sourceObj = JSON.parse(state.style.source)
                value.push("\t\tIP地址：" + sourceObj.ip)
                value.push("\t\t主机名：" + sourceObj.hostname)
                value.push("\t\t端口：" + sourceObj.portDesc)
                value.push("\t\t设备厂商：" + sourceObj.deviceBrand)
                value.push("\t\t设备型号：" + sourceObj.deviceModel)

                value.push("目标对象")
                let targetObj = JSON.parse(state.style.target)
                value.push("\t\tIP地址：" + targetObj.ip)
                value.push("\t\t主机名：" + targetObj.hostname)
                value.push("\t\t端口：" + targetObj.portDesc)
                value.push("\t\t设备厂商：" + targetObj.deviceBrand)
                value.push("\t\t设备型号：" + targetObj.deviceModel)

                graph.setTooltipForCell(state.cell, value.join('\n'))
            }
            addBindOjectTooltip = function (state) {
                var value = []
                // 添加ip
                let deviceListObj = JSON.parse(state.style.deviceList)
                let ipList = []
                for (let i = 0; i < deviceListObj.length; i++) {
                    ipList.push(deviceListObj[i].idcType + ':' + deviceListObj[i].ip)
                }
                value.push("绑定IP: " + ipList.join(','))
                // 添加指标
                let itemListObj = JSON.parse(state.style.itemList)
                let keyList = []
                for (let i = 0; i < itemListObj.length; i++) {
                    keyList.push(itemListObj[i].key)
                }
                value.push("绑定指标: " + keyList.join(','))
                // 添加对象名称
                let name = JSON.parse(state.style.name)
                value.push("对象名称: " + name)
                if (state.style.bandType == 0) {
                    // 添加告警范围
                    let alertLevelListObj = JSON.parse(state.style.alertLevelList)
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
                    value.push("告警范围： " + alertLevelDescList.join(','))
                    // value.push("告警值： " + state.style.value)
                } else {
                    // 添加计算方式
                    let countType = JSON.parse(state.style.countType)
                    if (countType == 'max') {
                        countType = '最大'
                    } else if (countType == 'min') {
                        countType = '最小'
                    } else if (countType == 'avg') {
                        countType = '平均'
                    } else if (countType == 'sum') {
                        countType = '求和'
                    }
                    value.push("计算方式: " + countType)
                    //添加阀值配置数
                    let sillListObj = JSON.parse(state.style.sillList)
                    value.push("阀值配置数: " + sillListObj.length)
                    // value.push("监控值： " + state.style.value)
                }

                graph.setTooltipForCell(state.cell, value.join('\n'))
            }
            // // 事件判断
            // eventChange = function (state, actionType) {
            //     actionChange = state.style['actionChange' + actionType]
            //
            //     switch (actionChange) {
            //         case 0:
            //             // 调用解除绑定
            //             break
            //         case 1:
            //             // 页面切换
            //             pageJump(state, actionType)
            //             break
            //         case 2:
            //             // 读取文件
            //             openFile(state, actionType)
            //             break
            //     }
            // }
            function _setFlow(evt) {
                if (evt.state) {
                    if (!evt.getCell().edge) {
                        if (window.isEdgeID) {
                            var _flage = false
                            Object.keys(window.isCellIDDown).forEach(function (item) {
                                if (window.isCellIDDown[item].indexOf(evt.getCell().id) > -1) {
                                    _flage = true
                                    _clearFlow(window.isEdgeID[item], item)
                                }
                            })
                            if (!_flage) {
                                Object.keys(window.isCellIDUp).forEach(function (item) {
                                    if (window.isCellIDUp[item].indexOf(evt.getCell().id) > -1) {
                                        _flage = true
                                        _clearFlow(window.isEdgeID[item], item)
                                    }
                                })
                            }
                            if (!_flage) {
                                _setFlowIndex++
                            } else {
                                return
                            }
                        }
                        if (evt.getCell().edges && evt.getCell().edges.length > 0) {
                            window.isCellIDDown[_setFlowIndex] = [evt.getCell().id]
                            window.isCellIDUp[_setFlowIndex] = [evt.getCell().id]
                            window.isEdgeID[_setFlowIndex] = []
                            getEdges(evt.getCell().id, evt.getCell().edges, window.isEdgeID[_setFlowIndex], 0)
                        }
                        window.isEdgeID[_setFlowIndex] = [...new Set(window.isEdgeID[_setFlowIndex])]
                        _setFlow_ID(window.isEdgeID[_setFlowIndex], true)
                    }
                } else {
                    if (window.isEdgeID && _setFlowIndex === 0) {
                        return
                    }
                    _clearFlow(window.isEdgeID, false, true)
                    flow(graph)
                    return false
                }
            }

            function _clearFlow(data, item, all) {
                if (all) {
                    var flowV = document.getElementsByClassName('flowV')
                    var flowc = document.getElementsByClassName('flowc')
                    if (flowV.length > 0) {
                        for (let i = 0; i < flowV.length; i++) {
                            flowV[i].setAttribute('class', '');
                        }
                    }
                    if (flowc.length > 0) {
                        for (let i = 0; i < flowc.length; i++) {
                            flowc[i].setAttribute('class', '');
                        }
                    }
                    Object.keys(data).forEach(function (item) {
                        for (let i = 0; i < data[item].length; i++) {
                            var cell = graph.getModel().getCell(data[item][i])
                            var state = graph.view.getState(cell)
                            if (!state) {
                                return
                            }
                            var path = state.shape.node.getElementsByTagName('path')
                            for (let i = 1; i < path.length; i++) {
                                path[i].setAttribute('class', '');
                            }
                        }
                    })
                    window._setFlowIndex = 0
                    window.isEdgeID = {}
                    window.isCellIDDown = {}
                    window.isCellIDUp = {}
                    flow(graph)
                    return
                }
                if (data) {
                    for (let x = 0; x < data.length; x++) {
                        var cell = graph.getModel().getCell(data[x])
                        var state = graph.view.getState(cell)
                        if (!state) {
                            return
                        }
                        var path = state.shape.node.getElementsByTagName('path')
                        for (let i = 1; i < path.length; i++) {
                            path[i].setAttribute('class', '');
                        }
                    }
                }
                if (item) {
                    delete(window.isEdgeID[item])
                    delete(window.isCellIDUp[item])
                    delete(window.isCellIDDown[item])
                }
                Object.keys(window.isEdgeID).forEach(function (item) {
                    if (window.isEdgeID[item]) {
                        _setFlow_ID(window.isEdgeID[item], false)
                    }
                })
                flow(graph)
            }

            function _setFlow_ID(id, type) {
                if (id) {
                    for (let t = 0; t < id.length; t++) {
                        var cell = graph.getModel().getCell(id[t])
                        var state = graph.view.getState(cell)
                        if (state) {
                            var path = state.shape.node.getElementsByTagName('path')
                            for (let i = 1; i < path.length; i++) {
                                if (i === 1) {
                                    path[i].setAttribute('class', 'flowV');
                                    path[i].setAttribute('strokeWidth', 5 * graph.view.scale);
                                } else {
                                    path[i].setAttribute('class', 'flowc');
                                    path[i].setAttribute('strokeWidth', 5 * graph.view.scale);
                                }
                            }
                        }
                    }
                }
                if (type) {
                    flow(graph)
                }
            }

            function getEdges(id, edges, isEdgeID, type) {
                // debugger
                // 第一次循环判断流向
                if (!type) {
                    for (var i = 0; i < edges.length; i++) {
                        if (edges[i].source && edges[i].source.id === id) {
                            if (edges[i].target && edges[i].target.edges && window.isCellIDDown[_setFlowIndex].indexOf(edges[i].target.id) === -1) {
                                window.isCellIDDown[_setFlowIndex].push(edges[i].target.id)
                                isEdgeID.push(edges[i].id)
                                getEdges(edges[i].target.id, edges[i].target.edges, isEdgeID, 1)
                            }
                        }
                        if (edges[i].target && edges[i].target.id === id) {
                            if (edges[i].source && edges[i].source.edges && window.isCellIDUp[_setFlowIndex].indexOf(edges[i].source.id) === -1) {
                                window.isCellIDUp[_setFlowIndex].push(edges[i].source.id)
                                isEdgeID.push(edges[i].id)
                                getEdges(edges[i].source.id, edges[i].source.edges, isEdgeID, 2)
                            }
                        }
                    }
                } else if (type === 1) {
                    // 向下寻找
                    for (var i = 0; i < edges.length; i++) {
                        if (edges[i].source && edges[i].source.id === id) {
                            isEdgeID.push(edges[i].id)
                            if (edges[i].target && edges[i].target.edges && window.isCellIDDown[_setFlowIndex].indexOf(edges[i].target.id) === -1) {
                                window.isCellIDDown[_setFlowIndex].push(edges[i].target.id)
                                getEdges(edges[i].target.id, edges[i].target.edges, isEdgeID, 1)
                            }
                        }
                    }
                } else if (type === 2) {
                    // 向上寻找
                    for (var i = 0; i < edges.length; i++) {
                        if (edges[i].target && edges[i].target.id === id) {
                            isEdgeID.push(edges[i].id)
                            if (edges[i].source && edges[i].source.edges && window.isCellIDUp[_setFlowIndex].indexOf(edges[i].source.id) === -1) {
                                window.isCellIDUp[_setFlowIndex].push(edges[i].source.id)
                                getEdges(edges[i].source.id, edges[i].source.edges, isEdgeID, 2)
                            }
                        }
                    }
                }
            }

            // 下钻
            pageJump = function (state, actionType) {
                viewNameTi = 'viewName' + actionType
                viewIdTi = 'viewId' + actionType
                pictureTypeTi = 'pictureType' + actionType
                if (state && state.style[viewNameTi] && state.style[viewIdTi] && (state.style[pictureTypeTi] || state.style[pictureTypeTi] === 0)) {
                    window.parent.postMessage({
                        cmd: 'getChildView',
                        params: {
                            name: state.style[viewNameTi],
                            precinctId: state.style[viewIdTi],
                            pictureType: state.style[pictureTypeTi]
                        }
                    }, '*');
                }
            }
            // 打开文件
            openFile = function (state, actionType) {
                uploadFileUrlTi = "uploadFileUrl" + actionType
                if (state && state.style[uploadFileUrlTi]) {
                    window.parent.postMessage({
                        cmd: 'openUploadFile',
                        params: {
                            success: true,
                            data: state.style[uploadFileUrlTi]
                        }
                    }, '*');
                }
            }

            // 接受主系统传递过来的值
            window.addEventListener("message", function (event) {
                switch (event.data.cmd) {
                    case 'sendData':
                        show(event.data.params)
                        break
                    case 'bindNode':
                        bindNode(newEditorUi, JSON.parse(event.data.params.data))
                        break
                    case 'zoomIn':
                        // 放大
                        fangDa()
                        break
                    case 'zoomOut':
                        // 缩小
                        suoXiao()
                        break
                    case 'back':
                        // 回到初始位置
                        back()
                        break
                    case 'exportData':
                        // 回到初始位置
                        exportData()
                        break
                    case 'moveCanvas':
                        // 回到初始位置
                        moveCanvas(event.data.params.data)
                        break
                    case 'sendViewPort':
                        // 自适应窗口大小
                        sendViewPort(event.data.params.data)
                        break
                    case 'getPath':
                      // 自适应窗口大小
                      getPath(event.data.params)
                      break
                }
            }, false);

            // window.parent.ptostMessage({type: 'childStatus', data: 'isReady'}, '*')
          function getPath(data){
            let oldDada = data.oldPathList
            if(oldDada && oldDada.length>0){
              oldDada.forEach(d=>{
                var nodes = graph.getModel().cells;
                let state = graph.view.getState(nodes[d]);
                var path = state.shape.node.getElementsByTagName('path')
                for (let i = 1; i < path.length; i++) {
                  // path[i].setAttribute('class', 'selected');
                  path[i].classList.remove('pathSelected')
                }
              })
            }
            let newDada = data.pathList
            if(newDada && newDada.length>0){
              newDada.forEach(d=>{
                var nodes = graph.getModel().cells;
                let state = graph.view.getState(nodes[d]);
                var path = state.shape.node.getElementsByTagName('path')
                for (let i = 1; i < path.length; i++) {
                  // path[i].setAttribute('class', 'selected');
                  path[i].classList.add('pathSelected')
                }
              })
            }
          };
            function show(data) {
                if (!data.data) {
                    return
                } else {
                    var content = JSON.parse(data.data)

                    // var content = data.data
                    var resValue = data.resValue
                    var bindObjList = data.bindObjList
                    newEditorUi.editor.graph.model.beginUpdate();
                    try {
                        newEditorUi.editor.setGraphXml(mxUtils.parseXml(content).documentElement);
                    } catch (e) {
                        error = e;
                    } finally {
                        newEditorUi.editor.graph.model.endUpdate();
                    }
                    var xmlDoc = mxUtils.parseXml(content);
                    var codec = new mxCodec(xmlDoc);
                    var code = codec.decode(xmlDoc.documentElement);

                    // 自适应居中
                    newEditorUi.editor.graph.zoomTo(1);
                    newEditorUi.resetScrollbars(); //位置

                    //setBackground
                    // newEditorUi.editor.graph.background = 'rgb(255, 255, 255)'
                    newEditorUi.editor.graph.view.validateBackground();

                    // setBgimage
                    if (code.backgroundImage) {
                        var img = new mxImage(code.backgroundImage, code.cells[0].imageWidth, code.cells[0].imageHeight);
                        graph.setBackgroundImage(img);
                        graph.view.validateBackgroundImage();
                    } else {
                        graph.setBackgroundImage();
                        graph.view.validateBackgroundImage();
                    }

                    // setscale
                    if (data.width && data.height) {
                        sendViewPortData = {
                            width: data.width,
                            height: data.height
                        }
                        setViewPort({
                            width: data.width,
                            height: data.height
                        })
                    }
                    if (!(data.width && data.height) && sendViewPortData) {
                        setViewPort(sendViewPortData)
                    }
                    // 取消滚动条
                    geDiagramContainerSc(2)

                    // setflow
                    flow(graph)

                    var nodes = graph.getModel().cells;

                    // Object.keys(nodes).forEach(function (key) {
                    //
                    // })
                    bindObjList && bindObjList.forEach(function (item) {
                        //值展示
                        if (item.valueView && item.value > 0) {
                            graph.getModel().beginUpdate();
                            try {
                                // graph.setCellStyles('bandType', index, graph.getSelectionCells());
                                let name = item.name ? item.name + ":" : "告警值："
                                var calcType = item.conversionType == 1 ? '*' : item.conversionType == 2 ? '/' : null
                                if (calcType && item.conversionVal && item.conversionVal != '') {
                                    item.value = eval(item.value + calcType + item.conversionVal).toFixed(2)
                                }
                                var value =  name + item.value + (item.unit || "")
                                if (item.bandType == 1) {
                                    value = nodes[item.id].getValue() ? nodes[item.id].getValue() + "\n" + value : value
                                    nodes[item.id].setValue(value)
                                    graph.getModel().setValue(nodes[item.id], value)
                                    graph.setCellStyles('value', item.value, nodes[item.id]);
                                } else {
                                    graph.setCellWarning(nodes[item.id], `<b>${value}</b>`)
                                }
                                // graph.refresh(nodes[item.id])
                            } finally {
                                graph.getModel().endUpdate();
                            }
                            // nodes[item.id].valueChanged(item.value)
                        }
                        let state = graph.view.getState(nodes[item.id]);
                        state.style.value = item.value
                        //颜色展示
                        if (item.colorView) {
                            // 告警值颜色
                            if (state.style.bandType == 0) {
                                if (item.value > 0) {
                                    if (state.cell.edge) { // 线条
                                        // let color = ''
                                        let style = nodes[item.id].style.split(';')
                                        style.push(mxConstants.STYLE_STROKECOLOR + '=' + 'red')
                                        graph.getModel().beginUpdate();
                                        try {
                                            // graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, color, nodes[item.id]);
                                             graph.getModel().setStyle(nodes[item.id], style.join(';'))
                                        } finally {
                                            graph.getModel().endUpdate();
                                        }
                                        for (let i = 1; i < state.shape.node.getElementsByTagName('path').length; i++) {
                                            // state.shape.node.getElementsByTagName('path')[i].appendChild(styleCustomiz);
                                            state.shape.node.getElementsByTagName('path')[i].setAttribute('class', `checkFlow`)
                                            // ${'.'+item.id+'Color'} {animation: ${item.id+'Node'} 1s infinite linear;}
                                            // state.shape.node.getElementsByTagName('path')[i].style.animation = `node4 1s infinite linear;`
                                        }
                                    } else {  //图标
                                        let style = nodes[item.id].style.split(';')
                                        var newstyle = []
                                        style.forEach(function (val, index) {
                                            if (val.indexOf('image=') >= 0) {
                                                let imagestyle = style[index]
                                                let imagearr = imagestyle.split('=')
                                                imagearr[1] = imagearr[1].split('.')[0] + '2.svg'
                                                val = imagearr.join('=')
                                                newstyle.push(val)
                                            } else {
                                                newstyle.push(val)
                                            }
                                        })
                                        // newstyle.push('class=checkFlow')
                                        newstyle = newstyle.join(';')
                                        graph.getModel().beginUpdate();
                                        try {
                                            // graph.setCellStyles(mxConstants.STYLE_IMAGE_BACKGROUND	, "#FF3030", nodes[item.id])
                                            // graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, "#FF3030", nodes[item.id]);
                                            graph.getModel().setStyle(nodes[item.id], newstyle)
                                        } finally {
                                            graph.getModel().endUpdate();
                                        }
                                    }
                                }
                            }
                            // 监控值颜色
                            if (state.style.bandType == 1 && state.cell.edge) {
                                for (var sillIndex = 0; sillIndex < item.sillList.length; sillIndex++) {
                                    var sillItem = item.sillList[sillIndex]
                                    var passFlag = false
                                    if (sillItem.expressionSymbol && sillItem.expressionValue) {
                                        try {
                                            passFlag = eval(item.value + sillItem.expressionSymbol + sillItem.expressionValue)
                                        } catch (e) {
                                            console.log("表达式计算错误", e)
                                        }
                                    }
                                    if (passFlag) {
                                        // if (state.style.class) {
                                        let color = sillItem.color
                                        let style = nodes[item.id].style.split(';')
                                        style.push(mxConstants.STYLE_STROKECOLOR + '=' + color)
                                        style.push(`class=color${item.id}`)
                                        graph.getModel().beginUpdate();
                                        try {
                                            // graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, color, nodes[item.id]);
                                             graph.getModel().setStyle(nodes[item.id], style.join(';'))
                                        } finally {
                                            graph.getModel().endUpdate();
                                        }
                                        var runkeyframes = `
                                         .color${item.id} {
                                           animation: node${item.id} 1s infinite linear;
                                         }
                                         @keyframes node${item.id} {
                                            0% {
                                              opacity: 1;
                                              stroke: ${color};
                                            }
                                            100% {
                                              opacity: 0;
                                              stroke: ${color};
                                            }
                                          }
                                         `

                                        // 创建style标签
                                        const styleCustomiz = document.createElement('style');
                                        // 设置style属性
                                        styleCustomiz.type = 'text/css';
                                        // 将 keyframes样式写入style内
                                        styleCustomiz.innerHTML = runkeyframes;
                                        document.getElementsByTagName('head')[0].appendChild(styleCustomiz);
                                        for (let i = 1; i < state.shape.node.getElementsByTagName('path').length; i++) {
                                            // state.shape.node.getElementsByTagName('path')[i].appendChild(styleCustomiz);
                                            state.shape.node.getElementsByTagName('path')[i].setAttribute('class', `color${item.id}`)
                                            // ${'.'+item.id+'Color'} {animation: ${item.id+'Node'} 1s infinite linear;}
                                            // state.shape.node.getElementsByTagName('path')[i].style.animation = `node4 1s infinite linear;`
                                        }
                                        break
                                    }
                                }

                            }
                        }

                    })

                    // 告警图标显示
                    Object.keys(nodes).forEach(function (key) {
                        let state = graph.view.getState(nodes[key]);
                        resValue && resValue.forEach(function (item) {
                            if (item.id === state.style.id) {

                                var levelLabel = ''
                                var levelCount = ''
                                switch (state.style.bandNum) {
                                    case 5:
                                        levelLabel = '一级告警'
                                        levelCount = resValue[0].levelsCount[1]
                                        break;
                                    case 6:
                                        levelLabel = '二级告警'
                                        levelCount = resValue[0].levelsCount[2]
                                        break;
                                    case 7:
                                        levelLabel = '三级告警'
                                        levelCount = resValue[0].levelsCount[3]
                                        break;
                                    case 8:
                                        levelLabel = '四级告警'
                                        levelCount = resValue[0].levelsCount[4]
                                        break;
                                    case 4:
                                        levelLabel = '总告警'
                                        levelCount = resValue[0].levelsCount[5]
                                        break;
                                }
                                var value = levelLabel + '  ' + levelCount

                                if (nodes[key].geometry && state.style.isShowAlarm === 'true' && !nodes[key].edge && item.hasAlert) {
                                    let style = nodes[key].style.split(';')
                                    var newstyle = []
                                    style.forEach(function (val, index) {
                                        if (val.indexOf('image=') >= 0) {
                                            let imagestyle = style[index]
                                            let imagearr = imagestyle.split('=')
                                            imagearr[1] = imagearr[1].split('.')[0] + '_alert.svg'
                                            val = imagearr.join('=')
                                            newstyle.push(val)
                                        } else {
                                            newstyle.push(val)
                                        }
                                    })
                                    newstyle.push('verticalAlign=bottom')
                                    newstyle.push('spacingBottom=-28')
                                    newstyle.push('fontSize=20')
                                    newstyle = newstyle.join(';')
                                    graph.getModel().beginUpdate();
                                    try {
                                        graph.getModel().setStyle(nodes[key], newstyle)
                                        graph.getModel().setValue(nodes[key], value)
                                    } finally {
                                        graph.getModel().endUpdate();
                                    }
                                }
                            }
                        })
                    })

                    function createOverlay(image, tooltip) {
                        var overlay = new mxCellOverlay(image, tooltip);
                        // Installs a handler for clicks on the overlay
                        overlay.addListener(mxEvent.CLICK, function (sender, evt) {
                            mxUtils.alert(tooltip);
                        });
                        return overlay;
                    }

                    // 设置全局流动线
                    // window.isEdgeID[_setFlowIndex] = []
                    // window.isCellIDDown[_setFlowIndex] = []
                    // Object.keys(nodes).forEach(function (key) {
                    //   if (nodes[key].edge) {
                    //     window.isEdgeID[_setFlowIndex].push(nodes[key].id)
                    //   } else {
                    //     window.isCellIDDown[_setFlowIndex].push(nodes[key].id)
                    //   }
                    // })
                    // _setFlow_ID(window.isEdgeID[_setFlowIndex], true)
                    // window._setFlowIndex++
                }
            }

            // function sendViewPort(data) {
            //   setViewPort(data)
            //   sendViewPortData = data
            // }

            function setViewPort(data) {
                var pageBounds = graph.view.graphBounds
                var flage = false
                if (pageBounds.width && pageBounds.height && data.width && data.height) {
                    pageBounds = graph.view.backgroundPageShape.bounds
                    if (data.width >= pageBounds.width && data.height >= pageBounds.height) {
                        return
                    }

                    if (Math.abs(data.width - pageBounds.width) <= Math.abs(data.height - pageBounds.height)) {
                        window._globalScale = parseFloat(100 * (data.height / (pageBounds.height))) / 100
                        graph.zoomTo(parseFloat(100 * (data.height / (pageBounds.height))) / 100)
                        flage = true
                    } else {
                        flage = false
                        window._globalScale = parseFloat(100 * (data.width / (pageBounds.width))) / 100
                        graph.zoomTo(parseFloat(100 * (data.width / (pageBounds.width))) / 100)
                    }

                    if (mxUtils.hasScrollbars(graph.container)) {
                        var pad = graph.getPagePadding();
                        if (!flage) {
                            var padding = data.padding || 0
                            graph.container.scrollTop = graph.container.scrollHeight / 2 - graph.container.clientHeight / 2 - padding - 20
                        } else {
                            graph.container.scrollTop = pageBounds.y - 40
                        }
                        graph.container.scrollLeft = Math.min(pad.x * graph.view.scale, (graph.container.scrollWidth - graph.container.clientWidth) / 2) - 1;
                    }
                }
            }

            function fangDa() {
                geDiagramContainerSc(1)
                newEditorUi.editor.graph.zoomIn();
            }

            function suoXiao() {
                geDiagramContainerSc(1)
                newEditorUi.editor.graph.zoomOut();
            }

            function back() {
                geDiagramContainerSc(1)
                newEditorUi.editor.graph.zoomTo(1);
                // newEditorUi.resetScrollbars();
                setViewPort(sendViewPortData)
                geDiagramContainerSc(2)
                moveCanvas(0)
                window.parent.postMessage({
                    cmd: 'resetMoveCanvas',
                    params: {
                        success: true
                    }
                }, '*');
            }

            function exportData() {
                var xml = mxUtils.getXml(newEditorUi.editor.getGraphXml())
                window.parent.postMessage({
                    cmd: 'xmlData',
                    params: {
                        success: true,
                        datas: xml
                    }
                }, '*');
            }

            function moveCanvas(type) {
                var graph = newEditorUi.editor.graph
                var spaceKeyPressed = false;

                if (type) {
                    geDiagramContainerSc(1)
                    spaceKeyPressed = true;
                    newEditorUi.hoverIcons.reset();
                    graph.container.style.cursor = 'move';
                } else {
                    graph.container.style.cursor = '';
                    spaceKeyPressed = false;
                    geDiagramContainerSc(2)
                }
                graph.panningHandler.isForcePanningEvent = function (me) {
                    return spaceKeyPressed;
                };
            }
        },
        function () {
            document.body.innerHTML =
                '<center style="margin-top:10%;">Error loading resource files. Please check browser console.</center>';
        });
})();