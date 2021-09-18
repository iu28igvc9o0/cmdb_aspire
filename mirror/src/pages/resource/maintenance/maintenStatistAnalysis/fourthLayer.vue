<template>
    <el-card class="box-card"
             style="min-height:120px"
             id="card4">
        <div slot="header">
            <span style="font-weight: bold">
                {{title.resourcePool}}(资源池)-{{title.maintenanceProjectType}}(维保类型)-{{title.deviceType}}(设备类型)-{{title.serviceEndTime}}(出保日期)出保项目明细
            </span>
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
                    <el-table-column label="编号"
                                     type="index"></el-table-column>
                    <el-table-column label="维保项目"
                                     prop="projectName"></el-table-column>
                    <el-table-column label="合同供应商"
                                     prop="contractProduce"></el-table-column>
                    <el-table-column label="服务供应商"
                                     prop="serviceProduce"></el-table-column>
                    <el-table-column label="设备类型"
                                     prop="deviceType"></el-table-column>
                    <el-table-column label="资源池"
                                     prop="resourcePool"></el-table-column>
                    <el-table-column label="采购方式"
                                     prop="procureType"></el-table-column>
                    <el-table-column label="服务到期时间"
                                     prop="serviceEndTime"></el-table-column>
                    <el-table-column label="服务数量"
                                     prop="serviceNumName"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column label="设备数量"
                                     prop="deviceNum"></el-table-column>
                    <el-table-column label="合同金额(万)"
                                     prop="money"
                                     :formatter="formatterMoney"></el-table-column>
                    <el-table-column label="折扣率(%)"
                                     prop="discountRate"></el-table-column>
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
            this.dealDataFormate(this.tranData)
        },
        watch: {
            'tranData'(val) {
                this.dealDataFormate(val)
            }
        },
        methods: {
            formatterMoney(row, column) {
                var obj = row[column.property]
                return parseFloat(obj).toFixed(2)
            },
            fileExport() {
                this.$emit('setExportFunc', false)
            },
            dealDataFormate(data) {
                data.forEach((item) => {
                    item.serviceEndTime = this.timestampToTime(item.serviceEndTime)
                })
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
