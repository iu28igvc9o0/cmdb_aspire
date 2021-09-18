<template>
    <div class="code-mirror-box">
        <el-form class="yw-form is-required"
                 ref="editForm"
                 :inline="true"
                 :rules="rowFormRules"
                 :model="formData"
                 label-width="120px">
            <el-form-item label="漏洞名称："
                          prop="name">
                <el-input style="width:178px"
                          v-model="formData.name"
                          :disabled="Boolean(formData.id)"></el-input>
            </el-form-item>
            <el-form-item label="分组名称："
                          prop="groupId">
                <el-select v-model="formData.groupId"
                           style="width:178px"
                           clearable>
                    <el-option v-for="item in groupList"
                               :key="item.vulnerability_group_id"
                               :label="item.name"
                               :value="item.vulnerability_group_id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="漏洞编号："
                          v-if="formData.id">
                <el-input style="width:178px"
                          v-model="formData.id"
                          disabled></el-input>
            </el-form-item>
            <el-form-item prop="cveNumber"
                          label="CVE编码：">
                <el-input style="width:178px"
                          v-model="formData.cveNumber"
                          :disabled="Boolean(formData.id)"></el-input>
            </el-form-item>
            <el-form-item prop="cncveNumber"
                          label="CNCVE编码：">
                <el-input style="width:178px"
                          v-model="formData.cncveNumber"
                          :disabled="Boolean(formData.id)"></el-input>
            </el-form-item>
            <el-form-item prop="cnvdNumber"
                          label="CNVD编码：">
                <el-input style="width:178px"
                          v-model="formData.cnvdNumber"
                          :disabled="Boolean(formData.id)"></el-input>
            </el-form-item>
            <el-form-item prop="cnnvdNumber"
                          label="CNNVD编码：">
                <el-input style="width:178px"
                          v-model="formData.cnnvdNumber"
                          :disabled="Boolean(formData.id)"></el-input>
            </el-form-item>

            <el-form-item label="漏洞发现厂商："
                          prop="discoverProducer">
                <el-select v-model="formData.discoverProducer"
                           style="width:178px"
                           placeholder="请选择">
                    <el-option label="绿盟"
                               value="lvmeng"></el-option>
                    <el-option label="启明星辰"
                               value="qiming"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否重启："
                          prop="needReboot">
                <el-select style="width:178px"
                           v-model="formData.needReboot"
                           placeholder="请选择">
                    <el-option label="是"
                               value="Y"></el-option>
                    <el-option label="否"
                               value="N"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="危险等级："
                          prop="riskLevel">
                <el-select v-model="formData.riskLevel"
                           style="width:178px"
                           clearable>
                    <el-option v-for="item in riskLevelList"
                               :key="item.id"
                               :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="端口："
                          prop="port">
                <el-input style="width:178px"
                          v-model="formData.port"></el-input>
            </el-form-item>
            <el-form-item label="协议："
                          prop="protocol">
                <el-select v-model="formData.protocol"
                           style="width:178px"
                           clearable>
                    <el-option v-for="item in protocolList"
                               :key="item.key"
                               :label="item.value"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="服务："
                          prop="service">
                <el-input style="width:178px"
                          v-model="formData.service"></el-input>
            </el-form-item>
            <el-form-item label="软件依赖："
                          prop="softDependencies">
                <el-input style="width:178px"
                          v-model="formData.softDependencies"></el-input>
            </el-form-item>
            <el-form-item label="是否可修复："
                          prop="canFixed">
                <el-select v-model="formData.canFixed"
                           style="width:178px"
                           placeholder="请选择">
                    <el-option label="是"
                               value="Y"></el-option>
                    <el-option label="否"
                               value="N"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="最后修改时间："
                          v-if="formData.updateTime">
                {{formData.updateTime}}
            </el-form-item>
            <el-form-item class="mtop10"
                          label="描述："
                          prop="describe">
                <el-input type="textarea"
                          style="width:178px"
                          :rows="3"
                          v-model="formData.describe"></el-input>
            </el-form-item>
            <el-form-item class="mtop10"
                          label="解决方法："
                          prop="repairMethod">
                <el-input type="textarea"
                          style="width:178px"
                          v-model="formData.repairMethod"
                          :rows="3"></el-input>
            </el-form-item>

        </el-form>
    </div>
</template>

<script>
    export default {
        props: {
            currentRowData: {
                type: Object,
                default() {
                    return {}
                }
            },
            groupList: {
                type: Array,
                default() {
                    return []
                }
            },
            riskLevelList: {
                type: Array,
                default() {
                    return []
                }
            },
        },
        data() {
            const numberValid = (rule, value, callback) => {
                let result = this.formData.cveNumber || this.formData.cncveNumber || this.formData.cnvdNumber || this.formData.cnnvdNumber
                if (!result) {
                    callback(new Error('至少输入一种编码'))
                } else {
                    callback()
                }
            }
            return {
                formData: {},
                rowFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请输入漏洞名称!',
                            trigger: 'blur'
                        }
                    ],
                    id: [
                        {
                            required: true,
                            message: '请输入漏洞编号!',
                            trigger: 'blur'
                        }
                    ],
                    riskLevel: [
                        {
                            required: true,
                            message: '请选择危险等级!',
                        }
                    ],
                    port: [
                        {
                            required: true,
                            message: '请输入端口!',
                            trigger: 'blur'
                        }
                    ],
                    protocol: [
                        {
                            required: true,
                            message: '请选择协议!',
                        }
                    ],
                    service: [
                        {
                            required: true,
                            message: '请输入服务!',
                            trigger: 'blur'
                        }
                    ],
                    cveNumber: [
                        {
                            required: true,
                            trigger: 'blur',
                            validator: numberValid
                        }
                    ],
                    cncveNumber: [
                        {
                            required: true,
                            trigger: 'blur',
                            validator: numberValid
                        }
                    ],
                    cnvdNumber: [
                        {
                            required: true,
                            trigger: 'blur',
                            validator: numberValid
                        }
                    ],
                    cnnvdNumber: [
                        {
                            required: true,
                            trigger: 'blur',
                            validator: numberValid
                        }
                    ],

                },
                name: '',
                id: '',
                riskLevel: '',
                port: '',
                protocol: '',
                service: '',
                protocolList: [{    // 协议列表
                    key: '1',
                    value: 'TCP'
                }, {
                    key: '2',
                    value: 'UDP'
                }, {
                    key: '3',
                    value: '其他协议'
                }],
            }
        },
        watch: {
            currentRowData: {
                handler(newVal) {
                    this.formData = this.$utils.deepClone(newVal)
                },
                deep: true,
                immediate: true
            },
        }
    }
</script>
<style lang="scss" scoped>
</style>
