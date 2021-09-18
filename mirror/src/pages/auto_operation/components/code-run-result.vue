<template>
    <div class="components-container">
        <div>
            <el-progress :text-inside="true"
                         :stroke-width="20"
                         :percentage="percentage"
                         status="success"></el-progress>
            <table class="bordered mtop20">
                <tr>
                    <td width="90">节点名称</td>
                    <td width="20%">{{codeRunResult.block_name}}</td>
                    <td width="90">节点类型</td>
                    <td width="20%">
                        <span v-if="codeRunResult.ops_type === 0">执行脚本</span>
                        <span v-else-if="codeRunResult.ops_type === 1">文件分发</span>
                    </td>
                    <td width="90">服务器账户</td>
                    <td width="20%">{{codeRunResult.target_ops_user}}</td>
                    <td width="90">总时间(s)</td>
                    <td>{{codeRunResult.total_time}}</td>
                </tr>
                <tr>
                    <td>节点状态</td>
                    <td>
                        <status-list :status="codeRunResult.status"></status-list>
                    </td>
                    <td>开始时间</td>
                    <td>{{codeRunResult.start_time}}</td>
                    <td>结束时间</td>
                    <td>{{codeRunResult.end_time}}</td>
                    <td></td>
                    <td></td>
                </tr>
            </table>
        </div>

        <div class="mtop20">
            <el-tabs v-model="activeName"
                     type="card"
                     @tab-click="handleTabClick">
                <el-tab-pane :key="item.key"
                             v-for="item in tabList"
                             :label="item.name"
                             :name="item.key"></el-tab-pane>
            </el-tabs>
        </div>

        <div>
            <el-row :gutter="10">
                <el-col :span="22">
                    <el-input v-model="searchKey"
                              placeholder="请输入搜索内容"
                              clearable></el-input>
                </el-col>
                <el-col :span="2">
                    <el-button type="primary"
                               @click="searchLog()">搜索日志</el-button>
                </el-col>
                <!-- <el-col :span="3">
          <el-button type="primary">导出执行日志</el-button>
        </el-col>-->
            </el-row>
        </div>

        <el-form class="yw-form mtop20">
            <div class="yw-el-table-wrap">
                <el-row :gutter="20">
                    <el-col :span="10">
                        <el-form-item label="IP过滤"
                                      prop="filterKey">
                            <el-input v-model="filterKey"
                                      clearable></el-input>
                            <el-button type="primary"
                                       class="copyBtn"
                                       :data-clipboard-text="ipList"
                                       @click="copyIpList">复制IP</el-button>
                        </el-form-item>
                        <el-table ref="logTable"
                                  :data="logDataFilter"
                                  @row-click="handleRowClick"
                                  highlight-current-row
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  height="170"
                                  v-loading="loading">
                            <el-table-column min-width="140"
                                             show-overflow-tooltip
                                             prop="pool"
                                             label="资源池名称"></el-table-column>
                            <el-table-column min-width="100"
                                             prop="agent_ip"
                                             label="IP"></el-table-column>
                            <el-table-column prop="exit_code"
                                             width="60"
                                             label="返回码"></el-table-column>
                            <el-table-column label="状态">
                                <!-- 状态  9 成功   101 执行失败  102 执行超时 -->
                                <template slot-scope="scope">
                                    <status-list :status="scope.row.status"></status-list>
                                </template>
                            </el-table-column>
                            <el-table-column prop="aspnode_result"
                                             label="结果状态">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.aspnode_result === 0">正常</span>
                                    <span v-if="scope.row.aspnode_result === 1">异常</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="aspnode_msg"
                                             min-width="200"
                                             show-overflow-tooltip
                                             label="结果值"></el-table-column>
                            <el-table-column prop="isOpen"
                                             label="耗时(s)"></el-table-column>
                            <el-table-column label="操作"
                                             min-width="160">
                                <template slot-scope="scope"
                                          v-if="scope.row.ops_type === 2 && scope.row.aspnode_msg">
                                    <div class="pointer blue"
                                         v-for="(path, index) in scope.row.aspnode_msg.split(',')"
                                         :key="index"
                                         @click="downloadFile(path)">下载：{{path | getFilename}}</div>
                                </template>
                            </el-table-column>
                        </el-table>
                        <div class="yw-page-wrap">
                            <el-pagination @size-change="handleSizeChange"
                                           @current-change="handleCurrentChange"
                                           :current-page="currentPage"
                                           :page-sizes="pageSizes"
                                           :page-size="pageSize"
                                           :total="total"
                                           layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                        </div>
                    </el-col>
                    <el-col :span="14">
                        <div>完整日志
                            <el-checkbox v-model="queryStop"
                                         class="mleft20"
                                         label="暂停查询"></el-checkbox>
                        </div>
                        <div class="log-box"
                             ref="logBox"
                             v-html="opsLog"></div>
                    </el-col>
                </el-row>
            </div>
        </el-form>
    </div>
</template>

