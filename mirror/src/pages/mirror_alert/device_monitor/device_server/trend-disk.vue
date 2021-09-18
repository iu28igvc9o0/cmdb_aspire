<template>
    <!-- 趋势图：磁盘分区 -->
    <section class="chart-part">
        <div class="chart-title">{{title}}</div>
        <!-- <div class="chart-content">
            <YwLine :lineDatas="lineDatas[0]"
                    v-if="lineDatas[0].show"></YwLine>
        </div> -->
        <div class="chart-table">
            <YwTable :tableTitles="tableTitles"
                     :option="{'highlight-current-row':true,'enClick':true,'selfClass':''}"
                     @sortTable="sortTable"
                     @clickTableRow="clickTableRow"
                     :tableDatas="tableDatas"></YwTable>
        </div>
    </section>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import Filter from '../filter.js'
    import { formatDateByFilter } from 'src/assets/js/utility/rb-filters.js'

    export default {
        mixins: [QueryObject, Filter],
        props: ['moduleData'],
        components: {
            YwLine: () => import('src/components/common/yw-line.vue'),
            YwTable: () => import('src/components/common/yw-table.vue'),
        },
        data() {
            return {
                title: '磁盘分区使用率',
                // 查询结果
                result: '',
                lineDatas: [
                    {
                        show: true,
                        id: 'line-1',
                        chartOption: 'line-area-option',
                        chartDatas: [],
                        details: { legend: { show: false }, seriesName: [{ name: '', yLabel: 'cpu', xLabel: 'name', yLabelDatas: '', xLabelDatas: '', lineColor: '#6FDBF8' }] }
                    },
                ],
                tableTitles: [],
                tableDatas: [],
                // 当前活动行(默认最多展示4条数据)
                activeTableRow: 'all',
            }
        },
        methods: {

            // 表格点击事件
            async clickTableRow(row) {

                if (this.activeTableRow === row.id) {
                    this.activeTableRow = 'all'
                } else {
                    this.activeTableRow = row.id
                }
                await this.getTableDatas()
                this.getLineDatas()

            },
            // 表格排序
            sortTable(datas) {
                this.tableDatas = datas
            },
            // 获得表格数据
            getTableDatas() {
                // 更新表格
                // this.updateComponent()
                this.tableTitles = [
                    { name: 'name', title: '分区信息', titleHtml: '<span style="color:#21aeff">分区信息</span>', style: { sortable: true } },
                    { name: 'num', title: '使用率', titleHtml: '<span style="color:#21aeff">使用率</span>', style: { sortable: true, isHtml: true, width: 200, align: 'right' } },

                ]

                let params = {
                    'instanceId': this.moduleData.instanceId,
                    itermType: 'diskUse',
                    deviceClass: '服务器'
                }
                return this.$api.queryServerData(params).then((res) => {
                    if (!res) {
                        this.tableDatas = []
                        return false
                    }
                    this.result = res
                    this.tableDatas = res.series.map((item) => {
                        let num = item.avg ? item.avg : 0
                        let color = '#31A82D'
                        if (num > 50) {
                            color = '#D77728'
                        }
                        let resObj = {
                            id: item.name,
                            name: item.name,
                            num: num,
                            numHtml: `<span style="color:${color};">${num}%</span>`
                        }

                        return resObj
                    })
                    return res
                })


            },
            // 获得趋势
            getLineDatas() {
                let res = this.tableDatas.concat([]).slice(0, 4)
                if (res && res.length > 0) {
                    if (this.activeTableRow && (this.activeTableRow !== 'all')) {
                        res = this.tableDatas.filter((num) => {
                            return num.id === this.activeTableRow
                        })
                    }

                    let datetime = this.result && this.result.xaxis || []
                    let time = datetime.map((item) => {
                        return formatDateByFilter(item, 'HH:mm')
                    })

                    this.lineDatas[0].details.seriesName = res.map((item) => {
                        return {
                            name: item.name,
                            yLabelDatas: item.data,
                            xLabelDatas: time,
                            xLabelDatasTooltip: datetime,
                            lineColor: item.lineColor,
                            yLabelName: 'MB/s'
                        }
                    })
                    this.lineDatas[0].chartDatas = res
                }


                this.lineDatas[0].show = false
                this.$nextTick(() => {
                    this.lineDatas[0].show = true
                })

            },
            async init() {
                await this.getTableDatas()
                // this.getLineDatas()
            }
        },
        mounted() {
            if (this.moduleData.instanceId) {
                this.init()
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../device-monitor.scss";
    .chart-part {
        // .chart-content {
        //     height: calc(100% - 180px);
        // }
        .chart-table {
            width: 100%;
            height: calc(100% - 40px);
            position: absolute;
            top: 60px;
            padding: 0 20px;
        }
    }
</style>
