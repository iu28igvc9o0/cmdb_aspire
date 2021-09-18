<template>
    <div>
        <div class="alert-order-config">
            <div>
                <div class="div-header">
                    <p class="p-text">基本信息</p>
                </div>
                <div>
                    <el-form label-position="right"
                             label-width="100px"
                             ref="alertOrderConfigForm"
                             :model="alertOrderConfigForm"
                             :rules="alertOrderConfigFormRules"
                             :disabled="detailDisabled">
                        <el-form-item label="规则名称"
                                      prop="configName">
                            <el-input v-model="alertOrderConfigForm.configName"
                                      style="width: 300px;"></el-input>
                        </el-form-item>
                        <el-form-item label="配置生效类型"
                                      prop="configTimeType">
                            <div>
                                <el-radio-group v-model="alertOrderConfigForm.configTimeType"
                                                @change="configTimeChange()">
                                    <el-radio label="1">永久生效</el-radio>
                                    <el-radio label="2">有效时间</el-radio>
                                </el-radio-group>
                            </div>
                        </el-form-item>
                        <el-form-item v-if="configTimeRangeFlag">
                            <div>
                                <span style="padding-right: 10px;font-size: 12px">生效时间</span>
                                <span>
                                    <el-date-picker v-model="alertOrderConfigForm.startTime"
                                                    style="width: 175px;"
                                                    type="datetime"
                                                    placeholder="选择日期"
                                                    :picker-options="pickerOptions"
                                                    value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                                </span>
                                <span style="padding-left: 20px;padding-right: 10px;font-size: 12px">失效时间</span>
                                <span>
                                    <el-date-picker v-model="alertOrderConfigForm.endTime"
                                                    style="width: 175px;"
                                                    type="datetime"
                                                    placeholder="选择日期"
                                                    :picker-options="pickerOptions"
                                                    value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
                                </span>
                            </div>
                        </el-form-item>
                        <el-form-item label="维护用户">
                            <el-checkbox v-model="alertOrderConfigForm.currentUserFlag"
                                         @change="selectCurrent()">当前用户</el-checkbox>
                            <el-checkbox label="其他用户"
                                         v-model="alertOrderConfigForm.otherUserFlag"
                                         @change="selectOther()"></el-checkbox>
                            <el-input style="width: 200px;"
                                      v-model="alertOrderConfigForm.userName"
                                      :disabled="true"></el-input>
                            <el-button type="primary"
                                       @click="getUser()">选择</el-button>
                        </el-form-item>
                        <el-form-item label="规则描述">
                            <el-input type="textarea"
                                      style="width: 300px;"
                                      :rows="1"
                                      placeholder="请输入内容"
                                      v-model="alertOrderConfigForm.configDescription">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="启停开关">
                            <el-switch v-model="alertOrderConfigForm.isOpen"
                                       active-color="#13ce66"
                                       inactive-color="#ff4949"
                                       active-value="1"
                                       inactive-value="2"
                                       :disabled="type === 'add'">
                            </el-switch>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
            <div>
                <div class="div-header">
                    <p class="p-text">告警数据过滤</p>
                </div>
                <div>
                    <el-form label-position="right"
                             label-width="100px"
                             ref="alertFieldForm"
                             :model="alertOrderConfigForm"
                             :disabled="detailDisabled">
                        <YwFilterOrder ref="filterOrder"
                                       :dataList="optionData"
                                       :detailFlag="detailFlag"
                                       :orderConfig="true"
                                       @changeFilterOrder="changeFilterOrder"></YwFilterOrder>
                    </el-form>
                </div>
            </div>
            <div>
                <div class="div-header">
                    <p class="p-text">派单设置</p>
                </div>
                <div>
                    <el-form label-position="right"
                             label-width="100px"
                             ref="alertFieldForm"
                             :model="alertOrderConfigForm"
                             :disabled="detailDisabled">
                        <el-form-item label="工单类型">
                            <el-radio-group v-model="alertOrderConfigForm.orderType">
                                <el-radio label="1">告警工单</el-radio>
                                <el-radio label="2">故障工单</el-radio>
                                <el-radio label="3">维保工单</el-radio>
                                <el-radio label="4">调优工单</el-radio>
                            </el-radio-group>
                            <span style="font-size: 10px"><i class="el-icon-info"></i>工单类型优先级:告警&lt;故障&lt;维保&lt;调优</span>
                        </el-form-item>
                        <el-form-item label="派单间隔">
                            <el-input-number v-model="alertOrderConfigForm.orderTimeSpace"
                                             :min="1"
                                             :max="60"></el-input-number>
                        </el-form-item>
                        <el-form-item label="派单时段">
                            <el-input v-model="alertOrderConfigForm.orderTimeInterval"
                                      style="width: 180px;"
                                      placeholder="00:00-23:59"></el-input>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>

        <DialogUser :dialogUser="dialogUser"
                    @closeDialogUser="closeDialogUser"
                    v-if="dialogUser.dialogVisible"></DialogUser>

    </div>
</template>

