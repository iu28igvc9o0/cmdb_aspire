<template>
    <div class="components-container yw-dashboard"
         v-loading="loading"
         :element-loading-text="loading_text">

        <el-card class="box-card">
            <div slot="header">
                <span style="font-weight: bold">资源池-设备统计</span>
                <div class="clearfix fr"
                     style="margin: 0px 10px">
                    <el-button type="text"
                               icon="el-icon-download"
                               @click="downloadIdcType">下载</el-button>
                </div>
                <div class="clearfix fr">
                    <el-button type="text"
                               icon="el-icon-upload2"
                               @click="exportExcelData('selectIdcTypeStatistic')">导出</el-button>
                </div>
            </div>
            <el-form class="yw-form">
                <div class="yw-el-table-wrap">
                    <el-table :data="idcData"
                              class="yw-el-table"
                              ref="multipleTable"
                              stripe
                              tooltip-effect="dark"
                              show-summary
                              :summary-method="getSummaries"
                              @selection-change="handleSelectionChange"
                              border>
                        <el-table-column type="selection"
                                         width="55"></el-table-column>
                        <el-table-column label="资源池"
                                         min-width="300px"
                                         prop="idcType">
                            <template slot-scope="scope">
                                <el-button style="text-decoration:none"
                                           type="text"
                                           align="left"
                                           @click="setIdcType(scope.row)">{{scope.row.idcType}}</el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="物理机设备数量"
                                         prop="phyNumber"></el-table-column>
                        <el-table-column label="虚拟机设备数量"
                                         prop="vmNumber"></el-table-column>
                        <el-table-column label="网络设备数量"
                                         prop="networkNumber"></el-table-column>
                        <el-table-column label="安全设备数量"
                                         prop="safeNumber"></el-table-column>
                    </el-table>
                </div>
            </el-form>
        </el-card>
        <br />
        <el-card class="box-card"
                 v-show="selectIdcType !== ''">
            <div slot="header">
                <span style="font-weight: bold">{{selectIdcType}}-工期设备统计</span>
                <div class="clearfix fr">
                    <el-button type="text"
                               icon="el-icon-upload2"
                               @click="exportExcelData('selectProjectStatistic')">导出</el-button>
                </div>
            </div>
            <el-form class="yw-form">
                <div class="yw-el-table-wrap">
                    <el-table :data="projectData"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              show-summary
                              :summary-method="getSummaries"
                              border>
                        <el-table-column label="工期"
                                         prop="projectName">
                            <template slot-scope="scope">
                                <el-button style="text-decoration:none"
                                           type="text"
                                           align="left"
                                           @click="setProject(scope.row)">{{scope.row.projectName}}</el-button>
                            </template>
                        </el-table-column>
                        <el-table-column label="物理机设备数量"
                                         prop="phyNumber"
                                         width="150"></el-table-column>
                        <el-table-column label="虚拟机设备数量"
                                         prop="vmNumber"
                                         width="150"></el-table-column>
                        <el-table-column label="网络设备数量"
                                         prop="networkNumber"
                                         width="150"></el-table-column>
                        <el-table-column label="安全设备数量"
                                         width="150"
                                         prop="safeNumber"></el-table-column>
                    </el-table>
                </div>
            </el-form>
        </el-card>
        <br />
        <el-card class="box-card"
                 v-show="selectProject !== ''">
            <div slot="header">
                <span style="font-weight: bold">{{selectIdcType}}-{{selectProject}}-POD池设备统计</span>
                <div class="clearfix fr">
                    <el-button type="text"
                               icon="el-icon-upload2"
                               @click="exportExcelData('selectPodStatistic')">导出</el-button>
                </div>
            </div>
            <el-form class="yw-form"
                     v-show="selectProject !== ''">
                <div class="yw-el-table-wrap">
                    <el-table :data="podData"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              show-summary
                              :summary-method="getSummaries"
                              border>
                        <el-table-column label="POD池"
                                         prop="podName"></el-table-column>
                        <el-table-column label="物理机设备数量"
                                         prop="phyNumber"
                                         width="150"></el-table-column>
                        <el-table-column label="虚拟机设备数量"
                                         prop="vmNumber"
                                         width="150"></el-table-column>
                        <el-table-column label="网络设备数量"
                                         prop="networkNumber"
                                         width="150"></el-table-column>
                        <el-table-column label="安全设备数量"
                                         width="150"
                                         prop="safeNumber"></el-table-column>
                    </el-table>
                </div>
            </el-form>
        </el-card>
    </div>
