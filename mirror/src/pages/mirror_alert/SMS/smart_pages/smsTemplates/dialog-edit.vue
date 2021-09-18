<template>
    <!-- 弹窗：编辑模板内容 -->
    <el-dialog width="1000px"
               class="yw-dialog"
               title="编辑模板内容"
               :visible.sync="dialogMsg.dialogVisible"
               :destroy-on-close="true"
               :close-on-click-modal="false">
        <div class="yw-dialog-main">
            <asp-smart-form ref="aspSmartForm"
                            :formJson="formJson"
                            :beforeHttp="beforeHttp"
                            :afterHttp="afterHttp"
                            v-model="model"
                            @on="onbind">
            </asp-smart-form>
        </div>
    </el-dialog>
</template>

<script>
    import formJson from '../smart_data/editTemplates.json'
    import rbAlertSmsServices from 'src/services/alert/rb-alert-sms-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [CommonOption],
        props: ['dialogMsg'],
        components: {

        },
        data() {
            return {
                // form
                formJson: formJson,
                model: {},
            }
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {

                switch (data.item.columnName) {

                    // 保存
                    case 'btn-save':
                        {
                            this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                            let params = {
                                id: this.dialogMsg.data.id,
                                content: this.model.templateContents
                            }
                            rbAlertSmsServices.editSmsTemplates(params).then(res => {
                                if (res.state === 'success') {
                                    this.$message.success('保存成功')
                                } else {
                                    this.$message.error(res)
                                }
                                this.$emit('closeDialog', { type: 'update' })
                            }).finally(() => {
                                this.closeFullScreenLoading()
                            })
                        }
                        break
                    // 取消
                    case 'btn-cancel':
                        this.$emit('closeDialog', '')
                        break
                }
            },
            /**
             * 智能表格页面所有请求前的前置操作
             * 例如：修改请求参数、修改请求方式、修改请求URL、或者请求条件不满足不给发送请求
             * @param tableItem 组件对象属性集
             * @param params 请求参数body
             * @param httpMethod.url 请求地址URL
             * @param httpMethod.type 请求方式，目前主要三种：'post+json', 'post+form', 'get'
             * @param row 当组件为表格并且是表格操作列触发的请求，此参数返回表格行数据，其它返回undefined
            */
            beforeHttp({ tableItem, params, httpMethod, row }) {
                switch (tableItem.columnName) {
                    // 保存
                    case 'btn-save':
                        Object.assign(params, {
                            pageNo: params.page,
                            pageSize: params.rows
                        })
                        break
                }
            },
            /**
             * 智能表格页面所有请求后置操作
             * 例如：请求后的数据包体需要做二次处理
             * @param tableItem 组件对象属性集
             * @param responseBody 响应数据body
            */
            afterHttp({ tableItem, responseBody }) {

            },
            // 初始化
            init() {
                this.$set(this.model, 'templateContents', this.dialogMsg.data.content)
            }

        },
        created() {
            this.init()
        }
    }
</script>

<style lang="scss" scoped>
    @import "../sms.scss";
</style>
