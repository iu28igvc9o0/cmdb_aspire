<!--
@author qiangjun
@date   20.3.10
@description 分组管理页面
-->
<template>
    <div class="container">
        <el-form ref="departmentform"
                 class="yw-form components-condition is-required"
                 label-width="70px"
                 :rules="departmentRules"
                 :inline="true"
                 :model="departmentForm">
            <el-form-item style="display:none;">
                <el-input hidden
                          v-model="departmentForm.parent"></el-input>
            </el-form-item>
            <el-form-item label="上级分组"
                          prop="parentName">
                <el-input readonly
                          v-model="departmentForm.parentName"
                          placeholder="请选择分组"></el-input>
            </el-form-item>
            <el-form-item label="分组名称"
                          prop="name">
                <el-input v-model="departmentForm.name"
                          placeholder="请输入分组名称"></el-input>
            </el-form-item>
            <el-form-item label="分组描述">
                <el-input v-model="departmentForm.descr"
                          placeholder="请输入描述内容"></el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleAddGroup">保存分组</el-button>
            </section>
        </el-form>

        <el-form class="yw-form"
                 style="height:calc(100vh - 58px - 40px - 100px)"
                 :inline="true">
            <el-row :gutter="22"
                    style="height:100%">
                <el-col :span="6"
                        style="height:100%">
                    <comtree :ref="treeName"
                             class="person-manage__tree"
                             :data="departmentData"
                             :props="departmentTreeDefault"
                             :draggable="false"
                             node-key="group_id"
                             :default-expanded-keys="deviceAuthDataExpanded"
                             :exId="treeName"
                             ex-show-search
                             :ex-control="true"
                             :ex-control-opt="customControl"
                             @node-click="handleNodeClick"></comtree>
                </el-col>
                <el-col :span="18"
                        style="height:100%">
                    <div class="person-manage__bar">
                        <div class="person-manage__bar-left"
                             style="width: 600px">
                            <el-input class="search-box"
                                      style="width: 30%"
                                      v-model="searchTextGroup"
                                      placeholder="请输入分组名称">
                                <i slot="suffix"
                                   class="el-input__icon el-icon-search"
                                   @click="searchData"></i>
                            </el-input>
                            <el-select v-model="valueObject"
                                       style="width: 30%"
                                       placeholder="请选择对象类型">
                                <el-option v-for="item in objectTypeOptions"
                                           :key="item.value"
                                           :label="item.label"
                                           :value="item.value">
                                </el-option>
                            </el-select>
                            <el-input class="search-box"
                                      style="width: 30%"
                                      v-model="searchText"
                                      placeholder="请输入对象名称">
                                <i slot="suffix"
                                   class="el-input__icon el-icon-search"
                                   @click="searchData"></i>
                            </el-input>
                        </div>
                        <div class="person-manage__bar-right">
                            <el-button type="text"
                                       @click="handleDelete"
                                       icon="el-icon-delete">删除</el-button>
                            <el-button type="text"
                                       @click="handleExport"
                                       icon="el-icon-upload2">导出</el-button>
                            <el-button type="text"
                                       @click="handleAddPerson"
                                       icon="el-icon-plus">新增对象归属</el-button>
                            <el-button type="text"
                                       @click="batchHandleDept"
                                       icon="el-icon-edit">修改对象归属</el-button>
                        </div>
                    </div>
                    <person-list ref="personlist"
                                 :searchText="searchText"
                                 :searchTextGroup="searchTextGroup"
                                 :valueObject="valueObject"
                                 :selectedGroupId="selectedGroupId"
                                 @getGroupListData="getGroupListData"
                                 @selectGroupObject="selectGroupObject"
                                 @detial-click="handleUpdatePerson"></person-list>
                </el-col>
            </el-row>
        </el-form>

        <batchmod-dept-dialog :visible="batchModDeptDialogVisible"
                              :dialogName="dialogName"
                              :groupData="groupSelectData"
                              :groupListData="groupListData"
                              @add-done="handleModDeptAddDone"
                              @dialog-close="handleModDeptDialogClose">
        </batchmod-dept-dialog>
        <el-dialog class="yw-dialog"
                   title="编辑"
                   :visible.sync="departmentDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="410px"
                   :before-close="handleDepartmentDialogClose">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         ref="groupUpdateForm"
                         :model="groupUpdateForm"
                         :rules="departmentRules"
                         label-width="70px">
                    <el-form-item label="分组名称"
                                  prop="name">
                        <el-input v-model="groupUpdateForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="分组描述">
                        <el-input v-model="groupUpdateForm.descr"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleUpdateDepartment">确定</el-button>
                <el-button @click="departmentDialogVisible = false">取消</el-button>
            </section>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   title="移动"
                   :visible.sync="departmentMoveDialogVisible"
                   :modal="false"
                   :modal-append-to-body="false"
                   width="500px"
                   :before-close="handleDepartmentMoveDialogClose">
            <section class="yw-dialog-main">
                <comtree :ref="treeName1"
                         :data="moveDepartmentData"
                         :props="departmentTreeDefault"
                         node-key="group_id"
                         :default-expand-all="true"
                         :exId="treeName1"
                         ex-show-search
                         :ex-control="true"
                         :ex-control-opt="[]"></comtree>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleMoveDepartment">确定</el-button>
                <el-button @click="departmentMoveDialogVisible = false">取消</el-button>
            </section>
        </el-dialog>
        <!-- dialog -->
    </div>
