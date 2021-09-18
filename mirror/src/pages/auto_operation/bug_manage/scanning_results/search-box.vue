<!-- 漏洞管理：漏洞扫描结果/漏洞扫描报告/漏洞设备列表  共用查询条件 -->
<template>
    <el-form class="components-condition yw-form"
             :model="formSearch"
             @keyup.enter.native="search(1)"
             ref="formSearch"
             :inline="true"
             label-width="75px">
        <el-form-item label="漏洞名称"
                      v-if="isScanningResult"
                      prop="name"
                      label-width="75px">
            <el-input v-model="formSearch.name"
                      placeholder="请输入漏洞名称"
                      clearable></el-input>
        </el-form-item>
        <el-form-item label="是否可修复"
                      prop="canFixed">
            <el-select v-model="formSearch.canFixed"
                       clearable>
                <el-option label="是"
                           value="Y"></el-option>
                <el-option label="否"
                           value="N"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="危险等级"
                      prop="riskLevel">
            <el-select v-model="formSearch.riskLevel"
                       multiple
                       collapse-tags
                       clearable>
                <el-option v-for="item in riskLevelList"
                           :key="item.id"
                           :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="分组名称"
                      prop="groupId"
                      v-if="isScanningResult">
            <el-select v-model="formSearch.groupId"
                       clearable>
                <el-option v-for="item in groupList"
                           :key="item.vulnerability_group_id"
                           :label="item.name"
                           :value="item.vulnerability_group_id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="漏洞编号"
                      prop="id"
                      label-width="75px"
                      v-if="isScanningResult">
            <el-input v-model="formSearch.id"
                      placeholder="请输入漏洞编号"
                      clearable></el-input>
        </el-form-item>
        <el-form-item label="协议"
                      prop="protocol">
            <el-select v-model="formSearch.protocol"
                       clearable>
                <el-option v-for="item in protocolList"
                           :key="item.key"
                           :label="item.value"
                           :value="item.value"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="服务"
                      prop="service"
                      label-width="75px">
            <el-input v-model="formSearch.service"
                      placeholder="请输入服务"
                      clearable></el-input>
        </el-form-item>
        <el-form-item label="IP"
                      v-if="isScanningResult"
                      prop="hostIp"
                      label-width="75px">
            <el-input v-model="formSearch.hostIp"
                      placeholder="请输入IP"
                      clearable></el-input>
        </el-form-item>
        <el-form-item label="资源池">
            <el-select v-model="formSearch.poolName"
                       multiple
                       collapse-tags
                       filterable
                       clearable>
                <el-option v-for="item in idcTypeOption"
                           :key="item.id"
                           :label="item.key"
                           :value="item.value"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="一级部门"
                      prop="department1Index">
            <el-select v-model="formSearch.department1"
                       filterable
                       multiple
                       collapse-tags
                       @change="queryDepartment2"
                       clearable>
                <!-- <el-option v-for="(item, department1Index) in department1List"
                           :key="item.id"
                           :label="item.name"
                           :value="department1Index"></el-option> -->
                <el-option v-for="(item) in department1List"
                           :key="item.id"
                           :label="item.name"
                           :value="item.name"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="二级部门"
                      prop="department2">
            <el-select v-model="formSearch.department2"
                       filterable
                       multiple
                       collapse-tags
                       :placeholder="holderTips"
                       @change="selectSysteimList"
                       clearable>
                <el-option v-for="item in department2List"
                           :key="item.id"
                           :label="item.name"
                           :value="item.name"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="业务系统"
                      prop="bizSystem">
            <el-select v-model="formSearch.bizSystem"
                       multiple
                       collapse-tags
                       filterable
                       clearable>
                <el-option v-for="item in bizSystemList"
                           :key="item.id"
                           :label="item.bizSystem"
                           :value="item.bizSystem"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="操作系统"
                      prop="osType">
            <YwCodeFrame v-if="osTypeShow"
                         :frameDatas="frameDatas"
                         :frameOptions="frameDatas.frameOptions"
                         @changeSelect="changeSelect"></YwCodeFrame>
        </el-form-item>
        <el-form-item label="扫描周期"
                      prop="scanCycle">
            <el-select v-model="formSearch.scanCycle"
                       clearable>
                <el-option v-for="item in scanCycleList"
                           :key="item.scanCycle"
                           :label="item.scanCycle"
                           :value="item.scanCycle"></el-option>
            </el-select>
        </el-form-item>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="search(1)">查询</el-button>
            <el-button @click="reset()">重置</el-button>
        </section>
    </el-form>
