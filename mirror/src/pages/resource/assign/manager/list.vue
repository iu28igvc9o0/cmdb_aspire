<template>
    <div class="component-container"
         v-loading="loading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="75px">
            <el-form-item label="资源池">
                <!--<el-input v-model="queryForm.resourcePool" clearable></el-input>-->
                <el-select v-model="queryForm.resourcePool"
                           clearable>
                    <el-option v-for="(item, index) in resourcePoolList"
                               :key="index"
                               :value="item.name"
                               :label="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="一级租户">
                <el-select v-model="queryForm.department1"
                           @change="getDepartment2($event)"
                           clearable>
                    <el-option v-for="(item, index) in department1List"
                               value-key="id"
                               :key="index"
                               :value="item"
                               :label="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="二级租户">
                <el-select v-model="queryForm.department2"
                           value-key="id"
                           @change="getBizSysList"
                           clearable>
                    <el-option v-for="(item, index) in department2List"
                               :key="index"
                               :label="item.name"
                               :value="item">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="业务系统">
                <el-select v-model="queryForm.bizSystem"
                           clearable>
                    <el-option v-for="(item, index) in bizSystemList"
                               :key="index"
                               :label="item.bizSystem"
                               :value="item.bizSystem">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询</el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>
        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-download"
                           @click="importAssign()">导入</el-button>
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="exportAssign()">导出</el-button>
            </div>
            <div class="yw-el-table-wrap"
                 style="margin-top:10px">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 300px)">
                    <el-table-column label="资源池名称"
                                     prop="resourcePool"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="类型"
                                     prop="type"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="一级租户"
                                     prop="department1"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="二级租户"
                                     prop="department2"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="业务系统"
                                     prop="bizSystem"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="租户资源需求"
                                     prop="departNeedCount"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="已建设量"
                                     prop="builtCount"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="已分配量"
                                     prop="assignedCount"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="预分配量"
                                     prop="preAssignCount"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="未建设量"
                                     prop="unBuiltCount"
                                     min-width="100"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="操作"
                                     width="80"
                                     fixed="right">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           icon="el-icon-delete"
                                           @click="deleteAssign(scope.row.id)">
                                </el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-form>
        <!-- dialog -->
        <YwImport ref="importInstances"
                  v-if="importData.isImport"
                  :showImport="importData.isImport"
                  :dataStart="importData.dataStart"
                  @setImportDisplay="setImportDisplay"
                  :importType="importData.importType"></YwImport>
    </div>
</template>

<script>
    import rbAssignServiceFactory from 'src/services/cmdb/rb-assign-services.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import configDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'

    export default {
        // name: 'list'
        components: {
            YwImport: () => import('src/components/common/yw-import.vue')
        },
        data() {
            return {
                queryForm: {
                    resourcePool: '',
                    department1: '',
                    department2: '',
                    bizSystem: ''
                },
                resourcePoolList: [],
                department1List: [],
                department2List: [],
                bizSystemList: [],
                tableData: [],
                loading: false,
                // 导入
                importData: {
                    isImport: false,
                    importType: 'resourceassign',
                    dataStart: 1
                },
            }
        },
        mounted() {
            this.getResourcePool()
            this.getDepartment1()
            this.getDepartment2()
            this.getBizSysList()
            this.query()
        },
        methods: {
            importAssign() {
                this.importData.isImport = true
            },
            // 导入弹窗
            setImportDisplay(val) {
                this.importData.isImport = val
                this.query()
            },
            query() {
                let that = this
                this.loading = true
                let data = {
                    resourcePool: this.queryForm.resourcePool,
                    department1: this.queryForm.department1 ? this.queryForm.department1.name : '',
                    department2: this.queryForm.department2 ? this.queryForm.department2.name : '',
                    bizSystem: this.queryForm.bizSystem
                }
                rbAssignServiceFactory.listAssign(data).then((res) => {
                    that.tableData = res.data
                }).finally(() => {
                    this.loading = false
                })
            },
            exportAssign() {
                this.loading = true
                let data = {
                    resourcePool: this.queryForm.resourcePool,
                    department1: this.queryForm.department1 ? this.queryForm.department1.name : '',
                    department2: this.queryForm.department2 ? this.queryForm.department2.name : '',
                    bizSystem: this.queryForm.bizSystem
                }
                rbAssignServiceFactory.exportAssign(data).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '资源分配分析报表.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                }).finally(() => {
                    this.loading = false
                })
            },
            deleteAssign(id) {
                this.$confirm('确认删除数据？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.loading = true
                    rbAssignServiceFactory.deleteAssign({ id: id }).then((res) => {
                        if (res.success) {
                            this.$message.success(res.message)
                            this.query()
                        } else {
                            this.$message.error(res.message)
                        }
                    }).finally(() => {
                        this.loading = false
                    })
                })
            },
            getResourcePool() {
                rbCmdbServiceFactory.getDictsByType({ 'type': 'idcType' }).then((res) => {
                    if (res) {
                        this.resourcePoolList = res
                    }
                })
            },
            // 获取部门
            getDepartment1() {
                rbCmdbServiceFactory.getDictsByType({ 'type': 'department1' }).then((res) => {
                    if (res) {
                        this.department1List = res
                    }
                })
            },
            getDepartment2(item) {
                this.queryForm.department2 = ''
                this.department2List = []
                let pid = ''
                if (item === null) {
                    pid = ''
                } else {
                    pid = this.queryForm.department1.id
                }
                let params = { 'type': 'department2', 'pid': pid || '' }
                rbCmdbServiceFactory.getDictsByType(params).then((res) => {
                    if (res) {
                        this.department2List = res
                    }
                })
                this.getBizSysList(item)
            },
            // 获取业务系统
            getBizSysList() {
                this.queryForm.bizSystem = ''
                this.bizSystemList = []
                let data = {
                    'department1': this.queryForm.department1.name,
                    'department2': this.queryForm.department2.name
                }
                configDictServiceFactory.getBizSystem(data).then((res) => {
                    this.bizSystemList = res
                })
                // let pid = ''
                // if (this.queryForm.department2 !== null &&  this.queryForm.department2 !== '') {
                //   pid = this.queryForm.department2.id
                // } else if (this.queryForm.department1 !== null &&  this.queryForm.department1 !== ''){
                //   pid = this.queryForm.department1.id
                // }
                // let params = {'type': 'bizSystem', 'pid': pid || ''}
                // rbCmdbServiceFactory.getDictsByType(params).then((res) => {
                //   if (res) {
                //     this.bizSystemList = res
                //   }
                // })
            },
            reset() {
                this.queryForm = {
                    resourcePool: '',
                    department1: '',
                    department2: '',
                    bizSystem: ''
                }
                this.query()
            }
        }
    }
</script>

<style scoped>
</style>
