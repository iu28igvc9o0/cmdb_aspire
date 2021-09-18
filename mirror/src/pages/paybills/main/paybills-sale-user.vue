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
                    height="calc(100vh - 250px)"
                    width="100%"
                    border
                    :data="deptList"
                >
                    <el-table-column prop="name1" label="一级部门" align="center" width="170px">
                        <template slot-scope="scope">
                            {{ scope.row.name1 }}
                            <span>
                                <el-tooltip content="展开业务系统" effect="light" placement="right">
                                    <i
                                        class="el-icon-caret-right icon-style"
                                        v-if="scope.row.name1"
                                        @click="getSystems(scope.$index, scope.row)"
                                    ></i>
                                </el-tooltip>
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="name" align="center" label="二级部门" width="170px">
                        <template slot-scope="scope">
                            {{ scope.row.name }}
                            <span>
                                <el-tooltip content="展开业务系统" effect="light" placement="right">
                                    <i
                                        v-if="scope.row.name"
                                        class="el-icon-caret-right icon-style"
                                        @click="getSystems(scope.$index, scope.row)"
                                    ></i>
                                </el-tooltip>
                                <!-- <el-tooltip content="关闭业务系统" effect="light" placement="right">
                                    <i
                                        class="el-icon-caret-bottom icon-style"
                                        v-if="scope.row.name"
                                    ></i>
                                </el-tooltip> -->
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="businessName" align="center" label="业务系统">
                    </el-table-column>
                    <el-table-column width="200px" prop="discount" align="center" label="折扣[0~1]">
                        <template slot-scope="scope">
                            <div>
                                <el-input
                                    v-model="scope.row.discount"
                                    placeholder="输入折扣值"
                                    maxlength="5"
                                    size="mini"
                                    @input="scope.row.discount = checked(scope.row.discount)"
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
            deptList: [], // 部门树
            systemList: [], // 根据部门id获取业务系统id
            initList: [], // 获取业务系系统列表，初始化
        }
    },
    computed: {},
    mounted() {
        let that = this
        this.loading = true
        this.getDepts().then(() => {
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
        // 获取租户树
        async getDepts() {
            return this.rbHttp
                .sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/discount/loadDeptTreeData',
                    params: {},
                })
                .then((res) => {
                    this.deptList = res
                    this.deptList = this.deptList.map((item) => {
                        return { ...item, discount: '' }
                    })
                    return this.deptList
                })
        },
        // 根据Id查询业务系统
        async getSystemData(index, row) {
            return this.rbHttp
                .sendRequest({
                    method: 'GET',
                    url: '/v1/cmdb/bill/discount/loadBusinessList',
                    params: {
                        dept1: row.pId === '0' ? row.id : '',
                        dept2: row.pId !== '0' ? row.id : '',
                    },
                })
                .then((res) => {
                    this.systemList = res
                    if (!res.length) {
                        this.$message.warning('该部门下无业务系统')
                    }
                    return res
                })
        },
        // 获取业务系统
        getSystems(index, row) {
            // 如果业务系统未被展开过
            if (!this.deptList[index + 1].businessName) {
                let that = this
                this.loading = true
                this.getSystemData(index, row).then(() => {
                    that.rbHttp
                        .sendRequest({
                            method: 'GET',
                            url: '/v1/cmdb/bill/discount/list',
                            params: { type: 'dept' },
                        })
                        .then((res) => {
                            that.initList = res

                            that.systemList.forEach((item) => {
                                // 更新vue视图，改变对象属性值
                                this.$set(item, 'discount', '')
                                that.initList.forEach((item2) => {
                                    if (item2.id === item.id) {
                                        item.discount = item2.discount
                                    }
                                }) // 遍历初始化数组，将业务系统折扣初始化，只更新新获取业务系统折扣
                            })
                            that.systemList.forEach((it, index2) => {
                                // 往源数组中点击的部门后面顺序添加业务系统数组
                                that.deptList.splice(index + 1 + index2, 0, it)
                            })
                            that.loading = false
                            return res
                        })
                })

                // console.log(this.deptList)
            } else {
                this.$message.warning('业务系统已经展开')
            }
        },
        // 保存折扣
        save() {
            this.loading = true
            let that = this
            let discountList = this.deptList.map((item) => {
                return { resType: 'dept', id: item.id, discount: item.discount }
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
                    params: { type: 'dept' },
                })
                .then((res) => {
                    this.initList = res
                    this.initList.forEach((item) => {
                        this.deptList.forEach((item2) => {
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
