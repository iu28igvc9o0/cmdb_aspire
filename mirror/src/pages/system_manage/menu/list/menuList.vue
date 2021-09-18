<template>
    <div class="components-container">
        <el-form class="yw-form components-condition"
                 label-width="75px"
                 :inline="true">
            <el-form-item label="系统">
                <el-select v-model="sysId"
                           clearable>
                    <el-option v-for="(item, index) in sysList"
                               :key="index"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="queryMenu()">查询
                </el-button>

                <el-button type="primary"
                           @click="add()">新增菜单
                </el-button>
            </section>
        </el-form>
        <div class="yw-el-table-wrap">
            <el-table class="yw-el-table"
                      v-loading="loading"
                      :data="menuList"
                      stripe
                      border
                      row-key="id"
                      default-expand-all
                      :tree-props="{children: 'children', hasChildren: 'hasChildren'}">

                <el-table-column label="名称"
                                 width="200">
                    <template slot-scope="scope">
                        <a @click="view(scope.row.id)">{{scope.row.name}}</a>
                    </template>
                </el-table-column>
                <el-table-column label="链接"
                                 show-overflow-tooltip
                                 width="540">
                    <template slot-scope="scope">
                        <span v-if="scope.row.base">{{scope.row.base}}</span>
                        <span v-else-if="scope.row.path">{{scope.row.path}}</span>
                        <span v-else-if="scope.row.url">{{scope.row.url}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="文件名"
                                 width="140">
                    <template slot-scope="scope">
                        <span v-if="scope.row.component">{{scope.row.component}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="排序"
                                 width="80">
                    <template slot-scope="scope">
                        {{scope.row.sort}}
                    </template>
                </el-table-column>
                <el-table-column label="是否显示"
                                 width="80">
                    <template slot-scope="scope">
                        <span v-if="scope.row.isShow === '0'">隐藏</span>
                        <span v-else>显示</span>
                    </template>
                </el-table-column>
                <el-table-column label="页面类型"
                                 width="140">
                    <template slot-scope="scope">
                        <span v-if="scope.row.menuType === 'children'">父菜单</span>
                        <span v-else-if="scope.row.menuType === 'routers'">菜单</span>
                        <span v-else-if="scope.row.menuType === 'vue'">vue页面</span>
                        <span v-else-if="scope.row.menuType === 'page'">外嵌页面</span>
                        <span v-else-if="scope.row.menuType === 'iframe'">内嵌页面</span>
                        <span v-else>{{scope.row.menuType}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="创建人"
                                 width="140">
                    <template slot-scope="scope">
                        {{scope.row.creater}}
                    </template>
                </el-table-column>
                <el-table-column prop="region"
                                 label="操作"
                                 min-width="240">
                    <template slot-scope="scope">
                        <div class="yw-table-operator">
                            <el-button type="text"
                                       @click="view(scope.row.id)"
                                       title="查看"
                                       icon="el-icon-view"></el-button>
                            <el-button type="text"
                                       @click="edit(scope.row.id)"
                                       title="编辑"
                                       icon="el-icon-edit"
                                       v-if="checkUse(scope.row)"></el-button>
                            <el-button type="text"
                                       title="删除"
                                       @click="del(scope.row.id)"
                                       icon="el-icon-delete"
                                       v-if="checkUse(scope.row)"></el-button>
                            <el-button type="text"
                                       title="添加下级菜单"
                                       @click="add(scope.row)"
                                       icon="el-icon-bottom-right"
                                       v-if="scope.row.menuType === 'children' || scope.row.menuType === 'routers'"></el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog class="yw-dialog"
                   width="400px"
                   :close-on-click-modal="false"
                   title="新增菜单"
                   :visible.sync="dialogMsg.dialogVisible">
            <DialogMenu :dialogMsg="dialogMsg"
                        @closeDialog="closeDialog">
            </DialogMenu>
        </el-dialog>
    </div>
</template>

<script>
    import rbSysMenuServiceFactory from '../../../../services/sys/rb-sys-menu-service.factory'

    export default {
        // name: '/logs/device/list',
        mixins: [],
        components: {
            DialogMenu: () => import('./dialog-menu.vue'),
        },
        data() {
            return {
                sysList: [],
                sysId: '',
                menuList: [],
                loading: true,
                // dialog
                dialogMsg: {
                    dialogVisible: false,
                    data: {}, // 数据
                    type: ''
                },
            }
        },
        created() {

            this.querySystem()
        },
        methods: {
            querySystem() {
                rbSysMenuServiceFactory.getSystemList().then(data => {
                    this.sysList = data
                    this.sysId = data[0].id
                    this.queryMenu()
                })
            },
            queryMenu(sysId) {
                this.loading = true
                if (!sysId || sysId === '') {
                    sysId = this.sysId
                }
                rbSysMenuServiceFactory.getMenuListById(sysId).then(data => {
                    console.log('=====', data)
                    this.menuList = data
                }).finally(() => {
                    this.loading = false
                })
            },
            checkUse(rowData) {
                let createUser = rowData.creater
                let userName = sessionStorage.getItem('username')
                let namespace = sessionStorage.getItem('namespace')
                if (userName === createUser || userName === namespace) {
                    return true
                } else {
                    return false
                }
            },
            // 关闭弹窗
            closeDialog(type) {
                this.dialogMsg.dialogVisible = false
                if (type === 'update') {
                    this.queryMenu()
                }

            },
            // 新增
            add(menu) {
                this.dialogMsg.data = {
                    'sysId': this.sysId,
                    'parent': menu
                }
                this.dialogMsg.dialogVisible = true
                this.dialogMsg.type = 'insert'
            },
            view(id) {
                this.dialogMsg.data = {
                    'sysId': this.sysId,
                    'id': id
                }
                this.dialogMsg.dialogVisible = true
                this.dialogMsg.type = 'detail'
            },
            edit(id) {
                this.dialogMsg.data = {
                    'sysId': this.sysId,
                    'id': id
                }
                this.dialogMsg.dialogVisible = true
                this.dialogMsg.type = 'update'
            },
            del(id) {
                this.$confirm('是否删除此菜单?').then(() => {
                    rbSysMenuServiceFactory.delete(id).then(() => {
                        this.queryMenu()
                    })
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
