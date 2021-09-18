

<template>
    <!-- 统计 -->
    <div class="content-chart"
         style="width: 24.2%;">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icontongji"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
        </section>

        <!-- 多图表 -->
        <section class="chart-section statisticsNum">
            <YwAlert :datas="chartData.chartList"></YwAlert>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import iframe from 'src/services/iframe/iframe.js'
    export default {
        components: {
            YwAlert: () => import('src/components/common/yw-alert.vue')
        },
        data() {
            return {
                chartData: {
                    name: '统计',
                    chartList: [{
                        name: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? '资源池' : '资源区域',
                        icon: require('src/assets/theme/dark/img/resource.png'),
                        num: ''
                    },
                    {
                        name: (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') ? '业务线' : 'POD池',
                        icon: require('src/assets/theme/dark/img/POD.png'),
                        num: ''
                    }]
                }
            }
        },
        methods: {
            // 获得数据
            getData() {
                if (window.projectName == '集中网管' || window.projectName == '客响-集中运维平台') {
                    let queryData = {
                        queryName: 'ICountInstByCdtAPI_internet_countIdcAndBusiness'
                    }
                    iframe.countObject(queryData).then((res) => {
                        this.chartData.chartList[0].num = res.idcCount
                        this.chartData.chartList[1].num = res.businessCount
                    })
                } else {
                    iframe.getCountIdcAndPod().then((res) => {
                        this.chartData.chartList[0].num = res.idcCount
                        this.chartData.chartList[1].num = res.podCount
                    })
                }
            }

        },
        mounted() {
            this.getData()
        }

    }
</script>

<style lang="scss">
    .statisticsNum .yw-alert-wrap .yw-alert .fast-num {
        color: #1ebdff !important;
    }
</style>
