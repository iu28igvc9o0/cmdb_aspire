<template>
    <div class="component-container yw-dashboard"
         style=" height: 100%">
        <el-container style="width:100%;height:100%;"
                      v-if="showList && resetComponent">
            <el-aside class="yw-dashboard"
                      width="260px">
                <section class="yw-dashboard-filter">
                    <el-button type="primary"
                               @click="changeTree">模型分组管理</el-button>
                </section>
                <el-scrollbar class="aside-main"
                              style="height: calc(100% - 60px)">
                    <YwTree :datas="rootTreeDatas"
                            :options="rootTreeOptions"
                            @clickTree="clickTree"></YwTree>
                </el-scrollbar>
            </el-aside>
            <el-main>
                <el-form class="yw-form">
                    <div class="table-operate-wrap clearfix">
                        <el-button type="text"
                                   @click="addModule({catalogId:''})">
                            <i class="el-icon-plus"></i>
                            添加
                        </el-button>
                    </div>
                    <div class="yw-el-table-wrap"
                         style="margin-top:10px">
                        <el-table ref="singleTable"
                                  v-loading="loading"
                                  :data="tableData"
                                  border
                                  class="yw-el-table"
                                  row-key="id"
                                  :expand-row-keys="[firstLevelModule]"
                                  :tree-props="{children: 'childModules', hasChildren: 'hasChildren'}"
                                  @selection-change="handleSelectionChange"
                                  highlight-current-row
                                  @current-change="handleCurrentChange">
                            <el-table-column prop="name"
                                             label="模型名称"
                                             width="180"></el-table-column>
                            <el-table-column prop="code"
                                             label="模型编码"
                                             width="180">
                            </el-table-column>
                            <el-table-column prop="iconUrl"
                                             label="模型图标">
                                <template slot-scope="scope">
                                    <img width="30"
                                         height="30"
                                         :src="CMDB_SERVER_URL + scope.row.iconUrl"
                                         class="image" />
                                </template>
                            </el-table-column>
                            <el-table-column prop="disabled"
                                             label="是否禁用">
                                <template slot-scope="scope">
                                    {{scope.row.disabled === 'true' ? '是' : '否'}}
                                </template>
                            </el-table-column>
                            <el-table-column prop="module.builtin"
                                             label="是否内置">
                                <template slot-scope="scope">
                                    {{scope.row.builtin === 'true' ? '是' : '否'}}
                                </template>
                            </el-table-column>
                            <el-table-column label="操作"
                                             min-width="100px">
                                <template slot-scope="scope">
                                    <el-button @click="addModule(scope.row)"
                                               title="添加"
                                               type="text"
                                               icon="el-icon-plus"></el-button>
                                    <el-button @click="editModule(scope.row)"
                                               title="编辑"
                                               type="text"
                                               icon="el-icon-edit"></el-button>
                                    <el-button @click="copyModule(scope.row)"
                                               type="text"
                                               title="复制"
                                               icon="el-icon-document-copy"></el-button>
                                    <!-- <el-button @click="deleteModule(scope.row)"
                             v-if="!scope.row.childModules || scope.row.childModules.length<1"
                             type="text"
                             title="删除"
                             icon="el-icon-delete"></el-button> -->
                                    <el-button @click="deleteModule(scope.row)"
                                               type="text"
                                               title="删除"
                                               icon="el-icon-delete"></el-button>
                                    <el-button @click="bingModule(scope.row)"
                                               type="text"
                                               title="绑定模型事件"
                                               icon="el-icon-paperclip"></el-button>
                                    <el-button @click="up(scope.row)"
                                               v-if="scope.$index >0"
                                               type="text"
                                               title="升序"
                                               icon="el-icon-top"></el-button>
                                    <el-button @click="down(scope.row)"
                                               v-if="scope.$index >0"
                                               type="text"
                                               title="降序"
                                               icon="el-icon-bottom"></el-button>
                                    <el-button @click="addModuleRelation(scope.row)"
                                               type="text"
                                               title="添加模型关系"
                                               icon="el-icon-connection"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form>

                <div>

                </div>
            </el-main>
        </el-container>

        <add-module v-if="showAdd"
                    @setShow="setShow"
                    :activeModuleDatas="activeModuleDatas"
                    :moduleId="moduleId"
                    :parentId="parentId"
                    :state="state"></add-module>

        <ModuleRelation v-if="moduleRlObj.visiable"
                        :moduleRlObj="moduleRlObj"></ModuleRelation>

        <!-- dialog -->
        <DialogTree :dialogMsg="dialogTree"
                    @closeDialog="closeDialog"
                    v-if="dialogTree.dialogVisible"></DialogTree>
        <!-- dialog -->

        <!-- dialog -->
        <DialogBindEvent :dialogMsg="dialogBindEvent"
                         @closeDialogBind="closeDialogBind"
                         v-if="dialogBindEvent.dialogVisible"></DialogBindEvent>
        <!-- dialog -->
    </div>
