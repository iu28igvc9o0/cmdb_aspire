<template>
    <el-card class="box-card">
        <div slot="header">
            <span style="font-weight: bold">{{tranData.title}}</span>
            <div class="clearfix fr">
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="fileExport">导出</el-button>
            </div>
        </div>
        <el-form class="yw-form">
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="tranData.data"
                          max-height="500px"
                          :element-loading-text="loading_text"
                          stripe
                          border
                          size="mini"
                          style="width: 100%">
                    <el-table-column label="项目名称"
                                     align="left"
                                     width="180"
                                     prop="project_name"
                                     fixed="left"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同编号"
                                     align="left"
                                     width="150"
                                     prop="project_no"
                                     fixed="left"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同供应商"
                                     align="left"
                                     width="100"
                                     prop="contractProduce"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同联系人"
                                     align="left"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="contractNames"></el-table-column>
                    <el-table-column label="合同联系人电话"
                                     align="left"
                                     width="180"
                                     :show-overflow-tooltip="true"
                                     prop="contractPhones"></el-table-column>
                    <el-table-column label="合同联系人邮箱"
                                     align="left"
                                     width="180"
                                     :formatter="supplyFormat"
                                     :show-overflow-tooltip="true"
                                     prop="contractEmails"></el-table-column>
                    <el-table-column label="维保类型"
                                     align="center"
                                     width="100"
                                     prop="maintenance_type"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务形式"
                                     align="center"
                                     width="100"
                                     prop="service_type"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="开始时间"
                                     align="center"
                                     width="120"
                                     sortable
                                     :formatter="dateFormat"
                                     prop="service_start_time"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="结束时间"
                                     align="center"
                                     sortable
                                     width="120"
                                     :formatter="dateFormat"
                                     prop="service_end_time"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务供应商"
                                     align="center"
                                     width="120"
                                     prop="serviceProduce"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务联系人"
                                     align="left"
                                     width="150"
                                     :show-overflow-tooltip="true"
                                     prop="serviceNames"></el-table-column>
                    <el-table-column label="服务联系人电话"
                                     align="left"
                                     width="150"
                                     :show-overflow-tooltip="true"
                                     prop="servicePhones"></el-table-column>
                    <el-table-column label="服务联系人邮箱"
                                     align="left"
                                     width="150"
                                     :show-overflow-tooltip="true"
                                     prop="serviceEmails"></el-table-column>
                    <el-table-column label="设备区域"
                                     align="center"
                                     width="120"
                                     :show-overflow-tooltip="true"
                                     prop="device_area"></el-table-column>
                    <el-table-column label="维保对象类型"
                                     align="center"
                                     width="120"
                                     :show-overflow-tooltip="true"
                                     prop="maintenance_project_type"></el-table-column>
                    <el-table-column label="采购类型"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="procure_type"></el-table-column>
                    <el-table-column label="设备类型"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="device_type"></el-table-column>
                    <el-table-column label="金额(万)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="money">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.money)) ? parseFloat(scope.row.money).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="税前(万)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="pre_tax">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.pre_tax)) ? parseFloat(scope.row.pre_tax).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="税率(%)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="tax_rate">
                    </el-table-column>
                    <el-table-column label="税后(万)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="after_tax">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.after_tax)) ? parseFloat(scope.row.after_tax).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="单价(万)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="unit_price">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.unit_price)) ? parseFloat(scope.row.unit_price).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="总价(万)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="total_price">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.total_price)) ? parseFloat(scope.row.total_price).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="结算方式"
                                     align="center"
                                     width="120"
                                     :show-overflow-tooltip="true"
                                     prop="pay_method">
                    </el-table-column>
                    <el-table-column label="折扣后金额(万)"
                                     align="center"
                                     width="150"
                                     :show-overflow-tooltip="true"
                                     prop="discount_amount">
                        <el-template slot-scope="scope">
                            {{!isNaN(parseFloat(scope.row.discount_amount)) ? parseFloat(scope.row.discount_amount).toFixed(2) : ''}}
                        </el-template>
                    </el-table-column>
                    <el-table-column label="折扣率(%)"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="discount_rate"></el-table-column>
                    <el-table-column label="服务数量"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="service_num"></el-table-column>
                    <el-table-column label="关联设备"
                                     align="center"
                                     width="100"
                                     :show-overflow-tooltip="true"
                                     prop="deviceNum"></el-table-column>
                </el-table>
                <YwPagination @handleSizeChange="sizeChange"
                              @handleCurrentChange="pageChange"
                              :current-page="tranData.page.pageNo"
                              :page-sizes="tranData.page.pageSizes"
                              :page-size="tranData.page.pageSize"
                              :total="tranData.page.totalSize"></YwPagination>
            </div>
        </el-form>
    </el-card>
</template> 

<script>
    import moment from 'moment'
    export default {
        props: ['tranData'],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
            }
        },
        mounted() {
        },
        methods: {
            sizeChange(row) {
                var page = {
                    pageNo: this.tranData.page.pageNo,
                    pageSize: row
                }
                this.$emit('setPageWithSecond', page)
            },
            pageChange(row) {
                var page = {
                    pageNo: row,
                    pageSize: this.tranData.page.pageSize
                }
                this.$emit('setPageWithSecond', page)
            },
            fileExport() {
                this.$emit('setExportFile', false)
            },
            // 格式化日期为yyyy-MM-dd
            dateFormat(row, column) {
                var date = row[column.property]
                if (date === undefined) {
                    return ''
                }
                var dataStr = moment(date).format('YYYY-MM-DD')
                if (dataStr == '1970-10-01') {
                    return '----/--/--'
                }
                return dataStr
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
