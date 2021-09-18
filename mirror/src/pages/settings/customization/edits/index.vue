
<template>
    <!-- 定制化首页 -->
    <div class="components-container container-view">
        <div class="container-edit">
            <!-- 头部 -->
            <section class="header-wrap clearfix"
                     height="45px">
                <div class="header">
                    <svg class="svg-icon"
                         aria-hidden="true">
                        <use xlink:href="#icondashboard"></use>
                    </svg>
                    <span class="title">{{title}}</span>
                </div>
                <div class="btn-wrap">
                    <el-tooltip class="item"
                                effect="dark"
                                content="返回"
                                placement="bottom"
                                :hide-after=1000>
                        <el-button type="text"
                                   @click="back()">
                            <svg class="svg-icon svg-icon-24"
                                 aria-hidden="true">
                                <use xlink:href="#icondashboardBack"></use>
                            </svg>
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item"
                                effect="dark"
                                content="保存"
                                placement="bottom"
                                :hide-after=1000>
                        <el-button type="text"
                                   @click="save()">
                            <svg class="svg-icon svg-icon-24"
                                 aria-hidden="true">
                                <use xlink:href="#iconright"></use>
                            </svg>
                        </el-button>
                    </el-tooltip>

                </div>
            </section>
            <!-- 头部 -->

            <!-- 侧边栏 -->
            <section class="aside-wrap">
                <Menu></Menu>
            </section>
            <!-- 侧边栏 -->

            <!-- 内容 -->
            <section class="main-wrap">
                <Main></Main>
            </section>
            <!-- 内容 -->

        </div>
    </div>

</template>
 
<script>
    export default {
        components: {
            Menu: () => import('./menu.vue'),
            Main: () => import('./main.vue'),
        },
        data() {
            return {
                title: '定制首页'

            }
        },

        methods: {
            // 保存数据
            saveDatas() {

                let componentsDir = this.$store.state.customization.componentsDir
                let componentsData = this.$store.state.customization.componentsData
                // let componentsDir = 'home' //模块目录（比如综合首页）
                // let componentsData = [{ componentName: 'TodayNewOrder' },  //模块名称
                // { componentName: 'deviceUseRate' }, { componentName: 'orderStatistics' }];
                let params = {
                    'moduleCode': componentsDir,
                    'data': JSON.stringify({ componentsData: componentsData }),

                }
                this.$api.addModuleInfo(params).then((res) => {
                    if (res) {
                        this.$message.success('保存成功！')
                    }

                }).catch(() => {
                    this.$message.error('服务中断,请联系管理员！')
                })
            },

            // 保存
            save() {
                this.$confirm('保存吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.saveDatas()
                }).catch(() => {
                })
            },

            // 返回
            back() {
                this.$confirm('返回吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$router.go(-1)
                }).catch(() => {
                })

            }
        }
    }
</script>
 
<style  lang="scss" scoped>
    .components-container {
        width: 100%;
        height: 100%;
        overflow: auto;
        padding: 0;
    }
    .container-edit {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        width: calc(100% + 300px);
        padding: 12px;
        .header-wrap {
            position: relative;
            width: 100%;
            height: 45px;
            line-height: 45px;
            overflow: hidden;
            .header {
                display: inline-block;
                font-size: 16px;
                color: $color-link-dark;
                .title {
                    display: inline-block;
                    font-weight: bold;
                }
            }
            .btn-wrap {
                display: inline-block;
                margin-left: 210px;
            }
        }
        .aside-wrap {
            width: 266px;
        }
        .main-wrap {
            width: calc(100% - 300px);
        }
    }
</style>
