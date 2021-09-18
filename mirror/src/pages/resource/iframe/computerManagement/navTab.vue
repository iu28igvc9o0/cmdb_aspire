<template>
    <div class="head-tab">
        <el-button @click="btnCheck(tabsType.index1)"
                   :type="primaryShow ? primary[0]: primary[1]"
                   plain>{{tabsType.name1}}</el-button>
        <el-button @click="btnCheck(tabsType.index2)"
                   plain
                   :type="!primaryShow ? primary[0]: primary[1]">{{tabsType.name2}}</el-button>
    </div>
</template>
<script>
    export default {
        name: 'NavTab',
        props: {
            tabsType: {
                type: Object,
                default: function () {
                    return {}
                }
            }
        },
        data() {
            return {
                primary: ['primary', 'default'],
                primaryShow: true
            }
        },
        methods: {
            btnCheck(bool) {
                this.primaryShow = !this.primaryShow
                this.$emit('changeTabs', bool)
            },
        }
    }
</script>
<style lang="scss" scoped>
    .head-tab {
        background: #fff;
        box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
        border-radius: 6px;
        padding: 10px;
        margin: 10px;
    }
</style>