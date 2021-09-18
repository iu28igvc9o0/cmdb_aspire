<template>
    <!-- Ip采集存活IP -->
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <ul class="conflict-title">
            <li><span>存活IP数：</span><span>{{ipCount.ipTotal}}</span></li>
            <li><span>IPV4：</span><span>{{ipCount.ipv4Total}}</span></li>
            <li><span>IPV6：</span><span>{{ipCount.ipv6Total}}</span></li>
        </ul>
        <el-form class="yw-form"
                 :model="dialogForm"
                 :inline="true"
                 :rules="formRules"
                 label-width="85px"
                 ref="dialogForm">
            <el-form-item label="检测时间"
                          prop="testTime">
                <el-date-picker v-model="dialogForm.testTime"
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
                          prop="ip">
                <el-input v-model="dialogForm.ip"></el-input>
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
            <el-form-item label="网络设备IP"
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
            <el-form-item label="IP类型"
                          prop="iptype">
                <el-select v-model="dialogForm.iptype"
                           placeholder="请选择">
                    <el-option v-for="item in ipOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="MAC地址"
                          prop="mac">
                <el-input v-model="dialogForm.mac"></el-input>
                <el-popover placement="bottom"
                            width="400"
                            trigger="manual"
                            v-model="visibleAddress">
                    <div class="many-ip">
                        <el-input type="textarea"
                                  v-model="manyMac"
                                  :rows="6"
                                  placeholder="可输入多个MAC地址进行查询，逗号分隔或者回车"></el-input>
                        <div style="margin-top:10px;text-align:center;">
                            <el-button type="primary"
                                       @click="addressSure">确认</el-button>
                            <el-button @click="visibleAddress=false">取消</el-button>
                        </div>
                    </div>
                    <span slot="reference"
                          class="el-icon-plus add-ip"
                          @click="MacCheck"></span>
                </el-popover>
            </el-form-item>
            <el-form-item label="数据来源"
                          prop="source">
                <el-select v-model="dialogForm.source"
                           placeholder="请选择">
                    <el-option v-for="item in sourceOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <!-- <el-form-item label="IP去重"
                          prop="oneIpFlag">
                <el-select v-model="dialogForm.oneIpFlag"
                           placeholder="请选择"
                           style="width:120px;">
                    <el-option v-for="item in oneIpFlagOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item> -->
            <el-form-item>
                <el-button type="primary"
                           @click="search">查询</el-button>
                <el-button @click="reset">重置</el-button>
                <el-button @click="tableExport">导出</el-button>
            </el-form-item>
        </el-form>

        <div class="yw-table">
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          border
                          :data="collectionData.tableBody"
                          style="cursor: pointer;"
                          stripe
                          tooltip-effect="dark"
                          @sort-change="sortChange"
                          height="calc(100vh - 310px)">
                    <el-table-column v-for="(item,index) in collectionData.tableHeader"
                                     :prop="item.value"
                                     :sortable="item.sortable"
                                     :label="item.label"
                                     :show-overflow-tooltip="true"
                                     :key="index"> </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap"
                 v-show="collectionData.pagination.total>0">
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
    </div>
</template>

