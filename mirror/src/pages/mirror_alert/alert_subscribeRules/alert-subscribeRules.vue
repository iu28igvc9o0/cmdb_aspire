<!-- 通知订阅规则管理 -->
<template>
    <div>
        <div class="components-container yw-dashboard">
            <el-form class="yw-form components-condition"
                     :inline="true"
                     label-width="65px">
                <el-form-item label="规则名称">
                    <el-input v-model="dataParmas.subscribeRules"></el-input>
                </el-form-item>
                <el-form-item label="启停状态">
                    <el-select v-model="dataParmas.isOpen"
                               placeholder="请选择">
                        <el-option label="启用"
                                   value="启用"></el-option>
                        <el-option label="关闭"
                                   value="关闭"></el-option>
                    </el-select>
                </el-form-item>
                <section class="btn-wrap">
                    <el-button type="primary"
                               @click="goQuery">查询</el-button>
                    <el-button @click="reset">重置</el-button>
                </section>
            </el-form>

            <el-form class="yw-form">
                <div class="table-operate-wrap clearfix">
                    <el-button type="text"
                               icon="fa fa-unlock-alt"
                               @click="openBtn">启用</el-button>
                    <el-button type="text"
                               icon="fa fa-lock"
                               @click="goClose">关闭</el-button>
                    <el-button type="text"
                               icon="el-icon-delete"
                               @click="delBtn">删除</el-button>
                </div>
                <div class="yw-el-table-wrap">
                    <el-table class="yw-el-table"
                              style="cursor:pointer;height: calc(100vh - 276px) !important;"
                              :data="subscribeData"
                              stripe
                              tooltip-effect="dark"
                              border
                              @selection-change="changeCheckbox"
                              size="samll"
                              v-loading="loading">
                        <el-table-column type="selection"
                                         :selectable="checkSelectable"
                                         width="40px"></el-table-column>
                        <el-table-column prop="subscribeRules"
                                         label="规则名称"
                                         sortable></el-table-column>
                        <el-table-column prop="notifyType"
                                         label="通知方式"></el-table-column>
                        <el-table-column prop="isOpen"
                                         label="启用状态"></el-table-column>
                        <el-table-column prop="count"
                                         label="订阅告警"
                                         sortable></el-table-column>
                        <el-table-column prop="curSendTime"
                                         label="最新发送时间"
                                         sortable></el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <template v-if="scope.row.defensetor">
                                    <el-button type="text"
                                               icon="el-icon-edit"
                                               title="编辑"
                                               :disabled="scope.row.defensetor.indexOf(myUser) === -1"
                                               @click="editBtn(scope.row.id)"></el-button>
                                    <template v-if="scope.row.isOpen==='关闭'">
                                        <el-button type="text"
                                                   icon="fa fa-unlock-alt"
                                                   title="启用"
                                                   :disabled="scope.row.defensetor.indexOf(myUser) === -1"
                                                   @click="closeBtn(scope.row)"></el-button>
                                    </template>
                                    <template v-else>
                                        <el-button type="text"
                                                   icon="fa fa-lock"
                                                   title="关闭"
                                                   :disabled="scope.row.defensetor.indexOf(myUser) === -1"
                                                   @click="closeBtn(scope.row)"></el-button>
                                    </template>
                                </template>

                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page.sync="dataParmas.pageNo"
                                   :page-sizes="[10, 20, 50]"
                                   :page-size="dataParmas.pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper">
                    </el-pagination>
                </div>
            </el-form>
        </div>
        <alertSubscribeRulesAdd :addDialog="addDialog"
                                @closeDialog="closeDialog"
                                v-if="addDialog.dialogVisible"></alertSubscribeRulesAdd>

    </div>
</template>

