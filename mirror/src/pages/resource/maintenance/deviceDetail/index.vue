<template>
    <section class="yw-dialog-main"
             v-loading.fullscreen.lock="pageLoading"
             :element-loading-text="loading_text">
        <div class="table-operate-wrap clearfix">
            <el-button class="btn-icons-wrap"
                       type="text"
                       icon="el-icon-plus"
                       @click="showBox('add')">新增</el-button>
            <el-button class="btn-icons-wrap"
                       type="text"
                       icon="el-icon-download"
                       @click="importMaintenanceProject">导入</el-button>
            <el-button class="btn-icons-wrap"
                       type="text"
                       icon="el-icon-upload2"
                       @click="exportOut"
                       :disabled="can_export">导出</el-button>
        </div>

        <div class="yw-el-table-wrap">
            <el-table class="yw-el-table"
                      :data="dataList"
                      :element-loading-text="loadingText"
                      stripe
                      border
                      size="mini"
                      style="width: 100%;margin-top:10px"
                      height="340px"
                      @selection-change="handleSelectionChange">
                <el-table-column label="规格型号"
                                 align="left"
                                 prop="specificModel"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="配置描述"
                                 align="left"
                                 prop="configDesc"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="部件序列号"
                                 align="left"
                                 prop="serialNumber"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="部件名称"
                                 align="left"
                                 prop="componentName"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="单位"
                                 align="left"
                                 prop="unit"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="数量"
                                 align="left"
                                 prop="quantity"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="不含税单价(元)"
                                 align="left"
                                 width="110px"
                                 prop="unitNotTax"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="不含税价格(元)"
                                 align="left"
                                 width="110px"
                                 prop="moneyNotTax"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="增值税率或征收率(%)"
                                 align="left"
                                 width="140px"
                                 prop="addTax"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="增值税税额(元)"
                                 align="left"
                                 width="110px"
                                 prop="addAmount"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="含税价格(元)"
                                 align="left"
                                 width="100px"
                                 prop="moneyWithTax"
                                 :show-overflow-tooltip="true"></el-table-column>
                <el-table-column label="操作"
                                 align="left"
                                 width="120px"
                                 :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-edit"
                                   @click="showBox('edit', scope.row)">修改</el-button>
                        <el-button class="btn-icons-wrap"
                                   type="text"
                                   icon="el-icon-delete"
                                   @click="remove(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="yw-page-wrap">
            <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="currentPage"
                           :page-sizes="pageSizes"
                           :page-size="pageSize"
                           :total="total"
                           layout="total, sizes, prev, pager, next, jumper"></el-pagination>
        </div>

        <!-- 新增设备弹框 -->
        <el-dialog class="yw-dialog"
                   width="720px"
                   :title="dialogName"
                   :visible.sync="addDeviceBox"
                   :append-to-body="true"
                   :close-on-click-modal="false"
                   :destroy-on-close="true">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :inline="true"
                         :model="submitInfo"
                         ref="deviceForm"
                         :rules="rules"
                         label-width="110px">
                    <el-form-item label="规格型号"
                                  prop="specificModel">
                        <el-input clearable
                                  v-model="submitInfo.specificModel"></el-input>
                    </el-form-item>
                    <el-form-item label="配置描述"
                                  prop="configDesc">
                        <el-input clearable
                                  v-model="submitInfo.configDesc"></el-input>
                    </el-form-item>
                    <el-form-item label="部件序列号"
                                  prop="serialNumber">
                        <el-input clearable
                                  v-model="submitInfo.serialNumber"></el-input>
                    </el-form-item>
                    <el-form-item label="部件名称"
                                  prop="componentName">
                        <el-input clearable
                                  v-model="submitInfo.componentName"></el-input>
                    </el-form-item>
                    <el-form-item label="单位"
                                  prop="unit">
                        <el-select v-model="submitInfo.unit"
                                   clearable
                                   filterable>
                            <el-option v-for="(val, index) in unitList"
                                       :key="index"
                                       :label="val.name"
                                       :value="val.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="数量"
                                  prop="quantity">
                        <el-input clearable
                                  v-model="submitInfo.quantity"></el-input>
                    </el-form-item>
                    <el-form-item label="不含税单价(元)"
                                  prop="unitNotTax">
                        <el-input clearable
                                  v-model="submitInfo.unitNotTax"></el-input>
                    </el-form-item>
                    <el-form-item label="不含税价格(元)"
                                  prop="moneyNotTax">
                        <el-input clearable
                                  v-model="submitInfo.moneyNotTax"></el-input>
                    </el-form-item>
                    <el-form-item label="增值税率(%)"
                                  prop="addTax">
                        <el-input clearable
                                  v-model="submitInfo.addTax"></el-input>
                    </el-form-item>
                    <el-form-item label="增值税税额(元)"
                                  prop="addAmount">
                        <el-input clearable
                                  v-model="submitInfo.addAmount"></el-input>
                    </el-form-item>
                    <el-form-item label="含税价格(元)"
                                  prop="moneyWithTax">
                        <el-input clearable
                                  v-model="submitInfo.moneyWithTax"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="save()">保存</el-button>
                <el-button @click="closeBox()">取消</el-button>
            </section>
        </el-dialog>

        <YwImport v-if="importProjectData.isImport"
                  :showImport="importProjectData.isImport"
                  :dataStart="importProjectData.dataStart"
                  :maintenanceId="deviceId"
                  @setImportDisplay="setShowImportProject"
                  @getSearch="search"
                  :importType="importProjectData.importType"></YwImport>
    </section>
