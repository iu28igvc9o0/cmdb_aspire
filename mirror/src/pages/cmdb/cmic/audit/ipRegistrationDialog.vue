<template>
    <!-- IP稽核内网、公网、IPV6等IP登记弹框-->
    <div class="ip-registration">
        <el-dialog class="yw-dialog"
                   :visible.sync="dialogRegistration"
                   width="400px"
                   :close-on-click-modal="false"
                   :title="title"
                   @close="closeDialog">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         :model="registrationForm"
                         :rules="registrationRules"
                         label-width="85px"
                         ref="registrationForm">
                    <el-form-item label="分配一级业务"
                                  prop="businessName1">
                        <el-select v-model="registrationForm.businessName1"
                                   clearable
                                   placeholder="请选择">
                            <el-option v-for="item in firstBusinessOptions"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="分配独立业务"
                                  prop="businessName2">
                        <el-select v-model="registrationForm.businessName2"
                                   clearable
                                   placeholder="请选择">
                            <el-option v-for="item in aloneBusinessSystemOptions"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="地址用途"
                                  prop="ipUse">
                        <el-select v-model="registrationForm.ipUse"
                                   clearable
                                   placeholder="请选择">
                            <el-option v-for="item in ipUseOptions"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="申请人"
                                  prop="requestPerson">
                        <el-input v-model="registrationForm.requestPerson"
                                  clearable></el-input>
                    </el-form-item>
                    <el-form-item label="申请工单"
                                  prop="requestOrder">
                        <el-input v-model="registrationForm.requestOrder"
                                  clearable></el-input>
                    </el-form-item>
                    <el-form-item label="申请时间"
                                  prop="requestTime">
                        <el-date-picker v-model="registrationForm.requestTime"
                                        format="yyyy-MM-dd HH:mm:ss"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        type="datetime"
                                        placeholder="选择日期时间">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="使用期限(年)"
                                  prop="expirationDate">
                        <el-input v-model="registrationForm.expirationDate"
                                  clearable></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleRegistrationSure">确定</el-button>
                <el-button @click="handleRegistrationCancel">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbIpCollectionServiceFactory from 'src/services/cmdb/rb-cmdb-ip-management-factory.js'
    export default {
        data() {
            var checkExpiration = (rule, value, callback) => {
                if (value) {
                    const reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
                    if (reg.test(value)) {
                        callback()
                    } else {
                        callback(new Error('请输入正确的使用期限'))
                    }
                } else {
                    callback()
                }
            }
            return {
                dialogRegistration: true,
                registrationForm: {
                    businessName1: '',
                    businessName2: '',
                    ipUse: '',
                    requestPerson: '',
                    requestTime: '',
                    requestOrder: '',
                    expirationDate: ''
                },
                registrationRules: {
                    expirationDate: [{ validator: checkExpiration, trigger: 'blur' }]
                },
                firstBusinessOptions: [],
                aloneBusinessSystemOptions: [],
                ipUseOptions: []
            }
        },
        props: ['ipUseOptions', 'firstBusinessOptions', 'aloneBusinessSystemOptions', 'ipIds', 'segmentIds', 'opIpType', 'title'],
        methods: {
            async getRestrationList(queryData) {
                try {
                    let res = await rbIpCollectionServiceFactory.updateIpRepositoryInfo(queryData)
                    if (res.success) {
                        this.$message({ type: 'success', message: res.msg })
                        this.$parent.getTableList('find')
                    } else {
                        this.$message({ type: 'error', message: res.msg })
                    }
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    this.$emit('closeRegistrationDialog', false)
                }

            },
            handleRegistrationSure() {
                let queryDatas = {
                    ipIds: JSON.parse(JSON.stringify(this.ipIds)),
                    segmentIds: JSON.parse(JSON.stringify(this.segmentIds)),
                    opIpType: this.opIpType,
                }
                for (let prop in this.registrationForm) {
                    if (this.registrationForm[prop] || this.registrationForm[prop] === '0') {
                        queryDatas[prop] = this.registrationForm[prop]
                    }
                }
                if (Object.values(queryDatas).length > 3) {
                    this.$refs['registrationForm'].validate((valid) => {
                        if (valid) {
                            this.getRestrationList(queryDatas)
                        } else {
                            return false
                        }
                    })

                } else {
                    this.$message({ type: 'error', message: '表单项不能全为空，请填写内容' })
                }

            },
            handleRegistrationCancel() {
                this.$emit('closeRegistrationDialog', false)
            },
            closeDialog() {
                this.$emit('closeRegistrationDialog', false)
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
