<template>
    <div>

        <!-- 弹窗：编辑数据 -->
        <el-dialog width="1200px"
                   class="yw-dialog"
                   title="故障数据编辑"
                   :visible.sync="dialogMsg.dialogVisible"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <div class="yw-dialog-main">
                <asp-smart-form ref="faultEditForm"
                                :formJson="editTagsJson"
                                :beforeHttpPro="beforeHttpPro"
                                :afterHttpPro="afterHttpPro"
                                v-model="faultEditModel"
                                @on="onbind">
                </asp-smart-form>
            </div>
        </el-dialog>
        <el-dialog class="yw-dialog"
                   v-if="report.reportDialog"
                   title="上报人"
                   :visible.sync="report.reportDialog"
                   width="700px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :show-close="false">
            <section class="yw-dialog-main">
                <rb-mirror-alert-use-list ref="rbMirrorAlertUseList"></rb-mirror-alert-use-list>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           size="small"
                           @click="sendUser()">确定</el-button>
                <el-button size="small"
                           @click="report.reportDialog=false">取消</el-button>

            </section>
        </el-dialog>
        <YwImport v-if="display.isImport"
                  :showImport="display.isImport"
                  @setImportDisplay="closeParent"
                  @setImportPath="setImportPath"
                  :importType="importType"
                  :importId="importId"></YwImport>

    </div>

</template>

