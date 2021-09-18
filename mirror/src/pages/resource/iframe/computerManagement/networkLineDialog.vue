<template>
    <div>
        <dialog-model ref="dialogModel"
                      :dialogAttribute="dialogAttribute"
                      @handleSureDialog="handleSureDialog"
                      @handleCancelDialog="handleCancelDialog">
            <template slot="content">
                <div class="operate-content">
                    <div class="operate-header">
                        <el-form ref="edgeForm"
                                 :rules="edgeRules"
                                 :model="edgeForm"
                                 label-width="140px"
                                 :inline="true">
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="线路名称："
                                                  prop="server_line">
                                        <el-input v-model="edgeForm.server_line"
                                                  clearable
                                                  :disabled="operationParams.isEdit"></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="资源池："
                                                  prop="idcType">
                                        <el-select v-model="edgeForm.idcType"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in idcTypeOptions"
                                                       :key="index"
                                                       :value="item.id"
                                                       :label="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="网络类型："
                                                  prop="server_network_type">
                                        <el-select v-model="edgeForm.server_network_type"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in networkOptions"
                                                       :key="index"
                                                       :value="item.id"
                                                       :label="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="计费方式："
                                                  prop="server_bill_type">
                                        <el-select v-model="edgeForm.server_bill_type"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in billOptions"
                                                       :key="index"
                                                       :value="item.id"
                                                       :label="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="单位："
                                                  prop="server_bill_unit">
                                        <el-select v-model="edgeForm.server_bill_unit"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in unitOptions"
                                                       :key="index"
                                                       :value="item.id"
                                                       :label="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="数量："
                                                  prop="server_line_count">
                                        <el-input v-model="edgeForm.server_line_count"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="规格："
                                                  prop="server_line_standard">
                                        <el-input v-model="edgeForm.server_line_standard"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="A端："
                                                  prop="server_line_a">
                                        <el-input v-model="edgeForm.server_line_a"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="Z端："
                                                  prop="server_line_b">
                                        <el-input v-model="edgeForm.server_line_b"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item prop="server_line_create_date"
                                                  label="线路新增时间：">
                                        <el-date-picker v-model="edgeForm.server_line_create_date"
                                                        type="date"
                                                        format="yyyy-MM-dd"
                                                        value-format="yyyy-MM-dd"
                                                        clearable
                                                        placeholder="选择日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item prop="server_line_update_date"
                                                  label="线路调整时间：">
                                        <el-date-picker v-model="edgeForm.server_line_update_date"
                                                        type="date"
                                                        format="yyyy-MM-dd"
                                                        value-format="yyyy-MM-dd"
                                                        clearable
                                                        placeholder="选择日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="线路取消时间："
                                                  prop="server_line_cancal_date">
                                        <el-date-picker v-model="edgeForm.server_line_cancal_date"
                                                        type="date"
                                                        format="yyyy-MM-dd"
                                                        value-format="yyyy-MM-dd"
                                                        clearable
                                                        placeholder="选择日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="所属项目："
                                                  prop="project_belong_to">
                                        <el-select v-model="edgeForm.project_belong_to"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in projectOptions"
                                                       :key="index"
                                                       :label="item.value"
                                                       :value="item.id">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="所属业务："
                                                  prop="business_belong_to">
                                        <el-input v-model="edgeForm.business_belong_to"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>

                        </el-form>
                    </div>
                </div>
            </template>
        </dialog-model>
    </div>
