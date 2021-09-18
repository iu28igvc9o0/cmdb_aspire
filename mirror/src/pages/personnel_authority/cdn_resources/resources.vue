<!--
@author lupengjie
@date   20.9.16
@description cdn资源权限
-->
<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-row :gutter="35"
                        type="flex"
                        align="middle">
                    <el-col :span="8">
                        <el-button type="text"
                                   icon="el-icon-plus"
                                   @click="handleAdd">增加资源权限</el-button>
                    </el-col>
                    <el-col :span="15">
                        <el-tabs v-model="activeName"
                                 @tab-click="handleClick">
                            <el-tab-pane label="资源维度"
                                         name="first"></el-tab-pane>
                        </el-tabs>
                    </el-col>
                </el-row>
            </div>
            <div>
                <el-row :gutter="35">
                    <el-col :span="8">
                        <div class="resoure">
                            <div class="res-manage__main">
                                <comtree class="res-manage__tree"
                                         :ref="treeName1"
                                         :data="resourceData"
                                         :props="resourceTreeDefault"
                                         node-key="id"
                                         ex-title="资源权限"
                                         :exId="treeName1"
                                         :ex-control="true"
                                         :ex-control-opt="customControl"
                                         ex-show-search
                                         @node-click="handleNodeClick">
                                </comtree>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="12"
                            style="padding-left:0;">
                        <!-- 设备资源 -->
                        <div class="resoure"
                             v-show="activeName === 'first'">
                            <div class="res-manage__main">
                                <el-row :gutter="50">
                                    <el-col :span="20"
                                            class="res-manage__aicon">
                                        <p class="res-manage__title">省份</p>
                                        <el-row :gutter="50">
                                            <el-col :span="18">
                                                <comtree :ref="treeName2"
                                                         class="res-manage__tree"
                                                         node-key="uuid"
                                                         :default-expanded-keys="areaDataExpanded"
                                                         :default-checked-keys="areaDataChecked"
                                                         :data="areaData"
                                                         :props="areaDataDefault"
                                                         show-checkbox
                                                         :exId="treeName2"
                                                         ex-show-search></comtree>
                                            </el-col>
                                        </el-row>
                                    </el-col>

                                </el-row>
                            </div>
                        </div>
                    </el-col>
                    <el-dialog :visible.sync="dialogTableVisible"
                               :show-close="false"
                               width="450px"
                               top="12%"
                               class="acc-manage__dialog">
                        <span class="dialog__header">已绑定账号</span>
                        <el-table class="mod-table"
                                  :data="accountList"
                                  style="width: 100%"
                                  highlight-current-row>
                            <el-table-column prop="account"
                                             label="账号">
                            </el-table-column>
                            <el-table-column prop="name"
                                             label="用户名">
                            </el-table-column>
                            <el-table-column prop="dept_id"
                                             label="部门">
                            </el-table-column>
                            <el-table-column prop="no"
                                             label="工号">
                            </el-table-column>
                        </el-table>
                        <span slot="footer"
                              class="acc-manage dialog-footer">
                            <el-button class="mod-btn"
                                       @click="dialogTableVisible = false">取 消</el-button>
                        </span>
                    </el-dialog>
                </el-row>
            </div>

        </el-form>
    </div>

