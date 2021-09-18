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
            <el-form-item label="采购方式">
                <el-select v-model="search.procure_type"
                           filterable
                           clearable
                           placeholder="请选择采购方式">
                    <el-option v-for="item in procureTypeList"
                               :key="item.name"
                               :label="item.value"
                               :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务方式">
                <el-select v-model="search.service_type"
                           filterable
                           clearable
                           placeholder="请选择服务方式">
                    <el-option v-for="item in serviceTypeList"
                               :key="item.name"
                               :label="item.name"
                               :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="维保对象类型">
                <el-select v-model="search.maintenance_project_type"
                           filterable
                           clearable
                           placeholder="请选择维保对象类型">
                    <el-option v-for="item in maintenanceProjectTypeList"
                               :key="item.name"
                               :label="item.value"
                               :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="资源池">
                <el-select v-model="search.resource_pool"
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
        <br /><br />
        <el-card class="box-card"
                 style="min-height:120px">
            <div slot="header">
                <span style="font-weight: bold">各资源池维保信息统计</span>
                <div class="clearfix fr">
                    <el-button type="text"
                               icon="el-icon-upload2"
                               @click="firstLayerExport">导出</el-button>
                </div>
            </div>
            <el-form class="yw-form">
                <div class="yw-el-table-wrap">
                    <el-table :data="firstData"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              empty-text="无数据"
                              :span-method="rowSpanDeal"
                              border>
                        <el-table-column label="资源池"
                                         prop="resourcePool"></el-table-column>
                        <el-table-column label="维保种类"
                                         prop="maintenanceProjectType">
                            <template slot-scope="scope">
                                <el-button type="text"
                                           @click="showSecondLayer(scope.row)">
                                    {{scope.row.maintenanceProjectType}}
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="维保项目数量"
                                         prop="projectNum"></el-table-column>
                        <el-table-column label="维保设备数量"
                                         prop="deviceNum"></el-table-column>
                        <el-table-column label="总合同金额(万)"
                                         prop="totalMoney"
                                         :formatter="formatterMoney"></el-table-column>
                        <el-table-column label="最近维保到期时间"
                                         prop="serviceEndTime"
                                         :formatter="formatTime"></el-table-column>
                    </el-table>
                </div>
            </el-form>
        </el-card><br />
        <secondLayer id="card2"
                     v-if="secondLayer.flag"
                     :tranData="secondLayer.data"
                     :title="secondRequest"
                     @setCallBack="secondLayerCallBack"
                     @setExportFunc="secondLayerExport"></secondLayer><br />
        <thirdLayer id="card3"
                    v-if="thirdLayer.flag"
                    :tranData="thirdLayer.data"
                    :title="thirdRequest"
                    @setCallBack="thirdLayerCallBack"
                    @setExportFunc="thirdLayerExport"></thirdLayer><br />
        <fourthLayer id="card4"
                     v-if="fourthLayer.flag"
                     :tranData="fourthLayer.data"
                     :title="fourthRequest"
                     @setExportFunc="fourthLayerExport"></fourthLayer>
    </div>
</template>

