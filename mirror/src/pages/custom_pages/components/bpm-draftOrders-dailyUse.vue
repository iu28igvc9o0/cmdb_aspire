<template>
    <!-- 深圳服务台首页：工单起草+常用操作 -->
    <div class="flex-column-space-between">
        <!-- <bpm-order-types style="height:330px;"
                         :moduleDatas="orderTypesDatas"></bpm-order-types> -->
        <bpm-order-types style="height:330px;"></bpm-order-types>
        <bpm-daily-use style="height:330px;"></bpm-daily-use>
    </div>
</template>

<script>
    // import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    export default {
        components: {
            'bpm-order-types': () => import('./bpm-order-types.vue'),
            'bpm-daily-use': () => import('./bpm-daily-use.vue'),
        },
        data() {
            return {
                // 工单起草数据
                orderTypesDatas: {
                    // 更多
                    gotoMore: () => {
                        this.$router.push({
                            path: '/resource/flow',
                            query: {
                                routerHash: 'flow/create',
                                currentTitle: '工单起草'
                            }
                        })
                    },
                    // 数据
                    dataList: []
                }
            }
        },
        methods: {
            // 获得工单起草数据
            getOrderTypesDatas() {
                // rbBpmHomeServices
                //     .getUserDraftList()
                //     .then(res => {
                //         if (res && res.length > 0) {
                //             this.orderTypesDatas.dataList = res.map((item) => {
                //                 // click事件
                //                 item.click = () => {
                //                     this.$router.push({
                //                         path: '/resource/flow',
                //                         query: {
                //                             routerHash: item.fullUrl.split('#')[1].slice(1),
                //                             currentTitle: item.formName
                //                         }
                //                     })
                //                 }
                //                 return item
                //             })
                //         }
                //     })
            },
            // 获得常用操作数据
            getDailyUseDatas() {

            },
            // 初始化
            init() {
                this.getOrderTypesDatas()
                this.getDailyUseDatas()
            }
        },
        created() {
            // this.init()
        }

    }
</script>

<style lang="scss" scoped>
</style>

