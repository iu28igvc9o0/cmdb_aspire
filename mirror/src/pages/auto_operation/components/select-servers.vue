<template>
    <!-- 选择服务器 -->
    <div class="host-box">
        <!-- 自动化-非巡检任务-cmdb目标主机选择 -->
        <template v-if="from !== 'inspectionTask'">
            <el-button type="primary"
                       :disabled="custom_target_hosts && isAutoRepair"
                       @click="showDialog">
                选择服务器
            </el-button>
            <el-button type="primary"
                       @click="exportMachine">
                导出
            </el-button>
            <el-button type="primary"
                       @click="importMachine">
                导入
            </el-button>
            <el-checkbox v-show="isAutoRepair"
                         v-model="custom_target_hosts"
                         class="mleft10"
                         label="模版属性"></el-checkbox>
            <el-select class="limit-width"
                       v-if="custom_target_hosts && isAutoRepair"
                       v-model="custom_target_hosts_value"
                       placeholder="请选择属性类型">
                <el-option value="A"
                           label="追加"></el-option>
                <el-option value="R"
                           label="替换"></el-option>
            </el-select>
            <!-- 已选中列表 -->
            <div class="yw-el-table-wrap mtop10">
                <el-table :data="selectedDataList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border>
                    <el-table-column :key="index"
                                     v-for="(column, index) in tableColumns"
                                     :prop="column.prop"
                                     :label="column.label"
                                     :width="column.width"
                                     :min-width="column.minWidth"
                                     :show-overflow-tooltip="column.showTooltip">
                        <template slot-scope="scope">
                            {{scope.row[column.prop2] || scope.row[column.prop]}}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作"
                                     fixed="right"
                                     width="60">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="removeRow(scope.$index)"></el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </template>

        <!-- 待选择数据列表 -->
        <!-- local 类型主机 -->
        <el-dialog class="yw-dialog"
                   title="选择目标机器"
                   :visible.sync="dialogShow"
                   :append-to-body="true"
                   width="1050px"
                   height="300"
                   :close-on-click-modal="false">
            <el-form class="yw-form">
                <section class="yw-dialog-main"
                         v-if="!isCmdbType">
                    <el-tabs v-model="activeName">
                        <el-tab-pane label="选择目标机器"
                                     name="first">
                            <el-form class="components-condition yw-form"
                                     :model="serverSearch"
                                     label-position='left'
                                     @keyup.enter.native="search(1)"
                                     ref="serverSearch"
                                     :inline="true"
                                     label-width="50px">
                                <el-form-item label="ip"
                                              prop="agent_ip">
                                    <el-input v-model="serverSearch.agent_ip"
                                              placeholder="请输入ip"
                                              clearable></el-input>
                                </el-form-item>
                                <el-form-item label="资源池"
                                              prop="pool">
                                    <el-input v-model="serverSearch.pool"
                                              placeholder="请输入资源池"
                                              clearable></el-input>
                                </el-form-item>
                                <el-form-item label="状态"
                                              prop="status">
                                    <el-input v-model="serverSearch.status"
                                              placeholder="请输入状态"
                                              clearable></el-input>
                                </el-form-item>
                                <section class="btn-wrap">
                                    <el-button type="primary"
                                               @click="search(1)">查询</el-button>
                                    <el-button @click="reset()">重置</el-button>
                                </section>
                            </el-form>
                            <el-table ref="dataTable"
                                      :data="dataList"
                                      class="yw-el-table mtop10"
                                      stripe
                                      border
                                      width="100%"
                                      height="300"
                                      v-loading="loading"
                                      @select="handleSelectionChange"
                                      @select-all="handleSelectionChange">
                                <el-table-column type="selection"></el-table-column>
                                <template v-for="(column, index) in tableColumns">
                                    <el-table-column :key="index"
                                                     :prop="column.prop"
                                                     :label="column.label"
                                                     :width="column.width"
                                                     :show-overflow-tooltip="column.showTooltip">
                                    </el-table-column>
                                </template>
                            </el-table>
                            <div class="yw-page-wrap">
                                <el-pagination @size-change="handleSizeChange"
                                               @current-change="handleCurrentChange"
                                               :current-page="currentPage"
                                               :page-sizes="pageSizes"
                                               :page-size="pageSize"
                                               :total="total"
                                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                            </div>

                        </el-tab-pane>
                        <el-tab-pane label="选择目标业务"
                                     name="second">
                            <el-button type="primary"
                                       @click="addList">新增</el-button>
                            <el-table ref="serviceTable"
                                      :data="serviceList"
                                      class="yw-el-table mtop10"
                                      stripe
                                      border
                                      width="100%"
                                      height="200">
                                <el-table-column label="资源池"
                                                 prop="pool_name">
                                    <template slot-scope="scope">
                                        <el-select v-model="scope.row.pool_name"
                                                   placeholder="请选择"
                                                   filterable
                                                   clearable>
                                            <el-option v-for="item in idcTypeOptions"
                                                       :key="item.value"
                                                       :label="item.label"
                                                       :value="item.value">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="一级部门"
                                                 prop="department1">
                                    <template slot-scope="scope">
                                        <el-select v-model="scope.row.department1"
                                                   @change="queryDepartment(scope.row,scope.$index)"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="item in department1List"
                                                       :key="item.id"
                                                       :label="item.name"
                                                       :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="二级部门"
                                                 prop="department2">
                                    <template slot-scope="scope">
                                        <el-select v-model="scope.row.department2"
                                                   @change="queryDepartment2(scope.row,scope.$index)"
                                                   placeholder="请选择"
                                                   filterable
                                                   clearable>
                                            <el-option v-for="item in department2List"
                                                       :key="item.id"
                                                       :label="item.name"
                                                       :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="业务系统"
                                                 prop="biz_system">
                                    <template slot-scope="scope">
                                        <el-select v-model="scope.row.biz_system"
                                                   placeholder="请选择"
                                                   filterable
                                                   clearable>
                                            <el-option v-for="item in bizSystemList"
                                                       :key="item.id"
                                                       :label="item.bizSystem"
                                                       :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="已交维"
                                                 prop="os_status">
                                    <template slot-scope="scope">
                                        <el-select v-model="scope.row.os_status"
                                                   placeholder="请选择"
                                                   clearable>
                                            <el-option v-for="item in options"
                                                       :key="item.value"
                                                       :label="item.key"
                                                       :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作系统"
                                                 prop="os_type">
                                    <template slot-scope="scope">
                                        <el-select v-model="scope.row.os_type"
                                                   placeholder="请选择"
                                                   clearable>
                                            <el-option v-for="item in osTypeList"
                                                       :key="item.id"
                                                       :label="item.key"
                                                       :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作">
                                    <template slot-scope="scope">
                                        <el-button type="text"
                                                   @click="deleteList(scope.$index)"
                                                   title="删除"
                                                   icon="el-icon-delete"></el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-tab-pane>
                    </el-tabs>
                </section>

                <!-- cmdb 类型主机 -->
                <section class="yw-dialog-main no-scroll"
                         v-if="isCmdbType">
                    <el-form ref="serverSearch"
                             label-width="90px"
                             :inline="true"
                             :model="cmdbServerSearch">
                        <template v-for="(item, i) in settingRelationList">
                            <el-form-item :label="item.cmdbCode.filedName"
                                          :prop="item.cmdbCode.filedCode"
                                          class="mleft20"
                                          :key="i">
                                <el-select clearable
                                           filterable
                                           @change="selectChange(item.cmdbCode.filedCode, $event)"
                                           v-if="item.cmdbCode.isBindSource === '是'"
                                           v-model="cmdbServerSearch[item.cmdbCode.filedCode]"
                                           :placeholder="'请选择' + item.cmdbCode.filedName">
                                    <el-option v-for="(v,index) in formOptions[item.cmdbCode.filedCode]"
                                               :key="v.id + index"
                                               :label="v.key"
                                               :value="v.id"></el-option>
                                </el-select>
                                <el-input v-else-if="item.cmdbCode.isBindSource === '否'"
                                          v-model="inputs[item.cmdbCode.filedCode]"
                                          :placeholder="'请输入' + item.cmdbCode.filedName"></el-input>
                            </el-form-item>
                        </template>
                        <el-form-item class="mleft20">
                            <el-button type="primary"
                                       @click="search()">查询</el-button>
                            <el-button @click="reset()">重置</el-button>
                        </el-form-item>
                    </el-form>
                    <div class="table">
                        <section class="yw-dialog-main no-scroll">
                            <el-table ref="dataTable"
                                      :data="dataList"
                                      class="yw-el-table"
                                      stripe
                                      tooltip-effect="dark"
                                      border
                                      height="300"
                                      v-loading="loading"
                                      @selection-change="handleSelectionChange">
                                <el-table-column type="selection"></el-table-column>
                                <el-table-column width="100"
                                                 prop="device_type_dict_note_name"
                                                 show-overflow-tooltip
                                                 label="设备类型"></el-table-column>
                                <el-table-column width="100"
                                                 prop="device_model"
                                                 show-overflow-tooltip
                                                 label="设备型号"></el-table-column>
                                <el-table-column width="100"
                                                 prop="device_mfrs_value_name"
                                                 show-overflow-tooltip
                                                 label="制造厂商 "></el-table-column>
                                <el-table-column width="120"
                                                 prop="agent_ip"
                                                 show-overflow-tooltip
                                                 label="agent_ip"></el-table-column>
                                <el-table-column width="120"
                                                 prop="ip"
                                                 show-overflow-tooltip
                                                 label="IP地址"></el-table-column>
                                <el-table-column width="100"
                                                 prop="ipmi_ip"
                                                 show-overflow-tooltip
                                                 label="IPMI地址"></el-table-column>
                                <el-table-column width="120"
                                                 prop="idcType_idc_name_name"
                                                 show-overflow-tooltip
                                                 label="资源池名称"></el-table-column>
                                <el-table-column width="100"
                                                 prop="project_name_project_name_name"
                                                 show-overflow-tooltip
                                                 label="项目名称"></el-table-column>
                                <el-table-column width="100"
                                                 prop="pod_name_pod_name_name"
                                                 show-overflow-tooltip
                                                 label="POD名称"></el-table-column>
                                <el-table-column width="100"
                                                 prop="department1_orgName_name"
                                                 show-overflow-tooltip
                                                 label="一级部门"></el-table-column>
                                <el-table-column width="100"
                                                 prop="department2_orgName_name"
                                                 show-overflow-tooltip
                                                 label="二级部门"></el-table-column>
                                <el-table-column width="100"
                                                 prop="bizSystem_bizSystem_name"
                                                 show-overflow-tooltip
                                                 label="业务系统"></el-table-column>
                                <el-table-column width="100"
                                                 prop="os_detail_version"
                                                 show-overflow-tooltip
                                                 label="os详细版本"></el-table-column>
                                <el-table-column width="100"
                                                 prop="ops"
                                                 show-overflow-tooltip
                                                 label="联系人"></el-table-column>
                                <el-table-column width="100"
                                                 prop="device_description"
                                                 show-overflow-tooltip
                                                 label="备注"></el-table-column>
                            </el-table>
                        </section>
                    </div>
                </section>

                <div class="yw-page-wrap"
                     v-if="isCmdbType">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>

                <section class="btn-wrap">
                    <el-button type="primary"
                               @click="addToSelectedList">添加</el-button>
                    <el-button @click="hideDialog">取消</el-button>
                </section>
            </el-form>
        </el-dialog>

        <!-- 导入目标机器 -->
        <!-- <importInstances ref="targetHost"
                         v-if="isImportShow"
                         :showImport="isImportShow"
                         @successHostList="successHostList"
                         @setImportDisplay="setImportDisplay"
                         importType="targetHost"></importInstances> -->

        <el-dialog class="yw-dialog"
                   title="导入Excel新增或更新数据"
                   :visible.sync="importShow"
                   width="500px">
            <el-form ref="importPanel"
                     class="demo-ruleForm">
                <el-form-item align="center">
                    <el-upload class="upload-demo"
                               drag
                               :http-request="UploadFile">
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    </el-upload>
                </el-form-item>
                <el-form-item align="center">
                    <el-button type="primary"
                               @click="downloadTem">还没有Excel模版?请下载模版</el-button>
                </el-form-item>
                <el-form-item>
                    <div style="font: 14px Small"
                         v-if="showFinish">
                        导入Excel数据结束, 共<span style="color: #409EFF; font-weight: bold;"> {{totalCount}} </span>条记录,
                        已处理<span style="color: #409EFF; font-weight: bold;"> {{processCount}} </span>条,
                        成功<span style="color: #67C23A; font-weight: bold;"> {{successCount}} </span>条,
                        失败<span style="color: #F56C6C; font-weight: bold;"> {{failCount}} </span>条。
                        <el-button style="text-decoration:none; color: #F56C6C; font-weight: bold;"
                                   type="text"
                                   align="left"
                                   @click="importErrorList">
                            失败列表
                        </el-button>

                    </div>
                </el-form-item>
            </el-form>
        </el-dialog>

        <el-dialog class="yw-dialog"
                   title="失败列表"
                   :visible.sync="showError"
                   width="500px">
            <section>
                <el-table ref="serviceTable"
                          :data="errorList"
                          class="yw-el-table mtop10"
                          stripe
                          border
                          width="100%"
                          height="200">
                    <el-table-column label="资源池"
                                     prop="pool">
                    </el-table-column>
                    <el-table-column label="ip"
                                     prop="agent_ip">
                    </el-table-column>
                    <el-table-column label="操作系统"
                                     prop="host_os_type">
                    </el-table-column>
                    <el-table-column label="失败原因"
                                     prop="error_reason">
                    </el-table-column>
                </el-table>
            </section>
        </el-dialog>

    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationListChooseMixin from 'src/services/auto_operation/rb-auto-operation-list-choose-mixin.js'
    import rbAutoOperationServicesFactory from 'src/services/auto_operation/rb-auto-operation-services.factory.js'
    import cmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'
    import YwCodeFrameOption from 'src/components/common/yw-code-frame/yw-code-frame.js'
    import { createDownloadFile } from 'src/utils/utils.js'
    import importInstances from 'src/pages/cmdb/v2/instance/import/import-instances.vue'

    import CmdbFactory from 'src/services/auto_operation/rb-auto-operation-cmdb.factory.js'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        name: 'SelectServersComponent',
        components: { importInstances },
        props: {
            dataSelected: {
                type: Array,
                default() {
                    return []
                }
            },
            targetObjectList: {
                type: Array,
                default() {
                    return []
                }
            },
            serversSelectShow: {
                type: Boolean,
                default() {
                    return false
                }
            },
            isAutoRepair: {
                type: Boolean,
                default() {
                    return false
                }
            },
            rowData: {
                type: Object,
                default() {
                    return {}
                }
            },
            // 自动化-巡检任务-cmdb目标主机选择
            inspectionTaskCmdbBoxShow: {
                type: Boolean,
                default() {
                    return false
                }
            },
            from: {
                type: String,
                default: ''
            }
        },
        data() {
            return {
                serviceList: [],
                idcTypeOptions: [],
                department1List: [],
                department1Id: '',
                department1Index: '',
                scopeIndex: '',
                osTypeList: [],
                department2List: [],
                bizSystemList: [],
                bizSystemLists: [],
                filterBiz: [],// 过滤一级部门业务系统
                filterBizSys: [],// 过滤二级部门业务系统

                errorList: [],
                showError: false,
                importShow: false,
                showFinish: false,
                totalCount: 0,
                processCount: 0,
                successCount: 0,
                failCount: 0,

                isImportShow: false,
                activeName: 'first',
                options: [],
                loading: false,
                ops_agent_loadfrom: '', // 主机类型

                serversCmdbBoxShow: false,
                serversBoxShow: false, // 主机选择弹框

                serverSearch: {
                    agent_ip: '',
                    pool: '',
                    status: ''
                },

                cmdbServerSearch: {
                },
                dialogShow: false,
                dataList: [],   // 可选择列表
                curKey: 'agent_ip',   // 比对是否选中该行的key
                curPropKey: 'target_host_list',   // 接口返回已选中的列表
                passDataType: 'Array',   // 返回已选中的数据类型
                custom_target_hosts: false,
                custom_target_hosts_value: '',

                // cmdb类型
                data: [],
                settingRelationList: [],
                formOptions: {},
                searchKeys: {},
                searchKey: [],
                restSQL: {},
                inputs: {},

                tableColumns: [
                    {
                        prop: 'pool',
                        label: '资源池',
                        width: '',
                        minWidth: '180',
                        showTooltip: true,
                    },
                    {
                        prop: 'host_os_type',
                        prop2: 'hostOsType',
                        label: '操作系统',
                        width: '110',
                    },
                    {
                        prop: 'agent_ip',
                        prop2: 'agentIp',
                        label: 'IP',
                        width: '110',
                    },
                    {
                        prop: 'status',
                        label: '状态',
                        width: '110',
                    },
                ],
            }
        },
        mixins: [rbAutoOperationMixin, rbAutoOperationListChooseMixin, CommonOption, YwCodeFrameOption],
        watch: {
            // department1List: {
            //     handler(newVal) {
            //         if (newVal.length) {
            //             console.log(this.department1List)
            //             this.department1List.forEach((item, index) => {
            //                 console.log(item)
            //                 if (item.name === this.serviceList[this.scopeIndex].department1) {
            //                     this.department1Index = index
            //                     console.log('this.department1Index', this.department1Index)
            //                 }
            //             })
            //         }
            //     },
            //     deep: true,
            //     immediate: true
            // },
            dataSelected: {
                handler(val) {
                    this.selectedDataList = val
                },
                immediate: true,
                deep: true,
            },
            targetObjectList: {
                handler(val) {
                    if (val) {
                        this.serviceList = val
                        this.serviceList.forEach((item, index) => {
                            let req = {
                                type: 'department2',
                                pid: item.department1
                            }
                            cmdbService
                                .getDictsByType(req)
                                .then(res => {
                                    this.department2List = res
                                })
                        })
                        console.log('this.serviceList', this.serviceList)

                    }
                },
                immediate: true,
                deep: true,
            },
            custom_target_hosts: {
                handler(val) {
                    this.$emit('updateCustomTargetHosts', val)
                },
                immediate: true,
                deep: true,
            },
            custom_target_hosts_value: {
                handler(val) {
                    this.$emit('updateCustomTargetHostsValue', val)
                },
                immediate: true,
                deep: true,
            },
            inspectionTaskCmdbBoxShow: {
                handler(val) {
                    if (val) {
                        this.showDialog()
                    }
                },
                immediate: true,
                deep: true,
            },
            dialogShow: {
                handler(val) {
                    if (!val && this.from === 'inspectionTask') {
                        this.$emit('close', '1')
                    }
                },
                immediate: true,
                deep: true,
            },
            rowData: {
                handler(val) {
                    // 还原模版参数
                    val.replace_attrs && val.replace_attrs.forEach(item => {
                        this['custom_' + item.replace_attr] = true
                        this['custom_' + item.replace_attr + '_value'] = item.replace_type
                    })
                },
                immediate: true,
                deep: true,
            },
        },
        computed: {
            isCmdbType() {
                return this.ops_agent_loadfrom === 'cmdb' ? true : false
            },
        },
        created() {
            this.init()
        },
        methods: {
            exportMachine() {
                rbAutoOperationServicesFactory.exportStepTargetIp(this.rowData.step_id).then(res => {
                    if (res) {
                        this.$message.success('下载成功')
                        createDownloadFile(res, '目标机器列表.xls')
                    }
                })
            },
            downloadTem() {
                rbCmdbServiceFactory.downloadTargetHostTemplate().then(res => {
                    let blob = new Blob([res], { type: 'application/msword' })
                    // 创建下载链接
                    let objectUrl = URL.createObjectURL(blob)
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '配置模板.xlsx'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            },
            importMachine() {
                this.importShow = true
            },
            importErrorList() {
                this.showError = true
            },
            UploadFile(param) {
                const formData = new FormData()
                formData.append('file', param.file)
                rbCmdbServiceFactory.importTargetHost(formData).then(res => {
                    if (res.flag === 'true') {
                        if (res.errorHostList.length > 0) {
                            this.failCount = res.errorHostList.length
                            this.showErrorList = true
                            this.errorList = []
                            this.errorList = res.errorHostList
                        }
                        if (res.successHostList.length > 0) {
                            this.successCount = res.successHostList.length
                            this.selectedDataList = res.successHostList
                            this.$emit('successHostList', res.successHostList)
                        }
                        this.totalCount = this.successCount + this.failCount
                        this.showFinish = true
                    }
                })
            },
            successHostList(data) {
                this.selectedDataList = this.selectedDataList.concat(data)
            },
            setImportDisplay(val) {
                this.isImportShow = val
            },
            async init() {
                this.ops_agent_loadfrom = sessionStorage.getItem('ops_agent_loadfrom')
                if (this.isCmdbType) {
                    await this.getSearchData()
                }
                this.search(1)
                this.queryDepartment1()
                this.queryBizSystemList()
                this.getOsType()
                this.getJwType()
                await this.queryConditionList({ condicationCode: 'cond_monitor_screen_view' })
                this.idcTypeList = this.conditionList.filter(item => {
                    return item.key === 'idcType'
                })
                this.getIdcType(this.idcTypeList[0].frameDatas.codeObj.codeId, 'idcType')
            },
            // 查询列表
            search(num) {
                if (this.isCmdbType) {
                    this.getCmdbServerlist(1)
                } else {
                    this.getServerlist(num)
                }
            },
            reset() {
                this.serverSearch = {
                    agent_ip: '',
                    pool: '',
                    status: ''
                }
                this.$refs['serverSearch'].resetFields()
                this.search(1)
            },
            // local 类型主机列表
            getServerlist(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    page_no: this.currentPage,
                    page_size: this.pageSize
                }
                req = Object.assign(req, this.serverSearch)
                // 防止翻页时触发selection-change，导致已选数据被清空
                this.initPage = true
                this.dataList = []
                this.loading = true
                rbAutoOperationServicesFactory
                    .fetchUserAuthedAgentHostList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalCount
                        this.dataList = res.dataList
                    })
            },

            // cmdb 类型主机列表
            // 获取查询条件
            async getSearchData() {
                await CmdbFactory.getCmdbCondition().then(res => {
                    if (!res) {
                        this.$message.warning('获取查询条件异常！')
                        return
                    }
                    this.data = res
                    this.settingRelationList = []
                    res.settingRelationList.forEach(res => {
                        let cmdbCode = res.cmdbCode
                        if (cmdbCode.filedCode !== 'is_install_agent') {
                            this.settingRelationList.push(res)
                            this.searchKeys[cmdbCode.filedCode] = cmdbCode.cascadeList
                            this.searchKey.push(cmdbCode.filedCode)
                            this.cmdbServerSearch[cmdbCode.filedCode] = ''
                            // if (cmdbCode.isBindSource === '是') {
                            //     this.getDictsBySql(cmdbCode.filedCode, cmdbCode.codeBindSource)
                            //     this.restSQL[cmdbCode.filedCode] = cmdbCode.codeBindSource
                            // }

                            // 自己数据
                            switch (cmdbCode.codeBindSource && cmdbCode.codeBindSource.bindSourceType) {
                                case '数据字典':
                                    this.getDictByType(cmdbCode)
                                    break
                                case '数据表':
                                    this.getDictsBySql(cmdbCode.filedCode, cmdbCode.codeBindSource.tableSql)
                                    this.restSQL[cmdbCode.filedCode] = cmdbCode.codeBindSource
                                    break
                                case '引用模型':
                                    this.getRefModuleDict(cmdbCode)
                                    this.restSQL[cmdbCode.filedCode] = cmdbCode.codeBindSource
                                    break
                            }
                        } else {
                            this.cmdbServerSearch[cmdbCode.filedCode] = '11462'
                        }
                    })
                    this.cmdbServerSearch['token'] = '5245ed1b-6345-11e'
                    this.cmdbServerSearch['condicationCode'] = 'automatic_agent_search'
                }).catch(error => {
                    this.showErrorTip(error)
                })
            },
            // 数据字典
            getDictByType(item = {}) {
                let params = {
                    type: item.codeBindSource.dictSource
                }
                return rbCmdbServiceFactory.getDictsByType(params).then((data) => {
                    this.setFormOptions(item.filedCode, data)
                    return data
                })
            },
            // 数据表
            getDictsBySql(filedCode, SQL) {
                let obj = {
                    sql: SQL.tableSql
                }
                CmdbFactory.queryTableData(obj).then(res => {
                    this.$set(this.formOptions, filedCode, res)
                }).catch(error => {
                    this.showErrorTip(error)
                })
            },
            // 引用模型
            getRefModuleDict(code) {
                let filedCode = code.filedCode
                let params = {
                    codeId: code.codeId
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((data) => {
                    this.setFormOptions(filedCode, data)
                })
            },
            // 引用模型
            getOsType() {
                let params = {
                    codeId: '1923c442568011e998180242ac110001'
                }

                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    this.osTypeList = res
                })
            },
            // 交维
            getJwType() {
                let params = {
                    codeId: '6b5045f656204d6eb4626b10715ea60b'
                }

                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    this.options = res
                })
            },

            // 下拉框数据
            setFormOptions(filedCode, data) {

                if (!Object.keys(this.formOptions).includes(filedCode)) {
                    this.$set(this.formOptions, filedCode, data)
                } else {
                    this.formOptions[filedCode] = data
                }
            },
            selectChange(code, event) {
                if (!event) {
                    this.searchKeys[code].forEach((v) => {
                        if (v) {
                            if (this.searchKey.indexOf(v.subFiledCode) > -1) {
                                this.cmdbServerSearch[v.subFiledCode] && (this.cmdbServerSearch[v.subFiledCode] = '')
                                this.getDictsBySql(v.subFiledCode, this.restSQL[v.subFiledCode])
                            }
                        } else {
                            this.$forceUpdate()
                        }
                    })
                    Object.keys(this.searchKeys).forEach((v) => {
                        let c = this.searchKeys[v]
                        if (this.cmdbServerSearch[v] && c && c.length) {
                            c.forEach((item) => {
                                this.cmdbServerSearch[item.subFiledCode] && (this.cmdbServerSearch[item.subFiledCode] = '')
                                let newSQL = item.sqlString.split('?').join(this.cmdbServerSearch[v])
                                this.getDictsBySql(item.subFiledCode, { tableSql: newSQL })
                            })
                        } else {
                            this.$forceUpdate()
                        }
                    })
                } else {
                    this.searchKeys[code].forEach((v) => {
                        if (v) {
                            if (this.searchKey.indexOf(v.subFiledCode) > -1 && v.sqlString) {
                                this.cmdbServerSearch[v.subFiledCode] && (this.cmdbServerSearch[v.subFiledCode] = '')
                                let newSQL = v.sqlString.split('?').join(event)
                                this.getDictsBySql(v.subFiledCode, { tableSql: newSQL })
                            }
                        } else {
                            this.$forceUpdate()
                        }
                    })
                }

                this.$forceUpdate()
            },
            getCmdbServerlist(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let obj = {
                    ...this.cmdbServerSearch,
                    ...this.inputs,
                    pageSize: this.pageSize,
                    currentPage: this.currentPage
                }
                this.dataList = []
                this.loading = true
                CmdbFactory.fetchUserAuthedAgentHostList(obj).then(res => {
                    this.total = res.totalCount
                    this.dataList = res.dataList
                    this.loading = false
                    this.$forceUpdate()
                }).catch(error => {
                    this.loading = false
                    this.showErrorTip(error)
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
            queryDepartment(row, index) {
                this.scopeIndex = index
                this.bizSystemList = []
                this.bizSystemList = JSON.parse(JSON.stringify(this.bizSystemLists))
                let newArr = JSON.parse(JSON.stringify(this.department1List))
                let newPar = newArr.filter(item => {
                    return item.id == row.department1
                })
                this.department1Id = ''
                this.department1Id = newPar[0].value
                this.bizSystemList = this.bizSystemList.filter(item => {
                    return item.department1 == this.department1Id
                })
                this.department1List.forEach((item, index) => {
                    if (item.id === this.serviceList[this.scopeIndex].department1) {
                        this.department1Index = index
                    }
                })

                let req = {
                    type: 'department2',
                    pid: this.department1List[this.department1Index].id
                }
                this.serviceList[index].department2 = ''
                cmdbService
                    .getDictsByType(req)
                    .then(res => {
                        this.department2List = []
                        this.department2List = res
                    })
            },
            queryDepartment2(row, index) {
                this.bizSystemList = []
                this.bizSystemList = JSON.parse(JSON.stringify(this.bizSystemLists))
                let newArr = JSON.parse(JSON.stringify(this.department2List))
                let newPar = newArr.filter(item => {
                    return item.id == row.department2
                })
                this.bizSystemList = this.bizSystemList.filter(item => {
                    return item.department1 == this.department1Id && item.department2 == newPar[0].value
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
                        this.bizSystemLists = res
                        this.$store.commit('setBizSystemList', res)
                    })
            },
            // 引用模型
            getIdcType(item, type) {
                let params = {
                    codeId: item
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((res) => {
                    if (type === 'idcType') {
                        this.idcTypeOptions = res
                    }
                    // return res
                })
            },
            addList() {
                this.serviceList.push({
                    pool_name: '',
                    department1: '',
                    department2: '',
                    biz_system: '',
                    os_type: '',
                    os_status: ''

                })
            },
            deleteList(index) {
                this.serviceList.splice(index, 1)
            }
        }
    }
</script>

<style lang="scss" scoped>
    .main {
      display: flex;
      flex-direction: column;
      overflow: auto;
      .el-form {
        display: flex;
        flex-wrap: wrap;
        .el-form-item {
          margin-right: 12px;
          display: flex;
        }
      }
      .table {
        margin-top: 10px;
        .btn-wrap {
          position: absolute;
          bottom: 20px;
          left: 20px;
        }
      }
    }
</style>