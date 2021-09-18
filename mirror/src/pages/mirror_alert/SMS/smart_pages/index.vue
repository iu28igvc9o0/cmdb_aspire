<template>
    <div class="preview"
         style="padding:0 0 0 15px;overflow: hidden;">
        <!-- 发短信 -->
        <!-- sendSmsModel:{{sendTemplates}} -->
        <asp-smart-form ref="sendSmsFrom"
                        :formJson="sendSmsJson"
                        :status="sendSmsStatus"
                        :beforeHttpPro="beforeHttpPro"
                        :afterHttpPro="afterHttpPro"
                        v-model="sendSmsModel"
                        @on="onbind">
            <!-- 接收手机 -->
            <section slot="sendPhones"
                     class="sendPhones-box">
                <YwEmailRecipient :tagDatas="emailRecipient.datas"
                                  @changeEmailRecipient="(data)=>{onbind({item:{columnName:'btn-change-emailRecipient'},model:{emailRecipient:data}})}"
                                  v-if="emailRecipient.show"></YwEmailRecipient>
            </section>
            <!-- 短信模板 -->
            <section slot="smsTemplates"
                     class="smsTemplates-box">
                <PageSmsTemplates @editTags="onbind({item:{columnName:'btn-edit-tags'}})"
                                  v-if="showPageSmsTemplates"
                                  @chooseTemplates="(data)=>{onbind({item:{columnName:'btn-choose-template'},model:{sendTemplates:data.content}})}"></PageSmsTemplates>
            </section>
            <!-- 短信模板 -->

            <!-- 通讯录 -->
            <section slot="smsAddressBook"
                     class="addressBook-box">
                <YwTree @clickTree="(treeBack)=>{onbind({item:{columnName:'btn-choose-addressBook'},treeBack:treeBack})}"
                        :options="treeOptions"></YwTree>
            </section>
            <!-- 通讯录 -->
        </asp-smart-form>
        <!-- 发短信 -->

        <!-- 弹窗：编辑模板分类 -->
        <DialogSmsTags v-if="dialogTags.dialogVisible"
                       @closeDialog="hideDialogTags"
                       :dialogTags="dialogTags"></DialogSmsTags>
    </div>
</template>

