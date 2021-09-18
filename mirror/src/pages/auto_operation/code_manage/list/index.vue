<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="75px">
            <el-form-item label="脚本名称"
                          prop="codeName">
                <el-input v-model="formSearch.codeName"
                          placeholder="请输入脚本名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="创建人"
                          prop="creater">
                <el-input v-model="formSearch.creater"
                          placeholder="请输入创建人"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="创建时间">
                <el-col :span="11">
                    <el-form-item prop="createTimeStart">
                        <el-date-picker v-model="formSearch.createTimeStart"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="createTimeEnd">
                        <el-date-picker v-model="formSearch.createTimeEnd"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="作业名称"
                          prop="workName">
                <el-input v-model="formSearch.workName"
                          placeholder="请输入作业名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="分类标签"
                          prop="labelId">
                <el-select v-model="formSearch.labelId"
                           placeholder="请选择分类标签"
                           filterable
                           clearable>
                    <el-option v-for="val in labelList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="审核状态"
                          prop="updater"
                          label-width="75px">
                <el-select v-model="formSearch.auditStatus"
                           placeholder="请选择审核状态"
                           filterable
                           clearable>
                    <el-option v-for="val in auditStatusList"
                               :key="val.code"
                               :label="val.label"
                               :value="val.code"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div ref="operateBox"
                 class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="addCode">新建脚本</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="codeList"
                          class="yw-el-table"
                          stripe
                          highlight-current-row
                          tooltip-effect="dark"
                          border
                          :height="tableHeight"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column prop="script_name"
                                     min-width="110"
                                     show-overflow-tooltip
                                     label="脚本名称"></el-table-column>
                    <el-table-column prop="pipeline_name"
                                     min-width="110"
                                     show-overflow-tooltip
                                     label="作业名称"></el-table-column>
                    <el-table-column prop="creater"
                                     label="创建人"
                                     width="120"></el-table-column>
                    <el-table-column prop="updater"
                                     label="最后修改人"
                                     width="120"></el-table-column>
                    <el-table-column prop="reviewer"
                                     label="审核人"
                                     width="110"></el-table-column>
                    <el-table-column prop="create_time"
                                     label="创建时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="update_time"
                                     label="最后修改时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="review_time"
                                     label="审核时间"
                                     width="140"></el-table-column>
                    <el-table-column prop="script_id"
                                     label="ID"
                                     width="60"></el-table-column>
                    <el-table-column prop="label_id"
                                     width="90"
                                     show-overflow-tooltip
                                     label="分类"></el-table-column>
                    <el-table-column prop="is_public"
                                     label="是否公共"
                                     width="100">
                        <template slot-scope="scope">
                            <span v-if="scope.row.is_public === 1">公共</span>
                            <span v-else>非公共</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="审核状态">
                        <template slot-scope="scope">
                            <span v-if="scope.row.audit_status == 0">待审核</span>
                            <span v-if="scope.row.audit_status == 1">审核通过</span>
                            <span v-if="scope.row.audit_status == 2">审核拒绝</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="audit_desc"
                                     label="审核描述"
                                     width="120"></el-table-column>
                    <el-table-column prop="current_version"
                                     label="当前版本"
                                     width="140"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="120">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="去执行"
                                       icon="el-icon-video-play"
                                       @click="toRunCode(scope.row)"
                                       :disabled="scope.row.audit_status != '1'"></el-button>
                            <el-button type="text"
                                       title="编辑"
                                       icon="el-icon-edit"
                                       @click="goEdit(scope.row)"></el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="deleteCode(scope.row)"></el-button>
                            <el-button type="text"
                                       :disabled="scope.row.audit_status != '0'"
                                       title="审核"
                                       icon="el-icon-thumb"
                                       @click="gotoAuditWork(scope.row)"></el-button>
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
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   v-if="codeEditorShow"
                   :title="dialogName"
                   :visible.sync="codeEditorShow"
                   :close-on-click-modal="false"
                   :destroy-on-close="true"
                   width="850px">
            <section class="yw-dialog-main">
                <code-editor ref="codeEditor"
                             flag="脚本"
                             :codeNameShow="true"
                             :isAddCode="true"
                             :publicCodeShow="true"
                             :currentCodeInfo="currentCodeInfo"
                             :codeCloneList="codeList"
                             :paramType="paramType"
                             @passCodeContent="passCodeContent"></code-editor>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveCode()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
        <!-- 审核脚本 -->
        <el-dialog width="50%"
                   class="yw-dialog"
                   title="编辑审核"
                   :visible.sync="auditBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="auditForm"
                         :model="auditForm"
                         label-width="100px">
                    <el-form-item label="审核状态"
                                  prop="auditStatus">
                        <el-radio v-model="auditForm.auditStatus"
                                  label="1">通过</el-radio>
                        <el-radio v-model="auditForm.auditStatus"
                                  label="2">拒绝</el-radio>
                        <el-tooltip class="item"
                                    effect="dark"
                                    :content="tipContent"
                                    placement="right">
                            <i class="el-icon-question"></i>
                        </el-tooltip>

                    </el-form-item>
                    <el-form-item label="审核描述"
                                  prop="auditDesc">
                        <el-input type="textarea"
                                  :rows="2"
                                  placeholder="请输入内容"
                                  v-model="auditForm.auditDesc">
                        </el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="auditScript">确定</el-button>
                <el-button @click="auditBoxShow = false">取消</el-button>
            </section>
        </el-dialog>

        <!-- 编辑 -->
        <el-dialog class="yw-dialog"
                   title="编辑"
                   width="800px"
                   :visible.sync="editDialog"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form>
                    <el-form-item label="作业名称">
                        <div class="item-style">
                            <span>{{workName}}</span>
                            <el-button type="text"
                                       @click="updateCode(editData)">编辑</el-button>
                        </div>
                    </el-form-item>
                    <el-form-item>
                        <el-table class="yw-el-table"
                                  style="cursor:pointer"
                                  :data="pipelineHisList"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  height="calc(100vh - 270px)"
                                  size="samll"
                                  v-loading="loading">
                            <el-table-column prop="current_version"
                                             label="版本号"
                                             sortable></el-table-column>
                            <el-table-column prop="create_time"
                                             label="创建时间"
                                             sortable></el-table-column>
                            <el-table-column prop="creater"
                                             label="创建人"
                                             sortable></el-table-column>
                            <el-table-column label="状态"
                                             sortable>
                                <template slot-scope="scope">
                                    <p v-if="scope.row.current_version == currentVersion">使用</p>
                                    <p v-else>未使用</p>
                                </template>
                            </el-table-column>
                            <el-table-column prop="pool_name"
                                             label="操作"
                                             sortable>
                                <template slot-scope="scope">

                                    <el-button type="text"
                                               :disabled="scope.row.current_version == currentVersion"
                                               @click="updatePipelineVersion(scope.row)">启用</el-button>
                                    <el-button type="text"
                                               @click="viewTaskDetail(scope.row)">查看</el-button>
                                </template>
                            </el-table-column>

                        </el-table>
                    </el-form-item>
                </el-form>
            </section>

        </el-dialog>

    </div>
