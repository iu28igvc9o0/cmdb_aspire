<template>
    <div>
        <el-form ref="filterRuleForm"
                 :model="logAlertRuleData"
                 label-width="80px"
                 class="yw-form components-condition">
            <el-form-item label="规则名称" prop="name">
                <el-input v-model="logAlertRuleData.name" style="width: 300px;" :disabled="type === 'update'"></el-input>
            </el-form-item>
            <el-form-item label="规则描述" prop="description">
                <el-input type="textarea" v-model="logAlertRuleData.description" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item label="运行状态" prop="runStatus">
                <el-switch
                    v-model="logAlertRuleData.runStatus"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    :disabled="type === 'add'">
                </el-switch>
            </el-form-item>
            <el-form-item label="告警等级" prop="alertLevel">
                <el-select v-model="logAlertRuleData.alertLevel"
                           placeholder="请选择"
                           clearable
                           filterable>
                    <el-option v-for="item in alert_levels"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="资源池" prop="idcType">
                <el-select v-model="logAlertRuleData.idcType"
                           style="width: 300px;"
                           placeholder="请选择"
                           clearable
                           filterable>
                    <el-option v-for="item in resourcePools"
                               :key="item.value"
                               :label="item.name"
                               :value="item.value"> </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="设备IP" prop="deviceIp">
                <el-input v-model="logAlertRuleData.deviceIp" placeholder="设备IP" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item label="关键字" prop="include">
                <div>
                    <span><el-checkbox v-model="logAlertRuleData.include">包含</el-checkbox></span>
                    <span v-if="type !== 'detail'" style="padding-left: 12px;"><el-button type="text" @click="setKeys('include')">设置关键字</el-button></span>
                    <span><el-input style="width: 175px;" v-model="logAlertRuleData.includeKey" disabled></el-input></span>
                </div>
                <div>
                    <span><el-checkbox v-model="logAlertRuleData.noInclude">不包含</el-checkbox></span>
                    <span v-if="type !== 'detail'"><el-button type="text" @click="setKeys('noInclude')">设置关键字</el-button></span>
                    <span><el-input style="width: 175px;" v-model="logAlertRuleData.noIncludeKey" disabled></el-input></span>
                </div>
                <!--<el-radio-group v-model="logAlertRuleData.include">-->
                    <!--<el-radio label="包含"></el-radio>-->
                    <!--<el-radio label="不包含"></el-radio>-->
                <!--</el-radio-group>-->
                <!--<YwInputAdd :keys="logAlertRuleData.key"-->
                            <!--:activeKey="activeKey"-->
                            <!--@changeKey="changeKey"-->
                            <!--style="width: 300px;"></YwInputAdd>-->
            </el-form-item>
            <el-form-item label="时间过滤" prop="filterDate">
                <span>
                   <el-radio-group v-model="logAlertRuleData.filterDate" @change="filterDateChange()">
                      <el-radio :label="30">半小时</el-radio>
                      <el-radio :label="60">一小时</el-radio>
                   </el-radio-group>
                </span>
                <span style="padding-left: 10px;">最近</span>
                <span><el-input v-model="logAlertRuleData.filterDateValue" style="width: 50px;"></el-input></span>
                <span>分钟</span>
            </el-form-item>
        </el-form>
        <el-dialog class="yw-dialog"
                   :visible.sync="setKeysDialog"
                   title="设置关键字"
                   width="480px"
                   :modal="false"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <YwInputAdd :keys="logAlertRuleData.key"
                        :activeKey="activeKey"
                        @changeKey="changeKey"
                        v-if="setKeysDialog"
                        style="width: 300px;"></YwInputAdd>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="keySubmit()">提交</el-button>
                <el-button @click="setKeysDialog = false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>
