<template>
    <div class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div class="managementLeft">
            <el-input placeholder="输入厂商名称过滤"
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
                      slot-scope="{ node }">
                    <span>
                        <!--<img width="16"-->
                        <!--height="16"-->
                        <!--:src="data.iconUrl"-->
                        <!--class="image" />-->
                        {{ node.label }}
                    </span>
                </span>
            </el-tree>
            <!--<module-tree @changeModuleId="changeModuleId" @showLoading="showLoading" @hideLoading="hideLoading"></module-tree>-->
        </div>
        <div class="managementRight">
            <instance-list :parentParams="produceData"
                           :moduleType="moduleType"
                           :queryType="queryType"
                           @showLoading="showLoading"
                           @hideLoading="hideLoading"
                           :condicationCode="condicationCode"></instance-list>
        </div>
    </div>
</template>
<script>

    export default {
        name: 'ResourceIframeProduceView',
        components: {
            instanceList: () => import('src/pages/resource/iframe/list/view.vue')
        },
        data() {
            return {
                moduleType: 'host',
                condicationCode: 'instance_list',
                loading: false,
                loading_text: '正在加载数据，请稍等...',
                produceData: {
                    mfrFactory: '',
                    moduleId: '',
                    deviceType: ''
                },
                // queryType: 'module',
                queryType: 'tree',
                filterText: '',
                defaultProps: {
                    children: 'childModules',
                    label: 'produce_name'
                },
                // defaultProps: {
                //   children: 'subList',
                //   label: 'name',
                //   isLeaf: 'leaf',
                //   subSize: 'subSize'
                // },
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
                // 获取品牌
                this.rbHttp.sendRequest({
                    method: 'POST',
                    params: { params: { 'produce_type': '生产供应商' } },
                    url: '/v1/cmdb/module/module/data?moduleType=default_produce_module_id'
                }).then((res) => {
                    res.forEach(item => {
                        _.$set(item, 'type', 'produce')
                        _.menuParent.push(item)
                    })
                }).catch((e) => {
                    console.error(e)
                })
            },
            filterNode(value, data) {
                if (!value) return true
                return data.produce_name.indexOf(value) !== -1
            },
            nodeClick(node) {
                this.produceData = {}
                if (node.type === 'produce') {
                    this.produceData = {
                        mfrFactory: node.id
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
    }
</style>
<style lang="stylus">
    .managementLeft .el-tree-node:focus>.el-tree-node__content {
        color: #2089DA;
    }
</style>