<script>

    export default {
        props: ['alertOrderConfigDetail', 'type'],
        mixins: [],
        components: {
            DialogUser: () => import('src/components/common/yw-dialog-user.vue'),
            YwFilterOrder: () => import('src/components/common/yw-filter-order.vue'),
        },
        data() {
            return {
                alertOrderConfigForm: {
                    uuid: '',
                    configName: '',
                    configTimeType: '1',
                    startTime: '',
                    endTime: '',
                    currentUserFlag: false,
                    otherUserFlag: false,
                    userName: '',
                    configDescription: '',
                    isOpen: '2',
                    alertFilter: '',
                    conditionData: '',
                    orderType: '1',
                    orderTimeSpace: '',
                    orderTimeInterval: '',
                },
                alertOrderConfigFormRules: {},
                configTimeRangeFlag: false,
                curUser: sessionStorage.getItem('username'),
                dialogUser: {
                    dialogVisible: false,
                    date: [] // 数据
                },
                detailFlag: false,
                showFilterFlag: false,
                detailDisabled: false,
                updateDisabled: false,
                optionData: {},
            }
        },
        mounted() {
        },
        methods: {
            configTimeChange() {
                if (this.alertOrderConfigForm.configTimeType === '2') {
                    this.configTimeRangeFlag = true
                } else {
                    this.configTimeRangeFlag = false
                }
            },
            selectCurrent() {
                let userList = this.alertOrderConfigForm.userName.split(',')
                if (this.alertOrderConfigForm.currentUserFlag) {
                    if (userList.indexOf(this.curUser) === -1) {
                        userList.unshift(this.curUser)
                    }
                } else {
                    if (userList.indexOf(this.curUser) > -1) {
                        userList.splice(userList.indexOf(this.curUser), 1)
                    }
                }
                let str = userList.toString()
                if (str.substring(str.length - 1, str.length) === ',') {
                    str = str.substring(0, str.length - 1)
                }
                this.alertOrderConfigForm.userName = str
            },
            selectOther() {
                this.dialogUser.date = []
                if (!this.alertOrderConfigForm.otherUserFlag) {
                    this.alertOrderConfigForm.userName = this.alertOrderConfigForm.currentUserFlag ? this.curUser : ''
                }
            },
            getUser() {
                if (!this.alertOrderConfigForm.otherUserFlag) {
                    this.$alert('请先选择其它用户!', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.dialogUser.dialogVisible = true
            },
            // 关闭用户弹窗
            closeDialogUser(date) {
                this.dialogUser.dialogVisible = false
                if (date && date.type === 'update') {
                    // 确认的操作
                    this.dialogUser.date = date.userResult
                    let userList = []
                    if (this.alertOrderConfigForm.currentUserFlag) userList.push(this.curUser)
                    date.userResult.forEach((item) => {
                        userList.push(item)
                    })
                    this.alertOrderConfigForm.userName = userList.toString()
                }
            },
            changeFilterOrder() {
            },
            getFilterData() {
                return this.$refs.filterOrder.dataFilterList
            }
        },
        watch: {
            alertOrderConfigDetail: {
                handler: function () {
                    if (this.alertOrderConfigDetail) {
                        this.alertOrderConfigForm.uuid = this.alertOrderConfigDetail.uuid
                        this.alertOrderConfigForm.configName = this.alertOrderConfigDetail.configName
                        this.alertOrderConfigForm.configTimeType = this.alertOrderConfigDetail.configTimeType
                        this.configTimeRangeFlag = this.alertOrderConfigDetail.configTimeType === '2'
                        this.alertOrderConfigForm.startTime = this.alertOrderConfigDetail.startTime
                        this.alertOrderConfigForm.endTime = this.alertOrderConfigDetail.endTime
                        this.alertOrderConfigForm.userName = this.alertOrderConfigDetail.userName
                        if (this.alertOrderConfigDetail.userName.indexOf(this.curUser) > -1) {
                            this.alertOrderConfigForm.currentUserFlag = true
                        }
                        let userList = this.alertOrderConfigDetail.userName.split(',')
                        if (userList.indexOf(this.curUser) > -1) {
                            userList.splice(userList.indexOf(this.curUser), 1)
                        }
                        if (userList.length > 0) {
                            this.alertOrderConfigForm.otherUserFlag = true
                        }
                        this.alertOrderConfigForm.configDescription = this.alertOrderConfigDetail.configDescription
                        this.alertOrderConfigForm.isOpen = this.alertOrderConfigDetail.isOpen === '启用' ? '1' : '2'
                        this.alertOrderConfigForm.alertFilter = this.alertOrderConfigDetail.alertFilter
                        this.optionData = JSON.parse(this.alertOrderConfigDetail.alertFilter)
                        this.alertOrderConfigForm.orderType = this.alertOrderConfigDetail.orderType
                        this.alertOrderConfigForm.orderTimeSpace = this.alertOrderConfigDetail.orderTimeSpace
                        this.alertOrderConfigForm.orderTimeInterval = this.alertOrderConfigDetail.orderTimeInterval

                    }
                },
                immediate: true
            },
            type: {
                handler: function () {
                    if (this.type === 'detail') {
                        this.detailDisabled = true
                        this.detailFlag = true
                    } else if (this.type === 'update') {
                        this.updateDisabled = true
                    }
                },
                immediate: true
            },
        },

    }

</script>
<style lang="scss" scoped>
    .alert-order-config {
        .div-header {
            background-color: #d6e1e5;
            width: 100%;
            height: 24px;
            font-size: 12px;
        }
        .p-text {
            padding-top: 3px;
            padding-left: 10px;
            font-size: 12px;
        }
        /deep/ .el-form-item__label {
            font-size: 12px;
        }
        /deep/ .el-form-item {
            margin: 1px;
            padding-right: 20px;
            height: 50px;
        }
        /deep/ .el-radio {
            margin-right: 5px;
        }
        /deep/ .el-radio__label {
            padding-left: 5px;
        }
        /deep/ .el-checkbox {
            margin-right: 5px;
        }
        /*/deep/ .el-checkbox-inner {}*/
    }
</style>
