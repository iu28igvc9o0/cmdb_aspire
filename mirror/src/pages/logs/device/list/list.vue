<template>
    <div class="components-container yw-dashboard">
        <el-form :inline="true"
                 class="yw-form components-condition">
            <el-form-item label="过滤规则">
                <el-select v-model="filterRule"
                           placeholder="请选择"
                           filterable
                           clearable
                           @clear="clear()"
                           @change="change()">
                    <el-option v-for="item in filterRules"
                               :key="item.uuid"
                               :label="item.name"
                               :value="item.uuid">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button @click="add()">添加</el-button>
                <el-button @click="update()"
                           :disabled="isDisabled">修改</el-button>
                <el-button @click="deleteFilter"
                           :disabled="isDisabled">删除</el-button>
                <el-button type="text"
                           @click="toSysLogAlertRule()">告警规则生成</el-button>
            </el-form-item>
        </el-form>
        <el-form class="yw-form components-condition"
                 label-width="55px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="设备IP">
                <el-input v-model="formData.ip"
                          placeholder="设备IP"></el-input>
            </el-form-item>
            <el-form-item label="时间">
                <el-date-picker v-model="formData.date"
                                type="datetimerange"
                                align="right"
                                unlink-panels
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                :picker-options="pickerOptions24">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="资源池">
                <YwSelectResource :pool="pool"
                                  @changePool="changePool"
                                  @cleanPool="cleanPool"></YwSelectResource>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form components-condition"
                 label-width="90px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="包含关键字">
                <YwInputAdd :keys="keys"
                            :activeKey="activeKey"
                            @changeKey="changeKey"></YwInputAdd>
            </el-form-item>
            <el-form-item label="不包含关键字">
                <YwInputAdd :keys="formData.noIncludeKey"
                            :activeKey="formData.noIncludeActiveKey"
                            @changeKey="changeNoIncludeKey"></YwInputAdd>
            </el-form-item>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-upload2"
                           @click="exportOut">导出</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="result"
                          height="calc(100vh - 340px)"
                          v-loading="loading"
                          stripe
                          border>
                    <el-table-column label="资源池"
                                     width="160">
                        <template slot-scope="scope">
                            {{ scope.row.pool_name }}
                        </template>
                    </el-table-column>
                    <el-table-column label="设备IP"
                                     prop="ip"
                                     sortable
                                     width="140">
                        <template slot-scope="scope">
                            <a class="table-link"
                               @click="getDetails(scope.row)">{{ scope.row.ip }}</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="日志生成时间"
                                     prop="log_create_time"
                                     sortable
                                     width="160">
                        <template slot-scope="scope">
                            {{ scope.row.log_create_time | formatDate}}
                        </template>
                    </el-table-column>

                    <el-table-column label="日志内容">
                        <template slot-scope="scope">
                            <!-- <el-tooltip placement="top">
                              <div slot="content">{{ scope.row.massage }}</div>
                              <span class="text-ellipse"
                                    :title="scope.row.massage">{{ scope.row.massage }}</span>
                            </el-tooltip> -->
                            <span class="pre-wrap">{{ scope.row.massage }}</span>
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
        <el-dialog class="yw-dialog"
                   width="600px"
                   title="基本信息"
                   :visible.sync="dialogMsg.dialogVisible"
                   v-if="dialogMsg.dialogVisible"
                   :modal="false"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <DialogDeviceDetails :obj="dialogMsg.data"
                                 @closeDialog="closeDialog"></DialogDeviceDetails>
        </el-dialog>
        <!-- dialog -->

        <el-dialog class="yw-dialog"
                   v-if="filterRuleDialog"
                   title="过滤规则设置"
                   :visible.sync="filterRuleDialog"
                   width="400px"
                   :show-close="false"
                   :close-on-press-escape="false"
                   :close-on-click-modal="false">
            <el-form ref="filterRuleForm"
                     :rules="filterFromRule"
                     :model="filterRuleData"
                     label-width="100px"
                     class="yw-form components-condition">
                <el-form-item label="过滤规则类型:"
                              prop="ruleType">
                    <el-select v-model="filterRuleData.ruleType"
                               style="width: 200px;">
                        <el-option label="通用"
                                   value="common"></el-option>
                        <el-option label="登录进程"
                                   value="login"></el-option>
                        <el-option label="权限级别检查"
                                   value="permission"></el-option>
                        <el-option label="操作审计"
                                   value="operation"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="过滤规则名称:"
                              prop="name">
                    <el-input v-model="filterRuleData.name"
                              style="width: 200px;"></el-input>
                </el-form-item>
                <el-form-item label="过滤规则描述:"
                              prop="description">
                    <el-input type="textarea"
                              v-model="filterRuleData.description"
                              style="width: 200px;"
                              :rows="8"></el-input>
                </el-form-item>
            </el-form>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="filterRuleSubmit('filterRuleForm')">提交</el-button>
                <el-button size="small"
                           @click="resetForm('filterRuleForm')">取消</el-button>
            </section>
        </el-dialog>

    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import LogServiceFactory from 'src/services/sys/log-services.js'
    import rbAlertNotifyConfigFactory from 'src/services/alert/rb-alert-notify-config.factory.js'

    export default {
        name: 'LogsDeviceList',
        mixins: [QueryObject],
        props: ['ruleType'],
        components: {
            YwSelectResource: () => import('src/components/common/yw-select-resource.vue'),
            YwInputAdd: () => import('src/components/common/yw-input-add.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            // DialogDetails: () => import('../dialog-details.vue'),
            DialogDeviceDetails: () => import('../dialog-device-details.vue'),
            sysLogAlertRule: () => import('src/pages/logs/alert/add/syslog-alert-rule.vue')
        },
        data() {
            return {
                // 表单数据
                formData: {
                    ip: '',
                    date: '',
                    noIncludeActiveKey: '',
                    noIncludeKey: []
                },
                // 查询条件
                queryParams: {

                },
                // 查询结果
                result: [],
                // dialog
                dialogMsg: {
                    id: '',// 每个弹窗数据的唯一标识
                    dialogVisible: false,
                    data: {} // 数据(暂时没有详情接口，从列表数据携带)
                },
                filterRules: [],
                filterRule: '',
                filterRuleDialog: false,
                filterRuleData: {
                    ruleType: 'common',
                    name: '',
                    description: ''
                },
                filterFromRule: {
                    ruleType: [
                        { required: true, message: '请输入过滤规则类型', trigger: 'change' }
                    ],
                    name: [
                        { required: true, message: '请输入过滤规则名称', trigger: 'blur' }
                    ],
                    description: [
                        { required: true, message: '请输入过滤规则描述', trigger: 'blur' }
                    ]
                },
                filterRuleSubmitType: '',
                logFilterRuleDetail: {},
                isDisabled: true,
            }
        },
        created() {
            this.int_()
        },
        methods: {
            int_() {
                // 初始时间
                this.initDate()
                let obj = this.$route.query.searchData
                if (obj) {
                    // if (obj.create_time_start && obj.create_time_end) {
                    //     this.formData.date = [obj.create_time_start, obj.create_time_end]
                    // }
                    this.formData.ip = obj.ip
                    this.pool = obj.pool
                    this.keys = obj.includeKey ? obj.includeKey.toString().split(',') : []
                    this.formData.noIncludeKey = obj.noIncludeKey ? obj.noIncludeKey.toString().split(',') : []
                }
                this.query()
                this.getLogFilterRuleList()
            },
            // 初始化
            initDate() {
                let now = new Date()
                let before = now.getTime() - 1000 * 60 * 60 * 2
                this.formData.date = [formatDate(before), formatDate(now)]
            },

            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {

                    this.queryParams = {
                        //          'create_time_end': formatDate(this.formData.date && this.formData.date[1]),
                        //          'create_time_start': formatDate(this.formData.date && this.formData.date[0]),
                        'create_time_start': this.formData.date ? this.formData.date[0] : '',
                        'create_time_end': this.formData.date ? this.formData.date[1] : '',
                        'ip': this.formData.ip,
                        'pool': this.pool,
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize,
                        'params': [],
                        'includeKey': this.keys.toString(),
                        'noIncludeKey': this.formData.noIncludeKey.toString()
                    }
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.loading = true

                this.setParams(activePagination)

                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/log/getLogData',
                    data: this.queryParams
                }).then((res) => {
                    this.total = res.count
                    this.result = res.result
                    return res
                }).catch((res) => {
                    this.$message.error(res.statusText)
                }).finally(() => {
                    this.loading = false
                })
            },
            // 详情
            getDetails(row) {
                this.dialogMsg.data = row
                this.dialogMsg.dialogVisible = true
            },
            // 重置
            reset() {
                this.formData = {
                    ip: '',
                    date: '',
                    noIncludeActiveKey: '',
                    noIncludeKey: []
                }
                this.cleanKey()
                this.filterRule = ''
                this.cleanPool()
            },
            // 导出
            exportOut() {
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: this.queryParams,
                    binary: true,
                    url: '/v1/log/exportSysLog'
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '日志列表.xls'
                    })
                    return res
                })
            },
            getLogFilterRuleList() {
                LogServiceFactory.getLogFilterRuleList(null).then((res) => {
                    this.filterRules = res
                })
            },
            getLogFilterRuleDetail() {
                LogServiceFactory.getLogFilterRuleDetail(this.filterRule).then((res) => {
                    this.logFilterRuleDetail = res
                    this.filterRuleData.ruleType = res.ruleType
                    this.filterRuleData.name = res.name
                    this.filterRuleData.description = res.description
                    this.formData.ip = res.ip
                    this.formData.date = [res.startTime, res.endTime]
                    this.pool = res.idcType
                    //                    this.keys = res.param ? res.param.split(',') : []
                    this.keys = res.includeKey ? res.includeKey.split(',') : []
                    this.formData.noIncludeKey = res.noIncludeKey ? res.noIncludeKey.split(',') : []
                    this.getOperationPermission(res.creator)
                })
            },
            deleteLogFilterRule() {
                this.$confirm('日志过滤规则删除后无法恢复，确认删除?').then(() => {
                    LogServiceFactory.deleteLogFilterRule(this.filterRule).then((res) => {
                        if (res === 'success') {
                            this.$message({
                                message: '删除成功!',
                                type: 'success'
                            })
                            this.filterRule = ''
                            this.getLogFilterRuleList()
                            this.resetParams()
                        } else {
                            this.$message.error('删除失败!')
                        }
                    })
                })
            },
            createLogFilterRule(data) {
                LogServiceFactory.createLogFilterRule(data).then((res) => {
                    if (res.status === 'success') {
                        this.$message({
                            message: '创建成功!',
                            type: 'success'
                        })
                        this.filterRuleDialog = false
                        this.getLogFilterRuleList()
                        this.filterRule = res.result
                        this.getLogFilterRuleDetail()
                    } else if (res.status === 'error') {
                        this.$message.error(res.result)
                    } else {
                        this.$message.error('创建失败!')
                        this.filterRuleDialog = false
                    }
                }).catch(() => {
                    this.$message.error('创建失败!')
                    this.filterRuleDialog = false
                })
            },
            updateLogFilterRule(data) {
                data['uuid'] = this.filterRule
                LogServiceFactory.updateLogFilterRule(data).then((res) => {
                    if (res === 'success') {
                        this.$message({
                            message: '更新成功!',
                            type: 'success'
                        })
                        this.getLogFilterRuleList()
                        this.filterRuleDialog = false
                    } else {
                        this.$message.error('更新失败!')
                    }
                }).catch(() => {
                    this.$message.error('更新失败!')
                })
            },
            getLogDataByFilterRule() {
                this.loading = true
                LogServiceFactory.getLogDataByFilterRule(this.filterRule, this.initPageChange(), this.pageSize).then((res) => {
                    this.total = res.count
                    this.result = res.result
                }).finally(() => {
                    this.loading = false
                })
            },
            getOperationPermission(req) {
                rbAlertNotifyConfigFactory.getOperationPermission(req).then((res) => {
                    this.isDisabled = !res
                })
            },
            getParams() {
                let obj = {}
                obj.ruleType = this.filterRuleData.ruleType
                obj.name = this.filterRuleData.name
                obj.description = this.filterRuleData.description
                obj.ip = this.formData.ip ? this.formData.ip : ''
                obj.idcType = this.pool ? this.pool : ''
                //                obj.param = this.keys && this.keys.length > 0 ? this.keys.toString() : ''
                obj.includeKey = this.keys && this.keys.length > 0 ? this.keys.toString() : ''
                obj.noIncludeKey = this.formData.noIncludeKey && this.formData.noIncludeKey.length > 0
                    ? this.formData.noIncludeKey.toString() : ''
                obj.startTime = this.formData.date ? this.formData.date[0] : ''
                obj.endTime = this.formData.date ? this.formData.date[1] : ''
                obj.creator = sessionStorage.getItem('username')
                return obj
            },
            resetParams() {
                this.filterRuleData.name = ''
                this.filterRuleData.description = ''
                this.formData.ip = ''
                this.formData.date = []
                this.pool = ''
                this.keys = []
                this.formData.noIncludeKey = []
            },
            filterRuleSubmit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.filterRuleSubmitType === 'add') {
                            this.createLogFilterRule(this.getParams())
                        } else if (this.filterRuleSubmitType === 'update') {
                            this.updateLogFilterRule(this.getParams())
                        }
                    } else {
                        return false
                    }
                })
            },
            resetForm(formName) {
                this.filterRuleDialog = false
                this.$refs[formName].resetFields()
            },
            change() {
                if (this.filterRule) {
                    this.getLogFilterRuleDetail()
                    this.getLogDataByFilterRule()
                }
            },
            clear() {
                this.resetParams()
                this.initDate()
                this.query()
            },
            add() {
                this.filterRuleData.name = ''
                this.filterRuleData.description = ''
                this.filterRuleSubmitType = 'add'
                this.filterRuleDialog = true
            },
            update() {
                if (!this.filterRule) {
                    this.$alert('请先选择日志过滤规则!', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.filterRuleData.name = this.logFilterRuleDetail.name
                this.filterRuleData.description = this.logFilterRuleDetail.description
                this.filterRuleSubmitType = 'update'
                this.filterRuleDialog = true
            },
            deleteFilter() {
                if (!this.filterRule) {
                    this.$alert('请先选择日志过滤规则!', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.deleteLogFilterRule()
            },
            closeDialog(val) {
                this.dialogMsg.dialogVisible = val
            },
            toSysLogAlertRule() {
                this.$router.push({
                    path: '/logs/alert/list',
                    query: {
                        dialog: true,
                        ip: this.formData.ip,
                        idcType: this.pool,
                        param: this.keys,
                        noParam: this.formData.noIncludeKey
                    }
                })
            },
            changeNoIncludeKey(val) {
                this.formData.noIncludeKey = val
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
