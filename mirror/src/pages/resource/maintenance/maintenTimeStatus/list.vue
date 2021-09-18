<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="90px">
            <el-form-item label="资源池">
                <el-select v-model="search.resourcePool"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="(item, index) in resourcePoolList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="维保对象类型"
                          label-width="105px">
                <el-select v-model="search.maintenProjectType"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="(item, index) in maintenanceProjectTypeList"
                               :key="index"
                               :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备类型">
                <el-select v-model="search.deviceType"
                           filterable
                           clearable
                           placeholder="请选择">
                    <el-option v-for="item in deviceTypeList"
                               :key="item.name"
                               :label="item.value"
                               :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务供应商">
                <el-select v-model="search.serviceProduce"
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
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="doSearch()">查询</el-button>
                <el-button @click="resetSearch()">重置</el-button>
            </section>
        </el-form>
        <firstLayer :tranData="firstData"
                    @setSecondRequest="secondRequest"
                    @setExportFile="exportFirstData"></firstLayer><br />
        <secondLayer v-if="secondData.visiable"
                     :tranData="secondData"
                     @setPageWithSecond="pagingWithSecod"
                     @setExportFile="exportSecondData"></secondLayer>
    </div>
</template>

<script>
    import maintenStatistAnalysisService from 'src/services/cmdb/rb-maintenStatistAnalysis-service.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    export default {
        mixins: [QueryObject],
        components: {
            firstLayer: () => import('src/pages/resource/maintenance/maintenTimeStatus/first.vue'),
            secondLayer: () => import('src/pages/resource/maintenance/maintenTimeStatus/second.vue'),
        },
        data() {
            return {
                loading: false,
                loading_text: '正在查询数据,请稍等...',
                resourcePoolList: [{
                    name: '哈尔滨资源池',
                    value: '哈尔滨资源池'
                }],
                maintenanceProjectTypeList: [{
                    name: '硬件',
                    value: '硬件'
                }],
                deviceTypeList: [],
                produceList: [{
                    name: '华为',
                    value: '华为'
                }],
                search: {
                    resourcePool: null,
                    maintenProjectType: null,
                    deviceType: null,
                    serviceProduce: null
                },
                firstData: [],
                secondData: {
                    visiable: false,
                    title: '维保项目列表',
                    data: [],
                    page: {
                        pageNo: 1,
                        pageSize: 50,
                        totalSize: 0,
                        pageSizes: [10, 20, 50, 100]
                    }
                }
            }
        },
        mounted() {
            this.initList()
            this.firstRequest()
        },
        methods: {
            doSearch() {
                this.firstRequest(this.search)
                this.secondData.visiable = false
            },
            resetSearch() {
                var search = {
                    resourcePool: null,
                    maintenProjectType: null,
                    deviceType: null,
                    serviceProduce: null
                }
                this.search = search
                this.secondData.visiable = false
                this.firstRequest(this.search)
            },
            // 第一层导出
            exportFirstData() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                maintenStatistAnalysisService.exportMaintenStatusStatist(this.search).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '维保状态信息统计.xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 第二层导出
            exportSecondData() {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                var status = this.search['timeStatus']
                // 当pageNo为null时表示不分页
                this.search['pageNo'] = null
                this.search['pageSize'] = null
                maintenStatistAnalysisService.exportMaintenObjList(this.search).then((res) => {
                    this.exportFiles({
                        data: res,
                        fileType: 'application/vnd.ms-excel',
                        fileName: '维保项目列表(' + status + ').xls'
                    })
                    return res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 参数复原
            paramInit() {
                var page = {
                    pageNo: 1,
                    pageSize: 50,
                    totalSize: 0,
                    pageSizes: [10, 20, 50, 100]
                }
                this.secondData.page = page
            },
            // 触发第二层
            secondRequest(param) {
                this.paramInit()
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                this.secondData.title = '维保项目列表(' + param + ')'
                this.secondData.visiable = true
                // 添加查询条件
                this.search['timeStatus'] = param
                // 初始分页数据
                this.search['pageNo'] = 1
                this.search['pageSize'] = 50
                maintenStatistAnalysisService.getMaintenObjList(this.search).then(res => {
                    var _data = res.data
                    _data = this.makeDealServiceNum(_data)
                    this.secondData.data = _data
                    this.secondData.page.totalSize = res.totalSize
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 处理维保数量
            makeDealServiceNum(data) {
                data.forEach(item => {
                    var rsStr = ''
                    if (item.serviceType != null && item.serviceType != '') {
                        var types = item.serviceType.split(',')
                        var nums = item.serviceNum.split(',')
                        rsStr = types[0] + '(' + nums[0] + ')'
                        for (var i = 1; i < types.length; i++) {
                            rsStr = rsStr + ';' + types[i] + '(' + nums[i] + ')'
                        }
                    }
                    item['service_num'] = rsStr
                })
                console.info(data)
                return data
            },
            // 第二层分页触发
            pagingWithSecod(obj) {
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                // 组合请求参数
                this.search['pageNo'] = obj.pageNo
                this.search['pageSize'] = obj.pageSize
                // 更新子层的分页信息
                this.secondData.page.pageNo = obj.pageNo
                this.secondData.page.pageSize = obj.pageSize
                maintenStatistAnalysisService.getMaintenObjList(this.search).then(res => {
                    this.secondData.data = res.data
                    this.secondData.page.totalSize = res.totalSize
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            firstRequest(param) {
                this.loading = true
                this.loading_text = '正在查询数据,请稍等...'
                maintenStatistAnalysisService.maintenStatusStatist(param).then((res) => {
                    this.firstData = res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            initList() {
                this.getIdcTypeList()
                this.getProduceList()
                this.getMaintenanceProjectTypeList()
                this.getDeviceTypeList()
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
            // 获取设备类型列表
            getDeviceTypeList() {
                let params = {
                    'type': 'device_type'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.deviceTypeList = res
                    return res
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
