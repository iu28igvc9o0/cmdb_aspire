<!--  -->
<template>
    <div class="overview">
        <div class="components-container yw-dashboard">
            <el-form class="yw-form components-condition">
                <el-divider content-position="left">机柜下电告警设置</el-divider>
                <el-form-item label="触发条件："
                              label-width="100px">
                    <el-input placeholder="请输入时间"
                              v-model="addData.timeRange"></el-input><span>（分钟）范围内，机柜下的已监控的服务器下电告警设备数占比超过</span>
                    <el-input placeholder="请输入下电设备告警占比"
                              v-model="addData.alertPercentage"></el-input><span>%，并且没有列头柜下电告警</span>
                </el-form-item>
                <el-form-item label="下电监控项："
                              label-width="100px">
                    <div class="yw-el-table-wrap">
                        <el-table class="yw-el-table"
                                  style="cursor:pointer"
                                  :data="powerItem"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  size="samll">
                            <el-table-column label="设备分类"
                                             prop="deviceClass">
                            </el-table-column>
                            <el-table-column label="告警名称"
                                             prop="keyComment">
                            </el-table-column>
                            <el-table-column label="监控告警来源"
                                             prop="source">
                            </el-table-column>
                            <el-table-column label="监控方式"
                                             prop="itemTpye">
                            </el-table-column>

                        </el-table>
                    </div>
                </el-form-item>
                <el-form-item label="告警生成信息："
                              label-width="100px">
                    <div class="yw-el-table-wrap">
                        <el-table class="yw-el-table"
                                  style="cursor:pointer"
                                  :data="cabinetAlert"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  size="samll">
                            <el-table-column label="告警字段"
                                             prop="alertCol">
                            </el-table-column>
                            <el-table-column label="对应内容"
                                             prop="content">
                            </el-table-column>
                            <el-table-column label="描述">
                                <template slot-scope="scoped">
                                    <span v-html="change(scoped.row.dsc)"></span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form-item>
                <el-divider content-position="left">列头柜下电告警设置</el-divider>
                <el-form-item label="触发条件："
                              label-width="100px">
                    <p>列头柜下有2个及以上的机柜产生下电告警</p>
                </el-form-item>
                <el-form-item label="告警生成信息："
                              label-width="100px">
                    <div class="yw-el-table-wrap">
                        <el-table class="yw-el-table"
                                  style="cursor:pointer"
                                  :data="cabinetColumnAlert"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  size="samll">
                            <el-table-column label="告警字段"
                                             prop="alertCol">
                            </el-table-column>
                            <el-table-column label="对应内容"
                                             prop="content">
                            </el-table-column>
                            <el-table-column label="描述">
                                <template slot-scope="scoped">
                                    <span v-html="change(scoped.row.dsc)"></span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form-item>
                <el-form-item style="text-align: center;
">
                    <div>
                        <el-button type="primary"
                                   @click="saveBtn">保存</el-button>
                        <el-button @click="cancelBtn">取消</el-button>
                    </div>

                </el-form-item>

            </el-form>
        </div>
    </div>
</template>

<script>
    import alertCabinet from 'src/services/alert/rb-alert-cabinet-services.factory.js'

    export default {
        data() {
            return {
                addData: {
                    timeRange: '',
                    alertPercentage: '',
                    idcType: '',
                    roomId: '',
                    configType: 1
                },
                params: {},
                powerItem: [],
                cabinetAlert: [],
                cabinetColumnAlert: []
            }
        },
        props: ['parentParams', 'childParams'],
        created() {
            this.getScheduleConfig()
            this.getConfig()
        },
        methods: {
            getConfig() {
                alertCabinet.getConfig(this.addData).then(res => {
                    this.addData.timeRange = res.timeRange
                    this.addData.alertPercentage = res.alertPercentage
                })
            },
            getScheduleConfig() {
                // 下电监控项
                alertCabinet.getScheduleConfig({ indexType: 'powerItem' }).then(res => {
                    this.powerItem = res
                })
                // 机柜告警生成信息
                alertCabinet.getScheduleConfig({ indexType: 'cabinetAlert' }).then(res => {
                    this.cabinetAlert = res
                })
                // 列头柜告警生成信息
                alertCabinet.getScheduleConfig({ indexType: 'cabinetColumnAlert' }).then(res => {
                    this.cabinetColumnAlert = res
                })
            },
            saveBtn() {
                let arrData = []
                arrData.push(this.addData)
                alertCabinet.manageConfig(arrData).then(res => {
                    if (res === 'success') {
                        this.$router.go(-1)
                    }
                })
            },
            cancelBtn() {
                this.$router.go(-1)
            },
            change(content) {
                // content = content.replace(/||/g, '</br>{')
                content = content.replace(/&/g, '</br>')
                return content
            }
        },
        watch: {
            parentParams(val) {
                Object.assign(this.$data.addData, this.$options.data().addData)
                this.getConfig()
                this.addData.idcType = val.name
                this.addData.configType = 2
            },
            childParams(val) {
                this.addData.roomId = val.name
                this.addData.configType = 3
            }

        }
    }

</script>
<style lang='scss' scoped>
</style>