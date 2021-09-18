<template>
    <div class="navbar-card">
        <div v-for="(item, index) of menu.children"
             :key="index"
             class="submenu"
             v-show="item.show">
            <p class="submenu__title">{{ item.name }}</p>
            <ul class="submenu__list">
                <li v-for="(i, idx) of item.children"
                    v-show="i.show && item.show"
                    :key="`${index}${idx}`"
                    class="submenu__item"
                    @click="toRoute(i, item)">{{ i.name}}</li>
            </ul>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'NavbarCard',
        props: {
            menu: {
                type: Object,
                default() {
                    return {}
                }
            },
            currentMenuIndex: {
                type: Number,
                default() {
                    return 0
                }
            },
        },
        data() {
            return {

            }
        },
        methods: {
            toRoute(item, parent) {
                this.$emit('toRoute', { item, parent, menu: this.menu, currentMenuIndex: this.currentMenuIndex })
            }
        }
    }
</script>
<style lang='scss' scoped>
    body > .el-menu--horizontal {
        display: block !important;
        max-width: 100% !important;
        overflow: hidden;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }
    .navbar-card {
        display: flex;
        flex-wrap: wrap;
        padding: 0 10px 10px;
        margin-left: 5px;
        background: #06082b;
        border: 1px solid rgba(0, 90, 226, 1);
        box-shadow: 0px 3px 7px 0px rgba(0, 0, 0, 0.35);
        max-height: 100vh;
        max-width: 100vw;
        overflow-y: auto;
        // 菜单超出高度显示滚动条
        ::-webkit-scrollbar {
            width: 3px;
            background: #042854 !important;
        }
        ::-webkit-scrollbar-track-piece {
            background: #042854 !important;
        }
        ::-webkit-scrollbar-thumb {
            background: #06647f !important;
        }
        .submenu__list {
            max-height: calc(100vh - 180px);
            overflow-y: auto;
        }
        .submenu {
            padding: 0 10px;
            width: 140px;
            box-sizing: border-box;
            &__title {
                color: #40acff;
                font-size: 14px;
                height: 55px;
                line-height: 55px;
                border-bottom: 1px solid #005ae2;
            }
            &__list {
                padding: 10px 0;
            }
            &__item {
                padding: 10px 5px;
                font-size: 12px;
                color: #96d1ff;
                &:hover {
                    color: #96d1ff;
                    background: #0a51bc;
                    cursor: pointer;
                }
            }
            &__item.active {
                color: #1f89da;
                background: #f2f2f2;
            }
        }
        // .submenu + .submenu {
        //   margin-left: 28px;
        // }
    }
</style>
<style lang='scss'>
    // .el-menu--popup-bottom-start {
    //   background:rgba(6,8,43,1);
    //   border:1px solid rgba(0,90,226,1);
    //   box-shadow:0px 3px 7px 0px rgba(0, 0, 0, 0.35)
    // }
    .el-menu--popup-bottom-start {
        background: none;
        border: none;
    }
</style>