<script>
    import editTagsJson from './smart_data/dialogEdit.json'
    import rbMirrorAlertUseList from 'src/pages/mirror_alert/common/rb-mirror-alert-use-list.vue'
    import rbFaultServices from 'src/services/door/rb-fault-services.factory.js'

    export default {
        props: ['dialogMsg'],
        components: {
            rbMirrorAlertUseList,
            YwImport: () => import('src/components/common/yw-import.vue')

        },
        data() {
            return {
                // 编辑模板分类
                editTagsJson: editTagsJson,
                faultEditModel: editTagsJson.model,
                report: {
                    reportDialog: false
                },
                display: {
                    isImport: false
                },
                importType: 'fault',
                importId: '',
                userType: ''
            }
        },

        mounted() {
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 数据编辑
                    case 'btn-edit-save':
                        {
                            // const validate = this.$refs.faultEditForm.asp_validate()
                            this.$refs.faultEditForm.asp_validate().then(res => {
                                if (res == true) {
                                    // 将yymmddhhmmss转成毫秒
                                    if (this.faultEditModel.faultHappenTime) {
                                        this.faultEditModel.faultHappenTime = this.dateTime(this.faultEditModel.faultHappenTime)
                                    }
                                    if (this.faultEditModel.faultReportTime) {
                                        this.faultEditModel.faultReportTime = this.dateTime(this.faultEditModel.faultReportTime)
                                    }
                                    if (this.faultEditModel.faultRegtime) {
                                        this.faultEditModel.faultRegtime = this.dateTime(this.faultEditModel.faultRegtime)
                                    }
                                    if (this.faultEditModel.reportCreateTime) {
                                        this.faultEditModel.reportCreateTime = this.dateTime(this.faultEditModel.reportCreateTime)
                                    }
                                    if (this.faultEditModel.reportPlainFinish) {
                                        this.faultEditModel.reportPlainFinish = this.dateTime(this.faultEditModel.reportPlainFinish)
                                    }
                                    if (this.faultEditModel.reportFinishTime) {
                                        this.faultEditModel.reportFinishTime = this.dateTime(this.faultEditModel.reportFinishTime)
                                    }
                                    if (this.faultEditModel.reportPlatformRecoverytime) {
                                        this.faultEditModel.reportPlatformRecoverytime = this.dateTime(this.faultEditModel.reportPlatformRecoverytime)
                                    }
                                    if (this.faultEditModel.reportBizsysRecoverytime) {
                                        this.faultEditModel.reportBizsysRecoverytime = this.dateTime(this.faultEditModel.reportBizsysRecoverytime)
                                    }
                                    // 判断及时性
                                    if (this.faultEditModel.faultReportTime && this.faultEditModel.faultHappenTime) {
                                        if (this.faultEditModel.faultReportTime - this.faultEditModel.faultHappenTime < 3600000) {
                                            this.faultEditModel.faultReportTimely = 1
                                        } else {
                                            this.faultEditModel.faultReportTimely = 2
                                        }
                                    }
                                    if (this.faultEditModel.reportPlainFinish && this.faultEditModel.reportFinishTime) {
                                        if (this.faultEditModel.reportFinishTime - this.faultEditModel.reportPlainFinish < 0) {
                                            this.faultEditModel.reportTimely = 1
                                        } else {
                                            this.faultEditModel.reportTimely = 2
                                        }
                                    }

                                    rbFaultServices.editFault(this.faultEditModel).then(res => {
                                        if (!res.error) {
                                            this.$message.success('修改成功')
                                            this.$emit('editData', true)
                                            this.dialogMsg.dialogVisible = false
                                        } else {
                                            this.$message.error(res.error.message)
                                        }

                                    })

                                }
                            })

                        }
                        break
                    // 附件上传
                    case 'upload':
                        {
                            this.display.isImport = true
                        }
                        break
                    // 取消
                    case 'btn-edit-cancel':
                        {
                            this.dialogMsg.dialogVisible = false
                        }
                        break
                    case 'data-user':
                        {
                            this.userType = 'data-user'
                            this.report.reportDialog = true
                        }
                        break
                    case 'report-user':
                        {
                            this.userType = 'report-user'
                            this.report.reportDialog = true
                        }
                        break
                }
            },
            // 智能表单页面所有请求前置操作 
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {
                // console.log('beforeHttpPro===', { item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback)
                callback(httpObject)
            },
            // 智能表单页面所有请求后置操作
            // 处理返回后的数据格式responseBody，smartLayout 要求格式必须统一为 {data: {}, status: 200}
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {
            },
            closeParent(val) {
                this.display.isImport = val
            },
            setImportPath(val) {
                this.faultEditModel.reportEnclosure = val
            },
            // 上报人
            sendUser() {
                let notifyUser = this.$refs.rbMirrorAlertUseList.multipleSelection
                if (this.userType === 'data-user') {
                    this.faultEditModel.faultReportUser = notifyUser[0].name
                    this.faultEditModel.faultReportEmail = notifyUser[0].mail
                    this.faultEditModel.faultReporMobile = notifyUser[0].mobile
                } else {
                    this.faultEditModel.reportUser = notifyUser[0].name
                    this.faultEditModel.reportEmail = notifyUser[0].mail
                    this.faultEditModel.reportMobile = notifyUser[0].mobile
                }
                this.report.reportDialog = false
            },
            formatDate(now) {
                var date = new Date(now)
                var y = date.getFullYear() // 年份
                var m = date.getMonth() + 1 // 月份
                if (m < 10) {
                    m = '0' + m
                }
                var d = date.getDate() // 日
                if (d < 10) {
                    d = '0' + d
                }
                var h = date.getHours() // 小时
                if (h < 10) {
                    h = '0' + h
                }
                var min = date.getMinutes() // 分钟
                if (min < 10) {
                    min = '0' + min
                }
                var s = date.getSeconds() // 秒
                if (s < 10) {
                    s = '0' + s
                }
                return y + '-' + m + '-' + d + ' ' + h + ':' + min + ':' + s
            },
            dateTime(time) {
                time = time.replace(new RegExp('-', 'gm'), '/')
                return (new Date(time)).getTime() // 得到毫秒数

            }
        },
        watch: {
            dialogMsg: {
                immediate: true,
                deep: true,
                handler(val) {
                    console.log('xxx==', val)
                    if (val.data.id) {
                        rbFaultServices.editFaultData(val.data.id).then(res => {
                            if (res) {
                                this.faultEditModel = Object.assign(this.faultEditModel, res)
                                this.importId = this.faultEditModel.id
                                // 将毫秒装成yymmddhhmmss
                                if (res.faultHappenTime) {
                                    this.faultEditModel.faultHappenTime = this.formatDate(res.faultHappenTime)
                                }
                                if (res.faultReportTime) {
                                    this.faultEditModel.faultReportTime = this.formatDate(res.faultReportTime)
                                }
                                if (res.faultRegtime) {
                                    this.faultEditModel.faultRegtime = this.formatDate(res.faultRegtime)
                                }
                                if (res.reportCreateTime) {
                                    this.faultEditModel.reportCreateTime = this.formatDate(res.reportCreateTime)
                                }
                                if (res.reportPlainFinish) {
                                    this.faultEditModel.reportPlainFinish = this.formatDate(res.reportPlainFinish)
                                }
                                if (res.reportFinishTime) {
                                    this.faultEditModel.reportFinishTime = this.formatDate(res.reportFinishTime)
                                }
                                if (res.reportPlatformRecoverytime) {
                                    this.faultEditModel.reportPlatformRecoverytime = this.formatDate(res.reportPlatformRecoverytime)
                                }
                                if (res.reportBizsysRecoverytime) {
                                    this.faultEditModel.reportBizsysRecoverytime = this.formatDate(res.reportBizsysRecoverytime)
                                }
                                // 及时性
                                if (res.faultReportTimely) {
                                    if (res.faultReportTimely === '1') {
                                        this.faultEditModel.text_1604655161041 = '及时'
                                    } else if (res.faultReportTimely === '2') {
                                        this.faultEditModel.text_1604655161041 = '超时'
                                    } else {
                                        this.faultEditModel.text_1604655161041 = ''
                                    }
                                }
                                if (res.reportTimely !== null) {
                                    this.faultEditModel.text_1604973379005 = ''
                                    if (res.reportTimely === '1') {
                                        this.faultEditModel.text_1604973379005 = '及时'
                                    } else if (res.reportTimely === '2') {
                                        this.faultEditModel.text_1604973379005 = '超时'
                                    } else {
                                        this.faultEditModel.text_1604973379005 = ''
                                    }
                                }
                                // if (res.reportCreateTime) {
                                //     this.faultEditModel.reportPlainFinish = this.formatDate(res.reportCreateTime + 345600000)
                                // }
                                // if (res.reportPlatformRecoverytime && res.faultHappenTime) {
                                //     this.faultEditModel.reportBizsysRecoveryhours = ((res.reportPlatformRecoverytime - res.faultHappenTime) / 1000 / 60 / 60).toFixed(2)
                                // }
                            }
                        })
                    }
                }
            },
            // 事件要求完成时间+4天
            'faultEditModel.reportCreateTime': {
                immediate: true,
                deep: true,
                handler(val) {
                    let newVal = this.dateTime(val)
                    this.faultEditModel.reportPlainFinish = this.formatDate(newVal + 345600000)
                }
            },
            // 故障持续时间小时保留2位
            'faultEditModel.reportPlatformRecoverytime': {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val && this.faultEditModel.faultHappenTime) {
                        this.faultEditModel.reportPlatformRecoveryhours = ((this.dateTime(val) - this.dateTime(this.faultEditModel.faultHappenTime)) / 1000 / 60 / 60).toFixed(2)

                    } else {
                        this.faultEditModel.reportPlatformRecoveryhours = ''
                    }
                }
            },
            'faultEditModel.faultReportTime': {
                immediate: true,
                deep: true,
                handler(val) {
                    let newVal = this.dateTime(val) - this.dateTime(this.faultEditModel.faultHappenTime)
                    if (newVal > 1800000) {
                        this.faultEditModel.text_1604655161041 = '超时'
                    } else {
                        this.faultEditModel.text_1604655161041 = '及时'
                    }
                }
            },
            'faultEditModel.faultHappenTime': {
                immediate: true,
                deep: true,
                handler(val) {
                    let newVal = this.dateTime(this.faultEditModel.faultReportTime) - this.dateTime(val)
                    if (newVal > 1800000) {
                        this.faultEditModel.text_1604655161041 = '超时'
                    } else {
                        this.faultEditModel.text_1604655161041 = '及时'
                    }
                }
            },
            'faultEditModel.reportPlainFinish': {
                immediate: true,
                deep: true,
                handler(val) {
                    this.faultEditModel.text_1604973379005 = ''
                    let newVal = (this.dateTime(val) - this.dateTime(this.faultEditModel.reportFinishTime)) / 1000 / 60
                    if (this.faultEditModel.reportFinishTime !== null && this.faultEditModel.reportPlainFinish !== null) {
                        if (newVal < 0) {
                            this.faultEditModel.text_1604973379005 = '超时'
                        } else {
                            this.faultEditModel.text_1604973379005 = '及时'
                        }
                    } else {
                        this.faultEditModel.text_1604973379005 = ''
                    }
                }
            },
            'faultEditModel.reportFinishTime': {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val) {
                        this.faultEditModel.text_1604973379005 = ''
                        let newVal = this.dateTime(this.faultEditModel.reportPlainFinish) - this.dateTime(val)
                        if (this.faultEditModel.reportFinishTime !== null && this.faultEditModel.reportPlainFinish !== null) {
                            if (newVal < 0) {
                                this.faultEditModel.text_1604973379005 = '超时'
                            } else {
                                this.faultEditModel.text_1604973379005 = '及时'
                            }
                        } else {
                            this.faultEditModel.text_1604973379005 = ''
                        }
                    }
                }
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>
