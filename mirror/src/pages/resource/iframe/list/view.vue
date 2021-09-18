<template>
    <!-- 业务视图/品牌视图/资源分类视图  共用该页面作为组件 -->
    <div class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div class="managementRight">
            <!-- 条件列表组件 -->
            <condition-list ref="formSearch"
                            :condicationCodeDefault="condicationCodeDefault"
                            :parentParams="parentParams"
                            :total="total"
                            @initTableHeight="initTableHeight"
                            @queryByCondition="queryByCondition"
                            @queryAllList="queryAllList"></condition-list>

            <!-- 查询结果 -->
            <el-form class="yw-form form"
                     style="padding-right:10px">
                <div ref="operateBox"
                     class="table-operate-wrap clearfix">
                    <section class="fr"
                             style="margin-top: 10px;">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-d-caret"
                                   v-popover:popover>选择列</el-button>
                        <el-popover ref="popover"
                                    @show="popShow"
                                    @hide="popHide"
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
                    <div style="display:inline-block;vertical-align:middle;"
                         v-if="resetComponent && !this.queryForm.moduleId && buttonPermission.create">
                        <el-menu class="el-menu-button"
                                 menu-trigger='hover'
                                 mode="horizontal"
                                 style="margin-top:-5px">
                            <el-submenu index="1">
                                <template slot="title">
                                    <el-button type="primary"
                                               @click="updateComponent()">新增</el-button>
                                </template>
                                <div style="max-height: 150px;overflow-y: auto">
                                    <template v-for="item in moduleList">
                                        <el-submenu v-if="!item.isDisplay && item.isDisplay === 0"
                                                    :index="item.id"
                                                    :key="item.id">
                                            <template slot="title">
                                                <span class="menu-text">{{item.name}}</span>
                                            </template>
                                            <div style="max-height: 150px;overflow-y: auto">
                                                <template v-for="child in item.childModules">
                                                    <el-submenu v-if="!child.isDisplay && child.isDisplay === 0"
                                                                :index="child.id"
                                                                :key="child.id">
                                                        <template slot="title">
                                                            <span class="menu-text">{{child.name}}</span>
                                                        </template>
                                                        <div style="max-height: 150px;overflow-y: auto">
                                                            <template v-for="child2 in child.childModules">
                                                                <el-submenu v-if="!child2.isDisplay && child2.isDisplay === 0"
                                                                            :index="child2.id"
                                                                            :key="child2.id">
                                                                    <template slot="title">
                                                                        <span class="menu-text"
                                                                              @click="handleSelect(child,child2)">{{child2.name}}</span>
                                                                    </template>
                                                                </el-submenu>
                                                            </template>
                                                        </div>
                                                    </el-submenu>
                                                </template>
                                            </div>
                                        </el-submenu>
                                    </template>
                                </div>
                            </el-submenu>
                        </el-menu>
                    </div>
                    <div style="margin:5px 0 10px;display: inline-block;">
                        <el-button type="primary"
                                   v-if=" logCodes.indexOf(condicationCodeDefault) < 0 && !(resetComponent && !this.queryForm.moduleId) && buttonPermission.create"
                                   @click="handEmptySelect()">新增</el-button>
                        <el-button type="primary"
                                   v-if="  logCodes.indexOf(condicationCodeDefault) < 0 && buttonPermission.update"
                                   @click="toUpdate()">编辑</el-button>

                        <el-button type="primary"
                                   v-if="logCodes.indexOf(condicationCodeDefault) < 0 && buttonPermission.batchUpdate"
                                   @click="toBatchUpdate()">批量修正</el-button>
                        <el-button type="primary"
                                   v-if=" logCodes.indexOf(condicationCodeDefault) < 0 && buttonPermission.delete"
                                   @click="toDelete()">删除</el-button>
                        <el-button type="primary"
                                   v-if=" logCodes.indexOf(condicationCodeDefault) < 0 && buttonPermission.import"
                                   @click="importInstance()">导入</el-button>
                        <el-button @click="exportData()"
                                   v-if=" logCodes.indexOf(condicationCodeDefault) < 0 && buttonPermission.export">导出</el-button>
                        <el-button @click="exportConfiguration()"
                                   v-if="additionalParams">导出配置信息</el-button>
                    </div>
                </div>
                <div class="yw-el-table-wrap">
                    <el-table class="yw-el-table"
                              ref="viewTable"
                              @sort-change="handleSortChange"
                              :data="instanceDataList"
                              v-loading="tableLoading"
                              @row-dblclick="toDetail"
                              :height="tableHeight"
                              style="width:100%"
                              stripe
                              border
                              @selection-change="changeCheckbox">
                        <el-table-column type="selection"
                                         width="50"></el-table-column>
                        <el-table-column label="管理IP"
                                         prop="ip"
                                         v-if="isHost('ip')"
                                         width="120"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <a class="table-link"
                                   @click="toDetail(scope.row)">{{scope.row.ip}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="device_name"
                                         v-if="isHost('device_name')"
                                         min-width="120"
                                         label="设备名称"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <a class="table-link"
                                   @click="toDetail(scope.row)">{{scope.row.device_name}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="bizSystem"
                                         v-if="isBusiness('bizSystem')"
                                         min-width="120"
                                         label="业务系统"
                                         sortable
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <a class="table-link"
                                   @click="toDetail(scope.row)">
                                    {{refColumns['bizSystem'] ? scope.row[refColumns['bizSystem']] : scope.row['bizSystem'] }}
                                </a>
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
                            <template slot-scope="scope">
                                {{refColumns[item.filedCode] ? scope.row[refColumns[item.filedCode]] : scope.row[item.filedCode] }}
                            </template>
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
            <!-- 查询结果 -->
            <importInstances ref="importInstances"
                             v-if="isImportShow"
                             :showImport="isImportShow"
                             @setImportDisplay="setImportDisplay"
                             importType="instance"
                             :moduleType="moduleTypeDefault"
                             :moduleId="queryForm.moduleId"
                             :catalogId="queryForm.catalogId"></importInstances>
        </div>

        <BatchEdit v-if="BatchEditDisplay"
                   :batchShowChild="BatchEditDisplay"
                   @batchShowChildS="toBatchUpdate1"></BatchEdit>
    </div>
</template>
<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import updateComponent from 'src/utils/updateComponent.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        name: 'ResourceIframeList',
        mixins: [rbAutoOperationMixin, QueryObject, updateComponent],
        props: ['queryType', 'parentParams', 'moduleType', 'condicationCode', 'catalogId', 'additionalParams'],
        components: {
            importInstances: () => import('src/pages/cmdb/v2/instance/import/import-instance.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
            BatchEdit: () => import('../list/batchEdit.vue'),
            conditionList: () => import('./condition-list.vue')
        },
        data() {
            return {
                total: 0,
                queryNumber: 0,
                loading: false,
                tableLoading: false,
                loading_text: '正在加载数据...',
                newCurrentOrder: '',
                BatchEditDisplay: false,    // 批量修正弹框

                logCodes: ['cmdb_operate_log', 'cmdb_restful_log'], // 不展示任何按钮

                isImportShow: false,

                inputHome: false,
                inputShow: false,
                insertTime: '',
                queryForm: {
                    catalogId: '',
                    moduleId: '',
                },

                department1List: [], // 一级部门列表
                checkColumns: [], // 选择列-所有数据
                selectColumns: [], // 选择列-选中项
                moduleColumns: [], // 模型码表数据
                column_data: {},
                columnList: [],
                refColumns: {},
                dbFilterData: {}, // 数据中保存的选择列值
                instanceDataList: [], // 主机数据集合
                cloneData: [], // 主机数据集合克隆
                multipleSelection: [],
                moduleList: [], // 模型列表
                currentModuleInfo: null,  // 当前模型信息
                parentModuleInfo: null, // 当前父模型信息
                buttonPermission: {}, // 按钮的操作权限

                urlParams: {},  // url参数
                conditionParamsMerge: {}, // 合并默认条件+已选中的条件
                isRouteQueryInit: false,
                deviceClass: '',
                deviceType: '',
                allList: []
            }
        },
        computed: {
            // 本地图片域名
            localImgHost() {
                return location.origin.includes('localhost') ? 'http://10.12.70.40:8080' : ''
            },
            // 获取 条件查询参数 condicationCode
            condicationCodeDefault() {
                return this.urlParams.condicationCode || this.condicationCode || 'instance_list'
            },
            // url参数
            routeQuery() {
                return this.$route.query
            },
            moduleTypeDefault() {
                return this.urlParams.moduleType || this.moduleType
            }
        },
        watch: {
            // 路由参数
            routeQuery: {
                handler(newVal) {
                    this.getTableData()
                },
                deep: true,
                immediate: true
            },
            // 资源分类视图，点击左侧树，更新header
            parentParams: {
                handler(newVal) {
                    if (newVal.deviceClass && newVal.moduleId) {
                        this.queryForm.moduleId = newVal.moduleId
                        // 获取当前模型的过滤列
                        this.getColumnFilter()
                    }
                },
                deep: true,
                immediate: true,
            },
        },

        mounted: function () {
            this.showLoading('正在加载数据，请稍等...')

            this.getTableData(true)

            // 获取按钮权限
            this.initPermission()
            // 获取当前模型的过滤列
            this.getColumnFilter()

            // 没有模型ID，获取表格数据信息
            if (!this.queryForm.moduleId) {
                this.queryModuleList()

                // 有模型ID，查询模型信息
            } else {
                this.getParentInfo()
            }
        },
        methods: {
            // 获取表格数据
            getTableData(getData) {
                if (!this.routeQuery.parentParams) {
                    this.urlParams = {
                        moduleType: this.routeQuery.module_type,
                        module_id: this.routeQuery.module_id,
                        condicationCode: this.routeQuery.condicationCode
                    }

                    this.queryForm.catalogId = this.routeQuery.catalogId
                    this.queryForm.moduleId = this.routeQuery.module_id

                    if (!this.queryForm.moduleId && !this.routeQuery.catalogId) {
                        this.queryForm.catalogId = '96603733-5793-11ea-ab96-fa163e982f89'
                    }

                    if (getData) {
                        this.query()
                    }
                }
            },
            // 查询模型信息
            getParentInfo() {
                rbCmdbModuleServiceFactory.getParentInfo({ 'module_id': this.queryForm.moduleId }).then((data) => function () {
                    if (data) {
                        this.currentModuleInfo = {
                            module_id: data.module_id,
                            module_name: data.module_name
                        }
                        this.parentModuleInfo = {
                            module_id: data.parent_id,
                            module_name: data.parent_name
                        }
                    }
                })
            },
            // 点击条件查询
            queryByCondition(conditionParams) {
                this.currentPage = 1
                let req = { ...this.getDefaultParams(), ...this.urlParams, ...conditionParams }
                this.conditionParamsMerge = req

                // 带有url参数，初始化页面根据url参数查询
                if (!this.isRouteQueryInit) {
                    return
                }
                this.getInstanceList(req)
            },
            // 查询列表
            getInstanceList(req) {
                this.showLoading('正在加载数据，请稍等...')
                rbCmdbServiceFactory.getInstanceList(req).then((data) => {
                    this.total = data.totalSize
                    this.instanceDataList = data.data
                    this.columnList = data.columns
                    for (const key in this.columnList) {
                        const item = this.columnList[key]
                        if (item.type === 'ref') {
                            this.$set(this.refColumns, item.filed_code, item.ref_name)
                        }
                    }
                    this.isRouteQueryInit = true
                }).finally(() => {
                    this.hideLoading()
                })
            },
            // 初始化权限
            initPermission() {
                this.buttonPermission = {
                    create: true,
                    update: true,
                    delete: true,
                    batchUpdate: true,
                    import: true,
                    export: true,
                    assign: true,
                    unAssign: true
                }
            },
            handleSortChange(val) {
                this.currentPage = 1
                this.newCurrentValue = val.prop
                if (val.order == 'ascending') {
                    this.newCurrentOrder = 'asc'
                } else if (val.order == 'descending') {
                    this.newCurrentOrder = 'desc'
                } else {
                    this.newCurrentOrder = ''
                }
                this.query()
            },
            async query() {
                let resdata = { ...this.getDefaultParams(), ...this.urlParams }
                if (this.queryNumber == 0 && this.$route.query.parentParams != undefined) {
                    if (!this.department1List.length) {
                        await this.queryDepartment1()
                    }
                    let params = this.$route.query.parentParams
                    let departmentId = ''
                    this.department1List.some((item) => {
                        if (item.name === params.department1) {
                            departmentId = item.id
                            return true
                        } else {
                            return false
                        }
                    })
                    resdata = { ...resdata, ...params, ...{ department1: departmentId, device_class: this.deviceClass, device_type: this.deviceType } }
                }

                this.getInstanceList(resdata)
                this.queryNumber++
            },
            getDefaultParams() {
                let resdata = {
                    'condicationCode': this.condicationCodeDefault,
                    'pageSize': this.pageSize,
                    'currentPage': this.currentPage
                }
                resdata = Object.assign(this.conditionParamsMerge, resdata)
                if (this.newCurrentOrder) {
                    resdata.sort = [{
                        'filed': this.newCurrentValue,
                        'type': this.newCurrentOrder
                    }]
                }
                return resdata
            },
            changeCheckbox(val) {
                this.multipleSelection = val
            },
            // 获取模型列表
            queryModuleList() {
                let _t = this
                rbCmdbModuleServiceFactory.getModuleTree({ 'moduleType': this.moduleTypeDefault }).then((data) => {
                    _t.moduleList = data
                })
            },

            // 获取表格头部信息
            queryInstanceHeader() {
                let _this = this
                this.checkColumns = []
                _this.selectColumns = []
                _this.moduleColumns = []
                rbCmdbServiceFactory.getInstanceHeader(this.queryForm.moduleId || '').then((item) => {
                    item.forEach(item2 => {
                        _this.moduleColumns.push(item2.filedCode)
                    })
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

            // 获取已保存的自定义列
            getColumnFilter() {
                let _this = this
                rbCmdbServiceFactory.getColumnFilter({ menuType: 'CMDB_INSTANCE', moduleId: this.queryForm.moduleId }).then((data) => {
                    _this.dbFilterData = data
                    // 获取表格头信息
                    _this.queryInstanceHeader()
                })
            },

            // 跳转到设备详情
            toDetail(row) {
                if (this.logCodes.indexOf(this.condicationCodeDefault) > -1) {
                    return
                }
                let queryParams = {
                    instanceId: row.id,
                    moduleId: row['module_id'],
                    state: 'detail',
                    ip: row['ip']
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
            },
            // 批量修正
            toBatchUpdate() {
                this.BatchEditDisplay = true
            },
            toBatchUpdate1() {
                this.BatchEditDisplay = false
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
                    moduleId: this.multipleSelection[0]['module_id'],
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'update'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            // 删除功能
            toDelete() {
                if (this.multipleSelection.length <= 0) {
                    this.$alert('请选择需要删除的数据', '提示', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let _this = this
                this.$confirm('确定要删除选中的' + this.multipleSelection.length + '条记录吗?').then(() => {
                    _this.showLoading('系统正在删除数据,请稍等...')
                    rbCmdbServiceFactory.deleteInstance({ 'instanceList': _this.multipleSelection }).then((data) => {
                        if (data && data.success === true) {
                            this.$message({ type: 'success', message: data.message })
                            this.query()
                        } else {
                            this.$message({ type: 'error', message: data.message })
                        }
                    }).catch((e) => {
                        this.$message({ type: 'error', message: e.message })
                    }).finally(() => {
                        _this.hideLoading()
                    })
                })
            },
            // 获取一级部门
            queryDepartment1: function () {
                let setf = this
                return this.rbHttp.sendRequest({
                    method: 'GET',
                    params: { 'type': 'department1' },
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    setf.department1List = res
                    return res
                })
            },

            // 导入功能
            importInstance() {
                this.setImportDisplay(true)
            },

            // 设置导入窗口是否显示 true:显示 false:不显示
            setImportDisplay(val) {
                this.isImportShow = val
            },
            showLoading(loading_text) {
                if (this.queryType && this.queryType !== '') {
                    this.$emit('showLoading', loading_text)
                } else {
                    this.loading = true
                    if (loading_text) {
                        this.loading_text = loading_text
                    }
                }
            },
            hideLoading() {
                if (this.queryType && this.queryType !== '') {
                    this.$emit('hideLoading')
                } else {
                    this.loading = false
                    this.loading_text = ''
                }
            },
            // 导出功能
            exportData() {
                this.showLoading('正在导出数据,由于数据量较大请耐心等待...')
                let resdata = this.$utils.deepClone(this.conditionParamsMerge)
                resdata.pageSize = null
                resdata.currentPage = null
                rbConfigDictServiceFactory.getDictsByType({ type: 'export_type' }).then((res) => {
                    if (res[0] && res[0].value === 'ftp') {
                        rbCmdbServiceFactory.exportInstanceList(resdata, this.moduleTypeDefault).then((data) => {
                            if (data.code === 'success') {
                                let downLoadElement = document.createElement('a')
                                downLoadElement.href = data.path
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
                        rbCmdbServiceFactory.exportInstanceListStream(resdata, this.moduleTypeDefault).then((data) => {
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
            // 导出配置文件
            exportConfiguration() {
                this.showLoading('正在导出数据,由于数据量较大请耐心等待...')
                let resdata = this.$utils.deepClone(this.conditionParamsMerge)
                resdata.pageSize = null
                resdata.currentPage = null
                resdata.exportTabType = 'export'
                rbCmdbServiceFactory.exportFileConfiguration(resdata, this.moduleTypeDefault).then((data) => {
                    let blob = new Blob([data], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '主机配置信息列表.xlsx'
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
            },
            // 新增
            handEmptySelect() {
                let queryParams = {
                    instanceId: '',
                    moduleId: this.queryForm.moduleId,
                    'device_class': {
                        id: this.parentModuleInfo && this.parentModuleInfo.module_id || '',
                        name: this.parentModuleInfo && this.parentModuleInfo.module_name || ''
                    },
                    'device_type': {
                        id: this.currentModuleInfo && this.currentModuleInfo.module_ud || '',
                        name: this.currentModuleInfo && this.currentModuleInfo.module_name || ''
                    },
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            // 新增
            handleSelect(parentItem, item) {
                let queryParams = {
                    instanceId: '',
                    moduleId: item.id,
                    device_class: { id: parentItem.id, name: parentItem.name },
                    device_type: { id: item.id, name: item.name },
                    originRouter: this.$route.fullPath,// 当前路由
                    state: 'add'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            // 选择列时，清空table数据，避免卡顿
            popShow() {
                this.tableLoading = true
                this.cloneData = this.$utils.deepClone(this.instanceDataList)
                this.instanceDataList = []
            },
            // 选择列完成后，回填数据
            popHide() {
                this.tableLoading = false
                this.instanceDataList = this.cloneData
            },
            // 选择列
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
                    if (!data.success) {
                        this.$notify({
                            title: '提示',
                            message: data.msg,
                            type: 'error',
                            duration: 3000
                        })
                    }
                })
            },

            isHost(filedCode) {
                let flag = false
                if (this.moduleColumns.indexOf(filedCode) > 0) {
                    flag = true
                }
                return flag && this.moduleTypeDefault !== 'business' && this.moduleTypeDefault !== 'dict'
            },
            isBusiness(filedCode) {
                let flag = false
                if (this.moduleColumns.indexOf(filedCode) > 0) {
                    flag = true
                }
                return flag && this.moduleTypeDefault !== 'business'
            },
            queryAllList(list) {
                this.allList = list
                let classificationList = []
                let typeList = []
                let params = this.$route.query.parentParams

                classificationList = list.filter(item => {
                    return item.cmdbCode.filedName === '设备分类'
                })

                classificationList[0].cmdbCode.conditionList.forEach((item) => {
                    if (item.value === params.device_class) {
                        this.deviceClass = item.id
                        return true
                    } else {
                        return false
                    }
                })

                typeList = list.filter(item => {
                    return item.cmdbCode.filedName === '设备类型'
                })

                typeList[0].cmdbCode.conditionList.forEach((item) => {
                    if (item.value === params.device_type) {
                        this.deviceType = item.id
                        return true
                    } else {
                        return false
                    }
                })
                this.query()


            }
        }
    }
</script>
<style lang="scss" scoped>
    @import '../iframeHome/components/rem.scss';
    @import './cond_style.scss';
</style>
