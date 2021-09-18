<template>
    <div class="components-container evalute01">
        <el-form class="yw-form">
            <div style="text-align:right;padding-bottom:10px">
                <el-button type="primary" size="mini" @click="save">保存</el-button>
            </div>

            <div class="yw-el-table-wrap">
                <el-table
                    v-loading="loading"
                    class="yw-el-table evaluate-table"
                    height="calc(100vh - 230px)"
                    width="100%"
                    border
                    :data="poolList"
                >
                    <el-table-column prop="name" align="center" label="资源池">
                        <template slot-scope="scope">
                            {{ scope.row.name }}
                        </template>
                    </el-table-column>
                    <el-table-column width="200px" prop="discount" align="center" label="折扣[0~1]">
                        <template slot-scope="scope">
                            <div>
                                <el-input
                                    maxlength="5"
                                    @input="scope.row.discount = checked(scope.row.discount)"
                                    v-model="scope.row.discount"
                                    placeholder="输入折扣值"
                                    size="mini"
                                ></el-input>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-form>
    </div>
</template>

<script>
// import QueryObject from 'src/utils/queryObject.js'
export default {
    // mixins: [QueryObject],
    components: {
        // YwPagination: () => import('src/components/common/yw-pagination.vue'),
    },
    data() {
        return {
            loading: false,
            poolList: [],
        }
    },
    computed: {},
    mounted() {
        let that = this
        this.loading = true
        this.getPoolList().then(() => {
            that.init().then(() => {
                that.loading = false
            })
        })
    },
    methods: {
        // 限制输入折扣0-1
        checked(input) {
            var reg = /^(0.\d+|0|1)$/
            if (reg.test(input)) {
                return input
            } else {
                if (input != '0.') {
                    return ''
                } else return input
            }
        },
        // 获取资源池折扣列表
        async getPoolList() {
            return this.rbHttp
                .sendRequest({
                    method: 'GET',
                    url: ' /v1/cmdb/bill/discount/loadPoolTreeData',
                    params: {},
                })
                .then((res) => {
                    this.poolList = res
                    this.poolList = this.poolList.map((item) => {
                        return { ...item, discount: '' }
                    })
                    return this.poolList
                })
        },
        // 保存折扣
        save() {
            this.loading = true
            let that = this
            let discountList = this.poolList.map((item) => {
                return { resType: 'pool', id: item.id, discount: item.discount }
            })
            this.rbHttp
                .sendRequest({
                    method: 'post',
                    data: { discountList: discountList },
                    url: '/v1/cmdb/bill/discount',
                })
                .then((res) => {
                    that.loading = false
                    if (res > 0) {
                        this.$message.success('保存成功')
                    }
                })
        },
        // 初始化折扣
        async init() {
            return this.rbHttp
                .sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/discount/list',
                    params: { type: 'pool' },
                })
                .then((res) => {
                    this.initList = res
                    this.initList.forEach((item) => {
                        this.poolList.forEach((item2) => {
                            if (item.id === item2.id) {
                                item2.discount = item.discount
                            }
                        })
                    })

                    return res
                })
        },
    },
}
</script>

<style lang="scss" scoped>
.icon-style {
    cursor: pointer;
    color: #24a7e3;
}
</style>
