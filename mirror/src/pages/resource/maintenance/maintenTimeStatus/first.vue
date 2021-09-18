<template>
    <el-card class="box-card">
        <div slot="header">
            <span style="font-weight: bold">维保状态统计</span>
            <div class="clearfix fr">
                <el-button type="text" icon="el-icon-upload2" @click="fileExport">导出</el-button>
            </div>
        </div>
        <el-form class="yw-form">
            <div class="yw-el-table-wrap">
                <el-table :data="tranData" 
                          class="yw-el-table" 
                          stripe
                          show-summary
                          :summary-method="getSummaries"
                          tooltip-effect="dark"
                          empty-text="无数据"
                          border>
                    <el-table-column label="维保状态" prop="maintenStatus" min-width="302">
                        <template slot-scope="scope">
                            <el-button type="text" @click="showSecondLayer(scope.row)">
                                {{scope.row.maintenStatus}}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="维保项目数量" prop="maintenNum" min-width="50"></el-table-column>
                    <el-table-column label="维保设备数量" prop="deviceNum" min-width="50"></el-table-column>
                </el-table>
            </div>
        </el-form>
    </el-card>
</template>

<script>
    export default {
        props: ['tranData'],
        data () {
            return {
            }
        },
        mounted () {
        },
        methods: {
            fileExport(){
                this.$emit('setExportFile',false)
            },
            showSecondLayer(row){
                this.$emit('setSecondRequest',row.maintenStatus)
            },
            // 尾行合计
            getSummaries(param) {
                const { columns, data } = param
                const sums = []
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '总价'
                        return
                    }
                    const values = data.map(item => Number(item[column.property]))
                    if (!values.every(value => isNaN(value))) {
                        // 只计算在建和已建的总数
                        sums[index] = Number(values[0]) + Number(values[1])
                    }
                })
                return sums
            }
        }
    }
</script>

<style lang="scss" scoped>
</style>
