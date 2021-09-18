<template>
    <!-- 多级菜单 -->
    <el-submenu index="2-4">
        <template slot="title">选项4</template>
        <el-menu-item index="2-4-1">选项1</el-menu-item>
        <el-menu-item index="2-4-2">选项2</el-menu-item>
        <el-menu-item index="2-4-3">选项3</el-menu-item>
    </el-submenu>
</template>

<script>

    export default {
        props: ['datas', 'options'],
        components: {

        },
        data () {
            return {
                // 配置项
                defaultOptions: {
                    children: 'children',// 子节点字段配置
                    label: 'label',// 显示字段配置
                    key: 'id',// 唯一性字段配置
                    expandAll: false,// 是否展开全部
                    // disabled: false,
                    // isLeaf:false
                }
            }
        },
        computed: {
            // 配置项
            activeOptions () {
                let temp = this.defaultOptions
                temp.label = this.options && this.options.label ? this.options.label : temp.label
                temp.children = this.options && this.options.children ? this.options.children : temp.children
                temp.key = this.options && this.options.key ? this.options.key : temp.key
                temp.expandAll = this.options && this.options.expandAll ? this.options.expandAll : temp.expandAll
                return temp
            }
        },
        methods: {
            // 节点点击
            handleNodeClick (data) {
                this.$emit('clickTree', data)
            }
        },

    }
</script>
<style lang="scss" scoped>
</style>
