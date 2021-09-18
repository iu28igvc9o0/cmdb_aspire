<template>
    <div>
        <!-- 列表 -->
        <el-form class="yw-form form"
                 style="padding-right:10px">
            <div class="table-operate-wrap clearfix">
                <section class="fr">
                    <el-button class="btn-icons-wrap"
                               type="text"
                               icon="el-icon-d-caret"
                               v-popover:popover>选择列</el-button>
                    <el-popover ref="popover"
                                placement="top"
                                trigger="click">
                        <div id="popover"
                             class="choose-column-popover">
                            <el-checkbox-group v-model="selectColumns"
                                               @change="handleCheckedColumnChange">
                                <div v-for="(column,index) in checkColumns"
                                     :key="index">
                                    <el-checkbox :label="column">{{ column.filedName }}</el-checkbox>
                                </div>
                            </el-checkbox-group>
                        </div>
                    </el-popover>
                </section>
                <el-button type="primary"
                           v-show="queryForm.moduleId"
                           @click="add()">新增</el-button>
                <el-menu class="el-menu-button"
                         :default-active="activeIndex"
                         mode="horizontal"
                         v-show="!queryForm.moduleId"
                         style="margin-top:-5px">
                    <el-submenu index="1">
                        <template slot="title">
                            <el-button type="primary">新增</el-button>
                        </template>
                        <el-submenu v-for="(item, index) in moduleList"
                                    :index="item.id"
                                    :key="index">
                            <template slot="title">{{item.name}}</template>
                            <div style="min-height:100px;max-height: 150px;overflow-y: auto">
                                <template v-for="(child, childIdx) in item.childModules">
                                    <el-menu-item v-if="item.childModules && item.childModules.length > 0"
                                                  :index="child.id"
                                                  :key="childIdx"
                                                  @click="handleSelect(item, child)">{{child.name}}
                                    </el-menu-item>
                                </template>
                            </div>
                        </el-submenu>
                    </el-submenu>
                </el-menu>
                <el-button type="primary"
                           @click="toUpdate()">编辑</el-button>
                <el-button type="primary"
                           @click="importInstance()">导入</el-button>
                <el-button @click="exportData()">导出</el-button>

            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          ref="viewTable"
                          :data="instanceDataList"
                          @header-dragend="expandTable"
                          height="calc(100vh - 440px)"
                          style="width:100%"
                          stripe
                          border
                          @selection-change="changeCheckbox">
                    <el-table-column type="selection"
                                     width="50"></el-table-column>
                    <el-table-column label="管理IP"
                                     width="120"
                                     sortable
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <a class="table-link"
                               @click="toDetail(scope.row)">{{scope.row.ip}}</a>
                        </template>
                    </el-table-column>

                    <el-table-column prop="device_name"
                                     min-width="120"
                                     label="设备名称"
                                     sortable
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            <a class="table-link"
                               @click="toDetail(scope.row)">{{scope.row.device_name}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="insert_time"
                                     label="创建时间"
                                     width="160"
                                     sortable
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="update_time"
                                     label="最后更新时间"
                                     width="160"
                                     sortable
                                     show-overflow-tooltip></el-table-column>

                    <el-table-column v-for="(item,index) in selectColumns"
                                     :key="index"
                                     :prop="item.filedCode"
                                     :label="item.filedName"
                                     width="160"
                                     sortable
                                     show-overflow-tooltip>
                    </el-table-column>
                </el-table>
            </div>
            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </el-form>
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'

    export default {
        mixins: [QueryObject],
        props: ['queryType', 'parentParams'],
        components: {
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        data() {
            return {
                querySelectorClass: false,
                ImgSelected: '',
                imgIndex: '',
                overviewShow: true,
                poolID: null,
                PODID: null,
                selectedShow: false,

                publicPool: false,
                publicPeriod: false,
                publicPOD: false,
                publicClassification: false,
                publicBrand: false,
                publicState: false,
                publicManufactor: false,
                publicIP: false,
                publicDeviceName: false,
                publicDeviceType: false,
                publicDeviceClass3: false,
                publicDepartment1: false,
                publicDepartment2: false,
                publicDeviceSN: false,
                publicDeviceModel: false,
                publicRoom: false,
                publicDeviceProject: false,
                publicDeptOperation: false,
                publicOps: false,
                publicInsertTime: false,
                publicBizSystem: false,

                inputPool: '',
                inputPeriod: '',
                inputPOD: '',
                inputClassification: '',
                inputBrand: '',
                inputState: '',
                inputManufactor: '',
                inputIP: '',
                inputDeviceName: '',
                inputDeviceType: '',
                inputDeviceClass3: '',
                inputDepartment1: '',
                inputDepartment2: '',
                inputDeviceSN: '',
                inputDeviceModel: '',
                inputRoom: '',
                inputDeviceProject: '',
                inputDeptOperation: '',
                inputOps: '',
                inputInsertTime: '',
                inputBizSystem: '',

                pool: false,
                poolB: false,
                poolUP: false,
                POD: false,
                PODB: false,
                PODBUP: false,
                factory: false,
                factoryB: true,
                factoryUP: false,
                status: false,
                deviceStatusB: true,
                deviceStatusUP: false,
                equipmentBrand: false,
                equipmentBrandB: true,
                equipmentBrandBUP: false,
                queryBotton: true,
                queryBottonA: false,
                queryBottonS: true,
                queryBottonA1: true,
                showDown: false,
                activeIndex: '1',
                queryParams: this.parentParams || {},
                queryKeys: ['moduleId', 'idcType', 'pod', 'deviceClass', 'deviceClass3', 'deviceType', 'deviceModel', 'department1',
                            'department2', 'bizSystem', 'ip', 'deviceName', 'deviceSN', 'room', 'deviceProject', 'deviceStatus',
                            'maintenanceFactory', 'mfrFactory', 'startInsertTime', 'endInsertTime', 'deptOperation', 'ops', 'projectName', 'moduleName'],
                pod: false,
                importType: 'instance',
                display: {
                    isImport: false,
                    isEdit: false,
                    isSearchPlane: false,
                    isBatchUpdate: false
                },
                conditionShow: true,
                inputHome: false,
                inputShow: false,
                insertTime: '',
                queryForm: {
                    moduleId: '',
                    idcType: '',
                    projectName: '', // 工期
                    deviceStatus: '', // 运行状态
                    pod: '',
                    deviceClass: '',
                    deviceClass3: '',
                    deviceType: '',
                    deviceModel: '',
                    department1: '',
                    department2: '',
                    bizSystem: '',
                    ip: '',
                    deviceName: '',
                    deviceSN: '',
                    room: '',
                    deviceProject: '',
                    maintenanceFactory: '',
                    mfrFactory: '',
                    startInsertTime: '',
                    endInsertTime: '',
                    deptOperation: '',
                    ops: ''
                },
                allDictList: [], // 所有的设备字典
                poolList: [], // 资源池列表
                projectNameList: [], // 工期
                factoryList: [], // 维护厂家列表
                runningStateList: [], // 运行状态
                deviceClassList: [], // 设备分类列表
                deviceClass3List: [], // 设备三级分类列表
                deviceTypeList: [], // 设备类型列表
                podList: [], // POD池列表
                department1List: [], // 一级部门列表
                department2List: [], // 二级部门列表
                roomList: [], // 机房位置
                bizSystemList: [], // 业务系统列表
                deviceStatusList: [], // 设备运行状态
                maintenanceFactoryList: [], // 维保厂家
                mfrFactoryList: [], // 品牌
                brandList: [], // 设备品牌

                checkColumns: [], // 选择列-所有数据
                selectColumns: [], // 选择列-选中项
                column_data: {},
                columnList: [],

                dbFilterData: {}, // 数据中保存的选择列值
                instanceDataList: [], // 主机数据集合
                multipleSelection: [],
                multipleSelectionAll: [],
                paginationData: {
                    currentPage: 1,
                    total: 0,
                    pageSize: 50,
                    selectPageSizes: [30, 50, 100],
                    sort: 'insert_time',
                    order: 'desc'
                },
                currentPage: 1,
                pageSize: 50,
                condicationName: '', // 配置名称
                condicationType: '', // 配置类型
                moduleList: [], // 模型列表

                QuerysearchList: [] // 查询条件
            }
        },
        computed: {

        },
        watch: {
        },

        mounted: function () {
            // 获取当前模型的过滤列
            this.getColumnFilter()

            if (this.$route.query.parentParams) {
                this.queryParams = this.$route.query.parentParams
            }

            // 初始化字典值
            this.initDict()

            // 获取表格数据信息
            this.searchInstanceList()
            if (!this.queryForm.moduleId) {
                this.queryModuleList()
            }
        },
        methods: {
            // table 多选框
            changeCheckbox(val) {
                this.multipleSelection = val
            },
            // 获取模型列表
            queryModuleList() {
                let _t = this
                rbCmdbServiceFactory.getModuleTree('').then((data) => {
                    _t.moduleList = data
                })
            },

            // 获取表格头部信息
            queryInstanceHeader() {
                let _this = this
                this.checkColumns = []
                _this.selectColumns = []
                rbCmdbServiceFactory.getInstanceHeader(this.queryForm.moduleId || '').then((item) => {
                    // 不显示固定的几列
                    _this.checkColumns = item.filter((item2) => {
                        if (['ip', 'device_name', 'insert_time', 'update_time'].indexOf(item2.filedCode) < 0) {
                            return true
                        }
                    })

                    if (_this.dbFilterData.columnInfo) {
                        let ci = JSON.parse(_this.dbFilterData.columnInfo)
                        _this.checkColumns.forEach(item => {
                            if (ci[item['filedCode']]) {
                                _this.selectColumns.push(item)
                                _this.column_data[item['filedCode']] = ci[item['filedCode']]
                            }
                        })
                    }
                    this.layoutTable('viewTable')
                }).catch((item) => {
                    _this.$message.error('获取模型表头失败' + item)
                })
            },

            // 设置表格查询参数
            setParams(activePagination) {
                if (activePagination) {
                    this.queryForm['pageNumber'] = this.currentPage
                    this.queryForm['pageSize'] = this.pageSize
                } else {
                    this.initPageChange(this.staticCurrentPage, this.staticPageSize)

                    this.queryForm.startInsertTime = this.insertTime[0] && this.insertTime[0] !== '' ? this.insertTime[0] : ''
                    this.queryForm.endInsertTime = this.insertTime[1] && this.insertTime[1] !== '' ? this.insertTime[1] : ''
                    this.queryForm.sortColumn = this.paginationData.sort
                    this.queryForm.sortType = this.paginationData.order
                    this.queryForm.queryType = this.queryType || 'normal'
                    this.queryForm.pageNumber = this.initPageChange()
                    this.queryForm.pageSize = this.pageSize
                }
            },

            /** 表格查询
             * activePagination:分页活动下保持先前的查询条件
             */
            query(activePagination = false) {
                this.$emit('showLoading', '正在查询数据,请稍等...')

                this.setParams(activePagination)

                rbCmdbServiceFactory.getInstanceList(this.queryForm).then((data) => {
                    this.total = data.totalSize
                    this.instanceDataList = data.data
                    this.columnList = data.columnList
                }).finally(() => {
                    this.$emit('hideLoading')
                })
            },

            // 带条件查询
            searchInstanceList: function () {
                this.query()
            },

            // 获取已保存的自定义列
            getColumnFilter() {
                let _this = this
                rbCmdbServiceFactory.getColumnFilter({ menuType: 'CMDB_INSTANCE', moduleId: this.queryForm.moduleId || '' }).then((data) => {
                    _this.dbFilterData = data
                    // 获取表格头信息
                    this.queryInstanceHeader()
                })
            },

            // 跳转到设备详情
            toDetail(row) {
                let _t = this
                if ((this.queryForm.moduleId || '') === '') {
                    rbCmdbServiceFactory.getModuleByInstanceId(row.id).then((data) => {
                        if (data) {
                            let queryParams = {
                                moduleId: data.module.id,
                                name: data.module.name,
                                instanceId: row.id,
                                idcType: row.idcType,
                                ip: row.ip,
                                state: 'detail'
                            }
                            queryParams = JSON.stringify(queryParams)
                            _t.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
                        } else {
                            _t.$message.error('获取模型数据失败')
                        }
                    })
                } else {
                    let queryParams = {
                        moduleId: this.queryForm.moduleId,
                        instanceId: row.id,
                        idcType: row.idcType,
                        ip: row.ip,
                        state: 'detail'
                    }
                    queryParams = JSON.stringify(queryParams)
                    _t.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
                }
            },

            // 跳转修改页面
            toUpdate() {
                if (this.multipleSelection.length <= 0) {
                    this.$alert('请选择一条数据进行修改', '通知', {
                        confirmButtonText: '确定'
                    })
                    return
                } else if (this.multipleSelection.length > 1) {
                    this.$alert('只能对一条数据进行修改', '通知', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let queryParams = {
                    instanceId: this.multipleSelection[0].id,
                    state: 'update'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },

            // 初始化字典值
            initDict() {
                // 获取所有的字典值
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/configDict/getAll'
                }).then((res) => {
                    const deviceClassOptions = []
                    const deviceTypeOptions = []
                    const deviceStatusOptions = []
                    if (res) {
                        this.allDictList = res
                        res.forEach((item) => {
                            if (item.colName === 'device_class') {
                                deviceClassOptions.push(this.convertDictConfig(item))
                            }
                            if (item.colName === 'device_type') {
                                deviceTypeOptions.push(this.convertDictConfig(item))
                            }
                            if (item.colName === 'device_status') {
                                deviceStatusOptions.push(this.convertDictConfig(item))
                            }
                        })
                        this.deviceClassList = this.fillSelectOption(deviceClassOptions, 'device_class')
                        this.deviceTypeList = deviceTypeOptions
                        // this.deviceStatusList = deviceStatusOptions
                    }
                })
            },

            // 将下拉框添加"全部"选项
            fillSelectOption(options, selectType) {
                const newOptions = [{ id: '', name: '全部', type: selectType, value: '', pid: '' }]
                if (options) {
                    options.forEach((item) => {
                        newOptions.push(item)
                    })
                }
                return newOptions
            },

            // 将获取到的字典值转化为可识别的json对象
            convertDictConfig(dict) {
                return {
                    id: dict.dictId,
                    type: dict.colName,
                    name: dict.dictCode,
                    value: dict.dictNote,
                    pid: dict.upDict
                }
            },

            // 导入功能
            importInstance() {
                this.setImportDisplay(true)
            },

            // 设置导入窗口是否显示 true:显示 false:不显示
            setImportDisplay(val) {
                this.display.isImport = val
            },

            // 导出功能
            exportData() {
                // this.$message('正在导出数据,由于数据量较大请耐心等待...')
                this.$emit('showLoading', '正在导出数据,由于数据量较大请耐心等待...')
                this.queryForm.pageNumber = null
                this.queryForm.pageSize = null
                if (this.insertTime !== '') {
                    this.queryForm.startInsertTime = this.insertTime[0]
                    this.queryForm.endInsertTime = this.insertTime[1]
                }
                this.queryForm.sortColumn = this.paginationData.sort
                this.queryForm.sortType = this.paginationData.order
                this.queryForm.queryType = this.queryType || 'normal'
                rbConfigDictServiceFactory.getDictsByType({ type: 'export_type'}).then((res) => {
                    if (res.value !== 'stream') {
                         rbCmdbServiceFactory.exportInstanceList(this.queryForm).then((data) => {
                                if (data.code === 'success') {
                                let downLoadElement = document.createElement('a')
                                downLoadElement.href = data.path
                                // downLoadElement.download = '历史告警数据列表.xlsx'
                                downLoadElement.setAttribute('download', '资源列表.xlsx')
                                document.body.appendChild(downLoadElement)
                                downLoadElement.click()
                                document.body.removeChild(downLoadElement)
                                } else {
                                    this.$message.error(data.message)
                                }
                        }).catch((e) => {
                            this.pageLoading = false
                            this.$message.error('导出失败!' + e)
                        }).finally(() => {
                            this.hideLoading()
                        })
                    } else {
                        rbCmdbServiceFactory.exportInstanceListStream(this.queryForm).then((data) => {

                            let blob = new Blob([data], { type: 'application/msword' })
                            // 创建下载链接
                            let objectUrl = URL.createObjectURL(blob)
                            let downLoadElement = document.createElement('a')
                            downLoadElement.href = objectUrl
                            downLoadElement.download = '资源列表.xlsx'
                            document.body.appendChild(downLoadElement)
                            downLoadElement.click()
                            document.body.removeChild(downLoadElement)
                            URL.revokeObjectURL(objectUrl)
                         }).catch((e) => {
                                this.pageLoading = false
                                this.$message.error('导出失败!' + e)
                            }).finally(() => {
                            this.hideLoading()
                        })
                    }
                })
            },
            add() {
                let queryParams = {
                    moduleId: this.queryForm.moduleId,
                    deviceClass: this.queryForm.moduleId,
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            handleSelect(item, child) {
                let queryParams = {
                    moduleId: child.id,
                    deviceClass: item.name,
                    deviceType: child.name,
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            handleCheckedColumnChange() {
                this.layoutTable('viewTable')

                // 保存
                this.column_data = {}
                this.selectColumns.forEach((item) => {
                    this.column_data[item.filedCode] = true
                })

                let filter = this.dbFilterData
                filter.columnMap = this.column_data
                rbCmdbServiceFactory.updateColumnFilter(filter).then((data) => {
                    // this.query()
                    if (!data.success) {
                        this.$notify({
                            title: '提示',
                            message: data.msg,
                            type: 'error',
                            duration: 3000
                        })
                    }
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>
