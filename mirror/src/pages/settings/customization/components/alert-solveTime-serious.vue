<template>
    <!-- 重大告警处理时长 -->
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
                            v-if="barDatas[0].show"></YwChartBar>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>
    import { fixedNumberPointTwo } from 'src/assets/js/utility/rb-filters.js'
    export default {
        components: {
            YwChartBar: () => import('src/components/common/yw-bar-vertical.vue'),
        },
        data() {
            return {
                chartData: {
                    // name: 'POD池Top10',
                    name: '重大告警处理时长-资源池分布（最近7天）',
                    filter: [{ name: '全部', label: '' }, { name: '服务器', label: '服务器' }, { name: '防火墙', label: '防火墙' }, { name: '路由器', label: '路由器' }, { name: '交换机', label: '交换机' }],
                    activeFilter: '',
                },
                barDatas: [
                    {
                        show: false,
                        id: 'bar-1', chartOption: 'bar-option', chartDatas: [],
                        details: {
                            title: '平均处理时长(小时)',
                            // valueType: { name: '均峰值利用率', label: 'max' },
                            seriesName: [
                                { yLabel: 'count', xLabel: 'colName', yLabelName: '时长', xLabelName: '' },
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
                    'alertLevel': 5,
                }
                this.$api.queryAlertSolveTime(params).then((res) => {
                    this.barDatas[0].chartDatas = res.map((item) => {
                        return {
                            colName: item.idc_type,
                            count: fixedNumberPointTwo(item.hours, 2)
                        }
                    })
                    this.barDatas[0].show = false
                    this.$nextTick(() => {
                        this.barDatas[0].show = true
                    })

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
</style>

