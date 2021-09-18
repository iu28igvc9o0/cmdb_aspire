<template>
    <!-- 资源检索 -->
    <el-dialog class="yw-dialog"
               :width="dialogWidth"
               title="选择监控项"
               @close="cancel"
               :visible.sync="theServer.dialogVisible">
        <div class="clearfix heightp100-box">
            <div class="theServerContent">
                <span style="color:red">*</span>
                <el-select v-model="queryFrom.deviceType"
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in options"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value"
                               @click.native="theServerClick(item.label)">
                    </el-option>
                </el-select>
            </div>
            <div class="theServerContent tcback">
                <div class="tcLeft">
                    <div class="titleName">资源设备</div>
                    <div class="tableTranContent mtop10">
                        <div class="boxShadow tableTran">
                            <div class="litteName">
                                <span>请选择设备资源</span>
                            </div>

                            <div class="tableTranList selectWidth">
                                <el-select clearable
                                           filterable
                                           v-model="queryFrom.idcType"
                                           style="margin-right:5px;"
                                           @clear="idcTypeClear"
                                           placeholder="资源池">
                                    <el-option v-for="item in resourcePoolList"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value"
                                               @click.native="idcTypeChange(item.value)">
                                    </el-option>
                                </el-select>
                                <!-- :multiple="true" -->
                                <YwSelectTree @changeTree="changeTree"></YwSelectTree>
                                <!-- <Treeselect class="yw-treeselect"
                                            style="font-color: #606266"
                                            v-model="queryFrom.bizSystem"
                                            :options="bizDepTreeSelOpts"
                                            :limit="1"
                                            placeholder="业务系统" /> -->

                                <!-- suffix-icon="el-icon-search" @change="IPchange(queryFrom.ip)" -->
                                <el-input v-model="queryFrom.ip"
                                          @change="IPchange(queryFrom.ip)"
                                          clearable
                                          filterable
                                          @keyup.enter.native="queryIP"
                                          placeholder="设备IP查询">
                                    <i class="el-icon-search"
                                       style="margin-top:6px;"
                                       slot="suffix"
                                       @click="IPclick(queryFrom.ip)">
                                    </i>
                                </el-input>

                                <div class="yw-el-table-wrap">
                                    <el-table class="yw-el-table tableAuto"
                                              stripe
                                              :data="tableData"
                                              @selection-change="handleSelectionChange"
                                              :header-cell-style="{background:'rgba(232,240,252,1)',color:'#3A4154',height:'30px'}"
                                              style="width: 100%;margin-top:10px;height:230px;border:1px solid rgba(222, 233, 252, 1)">
                                        <el-table-column type="selection"
                                                         width="45">
                                        </el-table-column>
                                        <el-table-column label="资源池"
                                                         width="100"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.idcType }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="设备分类"
                                                         width="80"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.device_class }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="设备类型"
                                                         width="80"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.device_type }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="设备IP"
                                                         width="130"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.ip }}
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                    <YwPagination small
                                                  @handleSizeChange="handleSizeChange"
                                                  @handleCurrentChange="handleCurrentChange"
                                                  :current-page="queryFrom.pageNo"
                                                  :page-sizes="pageSizes"
                                                  :page-size="queryFrom.pageSize"
                                                  :pager-count="pagercount"
                                                  layout="total, prev, pager, next, jumper"
                                                  :total="total"></YwPagination>
                                </div>
                            </div>
                        </div>
                        <div style="width:60px;">
                            <section class="search-arrow fl"
                                     @click="addItems()">
                                <i class="el-icon-right"></i>
                            </section>

                        </div>
                        <div class="boxShadow tableTran">
                            <div class="litteName">
                                <span>已选</span><span style="font-size:12px;color:#278BDC">（最多选择20条）</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <span v-if="emptyShow"
                                      @click="empty()"
                                      style="cursor: pointer;color:#278BDC;font-size:14px">清空选项</span>
                            </div>
                            <div class="tableTranList">
                                <el-input v-model="inputIP"
                                          @change="inputIPChange(inputIP)"
                                          clearable
                                          placeholder="设备IP查询">

                                    <i class="el-icon-search"
                                       style="margin-top:6px;"
                                       slot="suffix"
                                       @click="inputIPclick(inputIP)"></i>
                                </el-input>
                                <div class="yw-el-table-wrap">
                                    <el-table class="yw-el-table tableAuto"
                                              stripe
                                              :data="tableDataA"
                                              :header-cell-style="{background:'rgba(232,240,252,1)',color:'#3A4154',height:'30px'}"
                                              style="width: 100%;margin-top:10px;height:230px;border:1px solid rgba(222, 233, 252, 1);">
                                        <el-table-column label="资源池"
                                                         width="100"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.idcType }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="设备分类"
                                                         width="80"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.deviceClass }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="设备类型"
                                                         width="80"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.deviceType }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="设备IP"
                                                         show-overflow-tooltip
                                                         align="left">
                                            <template slot-scope="scope">
                                                {{ scope.row.ip }}
                                            </template>
                                        </el-table-column>
                                        <el-table-column label="操作"
                                                         align="left"
                                                         width="50">
                                            <template slot-scope="scope">
                                                <a class="yw-table-link"
                                                   @click="del(scope.$index)"><i class="el-icon-delete"></i></a>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="tcRight">
                    <div class="titleName">监控项</div>
                    <div class="boxShadow"
                         style="padding:15px">
                        <!-- check-change -->
                        <el-tree :data="datatree"
                                 :show-checkbox='showCheckbox'
                                 node-key="id"
                                 highlight-current
                                 @check="getNodeTree"
                                 @node-click="nodeClick"
                                 :props="defaultProps">
                        </el-tree>
                    </div>
                </div>
            </div>
            <div class="mtop10">
                <span style="color:#333;font-family:Microsoft YaHei;font-weight:400;font-size:14px">统计方式</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>
                    <el-radio v-model="radio"
                              label="1">单台设备</el-radio>
                    <el-radio v-model="radio"
                              label="2">多台设备</el-radio>
                </span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span>
                    <el-select clearable
                               filterable
                               v-model="count_type"
                               placeholder="请选择">
                        <el-option v-for="item in statisticalMethodList"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value"
                                   @click.native="countTypelick(item.value,item.label)">
                        </el-option>
                    </el-select>
                </span>
            </div>
            <div class="fixed-bottom-box t-center">
                <el-button @click="cancel">取消</el-button>
                <el-button type="primary"
                           @click="determine">确定</el-button>
            </div>
        </div>
    </el-dialog>

