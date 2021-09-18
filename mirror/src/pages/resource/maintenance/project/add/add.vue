<template>
    <el-dialog class="yw-dialog"
               width="720px"
               :title="dialogMsg.dialogTitle"
               :visible.sync="dialogMsg.dialogVisible">
        <section class="yw-dialog-main">
            <el-form class="yw-form is-required"
                     :inline="true"
                     :model="project"
                     ref="projectForm"
                     :rules="rules"
                     label-width="110px">
                <el-form-item label="项目名称"
                              prop="projectName">
                    <el-input v-model="project.projectName"></el-input>
                </el-form-item>
                <el-form-item label="合同编号"
                              prop="projectNo">
                    <el-input v-model="project.projectNo"></el-input>
                </el-form-item>
                <el-form-item label="维保类型"
                              prop="maintenanceType">
                    <el-select v-model="project.maintenanceType"
                               clearable
                               placeholder="请选择维保类型">
                        <el-option v-for="item in maintenanceList"
                                   :key="item.name"
                                   :label="item.value"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="服务形式"
                              prop="serviceType">
                    <el-select v-model="project.serviceType"
                               filterable
                               placeholder="请选择服务形式">
                        <el-option v-for="item in serviceTypeList"
                                   :key="item.name"
                                   :label="item.name"
                                   :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始时间"
                              prop="serviceStartTime">
                    <el-date-picker v-model="project.serviceStartTime"
                                    placeholder="选择时间"
                                    :picker-options="startDatePicker()"
                                    value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间"
                              prop="serviceEndTime">
                    <el-date-picker v-model="project.serviceEndTime"
                                    placeholder="选择时间"
                                    :picker-options="endDatePicker()"
                                    value-format="yyyy-MM-dd"></el-date-picker>
                </el-form-item>
                <el-form-item label="维保对象类型"
                              prop="maintenanceProjectType">
                    <el-select v-model="project.maintenanceProjectType"
                               placeholder="请选择维保对象类型">
                        <el-option v-for="item in maintenanceProjectTypeList"
                                   :key="item.name"
                                   :label="item.value"
                                   :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="采购类型"
                              prop="procureType">
                    <el-select v-model="project.procureType"
                               placeholder="请选择采购类型">
                        <el-option v-for="item in procureTypeList"
                                   :key="item.name"
                                   :label="item.value"
                                   :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="金额(万)"
                              prop="money">
                    <el-input v-model="project.money"></el-input>
                </el-form-item>
                <el-form-item label="设备类型"
                              prop="deviceType">
                    <el-select v-model="project.deviceType"
                               filterable
                               clearable
                               placeholder="请选择设备类型">
                        <el-option v-for="item in deviceTypeList"
                                   :key="item.name"
                                   :label="item.name"
                                   :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="合同供应商"
                              prop="contractProduce">
                    <el-select v-model="project.contractProduce"
                               filterable
                               clearable
                               @change="contractProduceChangeEvent"
                               placeholder="请选择合同供应商">
                        <el-option v-for="item in produceList"
                                   :key="item"
                                   :label="item"
                                   :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="服务供应商"
                              prop="maintenProduce">
                    <el-select v-model="project.maintenProduce"
                               filterable
                               clearable
                               @change="serviceProduceChangeEvent"
                               placeholder="请选择服务厂商">
                        <el-option v-for="item in produceList"
                                   :key="item"
                                   :label="item"
                                   :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="合同联系人名称"
                              prop="contractProduceName">
                    <el-input v-model="project.contractProduceName"></el-input>
                </el-form-item>
                <el-form-item label="服务联系人名称"
                              prop="maintenProduceName">
                    <el-input v-model="project.maintenProduceName"></el-input>
                </el-form-item>
                <el-form-item label="合同联系人邮箱"
                              prop="contractProduceEmail">
                    <el-input v-model="project.contractProduceEmail"></el-input>
                </el-form-item>
                <el-form-item label="服务联系人邮箱"
                              prop="maintenProduceEmail">
                    <el-input v-model="project.maintenProduceEmail"></el-input>
                </el-form-item>
                <el-form-item label="合同联系人电话"
                              prop="contractProducePhone">
                    <el-input v-model="project.contractProducePhone"></el-input>
                </el-form-item>
                <el-form-item label="服务联系人电话"
                              prop="maintenProducePhone">
                    <el-input v-model="project.maintenProducePhone"></el-input>
                </el-form-item>
                <el-form-item label="设备区域"
                              prop="localDeviceArea">
                    <span slot="label">
                        <font style="color:#F56C6C;margin-left:-10px;">*</font> 设备区域
                    </span>
                    <el-select v-model="localDeviceArea"
                               multiple
                               collapse-tags
                               size="mini"
                               filterable
                               placeholder="请选择设备区域">
                        <el-option v-for="item in deviceAreaList"
                                   :key="item.name"
                                   :label="item.name"
                                   :value="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="税前金额(万)"
                              prop="preTax">
                    <el-input v-model="project.preTax"></el-input>
                </el-form-item>
                <el-form-item label="税率(%)"
                              prop="taxRate">
                    <el-input v-model="project.taxRate"></el-input>
                </el-form-item>
                <el-form-item label="税后金额(万)"
                              prop="afterTax">
                    <el-input v-model="project.afterTax"></el-input>
                </el-form-item>
                <el-form-item label="单价(万)"
                              prop="unitPrice">
                    <el-input v-model="project.unitPrice"></el-input>
                </el-form-item>
                <el-form-item label="总价(万)"
                              prop="totalPrice">
                    <el-input v-model="project.totalPrice"></el-input>
                </el-form-item>
                <el-form-item label="结算方式"
                              prop="payMethod">
                    <el-select v-model="project.payMethod"
                               filterable
                               clearable
                               placeholder="请选择设备类型">
                        <el-option v-for="item in payMethodList"
                                   :key="item.name"
                                   :label="item.name"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="折扣后金额(万)"
                              prop="discountAmount">
                    <el-input v-model="project.discountAmount"></el-input>
                </el-form-item>
                <el-form-item label="折扣率(%)"
                              prop="discountRate">
                    <el-input v-model="project.discountRate"></el-input>
                </el-form-item>
            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="save()">保存</el-button>
            <el-button @click="cancel()">取消</el-button>
        </section>
    </el-dialog>
