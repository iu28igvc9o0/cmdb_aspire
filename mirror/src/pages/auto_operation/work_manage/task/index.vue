<template>
    <div class="minheightp100 pbottom100">
        <!-- 新增、编辑作业 -->
        <work-edit></work-edit>

        <!-- 执行作业 -->
        <run-work-dialog></run-work-dialog>

    </div>
</template>

<script>
    import runWorkDialog from './run-work-dialog.vue'
    import workEdit from './work-edit.vue'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'

    export default {
        name: 'AutoOperationWorkManageEditWork',
        components: {
            runWorkDialog,
            workEdit,
        },
        data() {
            return {
                // 作业预览数据
                pipeline_id: '',
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {},
        watch: {},
        mounted() { },
        methods: {
            // 执行结果详情
            showDetail() {
                this.runWorkBoxShow = false
            },
        }
    }
</script>


<style lang="scss" scoped>
    @import "../../assets/global.scss";
</style>