<script>
    import sendSmsJson from './smart_data/sendSms.json'
    import rbAlertSmsServices from 'src/services/alert/rb-alert-sms-service.factory.js'

    export default {
        name: 'SendSMS',
        components: {
            YwTree: () => import('src/components/common/yw-addressBook-tree.vue'),
            YwEmailRecipient: () => import('src/components/common/yw-email-recipient.vue'),
            DialogSmsTags: () => import('./smsTags/smsTags.vue'),
            PageSmsTemplates: () => import('./smsTemplates/smsTemplates.vue'),
        },
        data() {
            return {
                // 发送短信
                sendSmsJson: sendSmsJson,
                sendSmsStatus: 'add',
                sendSmsModel: {},
                // 收件人
                emailRecipient: {
                    show: true,
                    datas: []
                },

                // 通讯录tree
                treeOptions: {
                    dataParams: {
                        treeCode: '',
                    },
                    search: {
                        show: true,
                    },
                    style: {
                        height: 'calc(100vh - 235px)'
                    }
                },
                // 短信模板
                showPageSmsTemplates: true,

                // dialog
                dialogTags: {
                    dialogVisible: false,
                    title: '编辑模板分类',
                    status: 'editTags',
                    chooseTagList: [// 分类列表

                    ],
                    chooseTag: {},
                    addTag: {
                        name: ''
                    },
                    templates: ''// 模板内容
                },
            }
        },
        mounted() {
            this.$refs.sendSmsFrom.asp_setValue('sendTemplatesTips', '本次将以0条计费')
            // this.$refs.sendSmsFrom.asp_setValue('sendTemplatesTips', '还可输入350字，本次将以0条计费')
            this.setSendSmsModel()
        },
        methods: {
            setSendSmsModel() {
                this.$set(this.sendSmsModel, 'sendTemplates', this.$route.query.sendTemplates)
            },
            // 表单及表格 回调事件
            onbind(data) {

                switch (data.item.columnName) {
                    // 接收手机变更
                    case 'btn-change-emailRecipient':
                        this.emailRecipient.datas = data.model.emailRecipient
                        break
                    // 短信发送记录
                    case 'btn-send-record':
                        this.$router.push({
                            path: '/mirror_alert/SMS/smart_pages/smsSendRecords',
                            query: {

                            }
                        })
                        break
                    // 短信发送
                    case 'btn-send-sms':
                        {

                            if (!this.validatePhones()) {
                                return false
                            }
                            if (!this.validateTemplates()) {
                                return false
                            }
                            let params = {
                                senderUuid: '',
                                content: this.sendSmsModel.sendTemplates,
                                receivers: this.emailRecipient.datas || [],
                            }
                            rbAlertSmsServices.sendSms(params).then(res => {
                                if (res.state === 'success') {
                                    this.$message.success('短信发送成功!')
                                } else {
                                    this.$message.error('短信发送失败!')
                                }
                            })

                        }

                        break
                    // 内容保存到模板
                    case 'btn-save-templates':
                        {
                            if (!this.validateTemplates()) {
                                return false
                            }
                            this.dialogTags.title = '短信内容保存到模板分类'
                            this.dialogTags.status = 'editContentToTags'
                            this.dialogTags.templates = this.sendSmsModel.sendTemplates
                            this.showDialogTags()
                        }

                        break
                    // 编辑模板分类
                    case 'btn-edit-tags':
                        {
                            this.dialogTags.title = '编辑模板分类'
                            this.dialogTags.status = 'editTags'
                            this.dialogTags.templates = this.sendSmsModel.sendTemplates
                            this.showDialogTags()
                        }

                        break
                    // 选择模板
                    case 'btn-choose-template':
                        this.$refs.sendSmsFrom.asp_setValue('sendTemplates', data.model.sendTemplates)
                        break
                    // 选择通讯录
                    case 'btn-choose-addressBook':
                        {

                            let currentData = data.treeBack.userList
                            if (currentData && currentData.length > 0) {
                                let mobiles = this.emailRecipient.datas.map((item) => {
                                    return item.mobile
                                })
                                currentData.forEach((item) => {
                                    if (!mobiles.includes(item.mobile)) {
                                        item.showContent = `${item.name}<${item.mobile}>`
                                        this.emailRecipient.datas.push(item)
                                    }

                                })

                            }

                            this.emailRecipient.show = false
                            this.$nextTick(() => {
                                this.emailRecipient.show = true
                            })

                        }


                        break
                }
            },

            /**
             * 智能表格页面上的按钮的前置操作：包括不限于查询区域，表格顶部、表格操作列
             * 例如：对操作按钮进行处理的数据进行预处理，或者对按钮请求进行个性胡逻辑判断等
             * 注意：本函数有next()回调函数需要执行，调取next()函数才能继续后面的业务逻辑，否则操作将中止
             * @param item 组件对象属性集
             * @param rowObj 当组件为表格操作列中的按钮，此参数返回表格行数据，其它返回undefined
             * @param next 回调函数
            */
            beforeButton({ item, rowObj, next }) {

            },

            // 智能表单页面所有请求前置操作 
            beforeHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, httpObject, callback) {

            },
            // 智能表单页面所有请求后置操作
            // 处理返回后的数据格式responseBody，smartLayout 要求格式必须统一为 {data: {}, status: 200}
            afterHttpPro({ item, parent, type, index, model, row, fileData, subFormSelectData }, responseBody, callback) {

            },


            showDialogTags() {
                this.dialogTags.dialogVisible = true
            },
            hideDialogTags(data) {
                this.dialogTags.dialogVisible = false
                if (data && data.type === 'update') {
                    // 保存的操作
                    this.showPageSmsTemplates = false
                    this.$nextTick(() => {
                        this.showPageSmsTemplates = true
                    })
                }

            },

            // 接收手机校验
            validatePhones() {
                let validate = true
                if (!this.emailRecipient.datas || this.emailRecipient.datas.length < 1) {
                    this.$confirm('接收手机不能为空', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    validate = false
                } else if (this.emailRecipient.datas.some((item) => {
                    if (item.type === 'danger') {
                        return true
                    } else {
                        return false
                    }
                })) {
                    this.$confirm('手机格式不正确，请检查后再发送', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    validate = false
                } else {
                    validate = true
                }
                return validate
            },
            // 模板内容校验
            validateTemplates() {
                if (!this.sendSmsModel.sendTemplates || !this.sendSmsModel.sendTemplates.trim()) {
                    this.$confirm('短信内容不能为空', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                } else {
                    return true
                }
            }



        }
    }
</script>

<style lang="scss" scoped>
    @import "./sms.scss";
</style>
