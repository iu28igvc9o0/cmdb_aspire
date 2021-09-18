<template>
    <div class="components-container">

        <el-form class="yw-form">

            <div class="table-operate-wrap clearfix">
                <!-- <div class="fr"><span class="fr-num">上报总数：{{count}}</span></div> -->
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="resourceExport()">导出</el-button>
                <el-button type="text"
                           icon="el-icon-message"
                           @click="goMail()">租户短信通知</el-button>
                <template v-if="$route.query.keyComment">
                    <template v-if="$route.query.keyComment.indexOf('列头柜')>-1||$route.query.keyComment.indexOf('机柜')>-1">
                        <span class="mgl"
                              v-if="$route.query.keyComment.indexOf('列头柜')>-1">管理机柜数（{{totalParams.cabinetCount}}）/机柜异常数（{{totalParams.cabinetAlertCount}}）</span>
                        <span class="mgl">实际设备管理数（{{totalParams.deviceCount}}）/监控设备数（{{totalParams.monitorCount}}）/监控设备异常数（{{totalParams.deviceAlertCount}}）</span>
                        <span class="mgl">承载业务数（{{totalParams.bizSysCount}}）/告警影响业务数（{{totalParams.bizSystemCount}}）</span>
                    </template>
                </template>
            </div>

            <div class="yw-el-table-wrap">
                <el-table :data="detailData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="300px">
                    <el-table-column label="影响业务系统"
                                     prop="biz_sys"></el-table-column>
                    <el-table-column label="业务联系人姓名"
                                     prop="concat"></el-table-column>
                    <el-table-column label="业务联系人手机号码"
                                     prop="phone"></el-table-column>
                    <el-table-column label="影响设备数"
                                     prop="deviceCount"></el-table-column>
                    <el-table-column label="影响上级告警数量"
                                     prop="alertCount"></el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </el-form>

        <el-dialog class="yw-dialog"
                   title="租户短信通知"
                   :visible.sync="emailDialog"
                   width="500px">
            <section class="yw-dialog-main">
                <el-form class="yw-form"
                         :model="emailData"
                         :rules="rules"
                         ref="emailData"
                         label-width="82px">
                    <el-form-item label="手机号码："
                                  prop="mobiles">
                        <el-input placeholder="请输入手机号码"
                                  :disabled="true"
                                  @input="checkLength"
                                  v-model="mobilesList"></el-input>
                    </el-form-item>
                    <el-form-item label="通知内容："
                                  prop="message">
                        <el-input type="textarea"
                                  placeholder="请输入内容"
                                  v-model="emailData.message"
                                  show-word-limit></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submitWork()">确定</el-button>
                <el-button @click="cancel()">取消</el-button>
            </section>

        </el-dialog>
    </div>
</template>
<script>
    import rbAlertKanBanServiceFactory from 'src/services/alert/rb-alert-kanban-services.factory.js'
    import rbAlertServicesFactory from 'src/services/alert/rb-alert-services.factory.js'
    export default {
        components: {
        },
        props: [
            'obj', 'type', 'alertId', 'source'
        ],
        data() {
            return {
                alert_id: '',
                sourceVal: '',
                alert_type: '',
                count: 0,
                detailData: [],
                emailDialog: false,
                mobilesList: '',
                emailData: {
                    message: '',
                    mobiles: ''
                },
                rules: {
                    mobiles: [
                        { required: true, message: '请输入手机号码', trigger: 'blur' }],
                    message: [
                        { required: true, message: '请输入通知内容', trigger: 'blur' }
                    ]
                },
                totalParams: {},
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [20, 50, 100], // 每页多少行数组
                total: 0 // 总共多少行数据
            }
        },
        mounted: function () {
            this.start()
        },
        methods: {
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.init_()
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.init_()
            },
            getRequest() {
                let req = {
                    'alert_id': this.alert_id,
                    'source': this.sourceVal,
                    'alert_type': this.alert_type,
                    'pageNo': this.currentPage,
                    'pageSize': this.pageSize
                }
                return req
            },
            getAlertDeviceInformation() {
                rbAlertServicesFactory.alertDeviceInformation({ alertId: this.alertId }).then(res => {
                    if (res) {
                        this.totalParams = res
                    }
                })
            },
            getAlertGenerateList(req) {
                rbAlertServicesFactory.alertInfluenceList(req).then((res) => {
                    if (res.result) {
                        this.total = res.count
                        this.count = res.count
                        this.detailData = res.result
                    }
                })
            },
            init_() {
                this.getAlertGenerateList(this.getRequest())
                this.getAlertDeviceInformation()
            },
            goMail() {
                this.emailDialog = true
                let userList = JSON.parse(JSON.stringify(this.detailData))
                let arr = []
                this.mobilesList = ''
                this.emailData.mobiles = ''
                const map = new Map()
                arr = userList.filter(item => {
                    return !map.has(item.phone) && map.set(item.phone, 1)
                })
                arr.forEach(items => {
                    this.mobilesList += items.concat + '(' + items.phone + ')'
                    if (!this.emailData.mobiles) {
                        this.emailData.mobiles += items.phone
                    } else {
                        this.emailData.mobiles += ',' + items.phone
                    }
                })
            },
            submitWork() {
                this.$refs.emailData.validate((valid) => {
                    if (valid) {
                        rbAlertServicesFactory.smsTenantNotify(this.emailData).then(res => {
                            if (res.status == 'true') {
                                this.$refs.emailData.resetFields()
                                this.emailDialog = false
                                this.$message.success(res.message)
                            } else {
                                this.$message.error(res.message)
                            }
                        })
                    } else {
                        this.$message.error('请填写内容')
                        return false
                    }
                })
            },
            cancel() {
                this.$refs.emailData.resetFields()
                this.emailDialog = false
            },
            checkRtelePhone(rule, value, callback) {
                if (!value) {
                    callback()
                } else {
                    const reg = /^1[3|4|5|7|8|9][0-9]\d{8}$/
                    console.log(reg.test(value))
                    if (reg.test(value)) {
                        callback()
                    } else {
                        return callback(new Error('请输入正确的手机号'))
                    }
                }
            },
            checkLength(e) {
                if (e.length > 11) {
                    this.emailData.mobiles = e.slice(0, 11)
                }
            },
            resourceExport() {
                let excelName = '影响范围列表.xls'
                let url = '/v1/alerts_his/exportAlertRelateData?alert_id=' + this.alert_id + '&alert_type=' + this.alert_type + '&source=' + this.sourceVal
                rbAlertKanBanServiceFactory.alertInfluenceDownload(url).then((res) => {
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    // window.location.href = objectUrl
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = excelName
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                })
            },
            start() {
                let indexOfNum = this.$route.path.indexOf('his')
                if (indexOfNum > -1) {
                    this.alert_type = 2
                } else {
                    this.alert_type = 1

                }
                if (this.source) {
                    this.sourceVal = this.source
                }
                if (this.alertId) {
                    this.alert_id = this.alertId
                    this.init_()
                }
            }
        },
        watch: {
            alertId: {
                handler: function () {
                    this.start()
                }
            }
        }
    }
</script>
<style scoped lang="scss">
    .mgl {
        margin-left: 30px;
    }
</style>