<script>
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    import { alert_level } from '../../../mirror_alert/alert/config/options.js'
    export default {
        mixins: [QueryObject],
        components: {
            YwSelectResource: () => import('src/components/common/yw-select-resource.vue'),
            YwInputAdd: () => import('src/components/common/yw-input-add.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue')
        },
        props: [
            'type', 'logAlertRuleDetailData', 'ip', 'idcType', 'param', 'noParam'
        ],
        data () {
            return {
                resourcePools: [],
                alert_levels: alert_level,
                logAlertRuleData: {
                    uuid: '',
                    name: '',
                    description: '',
                    runStatus: false,
                    idcType: this.idcType ? this.idcType : '',
                    deviceIp: this.ip ? this.ip : '',
                    include: true,
                    includeKey: this.param ? this.param.toString() : '',
                    noInclude: this.noParam && this.noParam.length > 0,
                    noIncludeKey: this.noParam ? this.noParam : '',
                    key: [],
                    filterDate: 30,
                    filterDateValue: 30,
                    alertLevel: ''
                },
                setKeysDialog: false,
                changeInclude: '',
            }
        },
        mounted: function () {
            this.getResourcePool()
        },
        methods: {
            // 获取资源池
            getResourcePool (type) {
                let obj = {
                    'type': 'idcType',
                    'pid': ''
                }
                rbProjectDataServiceFactory.getConfigDictByType(obj).then((res) => {
                    if (res) {
                        this.resourcePools = res
                    }
                })
            },
            filterDateChange () {
                this.logAlertRuleData.filterDateValue = parseInt(this.logAlertRuleData.filterDate)
            },
            resetForm() {
                this.$refs['filterRuleForm'].resetFields()
                this.filterDateChange()
            },
            setKeys (val) {
                this.changeInclude = val
                let params = val === 'include' ? this.logAlertRuleData.includeKey : this.logAlertRuleData.noIncludeKey
                console.log(typeof params)
                this.logAlertRuleData.key = params ? params.toString().split(',') : []
                this.setKeysDialog = true
            },
            keySubmit () {
                if (this.changeInclude === 'include') {
                    this.logAlertRuleData.includeKey = this.logAlertRuleData.key.toString()
                } else if (this.changeInclude === 'noInclude') {
                    this.logAlertRuleData.noIncludeKey = this.logAlertRuleData.key.toString()
                }
                this.setKeysDialog = false
            },
        },
        watch: {
            logAlertRuleDetailData: {
                handler: function () {
                    if (this.logAlertRuleDetailData) {
                        this.logAlertRuleData.uuid = this.logAlertRuleDetailData.uuid
                        this.logAlertRuleData.name = this.logAlertRuleDetailData.name
                        this.logAlertRuleData.description = this.logAlertRuleDetailData.description
                        this.logAlertRuleData.runStatus = this.logAlertRuleDetailData.runStatus !== '0'
                        this.logAlertRuleData.idcType = this.logAlertRuleDetailData.idcType
                        this.logAlertRuleData.deviceIp = this.logAlertRuleDetailData.ip
                        if (this.logAlertRuleDetailData.filterTime === 30) {
                            this.logAlertRuleData.filterDate = 30
                        } else if (this.logAlertRuleDetailData.filterTime === 60) {
                            this.logAlertRuleData.filterDate = 60
                        } else {
                            this.logAlertRuleData.filterDate = 0
                        }
                        this.logAlertRuleData.filterDateValue = this.logAlertRuleDetailData.filterTime
                        this.logAlertRuleData.alertLevel = this.logAlertRuleDetailData.alertLevel
                        if (this.logAlertRuleDetailData.include) {
                            this.logAlertRuleData.include = true
                            this.logAlertRuleData.includeKey =
                                this.logAlertRuleDetailData.includeKey ? this.logAlertRuleDetailData.includeKey.split(',') : []
                        }
                        if (this.logAlertRuleDetailData.noInclude) {
                            this.logAlertRuleData.noInclude = true
                            this.logAlertRuleData.noIncludeKey =
                                this.logAlertRuleDetailData.noIncludeKey ? this.logAlertRuleDetailData.noIncludeKey.split(',') : []
                        }
                    }
                },
                immediate: true
            }
        }
    }
</script>
<style scoped lang="scss">
</style>
