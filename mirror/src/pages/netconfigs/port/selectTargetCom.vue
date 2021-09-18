<!--  -->
<template>
    <div>
        <el-dialog width="640px"
                   class="yw-dialog"
                   title="选择端口指标"
                   :visible.sync="selectBox.dialogVisible"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-transfer filterable
                             filter-placeholder="请输入指标名称"
                             :titles="['指标列表', '已选指标']"
                             v-model="selectBox.date"
                             :data="selectList">
                </el-transfer>

            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="submit()">确认</el-button>
                <el-button @click="cancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        props: ['selectBox'],
        data() {
            return {
                selectList: [],
                selectResult: []
            }
        },

        methods: {
            submit() { },
            cancel() {
                this.$emit('closeDialog')
            },
        }
    }

</script>
<style lang='scss' scoped>
</style>