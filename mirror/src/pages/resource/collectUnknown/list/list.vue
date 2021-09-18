<template>
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="75px">
            <el-form-item label="资源池">
                <el-select v-model="condition.idcType"
                           clearable>
                    <el-option v-for="(item, index) in idcTypeList"
                               :key="index"
                               :value="item.id"
                               :label="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="IP地址">
                <el-input v-model="condition.ip"></el-input>
            </el-form-item>
            <el-form-item label="数据来源">
                <el-select v-model="condition.dataFrom"
                           clearable>
                    <el-option v-for="(item, index) in dataForm"
                               :key="index"
                               :label="item"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="处理状态">
                <el-select v-model="condition.handleStatus"
                           clearable>
                    <el-option v-for="(item, index) in handleStatus"
                               :key="index"
                               :label="item.name"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search()">查询</el-button>
                <el-button @click="cancel()">重置</el-button>
            </section>
        </el-form>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="exportData()">导出</el-button>
                <el-button type="text"
                           icon="el-icon-set-up"
                           @click="allMaintain()">批量维护

                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 300px)">
                    <el-table-column label="资源池"
                                     prop="idcTypeName"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button v-if="scope.row.instanceId"
                                       type="text"
                                       @click="toDetail(scope.row.instanceId)">
                                {{scope.row.idcTypeName}}
                            </el-button>
                            <span v-else>
                                {{scope.row.idcTypeName}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="IP地址"
                                     prop="ip"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button v-if="scope.row.instanceId"
                                       type="text"
                                       @click="toDetail(scope.row.instanceId)">
                                {{scope.row.ip}}
                            </el-button>
                            <span v-else>
                                {{scope.row.ip}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="设备名称"
                                     prop="deviceName"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button v-if="scope.row.instanceId"
                                       type="text"
                                       @click="toDetail(scope.row.instanceId)">
                                {{scope.row.deviceName}}
                            </el-button>
                            <span v-else>
                                {{scope.row.deviceName}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="设备类型"
                                     prop="deviceTypeName"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="数据来源"
                                     prop="dataFrom"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="备注"
                                     prop="remark"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="处理状态"
                                     prop="handleStatus"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{scope.row.handleStatus === 0 ? '待处理' : scope.row.handleStatus === 1 ? '已维护' : '已屏蔽'}}
                        </template>
                    </el-table-column>
                    <el-table-column label="提交人"
                                     prop="commitUser"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="提交时间"
                                     prop="commitTime"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{scope.row.commitTime | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column label="处理人"
                                     prop="handleUser"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="处理时间"
                                     prop="handleTime"
                                     min-width="100"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            {{scope.row.handleTime | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     width="80"
                                     fixed="right">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button v-if="scope.row.handleStatus === 0"
                                           type="text"
                                           title="维护"
                                           icon="el-icon-circle-check"
                                           @click="toCreate(scope.row)">
                                </el-button>
                                <el-button v-if="scope.row.handleStatus === 0"
                                           type="text"
                                           title="屏蔽"
                                           icon="el-icon-circle-close"
                                           @click="update(scope.row, 2)">
                                </el-button>
                                <el-button v-if="scope.row.handleStatus === 2"
                                           type="text"
                                           title="解屏蔽"
                                           icon="el-icon-circle-close"
                                           @click="update(scope.row, 0)">
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="pageInfo.currentPage"
                               :page-sizes="pageInfo.pageSizes"
                               :page-size="pageInfo.pageSize"
                               :total="pageInfo.total"
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </el-form>

        <MaintainDialog v-if="processInfo.showProcess"
                        :processInfo="processInfo"
                        @refresh="getUnknownList"></MaintainDialog>

        <el-dialog class="yw-dialog"
                   title="选择设备类型"
                   :visible.sync="showDialog"
                   width="500px">
            <section class="yw-dialog-main">

                <treeselect class="yw-treeselect"
                            v-model="newModuleId.id"
                            :options="moduleLists"
                            :multiple="false"
                            :auto-load-root-options="false"
                            :autoSelectAncestors="true"
                            :limit="1"
                            :flat="true"
                            noChildrenText="暂无子节点"
                            noOptionsText="暂无数据"
                            placeholder="请选择设备模型"
                            @select="selectDepart">
                </treeselect>
                <form id="frmHostInfo"
                      name="frmHostInfo"
                      method="post"
                      fit="true"></form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submit()">确认选择</el-button>
                <el-button @click="showDialog=false">返回</el-button>
            </section>
        </el-dialog>
    </div>

</template>

<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import treeselect from '@riophae/vue-treeselect'
    import '@riophae/vue-treeselect/dist/vue-treeselect.css'
    import { filterVueTree } from 'src/assets/js/utility/rb-filters.js'

    export default {
        // name: 'list'
        components: {
            MaintainDialog: () => import('src/pages/resource/collectUnknown/list/maintain-dialog.vue'),
            treeselect
        },
        data() {
            return {
                loading: false,
                condition: {
                    handleStatus: 0
                },
                pageInfo: {
                    currentPage: 1, // 当前页
                    pageSize: 50, // 分页每页多少行数据
                    pageSizes: [10, 20, 50, 100], // 每页多少行数组
                    total: 0, // 总共多少行数据
                },
                idcTypeList: [],
                deviceTypes: [],
                tableData: [],
                dataForm: ['监控上报', '自动采集', '苏研数据'],
                handleStatus: [
                    { name: '待处理', value: 0 },
                    { name: '已维护', value: 1 },
                    { name: '已屏蔽', value: 2 }
                ],
                processInfo: {
                    showProcess: false,
                    processId: ''
                },
                showDialog: false,
                newModuleId: {
                    id: null,
                    label: null
                },
                dialogData: {},
                selectData: {},
                parentData: {},
                moduleLists: [],
                catalogId: '96603733-5793-11ea-ab96-fa163e982f89'
            }
        },
        mounted() {
            this.getIdcTypList()
            this.getDeviceType()
            this.getUnknownList()
            this.initData()
        },
        methods: {
            initData() {
                this.moduleLists = []
                // 获取默认的主机分类列表
                rbCmdbModuleServiceFactory.getModuleTreeByCatalogIdOrModuleId({ 'catalogId': this.catalogId, 'moduleId': '' }).then((data) => {
                    if (data && data.length > 0) {
                        this.moduleLists = filterVueTree(data, {
                            id: 'id',
                            label: 'name',
                            children: 'childModules',
                            isDisabled: (item) => this.filterDisabled(item),
                            isDisplay: 'isDisplay',
                            autoAddRoot: false
                        })
                    }
                }).catch((e) => {
                    console.error(e)
                })
            },
            filterDisabled(item) {
                if (item.isDisplay && item.isDisplay === 1) {
                    return true
                }
                if (item.childModules && item.childModules.length > 0) {
                    return true
                }
                return false
            },
            selectDepart(val) {
                this.selectData = {}
                this.selectData = val
                let arrNew = []
                this.parentData = {}
                arrNew = JSON.parse(JSON.stringify(this.moduleLists[0].children))
                arrNew.forEach(item => {
                    item.children && item.children.filter(key => {
                        if (key.id === this.selectData.id) {
                            this.parentData = item
                        }
                    })

                })
            },
            async submit() {
                let row = {}
                row = this.dialogData
                let queryParams = {
                    ip: row.ip,
                    idcType: row.idcType,
                    'device_class': { id: this.parentData.id, name: this.parentData.label },
                    'device_type': { id: this.selectData.id, name: this.selectData.label },
                    moduleId: this.selectData.id,
                    unknownId: row.id,
                    device_name: row.deviceName,
                    state: 'unknown'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
            },
            toDetail(instanceId) {
                let _t = this
                rbCmdbServiceFactory.getModuleByInstanceId(instanceId).then((data) => {
                    if (data) {
                        let queryParams = {
                            moduleId: data.module.id,
                            name: data.module.name,
                            instanceId: instanceId,
                            state: 'detail'
                        }
                        queryParams = JSON.stringify(queryParams)
                        this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
                    } else {
                        _t.$message.error('获取模型数据失败')
                    }
                })
            },
            async toCreate(row) {
                if (!row.deviceType) {
                    // this.$message.warning('当前设备类型为空，无法查找模型')
                    this.dialogData = {}
                    this.dialogData = row
                    this.showDialog = true
                    return
                }
                let flag = false
                await rbCmdbServiceFactory.getDictsByType({ type: 'idcType' }).then((res) => {
                    res.forEach(item => {
                        if (item.id === row.idcType) {
                            flag = true
                        }
                    })
                })
                if (!flag) {
                    this.$message.warning('当前资源池：' + row.idcType + '不存在')
                    return
                }
                let deviceClassName = await rbConfigDictServiceFactory.getDictById({ dictId: row.deviceClass }).then((res) => {
                    return res.dictNote
                })
                let queryParams = {
                    ip: row.ip,
                    idcType: row.idcType,
                    'device_class': { id: row.deviceClass, name: deviceClassName },
                    'device_type': { id: row.deviceType, name: row.deviceTypeName },
                    moduleId: row.moduleId,
                    unknownId: row.id,
                    device_name: row.deviceName,
                    state: 'unknown'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })

                // rbCmdbModuleServiceFactory.getModuleSelective({ 'name': row.deviceType }).then((res) => {
                //     rbCmdbModuleServiceFactory.getModuleSelective({ 'id': res[0].parentId }).then((parent) => {
                //         let queryParams = {
                //             ip: row.ip,
                //             idcType: row.idcType,
                //             deviceClass: parent[0].name,
                //             deviceType: res[0].name,
                //             moduleId: res[0].id,
                //             unknownId: row.id,
                //             deviceName: row.deviceName,
                //             state: 'unknown'
                //         }
                //         queryParams = JSON.stringify(queryParams)
                //         this.$router.push({ path: '/resource/iframe/add', query: { queryParams: queryParams } })
                //     })

                // }).catch(() => {
                //     this.$message.warning('当前不存在模型：' + row.deviceType + '.请先创建模型')
                // })

            },
            update(row, status) {
                let data = {
                    id: row.id,
                    handleStatus: status,
                }
                // row.handleStatus = status
                rbCmdbServiceFactory.updateCollectUnknown(data).then((res) => {
                    if (res.success) {
                        this.$message.success(res.message)
                        this.search()
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            search() {
                this.pageInfo.currentPage = 1
                this.getUnknownList()

            },
            cancel() {
                this.condition = {}
            },
            exportData() {
                rbCmdbServiceFactory.exportCollectUnknown(this.condition).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '未知设备表.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            },
            getIdcTypList() {
                rbCmdbServiceFactory.getDictsByType({ 'type': 'idcType' }).then((res) => {
                    this.idcTypeList = res
                })
            },
            getDeviceType() {
                rbCmdbServiceFactory.getDictsByType({ 'type': 'device_type' }).then((res) => {
                    this.deviceTypes = res
                })
            },
            getUnknownList() {
                this.loading = true
                this.tableData = []
                this.$set(this.condition, 'pageSize', this.pageInfo.pageSize)
                this.$set(this.condition, 'pageNum', this.pageInfo.currentPage)
                rbCmdbServiceFactory.getCollectUnknownList(this.condition).then((res) => {
                    this.tableData = res.data
                    this.pageInfo.total = res.count
                }).finally(() => {
                    this.loading = false
                })
            },
            allMaintain() {
                this.$confirm('确认开始创建当前条件下的CI设备吗?').then(() => {
                    this.loading = true
                    rbCmdbServiceFactory.maintainUnknownList(this.condition).then((res) => {
                        this.loading = false
                        if (res.flag === 'true') {
                            // this.$message.success('审核成功')
                            this.processInfo.showProcess = true
                            this.processInfo.processId = res.processId
                        } else {
                            // this.$message.error('审核失败')
                        }
                    }).finally(() => {
                        this.loading = false
                    })
                })
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageInfo.pageSize = val
                this.getUnknownList()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.pageInfo.currentPage = val
                this.getUnknownList()
            },
        }
    }
</script>

<style scoped>
</style>
