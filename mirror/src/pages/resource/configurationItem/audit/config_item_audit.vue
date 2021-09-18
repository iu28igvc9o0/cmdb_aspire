<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         element-loading-text="正在查询数据, 请稍等...">
        <!-- 动态select:{{codeFrameObj}} -->
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="80px">
            <el-form-item :label="labelItem.name"
                          v-for="(labelItem,labelIndex) in conditionList"
                          :key="labelIndex">
                <YwCodeFrame :frameDatas="labelItem.frameDatas"
                             v-if="labelItem.frameDatas.show"
                             :frameOptions="labelItem.frameOptions"
                             @changeSelect="changeSelect"></YwCodeFrame>
            </el-form-item>
            <el-form-item label="配置项名称">
                <el-select v-model="codeFiledName"
                           clearable
                           placeholder="请选择"
                           filterable>
                    <el-option v-for="(item,index) in filedNameList"
                               :key="index"
                               :label="item.value"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="变更方式">
                <el-select v-model="approvalType"
                           clearable
                           placeholder="请选择">
                    <el-option v-for="(item,index) in approvalTypeList"
                               :key="index"
                               :label="item.id"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="变更来源">
                <el-select v-model="operaterType"
                           clearable
                           placeholder="请选择">
                    <el-option v-for="(item,index) in operaterTypeList"
                               :key="index"
                               :label="item.id"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="提交人">
                <el-input v-model="operator"
                          class="labelSubmitter"></el-input>
            </el-form-item>
            <el-form-item label="提交时间">
                <el-date-picker v-model="startDate"
                                type="datetime"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择时间"
                                :picker-options="startMonthOption">
                </el-date-picker> -
                <el-date-picker v-model="endDate"
                                type="datetime"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="选择时间"
                                :picker-options="endMonthOption">
                </el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-circle-check"
                           @click="batchAudit">批量通过</el-button>
                <el-button type="text"
                           icon="el-icon-circle-close"
                           @click="batchRefuse">批量驳回</el-button>
                <el-button type="text"
                           icon="el-icon-circle-check"
                           @click="allPass">全量通过</el-button>
                <el-button type="text"
                           icon="el-icon-circle-close"
                           @click="showRefuseDialog">全量驳回</el-button>
            </div>

            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                     :selectable='checkboxInit'
                                     max-width="50"> </el-table-column>
                    <el-table-column v-for="(titleItem,titleIndex) in tableTitles"
                                     :key="titleIndex"
                                     :label="titleItem.filedName"
                                     :prop="titleItem.filedCode"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       @click="gotoDetail(scope.row)">
                                {{scope.row[titleItem.filedCode]}}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="配置项名称"
                                     prop="filedName"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="配置项原值"
                                     prop="oldValue"
                                     min-width="150"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="配置项新值"
                                     prop="currValue"
                                     min-width="150"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="变更方式"
                                     prop="approvalType"
                                     min-width="80"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <span v-if="scope.row.approvalType === 'delete'">删除</span>
                            <span v-else-if="scope.row.approvalType === 'update'">更新</span>
                            <span v-else-if="scope.row.approvalType === 'add'">新增</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="变更来源"
                                     prop="operaterType"
                                     min-width="80"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="提交时间"
                                     prop="operatorTime"
                                     min-width="120"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{scope.row.operatorTime | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column label="提交人"
                                     prop="operator"
                                     min-width="80"
                                     :show-overflow-tooltip="true">
                    </el-table-column>
                    <el-table-column label="操作"
                                     prop="safety_number"
                                     width="80"
                                     fixed="right">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           title="通过"
                                           icon="el-icon-circle-check"
                                           @click="audit(scope.row)"></el-button>
                                <el-button type="text"
                                           title="驳回"
                                           icon="el-icon-circle-close"
                                           @click="refuse(scope.row)"></el-button>
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
        <el-dialog class="yw-dialog"
                   :visible.sync="auditDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   title="配置项审核"
                   v-loading="loading"
                   @close="resetRefForm">
            <section class="yw-dialog-main">
                <el-form :model="ruleForm"
                         :rules="rules"
                         ref="ruleForm"
                         class="yw-form  is-required"
                         label-width="84px">
                    <el-form-item label="审核状态">
                        {{auditStatusDesc}}
                    </el-form-item>
                    <el-form-item label="审核描述"
                                  prop="auditDesc"
                                  v-show="descShow">
                        <el-input type="textarea"
                                  :rows="3"
                                  placeholder="请输入审核描述"
                                  v-model="ruleForm.auditDesc" />
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="refuseType === 'part' ? saveAuditForm() : allRefuse()">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </section>
        </el-dialog>
        <ApprovalDialog v-if="processInfo.showProcess"
                        @setImportDisplay="setImportDisplay"
                        :processInfo="processInfo"
                        @refresh="refresh"></ApprovalDialog>
    </div>
</template>

<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import YwPaginationOption from 'src/components/common/yw-pagination/yw-pagination.js'
    export default {
        mixins: [CommonOption, YwCodeFrameOption, YwPaginationOption],
        props: ['treeParams', 'condicationCode', 'tableCode'],
        components: {
            ApprovalDialog: () => import('src/pages/resource/configurationItem/audit/approval-dialog.vue'),
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
            YwPagination: () => import('src/components/common/yw-pagination/yw-pagination.vue'),

        },
        data() {
            return {
                multipleSelection: [],
                handleType: '',
                filedNameList: [],
                auditDialog: false,
                descShow: false,
                auditStatusDesc: '',
                refuseType: '',
                ruleForm: {
                    auditStatus: '',
                    auditDesc: '',
                },
                rules: {
                    auditDesc: [
                        { required: true, message: '请填审核描述', trigger: 'blur' }
                    ]
                },
                startMonthOption: {
                    disabledDate: time => {
                        let dateVal = this.endDate
                        if (dateVal) {
                            return time.getTime() > new Date(dateVal).getTime()
                        }
                    }
                },
                endMonthOption: {
                    disabledDate: time => {
                        let dateVal = this.startDate
                        if (dateVal) {
                            return time.getTime() < new Date(dateVal).getTime() - 1 * 24 * 60 * 60 * 1000
                        }
                    }
                },

                codeFiledName: '',
                operaterType: '',
                operaterTypeList: [],
                approvalType: '',
                approvalTypeList: [
                    {
                        id: '新增',
                        key: '新增',
                        value: 'add'
                    },
                    {
                        id: '更新',
                        key: '更新',
                        value: 'update'
                    },
                    {
                        id: '删除',
                        key: '删除',
                        value: 'delete'
                    }
                ],
                operator: '',
                startDate: '',
                endDate: '',
                processInfo: {
                    showProcess: false,
                    processId: ''
                },

                // 表头
                tableTitles: [],
                // 表单数据
                tableData: [],
                // 左侧树的moduleId
                moduleId: ''
            }
        },
        computed: {
            codeFrameObj() {
                let obj = {}
                if (this.conditionList && this.conditionList.length > 0) {
                    this.conditionList.forEach((item) => {
                        this.$set(obj, item.key, this.getSelectValueByKey(item.key))
                    })
                }
                return obj
            }
        },
        methods: {
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['pageNum'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = Object.assign({
                        'pageNum': this.initPageChange(),
                        'pageSize': this.pageSize,

                        'codeFiledName': this.codeFiledName,
                        'approvalType': this.approvalType,
                        'operaterType': this.operaterType,
                        'operator': this.operator,
                        'startTime': this.startDate,
                        'endTime': this.endDate,
                        'type': 'approval',
                        'moduleId': this.moduleId,
                        'primaryQuery': this.codeFrameObj
                    })
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                if (!this.moduleId) {
                    this.$confirm('缺少模型,请先选择左侧模型数据树', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                }

                this.showLoading()

                this.setParams(activePagination)

                rbCmdbServiceFactory.getApprovalList(this.queryParams).then((res) => {
                    if (res) {
                        this.total = res.count
                        this.tableData = res.data
                    }
                }).finally(() => {
                    this.closeLoading()
                })
            },
            setImportDisplay(val) {
                this.processInfo.showProcess = val
            },
            gotoDetail(row) {
                let queryParams = {
                    name: row.deviceType,
                    instanceId: row.instanceId,
                    moduleId: row.moduleId,
                    state: 'detail'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
            },
            getOperaterType() {
                rbCmdbServiceFactory.getOperaterType().then((res) => {
                    this.operaterTypeList = res
                })
            },

            getFiledNameList() {
                let t = this
                t.filedNameList = []
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/collect/approval/getFiledNameList',
                    timeout: 80000
                }).then((res) => {
                    if (res) {
                        t.filedNameList = res
                        // t.filedNameList.push({ 'filed_code': '', 'filed_name': '全部' })
                        // // t.filedNameList.push({'filed_code': '', 'filed_name': '全部'})
                        // res.forEach((item) => {
                        //     t.filedNameList.push(item)
                        // })
                    }
                }).finally(() => {
                })
            },
            checkboxInit(row) {
                if (row.approvalStatus !== 0)
                    return 0// 不可勾选
                else
                    return 1// 可勾选
            },
            // element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },

            refresh() {
                this.query()
                // if (this.handleType === 'batch') {
                //     if ((this.total - this.multipleSelection.length) % this.pageSize === 0
                //         && parseInt(this.total / this.pageSize) === this.currentPage) {
                //         this.currentPage = this.currentPage - 1
                //         this.query()
                //     } else {
                //         this.query()
                //     }
                // } else {
                //     this.currentPage = 1
                //     this.query()
                // }

            },

            // 全量通过
            allPass() {
                this.handleType = 'all'
                this.query()
                if (this.total <= 0) {
                    this.$message.info('根据条件未查到数据')
                    return
                }
                this.$confirm('审核通过后将替换配置项值, 确定审核通过吗?').then(() => {
                    let data = {
                        allUpdateQuery: Object.assign({}, this.queryParams),
                        updateStatus: 1
                    }
                    this.showLoading()
                    rbCmdbServiceFactory.updateApporval(data).then((res) => {
                        if (res.flag === 'true') {
                            // this.$message.success('审核成功')
                            this.processInfo.showProcess = true
                            this.processInfo.processId = res.processId
                        } else {
                            // this.$message.error('审核失败')
                        }
                    }).finally(() => {
                        this.closeLoading()
                    })
                })
            },
            // 全量驳回
            showRefuseDialog() {
                this.handleType = 'all'
                this.refuseType = 'all'
                this.auditDialog = true
                this.auditStatusDesc = '驳回'
                this.descShow = true
            },
            resetRefForm() {
                this.$refs['ruleForm'].resetFields()
            },
            allRefuse() {
                this.$refs['ruleForm'].validate((valid) => {
                    if (valid) {
                        let data = {
                            allUpdateQuery: Object.assign({}, this.queryParams),
                            updateStatus: 2,
                            refuseDesc: this.ruleForm.auditDesc
                        }
                        this.showLoading()
                        rbCmdbServiceFactory.updateApporval(data).then((res) => {
                            if (res.flag === 'true') {
                                // this.$message.success('审核成功')
                                this.processInfo.showProcess = true
                                this.processInfo.processId = res.processId
                                this.auditDialog = false
                                // this.query()
                            } else {
                                // this.$message.error('审核失败')
                                // this.processInfo.showProcess = true
                                // this.processInfo.processId = res.processId
                                this.query()
                            }
                        }).finally(() => {
                            this.closeLoading()
                        })
                    }
                })
            },
            // 批量通过
            batchAudit() {
                this.handleType = 'batch'
                if (this.multipleSelection.length < 1) {
                    this.$message.error('请选择需要审核的数据')
                    return false
                }
                let list = []
                this.multipleSelection.forEach((item) => {
                    let tem = item
                    tem['approvalStatus'] = '1'
                    list.push(tem.id)
                })
                this.$confirm('审核通过后将替换配置项值, 确定审核通过吗?').then(() => {
                    this.showLoading()
                    this.rbHttp.sendRequest({
                        method: 'POST',
                        url: '/v1/cmdb/collect/approval/update',
                        data: { partUpdateList: list, updateStatus: 1 }
                    }).then((res) => {
                        this.processInfo.showProcess = true
                        this.processInfo.processId = res.processId
                    }).finally(() => {
                        this.closeLoading()
                    })
                })
            },
            audit(item) {
                this.multipleSelection = []
                this.multipleSelection.push(item)
                this.batchAudit()
            },
            // 批量驳回
            batchRefuse() {
                this.handleType = 'batch'
                if (this.multipleSelection.length < 1) {
                    this.$message.error('请选择需要审核的数据')
                    return false
                }
                this.auditDialog = true
                this.auditStatusDesc = '驳回'
                this.descShow = true
                this.ruleForm.auditStatus = 2
                this.refuseType = 'part'
            },
            refuse(item) {
                this.multipleSelection = []
                this.multipleSelection.push(item)
                this.batchRefuse()
            },
            saveAuditForm() {
                let list = []
                this.$refs['ruleForm'].validate((valid) => {
                    if (valid) {
                        this.multipleSelection.forEach((item) => {
                            let tem = item
                            tem['approvalStatus'] = '2'
                            tem['approvalDescribe'] = this.ruleForm.auditDesc
                            list.push(tem.id)
                        })
                        this.$confirm('审核驳回后将忽略本次的配置项值, 确定审核驳回吗?').then(() => {
                            this.$refs['ruleForm'].validate((valid) => {
                                if (valid) {
                                    this.showLoading()
                                    this.rbHttp.sendRequest({
                                        method: 'POST',
                                        url: '/v1/cmdb/collect/approval/update',
                                        data: { partUpdateList: list, updateStatus: 2, refuseDesc: this.ruleForm.auditDesc }
                                    }).then((res) => {
                                        this.auditDialog = false
                                        this.processInfo.showProcess = true
                                        this.processInfo.processId = res.processId
                                    }).finally(() => {
                                        this.closeLoading()
                                    })
                                }
                            })
                        })
                    }
                })
            },
            cancel() {
                this.auditDialog = false
            },
            // 重置
            reset() {
                this.resetCondition()
                this.codeFiledName = ''
                this.approvalType = ''
                this.operaterType = ''
                this.operator = ''
                this.startDate = ''
                this.endDate = ''
                this.query()
            },
            // 获得表头
            getTableTitles() {
                this.tableTitles = [
                    {
                        name: '测试',
                        key: 'test'
                    }
                ]
            },
            // 获得动态数据
            getApprovalHeaderCode() {
                this.conditionList = []
                this.tableTitles = []

                if (this.moduleId) {
                    let params = {
                        moduleId: this.moduleId
                    }
                    rbCmdbServiceFactory.getApprovalHeaderCode(params).then((res) => {
                        res.forEach((item) => {
                            let obj = {
                                key: item.filedCode,
                                name: item.filedName,
                                frameDatas: {
                                    show: true,
                                    // 当前选中值
                                    select: '',
                                    // 当前code对象
                                    codeObj: { codeId: item.codeId, filedCode: item.filedCode },// 暂时写死，后面看cmdb后端字段优化
                                    // 父级code对象
                                    parentCode: '',
                                    // 父级选中的值
                                    parentSelect: '',
                                    // 级联的子级key
                                    cascadeList: [],
                                },
                                frameOptions: {
                                    type: 'input',
                                }
                            }

                            this.conditionList.push(obj)
                            this.tableTitles.push(item)
                        })
                    })
                }

            },
            // 初始化
            async init() {
                this.moduleId = this.treeParams && this.treeParams.metaData && this.treeParams.metaData.id || ''
                // 方式一:获得conditionList和表头
                this.getApprovalHeaderCode()
                // 方式二：获得conditionList
                // if (this.condicationCode) {
                //     this.queryConditionList({ condicationCode: this.condicationCode })
                // }
                // 方式二：获得表头
                // if (this.tableCode) {
                //     this.getTableTitles()
                // }

                if (this.moduleId) {
                    this.query()
                }

            }
        },
        created() {
            this.init()
            this.getOperaterType()
            this.getFiledNameList()
        },
    }
</script>

<style lang="scss" scoped>
    .labelSubmitter {
        width: 193px;
    }
</style>
