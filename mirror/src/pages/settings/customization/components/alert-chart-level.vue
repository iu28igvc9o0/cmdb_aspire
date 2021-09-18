<template>
    <!-- 告警：告警级别 -->
    <div class="content-chart"
         :style="{width: isCustomPage ? '100%' : '24.2%', height: isCustomPage ? '320px' : '310px'}"
         :class="{'mtop20' : isCustomPage}">
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#icongaojing"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <!-- <el-tabs class="yw-tabs chart-filter-item"
                 v-model="chartData.activeFilter"
                 @tab-click="changeTab">
          <el-tab-pane :label="tabItem.label"
                       :name="tabItem.name"
                       v-for="(tabItem,tabIndex) in chartData.filter"
                       :key="tabIndex">
          </el-tab-pane>
        </el-tabs> -->
            </div>
        </section>

        <!-- 多图表 -->
        <section class="chart-section">
            <div class="progress-rank-wrap">
                <div class="progress-rank-item"
                     v-for="(item,index) in chartData.rankList"
                     :key="index">
                    <YwProgress :datas="{name:item.level,percentage:item.rate,index:index+1}"
                                :option="progressOption"></YwProgress>
                </div>
            </div>
        </section>
        <!-- 多图表 -->
    </div>
</template>

<script>

    import DrawChart from 'src/utils/drawCharts.js'

    export default {
        mixins: [DrawChart],
        components: {
            YwProgress: () => import('src/components/common/yw-progress.vue'),
        },
        data() {
            return {
                progressOption: {
                    type: 'single',
                    title: {
                        show: true,
                        position: 'left',
                    },
                    label: {
                        // 位置
                        position: 'outer',// ['outer','inner']
                        // 百分比
                        percentage: {
                            show: true
                        },
                    }
                },
                chartData: {
                    name: '告警级别',
                    filter: [{ name: '日', label: 'day' }, { name: '周', label: 'week' }, { name: '月', label: 'month' }, { name: '季', label: 'season' }, { name: '年', label: 'year' }],
                    activeFilter: 'day',
                    rankList: [
                        { level: '重大', rate: 0 },
                        { level: '高', rate: 0 },
                        { level: '中', rate: 0 },
                        { level: '低', rate: 0 },
                    ],
                }
            }
        },
        methods: {
            // 获得数据
            getDatas() {
                let params = {
                    'idcType': this.conditionParams.poolActive,
                    'span': this.chartData.activeFilter,
                    // 'span': 'day',
                }
                this.$api.queryAlertLevel(params).then((res) => {
                    if (res.summary > 0) {
                        this.chartData.rankList[0].rate = parseInt(res.serious / res.summary * 100)
                        this.chartData.rankList[1].rate = parseInt(res.high / res.summary * 100)
                        this.chartData.rankList[2].rate = parseInt(res.medium / res.summary * 100)
                        this.chartData.rankList[3].rate = parseInt(res.low / res.summary * 100)
                    }
                })

            },

            changeTab() {
                this.getDatas()
            },

        },
        mounted() {
            this.getDatas()
        }

    }
</script>

<style lang="scss" scoped>
    .progress-rank-wrap {
        display: flex;
        flex-direction: column;
        justify-content: space-around;
    }
</style>

