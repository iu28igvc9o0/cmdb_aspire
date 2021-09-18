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
    mxResources.parse(grapheditor);
    // Configures the default graph theme
    var themes = new Object();
    themes[Graph.prototype.defaultThemeName] = mxUtils.parseXml(defaultXml).documentElement;
    // 实例化 EditorUi
    // 如果缓存不为空，则读取缓存数据渲染
    window.newEditorUi = new EditorUi(new Editor(urlParams['chrome'] == '0', themes));
    // graph
    var graph = newEditorUi.editor.graph
    // console.log(newEditorUi)
    // console.log(urlParams['chrome'] == '0')
    // console.log(new Editor(urlParams['chrome'] == '0', themes))

    // 接受主系统传递过来的值

    window.addEventListener("message", function (event) {
        var data = event.data
        // console.log(data)
        switch (data.cmd) {
            case 'sendData':
                initEditor(newEditorUi, data)
                break
            case 'sendProjectNameData':
                setProjectName(newEditorUi, data)
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
            case 'backLoadExcelData':
                backLoadExcelData(newEditorUi, data.params.excelData)
                break
        }
    }, false);
    // graph.addListener(mxEvent.CELLS_ADDED, (sender, evt) => {
    //     console.log(evt)
    //     console.log(evt.properties)
    //     if (evt.properties.cells.length > 1) return false
    //     const cell = evt.properties.cells[0];
    //     // if (graph.isPart(cell)) {
    //     //     return;
    //     // }
    //     console.log(cell)
    //     console.log(cell.getStyle())
    //     if (cell.style.indexOf('needInputCode') >= 0) {
    //         console.log(graph.getModel())
    //         document.querySelector('.dialog-wrapper').style.display = 'block'
    //         graph.getModel().beginUpdate();
    //         try {
    //
    //             // graph.getModel().setStyle(cell, {a: 123})
    //         } finally {
    //             graph.getModel().endUpdate();
    //         }
    //     }
    // });
    // graph.addListener('dblClick', function (sender, evt) {
    //     let state = graph.view.getState(evt);
    //     if (cell.style.indexOf('needInputCode') >= 0) {
    //         document.querySelector('.dialog-wrapper').style.display = 'block'
    //         setTimeout(function() {
    //             if (state.style.precinctId || state.style.precinctId === 0) {
    //                 document.querySelector('#precinctCode').value = state.style.precinctId
    //             }
    //         })
    //     }
    // })

    function setProjectName(newEditorUi, data) {
        if (data.params.projectNameList) {
            var $projectName = document.querySelector('.projectName');
            if ($projectName) {
                $projectName.innerHTML = "";
                var projectNameDefaultOption = document.createElement('option')
                projectNameDefaultOption.value = ""
                projectNameDefaultOption.innerText = "请选择"
                $projectName.appendChild(projectNameDefaultOption)
                window.projectNameOptionData = data.params.projectNameList
                for (var index = 0; index < projectNameOptionData.length; index++) {
                    var projectNameOption = document.createElement('option')
                    projectNameOption.value = projectNameOptionData[index].project_name
                    projectNameOption.innerText = projectNameOptionData[index].project_name
                    $projectName.appendChild(projectNameOption)
                    if (sessionStorage.getItem('projectName') == projectNameOption.value) {
                        projectNameOption.selected = true
                    }
                }
                sessionStorage.setItem('projectName', projectNameSelect.value)
            }
            // projectNameSelect.value = sessionStorage.getItem('projectName')
        }
    }
    // 初始化视图
    function initEditor(newEditorUi, data) {
        var $viewTitle = document.querySelector('.view-title');
        if (data.params.name) {
            sessionStorage.setItem('viewTitle', data.params.name)
            $viewTitle.value = data.params.name
            // $viewBindInput.value = data.params.name
        } else {
            sessionStorage.setItem('viewTitle', '')
            $viewTitle.value = ''
            // $viewBindInput.value = ''
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
        // 读取pod
        // var $idcType = document.querySelector('.idcType');
        if (data.params.projectName) {
            sessionStorage.setItem('projectName', data.params.projectName)
            // $idcType.value = data.params.idc
        } else {
            sessionStorage.setItem('projectName', '')
            // $idcType.value = ''
        }
        if (data.params.idc) {
            sessionStorage.setItem('idcType', data.params.idc)
            // $idcType.value = data.params.idc
        } else {
            sessionStorage.setItem('idcType', '')
            // $idcType.value = ''
        }
        if (data.params.pod) {
            sessionStorage.setItem('pod', data.params.pod)
            // $idcType.value = data.params.idc
        } else {
            sessionStorage.setItem('pod', '')
            // $pictureType.value = ''
        }
        if (data.params.bizSystem) {
            sessionStorage.setItem('bizSystem', data.params.bizSystem)
            // $idcType.value = data.params.idc
        } else {
            sessionStorage.setItem('bizSystem', '')
            // $pictureType.value = ''
        }
        if (data.params.idcList) {
            window.idcTypeOptionData = data.params.idcList
        }
        if (data.params.podList) {
            window.podOptionData = data.params.podList
        }
        if (data.params.bizSystemList) {
            window.bizSystemOptionData = data.params.bizSystemList
        }
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
            graph.setCellStyles('bandTypeTemp', null, graph.getSelectionCells());
            graph.setCellStyles('isBind', null, graph.getSelectionCells());
            graph.setCellStyles('name', null, graph.getSelectionCells());
            graph.setCellStyles('unit', null, graph.getSelectionCells());
            graph.setCellStyles('conversionType', null, graph.getSelectionCells());
            graph.setCellStyles('conversionVal', null, graph.getSelectionCells());
            graph.setCellStyles('valueView', null, graph.getSelectionCells());
            graph.setCellStyles('colorView', null, graph.getSelectionCells());
            graph.setCellStyles('sillList', null, graph.getSelectionCells());
            graph.setCellStyles('countType', null, graph.getSelectionCells());
            graph.setCellStyles('alertLevelList', null, graph.getSelectionCells());
            graph.setCellStyles('deviceList', null, graph.getSelectionCells());
            graph.setCellStyles('itemList', null, graph.getSelectionCells());
            Object.keys(data).forEach(function (item) {
                graph.setCellStyles(item, JSON.stringify(data[item]), graph.getSelectionCells());
            })
            graph.setCellStyles('bandType', data.bandType, graph.getSelectionCells());
        } finally {
            graph.getModel().endUpdate();
        }
        // var $nodeBindObject = document.getElementById('bindDataDiv-bandNode')
        // debugger
        // if (data.deviceList) {
        //     let ipList = []
        //     for (let i = 0; i < data.deviceList.length; i++) {
        //         ipList.push(data.deviceList[i].idcType + ':' + data.deviceList[i].ip)
        //     }
        //     let ipBindDiv = document.createElement('div')
        //     ipBindDiv.className = 'part_Div'
        //     let ipBindLableP = document.createElement('P')
        //     ipBindLableP.className = 'lable_Title'
        //     ipBindLableP.innerText = '绑定IP'
        //     let ipRightDiv = document.createElement('div')
        //     ipRightDiv.style.paddingLeft = '20px'
        //     ipRightDiv.className = 'panel_container'
        //     let ipAextArea = document.createElement('textarea')
        //     ipAextArea.className = 'ipArea'
        //     ipAextArea.value = ipList.join(',')
        //     ipRightDiv.appendChild(ipAextArea)
        //     ipBindDiv.appendChild(ipBindLableP)
        //     ipBindDiv.appendChild(ipRightDiv)
        //     $nodeBindObject.appendChild(ipBindDiv)
        // }
    }

    // 节点绑定视图
    function nodeBindView(newEditorUi, data) {
        // console.log(data)
        var graph = newEditorUi.editor.graph;
        var switchType = sessionStorage.getItem('switchType') || 0
        Object.keys(data).forEach(function (item) {
            graph.setCellStyles(item + switchType, data[item], graph.getSelectionCells());
        })
    }
    // 节点绑定上传的文件
    function nodeBindUploadFile(newEditorUi, data) {
        // console.log(data)
        var graph = newEditorUi.editor.graph;
        var switchType = sessionStorage.getItem('switchType') || 0

        graph.setCellStyles('uploadFileName' + switchType, data.name, graph.getSelectionCells());
        graph.setCellStyles('uploadFileUrl' + switchType, data.url, graph.getSelectionCells());

    }

    function backLoadExcelData (newEditorUi, data) {
        // console.log(data)
        var graph = newEditorUi.editor.graph;
        var cellIndex = {}
        var ExcelStyle = {
            // ATS柜
            atsg: "shape=image;needInputCode=true;warning=true;type=ATS;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/ATS_S.svg",
            // 变压器
            byq: "shape=image;needInputCode=true;warning=true;type=byq;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/byq_S.svg",
            // 油机
            yj: "shape=image;needInputCode=true;warning=true;type=yj;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/yj_S.svg",
            // 低压出线柜
            dycxg: "shape=image;needInputCode=true;warning=true;type=dycxg;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/dycxg_S.svg",
            // 低压进线柜
            dyjxg: "shape=image;needInputCode=true;warning=true;type=dyjxg;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/dyjxg_S.svg",
            // 配电柜
            pdg: "shape=image;needInputCode=true;warning=true;type=pdg;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/pdg_S.svg",
            // 列头柜
            ltg: "shape=image;needInputCode=true;warning=true;type=ltg;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/ltg_S.svg",
            // 微模块
            wmk: "shape=image;needInputCode=true;warning=true;type=wmk;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/wmk_S.svg",
            // UPS
            UPS: "shape=image;needInputCode=true;warning=true;type=UPS;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationPeiDian/UPS_S.svg",
            // 开关电源
            kgdy: "shape=image;needInputCode=true;warning=true;type=dykg;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationPeiDian/dykg_S.svg",
            // 高压直流
            gyzl: "shape=image;needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationPeiDian/gyzl_S.svg"
        }
        var excelKeys = {
            atsg: 'ATS柜',
            byq: '变压器',
            yj: '油机',
            dycxg: '出线柜',
            dyjxg: '进线柜',
            pdg: '配电柜',
            ltg: '列头柜',
            wmk: '微模块',
            UPS: 'UPS',
            kgdy: '开关电源',
            gyzl: '高压直流'
        }
        var parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        try {
            var pageWidth = 1880
            // 变压器 一层
            var voltageListlength = data.voltageList.length
            data.voltageList.forEach(function (item, index) {
                var style = ''
                var value = ''
                var left = (pageWidth - ((voltageListlength - 1) * 200)) / 2
                Object.keys(excelKeys).forEach(function (val) {
                    if (item.name.indexOf(excelKeys[val]) >= 0) {
                        style = ExcelStyle[val]
                        value = item.name
                    }
                })
                var cell = graph.insertVertex(parent, null, value, left + (200 * index), 200, 48, 48, style);
                cellIndex[item.index] = cell
            })
            // 出线柜 二层
            var cabinetListlength = data.cabinetList.length
            data.cabinetList.forEach(function (item,index) {
                var style = ''
                var value = ''
                var left = (pageWidth - ((cabinetListlength - 1) * 200)) / 2
                Object.keys(excelKeys).forEach(function (val) {
                if (item.name.indexOf(excelKeys[val]) >= 0) {
                    style = ExcelStyle[val]
                    value = item.name
                }
            })
                var cell = graph.insertVertex(parent, null, value, left + (200 * index), 400, 48, 48, style);
                cellIndex[item.index] = cell
            })
            // UPS 三层
            var cupsListlength = data.upsList.length
            data.upsList.forEach(function (item, index) {
                var style = ''
                var value = ''
                var left = (pageWidth - ((cupsListlength - 1) * 200)) / 2
                Object.keys(excelKeys).forEach(function (val) {
                    if (item.name.indexOf(excelKeys[val]) >= 0) {
                        style = ExcelStyle[val]
                        value = item.name
                    }
                })
                var cell = graph.insertVertex(parent, null, value, left + (200 * index), 600, 48, 48, style);
                cellIndex[item.index] = cell
            })
            // 机楼 四层
            var deviceListlength = data.deviceList.length
            data.deviceList.forEach(function (item, index) {
                var left = (pageWidth - ((deviceListlength - 1) * 200)) / 2
                var style = "shape=image;needInputCode=true;warning=true;type=ltg;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/simulationDiya/ltg_S.svg";
                var cell = graph.insertVertex(parent, null, item.name, left + (200 * index), 800, 48, 48, style);
                cellIndex[item.index] = cell
            })
            data.lineList.forEach(function (item) {
                var e1 = graph.insertEdge(parent, null, '', cellIndex[item.to], cellIndex[item.from], "edgeStyle=elbowEdgeStyle;class=flowx;rounded=0;elbow=vertical;html=1;jettySize=auto;orthogonalLoop=1;")
            })
        } finally {
            graph.getModel().endUpdate();
        }
    }
})();