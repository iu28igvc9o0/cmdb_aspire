<template>
    <div id="home"
         v-loading.fullscreen.lock="tabValues.length === 0 && isMenuNormal"
         :element-loading-text="loading_text">
        <div class="home-right-pane"
             :class="{ nav_collapsed: navClosed }">
            <div class="right-head clearfix displaybox">
                <div class="logo-wrap clearfix">
                    <a href="/"
                       class="link">
                        <div class="logo">
                            <img :src="projectLogo" />
                        </div>
                    </a>
                    <span class="split"></span>
                    <h1 class="title-wrap">
                        <!-- 中移服务台logo -->
                        <img v-if="platForm === 'it_devops_receptionp_platform' && cityName === 'master'"
                             class="align-middle"
                             src="/static/img/logo-itcloud-white.png" />

                        <span class="title">{{
                            platForm === "it_devops_receptionp_platform"
                                ? "IT云服务台"
                                : projectName
                        }}</span>
                    </h1>
                </div>
                <div class="nav-wrap clearfix boxflex01">
                    <div>
                        <el-menu class="el-menu-nav"
                                 mode="horizontal"
                                 @select="handleSelect">
                            <!-- 新需求改造导航 -->
                            <template v-for="(item, index) of firstMenu">
                                <el-menu-item v-if="item.url"
                                              :key="index"
                                              :index="`${index}`"
                                              v-show="item.show">
                                    <i class="el-icon-menu"></i>
                                    <span @click="handleFirstMenuClick(item)">{{
                                        item.name
                                    }}</span>
                                </el-menu-item>
                                <el-submenu v-else
                                            :key="index"
                                            :index="`${index}`"
                                            :class="{active: item.active, 'last-child': item.name === '配置'}"
                                            v-show="item.show">
                                    <template slot="title">
                                        <svg class="svg-icon svg-icon-24"
                                             aria-hidden="true">
                                            <use :xlink:href="item.icon"></use>
                                        </svg>
                                        <span @click="handleFirstMenuClick(item)">{{ item.name }}</span>
                                    </template>
                                    <navbar-card :menu="item"
                                                 :currentMenuIndex="index"
                                                 @toRoute="toRoute" />
                                </el-submenu>
                            </template>
                        </el-menu>
                    </div>
                </div>
                <Btn v-if="platForm === 'it_devops_receptionp_platform' && cityName === 'master'" />
                <div class="user-wrap">
                    <div class="profile-dropdown clearfix fr">
                        <span class="split"></span>
                        <span class="user">{{ username
                            }}{{ roleName ? "(" + roleName + ")" : " " }}
                        </span>
                        <span class="logout"
                              @click="logout">退出</span>
                    </div>
                </div>
            </div>
            <div class="right-tab">
                <svg class="svg-icon svg-icon-20 el-icon-delete"
                     @click="removeAllTab()"
                     aria-hidden="true">
                    <use xlink:href="#iconshanchu-baobiaojitoubutab"></use>
                </svg>
                <ul class="tab-list">
                    <li v-for="(item, index) in tabValues"
                        :key="index"
                        class="tab-list__item"
                        :class="{ active: index === tabIndex }"
                        @click="activeTab(item, index)">
                        <el-tooltip :content="item.title"
                                    placement="top"
                                    :tabindex="-1"
                                    class="yw-tooltip">
                            <p>{{ item.title }}</p>
                        </el-tooltip>
                        <i class="el-icon-close"
                           @click.stop="removeTab(item, index)"></i>
                    </li>
                </ul>
            </div>
            <div class="right-body"
                 v-if="tabValues.length > 0">
                <!-- {{includePagesName}} -->
                <keep-alive :include="includePagesName">
                    <router-view :key="routerKey"
                                 ref="curRouter"
                                 v-on:closeTab="closeTab"></router-view>
                </keep-alive>
            </div>
        </div>
    </div>
</template>

