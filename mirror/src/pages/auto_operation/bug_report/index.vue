<!--  -->
<template>
    <div class="preview">

        <asp-smart-table ref="aspSmartTable"
                         :tableJson="tableJson"
                         v-model="model"
                         :beforeHttp="beforeHttp"
                         :afterHttp="afterHttp"
                         :beforeTableRender="beforeTableRender"
                         :before-button="beforeButton"
                         @on="onbind">
        </asp-smart-table>
        <el-dialog width="1037px"
                   class="yw-dialog"
                   title="报告详情"
                   v-if="reportShow"
                   :visible.sync="reportShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-table ref="reportDetailTable"
                          :data="reportDetailList"
                          class="yw-el-table mtop10"
                          stripe
                          border
                          width="100%"
                          height="200"
                          v-loading="loading">
                    <el-table-column label="漏洞级别"
                                     prop="riskLevel">
                        <template slot-scope="scope">
                            <span v-if="scope.row.riskLevel==='1'">高危险</span>
                            <span v-if="scope.row.riskLevel==='2'">中危险</span>
                            <span v-if="scope.row.riskLevel==='3'">低危险</span>
                            <span v-if="scope.row.riskLevel==='4'">信息</span>
                            <span v-if="scope.row.riskLevel==='99'">未知</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="已修复漏洞数量"
                                     prop="processedNum">
                    </el-table-column>
                    <el-table-column label="未修复漏洞数量"
                                     prop="waitRepairNum">
                    </el-table-column>
                    <el-table-column label="无法修复漏洞数量"
                                     prop="beyondRepairNum">
                    </el-table-column>
                    <el-table-column label="无需修复漏洞数量"
                                     prop="noNeedNum">
                    </el-table-column>
                </el-table>
                <el-table ref="reportDetailTable"
                          :data="reportItemList"
                          class="yw-el-table mtop10"
                          stripe
                          border
                          width="100%"
                          height="200"
                          v-loading="loading">
                    <el-table-column label="IP地址"
                                     prop="ip">
                        <template slot-scope="scope">
                            <a @click="downloadIp(scope.row)">{{scope.row.ip}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="高危漏洞数量"
                                     prop="highNum">
                    </el-table-column>
                    <el-table-column label="中危漏洞数量"
                                     prop="midNum">
                    </el-table-column>
                    <el-table-column label="低危漏洞数量"
                                     prop="lowNum">
                    </el-table-column>
                </el-table>
                <el-pagination @current-change="handleCurrentChange"
                               :page-size="10"
                               layout="total, prev, pager, next"
                               :total="total">
                </el-pagination>
            </section>
        </el-dialog>

        <!-- 导入漏洞报告 -->
        <el-dialog width="50%"
                   class="yw-dialog"
                   title="导入漏洞报告"
                   :visible.sync="uploadBoxShow"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <import-bug-report @uploadSuccess="uploadSuccess"></import-bug-report>
        </el-dialog>

    </div>
</template>

<script>
    import tableJson from './smart_data/reportList.json'
    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import cmdbServiceFactory from 'src/services/cmdb/rb-demand-service.factory.js'
    import importBugReport from 'src/pages/auto_operation/bug_manage/import-bug-report.vue'

    import { createDownloadFile } from 'src/utils/utils.js'

    export default {
        data() {
            return {
                myHeader: { 'Content-Type': 'text/plain;charset=UTF-8' },
                tableJson: tableJson,
                model: tableJson.model,
                reportShow: false,
                uploadBoxShow: false,
                formSearch: {},
                total: 0,
                params: {
                    pageNo: 1,
                    pageSize: 10
                },
                riskLevelList: [],// 漏洞等级列表
                department1List: [],// 一级部门列表
                department2List: [],
                department1Index: [],
                reportItemList: [],
                reportDetailList: [],
                idcTypeList: [],
                bizSystemList: [],
                scanCycleList: [],
                filterBiz: [],// 过滤一级部门业务系统
                filterBizSys: []// 过滤二级部门业务系统
            }
        },
        components: { importBugReport },
        mixins: [YwCodeFrameOption],
        computed: {
            // 一级部门列表
            department1ListStore() {
                return this.$store.state.autoOperation.department1List
            },
            // 业务系统列表
            bizSystemListStore() {
                return this.$store.state.autoOperation.bizSystemList
            },
        },
        created() {
            console.log('this.model', this.model)
            this.init()
        },
        methods: {
            async init() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                this.idcTypeList = this.conditionList.filter(item => {
                    return item.key === 'idcType'
                })
                this.getRefModuleDict(this.idcTypeList[0].frameDatas.codeObj.codeId, 'idcType')
                this.getOsType()
            },
            // 引用模型
            getRefModuleDict(item, type) {
                let params = {
                    codeId: item
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    if (type === 'idcType') {
                        console.log('res===', res)

                        this.$refs.aspSmartTable.asp_setOptions('poolName',
                            res)
                    }
                    // return res
                })
            },
            getOsType() {
                let params = {
                    codeId: '1923c442568011e998180242ac110001'
                }
                rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    this.$refs.aspSmartTable.asp_setOptions('osType',
                        res)
                })
            },

            // 查询漏洞等级列表
            queryRiskLevelList() {
                return bugManageService
                    .queryRiskLevelList()
                    .then(res => {
                        this.riskLevelList = res
                        this.riskLevelList.forEach(item => {
                            for (const key in item) {
                                item.id = key
                                item.name = item[key]
                            }
                        })
                        this.$refs.aspSmartTable.asp_setOptions('riskLevel',
                            this.riskLevelList)
                        this.$store.commit('setRiskLevelList', this.riskLevelList)
                    })
            },
            // 查询一级部门
            queryDepartment1() {
                let req = {
                    type: 'department1'
                }
                return cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department1List = res
                        this.$refs.aspSmartTable.asp_setOptions('department1',
                            res)
                        this.$store.commit('setDepartment1List', res)
                    })
            },
            // 查询二级部门
            queryDepartment2(index) {
                let req = {
                    type: 'department2',
                    pid: this.department1List[index].id
                }
                cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department2List = this.department2List.concat(res)
                        this.$refs.aspSmartTable.asp_setOptions('department2',
                            this.department2List)
                    })
            },
            // 查询业务系统 moduleType=default_business_system_module_id
            queryBizSystemList() {
                let req = {
                    params: {
                        moduleType: 'default_business_system_module_id'
                    }
                }
                return cmdbService
                    .getRefModuleData(req)
                    .then(res => {
                        this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                            res)
                        this.bizSystemList = []
                        this.bizSystemList = res
                        this.$store.commit('setBizSystemList', res)
                    })
            },
            // 获取业务系统
            getBizSysList($event) {
                this.demand.bizSystem = ''
                let pid = $event.id
                let obj = {
                    'type': 'bizSystem',
                    'pid': pid,
                    'pValue': '',
                    'pType': ''
                }
                cmdbServiceFactory.getConfigDictByType(obj).then((res) => {
                    if (res) {
                        this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                            res)
                        this.$store.commit('setBizSystemList', res)
                    }
                })
            },
            // 查询分组列表
            queryVulnerabilityGroupList() {
                let req = {
                    page_no: 1,
                    page_size: 200
                }
                return bugManageService
                    .queryVulnerabilityGroupList(req)
                    .then(res => {
                        this.$refs.aspSmartTable.asp_setOptions('groupId',
                            res)
                        this.$store.commit('setGroupList', res)
                    })
            },
            // 获取扫描周期列表
            getScanCycleList() {
                bugManageService
                    .getScanCycleList()
                    .then(res => {
                        this.scanCycleList = res
                        // this.model.scanCycle.push(this.scanCycleList[0].scanCycle)
                        this.$refs.aspSmartTable.asp_setOptions('scanCycle',
                            res)
                        if (this.model.scanCycle === '') {
                            this.model.scanCycle = this.scanCycleList[0].scanCycle
                            // params.scanCycle = nowDate || ''
                        }

                        this.$store.commit('setScanCycleList', res)
                    })
            },
            downloadIp(row) {
                let path = '/targetVul/' + this.formSearch.scanCycle + '/' + row.poolCode + '/' + row.ip + '/' + row.ip + '.html'
                window.open(path)
                // this.$message.success('请求成功，下载中…')
                // let downLoadElement = document.createElement('a')
                // downLoadElement.href = path
                // downLoadElement.setAttribute('download', '故障报告.zip')
                // document.body.appendChild(downLoadElement)
                // downLoadElement.click()
                // document.body.removeChild(downLoadElement)
            },
            uploadSuccess() {
                this.uploadBoxShow = false
                // this.search()
            },
            handleCurrentChange(val) {
                this.params.pageNo = val
                Object.assign(this.formSearch, this.params)
                this.getVulReportItemList(this.formSearch)
            },
            getVulReportItemList(obj) {
                bugManageService.queryVulReportItemList(obj).then(res => {
                    this.reportItemList = []
                    this.reportItemList = res.dataList
                    this.total = res.totalCount
                })
            },
            changeSearch(data) {
                this.formSearch.bizSystem = ''
                this.formSearch.bizSystem = data.row.bizSystem
                this.formSearch.bizSystem = this.formSearch.bizSystem.toString()
                this.formSearch.department1 = this.formSearch.department1.toString()
                this.formSearch.department2 = this.formSearch.department2.toString()
                this.formSearch.groupId = this.formSearch.groupId.toString()
                this.formSearch.poolName = this.formSearch.poolName.toString()
                this.formSearch.protocol = this.formSearch.protocol.toString()
                this.formSearch.riskLevel = this.formSearch.riskLevel.toString()
                // this.formSearch.scanCycle = this.formSearch.scanCycle.toString()
                this.formSearch.status = this.formSearch.status.toString()
                this.formSearch.statisticalDimension = this.formSearch.statisticalDimension.toString()

            },
            onbind(data) {
                this.formSearch = this.$utils.deepClone(data.model)
                switch (data.item.columnName) {
                    case 'department1':
                        {
                            this.department1Index = []
                            this.department2List = []
                            this.filterBiz = []
                            let deepBiz = JSON.parse(JSON.stringify(this.bizSystemListStore))
                            data.model.department2 = ''
                            this.$refs.aspSmartTable.asp_setOptions('department2',
                                this.department2List)
                            this.department1List.forEach((item, index) => {
                                data.model.department1.forEach(items => {
                                    if (item.name === items) {
                                        this.department1Index.push(index)
                                    }
                                })
                                // if (item.name === data.model.department1) {
                                //     this.department1Index = index
                                //     data.model.department2 = ''
                                //     this.queryDepartment2()
                                // }
                            })
                            data.model.department1.forEach(val => {
                                let filVal = deepBiz.filter(bizVal => {
                                    return bizVal.department1 === val
                                })

                                this.filterBiz = this.filterBiz.concat(filVal)
                            })
                            this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                                this.filterBiz)
                            this.department1Index.forEach(items => {
                                this.queryDepartment2(items)
                            })
                            break
                        }
                    case 'department2':
                        {
                            this.filterBizSys = []
                            // if (data.model.department2 === '' && !this.filterBizSys) {
                            //     this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                            //         this.filterBiz)
                            // }
                            let deepBizSys = JSON.parse(JSON.stringify(this.filterBiz))
                            console.log(deepBizSys)
                            data.model.department2.forEach(val => {
                                let filVals = deepBizSys.filter(bizVals => {
                                    return bizVals.department2 === val
                                })
                                this.filterBizSys = this.filterBizSys.concat(filVals)
                            })
                            this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                                this.filterBizSys)
                            break
                        }
                    case 'reportDetail': {
                        Object.assign(this.formSearch, this.params)
                        this.changeSearch(data)
                        bugManageService.queryVulReportDetailStatistics(this.formSearch).then(res => {
                            this.reportDetailList = []
                            this.reportDetailList = res
                        })
                        this.getVulReportItemList(this.formSearch)
                        this.reportShow = true
                        break
                    }
                    case 'import-report': {
                        this.uploadBoxShow = true
                        break
                    }
                    case 'export-report': {
                        this.formSearch.bizSystem = this.formSearch.bizSystem.toString()
                        this.formSearch.department1 = this.formSearch.department1.toString()
                        this.formSearch.department2 = this.formSearch.department2.toString()
                        this.formSearch.groupId = this.formSearch.groupId.toString()
                        this.formSearch.poolName = this.formSearch.poolName.toString()
                        this.formSearch.protocol = this.formSearch.protocol.toString()
                        this.formSearch.riskLevel = this.formSearch.riskLevel.toString()
                        // this.formSearch.scanCycle = this.formSearch.scanCycle.toString()
                        this.formSearch.status = this.formSearch.status.toString()
                        this.formSearch.statisticalDimension = this.formSearch.statisticalDimension.toString()
                        bugManageService.exportVulNewReport(this.formSearch).then(res => {
                            if (res) {
                                this.$message.success('下载成功')
                                createDownloadFile(res, '漏洞报表列表.xls')
                            }
                        })
                        break
                    }
                    case 'reportZip': {
                        this.changeSearch(data)
                        bugManageService.downloadSourceReport(this.formSearch).then(res => {
                            if (res) {
                                this.$message.success('开始下载')
                                let fileName = data.row.bizSystem + '.zip'
                                // this.$utils.createDownloadFileBlob(res, fileName)
                                let blob = new Blob([res], { type: 'application/octet-stream;charset=UTF-8' })
                                let objectUrl = URL.createObjectURL(blob)
                                let downLoadElement = document.createElement('a')
                                downLoadElement.href = objectUrl
                                downLoadElement.download = fileName
                                document.body.appendChild(downLoadElement)
                                downLoadElement.click()
                                document.body.removeChild(downLoadElement)

                            }
                            // this.reportItemList = []
                            // this.reportItemList = res.dataList
                        })
                        break
                    }
                }
            },
            beforeHttp({ tableItem, params, httpMethod, row }) {
                this.queryRiskLevelList()
                this.queryBizSystemList()
                this.queryDepartment1()
                this.queryVulnerabilityGroupList()
                // this.getScanCycleList()
                // var date = new Date()
                // let yy = date.getFullYear()
                // let mm = date.getMonth() + 1
                // let nowDate = ''
                // if (mm > 10) {
                //     nowDate = yy + '' + mm
                // } else {
                //     nowDate = yy + '0' + mm
                // }

                switch (tableItem.columnName) {
                    case 'table_report': {
                        console.log('表格请求')
                        if (this.model.scanCycle === '' && this.scanCycleList.length > 0) {
                            this.model.scanCycle = this.scanCycleList[0].scanCycle || ''
                            params.scanCycle = this.scanCycleList[0].scanCycle || ''
                        }

                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows,
                            bizSystem: params.bizSystem.toString(),
                            department1: params.department1.toString(),
                            department2: params.department2.toString(),
                            groupId: params.groupId.toString(),
                            poolName: params.poolName.toString(),
                            protocol: params.protocol.toString(),
                            riskLevel: params.riskLevel.toString(),
                            // scanCycle: params.scanCycle.toString(),
                            status: params.status.toString(),
                            statisticalDimension: params.statisticalDimension.toString(),
                        })
                        break
                    }
                }
            },
            afterHttp({ tableItem, responseBody }) {
                switch (tableItem.columnName) {
                    case 'scanCycle':
                        this.$utils.smartTableSelectDataFormat(responseBody)
                        this.scanCycleList = responseBody
                        if (this.model.scanCycle === '' && responseBody.length > 0) {
                            this.model.scanCycle = responseBody[0].scanCycle
                        }
                        this.$refs.aspSmartTable.asp_refreshTableList('table_report')
                        break
                    default:
                        this.$utils.smartTableDataFormat(tableItem, responseBody)
                }
            },
            beforeButton({ item, rowObj, next }) {
                next(item, rowObj)
            },
            beforeTableRender({ tableName, tableData, columnItem, scope }, callBack) {
                if (scope.row.bizSystem !== null) {
                    callBack(tableName, 'reportDetail', [], { content: '<span>详情</span>' })
                    callBack(tableName, 'reportZip', [], { content: `<span>${scope.row.bizSystem}.zip</span>` })
                }
                // callBack(tableName, 'status', [], { content: `<img src="/static/img/sms/${img}">` })

            },


        },
        watch: {
            'model.department1'(val) {
                console.log('this.model.department1', val)
                if (val && val.length > 0) {
                    this.filterBiz = []
                    this.bizSystemList = []
                    let deepBiz = JSON.parse(JSON.stringify(this.bizSystemListStore))
                    this.model.department1.forEach(val => {
                        let filVal = deepBiz.filter(bizVal => {
                            return bizVal.department1 === val
                        })
                        this.filterBiz = this.filterBiz.concat(filVal)
                    })
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.filterBiz)
                } else {
                    console.log('this.model', this.model)
                    this.model.bizSystem = []
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.bizSystemListStore)
                }
            },
            'model.department2'(val) {
                if (this.model.department1.length > 0 && val.length === 0) {
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.filterBiz)
                } else if (this.model.department1.length === 0 && val.length === 0) {
                    this.$refs.aspSmartTable.asp_setOptions('bizSystem',
                        this.bizSystemListStore)
                }
            },
            'model.statisticalDimension'(newVal, oldVal) {
                if (newVal) {
                    this.$refs.aspSmartTable.asp_sendTableQuery('table_report')
                }
            }
        }
    }

</script>
<style lang='scss' scoped>
    /deep/ .el-tag:nth-of-type(1).el-tag--info {
        width: 68px !important;
        text-overflow: ellipsis !important; /*让截断的文字显示为点点。还有一个值是clip意截断不显示点点*/
        white-space: nowrap !important; /*让文字不换行*/
        overflow: hidden !important; /*超出要隐藏*/
    }

    .slot-box {
        position: relative;
        right: 510%;
    }
</style>