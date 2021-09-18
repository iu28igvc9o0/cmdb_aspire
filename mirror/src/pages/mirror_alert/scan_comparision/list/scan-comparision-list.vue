<template>
    <el-tabs class="yw-tabs"
             style="margin-top:10px"
             v-model="activeName"
             @tab-click="handleClick"
             type="card">
        <el-tab-pane label="告警扫描对账"
                     name="first"
                     type="card">
            <rb-Alert-San-Comparision v-if="tableList.first.flag"
                                      ref="rbAlertSanComparision">
            </rb-Alert-San-Comparision>
        </el-tab-pane>
        <el-tab-pane label="监控扫描对账"
                     name="second">
            <rb-Monitor-San-Comparision v-if="tableList.second.flag"
                                        ref="rbMonitorSanComparision">
            </rb-Monitor-San-Comparision>
        </el-tab-pane>
    </el-tabs>
</template>
<script>
    import rbAlertSanComparision from 'src/pages/mirror_alert/scan_comparision/list/alert-scan-comparision-list.vue'
    import rbMonitorSanComparision from 'src/pages/mirror_alert/scan_comparision/monitor/monitor-scan-comparision-list.vue'
    export default {
        components: {
            rbAlertSanComparision,
            rbMonitorSanComparision
        },
        data() {
            return {
                activeName: 'first',
                tableList: {
                    first: {
                        flag: true,
                    },
                    second: {
                        flag: false,
                    },
                }
            }
        },
        methods: {
            handleClick(tab) {
                this.tableList[tab.name].flag = true
                Object.keys(this.tableList).forEach((item) => {
                    if (item !== tab.name) {
                        this.tableList[item].flag = false
                    }
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
</style>