</template>

<script>
    import rbdeviceStatisticServiceFactory from 'src/services/cmdb/rb-deviceStatistic-services.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        data() {
            return {
                selectIdcType: '',
                selectIdcTypeId: '',
                selectProject: '',
                selectProjectId: '',
                idcData: [],
                projectData: [],
                podData: [],
                loading: false,
                loading_text: '正在查询数据,请稍等...',
                multipleSelection: []
            }
        },
        watch: {
            selectIdcType(val) {
                if (val !== '') {
                    this.selectProject = ''
                    this.projectData = []
                    this.podData = []
                    this.queryProjectStatistic(this.selectIdcTypeId)
                }
            },
            selectProject(val) {
                if (val !== '') {
                    this.podData = []
                    this.queryPodStatistic(this.selectIdcTypeId, this.selectProjectId)
                }
            }
        },
        mounted() {
            this.queryIdcStatistic()
        },
        methods: {
            downloadIdcType() {
                if (this.multipleSelection.length == 0) {
                    this.$message.error('请选择需要下载的资源池')
                    return
                }
                this.loading = true
                this.loading_text = '正在下载数据,请稍等...'
                // 格式化请求参数
                var data = []
                this.multipleSelection.forEach(item => {
                    data.push(item.idcTypeId)
                })
                rbdeviceStatisticServiceFactory.downloadMultiProjectStatistic(data).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '各资源池的工期统计信息.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                    this.$refs.multipleTable.clearSelection()
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            // 处理多选
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
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
            setIdcType(row) {
                this.selectIdcType = row.idcType
                this.selectIdcTypeId = row.idcTypeId
            },
            setProject(row) {
                this.selectProject = row.projectName
                this.selectProjectId = row.projectNameId
            },
            queryIdcStatistic() {
                this.loading = true
                this.idcData = []
                rbdeviceStatisticServiceFactory.selectIdcStatistic().then((res) => {
                    this.idcData = res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            queryProjectStatistic(idcType) {
                this.loading = true
                this.projectData = []
                rbdeviceStatisticServiceFactory.selectProjectStatistic({ idcType: idcType }).then((res) => {
                    this.projectData = res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            queryPodStatistic(idcType, projectName) {
                this.loading = true
                this.podData = []
                rbdeviceStatisticServiceFactory.selectPodStatistic({ idcType: idcType, projectName: projectName }).then((res) => {
                    this.podData = res
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            },
            exportExcelData(exportType) {
                this.loading = true
                this.loading_text = '正在导出数据,请稍等...'
                const params = {}
                if (exportType === 'selectIdcTypeStatistic') {
                    params.fileName = '资源池-设备统计'
                    params.header = ['资源池', '物理机设备数量', '虚拟机设备数量', '网络设备数量', '安全设备数量']
                    params.header_key = ['idcType', 'phyNumber', 'vmNumber', 'networkNumber', 'safeNumber']
                }
                if (exportType === 'selectProjectStatistic') {
                    params.fileName = this.selectIdcType + '-工期统计'
                    params.idcType = this.selectIdcTypeId
                    params.header = ['工期', '物理机设备数量', '虚拟机设备数量', '网络设备数量', '安全设备数量']
                    params.header_key = ['projectName', 'phyNumber', 'vmNumber', 'networkNumber', 'safeNumber']
                }
                if (exportType === 'selectPodStatistic') {
                    params.fileName = this.selectIdcType + '-' + this.selectProject + '-POD池统计'
                    params.idcType = this.selectIdcTypeId
                    params.projectName = this.selectProjectId
                    params.header = ['POD池', '物理机设备数量', '虚拟机设备数量', '网络设备数量', '安全设备数量']
                    params.header_key = ['podName', 'phyNumber', 'vmNumber', 'networkNumber', 'safeNumber']
                }
                rbCmdbServiceFactory.downloadReport(exportType, params).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    // window.location.href = objectUrl
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = params.fileName + '.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                    this.loading_text = '正在查询数据,请稍等...'
                })
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
