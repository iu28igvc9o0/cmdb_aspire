<template>
    <div @click="routerClick()"
         class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div class="managementLeft">
            <div class="inputQuery">
                <el-input prefix-icon="el-icon-search"
                          v-model="filterText"
                          placeholder="请输入部门或业务名称查询"></el-input>
            </div>
            <div class="tree">
                <el-tree class="filter-tree"
                         :data="dataTree"
                         :props="defaultProps"
                         :filter-node-method="filterNode"
                         node-key="uuid"
                         @node-click="nodeClick"
                         ref="tree2"
                         accordion>
                    <span class="custom-tree-node"
                          slot-scope="{ node, data }">
                        <span>
                            <span>
                                <i :class="data.icon"></i>
                                <span v-if="data.type === 'module'">{{node.label}}</span>
                                <span v-else-if="data.type !== 'biz'">{{node.label}} ({{data.subSize?data.subSize:0}})</span>
                                <span v-else>{{node.label}} ({{data.instanceSize?data.instanceSize:0}})</span>
                            </span>
                        </span>
                    </span>
                </el-tree>
            </div>
        </div>
    </div>
</template>
<script>
    import orgManagerService from 'src/services/cmdb/rb-orgManager-service.factory'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    export default {
        name: 'ResourceIframeResourceManagement',
        components: {
        },
        data() {
            return {
                loading: false,
                input: '',
                loading_text: '正在加载数据...',
                moduleId: '',
                filterText: '',
                department1: '',
                department2: '',
                bizSystem: '',
                bizSystemCondition: {},
                dataTree: [],
                defaultProps: {
                    children: 'subList',
                    label: 'name'
                    // isLeaf: 'leaf',
                    // subSizeaa: 'subSize'
                }
            }
        },
        mounted: function () {
            this.initTree()
        },
        watch: {
            // input 框模糊查询树形原色
            filterText(val) {
                this.$refs.tree2.filter(val)
            }
        },
        methods: {
            // routerClick() {
            //     console.log('this.$route.query.name', this.$route.query.name, this.$route.query.seriesName)
            // },
            showLoading(loading_text) {
                this.loading = true
                if (loading_text) {
                    this.loading_text = loading_text
                }
            },
            hideLoading() {
                this.loading = false
            },
            initTree() {
                let _t = this
                this.loading = true
                orgManagerService.loadTreeDepBiz().then((data) => {
                    console.log('-==-=-=', data)
                    _t.dataTree = data
                    this.loading = false
                }).catch(() => {
                    this.loading = false
                })
            },
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            resolveCondition(tree) {
                if (tree.data.type === 'module') {
                    this.moduleId = tree.data.uuid
                }
                if (tree.data.type === 'biz') {
                    this.bizSystem = tree.data.name
                }
                if (tree.data.type === 'department2') {
                    this.department2 = tree.data.name
                }
                if (tree.data.type === 'department1') {
                    this.department1 = tree.data.name
                }
                if (tree.level !== 0) {
                    this.resolveCondition(tree.parent)
                }
            },
            nodeClick(node, tree) {
                console.log('0-;;;;', node.name, node.uuid)
                this.$emit('sendParent', node.uuid)
                this.department1 = ''
                this.department2 = ''
                this.bizSystem = ''
                this.moduleId = ''
                // 判断节点类型
                this.resolveCondition(tree)
                if (node.type && node.type === 'biz') {
                    if (!node.subList || node.subList.length === 0) {
                        // 加载模型数据
                        let _t = this
                        rbCmdbModuleServiceFactory.getModuleTree().then((data) => {
                            _t.$set(node, 'subList', [])
                            data.forEach((item) => {
                                if (item.childModules && item.childModules.length > 0) {
                                    item.childModules.forEach((item2) => {
                                        node.subList.push({ uuid: item2.id, name: item2.name, icon: item2.iconUrl, type: 'module' })
                                    })
                                }
                            })
                        })
                    }
                }
                // 跳转页面查询
                this.bizSystemCondition = {
                    department1: this.department1,
                    department2: this.department2,
                    bizSystem: this.bizSystem,
                    moduleId: this.moduleId
                }
            },
            refresh() {
                this.department1 = ''
                this.filterText = ''
                this.initTree()
            }
        }
    }
</script>
<style lang="scss" scoped>
    .resourceManagement {
        height: 100%;
        width: 100%;
        .managementLeft {
            float: left;
            width: 250px;
            height: 100%;
            padding: 10px;
            background: #fff;
            overflow-y: auto;
            .inputQuery {
                width: 90%;
                margin: 0 auto;
            }
        }
    }
</style>
