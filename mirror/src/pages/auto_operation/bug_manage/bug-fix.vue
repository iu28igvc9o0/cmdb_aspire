<template>
    <div>
        <!-- 漏洞概要 -->
        <table class="bordered">
            <tr>
                <td width="90"
                    class="bold">漏洞名称</td>
                <td width="20%">{{currentRowData.name}}</td>
                <td width="90"
                    class="bold">描述</td>
                <td width="50%">{{currentRowData.describe}}</td>
                <td width="90"
                    class="bold">是否可修复</td>
                <td>{{currentRowData.canFixed === 'Y' ? '是': '否'}}</td>
            </tr>
            <tr>
                <td class="bold">危险等级</td>
                <td>
                    {{currentRowData.riskLevelDesc}}
                </td>
                <td class="bold">解决方法</td>
                <td>{{currentRowData.repairMethod}}</td>
                <td class="bold">备注</td>
                <td>{{currentRowData.remark}}</td>
            </tr>
        </table>

        <!-- 漏洞详情 -->
        <!-- <div>漏洞详情</div> -->

        <!-- 待修复主机列表 -->
        <div class="yw-el-table-wrap mtop10">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           :disabled="loading"
                           @click="batchFixBug">
                    <i class="icon iconfont f14">&#xe62b;</i>
                    批量修复
                </el-button>
            </div>
            <div class="yw-el-table-wrap"
                 style="margin-top:10px;">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          width="100%"
                          height="400"
                          v-loading="loading"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="hostIp"
                                     label="主机名称"></el-table-column>
                    <el-table-column prop="poolName"
                                     label="资源池"></el-table-column>
                    <el-table-column prop="reportTime"
                                     label="报告时间"></el-table-column>
                    <el-table-column prop="repairTime"
                                     label="修复时间"></el-table-column>
                    <el-table-column prop="repairPerson"
                                     label="修复人"></el-table-column>
                    <el-table-column label="选择作业">
                        <template slot="header">
                            <span class="required-option">选择作业</span>
                        </template>
                        <template slot-scope="scope">
                            <el-form @submit.native.prevent
                                     :model="scope.row">
                                <el-form-item prop="pipelineId">
                                    <el-select v-model="scope.row.pipelineId"
                                               :disabled="scope.row.status === 'BEYOND_REPAIR'"
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
                    <el-table-column label="操作"
                                     width="80">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       :disabled="scope.row.status === 'BEYOND_REPAIR' || !scope.row.isAgentRun"
                                       title="修复"
                                       @click="fixBug(scope.row)">
                                <i class="icon iconfont f14">&#xe62b;</i>
                            </el-button>
                            <!-- 状态：WAIT_REPAIR, BEYOND_REPAIR, PROCESSED 等待修复, 不可修复, 已处理' -->
                            <el-button type="text"
                                       :disabled="scope.row.status === 'BEYOND_REPAIR'"
                                       title="不可修复"
                                       icon="el-icon-lock"
                                       @click="handleCanFixed(scope.row)"></el-button>
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
        </div>

    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import { setObjKeys } from 'src/utils/globalUtils.js'
    export default {
        name: 'WorksChooseComponent',
        components: {
        },
        props: {
            currentRowData: {
                type: Object,
                default() {
                    return {}
                }
            },
            originWorkData: {
                type: Object,
                default() {
                    return {}
                }
            },
        },
        data() {
            return {
                dialogShow: false,
                // 主机列表
                dataList: [],
                // 已选中的设备列表
                specialVulInstanceList: [],
            }
        },
        watch: {
            multipleSelection: {
                handler(newVal) {
                    newVal.forEach(row => {
                        this.handleDeviceSelected(row.pipelineId, row)
                    })
                },
                deep: true
            }
        },
        computed: {
            // 当前漏洞已关联的作业列表
            curWorkList() {
                let arr = []
                this.originWorkData.dataList && this.originWorkData.dataList.forEach(item => {
                    this.currentRowData.pipelineIdList.forEach(pipelineId => {
                        if (item.pipeline_id === pipelineId) {
                            arr.push(item)
                        }
                    })
                })
                return arr
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search(1)
        },
        methods: {
            // 处理已选中的设备列表
            handleDeviceSelected(curPipelineId, row) {
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
            // 批量修复
            batchFixBug() {
                if (!this.curWorkList.length) {
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
                                this.showErrorTip(res)
                            }
                            this.loading = false
                        })
                        .catch(error => {
                            this.loading = false
                            this.showErrorTip(error)
                        })
                })
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
            // 查询列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                    vulnerabilityId: this.currentRowData.id,
                    historyFlag: 'N'    // Y 历史数据 N 当前周期，默认 N
                }
                this.loading = true
                bugManageService
                    .queryVulInstanceList(req)
                    .then(res => {
                        this.loading = false
                        this.dataList = res.dataList
                        this.dataList.forEach(item => {
                            setObjKeys(this, item, 'pipelineId', item.pipelineId || (this.curWorkList[0] && this.curWorkList[0].pipeline_id))
                        })
                        this.total = res.totalCount
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
