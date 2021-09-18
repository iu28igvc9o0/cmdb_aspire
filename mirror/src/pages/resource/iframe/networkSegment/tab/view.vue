<template>
    <div class="head-tab">
        <el-button @click="btnCheck(1)"
                   :type="primaryShow ? primary[0]: primary[1]"
                   :plain="false">网段地址列表</el-button>
        <el-button @click="btnCheck(2)"
                   plain
                   :type="!primaryShow ? primary[0]: primary[1]">IP地址列表</el-button>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                primary: ['primary', 'default'],
                primaryShow: true
            }
        },
        methods: {
            btnCheck(data, bool) {
                if (bool) {
                    this.primaryShow = false

                } else {
                    this.primaryShow = !this.primaryShow
                    this.$emit('tabCheck', this.primaryShow)
                }

            },
            ipBtn() {
                this.primaryShow = false
            }
        }
    }
</script>
<style lang="scss" scoped>
    .head-tab {
        background: #fff;
        box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
        border-radius: 6px;
        padding: 10px;
        margin-top: 10px;
        margin-right: 10px;
    }
</style>