</template>
<script>
    import groupDataService from 'src/services/auto_operation/rb-auto-operation-group-services.js'
    import comtree from './components/tree.vue'
    // import personDialog from 'src/pages/system_manage/components/addPersonDialog.vue'
    import batchmodDeptDialog from './components/batchmodDeptDialog.vue'
    import personList from './components/personList.vue'
    // import { dictionaryService, DIC_KEY } from 'src/services/sys/dictionary-service.js'

    export default {
        name: 'Person',
        components: {
            comtree,
            // personDialog,
            personList,
            batchmodDeptDialog
        },
        data() {
            const that = this
            return {
                objectTypeOptions: [
                    {
                        value: '',
                        label: '全部'
                    }, {
                        value: 'pipeline',
                        label: '作业'
                    }, {
                        value: 'script',
                        label: '脚本'
                    }, {
                        value: 'yum',
                        label: 'yum文件'
                    }, {
                        value: 'scenes',
                        label: '场景'
                    }, {
                        value: 'ap_scheme',
                        label: '自愈规则'
                    }, {
                        value: 'file',
                        label: '文件'
                    }],
                valueObject: '',
                dynamicTags: [],
                prepareMoveData: {},
                departmentForm: {
                    name: '',
                    parent: '',
                    parentName: '',
                    descr: ''
                },
                departmentRules: {
                    name: [
                        { required: true, message: '请输入分组名称', trigger: 'blur' }
                    ],
                    parentName: [
                        { required: true, message: '请输入上级分组', trigger: 'blur' }
                    ]
                    // type: [
                    //   { required: true, message: '请输入部门类型', trigger: 'blur' }
                    // ]
                },
                groupUpdateForm: {
                    id: '',
                    name: '',
                    descr: ''
                },
                treeName: 'persontree',
                treeName1: 'departmenttree',
                depTypeOptions: [],
                personTypeOptions: [],
                personSexalOptions: [],
                personDic: {},
                personDicOption: {},
                dictionary: {},
                departmentData: [],
                moveDepartmentData: [],
                departmentTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                },
                deviceAuthDataExpanded: [],
                searchText: '',
                searchTextGroup: '',
                customControl: [
                    {
                        title: '移动至',
                        icon: 'el-icon-rank',
                        callback: that.customMove
                    },
                    {
                        title: '编辑',
                        icon: 'el-icon-edit-outline',
                        callback: that.customEdit
                    },
                    {
                        title: '删除',
                        icon: 'el-icon-delete',
                        callback: that.customDelete
                    }
                ],
                selectedGroupId: '',
                selectedGroupName: '',
                groupSelectData: {},
                groupListData: [],
                //   personDialogVisible: false,
                departmentDialogVisible: false,
                departmentMoveDialogVisible: false,
                batchModDeptDialogVisible: false,
                selectedPersonId: '',
                importUrl: '',
                importHeader: {},
                clickParentNode: '1',
                dialogName: '',
                selectedGroupObjectList: []
            }
        },
        methods: {
            // 移动分组
            handleMoveDepartment() {
                let data = this.prepareMoveData
                let parentId = this.$refs[this.treeName1].getCurrentKey()
                this.departmentMoveDialogVisible = false
                const params = {
                    group_id: data.group_id,
                    parent_id: parentId,
                }
                groupDataService.saveGroup(params).then((res) => {
                    if (res.flag) {
                        this.$message({
                            message: '移动成功',
                            type: 'success'
                        })
                        this.getDepartment()
                        this.resetGroupUpdateForm()
                    } else {
                        var message
                        if (res.data.errors[0]) {
                            message = res.data.errors[0].message
                        }
                        this.$message({
                            message: message ? message.toString() : '操作失败',
                            type: 'error'
                        })
                    }
                }).catch((res) => {
                    var message
                    if (res.data.errors[0]) {
                        message = res.data.errors[0].message
                    }
                    this.$message({
                        message: message ? message.toString() : '操作失败',
                        type: 'error'
                    })
                })
            },
            // 修改
            customEdit(node, data) {
                // console.log('customEdit', node, data, event)
                this.departmentDialogVisible = true
                this.groupUpdateForm.id = data.group_id
                this.groupUpdateForm.name = data.group_name
                this.groupUpdateForm.descr = data.group_desc
            },
            customMove(node, data) {
                // console.log('customEdit', node, data, event)
                this.prepareMoveData = data
                this.moveDepartmentData = JSON.parse(JSON.stringify(this.departmentData))
                this.dealData(this.moveDepartmentData, data)
                this.departmentMoveDialogVisible = true
            },
            dealData(departmentData, data) {
                for (let index = 0; index < departmentData.length; index++) {
                    if (departmentData[index].group_id === data.group_id) {
                        departmentData.splice(index, 1)
                        break
                    }
                    if (departmentData[index].sub_group_list && departmentData[index].sub_group_list.length > 0) {
                        this.dealData(departmentData[index].sub_group_list, data)
                    }
                }

            },
            // 删除
            customDelete(node, data) {
                this.$confirm('确定要删除该分组吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    groupDataService.deleteGroup(data.group_id).then((res) => {
                        if (res.flag) {
                            this.$message({
                                message: '操作成功',
                                type: 'success'
                            })
                            this.$refs[this.treeName].remove(node)
                            this.resetGroupUpdateForm()
                        } else {
                            this.$message({
                                message: res.error_tip,
                                type: 'error'
                            })
                        }
                    }).catch((res) => {
                        // error_tip
                        var message
                        if (res.data.errors[0].code === 'The_father_departemnt_not_delete' || res.data.errors[0].code === 'Departemnt_exist_user_not_delete') {
                            message = res.data.errors[0].message
                        }
                        this.$message({
                            message: message ? message.toString() : '操作失败',
                            type: 'error'
                        })
                    })
                })
            },

            // 点击部门
            handleNodeClick(data) {
                // console.log(data,data1)
                if (data.parent_id === '') {
                    this.selectedGroupId = ''
                } else {
                    this.selectedGroupId = data.group_id
                }
                this.selectedGroupName = data.group_name
                this.groupSelectData = { 'group_id': data.group_id, 'group_name': data.group_name, }
                this.departmentForm.parent = data.group_id
                this.departmentForm.parentName = data.group_name
                // 用于对父节点的类型判断
                this.clickParentNode = data.dept_type + ''

                this.getPersonList(this.selectedGroupId)
            },
            // 新增对象归属
            handleAddPerson() {
                if (this.departmentForm.parentName) {
                    this.dialogName = '新增'
                    this.showUpdateDeptPersonDialog()
                } else {
                    this.$confirm('请先选中一个分组，才可以进行新增操作。', '提示', {
                        showConfirmButton: false,
                        type: 'warning'
                    })
                }
            },
            // 修改对象归属
            batchHandleDept() {
                if (this.departmentForm.parentName) {
                    this.dialogName = '修改'
                    this.showUpdateDeptPersonDialog()
                } else {
                    this.$confirm('请先选中一个分组，才可以进行修改操作。', '提示', {
                        showConfirmButton: false,
                        type: 'warning'
                    })
                }
            },
            showUpdateDeptPersonDialog() {
                this.batchModDeptDialogVisible = true
            },
            handleModDeptDialogClose() {
                this.batchModDeptDialogVisible = false
            },
            // 获取选中的对象数组，进行删除
            selectGroupObject(val) {
                this.selectedGroupObjectList = val
                // group_relation_id
            },
            // 获取列表数据
            getGroupListData(data) {
                this.groupListData = data
            },
            // 关闭 新建修改的弹窗
            handleModDeptAddDone() {
                this.getPersonList(this.selectedGroupId)
                this.handleModDeptDialogClose()
            },
            // 修改分组
            handleUpdateDepartment() {
                this.$refs['groupUpdateForm'].validate((valid) => {
                    if (valid) {
                        const params = {
                            group_id: this.groupUpdateForm.id,
                            group_name: this.groupUpdateForm.name,
                            group_desc: this.groupUpdateForm.descr
                        }
                        groupDataService.saveGroup(params).then((res) => {
                            if (res.flag) {
                                this.$message({
                                    message: '修改成功',
                                    type: 'success'
                                })
                                this.getDepartment()
                                this.resetGroupUpdateForm()
                                this.handleDepartmentDialogClose()
                            } else {
                                this.$message({
                                    message: '操作失败',
                                    type: 'error'
                                })
                            }
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
            // 关闭弹窗
            handleDepartmentDialogClose() {
                this.departmentDialogVisible = false
            },
            handleDepartmentMoveDialogClose() {
                this.departmentMoveDialogVisible = false
            },
            // 保存分组
            handleAddGroup() {
                this.$refs['departmentform'].validate((valid) => {
                    if (valid) {
                        const params = {
                            parent_id: this.departmentForm.parent,
                            group_name: this.departmentForm.name,
                            group_desc: this.departmentForm.descr
                        }
                        groupDataService.saveGroup(params).then((res) => {
                            if (res.flag) {
                                this.$message({
                                    message: '创建成功',
                                    type: 'success'
                                })
                                this.getDepartment()
                                this.resetGroupUpdateForm()
                            } else {
                                var message
                                if (res.error_tip === 'The group name is already exists') {
                                    message = '分组名称已经存在'
                                } else {
                                    message = '创建失败'
                                }
                                this.$message({
                                    message: message ? message.toString() : '创建失败',
                                    type: 'error'
                                })
                            }
                        }).catch((res) => {
                            var message
                            if (res.data && res.data.errors && res.data.errors[0]) {
                                message = res.data.errors[0].message
                            }
                            this.$message({
                                message: message ? message.toString() : '创建失败',
                                type: 'error'
                            })
                        })
                    } else {
                        return false
                    }
                })
            },
            // 模糊查询方法
            searchData() {
                this.$refs['personlist'].searchData()
            },
            // 删除
            handleDelete() {
                if (this.selectedGroupObjectList.length <= 0) {
                    this.$confirm('请至少勾选一个！', '提示', {
                        showConfirmButton: false,
                        type: 'warning'
                    })
                    return
                }
                const deleteRelationIds = []
                this.selectedGroupObjectList.forEach(item => {
                    deleteRelationIds.push(item.group_relation_id)
                })
                const params = {
                    group_relation_ids: deleteRelationIds.join(',')
                }
                groupDataService.deleteGroupRelation(params).then((res) => {
                    if (res.flag) {
                        this.$message({
                            message: '批量删除对象归属成功',
                            type: 'success'
                        })
                        this.selectedGroupObjectList = []
                        this.getPersonList(this.selectedGroupId)
                    } else {
                        this.$message({
                            message: '操作失败',
                            type: 'error'
                        })
                    }
                }).catch(() => {
                    this.$message({
                        message: '操作失败',
                        type: 'error'
                    })
                })

            },
            // 导出
            handleExport() {
                groupDataService.exportGroupRelation(this.selectedGroupId, this.searchText, this.searchTextGroup, this.valueObject).then((res) => {
                    try {
                        const link = window.URL.createObjectURL(new Blob([res], { type: 'application/vnd.ms-excel' }))
                        // console.log('personExport link ', link)
                        let downLoadElement = document.createElement('a')
                        downLoadElement.href = link
                        downLoadElement.download = '分组关系列表数据.xls'
                        document.body.appendChild(downLoadElement)
                        downLoadElement.click()
                        document.body.removeChild(downLoadElement)
                        URL.revokeObjectURL(link)
                    } catch (err) {
                        this.$message({
                            message: '导出失败',
                            type: 'warning'
                        })
                    }
                })
            },
            // 重置
            resetGroupUpdateForm() {
                this.groupUpdateForm = {
                    id: '',
                    name: '',
                    descr: ''
                }
                this.departmentForm = {
                    name: '',
                    parent: '',
                    parentName: '',
                    descr: ''
                }
                this.selectedGroupId = ''
                this.selectedGroupName = ''
                this.searchText = ''
                this.searchTextGroup = ''
                this.valueObject = ''
                this.groupSelectData = {}
                this.groupListData = []
                this.getPersonList()
            },
            getPersonList(id) {
                this.$refs['personlist'].searchData(id)
            },
            // 获取部门树
            getDepartment() {
                groupDataService.getQueryGroupTree().then((res) => {
                    // console.log('getQueryGroupTree', res)
                    this.departmentData = res
                    if (res.length > 0) {
                        this.deviceAuthDataExpanded = [res[0].group_id]
                    }
                })
            }
        },
        mounted() {
            this.getDepartment()
            this.getPersonList()
            // this.getDictonary()
            // this.setImportOptions()
        }
    }
</script>

<style lang="scss" scoped>
    .container {
        padding: 15px;
        background: rgba(255, 255, 255, 1);
        box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.05);
        border-radius: 16px;

        min-height: calc(100vh - 58px - 40px);
    }
    /deep/.components-condition {
        padding-right: 90px;
    }
    .person-manage__tree {
        /deep/ .el-tree {
            height: 480px;
            overflow: auto;
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
