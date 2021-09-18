// import Vue from 'vue'
// import Router from 'vue-router'
import rbHttp from 'src/assets/js/utility/rb-http.factory'
import store from 'src/store/index'

export function handleVueRouter(menus, VueRouter) {
    const homeRouter = []
    // 一级菜单
    menus.categories && menus.categories.forEach((categorie) => {

        // 二级菜单
        if (categorie.children && categorie.children.length > 0) {
            categorie.children.forEach((item) => {
                const firstBase = item.base
                let mainPage = ''

                // item.routers 二级菜单路由（点击进入二级菜单页面，一般不配置）
                if (item.routers && item.routers.length > 0) {
                    console.log('item.routers==', item.routers)
                    //     mainPage = `${firstBase}${secondBase}${item.routers[i].path}`

                    // 并列二级菜单路由
                    item.routers.forEach((item) => {
                        homeRouter.push({
                            meta: {
                                title: item.name,
                                mainPage: mainPage,
                                name: item.name
                            },
                            path: `${firstBase}${item.path}`,
                            component: () => import( /* webpackChunkName: "pagechunk", webpackPrefetch: true */ `src/pages${firstBase}${item.path}/${item.component}`)
                            // component: resolve => require([`src/pages${firstBase}${item.path}/${item.component}`], resolve)
                        })
                    })
                }

                // 三级菜单
                if (item.children && item.children.length > 0) {
                    item.children.forEach((item) => {
                        const secondBase = item.base
                        let mainPage = ''

                        // item.routers 三级菜单路由（点击进入三级菜单页面，常规配置）
                        if (item.routers && item.routers.length > 0) {

                            // 带参数的路由菜单路径，并列三级菜单路由
                            item.routers.forEach((item) => {
                                mainPage = `${firstBase}${secondBase}${item.path}`
                                // 处理带参数的菜单路径；避免404
                                let componentPath
                                try {
                                    if (item.path && item.path.includes('?')) {
                                        componentPath = item.path.split('?')[0]
                                    } else if (item.path) {
                                        componentPath = item.path
                                    } else {
                                        console.error('菜单path参数缺失===', item)
                                    }
                                } catch (error) {
                                    console.error(error)
                                }
                                let duplicateRouter = homeRouter.find(homeRout => {
                                    return homeRout.name === mainPage
                                })
                                // 过滤重复的router
                                if (!duplicateRouter) {
                                    homeRouter.push({
                                        meta: {
                                            title: item.name,
                                            mainPage: mainPage,
                                        },
                                        name: mainPage,
                                        path: `${firstBase}${secondBase}${componentPath}`,
                                        component: () => import( /* webpackChunkName: "pagechunk", webpackPrefetch: true */ `src/pages${firstBase}${secondBase}${componentPath}/${item.component}`)
                                        // component: resolve => require([`src/pages${firstBase}${secondBase}${componentPath}/${item.component}`], resolve)
                                    })
                                }

                                // 第五层内部扩充；一般不配置
                                if (item.children && item.children.length > 0) {
                                    console.log('item.children==', item.children)
                                    item.children.forEach((item) => {
                                        const ThirdBase = item.base
                                        let mainPage = ''
                                        if (item.routers && item.routers.length > 0) {
                                            mainPage = `${firstBase}${secondBase}${ThirdBase}`
                                            item.routers.forEach((item) => {
                                                // 最里层的模板管理
                                                homeRouter.push({
                                                    meta: {
                                                        title: item.name,
                                                        mainPage: mainPage,
                                                    },
                                                    path: `${firstBase}${secondBase}${ThirdBase}${item.path}`,
                                                    component: () => import( /* webpackChunkName: "thirdchunk", webpackPrefetch: true */ `src/pages${firstBase}${secondBase}${ThirdBase}${item.path}/${item.component}`)
                                                    // component: resolve => require([`src/pages${firstBase}${secondBase}${ThirdBase}${item.path}/${item.component}`], resolve)
                                                })
                                            })
                                        }
                                    })
                                }
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

    let vueRouter = new VueRouter({
        mode: 'history',
        base: '/',
        routes: [
            {
                path: '/',
                redirect: '/home'
            },
            {
                path: '/login',
                component: () => import('src/pages/login/message.vue')
            },
            {
                path: '/home',
                component: () => import('src/pages/potral/home.vue'),
                children: homeRouter
            },
            {
                path: '*',
                redirect: '/404',
                hidden: true
            },
            {
                path: '/404',
                component: () => import('src/pages/error/404.vue'),
                name: '404'
            }
        ]
    })
    vueRouter.beforeEach((to, from, next) => {
        next()
    })

    return vueRouter
}
