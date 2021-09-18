<template>
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <!-- Tab页 -->
        <el-tabs v-model="activeName"
                 @tab-click="handleClick">
            <el-tab-pane label="三个月"
                         name="first"></el-tab-pane>
            <el-tab-pane label="六个月"
                         name="second"></el-tab-pane>
        </el-tabs>
        <!-- 查询条件开始 -->
        <el-form class="components-condition yw-form"
                 :inline="true"
                 :model="formData"
                 label-position="left"
                 label-width="75px">
            <el-form-item label="开始时间">
                <el-date-picker v-model="formData.date"
                                style="width: 260px"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="项目名称">
                <el-input v-model="formData.projectName"
                          placeholder="项目名称"
                          style="width: 130px"></el-input>
            </el-form-item>
            <el-form-item label="合同编号">
                <el-input v-model="formData.projectNo"
                          placeholder="合同编号"
                          style="width: 130px"></el-input>
            </el-form-item>
            <el-form-item label="服务供应商">
                <el-select v-model="formData.maintenProduce"
                           style="width: 130px;"
                           clearable
                           filterable>
                    <el-option v-for="(item, index) in companyList"
                               :key="index"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <!-- 查询条件结束 -->
        <!-- 结果开始 -->
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-upload2"
                           @click="exportOut"
                           :disabled="can_export">导出</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                        :data="result"
                        height="calc(100vh - 50px - 210px)"
                        :element-loading-text="loading_text"
                        stripe
                        border
                        size="mini"
                        style="width: 100%">
                    <el-table-column type="selection"
                                    min-width="20px"
                                    align="center"></el-table-column>
                    <el-table-column label="项目名称"
                                    align="left"
                                    width="180"
                                    prop="projectName"
                                    fixed="left"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同编号"
                                    align="left"
                                    width="150"
                                    prop="projectNo"
                                    fixed="left"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="合同供应商"
                                    align="left"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="contractProduce"></el-table-column>
                    <el-table-column label="合同联系人"
                                    align="left"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="contractProduceName"></el-table-column>
                    <el-table-column label="合同联系人电话"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="contractProducePhone"></el-table-column>
                    <el-table-column label="合同联系人邮箱"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="contractProduceEmail"></el-table-column>
                    <el-table-column label="维保类型"
                                    align="center"
                                    width="100"
                                    prop="maintenanceType"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务形式"
                                    align="center"
                                    width="100"
                                    prop="serviceType"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="开始时间"
                                    align="center"
                                    width="120"
                                    sortable
                                    :formatter="dateFormat"
                                    prop="serviceStartTime"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="结束时间"
                                    align="center"
                                    sortable
                                    width="120"
                                    :formatter="dateFormat"
                                    prop="serviceEndTime"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务供应商"
                                    align="center"
                                    width="100"
                                    prop="maintenProduce"
                                    :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="服务联系人"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="maintenProduceName"></el-table-column>
                    <el-table-column label="服务联系人电话"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="maintenProducePhone"></el-table-column>
                    <el-table-column label="服务联系人邮箱"
                                    align="left"
                                    width="180"
                                    :show-overflow-tooltip="true"
                                    prop="maintenProduceEmail"></el-table-column>
                    <el-table-column label="设备区域"
                                    align="center"
                                    width="120"
                                    :show-overflow-tooltip="true"
                                    prop="deviceArea"></el-table-column>
                    <el-table-column label="维保对象类型"
                                    align="center"
                                    width="120"
                                    :show-overflow-tooltip="true"
                                    prop="maintenanceProjectType"></el-table-column>
                    <el-table-column label="采购类型"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="procureType"></el-table-column>
                    <el-table-column label="金额(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="money"></el-table-column>
                    <el-table-column label="设备类型"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="deviceType"></el-table-column>
                    <el-table-column label="税前(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="preTax"></el-table-column>
                    <el-table-column label="税率(%)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="taxRate"></el-table-column>
                    <el-table-column label="税后(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="afterTax"></el-table-column>
                    <el-table-column label="单价(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="unitPrice"></el-table-column>
                    <el-table-column label="总价(万)"
                                    align="center"
                                    width="80"
                                    :show-overflow-tooltip="true"
                                    prop="totalPrice"></el-table-column>
                    <el-table-column label="结算方式"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="payMethod"></el-table-column>
                    <el-table-column label="折扣后金额(万)"
                                    align="center"
                                    width="130"
                                    :show-overflow-tooltip="true"
                                    prop="discountAmount"></el-table-column>
                    <el-table-column label="折扣率(%)"
                                    align="center"
                                    width="100"
                                    :show-overflow-tooltip="true"
                                    prop="discountRate"></el-table-column>
                </el-table>
                <YwPagination @handleSizeChange="handleSizeChange"
                            @handleCurrentChange="handleCurrentChange"
                            :current-page="currentPage"
                            :page-sizes="pageSizes"
                            :page-size="pageSize"
                            :total="total"></YwPagination>
            </div>
        </el-form>
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    import moment from 'moment'

    export default {
        name: 'ResourceMaintenanceProjectList',
        mixins: [QueryObject],
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        data() {
            return {
                // 绑定Tab标签
                activeName: 'first',
                intervalTime: 3,
                loading: false,
                loading_text: '正在查询数据, 请稍等...',
                can_export: false,
                // 表单数据
                formData: {
                    date: '',
                    projectName: '',
                    projectNo: '',
                    maintenProduce: '',
                },
                // 查询条件
                queryParams: {
                },
                companyList: [],
                // 查询结果
                result: []
            }
        },
        watch: {
        },
        created() {
            this.query()
            this.getBrandList()
        },
        methods: {
            // 处理Tab页切换
            handleClick(val) {
                this.activeName = val.name
                if (this.activeName == 'first') {
                    this.intervalTime = 3
                } else {
                    this.intervalTime = 6
                }
                this.reset()
                this.query()
            },
            // 获取厂家详情
            gotoDetail(row) {
                this.$router.push({
                    path: '/resource/maintenance/produceDetail',
                    query: { id: row }
                })
            },
            getBrandList() {
                let req = {
                    'params': { 'produce_type': '维保供应商' }
                }
                rbmaintenanceCommonUtil.getProducesByType(req).then((res) => {
                    const list = []
                    for (let i in res) {
                        list.push(res[i].produce_name)
                    }
                    this.companyList = list
                })
            },
            // 设置参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryParams['pageNo'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {
                    this.queryParams = {
                        'pageNo': this.initPageChange(),
                        'pageSize': this.pageSize,
                        'serviceStartTime': this.formData.date[0],
                        'serviceEndTime': this.formData.date[1],
                        'projectName': this.formData.projectName,
                        'projectNo': this.formData.projectNo,
                        'maintenProduce': this.formData.maintenProduce,
                        'intervalTime': this.intervalTime
                    }
                }
            },
            // 格式化日期为yyyy-MM-dd
            dateFormat(row, column) {
                var date = row[column.property]
                if (date === undefined) {
                    return ''
                }
                return moment(date).format('YYYY-MM-DD')
            },
            // 格式化供应商厂商联系人信息
            supplyFormat(row, column) {
                var produceInfo = row['contractProduceInfo']
                var concat = produceInfo == null ? null : produceInfo.concats
                var value = ''
                if (concat !== null && concat.length > 0) {
                    concat.forEach((item) => {
                        if (item[column.property] !== undefined) {
                            if (value !== '') {
                                value += ','
                            }
                            value += item[column.property]
                        }
                    })
                }
                return value
            },
            // 格式化服务厂商联系人信息
            serviceFormat(row, column) {
                var produceInfo = row['produceInfo']
                var concat = produceInfo == null ? null : produceInfo.concats
                var value = ''
                if (concat !== null && concat.length > 0) {
                    concat.forEach((item) => {
                        if (item[column.property] !== undefined) {
                            if (value !== '') {
                                value += ','
                            }
                            value += item[column.property]
                        }
                    })
                }
                return value
            },
            /** 查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.setParams(activePagination)
                this.loading = true
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/cmdb/maintenance/project/list',
                    data: this.queryParams
                }).then((res) => {
                    this.total = res.totalSize
                    this.result = res.data
                    return res
                }).finally(() => {
                    this.loading = false
                })
            },
            // 重置
            reset() {
                this.formData = {
                    date: '',
                    projectName: '',
                    projectNo: '',
                    maintenProduce: '',
                }
            },
            // 导出
            exportOut() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                this.can_export = true
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/cmdb/maintenance/project/export',
                    data: this.queryParams,
                    binary: true
                }).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '维保视图-' + this.intervalTime + '个月' + '.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                    this.can_export = false
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .el-date-editor .el-range-separator {
        padding: 0px;
    }
    // .home-right-pane .right-body {
    //     height: calc(100vh - 58px - 70px);
    // }
    .yw-el-table.el-table {
        height: calc((100vh - 150px) - 210px) !important;
    }
    // .yw-dashboard.components-container:not(.drag-main-box)
    //     > .yw-form:not(.components-condition) {
    //     flex: unset;
    // }
</style>