</template>

<script>
    import rbmaintenanceCommonUtil from 'src/services/mainten/rb-cmdb-mainten-common.js'
    export default {
        props: ['dialogMsg'],
        components: {
            YwInputAdd: () => import('src/components/common/yw-input-add.vue'),
            YwSelect: () => import('src/components/common/yw-select.vue')
        },
        mounted() {
            this.getProduceList()
            this.getServiceType()
            this.getMaintenanceList()
            this.getDeviceAreaList()
            this.getMaintenanceProjectTypeList()
            this.getProcureTypeList()
            this.getDeviceTypeList()
            this.getPayMethodList()
            if (this.dialogMsg.data.id) {
                // 获取维保项目详细信息
                this.getProjectDetail()
            }
        },
        data() {
            return {
                project: {
                    id: '',
                    projectName: '',
                    projectNo: '',
                    serviceType: '',
                    serviceStartTime: '',
                    serviceEndTime: '',
                    maintenanceType: '',
                    deviceArea: '',
                    maintenanceProjectType: '',
                    procureType: '',
                    money: '',
                    deviceType: '',
                    maintenProduce: '',
                    maintenProduceName: '',
                    maintenProduceEmail: '',
                    maintenProducePhone: '',
                    contractProduce: '',
                    contractProduceName: '',
                    contractProduceEmail: '',
                    contractProducePhone: '',
                    preTax: '',
                    taxRate: '',
                    afterTax: '',
                    unitPrice: '',
                    totalPrice: '',
                    payMethod: '',
                    discountAmount: '',
                    discountRate: '',
                    quarterFlag: ''
                },
                // 结算方式
                payMethodList: [],
                // 用户选择后的设备区域值
                localDeviceArea: [],
                // 设备类型
                deviceTypeList: [{
                    name: '个人PC',
                    value: '个人PC'
                }],
                // 厂商
                produceList: [],
                // 采购类型
                procureTypeList: [{
                    name: '公开招标',
                    value: '公开招标'
                }],
                // 维保对象类型
                maintenanceProjectTypeList: [{
                    name: '硬件',
                    value: '硬件'
                }, {
                    name: '软件',
                    value: '软件'
                }],
                // 设备区域
                deviceAreaList: [{
                    name: '哈尔滨资源池',
                    value: '哈尔滨资源池'
                }, {
                    name: '信息港资源池',
                    value: '信息港资源池'
                }],
                // 维保类型
                maintenanceList: [],
                // 服务类型
                serviceTypeList: [],
                // 厂商键值对
                produceMp: null,
                rules: {
                    projectName: [{ required: true, message: '请输入项目名称', trigger: 'change' }],
                    serviceType: [{ required: true, message: '请选择服务类型', trigger: 'change' }],
                    serviceStartTime: [{ required: true, message: '请选择服务开始时间', trigger: 'change' }],
                    serviceEndTime: [{ required: true, message: '请选择服务结束时间', trigger: 'change' }],
                    localDeviceArea: [{ validator: (rule, value, callback) => this.validDeviceArea(rule, value, callback), trigger: 'change' }],
                    maintenanceProjectType: [{ required: true, message: '请选择维保对象类型', trigger: 'change' }],
                    money: [{ validator: (rule, value, callback) => this.validMoneyNumber(rule, value, callback), trigger: 'change' }],
                    procureType: [{ required: true, message: '请选择采购类型', trigger: 'change' }],
                    maintenProduce: [{ required: true, message: '请输入维保供应商', trigger: 'change' }],
                    maintenProducePhone: [{ validator: (rule, value, callback) => this.validPhone(rule, value, callback), trigger: 'change' }],
                    maintenProduceEmail: [{ validator: (rule, value, callback) => this.validEmail(rule, value, callback), trigger: 'change' }],
                    contractProducePhone: [{ validator: (rule, value, callback) => this.validPhone(rule, value, callback), trigger: 'change' }],
                    contractProduceEmail: [{ validator: (rule, value, callback) => this.validEmail(rule, value, callback), trigger: 'change' }],
                    preTax: [{ validator: (rule, value, callback) => this.validMoneyNumber(rule, value, callback), trigger: 'change' }],
                    taxRate: [{ validator: (rule, value, callback) => this.validPercentage(rule, value, callback), trigger: 'change' }],
                    afterTax: [{ validator: (rule, value, callback) => this.validMoneyNumber(rule, value, callback), trigger: 'change' }],
                    unitPrice: [{ validator: (rule, value, callback) => this.validMoneyNumber(rule, value, callback), trigger: 'change' }],
                    totalPrice: [{ validator: (rule, value, callback) => this.validMoneyNumber(rule, value, callback), trigger: 'change' }],
                    discountAmount: [{ validator: (rule, value, callback) => this.validMoneyNumber(rule, value, callback), trigger: 'change' }],
                    discountRate: [{ validator: (rule, value, callback) => this.validPercentage(rule, value, callback), trigger: 'change' }]
                }
            }
        },
        methods: {
            // 服务供应商变动联动
            serviceProduceChangeEvent(value) {
                const obj = this.produceMp.get(value)
                console.info(obj)
                // 如果文本框已经填写值，则用填写值；若没有值，则联通厂家的联系人信息
                if (this.project.maintenProduceName == null || this.project.maintenProduceName == '') {
                    this.project.maintenProduceName = obj.mainteName
                }
                if (this.project.maintenProduceEmail == null || this.project.maintenProduceEmail == '') {
                    this.project.maintenProduceEmail = obj.maintenEmail
                }
                if (this.project.maintenProducePhone == null || this.project.maintenProducePhone == '') {
                    this.project.maintenProducePhone = obj.maintePhone
                }
            },
            // 合同供应商变动联动
            contractProduceChangeEvent(value) {
                const obj = this.produceMp.get(value)
                if (this.project.contractProduceName == null || this.project.contractProduceName == '') {
                    this.project.contractProduceName = obj.contractName
                }
                if (this.project.contractProduceEmail == null || this.project.contractProduceEmail == '') {
                    this.project.contractProduceEmail = obj.contractEmail
                }
                if (this.project.contractProducePhone == null || this.project.contractProducePhone == '') {
                    this.project.contractProducePhone = obj.contractPhone
                }
            },
            // 验证输入框的百分格式
            validPercentage(rule, value, callback) {
                if (value == null || value == '') {
                    return callback()
                }
                const regex = /^([1-9]\d?|100)$/
                if (value && !regex.test(value)) {
                    return callback(new Error('请输入[1,100]之间的整数'))
                }
                return callback()
            },
            // 验证输入框的金钱格式
            validMoneyNumber(rule, value, callback) {
                if (value == null || value == '') {
                    return callback()
                }
                // 数字转字符串
                value = value.toString()
                // 特殊处理 0 和 0.00
                if (value == '0' || (value.indexOf('.') != -1 && value.charAt(value.length - 1) == '0' && value.charAt(0) == '0')) {
                    return callback(new Error('请输入大于零的正数'))
                }
                const pattern = /^[+]?(0|[1-9]\d*)(\.\d{1,6})?$/
                if (value && !pattern.test(value)) {
                    if (value.indexOf('.') != -1 && value.substr(value.lastIndexOf('.')).length > 7) {
                        return callback(new Error('浮点数不超过6位'))
                    } else {
                        return callback(new Error('请输入格式正确的正数'))
                    }
                }
                return callback()
            },
            // 验证手机号码
            validPhone(rule, value, callback) {
                if (value == null || value == '') {
                    return callback()
                }
                if (!(/^1(3|4|5|6|7|8|9)\d{9}$/.test(value))) {
                    return callback('手机号码有误，请重填')
                }
                return callback()
            },
            // 验证邮箱
            validEmail(rule, value, callback) {
                if (value == null || value == '') {
                    return callback()
                }
                if (!(/^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/.test(value))) {
                    return callback('邮箱有误，请重填')
                }
                return callback()
            },
            // 设备区域值，用逗号连成字符串
            changeDeviceArea(val) {
                if (val.length > 0) {
                    var area = val[0]
                    for (var i = 1; i < val.length; i++) {
                        area = area + ',' + val[i]
                    }
                    return area
                }
            },
            // 根据projectId获取对象属性进而填充输入框
            getProjectDetail() {
                let _t = this
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/maintenance/project/get',
                    params: { 'projectId': _t.dialogMsg.data.id }
                }).then((res) => {
                    if (res) {
                        _t.project = res
                        // 将设备区域的字符串改为数组
                        _t.localDeviceArea = _t.project.deviceArea
                        if (_t.project.deviceArea != null) {
                            _t.localDeviceArea = _t.project.deviceArea.split(',')
                        }
                        if (_t.project.contractProduceInfo == null) {
                            _t.project.contractProduceInfo = {
                                id: ''
                            }
                        }
                        if (_t.project.produceInfo == null) {
                            _t.project.produceInfo = {
                                id: ''
                            }
                        }
                    }
                })
            },
            startDatePicker() {
                const self = this
                return {
                    disabledDate(time) {
                        if (self.project.serviceEndTime) {  // 如果结束时间不为空，则小于结束时间
                            return new Date(self.project.serviceEndTime).getTime() < time.getTime()
                        }
                    }
                }
            },
            endDatePicker() {
                const self = this
                return {
                    disabledDate(time) {
                        if (self.project.serviceStartTime) {  // 如果开始时间不为空，则结束时间大于开始时间
                            return new Date(self.project.serviceStartTime).getTime() > time.getTime()
                        }
                    }
                }
            },
            // 获取维保供应商
            getProduceList() {
                let req = {
                    'params': { 'produce_type': '维保供应商' }
                }
                rbmaintenanceCommonUtil.getProducesByType(req).then((res) => {
                    const list = []
                    const produceMp = new Map()
                    for (let i in res) {
                        list.push(res[i].produce_name)
                        let obj = {
                            mainteName: res[i].provider_cont,
                            maintePhone: res[i].service_concat_phone,
                            maintenEmail: res[i].service_concat_email,
                            contractName: res[i].mainten_concat,
                            contractPhone: res[i].mainten_concat_phone,
                            contractEmail: res[i].mainten_concat_email
                        }
                        produceMp.set(res[i].produce_name, obj)
                    }
                    this.produceMp = produceMp
                    this.produceList = list
                })
            },
            // 获取结算方式列表
            getPayMethodList() {
                let obj = {
                    'type': 'pay_method'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.payMethodList = res
                    return res
                })
            },
            // 获取维保类型列表
            getMaintenanceList() {
                let obj = {
                    'type': 'maintenance_type'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.maintenanceList = res
                    return res
                })
            },
            // 获取设备区域列表
            getDeviceAreaList() {
                let obj = {
                    'type': 'idctype'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.deviceAreaList = res
                    return res
                })
            },
            // 获取维保对象类型列表
            getMaintenanceProjectTypeList() {
                let obj = {
                    'type': 'maintenance_project_type'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.maintenanceProjectTypeList = res
                    return res
                })
            },
            // 获取采购类型列表
            getProcureTypeList() {
                let obj = {
                    'type': 'procure_type'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.procureTypeList = res
                    return res
                })
            },
            // 获取设备类型列表
            getDeviceTypeList() {
                let obj = {
                    'type': 'device_type'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.deviceTypeList = res
                    return res
                })
            },
            // 获取服务类型列表
            getServiceType() {
                let obj = {
                    'type': 'mainten_service_type'
                }
                this.rbHttp.sendRequest({
                    method: 'GET',
                    params: obj,
                    url: '/v1/cmdb/configDict/getDictsByType'
                }).then((res) => {
                    this.serviceTypeList = res
                    return res
                })
            },
            save() {
                this.$refs['projectForm'].validate((valid) => {
                    if (valid) {
                        // 检测维保项目是否已经存在
                        let _t = this
                        this.rbHttp.sendRequest({
                            method: 'GET',
                            url: '/v1/cmdb/maintenance/project/getByProjectName',
                            params: { 'projectName': _t.project.projectName }
                        }).then((res) => {
                            if (res && res.id !== _t.project.id) {
                                _t.$message.error('维保项目名称已经存在, 请更换后提交')
                            } else {
                                _t.saveData()
                            }
                        })
                    }
                })
            },
            saveData() {
                let _t = this
                _t.project.deviceArea = this.changeDeviceArea(this.localDeviceArea)
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/cmdb/maintenance/project/save',
                    data: _t.project
                }).then((res) => {
                    if (res && res.flag === 'success') {
                        _t.$message.success('保存成功')
                        _t.dialogMsg.dialogVisible = false
                        _t.reset()
                    } else {
                        _t.$message.error('保存失败')
                        console.error(res.msg)
                    }
                })
            },
            // 重置项目信息
            reset() {
                this.project = []
                this.selectConcat = ''
                this.selectConcatPhone = ''
                this.selectConcatEmail = ''
            },
            cancel() {
                this.dialogMsg.dialogVisible = false
            },
            validDeviceArea(rule, value, callback) {
                if (this.localDeviceArea == null || this.localDeviceArea == 0) {
                    return callback(new Error('至少选择一个设备区域'))
                }
                return callback()
            }
        }
    }
</script>
<style lang="scss" scoped>
    .tags {
        max-width: 160px;
    }
    /deep/ .el-form-item__content {
        width: 200px;
        div {
            width: 100%;
        }
        .el-input {
            width: 100%;
        }
    }
</style>

