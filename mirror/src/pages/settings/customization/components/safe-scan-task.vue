<template>
    <!-- 标题 -->
    <div class="content-chart"
         style="width: 24.2%;height:240px;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongonggao"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <a class="table-link"
                   @click="gotoMore()">更多</a>
            </div>
        </section>
        <!-- 标题 -->

        <!-- 多图表 -->
        <section class="chart-section">
            <YwNotice :datas="chartData.chartList"
                      :noticeOption="noticeOption"></YwNotice>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import { formatDate2 } from 'src/assets/js/utility/rb-filters.js'


    export default {
        components: {
            YwNotice: () => import('src/components/common/yw-notice.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '漏洞扫描工单',
                    chartList: [{
                        status: 'pink',
                        statusName: '',
                        name: '--',
                        person: '--',
                        date: formatDate2(new Date())
                    },
                    ],
                },

                // 通告组件
                noticeOption: {
                    orderStyle: {
                        width: '35%',
                    },
                    dateStyle: {
                        width: '45%',
                    },
                    personStyle: {
                        width: '20%',
                        display: 'inline-block'
                    },
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {


                let params = {
                    'defKey': 'aqsmrwlc',
                    'status': 0
                }
                this.$api.querySafeTask(params).then((res) => {
                    this.chartData.chartList = res.safeOrderVoDetails.map((item) => {
                        return {
                            status: 'pink',
                            statusName: '',
                            name: item.title ? item.title : '--',
                            date: item.scanningTime ? item.scanningTime : '--',
                            person: item.executor ? item.executor : '--'
                        }
                    })
                })
            },

            // 更多
            gotoMore() {
                let url = `${sessionStorage.getItem('X7_SERVER_URL')}/front/#/flow/todoList?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            }

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        padding-left: 2px;
        padding-right: 2px;
    }
</style>

