<template>
    <el-container class="components-container">
        <el-aside class="yw-dashboard"
                  width="54%"
                  style="overflow: unset;">
            <el-scrollbar class="aside-main"
                          style="height: calc(100%)">
                <el-form ref="roleform"
                         class="yw-form"
                         :rules="roleRules"
                         :model="roleForm"
                         label-width="0px">
                    <el-row type="flex"
                            :gutter="30">
                        <el-col :span="22">
                            <el-form-item label=""
                                          prop="name">
                                <div class="function-role__sub-title"><span style="color:red">*</span>角色名称</div>
                                <el-input placeholder="请输入内容"
                                          v-model="roleForm.name"></el-input>
                            </el-form-item>
                        </el-col>
                        <!-- 角色描述 字段   describe -->

                    </el-row>
                    <el-row type="flex"
                            :gutter="30">
                        <el-col :span="22">
                            <el-form-item>
                                <div class="function-role__sub-title">访问权限</div>
                                <div class="role-section">
                                    <el-tree id="tree"
                                             ref="visittree"
                                             :data="authTreeData"
                                             show-checkbox
                                             node-key="resource"
                                             :default-expanded-keys="['all']"
                                             :default-checked-keys="roleDataChecked"
                                             @node-click="handleVisitNodeClick"
                                             :props="defaultProps"
                                             @check="checkNodeClick">
                                    </el-tree>
                                </div>

                            </el-form-item>
                        </el-col>

                    </el-row>
                    <el-form-item class="form-button"
                                  label="">
                        <el-button type="primary"
                                   @click="e_save">保存</el-button>
                        <el-button @click="cancel_mod">取消修改</el-button>
                        <el-button @click="e_reset">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-scrollbar>
        </el-aside>
        <el-main class="yw-dashboard">
            <div class="table-operate-wrap clearfix">

            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 200px)"
                          :data="roleList">
                    <el-table-column prop="name"
                                     label="角色名称">
                    </el-table-column>
                    <el-table-column prop="describe"
                                     label="角色描述">
                    </el-table-column>
                    <el-table-column show-overflow-tooltip
                                     prop="updated_at"
                                     label="最后更新时间">
                    </el-table-column>
                    <el-table-column label="操作"
                                     width="100">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button @click="handleUpdate(scope.row)"
                                           icon="el-icon-tickets"
                                           title="查看"
                                           type="text"></el-button>
                                <el-button @click="handleDelete(scope.row)"
                                           title="删除"
                                           icon="el-icon-delete"
                                           type="text"></el-button>
                            </div>

                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination class="person-manage__pager"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-size="pagesize"
                               :page-sizes="[5, 10]"
                               layout="total, sizes, prev, pager, next, jumper"
                               :total="total">
                </el-pagination>
            </div>

        </el-main>
    </el-container>
