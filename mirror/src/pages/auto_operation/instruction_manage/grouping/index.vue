<!--  -->
<template>
    <div>
        <el-form class="yw-form">
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 182px)"
                          v-loading="loading">
                    <el-table-column prop="name"
                                     label="指令级别"
                                     width="100"
                                     sortable></el-table-column>
                    <el-table-column prop="levelDesc"
                                     label="指令级别描述"
                                     min-width="140"
                                     sortable
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="createTime"
                                     label="创建时间"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column prop="updateTime"
                                     label="修改时间"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column prop="updater"
                                     label="修改人"
                                     sortable
                                     width="140"></el-table-column>
                    <el-table-column label="操作"
                                     width="120">
                        <template slot-scope="scope">
                            <div>
                                <el-button type="text"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           @click="editRow(scope.row)"></el-button>
                            </div>
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

        <el-dialog class="yw-dialog"
                   title="编辑"
                   :visible.sync="dialogForm.show"
                   :destroy-on-close="true"
                   width="1000px">
            <section class="yw-dialog-main">
                <div class="code-mirror-box">

                    <el-form class="yw-form is-required"
                             label-width="120px">
                        <el-form-item label="指令级别">
                            <el-input placeholder="请输入指令级别"
                                      v-model="addForm.name"></el-input>
                        </el-form-item>
                        <el-form-item label="指令级别描述">
                            <el-input placeholder="请输入指令级别描述"
                                      v-model="addForm.levelDesc"></el-input>
                        </el-form-item>

                        <el-form-item label="可执行系统角色">
                            <el-button type="primary"
                                       @click="showServersDialog('execRole')">选择角色</el-button>
                            <div class="yw-el-table-wrap">
                                <el-table class="yw-el-table"
                                          :data="execList"
                                          stripe
                                          tooltip-effect="dark"
                                          border
                                          v-loading="loading">
                                    <el-table-column type="index"
                                                     label="序号"
                                                     width="60"></el-table-column>
                                    <el-table-column prop="name"
                                                     label="角色名称"></el-table-column>
                                    <el-table-column label="操作"
                                                     width="60">
                                        <template slot-scope="scope">
                                            <div class="yw-table-operator">
                                                <el-button type="text"
                                                           title="删除"
                                                           icon="el-icon-delete"
                                                           @click="clearSelectedData(scope.$index,'execRole')"></el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>

                        </el-form-item>

                        <el-form-item label="执行审核赋权角色">
                            <el-button type="primary"
                                       @click="showServersDialog('auditRole')">选择角色</el-button>
                            <el-button type="primary"
                                       @click="cloneRole">拷贝增加可执行角色</el-button>
                            <div class="yw-el-table-wrap">
                                <el-table class="yw-el-table"
                                          stripe
                                          tooltip-effect="dark"
                                          :data="auditList"
                                          border
                                          v-loading="loading">
                                    <el-table-column type="index"
                                                     label="序号"
                                                     width="60"></el-table-column>
                                    <el-table-column prop="name"
                                                     label="角色名称"></el-table-column>
                                    <el-table-column label="操作"
                                                     width="60">
                                        <template slot-scope="scope">
                                            <div class="yw-table-operator">
                                                <el-button type="text"
                                                           title="删除"
                                                           icon="el-icon-delete"
                                                           @click="clearSelectedData(scope.$index,'auditRole')"></el-button>
                                            </div>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>

                        </el-form-item>
                    </el-form>
                </div>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="sendSaveWorkReq">保存</el-button>
                <el-button @click="cancelBtn">取消</el-button>
            </section>

        </el-dialog>

        <el-dialog class="yw-dialog"
                   title="选择角色"
                   :visible.sync="roleShow"
                   width="700px"
                   :close-on-click-modal="false">
            <!-- 列表 -->
            <section class="yw-dialog-main no-scroll">
                <el-table ref="serverTable"
                          :data="dataListDialog"
                          row-key="name"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="300"
                          v-loading="loading"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                     :reserve-selection="true"></el-table-column>
                    <el-table-column sortable
                                     prop="name"
                                     label="角色名称"></el-table-column>
                </el-table>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChangeDialog"
                                   @current-change="handleCurrentChangeDialog"
                                   :current-page="currentPageDialog"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSizeDialog"
                                   :total="totalDialog"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </section>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addToTargetList">添加</el-button>
                <el-button @click="cancelShow">取消</el-button>
            </section>
        </el-dialog>

    </div>
</template>

