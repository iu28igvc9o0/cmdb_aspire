
<template>
    <!-- 变更计划 -->
    <div class="components-container yw-dashboard"
         :class="{'show-list-view' : listViewShow}"
         v-loading.fullscreen.lock="loading"
         :element-loading-text="loading_text">
        <div class="wp100 displaybox boxalignstretch">
            <!-- 主视图 -->
            <div class="plan-box boxflex01 relative screen-height">
                <div class="door-plan-btn absolute clearfix">
                    <el-button type="primary"
                               icon="el-icon-plus"
                               @click="showBox('创建任务')">创建任务</el-button>
                    <el-dropdown trigger="click"
                                 ref="filterBox">
                        <el-button @click="showDropdownBox('filterBox')">
                            <i class="icon iconfont">&#xe68c;</i>
                            筛选
                        </el-button>
                        <!-- 筛选菜单 -->
                        <div>
                            <el-dropdown-menu slot="dropdown">
                                <div class="pright20">
                                    <el-dropdown-item disabled>执行状态</el-dropdown-item>
                                    <el-checkbox-group class="checkbox-hide"
                                                       v-model="selectedStatus">
                                        <el-checkbox v-for="item in statusList"
                                                     :label="item.value"
                                                     :disabled="item.disabled"
                                                     :key="item.value">{{item.name}}</el-checkbox>
                                    </el-checkbox-group>
                                    <el-dropdown-item class="mtop10"
                                                      disabled>执行结果</el-dropdown-item>
                                    <el-checkbox-group class="checkbox-hide"
                                                       v-model="selectedResult">
                                        <el-checkbox v-for="item in resultList"
                                                     :label="item.value"
                                                     :disabled="item.disabled"
                                                     :key="item.value">{{item.name}}</el-checkbox>
                                    </el-checkbox-group>
                                    <el-dropdown-item class="mtop10"
                                                      disabled>执行反馈</el-dropdown-item>
                                    <el-checkbox-group class="checkbox-hide"
                                                       v-model="selectedMsg">
                                        <el-checkbox v-for="item in msgList"
                                                     :label="item.value"
                                                     :disabled="item.disabled"
                                                     :key="item.value">{{item.name}}</el-checkbox>
                                    </el-checkbox-group>

                                    <section class="btn-wrap mtop20 t-right mleft20 border-top">
                                        <el-button @click="resetFilter">重置</el-button>
                                        <el-dropdown-item class="filter-box">
                                            <el-button type="primary"
                                                       @click="handleSearch">
                                                确定
                                            </el-button>
                                        </el-dropdown-item>
                                    </section>
                                </div>
                            </el-dropdown-menu>
                        </div>
                    </el-dropdown>
                    <!-- 该版本暂时不做通知功能 -->
                    <!-- <el-button icon="el-icon-bell"
                               @click="showBox('创建任务')">通知</el-button> -->
                    <el-button @click="importShow = true">
                        <i class="icon iconfont">&#xe68a;</i>
                        导入
                    </el-button>
                    <el-dropdown trigger="click"
                                 :hide-on-click="false"
                                 ref="exportBox">
                        <el-button @click="showDropdownBox('exportBox')">
                            <i class="icon iconfont">&#xe689;</i>
                            导出
                        </el-button>
                        <!-- 导出 -->
                        <div>
                            <el-dropdown-menu slot="dropdown">
                                <div class="mleft20 pright20 mtop10">
                                    <div>请选择导出的时间范围</div>
                                    <el-date-picker v-model="exportTime"
                                                    @change="showDropdownBox('exportBox')"
                                                    class="mtop10"
                                                    type="datetimerange"
                                                    range-separator="至"
                                                    start-placeholder="开始时间"
                                                    end-placeholder="结束时间">
                                    </el-date-picker>

                                    <section class="btn-wrap mtop10 t-right">
                                        <el-button @click="resetTime">重置</el-button>
                                        <el-dropdown-item command="close"
                                                          class="filter-box">
                                            <el-button type="primary"
                                                       @click="handleExport">
                                                导出
                                            </el-button>
                                        </el-dropdown-item>
                                    </section>
                                </div>
                            </el-dropdown-menu>
                        </div>
                    </el-dropdown>
                </div>
                <!-- 月视图、周视图 -->
                <plan-full-calendar @handleTaskClick="handleTaskClick"
                                    @handleDateClick="handleDateClick"
                                    @showListView="showListView"
                                    @search="search"
                                    :dataList="taskList"></plan-full-calendar>
                <!-- 自定义视图 表格 -->
                <list-view :dataList="taskList"
                           :total="total"
                           :loading="loading"
                           v-if="listViewShow"
                           @handleTaskClick="handleTaskClick"
                           @search="search"></list-view>
            </div>

            <!-- 右侧框 -->
            <div class="content-box box-shadow mleft10 screen-height"
                 v-show="boxShow">
                <!-- 右侧弹框名称 -->
                <div class="box-title">
                    {{boxTitle}}
                    <el-button type="text"
                               title="关闭"
                               class="f-right"
                               icon="el-icon-close"
                               @click="closeBox"></el-button>
                </div>
                <!-- 创建、编辑 -->
                <task-edit @closeBox="closeBox"
                           @search="search"
                           @showTaskDetail="handleTaskClick"
                           v-if="taskEditShow"
                           :startTime="startTime"
                           @startTask="startTask"
                           :taskDetailData="taskDetailData"
                           :boxTitle="boxTitle"
                           :uuid="curTaskId"></task-edit>
                <!-- 查看任务 -->
                <task-detail @closeBox="closeBox"
                             @search="search"
                             :status3="status3"
                             v-show="taskDetailShow"
                             :taskDetailData="taskDetailData"
                             @editTask="editTask"
                             @startExecution="startExecution"
                             :endExecutionSuccessfulnotice='endExecutionSuccessfulnotice'
                             :uuid="curTaskId"
                             @confirmExecutionR="confirmExecutionR"
                             @endTaskConfirm="endTaskConfirm"></task-detail>
                <!-- 结束执行 -->
                <end-execution @closeBox="closeBox"
                               @search="search"
                               :uuid="curTaskId"
                               v-show="endExecutionShow"
                               @showTaskDetail='handleTaskClick'
                               @endTaskBsuuList='endTaskBsuuList'
                               :taskDetailData="taskDetailData"
                               @endExecutionSuccessful="endExecutionSuccessful"></end-execution>
                <!-- 导入 -->
                <YwImport v-if="importShow"
                          :showImport="importShow"
                          @setImportDisplay="setImportDisplay"
                          @downloadTaskTemplate="downloadTaskTemplate"
                          :importType="importType"></YwImport>
            </div>
        </div>
    </div>
