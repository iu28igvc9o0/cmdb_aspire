<template>
    <div class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div id="TREE"
             class="managementLeft">
            <div class="inputQuery">
                <el-input prefix-icon="el-icon-search"
                          v-model="filterText"
                          placeholder="请输入部门或业务名称查询"></el-input>
            </div>
            <div class="tree">
                <el-tree class="filter-tree"
                         :data="data2"
                         :props="defaultProps"
                         :filter-node-method="filterNode"
                         node-key="uuid"
                         highlight-current
                         @node-click="nodeClick"
                         ref="tree2"
                         accordion>
                    <span class="custom-tree-node"
                          slot-scope="{ node, data }">
                        <span>
                            <span>
                                <i :class="data.icon"></i>
                                <span v-if="data.type === 'module'">{{node.label}}</span>
                                <span v-else-if="data.type !== 'bizSystem'">{{node.label}} ({{data.subSize?data.subSize:0}})</span>
                                <span v-else>{{node.label}} ({{data.instanceSize?data.instanceSize:0}})</span>
                            </span>
                        </span>
                    </span>
                </el-tree>
            </div>
        </div>
        <div class="managementRight">
            <instance-list :parentParams="bizSystemCondition"
                           :queryType="queryType"
                           @showLoading="showLoading"
                           :additionalParams="additionalParams"
                           @hideLoading="hideLoading"
                           :condicationCode="condicationCode"
                           :moduleType="moduleType"></instance-list>
        </div>
    </div>
</template>
<script>
    import instanceList from 'src/pages/resource/iframe/list/view.vue'
    import orgManagerService from 'src/services/cmdb/rb-orgManager-service.factory'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    export default {
        name: 'ResourceIframeResourceManagement',
        components: {
            instanceList
        },
        data() {
            return {
                moduleType: 'host',
                condicationCode: 'instance_list',
                loading: false,
                loading_text: '正在加载数据，请稍等...',
                businessUuid: '',
                moduleId: '',
                filterText: '',
                department1: '',
                department2: '',
                bizSystem: '',
                deviceType: '',
                bizSystemCondition: {},
                queryType: 'tree',
                data2: [],
                defaultProps: {
                    children: 'subList',
                    label: 'name',
                    isLeaf: 'leaf',
                    subSizeaa: 'subSize'
                },
                additionalParams: window.projectName == '集中网管' ? true : false
            }
        },
        mounted: function () {
            this.initTree()
        },
        watch: {
            filterText(val) {
                this.$refs.tree2.filter(val)
            }
        },
        methods: {
            // router() {
            //     console.log('this.$route.query.name', this.$route.query.name, this.$route.query.seriesName)
            //     // let businessUuid = document.getElementsByClassName('managementLeft')[0]
            //     // document.getElementById('TREE').style.background = 'red'
            //     // .console.log('0-0-0-0-', bbb)
            //     // bbb.style.background = 'red'
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
                orgManagerService.loadTreeDepBiz().then((data) => {
                    _t.data2 = data
                    console.log(data)
                }).catch(() => {
                })
            },
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            resolveCondition(tree) {
                if (tree.data.type === 'module') {
                    this.moduleId = tree.data.uuid
                    this.deviceType = tree.data.name
                }
                if (tree.data.type === 'biz') {
                    this.bizSystem = tree.data.name
                }
                if (tree.data.type === 'department2') {
                    this.department2 = tree.data.name
                }
                if (tree.data.type === 'department1') {
                    this.department1 = tree.data.name
                    this.businessUuid = tree.data.uuid
                }
                if (tree.level !== 0) {
                    this.resolveCondition(tree.parent)
                }
            },
            nodeClick(node, tree) {
                console.log(tree)
                console.log('业务视图树', node)
                this.department1 = ''
                this.department2 = ''
                this.bizSystem = ''
                this.moduleId = ''
                this.deviceType = ''
                this.businessUuid = ''
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
                    moduleId: this.moduleId,
                    // moduleId: node.uuid,
                    deviceType: this.deviceType,
                    businessUuid: this.businessUuid,
                    all: {
                        treeData: this.data2,
                        node: node,
                        value: node.name,
                        key: node.name,
                        id: node.uuid,
                        type: node.type,
                        tree: tree
                    }
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
<style lang="stylus" scoped>
    .resourceManagement {
        display: flex;
        overflow: auto;
        overflow: hidden;
        height: 100%;
        width: 100%;

        .managementLeft {
            box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
            flex: inherit;
            width: 250px;
            height: 97%;
            padding: 10px;
            background: #fff;
            overflow-y: auto;
            overflow-x: auto;

            .inputQuery {
                width: 90%;
                margin: 0 auto;
            }
        }

        .managementRight {
            overflow-y: auto;
            width: calc(100% - 250px);
            height: 100%;
        }

        &::-webkit-scrollbar {
            width: 0 !important;
        }
    }

    .resourceManagement::-webkit-scrollbar {
        width: 0 !important;
    }
</style>
<style lang="stylus">
    .managementLeft .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
        color: #2089DA;
    }
</style>
