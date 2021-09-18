<template>
    <!-- 分页 -->
    <div class="yw-page-wrap clerafix">
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="currentPage"
                       :page-sizes="pageSizes"
                       :page-size="pageSize"
                       :layout=" layout ? layout : defaultLayout"
                       :pager-count=" pagerCount ? pagerCount  : defaultPagerCount"
                       :total="total">
        </el-pagination>
    </div>

</template>
<script>
    export default {
        props: ['currentPage', 'pageSizes', 'pageSize', 'total', 'layout', 'pagerCount'],
        data () {
            return {
                defaultLayout: 'total, sizes, prev, pager, next, jumper',
                defaultPagerCount: 7
            }
        },
        methods: {
            handleSizeChange (val) {
                this.$emit('handleSizeChange', val)
            },
            handleCurrentChange (val) {
                this.$emit('handleCurrentChange', val)
            }
        }

    }
</script>
<style lang="scss" scoped>
.yw-page-wrap {
  text-align: right;
}
</style>
