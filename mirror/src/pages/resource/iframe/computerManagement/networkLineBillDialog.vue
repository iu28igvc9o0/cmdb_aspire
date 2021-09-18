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
                                    <el-form-item prop="bill_year"
                                                  class="bill-year"
                                                  label="结算年份">
                                        <el-date-picker v-model="edgeForm.bill_year"
                                                        type="year"
                                                        format="yyyy"
                                                        value-format="yyyy"
                                                        clearable
                                                        placeholder="请选择年份">
                                        </el-date-picker>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="资源池："
                                                  prop="idcType">
                                        <el-select v-model="edgeForm.idcType"
                                                   disabled
                                                   clearable
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
                                                   disabled
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
                                                   disabled
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
                                                   disabled
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
                                                  disabled
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="规格："
                                                  prop="server_line_standard">
                                        <el-input v-model="edgeForm.server_line_standard"
                                                  disabled
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="线路名称："
                                                  prop="server_line">
                                        <el-input v-model="edgeForm.server_line"
                                                  disabled
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="A端："
                                                  prop="server_line_a">
                                        <el-input v-model="edgeForm.server_line_a"
                                                  disabled
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="Z端："
                                                  prop="server_line_b">
                                        <el-input v-model="edgeForm.server_line_b"
                                                  disabled
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item prop="server_line_create_date"
                                                  label="线路新增时间：">
                                        <el-date-picker v-model="edgeForm.server_line_create_date"
                                                        disabled
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
                                                        disabled
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
                                    <el-form-item label="线路取消时间："
                                                  prop="server_line_cancal_date">
                                        <el-date-picker v-model="edgeForm.server_line_cancal_date"
                                                        disabled
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
                                                   disabled
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
                                                  disabled
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="1月容量："
                                                  prop="capacity_month1">
                                        <el-input v-model="edgeForm.capacity_month1"
                                                  disabled
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="1月单价（元）："
                                                  prop="bill_month_unit1">
                                        <el-input v-model="edgeForm.bill_month_unit1"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="1月计费（元）："
                                                  prop="bill_month_total1">
                                        <el-input v-model="edgeForm.bill_month_total1"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="2月单价（元）："
                                                  prop="bill_month_unit2">
                                        <el-input v-model="edgeForm.bill_month_unit2"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="2月计费（元）："
                                                  prop="bill_month_total2">
                                        <el-input v-model="edgeForm.bill_month_total2"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="3月单价（元）："
                                                  prop="bill_month_unit3">
                                        <el-input v-model="edgeForm.bill_month_unit3"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="3月计费（元）："
                                                  prop="bill_month_total3">
                                        <el-input v-model="edgeForm.bill_month_total3"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="4月单价（元）："
                                                  prop="bill_month_unit4">
                                        <el-input v-model="edgeForm.bill_month_unit4"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="4月计费（元）："
                                                  prop="bill_month_total4">
                                        <el-input v-model="edgeForm.bill_month_total4"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>

                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="5月单价（元）："
                                                  prop="bill_month_unit5">
                                        <el-input v-model="edgeForm.bill_month_unit5"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="5月计费（元）："
                                                  prop="bill_month_total5">
                                        <el-input v-model="edgeForm.bill_month_total5"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="6月单价（元）："
                                                  prop="bill_month_unit6">
                                        <el-input v-model="edgeForm.bill_month_unit6"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>

                                <el-col :span="8">
                                    <el-form-item label="6月计费（元）："
                                                  prop="bill_month_total6">
                                        <el-input v-model="edgeForm.bill_month_total6"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="7月单价（元）："
                                                  prop="bill_month_unit7">
                                        <el-input v-model="edgeForm.bill_month_unit7"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="7月计费（元）："
                                                  prop="bill_month_total7">
                                        <el-input v-model="edgeForm.bill_month_total7"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="8月单价（元）："
                                                  prop="bill_month_unit8">
                                        <el-input v-model="edgeForm.bill_month_unit8"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="8月计费（元）："
                                                  prop="bill_month_total8">
                                        <el-input v-model="edgeForm.bill_month_total8"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="9月单价（元）："
                                                  prop="bill_month_unit9">
                                        <el-input v-model="edgeForm.bill_month_unit9"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="9月计费（元）："
                                                  prop="bill_month_total9">
                                        <el-input v-model="edgeForm.bill_month_total9"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="10月单价（元）："
                                                  prop="bill_month_unit10">
                                        <el-input v-model="edgeForm.bill_month_unit10"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="10月计费（元）："
                                                  prop="bill_month_total10">
                                        <el-input v-model="edgeForm.bill_month_total10"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="11月单价（元）："
                                                  prop="bill_month_unit11">
                                        <el-input v-model="edgeForm.bill_month_unit11"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="11月计费（元）："
                                                  prop="bill_month_total11">
                                        <el-input v-model="edgeForm.bill_month_total11"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                                <el-col :span="8">
                                    <el-form-item label="12月单价（元）："
                                                  prop="bill_month_unit12">
                                        <el-input v-model="edgeForm.bill_month_unit12"
                                                  placeholder="请输入"
                                                  clearable></el-input>
                                    </el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="8">
                                    <el-form-item label="12月计费（元）："
                                                  prop="bill_month_total12">
                                        <el-input v-model="edgeForm.bill_month_total12"
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
                    dialogTitle: '修改网络线路结算信息',
                    dialogSure: '保存',
                    dialogCancel: '取消'
                },
                edgeForm: {
                    bill_year: '',
                    idcType: '',
                    server_network_type: '',
                    server_bill_type: '',
                    server_bill_unit: '',
                    server_line_count: '',
                    server_line_standard: '',
                    server_line: '',
                    server_line_a: '',
                    server_line_b: '',
                    server_line_create_date: '',
                    server_line_update_date: '',
                    server_line_cancal_date: '',
                    project_belong_to: '',
                    business_belong_to: '',
                    capacity_month1: '',
                    bill_month_unit1: '',
                    bill_month_total1: '',
                    bill_month_unit2: '',
                    bill_month_total2: '',
                    bill_month_unit3: '',
                    bill_month_total3: '',
                    bill_month_unit4: '',
                    bill_month_total4: '',
                    bill_month_unit5: '',
                    bill_month_total5: '',
                    bill_month_unit6: '',
                    bill_month_total6: '',
                    bill_month_unit7: '',
                    bill_month_total7: '',
                    bill_month_unit8: '',
                    bill_month_total8: '',
                    bill_month_unit9: '',
                    bill_month_total9: '',
                    bill_month_unit10: '',
                    bill_month_total10: '',
                    bill_month_unit11: '',
                    bill_month_total11: '',
                    bill_month_unit12: '',
                    bill_month_total12: ''
                },
                edgeRules: {
                    bill_year: [{ required: true, message: '请选择结算年份', trigger: 'change' }],
                    server_line: [{ required: true, message: '请输入线路名称', trigger: 'blur' }],
                    idcType: [{ required: true, message: '请选择资源池', trigger: 'change' }],
                    server_network_type: [{ required: true, message: '请选择网络类型', trigger: 'change' }],
                    server_bill_type: [{ required: true, message: '请选择计费方式', trigger: 'change' }],
                    server_bill_unit: [{ required: true, message: '请选择单位', trigger: 'change' }],
                    server_line_count: [{ required: true, message: '请输入数量', trigger: 'blur' }],
                    server_line_a: [{ required: true, message: '请输入A端', trigger: 'blur' }],
                    server_line_b: [{ required: true, message: '请输入Z端', trigger: 'blur' }],
                    bill_month_unit1: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total1: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit2: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total2: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit3: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total3: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit4: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total4: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit5: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total5: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit6: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total6: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit7: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total7: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit8: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total8: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit9: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total9: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit10: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total10: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit11: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total11: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_unit12: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }],
                    bill_month_total12: [{ pattern: /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/, message: '请输入非负数', trigger: 'blur' }]
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
            }
        },
        methods: {
            saveEditData(status) {
                let queryData = {}
                for (let prop in this.edgeForm) {
                    if (this.edgeForm[prop] && this.edgeForm[prop] !== 0) {
                        queryData[prop] = this.edgeForm[prop]
                    }
                }
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