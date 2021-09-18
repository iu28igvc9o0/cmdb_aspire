<!--  -->
<template>
    <div>
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
                          :data="dataList"
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
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </section>

            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addToTargetList">添加</el-button>
                <el-button @click="roleShow=false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'

    export default {
        props: ['dialogForm'],
        data() {
            return {
                loading: false,
                roleShow: false,
                total: 0,
                currentPage: 1,
                pageSize: 10,
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
                dataList: [],
                multipleSelection: [],
                pageSizes: ['10', '20', '50'],
                roleType: ''
            }
        },
        methods: {
            getSensitiveConfigList(id) {
                rbAutoOperationInstructionServicesFactory
                    .getSensitiveConfig({ sensitiveConfigId: id }).then((res) => {
                        console.log(res)
                    })

            },
            showServersDialog(type) {
                this.roleType = type
                let req = {
                    page_no: this.currentPage,  // 此处参数pageNo须为驼峰，否则报错，其他为下划线。。。
                    page_size: this.pageSize,
                    role_type: 0 // 获取功能角色
                }
                this.loading = true
                rbAutoOperationInstructionServicesFactory
                    .queryRoleList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.count
                        this.dataList = res.result
                        this.roleShow = true
                        this.$nextTick(() => {
                            this.handleRowSelect()
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
                    this.execList = this.multipleSelection
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                } else if (this.roleType === 'auditRole') {
                    this.auditList = this.multipleSelection
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                }
                this.roleShow = false
            },
            // 删除选中服务器
            clearSelectedData(index, type) {
                if (type === 'execRole') {
                    this.execList.splice(index, 1)
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                    this.multipleSelection = this.execList
                } else {
                    this.auditList.splice(index, 1)
                    this.$refs.serverTable && this.$refs.serverTable.clearSelection()
                    this.multipleSelection = this.auditList
                }
            },
            // 保存
            sendSaveWorkReq() {
                this.execList.forEach(e => {
                    this.addForm.execRoleList.push(e.uuid)
                })
                this.auditList.forEach(e => {
                    this.addForm.auditRoleList.push(e.uuid)
                })
                rbAutoOperationInstructionServicesFactory.updateSensitiveLevelById(this.addForm).then(res => {
                    if (res.flag) {
                        this.$emit('changeShow', false)
                        this.$message.success('编辑成功')
                    }
                })
            },
            // 取消
            cancelBtn() {
                this.$emit('changeShow', false)
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
                if (this.roleType === 'execRole') {
                    this.execList.forEach((item) => {
                        this.dataList.forEach((row) => {
                            if (item.name && item.name === row.name) {
                                this.toggleRow(row)
                            } else if (item.uuid && item.uuid === row.uuid) {
                                this.toggleRow(row)
                            }
                        })
                    })
                } else {
                    console.log(9999)
                    this.auditList.forEach((item) => {
                        this.dataList.forEach((row) => {
                            if (item.name && item.name === row.name) {
                                this.toggleRow(row)
                            } else if (item.uuid && item.uuid === row.uuid) {
                                this.toggleRow(row)
                            }
                        })
                    })
                }
            },
            // 改变每页条数
            handleSizeChange(val) {
                this.pageSize = val
                this.currentPage = 1
                this.showServersDialog(this.roleType)
            },
            // 跳转到第n页
            handleCurrentChange(val) {
                this.currentPage = val
                this.showServersDialog(this.roleType)
            },

        },
        mounted() {
        },
        watch: {
            'dialogForm.sensitiveConfigId': {
                immediate: true,
                handler(val) {
                    if (val) {
                        this.getSensitiveConfigList(val)
                        this.addForm.id = val
                    }
                }
            },
            'dialogForm.name': {
                immediate: true,
                handler(val) {
                    if (val) {
                        this.addForm.name = val
                    }
                }
            },
            'dialogForm.levelDesc': {
                immediate: true,
                handler(val) {
                    if (val) {
                        this.addForm.levelDesc = val
                    }
                }
            },
            'dialogForm.execRoleList': {
                immediate: true,
                handler(val) {
                    if (val) {
                        this.execList = []
                        val.forEach(e => {
                            this.execList.push(JSON.parse(e))
                        })
                    }
                }
            },
            'dialogForm.auditRoleList': {
                immediate: true,
                handler(val) {
                    console.log(val)
                    if (val) {
                        this.auditList = []
                        val.forEach(e => {
                            this.auditList.push(JSON.parse(e))
                        })
                    }
                }
            }
        }
    }

</script>
<style lang='scss' scoped>
</style>