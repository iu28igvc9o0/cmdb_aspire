<template>
    <!-- 服务台人员首页： 工单起草 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div class="chart-title">工单起草</div>
                </div>
            </div>
            <div class="chart-filter">
                <el-button type="text"
                           @click="gotoMore">更多</el-button>
            </div>
        </section>
        <section class="chart-section">
            <div class="displaybox flex-wrap order-type-list-other daily-use-list">
                <template v-for="(item,index) in dataList">
                    <div class="text-block t-center pointer daily-use"
                         @click="addNewOrder(item)"
                         v-if="index < 2"
                         :key="item.id">
                        <img :src="'/static/img/custom_modules/' + item.iconUrl">
                        <div class="mtop5">{{item.formName}}</div>
                    </div>
                </template>
            </div>
        </section>
    </div>
</template>

<script>
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    export default {
        data() {
            return {
                dataList: [],
            }
        },
        created() {
            this.getData()
        },
        methods: {
            // 更多创建工单
            gotoMore() {
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: 'flow/create',
                        currentTitle: '工单起草'
                    }
                })
            },
            getData() {
                rbBpmHomeServices
                    .getUserDraftList()
                    .then(res => {
                        if (res.length) {
                            this.dataList = res
                        }
                    })
            },
            // 常用操作跳转
            addNewOrder(item) {
                let router
                if (!item.fullUrl) {
                    return
                }
                if (item.fullUrl.includes('#')) {
                    router = {
                        path: '/resource/flow',
                        query: {
                            routerHash: item.fullUrl.split('#')[1].slice(1),
                            currentTitle: item.formName
                        }
                    }
                } else {
                    router = item.fullUrl
                }
                this.$router.push(router)
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>

