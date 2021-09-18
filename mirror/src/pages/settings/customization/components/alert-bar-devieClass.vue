<template>
    <!-- 告警-柱状图-设备分类 -->
    <div class="content-chart"
         :style="{width: isCustomPage ? '24.2%' : '24.2%', height: isCustomPage ? '310px' : '350px'}">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojing"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <!-- <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
            </div> -->
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="chart-box-wrap">
                <YwChartBar :barDatas="barDatas[0]"
                            class="chart-box-item"
                            v-if="barDatas[0].show"
                            @clickChart="clickChart"></YwChartBar>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    export default {
        components: {
            YwChartBar: () => import('src/components/common/yw-bar-horizontal.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '设备分类',
                    filter: [{ name: '全部', label: '' }, { name: '服务器', label: '服务器' }, { name: '防火墙', label: '防火墙' }, { name: '路由器', label: '路由器' }, { name: '交换机', label: '交换机' }],
                    activeFilter: '',
                },
                barDatas: [
                    {
                        show: false,
                        id: 'bar-1', chartOption: 'bar-horizontal-option', chartDatas: [],
                        details: {
                            title: '各资源池内部租户数量',
                            valueType: { name: '均峰值利用率', label: 'max' },
                            seriesName: [
                                { yLabel: 'name', xLabel: 'waitSolve', yLabelName: '待处理', xLabelName: '数量' },
                                { yLabel: 'name', xLabel: 'inSolve', yLabelName: '处理中', xLabelName: '数量' }
                            ]
                        }
                    },
                ],

            }
        },
        methods: {
            // 获得数据
            getDatas() {
                let params = {
                    // 'operateStatus': '',
                    'colType': 'devicClasss',
                }
                this.$api.queryAlertByOperateStatus(params).then((res) => {
                    this.barDatas[0].chartDatas = res.map((item) => {
                        return {
                            name: item.col,
                            waitSolve: item.todoCount,
                            inSolve: item.count
                        }
                    })
                    this.barDatas[0].show = false
                    this.$nextTick(() => {
                        this.barDatas[0].show = true
                    })

                })
            },

            // 跳转到告警页
            clickChart(params = '') {
                let solveStatus = 'first'
                switch (params.seriesName) {
                    case '处理中':
                        solveStatus = 'second'
                        break
                    default:
                        solveStatus = 'first'
                        break
                }
                this.$router.push({
                    path: '/mirror_alert/alert_new/manage',
                    query: {
                        solveStatus: solveStatus,// 处理状态
                        filter: JSON.stringify([{ code: 'device_class', name: params.name, type: 'in_and' }])
                    }
                })
            },
            changeTab() {
                this.getDatas()
            },
        },
        mounted() {
            this.$utils.creatInterval(this, this.getDatas, 300000)
        },
        activated() {
            this.$utils.creatInterval(this, this.getDatas, 300000)
        }

    }
</script>

<style lang="scss" scoped>
    .content-chart {
        position: relative;
        .chart-section {
            height: 97%;
            margin-top: -48px;
        }
    }
</style>

