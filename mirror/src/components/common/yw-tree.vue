<template>
    <!-- 树形结构 -->
    <div class="yw-tree">
        <el-tree :data="datas"
                 :default-expand-all="activeOptions.expandAll"
                 :highlight-current="true"
                 :node-key="activeOptions.key"
                 :current-node-key="datas[0][activeOptions.key]"
                 :props="activeOptions"
                 :check-on-click-node="true"
                 @node-click="handleNodeClick"></el-tree>
    </div>
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
