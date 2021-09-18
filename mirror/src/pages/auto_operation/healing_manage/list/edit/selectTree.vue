<template>
    <div>
        <el-tag v-if="values && values.group_name"
                :key="values.group_id"
                style="margin-right: 5px;"
                :disable-transitions="true"
                size="small">{{values.group_name}}</el-tag>
        <el-popover placement="bottom-start"
                    trigger="click">
            <ctree :data="codeList"
                   :props="gruopTreeDefault"
                   :ex-control="true"
                   :expandOnClickNode="false"
                   ex-show-search
                   @node-click="handleNodeClick"></ctree>
            <el-button slot="reference"
                       class="mod-btn"
                       size="small">请选择</el-button>
        </el-popover>
    </div>
</template>

<script>
    import rbAutoHealingServicesFactory from 'src/services/auto_operation/rb-auto-healing-services.factory.js'
    import ctree from '../../../components/tree'
    export default {
        components: {
            ctree
        },
        props: ['values'],
        data() {
            return {
                codeList: [],
                gruopTreeDefault: {
                    label: 'group_name',
                    children: 'sub_group_list'
                }
            }
        },
        methods: {
            queryTree() {
                let obj = {
                    opsGroup: null
                }
                rbAutoHealingServicesFactory.queryTree(obj).then((res) => {
                    this.codeList = res
                })
            },
            handleNodeClick(data) {
                this.values = []
                this.$nextTick(() => {
                    this.values = data
                    this.$emit('value-change', this.values)
                })
            }
        },
        mounted() {
            this.queryTree()
        }
    }
</script>
<style lang="sass" scoped>

</style>
