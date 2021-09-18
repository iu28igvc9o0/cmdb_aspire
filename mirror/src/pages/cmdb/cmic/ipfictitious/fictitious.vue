<template>
    <!-- Ip采集虚拟IP -->
    <div class="components-container yw-dashboard"
         v-loading="loading">
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
                          prop="vip">
                <el-input v-model="dialogForm.vip"></el-input>
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
            <el-form-item label="虚拟IP使用类型"
                          prop="usetype"
                          label-width="100px">
                <el-select v-model="dialogForm.usetype"
                           placeholder="请选择">
                    <el-option v-for="item in useTypeOptions"
                               :key="item.value"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="当前绑定IP"
                          prop="bindip">
                <el-input v-model="dialogForm.bindip"></el-input>
            </el-form-item>
            <el-form-item label="漂移IP列表"
                          prop="iplist">
                <el-input v-model="dialogForm.iplist"></el-input>
            </el-form-item>
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
                          :default-sort="{prop: 'date', order: 'descending'}"
                          height="calc(100vh - 270px)">
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
        name: 'CmdbCmicIpfictitious',
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
                    //     this.dialogForm.vip = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.vip = temp.join(',')
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
                    //     callback(new Error('请输入正确的当前绑定IP'))
                    // } else {
                    //     this.dialogForm.bindip = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.bindip = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            var validateIp2 = (rule, value, callback) => {
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
                    //     callback(new Error('请输入正确的漂移IP列表'))
                    // } else {
                    //     this.dialogForm.iplist = temp.join(',')
                    //     callback()
                    // }
                    this.dialogForm.iplist = temp.join(',')
                    callback()

                } else {
                    callback()
                }
            }
            return {
                loading: false,
                dialogForm: {
                    testTime: '',
                    vip: '',
                    resource: '',
                    usetype: '',
                    bindip: '',
                    iplist: ''
                },
                resourceOptions: [],
                useTypeOptions: [],
                formRules: {
                    vip: [{ validator: validateIp, trigger: 'blur' }],
                    bindip: [{ validator: validateIp1, trigger: 'blur' }],
                    iplist: [{ validator: validateIp2, trigger: 'blur' }]
                },
                collectionData: {
                    tableHeader: [
                        {
                            label: '检测时间',
                            value: 'time',
                            sortable: true
                        },
                        {
                            label: '虚拟IP',
                            value: 'vip',
                            sortable: false
                        },
                        {
                            label: '当前绑定IP',
                            value: 'bindip',
                            sortable: false
                        },
                        {
                            label: '漂移IP列表',
                            value: 'iplist',
                            sortable: false
                        },
                        {
                            label: '虚拟IP使用类型',
                            value: 'usetype',
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
            }
        },
        created() {
            this.getResource()
            this.getUseType()
            this.getTableList()
        },
        methods: {
            async getResource() {
                try {
                    let res = await rbIpCollectionServiceFactory.fictitiousResourcePool()
                    this.resourceOptions = res.data
                } catch (error) {
                    this.$message({
                        message: error.data.errors[0].message,
                        type: 'error'
                    })
                }
            },
            async getUseType() {
                try {
                    let res = await rbIpCollectionServiceFactory.fictitiousGetUserType()
                    this.useTypeOptions = res.data
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
                    if (prop && prop !== 'testTime') {
                        queryData[prop] = this.dialogForm[prop]
                    }

                }
                queryData.startTime = this.dialogForm.testTime ? this.dialogForm.testTime[0] : ''
                queryData.endTime = this.dialogForm.testTime ? this.dialogForm.testTime[1] : ''
                queryData.pageSize = this.collectionData.pagination.pageSize
                queryData.pageNo = this.collectionData.pagination.currentPage
                try {
                    let res = await rbIpCollectionServiceFactory.fictitiousIpSearch(queryData)
                    this.collectionData.tableBody = res.data || []
                    this.collectionData.pagination.total = res.totalSize
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
                    let res = await rbIpCollectionServiceFactory.fictitiousIpExport(queryData)
                    let blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
                    let isSupportDownload = 'download' in document.createElement('a')
                    if (isSupportDownload) {
                        const link = document.createElement('a')
                        link.download = '虚拟IP列表' // a标签添加属性
                        link.style.display = 'none'
                        link.href = URL.createObjectURL(blob)
                        document.body.appendChild(link)
                        link.click()
                        document.body.removeChild(link)
                    } else {
                        window.navigator.msSaveBlob(blob, '虚拟IP列表.xlsx')
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
                    //     this.dialogForm.vip = temp.join(',')
                    // }
                    this.visible = false
                    this.dialogForm.vip = temp.join(',')

                } else {
                    this.$message({
                        type: 'error',
                        message: '文本框不能为空，请输入IP'
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
        width: 178px !important;
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
</style>
