<template>
    <div class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div class="managementLeft">
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
                             :src="CMDB_SERVER_URL + data.iconUrl"
                             class="image" /> {{ node.label }}
                    </span>
                </span>
            </el-tree>
            <!--<module-tree @changeModuleId="changeModuleId" @showLoading="showLoading" @hideLoading="hideLoading"></module-tree>-->
        </div>
        <div class="managementRight">
            <instance-list :parentParams="moduleData"
                           :queryType="queryType"
                           @showLoading="showLoading"
                           @hideLoading="hideLoading"
                           :condicationCode="condicationCode"
                           :moduleType="moduleType"
                           :catalogId="catalogId"></instance-list>
        </div>
    </div>
</template>
<script>
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import { getUrlKey } from 'src/assets/js/utility/rb-filters.js'
    export default {
        name: 'ResourceIframeResourceClassification',
        components: {
            instanceList: () => import('src/pages/resource/iframe/list/view.vue')
        },
        data() {
            return {
                moduleType: 'host',
                catalogId: '96603733-5793-11ea-ab96-fa163e982f89',
                condicationCode: 'instance_list',
                loading: false,
                loading_text: '正在加载数据，请稍等...',
                deviceClassMap: {},
                deviceTypeMap: {},
                moduleData: {
                    moduleId: '',
                    deviceType: '',
                    deviceClass: ''
                    // moduleName: '' // 以前的
                },

                deviceClassName: '',
                deviceTypeName: '',

                queryType: 'tree',
                filterText: '',
                defaultProps: {
                    children: 'childModules',
                    label: 'name'
                },
                menuParent: [], // 源导航
                defaultOpenNav: [''], // 默认展开的Nav
                CMDB_SERVER_URL: window.CMDB_SERVER_URL
            }
        },
        watch: {
            filterText(val) {
                this.$refs.tree2.filter(val)
            }
        },
        mounted: function () {
            this.initUrlParams()
            this.initModuleTree()
            this.getDeviceClassAndType()
        },
        methods: {
            getDeviceClassAndType() {
                rbConfigDictServiceFactory.getDictsByType({ type: 'device_class' }).then((res) => {
                    res.forEach(item => {
                        this.$set(this.deviceClassMap, item.name, item.id)
                    })
                })
                rbConfigDictServiceFactory.getDictsByType({ type: 'device_type' }).then((res) => {
                    res.forEach(item => {
                        this.$set(this.deviceTypeMap, item.name, item.id)
                    })

                })
            },
            showLoading(loading_text) {
                this.loading = true
                if (loading_text) {
                    this.loading_text = loading_text
                }
            },
            hideLoading() {
                this.loading = false
            },
            initUrlParams() {
                if (getUrlKey('moduleType')) {
                    this.moduleType = getUrlKey('moduleType')
                }
                if (getUrlKey('catalogId')) {
                    this.catalogId = getUrlKey('catalogId')
                }
                if (getUrlKey('condicationCode')) {
                    this.condicationCode = getUrlKey('condicationCode')
                }
            },
            initModuleTree() {
                let _ = this
                _.menuParent = []
                _.defaultOpenNav = []
                // 获取默认的主机分类列表
                rbCmdbModuleServiceFactory.getModuleTree({
                    'catalogId': this.catalogId || '',
                    'moduleType': this.moduleType || ''
                }).then((data) => {
                    // 权限列表
                    const myMenuCode = [0]
                    _.defaultOpenNav.push(data[0].id)
                    const filterMenu = (data, menuCode) => {
                        return data.filter(item => {
                            return menuCode.indexOf(item.isDisplay) > -1
                        }).map(item => {
                            item = Object.assign({}, item)
                            if (item.childModules.length > 0) {
                                item.childModules = filterMenu(item.childModules, menuCode)
                            }
                            return item
                        })
                    }
                    // 过滤后的菜单
                    const myMenu = filterMenu(data, myMenuCode)
                    // debugger
                    _.menuParent = myMenu
                }).catch((e) => {
                    this.$message.error(e)
                })
            },
            filterNode(value, data) {
                if (data.isDisplay == 1) return true
            },
            nodeClick(node, tree) {
                let nodeName = node.name
                let parentName
                if (tree.parent) {
                    parentName = tree.parent.data.name
                }
                // 一级节点
                if (tree.level === 1) {
                    this.moduleData = {
                        level: 1,
                        moduleId: node.id,
                        deviceType: '',
                        deviceClassName: '',
                        deviceClass: '',
                        catalogId: this.catalogId,
                        tree: tree
                    }
                }
                // 二级节点
                if (tree.level === 2) {
                    this.moduleData = {
                        level: 2,
                        moduleId: node.id,
                        deviceType: '',
                        deviceClassName: nodeName,
                        deviceClass: this.deviceClassMap[nodeName],
                        catalogId: this.catalogId,
                        tree: tree
                    }
                }
                // 三级节点
                if (tree.level === 3) {
                    this.moduleData = {
                        level: 3,
                        moduleId: node.id,
                        deviceType: this.deviceTypeMap[nodeName],
                        deviceClassName: parentName,
                        deviceClass: this.deviceClassMap[parentName],
                        deviceTypeName: nodeName,
                        catalogId: this.catalogId,
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
    }
</style>
<style lang="stylus">
    .managementLeft .el-tree-node:focus>.el-tree-node__content {
        color: #2089DA;
    }
</style>
