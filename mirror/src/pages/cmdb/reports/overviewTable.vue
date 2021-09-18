<template>
    <div class="components-container yw-dashboard">
        <el-form :inline="true"
                 class="yw-form components-condition">
            <el-form-item label="月份">
                <el-date-picker v-model="params.month"
                                type="month"
                                value-format="yyyy-MM"
                                :clearable="false"
                                placeholder="选择月">
                </el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button type="primary"
                           @click="exportAll()">导出</el-button>
            </section>
        </el-form>
        <el-card class="box-card"
                 v-for="(tableItem, tableIndex) in tables"
                 :key="tableIndex"
                 style="margin-bottom: 5px;">
            <div slot="header">
                <span style="font-weight: bold">{{tableItem.name}}</span>
                <div class="clearfix fr">
                    <el-button type="text"
                               icon="el-icon-download"
                               @click="exportData(tableItem.name, tableItem.id)">导出</el-button>
                </div>
            </div>
            <el-table class="yw-el-table"
                      style="width:100%;"
                      :data="tableDatas[tableItem.id]"
                      stripe
                      border>
                <el-table-column :label="tableTitle"
                                 min-width="200"
                                 align="center"
                                 :prop="tableTitle"
                                 v-for="(tableTitle, tableTitleIndex) in tableTitles[tableItem.id]"
                                 :key="tableTitleIndex">
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>

<script>
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        props: ['params'],
        components: {
        },
        data() {
            return {
                // 汇总的表格
                tables: [],
                legendNames: {},
                /** 多表操作 */
                // 查询表头
                tableTitles: {},
                // 查询数据
                tableDatas: {}
            }
        },
        computed: {
        },
        methods: {
            // 导出全部报表
            exportAll() {
                this.$emit('setLoadingMethod', { loading: true, loading_text: '正在导出数据，请稍等' })
                let _this = this
                rbCmdbService.exportReportAllTableData(_this.params.month).then((data) => {
                    let blob = new Blob([data], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '报表数据汇总.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).catch(() => {
                    this.$message.error(_this.params.month + '没有数据!')
                })
                    .finally(() => {
                        this.$emit('setLoadingMethod', { loading: false, loading_text: '加载中' })
                    })
            },
            // 获得tableData
            getDatas() {
                let _this = this
                if (_this.tables) {
                    _this.tables.forEach(function (item) {
                        let tableId = item.id
                        // 获取总览页面数据
                        rbCmdbService.getOverviewReportTableData(tableId, _this.params.month, _this.params.type).then((data) => {
                            _this.$set(_this.legendNames, tableId, data.tableName)
                            _this.$set(_this.tableTitles, tableId, data.headers)
                            _this.$set(_this.tableDatas, tableId, data.tableData)
                        })
                    })
                }
            },
            // 获得table
            getTables() {
                return rbCmdbService.getOverviewReportTable(this.params.type, '').then((data) => {
                    this.tables = data
                    return data
                })
            },
            // 查询数据
            async query() {
                await this.getTables()
                this.getDatas()
            },
            // 导出数据
            exportData(tableName, tableId) {
                let _this = this
                let fileName = tableName + '_' + _this.params.month + '数据总览'
                rbCmdbService.exportOverviewReportTableData(tableId, _this.params.month, _this.params.type).then((data) => {
                    let blob = new Blob([data], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    // window.location.href = objectUrl
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = fileName + '.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            }
        },
        mounted() {
            this.query()
        },
    }
</script>

<style lang="scss" scoped>
</style>