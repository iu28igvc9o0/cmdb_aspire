export default {
    // 分页事件
    data() {
        return {
            // 分页
            currentPage: 1, // 当前页
            pageSize: 50, // 分页每页多少行数据
            pageSizes: [10, 20, 50, 100], // 每页多少行数组
            total: 0, // 总共多少行数据

            // 查询条件
            queryParams: {

            },

        }
    },
    methods: {
        /** 查询
         * keepParams:是否要保持查询条件
         */
        query() {

        },

        // 第一页
        initPageChange() {
            this.currentPage = 1
            return this.currentPage
        },

        // 每页展示条数
        handleSizeChange(val) {
            this.currentPage = 1
            this.pageSize = val
            // console.log(`每页数据条数： ${this.currentPage} `);
            this.query(true)
        },
        // 当前页
        handleCurrentChange(val) {
            this.currentPage = val
            // console.log(`当前页: ${this.pageSize}`);
            this.query(true)
        },

    },


}
