<template>
    <!-- 服务台： 工单列表组件 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div>{{orderTypeNames[orderType]}}</div>
                    <!-- tab -->
                    <tableTabs :tabList="tabList"
                               :activeIndex="activeIndex"
                               @handleTabClick="handleTabClick"></tableTabs>
                </div>
            </div>
            <div class="chart-filter">
                <el-input v-model="searchWord"
                          clearable
                          placeholder="请输入流程标题">
                    <i slot="suffix"
                       class="el-input__icon el-icon-search"
                       @click="search"></i>
                </el-input>
                <el-button type="primary"
                           size="small"
                           v-if="orderType === 'myList'"
                           class="mleft20"
                           @click="dialogBoxShow=true">
                    <i class="icon iconfont f14">&#xe6a1;</i>催办
                </el-button>
                <el-button type="text"
                           class="mleft20"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <div class="chart-section p10">
            <div class="yw-el-table-wrap hp100 wp100">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          border
                          tooltip-effect="dark"
                          height="calc(100% - 32px)"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column type="selection"
                                     v-if="orderType === 'myList'"
                                     :selectable="selectable"
                                     width="40"></el-table-column>
                    <el-table-column prop="bpmnInstId"
                                     label="工单号"
                                     show-overflow-tooltip
                                     width="180">
                        <template slot-scope="scope">
                            <!-- 待办  工单号：procInstId；调整为id-20201119
                                 已办、我发起的 工单号：id -->
                            {{scope.row.id}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="procDefName"
                                     label="流程名称"
                                     show-overflow-tooltip
                                     min-width="90"></el-table-column>
                    <el-table-column prop="subject"
                                     label="流程标题"
                                     show-overflow-tooltip
                                     min-width="160">
                        <template slot-scope="scope">
                            <span class="blue pointer"
                                  @click="gotoDetail(scope.row)">{{scope.row.subject}}</span>
                        </template>
                    </el-table-column>
                    <!-- <el-table-column prop="operator"
                                     label="流程分类"
                                     width="120"></el-table-column> -->
                    <el-table-column prop="createTime"
                                     label="发起时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="preNodeOpinion"
                                     label="审批意见"
                                     width="140"></el-table-column>
                    <el-table-column label="当前环节"
                                     width="120">
                        <template slot-scope="scope">
                            <!-- 待办  当前环节：name；调整为taskName-20201119
                                已办、我发起的 当前环节：taskName -->
                            {{scope.row.taskName}}
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
                                   small
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </div>
        </div>
        <el-dialog class="yw-dialog"
                   v-if="dialogBoxShow"
                   title="催办"
                   :visible.sync="dialogBoxShow"
                   :close-on-click-modal="false"
                   :destroy-on-close="true"
                   width="540px">
            <section class="yw-dialog-main middle-size-form">
                <el-form class="yw-form is-required"
                         ref="addForm"
                         :model="addForm"
                         :rules="addFormRules"
                         label-width="">
                    <el-form-item label=""
                                  prop="content">
                        <el-input type="textarea"
                                  rows="4"
                                  v-model="addForm.content"
                                  placeholder="请输入催办内容"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="reminderOrder">确定</el-button>
                <el-button @click="dialogBoxShow=false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import tableTabs from './table-tabs.vue'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        props: {
            orderType: {
                type: String,
                default: 'all'
            }
        },
        components: {
            tableTabs
        },
        data() {
            return {
                orderTypeNames: {
                    all: '我的工单',
                    myList: '我发起的工单',
                    todoList: '待办事项',
                },
                searchWord: '',
                activeName: '',
                activeIndex: 0,
                tabList: [
                    {
                        name: '处理中',
                        type: 3,
                        status: 'running',
                        orderType: 'myList',
                        count: '-'
                    },
                    {
                        name: '已关闭',
                        type: 3,
                        status: 'end',
                        orderType: 'myList',
                        count: '-'
                    },
                    {
                        name: '全部',
                        type: 3,
                        status: '',
                        orderType: 'myList',
                        count: '-'
                    },
                    {
                        name: '待办',
                        type: 1,
                        orderType: 'todoList',
                        count: '-'
                    },
                    {
                        name: '已办',
                        type: 2,
                        orderType: 'todoList',
                        count: '-'
                    },
                ],
                dataList: [], // 表格数据
                dialogBoxShow: false,
                addForm: {
                    content: ''
                },
                addFormRules: {
                    content: [
                        {
                            required: true,
                            message: '请输入催办内容!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 300,
                            message: '长度在 2 到 300 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ]
                }
            }
        },
        watch: {
            orderType: {
                handler(val) {
                    if (val !== 'all') {
                        this.tabList = this.tabList.filter(tab => {
                            return tab.orderType === val
                        })
                    } else {
                        return this.tabList
                    }
                    this.activeName = this.tabList[0].key
                },
                immediate: true
            }
        },
        computed: {
            curTab() {
                return this.tabList[this.activeIndex]
            }
        },
        mixins: [rbAutoOperationMixin],
        mounted() {
            this.search()
        },
        methods: {
            selectable(row) {
                return row.status === 'running'
            },
            // 获取列表数据 
            search() {
                let req = {
                    pageNo: this.currentPage,   // 获取总数的时候不传
                    pageSize: this.pageSize,    // 获取总数的时候不传
                    subject: this.searchWord,   // 标题
                    type: this.curTab.type, // 1待办 2已办 3我发起的
                    status: this.curTab.status,  // running处理中  end关闭；获取全部数据不传。
                    cntFlag: 'Y',    // Y -- 返回总数
                }
                this.loading = true
                rbBpmHomeServices
                    .getOrderList(req)
                    .then(res => {
                        this.loading = false
                        // 设置待办/处理中工单的数量
                        this.total = this.curTab.count = res.page.total
                        this.dataList = res.page.rows

                        // 设置已办工单的数量
                        if (this.curTab.type === 1) {
                            this.tabList[1].count = res.doneCnt

                            // 设置已关闭、全部工单的数量
                        } else if (this.curTab.status === 'running') {
                            this.tabList[1].count = res.myRequestEnd
                            this.tabList[2].count = res.myRequestTotal
                        }
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 催办
            reminderOrder() {
                this.$refs.addForm.validate((valid) => {
                    if (!valid) {
                        return
                    }
                    let arr = []
                    this.multipleSelection.forEach(item => {
                        let obj = {
                            procDefKey: '',
                            procInstId: ''
                        }
                        obj.procDefKey = item.procDefKey
                        obj.procInstId = item.id
                        arr.push(obj)
                    })
                    let req = {
                        content: this.addForm.content,
                        procInstVoList: arr,
                        type: 1,
                    }
                    rbBpmHomeServices
                        .reminderOrder(req)
                        .then(res => {
                            if (res.state) {
                                this.$message.success(res.message)
                            } else {
                                this.$message.error(res.message)
                            }
                            this.dialogBoxShow = false
                        })
                })
            },
            handleTabClick(index) {
                this.activeIndex = index
                this.currentPage = 1
                console.log(index)
                this.search()
            },
            // 更多我的工单
            gotoMore() {
                let routerHash = ''
                if (this.orderType === 'myList') {
                    routerHash = 'flow/request'
                } else if (this.curTab.type === 2) {
                    routerHash = 'flow/done'
                } else {
                    routerHash = 'flow/todoList'
                }
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: this.curTab.type === 2 ? '已办事项' : this.orderTypeNames[this.orderType]
                    }
                })
            },
            gotoDetail(row) {
                // 待办 task/2009241132348055991  
                // 已办、我发起的 inst/2009241132348055991
                let routerHash = ''
                if (this.curTab.type === 1) {
                    routerHash = 'task/' + row.taskId
                } else {
                    routerHash = 'inst/' + row.id
                }
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: routerHash,
                        currentTitle: row.subject
                    }
                })
            },

        }

    }
</script>

<style lang="scss" scoped>
</style>