<script>
    import subscribeFactory from 'src/services/alert/rb-alert-subscribe-service.factory.js'
    export default {
        data() {
            return {
                myUser: sessionStorage.username,
                subscribeData: [],  // 列表数据
                loading: false,// 数据是否加载
                currentPage: 1, // 显示第几页
                pageSizes: [20, 50, 100], // 设置每页多少行数组
                pageSize: 10, // 每页条数
                total: 0, // 总过几条数据
                addRules: false,// 新增弹窗显示
                addDialog: {// 用户列表dialog
                    dialogVisible: false,
                    data: {} // 数据
                },
                dataParmas: {// 请求列表参数
                    subscribeRules: '',// 告警配置名称
                    isOpen: '',// 开关
                    pageSize: 10,
                    pageNo: 1
                },
                idlist: [],// 开关，删除
                addSubmitDisabled: false,
                multipleSelection: []
            }
        },
        components: {
            alertSubscribeRulesAdd: () => import('./components/alert-subscribeRules-add.vue'),
            // alertSubscribeRulesEdit: () => import('./components/alert-subscribeRules-edit.vue'),
        },
        methods: {
            // 获取列表数据
            getTableData() {
                let dataParmas = this.dataParmas
                subscribeFactory.getSubscribeRulesData(dataParmas).then(res => {
                    let _this = this
                    _this.total = res.count
                    _this.subscribeData = res.result
                    _this.loading = false
                    console.log('_this.subscribeData===', _this.subscribeData)
                })
            },
            // 查询
            goQuery() {
                this.getTableData()
            },
            // 重置
            reset() {
                this.dataParmas.isOpen = ''
                this.dataParmas.subscribeRules = ''
            },
            // 开启按钮
            openBtn() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择规则名称', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let arr = []
                    this.multipleSelection.forEach((item) => {
                        arr.push(item.id)
                    })
                    let req = {}
                    req.ids = arr.join(',')
                    req.isOpen = 1
                    subscribeFactory.updataSubscribeService(req).then(res => {
                        if (res === 'success') {
                            this.getTableData()
                        }
                    })
                }
            },
            // 批量关闭按钮
            goClose() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择规则名称', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let arr = []
                    this.multipleSelection.forEach((item) => {
                        arr.push(item.id)
                    })
                    let req = {}
                    req.ids = arr.join(',')
                    req.isOpen = 0
                    subscribeFactory.updataSubscribeService(req).then(res => {
                        if (res === 'success') {
                            this.getTableData()
                        }
                    })
                }
            },
            // 单独关闭
            closeBtn(row) {
                console.log(row)
                let req = {}
                req.ids = row.id
                if (row.isOpen === '关闭') {
                    req.isOpen = 1
                } else {
                    req.isOpen = 0
                }

                subscribeFactory.updataSubscribeService(req).then(res => {
                    if (res === 'success') {
                        this.getTableData()
                    }
                })
            },
            // 删除按钮
            delBtn() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择规则名称', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let arr = []
                    this.multipleSelection.forEach((item) => {
                        arr.push(item.id)
                    })
                    let req = {}
                    req.ids = arr.join(',')
                    subscribeFactory.deteleSubscribeService(req).then(res => {
                        if (res === 'success') {
                            this.getTableData()
                        }
                    })
                }
            },
            // 编辑按钮
            editBtn(id) {
                this.addDialog.data = { 'id': id }
                this.addDialog.dialogVisible = true
            },
            // 关闭弹窗
            closeDialog(type) {
                this.addDialog.dialogVisible = false
                if (type === 'update') {
                    this.query()
                }
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.dataParmas.pageSize = val
                this.getTableData()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.dataParmas.pageNo = val
                this.getTableData()
            },
            changeCheckbox(val) {
                this.multipleSelection = val
            },
            checkSelectable(row) {
                if (row.defensetor) {
                    return row.defensetor.indexOf(sessionStorage.username) !== -1

                }
            }

        },
        created() {
            this.getTableData()
            console.log(sessionStorage.username)
        },
        watch: {
            'addDialog.dialogVisible'(val) {
                if (val == false) {
                    this.getTableData()
                }
            }
        }
    }

</script>
<style lang='scss' scoped>
</style>