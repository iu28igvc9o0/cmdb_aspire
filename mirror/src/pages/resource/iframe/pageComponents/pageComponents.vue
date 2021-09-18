<template>
    <!-- 通用配置型页面 -->
    <div class="components-container"
         style="padding:0;">
        <el-container>
            <el-aside width="260px"
                      v-if="treeOptions.dataParams.treeCode"
                      style="overflow:hidden"
                      class="aside-tree">
                <!-- 左侧树 -->
                <YwTree @clickTree="clickTree"
                        :options="treeOptions"></YwTree>
                <!-- 左侧树 -->
            </el-aside>
            <el-main class="tree-main"
                     style="padding:0 0 0 10px;">
                <!-- 页面 -->
                <template v-if="pageCode && resetComponent">
                    <component :is="item.componentName.default"
                               :treeParams="treeParams"
                               :condicationCode="condicationCode"
                               :module_id="module_id"
                               :tableHeaderCode="tableHeaderCode"
                               v-for="(item,index) in componentsRequire"
                               :key="index"></component>
                </template>
                <!-- 页面 -->

                <template v-else>
                    <!-- 查询条件(级联下拉框类型) -->
                    <!-- 预留 condicationCode-->
                    <!-- 查询条件 -->

                    <!-- 查询表格 -->
                    <!-- 预留 module_id-->
                    <!-- 查询表格 -->
                </template>

            </el-main>
        </el-container>
    </div>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        name: 'ResourceIframePageComponents',
        mixins: [CommonOption],
        components: {
            YwTree: () => import('src/components/common/yw-code-tree.vue'),
            YwCodeFrame: () => import('src/components/common/yw-code-frame/yw-code-frame.vue'),
        },
        data() {
            return {
                // 表单数据
                formData: {

                },
                // 左侧tree
                treeOptions: {
                    dataParams: {
                        // 树数据来源编码
                        treeCode: '',
                        // treeCode: 'business_auth_tree',
                    },
                    search: {
                        show: true,
                    },
                    style: {
                        height: 'calc(100vh - 120px)'
                    }
                },
                // 页面内容通过路径获取
                pageCode: '',
                // 查询条件
                condicationCode: '',
                // table
                module_id: '',
                // table表头
                tableHeaderCode: '',
                // 树节点数据
                treeParams: ''

            }
        },
        computed: {
            // 动态引入组件
            componentsRequire() {
                let componentsRequire = []
                if (this.pageCode) {
                    let pageCodeList = [this.pageCode]
                    pageCodeList.forEach(item => {
                        componentsRequire.push({ componentName: require(`src/${item}.vue`) })
                        // componentsRequire.push({ componentName: require('src/pages/resource/configurationItem/audit/config_item_audit_copy.vue') })
                    })
                }

                return componentsRequire
            }
        },
        watch: {
            $route: {
                handler(to, from) {
                    if (to.path === '/resource/iframe/pageComponents') {
                        this.init()
                    }
                },
                immediate: true,
                deep: true
            }
        },
        methods: {
            // 点击树
            clickTree(val = {}) {
                if (val.data && val.data.queryToData) {
                    this.treeParams = JSON.parse(JSON.stringify(val.data))
                    this.updateComponent()
                } else {
                    this.treeParams = JSON.parse(JSON.stringify({}))
                    this.updateComponent()
                    // this.$message.error('查询配置条件缺少' + val.data.nodeName + '条件, 请确认更新条件配置')
                }

            },
            // 初始化
            async init() {
                // tree
                this.treeOptions.dataParams.treeCode = this.$route.query.treeCode
                // page
                this.pageCode = this.$route.query.pageCode
                // 查询条件
                this.condicationCode = this.$route.query.condicationCode
                this.module_id = this.$route.query.module_id
                this.tableHeaderCode = this.$route.query.tableHeaderCode
            }
        },
        created() {
            // this.init()
        }
    }
</script>

<style>
</style>