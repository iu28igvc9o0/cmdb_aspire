<template>
    <div class="components-container yw-dashboard" v-loading="loading" :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="85px">
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
            <el-form-item label="统计年份">
                <el-date-picker v-model="search.year"
                                style="width: 95px"
                                type="year"
                                value-format="yyyy"
                                :clearable="false"
                                placeholder="年"></el-date-picker>
            </el-form-item>
            <el-form-item label="业务系统">
                <el-select v-model="search.bizSystem"
                           placeholder="请选择"
                           filterable
                           clearable>
                    <el-option v-for="(item, index) in bizSystemList"
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
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="doSearch()">查询</el-button>
                <el-button @click="resetSearch()">重置</el-button>
            </section>
        </el-form>
        <div class="yw-el-table-wrap">
            <el-table :data="dataList" 
                      class="yw-el-table" 
                      stripe 
                      tooltip-effect="dark"
                      empty-text="无数据"
                      border>
                <el-table-column label="月份" prop="mouth" align="center" :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="设备增量" prop="count" align="center" :show-overflow-tooltip="true"></el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script>
    import maintenStatistAnalysisService from 'src/services/cmdb/rb-maintenStatistAnalysis-service.factory.js'
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    export default {
        data () {
            return {
                resourcePoolList:[],
                deviceTypeList: [],
                bizSystemList: [],
                search: {
                    year: '',
                    resourcePool: '',
                    deviceType: '',
                    bizSystem: ''
                },
                dataList: []
            }
        },
        mounted () {
            this.resetSearch()
            this.getIdcTypeList()
            this.getDeviceTypeList()
            this.getBizSysList()
            this.getDataList()
        },
        methods: {
            // 查询
            doSearch() {
                this.getDataList(this.search)
            },
            resetSearch(){
                let nowDate = new Date()
                let year = nowDate.getFullYear() + ''
                let tmpSearch = {
                    year: year,
                    resourcePool: '',
                    deviceType: '',
                    bizSystem: ''
                }
                this.search = tmpSearch
            },
            // 获取列表数据
            getDataList(){
                maintenStatistAnalysisService.statistDeviceIncreByTime(this.search).then((res) => {
                    this.dataList = res
                })
            },
            // 获取资源池列表
            getIdcTypeList () {
                let params = {
                    'type': 'idcType'
                }
                maintenStatistAnalysisService.getDictDataByType(params).then((res) => {
                    this.resourcePoolList = res
                    return res
                })
            },
            // 获取业务系统
            getBizSysList () {
                rbmaintenanceCommonUtil.getBizSystemList({}).then((res) => {
                    const bzList = []
                    var len = res.length
                    for(var i=0;i<len;i++) {
                        bzList.push({
                            name: res[i].bizSystem,
                            value: res[i].bizSystem
                        })
                    }
                    this.bizSystemList = bzList
                })
            },
            // 获取设备类型列表
            getDeviceTypeList () {
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
