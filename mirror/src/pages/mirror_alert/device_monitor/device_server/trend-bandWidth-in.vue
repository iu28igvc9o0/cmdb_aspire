<template>
    <!-- 趋势图：网络带宽（In） -->
    <section class="chart-part">
        <div class="chart-title">{{title}}</div>
        <div class="chart-content">
            <YwLine :lineDatas="lineDatas[0]"
                    v-if="lineDatas[0].show"></YwLine>
        </div>
        <div class="chart-table">
            <YwTable :tableTitles="tableTitles"
                     :option="{'highlight-current-row':true,'enClick':true,'selfClass':'','max-height':'150'}"
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
                title: '网络进带宽（in）',
                // 查询结果
                result: '',
                lineDatas: [
                    {
                        show: true,
                        id: 'line-1',
                        chartOption: 'line-area-option',
                        chartDatas: [],
                        details: { legend: { show: false }, seriesName: [{ name: '', yLabel: 'cpu', xLabel: 'name', yLabelName: 'MB/s', yLabelDatas: '', xLabelDatas: '', lineColor: '#6FDBF8' }] }
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
                    { name: 'name', title: '', style: { isHtml: true } },
                    { name: 'max', title: 'max', titleHtml: '<span style="color:#21aeff">max</span>', style: { sortable: true, isHtml: true, width: 200, align: 'right' } },
                    { name: 'avg', title: 'avg', titleHtml: '<span style="color:#21aeff">avg</span>', style: { sortable: true, isHtml: true, width: 200, align: 'right' } },

                ]

                let params = {
                    'instanceId': this.moduleData.instanceId,
                    itermType: 'netIn',
                    deviceClass: '服务器'
                }
                return this.$api.queryServerData(params).then((res) => {
                    if (!res) {
                        this.tableDatas = []
                        return false
                    }
                    this.result = res
                    this.tableDatas = res.series.map((item, index) => {
                        let id = item.name
                        let textColor = '#333'
                        if (this.activeTableRow !== 'all' && this.activeTableRow !== id) {
                            textColor = '#ccc'
                        }

                        let flagColor = this.lineColorList[index % (this.lineColorList.length)]

                        let flag = `<span style="display:inline-block;vertical-align:middle;width:12px;height:4px;background:${flagColor};margin-right:5px;"></span>`
                        let resObj = {
                            id: id,
                            name: item.name,
                            nameHtml: `<span style="cursor:pointer;">${flag}<span style="color:${textColor}">${item.name}</span></span>`,
                            data: item.data,
                            max: item.max,
                            maxHtml: `<span style="color:${textColor}">${item.max}MB/s</span>`,
                            avg: item.avg,
                            avgHtml: `<span style="color:${textColor}">${item.avg}MB/s</span>`,
                            lineColor: flagColor,
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
                this.getLineDatas()
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
        .chart-content {
            height: calc(100% - 180px);
        }
    }
</style>
