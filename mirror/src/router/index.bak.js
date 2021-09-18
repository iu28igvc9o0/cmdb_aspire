import rbHttp from 'src/assets/js/utility/rb-http.factory'
import store from 'src/store/index'

function getMenu() {
    return rbHttp.sendRequest({
        method: 'GET',
        url: `/v1/sys_menu/menu/it_devops_manage_platform`
    }).then((res) => {
        // return res;
        return getRouters(res);
    })
}

function getRouters(navConfig) {
    const homeRouter = []
    navConfig.categories.forEach((categorie) => {

        let crumbone = ''
        let crumbtwo = ''
        let crumbthree = ''
        let crumbfoure = ''
        let crumbfive = ''
        let clickCrumbIndex
        if (categorie.name !== 'other') {
            // 自动化管理
            crumbone = categorie.name
        }
        if (categorie.children && categorie.children.length > 0) {
            // 第二层孩子数组里面
            categorie.children.forEach((item) => {
                // mirror
                const firstBase = item.base
                // 巡检管理
                crumbtwo = item.name
                let mainPage = ''
                if (item.routers && item.routers.length > 0) {
                    // for (var i = 0; i < item.routers.length; i++) {
                    //   if (item.routers[i].isMainPage) {
                    mainPage = `${firstBase}${item.routers[0].path}`
                    //     break
                    //   }
                    // }
                    // 第三层
                    item.routers.forEach((item) => {
                        crumbthree = item.name
                        let breadcrumb = [crumbone, crumbtwo, crumbthree]
                        breadcrumb = breadcrumb.filter(data => {
                            return data
                        })
                        if (crumbone.length > 0) {
                            clickCrumbIndex = 1
                        } else {
                            clickCrumbIndex = -1
                        }
                        homeRouter.push({
                            meta: {
                                title: item.name,
                                mainPage: mainPage,
                                clickCrumbIndex: clickCrumbIndex,
                                breadcrumb: breadcrumb,
                                name: item.name
                            },
                            path: `${firstBase}${item.path}`,
                            component: () => import( /* webpackChunkName: "commonchunk" */ `src/pages${firstBase}${item.path}/${item.component}`)
                            //   component: resolve => require([`src/pages${firstBase}${item.path}/${item.component}`], resolve)
                        })
                    })
                }

                if (item.children && item.children.length > 0) {
                    item.children.forEach((item) => {
                        // 巡检模板管理
                        crumbthree = item.name
                        // template
                        const secondBase = item.base
                        let mainPage = ''
                        // for (var i = 0; i < item.routers.length; i++) {
                        //   if (item.routers[i].isMainPage) {
                        //     mainPage = `${firstBase}${secondBase}${item.routers[i].path}`
                        //     break
                        //   }
                        // }
                        // first mirror  second template
                        if (item.routers && item.routers.length > 0) {
                            mainPage = `${firstBase}${secondBase}${item.routers[0].path}`
                            item.routers.forEach((item) => {
                                // 最里面的模板管理
                                crumbfoure = item.name
                                let breadcrumb = [crumbone, crumbtwo, crumbthree, crumbfoure]
                                breadcrumb = breadcrumb.filter(data => {
                                    return data
                                })
                                if (crumbone.length > 0) {
                                    clickCrumbIndex = 2
                                } else {
                                    clickCrumbIndex = 1
                                }
                                homeRouter.push({
                                    meta: {
                                        title: item.name,
                                        mainPage: mainPage,
                                        clickCrumbIndex: clickCrumbIndex,
                                        breadcrumb: breadcrumb,
                                        keepAlive: item.keepAlive
                                    },
                                    path: `${firstBase}${secondBase}${item.path}`,
                                    component: () => import( /* webpackChunkName: "secondchunk" */ `src/pages${firstBase}${secondBase}${item.path}/${item.component}`)
                                    //   component: resolve => require([`src/pages${firstBase}${secondBase}${item.path}/${item.component}`], resolve)
                                })
                                // 第五层内部扩充
                                if (item.children && item.children.length > 0) {
                                    item.children.forEach((item) => {
                                        // 模板设置
                                        crumbfoure = item.name
                                        // add
                                        const ThirdBase = item.base
                                        let mainPage = ''
                                        if (item.routers && item.routers.length > 0) {
                                            mainPage = `${firstBase}${secondBase}${ThirdBase}`
                                            item.routers.forEach((item) => {
                                                // 最里层的模板管理
                                                crumbfive = item.name
                                                let breadcrumb = [crumbone, crumbtwo, crumbthree, crumbfoure, crumbfive]
                                                breadcrumb = breadcrumb.filter(data => {
                                                    return data
                                                })
                                                if (crumbone.length > 0) {
                                                    clickCrumbIndex = 3
                                                } else {
                                                    clickCrumbIndex = 1
                                                }
                                                homeRouter.push({
                                                    meta: {
                                                        title: item.name,
                                                        mainPage: mainPage,
                                                        clickCrumbIndex: clickCrumbIndex,
                                                        breadcrumb: breadcrumb,
                                                        keepAlive: item.keepAlive
                                                    },
                                                    path: `${firstBase}${secondBase}${ThirdBase}${item.path}`,
                                                    component: () => import( /* webpackChunkName: "thirdchunk" */ `src/pages${firstBase}${secondBase}${ThirdBase}${item.path}/${item.component}`)
                                                    //   component: resolve => require([`src/pages${firstBase}${secondBase}${ThirdBase}${item.path}/${item.component}`], resolve)
                                                })
                                            })
                                        }
                                    })
                                }
                                // 新添加
                            })
                        }
                    })
                }
            })
        }
    })

    homeRouter.push({
        meta: {
            title: '刷新'
        },
        path: '/refresh',
        component: resolve => require(['src/pages/refresh/refresh.vue'], resolve)
    })

    let router = new VueRouter({
        mode: 'history',
        base: '/',
        routes: [{
                path: '/',
                redirect: '/home'
            },
            {
                path: '/login',
                component: resolve => require(['../pages/login/message.vue'], resolve)
            },
            {
                path: '/home',
                component: resolve => require(['../pages/potral/home.vue'], resolve),
                children: homeRouter
            }
        ]
    })
    router.beforeEach((to, from, next) => {
        const breadcrumb = to.meta.breadcrumb
        if (breadcrumb) {
            const breadcrumb2 = []
            for (var i = 0; i < breadcrumb.length; i++) {
                if (i === to.meta.clickCrumbIndex) {
                    breadcrumb2.push({
                        name: breadcrumb[i],
                        to: to.meta.mainPage
                    })
                } else {
                    breadcrumb2.push({
                        name: breadcrumb[i]
                    })
                }
            }
            store.commit('modifyBreadcrumb', breadcrumb2)
        }
        // const a = store.state.homeStore.breadcrumb
        next()
    })

    return router
}

export let activeRouter = getMenu()