</template>

<script>
    import rbmaintenanceServiceFactory from 'src/services/cmdb/rb-maintenance-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import QueryObject from 'src/utils/queryObject.js'
    export default {
        props: ['deviceId'],
        components: {
            YwImport: () => import('src/components/common/yw-import.vue'),
        },
        data() {
            const validateInteger = (rule, value, callback) => {
                if (value && value.length > 7) {
                    callback(new Error('长度在 1 到 12 个字符'))
                } else if (/(^(1|([1-9]\d{0,11}))$)/.test(value)) {
                    callback()
                } else {
                    callback(new Error('请输入12位以内整数'))
                }
            }
            const validatePositiveInteger = (rule, value, callback) => {
                if (value && value.length > 3) {
                    callback(new Error('长度在 1 到 3 个字符'))
                } else if (/(^(1|([1-9]\d{0,1})|100)$)/.test(value)) {
                    callback()
                } else {
                    callback(new Error('请输入1~100整数'))
                }
            }
            const validateNumFixedTwo = (rule, value, callback) => {
                if (value && value.length > 13) {
                    callback(new Error('长度在 1 到 13 个字符'))
                } else if (/(^[1-9][0-9]*(.[0-9]{1,2})?$)/.test(value)) {
                    callback()
                } else {
                    callback(new Error('请输入非零正整数或两位小数'))
                }
            }
            const validateNumRule = {
                required: true,
                trigger: ['blur', 'change'],
                validator: validateNumFixedTwo
            }
            const validateNormalRule = {
                min: 1,
                max: 50,
                message: '长度在 1 到 50 个字符!',
                trigger: ['blur', 'change']
            }
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                importProjectData: {
                    isImport: false,
                    importType: 'mainten_component'
                },
                can_export: false,
                loading: false,
                loadingText: '正在查询数据, 请稍等...',
                dataList: [],
                unitList: [],
                addDeviceBox: false,
                eventType: 'add', // 触发事件
                dialogName: '新增设备',
                currentData: {},
                // 设备字段
                submitInfo: {
                    deviceName: '',
                    deviceNo: ''
                },

                rules: {
                    fileType: [
                        {
                            required: true,
                            message: '请选择文件类型!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    fileObject: [
                        {
                            required: true,
                            message: '请选择文件对象!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    specificModel: [
                        {
                            required: true,
                            message: '请输入规格型号!',
                            trigger: ['blur', 'change']
                        },
                        validateNormalRule
                    ],
                    configDesc: [
                        {
                            required: true,
                            message: '请输入配置描述!',
                            trigger: ['blur', 'change']
                        },
                        validateNormalRule
                    ],
                    serialNumber: [
                        {
                            required: true,
                            message: '请输入部件序列号!',
                            trigger: ['blur', 'change']
                        },
                        validateNormalRule
                    ],
                    componentName: [
                        {
                            required: true,
                            message: '请输入部件名称!',
                            trigger: ['blur', 'change']
                        },
                        validateNormalRule
                    ],
                    unit: [
                        {
                            required: true,
                            message: '请选择单位!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    quantity: [
                        {
                            required: true,
                            trigger: ['blur', 'change'],
                            validator: validateInteger
                        },
                    ],
                    unitNotTax: [
                        validateNumRule
                    ],
                    moneyNotTax: [
                        validateNumRule
                    ],
                    addTax: [
                        {
                            required: true,
                            trigger: ['blur', 'change'],
                            validator: validatePositiveInteger
                        },
                    ],
                    addAmount: [
                        validateNumRule
                    ],
                    moneyWithTax: [
                        validateNumRule
                    ],
                },
            }
        },
        mixins: [QueryObject, rbAutoOperationMixin],
        watch: {
            deviceId: {
                handler: function (val) {
                    if (val) {
                        this.dataList = []
                        this.search()
                    }
                },
                immediate: true
            }
        },
        mounted() {
            this.getDictsByType()
        },
        methods: {
            // 查询设备列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                    maintenanceId: this.deviceId
                }
                this.loading = true
                rbmaintenanceServiceFactory
                    .queryComponentsList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalSize
                        this.dataList = res.data
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 获取单位
            getDictsByType() {
                let req = {
                    type: 'unit_type'
                }
                rbmaintenanceServiceFactory
                    .getDictsByType(req)
                    .then(res => {
                        this.unitList = res
                    })
            },
            importMaintenanceProject() {
                this.importProjectData.isImport = true
            },
            // 设置导入维保项目显示
            setShowImportProject(val) {
                this.importProjectData.isImport = val
            },
            // 导出
            exportOut() {
                this.pageLoading = true
                this.can_export = true

                rbmaintenanceServiceFactory
                    .exportComponent({
                        maintenanceId: this.deviceId
                    })
                    .then(res => {
                        this.pageLoading = false
                        this.can_export = false
                        this.$message.success('下载完成')
                        this.exportFiles({
                            data: res,
                            fileType: 'application/vnd.ms-excel',
                            fileName: '维保部件清单.xls'
                        })
                    })
                    .catch(() => {
                        this.pageLoading = false
                        this.can_export = false
                    })

            },

            // 提交设备
            saveFile(req, type) {
                this.pageLoading = true
                rbmaintenanceServiceFactory[type](req).then(res => {
                    if (res.flag === 'success') {
                        this.$message.success('保存成功')
                        this.search()
                        this.closeBox()
                        this.pageLoading = false
                    } else {
                        this.$message.error(res.error_tip)
                        this.pageLoading = false
                    }
                })
            },
            // 保存设备
            save() {
                this.$refs.deviceForm.validate(valid => {
                    console.log('valid==', valid)
                    if (!valid) {
                        // this.$message.warning("请先完善信息");
                        return
                    }
                    let req = this.submitInfo
                    req.maintenanceId = this.deviceId
                    if (this.eventType === 'edit') {
                        this.saveFile(req, 'updateComponent')
                    } else {
                        this.saveFile(req, 'saveComponent')
                    }
                })
            },
            // 删除文件
            remove(id) {
                this.$confirm('您确定要删除该设备详情吗？').then(() => {
                    this.loading = true
                    rbmaintenanceServiceFactory.deleteComponent({ id: id }).then(res => {
                        if (res.flag === 'success') {
                            this.$message.success('删除成功')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                            this.loading = false
                        }
                    })
                })
            },
            // 查看文件
            viewDetail(row) {
                if (JSON.stringify(row) !== '{}') {
                    this.submitInfo = row
                }
            },
            // 新建文件
            showBox(type, row) {
                this.eventType = type
                if (row) {
                    this.currentData = JSON.parse(JSON.stringify(row))
                } else {
                    this.currentData = {}
                }
                if (type === 'add') {
                    this.dialogName = '新增设备'
                    this.viewDetail(this.currentData)
                } else if (type === 'edit') {
                    this.dialogName = '编辑设备'
                    this.viewDetail(this.currentData)
                }
                this.addDeviceBox = true
            },
            closeBox() {
                this.submitInfo = {}
                this.addDeviceBox = false
            },
        }
    }
</script>
<style lang="scss" scoped>
</style>