</template>

<script>
    import updateComponent from 'src/utils/updateComponent.js'
    import rbCmdbModuleServiceFactory from '../../../../services/cmdb/rb-cmdb-module-service.factory'
    import CommonOption from 'src/utils/commonOption.js'

    import AddModule from './components/add-module'
    export default {
        mixins: [updateComponent, CommonOption],
        name: 'ModuleList',
        components: {
            AddModule,
            YwTree: () => import('src/components/common/yw-tree.vue'),
            DialogTree: () => import('./dialog-tree.vue'),
            DialogBindEvent: () => import('./dialog-bindEvent.vue'),
            ModuleRelation: () => import('./components/module-relation.vue'),
        },
        data() {
            return {
                // 根树数据
                rootTreeDatas: [],
                // 选中的树节点
                activeRootTree: '',
                // 配置项
                rootTreeOptions: {
                    children: 'children',
                    label: 'catalogName',
                    key: 'id'
                },
                // dialogTree
                dialogTree: {
                    dialogVisible: false,
                    id: '',// 预留：每个弹窗数据的唯一标识
                    data: {} // 预留：数据
                },
                // dialogBindEvent
                dialogBindEvent: {
                    dialogVisible: false,
                    id: '',// 预留：每个弹窗数据的唯一标识
                    data: {} // 预留：数据
                },
                // 编辑状态下选中的模型数据
                activeModuleDatas: {
                    auths: [],// 权限
                    catalogId: null, // 模型分组id
                    refModules: null // 引用模型id
                },

                moduleData: [],
                showList: true,
                showAdd: false,
                openRow: ['e05ca831eeda43f4b41835442bd77c55'],
                tableData: [],
                firstLevelModule: '',
                moduleId: '',
                parentId: '',
                state: '',
                // 新增模型关系的传递数据
                moduleRlObj: {
                    visiable: false,
                    data: {}
                },
                CMDB_SERVER_URL: window.CMDB_SERVER_URL
            }
        },
        mounted() {
            this.getRootTree()
        },
        methods: {
            // 关闭弹窗
            closeDialog(val) {
                this.dialogTree.dialogVisible = false
                if (val === 'update') {
                    this.getRootTree()
                }

            },
            closeDialogBind(val) {
                this.dialogBindEvent.dialogVisible = false
                if (val === 'update') {
                    this.getRootTree()
                }
            },

            // 获得根树数据
            getRootTree() {
                let params = {

                }

                rbCmdbModuleServiceFactory.getRootTree(params).then((data) => {
                    this.rootTreeDatas = data
                    this.activeRootTree = this.rootTreeDatas[0]
                    this.getModule()
                    this.updateComponent()
                })
            },
            // 树节点点击
            clickTree(val) {
                this.activeRootTree = val
                this.getModule()
            },
            // 模型树节点变更
            changeTree() {
                this.dialogTree.dialogVisible = true
            },
            // 绑定模型事件
            bingModule(row) {
                this.dialogBindEvent.data = row
                this.dialogBindEvent.dialogVisible = true
            },
            // 升序
            up(row) {
                let params = {
                    moduleId: row.id,
                    sortType: 'up'
                }
                rbCmdbModuleServiceFactory.sortModuleTree(params).then((data) => {
                    if (data.success) {
                        this.$message.success('排序成功！')
                        this.getModule()
                    } else {
                        this.$message.error(data.message)
                    }
                })
            },
            // 降序
            down(row) {
                let params = {
                    moduleId: row.id,
                    sortType: 'down'
                }
                rbCmdbModuleServiceFactory.sortModuleTree(params).then((data) => {
                    if (data.success) {
                        this.$message.success('排序成功！')
                        this.getModule()
                    } else {
                        this.$message.error(data.message)
                    }
                })
            },
            // 添加模型关系
            addModuleRelation(val) {
                this.moduleRlObj.visiable = true
                this.moduleRlObj.data = val
            },
            expandAll() {
            },

            setShow(val) {
                this.showAdd = val
                this.showList = !this.showAdd
                if (this.showList) {
                    this.getRootTree()
                }
            },
            handleSelectionChange() {
            },
            handleCurrentChange() {
                //        if (val && !val.children) {
                //          this.$refs.singleTable.setCurrentRow()
                //        }
                //        if (val && val.children) {
                //          this.parentId = val.id
                //        }
            },
            formatData(data) {
                if (data.children !== null && data.children) {
                    data.children.forEach(item => {
                        let tmp = JSON.parse(JSON.stringify(item))
                        item = tmp.module
                        item['groupList'] = tmp.groupList
                    })
                }
            },
            getModule() {
                this.showLoading()

                this.$store.commit('setCommonObj', { topCatalogId: this.activeRootTree.id })
                let params = {
                    'catalogId': this.activeRootTree.id || '',
                }
                rbCmdbModuleServiceFactory.getModuleTree(params).then((data) => {
                    this.tableData = data
                    if (this.tableData.length > 0) {
                        this.firstLevelModule = this.tableData[0].id
                    }
                }).catch(() => {
                    this.$notify({
                        title: '提示',
                        message: '查询模型信息失败',
                        type: 'error',
                        duration: 3000
                    })
                }).finally(() => {
                    this.closeLoading()
                })
            },
            addModule(row) {
                this.state = 'add'
                this.showAdd = true
                this.showList = false
                this.$store.commit('setCommonObj', { moduleStatus: 'add' })
                this.$store.commit('setModuleObj', { id: row.id || '' })
            },
            editModule(row) {

                this.parentId = row.parentId
                this.moduleId = row.id
                this.activeModuleDatas.auths = row.auths || []
                this.activeModuleDatas.catalogId = row.catalogId || null
                this.activeModuleDatas.refModules = row.refModules || null
                this.state = 'edit'
                this.showAdd = true
                this.showList = false
                this.$store.commit('setCommonObj', { moduleStatus: 'edit' })
                this.$store.commit('setModuleObj', { id: row.id || '' })
            },
            copyModule(row) {
                this.parentId = row.parentId
                this.moduleId = row.id
                this.activeModuleDatas.auths = row.auths || []
                this.activeModuleDatas.catalogId = row.catalogId || null
                this.activeModuleDatas.refModules = row.refModules || null
                this.state = 'copy'
                this.showAdd = true
                this.showList = false
                this.$store.commit('setCommonObj', { moduleStatus: 'copy' })
                this.$store.commit('setModuleObj', { id: row.id || '' })
            },
            deleteDatas(row) {
                let params = {
                    topCatalogId: this.activeRootTree.id,
                }
                this.rbHttp.sendRequest({
                    method: 'DELETE',
                    url: `/v1/cmdb/module/deleteModule/${row.id}`,
                    params: params
                }).then((res) => {
                    if (res.success) {
                        this.$message.success('删除成功！')
                        this.getModule()
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            deleteModule(row) {
                this.$store.commit('setCommonObj', { moduleStatus: 'delete' })
                this.$store.commit('setModuleObj', { id: row.id || '' })
                this.$confirm('确定删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteDatas(row)
                }).catch(() => {
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
    .image {
        width: 24px;
        height: 24px;
        background-color: gray;
    }
    /*.module-table {*/
    /*width: 100%;*/
    /*min-height:calc(100vh - 196px);*/
    /*margin-bottom: 20px;*/
    /*/deep/.el-table td{*/
    /*padding: 5px 0;*/
    /*}*/
    /*/deep/ .el-table__body-wrapper{*/
    /*height: calc(100vh - 196px - 48px);*/
    /*overflow-y: auto;*/
    /*}*/
    /*}*/
</style>
