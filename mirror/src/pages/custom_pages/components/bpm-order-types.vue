<template>
    <!-- 服务台： 工单起草 -->
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
            <div class="displaybox flex-wrap order-type-list border-box">
                <div class="text-block t-center pointer bg-order-add"
                     v-for="(item,index) in dataList"
                     :key="item.formDefId"
                     :style="setBgImg(index)"
                     @click="addNewOrder(item)">
                    {{item.formName}}
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'
    export default {
        props: ['moduleDatas'],
        data() {
            return {
                dataList: []
            }
        },
        created() {
            this.getData()
        },
        methods: {
            // 112432
            setBgImg(index) {
                let imgIndex = ''
                if(index < 2) {
                    imgIndex = '01'
                }else if(index === 2 || index === 5) {
                    imgIndex = '02'
                }else if(index === 3) {
                    imgIndex = '04'
                }else if(index === 4) {
                    imgIndex = '03'
                }
                let platForm = sessionStorage.getItem('platForm')
                let cityName = sessionStorage.getItem('cityName')
                if (platForm === 'it_devops_receptionp_platform' && cityName === 'master') {
                    let style = {
                        background: 'url(' + '/static/img/custom_modules/icon-btn-bg' + imgIndex + '.png) no-repeat',
                        backgroundSize: 'cover',
                        color: '#ffffff'
                    }
                    return style
                }
            },
            getData() {
                if (this.moduleDatas && this.moduleDatas.dataList) {
                    this.dataList = this.moduleDatas.dataList
                    return
                }
                rbBpmHomeServices
                    .getUserDraftList()
                    .then(res => {
                        if (res.length) {
                            this.dataList = res
                        }
                    })
            },
            // 更多创建工单
            gotoMore() {
                if (this.moduleDatas && this.moduleDatas.gotoMore) {
                    this.moduleDatas.gotoMore()
                    return
                }
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: 'flow/create',
                        currentTitle: '工单起草'
                    }
                })
            },
            addNewOrder(item) {
                if (this.moduleDatas && item.click) {
                    item.click()
                    return
                }

                // 测试-流程工单跳转
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: item.fullUrl.split('#')[1].slice(1),
                        currentTitle: item.formName
                    }
                })
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>

