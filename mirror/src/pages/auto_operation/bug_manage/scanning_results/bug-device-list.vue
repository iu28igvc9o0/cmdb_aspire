<!-- 漏洞管理：漏洞扫描结果相关的设备列表 -->
<template>
    <div v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">

        <!-- 搜索组件 -->
        <search-box :currentRowData="currentRowData"
                    :currentProp="currentProp"
                    :scanCycleNum="scanCycleNum"
                    @updateFormSearch="updateFormSearch"
                    @search="search"></search-box>

        <el-form class="yw-form">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           v-if="!isReadonly"
                           :disabled="loading"
                           @click="batchFixBug">
                    <i class="icon iconfont f14">&#xe62b;</i>
                    批量修复
                </el-button>
                <el-button type="text"
                           :disabled="loading"
                           icon="el-icon-download"
                           @click="exportVulInstance">
                    导出
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 330px)"
                          v-loading="loading">
                    <el-table-column prop="poolName"
                                     label="资源池"
                                     min-width="90"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="department1"
                                     label="一级部门"
                                     show-overflow-tooltip
                                     min-width="80"></el-table-column>
                    <el-table-column prop="department2"
                                     label="二级部门"
                                     show-overflow-tooltip
                                     min-width="90"></el-table-column>
                    <el-table-column prop="bizSystem"
                                     label="业务系统"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="hostIp"
                                     label="IP地址"
                                     show-overflow-tooltip
                                     min-width="90"></el-table-column>
                    <el-table-column prop="osType"
                                     label="操作系统"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="canFixed"
                                     label="是否可修复"
                                     show-overflow-tooltip
                                     width="90">
                        <template slot-scope="scope">
                            <span v-if="scope.row.canFixed !== 'N'">是</span>
                            <span v-else>否</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="status"
                                     label="修复状态"
                                     show-overflow-tooltip
                                     width="90">
                        <template slot-scope="scope">
                            <span v-if="scope.row.status === 'WAIT_REPAIR'">等待修复</span>
                            <span v-else-if="scope.row.status === 'PROCESSED'">已处理</span>
                            <span v-else>不能修复</span>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column prop="needReboot"
                                     label="是否重启"
                                     show-overflow-tooltip
                                     width="90">
                        <template slot-scope="scope">
                            <span v-if="scope.row.needReboot === 'Y'">是</span>
                            <span v-else>否</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="riskLevelDesc"
                                     label="漏洞级别"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="repairMethod"
                                     label="漏洞修复建议"
                                     show-overflow-tooltip
                                     min-width="100"></el-table-column> -->
                    <el-table-column prop="is_fixed"
                                     label="修复成功"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <span v-if="scope.row.isFixed==='Y'">是</span>
                            <span v-if="scope.row.isFixed==='N'">否</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="repairTime"
                                     label="修复时间"></el-table-column>
                    <el-table-column prop="repairPerson"
                                     label="修复人"
                                     min-width="50"></el-table-column>
                    <el-table-column prop="goBackTime"
                                     label="回退时间"></el-table-column>
                    <el-table-column prop="goBackPerson"
                                     label="回退人"
                                     min-width="50"></el-table-column>
                    <el-table-column label="选择修复作业"
                                     v-if="!isReadonly">
                        <template slot="header">
                            <span class="required-option">选择修复作业</span>
                        </template>
                        <template slot-scope="scope">
                            <el-form @submit.native.prevent
                                     class="yw-form"
                                     :model="scope.row">
                                <el-form-item>
                                    <el-select v-model="scope.row.pipelineId"
                                               :disabled="isReadonly || scope.row.status === 'BEYOND_REPAIR'"
                                               placeholder="请选择作业"
                                               filterable>
                                        <el-option v-for="val in curWorkList"
                                                   :key="val.pipeline_id"
                                                   :label="val.pipeline_name"
                                                   :value="val.pipeline_id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column label="选择回退作业"
                                     v-if="!isReadonly">
                        <template slot="header">
                            <span class="required-option">选择回退作业</span>
                        </template>
                        <template slot-scope="scope">
                            <el-form @submit.native.prevent
                                     class="yw-form"
                                     :model="scope.row">
                                <el-form-item>
                                    <el-select v-model="scope.row.workId"
                                               :disabled="isReadonly || scope.row.status === 'BEYOND_REPAIR'"
                                               placeholder="请选择作业"
                                               filterable>
                                        <el-option v-for="val in curBackList"
                                                   :key="val.pipeline_id"
                                                   :label="val.pipeline_name"
                                                   :value="val.pipeline_id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column label="选择复查作业"
                                     v-if="!isReadonly">
                        <template slot="header">
                            <span class="required-option">选择复查作业</span>
                        </template>
                        <template slot-scope="scope">
                            <el-form @submit.native.prevent
                                     class="yw-form"
                                     :model="scope.row">
                                <el-form-item>
                                    <el-select v-model="scope.row.workId"
                                               :disabled="isReadonly || scope.row.status === 'BEYOND_REPAIR'"
                                               placeholder="请选择作业"
                                               filterable>
                                        <el-option v-for="val in curRecheckList"
                                                   :key="val.pipeline_id"
                                                   :label="val.pipeline_name"
                                                   :value="val.pipeline_id"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="180">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="查看设备详情"
                                       icon="el-icon-view"
                                       @click="viewDeviceDetail(scope.row)"></el-button>
                            <el-button type="text"
                                       v-if="!isReadonly && scope.row.status !== 'BEYOND_REPAIR'"
                                       :disabled="!scope.row.isAgentRun"
                                       title="修复"
                                       @click="fixBug(scope.row)">
                                <i class="icon iconfont f14">&#xe62b;</i>
                            </el-button>
                            <el-button type="text"
                                       title="回退"
                                       :disabled="!scope.row.isAgentRun && scope.row.status !== 'PROCESSED'"
                                       icon="el-icon-s-promotion"
                                       @click="goBack(scope.row)"></el-button>
                            <el-button type="text"
                                       title="复查"
                                       :disabled="!scope.row.isAgentRun"
                                       icon="el-icon-refresh"
                                       @click="refreshFixed(scope.row)"></el-button>
                            <el-button type="text"
                                       v-if="!isReadonly && scope.row.status !== 'BEYOND_REPAIR'"
                                       title="不可修复"
                                       icon="el-icon-lock"
                                       @click="handleCanFixed(scope.row)"></el-button>
                            <el-button type="text"
                                       v-if="!isReadonly && scope.row.status !== 'NO_NEED'"
                                       title="无需修复"
                                       icon="el-icon-close-notification"
                                       @click="noNeedFixed(scope.row)"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
            </div>
        </el-form>

        <!-- 设备信息弹框 -->
        <el-dialog width="90%"
                   class="yw-dialog"
                   title="设备信息"
                   v-if="deviceDetailBoxShow"
                   :visible.sync="deviceDetailBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <bug-device-detail :deviceData="curDeviceData"></bug-device-detail>
        </el-dialog>

    </div>
