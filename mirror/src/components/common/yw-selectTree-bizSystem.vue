<template>
    <!-- 下拉框：业务视图单选树 -->
    <div class="yw-selectTree">
        <el-select v-model="filterText"
                   style="width:178px"
                   @blur="blur"
                   :filter-method="filterSelect"
                   clearable
                   filterable
                   placeholder="业务系统"
                   collapse-tags
                   @clear="clear()">
            <el-option :value="mineStatusValue"
                       style="height: auto">
                <el-tree :filter-node-method="filterNode"
                         :data="treeData"
                         node-key="id"
                         ref="tree"
                         highlight-current
                         :props="defaultProps"
                         @node-click="handleNodeClick"></el-tree>
            </el-option>
        </el-select>
    </div>
</template>

<script>

    export default {
        // props: ['datas', 'options'],
        components: {

        },
        data() {
            return {
                filterText: '',
                mineStatusValue: null,
                // 树数据
                treeData: [],
                // 选中的业务系统id
                selectId: '',
                // 业务系统树形下拉框
                defaultProps: {
                    children: 'subList',
                    label: 'name',
                    id: 'uuid'
                },
            }
        },
        computed: {

        },
        methods: {
            blur() {
                this.$refs.tree.filter('')
            },
            filterSelect(value) {
                this.$refs.tree.filter(value)
            },
            clear() {
                this.$emit('changeTree', '')
            },
            filterNode(value, data) {
                if (!value) return true
                return data.name.indexOf(value) !== -1
            },
            handleNodeClick(item) {
                this.filterText = item.name
                if (item.type === 'bizSystem') {
                    this.selectId = item.uuid
                    this.filterText = item.name
                    this.$emit('changeTree', this.selectId)
                }
            },
            query() {
                this.$api.queryBizSys().then((res) => {
                    this.treeData = res
                })
            }
        },
        mounted() {
            this.query()
        }

    }
</script>
<style lang="scss" scoped>
    .yw-selectTree {
        display: inline-block;
    }
</style>