</template>

<script>
    import codeEditor from '../../components/code-editor.vue'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import rbAutoOperationWorkServicesFactory from 'src/services/auto_operation/rb-auto-operation-work-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        name: 'AutoOperationCodeManageList',
        components: {
            codeEditor
        },
        data() {
            return {
                tipContent: '',
                auditStatusList: [{ code: '0', label: '待审核' }, { code: '1', label: '审核通过' }, { code: '2', label: '审核拒绝' }],
                editData: [],
                editDialog: false,
                pipelineId: '',
                workName: '',
                currentVersion: '',
                pipelineHisList: [],
                labelList: [],
                auditBoxShow: false,
                auditForm: {
                    scriptId: '',
                    auditStatus: '',
                    auditDesc: ''
                },
                pageLoading: false,
                loading_text: '请稍候...',
                codeList: [], // 脚本列表
                codeEditorShow: false,
                formSearch: {
                    codeName: '',
                    creater: sessionStorage.getItem('username'),
                    createTimeStart: '',
                    createTimeEnd: '',
                    workName: ''
                },
                codeCloneList: [],  // 脚本克隆列表
                codeInfo: {}, // 脚本弹框最新内容
                currentCodeInfo: {},  // 当前行脚本内容
                dialogName: '新建脚本',  // 弹框title
                paramType: ''
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.queryOpsLabelList()
            this.search()
        },
        methods: {
            // 编辑
            goEdit(row) {
                this.editData = row
                this.editDialog = true
                this.pipelineId = row.script_id
                this.workName = row.script_name
                this.currentVersion = row.current_version
                this.paramType = 'edit'
                this.getHistoryList()
            },
            // 启用历史
            updatePipelineVersion(row) {
                rbAutoOperationServicesFactory.updateScriptVersion({ scriptHisId: row.id }).then(res => {
                    if (res.flag) {
                        this.$message.success('启用成功')
                        this.search()
                        this.editDialog = false
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 历史作业
            getHistoryList() {
                rbAutoOperationServicesFactory.getScriptHisListByScriptId({ scriptId: this.pipelineId }).then(res => {
                    this.pipelineHisList = res
                })
            },
            // 查看脚本/作业详情
            viewTaskDetail(row) {
                this.editDialog = false
                this.$router.push({
                    path: '/auto_operation/code_manage/run',
                    query: {
                        scriptId: row.id,
                        type: 'detail',
                        historyVal: '1',
                        currentTitle: '脚本详情'
                    }
                })
            },

            queryOpsLabelList() {
                rbAutoOperationWorkServicesFactory
                    .queryOpsLabelList()
                    .then(res => {
                        this.labelList = res
                    })
            },
            // 审核脚本
            gotoAuditWork(row) {
                this.auditForm.scriptId = row.script_id
                this.auditForm.auditStatus = '1'
                this.auditForm.auditDesc = ''
                this.auditBoxShow = true
                let arr = []
                arr.push(row)
                rbAutoOperationServicesFactory
                    .checkScriptSensitivity({ script_list: arr })
                    .then(res => {
                        if (res.flag) {
                            this.tipContent = '暂无敏感指令'
                        } else {
                            this.tipContent = res.error_tip
                        }
                    })
            },
            auditScript() {
                if (this.auditForm.auditStatus == '2' && this.auditForm.auditDesc == '') {
                    this.$message.error('审核拒绝必须填写审核描述')
                    return
                }
                rbAutoOperationServicesFactory.auditScript(this.auditForm).then(res => {
                    if (res.flag) {
                        this.$message({
                            message: '审核成功!',
                            type: 'success'
                        })
                    } else {
                        this.$message.error(res.error_tip)
                    }
                    this.auditBoxShow = false
                    this.search()
                })
                    .catch(error => {
                        this.showErrorTip(error)
                    })
            },
            // 查询脚本列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    scriptNameLike: this.formSearch.codeName,
                    creater: this.formSearch.creater,
                    pipelineNameLike: this.formSearch.workName,
                    createTimeStart: this.formSearch.createTimeStart,
                    createTimeEnd: this.formSearch.createTimeEnd,
                    label_id: this.formSearch.labelId,
                    audit_status: this.formSearch.auditStatus,
                    content_type: '', // 脚本内容类型int
                    scriptContentLike: '',  // 脚本内容string
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                this.loading = true
                this.codeCloneList = []
                rbAutoOperationServicesFactory
                    .queryOpsScriptList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.codeList = res.dataList
                        this.codeCloneList = JSON.parse(JSON.stringify(res.dataList))
                    })
                    .catch(error => {
                        this.loading = false
                        this.$message.error('该账号没有数据权限')
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },
            // 保存脚本
            saveCode() {
                this.$refs.codeEditor.$refs.codeForm.validate((valid) => {
                    if (!valid) {
                        this.$message.warning('请先完善信息')
                        return
                    }

                    this.pageLoading = true
                    const groupIdList = []
                    this.codeInfo.groupTagids.forEach(item => {
                        groupIdList.push(item.group_id)
                    })
                    // alert(0)
                    // console.log(this.codeInfo.groupTagids, groupIdList, '2133245')
                    let req = {
                        step_id: '',
                        is_public: this.codeInfo.is_public,
                        label_id: this.codeInfo.labelId,
                        script_id: this.codeInfo.codeId,
                        content_type: this.codeInfo.languageType,
                        script_content: this.codeInfo.codeContent,
                        script_use_desc: this.codeInfo.script_use_desc,
                        script_name: this.codeInfo.codeName,
                        base64_encode: true,
                        group_id_list: groupIdList,
                        // 自定义参数code
                        ops_param_code: this.codeInfo.ops_param_code,
                        // 自定义参数产出密码
                        package_password: this.codeInfo.package_password,
                        ops_param_reference_list: this.codeInfo.ops_param_reference_list,
                    }
                    rbAutoOperationServicesFactory
                        .saveScript(req)
                        .then(res => {
                            if (res.flag) {
                                this.$message.success('保存成功')
                                this.search()
                                this.codeEditorShow = false
                                this.editDialog = false
                            } else {
                                this.$message.error(res.error_tip)
                            }
                            this.pageLoading = false
                        })
                        .catch(error => {
                            this.pageLoading = false
                            this.showErrorTip(error)
                        })
                })
            },
            // 取消关闭弹框
            addCancel() {
                this.codeEditorShow = false
            },
            // 显示新建脚本弹框
            addCode() {
                this.dialogName = '新建脚本'
                this.currentCodeInfo = {
                    codeName: '',
                    labelId: '',
                    codeId: '',
                    codeType: '-1',
                    codeContent: '',
                    languageType: 1,
                    groupTagids: []
                }
                this.codeEditorShow = true
                this.paramType = 'add'

            },
            // 编辑脚本
            updateCode(editData) {
                this.dialogName = '编辑脚本'
                let data = {
                    codeType: editData.is_public === 1 ? '2' : '-1', // 是否公共脚本
                    codeName: editData.script_name,
                    labelId: editData.label_id,
                    codeId: editData.script_id,
                    codeContent: editData.script_content,
                    is_public: editData.is_public || 0,
                    languageType: editData.content_type,
                    groupTagids: editData.group_relation_list
                }
                this.currentCodeInfo = this.$utils.deepClone(Object.assign(editData, data))
                this.codeEditorShow = true
            },
            // 去执行脚本
            toRunCode(row) {
                this.$router.push({
                    path: '/auto_operation/code_manage/run',
                    query: {
                        scriptId: row.script_id,
                        type: 'fromCodeList'
                    }
                })
            },
            // 删除脚本
            deleteCode(row) {
                this.$confirm('确认删除?').then(() => {
                    rbAutoOperationServicesFactory.removeScript(row.script_id).then(res => {
                        if (res.flag) {
                            this.$message({
                                message: '删除成功!',
                                type: 'success'
                            })
                            this.search()
                        } else {
                            // console.log(res)
                            this.showErrorTip(res)
                        }
                    })
                        .catch(error => {
                            this.showErrorTip(error)
                        })
                })
            },
            // 传递脚本内容
            passCodeContent(data) {
                this.codeInfo = data
            }
        }
    }
</script>

<style lang="scss" scoped>
    .item-style {
        display: flex;
        justify-content: space-between;
    }
</style>