<script>
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'

    export default {
        data() {
            return {
                loading: false,
                dataList: [],
                currentPage: 1,
                pageSize: 10,
                pageSizes: ['10', '20', '50'],
                total: 0,
                dialogForm: {
                    show: false,
                    sensitiveConfigId: '',
                    name: '',
                    levelDesc: '',
                    auditRoleList: [],
                    execRoleList: []
                },
                //
                roleShow: false,
                totalDialog: 0,
                currentPageDialog: 1,
                pageSizeDialog: 10,
                addForm: {
                    id: '',
                    name: '',
                    levelDesc: '',
                    execRoleList: [],// 执行角色
                    auditRoleList: []// 审核角色
                },
                execList: [],// 执行角色
                auditList: [],// 审核角色
                serverTable: [],
                dataListDialog: [],
                multipleSelection: [],
                //  pageSizes: ['10', '20', '50'],
                roleType: ''

            }
        },
        methods: {
            getSensitiveLevelList() {
                rbAutoOperationServicesFactory.querySensitiveLevelList({
                    page_no: this.currentPage,
                    page_size: this.pageSize,

                }).then(res => {
                    this.dataList = res.dataList
                    this.total = res.totalCount
                })
            },
            editRow(row) {
                this.auditList = []
                this.execList = []
                this.addForm.id = row.id
                this.addForm.name = row.name
                this.addForm.levelDesc = row.levelDesc
                row.execRoleList.forEach(e => {
                    this.execList.push(JSON.parse(e))
                })
                row.auditRoleList.forEach(e => {
                    this.auditList.push(JSON.parse(e))
                })
                this.dialogForm.show = true
            },
            // 改变每页条数
            handleSizeChange(val) {
                this.pageSize = val
                this.currentPage = 1
                this.getSensitiveLevelList()
            },
            // 跳转到第n页
            handleCurrentChange(val) {
                this.currentPage = val
                this.getSensitiveLevelList()
            },
            // 弹窗改变每页条数
            handleSizeChangeDialog(val) {
                this.pageSizeDialog = val
                this.currentPageDialog = 1
                this.showServersDialog(this.roleType)
            },
            // 弹窗跳转到第n页
            handleCurrentChangeDialog(val) {
                this.currentPageDialog = val
                this.showServersDialog(this.roleType)
            },
            showServersDialog(type) {
                this.roleType = type
                let req = {
                    page_no: this.currentPageDialog,  // 此处参数pageNo须为驼峰，否则报错，其他为下划线。。。
                    page_size: this.pageSizeDialog,
                    role_type: 0 // 获取功能角色
                }
                this.loading = true
                rbAutoOperationInstructionServicesFactory
                    .queryRoleList(req)
                    .then(res => {
                        this.dataListDialog = []
                        this.loading = false
                        this.totalDialog = res.count
                        this.dataListDialog = res.result
                        this.roleShow = true
                        this.$nextTick(() => {
                            // this.handleRowSelect()
                        })
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 拷贝增加可执行角色
            cloneRole() {
                if (this.execList.length) {
                    this.auditList = JSON.parse(
                        JSON.stringify(this.execList)
                    )
                    this.$message.success('拷贝成功！')
                } else {
                    this.$message.warning('请先选择可执行系统角色！')
                }
            },
            // 添加选中服务器/关闭弹框
            addToTargetList() {
                if (this.roleType === 'execRole') {
                    let newArr = []
                    newArr = this.execList.concat(this.multipleSelection)
                    const map = new Map()
                    this.execList = newArr.filter(e => !map.has(e.name) && map.set(e.name, 1))
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                } else if (this.roleType === 'auditRole') {
                    let newArr = []
                    newArr = this.auditList.concat(this.multipleSelection)
                    const map = new Map()
                    this.auditList = newArr.filter(e => !map.has(e.name) && map.set(e.name, 1))
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                }
                this.roleShow = false
            },
            // 删除选中服务器
            clearSelectedData(index, type) {
                if (type === 'execRole') {
                    this.execList.splice(index, 1)
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                } else {
                    this.auditList.splice(index, 1)
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                }
            },
            // 保存
            sendSaveWorkReq() {
                this.addForm.execRoleList = []
                this.addForm.auditRoleList = []
                this.execList.forEach(e => {
                    this.addForm.execRoleList.push(e.uuid)
                })
                this.auditList.forEach(e => {
                    this.addForm.auditRoleList.push(e.uuid)
                })
                rbAutoOperationInstructionServicesFactory.updateSensitiveLevelById(this.addForm).then(res => {
                    if (res.flag) {
                        this.dialogForm.show = false
                        this.getSensitiveLevelList()
                        this.$message.success('编辑成功')
                    }
                })
            },
            // 取消
            cancelBtn() {
                this.dialogForm.show = false
            },
            cancelShow() {
                this.multipleSelection = []
                this.roleShow = false
            },
            // 复选框 已勾选数据
            handleSelectionChange(val) {
                this.multipleSelection = val

            },
            // 选中表格行
            toggleRow(row) {
                this.$refs.serverTable && this.$refs.serverTable.toggleRowSelection(row, true)
            },
            // 判断是否需要勾选
            handleRowSelect() {
                console.log('this.dataListDialog===', this.dataListDialog)
                if (this.roleType === 'execRole') {
                    this.execList.forEach((item) => {
                        this.dataListDialog.forEach((row) => {
                            if (item.name && item.name === row.name) {
                                this.toggleRow(row)
                            } else if (item.uuid && item.uuid === row.uuid) {
                                this.toggleRow(row)
                            }
                        })
                    })
                } else {
                    this.auditList.forEach((item) => {
                        this.dataListDialog.forEach((row) => {
                            if (item.name && item.name === row.name) {
                                this.toggleRow(row)
                            } else if (item.uuid && item.uuid === row.uuid) {
                                this.toggleRow(row)
                            }
                        })
                    })
                }
            },
        },
        created() {
            this.getSensitiveLevelList()
        }
    }

</script>
<style lang='scss' scoped>
</style>