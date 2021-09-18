<template>
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <ul class="conflict-title">
            <li><span>冲突IP总数：</span><span>{{clashCount.clashTotal}}</span></li>
            <li><span>IP冲突次数：</span><span>{{clashCount.changeTotal}}</span></li>
            <li><span>待处理IP数：</span><span>{{clashCount.pendingTotal}}</span></li>
            <li><span>已处理IP数：</span><span>{{clashCount.processedTotal}}</span></li>
        </ul>
        <el-form class="yw-form"
                 :model="dialogForm"
                 :inline="true"
                 :rules="formRules"
                 label-width="85px"
                 ref="dialogForm">
            <el-form-item label="检测时间"
                          prop="time">
                <el-date-picker v-model="dialogForm.time"
                                format="yyyy-MM-dd HH:mm:ss"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                type="datetimerange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="IP"
                          label-width="30px"
                          prop="clashIp">
                <el-input v-model="dialogForm.clashIp"></el-input>
                <el-popover placement="bottom"
                            width="400"
                            trigger="manual"
                            v-model="visible">
                    <div class="many-ip">
                        <el-input type="textarea"
                                  v-model="manyIp"
                                  :rows="6"
                                  placeholder="可输入多个ip进行查询，逗号分隔或者回车"></el-input>
                        <div style="margin-top:10px;text-align:center;">
                            <el-button type="primary"
                                       @click="IpSure">确认</el-button>
                            <el-button @click="visible=false">取消</el-button>
                        </div>
                    </div>
                    <span slot="reference"
                          class="el-icon-plus add-ip"
                          @click="IpCheck"></span>
                </el-popover>
            </el-form-item>
            <el-form-item label="网关设备IP"
                          prop="gateway">
                <el-input v-model="dialogForm.gateway"></el-input>
            </el-form-item>
            <el-form-item label="所属资源池"
                          prop="resource">
                <el-select v-model="dialogForm.resource"
                           filterable
                           placeholder="请选择">
                    <el-option v-for="item in resourceOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="处理状态"
                          prop="handleStatus">
                <el-select v-model="dialogForm.handleStatus"
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in statusOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="是否已通知"
                          prop="isNotify">
                <el-select v-model="dialogForm.isNotify"
                           style="width:120px;"
                           placeholder="请选择">
                    <el-option v-for="item in notifyOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="工单号"
                          label-width="55px"
                          prop="jobNumber">
                <el-input v-model="dialogForm.jobNumber"></el-input>
            </el-form-item>
            <el-form-item label="数据来源"
                          prop="checkType">
                <el-select v-model="dialogForm.checkType"
                           placeholder="请选择">
                    <el-option v-for="item in checkTypeOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary"
                           @click="search">查询</el-button>
                <el-button type="primary"
                           @click="handelSenior">高级查询</el-button>
                <el-button @click="reset">重置</el-button>
                <el-button @click="tableExport">导出</el-button>
            </el-form-item>
            <div class="senior-search"
                 v-show="isSenior">
                <el-form-item label="操作时间"
                              prop="updateTime">
                    <el-date-picker v-model="dialogForm.updateTime"
                                    format="yyyy-MM-dd HH:mm:ss"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    type="datetimerange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="绑定MAC地址"
                              label-width="120px"
                              prop="nowMac">
                    <el-input v-model="dialogForm.nowMac"></el-input>
                </el-form-item>
                <!-- <el-form-item label="上次绑定MAC地址"
                              label-width="120px"
                              prop="oldMac">
                    <el-input v-model="dialogForm.oldMac"></el-input>
                </el-form-item> -->
                <el-form-item label="操作人"
                              label-width="55px"
                              prop="operator">
                    <el-input v-model="dialogForm.operator"></el-input>
                </el-form-item>
                <el-form-item label="累计切换次数"
                              prop="changeTotal">
                    <el-input v-model.number="dialogForm.changeTotal"></el-input>
                </el-form-item>
                <!-- <el-form-item label="系统推测"
                              prop="systemInfer">
                    <el-input v-model="dialogForm.systemInfer"></el-input>
                </el-form-item> -->
                <el-form-item label="原因说明"
                              prop="notHandleReason">
                    <el-input v-model="dialogForm.notHandleReason"></el-input>
                </el-form-item>
                <el-form-item label="来源"
                              prop="collectType"
                              label-width="60px">
                    <el-select v-model="dialogForm.collectType"
                               style="width:120px;"
                               placeholder="请选择">
                        <el-option v-for="item in collectTypeOptions"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
            </div>
        </el-form>

        <div class="yw-table">
            <div class="deal-btn">
                <el-button v-if="buttonPermission.ip_check_operator_stop"
                           @click="notHandle">暂不处理</el-button>
                <el-button v-if="buttonPermission.ip_check_operator_continue"
                           @click="continueHandle">继续处理</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          :data="collectionData.tableBody"
                          style="width:100%;cursor: pointer;"
                          stripe
                          border
                          tooltip-effect="dark"
                          :default-sort="{prop: 'date', order: 'descending'}"
                          :cell-style="cellStyle"
                          height="calc(100vh - 340px)"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                     :selectable='checkboxT'
                                     width="55">
                    </el-table-column>
                    <el-table-column v-for="(item,index) in collectionData.tableHeader"
                                     :prop="item.value"
                                     :sortable="item.sortable"
                                     :label="item.label"
                                     :width="item.width"
                                     :show-overflow-tooltip="true"
                                     :key="index"> </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap"
                 v-if="collectionData.pagination.total>0">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="collectionData.pagination.currentPage"
                               :page-sizes="collectionData.pageSizes"
                               :page-size="collectionData.pagination.pageSize"
                               :total="collectionData.pagination.total"
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </div>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   :visible.sync="dialogFormReason"
                   width="300px"
                   :close-on-click-modal="false"
                   title="原因说明">
            <section class="yw-dialog-main">
                <el-input type="textarea"
                          v-model="reasonExplain"
                          placeholder="请输入暂不处理原因说明"
                          :rows="6"></el-input>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="handleReasonSure">确定</el-button>
                <el-button @click="dialogFormReason=false">取消</el-button>
            </section>
        </el-dialog>
        <!-- dialog -->
    </div>
