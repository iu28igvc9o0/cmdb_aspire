<template>
    <div class="components-container yw-dashboard">
        <el-form class="yw-form components-condition"
                 label-width="75px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="屏蔽规则">
                <el-select v-model="formData.isolateId"
                           filterable
                           clearable>
                    <el-option v-for="(item, index) in isolateList"
                               :key="index"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-for="(itemA,indexA) in tileDatalist"
                          :key="indexA"
                          :label="itemA.queryParamName">
                <el-select v-if="itemA.code === 'biz_sys' && itemA.type === 'tree_in'"
                           clearable
                           filterable
                           v-model="itemA.value"
                           :placeholder="itemA.name"
                           collapse-tags>
                    <el-option :value="mineStatusValue"
                               style="height: auto">
                        <el-tree :data="itemA.listData"
                                 node-key="id"
                                 ref="tree"
                                 highlight-current
                                 :props="defaultProps"
                                 @node-click="handleNodeClick"></el-tree>
                    </el-option>
                </el-select>
                <el-input v-else-if="itemA.type === 'like' || itemA.type === 'and'"
                          v-model="itemA.value"
                          :placeholder="itemA.name"
                          :clearable="true">
                </el-input>
                <el-select v-else-if="itemA.type === 'in'"
                           v-model="itemA.valueList"
                           placeholder="请选择"
                           clearable
                           filterable
                           multiple
                           @change="changePool()"
                           @clear="cleanPool()">
                    <el-option v-for="(item,index) in itemA.listData"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
                <el-select v-else-if="itemA.type === 'in_and'"
                           v-model="itemA.value"
                           placeholder="请选择"
                           clearable
                           filterable
                           @change="changePool()"
                           @clear="cleanPool()">
                    <el-option v-for="(item,index) in itemA.listData"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
                <el-date-picker v-else-if="itemA.type === 'date' || itemA.type === 'datetime'"
                                v-model="itemA.valueList"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                value-format="yyyy-MM-dd HH:mm:ss">
                </el-date-picker>
                <el-input v-else
                          v-model="itemA.value"
                          :placeholder="itemA.name"
                          :clearable="true">
                </el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <!-- tab -->
        <section class="tab-wrap">
            <el-tabs class="yw-tabs"
                     v-model="activeTab"
                     @tab-click="changeTab">
                <el-tab-pane :label="item.label"
                             tabindex="-1"
                             :name="item.name"
                             v-for="(item,index) in tabData"
                             :key="index">
                </el-tab-pane>
            </el-tabs>

        </section>
        <!-- tab -->

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="exportBatch()"
                           :disabled="exportDisabled">导出
                </el-button>
                <el-button class="btn-icons-wrap"
                           v-if="activeTab === 'current'"
                           type="text"
                           icon="el-icon-position"
                           @click="send">派单</el-button>
                <el-button class="btn-icons-wrap"
                           v-if="activeTab === 'current'"
                           type="text"
                           icon="el-icon-refresh-left"
                           @click="renew">恢复</el-button>
                <el-button class="btn-icons-wrap"
                           v-if="activeTab === 'current'"
                           type="text"
                           icon="el-icon-delete"
                           @click="clear">清除</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          v-loading="loading"
                          :data="result"
                          height="calc(100vh - 310px)"
                          stripe
                          @selection-change="changeSelection"
                          border>
                    <el-table-column type="selection"
                                     width="40"></el-table-column>
                    <el-table-column label="规则名称"
                                     width="160">
                        <template slot-scope="scope">
                            <a>{{scope.row.isolate_name}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column v-for="itemData in listShowList"
                                     :key="itemData.fieldCode"
                                     :label="itemData.listShowName"
                                     :width="((itemData.tableColumnWidth && itemData.tableColumnWidth > 0) ? itemData.tableColumnWidth : '150') + 'px'">
                        <template slot-scope="scope">
                            <label v-if="itemData.dataType === 'datetime'">
                                {{scope.row[itemData.fieldCode] | formatDate}}
                            </label>
                            <rb-mirror-alert-status v-else-if="itemData.fieldCode === 'alert_level'"
                                                    :mode="'list'"
                                                    :status="scope.row.alert_level">
                            </rb-mirror-alert-status>
                            <label v-else>
                                {{scope.row[itemData.fieldCode]}}
                            </label>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </el-form>

        <!-- dialog -->
        <DialogSend :dialogMsg="dialogSend"
                    v-if="dialogSend.dialogVisible"
                    @closeDialog="closeDialogSend"></DialogSend>
        <DialogRenew :dialogMsg="dialogRenew"
                     v-if="dialogRenew.dialogVisible"
                     @closeDialog="closeDialogRenew"></DialogRenew>
        <DialogClear :dialogMsg="dialogClear"
                     v-if="dialogClear.dialogVisible"
                     @closeDialog="closeDialogClear"></DialogClear>
        <!-- dialog -->
    </div>
</template>

<script>
    import rbHttp from 'src/assets/js/utility/rb-http.factory'
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertIsolateServiceFactory from 'src/services/alert/rb-alert-isolate-service.factory'

    export default {
        name: 'MirrorAlertShieldAlert',
        mixins: [QueryObject],
        components: {
            DialogSheild: () => import('../dialog-shield.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            rbMirrorAlertStatus: () => import('src/pages/mirror_alert/common/rb-mirror-alert-status.vue'),
            DialogSend: () => import('./dialog-send.vue'),
            DialogRenew: () => import('./dialog-renew.vue'),
            DialogClear: () => import('./dialog-clear.vue'),
        },
        data() {
            return {
                mineStatusValue: '',
                listShowList: [],
                modelFieldDataS: [],
                tileDatalist: [],
                // 表单数据
                formData: {
                    isolateId: '',// 屏蔽规则
                },
                // 查询条件
                queryParams: {
                },
                defaultProps: { // 业务系统树形下拉框
                    children: 'subList',
                    label: 'name'
                },
                // 查询结果
                result: [],
                isolateList: [],

                exportDisabled: false,
                // tab
                activeTab: 'current',
                tabData: [
                    { label: '当前告警', data: '', name: 'current' },
                    { label: '历史告警', data: '', name: 'history' },
                ],
                // dialog
                dialogSend: {
                    id: '',// 每个弹窗数据的唯一标识
                    dialogVisible: false,
                    data: {} // 数据
                },
                dialogRenew: {
                    id: '',// 每个弹窗数据的唯一标识
                    dialogVisible: false,
                    data: {} // 数据
                },
                dialogClear: {
                    id: '',
                    dialogVisible: false,
                    data: {}
                },
                // 表格行选中
                multipleSelection: []
            }
        },
        created() {
            if (this.$route.query.id) {
                this.formData.isolateId = parseInt(this.$route.query.id)
            }

            // 查询条件
            this.getConditions()
            // 屏蔽规则
            this.queryIsolates()
            // 表格数据
            this.query()
        },
        methods: {
            // 获得查询条件
            async getConditions() {
                rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v2/alerts/model/field/listByTableName/alert_isolate_alerts'
                }).then((res) => {
                    // 列表头部展示数据
                    let objList = res.filter((item) => {
                        return item.isListShow == 1
                    })
                    this.listShowList = objList
                    // 过滤不需要的数据
                    let obj = res.filter((item) => {
                        return item.isQueryParam == 1
                    })
                    this.modelFieldDataS = obj.sort(function (a, b) {
                        return a.queryParamSort - b.queryParamSort
                    })
                    this.modelFieldDataS.forEach(async (fieldCodeType) => {
                        let OBJ = {}
                        OBJ.name = fieldCodeType.fieldName
                        OBJ.queryParamName = fieldCodeType.queryParamName
                        OBJ.code = fieldCodeType.fieldCode
                        OBJ.type = fieldCodeType.queryParamType
                        OBJ.valueList = ''
                        OBJ.queryParamSort = fieldCodeType.queryParamSort
                        OBJ.value = ''
                        if (fieldCodeType.fieldCode === 'device_mfrs') {
                            // 获取设备品牌
                            let produceList = await this.getConditionByProduce()
                            if (produceList && produceList.data) {
                                OBJ.listData = produceList.data.map((item) => {
                                    return { 'name': item.produce, 'value': item.produce }
                                })
                            }
                            // 获取设备品牌
                            // rbHttp.sendRequest({
                            //     method: 'POST',
                            //     params: { 'type': '生产供应商', 'orderBy': 'produce' },
                            //     url: '/v1/cmdb/maintenProduce/listProduceByPage'
                            // }).then((res) => {
                            //     OBJ.listData = res.data.map((item) => {
                            //         return { 'name': item.produce, 'value': item.produce }
                            //     })
                            // })
                        } else if (fieldCodeType.queryParamSource) {
                            if (fieldCodeType.queryParamSource.startsWith('[')) {
                                OBJ.listData = JSON.parse(fieldCodeType.queryParamSource)
                            } else {
                                OBJ.listData = await this.getConditionByOther(fieldCodeType.queryParamSource)
                                // rbHttp.sendRequest({
                                //     method: 'GET',
                                //     url: fieldCodeType.queryParamSource
                                // }).then((resData) => {
                                //     OBJ.listData = resData
                                // })
                            }
                        } else {
                            OBJ.listData = []
                        }

                        this.tileDatalist.push(OBJ)
                        // setTimeout(() => {

                        //     this.tileDatalist.push(OBJ)
                        // }, 1000)
                    })
                })
            },
            // 获得查询条件列表-设备品牌
            getConditionByProduce() {
                return rbHttp.sendRequest({
                    method: 'POST',
                    params: { 'type': '生产供应商', 'orderBy': 'produce' },
                    url: '/v1/cmdb/maintenProduce/listProduceByPage'
                }).then((res) => {
                    return res
                })
            },
            // 获得查询条件列表-其他
            getConditionByOther(params) {
                return rbHttp.sendRequest({
                    method: 'GET',
                    url: params
                }).then((resData) => {
                    return resData
                })
            },
            // 表格行选择
            changeSelection(val) {
                this.multipleSelection = val
            },
            // tab切换
            changeTab() {
                this.query()
            },
            // 派单
            send() {

                if (!this.multipleSelection || this.multipleSelection.length < 1) {
                    this.$confirm('请选择告警进行派单', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }
                let orderIdExist = this.multipleSelection.some((item) => {
                    if (item['order_id']) {
                        return true
                    } else {
                        return false
                    }
                })
                if (orderIdExist) {
                    this.$confirm('有工单编号的数据已经派过单,请重新选择', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }

                this.dialogSend.data = this.multipleSelection
                this.dialogSend.dialogVisible = true
            },
            // 恢复
            renew() {
                if (!this.multipleSelection || this.multipleSelection.length < 1) {
                    this.$confirm('请选择告警进行恢复', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }
                this.dialogRenew.data = this.multipleSelection
                this.dialogRenew.dialogVisible = true
            },
            // 清除
            clear() {
                if (!this.multipleSelection || this.multipleSelection.length < 1) {
                    this.$confirm('请选择告警进行恢复', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }
                this.dialogClear.data = this.multipleSelection
                this.dialogClear.dialogVisible = true
            },
            // 关闭弹窗
            closeDialogSend(val) {
                this.dialogSend.dialogVisible = false
                if (val === 'update') {
                    // 刷新表格
                    this.query()
                }
            },
            // 关闭弹窗
            closeDialogRenew(val) {
                this.dialogRenew.dialogVisible = false
                if (val === 'update') {
                    // 刷新表格
                    this.query()
                }
            },
            // 关闭弹窗
            closeDialogClear(val) {
                this.dialogClear.dialogVisible = false
                if (val === 'update') {
                    // 刷新表格
                    this.query()
                }
            },
            handleNodeClick(e) {
                let array = this.tileDatalist
                array.forEach(item => {
                    if (item.code === 'biz_sys') {
                        item.value = e.name
                    }
                })
            },
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {
                    this.queryParams = {
                        'list': [],
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize,
                        // 'tabParams': this.activeTab
                    }
                    if (this.formData.isolateId) {
                        this.queryParams.list.push({
                            'fieldName': 'isolate_id',
                            'fieldType': 'and',
                            'fieldValue': this.formData.isolateId
                        })
                    }
                    this.tileDatalist.forEach(data => {
                        if (data.value !== '') {
                            let queryObj = {}
                            queryObj.fieldName = data.code
                            queryObj.fieldType = data.type
                            queryObj.fieldValue = data.value
                            this.queryParams.list.push(queryObj)
                        } else if (data.valueList.length > 0) {
                            let queryObj = {}
                            queryObj.fieldName = data.code
                            queryObj.fieldType = data.type
                            queryObj.fieldValue = data.valueList.join(',')
                            this.queryParams.list.push(queryObj)
                        }
                    })
                }
            },
            // 查询表格数据
            query(activePagination = false) {
                if (this.activeTab === 'current') {
                    this.queryByCurrent(activePagination)
                } else {
                    this.queryHistory(activePagination)
                }

            },
            // 查询当前
            queryByCurrent(activePagination = false) {
                this.loading = true
                this.setParams(activePagination)
                rbAlertIsolateServiceFactory.getIsolateAlertList(this.queryParams).then((data) => {

                    this.result = data.result
                    this.total = data.count
                }).finally(() => {
                    this.loading = false
                })
            },
            // 查询历史
            queryHistory(activePagination = false) {
                this.loading = true
                this.setParams(activePagination)
                rbAlertIsolateServiceFactory.getIsolateAlertHistory(this.queryParams).then((data) => {

                    this.result = data.result
                    this.total = data.count
                }).finally(() => {
                    this.loading = false
                })
            },
            // 查询屏蔽规则下拉框
            queryIsolates() {
                rbAlertIsolateServiceFactory.getIsolateList({ 'pageSize': -1 }).then((data) => {
                    this.isolateList = data.result
                    this.$forceUpdate()
                })
            },
            reset() {

                for (let item in this.tileDatalist) {
                    var data = this.tileDatalist[item]
                    data.value = ''
                    data.valueList = []
                }
                this.formData.isolateId = ''
            },
            exportBatch() {
                if (this.activeTab === 'current') {
                    this.exportCurrent()
                } else {
                    this.exportHistory()
                }
            },
            // 导出当前
            exportCurrent() {
                this.loading = true
                this.exportDisabled = true
                this.setParams()
                rbAlertIsolateServiceFactory.exportIsolateAlertList(this.queryParams).then((res) => {
                    if (res.code === '0000') {
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.path
                        downLoadElement.setAttribute('download', '告警屏蔽记录.xlsx')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.$message.error(res.message)
                    }
                }).finally(() => {
                    this.loading = false
                    this.exportDisabled = false
                })
            },
            // 导出历史
            exportHistory() {
                this.loading = true
                this.exportDisabled = true
                this.setParams()
                rbAlertIsolateServiceFactory.exportIsolateAlertHistory(this.queryParams).then((res) => {
                    if (res.code === '0000') {
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = res.path
                        downLoadElement.setAttribute('download', '历史告警屏蔽记录.xlsx')
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                    } else {
                        this.$message.error(res.message)
                    }
                }).finally(() => {
                    this.loading = false
                    this.exportDisabled = false
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
