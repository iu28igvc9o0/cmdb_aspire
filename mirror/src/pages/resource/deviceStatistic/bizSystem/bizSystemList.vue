<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         :element-loading-text="loading_text">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="65px">
            <el-form-item label="业务系统">
                <el-select v-model="biz_system"
                           placeholder="请选择"
                           clearable
                           filterable>
                    <el-option v-for="item in biz_systemList"
                               :key="item.id"
                               :label="item.bizSystem"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="资源池">
                <el-select v-model="idc_type"
                           placeholder="请选择"
                           @change="getpodNameList()"
                           clearable
                           filterable>
                    <el-option v-for="item in idc_typeList"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="POD项目">
                <el-select v-model="pod_name"
                           placeholder="请选择"
                           clearable
                           filterable>
                    <el-option v-for="item in pod_nameList"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="reloadTable">查询</el-button>
                <el-button @click="resetSearch">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="exportExcelData">导出</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          show-summary
                          :summary-method="getSummaries"
                          border
                          height="calc(100vh - 230px)">
                    <el-table-column label="业务系统"
                                     prop="biz_system"
                                     min-width="300px"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column label="资源池"
                                     prop="idc_type"></el-table-column>
                    <el-table-column label="POD项目"
                                     prop="pod_name"></el-table-column>
                    <el-table-column label="设备数量"
                                     prop="number"></el-table-column>
                    <el-table-column label="服务器数量"
                                     prop="server_number"></el-table-column>
                    <el-table-column label="网络设备数量"
                                     prop="network_number"></el-table-column>
                    <el-table-column label="存储设备数量"
                                     prop="storage_number"></el-table-column>
                    <el-table-column label="安全设备数量"
                                     prop="safety_number"></el-table-column>
                    <el-table-column label="其他设备数量"
                                     prop="other_number"></el-table-column>

                </el-table>
            </div>
        </el-form>
    </div>

</template>

<script>
    import configDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import rbdeviceStatisticServiceFactory from 'src/services/cmdb/rb-deviceStatistic-services.factory.js'

    export default {
        data() {
            return {
                biz_systemList: [],
                idc_typeList: [],
                pod_nameList: [],
                biz_system: '',
                idc_type: '',
                pod_name: '',
                tableData: [],
                currentPage: '',
                loading: false,
                loading_text: '正在查询数据,请稍等...',
                frameDatas: {
                    show: true,
                    // 默认值(支持整个对象,或id、name、key等字符串)
                    select: '',
                    // 当前code对象
                    codeObj: {'codeId': '1923bf6f568011e998180242ac110001',
                        'filedCode': 'department1',
                        'filedName': '一级部门',
                        'moduleCatalogId': '96603733-5793-11ea-ab96-fa163e982f89'},
                    // 级联的父级code对象
                    parentCodeObj: {'codeId': '1923bf6f568011e998180242ac110001',
                        'filedCode': 'department1',
                        'filedName': '一级部门',
                        'moduleCatalogId': '96603733-5793-11ea-ab96-fa163e982f89'},

                    // 级联的父级下拉框数据--选中的对象
                    parentSelectObj: ''
                },
                // 数据框配置
                frameOptions: {
                    type: 'select',// ['input','select','date','datetime']
                    // disabled: false,// 可置灰
                    // clearable: true,// 可清除
                    // filterable: true,// 可搜索
                    style: {// 样式类
                        width: '300px'
                    }
                }
            }
        },
        mounted() {
            this.getbizSystemList()
            this.getidcTypeList()
            // this.getpodNameList()
            this.reloadTable()
        },
        methods: {
            getSummaries(param) {
                const { columns, data } = param
                const sums = []
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '合计'
                        return
                    }
                    const values = data.map(item => Number(item[column.property]))
                    if (!values.some(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr)
                            if (!isNaN(value)) {
                                return prev + curr
                            } else {
                                return prev
                            }
                        }, 0)
                    } else {
                        sums[index] = ''
                    }
                })
                return sums
            },
            reloadTable(num) {
                if (num !== 1) {
                    // 搜索前将当前页置为1
                    this.currentPage = 1
                }
                this.getPageData()
            },
            getPageData() {
                this.loading = true
                var obj = {
                    'biz_system': this.biz_system,
                    'idc_type': this.idc_type,
                    'pod_name': this.pod_name
                }
                rbdeviceStatisticServiceFactory.selectDeviceBybizSystem(obj).then((res) => {
                    this.tableData = res
                }).catch(() => {
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            resetSearch() {
                this.biz_system = ''
                this.idc_type = ''
                this.pod_name = ''
                this.reloadTable()
            },
            exportExcelData() {
                var obj = {
                    'biz_system': this.biz_system,
                    'idc_type': this.idc_type,
                    'pod_name': this.pod_name
                }
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                rbdeviceStatisticServiceFactory.downloadDeviceBybizSystem(obj).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    // window.location.href = objectUrl
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '各业务系统分类数量统计.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            getbizSystemList() {
                configDictServiceFactory.getBizSystem().then((res) => {
                    this.biz_systemList = res
                })
                // var obj = {
                //   type: 'bizSystem'
                // }
                // configDictServiceFactory.getDictsByType(obj).then((res) => {
                //   this.biz_systemList = res
                // })
            },
            getidcTypeList() {
                var obj = {
                    type: 'idcType'
                }
                configDictServiceFactory.getDictsByType(obj).then((res) => {
                    this.idc_typeList = res
                })
            },
            getpodNameList() {
                var obj = {
                    type: 'pod_name',
                    pid: this.idc_type
                }
                configDictServiceFactory.getDictsByType(obj).then((res) => {
                    this.pod_nameList = res
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
