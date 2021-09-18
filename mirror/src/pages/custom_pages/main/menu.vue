<template>
    <!-- 定制化：菜单 -->
    <div class="container-menu">
        <el-tabs class="yw-menu-tabs"
                 v-model="activeName"
                 type="border-card"
                 tab-position="left"
                 @tab-click="changMenu">
            <el-tab-pane :label="item.name"
                         :name="item.name"
                         v-for="(item,index) in tabDatas"
                         :key="index">
                <draggable class="thumb-list"
                           element="ul"
                           group="viewInfo"
                           v-model="activeMenusList"
                           :options="dragOptionsMenu">
                    <li class="thumb-item"
                        v-for="(menuItem,menuIndex) in activeMenusList"
                        :key="menuIndex + menuItem.name">
                        <img class="item-img"
                             :src="require(`src/assets/img/custom_modules/${menuItem.thumbnail || menuItem.name}.jpg`) || defaultImg"
                             alt="暂无图片" />
                        <p class="item-name text-ellipse">{{menuItem.label}}</p>
                    </li>
                </draggable>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>
 
<script>
    import draggable from 'vuedraggable'
    import modluesData from '../mixin/modules'

    export default {
        props: {
            drawerShow: {
                type: Boolean,
                default: false
            },
            pageType: {
                type: String,
                default: 'dark'
            },
            viewsData: {
                type: Array,
                default: []
            },
        },
        components: {
            draggable
        },
        data() {
            return {
                defaultImg: require('src/pages/settings/customization/components/img/home1.png'),
                activeMenusList: [],// 当前选中的tab
                activeName: '',
                tabDatas: [
                    {
                        name: '综合首页',
                        code: 'index'
                    },
                    {
                        name: '通用首页',
                        code: 'bpmIndex'
                    },
                ],
                activeTabIndex: 0,
                lightModePages: ['bpmIndex'], // 浅色页面

            }
        },
        computed: {
            dragOptionsMenu() {
                return {
                    animation: 0,
                    group: {
                        name: 'chartGroup',// 拖放位置
                        pull: 'clone',
                        put: false
                    },
                    ghostClass: 'ghost'
                }
            },
            // 从模块数据里获取到模块菜单
            menuData() {
                let menuData = {}
                let arr = []
                this.tabDatas = []
                modluesData.forEach(singleModule => {
                    if (this.pageType === singleModule.pageType) {
                        this.tabDatas.push(singleModule)
                        singleModule.data.forEach(item => {
                            arr = [...item.children, ...arr]
                        })
                        menuData[singleModule.code] = arr
                    }
                })
                return menuData
            },
        },
        watch: {
            drawerShow: {
                handler(val) {
                    if (val) {
                        this.getMenu()
                    }
                },
                immediate: true
            }
        },
        mounted() {
            this.$bus.on('updateMenuList', this.updateMenuList)
        },
        methods: {
            updateMenuList(item) {
                if (!Array.isArray(item)) {
                    this.activeMenusList.push(item)
                } else {
                    this.activeMenusList = [].concat(this.activeMenusList, item)
                }
            },
            // 获得菜单
            getMenu() {
                this.activeMenusList = this.$utils.deepClone(this.menuData[this.tabDatas[this.activeTabIndex].code])
                this.activeName = this.tabDatas[this.activeTabIndex].name
                this.handleMenuList()

            },
            // 从菜单里移除已在视图中的模块
            handleMenuList() {
                if (!this.viewsData.length) {
                    this.activeMenusList = []
                    return
                }
                let curViewStr = JSON.stringify(this.viewsData)
                this.activeMenusList.forEach((item, index) => {
                    if (curViewStr.includes(item.name)) {
                        this.activeMenusList.splice(index, 1, null)
                    }
                })
                // 去除空值
                this.activeMenusList = this.activeMenusList.filter(item => item)
            },

            // 切换菜单
            changMenu(tab) {
                this.activeTabIndex = tab.index
                this.getMenu()
            },

        }
    }
</script>
 
<style  lang="scss" scoped>
    .container-menu {
        height: 100%;
        /deep/ .el-tabs__content {
            height: calc(100vh - 76px);
            overflow-y: scroll;
        }
        .thumb-list {
            min-height: calc(100vh - 110px);
            .thumb-item {
                margin-bottom: 15px;
                text-align: center;
                cursor: move;

                .item-img {
                    display: inline-block;
                    width: 100%;
                    height: 76px;
                    border: 1px solid $color-border;
                }
                .item-name {
                    font-size: 12px;
                }

                &:hover,
                &.active {
                    .item-img {
                        border: 2px solid $color-link-dark;
                    }
                }
            }
        }
    }
</style>
