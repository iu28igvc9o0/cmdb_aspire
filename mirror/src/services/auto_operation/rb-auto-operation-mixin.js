export default {
    data() {
        return {
            pageLoading: false, // 全屏加载状态
            loading_text: '请稍候...',
            loading: false, // 表格加载状态
            multipleSelection: [],  // 复选框 已勾选数据
            currentPage: 1, // 当前页
            pageSize: 50, // 当前页多少条数据
            pageSizes: [10, 20, 50, 100, 200], // 改变每页条数
            total: 0, // 总共多少条数据
            formSearchAndOperateBoxHeight: '50',    // 搜索框+操作行高度
        }
    },
    computed: {
        // 动态计算列表高度
        tableHeight() {
            return `calc(100vh - 185px - ${this.formSearchAndOperateBoxHeight}px)`
        }
    },
    mounted() {
        this.initTableHeight()
        window.addEventListener('resize', this.initTableHeight)
    },
    methods: {
        initTableHeight(defaultHeight) {
            this.$nextTick(() => {
                // 条件容器
                let formDom = document.querySelector('.components-condition.yw-form')
                let formSearch = this.$refs.formSearch
                // 按钮容器
                let tableOperateWrap = document.querySelector('.table-operate-wrap.clearfix')
                this.formSearchAndOperateBoxHeight = defaultHeight
                    || ((formDom && formDom.clientHeight) || (formSearch && formSearch.$el.clientHeight) || 0) + (tableOperateWrap && tableOperateWrap.clientHeight || 0)
            })
        },
        // 复选框 已勾选数据
        handleSelectionChange(val) {
            this.multipleSelection = val
        },
        // 改变每页条数
        handleSizeChange(val) {
            this.pageSize = val
            this.currentPage = 1
            this.search()
        },
        // 跳转到第n页
        handleCurrentChange(val) {
            this.currentPage = val
            this.search()
        },
        // 统一处理错误信息
        showErrorTip(res) {
            let msg = (res.data && res.data.errors && res.data.errors[0].message) || res.message || res.statusText || res.error_tip || (res.error && res.error[0].message)
            msg && this.$message.error(msg)
        },
    }
}
