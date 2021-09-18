<template>
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
                <!-- 右侧内容 -->
                <instance-list :parentParams="parentParams"
                               :queryType="queryType"
                               :additionalParams="additionalParams"
                               @showLoading="showLoading"
                               @hideLoading="hideLoading"
                               :condicationCode="condicationCode"
                               :moduleType="moduleType"></instance-list>
                <!-- 右侧内容 -->

            </el-main>
        </el-container>
    </div>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        name: 'ResourceIframeViewComponents',
        mixins: [CommonOption],
        components: {
            YwTree: () => import('src/components/common/yw-code-tree.vue'),
            instanceList: () => import('./view.vue')
        },
        data() {
            return {
                // 左侧tree
                treeOptions: {
                    dataParams: {
                        // 树数据来源编码
                        treeCode: '',
                    },
                    search: {
                        show: true,
                    },
                    style: {
                        height: 'calc(100vh - 120px)'
                    }
                },
                // 右侧查询条件
                condicationCode: 'instance_list',// 条件来源编码
                parentParams: {},// 选中树参数
                queryType: 'tree',// cmdb原有参数
                moduleType: 'host',// cmdb原有参数
                additionalParams: window.projectName == '集中网管' ? true : false
                // 右侧表格
                // module_id: '9409510cdcc14228a1ac2bbef35f9eca'// 表格来源编码(view.vue原有代码已经监听)
            }
        },
        watch: {
            $route: {
                handler(to, from) {
                    if (to.fullPath !== this.$store.state.homeStore.curPageFullpath) {
                        this.$store.commit('setCurPageFullpath', to.fullPath)
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
                // debugger
                if (val.data && val.data.queryToData) {
                    this.parentParams = JSON.parse(JSON.stringify(val.data))
                } else {
                    this.$message.error('查询配置条件缺少' + val.data.nodeName + '条件, 请确认更新条件配置')
                }

            },
            // 显示加载条
            showLoading(loading_text) {
                let text = '正在查询数据, 请稍等...'
                if (loading_text) {
                    text = loading_text
                }
                this.showFullScreenLoading({ text: text })
            },
            // 隐藏加载条
            hideLoading() {
                this.closeFullScreenLoading()
            },
            // 初始化
            init() {
                this.treeOptions.dataParams.treeCode = this.$route.query.treeCode
                this.condicationCode = this.$route.query.condicationCode
            }
        },
        created() {
            // this.init()
        }
    }
</script>

<style>
</style>