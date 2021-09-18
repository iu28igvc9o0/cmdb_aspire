<!-- 漏洞管理：漏洞扫描结果/漏洞扫描报告/漏洞设备列表  共用查询条件 -->
<template>
    <div>
        <el-form class="components-condition yw-form"
                 :model="formSearch"
                 label-position="left"
                 @keyup.enter.native="search(1)"
                 ref="formSearch"
                 :inline="true"
                 label-width="75px">
            <el-form-item label="账户名"
                          prop="username"
                          label-width="75px">
                <el-input v-model="formSearch.username"
                          placeholder="请输入账户名"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="IP"
                          prop="agent_ip"
                          label-width="75px">
                <el-input v-model="formSearch.agent_ip"
                          placeholder="请输入漏洞名称"
                          clearable></el-input>
            </el-form-item>
            <el-form-item label="一级部门"
                          prop="department1Index">
                <el-select v-model="formSearch.department1Index"
                           filterable
                           @change="queryDepartment2"
                           clearable>
                    <el-option v-for="(item, department1Index) in department1List"
                               :key="item.id"
                               :label="item.name"
                               :value="department1Index"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="二级部门"
                          prop="department2">
                <el-select v-model="formSearch.department2"
                           filterable
                           :placeholder="holderTips"
                           clearable>
                    <el-option v-for="item in department2List"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="资源池"
                          prop="pool">
                <el-select v-model="formSearch.pool"
                           @change="getIdcType"
                           clearable
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in typeOption"
                               :key="item.value"
                               :label="item.name"
                               :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="业务系统"
                          prop="biz_system">
                <el-select v-model="formSearch.biz_system"
                           filterable
                           clearable>
                    <el-option v-for="item in bizSystemList"
                               :key="item.id"
                               :label="item.bizSystem"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="操作系统"
                          prop="osType">
                <YwCodeFrame v-if="osTypeShow"
                             :frameDatas="frameDatas"
                             :frameOptions="frameDatas.frameOptions"
                             @changeSelect="changeSelect"></YwCodeFrame>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search(1)">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
    </div>
</template>

<script>
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import YwCodeFrame from 'src/components/common/yw-code-frame/yw-code-frame.vue'

    export default {
        name: 'ScanningResultsIndex',
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
        },
        data() {
            return {
                typeOption: [],// 资源池
                formSearch: {
                    agent_ip: '',
                    username: '',
                    pool: '',
                    department1Index: '',
                    is_password_download: true,
                    department1: '',
                    department2: '',
                    biz_system: '',
                    os_type: '',
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
                department1List: [], // 一级部门列表
                department2List: [], // 二级部门列表
                bizSystemList: [], // 业务系统列表
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
                }
            }
        },
        components: {
            YwCodeFrame
        },
        watch: {
            'formSearch.department1Index'(val) {
                let hasSelectedDepartment1 = (typeof val) === 'number'
                if (hasSelectedDepartment1) {
                    this.formSearch.department1 = this.department1List[val].id
                } else {
                    this.formSearch.department1 = ''
                }
            },
            // 漏洞扫描报告：初始化默认搜索条件
            /* department1List: {
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
            }, */
            // 更新搜索条件
            formSearch: {
                handler(newVal) {
                    this.$emit('updateFormSearch', newVal)
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
            this.getIdcType()
        },
        methods: {
            init() {
                if (this.riskLevelListStore.length) {
                    this.department1List = this.department1ListStore
                    this.bizSystemList = this.bizSystemListStore
                    this.$nextTick(() => {
                        this.search(1)
                    })
                } else {
                    Promise.all([
                        this.queryDepartment1(),
                        this.queryBizSystemList(),
                        // this.queryRiskLevelList(),
                        // this.queryVulnerabilityGroupList(),
                    ]).then(() => {
                        // this.getScanCycleList()
                    })
                        .catch((err) => {
                            this.$message(err)
                        })
                }
            },
            // 下拉框选中事件
            // eslint-disable-next-line no-unused-vars
            changeSelect(frameDatas, frameOptions, select, codeObj) {
                this.formSearch.os_type = this.frameDatas.select = select.id
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
            queryDepartment2() {
                let req = {
                    type: 'department2',
                    pid: this.department1List[this.formSearch.department1Index].id
                }
                this.formSearch.department2 = ''
                cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department2List = res
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
            getIdcType() {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'idcType' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    // let Arrey = []
                    this.typeOption = res
                })

            },
            reset() {
                this.$refs['formSearch'].resetFields()
                this.frameDatas.select = ''
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
</style>
