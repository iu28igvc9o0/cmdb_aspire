<!--  -->
<template>
    <div>
        <div class="components-container yw-dashboard">
            <!-- 搜索组件 -->
            <search-box :isScanningResult="true"
                        @search="search"
                        @updateFormSearch="updateFormSearch"></search-box>

            <el-form class="yw-form components-condition">
                <div class="table-operate-wrap clearfix"
                     style="margin:5px 0 10px 0;">
                    <el-button type="text"
                               @click="passWordDialog = true">全量导出</el-button>
                    <el-button type="text"
                               @click="someExport('some')">批量导出</el-button>
                </div>

                <div class="yw-el-table-wrap">

                    <el-table class="yw-el-table"
                              style="cursor:pointer"
                              :data="dataTable"
                              stripe
                              tooltip-effect="dark"
                              border
                              @selection-change="changeCheckbox"
                              height="calc(100vh - 280px)"
                              size="samll"
                              v-loading="loading">
                        <el-table-column type="selection"
                                         width="40px"></el-table-column>
                        <el-table-column prop="pool_name"
                                         label="资源池"
                                         sortable></el-table-column>
                        <el-table-column prop="department1_name"
                                         label="一级部门"
                                         sortable></el-table-column>
                        <el-table-column prop="department2_name"
                                         label="二级部门"
                                         sortable></el-table-column>
                        <el-table-column prop="biz_system_name"
                                         label="业务系统"
                                         sortable></el-table-column>
                        <el-table-column prop="os_status_name"
                                         label="交维状态"
                                         sortable></el-table-column>
                        <el-table-column prop="os_type_name"
                                         label="操作系统"
                                         sortable></el-table-column>
                        <el-table-column prop="agent_ip"
                                         label="IP"
                                         sortable></el-table-column>
                        <el-table-column label="操作"
                                         sortable
                                         width="170">
                            <template slot-scope="scope">
                                <div class="table-operate-wrap clearfix">
                                    <el-button type="text"
                                               @click="ipAllExport(scope.row)">全量导出</el-button>
                                    <el-button type="text"
                                               @click="characteristicExport(scope.row)">特点用户导出</el-button>

                                </div>
                            </template>
                        </el-table-column>

                    </el-table>
                </div>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page.sync="formSearch.page_no"
                                   :page-sizes="[10, 20, 50]"
                                   :page-size="formSearch.page_size"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper">
                    </el-pagination>

                </div>

            </el-form>
        </div>
        <el-dialog class="yw-dialog"
                   title="特定用户导出"
                   width="800px"
                   :visible.sync="userDialog"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form">
                    <div class="table-operate-wrap clearfix">
                        <el-button type="text"
                                   @click="allUserExport">全量导出</el-button>
                        <el-button type="text"
                                   @click="someUserExport">批量导出</el-button>
                    </div>

                    <div class="yw-el-table-wrap">

                        <el-table class="yw-el-table"
                                  style="cursor:pointer"
                                  :data="dialogList"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  @selection-change="changeDialogCheckbox"
                                  height="calc(100vh - 270px)"
                                  size="samll"
                                  v-loading="loading">
                            <el-table-column type="selection"
                                             width="40px"></el-table-column>

                            <el-table-column label="账户名称"
                                             sortable>
                                <template slot-scope="scope">
                                    {{scope.row}}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form>
            </section>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   title="导出"
                   width="800px"
                   :visible.sync="passWordDialog"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form">
                    <el-form-item label="导出密码">
                        <el-input placeholder="请输入密码"
                                  type="password"
                                  :maxlength="20"
                                  v-model="passWordInput"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="goExcel">导出</el-button>
                <el-button @click="cancelBtn">取消</el-button>
            </section>

        </el-dialog>
    </div>
</template>

