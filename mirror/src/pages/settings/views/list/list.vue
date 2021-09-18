<template>
    <div class="components-container yw-dashboard">
        <el-form class="yw-form components-condition"
                 label-width="70px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="视图编码">
                <el-input v-model="formData.code"
                          placeholder=""></el-input>
            </el-form-item>
            <el-form-item label="视图名称">
                <el-input v-model="formData.name"
                          placeholder=""></el-input>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="init()">查询</el-button>
                <el-button @click="add()">创建新视图</el-button>
            </section>
        </el-form>

        <el-form class="yw-form"
                 style="height: calc(100% - 40px);">
            <!-- tab -->
            <el-tabs class="yw-tabs"
                     v-model="activeTabName"
                     @tab-click="changeTab">
                <el-tab-pane :label="item.label"
                             tabindex="-1"
                             :name="item.name"
                             v-for="(item,index) in tabDatas"
                             :key="index">
                </el-tab-pane>
            </el-tabs>
            <!-- tab -->
            <div class="views-wrap"
                 style="height: calc(100% - 80px);">

                <section class="views-item"
                         v-if="!result || result.length<1"
                         :key="viewsIndex">
                    <div class="views-header clearfix">
                    </div>
                    <div class="views-main">
                        <p class="views-empty">暂无业务视图</p>
                    </div>
                </section>
                <section class="views-item"
                         v-else
                         v-for="(viewsItem,viewsIndex) in result"
                         :key="viewsIndex">
                    <div class="views-header clearfix">
                        <span class="views-title">{{viewsItem.name}}</span>
                        <div class="views-btn fr">
                            <el-tooltip class="item"
                                        effect="dark"
                                        content="刷新"
                                        placement="bottom"
                                        :hide-after=1000>
                                <el-button type="text"
                                           @click="refresh(viewsItem)"
                                           class="el-icon-refresh">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item"
                                        effect="dark"
                                        content="编辑"
                                        placement="bottom"
                                        :hide-after=1000>
                                <el-button type="text"
                                           @click="edit(viewsItem)"
                                           class="el-icon-edit">
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item"
                                        effect="dark"
                                        content="删除"
                                        placement="bottom"
                                        :hide-after=1000>
                                <el-button type="text"
                                           @click="del(viewsItem)"
                                           class="el-icon-delete">
                                </el-button>
                            </el-tooltip>
                        </div>
                    </div>
                    <div class="views-main">
                        <el-tree class="arrow-show"
                                 :data="viewsItem.treeDatas"
                                 :default-expand-all="true"
                                 :props="defaultProps"
                                 @current-change="changeTreeBefore(viewsItem)"
                                 @node-click="changeTree">
                            <span class="custom-tree-node"
                                  slot-scope="{ node, data }">
                                <i class="icon-arrow el-icon-caret-right"
                                   :class="{'is-leaf':data['is-leaf']}"></i>
                                <i :class="data.icon"></i>
                                <span>{{ data.nodeName }}</span>
                                <span v-if="data.showInfo">({{data.showInfo}})</span>
                            </span>
                        </el-tree>
                    </div>
                </section>

            </div>
        </el-form>
        <section class="page-wrap">
            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          page-sizes=""
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </section>

        <!-- dialog -->
        <DialogEdit :dialogMsg="dialogMsg"
                    @closeDialog="closeDialog"
                    v-if="dialogMsg.dialogVisible"></DialogEdit>
        <!-- dialog -->

    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import CommonOption from 'src/utils/commonOption.js'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory'

    export default {
        mixins: [QueryObject, CommonOption],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            DialogEdit: () => import('../dialog-edit.vue'),
        },
        data() {
            return {
                // 表单数据
                formData: {
                    code: '',
                    name: '',
                },
                // 查询条件
                queryParams: {

                },
                // 查询结果
                result: [],
                // dialog
                dialogMsg: {
                    dialogVisible: false,
                    id: '',// 每个弹窗数据的唯一标识
                    data: {} // 数据(暂时没有详情接口，从列表数据携带)
                },
                // tab
                activeTabName: '',
                tabDatas: [],
                // tree配置
                defaultProps: {
                    // children: 'children',
                    // label: 'label'
                    children: 'subNodeList',
                    label: 'nodeName'
                },
                // 当前活动视图(正在操作的视图)
                activeView: ''
            }
        },

        methods: {
            // 设置参数
            setParams(activePagination) {
                this.pageSize = 6

                if (activePagination) {
                    this.queryParams['currentPage'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'currentPage': this.initPageChange(),
                        'pageSize': this.pageSize,
                        viewCode: this.formData.code,
                        viewName: this.formData.name,
                        catalogId: this.activeTabName,

                    }
                }

            },

            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.setParams(activePagination)
                this.queryViewsList()

            },
            // 获得视图列表
            queryViewsList() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })

                rbCmdbService.getViewsList(this.queryParams).then((res) => {
                    this.tabDatas.some((item) => {
                        if (item.name === this.activeTabName) {
                            item.total = res.totalSize
                            return true
                        }
                    })
                    this.total = res.totalSize
                    this.result = res.data.map((item) => {
                        let obj = Object.assign({}, item, {
                            name: `${item.viewCode} - ${item.viewName}`,
                            treeDatas: ''
                        })

                        // 查询视图树
                        this.queryViewsTree({
                            viewCode: item.viewCode,
                        }, obj.treeDatas, obj)

                        return obj
                    })

                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 获得视图树
            queryViewsTree(param = {}, datas = '', rootNode = {}) {
                // this.showLoading()
                let params = {
                    viewCode: param.viewCode,
                    nodeList: param.nodeList,
                }
                return rbCmdbService.getViewsTree(params).then((res) => {
                    if (datas) {
                        this.$set(datas, 'subNodeList', res.nodeList)
                        if (!res.nodeList || res.nodeList.length < 1) {
                            datas['is-leaf'] = true
                        }
                    } else {
                        this.$set(rootNode, 'treeDatas', res.nodeList)
                    }
                    return datas

                }).finally(() => {
                    // this.closeLoading()
                })
            },


            // 获得tab
            getTabDatas() {
                let params = {}
                return rbCmdbModuleService.getRootTree(params).then((res) => {
                    this.tabDatas = res.map((item) => {
                        return {
                            label: item.catalogName,
                            name: item.id,
                            catalogCode: item.catalogCode,
                            total: -1
                        }
                    })
                    this.activeTabName = this.tabDatas[0].name
                    return this.activeTabName
                })
            },
            changeTab() {
                this.query()
            },
            changeTreeBefore(val) {
                if (val) {
                    this.activeView = val
                }
            },
            changeTree(val) {
                let nodeList = [{ nodeId: val.nodeId, queryInfo: val.queryInfo, queryToData: val.queryToData }]
                if (!val.nodeId && !val.queryInfo) {
                    nodeList = null
                }
                let params = {
                    viewCode: this.activeView.viewCode,
                    nodeList: nodeList,
                }

                this.queryViewsTree(params, val)
            },
            refresh(item) {
                let params = {
                    type: 'CMDB_VIEW',
                    key: item.viewCode
                }
                rbCmdbService.refreshViewsTree(params).then((res) => {
                    if (res.success) {
                        this.$message.success(res.message)
                        this.query()
                    } else {
                        this.$message.error(res.message)
                    }
                })

            },
            edit(item) {
                this.dialogMsg.data.item = item
                this.dialogMsg.data.type = 'edit'
                this.dialogMsg.data.title = '编辑视图'
                this.dialogMsg.dialogVisible = true
            },
            del(item) {
                this.$confirm('确定删除吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let params = {
                        id: item.id
                    }
                    return rbCmdbService.deleteViewsTree(params).then((res) => {
                        if (res.success) {
                            this.$message.success(res.message)
                            this.query()
                        } else {
                            this.$message.error(res.message)
                        }

                    }).finally(() => {

                    })

                })
            },

            add() {
                this.dialogMsg.data.type = 'add'
                this.dialogMsg.data.title = '创建新视图'
                this.dialogMsg.dialogVisible = true
            },
            // 关闭弹窗
            closeDialog(val) {
                this.dialogMsg.dialogVisible = false
                if (val === 'update') {
                    this.init()
                }

            },
            // 初始化
            async init() {
                await this.getTabDatas()
                this.query()
            }
        },
        mounted() {
            this.init()
        }
    }
</script>

<style lang="scss" scoped>
    @import "src/assets/css/views.scss";
    .components-container {
        position: relative;
        padding-bottom: 60px;
        .page-wrap {
            position: absolute;
            bottom: 20px;
            right: 20px;
        }
    }
    .components-condition {
        padding-right: 180px;
    }
</style>