</template>
<script>
    import resourceDataService from 'src/services/sys/reource-cdn-services.js'
    import resourceDataMixin from 'src/services/sys/reource-mixin.js'
    import comtree from 'src/pages/system_manage/components/tree.vue'
    import systemAccountService from 'src/services/sys/account-services.js'
    export default {
        name: 'ResourceListPage',
        components: {
            comtree
        },
        data() {
            const that = this
            return {
                treeName1: 'resourceTree',
                treeName2: 'areaTree',
                resourceData: [],
                resourceTreeDefault: {
                    label: 'name',
                    children: 'childList'
                },
                areaData: [],

                areaDataExpanded: [],
                areaDataChecked: [],
                areaDataDefault: {
                    label: 'name',
                    children: 'subList',
                    disabled: function () {
                        return true
                    }
                },

                deviceAuthData: [],
                customControl: [
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
                selectedNode: '',
                needAddRoot: false,
                dialogTableVisible: false,
                accountList: [],
            }
        },
        watch: {
            deviceAuthData: {
                handler() {
                    this.$forceUpdate()
                },
                deep: true
            }
        },
        mixins: [resourceDataMixin],
        methods: {

            getParseId(node, authIdArray) {
                if (node.data.id) {
                    authIdArray.push(node.data.id)
                }
                if (node.parent) {
                    this.getParseId(node.parent, authIdArray)
                }
            },
            // 重置
            resetData() {
                let treeArr = [this.treeName2, 'groupSource', 'workSource', 'scriptSource', 'yumSource']
                treeArr.forEach((item, index) => {
                    this.$refs[item] && this.$refs[item].setCheckedKeys([])
                    if (index > 3) {
                        this[item + 'DataChecked'] = []
                    }
                })

                this.areaDataChecked = []
            },
            // 点新增
            handleAdd() {
                if (this.selectedNode) {
                    this.goAdd(this.selectedNode)
                } else {
                    this.goAdd(resourceDataService.ROOT_ID)
                }
            },
            goAdd(pid) {
                this.$router.push({
                    path: '/personnel_authority/cdn_resources-add',
                    query: {
                        pid: pid
                    }
                })
            },
            // 点击资源
            handleNodeClick(data) {
                this.selectedNode = data.uuid
                this.getResourceDetail(data.uuid)
            },
            // 获取账户列表
            getAccountList(pageSize, pageNo, searchData) {
                this.accountList = []
                systemAccountService.getAccountList(pageSize, pageNo, searchData).then((res) => {
                    if (res) {
                        res.results.map((item) => {
                            if (item.userInfo !== undefined) {
                                this.accountList.push({
                                    account: item.username,
                                    name: item.userInfo.name,
                                    dept_id: item.userInfo.dept_id,
                                    no: item.userInfo.no
                                })
                            }
                        })
                    }
                })
            },
            // 修改
            customEdit(node, data) {
                this.$router.push({
                    path: '/personnel_authority/cdn_resources-add',
                    query: {
                        id: data.uuid
                    }
                })
            },
            // 删除
            customDelete(node, data) {
                this.$confirm('确定要删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    resourceDataService.deleteResource(data.uuid).then(() => {
                        this.$message({
                            message: '操作成功',
                            type: 'success'
                        })
                        this.$refs[this.treeName1].remove(node)
                    })
                })
            },
            // 显示详细数据
            handleDetailData(data) {
                const constraints = data.permissions[0].constraints[0]

                if (constraints.area) {
                    var areaChecked = constraints.area.split(',')
                    let allIds = []
                    let roomDataChecked = []
                    areaChecked.forEach(item => {
                        let itemArea = item.split('_')
                        if (!allIds.includes(itemArea[0])) {
                            allIds.push(itemArea[0])
                        }
                        if (!roomDataChecked.includes(itemArea[1])) {
                            roomDataChecked.push(itemArea[1])
                        }
                    })
                    this.areaDataChecked = roomDataChecked
                }
            },
            // 获取资源数据 cdn
            getResourceList() {
                resourceDataService.getResourceList().then((res) => {
                    if (res) {
                        this.needAddRoot = res.length === 0
                    }
                    this.resourceData = res
                })
            },

            // 获取通用权限
            getAreaData() {
                resourceDataService.getAreaTree().then((res) => {
                    this.areaData = res
                    if (res.length > 0) {
                        this.areaDataExpanded = [res[0].id]
                    }
                })
            },

            // 获取详细数据
            getResourceDetail(id) {
                this.resetData()
                resourceDataService.getResourceDetail(id).then((res) => {
                    this.handleDetailData(res)
                })
            },
        },
        mounted() {
            this.getResourceList()
            this.getAccountList()
            this.initDevice('cdn')
        },
        actived() {
            this.getResourceList()
            this.getAccountList()
            this.initDevice('cdn')

        }
    }
</script>
<style lang="scss" scoped>
    /deep/.yw-search-box {
        width: 100%;
    }

    .mod-btn {
        border-radius: 8px;
    }
    .res-manage__tree {
        /deep/ .el-tree {
            overflow-y: auto;
            overflow-x: auto;
            border-radius: 8px;
        }
    }
    .resoure {
        // background: #f4f4f4;
        height: 500px;
        padding: 20px;
        border-radius: 8px;
        /deep/ .el-tree {
            padding: 10px;
            height: 400px;
            border: 1px solid #dcdfe6;
            border-radius: 8px;
        }
    }
    .res-manage {
        .el-tree {
            padding: 10px;
            height: 507px;
            border: 1px solid #dcdfe6;
            border-radius: 8px;
        }

        .res-manage__top {
            margin-bottom: 20px;
            padding-left: 17.5px;
            padding-right: 17.5px;
        }

        .res-manage__main {
            margin-bottom: 20px;
            padding: 20px 20px 20px 0;
            border-top: 1px solid #f4f4f4;
        }

        .res-manage__tree {
            .el-tree {
                height: 400px;
            }
        }

        .res-manage__aicon {
            position: relative;
            .icon {
                margin-top: 12px;
                position: absolute;
                top: 50%;
                right: -10px;
                font-size: 24px;
                color: #dddddd;
            }
        }

        .res-manage__title {
            font-size: 12px;
        }
    }
    /deep/ .acc-manage__dialog {
        font-size: 12px;
        overflow: hidden;
        .el-dialog {
            margin: 0 auto;
            margin-bottom: 0;
            border: 1px solid rgba(229, 232, 237, 1);
            box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.2);
            border-radius: 16px;
        }
        .dialog__header {
            display: block;
            padding: 14px 0 18px;
        }
        .el-dialog__body {
            font-size: 12px;
            height: 240px;
            overflow-y: auto;
        }
    }
</style>
