<template>
    <div class="iframe-wrap"
         v-loading.body.lock="pageLoading"
         :element-loading-text="loading_text">
        <div v-for="(item,index) in pageList"
             :key="index">
            <iframe :src="item"
                    v-show="page === item"
                    id="myIframe"
                    class="iframe"
                    frameborder='0'
                    scrolling='auto'
                    style='overflow-x: hidden；overflow-y: auto'
                    :class="{ 
                        'bg-color' : page.includes('/previewPages/index.html'), 
                        'flow-page' : $route.path === '/resource/flow'
                    }"></iframe>
            <div v-if="!item"
                 class="t-center">没找到可用视图</div>
        </div>
    </div>
</template>
<script>
    import iframeObject from 'src/pages/iframe/iframe.js'

    export default {
        name: 'CommonView',
        props: {

        },
        data () {
            return {
                page: '',
                pageList: [],
                pageLoading: false,
                loading_text: '请稍候...',
            }
        },
        computed: {
            routeQuery () {
                return this.$route.query
            },
            // bpm跳转url
            curRouterHash () {
                return this.$store.state.bpm.curRouterHash
            },
        },
        watch: {
            routeQuery: {
                handler (newVal) {
                    // url变更后，更新当前routerHash
                    this.$store.commit('setCurRouterHash', newVal.routerHash)
                    this.initPage()
                },
                deep: true,
                immediate: true
            },
        },
        created () {
            console.log(this.page)
        },
        mounted () {
            this.getIframeHeight()
        },
        methods: {
            getPath () {
                return this.page
            },
            initPage () {
                let query = this.$route.query
                // 其它静态页面演示
                if (query.path && query.path.includes('demohtml')) {
                    this.page = iframeObject['/resource/ohters'].page + query.path + '/index.html'

                    // 全路径跳转页面
                } else if (query.fullpath) {
                    this.page = query.fullpath

                } else {
                    const curTreeNode = this.$route.query.curTreeNode
                    this.page = iframeObject[this.$route.path].page
                    // 跳转流程工单新增/详情/列表页：获取路径
                    if (query.routerHash) {
                        // this.page = 'http://localhost:8081' + '/front/#/' + query.routerHash
                        this.page = sessionStorage.getItem('X7_SERVER_URL') + '/front/#/' + query.routerHash
                    }

                    // 跳转流程增加 token 自动登录
                    if (this.$route.path === '/resource/flow') {
                        // this.page = 'http://10.12.70.40:8080?toPage=/cmdb/v3/cmdbConfigDict?biz_sys=DHXT'
                        this.page = this.page + '?mirrorToken=' + sessionStorage.getItem('mirror') + '&pageFrom=ums'
                        // 流程检索子类菜单
                        if (curTreeNode) {
                            this.page = this.page + '&curTreeNode=' + curTreeNode
                        }
                        if (!this.pageList.includes(this.page)) {
                            this.pageLoading = true
                        } else {
                            this.pageLoading = false
                        }
                    }

                    // 跳转流程manage增加 token 自动登录
                    if (this.$route.path === '/resource/manage') {
                        // this.page = 'http://localhost:3000' + '/manage/?code=' + sessionStorage.getItem('mirror') + '&service=s&type=mirrorToken&pageFrom=ums'
                        this.page = sessionStorage.getItem('X7_MANAGE_URL') + '/manage/?code=' + sessionStorage.getItem('mirror') + '&service=s&type=mirrorToken&pageFrom=ums'
                    }

                    // 云管客服
                    if (this.$route.path === '/iframe/yunguan') {

                        this.page = this.page + '?token=' + sessionStorage.getItem('yunguanToken')

                    }
                }
                if (!this.pageList.includes(this.page)) {
                    this.pageList.push(this.page)
                }
            },
            getIframeHeight () {
                let self = this
                let myIframe = document.getElementById('myIframe')
                if (this.$route.path === '/iframe/yunguan') {
                    myIframe.style.height = '88vh'
                }
                window.addEventListener('message', function (e) {
                    if (e.data === 'changed' || e.data.height === 0) {
                        return
                    } else if (e.data.height > 200) {
                        myIframe.style.height = e.data.height + 135 + 'px'
                        self.pageLoading = false
                    }

                    // bpm跳转事件
                    if (e.data.bpmJump) {
                        // console.log('e.data.bpmJump------------------------------------------', e.data, self.curRouterHash)
                        if (self.curRouterHash === e.data.toPage) {
                            return
                        }
                        self.$store.commit('setCurRouterHash', e.data.toPage)
                        self.$nextTick(() => {
                            self.$router.push({
                                path: '/resource/flow',
                                query: {
                                    routerHash: e.data.toPage.replace(/^\//, ''),
                                    currentTitle: e.data.currentTitle
                                }
                            })
                        })
                    }
                })
            }
        }
    }
</script>
<style>
    .iframe {
      width: 100%;
      height: 100vh;
      border-width: 0;
      overflow-x: hidden;
    }
    .bg-color {
      background: #01061a;
    }
</style>
