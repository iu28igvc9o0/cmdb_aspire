<template>
    <el-container class="components-container"
                  v-loading="loading">
        <el-aside class="yw-dashboard"
                  v-if="params.link.trim() !== 'external'"
                  width="260px">
            <section class="search-wrap">
                <el-input placeholder="筛选"
                          v-model="filterText">
                </el-input>
                <!-- <el-button type="text"
                           icon="el-icon-refresh"
                           @click="refresh()"></el-button> -->
            </section>
            <el-scrollbar class="aside-main"
                          style="height: calc(100% - 60px)">

                <ul class="select-section"
                    v-if="this.params.link !== 'external'">
                    <li class="select-item"
                        v-for="(item) in repeatResultFilter"
                        :class="item.name == activeName ? 'activeItem': ''"
                        @click="checkInfo(item)"
                        :key="item.name">
                        <p v-if="!filterText || item.name.indexOf(filterText.trim()) > -1 || item.statusName.indexOf(filterText.trim()) > -1">
                            {{item.name}} <span class="check-status-icon"
                                  :class="item.statusColor">{{item.statusName}}</span>
                        </p>
                    </li>
                </ul>
            </el-scrollbar>
        </el-aside>
        <el-main class="yw-dashboard">
            <!-- 表格 -->
            <ReportTable :params="tableParams"
                         @changeDate="changeDate"
                         @queryStatus="queryStatus"
                         v-if="showTable && resetComponent"></ReportTable>
            <!-- 表格 -->
        </el-main>
    </el-container>

</template>

<script>
    import department from 'src/utils/department.js'
    import CommonOption from 'src/utils/commonOption.js'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'

    export default {
        mixins: [department, CommonOption],
        props: ['params'],
        components: {
            ReportTable: () => import('./reportTable.vue'),
        },
        data() {
            return {
                showTable: false,
                // 表格入参
                tableParams: {},
                // 左侧列表数组
                repeatDeviceResult: [],
                repeatResultFilter: [],

                // 搜索内容
                filterText: '',
                // 当前选中的部门名称
                activeName: '',
                // 审核状态列表
                statusList: []
            }
        },

        created() {

        },
        mounted() {
            this.query()
        },
        methods: {
            // 切换时间
            changeDate(val) {
                this.$emit('changeDate', val)
            },
            // 查询
            query() {

                if (this.params.link.trim() === 'external') {
                    this.queryBPM()
                } else {
                    this.queryUMS()
                }

            },
            async queryBPM() {
                this.repeatDeviceResult = await this.getDepartmentList()
                // this.queryStatus({ submitMonth: this.params.date })
                this.queryTable()
            },
            async queryUMS() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                this.repeatDeviceResult = await this.getDepartmentList()
                this.queryStatus({ submitMonth: this.params.date })
                // this.queryTable()
                this.closeFullScreenLoading()
            },
            // 查询部门对应状态
            queryStatus(obj = {}) {
                this.statusList = [
                    // {
                    //     province: '北京公司',
                    //     status: '待审核',
                    // },
                    // {
                    //     province: '山东公司',
                    //     status: '审核驳回',
                    // },
                    // {
                    //     province: '江西公司',
                    //     status: '审核通过',
                    // },
                    // {
                    //     province: '河南公司',
                    //     status: '待提交',
                    // },
                ]
                if (!obj.submitMonth) { return false }
                let params = {
                    submitMonth: obj.submitMonth,
                    resourceOwner: this.params.source
                }
                return rbCmdbService.getProvinceStatus(params).then((res) => {
                    this.statusList = res
                    this.repeatResultFilter = this.filterProvince()
                    // if (this.params.type === 'query') {
                    //     this.queryTable(this.repeatResultFilter[0])
                    // } else {
                    //     this.queryTable()
                    // }
                    this.queryTable()

                    return res
                })
            },
            // 省份树过滤(排序、加状态等)
            filterProvince() {
                // 自定义状态
                let statusObj = {}
                // 省份状态对应关系
                let provinceStatus = {}
                // 排序等处理后的数据
                let solveResult = []

                statusObj = {
                    '待审核': {
                        number: 1,
                        color: 'red'
                    },
                    '待提交': {
                        number: 2,
                        color: 'gray'
                    },
                    '审核驳回': {
                        number: 3,
                        color: 'red'
                    },
                    '审核通过': {
                        number: 4,
                        color: 'green'
                    },
                }

                if (this.statusList && this.statusList.length > 0) {
                    this.statusList.forEach((item) => {
                        provinceStatus[item.province] = Object.assign({}, item, {
                            statusName: item.status,
                            statusNumber: statusObj[item.status].number,
                            statusColor: statusObj[item.status].color,

                        })
                    })
                }

                if (this.repeatDeviceResult && this.repeatDeviceResult.length > 0) {
                    solveResult = this.repeatDeviceResult.map((item) => {

                        let temp = Object.assign({}, item, {
                            name: item.orgName,
                            statusName: provinceStatus[item.orgName] && provinceStatus[item.orgName]['statusName'] || '待提交',
                            statusNumber: provinceStatus[item.orgName] && provinceStatus[item.orgName]['statusNumber'] || 2,
                            statusColor: provinceStatus[item.orgName] && provinceStatus[item.orgName]['statusColor'] || 'gray',
                        })
                        return temp
                    })
                    solveResult = _.sortBy(solveResult, 'statusNumber', (i) => { return i })
                }

                return solveResult
            },
            // 刷新
            refresh() {
                this.filterText = ''
            },
            // 左侧选择
            checkInfo(item) {
                this.queryTable(item)
            },
            // 查询表格
            queryTable(item = '') {
                if (item) {
                    this.tableParams = Object.assign({}, this.params, { province: item.name })
                } else {
                    this.tableParams = Object.assign({}, this.params)
                }
                this.activeName = this.tableParams.province
                this.showTable = true
                // 更新组件
                this.updateComponent()
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "src/assets/css/status.scss";
    .main-head {
        width: 100%;
        box-sizing: border-box;
        justify-content: center;
        display: flex;
        align-items: center;
        .head-font {
            margin-right: 20px;
        }
        .head-btn {
            margin-left: 50px;
        }
    }
    input::-webkit-outer-spin-button,
    input::-webkit-inner-spin-button {
        -webkit-appearance: none;
    }
    input[type="number"] {
        -moz-appearance: textfield;
    }
    .btn-wrap {
        text-align: center;
    }
    .components-condition {
        padding-right: 60px;
    }
    .wait {
        color: white;
        border-radius: 10px;
        padding: 2px 10px;
        background-color: #f56c6c;
    }
    .approved {
        color: white;
        border-radius: 10px;
        padding: 2px 10px;
        background-color: #67c23a;
    }
    .disabeld {
        cursor: not-allowed;
        // color: #f9f9f9;
    }
    .activeItem {
        color: #54adff;
        cursor: pointer;
    }
</style>
