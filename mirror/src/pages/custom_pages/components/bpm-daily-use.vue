<template>
    <!-- 服务台： 常用操作 -->
    <div class="content-chart">
        <section class="chart-title-wrap clearfix displaybox">
            <div class="chart-title boxflex01">
                <div class="displaybox">
                    <div class="chart-title">常用操作</div>
                </div>
            </div>
        </section>
        <section class="chart-section">
            <div class="displaybox flex-wrap order-type-list daily-use-list">
                <div class="text-block t-center pointer daily-use"
                     @click="addNewOrder(item)"
                     v-for="item in dataList"
                     :key="item.id">
                    <img :src="'/static/img/custom_modules/' + item.logo">
                    <div class="mtop5">{{item.name}}</div>
                </div>
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
            getData() {
                rbBpmHomeServices
                    .getUserOperationsList()
                    .then(res => {
                        if (res.length) {
                            this.dataList = res
                        }
                    })
            },
            // 常用操作跳转
            addNewOrder(item) {

                if (!item.linkUrl) {
                    return
                }
                if (item.isExternal === 'Y') {
                    window.open(item.linkUrl, 'blank')
                    return
                }

                let router
                if (item.linkUrl.includes('#')) {
                    router = {
                        path: '/resource/flow',
                        query: {
                            routerHash: item.linkUrl.split('#')[1].slice(1),
                            currentTitle: item.name
                        }
                    }
                } else {
                    router = item.linkUrl
                }
                this.$router.push(router)
            }
        }

    }
</script>

<style lang="scss" scoped>
    .chart-section .daily-use {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 90% !important;
    }
</style>

