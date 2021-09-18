<template>
    <div class="hp100"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <!-- 查询设备拓扑图 -->
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search()"
                 ref="formSearch"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="资源池"
                          prop="idc">
                <el-select v-model="formSearch.idc"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in resourcePoolList"
                               :key="val.id"
                               :label="val.name"
                               :value="val.name"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="ip地址"
                          prop="deviceIp"
                          label-width="75px">
                <el-input v-model="formSearch.deviceIp"
                          placeholder="请输入ip地址"
                          clearable></el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询</el-button>
            </section>
        </el-form>
        <div id="toolBar"></div>
        <!-- 设备提示信息 -->
        <div class="tooltip-box"
             ref="toolTipBox"
             v-show="tipShow"
             v-html="tipsText"></div>
        <!-- 展示设备拓扑图 -->
        <div id="graphContainer"></div>
    </div>
</template>

<script>
    import TopoApi from 'src/services/topo'
    import mxgraph from './index.js'
    const {
        mxGraph,
        mxUtils,
        mxConstants,
        mxEvent,
        mxRectangle,
        mxImage,
        mxEdgeStyle,
        mxCompactTreeLayout,
        mxLayoutManager,
        mxCylinder,
        mxGraphView,
        mxPoint,
        mxCellRenderer,
        mxShape,
    } = mxgraph
    export default {
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                // 设备拓扑图
                resourcePoolList: [], // 资源池列表
                formSearch: {
                    idcType: '哈尔滨资源池',
                    ip: '10.198.98.169'
                },
                deviceInfo: {}, // 资源池设备信息
                deviceRelationList: [], // 资源池设备关系节点
                showTypes: ['上联', '下联', '连接'],
                childNodes: [], // 点击节点查询出来的子节点
                allNodesList: [], // 所有的节点扁平数组
                geometryY: '', // 新节点的y坐标
                mouseTimer: null, // mousemove 节流
                clickTimer: null, // click 节流
                clickCount: 0,
                alertCount: null, // 查询告警节流
                tipShow: false,
                tipsText: '', // 节点提示框信息
                currentCellId: '', // 当前hover/click节点的关系id
                deviceAlert: [], // 当前设备告警情况
                graphObj: null, // graph实例
                hoverCellDetails: null, // 鼠标悬浮时请求获取该节点信息
            }
        },
        computed: {
            currentCellInfo() {
                // 比对该关系id，返回当前节点数据
                let data = this.allNodesList.find(item => {
                    return item.id === this.currentCellId
                })
                return data
            },
            allNodesIpAndIdctype() {
                let arr = []
                this.allNodesList.forEach(item => {
                    if (item.ip) {
                        arr.push({
                            ip: item.ip,
                            idcType: item.idcType
                        })
                    } else {
                        arr.push({
                            ip: item.zIp,
                            idcType: item.zIdcType
                        })
                    }
                })
                return arr
            }
        },
        watch: {
            tipShow(val) {
                if (val) {
                    // 处理提示框超出屏幕
                    this.$nextTick(() => {
                        let tipBox = this.$refs.toolTipBox
                        let boxWidth = tipBox.clientWidth
                        let boxHeight = tipBox.clientHeight
                        let top = tipBox.style.top.replace('px', '') * 1
                        let left = tipBox.style.left.replace('px', '') * 1
                        let offsetTop = this.getOffset(tipBox, 'offsetTop')
                        let offsetLeft = this.getOffset(tipBox, 'offsetLeft')
                        let docWidth = document.body.clientWidth
                        let docHeight = document.body.clientHeight
                        if (
                            offsetTop + boxHeight > docHeight &&
                            offsetLeft + boxWidth > docWidth
                        ) {
                            tipBox.style.top = top - boxHeight / 2 + 'px'
                            tipBox.style.left = left - boxWidth / 2 + 'px'
                        }
                        if (offsetTop + boxHeight > docHeight) {
                            tipBox.style.top = top - boxHeight / 2 + 'px'
                        }
                        if (offsetLeft + boxWidth > docWidth) {
                            tipBox.style.left = left - boxWidth / 2 + 'px'
                        }
                    })
                }
            }
        },
        mounted: function () {
            this.getDictsByType()
        },
        components: {},
        methods: {
            setWarning(graph, id) {
                let cellsObj = graph.getModel().cells
                let cId = Object.keys(graph.getModel().cells).find(cellId => {
                    return cellId === id
                })
                graph.convertValueToString(cellsObj[cId], this.updateDom)
                // graph.center(true, true, 0.5, 0.5); //将画布放到容器中间 
            },
            updateDom(cell) {
                cell.div.style.filter = 'drop-shadow(60px 0px 0px red)'
            },
            // 计算有告警的节点信息
            getAlertNode() {
                this.graphObj.zoomIn()
                this.graphObj.zoomOut()
                this.deviceAlert.forEach(item => {
                    this.allNodesList.forEach(node => {
                        // console.log('node告警22==', node, item)
                        let firstNodeHasAlert = node.ip && node.ip === item.ip
                        let childNodeHasAlert = node.zIp === item.ip
                        if ((firstNodeHasAlert || childNodeHasAlert) && item.count) {
                            // console.log("node告警11==", node, item);
                            this.setWarning(this.graphObj, node.id)
                        }
                    })
                })
            },
            // 获取元素距离文档边缘距离
            getOffset(obj, type) {
                var tmp = obj[type]
                var node = obj.offsetParent
                while (node != null) {
                    tmp += node[type]
                    node = node.offsetParent
                }
                return tmp
            },
            // 初始化当前设备拓扑图
            initFormSearch(queryParams) {
                this.formSearch.idc = queryParams.idcType
                this.formSearch.deviceIp = queryParams.ip
                this.search()
            },
            // 计算该设备是否有未显示的关系节点，剔除已经显示的关系节点(已存在this.allNodesList里)
            handleRepeatedNodes(list) {
                let deviceList = JSON.parse(JSON.stringify(list))
                deviceList.forEach((device, index) => {
                    this.allNodesList.forEach(item => {
                        if (device.zInstanceId === item.id || device.zInstanceId === item.zInstanceId) {
                            deviceList.splice(index, 1)
                        }
                    })
                })
                return deviceList
            },
            // 目前仅显示 上联、下联、连接
            filterNodeTypes(list) {
                return list.filter(item => {
                    return this.showTypes.includes(item.connectType)
                })
            },
            // 根据本端设备id查询设备关系
            queryInstancePortRelation(params, type, graph, parent, cell) {
                params.pageNo = 1
                params.pageSize = 50
                this.pageLoading = true
                TopoApi.queryInstancePortRelation(params)
                    .then(res => {
                        this.pageLoading = false
                        if (type === 'getChild') {
                            // 计算该设备是否有未显示的关系节点，剔除已经显示的关系节点
                            this.childNodes = this.handleRepeatedNodes(res.data)
                            this.childNodes = this.filterNodeTypes(this.childNodes)
                            if (this.childNodes.length && cell && cell.edges.length <= 1) {
                                this.allNodesList = [].concat(this.allNodesList, res.data)
                                this.insertChildNode(graph, parent, cell)
                            }
                            if (!this.childNodes.length) {
                                this.$message.warning('该设备已无其他关系节点')
                            }
                        } else {
                            this.deviceRelationList = res.data
                            this.deviceRelationList = this.filterNodeTypes(this.deviceRelationList)
                            this.allNodesList = [].concat(this.allNodesList, this.deviceRelationList)
                            this.initTopo(document.getElementById('graphContainer'))
                        }
                        // 查询告警数量
                        this.getAlertCountByIpAndIdc()
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.$message.error(error)
                    })
            },
            // 根据资源池和ip查询第一台设备详情
            queryDeviceByRoomAndIP() {
                let data = this.formSearch
                TopoApi.queryDeviceByRoomAndIP(data)
                    .then(res => {
                        // 第一台设备详情
                        this.deviceInfo = res
                        if (!res) {
                            this.$message.error('暂无设备信息！')
                            return
                        }
                        this.queryInstancePortRelation({ aInstanceId: this.deviceInfo.id })
                        // 每次搜索重新存储节点
                        this.allNodesList = []
                        this.allNodesList.push(res)
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.$message.error(error)
                    })
            },
            // 根据资源池和ip查询子节点设备详情
            queryDeviceDetail(params, evt, cell, callback) {
                TopoApi.queryDeviceByRoomAndIP(params)
                    .then(res => {
                        this.hoverCellDetails = res
                        // console.log("this.hoverCellDetails====", this.hoverCellDetails);
                        callback && callback(evt, cell)
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.$message.error(error)
                    })
            },
            // 根据资源池和ip查询设备告警数量
            getAlertCountByIpAndIdc() {
                TopoApi.getAlertCountByIpAndIdc(this.allNodesIpAndIdctype)
                    .then(res => {
                        this.deviceAlert = res
                        this.getAlertNode()
                        if (this.alertCount) {
                            clearTimeout(this.alertCount)
                        }
                        this.alertCount = setTimeout(() => {
                            this.getAlertCountByIpAndIdc()
                        }, 60000)
                    })
                    .catch(() => {
                        this.pageLoading = false
                    })
            },
            // 获取资源池
            getDictsByType() {
                let data = {
                    type: 'idcType'
                }
                TopoApi.getDictsByType(data).then(res => {
                    this.resourcePoolList = res

                    // 如果链接带了参数，直接初始化拓扑图
                    let queryParams = JSON.parse(this.$route.query.queryParams)
                    if (queryParams.ip) {
                        this.initFormSearch(queryParams)
                    }
                })
            },
            // 查询设备拓扑图
            search() {
                if (!this.formSearch.idc) {
                    this.$message.warning('请选择资源池')
                    return
                }
                if (!this.formSearch.deviceIp) {
                    this.$message.warning('请输入ip地址')
                    return
                }
                this.queryDeviceByRoomAndIP()
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },
            initTopo(container) {
                const self = this
                container.innerHTML = ''
                // Creates the graph inside the given container
                var graph = new mxGraph(container)
                self.graphObj = graph
                const parent = graph.getDefaultParent()

                // Set some stylesheet options for the visual appearance
                var style = new Object()
                // style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_IMAGE;  //节点为图片类型

                style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_LABEL // 节点为形状类型
                style[mxConstants.STYLE_STROKECOLOR] = 'none' // 无边框
                style[mxConstants.STYLE_FILLCOLOR] = 'none' // 节点无背景

                style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER
                style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP
                style[mxConstants.STYLE_FONTSIZE] = 9
                style[mxConstants.STYLE_FONTSTYLE] = 0
                // style[mxConstants.STYLE_SPACING_TOP] = -10;
                // style[mxConstants.STYLE_SPACING_RIGHT] = 20;
                // style[mxConstants.STYLE_ROTATION] = 45; // 旋转角度
                style[mxConstants.STYLE_BACKGROUNDCOLOR] = '#000' // 背景色
                style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_CLASSIC
                style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_CLASSIC
                // style[mxConstants.ARROW_SIZE] = 50;
                graph.getStylesheet().putCellStyle('nodeStyle', style)

                // 边样式 arrow
                style = graph.getStylesheet().getDefaultEdgeStyle()
                style[mxConstants.STYLE_EDGE] = mxEdgeStyle.TopToBottom
                // 默认开启双向箭头
                style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_BLOCK
                style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_BLOCK
                style[mxConstants.STYLE_DASHED] = true
                style[mxConstants.STYLE_ROUNDED] = true
                style[mxConstants.STYLE_ARCSIZE] = 60 // 设置圆角程度

                style = mxUtils.clone(style)
                style[mxConstants.STYLE_DASHED] = true
                graph.getStylesheet().putCellStyle('2wayArrow', style)

                style = mxUtils.clone(style)
                style[mxConstants.STYLE_STARTARROW] = mxConstants.ARROW_BLOCK
                style[mxConstants.STYLE_ENDARROW] = ''
                graph.getStylesheet().putCellStyle('startArrow', style)

                style = mxUtils.clone(style)
                style[mxConstants.STYLE_STARTARROW] = ''
                style[mxConstants.STYLE_ENDARROW] = mxConstants.ARROW_BLOCK
                graph.getStylesheet().putCellStyle('endArrow', style)

                // Sets the collapse and expand icons. The values below are the default
                // values, but this is how to replace them if you need to.
                mxGraph.prototype.collapsedImage = new mxImage(
                    '/static/img/topo/collapsed.gif',
                    12,
                    12
                )
                mxGraph.prototype.expandedImage = new mxImage(
                    '/static/img/topo/expanded.gif',
                    12,
                    12
                )

                // 贝塞尔曲线
                mxShape.prototype.addPoints = function (
                    c,
                    pts,
                    rounded,
                    arcSize,
                    close,
                    exclude,
                    initialMove
                ) {
                    if (pts != null && pts.length > 0) {
                        initialMove = initialMove != null ? initialMove : true
                        var pe = pts[pts.length - 1]

                        // Adds virtual waypoint in the center between start and end point
                        if (close && rounded) {
                            pts = pts.slice()
                            var p0 = pts[0]
                            var wp = new mxPoint(
                                pe.x + (p0.x - pe.x) / 2,
                                pe.y + (p0.y - pe.y) / 2
                            )
                            pts.splice(0, 0, wp)
                        }

                        var pt = pts[0]
                        // let i = 1

                        // Draws the line segments
                        if (initialMove) {
                            c.moveTo(pt.x, pt.y)
                        } else {
                            c.lineTo(pt.x, pt.y)
                        }
                        const midX = pt.x / 2 + pe.x / 2
                        // 调用内置函数绘制三次贝塞尔曲线
                        c.curveTo(midX, pt.y, midX, pe.y, pe.x, pe.y)
                        // 忽略后面绘制折线的代码
                        return
                        // ...
                    }
                }

                graph.setHtmlLabels(true)

                // 插入dom节点导致edge边上的文案消失
                mxGraph.prototype.convertValueToString = function (cell, callback) {
                    if (cell && cell.edge !== true) {
                        let styleArr = cell.style.split('=')
                        let iconUrl = styleArr[styleArr.length - 1]
                        // 图标
                        let div = document.createElement('div')
                        div.style.width = '60px'
                        div.style.height = '60px'
                        div.style.position = 'relative'
                        div.style.left = '-60px'
                        div.style.borderRight = '60px solid transparent'
                        div.style.background = `url(${iconUrl}) no-repeat`
                        div.style.backgroundSize = '60px 60px'
                        div.style.filter = 'drop-shadow(60px 0 0 #003ee4)'

                        callback && callback(cell)

                        cell.div = div

                        // 图标包裹
                        let iconBox = document.createElement('div')
                        iconBox.style.width = '60px'
                        iconBox.style.height = '60px'
                        iconBox.style.margin = '0 auto'
                        iconBox.style.overflow = 'hidden'

                        // 节点包裹
                        let wrap = document.createElement('div')
                        wrap.style.align = 'center'

                        // 文案
                        let text = document.createElement('div')
                        text.style.width = '120px'
                        text.style.overflow = 'hidden'
                        text.style.whiteSpace = 'nowrap'
                        text.style.textOverflow = 'ellipsis'
                        text.innerText = cell.value

                        iconBox.appendChild(div)
                        wrap.appendChild(iconBox)
                        wrap.appendChild(text)
                        return wrap
                    }
                    if (cell && cell.edge === true) {
                        let edgeText = document.createElement('span')
                        edgeText.innerText = cell.value
                        return edgeText
                        // return mxGraph.prototype.convertValueToString.apply(this, arguments);
                    }
                }

                // Avoids overlap of edges and collapse icons
                // graph.keepEdgesInBackground = true;

                // Stops editing on enter or escape keypress
                // var keyHandler = new mxKeyHandler(graph);

                // Enables automatic layout on the graph and installs
                // a tree layout for all groups who's children are
                // being changed, added or removed.
                var layout = new mxCompactTreeLayout(graph, false)
                layout.useBoundingBox = false
                layout.edgeRouting = false
                layout.levelDistance = 50
                layout.nodeDistance = 30

                var layoutMgr = new mxLayoutManager(graph)

                layoutMgr.getLayout = function (cell) {
                    if (cell.getChildCount() > 0) {
                        return layout
                    }
                }

                // Disallow any selections
                graph.setCellsSelectable(false)
                graph.panningHandler.useLeftButtonForPanning = true // 右键平移
                graph.panningHandler.ignoreCell = true
                graph.setPanning(true) // 设置左键平移

                // 平移开始后，会重新渲染cell导致告警信息丢失，因此在结束后重新设置告警节点
                // 如有直接更新节点方法，可替换该方案
                graph.panningHandler.addListener(
                    mxEvent.PAN_END,
                    mxUtils.bind(this, function () {
                        this.getAlertNode()
                    })
                )

                // 判断鼠标滚轮滚动方向
                if (window.addEventListener)
                    // FF,火狐浏览器会识别该方法
                    window.addEventListener('DOMMouseScroll', wheel, false)
                window.onmousewheel = document.onmousewheel = wheel // W3C
                // 统一处理滚轮滚动事件
                function wheel(event) {
                    var delta = 0
                    if (!event) event = window.event
                    if (event.wheelDelta) {
                        // IE、chrome浏览器使用的是wheelDelta，并且值为“正负120”
                        delta = event.wheelDelta / 120
                        if (window.opera) delta = -delta // 因为IE、chrome等向下滚动是负值，FF是正值，为了处理一致性，在此取反处理
                    } else if (event.detail) {
                        // FF浏览器使用的是detail,其值为“正负3”
                        delta = -event.detail / 3
                    }
                    if (delta) handle(delta)
                }
                // 上下滚动时的具体处理函数
                function handle(delta) {
                    if (delta < 0) {
                        // 向下滚动
                        graph.zoomOut()
                        self.getAlertNode()
                    } else {
                        // 向上滚动
                        graph.zoomIn()
                        self.getAlertNode()
                    }
                }

                // 缩放按钮
                // graph.centerZoom = true;
                // let toolBar = document.getElementById('toolBar')
                // toolBar.innerHTML = ''
                // toolBar.appendChild(
                //   mxUtils.button("放大", function() {
                //     graph.zoomIn();
                //   })
                // );
                // toolBar.appendChild(
                //   mxUtils.button("缩小", function() {
                //     graph.zoomOut();
                //   })
                // );
                // Allows new connections but no dangling edges
                // graph.setConnectable(true);
                graph.setAllowDanglingEdges(false)

                // Defines the condition for showing the folding icon
                graph.isCellFoldable = function (cell) {
                    return this.model.getOutgoingEdges(cell).length > 0
                }

                // Defines the position of the folding icon
                graph.cellRenderer.getControlBounds = function (state) {
                    if (state.control != null) {
                        var oldScale = state.control.scale
                        var w = state.control.bounds.width / oldScale
                        var h = state.control.bounds.height / oldScale
                        var s = state.view.scale

                        return new mxRectangle(
                            // 折叠按钮x坐标
                            state.x + state.width / 2 - (w / 2) * s,
                            // 折叠按钮y坐标
                            state.y + state.height + TreeNodeShape.prototype.segment * s - (h / 2) * s,
                            w * s,
                            h * s
                        )
                    }

                    return null
                }

                // Implements the click on a folding icon
                graph.foldCells = function (collapse, recurse, cells) {
                    // console.log(collapse, recurse, cells)
                    this.model.beginUpdate()
                    try {
                        toggleSubtree(this, cells[0], !collapse)
                        this.model.setCollapsed(cells[0], collapse)

                        // Executes the layout for the new graph since
                        // changes to visiblity and collapsed state do
                        // not trigger a layout in the current manager.
                        layout.execute(graph.getDefaultParent())
                    } finally {
                        this.model.endUpdate()
                    }
                }

                graph.addMouseListener({
                    mouseDown: function () {
                        // console.log("mouseDown");
                    },
                    mouseMove: mxUtils.bind(this, function (sender, evt) {
                        let cell = evt.getCell()
                        if (cell && cell.edge !== true) {
                            // 已经请求到该节点详情，返回，避免重复请求
                            if (self.hoverCellDetails && self.currentCellId === cell.id) {
                                return
                            }
                            // 控制移动触发频率
                            if (self.mouseTimer) {
                                clearTimeout(self.mouseTimer)
                            }
                            self.mouseTimer = setTimeout(() => {
                                self.currentCellId = cell.id
                                let params = {
                                    ip: self.currentCellInfo.ip || self.currentCellInfo.zIp, // 应该用本端 aIp ？
                                    idcType:
                                        self.currentCellInfo.idcType || self.currentCellInfo.zIdcType // 应该用本端 aIdcType ？
                                }
                                self.queryDeviceDetail(params, evt, cell, addOnlineObjTooltip)
                                // addOnlineObjTooltip(evt, cell);
                            }, 100)
                        } else {
                            // 非节点元素，重置
                            clearTimeout(self.mouseTimer)
                            self.tipShow = false
                            self.hoverCellDetails = null
                        }
                    }),
                })

                // 设置节点提示信息
                var addOnlineObjTooltip = function (evt, cell) {
                    var value = []
                    let curInfo = self.currentCellInfo
                    let cellDetails = self.hoverCellDetails
                    let nullValue = '未分配'
                    // 需要根据ip和资源池idcType 获取设备详情 hoverCellDetails
                    value.push(
                        '设备名称：' + (curInfo.deviceName || cellDetails.deviceName || cellDetails.ip || nullValue)
                    )
                    value.push('资源池：' + (curInfo.idcType || cellDetails.idcType || nullValue))
                    value.push('POD：' + (curInfo.podName || cellDetails.podName || nullValue))
                    value.push(
                        '设备类型：' + (curInfo.deviceType || cellDetails.deviceType || nullValue)
                    )
                    value.push(
                        '一级部门：' + (curInfo.department1 || cellDetails.department1 || nullValue)
                    )
                    value.push(
                        '二级部门：' + (curInfo.department2 || cellDetails.department2 || nullValue)
                    )
                    value.push('业务系统：' + (curInfo.bizSystem || cellDetails.bizSystem || nullValue))
                    if (curInfo.aPortId) {
                        value.push('端口名称：' + curInfo.zPortId || nullValue)
                        value.push('对端设备：' + curInfo.aDeviceName || nullValue)
                        value.push('对端端口：' + curInfo.aPortId || nullValue)
                    }
                    /**
         *  本端设备名称: zDeviceName
            本端端口名称: zPortId
            对端设备名称:  接口中还没有提供 aDeviceName
            对端端口名称: aPortId
         */

                    self.tipsText = value.join().replace(/,/g, '<br>')
                    self.$refs.toolTipBox.style.top =
                        evt.graphY + cell.geometry.height / 2 + 'px'
                    self.$refs.toolTipBox.style.left =
                        evt.graphX + cell.geometry.width / 2 + 'px'
                    self.tipShow = true
                }

                // graph.setEnabled(false) // 编辑与运行状态
                // graph.setBackgroundImage();
                // graph.view.validateBackgroundImage();

                // 捕获任务节点的鼠标点击事件
                graph.addListener(mxEvent.CLICK, function (sender, evt) {
                    var cell = evt.getProperty('cell')
                    if (cell && cell.edge !== true) {
                        self.clickCount++
                        // 增加300毫秒间隔，避免双击无效
                        if (self.clickTimer) {
                            clearTimeout(self.clickTimer)
                        }
                        self.clickTimer = setTimeout(() => {
                            self.currentCellId = cell.id
                            if (self.clickCount === 1) {
                                // 根据本端设备参数，查询关系节点
                                let params = {
                                    // 关系列表里的 本端设备id：zInstanceId 或者第一台设备的id
                                    aInstanceId: self.currentCellInfo.zInstanceId || self.currentCellInfo.id,
                                    // aPortId: self.currentCellInfo.zPortId, // 本端端口： zPortId，如果不传该参数，将查询出很多重复的关系设备
                                    // zInstanceIp: self.currentCellInfo.zIp, // 对端ip： zIp
                                    // zPortId: self.currentCellInfo.aPortId // 对端端口： aPortId
                                }
                                self.queryInstancePortRelation(
                                    params,
                                    'getChild',
                                    graph,
                                    parent,
                                    cell
                                )
                            } else if (self.clickCount > 1) {
                                self.toDetail()
                            }
                            self.clickCount = 0
                        }, 500)
                    }
                })

                // 鼠标双击事件
                graph.dblClick = function (evt, params) {
                    if (self.clickTimer) {
                        clearTimeout(self.clickTimer)
                    }
                    if (params && params.edge !== true) {
                        console.log('双击事件==11', evt, params)
                        self.toDetail(params)
                    }
                }

                // tree
                /*
        Defines a custom shape for the tree node that includes the
        upper half of the outgoing edge(s).
      */
                function TreeNodeShape() { }

                TreeNodeShape.prototype = new mxCylinder()
                TreeNodeShape.prototype.constructor = TreeNodeShape

                // Defines the length of the upper edge segment.
                TreeNodeShape.prototype.segment = 20

                // Needs access to the cell state for rendering
                TreeNodeShape.prototype.apply = function (state) {
                    mxCylinder.prototype.apply.apply(this, arguments)
                    this.state = state
                }

                TreeNodeShape.prototype.redrawPath = function (
                    path,
                    x,
                    y,
                    w,
                    h,
                    isForeground
                ) {
                    var graph = this.state.view.graph
                    var hasChildren =
                        graph.model.getOutgoingEdges(this.state.cell).length > 0

                    if (isForeground) {
                        if (hasChildren) {
                            // Painting outside of vertex bounds is used here
                            path.moveTo(w / 2, h + this.segment)
                            path.lineTo(w / 2, h)
                            path.end()
                        }
                    } else {
                        path.moveTo(0, 0)
                        path.lineTo(w, 0)
                        path.lineTo(w, h)
                        path.lineTo(0, h)
                        path.close()
                    }
                }

                mxCellRenderer.registerShape('treenode', TreeNodeShape)

                // Defines a custom perimeter for the nodes in the tree
                mxGraphView.prototype.updateFloatingTerminalPoint = function (
                    edge,
                    start,
                    end,
                    source
                ) {
                    var pt = null

                    if (source) {
                        pt = new mxPoint(
                            start.x + start.width / 2,
                            start.y + start.height + TreeNodeShape.prototype.segment
                        )
                    } else {
                        pt = new mxPoint(start.x + start.width / 2, start.y)
                    }

                    edge.setAbsoluteTerminalPoint(pt, source)
                }

                // Updates the visible state of a given subtree taking into
                // account the collapsed state of the traversed branches
                function toggleSubtree(graph, cell, show) {
                    show = show != null ? show : true
                    var cells = []

                    graph.traverse(cell, true, function (vertex) {
                        if (vertex != cell) {
                            cells.push(vertex)
                        }

                        // Stops recursion if a collapsed cell is seen
                        return vertex == cell || !graph.isCellCollapsed(vertex)
                    })

                    graph.toggleCells(show, cells, true)
                }

                // 初始化渲染
                graph.getModel().beginUpdate()
                try {
                    const node01 = graph.insertVertex(
                        parent,
                        self.deviceInfo.id,
                        self.deviceInfo.deviceName || self.deviceInfo.ip || '没有名称',
                        120,
                        20,
                        60,
                        60
                        // "nodeStyle"
                    )

                    let iconUrl = self.deviceInfo.instanceIconUrl ? ('nodeStyle;type=icon;image=' + self.deviceInfo.instanceIconUrl) : 'nodeStyle;type=icon;image=/cmdb/static/images/IPSubnet-v1.png'
                    self.setStyle(
                        graph,
                        node01,
                        iconUrl
                    )

                    self.deviceRelationList.forEach((item, index) => {
                        const child = graph.insertVertex(
                            parent,
                            item.id, // 使用关系id(唯一)绘图
                            item.zDeviceName || item.zIp,
                            120 + 100 * index,
                            200,
                            60,
                            60,
                            'nodeStyle'
                        )
                        const e = graph.insertEdge(
                            parent,
                            null,
                            item.connectType,
                            node01,
                            child
                        )

                        // graph.setCellWarning(child, 'Tooltip');
                        // 根据connectType设置箭头方向
                        self.resetEdge(item.connectType, e)

                        self.setStyle(
                            graph,
                            child,
                            'nodeStyle;type=icon;image=' + item.zIconUrl
                        ) // 设置子节点icon
                        self.setStyle(
                            graph,
                            node01,
                            'nodeStyle;type=icon;image=' + item.aIconUrl
                        ) // 设置第一台设备icon
                    })

                    // 以动画的样式改变图形  卡顿，不开启
                    // graph.getModel().addListener(mxEvent.CHANGE, function(sender, evt)
                    // {
                    //   var changes = evt.getProperty('edit').changes;
                    //   mxEffects.animateChanges(graph, changes);
                    // });

                    // 没有效果，先不开启
                    // graph.fit(); //自适应
                    // graph.center(true, true, 0.5, 0.5); //将画布放到容器中间
                    // var sc = graph.getView().getScale(); //获取当前的缩放比例
                    // graph.zoomTo(Math.round(sc / 2)); //在缩放一半，否则是满屏状态，不好看
                } finally {
                    graph.getModel().endUpdate()
                }

                // init end
            },
            // 插入节点
            insertChildNode(graph, parent, cell) {
                this.tipShow = false
                if (!this.geometryY) {
                    this.geometryY = cell.geometry.y + 200
                } else {
                    this.geometryY += 200
                }
                graph.getModel().beginUpdate()
                try {
                    this.childNodes.forEach((item, index) => {
                        const child = graph.insertVertex(
                            parent,
                            item.id, // 使用关系id(唯一)绘图
                            item.zDeviceName || item.zIp,
                            cell.geometry.x + 100 * index,
                            this.geometryY,
                            60,
                            60
                            // "nodeStyle"
                        )
                        const e = graph.insertEdge(
                            parent,
                            null,
                            item.connectType,
                            cell,
                            child
                        )

                        // 根据connectType设置箭头方向
                        this.resetEdge(item.connectType, e)

                        // graph.getModel().setCollapsed(cell, 0);
                        this.setStyle(
                            graph,
                            child,
                            'nodeStyle;type=icon;image=' + item.zIconUrl
                        )
                    })
                } finally {
                    graph.getModel().endUpdate()
                }
            },
            setStyle(graph, node, imgUrl) {
                graph.getModel().setStyle(node, imgUrl)
            },
            // 跳转到设备详情
            toDetail() {
                let params = {
                    instanceId: this.currentCellInfo.zInstanceId || this.currentCellInfo.id,
                    idcType: this.currentCellInfo.zIdcType || this.currentCellInfo.idcType,
                    ip: this.currentCellInfo.zIp || this.currentCellInfo.ip,
                    state: 'detail',
                    timeStamp: new Date().getTime() // 增加时间戳，更新链接，否则参数相同不跳转
                }
                this.$router.push({
                    path: '/resource/iframe/detail',
                    query: {
                        queryParams: JSON.stringify(params)
                    }
                })
            },
            resetEdge(connectType, e) {
                if (connectType === '下联') {
                    e.style = 'startArrow'
                } else if (connectType === '上联') {
                    e.style = 'endArrow'
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .hp100 {
        height: 100%;
    }
    #graphContainer {
        width: 100%;
        height: calc(100vh - 213px);
        background: url("/static/img/topo/grid.gif");
        svg text {
            font-size: 12px;
            margin-top: 20px;
        }
    }
    .tooltip-box {
        position: absolute;
        z-index: 999;
        min-width: 300px;
        border: 1px solid $color-border;
        background: #fff;
        padding: 15px;
        border-radius: 4px;
        text-align: left;
    }
</style>
