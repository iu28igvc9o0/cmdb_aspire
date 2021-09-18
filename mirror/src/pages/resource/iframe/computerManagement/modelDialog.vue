<template>
    <!-- dialog -->
    <el-dialog class="yw-dialog"
               :fullscreen="isFullScreen"
               :visible.sync="modelDialog"
               :width="dialogAttribute.dialogWidth"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               :title="dialogAttribute.dialogTitle"
               @close="cancelDialog">
        <section class="yw-dialog-main">
            <slot name="content"></slot>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="sureDialog"
                       v-if="dialogAttribute.dialogSure">{{dialogAttribute.dialogSure}}</el-button>
            <el-button v-if="dialogAttribute.dialogCancel"
                       @click="cancelDialog">{{dialogAttribute.dialogCancel}}</el-button>
        </section>
    </el-dialog>
    <!-- dialog -->
</template>
<script>
    export default {
        name: 'DialogModel',
        props: {
            dialogAttribute: {
                type: Object,
                default: function () {
                    return {}
                }
            }
        },
        data() {
            return {
                modelDialog: true,
                isFullScreen: this.dialogAttribute.isFullScreen ? true : false
            }
        },
        methods: {
            sureDialog() {
                this.$emit('handleSureDialog', false)
            },
            cancelDialog() {
                this.$emit('handleCancelDialog', false)
                this.modelDialog = true
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>