</template>
<script>
    import rbAlertRepPanelServicesFactory from 'src/services/alert/rb-alertRepPanel-services.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        props: ['theServer'],
        components: {
            YwSelectTree: () => import('src/components/common/yw-selectTree-bizSystem.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },

        data() {
            return {
                showCheckbox: true,
                dialogWidth: '90%',
                emptyShow: false, // 清空按钮
                queryFrom: {
                    deviceType: 'X86服务器',
                    idcType: '',
                    bizSystem: null,
                    ip: '',
                    pageSize: 20, // 每页显示个数
                    pageNo: 1, // 当前页码
                    deviceClass: '服务器',
                },
                tableData: [],
                dataTree: [],
                tableDataA: [],
                tableDataAA: [],
                total: 0,
                pageSizes: [20, 30, 50, 100],
                pagercount: 3,
                inputIP: '',
                options: [{
                    value: '1',
                    label: 'X86服务器'
                }, {
                    value: '2',
                    label: '云主机'
                }],
                statisticalMethodList: [{
                    value: 'max',
                    label: '最大'
                }, {
                    value: 'min',
                    label: '最小'
                }, {
                    value: 'avg',
                    label: '平均'
                }, {
                    value: 'sum',
                    label: '求和'
                }],
                count_type: '', // 统计方式
                count_typeA: '', // 统计方式
                unit: '',// 单位
                unitList: [],
                unitList1: [],
                resourcePoolList: [], // 资源池下拉框数据
                bizDepTreeSelOpts: [], // 业务系统树结构
                datatree: [], // 监控项树 数据
                ipList: [],
                ipListArr: [],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                radio: '1',
                newArr: [],
                valueType: ''

            }
        },
        watch: {
            radio: {
                // 1 单台设备 2 多台设备
                handler(val) {
                    this.radio = val
                    if (this.radio == 1) {
                        this.showCheckbox = true
                    } else if (this.radio == 2) {
                        this.showCheckbox = false
                    }
                }
            },

        },
        mounted() {
            this.theServerClick()
            this.init()
            this.queryList()
        },
        methods: {
            changeTree(val = '') {
                this.queryFrom.bizSystem = val
                this.queryList()
            },
            nodeClick(e) {
                if (!e.children) {
                    this.dataTree.push(e)
                } else {
                    this.$message.warning('请选择子集')
                    return
                }
            },
            getNodeTree(data, checked) {
                // this.dataTree = checked.checkedNodes
                console.log('opopopooooooooo', this.ipList.length, data.checkedNodes, checked)

                this.dataTree = checked.checkedNodes.filter((item) => {
                    return !item.children
                })
            },
            //  服务器及联监控项事件
            theServerClick(data) {
                if (data) {
                    this.queryFrom.deviceType = data
                    this.queryList()
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/alerts/monitor/getSubMonitorList?deviceClass=' + '服务器' + '&deviceType=' + this.queryFrom.deviceType
                }).then((res) => {
                    this.datatree = Object.keys(res)
                        .map(val => {
                            const items = res[val]
                                .map(item => ({ value: item.moniter_item_key, label: item.moniter_item_name }))
                                .filter(item => item.value)
                            return { value: val, label: val, children: items }
                        })
                        .filter(val => val.children.length)
                })
            },
            init() {
                this.rbHttp.sendRequest({ // 资源池
                    method: 'GET',
                    url: '/v1/cmdb/configDict/getDictsByType',
                    params: { type: 'idcType' },
                }).then((res) => {
                    this.resourcePoolList = res
                    return res
                })
            },
            queryList() {
                this.queryFrom.pageNo = 1

                this.queryFrom['currentPage'] = this.currentPage
                this.queryFrom['token'] = '5245ed1b-6345-11e'

                rbCmdbServiceFactory.getCmdbPageList(this.queryFrom).then((res) => {
                    this.tableData = res.result
                    this.total = res.count
                })
            },
            idcTypeChange(data) {
                this.queryFrom.idcType = data
                this.queryList()
            },
            idcTypeClear() {
                this.queryFrom.idcType = ''
                this.queryList()
            },
            queryIP() { // 回车事件
            },
            IPchange(ip) {
                this.queryFrom.ip = ip
                if (ip == '') {
                    this.queryList()
                }
            },
            IPclick(ip) {
                this.queryFrom.ip = ip
                this.queryList()
            },
            inputIPclick(ip) {
                if (ip) {
                    this.tableDataA.forEach(item => {
                        if (item.ip.indexOf(ip) != -1) {
                            let objList = this.tableDataA.filter((item) => {
                                return item.ip.indexOf(ip) != -1
                            })
                            this.tableDataA = objList
                        }
                    })
                } else {
                    this.tableDataA = this.tableDataAA
                }
            },
            inputIPChange(ip) {
                if (ip == '') {
                    this.tableDataA = this.tableDataAA
                }
            },
            // 统计方式
            countTypelick(data) {
                this.count_typeA = data

            },
            // table 多选框事件
            handleSelectionChange(val) {
                this.tableDataAA = val
            },
            // 穿梭框 列表数据事件
            addItems() {
                this.inputIP = ''
                if (this.tableDataA.length == 20) {
                    this.$message.warning('超过最大条数20条，请删除再添加')
                } else if ((this.tableDataAA.length + this.tableDataA.length) > 20) {
                    this.$message.warning('需要添加几条，就删除几条')
                } else {
                    this.tableDataA = this.$utils.reduceArrObj(this.tableDataA.concat(this.tableDataAA))
                }

                if (this.tableDataA.length > 0) {
                    let Arrey = []
                    for (let item in this.tableDataA) {
                        Arrey.push(
                            this.tableDataA[item].ip,
                        )
                        this.ipList = Arrey.toString()
                    }
                    this.ipListArr = this.tableDataA.map(item => { return { 'ip': item.ip } })

                    this.emptyShow = true
                }
            },
            // 列表删除
            del(index) {
                this.tableDataA.splice(index, 1)
                if (this.tableDataA.length > 0) {
                    let Arrey = []
                    for (let item in this.tableDataA) {
                        Arrey.push(
                            this.tableDataA[item].ip,
                        )
                        this.ipList = Arrey.toString()
                    }
                    this.ipListArr = this.tableDataA.map(item => { return { 'ip': item.ip } })
                }
                if (this.tableDataA.length < 1) {
                    this.emptyShow = false
                }
            },
            // 清空选项
            empty() {
                this.tableDataA = []
                this.emptyShow = false
            },
            // 确定按钮：服务器渠道-确认选中的资源设备和监控项
            determine() {
                let _this = this
                if (_this.radio == 2) { // 多台设备
                    if (_this.tableDataA.length > 0 && _this.dataTree.length > 0 && _this.count_type) {

                        let dataA = {
                            ip: _this.ipList.split(',').filter(item => { return item !== '' }),
                            item: _this.dataTree[0].value,
                            rs: '',
                            reType: 6,
                            multiQuery: false
                        }
                        rbAlertRepPanelServicesFactory.getMonitorItemList(dataA).then((res) => {
                            if (res.units) {
                                _this.unit = res.units
                                _this.valueType = res.valueType
                                let arr = _this.dataTree.map(item => {
                                    return Object.assign(item, { mointerIndex: '' }, { resourceType: '' }, { subItemIds: '' }, { noSubItemIds: '1' }, { unit: res.units }, { valueType: res.valueType }, { count_type: _this.count_type }, { ip: _this.ipList }, { view_name: _this.count_typeA }, { device_type: _this.queryFrom.deviceType }, { device_class: '服务器' })
                                })
                                _this.$emit('dataTreeData', arr)
                                _this.theServer.dialogVisible = false
                            } else {
                                let arr = _this.dataTree.map(item => {
                                    return Object.assign(item, { mointerIndex: '' }, { resourceType: '' }, { subItemIds: '' }, { noSubItemIds: '1' }, { unit: '' }, { valueType: '' }, { count_type: _this.count_type }, { ip: _this.ipList }, { view_name: _this.count_typeA }, { device_type: _this.queryFrom.deviceType }, { device_class: '服务器' })
                                })
                                _this.$emit('dataTreeData', arr)
                                _this.theServer.dialogVisible = false
                            }
                        })
                        // _this.dataTree = []
                    } else if (_this.tableDataA.length == 0) {
                        _this.$message.warning('请选择数据')
                        return
                        // !_this.dataTree.hasOwnProperty('value')
                    } else if (_this.dataTree.length == 0 && _this.radio == 2) {
                        _this.$message.warning('请选择监控项子集')
                        return
                    } else if (_this.count_type == '') {
                        _this.$message.warning('统计方式必选')
                        return
                    }
                } else if (_this.radio == 1) { // 单台设备
                    if (_this.tableDataA.length > 0 && _this.dataTree.length > 0 && _this.count_type) {
                        let arr1 = _this.ipListArr
                        let arr2 = _this.dataTree
                        _this.newArr = []
                        arr1.forEach(ele => {
                            arr2.forEach(item => {
                                _this.newArr.push({
                                    ip: ele.ip,
                                    label: item.label,
                                    value: item.value
                                })
                            })
                        })
                        _this.newArr.forEach(itemZ => {
                            if (_this.ipList.split(',').filter(item => { return item !== '' }).length > 1) {
                                let dataA = {
                                    ip: _this.ipList.split(',').filter(item => { return item !== '' }),
                                    item: itemZ.value,
                                    rs: '',
                                    reType: 6,
                                    multiQuery: false
                                }
                                rbAlertRepPanelServicesFactory.getMonitorItemList(dataA).then((res) => {
                                    _this.unit = res.units
                                    _this.unitList.push({ unit: res.units, valueType: res.valueType })
                                    if (res.units) {
                                        _this.unit = res.units
                                        _this.newArr.map((item) => {
                                            return Object.assign(item, { mointerIndex: '' }, { resourceType: '' }, { subItemIds: '' }, { noSubItemIds: '1' }, { count_type: _this.count_type }, { view_name: _this.count_typeA }, { device_type: _this.queryFrom.deviceType }, { device_class: '服务器' })
                                        })
                                        let obj = _this.newArr.map((item, index) => {
                                            return { ...item, ..._this.unitList[index] }
                                        })
                                        _this.$emit('dataTreeData', obj)
                                        _this.theServer.dialogVisible = false

                                    } else {
                                        _this.newArr.map((item) => {
                                            return Object.assign(item, { mointerIndex: '' }, { resourceType: '' }, { subItemIds: '' }, { noSubItemIds: '1' }, { count_type: _this.count_type }, { view_name: _this.count_typeA }, { unit: '' }, { device_type: _this.queryFrom.deviceType }, { device_class: '服务器' })
                                        })
                                        let obj1 = _this.newArr.map((item, index) => {
                                            return { ...item, ..._this.unitList[index] }
                                        })
                                        _this.$emit('dataTreeData', obj1)
                                        _this.theServer.dialogVisible = false
                                    }
                                })
                            } else {
                                let dataA = {
                                    ip: _this.ipList.split(',').filter(item => { return item !== '' }),
                                    item: itemZ.value,
                                    rs: _this.ipList.length > 1 ? '' : _this.ipList.replace(/,/g, ''),
                                    reType: 6,
                                    multiQuery: false
                                }
                                rbAlertRepPanelServicesFactory.getMonitorItemList(dataA).then((res) => {
                                    _this.unitList.push({ unit: res.units ? res.units : '', key: res.key, valueType: res.valueType })
                                    setTimeout(() => {
                                        _this.newArr.map((item) => { return Object.assign(item, { mointerIndex: '' }, { resourceType: '' }, { subItemIds: '' }, { noSubItemIds: '1' }, { count_type: _this.count_type }, { view_name: _this.count_typeA }, { device_type: _this.queryFrom.deviceType }, { device_class: '服务器' }) })
                                        _this.newArr.forEach((itemD) => {
                                            _this.unitList.forEach(itemv => {
                                                if (itemD.value == itemv.key) {
                                                    itemD.unit = itemv.unit
                                                    itemD.valueType = itemv.valueType
                                                }
                                            })
                                        })
                                        // let obj = _this.newArr.map((item, index) => {
                                        //     return { ...item, ..._this.unitList[index] }
                                        // })
                                        _this.$emit('dataTreeData', _this.newArr)
                                        _this.theServer.dialogVisible = false
                                    }, 500)
                                })

                            }
                        })

                        _this.dataTree = []
                    } else if (_this.tableDataA.length == 0) {
                        _this.$message.warning('请选择数据')
                        return
                    } else if (_this.dataTree.length == 0) {
                        _this.$message.warning('请选择监控项')
                        return
                    } else if (_this.count_type == '') {
                        _this.$message.warning('统计方式必选')
                        return
                    }

                }



            },
            // 取消按钮
            cancel() {
                this.theServer.dialogVisible = false
            },
            // 分页
            handleSizeChange() {
            },
            handleCurrentChange(val) {

                setTimeout(() => {
                    this.queryFrom.pageNo = val
                    rbAlertRepPanelServicesFactory.getIP(this.queryFrom).then((res) => { // 列表数据
                        this.tableData = res.result
                        this.total = res.count
                    })
                }, 1000)
            }
        }
    }

</script>
<style lang="scss" scoped>
    @import "../theServer/theServerScss.scss";
</style>
