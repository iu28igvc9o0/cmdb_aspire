<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="85px">
            <el-form-item label="维保项目">
                <el-input v-model="search.project_name"></el-input>
            </el-form-item>
            <el-form-item label="合同供应商">
                <el-select v-model="search.contract_produce"
                           filterable
                           clearable
                           placeholder="请选择合同供应商">
                    <el-option v-for="item in produceList"
                               :key="item"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务供应商">
                <el-select v-model="search.service_produce"
                           filterable
                           clearable
                           placeholder="请选择服务供应商">
                    <el-option v-for="item in produceList"
                               :key="item"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="品牌">
                <el-select v-model="search.brand"
                           filterable
                           clearable
                           placeholder="请选择品牌">
                    <el-option v-for="item in brandList"
                               :key="item.produce_name"
                               :label="item.produce_name"
                               :value="item.produce_name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备分类">
                <el-select v-model="search.device_class"
                           filterable
                           clearable
                           placeholder="请选择设备分类">
                    <el-option v-for="item in deviceClassList"
                               :key="item.name"
                               :label="item.value"
                               :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="资源池">
                <el-select v-model="search.device_area"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="(item, index) in resourcePoolList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务周期">
                <el-date-picker v-model="search.service_start_time"
                                type="date"
                                placeholder="选择服务开始日期"
                                value-format="yyyy-MM-dd"
                                format="yyyy-MM-dd"></el-date-picker> -
                <el-date-picker v-model="search.service_end_time"
                                type="date"
                                placeholder="选择服务结束日期"
                                value-format="yyyy-MM-dd"
                                format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="doSearch()">查询</el-button>
                <el-button @click="resetSearch()">重置</el-button>
            </section>
        </el-form>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button class="btn-icons-wrap"
                           type="text"
                           icon="el-icon-upload2"
                           @click="periodExport">导出</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="firstData"
                        class="yw-el-table"
                        stripe
                        tooltip-effect="dark"
                        empty-text="无数据"
                        :span-method="rowSpanDeal"
                        border>
                    <el-table-column label="编号"
                                    type="index"
                                    :index="indexMethod"
                                    :show-overflow-tooltip="true"
                                    width="50px"></el-table-column>
                    <el-table-column label="维保项目"
                                    prop="projectName"
                                    :show-overflow-tooltip="true"
                                    width="120px"></el-table-column>
                    <el-table-column label="服务供应商"
                                    prop="serviceProduce"
                                    :show-overflow-tooltip="true"
                                    width="150px"></el-table-column>
                    <el-table-column label="合同供应商"
                                    prop="contractProduce"
                                    :show-overflow-tooltip="true"
                                    width="120px"></el-table-column>
                    <el-table-column label="设备类型"
                                    prop="deviceType"
                                    :show-overflow-tooltip="true"
                                    width="120px"></el-table-column>
                    <el-table-column label="资源池"
                                    prop="deviceArea"
                                    :show-overflow-tooltip="true"
                                    width="150px"></el-table-column>
                    <el-table-column label="周期"
                                    prop="period"
                                    :show-overflow-tooltip="true"
                                    width="100px"></el-table-column>
                    <el-table-column label="服务数量"
                                    :show-overflow-tooltip="true"
                                    prop="serviceNumName"
                                    width="100px"></el-table-column>
                    <el-table-column label="设备数量"
                                    prop="deviceNum"
                                    :show-overflow-tooltip="true"
                                    width="100px"></el-table-column>
                    <el-table-column label="合同金额(万)"
                                    prop="money"
                                    :show-overflow-tooltip="true"
                                    :formatter="numberFormate"
                                    width="120px"></el-table-column>
                    <el-table-column label="折扣率(%)"
                                    prop="discountRate"
                                    :show-overflow-tooltip="true"
                                    :formatter="numberFormate"
                                    width="100px"></el-table-column>
                    <el-table-column label="设备差额"
                                    prop="deviceNumDiff"
                                    :show-overflow-tooltip="true"
                                    width="150px"></el-table-column>
                    <el-table-column label="合同金额差额"
                                    prop="totalMoneyDiff"
                                    :formatter="numberFormate"
                                    :show-overflow-tooltip="true"
                                    width="150px"></el-table-column>
                    <el-table-column label="折扣率差额"
                                    prop="rateDiff"
                                    :show-overflow-tooltip="true"
                                    width="150px"></el-table-column>
                </el-table>
            </div>
        </el-form>
    </div>
</template>

