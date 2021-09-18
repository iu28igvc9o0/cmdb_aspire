<template>
    <!-- 弹窗：人员选择 -->
    <el-dialog width="900px"
               class="yw-dialog"
               title="标签汇总"
               :visible.sync="dialogMsg.dialogVisible"
               :append-to-body="true"
               :destroy-on-close="true"
               :close-on-click-modal="false">
        <section class="yw-dialog-main">
            标签汇总
        </section>
    </el-dialog>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    export default {
        mixins: [CommonOption],
        props: ['dialogMsg'],
        components: {

        },
        data() {
            return {

            }
        },

        methods: {


            // 初始化
            init() {


            }

        },
        mounted() {
            this.init()
        }
    }
</script>

<style lang="scss" scoped>
</style>
