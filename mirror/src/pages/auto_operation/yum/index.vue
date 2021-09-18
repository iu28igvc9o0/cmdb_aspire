<template>
    <div>
        <div class="displaybox boxalignstart">
            <div class="content-left">
                <el-row :gutter="10">
                    <el-col :span="8">
                        <el-button class="sidebar-btn"
                                   type="primary"
                                   @click="showNodeBox(null, {}, 'createRoot')">+根节点</el-button>
                    </el-col>
                    <el-col :span="16">
                        <el-input v-model="filterText"
                                  placeholder="输入分组名称查询"
                                  clearable></el-input>
                    </el-col>
                </el-row>
                <el-tree ref="tree"
                         show-checkbox
                         node-key="groupId"
                         :data="treeData"
                         :props="defaultProps"
                         :default-expanded-keys="[1]"
                         :expand-on-click-node="false"
                         :filter-node-method="filterNode"
                         @check="handleCheck">
                    <span class="tree-node"
                          slot-scope="{ node, data }">
                        <span>{{ node.label }}</span>
                        <span class="el-tree-btn">
                            <el-button type="text"
                                       title="新增子节点"
                                       icon="el-icon-plus"
                                       @click="showNodeBox(node, data, 'addChildren')"></el-button>
                            <el-button type="text"
                                       title="编辑"
                                       icon="el-icon-edit"
                                       @click="showNodeBox(node, data, 'editNode')"></el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       :disabled="data.children && data.children.length"
                                       @click="removeNode(data, 'removeNode')"></el-button>
                        </span>
                    </span>
                </el-tree>
            </div>
            <div class="content-right yw-dashboard components-container boxflex01 p10">
                <el-form class="components-condition yw-form"
                         :model="formSearch"
                         @keyup.enter.native="search(1)"
                         ref="formSearch"
                         :inline="true"
                         label-width="65px">
                    <el-form-item label="文件名称"
                                  prop="nameLike">
                        <el-input v-model="formSearch.nameLike"
                                  placeholder="请输入文件名称"
                                  clearable></el-input>
                    </el-form-item>
                    <el-form-item label="文件路径"
                                  prop="uploadFilePathLike">
                        <el-input v-model="formSearch.uploadFilePathLike"
                                  placeholder="请输入文件路径"
                                  clearable></el-input>
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
                                   @click="showYumBox('addYum')">新建</el-button>
                    </div>
                    <el-table :data="yumSourceList"
                              style="margin-top:10px"
                              class="yw-el-table"
                              :header-cell-style="{background:'#E8F0FC',color:'#3A4154',height:'19px'}"
                              stripe
                              tooltip-effect="dark"
                              border
                              height="calc(100vh - 275px)"
                              v-loading="loading">
                        <el-table-column sortable
                                         prop="name"
                                         label="文件名称"></el-table-column>
                        <el-table-column sortable
                                         prop="version"
                                         label="文件版本"
                                         width="100"></el-table-column>
                        <el-table-column sortable
                                         prop="uploadFilePath"
                                         label="文件路径"
                                         min-width="200"></el-table-column>
                        <el-table-column label="操作"
                                         width="180">
                            <template slot-scope="scope">
                                <el-button type="text"
                                           title="下载"
                                           icon="el-icon-download"
                                           @click="downloadYum(scope.row.uploadFilePath)"></el-button>
                                <el-button type="text"
                                           title="详情"
                                           icon="el-icon-view"
                                           @click="showYumBox('viewDetail', scope.row)"></el-button>
                                <el-button type="text"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           @click="showYumBox('editYum', scope.row)"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="removeYum(scope.row.id)"></el-button>
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
                </div>
            </div>
        </div>

        <!-- 新建、编辑分组 -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="nodeBoxShow"
                   width="500">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="nodeForm"
                         :model="nodeInfo"
                         :rules="nodeFormRules"
                         label-width="100px">
                    <el-form-item label="分组目录"
                                  prop="groupingDirectory">
                        <el-input v-model="nodeInfo.groupingDirectory"
                                  disabled></el-input>
                    </el-form-item>
                    <el-form-item label="分组名称"
                                  prop="groupingName">
                        <el-input v-model="nodeInfo.groupingName"
                                  placeholder="请输入分组名称"
                                  clearable></el-input>
                    </el-form-item>
                    <el-form-item label="分组描述"
                                  prop="groupingDesc">
                        <el-input type="textarea"
                                  rows="4"
                                  v-model="nodeInfo.groupingDesc"
                                  placeholder="请输入分组描述"
                                  clearable></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveNode()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
        <!-- 新建、编辑yum源文件 -->
        <el-dialog class="yw-dialog"
                   :title="yumDialogName"
                   :visible.sync="yumBoxShow"
                   width="500">
            <section class="yw-dialog-main middle-size-form">
                <el-form class="yw-form is-required"
                         ref="yumForm"
                         :model="yumInfo"
                         :rules="yumFormRules"
                         label-width="100px">
                    <el-form-item label="文件名称"
                                  prop="name">
                        <el-input v-model="yumInfo.name"
                                  placeholder="请输入文件名称"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="YUM源文件"
                                  prop="uploadFilePath">
                        <el-input class="middle-size-input"
                                  v-model="yumInfo.uploadFilePath"
                                  placeholder="请上传YUM源文件"
                                  readonly></el-input>
                        <el-upload v-show="!isReadonly"
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
                                  prop="version">
                        <el-input v-model="yumInfo.version"
                                  placeholder="请输入文件版本"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="分组目录"
                                  prop="yumFileGroupIds">
                        <el-cascader v-model="yumInfo.yumFileGroupIds"
                                     :options="treeData"
                                     :props="defaultYumProps"
                                     :disabled="isReadonly"
                                     :clearable="!isReadonly"></el-cascader>
                    </el-form-item>
                    <el-form-item label="文件描述"
                                  prop="yumDesc">
                        <el-input type="textarea"
                                  rows="4"
                                  v-model="yumInfo.yumDesc"
                                  placeholder="请输入文件描述"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="分组名称"
                                  prop="groupTagids">
                        <el-tag :key="tag.group_id"
                                style="margin-right: 5px;"
                                v-for="tag in yumInfo.groupTagids"
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
                           @click="saveYum()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   title="新增子分组"
                   :visible.sync="departmentDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="410px"
                   :append-to-body="true"
                   :before-close="handleDepartmentDialogClose">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="groupAddForm"
                         :model="groupAddForm"
                         label-width="100px">
                    <el-form-item label="子分组名称"
                                  prop="name">
                        <el-input v-model="groupAddForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="子分组描述">
                        <el-input v-model="groupAddForm.descr"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleUpdateDepartment">确定</el-button>
                <el-button @click="departmentDialogVisible = false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAutoOperationYumServicesFactory from 'src/services/auto_operation/rb-auto-operation-yum-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import comtree from './../components/tree.vue'
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'
    export default {
        name: 'YumList',
        components: { comtree },
        data() {
            const that = this
            return {
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
                dialogName: '新增分组',
                // yum源文件
                yumEventType: 'addYum', // yum触发事件
                currentYumData: {},
                yumInfo: {
                    name: '',
                    uploadFilePath: '',
                    version: '',
                    yumFileGroupId: '',
                    yumFileGroupIds: '',
                    yumDesc: '',
                    groupTagids: []
                },
                departmentDialogVisible: false,
                customControl: [
                    {
                        title: '新增子分组',
                        icon: 'el-icon-plus',
                        callback: that.customGroupAdd
                    }
                ],
                yumFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请输入文件名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 20,
                            message: '长度在 2 到 20 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    uploadFilePath: [
                        {
                            required: true,
                            message: '请上传YUM源文件!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    version: [
                        {
                            required: true,
                            message: '请输入文件版本!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 20,
                            message: '长度在 2 到 20 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    yumFileGroupId: [
                        {
                            required: true,
                            message: '请选择分组目录!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    yumFileGroupIds: [
                        {
                            required: true,
                            message: '请选择分组目录!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    yumDesc: [
                        {
                            required: true,
                            message: '请输入文件描述!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 20,
                            message: '长度在 2 到 20 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    groupTagids: [
                        { required: true, message: '请选择分组名称', trigger: 'change' }
                    ]
                },
                yumBoxShow: false,
                yumDialogName: '新建YUM源文件',
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

                yumSourceList: [], // yum源文件列表

                formSearch: {
                    uploadFilePathLike: '',
                    nameLike: '',
                }
            }
        },
        mixins: [rbAutoOperationMixin],
        mounted() {
            this.search()
            this.fetchOsDistributionList()
            this.queryYumFileGroupTreeList()
            this.getGroupTree()

        },
        watch: {
            // 触发filter-node-method
            filterText(val) {
                this.$refs.tree.filter(val)
            },
            // 计算单个分组节点id
            'yumInfo.yumFileGroupIds'(val) {
                if (typeof val === 'number') {
                    this.yumInfo.yumFileGroupId = val
                } else {
                    this.yumInfo.yumFileGroupId = (val && val[val.length - 1]) || ''
                }
            },
            checkedKeys() {
                this.search(1)
            }
        },
        computed: {
            isReadonly() {
                return this.yumEventType === 'viewDetail'
            }
        },
        methods: {
            // 快速新增分组
            customGroupAdd(node, data) {
                this.departmentDialogVisible = true
                this.groupAddForm.parentid = data.group_id
            },
            // 关闭弹窗
            handleDepartmentDialogClose() {
                this.departmentDialogVisible = false
            },
            // 新增分组
            handleUpdateDepartment() {
                this.$refs['groupAddForm'].validate((valid) => {
                    if (valid) {
                        const params = {
                            parent_id: this.groupAddForm.parentid,
                            group_name: this.groupAddForm.name,
                            group_desc: this.groupAddForm.descr
                        }
                        groupDataService.saveGroup(params).then(() => {
                            this.$message({
                                message: '新增分组成功',
                                type: 'success'
                            })
                            this.getGroupTree()
                            this.resetGroupAddForm()
                            this.handleDepartmentDialogClose()
                        }).catch(() => {
                            this.$message({
                                message: '操作失败',
                                type: 'error'
                            })
                        })
                    } else {
                        return false
                    }
                })
            },
            // 获取分组树
            getGroupTree() {
                groupDataService.getQueryGroupTree().then((res) => {
                    this.groupTreeData = res
                    // if (res.length > 0) {
                    //   this.deviceAuthDataExpanded = [res[0].group_id]
                    // }
                })
            },
            resetGroupAddForm() {
                this.groupAddForm = {
                    parentid: '',
                    name: '',
                    descr: ''
                }
            },
            handleClose(tag) {
                this.yumInfo.groupTagids.splice(this.yumInfo.groupTagids.indexOf(tag), 1)
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                if (_.map(this.yumInfo.groupTagids, 'group_id').indexOf(data.group_id) === -1) {
                    this.yumInfo.groupTagids.push({ 'group_name': data.group_name, 'group_id': data.group_id })
                }
                this.treeVisible = false
            },
            handleCheck(data, checkInfo) {
                this.checkedKeys = checkInfo.checkedKeys
            },
            // 上传yum源文件
            UploadFile(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                rbAutoOperationYumServicesFactory
                    .uploadYumLocalFile(formData)
                    .then(res => {
                        if (res.flag) {
                            this.$message('上传成功')
                            this.yumInfo.uploadFilePath = res.biz_data
                        }
                    })
            },
            handleRemove() {
                this.yumInfo.uploadFilePath = ''
            },
            // 下载Yum源文件
            downloadYum(path) {
                rbAutoOperationYumServicesFactory.downloadYumFile({ file_path: path }).then(res => {
                    this.$message('开始下载')
                    let pathArr = path.split('/')
                    let filename = pathArr[pathArr.length - 1]
                    this.createDownloadFile(res, filename)
                })
            },
            createDownloadFile(res, filename) {
                let element = document.createElement('a')
                element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(res))
                element.setAttribute('download', filename)
                element.style.display = 'none'
                document.body.appendChild(element)
                element.click()
                document.body.removeChild(element)
            },
            // 更新Yum源文件
            updateYum(req) {
                rbAutoOperationYumServicesFactory.updateYumSourceModel(req).then(res => {
                    if (res.flag) {
                        this.$message('更新成功')
                        this.search()
                        this.closeYumBox()
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 创建Yum源文件
            createYum(req) {
                rbAutoOperationYumServicesFactory.createYumSourceModel(req).then(res => {
                    if (res.flag) {
                        this.$message('创建成功')
                        this.search()
                        this.closeYumBox()
                    } else {
                        this.$message.error(res.error_tip)
                    }
                })
            },
            // 保存Yum源文件
            saveYum() {
                this.$refs.yumForm.validate(valid => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }
                    const groupIdList = []
                    this.yumInfo.groupTagids.forEach(item => {
                        groupIdList.push(item.group_id)
                    })
                    let req = {
                        name: this.yumInfo.name,
                        description: this.yumInfo.yumDesc,
                        osDistributionId: this.osData.id, // 关联的操作系统版本
                        uploadFilePath: this.yumInfo.uploadFilePath, // Yum源文件上传路径
                        version: this.yumInfo.version,
                        yumFileGroupId: this.yumInfo.yumFileGroupId, // Yum文件分组id
                        group_id_list: groupIdList
                    }
                    if (this.yumEventType === 'editYum') {
                        req.id = this.currentYumData.id
                        this.updateYum(req)
                    } else {
                        this.createYum(req)
                    }
                })
            },
            // 保存Yum源文件
            removeYum(id) {
                this.$confirm('您确定要删除该文件吗？').then(() => {
                    this.loading = true
                    rbAutoOperationYumServicesFactory.removeYumSource(id).then(res => {
                        if (res.flag) {
                            this.$message('删除成功')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                })
            },
            // 获取操作系统发行版本列表
            fetchOsDistributionList() {
                rbAutoOperationYumServicesFactory.fetchOsDistributionList().then(res => {
                    this.osData = res[0]
                })
            },
            // 查看yum源文件
            viewDetail(row) {
                this.yumInfo = {
                    name: row.name,
                    yumDesc: row.description,
                    uploadFilePath: row.uploadFilePath,
                    version: row.version,
                    yumFileGroupId: row.yumFileGroupId,
                    yumFileGroupIds: row.yumFileGroupId,
                    groupTagids: row.group_relation_list || []
                }
            },
            // 新建yum源文件
            showYumBox(type, row) {
                this.fileList = []
                this.yumEventType = type
                if (row) {
                    this.currentYumData = row
                } else {
                    this.currentYumData = {}
                }
                if (type === 'addYum') {
                    this.yumDialogName = '新建YUM源记录'
                    this.viewDetail(this.currentYumData)
                } else if (type === 'editYum') {
                    this.yumDialogName = '编辑YUM源记录'
                    this.viewDetail(this.currentYumData)
                } else {
                    this.yumDialogName = 'YUM源记录详情'
                    this.viewDetail(this.currentYumData)
                }
                this.yumBoxShow = true
            },
            closeYumBox() {
                this.yumBoxShow = false
            },

            // 最近任务执行记录
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    yumFileGroupIdList: this.checkedKeys
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                rbAutoOperationYumServicesFactory
                    .queryYumSourceList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.yumSourceList = res.dataList
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },

            // 过滤节点
            filterNode(value, data) {
                if (!value) return true
                return data.groupName.indexOf(value) !== -1
            },
            // 获取节点路径
            getNodePath(data, path = '') {
                path = '/' + data.data.groupName + path
                if (data.level !== 1) {
                    return this.getNodePath(data.parent, path)
                } else {
                    return path
                }
            },
            // 节点信息弹框
            showNodeBox(node, data, type) {
                this.nodeEventType = type
                this.nodeInfo.groupingDirectory = node && this.getNodePath(node)
                this.nodeInfo.groupingName = ''
                this.nodeInfo.groupingDesc = ''
                this.currentNodeData = data
                if (type === 'addChildren') {
                    this.dialogName = '新增分组'
                } else if (type === 'createRoot') {
                    this.dialogName = '新增分组'
                    this.nodeInfo.groupingDirectory = '根目录'
                } else if (type === 'editNode') {
                    this.dialogName = '编辑分组'
                    this.nodeInfo.groupingName = data.groupName
                    this.nodeInfo.groupingDesc = data.description
                }
                this.nodeBoxShow = true
            },
            closeNodeBox() {
                this.nodeBoxShow = false
            },
            // 统一处理节点操作及提示
            showNodeTip(res, tips) {
                if (res.flag) {
                    let currentNode = this.$refs.tree.getNode(this.currentNodeData.groupId)
                    if (this.nodeEventType === 'addChildren') {
                        this.appendNode(currentNode, res)
                    } else if (this.nodeEventType === 'createRoot') {
                        this.treeData.push(res.biz_data)
                    } else if (this.nodeEventType === 'editNode') {
                        this.modifyNode(currentNode)
                    } else if (this.nodeEventType === 'removeNode') {
                        this.spliceNode(currentNode)
                    }

                    this.closeNodeBox()
                    this.$message(tips)
                } else {
                    this.$message.error(res.error_tip)
                }
            },
            // 不重新请求数据，静默更新，避免重新渲染树
            appendNode(currentNode, res) {
                if (!currentNode.data.children) {
                    this.$set(currentNode.data, 'children', [])
                }
                currentNode.data.children.push(res.biz_data)
            },
            modifyNode(currentNode) {
                this.$set(currentNode.data, 'groupName', this.nodeInfo.groupingName)
                this.$set(currentNode.data, 'description', this.nodeInfo.groupingDesc)
            },
            spliceNode(currentNode) {
                const parent = currentNode.parent
                const children = parent.data.children || parent.data
                const index = children.findIndex(item => {
                    return item.groupId === this.currentNodeData.groupId
                })
                children.splice(index, 1)
            },
            // 创建分组、节点
            createNode() {
                let req = {
                    groupName: this.nodeInfo.groupingName,
                    description: this.nodeInfo.groupingDesc,
                    level: this.currentNodeData.level + 1 || 1,
                    parentGroupId: this.currentNodeData.groupId || -1 // 父节点id，根节点id为 -1
                }
                rbAutoOperationYumServicesFactory.createYumFileGroup(req).then(res => {
                    this.showNodeTip(res, '保存成功')
                })
            },
            // 更新分组、节点
            updateNode() {
                let req = {
                    groupName: this.nodeInfo.groupingName,
                    description: this.nodeInfo.groupingDesc,
                    groupId: this.currentNodeData.groupId,
                    level: this.currentNodeData.level,
                    parentGroupId: this.currentNodeData.parentGroupId // 父节点id
                }
                rbAutoOperationYumServicesFactory.updateYumFileGroup(req).then(res => {
                    this.showNodeTip(res, '更新成功')
                })
            },
            // 删除分组、节点
            removeNode(data, type) {
                this.nodeEventType = type
                this.currentNodeData = data
                this.$confirm('您确认要删除该分组吗？').then(() => {
                    rbAutoOperationYumServicesFactory
                        .removeYumFileGroup(data.groupId)
                        .then(res => {
                            this.showNodeTip(res, '删除成功')
                        })
                })
            },
            // 保存分组、节点
            saveNode() {
                this.$refs.nodeForm.validate(valid => {
                    if (!valid) {
                        this.$message('请先完善信息')
                        return
                    }
                    if (this.nodeEventType === 'createRoot' || this.nodeEventType === 'addChildren') {
                        this.createNode()
                    } else if (this.nodeEventType === 'editNode') {
                        this.updateNode()
                    }
                })
            },
            // 查询yum文件分组树
            queryYumFileGroupTreeList() {
                rbAutoOperationYumServicesFactory
                    .queryYumFileGroupTreeList()
                    .then(res => {
                        this.treeData = this.handleTreeData(res, -1)
                    })
            },
            // 扁平结构数组转换为树形结构
            handleTreeData(list, parentId) {
                let treeData = []
                for (let node of list) {
                    if (node.parentGroupId === parentId) {
                        let children = this.handleTreeData(list, node.groupId)
                        if (children.length) {
                            node.children = children
                        }
                        treeData.push(node)
                    }
                }
                return treeData
            },
            // 关闭弹框
            addCancel() {
                this.nodeBoxShow = this.yumBoxShow = false
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../assets/global.scss";
    .tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 12px;
        padding-right: 8px;
    }
    .el-tree-btn {
        display: none;
    }
    .el-tree-node__content:hover .el-tree-btn {
        display: inline-block;
    }
</style>