<script>
    import maintenStatistAnalysisService from 'src/services/cmdb/rb-maintenStatistAnalysis-service.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    export default {
        mixins: [QueryObject],
        data() {
            return {
                resourcePoolList: [{
                    name: '信息港资源池',
                    value: '信息港资源池'
                }], // 资源池集合
                brandList: [], // 品牌集合
                deviceClassList: [], // 设备分类集合
                produceList: [],  // 维保厂商集合（服务、合同）
                firstData: [],  // 数据集合
                loading: false,
                loading_text: '正在查询数据,请稍等...',
                search: {
                    project_name: '',
                    contract_produce: '',
                    service_produce: '',
                    service_start_time: '',
                    service_end_time: '',
                    brand: '',
                    device_class: '',
                    device_area: '',
                },
                columnSpanList: [0, 1, 2, 3, 4, 5, 11, 12, 13]
            }
        },
        mounted() {
            this.getBrandList()
            this.getDeviceClassList()
            this.getProduceList()
            this.getIdcTypeList()
            this.getMaintenPeriodData({})
        },
        methods: {
            // 服务数量格式处理
            formatServiceNum(data) {
                data.forEach(item => {
                    if (item.serviceType != null && item.serviceNum) {
                        var types = item.serviceType.split(',')
                        var nums = item.serviceNum.split(',')
                        var result = types[0] + '(' + nums[0] + ')'
                        for (var i = 1; i < types.length; i++) {
                            result += ';'
                            result += types[i] + '(' + nums[i] + ')'
                        }
                        item.serviceNumName = result
                    } else {
                        item.serviceNumName = ''
                    }
                })
            },
            formatterMoney(row, column) {
                var obj = row[column.property]
                return parseFloat(obj).toFixed(2)
            },
            numberFormate(row, column) {
                var obj = row[column.property]
                if (obj == '' || obj == null || obj == '0') {
                    return 0
                }
                return parseFloat(obj).toFixed(2)
            },
            // 合并单元格
            rowSpanDeal({ row, rowIndex, columnIndex }) {
                // 下标从0开始
                if (rowIndex % 2 == 0) {
                    // 整理表格数据，将下一行的数据往前提，用于做单元格合并
                    row.period = '上期周期'
                    var next = this.firstData[rowIndex + 1]
                    row.projectName = next.projectName
                    row.serviceProduce = next.serviceProduce
                    row.contractProduce = next.contractProduce
                    row.deviceType = next.deviceType
                    row.deviceArea = next.deviceArea
                    row.deviceNumDiff = this.strCalFunction(next.deviceNum, row.deviceNum)
                    row.totalMoneyDiff = this.strCalFunction(next.money, row.money)
                    row.rateDiff = this.strCalFunction(next.discountRate, row.discountRate)
                    var spans = this.columnSpanList
                    var flag = false
                    for (var i = 0; i < spans.length; i++) {
                        if (spans[i] == columnIndex) {
                            flag = true
                            break
                        }
                    }
                    if (flag) {
                        return {
                            rowspan: 2,
                            colspan: 1
                        }
                    } else {
                        return {
                            rowspan: 1,
                            colspan: 1
                        }
                    }
                } else {
                    row.period = '本期周期'
                    let spans = this.columnSpanList
                    let flag = false
                    for (let i = 0; i < spans.length; i++) {
                        if (spans[i] == columnIndex) {
                            flag = true
                            break
                        }
                    }
                    if (flag) {
                        return {
                            rowspan: 0,
                            colspan: 0
                        }
                    } else {
                        return {
                            rowspan: 1,
                            colspan: 1
                        }
                    }
                }
            },
            // 第一层导出
            periodExport() {
                this.loading = true
                this.loading_text = '正在导出数据，请稍后...'
                maintenStatistAnalysisService.maintenPeriodExport(this.search).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '统计周期.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据，请稍后...'
                })
            },
            // 查询
            doSearch() {
                this.getMaintenPeriodData(this.search)
            },
            resetSearch() {
                var search = {
                    project_name: '',
                    contract_produce: '',
                    service_produce: '',
                    service_start_time: '',
                    service_end_time: '',
                    brand: '',
                    device_class: '',
                    device_area: '',
                }
                this.search = search
                this.doSearch()
            },
            // 获取资源池列表
            getIdcTypeList() {
                let params = {
                    'type': 'idcType'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.resourcePoolList = res
                    return res
                })
            },
            // 获取品牌列表
            getBrandList() {
                let req = {
                    'params': { 'produce_type': '生产供应商' }
                }
                rbmaintenanceCommonUtil.getProducesByType(req).then((res) => {
                    this.brandList = res
                })
            },
            // 获取设备分类列表
            getDeviceClassList() {
                var params = {
                    'type': 'device_class'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.deviceClassList = res
                    return res
                })
            },
            // 获取维保供应商列表
            getProduceList() {
                let req = {
                    'params': { 'produce_type': '维保供应商' }
                }
                rbmaintenanceCommonUtil.getProducesByType(req).then((res) => {
                    const list = []
                    for (let i in res) {
                        list.push(res[i].produce_name)
                    }
                    this.produceList = list
                })
            },
            // 获取维保周期数据
            getMaintenPeriodData(search) {
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                maintenStatistAnalysisService.maintenPeriodAnalysis(search).then((res) => {
                    this.firstData = res
                    this.formatServiceNum(res)
                }).finally(() => {
                    this.loading = false
                })
            },
            // 自定义编号
            indexMethod(index) {
                return index / 2 + 1
            },
            strCalFunction(a, b) {
                if (a == null || a == '') {
                    a = 0
                }
                if (b == null || b == '') {
                    b = 0
                }
                return parseFloat(a) - parseFloat(b)
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
