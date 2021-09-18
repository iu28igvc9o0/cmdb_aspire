<template>
    <!-- 配置类多级树(异步加载) -->
    <div class="yw-code-tree"
         v-if="refreshTree"
         :style="{height:options && options.style && options.style.height ? options.style.height : '100%'}">
        <!-- 搜索框 -->
        <section class="search-wrap"
                 v-show="options && options.search && options.search.show">
            <el-input :placeholder="(options && options.search && options.search.placeholder) ? options.search.placeholder : '筛选'"
                      v-model="filterText">
            </el-input>
            <el-button type="text"
                       icon="el-icon-refresh"
                       @click="refresh()"></el-button>
        </section>
        <!-- 搜索框 -->

        <!-- 树 -->
        <section class="tree-wrap">
            <el-tree :props="props"
                     :load="loadNode"
                     :highlight-current="true"
                     :default-expand-all="false"
                     :default-expanded-keys="defaultExpandedKeys"
                     :expand-on-click-node="false"
                     :node-key="props['key']"
                     :current-node-key="activeTreeDatas[props['key']]"
                     :render-content="renderContent"
                     :filter-node-method="filterNode"
                     ref="asyncTree"
                     lazy>
            </el-tree>
        </section>
        <!-- 树 -->

    </div>

</template>

<script>
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory'
    export default {
        props: ['options'],
        components: {

        },
        data() {
            return {
                // 刷新树
                refreshTree: true,
                // 搜索内容
                filterText: '',
                // 树属性
                props: {
                    label: 'nodeName',
                    children: 'subNodeList',
                    isLeaf: 'leaf',
                    key: 'nodeName'// 自定义唯一标识字段
                },
                // 当前选中的节点数据
                activeTreeDatas: {},
                // 自定义配置
                // options: {
                //  dataParams: {//参数
                //     treeCode: 'department_view',//code类型
                // },
                //     search: {//搜索框
                //         show: true,//是否显示搜索框
                //     },
                //     style: {//样式
                //         height: 'calc(100vh - 300px)'
                //     }
                // },
                // 默认展开的节点key
                defaultExpandedKeys: []

            }
        },
        watch: {
            filterText(val) {
                this.$refs.asyncTree.filter(val)
            },
            'options.dataParams.treeCode': {
                handler(newVal, oldVal) {
                    if (newVal !== oldVal) {
                        this.refreshTree = false
                        this.$nextTick(() => {
                            this.refreshTree = true
                        })
                    }
                },
                // immediate: true,
                // deep: true
            }
        },

        methods: {
            // 刷新
            refresh() {
                this.filterText = ''

                let params = {
                    'type': 'CMDB_VIEW',
                    'key': this.options.dataParams.treeCode
                }
                return rbCmdbService.refreshTree(params).then((res) => {
                    if (res.success) {
                        this.refreshTree = false
                        this.$nextTick(() => {
                            this.refreshTree = true
                        })
                        this.$message.success(res.message)
                    } else {
                        this.$message.error(res.message)
                    }
                    return res

                })
            },

            // 异步加载节点(加载过的节点会在缓存中取，不会再走此接口)
            async loadNode(node, resolve) {
                if (!this.options || !this.options.dataParams || !this.options.dataParams.treeCode) {
                    console.error('treeCode不能为空')
                    return false
                }

                if (node.level === 0) {
                    // 初始化
                    let datas = await this.queryTree(this.options.dataParams.treeCode, null)
                    // 默认展开第一级
                    if (datas && datas.length < 2) {
                        this.defaultExpandedKeys = [datas[0][this.props.key]]
                    }

                    // this.clickTree(node, datas[0]) //默认选中第一条数据
                    return resolve(datas)
                } else {
                    let datas = []
                    if (node.data[this.props['children']]) {
                        datas = node.data[this.props['children']]
                    } else {
                        datas = await this.queryTree(this.options.dataParams.treeCode, [node.data])
                    }

                    return resolve(datas)
                }

            },
            // 自定义节点(绑定点击事件)
            renderContent(h, { node, data }) {
                let count = ''
                if (data && typeof data === 'object' && 'showInfo' in data) {
                    count = `(${data.showInfo})`
                } else {
                    count = ''
                }

                return (
                    <p class="custom-tree-node" on-click={() => this.clickTree(node, data)}>
                        <i class={data.icon}></i>
                        <span>{node.label}</span>
                        <span>{count}</span>
                    </p>)
            },

            // 节点过滤
            filterNode(value, data) {
                value = value.trim()
                if (!value) return true
                return data[this.props.label].indexOf(value) !== -1
            },

            // 获得视图树
            queryTree(viewCode = '', data = null) {
                let params = {
                    viewCode: viewCode,
                    nodeList: data,
                }
                return rbCmdbService.getViewsTree(params).then((res) => {
                    let result = res.nodeList || []
                    return result

                }).finally(() => {

                })
            },

            // 点击树节点事件
            clickTree(node, data) {
                // 设置节点选中
                this.$nextTick(() => {
                    this.$refs.asyncTree.setCurrentKey(data[this.props['key']])
                })
                // 缓存选中数据
                this.activeTreeDatas = Object.assign({}, data)
                this.$emit('clickTree', { node: node, data: data })
            },

        },
        created() {

        },

    }
</script>
<style lang="scss" scoped>
    .yw-code-tree {
        height: 100%;
        .search-wrap {
            width: 100%;
            .el-input {
                width: calc(100% - 30px);
                display: inline-block;
                vertical-align: middle;
            }
            .el-button {
                display: inline-block;
                vertical-align: middle;
                font-size: 22px;
                padding: 0;
            }
        }
        .tree-wrap {
            height: 100%;
            overflow: auto;
        }
    }
</style>
