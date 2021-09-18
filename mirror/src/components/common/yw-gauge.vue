<template>
    <!-- 仪表盘 -->
    <section class="yw-chart-gauge">
        <div class="content">
            <p class="chart-percent">{{gaugeDatas.chartDatas.rate}}<span class="chart-percent-cell">%</span></p>
            <!-- <p class="chart-change-wrap">
                同比上月份<span class="chart-change-percent">
                    <span class="down"
                          v-if="gaugeDatas.chartDatas.change<0"><i class="arrow">↓</i>{{-gaugeDatas.chartDatas.change}}%</span>
                    <span class="up"
                          v-else><i class="arrow">↑</i>{{gaugeDatas.chartDatas.change}}%</span>
                </span>
            </p>
            <p class="chart-name">{{gaugeDatas.chartDatas.name}}</p> -->
        </div>
        <!-- 图表 -->
        <div :ref="gaugeDatas.id"
             class="chart-box"></div>
        <!-- 图表 -->
    </section>
</template>

<script>
    import ChartOption from 'src/utils/chartOption.js'
    import DrawChart from 'src/utils/drawCharts.js'
    export default {
        mixins: [DrawChart],
        props: ['gaugeDatas'],
        data() {
            return {

            }
        },

        methods: {
            // 绘制图表
            drawCharts(subItem) {
                this.$nextTick(() => {
                    let myChart = echarts.init(this.$refs[subItem.id], null, { renderer: 'svg' })
                    myChart.clear()

                    // 数据格式处理
                    this.drawGauge(myChart, subItem)

                })

            },
            drawGauge(myChart, subItem) {
                // 数据格式处理
                let option = JSON.parse(JSON.stringify(ChartOption[subItem.chartOption]))
                let chartDatas = JSON.parse(JSON.stringify(subItem.chartDatas))
                // let colors = ChartOption['color-option']['linearColor']
                option.series[0] = JSON.parse(JSON.stringify(option.series[0]))
                option.series[0].name = chartDatas.name

                // option.series[0].axisLine.lineStyle.color[0] = [chartDatas.rate / 100, new echarts.graphic.LinearGradient(0, 0, 1, 0, colors.blue)]// 右/下/左/上
                option.series[0].axisLine.lineStyle.color[0][0] = this.gaugeDatas.chartDatas.rate / 100
                if (chartDatas && Object.keys(chartDatas).length > 0) {
                    option.series[0].data[0].name = ''
                    option.series[0].data[0].value = this.gaugeDatas.chartDatas.rate

                } else {
                    option.series[0].data[0].name = '占比'
                    option.series[0].data[0].value = '0'
                }

                myChart.setOption(option)

                // resize自适应
                this.setResizeFun(myChart)
            },

        },
        mounted() {
            this.drawCharts(this.gaugeDatas)
        }

    }
</script>
<style lang="scss" scoped>
    .yw-chart-gauge {
        position: relative;
        width: 100%;
        height: 100%;
        margin: auto;

        .chart-box {
            width: 100%;
            height: 100%;
            margin: 0 auto;
        }
        .content {
            position: absolute;
            left: 0;
            bottom: 20px;
            // top: 50px;
            width: 100%;
            text-align: center;
            z-index: 99;
            .chart-percent {
                font-size: 24px;
                margin-bottom: 15px;
                .chart-percent-cell {
                    font-size: 16px;
                }
            }
            .chart-change-wrap {
                font-size: 12px;
                color: #574fb6;
                margin-bottom: 15px;
                .chart-change-percent {
                    border-radius: 12px;
                    color: #fff;
                    margin-left: 5px;
                    .down {
                        background: #ff5136;
                        border-radius: 12px;
                        padding: 3px 5px;
                    }
                    .up {
                        background: #0dbe9c;
                        border-radius: 12px;
                        padding: 3px 5px;
                    }
                }
            }
            .chart-name {
                font-size: 14px;
            }
        }
    }
</style>
