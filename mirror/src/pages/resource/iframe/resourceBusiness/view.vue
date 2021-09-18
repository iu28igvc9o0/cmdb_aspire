<template>
    <div @click="router()"
         class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <!-- <div class="managementLeft">
            <el-input placeholder="输入模型名称过滤"
                      v-model="filterText">
            </el-input>
            <el-tree class="filter-tree"
                     :data="menuParent"
                     :props="defaultProps"
                     :filter-node-method="filterNode"
                     node-key="id"
                     @node-click="nodeClick"
                     :default-expanded-keys="defaultOpenNav"
                     ref="tree2">
                <span class="custom-tree-node"
                      slot-scope="{ node, data }">
                    <span>
                        <img width="16"
                             height="16"
                             :src="data.iconUrl"
                             class="image" /> {{ node.label }}
                    </span>
                </span>
            </el-tree>
            <module-tree @changeModuleId="changeModuleId" @showLoading="showLoading" @hideLoading="hideLoading"></module-tree>
    </div> -->
        <div class="managementRight">
            <instance-list :parentParams="moduleData"
                           :moduleType="moduleType"
                           :queryType="queryType"
                           @showLoading="showLoading"
                           @hideLoading="hideLoading"
                           :condicationCode="condicationCode"></instance-list>
        </div>
    </div>
</template>
<script>
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    export default {
        name: 'ResourceIframeResourceBusiness',
        components: {
            instanceList: () => import('src/pages/resource/iframe/list/view.vue')
        },
        data() {
            return {
                condicationCode: 'business_list',
                moduleType: 'business',
                loading: false,
                loading_text: '正在加载数据...',
                moduleData: {
                    moduleId: '',
                    deviceType: '',
                    deviceClass: ''
                    // moduleName: '' // 以前的
                },

                deviceClassName: '',
                queryType: 'tree',
                filterText: '',
                defaultProps: {
                    children: 'childModules',
                    label: 'name'
                },
                menuParent: [], // 源导航
                defaultOpenNav: [''] // 默认展开的Nav
            }
        },
        watch: {
            filterText(val) {
                this.$refs.tree2.filter(val)
            }
        },
        mounted: function () {
            this.initModuleTree()
        },
        methods: {
            // router() {
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
            initModuleTree() {
                let _ = this
                _.menuParent = []
                // 获取默认的主机分类列表
                rbCmdbModuleServiceFactory.getModuleTree({ 'moduleType': 'business' }).then((data) => {
                    _.menuParent = data
                    console.log('资源视图树状图数据', _.menuParent)
                }).catch((e) => {
                    console.error(e)
                })
            },
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            nodeClick(node, tree) {
                this.moduleData = {
                    moduleId: node.id,
                    deviceType: node.name,
                    deviceClass: node.name,
                    all: {
                        value: node.name,
                        key: node.name,
                        id: node.uuid,
                        type: node.type,
                        tree: tree
                    }
                }
            },
            refresh() {
                this.moduleId = ''
                this.filterText = ''
                this.initModuleTree()
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

        .image {
            background: #666;
        }

        .managementRight {
            width: 100%;
        }
    }
</style>
<style lang="stylus">
    .managementLeft .el-tree-node:focus>.el-tree-node__content {
        color: #2089DA;
    }
</style>
