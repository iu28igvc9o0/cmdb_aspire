<template>
    <div class="components-container yw-dashboard container-view drag-main-box wp100"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <div class="wp100 displaybox"
             :class="{ 'light-mode' : pageType === 'light'}">
            <div class="relative drawer-menu-box"
                 v-if="curViewId">
                <el-drawer custom-class="menu-list"
                           :visible.sync="drawerShow"
                           :modal="false"
                           :append-to-body="false"
                           :direction="direction">
                    <!-- 左侧菜单列表 -->
                    <modulesMenu :drawerShow="drawerShow"
                                 :pageType="pageType"
                                 :viewsData="viewsData"></modulesMenu>
                </el-drawer>
                <div class="menu-btns">
                    <el-button @click="drawerShow = true"
                               class="menu-btn"
                               icon="el-icon-arrow-right"
                               title="模块菜单"
                               type="primary">
                    </el-button>
                    <el-button @click="addModule"
                               class="menu-btn"
                               icon="el-icon-plus"
                               title="增加模块"
                               type="primary">
                    </el-button>
                    <el-button @click="resetModule"
                               class="menu-btn"
                               icon="el-icon-refresh-left"
                               title="还原模块"
                               type="primary">
                    </el-button>
                    <el-button @click="designPageView"
                               class="menu-btn"
                               icon="el-icon-finished"
                               title="保存视图"
                               type="primary">
                    </el-button>
                </div>
            </div>
            <div class="main-right boxflex01"
                 ref="mainRight">
                <div class="wechat-btn pointer"
                     v-if="$route.query.pageCode === 'bpmIndex'"
                     @click="qimoChatClick()">在线咨询</div>
                <preview ref="designPage"
                         :readonly="!curViewId"
                         :pageCode="pageCode"
                         :pageType="pageType"
                         :viewsData="viewsData"
                         @updatePageType="updatePageType"
                         @scrollToBottom="scrollToBottom"
                         @updatecurrentPageData="updatecurrentPageData"></preview>
            </div>
        </div>
    </div>
</template> 
<script>
    import draggable from 'vuedraggable'
    import modulesMenu from './menu'
    import preview from '../preview/index.vue'
    import drag from '../mixin/drag'
    import rbCustomServices from 'src/services/custom_pages/rb-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        name: 'CustomPagesMain',
        mixins: [drag, rbAutoOperationMixin],
        components: {
            draggable,
            modulesMenu,
            preview
        },
        data() {
            return {
                drawerShow: false,
                direction: 'ltr',
                viewsData: [],
                resPageType: '',    // 接口返回的pageType
            }
        },
        computed: {
            username() {
                return sessionStorage.getItem('username')
            },
            curViewId() {
                return this.$route.query.id
            },
            // pageCode定义 综合首页： index ；监控首页:  mirrorAlertIndex ；告警首页： alertIndex ；资源首页： resourceIndex ；服务台首页： bpmIndex
            pageCode() {
                return this.$route.query.pageCode
            },
            // 当前行定制化页面的内容
            storeRowContent() {
                return this.$store.state.homeStore.currentRowContent
            },
            // 当前页面定制化内容
            curPageContent() {
                // 带id编辑状态，模块为当前行内容
                if (this.curViewId) {
                    return this.storeRowContent
                } else {
                    return []
                }
            },
            // 当前页面模版类型，确定用哪个模版样式
            pageType() {
                return this.$route.query.pageType || this.$route.params.pageType || this.resPageType
            },
        },
        watch: {
            curPageContent: {
                handler: function (newVal) {
                    this.viewsData = newVal
                },
                immediate: true,
                deep: true
            }
        },
        mounted() {
            if (this.$route.query.pageCode === 'bpmIndex') {
                this.initOnlineServer()
            }
        },
        methods: {
            // 接入云管客服在线咨询功能 autoShow=false 点击按钮触发
            initOnlineServer() {
                const oScript = document.createElement('script')
                oScript.type = 'text/javascript'
                oScript.src = window.ONLINE_SERVER_URL
                document.body.appendChild(oScript)
            },
            qimoChatClick() {
                window.qimoChatClick()
            },
            gotoWechat() {
                this.$router.push({
                    path: '/iframe/yunguan',
                    query: {
                        yg_routerHash: '/index/webchat',
                        currentTitle: '在线咨询'
                    }
                })
            },
            // 更新接口返回的pageType
            updatePageType(data) {
                this.resPageType = data
            },
            scrollToBottom() {
                this.$nextTick(() => {
                    this.$refs.mainRight.scrollTop = this.$refs.mainRight.scrollHeight + 250
                })
            },
            // 还原模块
            resetModule() {
                this.$refs.designPage.resetModule()
            },
            // 新增模块
            addModule() {
                this.$refs.designPage.dialogBoxShow = true
            },
            // 保存视图
            designPageView() {
                let req = {
                    user_id: this.username,
                    id: this.curViewId,
                    content: JSON.stringify(this.viewsData),
                }
                rbCustomServices
                    .designPageView(req)
                    .then(res => {
                        if (res === 'success') {
                            this.$message.success('保存成功')
                        } else {
                            this.$message.error(res)
                        }
                        this.pageLoading = false
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            updatecurrentPageData(data) {
                this.viewsData = data
                this.$store.commit('setCurrentRowContent', data)
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import '../mixin/drag.scss';
</style>