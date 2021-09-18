<template>
    <el-card class="box-card"
             style="min-height:120px"
             id="card2">
        <div slot="header">
            <span style="font-weight: bold">
                {{title.resourcePool}}(资源池)-{{title.maintenanceProjectType}}(维保类型)维保信息统计</span>
            <div class="clearfix fr">
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="fileExport">导出</el-button>
            </div>
        </div>
        <el-form class="yw-form">
            <div class="yw-el-table-wrap">
                <el-table :data="tranData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          empty-text="无数据"
                          border>
                    <el-table-column label="资源池"
                                     prop="resourcePool"></el-table-column>
                    <el-table-column label="维保种类"
                                     prop="maintenanceProjectType"></el-table-column>
                    <el-table-column label="服务厂家数量"
                                     prop="produceNum"></el-table-column>
                    <el-table-column label="设备类型"
                                     prop="deviceType">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       @click="showThirdLayer(scope.row)">
                                {{scope.row.deviceType}}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column label="维保项目数量"
                                     prop="projectNum"></el-table-column>
                    <el-table-column label="维保设备数量"
                                     prop="deviceNum"></el-table-column>
                    <el-table-column label="总合同金额(万)"
                                     prop="totalMoney"
                                     :formatter="formatterMoney"></el-table-column>
                    <el-table-column label="最近维保到期时间"
                                     prop="serviceEndTime"
                                     :formatter="formatTime"></el-table-column>
                </el-table>
            </div>
        </el-form>
    </el-card>
</template>

<script>
    export default {
        props: ['tranData', 'title'],
        data() {
            return {

            }
        },
        mounted() {
        },
        methods: {
            formatterMoney(row, column) {
                var obj = row[column.property]
                return parseFloat(obj).toFixed(2)
            },
            fileExport() {
                this.$emit('setExportFunc', false)
            },
            // 第三层触发
            showThirdLayer(row) {
                var tmpData = {
                    maintenanceProjectType: row.maintenanceProjectType,
                    resourcePool: row.resourcePool,
                    deviceType: row.deviceType
                }
                this.$emit('setCallBack', tmpData)
            },
            formatTime(row, column) {
                return this.timestampToTime(row[column.property])
            },
            // 时间戳转时间格式
            timestampToTime(time) {
                const date = new Date(time)
                var Y = date.getFullYear() + '-'
                var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-'
                var D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate())
                return Y + M + D
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