</template>
<script>
    import systemDataService from 'src/services/sys/role-cdn-services.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    // import _ from 'lodash'
    export default {
        name: 'Role',
        data() {
            return {
                keyArr: [],
                roleForm: {
                    id: '',
                    name: '',
                    describe: '',
                    visitData: [],
                },
                authTreeData: [],
                currentPage: 1,
                total: 1,
                pagesize: 10,
                roleRules: {
                    name: [
                        { required: true, message: '请输入角色名称', trigger: 'blur' }
                    ]
                },
                roleDataChecked: [],
                defaultProps: {
                    label: 'name',
                    children: 'childList'
                },
                selectedNode: '',
                selectedActionList: [],
                roleList: [],
                actionDic: {},
                searchText: '',
                mark: {}
            }
        },
        watch: {

            selectedActionList: {
                deep: true,
                handler: function (val) {
                    if (val && val.length > 0) {
                        this.roleForm.actionData = val
                    } else {
                        this.roleForm.actionData = []
                    }
                }
            }
        },
        methods: {
            e_reset() {
                this.roleForm = {
                    id: '',
                    name: '',
                    describe: '',
                    visitData: [],
                }
                this.$refs['visittree'].setCheckedKeys([])
                this.selectedActionList = []
                if (this.mark) {
                    this.handleUpdate(this.mark)
                }
            },
            e_save() {
                console.log(this.roleForm)
                this.$refs['roleform'].validate((valid) => {
                    // const param = this.roleForm
                    if (valid) {
                        if (this.roleForm.id) {
                            this.roleForm.visitData = this.processVisitData()
                            this.updateRole(this.roleForm)
                        } else {
                            this.roleForm.visitData = this.processVisitData()
                            this.addRole(this.roleForm)
                        }
                    } else {
                        return false
                    }
                })
            },
            cancel_mod() {
                this.roleForm = {
                    id: '',
                    name: '',
                    describe: '',
                    visitData: [],
                }
                this.$refs['visittree'].setCheckedKeys([])
                this.selectedActionList = []
            },

            checkNodeClick(checkedNodes, checkedKeys) {
                console.log(checkedKeys)
                let nodeKey = checkedKeys.checkedNodes
                for (let i = 0; i < nodeKey.length; i++) {
                    Object.keys(nodeKey[i].actions).map((key) => {
                        this.keyArr.push({
                            id: key,
                            name: nodeKey[i].actions[key],
                            model: nodeKey[i].name
                        })
                    })

                }
                console.log(this.keyArr)
            },

            processVisitData() {
                return this._processTreeData('visittree', 'resource')
            },
            // 取出树里的数据
            _processTreeData(treeName, keyName) {
                const data = this.$refs[treeName].getCheckedNodes()
                let treeData = []
                if (data && data.length > 0) {
                    data.map((item) => {
                        treeData.push(String(item[keyName]))
                    })
                    return treeData
                } else {
                    return []
                }
            },
            // 树节点点击
            handleVisitNodeClick(node) {
                this.selectedNode = node.resource
                systemDataService.getOperateTree(node.resource).then((res) => {
                    if (res && res.actions) {
                        const data = []
                        Object.keys(res.actions).map((key) => {
                            data.push({
                                id: key,
                                name: res.actions[key],
                                model: res.name
                            })
                        })
                        this.actionList = data
                    } else {
                        this.actionList = []
                    }
                }).catch((err) => {
                    console.warn('getOperateTree error', err)
                })
            },
            // 新增角色
            addRole(data) {
                // console.log('addRole', data)
                data.actionData = this.keyArr
                console.log(data)
                systemDataService.addRole(data).then(() => {
                    this.$message({
                        message: '创建成功',
                        type: 'success'
                    })
                    this.e_reset()
                    this.getRoleList()
                }).catch(() => {
                    this.$message({
                        message: '创建失败，请确认角色是否已存在',
                        type: 'error'
                    })
                })
            },
            // 修改角色
            updateRole(data) {
                systemDataService.updateRole(data).then(() => {
                    this.$message({
                        message: '修改成功',
                        type: 'success'
                    })
                    this.mark = ''
                    this.e_reset()
                    this.getRoleList()
                }).catch(() => {
                    this.$message({
                        message: '修改失败',
                        type: 'error'
                    })
                })
            },
            // 点击修改
            handleUpdate(row) {
                this.mark = row
                // 防止多次点击修改产生覆盖
                this.roleForm = {
                    id: '',
                    name: '',
                    describe: '',
                    visitData: [],
                }
                this.$refs['visittree'].setCheckedKeys([])
                this.selectedActionList = []
                systemDataService.getRoleDetail(row.uuid).then((res) => {
                    // console.log('getRoleDetail', res)
                    const permissions = res.permissions[0]
                    const action = permissions.actions
                    const resource = permissions.resource
                    this.roleForm.id = res.uuid
                    this.roleForm.name = res.name
                    this.roleForm.describe = res.describe
                    // 设置操作权限
                    if (action) {
                        const selectedAction = []
                        Object.keys(action).map((key) => {
                            const resourceKey = key.split(':')[0]
                            const data = {
                                id: key,
                                name: action[key]
                                // ,
                                // model: resource[resourceKey]
                            }
                            if (!selectedAction[resourceKey]) {
                                selectedAction[resourceKey] = []
                            }
                            selectedAction.push(data)
                        })
                        this.selectedActionList = selectedAction
                    }
                    // 设置资源  点击查看图标设置元素
                    if (resource && Object.keys(resource).length === 1 &&
                        Object.keys(resource)[0] === '#') {
                        this.roleDataChecked = []
                        this.$refs['visittree'].setCheckedKeys([])
                    } else {
                        this.roleDataChecked = Object.keys(resource)
                    }
                }).catch((err) => {
                    console.warn('getRoleDetail error', err)
                })
            },
            // 点击删除
            handleDelete(row) {
                this.$confirm('确定要删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    systemDataService.deleteRole(row.uuid).then(() => {
                        this.$message({
                            message: '操作成功',
                            type: 'success'
                        })
                        this.getRoleList()
                    }).catch((res) => {
                        var message
                        if (res.data && res.data.errors && res.data.errors[0] && res.data.errors[0].code === 'invalid_args' && res.data.errors[0].fields[0]['role_uuid']) {
                            message = res.data.errors[0].fields[0]['role_uuid'][0]
                        }
                        this.$message({
                            message: message ? message.toString() : '删除失败',
                            type: 'error'
                        })
                    })
                })
            },
            // 获取角色列表
            getRoleList(page, val) {
                systemDataService.getPageList(page, val).then((res) => {
                    if (res && res.result && res.result.length > 0) {
                        res.result.map((item) => {
                            item.updated_at = formatDate(item.updated_at)
                        })
                        this.roleList = res.result
                        this.currentPage = res.curPage
                        this.total = res.count
                    }
                })
            },
            // 分页功能
            handleSizeChange(val) {
                this.pagesize = val
                // console.log(this.pagesize)
                this.getRoleList(val)
            },
            handleCurrentChange(val) {
                this.getRoleList(this.pagesize, val)
            },
            // 获取访问权限树
            getVisitTree(id) {
                systemDataService.getVisitTree(id).then((res) => {
                    this.authTreeData = res
                })
                //   let id = ''
                //   if (node && node.data) {
                //     id = node.data.resource
                //   }
                //   systemDataService.getVisitTree(id).then((res) => {
                //     if (res) {
                //       resolve(res)
                //     } else {
                //       resolve([])
                //     }
                //   })
            }
        },
        mounted() {
            // this.roleForm.actionData = this.selectedActionList
            this.getRoleList()
            this.getVisitTree('')
        }
    }
</script>
<style lang="scss" scoped>
    .form-button {
        margin-bottom: 20px;
    }
    .role-section {
        padding: 10px;
        height: 610px;
        font-size: $font-size;
        border-radius: $border-radius;
        overflow: auto;
        border: 1px solid $color-border;
        &.role-section-table {
            min-height: 160px;
            height: 285px;
        }
    }
    .el-main {
        padding-bottom: 0;
    }
    .components-container {
        padding-bottom: 8px;
    }
</style>
