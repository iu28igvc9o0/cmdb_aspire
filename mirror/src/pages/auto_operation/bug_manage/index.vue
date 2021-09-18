<!-- 漏洞管理：漏洞列表 -->
<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <!-- <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="75px">
            <el-form-item label="漏洞名称"
                          prop="name"
                          label-width="75px">
                <el-input v-model="formSearch.name"
                          placeholder="请输入漏洞名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="是否可修复"
                          prop="canFixed">
                <el-select v-model="formSearch.canFixed"
                           clearable>
                    <el-option label="是"
                               value="Y"></el-option>
                    <el-option label="否"
                               value="N"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="危险等级"
                          prop="riskLevel">
                <el-select v-model="formSearch.riskLevel"
                           clearable>
                    <el-option v-for="item in riskLevelList"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="分组名称"
                          prop="groupId">
                <el-select v-model="formSearch.groupId"
                           clearable>
                    <el-option v-for="item in groupList"
                               :key="item.vulnerability_group_id"
                               :label="item.name"
                               :value="item.vulnerability_group_id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="漏洞编号"
                          prop="id"
                          label-width="75px">
                <el-input v-model="formSearch.id"
                          placeholder="请输入漏洞编号"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="协议"
                          prop="protocol">
                <el-select v-model="formSearch.protocol"
                           clearable>
                    <el-option v-for="item in protocolList"
                               :key="item.key"
                               :label="item.value"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务"
                          prop="service"
                          label-width="75px">
                <el-input v-model="formSearch.service"
                          placeholder="请输入服务"
                          clearable></el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form> -->
        <!-- 搜索组件 -->
        <search-box :isScanningResult="true"
                    @search="search"
                    @updateFormSearch="updateFormSearch"></search-box>

        <el-form class="yw-form">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="addBug">添加漏洞</el-button>
                <el-button type="text"
                           icon="el-icon-download"
                           @click="exportList">导出</el-button>
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="importFile">导入漏洞报告</el-button>
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="importFixedFile">导入漏洞修复手册</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          :height="tableHeight"
                          v-loading="loading">
                    <el-table-column prop="name"
                                     label="漏洞名称"
                                     min-width="90"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="groupName"
                                     label="分组名称"
                                     show-overflow-tooltip
                                     width="80"></el-table-column>
                    <el-table-column label="关联分组"
                                     v-if="false"
                                     width="100">
                        <template slot-scope="scope">
                            <el-select v-model="scope.row.groupId"
                                       @change="handleGroup(scope.row.id, $event)"
                                       placeholder="请选择"
                                       filterable
                                       clearable>
                                <el-option :label="option.name"
                                           :value="option.vulnerability_group_id"
                                           v-for="option in groupList"
                                           :key="option.vulnerability_group_id"></el-option>
                            </el-select>
                        </template>
                    </el-table-column>
                    <el-table-column prop="id"
                                     label="漏洞编号"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="discoverProducer"
                                     label="漏洞发现厂商"
                                     show-overflow-tooltip
                                     width="100">
                        <template slot-scope="scope">
                            <span v-if="scope.row.discoverProducer === 'qiming'">启明星辰</span>
                            <span v-else-if="scope.row.discoverProducer === 'lvmeng'">绿盟</span>
                            <span v-else>{{scope.row.discoverProducer}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="是否重启"
                                     width="70">
                        <template slot-scope="scope">
                            <span v-if="scope.row.needReboot === 'Y'">是</span>
                            <span v-else>否</span>
                            <!-- <el-select v-model="scope.row.needReboot"
                                       @change="handleNeedReboot(scope.row.id, $event)"
                                       placeholder="请选择"
                                       filterable
                                       clearable>
                                <el-option label="是"
                                           value="Y"></el-option>
                                <el-option label="否"
                                           value="N"></el-option>
                            </el-select> -->
                        </template>
                    </el-table-column>
                    <el-table-column prop="riskLevelDesc"
                                     label="危险等级"
                                     show-overflow-tooltip
                                     width="70"></el-table-column>
                    <el-table-column prop="port"
                                     label="端口"
                                     show-overflow-tooltip
                                     width="60"></el-table-column>
                    <el-table-column prop="protocol"
                                     label="协议"
                                     show-overflow-tooltip
                                     width="50"></el-table-column>
                    <el-table-column prop="service"
                                     label="服务"
                                     show-overflow-tooltip
                                     width="90"></el-table-column>
                    <el-table-column prop="softDependencies"
                                     label="软件依赖"
                                     show-overflow-tooltip
                                     width="70"></el-table-column>
                    <el-table-column prop="describe"
                                     label="描述"
                                     min-width="160"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="repairMethod"
                                     label="解决方法"
                                     min-width="160"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="updateTime"
                                     label="最后修改时间"
                                     width="140"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="130">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="关联作业"
                                       icon="el-icon-connection"
                                       :disabled="scope.row.canFixed === 'N'"
                                       @click="chooseWork(scope.row)"></el-button>
                            <el-button type="text"
                                       title="修复"
                                       :disabled="scope.row.canFixed === 'N'"
                                       @click="bugFix(scope.row)">
                                <i class="icon iconfont f14">&#xe62b;</i>
                            </el-button>
                            <el-button type="text"
                                       title="编辑"
                                       icon="el-icon-edit"
                                       @click="editBug(scope.row)"></el-button>
                            <el-button type="text"
                                       :disabled="scope.row.canFixed === 'N'"
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
        </el-form>

        <!-- 关联作业弹框 -->
        <el-dialog width="1037px"
                   class="yw-dialog"
                   title="关联作业"
                   v-if="chooseWorkBoxShow"
                   :visible.sync="chooseWorkBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <works-choose :currentRowData="currentRowData"
                              :originWorkData="originWorkData"
                              propKey="pipelineIdList"
                              workType="选择修复作业"
                              @setSelectedKey="setSelectedKey"></works-choose>
                <works-choose :currentRowData="currentRowData"
                              :originWorkData="originWorkBackData"
                              propKey="goBackPipelineIdList"
                              workType="选择回退作业"
                              @setSelectedKey="setSelectedBackKey"></works-choose>
                <works-choose :currentRowData="currentRowData"
                              :originWorkData="originWorkRecheckData"
                              propKey="recheckPipelineIdList"
                              workType="选择复查作业"
                              @setSelectedKey="setSelectedRecheckKey"></works-choose>

                <div class="fixed-bottom-box t-center">
                    <el-button type="primary"
                               @click="checkSelectedList">确定
                    </el-button>
                    <el-button @click="hideChooseBox">取消</el-button>
                </div>
            </section>
        </el-dialog>

        <!-- 修复漏洞弹框 -->
        <el-dialog width="90%"
                   class="yw-dialog"
                   title="选择漏洞主机列表"
                   v-if="bugFixBoxShow"
                   :visible.sync="bugFixBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main">
                <bug-fix :currentRowData="currentRowData"
                         :originWorkData="originWorkData"></bug-fix>
            </div>
        </el-dialog>

        <!-- 修复编辑弹框 -->
        <el-dialog width="750px"
                   class="yw-dialog"
                   title="漏洞信息"
                   v-if="editBoxShow"
                   :visible.sync="editBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main">
                <bug-edit ref="editBox"
                          :currentRowData="currentRowData"
                          :riskLevelList="riskLevelList"
                          :groupList="groupList"
                          @hideediting="hideediting"></bug-edit>
            </div>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="confirmSubmit">确定
                </el-button>
                <el-button @click="hideediting">取消</el-button>
            </section>
        </el-dialog>

        <!-- 执行作业 -->
        <run-work-dialog></run-work-dialog>

        <!-- 导入漏洞报告 -->
        <el-dialog width="50%"
                   class="yw-dialog"
                   title="导入漏洞报告"
                   :visible.sync="uploadBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <import-bug-report @uploadSuccess="uploadSuccess"></import-bug-report>
        </el-dialog>

        <!-- 导入漏洞修复手册 -->
        <el-dialog width="50%"
                   class="yw-dialog"
                   title="导入漏洞修复手册"
                   :visible.sync="uploadFixFileShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <import-fix-file @uploadSuccess="uploadSuccess"></import-fix-file>
        </el-dialog>
    </div>
</template>

<script>
    import importFixFile from 'src/pages/auto_operation/bug_manage/import-fix-file.vue'
    import importBugReport from 'src/pages/auto_operation/bug_manage/import-bug-report.vue'
    import worksChoose from 'src/pages/auto_operation/bug_manage/works-choose.vue'
    import bugFix from 'src/pages/auto_operation/bug_manage/bug-fix.vue'
    import bugEdit from 'src/pages/auto_operation/bug_manage/bug-edit.vue'
    import runWorkDialog from 'src/pages/auto_operation/work_manage/task/run-work-dialog.vue'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import { createDownloadFile } from 'src/utils/utils.js'
    import searchBox from 'src/pages/auto_operation/bug_manage/scanning_results/search-box.vue'

    export default {
        name: 'AutoOperationBugManageList',
        components: {
            searchBox,
            worksChoose,
            bugFix,
            runWorkDialog,
            bugEdit,
            importBugReport,
            importFixFile
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                dataList: [], // 作业列表
                groupList: [], // 分组列表
                formSearch: {
                    name: '',
                    canFixed: '',
                    riskLevel: '',
                    groupName: '',
                    id: '',
                    protocol: '',
                    service: '',
                },
                currentRowData: {}, // 当前行内容
                chooseWorkBoxShow: false, // 关联作业弹框
                bugFixBoxShow: false, // 修复漏洞弹框
                editBoxShow: false, // 编辑列表弹框
                pipelineIdList: [], // 关联的作业id列表
                goBackPipelineIdList: [], // 关联的作业id列表
                recheckPipelineIdList: [],
                // 漏洞类型作业数据
                originWorkData: {
                    dataList: [],
                    totalCount: 0
                },
                // 漏洞类型作业数据
                originWorkBackData: {
                    dataList: [],
                    totalCount: 0
                },

                // 漏洞类型作业数据
                originWorkRecheckData: {
                    dataList: [],
                    totalCount: 0
                },

                riskLevelList: [], // 漏洞等级列表
                groupNamelList: [], // 分组列表
                protocolList: [{    // 协议列表
                    key: '1',
                    value: 'TCP'
                }, {
                    key: '2',
                    value: 'UDP'
                }, {
                    key: '3',
                    value: '其他协议'
                }],
                uploadBoxShow: false,
                uploadFixFileShow: false,
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
            this.queryVulnerabilityGroupList()
            this.searchWorkList()
            this.queryRiskLevelList()
            this.getGroupName()
        },
        methods: {
            updateFormSearch(params) {
                this.formSearch = this.$utils.deepClone(params)
            },
            // 导入漏洞报告
            importFile() {
                this.uploadBoxShow = true
            },
            // 导入漏洞修复手册
            importFixedFile() {
                this.uploadFixFileShow = true
            },
            uploadSuccess() {
                this.uploadBoxShow = false
                this.uploadFixFileShow = false
                this.search()
            },
            // 导出漏洞列表
            exportList() {
                this.$message('请稍候')
                bugManageService.exportVulnerability(this.formSearch).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '漏洞列表文件.xls')
                    }
                })
            },
            // 添加漏洞
            addBug() {
                this.currentRowData = {}
                this.editBoxShow = true
            },
            // 查询漏洞等级列表
            queryRiskLevelList() {
                bugManageService
                    .queryRiskLevelList()
                    .then(res => {
                        this.riskLevelList = res
                        this.riskLevelList.forEach(item => {
                            for (const key in item) {
                                item.id = key
                                item.name = item[key]
                            }
                        })
                    })
                    .catch(res => {
                        this.showErrorTip(res)
                    })
            },
            // 查询分组列表
            queryVulnerabilityGroupList() {
                let req = {
                    page_no: this.currentPage,
                    page_size: 200
                }
                bugManageService
                    .queryVulnerabilityGroupList(req)
                    .then(res => {
                        this.groupList = res.dataList
                    })
                    .catch(res => {
                        this.showErrorTip(res)
                    })
            },
            // 查询列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                bugManageService
                    .queryVulnerabilityList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        for (let i = 0; i < res.dataList.length; i++) {
                            if (res.dataList[i].groupName == null) {
                                res.dataList[i].groupName = 'unknow'
                            }
                        }
                        this.dataList = res.dataList

                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
                this.search()
            },
            // 已关联作业id列表
            setSelectedKey(data) {
                this.pipelineIdList = data
            },
            setSelectedBackKey(data) {
                this.goBackPipelineIdList = data
            },
            setSelectedRecheckKey(data) {
                this.recheckPipelineIdList = data
            },

            // 触发查询漏洞类型作业列表
            searchWorkList() {
                this.queryOpsPipelineList('vulnerability')
                this.queryOpsPipelineList('vulnerability_goback')
                this.queryOpsPipelineList('vulnerability_recheck')
            },
            // 查询漏洞类型作业列表
            queryOpsPipelineList(type) {
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    labelId: type  // 漏洞类型： vulnerability
                }
                rbAutoOperationWorkServicesFactory
                    .queryOpsPipelineList(req)
                    .then(res => {
                        if (type === 'vulnerability') {
                            this.originWorkData = res
                            this.originWorkData.dataList = res.dataList || []
                        } else if (type === 'vulnerability_goback') {
                            this.originWorkBackData = res
                            this.originWorkBackData.dataList = res.dataList || []
                        } else {
                            this.originWorkRecheckData = res
                            this.originWorkRecheckData.dataList = res.dataList || []
                        }
                    })
                    .catch(error => {
                        this.showErrorTip(error)
                    })
            },
            // 关联作业
            checkSelectedList() {
                this.pageLoading = true
                let req = {
                    id: this.currentRowData.id, // id
                    pipelineIdList: this.pipelineIdList,    // 关联的作业id列表
                    goBackPipelineIdList: this.goBackPipelineIdList,
                    recheckPipelineIdList: this.recheckPipelineIdList,

                }
                this.updateVulnerability(req)
            },
            // 
            confirmSubmit() {
                this.$refs.editBox.$refs.editForm.validate((valid) => {
                    if (!valid) {
                        return
                    }
                    let curFormData = this.$refs.editBox.formData
                    this.pageLoading = true
                    if (curFormData.id) {
                        this.updateVulnerability(curFormData)
                    } else {
                        this.saveVulnerability(curFormData)
                    }
                })
            },
            getGroupName(groupId) {
                let groupName = ''
                if (groupId) {
                    this.groupList.forEach(item => {
                        if (item.vulnerability_group_id === groupId) {
                            groupName = item.name
                        }
                    })
                }

                return groupName
            },
            // 设置所属分组
            handleGroup(id, val) {
                this.pageLoading = true
                let req = {
                    id: id, // 漏洞id
                    groupId: val,
                    groupName: this.getGroupName(val),
                }
                this.updateVulnerability(req)
            },
            // 设置漏洞为不可修复
            handleCanFixed(row) {
                this.$confirm(`确定设置【${row.name}】为不可修复？`).then(() => {
                    this.loading = true
                    let req = {
                        id: row.id, // 漏洞主机id
                        canFixed: 'N',
                    }
                    this.updateVulnerability(req)
                })
            },
            // 设置是否重启
            handleNeedReboot(id, val) {
                this.pageLoading = true
                let req = {
                    id: id, // 漏洞id
                    needReboot: val,
                }
                this.updateVulnerability(req)
            },
            // 更新漏洞 接口
            updateVulnerability(req) {
                bugManageService
                    .updateVulnerability(req)
                    .then(() => {
                        this.$message.success('操作成功')
                        this.search()
                        this.pageLoading = false
                        this.editBoxShow = false
                        this.hideChooseBox()
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 添加漏洞 接口
            saveVulnerability(req) {
                bugManageService
                    .saveVulnerability(req)
                    .then(() => {
                        this.$message.success('操作成功')
                        this.search()
                        this.pageLoading = false
                        this.editBoxShow = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 取消关闭弹框
            hideChooseBox() {
                this.chooseWorkBoxShow = false
            },
            hideediting() {
                this.editBoxShow = false
            },
            // 打开关联作业弹框
            chooseWork(row) {
                this.currentRowData = row
                this.chooseWorkBoxShow = true
            },
            // 打开漏洞详情，选中主机进行修复
            bugFix(row) {
                this.currentRowData = row
                this.bugFixBoxShow = true
            },
            editBug(row) {
                this.currentRowData = row
                this.editBoxShow = true
            }

        }
    }
</script>

<style lang="scss" scoped>
</style>
