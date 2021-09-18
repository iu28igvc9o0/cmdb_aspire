<template>
    <div class="components-container yw-dashboard">
        <el-form class="yw-form components-condition"
                 ref="queryParamForm"
                 label-width="75px"
                 :inline="true"
                 :model="queryParamForm">
            <el-form-item label="规则名称"
                          prop="configName">
                <el-input v-model="queryParamForm.configName"
                          placeholder="规则名称"></el-input>
            </el-form-item>
            <el-form-item label="启用状态"
                          prop="isOpen">
                <el-select v-model="queryParamForm.isOpen"
                           clearable>
                    <el-option label="启用"
                               value="1"></el-option>
                    <el-option label="停用"
                               value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="生效时间"
                          prop="configTime">
                <el-date-picker v-model="queryParamForm.configTime"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd"
                                size="mini"></el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="searchOrderConfig()">查询
                </el-button>
                <el-button @click="cancel('query')">重置</el-button>
            </section>
            <br />
            <el-form-item label="派单类型">
                <el-select v-model="queryParamForm.orderType"
                           placeholder="请选择"
                           clearable
                           filterable
                           multiple
                           collapse-tags>
                    <el-option v-for="item in order_type"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="派单时段"
                          prop="configName">
                <el-input v-model="queryParamForm.orderTimeInterval"
                          placeholder="00:00-23:59"></el-input>
            </el-form-item>
        </el-form>
        <el-form class="yw-form"
                 v-loading="loading">
            <div class="table-operate-wrap clearfix">
                <div class="fr">
                    <el-button type="text"
                               icon=""
                               @click="getOrderLogs(null)">派单日志
                    </el-button>
                </div>
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="addAlertOrderConfigDialog = true">新增
                </el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="delOrderConfig()">删除
                </el-button>
                <el-button type="text"
                           icon="fa fa-unlock-alt"
                           @click="openOrderConfig()">启用
                </el-button>
                <el-button type="text"
                           icon="fa fa-lock"
                           @click="closeOrderConfig()">停用
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          @selection-change="changeCheckbox"
                          :data="orderConfigData"
                          height="calc(100vh - 310px)"
                          stripe
                          border>
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column label="规则名称"
                                     prop="configName">
                    </el-table-column>
                    <el-table-column label="生效时间"
                                     prop="startTime">
                    </el-table-column>
                    <el-table-column label="失效时间"
                                     prop="endTime">
                    </el-table-column>
                    <el-table-column label="启用状态"
                                     prop="isOpen">
                    </el-table-column>
                    <el-table-column label="派单类型"
                                     prop="orderType">
                    </el-table-column>
                    <el-table-column label="派单时段"
                                     prop="orderTimeInterval">
                    </el-table-column>
                    <el-table-column prop="region"
                                     label="操作">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           @click="view(scope.row.uuid)"
                                           title="查看"
                                           icon="el-icon-view"></el-button>
                                <el-button type="text"
                                           @click="edit(scope.row.uuid)"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           v-if="scope.row.isOpen === '停用' && checkUse(scope.row)"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           @click="delOrderConfig(scope.row)"
                                           icon="el-icon-delete"
                                           v-if="scope.row.isOpen === '停用' && checkUse(scope.row)"></el-button>
                                <el-button type="text"
                                           @click="openOrderConfig(scope.row.uuid)"
                                           title="启用"
                                           icon="el-icon-unlock"
                                           v-if="scope.row.isOpen === '停用' && checkUse(scope.row)"></el-button>
                                <el-button type="text"
                                           @click="closeOrderConfig(scope.row.uuid)"
                                           title="停用"
                                           icon="el-icon-lock"
                                           v-if="scope.row.isOpen === '启用' && checkUse(scope.row)"></el-button>
                                <el-button type="text"
                                           icon="fa fa-clone"
                                           title="拷贝"
                                           @click="copy(scope.row.uuid)"
                                           v-if="scope.row.isOpen === '停用' && checkUse(scope.row)"></el-button>
                                <el-button type="text"
                                           @click="getOrderLogs(scope.row.uuid)"
                                           title="派单日志"
                                           icon="el-icon-document"></el-button>
                            </div>
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

        <!-- 新增告警派单配置 -->
        <el-dialog class="yw-dialog"
                   title="新增告警派单配置"
                   width="60%"
                   :visible.sync="addAlertOrderConfigDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <alert-auto-order-config ref="alertAutoOrderConfig"
                                         v-if="addAlertOrderConfigDialog"
                                         :type="'add'">
                </alert-auto-order-config>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           :disabled="addAlertOrderConfigDisabled"
                           @click="addAlertOrderConfigSubmit()">创建</el-button>
                <el-button @click="addAlertOrderConfigDialog = false">取消</el-button>
            </section>
        </el-dialog>
        <!-- 告警派单配置详情 -->
        <el-dialog class="yw-dialog"
                   title="告警派单配置详情"
                   width="60%"
                   :visible.sync="alertOrderConfigDetailDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <alert-auto-order-config ref="alertAutoOrderConfig"
                                         v-if="alertOrderConfigDetailDialog"
                                         :alertOrderConfigDetail="alertOrderConfigDetail"
                                         :type="'detail'">
                </alert-auto-order-config>
            </section>
        </el-dialog>
        <!-- 修改派单配置详情 -->
        <el-dialog class="yw-dialog"
                   title="修改派单配置详情"
                   width="60%"
                   :visible.sync="updateAlertOrderConfigDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <alert-auto-order-config ref="alertAutoOrderConfig"
                                         v-if="updateAlertOrderConfigDialog"
                                         :alertOrderConfigDetail="alertOrderConfigDetail"
                                         :type="'update'">
                </alert-auto-order-config>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           :disabled="updateAlertOrderConfigDisabled"
                           @click="updateAlertOrderConfigSubmit()">提交</el-button>
                <el-button @click="updateAlertOrderConfigDialog = false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertAutoOrderServicesFactory from 'src/services/alert/rb-alert-auto-order-services.factory.js'
    import alertAutoOrderConfig from 'src/pages/mirror_alert/alert_auto_order/add/alert-auto-order-config.vue'
    import { order_type } from '../../alert_his/config/options.js'
    export default {
        name: 'MirrorAlertShield',
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            alertAutoOrderConfig,
        },
        data() {
            return {
                // 多选框模板存放的值
                multipleSelection: [],
                queryParamForm: {
                    configName: '',
                    isOpen: '',
                    configTime: [],
                    orderType: [],
                    orderTimeInterval: '',
                },
                orderConfigData: [],
                loading: false,
                addAlertOrderConfigDialog: false,
                alertOrderConfigDetail: {},
                alertOrderConfigDetailDialog: false,
                updateAlertOrderConfigDialog: false,
                order_type: [],
                addAlertOrderConfigDisabled: false,
                updateAlertOrderConfigDisabled: false,
            }
        },
        created() {
            this.searchOrderConfig()
            this.order_type = order_type
        },
        methods: {
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.searchOrderConfig()
            },
            handleCurrentChange(val) {
                this.currentPage = val
                this.searchOrderConfig()
            },
            changeCheckbox(val) {
                this.multipleSelection = val
            },
            getQueryParams() {
                let params = {}
                params.configName = this.queryParamForm.configName
                params.isOpen = this.queryParamForm.isOpen
                params.orderType = this.queryParamForm.orderType.toString()
                params.orderTimeInterval = this.queryParamForm.orderTimeInterval
                params.startTime = this.queryParamForm.configTime ? this.queryParamForm.configTime[0] : null
                params.endTime = this.queryParamForm.configTime ? this.queryParamForm.configTime[1] : null
                params.pageNum = this.currentPage
                params.pageSize = this.pageSize
                return params
            },
            searchOrderConfig() {
                this.getAlertAutoOrderConfigList()
            },
            cancel(type) {
                if (type === 'query') {
                    this.queryParamForm.configName = ''
                    this.queryParamForm.isOpen = ''
                    this.queryParamForm.configTime = []
                }
            },
            getAlertAutoOrderConfigList() {
                this.loading = true
                rbAlertAutoOrderServicesFactory.getAlertAutoOrderConfigList(this.getQueryParams()).then((res) => {
                    this.total = res.count
                    this.orderConfigData = res.result
                }).finally(() => {
                    this.loading = false
                })
            },
            delOrderConfig(obj) {
                let req = []
                let boolean = true
                let message = ''
                if (obj) {
                    if (obj.isOpen === '启用') {
                        this.$alert('告警派单配置处于启用状态无法删除!', '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                    req.push(obj.uuid)
                } else {
                    if (this.multipleSelection.length < 1) {
                        boolean = false
                        message = '请先重新选择告警通知配置!'
                    } else {
                        this.multipleSelection.forEach((item) => {
                            if (item.isOpen === '启用') {
                                boolean = false
                                message = '告警派单配置没有全部关闭,请重新选择!'
                            } else {
                                req.push(item.uuid)
                            }
                        })
                    }
                }
                if (!boolean) {
                    this.$alert(message, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.$confirm('确认删除?').then(() => {
                    rbAlertAutoOrderServicesFactory.deleteAlertAutoOrderConfig(req).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '删除成功!',
                                type: 'success'
                            })
                            this.currentPage = 1
                            this.searchOrderConfig()
                        } else {
                            this.$message.error('删除失败!')
                        }
                    })
                })
            },
            openOrderConfig(uuid) {
                let req = []
                let boolean = true
                let message = ''
                let data = []
                if (uuid) {
                    req.push(uuid)
                } else {
                    if (this.multipleSelection.length < 1) {
                        boolean = false
                        message = '请先选择告警派单配置!'
                    } else {
                        this.multipleSelection.forEach((item) => {
                            if (item.isOpen === '启用') {
                                data.push(item)
                            } else {
                                req.push(item.uuid)
                            }
                        })
                        if (data.length === this.multipleSelection.length) {
                            boolean = false
                            message = '选择的告警派单配置已全部启用,请重新选择!'
                        }
                    }
                    if (!boolean) {
                        this.$alert(message, '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                }
                this.$confirm('确认修改?').then(() => {
                    rbAlertAutoOrderServicesFactory.updateAlertAutoOrderConfigStatus(req, '1').then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            })
                            this.searchOrderConfig()
                        } else {
                            this.$message.error('修改失败!')
                        }
                    })
                })
            },
            closeOrderConfig(uuid) {
                let req = []
                let boolean = true
                let message = ''
                let data = []
                if (uuid) {
                    req.push(uuid)
                } else {
                    if (this.multipleSelection.length < 1) {
                        boolean = false
                        message = '请先选择告警派单配置!'
                    } else {
                        this.multipleSelection.forEach((item) => {
                            if (item.isOpen === '停用') {
                                data.push(item)
                            } else {
                                req.push(item.uuid)
                            }
                        })
                        if (data.length === this.multipleSelection.length) {
                            boolean = false
                            message = '选择的告警派单配置已全部停用,请重新选择!'
                        }
                    }
                    if (!boolean) {
                        this.$alert(message, '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                }
                this.$confirm('确认修改?').then(() => {
                    rbAlertAutoOrderServicesFactory.updateAlertAutoOrderConfigStatus(req, '2').then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            })
                            this.searchOrderConfig()
                        } else {
                            this.$message.error('修改失败!')
                        }
                    })
                })
            },
            validateAddRequest() {
                let alertOrderConfigForm = this.$refs.alertAutoOrderConfig.alertOrderConfigForm
                alertOrderConfigForm.alertFilter = this.getAlertFilterData(this.$refs.alertAutoOrderConfig.getFilterData())
                let obj = {}
                obj.alertOrderConfigForm = alertOrderConfigForm
                obj.flag = true
                if (alertOrderConfigForm.configTimeType === '2' && (!alertOrderConfigForm.startTime || !alertOrderConfigForm.endTime)) {
                    obj.flag = false
                    obj.msg = '请输入生效时间或者失效时间!'
                    return obj
                } else if (!alertOrderConfigForm.configName) {
                    obj.flag = false
                    obj.msg = '请输入配置名称!'
                    return obj
                } else if (!alertOrderConfigForm.userName) {
                    obj.flag = false
                    obj.msg = '请输入维护用户!'
                    return obj
                } else {
                    return obj
                }
            },
            getAlertFieldRequest(alertOrderConfigForm) {
                let obj = {}
                obj.uuid = alertOrderConfigForm.uuid
                obj.configName = alertOrderConfigForm.configName
                obj.configTimeType = alertOrderConfigForm.configTimeType
                if (alertOrderConfigForm.configTimeType === '2') {
                    obj.startTime = alertOrderConfigForm.startTime
                    obj.endTime = alertOrderConfigForm.endTime
                }
                obj.userName = alertOrderConfigForm.userName
                obj.configDescription = alertOrderConfigForm.configDescription
                obj.isOpen = alertOrderConfigForm.isOpen
                obj.alertFilter = alertOrderConfigForm.alertFilter
                obj.orderType = alertOrderConfigForm.orderType
                obj.orderTimeSpace = alertOrderConfigForm.orderTimeSpace
                obj.orderTimeInterval = alertOrderConfigForm.orderTimeInterval
                return obj
            },
            addAlertOrderConfigSubmit() {
                let obj = this.validateAddRequest()
                if (!obj.flag) {
                    this.$alert(obj.msg, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let alertOrderConfigForm = obj.alertOrderConfigForm
                rbAlertAutoOrderServicesFactory.checkName(alertOrderConfigForm.configName).then((res) => {
                    console.log(res)
                    if (res) {
                        this.$alert('配置名称重复,请重新输入!', '警告', {
                            confirmButtonText: '确定'
                        })
                    } else {
                        this.addAlertOrderConfigDisabled = true
                        rbAlertAutoOrderServicesFactory.createAlertAutoOrderConfig(this.getAlertFieldRequest(obj.alertOrderConfigForm)).then((res) => {
                            if (res === 'SUCCESS') {
                                this.$message({
                                    message: '创建成功!',
                                    type: 'success'
                                })
                                this.currentPage = 1
                                this.searchOrderConfig()
                                this.addAlertOrderConfigDialog = false
                            } else {
                                this.$message.error('创建失败!')
                            }
                        }).catch(() => {
                            this.addAlertOrderConfigDialog = false
                        }).finally(() => {
                            this.addAlertOrderConfigDisabled = false
                        })
                    }
                })

            },
            view(uuid) {
                rbAlertAutoOrderServicesFactory.getAlertAutoOrderConfigDetail(uuid).then((res) => {
                    this.alertOrderConfigDetail = res
                    this.alertOrderConfigDetailDialog = true
                })
            },
            edit(uuid) {
                rbAlertAutoOrderServicesFactory.getAlertAutoOrderConfigDetail(uuid).then((res) => {
                    this.alertOrderConfigDetail = res
                    this.updateAlertOrderConfigDialog = true
                })
            },
            updateAlertOrderConfigSubmit() {
                let obj = this.validateAddRequest()
                if (!obj.flag) {
                    this.$alert(obj.msg, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                rbAlertAutoOrderServicesFactory.checkName(obj.alertOrderConfigForm.configName).then((resp) => {
                    console.log(resp)
                    if (resp && resp !== obj.alertOrderConfigForm.uuid) {
                        this.$alert('配置名称重复,请重新输入!', '警告', {
                            confirmButtonText: '确定'
                        })
                    } else {
                        this.updateAlertOrderConfigDisabled = true
                        rbAlertAutoOrderServicesFactory.updateAlertAutoOrderConfig(this.getAlertFieldRequest(obj.alertOrderConfigForm)).then((res) => {
                            if (res === 'SUCCESS') {
                                this.$message({
                                    message: '修改成功!',
                                    type: 'success'
                                })
                                this.searchOrderConfig()
                                this.updateAlertOrderConfigDialog = false
                            } else {
                                this.$message.error('修改失败!')
                            }
                        }).catch(() => {
                            this.updateAlertOrderConfigDialog = false
                        }).finally(() => {
                            this.updateAlertOrderConfigDisabled = false
                        })
                    }
                })
            },
            getAlertFilterData(obj) {
                let dataFilterJson = { data: [] }
                let dataFilterList = JSON.parse(JSON.stringify(obj))
                for (let i = 0; i < dataFilterList.length; i++) {
                    let rowList = dataFilterList[i].rowList
                    let andData = { data: [] }
                    for (let j = 0; j < rowList.length; j++) {
                        let col0 = rowList[j].colList[0].selectedVal
                        let col1 = rowList[j].colList[1].selectedVal
                        let col2 = rowList[j].colList[2].selectedVal
                        if (col0 === '' || col1 === '' || col2 === '') {
                            // console.log('有数据未填，不能提交')
                            // this.$message.warning(this.validationPhase.errorMsg)
                            // return
                        }
                        let colData = {}
                        if (rowList[j].colList[2].type === 'select' && rowList[j].colList[1].multipleFlag === true) {
                            colData['value'] = col2
                        } else if ((col1 === 'in' || col1.indexOf('not') === 0) && rowList[j].colList[2].type !== 'select') {
                            let vals = col2.split(',')
                            colData['value'] = vals
                        } else {
                            colData['value'] = col2
                        }
                        colData['filterItemName'] = col0
                        colData['operate'] = col1
                        colData['jdbcType'] = rowList[j].colList[0].jdcbType
                        andData['data'].push(colData)
                    }
                    dataFilterJson['data'].push(andData)
                }
                return JSON.stringify(dataFilterJson)
            },
            getOrderLogs(uuid) {
                this.$router.push({
                    path: '/mirror_alert/alert_auto_order/order_log',
                    query: {
                        orderConfigId: uuid
                    }
                })
            },
            copy(uuid) {
                this.$confirm('确认拷贝?').then(() => {
                    rbAlertAutoOrderServicesFactory.copyAlertAutoOrderConfig(uuid).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '复制成功!',
                                type: 'success'
                            })
                            this.currentPage = 1
                            this.searchOrderConfig()
                        } else {
                            this.$message.error('复制失败!')
                        }
                    }).catch(() => {
                        this.$message.error('复制失败!')
                    })
                })
            },
            checkUse(rowData) {
                let createUser = rowData.creator
                let operateUser = rowData.userName
                let userName = sessionStorage.getItem('username')
                let namespace = sessionStorage.getItem('namespace')
                if (userName === createUser || userName === namespace) {
                    return true
                }
                if (operateUser !== null) {
                    let userArray = operateUser.split(',')
                    if (userArray.indexOf(userName) > -1) {
                        return true
                    }
                }
                return false
            },
        }
    }
</script>

<style lang="scss" scoped>
    .yw-form.components-condition > .el-form-item:nth-last-child(2) {
        margin-right: 10px;
    }
    .yw-form.components-condition > .el-form-item {
        float: unset;
    }
</style>
