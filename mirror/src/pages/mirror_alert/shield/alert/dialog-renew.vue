<template>
    <!-- 恢复 -->
    <el-dialog class="yw-dialog"
               :visible.sync="dialogMsg.dialogVisible"
               width="446px"
               title="恢复">
        <section class="yw-dialog-main">
            <el-form class="yw-form"
                     label-width="70px">
                <!-- <el-form-item label="恢复类型">
                    <el-radio-group v-model="orderType">
                        <el-radio :label="1">告警工单</el-radio>
                    </el-radio-group>
                </el-form-item> -->
                <el-form-item label="恢复操作">直接后台恢复</el-form-item>
            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       size="small"
                       @click="submit()">确定</el-button>
            <el-button size="small"
                       @click="cancel()">取消</el-button>
        </section>
    </el-dialog>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import rbAlertService from 'src/services/alert/rb-alert-services.factory.js'
    export default {
        props: ['dialogMsg'],
        mixins: [CommonOption],
        components: {

        },
        data() {
            return {
                orderType: 1
            }
        },
        mounted() {

        },
        methods: {
            submit() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let params = {
                    // 'order_type': this.orderType,
                    'alert_ids': this.dialogMsg.data ? this.dialogMsg.data.map((item) => {
                        return item['alert_id']
                    }).join() : ''
                }
                rbAlertService.renewAlert(params).then((data) => {
                    if (data.code === '0000') {
                        this.$message.success('恢复成功！')
                        this.$emit('closeDialog', 'update')
                    } else {
                        this.$message.error(data.message)
                    }

                }).finally(() => {
                    this.closeFullScreenLoading()
                })

            },
            cancel() {
                this.$emit('closeDialog', '')
            }
        }
    }

</script>
<style lang="scss" scoped>
</style>
