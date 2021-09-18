<template>
    <!-- 弹窗：编辑后续计划 -->
    <el-dialog width="1000px"
               class="yw-dialog"
               title="计划完成"
               :visible.sync="dialogMsg.dialogVisible"
               :destroy-on-close="true"
               :close-on-click-modal="false">
        <div class="yw-dialog-main">
            <asp-smart-form ref="faultEditForm"
                            :formJson="planTagsJson"
                            :beforeHttpPro="beforeHttpPro"
                            :afterHttpPro="afterHttpPro"
                            v-model="faultPlanModel"
                            @on="onbind">
            </asp-smart-form>
        </div>
    </el-dialog>
</template>

<script>
    import planTagsJson from './smart_data/dialogPlan.json'
    import rbFaultServices from 'src/services/door/rb-fault-services.factory.js'
    export default {
        props: ['dialogMsg'],
        components: {

        },
        data() {
            return {
                // 编辑模板分类
                planTagsJson: planTagsJson,
                faultPlanModel: {},
            }
        },

        mounted() {
        },
        methods: {
            // 表单及表格 回调事件
            onbind(data) {
                switch (data.item.columnName) {
                    // 数据编辑
                    case 'btn-plan-save':
                        {
                            rbFaultServices.editFault(this.faultPlanModel).then(res => {
                                this.$message.success('修改成功')
                                this.$emit('editData', true)
                                this.dialogMsg.dialogVisible = false
                            })
                        }
                        break

                    // 取消
                    case 'btn-plan-cancel':
                        {
                            this.dialogMsg.dialogVisible = false
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
            }

        },
        watch: {
            dialogMsg: {
                immediate: true,
                deep: true,
                handler(val) {
                    if (val.data.id) {
                        rbFaultServices.editFaultData(val.data.id).then(res => {
                            if (res) {
                                this.faultPlanModel = res
                            }
                        })
                    }
                }
            }
        }

    }
</script>

<style lang="scss" scoped>
</style>