</template>

<script>
    import bugDeviceDetail from 'src/pages/auto_operation/bug_manage/scanning_results/bug-device-detail.vue'
    import searchBox from 'src/pages/auto_operation/bug_manage/scanning_results/search-box.vue'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import { setObjKeys } from 'src/utils/globalUtils.js'
    import { createDownloadFile } from 'src/utils/utils.js'
    import rbAutoOperationCustomParameterFactory from 'src/services/auto_operation/rb-auto-operation-custom-parameter.factory.js'

    export default {
        name: 'ScanningResultsBugDeviceList',
        components: {
            bugDeviceDetail,
            searchBox
        },
        props: {
            isReadonly: {
                type: Boolean,
                default: false
            },
            currentRowData: {
                type: Object,
                default() {
                    return {}
                }
            },
            currentProp: {
                type: String,
                default() {
                    return ''
                }
            },
            originWorkData: {
                type: Object,
                default() {
                    return {}
                }
            },
            originWorkBackData: {
                type: Object,
                default() {
                    return {}
                }
            },
            originWorkRecheckData: {
                type: Object,
                default() {
                    return {}
                }
            },
            formSearchParams: {
                type: Object,
                default() {
                    return {}
                }
            }
        },
        data() {
            return {
                workId: '',
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                deviceDetailBoxShow: false, // 关联作业弹框
                // 当前设备id
                curDeviceId: {},
                // 非默认作业的设备
                specialVulInstanceList: [],
                formSearch: {},
                scanCycleNum: ''
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {
            // 当前漏洞已关联的作业列表
            curWorkList() {
                let arr = []
                if (this.isReadonly) {
                    return arr
                }
                let hasPipelineIdList = this.currentRowData.pipelineIdList && this.currentRowData.pipelineIdList.length
                if (this.originWorkData.dataList && hasPipelineIdList) {
                    this.originWorkData.dataList.forEach(item => {
                        this.currentRowData.pipelineIdList.forEach(pipelineId => {
                            if (item.pipeline_id === pipelineId) {
                                arr.push(item)
                            }
                        })
                    })
                }
                return arr
            },
            curBackList() {
                let arr = []
                if (this.isReadonly) {
                    return arr
                }
                let hasPipelineIdList = this.currentRowData.goBackPipelineIdList && this.currentRowData.goBackPipelineIdList.length
                if (this.originWorkBackData.dataList && hasPipelineIdList) {
                    this.originWorkBackData.dataList.forEach(item => {
                        this.currentRowData.goBackPipelineIdList.forEach(pipelineId => {
                            if (item.pipeline_id === pipelineId) {
                                arr.push(item)
                            }
                        })
                    })
                }
                return arr
            },
            curRecheckList() {
                let arr = []
                if (this.isReadonly) {
                    return arr
                }
                let hasPipelineIdList = this.currentRowData.recheckPipelineIdList && this.currentRowData.recheckPipelineIdList.length
                if (this.originWorkRecheckData.dataList && hasPipelineIdList) {
                    this.originWorkRecheckData.dataList.forEach(item => {
                        this.currentRowData.recheckPipelineIdList.forEach(pipelineId => {
                            if (item.pipeline_id === pipelineId) {
                                arr.push(item)
                            }
                        })
                    })
                }
                return arr
            }


        },
        created() {
            this.loading = true

            // this.formSearch = this.formSearchParams
            this.scanCycleNum = this.formSearchParams.scanCycle
            console.log('formSearchParams===', this.formSearchParams)
        },
        methods: {
            // 回退
            goBack(row) {
                bugManageService.executeVulnerabilityGoBack({
                    vulInstanceId: row.id,
                    pipelineId: row.workId
                }).then(res => {
                    if (res.flag) {
                        this.$message.success('回退成功')
                        this.search()
                        this.showRunWorkResult(res.biz_data)
                    } else {
                        this.showErrorTip(res)
                    }
                })
            },
            // 复查
            refreshFixed(row) {
                bugManageService.executeVulnerabilityRecheck({
                    vulInstanceId: row.id,
                    pipelineId: row.workId
                }).then(res => {
                    if (res.flag) {
                        this.$message.success('复查成功')
                        this.search()
                        this.showRunWorkResult(res.biz_data)
                    } else {
                        this.showErrorTip(res)
                    }
                })
            },
            // 处理非默认作业的设备修复
            changeWork(curPipelineId, row) {
                let obj = {
                    pipelineId: curPipelineId,
                    vulInstanceId: row.id
                }
                let hasExist = false
                this.specialVulInstanceList.forEach(item => {
                    if (item.vulInstanceId === row.id) {
                        item.pipelineId = curPipelineId
                        hasExist = true
                    }
                })
                if (!hasExist) {
                    this.specialVulInstanceList.push(obj)
                }
            },
            // 导出
            exportVulInstance() {
                this.formSearch.vulnerabilityId = this.currentRowData.id
                rbAutoOperationCustomParameterFactory.exportVulInstance(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '漏洞实例列表.xls')
                    }

                })
            },
            // 批量修复
            batchFixBug() {
                if (!this.curWorkList.length && !this.curBackList.length && !this.curRecheckList.length) {
                    this.$message.error('请先关联作业!')
                    return
                }
                let req = {
                    defaultPipelineId: this.curWorkList[0].pipeline_id,
                    specialVulInstanceList: this.specialVulInstanceList,
                    vulId: this.currentRowData.id
                }
                this.$confirm('确定进行批量修复操作？').then(() => {
                    this.loading = true
                    bugManageService.batchExecuteVulnerabilityRepair(req)
                        .then(res => {
                            if (res.flag) {
                                this.$message.success('修复成功')
                            } else {
                                this.loading = false
                                this.showErrorTip(res)
                            }
                        })
                        .catch(error => {
                            this.loading = false
                            this.showErrorTip(error)
                        })
                })

            },
            updateFormSearch(params) {
                this.formSearch = this.$utils.deepClone(params)
            },
            // 查询列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                    vulnerabilityId: this.currentRowData.id
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                bugManageService
                    .queryVulInstanceList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                        this.dataList.forEach(item => {
                            setObjKeys(this, item, 'pipelineId', item.pipelineId || (this.curWorkList[0] && this.curWorkList[0].pipeline_id))
                        })
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 打开设备详情弹框
            viewDeviceDetail(row) {
                this.curDeviceData = row
                this.deviceDetailBoxShow = true
            },
            // 设置不可修复
            handleCanFixed(row) {
                this.$confirm(`确定设置【${row.hostIp}】为不可修复？`).then(() => {
                    this.loading = true
                    let req = {
                        id: row.id, // 漏洞主机id
                        status: 'BEYOND_REPAIR',
                    }
                    this.updateVulInstance(req)
                })
            },
            // 设置无需修复
            noNeedFixed(row) {
                this.$confirm(`确定设置【${row.hostIp}】为无需修复？`).then(() => {
                    this.loading = true
                    let req = {
                        id: row.id, // 漏洞主机id
                        status: 'NO_NEED',
                    }
                    this.updateVulInstance(req)
                })
            },
            // 更新漏洞主机实例 接口
            updateVulInstance(req) {
                bugManageService
                    .updateVulInstance(req)
                    .then(() => {
                        this.$message.success('操作成功')
                        this.search()
                        this.loading = false
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 开始执行作业（修复漏洞）
            fixBug(row) {
                this.executeVulnerabilityRepair({
                    vulInstanceId: row.id,
                    pipelineId: row.pipelineId
                })
            },
            // 点击执行修复，返回修复实例id（作业执行实例id），打开执行详情弹框
            executeVulnerabilityRepair(req) {
                this.loading = true
                bugManageService
                    .executeVulnerabilityRepair(req)
                    .then(res => {
                        if (res.flag) {
                            this.$message.success('修复成功')
                            this.search()
                            this.showRunWorkResult(res.biz_data)
                        } else {
                            this.showErrorTip(res)
                        }
                        this.loading = false
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            // 查看作业实例结果弹框
            showRunWorkResult(pipelineInstanceId) {
                this.$bus.emit('showRunWorkResult', pipelineInstanceId)
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