</template>
 
<script>
    import moment from 'moment'
    import planFullCalendar from './plan-full-calendar'
    import taskEdit from './task-edit'
    import taskDetail from './task-detail'
    import EndExecution from './end-execution'
    import listView from './list-view'
    import YwImport from 'src/components/common/yw-import.vue'
    import rbPlanServicesFactory from 'src/services/door/rb-plan-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        name: 'DoorPlanList',
        components: {
            planFullCalendar,
            taskEdit,
            taskDetail,
            EndExecution,
            listView,
            YwImport,
        },
        data() {
            return {
                importShow: false,
                importType: 'task_list_template',
                status3: false,
                endExecutionSuccessfulnotice: {},
                filterShow: false,
                exportShow: false,
                exportTime: [],
                selectedStatus: [],
                statusList: [
                    {
                        name: '全部',
                        value: '1,2,3,4',
                        disabled: false
                    },
                    {
                        name: '待执行',
                        value: '1',
                        disabled: false
                    },
                    {
                        name: '执行中',
                        value: '2',
                        disabled: false
                    },
                    {
                        name: '已执行',
                        value: '3,4',
                        disabled: false
                    },
                ],
                selectedResult: [],
                resultList: [
                    {
                        name: '全部',
                        value: '1,2',
                        disabled: false
                    },
                    {
                        name: '执行成功',
                        value: '1',
                        disabled: false
                    },
                    {
                        name: '执行失败',
                        value: '2',
                        disabled: false
                    },
                ],
                selectedMsg: [],
                msgList: [
                    {
                        name: '全部',
                        value: '1,2',
                        disabled: false
                    },
                    {
                        name: '有反馈',
                        value: '1',
                        disabled: false
                    },
                    {
                        name: '无反馈',
                        value: '2',
                        disabled: false
                    },
                ],
                boxTitle: '创建任务',
                boxTitleStart: '',
                boxShow: false,
                taskEditShow: false,
                taskDetailShow: false,
                endExecutionShow: false,
                taskDetailData: {},
                listViewShow: false,
                taskList: [],
                total: 0,
                formSearch: {
                    taskStatus: '',
                    taskResult: '',
                    taskMassage: '',
                    viewType: '',
                },
                viewSearch: {
                    startDateTime: '',
                    endDateTime: ''
                },
                pageSearch: {
                    pageNum: 1,
                    pageSize: 50,
                },
                curTaskId: '',
                startTime: '',
            }
        },
        watch: {
            selectedStatus(val) {
                let allValue = '1,2,3,4'
                if (val.includes('1') || val.includes('2') || val.includes(allValue)) {
                    this.setResultListDisabled(true)
                    this.selectedResult = []
                } else {
                    this.setResultListDisabled(false)
                }
                this.handleFilterSelect('1,2,3,4', val, 'selectedStatus')
            },
            selectedResult(val) {
                this.handleFilterSelect('1,2', val, 'selectedResult')
            },
            selectedMsg(val) {
                this.handleFilterSelect('1,2', val, 'selectedMsg')
            },
        },
        created() {
        },
        mixins: [rbAutoOperationMixin],
        methods: {
            // 所有选项是复选，其中全部与其他选项是互斥
            handleFilterSelect(allValue, selectedList, arrKey) {
                if (selectedList.length > 1 && selectedList.indexOf(allValue) > 0) {
                    this[arrKey] = [allValue]
                } else if (selectedList.length > 1 && selectedList.indexOf(allValue) === 0) {
                    this[arrKey].splice(0, 1)
                }
            },
            // 条件过滤搜索
            handleSearch() {
                this.formSearch.taskStatus = this.selectedStatus.toString()
                this.formSearch.taskResult = this.selectedResult.toString()
                this.formSearch.taskMassage = this.selectedMsg.toString()
                this.search()
            },
            handleTimeFormat(timeStamp) {
                return moment(timeStamp).format('YYYY-MM-DD HH:mm:ss')
            },
            // 关闭导入弹框
            setImportDisplay(val) {
                this.importShow = val
                this.search()
            },
            // 导入
            downloadTaskTemplate() {
                this.loading = true
                rbPlanServicesFactory
                    .downloadTaskTemplate()
                    .then(res => {
                        this.loading = false
                        this.$message.success('下载成功')
                        this.createDownloadFile(res, '变更计划数据列表模版.xlsx')
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 打开过滤、导出弹框
            showDropdownBox(name) {
                this.$refs[name].show()
            },
            // 重置时间
            resetTime() {
                this.exportTime = []
            },
            // 导出
            handleExport() {
                if (!this.exportTime.length) {
                    this.$message.warning('请选择导出时间')
                    return
                }
                let req = {
                    startTime: this.handleTimeFormat(this.exportTime[0]),
                    endTime: this.handleTimeFormat(this.exportTime[1])
                }
                this.loading = true
                rbPlanServicesFactory
                    .exportTask(req)
                    .then(res => {
                        this.loading = false
                        this.$message.success('导出成功')
                        this.createDownloadFile(res, '变更计划数据列表.xlsx')
                        this.$refs.exportBox.hide()
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            createDownloadFile(res, filename) {
                let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                let objectUrl = URL.createObjectURL(blob)
                let downLoadElement = document.createElement('a')
                downLoadElement.href = objectUrl
                downLoadElement.download = filename
                document.body.appendChild(downLoadElement)
                downLoadElement.click()
                document.body.removeChild(downLoadElement)
                URL.revokeObjectURL(objectUrl)
            },
            // 如果当前选择待执行和执行中，执行结果选项置灰不可用
            setResultListDisabled(status) {
                this.resultList.forEach(item => {
                    item.disabled = status
                })
            },
            // 重置条件
            resetFilter() {
                this.selectedStatus = []
                this.selectedResult = []
                this.selectedMsg = []
            },

            // 最近任务执行记录  viewType ： 1-月视图 2-周视图 3-列表视图
            search(viewType, req = {}) {
                // 切换月/周视图，缓存当前传入时间参数，新增后更新当前时间的数据
                if (req.startDateTime) {
                    this.viewSearch = this.$utils.deepClone(req)
                }
                // 缓存当前翻页参数
                if (req.pageNum) {
                    this.pageSearch = this.$utils.deepClone(req)
                }
                // 缓存当前视图
                if (viewType) {
                    this.formSearch.viewType = viewType
                }
                // 切换月/周视图，传入时间参数；table视图，传入翻页参数
                if (JSON.stringify(req) !== '{}') {
                    req = Object.assign(req, this.formSearch)
                } else {
                    req = Object.assign(this.formSearch, this.pageSearch)
                }
                // 非列表视图，加上时间参数；列表视图，去除时间参数
                if (req.viewType !== '3') {
                    this.listViewShow = false
                    req = Object.assign(req, this.viewSearch)
                } else {
                    req.startDateTime = ''
                    req.endDateTime = ''
                }
                this.loading = true
                this.getTaskList(req)
            },
            // 获取任务列表
            getTaskList(req) {
                this.taskList = []
                rbPlanServicesFactory
                    .getTaskList(req)
                    .then(res => {
                        this.taskList = res.result.result || res.result
                        this.total = res.result.count || 0
                        this.loading = false
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 创建、编辑任务
            showBox(titleText) {
                this.boxShow = true
                this.taskEditShow = true
                this.taskDetailShow = false
                this.endExecutionShow = false
                this.boxTitle = titleText
                this.taskDetailData = {}
                this.startTime = ''
            },
            // 创建任务编辑
            editTask(taskData) {
                this.showBox('编辑任务')
                this.taskDetailData = taskData
            },
            // 点击空白处新增
            handleDateClick(time) {
                this.showBox('创建任务')
                this.startTime = time
            },
            // 关闭右侧容器
            closeBox() {
                this.boxShow = false
            },
            // 点击任务，查看详情
            handleTaskClick(taskData) {
                this.taskDetailData = taskData
                this.boxShow = true
                this.taskEditShow = false
                this.endExecutionShow = false
                this.taskDetailShow = true
                this.boxTitle = '查看任务'
            },
            // 开始执行
            startExecution(taskId) {
                this.boxTitle = '开始执行'
                this.boxTitleStart = '开始执行'
                this.taskEditShow = true
                this.endExecutionShow = false
                this.taskDetailShow = false
                this.curTaskId = taskId
            },
            // 结束执行
            endTaskConfirm(taskData) {
                this.endExecutionShow = true
                this.taskEditShow = false
                this.taskDetailShow = false
                this.taskDetailData = taskData
                this.boxTitle = '结束执行'
            },
            // 执行完成后，更新列表，跳转到详情界面
            startTask(taskData) {
                this.taskDetailShow = true
                this.taskEditShow = false
                this.endExecutionShow = false
                this.taskDetailData = taskData
                this.boxTitle = '查看任务'
                this.search()

            },
            // 结束执行成功 跳转查看任务页面参数
            endExecutionSuccessful(data) {
                this.endExecutionShow = false
                this.taskEditShow = false
                this.taskDetailShow = true
                this.boxTitle = '查看任务'
                this.endExecutionSuccessfulnotice = data.endList
            },
            // 确认执行结果
            confirmExecutionR(data) {
                this.taskDetailData = data
                this.boxTitle = '确认执行结果'
                this.endExecutionShow = true
                this.taskEditShow = false
                this.taskDetailShow = false
            },
            // 确认执行结果   确认
            endTaskBsuuList(data) {
                this.boxTitle = '查看任务'
                this.endExecutionShow = false
                this.taskEditShow = false
                this.taskDetailShow = true
                this.taskDetailData = data
                this.status3 = true
            },
            // 切换列表视图
            showListView() {
                let req = {
                    pageNum: 1,
                    pageSize: 50,
                }
                this.listViewShow = true
                this.search('3', req)
            },
        },
    }
</script>
 
<style  lang="scss" scoped>
    .door-plan-btn {
        top: 3px;
        right: 0;
    }
    .box-title {
        padding: 10px;
        font-size: 12px;
        font-weight: bold;
        background: linear-gradient(to bottom, #e0e8ed, #f3f6f8);
    }
    .screen-height {
        height: calc(100vh - 140px);
        overflow-y: scroll;
    }
    .content-box {
        width: rem(392);
    }
    .box-shadow {
        box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
    }
    .title {
        display: inline;
    }
</style>