<script>
    import searchBox from 'src/pages/auto_operation/password_manage/search-box.vue'
    let Base64 = require('js-base64').Base64
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    export default {
        data() {
            return {
                userDialog: false,
                passWordDialog: false,
                passWordInput: '',
                exportType: '',
                loading: false,
                formSearch: {
                    /* name: '',
                    canFixed: '',
                    riskLevel: '',
                    id: '',
                    protocol: '',
                    service: '',
                    department1Index: '', */
                    agent_ip: '',
                    biz_system: '',
                    department1: '',
                    department2: '',
                    os_status: '',
                    os_type: '',
                    pool: '',
                    username: '',
                    is_password_download: true,
                    page_no: 1,
                    page_size: 10,
                },
                exportForm: {
                    agent_ip: '',
                    agent_ip_list: [],
                    biz_system: '',
                    department1: '',
                    department2: '',
                    os_status: '',
                    os_type: '',
                    pool: '',
                    username: '',
                    username_list: '',
                    is_password_download: true,
                },
                agentIpList: [],
                downloadPassword: '',
                dataTable: [],
                userList: [],
                dialogList: [],
                multipleSelection: [],
                multipleSelectionDialog: [],
                test: [],
                total: 0
            }
        },
        components: {
            searchBox
        },
        methods: {
            search(pageNum) {
                this.formSearch.page_no = pageNum ? pageNum : this.formSearch.page_no
                this.formSearch.page_size = this.formSearch.page_size ? this.formSearch.page_size : 10
                let req = {
                    page_no: this.formSearch.page_no,
                    page_size: this.formSearch.page_size
                }
                req = Object.assign(req, this.formSearch)
                rbAutoOperationServicesFactory.getNormalAgentHostList(req).then(res => {
                    if (res.dataList) {
                        this.dataTable = res.dataList
                        this.total = res.totalCount
                    }
                })

            },
            updateFormSearch(params) {
                this.formSearch = this.$utils.deepClone(params)
            },
            cancelBtn() {
                this.passWordDialog = false
                this.passWordInput = ''
            },
            //    导出
            goExcel() {
                let req = {}
                if (this.multipleSelection && this.exportType === 'some') {
                    this.multipleSelection.forEach(val => {
                        let str = ''
                        str = val.proxy_id + ':' + val.agent_ip
                        this.exportForm.agent_ip_list.push(str)
                    })
                }
                if (this.exportForm.agent_ip_list) {
                    req.agent_ip_list = this.exportForm.agent_ip_list
                }
                if (this.passWordInput) {
                    if (this.passWordInput.length < 8) {
                        return this.$message.error('密码需大于8位且不大于20位！')
                    }
                    this.downloadPassword = Base64.encode(this.passWordInput)
                    req = Object.assign(req, this.formSearch)
                    rbAutoOperationServicesFactory.exportUserPassword(req, this.downloadPassword).then(res => {
                        this.$message.success('开始下载')
                        if (res) {
                            this.$utils.createDownloadFileBlob(res, '用户密码表.zip')
                            this.exportForm.agent_ip_list = []
                            this.passWordInput = ''
                        } else {
                            this.$message.error('暂无数据')
                        }
                    })
                } else {
                    this.$message.error('请输入密码')
                }
            },
            someExport(type) {
                if (this.multipleSelection.length > 0) {
                    this.exportType = type
                    this.passWordDialog = true
                } else {
                    this.$message.error('请选择导出内容')
                }
            },
            ipAllExport(row) {
                this.exportForm.agent_ip_list.push(row.proxy_id + ':' + row.agent_ip)
                this.passWordDialog = true
            },
            characteristicExport(row) {
                rbAutoOperationServicesFactory.getUsernameListByAgentIp({
                    agentIp: row.proxy_id + ':' + row.agent_ip
                }).then(res => {
                    if (res) {
                        this.agentIpList.push(row.proxy_id + ':' + row.agent_ip)
                        this.dialogList = res
                        this.userDialog = true
                    }
                    console.log(res)
                })

            },
            allUserExport() {
                this.exportForm.agent_ip_list = this.agentIpList
                this.passWordDialog = true
            },
            someUserExport() {
                this.passWordDialog = true

            },
            changeCheckbox(val) {
                this.multipleSelection = val
                console.log(this.multipleSelection)
            },
            changeDialogCheckbox(val) {
                console.log(val)
                this.exportForm.username_list = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.formSearch.page_size = val
                this.search(1)

            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.formSearch.page_no = val
                this.search(val)

            },

        },
        created() {
            this.search(1)
        }
    }

</script>
<style lang='scss' scoped>
</style>