<script>
    import maintenStatistAnalysisService from 'src/services/cmdb/rb-maintenStatistAnalysis-service.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    export default {
        mixins: [QueryObject],
        components: {
            secondLayer: () => import('src/pages/resource/maintenance/maintenStatistAnalysis/secondLayer.vue'),
            thirdLayer: () => import('src/pages/resource/maintenance/maintenStatistAnalysis/thirdLayer.vue'),
            fourthLayer: () => import('src/pages/resource/maintenance/maintenStatistAnalysis/fourthLayer.vue'),
        },
        data() {
            return {
                resourcePoolList: [{
                    name: '哈尔滨资源池',
                    value: '哈尔滨资源池'
                }], // 资源池集合
                brandList: [], // 品牌集合
                deviceClassList: [], // 设备分类集合
                maintenanceProjectTypeList: [{
                    name: '硬件',
                    value: '硬件'
                }], // 维保对象类型集合
                procureTypeList: [], // 采购方式集合
                serviceTypeList: [], // 服务方式集合
                produceList: [],  // 维保厂商集合（服务、合同）
                firstData: [],  // 第一层数据集合
                loading: true,
                loading_text: '正在查询数据,请稍等...',
                search: {
                    project_name: '',
                    contract_produce: '',
                    service_produce: '',
                    service_start_time: '',
                    service_end_time: '',
                    brand: '',
                    device_class: '',
                    resource_pool: '',
                    procure_type: '',
                    service_type: '',
                    maintenance_project_type: '',
                    device_type: '',
                    warranty_date: ''
                },
                secondLayer: {
                    flag: false,
                    data: []
                },
                thirdLayer: {
                    flag: false,
                    data: []
                },
                fourthLayer: {
                    flag: false,
                    data: []
                },
                firstRequest: {},
                secondRequest: {},
                thirdRequest: {},
                fourthRequest: {}
            }
        },
        mounted() {
            this.getIdcTypeList()
            this.getBrandList()
            this.getDeviceClassList()
            this.getMaintenanceProjectTypeList()
            this.getProcureTypeList()
            this.getServiceType()
            this.getProduceList()
            this.getFirstLayerData({})
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
                    }
                })
            },
            formatterMoney(row, column) {
                var obj = row[column.property]
                return parseFloat(obj).toFixed(2)
            },
            // 第一层导出
            firstLayerExport() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                if (this.search.resource_pool == '') {
                    this.search.resource_pool = null
                }
                maintenStatistAnalysisService.firstLayerExport(this.firstRequest).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '各资源池维保信息统计.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 第二层导出
            secondLayerExport() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                this.formatterRequestParam(this.secondRequest)
                maintenStatistAnalysisService.secondLayerExport(this.search).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: this.secondRequest.resourcePool + '(资源池)-' + this.secondRequest.maintenanceProjectType + '(维保类型)维保信息统计.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 第三层导出
            thirdLayerExport() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                this.formatterRequestParam(this.thirdRequest)
                maintenStatistAnalysisService.thirdLayerExport(this.search).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: this.thirdRequest.resourcePool + '(资源池)-' + this.thirdRequest.maintenanceProjectType + '(维保类型)-' + this.thirdRequest.deviceType + '(设备类型)维保信息统计.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 第四层导出
            fourthLayerExport() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                this.formatterRequestParam(this.fourthRequest)
                maintenStatistAnalysisService.fourthLayerExport(this.search).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: this.fourthRequest.resourcePool + '(资源池)-' + this.fourthRequest.maintenanceProjectType + '(维保类型)-' + this.fourthRequest.deviceType + '(设备类型)-' + this.fourthRequest.serviceEndTime + '(出保日期)出保项目明细 .xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 触发第四层数据
            thirdLayerCallBack(row) {
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                this.fourthLayer.flag = true
                this.fourthRequest = row
                this.formatterRequestParam(this.fourthRequest)
                this.getFourthLayerData(this.search)
            },
            // 触发第三层数据
            secondLayerCallBack(row) {
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                this.fourthLayer.flag = false
                this.thirdLayer.flag = true
                this.thirdRequest = row
                this.formatterRequestParam(this.thirdRequest)
                this.getThirdLayerData(this.search)
            },
            // 触发第二层数据
            showSecondLayer(row) {
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                this.thirdLayer.flag = false
                this.fourthLayer.flag = false
                this.secondLayer.flag = true
                var tmpData = {
                    maintenanceProjectType: row.maintenanceProjectType,
                    resourcePool: row.resourcePool
                }
                this.secondRequest = tmpData
                this.formatterRequestParam(this.secondRequest)
                this.getSecondLayerData(this.search)
            },
            // 格式化请求参数
            formatterRequestParam(row) {
                if (row.maintenanceProjectType != undefined) {
                    this.search.maintenance_project_type = row.maintenanceProjectType
                } else {
                    this.search.maintenance_project_type = ''
                }
                if (row.resourcePool != undefined) {
                    this.search.resource_pool = row.resourcePool
                }
                if (row.deviceType != undefined) {
                    this.search.device_type = row.deviceType
                } else {
                    this.search.device_type = ''
                }
                if (row.serviceEndTime != undefined) {
                    this.search.warranty_date = row.serviceEndTime
                } else {
                    this.search.warranty_date = ''
                }
            },
            // 查询
            doSearch() {
                this.loading = true
                this.loading_text = '正在查数据,请稍等...'
                // 隐藏下钻的卡片
                this.secondLayer.flag = false
                this.thirdLayer.flag = false
                this.fourthLayer.flag = false
                // 重置下钻的条件
                this.search.warranty_date = ''
                this.search.device_type = ''
                if (this.search.resource_pool == '') {
                    this.search.resource_pool = null
                }
                this.firstRequest = this.search
                this.getFirstLayerData(this.search)
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
                    resource_pool: '',
                    procure_type: '',
                    service_type: '',
                    maintenance_project_type: '',
                    device_type: '',
                    warranty_date: ''
                }
                this.search = search
                this.firstRequest = search
                this.doSearch()
            },
            formatTime(row, column) {
                return this.timestampToTime(row[column.property])
            },
            // 时间戳转时间格式
            timestampToTime(time) {
                const date = new Date(time)
                var Y = date.getFullYear() + '-'
                var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
                return Y + M + D
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
                // var params = {
                //   'type': 'device_mfrs'
                // }
                // maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                //   this.brandList = res
                //   return res
                // })
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
            // 获取维保对象类型列表
            getMaintenanceProjectTypeList() {
                let params = {
                    'type': 'maintenance_project_type'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.maintenanceProjectTypeList = res
                    return res
                })
            },
            // 获取采购类型列表
            getProcureTypeList() {
                let params = {
                    'type': 'procure_type'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.procureTypeList = res
                    return res
                })
            },
            // 获取服务类型列表
            getServiceType() {
                let params = {
                    'type': 'mainten_service_type'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.serviceTypeList = res
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
            // 获取第一层数据
            getFirstLayerData(search) {
                maintenStatistAnalysisService.firstLayerAPI(search).then((res) => {
                    this.firstData = res
                    this.loading = false
                })
            },
            // 获取第二层数据
            getSecondLayerData(search) {
                maintenStatistAnalysisService.secondLayerAPI(search).then((res) => {
                    this.secondLayer.data = res
                    this.loading = false
                    document.querySelector('#card2').scrollIntoView(true)
                })
            },
            // 获取第三层数据
            getThirdLayerData(search) {
                maintenStatistAnalysisService.thirdLayerAPI(search).then((res) => {
                    this.thirdLayer.data = res
                    this.loading = false
                    document.querySelector('#card3').scrollIntoView(true)
                })
            },
            // 获取第四层数据
            getFourthLayerData(search) {
                maintenStatistAnalysisService.fourthLayerAPI(search).then((res) => {
                    this.fourthLayer.data = res
                    this.formatServiceNum(res)
                    this.loading = false
                    document.querySelector('#card4').scrollIntoView(true)
                })
            },
            // 合并单元格
            rowSpanDeal({ rowIndex, columnIndex }) {
                if (columnIndex === 0) {
                    if (rowIndex % 2 === 0) {
                        var srcIndex = rowIndex
                        var newIndex = rowIndex + 1
                        if (newIndex < this.firstData.length) {
                            if (this.firstData[srcIndex].resourcePool != this.firstData[newIndex].resourcePool) {
                                return {
                                    rowspan: 1,
                                    colspan: 1
                                }
                            }
                        }
                        return {
                            rowspan: 2,
                            colspan: 1
                        }
                    } else {
                        var oldValue = this.firstData[rowIndex - 1].resourcePool
                        var newValue = this.firstData[rowIndex].resourcePool
                        if (oldValue != newValue) {
                            return {
                                rowspan: 1,
                                colspan: 1
                            }
                        }
                        return {
                            rowspan: 0,
                            colspan: 0
                        }
                    }
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
