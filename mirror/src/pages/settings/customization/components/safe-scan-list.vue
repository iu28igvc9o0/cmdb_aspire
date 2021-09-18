<template>

    <div class="content-chart"
         style="width: 74.8%;">
        <!-- 标题 -->
        <section class="chart-title-wrap clearfix">
            <svg class="svg-icon svg-icon-24"
                 aria-hidden="true">
                <use xlink:href="#iconloudongsaomiaoqingdan"></use>
            </svg>
            <span class="chart-title">{{chartData.name}}</span>
            <div class="chart-filter">
                <el-button class="chart-filter-item yw-chart-button-wrap"
                           type="primary"
                           @click="exportDatas">
                    <svg class="svg-icon svg-icon-20"
                         style="margin:0 5px 0 0;"
                         aria-hidden="true">
                        <use xlink:href="#icondaochu1"></use>
                    </svg><span class="inline-block-middle">导出</span>
                </el-button>
                <el-radio-group class="yw-radio-button-wrap chart-filter-item"
                                v-model="chartData.activeFilter"
                                @change="changeTab">
                    <el-radio-button :label="tabItem.label"
                                     v-for="(tabItem,tabIndex) in chartData.filter"
                                     :key="tabIndex">{{tabItem.name}}</el-radio-button>
                </el-radio-group>
                <treeselect class="yw-treeselect chart-filter-item"
                            v-model="chartData.activeFilter2"
                            :options="chartData.filter2"
                            :multiple="true"
                            :limit="1"
                            @input="changeTab"
                            placeholder="请选择业务系统">
                </treeselect>
                <section class="yw-search-wrap chart-filter-item">
                    <YwSearch :searchParams="searchParams"
                              style="width:180px;"
                              @changeSearch="changeSearch"></YwSearch>
                </section>
            </div>
        </section>
        <!-- 标题 -->

        <!-- 多图表 -->
        <section class="chart-section">

            <div class="yw-rank-table-wrap has-pagination">
                <el-table class="yw-rank-table"
                          style="width:100%"
                          :data="chartData.chartList[0].tableDatas">
                    <el-table-column label="序号"
                                     :width="60">
                        <template slot-scope="scope">
                            {{ scope.$index + 1}}
                        </template>
                    </el-table-column>
                    <el-table-column label="归属业务线">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark"
                                        :content="scope.row.bizLine"
                                        placement="top-start">
                                <span class="text-ellipse">
                                    {{ scope.row.bizLine}}
                                </span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="高危漏洞数量">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark"
                                        :content="scope.row.highLeaks"
                                        placement="top-start">
                                <span class="text-ellipse">
                                    {{ scope.row.highLeaks}}
                                </span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="中危漏洞数量">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark"
                                        :content="scope.row.mediumLeaks"
                                        placement="top-start">
                                <span class="text-ellipse">
                                    {{ scope.row.mediumLeaks}}
                                </span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="低危漏洞数量">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark"
                                        :content="scope.row.lowLeaks"
                                        placement="top-start">
                                <span class="text-ellipse">
                                    {{ scope.row.lowLeaks}}
                                </span>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告地址">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark"
                                        :content="scope.row.reportFileName"
                                        placement="top-start">
                                <a class="table-link underline text-ellipse"
                                   @click="download(scope.row)">{{ scope.row.reportFileName}}</a>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="报告详情">
                        <template slot-scope="scope">
                            <!-- <el-tooltip effect="dark"
                          content="详情"
                          placement="top-start">
                <a class="table-link text-ellipse"
                   @click="gotoDetails(scope.row)">详情</a>
              </el-tooltip> -->
                            <a class="table-link text-ellipse"
                               @click="gotoDetails(scope.row)">详情</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="流程处理">
                        <template slot-scope="scope">
                            <el-tooltip effect="dark"
                                        :content="scope.row.bpmId"
                                        placement="top-start">
                                <a class="table-link text-ellipse"
                                   @click="gotoFlow(scope.row)"> {{ scope.row.bpmId}}</a>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column label="修复状态">
                        <template slot-scope="scope">
                            <!-- <el-tooltip effect="dark"
                          :content="scope.row.repairStat == 0 ? '未修复' : '已修复'"
                          placement="top-start">
                <span class="text-ellipse">
                  {{ scope.row.repairStat == 0 ? '未修复' : '已修复' }}
                </span>
              </el-tooltip> -->
                            {{ scope.row.repairStat == 0 ? '未修复' : '已修复' }}
                        </template>
                    </el-table-column>
                </el-table>
            </div>

            <YwPagination class="page-chart"
                          layout='total, prev, pager, next, jumper'
                          :pager-count="3"
                          @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </section>
        <!-- 多图表 -->

        <!-- dialog -->
        <DialogDetails v-if="dialogDetails.dialogVisible"
                       :dialogMsg="dialogDetails"></DialogDetails>
        <!-- dialog -->
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import DrawChart from 'src/utils/drawCharts.js'

    export default {
        mixins: [QueryObject, DrawChart],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            YwSearch: () => import('src/components/common/yw-search.vue'),
            DialogDetails: () => import('./safe-scan-dialog.vue'),
        },
        data() {
            return {
                chartData: {
                    name: '漏洞扫描清单',
                    filter: [{ name: '全部', label: null }, { name: '已修复', label: '1' }, { name: '未修复', label: '0' }],
                    activeFilter: null,
                    filter2: [],
                    activeFilter2: null,
                    chartList: [{ tableTitle: [], tableDatas: [] }],
                },
                // 搜索框参数
                searchParams: {
                    keyword: '',
                    desc: {
                        placeholder: '设备IP',
                        bgcolor: '',
                        borderRadius: '2px',
                        borderColor: '#53D8FF',
                        color: '#88AAB4',
                        iconColor: '#BCC0C6'
                    }
                },
                // 查询条件
                queryParams: {

                },
                // dialog
                dialogDetails: {
                    dialogVisible: false,
                    id: '',// 每个弹窗数据的唯一标识(watch型弹窗使用)
                    data: {} // 数据(暂时没有详情接口，从列表数据携带)
                },
            }
        },
        methods: {

            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['page_no'] = this.currentPage
                    this.queryParams['page_size'] = this.pageSize
                } else {
                    this.queryParams = {
                        'beginDate': this.conditionParams.dateRange[0],
                        'endDate': this.conditionParams.dateRange[1],
                        'page_no': this.initPageChange(),
                        'page_size': this.pageSize,
                        'bizSys': this.chartData.activeFilter2,
                        'ip': this.searchParams.keyword,
                        'repairStat': this.chartData.activeFilter
                    }
                }

            },
            query(activePagination = false) {

                this.setParams(activePagination)

                this.$api.querySafeScanList(this.queryParams).then((res) => {
                    this.total = res.count
                    this.chartData.chartList[0].tableDatas = res.result
                })

            },

            // 下载
            download(row) {
                window.location.href = row.reportFileUrl
            },
            // 详情
            gotoDetails(row) {
                this.dialogDetails.id = row.id
                this.dialogDetails.dialogVisible = true
            },
            // 流程处理
            gotoFlow(row) {
                let bpmId = row.bpmId
                let url = `${sessionStorage.getItem('X7_SERVER_URL')}/front/#/inst/${bpmId}?mirrorToken=${sessionStorage.getItem('mirror')}`
                window.open(url, '_blank')
            },
            // 导出
            exportDatas() {
                let params = {
                    beginDate: this.conditionParams.dateRange[0],
                    endDate: this.conditionParams.dateRange[1],
                }
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: params,
                    binary: true,
                    url: '/v1/alerts/leak-scan/exportDetail'
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '漏洞扫描清单.xlsx'
                    })
                    return res
                })
            },

            changeTab() {
                this.query()
            },
            // 搜索框查询
            changeSearch(val) {
                this.searchParams.keyword = val
                this.query()
            },

        },
        created() {
            this.pageSize = 7
            this.query()
        }

    }
</script>

<style lang="scss" scoped>
    /deep/.chart-section {
        padding-top: 15px;
        padding-bottom: 15px;
    }
</style>