</template>

<script>
    import rbIpCollectionServiceFactory from 'src/services/cmdb/rb-cmdb-ip-management-factory.js'
    import buttonPermission from 'src/services/cmdb/rb-audit-permission.js'
    export default {
        name: 'CmdbCmicIpconflict',
        data() {
            var validateIp = (rule, value, callback) => {
                let newVal = this.deleteSpace(value)
                if (newVal) {
                    let temp = newVal.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedIp = temp.filter(item => !this.isValidIps(item))
                    // if (unqualifiedIp.length > 0) {
                    //     callback(new Error('请输入正确的IP'))
                    // } else {
                    //     this.dialogForm.clashIp = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.clashIp = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            var validateIp1 = (rule, value, callback) => {
                let newVal = this.deleteSpace(value)
                if (newVal) {
                    let temp = newVal.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedIp = temp.filter(item => !this.isValidIps(item))
                    // if (unqualifiedIp.length > 0) {
                    //     callback(new Error('请输入正确的网络设备IP'))
                    // } else {
                    //     this.dialogForm.gateway = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.gateway = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            var checkCount = (rule, value, callback) => {
                if (!value) {
                    return callback()
                }
                if (!Number.isInteger(value)) {
                    callback(new Error('请输入数字值'))
                } else {
                    if (value < 0) {
                        callback(new Error('请输入正整数'))
                    } else {
                        callback()
                    }

                }
            }
            return {
                dialogFormReason: false,
                reasonExplain: '',
                loading: false,
                clashCount: {
                    changeTotal: '',
                    clashTotal: '',
                    pendingTotal: '',
                    processedTotal: ''
                },
                dialogForm: {
                    time: '',
                    clashIp: '',
                    gateway: '',
                    resource: '',
                    handleStatus: '',
                    isNotify: '',
                    jobNumber: '',
                    updateTime: '',
                    nowMac: '',
                    oldMac: '',
                    operator: '',
                    changeTotal: '',
                    // systemInfer: '',
                    notHandleReason: '',
                    collectType: '',
                    checkType: '1'
                },
                resourceOptions: [],
                statusOptions: [
                    {
                        label: '待处理',
                        value: '0'
                    },
                    {
                        label: '暂不处理',
                        value: '1'
                    },
                    {
                        label: '处理中',
                        value: '2'
                    },
                    {
                        label: '已处理',
                        value: '3'
                    }
                ],
                notifyOptions: [
                    {
                        label: '否',
                        value: '0'
                    },
                    {
                        label: '是',
                        value: '1'
                    }
                ],
                checkTypeOptions: [
                    {
                        label: '系统比对',
                        value: '0'
                    },
                    {
                        label: '复核比对',
                        value: '1'
                    }
                ],
                collectTypeOptions: [
                    {
                        label: '自动化一期',
                        value: '1'
                    },
                    {
                        label: '自动化二期',
                        value: '2'
                    }
                ],
                collectionData: {
                    tableHeader: [
                        {
                            label: '检测时间',
                            value: 'checkTime',
                            sortable: true,
                            width: '160px'
                        },
                        {
                            label: '冲突IP',
                            value: 'clashIp',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '绑定MAC地址',
                            value: 'nowMac',
                            sortable: false,
                            width: '220px'
                        },
                        // {
                        //     label: '上次绑定MAC地址',
                        //     value: 'oldMac',
                        //     sortable: false,
                        //     width: '125px'
                        // },
                        {
                            label: '累计切换次数',
                            value: 'changeTotal',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '系统推测',
                            value: 'systemInfer',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '网关设备IP',
                            value: 'gateway',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '所属资源池',
                            value: 'resource',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '处理状态',
                            value: 'handleStatusText',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '原因说明',
                            value: 'notHandleReason',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '操作人',
                            value: 'operator',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '操作时间',
                            value: 'updateTime',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '是否已通知',
                            value: 'isNotify',
                            sortable: false,
                            width: '100px'
                        },
                        {
                            label: '工单号',
                            value: 'jobNumber',
                            sortable: false,
                            width: '120px'
                        },
                        {
                            label: '来源',
                            value: 'collectType',
                            sortable: false,
                            width: '120px'
                        }
                    ],
                    tableBody: [],
                    pagination: {
                        currentPage: 1,
                        pageSize: 20,
                        total: 0
                    },
                    pageSizes: [10, 20, 50, 100]
                },
                formRules: {
                    clashIp: [{ validator: validateIp, trigger: 'blur' }],
                    gateway: [{ validator: validateIp1, trigger: 'blur' }],
                    changeTotal: [{ validator: checkCount, trigger: 'blur' }]
                },
                isSenior: false,
                visible: false,
                manyIp: '',
                multipleSelection: [],
                buttonPermission: buttonPermission
            }
        },
        created() {
            let currentTime = this.getCurrentDate(new Date().getTime())
            let currentStartTime = currentTime + ' ' + '00:00:00'
            let currentEndTime = currentTime + ' ' + '23:59:59'
            this.dialogForm.time = [currentStartTime, currentEndTime]
            this.getResource()
            this.getTableList()
        },
        methods: {
            // element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            async getResource() {
                try {
                    let res = await rbIpCollectionServiceFactory.getResourcePool()
                    this.resourceOptions = res.data
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            async getTableList(type) {
                this.loading = true
                let queryData = {}
                if (type) {
                    this.collectionData.pagination.currentPage = 1
                }
                for (let prop in this.dialogForm) {
                    if (prop && prop !== 'time' && prop !== 'updateTime') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                queryData.startTime = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.endTime = this.dialogForm.time ? this.dialogForm.time[1] : ''
                queryData.updateStartTime = this.dialogForm.updateTime ? this.dialogForm.updateTime[0] : ''
                queryData.updateEndTime = this.dialogForm.updateTime ? this.dialogForm.updateTime[1] : ''
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.clashIpSearch(queryData)
                    this.collectionData.tableBody = res.data || []
                    this.collectionData.pagination.total = res.totalSize
                    this.clashCount = res.topTotal
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    this.loading = false
                }

            },
            search() {
                this.$refs['dialogForm'].validate((valid) => {
                    if (valid) {
                        this.getTableList('find')
                    } else {
                        return false
                    }
                })
            },
            reset() {
                this.$refs['dialogForm'].resetFields()
                this.getTableList('find')
            },
            async tableExport() {
                const loading = this.$loading({
                    lock: true,
                    text: '正在下载中，请等待...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                })
                let queryData = {}
                for (let prop in this.dialogForm) {
                    if (prop && prop !== 'time') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                queryData.startTime = this.dialogForm.time ? this.dialogForm.time[0] : ''
                queryData.endTime = this.dialogForm.time ? this.dialogForm.time[1] : ''
                try {
                    let res = await rbIpCollectionServiceFactory.clashIpExport(queryData)
                    let blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    let isSupportDownload = 'download' in document.createElement('a')
                    if (isSupportDownload) {
                        const link = document.createElement('a')
                        link.download = '冲突IP列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '冲突IP列表.xlsx')
                    }
                } catch (error) {
                    console.log(error)
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                } finally {
                    loading.close()
                }
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.collectionData.pagination.pageSize = val
                this.getTableList('find')

            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.collectionData.pagination.currentPage = val
                this.getTableList()
            },
            checkboxT(row) {
                if (row.handleStatus === '3') {
                    return false
                } else {
                    return true
                }
            },
            // 高级查询
            handelSenior() {
                this.isSenior = !this.isSenior
            },
            IpCheck() {
                this.visible = true
                this.manyIp = ''
            },
            IpSure() {
                // let __this = this
                let manyIp = this.manyIp.replace(/^\s+|\s+$/g, '')
                if (manyIp) {
                    let temp = manyIp.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedIp = temp.filter(item => !__this.isValidIps(item))
                    // if (unqualifiedIp.length > 0) {
                    //     this.$alert(`以下IP不合法：${unqualifiedIp.join(',')}`, '提示')
                    // } else {
                    //     this.visible = false
                    //     this.dialogForm.clashIp = temp.join(',')
                    // }
                    this.visible = false
                    this.dialogForm.clashIp = temp.join(',')

                } else {
                    this.$message({
                        type: 'error',
                        message: '文本框不能为空，请输入IP'
                    })
                }

            },
            // 暂不处理
            notHandle() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.dialogFormReason = true
                    this.reasonExplain = ''
                }

            },
            // 继续处理
            continueHandle() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请至少选择一条数据', '通知', {
                        confirmButtonText: '确定'
                    })
                } else {
                    this.getHandleData('0')
                }

            },
            // 说明原因确认
            handleReasonSure() {
                if (this.deleteSpace(this.reasonExplain)) {
                    this.getHandleData('1')
                    this.dialogFormReason = false
                } else {
                    this.$message({ type: 'error', message: '说明原因不能为空' })
                }
            },
            // 暂不处理和继续处理调用接口方法
            async getHandleData(type) {
                let newArray = []
                for (let item of this.multipleSelection) {
                    if (item && item.mainId) {
                        newArray.push(item.mainId)
                    }
                }
                let queryData = {
                    mainId: newArray,
                    handleStatus: type,
                    operator: sessionStorage.getItem('username')
                }
                if (type === '1') {
                    queryData.notHandleReason = this.reasonExplain
                }
                try {
                    let res = await rbIpCollectionServiceFactory.clashIpStatus(queryData)
                    this.$message({
                        message: res.msg,
                        type: 'success'
                    })
                    this.getTableList('find')
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            // ip格式正则校验
            isValidIps(ip) {
                const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
                return reg.test(ip)
            },
            // 去掉字符串前后空格
            deleteSpace(str) {
                return str.replace(/^\s+|\s+$/g, '')

            },
            // 日期格式转换
            formatDate(data) {
                let date = new Date(data)
                let YY = date.getFullYear() + '-'
                let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                let hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':'
                let mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':'
                let ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds())
                return YY + MM + DD + ' ' + hh + mm + ss
            },
            getCurrentDate(data) {
                let date = new Date(data)
                let YY = date.getFullYear() + '-'
                let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                return YY + MM + DD
            },
            cellStyle({ row, columnIndex }) {
                if (columnIndex === 9 && row.handleStatus === '0') {
                    return 'color:red;'
                } else {
                    return ''
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    .yw-form {
        display: block !important;
        flex: 0 !important;
    }
    .yw-form .el-form-item__content > .el-input {
        width: 178px;
    }
    .el-date-editor.el-date-editor--datetimerange.el-input__inner {
        width: 312px;
    }
    div.el-select {
        width: 178px;
    }
    .add-ip {
        color: #169ef4;
        font-size: 20px;
        cursor: pointer;
        vertical-align: sub;
    }
    .yw-table {
        margin-top: 20px;
    }
    .conflict-title {
        display: flex;
        color: red;
        font-size: 17px;
        margin-bottom: 10px;
        li {
            margin-right: 60px;
        }
    }
    .deal-btn {
        margin-bottom: 10px;
    }
</style>