<script>
    import { createDownloadFile } from 'src/utils/utils.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    // import Clipboard from 'clipboard'
    import statusList from 'src/pages/auto_operation/components/status-list.vue'
    // let clipboard = new Clipboard('.copyBtn')
    export default {
        name: 'AutoOperationCodeRunResult',
        components: {
            statusList
        },
        props: ['stepInstId'],
        data() {
            return {
                tabList: [
                    {
                        name: '执行成功',
                        key: 'first'
                    }
                ],
                activeName: 'first', // 当前显示tab
                codeRunResult: {}, // 当前脚本执行结果
                searchKey: '', // 搜索日志关键词
                filterKey: '', // ip过滤关键词
                logData: [], // 主机执行结果数据
                currentRow: {}, // 当前高亮行数据
                percentage: 0, // 进度条进度
                ipList: '',
                queryStop: false, // 是否开启循环查询
                timer: null
            }
        },
        computed: {
            opsLog() {
                return this.currentRow && this.currentRow.ops_log
            },
            logDataFilter() {
                var search = this.filterKey
                if (search) {
                    return this.logData.filter((row) => {
                        return row['agent_ip'].includes(search)
                    })
                }
                return this.logData
            }
        },
        mixins: [rbAutoOperationMixin],
        watch: {
            // 当前脚本信息变更，更新脚本编辑框
            stepInstId: {
                handler(newVal) {
                    this.codeRunResult = {}
                    this.logData = []
                    this.queryStop = false
                    this.queryStepInstanceById(newVal)
                },
                immediate: true
            },
            queryStop(val) {
                if (!val) {
                    this.queryStatusLoop()
                }
            }
        },
        mounted() {
            this.queryStepInstanceById(this.stepInstId)
        },
        destroyed() {
            clearTimeout(this.timer)
        },
        methods: {
            downloadFile(path) {
                let arr = path.split('/')
                let filename = arr[arr.length - 1]
                let req = {
                    file_path: path,
                    is_relative: 'N'
                }
                rbAutoOperationServicesFactory
                    .downloadFile(req)
                    .then(res => {
                        if (res) {
                            this.$message.success('下载成功')
                            createDownloadFile(res, filename)
                        }
                    })
            },
            copyIpList() {
                let length = this.logDataFilter.length
                if (!length) {
                    this.$message('暂无信息可复制')
                    return
                }
                this.ipList = this.logDataFilter.map((row) => {
                    return row['agent_ip'] + '\n'
                }).toString().replace(/,/g, '')
                this.$message(`成功复制${length}条IP`)
            },
            handleTabClick(tab) {
                if (tab.label === '执行成功') {
                    this.getSuccessLog()
                } else {
                    this.getSearchLog()
                }
            },
            handleRowClick(row) {
                this.$refs.logTable.setCurrentRow(row)
                this.currentRow = row
            },
            // 根据ops步骤实例id查询实例详情
            queryStepInstanceById(stepInstId) {
                this.currentRow = {}
                this.addPercentage(50)
                rbAutoOperationServicesFactory
                    .queryStepInstanceById(stepInstId)
                    .then(res => {
                        this.codeRunResult = res
                        let status = this.codeRunResult.status
                        // 执行状态  9 执行成功 101 执行失败 102 执行超时
                        // 100 执行中  5 等待执行  6 暂停待确认
                        if (status === 100 || status === 5 || status === 6) {
                            this.queryStatusLoop()
                        } else {
                            this.getSuccessLog()
                            this.addPercentage(100)
                        }
                    })
            },
            // 查询执行状态
            queryStatusLoop() {
                if (!this.queryStop) {
                    // 清除之前的轮询
                    let time = +sessionStorage.getItem('timeStorageRunResult')
                    if (time) {
                        clearTimeout(time)
                    }
                    this.timer = setTimeout(() => {
                        this.getSuccessLog()
                        this.queryStepInstanceById(this.stepInstId)
                    }, 800)
                    // 将轮询id保存到sessionStorage
                    sessionStorage.setItem('timeStorageRunResult', this.timer)
                }
            },
            // 进度条递增完成
            addPercentage(num) {
                let _self = this
                let addPercent = setTimeout(() => {
                    _self.addPercentage(num)
                }, 200)
                this.percentage += Math.ceil(Math.random() * 40)

                if (this.percentage >= num) {
                    this.percentage = num
                    clearTimeout(addPercent)
                }
            },
            // 根据ops步骤实例查询主机执行详情列表
            queryOpsStepInstanceAgentRunResultList(req) {
                this.loading = true
                this.logData = []
                rbAutoOperationServicesFactory
                    .queryOpsStepInstanceAgentRunResultList(req)
                    .then(res => {
                        this.loading = false
                        this.logData = res.dataList
                        this.total = res.totalCount
                        if (this.logData.length) {
                            this.$refs.logTable && this.$refs.logTable.setCurrentRow(this.logData[0])
                            this.currentRow = this.logData[0]

                            this.scrollToBottom(this.$refs.logBox)
                        } else {
                            this.currentRow = {}
                        }
                    })
            },
            scrollToBottom(logBox) {
                setTimeout(() => {
                    logBox.scrollTop = logBox.scrollHeight
                }, 10)
            },
            // 查询日志
            searchLog() {
                if (!this.searchKey) {
                    this.$message('请输入搜索内容')
                    return
                }
                if (this.tabList.length < 2) {
                    this.tabList.push({
                        name: '搜索结果',
                        key: 'second'
                    })
                }
                this.activeName = 'second'
                this.getSearchLog()
            },
            // 获取自定义搜索日志
            getSearchLog() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    exit_code: '',
                    ops_log: this.searchKey,
                    status: '',
                    step_instance_id: this.codeRunResult.step_instance_id
                }
                this.queryOpsStepInstanceAgentRunResultList(req)
            },
            // 获取执行成功日志
            getSuccessLog() {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    exit_code: '',
                    ops_log: '',
                    // status: this.codeRunResult.status,
                    step_instance_id: this.codeRunResult.step_instance_id
                }
                this.queryOpsStepInstanceAgentRunResultList(req)
            },
            search() {
                if (this.activeName === 'first') {
                    this.getSuccessLog()
                } else {
                    this.searchLog()
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../assets/global.scss";

    .log-box {
        width: 100%;
        height: 235px;
        margin-top: 5px;
        padding: 5px 10px;
        overflow: scroll;
        background: #000000;
        color: #ffffff;
        white-space: pre-wrap;
    }
</style>
