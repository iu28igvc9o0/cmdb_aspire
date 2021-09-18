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
                                    <el-form-item label="机房："
                                                  prop="server_room_location">
                                        <el-select v-model="edgeForm.server_room_location"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in locationOptions"
                                                       :key="index"
                                                       :value="item.id"
                                                       :label="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="机柜编码："
                                                  prop="server_cabinet_code">
                                        <el-input v-model="edgeForm.server_cabinet_code"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="机房规格："
                                                  prop="server_cabinet_standard">
                                        <el-select v-model="edgeForm.server_cabinet_standard"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in standardOptions"
                                                       :key="index"
                                                       :value="item.id"
                                                       :label="item.value">
                                            </el-option>
                                        </el-select>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="加电设备数："
                                                  prop="online_count">
                                        <el-input v-model="edgeForm.online_count"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item prop="first_online_date"
                                                  label="首次加电日期：">
                                        <el-date-picker v-model="edgeForm.first_online_date"
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
                                    <el-form-item prop="bill_start_date"
                                                  label="计费起始日期：">
                                        <el-date-picker v-model="edgeForm.bill_start_date"
                                                        type="date"
                                                        format="yyyy-MM-dd"
                                                        value-format="yyyy-MM-dd"
                                                        clearable
                                                        placeholder="选择日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item prop="offline_date"
                                                  label="下架时间：">
                                        <el-date-picker v-model="edgeForm.offline_date"
                                                        type="date"
                                                        format="yyyy-MM-dd"
                                                        value-format="yyyy-MM-dd"
                                                        clearable
                                                        placeholder="选择日期">
                                        </el-date-picker>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="所属项目："
                                                  prop="project_belong_to">
                                        <el-select v-model="edgeForm.project_belong_to"
                                                   clearable
                                                   filterable
                                                   placeholder="请选择">
                                            <el-option v-for="(item,index) in projectOptions"
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
                    dialogTitle: '新增机柜信息',
                    dialogSure: '保存',
                    dialogCancel: '取消'
                },
                edgeForm: {
                    idcType: '',
                    server_room_location: '',
                    server_cabinet_code: '',
                    server_cabinet_standard: '',
                    online_count: '',
                    first_online_date: '',
                    bill_start_date: '',
                    offline_date: '',
                    project_belong_to: '',
                    business_belong_to: ''
                },
                edgeRules: {
                    idcType: [{ required: true, message: '请选择资源池', trigger: 'change' }],
                    server_room_location: [{ required: true, message: '请选择机房', trigger: 'change' }],
                    server_cabinet_code: [{ required: true, message: '请输入机柜编码', trigger: 'blur' }],
                    server_cabinet_standard: [{ required: true, message: '请选择机柜规格', trigger: 'change' }],
                    online_count: [{ required: true, message: '请输入加电设备数', trigger: 'blur' }],
                    first_online_date: [{ required: true, message: '请选择首次加电日期', trigger: 'change' }],
                    offline_date: [{ required: true, message: '请选择下架时间', trigger: 'change' }]
                },
                idcTypeOptions: [],
                locationOptions: [],
                standardOptions: [],
                projectOptions: []
            }
        },
        created() {
            this.idcTypeOptions = this.operationParams.idcTypeOptions
            this.locationOptions = this.operationParams.locationOptions
            this.standardOptions = this.operationParams.standardOptions
            this.projectOptions = this.operationParams.projectOptions
            if (this.operationParams.isEdit) {
                this.dialogAttribute.dialogTitle = '修改机柜信息'
                this.getDetails()
            } else {
                this.dialogAttribute.dialogTitle = '新增机柜信息'
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