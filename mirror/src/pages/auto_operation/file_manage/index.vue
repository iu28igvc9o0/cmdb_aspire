<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="文件名称"
                          prop="file_name_like">
                <el-input v-model="formSearch.file_name_like"
                          placeholder="请输入文件名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="文件版本"
                          prop="file_version_like">
                <el-input v-model="formSearch.file_version_like"
                          placeholder="请输入文件版本"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="更新时间">
                <el-col :span="11">
                    <el-form-item prop="update_time_start">
                        <el-date-picker v-model="formSearch.update_time_start"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="开始时间"></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col class="line"
                        :span="1">-</el-col>
                <el-col :span="11">
                    <el-form-item prop="update_time_end">
                        <el-date-picker v-model="formSearch.update_time_end"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="结束时间"></el-date-picker>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="文件类型"
                          prop="file_type">
                <el-select v-model="formSearch.file_type"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in fileTypeList"
                               :key="val.type"
                               :label="val.name"
                               :value="val.type"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="生成类型"
                          prop="file_generate_type">
                <el-select v-model="formSearch.file_generate_type"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in fileGenerationTypeList"
                               :key="val.type"
                               :label="val.name"
                               :value="val.type"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="文件分类"
                          prop="file_class">
                <el-select v-model="formSearch.file_class"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="val in fileClassificationTypeList"
                               :key="val.type"
                               :label="val.name"
                               :value="val.type"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <div class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="showDialogBox('addRow')">新建</el-button>
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="showDownloadDialogBox()">加密汇聚下载</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="dataList"
                          class="yw-el-table"
                          :header-cell-style="{background:'#E8F0FC',color:'#3A4154',height:'19px'}"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 275px)"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column type="selection"
                                     width="40"></el-table-column>
                    <el-table-column prop="file_name"
                                     label="文件名称"
                                     show-overflow-tooltip
                                     width="120"></el-table-column>
                    <el-table-column prop="file_version"
                                     label="文件版本"
                                     show-overflow-tooltip
                                     width="120"></el-table-column>
                    <el-table-column prop="file_type"
                                     show-overflow-tooltip
                                     label="文件类型">
                        <template slot-scope="scope">
                            <span v-if="scope.row.file_type === '1'">直接使用</span>
                            <span v-if="scope.row.file_type === '2'">拆分文件</span>
                            <span v-if="scope.row.file_type === '3'">安全下载</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="file_generate_type"
                                     show-overflow-tooltip
                                     width="100"
                                     label="文件生成类型">
                        <template slot-scope="scope">
                            <span v-if="scope.row.file_generate_type === '1'">本地上传</span>
                            <span v-if="scope.row.file_generate_type === '2'">自动生成</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="file_class"
                                     show-overflow-tooltip
                                     width="120"
                                     label="文件分类">
                        <template slot-scope="scope">
                            <span v-if="scope.row.file_class === '1'">基线文件</span>
                            <span v-if="scope.row.file_class === '2'">账号文件</span>
                            <span v-if="scope.row.file_class === '3'">巡检报告文件</span>
                            <span v-if="scope.row.file_class === '4'">汇总结果保存文件</span>
                            <span v-if="scope.row.file_class === '5'">日志文件</span>
                            <span v-if="scope.row.file_class === '6'">加密文件</span>
                            <span v-if="scope.row.file_class === '9'">其他</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="group_relation_list"
                                     label="文件分组"
                                     show-overflow-tooltip
                                     width="100">
                        <template slot-scope="scope">
                            <span>{{scope.row.group_relation_list | handleGroupName}}</span>
                        </template>

                    </el-table-column>
                    <el-table-column prop="file_desc"
                                     label="文件描述"
                                     show-overflow-tooltip
                                     width="100"></el-table-column>
                    <el-table-column prop="create_time"
                                     show-overflow-tooltip
                                     label="创建时间"
                                     min-width="140"></el-table-column>
                    <el-table-column prop="creater"
                                     show-overflow-tooltip
                                     label="创建人"></el-table-column>
                    <el-table-column prop="last_update_time"
                                     label="修改时间"
                                     show-overflow-tooltip
                                     min-width="140"></el-table-column>
                    <el-table-column prop="last_updater"
                                     show-overflow-tooltip
                                     label="修改人"></el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="130">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       title="详情"
                                       icon="el-icon-view"
                                       @click="showDialogBox('viewDetail', scope.row)"></el-button>
                            <el-button type="text"
                                       title="编辑"
                                       icon="el-icon-edit"
                                       @click="showDialogBox('editRow', scope.row)"></el-button>
                            <el-button type="text"
                                       title="下载"
                                       icon="el-icon-download"
                                       @click="downloadFile(scope.row)"></el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="removeRow(scope.row.file_id)"></el-button>
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
        <el-dialog class="yw-dialog"
                   title="加密汇聚下载"
                   :visible.sync="dialogDownloadBoxShow">
            <section class="yw-dialog-main middle-size-form">
                <el-form class="yw-form is-required"
                         ref="downloadForm"
                         :model="downloadForm"
                         :rules="downloadRules"
                         label-width="100px">
                    <el-form-item label="文件名称"
                                  prop="downloadFileName">
                        <el-input v-model="downloadForm.downloadFileName"
                                  placeholder="请输入文件名称"
                                  :clearable="true"></el-input>
                    </el-form-item>
                    <el-form-item label="加密密码"
                                  prop="downloadPassword">
                        <el-input v-model="downloadForm.downloadPassword"
                                  placeholder="请输入加密密码"
                                  type='password'
                                  :clearable="true"></el-input>
                    </el-form-item>
                    <section class="btn-wrap"
                             v-show="true">
                        <el-button type="primary"
                                   @click="convergeDownload()">下载</el-button>
                        <el-button @click="closeDownloadDialogBox()">取消</el-button>
                    </section>
                </el-form>
            </section>
        </el-dialog>
        <!-- 新建、编辑文件 -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="dialogBoxShow"
                   width="500">
            <section class="yw-dialog-main middle-size-form">
                <el-form class="yw-form is-required"
                         ref="addForm"
                         :model="addForm"
                         :rules="formRules"
                         label-width="100px">
                    <el-form-item label="文件名称"
                                  prop="file_name">
                        <el-input v-model="addForm.file_name"
                                  placeholder="请输入文件名称"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="文件上传"
                                  prop="file_path">
                        <el-input class="middle-size-input"
                                  v-model="addForm.file_path"
                                  placeholder="请上传文件"
                                  readonly></el-input>
                        <el-upload v-show="!isReadonly"
                                   action=""
                                   class="upload-demo"
                                   :show-file-list="false"
                                   :file-list="fileList"
                                   :http-request="UploadFile"
                                   :on-remove="handleRemove">
                            <el-button size="small"
                                       type="primary">上传</el-button>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="文件版本"
                                  prop="file_version">
                        <el-input v-model="addForm.file_version"
                                  placeholder="请输入文件版本"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="文件类型"
                                  prop="file_type">
                        <el-select v-model="addForm.file_type"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="val in fileTypeList"
                                       :key="val.type"
                                       :label="val.name"
                                       :value="val.type"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文件生成类型"
                                  prop="file_generate_type">
                        <el-select v-model="addForm.file_generate_type"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="val in fileGenerationTypeList"
                                       :key="val.type"
                                       :label="val.name"
                                       :value="val.type"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文件分类"
                                  prop="file_class">
                        <el-select v-model="addForm.file_class"
                                   placeholder="请选择"
                                   filterable
                                   clearable>
                            <el-option v-for="val in fileClassificationTypeList"
                                       :key="val.type"
                                       :label="val.name"
                                       :value="val.type"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文件描述"
                                  prop="file_desc">
                        <el-input type="textarea"
                                  rows="4"
                                  v-model="addForm.file_desc"
                                  placeholder="请输入文件描述"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="分组名称"
                                  prop="group_relation_list">
                        <el-tag :key="tag.group_id"
                                style="margin-right: 5px;"
                                v-for="tag in addForm.group_relation_list"
                                closable
                                :disable-transitions="false"
                                @close="handleClose(tag)"
                                size="small">
                            {{tag.group_name}}
                        </el-tag>
                        <el-popover placement="bottom-start"
                                    trigger="click">
                            <comtree :ref="treeName"
                                     :data="groupTreeData"
                                     :props="gruopTreeDefault"
                                     :exId="treeName"
                                     :ex-control="true"
                                     :ex-control-opt="customControl"
                                     ex-show-search
                                     @node-click="handleTreeClick">
                            </comtree>
                            <el-button slot="reference"
                                       class="mod-btn"
                                       size="small">请选择</el-button>
                        </el-popover>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap"
                     v-show="!isReadonly">
                <el-button type="primary"
                           @click="saveFormData()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import comtree from '../components/tree.vue'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'
    import fileManageService from 'src/services/auto_operation/rb-auto-operation-file-manage-services.js'
    export default {
        name: 'FileManageList',
        components: { comtree },
        data() {
            return {
                multipleSelection: [],
                formSearch: {
                    file_name_like: '',
                    file_version_like: '',
                    update_time_start: '',
                    update_time_end: '',
                    file_type: '',
                    file_generate_type: '',
                    file_class: '',
                    group_ids: '',
                },
                addForm: {
                    file_name: '',
                    file_version: '',
                    file_path: '',
                    file_type: '',
                    file_generate_type: '',
                    file_class: '',
                    file_desc: '',
                    // 已选中分组tag列表
                    group_relation_list: [],
                    group_id_list: [],  // 分组信息列表
                    process_rule: '',   // 处理规则 扩展字段
                },
                // 文件类型
                fileTypeList: [
                    {
                        name: '直接使用',
                        type: '1'
                    },
                    {
                        name: '拆分文件',
                        type: '2'
                    },
                    {
                        name: '安全下载',
                        type: '3'
                    },
                ],
                // 文件生成类型
                fileGenerationTypeList: [
                    {
                        name: '本地上传',
                        type: '1'
                    },
                    {
                        name: '自动生成',
                        type: '2'
                    },
                ],
                // 文件分类 1基线文件/2账号文件/3巡检报告文件/4汇总结果保存文件/5日志文件/6加密文件/9其他
                fileClassificationTypeList: [
                    {
                        name: '基线文件',
                        type: '1'
                    },
                    {
                        name: '账号文件',
                        type: '2'
                    },
                    {
                        name: '巡检报告文件',
                        type: '3'
                    },
                    {
                        name: '汇总结果保存文件',
                        type: '4'
                    },
                    {
                        name: '日志文件',
                        type: '5'
                    },
                    {
                        name: '加密文件',
                        type: '6'
                    },
                    {
                        name: '其他',
                        type: '9'
                    },
                ],
                groupAddForm: {
                    parentid: '',
                    name: '',
                    descr: ''
                },
                treeName: 'g1tree',
                // dynamicTags: [],
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                groupTreeData: [],
                fileList: [],
                osData: {},
                nodeEventType: 'createRoot', // 节点触发事件
                currentNodeData: {}, // 当前点击节点信息
                currentNodePath: '', // 当前点击节点路径
                // 分组、节点
                nodeInfo: {
                    groupingDirectory: '',
                    groupingName: '',
                    groupingDesc: ''
                },
                nodeFormRules: {
                    groupingName: [
                        {
                            required: true,
                            message: '请输入分组名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 20,
                            message: '长度在 2 到 20 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    groupingDesc: [
                        {
                            required: true,
                            message: '请输入分组描述!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 20,
                            message: '长度在 2 到 20 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ]
                },
                nodeBoxShow: false,
                // 文件
                showBoxType: 'addRow', // yum触发事件
                currentRowData: {},
                departmentDialogVisible: false,
                customControl: [
                    {
                        title: '新增子分组',
                        icon: 'el-icon-plus',
                        callback: this.customGroupAdd
                    }
                ],
                formRules: {
                    file_name: [
                        {
                            required: true,
                            message: '请输入文件名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    file_path: [
                        {
                            required: true,
                            message: '请上传文件!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    file_version: [
                        {
                            required: true,
                            message: '请输入文件版本!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    file_type: [
                        {
                            required: true,
                            message: '请上传文件!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    file_generate_type: [
                        {
                            required: true,
                            message: '请上传文件!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    file_class: [
                        {
                            required: true,
                            message: '请上传文件!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    group_relation_list: [
                        { required: true, message: '请选择分组名称', trigger: 'change' }
                    ]
                },
                dialogBoxShow: false,
                dialogDownloadBoxShow: false,
                downloadForm: {
                    downloadFileName: '',
                    downloadPassword: '',
                },
                downloadRules: {
                    downloadFileName: [
                        {
                            required: true,
                            message: '请输入汇聚文件名!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    downloadPassword: [
                        {
                            required: true,
                            message: '请输入汇聚加密密码!',
                            trigger: ['blur', 'change']
                        }
                    ]
                },
                dialogName: '添加文件',
                defaultYumProps: {
                    expandTrigger: 'hover',
                    children: 'children',
                    label: 'groupName',
                    value: 'groupId',
                    checkStrictly: true
                },

                filterText: '',
                defaultId: 1000,
                treeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'groupName',
                    id: 'groupId'
                },
                checkedKeys: [],

                dataList: [], // 文件列表
            }
        },
        filters: {
            handleGroupName: (list) => {
                return list.map(item => {
                    return item.group_name
                }).toString()
            }
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
            this.getGroupTree()

        },
        watch: {
            // 触发filter-node-method
            filterText(val) {
                this.$refs.tree.filter(val)
            },
            // 计算分组节点id
            'addForm.group_relation_list'(val) {
                this.addForm.group_id_list = []
                if (!val) {
                    return
                }
                val.forEach(item => {
                    this.addForm.group_id_list.push(item.group_id)
                })
            },
            checkedKeys() {
                this.search(1)
            }
        },
        computed: {
            isReadonly() {
                return this.showBoxType === 'viewDetail'
            }
        },
        methods: {
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handleClose(tag) {
                this.addForm.group_relation_list.splice(this.addForm.group_relation_list.indexOf(tag), 1)
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                if (_.map(this.addForm.group_relation_list, 'group_id').indexOf(data.group_id) === -1) {
                    this.addForm.group_relation_list.push({ 'group_name': data.group_name, 'group_id': data.group_id })
                }
                this.treeVisible = false
            },
            handleCheck(data, checkInfo) {
                this.checkedKeys = checkInfo.checkedKeys
            },
            // 上传文件
            UploadFile(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                fileManageService
                    .uploadFile(formData)
                    .then(res => {
                        if (res.flag) {
                            this.$message('上传成功')
                            this.addForm.file_path = res.biz_data
                        }
                    })
            },
            handleRemove() {
                this.addForm.file_path = ''
            },
            // 下载文件
            downloadFile(row) {
                if (row.file_type == '3') {
                    this.$message('该文件为安全下载类型文件，请通过加密汇聚方式下载！')
                    return
                }
                var path = row.file_path
                fileManageService.downloadFile({
                    file_path: path,
                    // "is_relative":"Y"   // Y|N  N: 下载路径为全路径  Y：下载路径为SFTP相对路径
                    is_relative: 'Y'
                }).then(res => {
                    if (res.byteLength > 0) {
                        this.$message('开始下载')
                        let pathArr = path.split('/')
                        let filename = pathArr[pathArr.length - 1]
                        this.$utils.createDownloadFileBlob(res, filename)
                    } else {
                        this.$message.error('暂无数据')
                    }
                })
            },
            // 更新文件
            updateFormData(req) {
                fileManageService.saveFile(req).then(res => {
                    if (res.flag) {
                        this.$message('更新成功')
                        this.search()
                        this.closeDialogBox()
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 创建文件
            createFormData(req) {
                fileManageService.saveFile(req).then(res => {
                    if (res.flag) {
                        this.$message('创建成功')
                        this.search()
                        this.closeDialogBox()
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 保存信息
            saveFormData() {
                this.$refs.addForm.validate(valid => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }
                    let req = this.addForm
                    if (this.showBoxType === 'editRow') {
                        req.file_id = this.currentRowData.file_id
                        this.updateFormData(req)
                    } else {
                        this.createFormData(req)
                    }
                })
            },
            // 删除文件
            removeRow(id) {
                this.$confirm('您确定要删除该文件吗？').then(() => {
                    this.loading = true
                    fileManageService.deleteFile({ file_id: id }).then(res => {
                        if (res.flag) {
                            this.$message('删除成功')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                })
            },
            // 查看文件
            setRowData(row) {
                this.addForm = this.$utils.deepClone(row)
            },
            showDownloadDialogBox() {
                if (this.multipleSelection.length < 1) {
                    this.$message('请先选择文件记录')
                    return
                }
                this.downloadForm = {
                    downloadFileName: '',
                    downloadPassword: ''
                }
                this.dialogDownloadBoxShow = true
            },
            closeDownloadDialogBox() {
                this.dialogDownloadBoxShow = false
            },
            convergeDownload() {
                var filePathList = []
                if (this.multipleSelection.length < 1) {
                    this.$message('请先选择文件记录')
                    return
                }
                this.$refs.downloadForm.validate(valid => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }
                    this.multipleSelection.forEach(item => { filePathList.push(item.file_path) })
                    fileManageService.convergeDownloadFile({
                        file_path: filePathList.join(','),
                        // "is_relative":"Y"   // Y|N  N: 下载路径为全路径  Y：下载路径为SFTP相对路径
                        file_name: this.downloadForm.downloadFileName,
                        password: this.downloadForm.downloadPassword,
                        is_relative: 'Y'
                    }).then(res => {
                        this.$message('开始下载')
                        // let pathArr = path.split('/')
                        let filename = this.downloadForm.downloadFileName + '.zip'
                        this.$utils.createDownloadFileBlob(res, filename)
                    })
                })
            },
            // 打开弹框
            showDialogBox(type, row) {
                this.fileList = []
                this.showBoxType = type
                if (row) {
                    this.currentRowData = row
                } else {
                    this.currentRowData = {
                        file_name: '',
                        file_version: '',
                        file_path: '',
                        file_type: '',
                        file_generate_type: '',
                        file_class: '',
                        file_desc: '',
                        // 已选中分组tag列表
                        group_relation_list: [],
                        group_id_list: [],  // 分组信息列表
                        process_rule: '',   // 处理规则 扩展字段
                    }
                }
                if (type === 'addRow') {
                    this.dialogName = '添加文件'
                } else if (type === 'editRow') {
                    this.dialogName = '编辑文件'
                } else {
                    this.dialogName = '文件详情'
                }
                this.setRowData(this.currentRowData)
                this.dialogBoxShow = true
            },
            closeDialogBox() {
                this.dialogBoxShow = false
            },

            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    yumFileGroupIdList: this.checkedKeys
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                fileManageService
                    .queryFileList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
                    .catch((res) => {
                        this.showErrorTip(res)
                        this.loading = false
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
                this.search(1)
            },
            // 获取分组树
            getGroupTree() {
                groupDataService.getQueryGroupTree().then((res) => {
                    this.groupTreeData = res
                })
            },
            // 关闭弹框
            addCancel() {
                this.nodeBoxShow = this.dialogBoxShow = false
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