<script>
    import Theme from 'src/assets/theme/index.js'
    import cookieService from 'src/services/cookie.service.js'
    import rbProjectDataService from 'src/services/rb-project-data-service.factory.js'
    import rbRoleDataServiceFactory from 'src/services/rbac_role/rb-role-data-service.factory.js'
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    import NavbarCard from './navbar-card'
    import Btn from './homeBtn'
    export default {
        name: 'HomePage',
        mixins: [Theme],
        components: {
            NavbarCard,
            Btn
        },
        data: function () {
            return {
                isMenuNormal: true,
                loading_text: '请稍候...',
                // flag: false,
                tabIndex: 0,
                tabValue: '',
                tabValues: [],
                username: '',
                namespace: '',
                roleName: '',
                navClosed: true, // 新需求关闭左侧导航栏
                activeIndex: '0',
                currentFirstLevel: 0,
                navConfig: this.$store.state.homeStore.operResource,

                hasClickMenu: false,
                // 全局环境变量
                projectLogo: window.projectLogo,
                projectName: window.projectName,
                cityName: window.cityName,
                // 判断是否有定制化首页
                hasCustomView: false,
                // 项目标识字段，默认为ums项目：it_devops_manage_platform
                platForm: 'it_devops_manage_platform',
                platFormInfo: {
                    it_devops_manage_platform: 'index',
                    it_devops_receptionp_platform: 'bpmIndex'
                },
                // 当前项目定制化首页标识，默认为ums定制化首页
                curPageCode: 'index',
                // 定制化首页名称
                customPageName: '',
                // 定制化首页样式类型，默认为深色模式
                pageType: 'dark'
            }
        },
        computed: {
            // 一级菜单数据
            firstMenu: function () {
                const levelOne = []
                this.navConfig.categories.forEach(function (item) {
                    levelOne.push({
                        base: item.base,
                        name: item.name,
                        url: item.url,
                        children: item.children,
                        icon: item.icon,
                        active: false,
                        show: item.show
                    })
                })
                return levelOne
            },
            // 二级菜单数据
            secondMenu: function () {
                return this.navConfig.categories[this.currentFirstLevel].children
            },
            // 本地环境
            isLocalEnv() {
                return location && location.host.includes('localhost')
            },
            // 已缓存页面，逗号分隔
            includePagesName() {
                return this.$store.state.homeStore.includePagesName
            },
            // 当前tab名称
            curRouterName() {
                return (
                    this.tabValues[this.tabIndex] &&
                    this.tabValues[this.tabIndex].name
                )
            },
            // bpm页面，用name做key值，其他页面使用fullPath
            routerKey() {
                return this.$route.path.includes('resource/flow')
                    ? this.$route.name
                    : this.$route.fullPath
            }
        },
        watch: {
            $route(to, from) {
                let isFromPageInTabs = false

                let toFullPath = to.fullPath
                let fromFullPath = from.fullPath.split('?')[0]

                let isExist = false

                this.tabValues.forEach((item, index) => {
                    // 带有参数 useOneTab=1，使用同一个tab
                    let useOneTab =
                        index !== 0 &&
                        item.fullPath.split('?')[0] === to.path &&
                        to.query.useOneTab &&
                        item.fullPath.includes('useOneTab')
                    // 以全路径（含参数来区分tab），使用不同tab；
                    if (item.fullPath === toFullPath || useOneTab) {
                        isExist = true
                        item.fullPath = toFullPath
                    }
                })
                let title = to.query.currentTitle || to.meta.title
                if (!isExist) {
                    if (title) {
                        this.tabValues.push({
                            fullPath: toFullPath,
                            title: title,
                            name: to.name
                        })
                    }
                } else if (to.query.currentTitle) {
                    // tab已打开，更新共用页面的title
                    this.tabValues.forEach(item => {
                        if (item.fullPath === toFullPath) {
                            item.title = title
                        }
                    })
                }

                this.tabValue = toFullPath
                // 高亮当前tab
                this.tabValues.forEach((val, key) => {
                    if (val.fullPath === this.tabValue) {
                        this.tabIndex = key
                    }
                    // 判断页面是否在tab中打开
                    if (val.fullPath === fromFullPath) {
                        isFromPageInTabs = true
                    }
                })

                if (
                    isFromPageInTabs &&
                    this.hasClickMenu &&
                    fromFullPath !== '/home/mapindex'
                ) {
                    from.meta.keepAlive = true
                } else {
                    from.meta.keepAlive = false
                }
            },
            curRouterName: {
                handler: function (newVal) {
                    if (newVal && this.$refs && this.$refs.curRouter) {
                        let pageName = this.handlePageName(newVal)
                        if (
                            pageName &&
                            !this.includePagesName.includes(pageName + ',')
                        ) {
                            this.$store.commit('setIncludePagesName', pageName)
                        }
                    }
                },
                immediate: true
            }
        },
        created() {
            /**
             * 不同项目菜单 platForm
             * 服务台： it_devops_receptionp_platform
             * IT云运维管理平台： it_devops_manage_platform
             */
            this.platForm = sessionStorage.getItem('platForm') || this.platForm
            this.curPageCode = this.platFormInfo[this.platForm]

            this.username = sessionStorage.getItem('username')
            this.namespace = sessionStorage.getItem('namespace')
            this.roleName = sessionStorage.getItem('roleName')
            this.getUserInfo()
            this.getDepartment()
        },
        mounted() {
            let logValid = sessionStorage.getItem('logValid')
            if (!logValid || logValid === 'false') {
                this.$router.push({ path: '/login' })
                return
            }
            if (this.navConfig.categories.length) {
                this.handleMenuPermissions()
            } else {
                this.isMenuNormal = false
                console.log(
                    '该账号权限出错，导致可操作菜单为空！请检查账号权限（接口：v1/roles/alauda/getRoleByUserName）'
                )
            }
        },
        methods: {
            // 根据router.name转换为页面name属性
            handlePageName(str) {
                if (!str) {
                    return ''
                } else {
                    return (str = str
                        .replace(/\?.*/, '')
                        .replace(/\/\w|-\w|_\w/g, function (theStr) {
                            return theStr.slice(1).toUpperCase()
                        }))
                }
            },
            // 根据帐号过滤菜单权限
            handleMenuPermissions() {
                this.navConfig.categories = this.navConfig.categories.filter(
                    data => {
                        return data.name !== 'other'
                    }
                )

                if (this.namespace !== this.username) {
                    // 根据权限过滤菜单
                    rbRoleDataServiceFactory
                        .getMenuPermissions(this.username)
                        .then(permissions => {
                            // 将权限信息缓存到session里
                            sessionStorage.setItem('permissions', permissions)
                            if (permissions.includes('*')) {
                                return
                            }
                            this.navConfig.categories = this.navConfig.categories.filter(
                                first => {
                                    if (first.children && first.children.length > 0) {
                                        first.children = first.children.filter(
                                            second => {
                                                if (
                                                    second.children &&
                                                    second.children.length > 0
                                                ) {
                                                    second.children = second.children.filter(
                                                        third => {
                                                            if (
                                                                third.resource &&
                                                                !permissions.includes(
                                                                    third.resource
                                                                )
                                                            ) {
                                                                return false
                                                            } else {
                                                                return true
                                                            }
                                                        }
                                                    )
                                                    if (
                                                        second.children.length > 0
                                                    ) {
                                                        return true
                                                    } else {
                                                        return false
                                                    }
                                                } else {
                                                    if (
                                                        second.resource &&
                                                        !permissions.includes(
                                                            second.resource
                                                        )
                                                    ) {
                                                        return false
                                                    } else {
                                                        return true
                                                    }
                                                }
                                            }
                                        )
                                        return true
                                    } else if (first.url) {
                                        return true
                                    } else {
                                        return false
                                    }
                                }
                            )
                            this.$router.push({ path: '/' })
                            this.handleSelect(0)
                        })
                } else {
                    // 将权限信息缓存到session里
                    sessionStorage.setItem('permissions', '*')
                    this.$router.push({ path: '/' })
                    this.handleSelect(0)
                }
            },
            closeTab(val) {
                let removeItem = ''
                let removeIndex = ''
                this.tabValues.forEach((item, index) => {
                    if (val === item.fullPath) {
                        removeItem = item
                        removeIndex = index
                    }
                })
                this.removeTab(removeItem, removeIndex)
            },
            // 科管项目：一级菜单点击直接进入页面
            handleFirstMenuClick(item) {
                if (this.cityName !== 'keguan') {
                    return
                }
                this.gotoCurrentPath(item.base)
                item.active = true
            },
            // 路由导航
            toRoute({ item, parent, menu, currentMenuIndex }) {
                this.currentFirstLevel = currentMenuIndex
                this.hasClickMenu = true
                if (window.projectName == '集中网管') {
                    rbProjectDataService
                        .operationLog(item.routers[0].path)
                        .then(data => {
                            console.log(data)
                        })
                }
                // 如果有 url 的一级路由直接打开新窗口
                if (item.url) {
                    this.handleUrlType(item)
                    return
                }

                // 限制窗口标签的数量(窗口标签的最小宽度是108px，标签的总和不允许超过整个窗口的宽度)
                if (
                    (this.tabValues.length + 1) * 108 >=
                    document.body.offsetWidth
                ) {
                    this.$message.warning('窗口标签已达上限，无法添加')
                    return
                }

                let path = item.base + item.routers[0].path
                path = parent.base + path

                this.gotoCurrentPath(path)

                menu.active = true
            },
            // 外链跳转菜单
            async handleUrlType(item) {
                if (item.name && item.name.trim().startsWith('流程')) {
                    window.open(
                        item.url +
                        '?hasTab=true&mirrorToken=' +
                        sessionStorage.getItem('mirror'),
                        '_blank'
                    )
                } else if (item.name && item.name.trim() === '容量管理平台') {
                    window.open(
                        item.url +
                        '?storageToken=' +
                        sessionStorage.getItem('mirror'),
                        '_blank'
                    )
                }
                // 加上外部链接start
                else if (item.name && item.name.trim() === '运维报表') {
                    window.open(
                        item.url +
                        '?user=' +
                        sessionStorage.getItem('usermirror'),
                        '_blank'
                    )
                }
                else if (item.name && item.name.trim() === '资源管理系统') {
                    window.open(
                        item.url +
                        '?user=' +
                        sessionStorage.getItem('usermirror'),
                        '_blank'
                    )
                }
                // end结束外部链接
                else if (item.name.trim() === '租户运营月报') {
                    let department1 = sessionStorage.getItem('userDepartmentOne')
                    let department2 = sessionStorage.getItem('userDepartmentTwo')
                    if (['中国移动'].indexOf(department1) > -1) {
                        department1 = department2
                        department2 = null
                    }

                    let linkQuestions =
                        location.protocol +
                        '//' +
                        location.host +
                        '/cmdb/solveQuestions/process'
                    let toPage = '/cmdb/solveQuestions/process'
                    let userName = sessionStorage.getItem('username')
                    window.open(
                        `${item.url}?department1=${department1}&department2=${department2}&linkQuestions=${linkQuestions}&userName=${userName}&toPage=${toPage}`,
                        '_blank'
                    )
                } else if (item.name.trim() === '计费账单大屏(租户侧)' || item.name.trim() === '计费账单大屏(运营侧)') {
                    let department1 = sessionStorage.getItem('userDepartmentOne')
                    let department2 = sessionStorage.getItem('userDepartmentTwo')
                    let username = sessionStorage.getItem('username')

                    window.open(
                        `${item.url}?department1=${department1}&department2=${department2}&username=${username}`,
                        '_blank'
                    )
                } else if (item.name.trim() === '2020年报') {
                    let department1 = sessionStorage.getItem('userDepartmentOne')
                    let department2 = sessionStorage.getItem('userDepartmentTwo')

                    window.open(
                        `${item.url}?filterData={"params":[{"param":{"col":"department1","type":"=","values":["${department1}"]}},{"param":{"col":"department2","type":"=","values":["${department2}"]}}]}`,
                        '_blank'
                    )
                    // 云客服跳转
                } else if (item.url.includes('yg_routerHash')) {
                    // 获取菜单对应token
                    let token = ''
                    const menuKey = item.url.split('=')[1]
                    if (menuKey) {
                        const req = {
                            module: menuKey
                        }
                        await rbBpmHomeServices
                            .getHomeYunToken(req)
                            .then(res => {
                                token = res.token
                            }).catch(error => {
                                console.log(error)
                            })
                    }
                    const url = window.YUNGUAN_KEFU_URL + '?token=' + token
                    window.open(
                        `${url}`,
                        '_blank'
                    )
                } else {
                    window.open(item.url, '_blank')
                }
            },
            // 跳转到当前点击菜单
            gotoCurrentPath(path) {
                let menuItem, menuItemKey
                this.tabValues.forEach((val, key) => {
                    if (val.fullPath === path) {
                        menuItem = val
                        menuItemKey = key
                    }
                })

                // 判断是否已经在窗口标签中，如果是则直接点亮标签，否则点亮最后一个标签
                if (menuItem) {
                    this.tabIndex = menuItemKey
                } else {
                    this.tabIndex = this.tabValues.length
                }
                // 记录当前菜单路由
                sessionStorage.setItem('currentMenuPath', path)
                // 跳转路由，并点亮该路由的菜单
                this.$router.push(path)

                this.firstMenu.forEach(item => {
                    item.active = false
                })

                // 收起所有菜单栏
                const elemList = document.querySelectorAll(
                    'body > .el-menu--horizontal'
                )
                elemList.forEach(val => {
                    val.style.display = 'none'
                })
            },
            // 删除所有标签
            removeAllTab() {
                this.$confirm('关闭所有标签页吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                })
                    .then(() => {
                        this.tabValues = this.tabValues.slice(0, 1)
                        // 点亮导航菜单
                        const curMenu = this.tabValues.find(
                            val => val.fullPath === this.tabValues[0].fullPath
                        )
                        this.setTabActive(curMenu)
                        this.$router.push({ path: this.tabValues[0].fullPath })
                    })
                    .catch(() => { })
            },
            // 删除标签
            removeTab(item, index) {
                let tabs = this.tabValues
                let tabValue = this.tabValue
                if (item.fullPath === this.tabValue) {
                    tabs.forEach((val, idx) => {
                        if (val.fullPath !== this.tabValue) return
                        // 关闭当前激活tab，进入前一个tab
                        let nextTab = tabs[idx - 1]
                        this.tabValue = nextTab.fullPath
                        // 点亮下一个窗口标签
                        const curMenu = tabs.find(
                            val => val.fullPath === this.tabValue
                        )
                        this.setTabActive(curMenu)
                    })
                    this.$router.push({ path: this.tabValue })
                }

                this.tabValues = tabs.filter(tab => tab.fullPath !== item.fullPath)
                // 点亮下一个窗口标签
                if (item.fullPath === tabValue) {
                    this.tabIndex = index - 1
                } else {
                    // 点亮前一个窗口标签
                    if (this.tabIndex > index) {
                        this.tabIndex = this.tabIndex - 1
                    }
                }
                this.$store.commit(
                    'deleteIncludePagesName',
                    this.handlePageName(item.name)
                )
            },
            // 点击tab
            activeTab(val, index) {
                if (this.tabValue === val.fullPath) return
                this.tabIndex = index
                this.tabValue = val.fullPath

                // 点亮导航菜单
                const curMenu = this.tabValues.find(
                    val => val.fullPath === this.tabValue
                )
                this.setTabActive(curMenu)
                this.$router.push({ path: val.fullPath })
            },
            // 点亮tab
            setTabActive(curMenu) {
                this.firstMenu.forEach(secondMenu => {
                    secondMenu.active = false
                    secondMenu.children &&
                        secondMenu.children.forEach(thirdMenu => {
                            thirdMenu.children &&
                                thirdMenu.children.forEach(menuItem => {
                                    if (curMenu.title === menuItem.name) {
                                        secondMenu.active = true
                                    }
                                })
                        })
                })
            },
            // 默认跳转的来源
            async handleSelect(index) {
                let currentMenuPath = sessionStorage.getItem('currentMenuPath')
                let url = this.firstMenu[index].url
                if (url !== undefined && url !== '') {
                    url = url.replace(
                        'BPMX_SERVER_URL',
                        sessionStorage.getItem('BPMX_SERVER_URL')
                    )
                    if (this.firstMenu[index].name.indexOf('流程') > -1) {
                        window.open(
                            url +
                            '?mirrorToken=' +
                            sessionStorage.getItem('mirror'),
                            '_blank'
                        )
                    } else if (this.firstMenu[index].name === '运营分析') {
                        url = url.replace(
                            'OPERATE_SERVER_URL',
                            sessionStorage.getItem('OPERATE_SERVER_URL')
                        )
                        //
                        window.open(
                            url +
                            '/webroot/decision/view/report?viewlet=%5B8fd0%5D%5B8425%5D%5B62a5%5D%5B8868%5D.cpt&op=write',
                            '_blank'
                        )
                    } else {
                        var form = $('<form method=\'post\' target=\'_blank\'></form>')
                        form.attr({ action: url })
                        $('html').append(form)
                        form.submit()
                    }
                    return
                }
                this.currentFirstLevel = index
                this.activeIndex = index + ''
                const currentMenu = this.secondMenu[0]
                let router
                try {
                    if (currentMenu.children && currentMenu.children.length > 0) {
                        router = `${currentMenu.base}${currentMenu.children[0].base}${currentMenu.children[0].routers[0].path}`
                    } else {
                        router = `${currentMenu.base}${currentMenu.routers[0].path}`
                    }
                } catch (error) {
                    console.error('router error===', error)
                }

                await this.getPageViewByLdapId()

                const customViewPath =
                    '/custom_pages/main?pageCode=' + this.curPageCode
                const title = this.hasCustomView
                    ? this.customPageName
                    : currentMenu.name
                router = this.hasCustomView ? customViewPath : router

                // 若有访问记录，跳转到上一次访问的菜单
                if (currentMenuPath && !this.tabValues.length) {
                    // tab菜单第一项：有定制化首页，则把定制化首页设为第一项
                    this.tabValues.push({
                        fullPath: router,
                        title: title
                    })

                    this.$router.push({ path: currentMenuPath })
                    // 跳转到定制化首页
                } else if (this.hasCustomView) {
                    this.$router.push({
                        path: customViewPath + '&currentTitle=' + this.customPageName,
                        query: {
                            pageType: this.pageType
                        }
                    })

                    // 跳转到综合首页
                } else {
                    if (sessionStorage.getItem('username') === 'alauda' && this.projectName == '集中网管') {
                        this.$router.push({
                            path: '/resource/iframe/viewComponents?treeCode=bizSystemView&condicationCode=instance_list'
                        })

                    } else {
                        this.$router.push({
                            path: router
                        })
                    }

                }
            },
            // 通过人员类别判断：当前账号是否有定制化首页
            async getPageViewByLdapId() {
                await rbRoleDataServiceFactory
                    .getPageViewByLdapId({ ldapId: this.username })
                    .then(res => {
                        if (res.length) {
                            let indexCustomContent = res.find(item => {
                                return item.pageCode === this.curPageCode
                            })

                            this.hasCustomView = Boolean(indexCustomContent)
                            if (this.hasCustomView) {
                                this.customPageName =
                                    indexCustomContent.customizedviewName
                                this.pageType = indexCustomContent.pageType
                            }
                        }
                    })
            },
            // 登出日志记录
            async saveDeskLogs() {
                let params = {
                    appType: '系统登出',   // 广州4A：OA单点登录/系统登录、系统登出
                    opText: '系统登出',  // 广州4A：OA单点登录/系统登录、系统登出
                    opType: 'Logout'  // Login  Logout
                }
                await rbProjectDataService
                    .saveDeskLogs(params)
            },
            logout() {
                this.$confirm('确认退出吗?', '退出', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    // 登出日志记录
                    await this.saveDeskLogs()
                    setTimeout(() => {
                        sessionStorage.removeItem('token')
                        sessionStorage.setItem('mirrorTokenStorage', null)
                        sessionStorage.setItem('logValid', false)
                        sessionStorage.setItem('currentMenuPath', '')
                        cookieService.setCookie('project', '')
                        cookieService.setCookie('sso_t_name', '')
                        cookieService.setCookie('sso_t_value', '')
                        const redirectUri =
                            window.location.protocol +
                            '//' +
                            window.location.hostname +
                            (window.location.port ? ':' + window.location.port : '') +
                            (this.platForm ? '?platForm=' + this.platForm : '')
                        const logoutUrl = this.keycloak.createLogoutUrl({
                            redirectUri: redirectUri
                        })
                        if (sessionStorage.getItem('pageFrom') === 'oa') {
                            window.location.href = sessionStorage.getItem('X7_SERVER_URL') + '/front/#/login?pageFrom=oa'
                            localStorage.removeItem('pageFrom')
                            return
                        }
                        if (
                            window.projectName == '集中网管' &&
                            window.location.hostname == '10.153.1.36'
                        ) {
                            window.location.href = 'http://172.16.108.52:8080/osa_web'
                        } else {
                            window.location.href = logoutUrl
                        }
                    })
                })
            },

            // 获得登录用户详情
            getUserInfo() {
                sessionStorage.setItem('loginUserInfo', JSON.stringify({}))

                return this.rbHttp
                    .sendRequest({
                        method: 'GET',
                        url: '/v1/user/findByLdapId/{ldap_id}',
                        params: {
                            ldapId: this.username
                        }
                    })
                    .then(res => {
                        sessionStorage.setItem(
                            'loginUserInfo',
                            JSON.stringify(res)
                        )
                        return res
                    })
            },

            // 获得部门信息
            getDepartment() {
                sessionStorage.setItem('userDepartmentOne', null)
                sessionStorage.setItem('userDepartmentTwo', null)

                let params = {
                    code: this.username
                }
                // 二级部门
                rbRoleDataServiceFactory.getDepartmentTwo(params).then(data => {
                    if (!data || !data.length) {
                        return false
                    }
                    sessionStorage.setItem(
                        'userDepartmentTwo',
                        data[0]['dept_list'][0].name
                    )

                    // 一级部门
                    this.getDepartmentOne(
                        data[0]['dept_list'][0]['parent_id'],
                        data[0]['dept_list'][0]['uuid']
                    )
                })
            },
            // 获取用户一级部门
            getDepartmentOne(parentId, uuid) {
                if (!parentId) {
                    return false
                }
                rbRoleDataServiceFactory
                    .getDepartmentOne(parentId, uuid)
                    .then(data => {
                        sessionStorage.setItem('userDepartmentOne', data.name)
                    })
                    .catch(error => {
                        if (error.status === 403) {
                            this.$message.error(
                                '该账号没有菜单权限，请联系管理员!'
                            )
                        }
                    })
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import './home.component.scss';
    .right-head .user-wrap {
      padding: 0px 20px 0px 0px !important;
    }
</style>