<script>
    import rbIpCollectionServiceFactory from 'src/services/cmdb/rb-cmdb-ip-management-factory.js'
    export default {
        name: 'CmdbCmicIpsurvival',
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
                    //     this.dialogForm.ip = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.ip = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            var validateGateWay = (rule, value, callback) => {
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
            var validateMac = (rule, value, callback) => {
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
                    // let unqualifiedMac = temp.filter(item => !this.isValidMac(item))
                    // if (unqualifiedMac.length > 0) {
                    //     callback(new Error('请输入正确的MAC地址'))
                    // } else {
                    //     this.dialogForm.mac = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.mac = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            return {
                loading: false,
                dialogForm: {
                    testTime: '',
                    ip: '',
                    mac: '',
                    resource: '',
                    iptype: '',
                    gateway: '',
                    source: '',
                    // oneIpFlag: ''
                },
                resourceOptions: [],
                sourceOptions: [],
                ipOptions: [{
                    value: '',
                    label: '请选择'
                }, {
                    value: 'ipv4',
                    label: 'ipv4'
                }, {
                    value: 'ipv6',
                    label: 'ipv6'
                }],
                // oneIpFlagOptions: [
                //     {
                //         value: '是',
                //         label: '是'
                //     }, {
                //         value: '否',
                //         label: '否'
                //     }
                // ],
                formRules: {
                    ip: [{ validator: validateIp, trigger: 'blur' }],
                    gateway: [{ validator: validateGateWay, trigger: 'blur' }],
                    mac: [{ validator: validateMac, trigger: 'blur' }]
                },
                collectionData: {
                    tableHeader: [
                        {
                            label: '检测时间',
                            value: 'time',
                            sortable: false
                        },
                        {
                            label: 'IP',
                            value: 'ip',
                            sortable: 'custom'
                        },
                        {
                            label: 'MAC地址',
                            value: 'mac',
                            sortable: false
                        },
                        {
                            label: 'IP类型',
                            value: 'iptype',
                            sortable: false
                        },
                        {
                            label: '数据来源',
                            value: 'source',
                            sortable: false
                        },
                        {
                            label: '网关设备IP',
                            value: 'gateway',
                            sortable: false
                        },
                        {
                            label: '所属资源池',
                            value: 'resource',
                            sortable: false
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
                visible: false,
                manyIp: '',
                visibleAddress: false,
                manyMac: '',
                ipCount: {
                    ipTotal: '',
                    ipv4Total: '',
                    ipv6Total: ''
                },
                ipSort: ''
            }
        },
        created() {
            let currentTime = this.formatDate(new Date().getTime())
            let currentStartTime = currentTime + ' ' + '00:00:00'
            let currentEndTime = currentTime + ' ' + '23:59:59'
            this.dialogForm.testTime = [currentStartTime, currentEndTime]
            this.getResource()
            this.getSourcePool()
        },
        methods: {
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
            async getSourcePool() {
                try {
                    let res = await rbIpCollectionServiceFactory.getSourcePool()
                    let totalIp = res.data.filter(item => item.label == '全量存活IP')
                    this.dialogForm.source = totalIp[0].value
                    this.sourceOptions = res.data
                    this.getTableList()
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
                if (type == 'asc') {
                    queryData.ipSort = type
                } else if (type == 'desc') {
                    queryData.ipSort = type
                } else if (this.ipSort) {
                    queryData.ipSort = this.ipSort
                }
                for (let prop in this.dialogForm) {
                    if (prop && prop !== 'testTime') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                queryData.startTime = this.dialogForm.testTime ? this.dialogForm.testTime[0] : ''
                queryData.endTime = this.dialogForm.testTime ? this.dialogForm.testTime[1] : ''
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.survivalIpSearch(queryData)
                    this.collectionData.tableBody = res.data || []
                    this.collectionData.pagination.total = res.totalSize
                    this.ipCount = res.topTotal
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
                    if (prop && prop !== 'testTime') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                queryData.startTime = this.dialogForm.testTime ? this.dialogForm.testTime[0] : ''
                queryData.endTime = this.dialogForm.testTime ? this.dialogForm.testTime[1] : ''
                try {
                    let res = await rbIpCollectionServiceFactory.survialIpExport(queryData)
                    let blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    let isSupportDownload = 'download' in document.createElement('a')
                    if (isSupportDownload) {
                        const link = document.createElement('a')
                        link.download = '存活IP列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '存活IP列表.xlsx')
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
            sortChange(column) {
                if (column.prop == 'ip' && column.order == 'ascending') {
                    this.ipSort = 'asc'
                    this.getTableList('asc')
                } else if (column.prop == 'ip' && column.order == 'descending') {
                    this.ipSort = 'desc'
                    this.getTableList('desc')
                } else {
                    this.ipSort = ''
                    return
                }
            },
            IpCheck() {
                this.visible = true
                this.manyIp = ''
            },
            MacCheck() {
                this.visibleAddress = true
                this.manyMac = ''
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
                    //     this.dialogForm.ip = temp.join(',')
                    // }
                    this.visible = false
                    this.dialogForm.ip = temp.join(',')

                } else {
                    this.$message({
                        type: 'error',
                        message: '文本框不能为空，请输入IP'
                    })
                }

            },
            addressSure() {
                // let __this = this
                let manyMac = this.manyMac.replace(/^\s+|\s+$/g, '')
                if (manyMac) {
                    let temp = manyMac.split(/[\n,，]/g)
                    for (let i = 0; i < temp.length; i++) {
                        if (temp[i] == '') {
                            temp.splice(i, 1)
                            // 删除数组索引位置应保持不变
                            i--
                        }
                    }
                    // let unqualifiedMac = temp.filter(item => !__this.isValidMac(item))
                    // if (unqualifiedMac.length > 0) {
                    //     this.$alert(`以下MAC地址不合法：${unqualifiedMac.join(',')}`, '提示')
                    // } else {
                    //     this.visibleAddress = false
                    //     this.dialogForm.mac = temp.join(',')
                    // }
                    this.visibleAddress = false
                    this.dialogForm.mac = temp.join(',')

                } else {
                    this.$message({
                        type: 'error',
                        message: '文本框不能为空，请输入MAC地址'
                    })
                }
            },
            // ip格式正则校验
            isValidIps(ip) {
                const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
                return reg.test(ip)
            },
            // MAC地址正则校验
            isValidMac(address) {
                const reg = /^[A-F0-9]{2}(-[A-F0-9]{2}){5}$|^[A-F0-9]{2}(:[A-F0-9]{2}){5}$/
                return reg.test(address)
            },
            // 去掉字符串前后空格
            deleteSpace(str) {
                return str.replace(/^\s+|\s+$/g, '')
            },
            // 日期获取
            formatDate(data) {
                let date = new Date(data)
                let YY = date.getFullYear() + '-'
                let MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                let DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                return YY + MM + DD
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
</style>
