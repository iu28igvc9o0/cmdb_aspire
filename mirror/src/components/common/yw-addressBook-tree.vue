<template>
    <!-- 通讯录多级树(部门全部加载、人员异步加载、搜索静态和联想) -->
    <div class="yw-code-tree"
         :style="{height:options && options.style && options.style.height ? options.style.height : '100%'}">
        <!-- 搜索框 -->
        <section class="search-wrap"
                 v-show="options && options.search && options.search.show">
            <!-- <el-input :placeholder="(options && options.search && options.search.placeholder) ? options.search.placeholder : '筛选'"
                      v-model="filterText">
            </el-input> -->
            <el-autocomplete v-model="filterText"
                             class="tree-autocomplete"
                             value-key="showContent"
                             :fetch-suggestions="querySearchAsync"
                             placeholder="筛选"
                             @select="handleSelect"></el-autocomplete>
            <el-button type="text"
                       icon="el-icon-refresh"
                       @click="refresh()"></el-button>
        </section>
        <!-- 搜索框 -->

        <!-- 树 -->
        <section class="tree-wrap"
                 v-loading="loading"
                 element-loading-text="加载中">
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
    export default {
        props: ['options'],
        components: {

        },
        data() {
            return {
                loading: false,
                // 搜索内容
                filterText: '',
                // 树属性
                props: {
                    label: 'name',
                    children: 'childList',
                    isLeaf: 'leaf',
                    key: 'uuid'// 自定义唯一标识字段
                },
                // 当前选中的节点数据
                activeTreeDatas: {},
                // 默认展开子节点的key
                defaultExpandedKeys: []
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
            }
        },
        watch: {
            filterText(val) {
                this.$refs.asyncTree.filter(val)
            }
        },

        methods: {
            // 刷新
            refresh() {
                this.filterText = ''
            },

            // 异步加载节点(加载过的节点会在缓存中取，不会再走此接口)
            async loadNode(node, resolve) {
                if (node.level === 0) {
                    this.loading = true
                    // 初始化
                    let resultObj = await this.queryTree()
                    let datas = resultObj.result
                    this.defaultExpandedKeys = datas.map((item) => {
                        return item[this.props.key]
                    })
                    this.loading = false

                    return resolve(datas)
                } else {

                    let datas = []
                    if (node.data[this.props['children']] && node.data[this.props['children']].length > 0) {
                        datas = node.data[this.props['children']]
                    }
                    // else if (node.data['userPayloadList'] && node.data['userPayloadList'].length > 0) {
                    //     datas = node.data[this.props['userPayloadList']]
                    // } 
                    else {
                        let resultObj = await this.queryTree(node.data['uuid'])
                        datas = resultObj.result
                    }

                    return resolve(datas)
                }

            },
            // 自定义节点(绑定点击事件)
            renderContent(h, { node, data }) {
                let content = (
                    <p class="custom-tree-node clearfix" style="width:100%;" on-click={() => this.clickTree(node, data)}>
                        <i class={data.icon}></i>
                        <span>{node.label}</span>
                        <i class="fa fa-user-plus fr" style="margin-right:15px;"></i>
                    </p>)
                if (data.mobile) {
                    content = (
                        <p class="custom-tree-node clearfix" on-click={() => this.clickTree(node, data)}>
                            <i class={data.icon}></i>
                            <span>{node.label}</span>
                            <span v-if="data.mobile">({data.mobile})</span>
                        </p>)
                }
                return content
            },

            // 节点过滤
            filterNode(value, data) {
                value = value.trim()
                if (!value) return true
                return data[this.props.label].indexOf(value) !== -1
            },

            // 获得视图树
            queryTree(deptId = '') {

                return this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/department/deptTree',
                    params: {
                        deptId: deptId
                    }
                }).then((res) => {
                    let obj = {
                        result: [],
                        deptList: res.deptList,
                        userList: res.userList
                    }

                    if (res.deptList && res.deptList.length > 0) {
                        obj.result = res.deptList
                    } else if (res.userList && res.userList.length > 0) {
                        obj.result = res.userList
                    } else {
                        obj.result = []
                    }

                    return obj
                }).catch((res) => {
                    this.$message.error(res)
                }).finally(() => {
                })
            },

            // 点击树节点事件
            async clickTree(node, data) {

                // 传值
                let userList = []
                if (data['user_type']) {
                    // 人员
                    userList = [data]

                } else {
                    // 部门
                    let resultObj = await this.queryTree(data['uuid'])
                    userList = resultObj.userList
                }

                this.$emit('clickTree', { node: node, userList: userList })
                // 设置节点选中
                this.$nextTick(() => {
                    this.$refs.asyncTree.setCurrentKey(data[this.props['key']])
                })
                // 缓存选中数据
                this.activeTreeDatas = Object.assign({}, data)

            },
            querySearchAsync(queryString, cb) {
                if (!queryString || !queryString.trim()) {
                    cb([])
                    return false
                }
                return this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/user/pageList',
                    params: {
                        pageNo: 1,
                        pageSize: 100,
                        search: queryString
                    }
                }).then((res) => {
                    if (res.result) {
                        res.result.map((item) => {
                            item.showContent = `${item.name}<${item.mobile}>(${item.dept_id})`
                        })
                    }
                    cb(res.result ? res.result : [])
                    return res
                })
            },
            handleSelect(val) {
                this.clickTree({}, val)
                this.filterText = ''
            }

        },
        created() {

        },

    }
</script>
<style lang="scss" scoped>
    .tree-autocomplete {
        width: calc(100% - 65px);
    }
    .yw-code-tree {
        height: 100%;
        .search-wrap {
            width: 100%;
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