</template>
<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        components: {
            DialogModel: () => import('src/pages/resource/iframe/computerManagement/modelDialog.vue')
        },
        props: {
            operationParams: {
                type: Object,
                default: function () {
                    return {}
                }
            }
        },
        data() {
            return {
                dialogAttribute: {
                    dialogWidth: '1200px',
                    dialogTitle: '新增网络线路信息',
                    dialogSure: '保存',
                    dialogCancel: '取消'
                },
                edgeForm: {
                    server_line: '',
                    idcType: '',
                    server_network_type: '',
                    server_bill_type: '',
                    server_bill_unit: '',
                    server_line_count: '',
                    server_line_standard: '',
                    server_line_a: '',
                    server_line_b: '',
                    server_line_create_date: '',
                    server_line_update_date: '',
                    server_line_cancal_date: '',
                    project_belong_to: '',
                    business_belong_to: ''
                },
                edgeRules: {
                    server_line: [{ required: true, message: '请输入线路名称', trigger: 'blur' }],
                    idcType: [{ required: true, message: '请选择资源池', trigger: 'change' }],
                    server_network_type: [{ required: true, message: '请选择网络类型', trigger: 'change' }],
                    server_bill_type: [{ required: true, message: '请选择计费方式', trigger: 'change' }],
                    server_bill_unit: [{ required: true, message: '请选择单位', trigger: 'change' }],
                    server_line_count: [{ required: true, message: '请输入数量', trigger: 'blur' }],
                    server_line_a: [{ required: true, message: '请输入A端', trigger: 'blur' }],
                    server_line_b: [{ required: true, message: '请输入Z端', trigger: 'blur' }]
                },
                idcTypeOptions: [],
                networkOptions: [],
                billOptions: [],
                unitOptions: [],
                projectOptions: []
            }
        },
        created() {
            this.idcTypeOptions = this.operationParams.idcTypeOptions
            this.networkOptions = this.operationParams.networkOptions
            this.billOptions = this.operationParams.billOptions
            this.unitOptions = this.operationParams.unitOptions
            this.projectOptions = this.operationParams.projectOptions
            if (this.operationParams.isEdit) {
                this.dialogAttribute.dialogTitle = '修改网络线路信息'
                this.getDetails()
            } else {
                this.dialogAttribute.dialogTitle = '新增网络线路信息'
            }
        },
        methods: {
            saveAddData(status) {
                let queryData = {}
                for (let prop in this.edgeForm) {
                    if (this.edgeForm[prop] && this.edgeForm[prop] !== 0) {
                        queryData[prop] = this.edgeForm[prop]
                    }
                }
                queryData.condicationCode = this.$route.query.condicationCode
                queryData.module_id = this.$route.query.module_id
                rbCmdbServiceFactory.addInstance(queryData).then((res) => {
                    if (res.success) {
                        if (res.message.indexOf('已存在') > -1) {
                            this.$message.error(res.message)
                        } else {
                            this.$emit('closeOperateDialog', status)
                            this.$message.success(res.message)
                            this.$parent.getTableList()
                        }
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            saveEditData(status) {
                let queryData = {}
                for (let prop in this.edgeForm) {
                    if (this.edgeForm[prop] && this.edgeForm[prop] !== 0) {
                        queryData[prop] = this.edgeForm[prop]
                    }
                }
                // queryData.condicationCode = this.$route.query.condicationCode
                queryData.module_id = this.$route.query.module_id
                rbCmdbServiceFactory.updateInstance(queryData, this.operationParams.editDatas.instance_id).then((res) => {
                    if (res.success) {
                        this.$emit('closeOperateDialog', status)
                        this.$message.success(res.message)
                        this.$parent.getTableList()
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            getDetails() {
                let params = {
                    instance_id: this.operationParams.editDatas.instance_id,
                    module_id: this.$route.query.module_id
                }
                rbCmdbServiceFactory.getFullInstance(params).then((data) => {
                    for (let prop in this.edgeForm) {
                        this.edgeForm[prop] = data[prop]
                    }
                })
            },
            handleSureDialog(status) {
                this.$refs.edgeForm.validate((valid) => {
                    if (valid) {
                        if (this.operationParams.isEdit) {
                            this.saveEditData(status)
                        } else {
                            this.saveAddData(status)
                        }

                    } else {
                        return false
                    }
                })
            },
            handleCancelDialog(status) {
                this.$emit('closeOperateDialog', status)
            }
        }
    }
</script>
<style lang="scss" scoped>
    .template-download {
        display: inline-block;
        background: url("../../../../assets/img/excel.png") no-repeat left center;
        padding-left: 20px;
        cursor: pointer;
    }
    .operate-content /deep/ .el-input {
        width: 220px;
    }
    .operate-header .el-row {
        margin-top: 10px;
    }
</style>