</template>

<script>
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import YwCodeFrame from 'src/components/common/yw-code-frame/yw-code-frame.vue'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'

    export default {
        name: 'ScanningResultsIndex',
        mixins: [YwCodeFrameOption],
        props: {
            currentRowData: {
                type: Object,
                default() {
                    return null
                }
            },
            currentProp: {
                type: String,
                default() {
                    return ''
                }
            },
            isScanningResult: {
                type: Boolean,
                default() {
                    return false
                }
            },
            scanCycleNum: {
                type: String,
                default() {
                    return ''
                }

            }
        },
        data() {
            return {
                idcTypeOption: [],
                formSearch: {
                    poolName: '',
                    name: '',
                    canFixed: '',
                    riskLevel: '',
                    id: '',
                    protocol: '',
                    service: '',
                    groupId: '',
                    department1Index: '',
                    department1: '',
                    department2: '',
                    bizSystem: '',
                    osType: '',
                    scanCycle: '',
                },
                protocolList: [{    // 协议列表
                    key: '1',
                    value: 'TCP'
                }, {
                    key: '2',
                    value: 'UDP'
                }, {
                    key: '3',
                    value: '其他协议'
                }],
                scanCycleList: [], // 扫描周期列表
                riskLevelList: [], // 漏洞等级列表
                department1List: [], // 一级部门列表
                department2List: [], // 二级部门列表
                bizSystemList: [], // 业务系统列表
                groupList: [], // 分组列表
                osTypeList: [], // 操作系统列表

                frameDatas: {
                    select: '',
                    codeObj: {
                        filedCode: 'device_os_type',
                    },
                    frameOptions: {
                        type: 'select'
                    }
                },
                osTypeShow: true,
                propsObj: {
                    highNum: '1',
                    midNum: '2',
                    lowNum: '3',
                    processedNum: 'PROCESSED',
                    beyondRepairNum: 'BEYOND_REPAIR',
                    waitRepairNum: 'WAIT_REPAIR',
                },

                // 多选过滤
                department1Index: [],
                filterBiz: [],// 过滤一级部门业务系统
                filterBizSys: []// 过滤二级部门业务系统

            }
        },
        components: {
            YwCodeFrame
        },
        watch: {
            'formSearch.department1Index'(val) {
                let hasSelectedDepartment1 = (typeof val) === 'number'
                if (hasSelectedDepartment1) {
                    this.formSearch.department1 = this.department1List[val].name
                } else {
                    this.formSearch.department1 = ''
                }
            },
            'formSearch.department1'(val) {
                if (val && val.length > 0) {
                    this.filterBiz = []
                    this.bizSystemList = []
                    let deepBiz = JSON.parse(JSON.stringify(this.bizSystemListStore))
                    this.formSearch.department1.forEach(val => {
                        let filVal = deepBiz.filter(bizVal => {
                            return bizVal.department1 === val
                        })
                        this.filterBiz = this.filterBiz.concat(filVal)
                    })
                    this.bizSystemList = this.filterBiz
                } else {
                    this.bizSystemList = []
                    this.bizSystemList = this.bizSystemListStore
                }
            },
            'formSearch.department2'(val) {
                if (this.formSearch.department1.length > 0 && val.length === 0) {
                    this.bizSystemList = []
                    this.bizSystemList = this.filterBiz
                } else if (this.formSearch.department1.length === 0 && val.length === 0) {
                    this.formSearch.bizSystem = []
                    this.bizSystemList = []
                    this.bizSystemList = this.bizSystemListStore
                }
            },
            // 漏洞扫描报告：初始化默认搜索条件
            department1List: {
                handler(newVal) {
                    if (newVal.length && this.currentProp) {
                        this.formSearch.groupId = this.currentRowData.groupId !== null ? this.currentRowData.groupId * 1 : null
                        this.department1List.forEach((item, index) => {
                            if (item.name === this.currentRowData.department1) {
                                this.formSearch.department1Index = index
                            }
                        })
                        this.formSearch.bizSystem = this.currentRowData.bizSystem

                        let curKey = ['highNum', 'midNum', 'lowNum'].includes(this.currentProp) ? 'riskLevel' : 'status'
                        this.formSearch[curKey] = this.propsObj[this.currentProp]
                    }
                },
                deep: true,
                immediate: true
            },
            // 更新搜索条件
            formSearch: {
                handler(newVal) {
                    let copyParams = JSON.parse(JSON.stringify(newVal))
                    copyParams.poolName = copyParams.poolName.toString()
                    copyParams.riskLevel = copyParams.riskLevel.toString()
                    copyParams.department1 = copyParams.department1.toString()
                    copyParams.department2 = copyParams.department2.toString()
                    copyParams.bizSystem = copyParams.bizSystem.toString()
                    this.$emit('updateFormSearch', copyParams)
                },
                deep: true,
                immediate: true
            },
        },
        computed: {
            holderTips() {
                let tips = '请先选择一级部门'
                let hasSelectedDepartment1 = (typeof this.formSearch.department1Index) === 'number'
                if (hasSelectedDepartment1 && !this.department2List.length) {
                    tips = '该一级部门下暂无数据'
                } else if (hasSelectedDepartment1 && this.department2List.length) {
                    tips = '请选择二级部门'
                }
                return tips
            },
            // 扫描周期列表
            scanCycleListStore() {
                return this.$store.state.autoOperation.scanCycleList
            },
            // 漏洞等级列表
            riskLevelListStore() {
                return this.$store.state.autoOperation.riskLevelList
            },
            // 一级部门列表
            department1ListStore() {
                return this.$store.state.autoOperation.department1List
            },
            // 业务系统列表
            bizSystemListStore() {
                return this.$store.state.autoOperation.bizSystemList
            },
            // 分组列表
            groupListStore() {
                return this.$store.state.autoOperation.groupList
            },
        },
        created() {
            this.init()
            this.initAction()
            console.log('scanCycleNum', this.scanCycleNum)
            this.formSearch.scanCycle = this.scanCycleNum
        },
        methods: {
            init() {
                if (this.riskLevelListStore.length) {
                    this.riskLevelList = this.riskLevelListStore
                    this.department1List = this.department1ListStore
                    this.bizSystemList = this.bizSystemListStore
                    this.groupList = this.groupListStore
                    this.scanCycleList = this.scanCycleListStore
                    this.formSearch.scanCycle = this.scanCycleList[0].scanCycle
                    this.$nextTick(() => {
                        this.search(1)
                    })
                } else {
                    Promise.all([
                        this.queryDepartment1(),
                        this.queryBizSystemList(),
                        this.queryRiskLevelList(),
                        this.queryVulnerabilityGroupList(),
                    ]).then(() => {
                        this.getScanCycleList()
                    })
                        .catch((err) => {
                            this.$message(err)
                        })
                }
            },
            async initAction() {
                // 查询级联下拉框字段
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                this.idcTypeList = this.conditionList.filter(item => {
                    return item.key === 'idcType'
                })
                this.getRefModuleDict(this.idcTypeList[0].frameDatas.codeObj.codeId, 'idcType')
            },
            // 引用模型
            getRefModuleDict(item, type) {
                let params = {
                    codeId: item
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    if (type === 'idcType') {
                        this.idcTypeOption = res
                    }
                    // return res
                })
            },

            // 下拉框选中事件
            // eslint-disable-next-line no-unused-vars
            changeSelect(frameDatas, frameOptions, select, codeObj) {
                this.formSearch.osType = this.frameDatas.select = select.value
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
                        this.$store.commit('setDepartment1List', res)
                    })
            },
            // 查询二级部门
            // queryDepartment2() {
            //     let req = {
            //         type: 'department2',
            //         pid: this.department1List[this.formSearch.department1Index].id
            //     }
            //     this.formSearch.department2 = ''
            //     cmdbService
            //         .getDictsByType(req)
            //         .then(res => {
            //             this.department2List = res
            //         })
            // },
            queryDepartment2() {
                this.department1Index = []
                this.department2List = []
                this.filterBiz = []
                let deepBiz = JSON.parse(JSON.stringify(this.bizSystemListStore))
                this.formSearch.department2 = ''
                this.department1List.forEach((item, index) => {
                    this.formSearch.department1.forEach(items => {
                        if (item.name === items) {
                            this.department1Index.push(index)
                        }
                    })
                })
                this.formSearch.department1.forEach(val => {
                    let filVal = deepBiz.filter(bizVal => {
                        return bizVal.department1 === val
                    })
                    this.filterBiz = this.filterBiz.concat(filVal)
                })
                this.bizSystemList = this.filterBiz
                this.department1Index.forEach(items => {
                    this.queryDepartment2List(items)
                })
            },
            selectSysteimList() {
                this.filterBizSys = []
                let deepBizSys = JSON.parse(JSON.stringify(this.filterBiz))
                console.log('this.formSearch.department2', this.formSearch.department2)
                this.formSearch.department2.forEach(val => {
                    console.log(val)
                    let filVals = deepBizSys.filter(bizVals => {
                        return bizVals.department2 === val
                    })
                    this.filterBizSys = this.filterBizSys.concat(filVals)
                })
                this.bizSystemList = this.filterBizSys
            },
            queryDepartment2List(index) {
                let req = {
                    type: 'department2',
                    pid: this.department1List[index].id
                }
                cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department2List = this.department2List.concat(res)
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
                        this.bizSystemList = res
                        this.$store.commit('setBizSystemList', res)
                    })
            },
            // 获取扫描周期列表
            getScanCycleList() {
                bugManageService
                    .getScanCycleList()
                    .then(res => {
                        this.scanCycleList = res
                        if (this.scanCycleList.length) {
                            this.formSearch.scanCycle = this.scanCycleList[0].scanCycle
                        }
                        this.$store.commit('setScanCycleList', res)
                        this.$nextTick(() => {
                            this.search(1)
                        })
                    })
            },
            // 查询分组列表
            queryVulnerabilityGroupList() {
                let req = {
                    page_no: this.currentPage,
                    page_size: 200
                }
                return bugManageService
                    .queryVulnerabilityGroupList(req)
                    .then(res => {
                        this.groupList = res.dataList
                        this.$store.commit('setGroupList', this.groupList)
                    })
            },
            // 查询列表
            search(pageNum) {
                this.$emit('search', pageNum)
            },
            reset() {
                this.$refs['formSearch'].resetFields()
                this.formSearch.osType = ''
                this.frameDatas.select = ''
                this.formSearch.poolName = ''
                this.osTypeShow = false
                this.$nextTick(() => {
                    this.search(1)
                    this.osTypeShow = true
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .el-tag:nth-of-type(1).el-tag--info {
        width: 76px !important;
        text-overflow: ellipsis !important; /*让截断的文字显示为点点。还有一个值是clip意截断不显示点点*/
        white-space: nowrap !important; /*让文字不换行*/
        overflow: hidden !important; /*超出要隐藏*/
    }
</style>
