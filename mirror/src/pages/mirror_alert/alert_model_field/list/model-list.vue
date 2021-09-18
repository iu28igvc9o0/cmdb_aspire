<template>
    <div>
        <div class="alertListQyery">
            <el-row>
                <el-col :span="6">
                    <div class="com-tree">
                        <div>
                            <span>
                                <el-input placeholder="输入关键字进行过滤"
                                          class="search-box"
                                          v-model="filterText"></el-input>
                            </span>
                            <!-- <span style="padding-left:20px;">
                                <el-button type="text" icon="el-icon-circle-plus-outline" @click="insertAlertModelDialog = true"></el-button>
                            </span> -->
                        </div>
                        <div class="yw-el-tree">
                            <el-tree ref="modelTree"
                                     :data="modelList"
                                     :filter-node-method="filterNode"
                                     node-key="id"
                                     highlight-current="true"
                                     :props="defaultProps"
                                     default-expand-all
                                     :expand-on-click-node="false"
                                     @node-click="handleNodeClick">
                                <span class="custom-tree-node"
                                      slot-scope="{ node, data }">
                                    <span>{{ node.label }}</span>
                                    <span>
                                        <el-button type="text"
                                                   size="mini"
                                                   icon="el-icon-bottom-right"
                                                   title="添加"
                                                   v-if="data.parentId == 'all' || data.parentId == '-1'"
                                                   @click="() => append(node,data)">
                                        </el-button>
                                        <el-button type="text"
                                                   size="mini"
                                                   icon="el-icon-edit"
                                                   title="编辑"
                                                   v-if="data.id != 'all'"
                                                   @click="() => editModel(node,data)">
                                        </el-button>
                                        <!-- <el-button
                                            type="text"
                                            size="mini"
                                            icon="el-icon-delete"
                                            title="删除"
                                            v-if="data.id != 'all'"
                                            @click="() => remove(node, data)">
                                        </el-button> -->
                                    </span>
                                </span>
                            </el-tree>
                        </div>
                    </div>
                </el-col>
                <el-col :span="18">
                    <div class="com-tree">
                        <div class="person-manage__bar">
                            <div class="person-manage__bar-left">
                                <el-input placeholder="输入字段名称或字段代码"
                                          class="search-box"
                                          v-model="searchText">
                                    <i slot="suffix"
                                       class="el-input__icon el-icon-search"
                                       @click="getSearchText()"></i>
                                </el-input>
                            </div>
                            <div class="person-manage__bar-right">
                                <el-button class="button-box"
                                           round
                                           @click="addAlertField()"
                                           :disabled="!fieldButonShow">新增</el-button>
                                <el-button class="button-box"
                                           round
                                           @click="synchronizeField()"
                                           :disabled="!fieldButonShow">同步告警模型</el-button>
                            </div>
                        </div>
                        <div class="yw-el-table-wrap">
                            <el-table class="yw-el-table"
                                      :data="alertModelFieldLists"
                                      stripe
                                      border
                                      height="500px"
                                      tooltip-effect="dark">
                                <el-table-column prop="fieldName"
                                                 label="字段姓名"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="fieldCode"
                                                 label="字段代码"
                                                 width="100"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="dataType"
                                                 label="数据类型"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="dataTip"
                                                 label="字段提示信息"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="fieldType"
                                                 label="字段类型"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="isLock"
                                                 label="是否锁定"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="paramCode"
                                                 label="CI码表字段"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="ciCode"
                                                 label="CI码表编码"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="isListShow"
                                                 label="是否列表展示"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column prop="isDetailShow"
                                                 label="是否详情展示"
                                                 align="center"
                                                 show-overflow-tooltip></el-table-column>
                                <el-table-column label="操作"
                                                 width="120px">
                                    <template slot-scope="scope">
                                        <el-button type="text"
                                                   icon="el-icon-view"
                                                   title="详情"
                                                   @click="getAlertFieldDetail(scope.row)"></el-button>
                                        <el-button type="text"
                                                   icon="el-icon-edit"
                                                   title="编辑"
                                                   @click="updateAlertField(scope.row)"></el-button>
                                        <el-button type="text"
                                                   icon="el-icon-delete"
                                                   title="删除"
                                                   v-if="scope.row.fieldType === '扩展字段' && scope.row.isLock === '否'"
                                                   @click="deleteAlertField(scope.row)"></el-button>
                                        <el-button type="text"
                                                   icon="el-icon-lock"
                                                   title="锁定"
                                                   v-if="scope.row.fieldType === '扩展字段' && scope.row.isLock === '否'"
                                                   @click="updateLockStatus(scope.row,'1')"></el-button>
                                        <!--<el-button type="text"-->
                                        <!--icon="el-icon-unlock"-->
                                        <!--title="非锁定"-->
                                        <!--v-if="scope.row.fieldType === '扩展字段' && scope.row.isLock === '是'"-->
                                        <!--@click="updateLockStatus(scope.row,'0')"></el-button>-->
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                        <div>
                            <YwPagination @handleSizeChange="handleSizeChange"
                                          @handleCurrentChange="handleCurrentChange"
                                          :current-page="currentPage"
                                          :page-sizes="pageSizes"
                                          :page-size="pageSize"
                                          :total="total"></YwPagination>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>

        <!-- dialog -->
        <!-- 新增告警模型 -->
        <el-dialog class="yw-dialog"
                   title="告警模型"
                   width="30%"
                   :visible.sync="insertAlertModelDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false"
                   :modal="false">
            <div>
                <el-form ref='AlertModeForm'
                         :model="AlertModeForm"
                         status-icon
                         :rules="AlertModeRules"
                         label-width="100px">
                    <el-form-item label="父模型名称"
                                  prop="parentName">
                        <el-input style="width: 250px;"
                                  v-model="AlertModeForm.parentName"
                                  disabled></el-input>
                    </el-form-item>
                    <el-form-item label="模型名称"
                                  prop="modelName">
                        <el-input style="width: 250px;"
                                  v-model="AlertModeForm.modelName"></el-input>
                    </el-form-item>
                    <el-form-item label="表名"
                                  prop="tableName"
                                  v-if="tableNameShow">
                        <el-input style="width: 250px;"
                                  v-model="AlertModeForm.tableName"></el-input>
                    </el-form-item>
                    <el-form-item label="排序"
                                  prop="sort">
                        <el-input style="width: 250px;"
                                  v-model.number="AlertModeForm.sort"></el-input>
                    </el-form-item>
                    <el-form-item label="备注"
                                  prop="description">
                        <el-input style="width: 250px;"
                                  v-model="AlertModeForm.description"></el-input>
                    </el-form-item>
                    <section class="btn-wrap">
                        <el-button type="primary"
                                   size="small"
                                   @click="insertModelSubmit()"
                                   v-if="!updateShow">提交</el-button>
                        <el-button type="primary"
                                   size="small"
                                   @click="updateModelSubmit()"
                                   v-else>编辑</el-button>
                        <el-button size="small"
                                   @click="cancel('model')">取消</el-button>
                    </section>
                </el-form>
            </div>
        </el-dialog>
        <!-- 新增告警模型字段 -->
        <el-dialog class="yw-dialog"
                   title="新增告警模型字段"
                   width="50%"
                   :visible.sync="addAlertFieldDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <alert-model-field-add ref="alertModelFieldAdd"
                                       v-if="addAlertFieldDialog"
                                       :type="'add'"
                                       :tableName="modelTableName">
                </alert-model-field-add>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addAlertFieldSubmit()">创建</el-button>
                <el-button @click="cancel('addField')">取消</el-button>
            </section>
        </el-dialog>
        <!-- 告警模型字段详情 -->
        <el-dialog class="yw-dialog"
                   title="告警模型字段详情"
                   width="50%"
                   :visible.sync="alertFieldDetailDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <alert-model-field-add ref="alertModelFieldAdd"
                                       v-if="alertFieldDetailDialog"
                                       :alertFieldDetail=alertFieldDetail
                                       :type="'detail'"
                                       :tableName="modelTableName">
                </alert-model-field-add>
            </section>

        </el-dialog>
        <!-- 更新告警模型字段 -->
        <el-dialog class="yw-dialog"
                   title="更新告警模型字段"
                   width="50%"
                   :visible.sync="updateAlertFieldDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :modal="false">
            <section class="yw-dialog-main">
                <alert-model-field-add ref="alertModelFieldAdd"
                                       v-if="updateAlertFieldDialog"
                                       :alertFieldDetail=alertFieldDetail
                                       :type="'update'"
                                       :tableName="modelTableName">
                </alert-model-field-add>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="updateAlertFieldSubmit()">更新</el-button>
                <el-button @click="cancel('updateField')">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbAlertModelServicesFactory from 'src/services/alert/rb-alert-model-services.factory.js'
    import rbAlertModelFieldServicesFactory from 'src/services/alert/rb-alert-model-field-services.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    import alertModelFieldAdd from 'src/pages/mirror_alert/alert_model_field/field/add/alert-model-field-add.vue'

    export default {
        mixins: [QueryObject],
        props: [],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            alertModelFieldAdd,
        },
        data() {
            return {
                filterText: '',
                modelData: [],
                modelList: [],
                defaultProps: {
                    children: 'childList',
                    label: 'modelName'
                },
                defaultCheckedKeys: [],
                deviceAuthDataExpanded: [],
                alertModelId: '',
                alertModelFieldLists: [],
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                insertAlertModelDialog: false,
                AlertModeForm: {
                    id: '',
                    parentName: '',
                    parentId: '',
                    modelName: '',
                    tableName: '',
                    sort: '',
                    description: ''
                },
                AlertModeRules: {
                    modelName: [
                        { required: true, message: '请输入模型名称', trigger: 'blur' },
                        { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
                    ],
                    tableName: [
                        { required: true, message: '请输入模型表名称', trigger: 'blur' }
                    ],
                    sort: [
                        { required: true, message: '排序不能为空' },
                        { type: 'number', message: '排序必须为数字值' }
                    ],
                },
                addAlertFieldDialog: false,
                alertFieldDetailDialog: false,
                updateAlertFieldDialog: false,
                alertFieldDetail: {},
                searchText: '',
                modelTableName: '',
                updateShow: false,
                tableNameShow: true,
                fieldButonShow: true,
            }
        },
        mounted() {
            this.getAlertModelTreeData()
            this.getAlertModelList()
        },
        methods: {
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.getAlertFieldListByModelId()
            },
            handleCurrentChange(val) {
                this.currentPage = val
                this.getAlertFieldListByModelId()
            },
            getAlertModelList() {
                rbAlertModelServicesFactory.getAlertModelList().then((res) => {
                    this.modelData = res
                })
            },
            // 获取告警模型树
            getAlertModelTreeData() {
                rbAlertModelServicesFactory.getAlertModelTreeData().then((res) => {
                    this.modelList = res
                    if (res.length > 0) {
                        this.alertModelId = null
                        // this.modelTableName = res[0].tableName
                        this.fieldButonShow = false
                        this.getAlertFieldListByModelId()
                    }
                })
            },
            getAlertFieldListRequest() {
                let obj = {}
                obj.modelId = this.alertModelId
                obj.pageNum = this.currentPage
                obj.pageSize = this.pageSize
                obj.searchText = this.searchText
                return obj
            },
            formatFieldList(obj) {
                obj.forEach((item) => {
                    item.fieldType = item.fieldType === '1' ? '基础字段' : '扩展字段'
                    item.isLock = item.isLock === '0' ? '否' : '是'
                    item.isListShow = item.isListShow === '0' ? '否' : '是'
                    item.isDetailShow = item.isDetailShow === '0' ? '否' : '是'
                })
                return obj
            },
            // 获取告警模型数据列表
            getAlertFieldListByModelId() {
                rbAlertModelFieldServicesFactory.getAlertFieldListByModelId(this.getAlertFieldListRequest()).then((res) => {
                    this.total = res.count
                    this.alertModelFieldLists = this.formatFieldList(res.result)
                })
            },

            filterNode(value, data, node) {
                if (!value) return true
                return node.label.indexOf(value) !== -1
            },
            handleNodeClick(data) {
                this.fieldButonShow = true
                if (data.parentId === '-1') {
                    this.alertModelId = null
                    this.fieldButonShow = false
                } else if (data.parentId === 'all') {
                    let list = []
                    data.childList.forEach((item) => {
                        list.push(item.id)
                    })
                    this.alertModelId = list.length > 0 ? list.join(',') : data.id
                    this.fieldButonShow = false
                } else {
                    this.alertModelId = data.id
                }
                this.currentPage = 1
                this.getAlertFieldListByModelId()
                this.getModelTableName()
            },
            cancel(type) {
                if (type === 'model') {
                    this.insertAlertModelDialog = false
                    if (this.updateShow) {
                        this.updateShow = false
                    }
                    this.tableNameShow = true
                    this.AlertModeForm.modelName = ''
                    this.AlertModeForm.tableName = ''
                    this.AlertModeForm.sort = ''
                    this.AlertModeForm.description = ''
                    this.$refs['AlertModeForm'].resetFields()
                } else if (type === 'addField') {
                    this.addAlertFieldDialog = false
                } else if (type === 'updateField') {
                    this.updateAlertFieldDialog = false
                }
            },
            insertModelSubmit() {
                this.$refs['AlertModeForm'].validate((valid) => {
                    if (valid) {
                        this.insertAlertModel(this.AlertModeForm)
                    } else {
                        console.log('error submit!!')
                        return false
                    }
                })
            },
            insertAlertModel(obj) {
                let boolean = true
                let message = ''
                this.modelData.forEach((item) => {
                    if (item.modelName === obj.modelName) {
                        boolean = false
                        message = '模型名称已经存在，请重新填写!'
                        return false
                    }
                    if (this.tableNameShow && item.tableName === obj.tableName) {
                        boolean = false
                        message = '模型表名称已经存在，请重新填写!'
                        return false
                    }
                })
                if (!boolean) {
                    this.$alert(message, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                rbAlertModelServicesFactory.insertAlertModel(obj).then((res) => {
                    if (res === 'SUCCESS') {
                        this.$message({
                            message: '添加成功!',
                            type: 'success'
                        })
                        this.getAlertModelTreeData()
                        this.getAlertModelList()
                    } else {
                        this.$message.error('添加失败!')
                    }
                }).catch(() => {
                    this.$message.error('添加失败')
                })
                this.cancel('model')
            },
            validateAddRequest(type) {
                let alertFieldForm = this.$refs.alertModelFieldAdd.alertFieldForm
                let obj = {}
                obj.alertFieldForm = alertFieldForm
                obj.flag = true
                if (type === 'add') {
                    this.alertModelFieldLists.forEach((item) => {
                        if (item.fieldName === alertFieldForm.fieldName) {
                            obj.flag = false
                            obj.msg = '告警模型字段名称重复,请重新输入!'
                            return obj
                        }
                        if (item.fieldCode === alertFieldForm.fieldCode) {
                            obj.flag = false
                            obj.msg = '告警模型字段编码重复,请重新输入!'
                            return obj
                        }
                    })
                }
                if (!alertFieldForm.fieldName) {
                    obj.flag = false
                    obj.msg = '请输入告警模型字段名称!'
                    return obj
                } else if (!alertFieldForm.fieldCode) {
                    obj.flag = false
                    obj.msg = '请输入告警模型字段编码!'
                    return obj
                } else if (!alertFieldForm.JDBCType) {
                    obj.flag = false
                    obj.msg = '请输入告警模型字段数据类型!'
                    return obj
                } else {
                    return obj
                }
            },
            getAlertFieldRequest(alertFieldForm) {
                let obj = {}
                obj.fieldName = alertFieldForm.fieldName
                obj.fieldCode = alertFieldForm.fieldCode
                obj.fieldType = alertFieldForm.fieldType
                obj.dataType = alertFieldForm.JDBCType
                obj.dataLength = alertFieldForm.JDBCLength
                obj.dataTip = alertFieldForm.JDBCTip
                obj.isLock = alertFieldForm.isLock
                obj.isCiParams = alertFieldForm.isCiParams
                if (alertFieldForm.isCiParams === '1') {
                    obj.paramsName = alertFieldForm.paramsName
                }
                obj.paramCode = alertFieldForm.paramCode
                obj.ciCode = alertFieldForm.CICode
                obj.isListShow = alertFieldForm.isListShow
                if (alertFieldForm.isListShow === '1') {
                    obj.listShowSort = alertFieldForm.listShowSort
                    obj.listShowName = alertFieldForm.listShowName
                    obj.listShowPattern = alertFieldForm.listShowPattern
                    obj.tableColumnWidth = alertFieldForm.tableColumnWidth
                }
                obj.isDetailShow = alertFieldForm.isDetailShow
                if (alertFieldForm.isDetailShow === '1') {
                    obj.detailShowSort = alertFieldForm.detailShowSort
                    obj.detailShowName = alertFieldForm.detailShowName
                    obj.detailShowPattern = alertFieldForm.detailShowPattern
                }
                obj.isQueryParam = alertFieldForm.isQueryParam
                if (alertFieldForm.isQueryParam === '1') {
                    obj.queryParamSort = alertFieldForm.queryParamSort
                    obj.queryParamName = alertFieldForm.queryParamName
                    obj.queryParamType = alertFieldForm.queryParamType
                    obj.queryParamSource = alertFieldForm.queryParamSource
                }
                obj.isDeriveField = alertFieldForm.isDeriveField
                if (alertFieldForm.isDeriveField === '1') {
                    obj.deriveFieldSort = alertFieldForm.deriveFieldSort
                    obj.deriveFieldName = alertFieldForm.deriveFieldName
                    obj.deriveFieldType = alertFieldForm.deriveFieldType
                    obj.deriveFieldSource = alertFieldForm.deriveFieldSource
                }
                obj.modelId = this.alertModelId
                obj.id = alertFieldForm.id
                return obj
            },
            addAlertFieldSubmit() {
                let obj = this.validateAddRequest('add')
                if (!obj.flag) {
                    this.$alert(obj.msg, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                rbAlertModelFieldServicesFactory.insertAlertField(this.getAlertFieldRequest(obj.alertFieldForm)).then((res) => {
                    if (res === 'SUCCESS') {
                        this.$message({
                            message: '创建成功!',
                            type: 'success'
                        })
                        this.currentPage = 1
                        this.getAlertFieldListByModelId()
                        this.addAlertFieldDialog = false
                    } else {
                        this.$message.error('创建失败!')
                    }
                })
            },
            getAlertFieldDetail(obj) {
                rbAlertModelFieldServicesFactory.getAlertFieldDetailById(obj.id).then((res) => {
                    this.alertFieldDetail = res
                    this.alertFieldDetailDialog = true
                })
            },
            updateAlertField(obj) {
                rbAlertModelFieldServicesFactory.getAlertFieldDetailById(obj.id).then((res) => {
                    this.alertFieldDetail = res
                    this.updateAlertFieldDialog = true
                })
            },
            updateAlertFieldSubmit() {
                let obj = this.validateAddRequest('update')
                if (!obj.flag) {
                    this.$alert(obj.msg, '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                rbAlertModelFieldServicesFactory.updateAlertField(this.getAlertFieldRequest(obj.alertFieldForm)).then((res) => {
                    if (res === 'SUCCESS') {
                        this.$message({
                            message: '修改成功!',
                            type: 'success'
                        })
                        this.getAlertFieldListByModelId()
                        this.updateAlertFieldDialog = false
                    } else {
                        this.$message.error('修改失败!')
                    }
                })
            },
            deleteAlertField(obj) {
                this.$confirm('字段删除无法恢复，确认删除?').then(() => {
                    rbAlertModelFieldServicesFactory.deleteAlertFieldDetailById(obj.id, this.alertModelId).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '删除成功!',
                                type: 'success'
                            })
                            this.currentPage = 1
                            this.getAlertFieldListByModelId()
                        } else {
                            this.$message.error('删除失败!')
                        }
                    })
                })
            },
            updateLockStatus(obj, isLock) {
                this.$confirm('确认修改?').then(() => {
                    rbAlertModelFieldServicesFactory.updateLockStatus(obj.id, this.alertModelId, isLock).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '修改成功!',
                                type: 'success'
                            })
                            this.getAlertFieldListByModelId()
                        } else {
                            this.$message.error('修改失败!')
                        }
                    })
                })
            },
            synchronizeField() {
                this.$confirm('确认同步?').then(() => {
                    rbAlertModelFieldServicesFactory.synchronizeField(this.alertModelId).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '同步成功!',
                                type: 'success'
                            })
                            this.currentPage = 1
                            this.getAlertFieldListByModelId()
                        } else {
                            this.$message.error('同步失败!')
                        }
                    })
                })
            },
            getSearchText() {
                this.currentPage = 1
                this.getAlertFieldListByModelId()
            },
            getModelTableName() {
                this.modelTableName = ''
                this.modelData.forEach((item) => {
                    if (item.id === this.alertModelId && item.tableName === 'alert_alerts') {
                        this.modelTableName = 'alert_alerts'
                    }
                })
            },
            addAlertField() {
                this.addAlertFieldDialog = true
            },
            append(node, data) {
                this.AlertModeForm.parentName = data.modelName
                this.AlertModeForm.parentId = data.id
                this.tableNameShow = !(data.parentId == '-1')
                this.insertAlertModelDialog = true
            },
            remove(node, data) {
                let list = []
                if (data.parentId === '-1') {
                    list = null
                } else if (data.parentId === 'all') {
                    list.push(data.id)
                    data.childList.forEach((item) => {
                        list.push(item.id)
                    })
                } else {
                    list.push(data.id)
                }
                this.$confirm('确认删除?').then(() => {
                    rbAlertModelServicesFactory.deleteAlertModel(list).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '删除成功!',
                                type: 'success'
                            })
                            this.getAlertModelTreeData()
                            this.getAlertModelList()
                        } else {
                            this.$message.error('删除失败!')
                        }
                    })
                })
            },
            editModel(node, data) {
                rbAlertModelServicesFactory.getAlertModelDetail(data.id).then((res) => {
                    this.AlertModeForm.id = res.id
                    this.AlertModeForm.modelName = res.modelName
                    this.AlertModeForm.parentId = res.parentId
                    let obj = this.$refs.modelTree.getNode(res.parentId)
                    this.AlertModeForm.parentName = obj.data.modelName
                    this.AlertModeForm.tableName = res.tableName
                    this.AlertModeForm.sort = res.sort
                    this.AlertModeForm.description = res.description
                    this.updateShow = true
                    if (data.parentId == 'all') {
                        this.tableNameShow = false
                    } else if (data.parentId == 'all' && node.isLeaf) {
                        this.tableNameShow = false
                    }
                    this.insertAlertModelDialog = true
                })
            },
            updateModelSubmit() {
                this.$refs['AlertModeForm'].validate((valid) => {
                    if (valid) {
                        this.updatetAlertModel(this.AlertModeForm)
                    } else {
                        console.log('error submit!!')
                        return false
                    }
                })
            },
            updatetAlertModel(obj) {
                this.$confirm('确认更新?').then(() => {
                    rbAlertModelServicesFactory.updateAlertModel(obj).then((res) => {
                        if (res === 'SUCCESS') {
                            this.$message({
                                message: '更新成功!',
                                type: 'success'
                            })
                            this.getAlertModelTreeData()
                            this.getAlertModelList()
                        } else {
                            this.$message.error('更新失败!')
                        }
                    }).catch(() => {
                        this.$message.error('更新失败')
                    })
                    this.cancel('model')
                })

            },
        },
        watch: {
            filterText(val) {
                this.$refs['modelTree'].filter(val)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .com-tree {
        padding-left: 20px;
        padding-top: 20px;
        padding-right: 10px;
        .search-box {
            margin-bottom: 14px;
            font-size: 12px;
            width: 350px;
        }
        .button-box {
            margin-bottom: 14px;
            font-size: 12px;
        }
        .yw-el-tree {
            height: 525px;
        }
        .el-tree {
            /deep/ .el-tree-node__content {
                .buss {
                    display: none;
                }
            }
            /deep/ .el-tree-node__content:hover {
                .buss {
                    display: inline-block;
                }
            }
        }
        .el-table {
            border-radius: 8px;
            font-size: 12px;
            height: 550px;
        }
        .custom-tree-node {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-size: 12px;
            padding-right: 8px;
        }
    }
    .person-manage__bar {
        display: -webkit-flex; /* Safari */
        display: flex;
        justify-content: space-between;
        align-content: flex-start;
        margin-bottom: 6px;
        .person-manage__bar-left {
            display: inline-block;
            width: 250px;
            vertical-align: top;
        }
        .person-manage__bar-right {
            display: inline-block;
            vertical-align: top;
            .person-manage__bar-import {
                display: inline-block;
                margin-right: 5px;
                margin-left: 5px;
            }
        }
    }
</style>

