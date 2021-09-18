<template>
    <div class="component-container yw-dashboard">
        <!--<div style="border:2px solid #f0f0f0;">-->
        <!--<el-form lass="yw-form is-required" :inline="true" width="100%">-->
        <!--<el-form-item label="报告名称" width="50%">{{reportDetail.name}}</el-form-item>-->
        <!--<el-form-item label="任务类型" width="50%">{{reportDetail.task_type}}</el-form-item>-->
        <!--<el-form-item label="扫描设备总数" width="50%">{{reportDetail.devivce_num}}</el-form-item>-->
        <!--<el-form-item label="巡检时间" width="50%">{{reportDetail.create_time}}</el-form-item>-->
        <!--</el-form>-->
        <!--</div>-->
        <table class="bordered mtop20">
            <tr>
                <td width="10%">报告名称</td>
                <td width="40%">{{reportDetail.name}}</td>
                <td width="10%">任务类型</td>
                <td width="40%">{{reportDetail.task_type}}</td>
            </tr>
            <tr>
                <td width="10%">扫描设备总数</td>
                <td width="40%">{{reportDetail.device_num}}</td>
                <td width="10%">巡检时间</td>
                <td width="40%">{{reportDetail.create_time}}</td>
            </tr>
            <tr>
                <td width="10%">结束时间</td>
                <td width="40%">{{reportDetail.finish_time}}</td>
                <td width="10%"></td>
                <td width="40%"></td>
            </tr>
        </table>
        <el-row>
            <el-select v-model="exportTable"
                           placeholder="请选择"
                           class="list-sel" style="width: 300px;">
                <el-option label="EXCEL"
                           value="EXCEL"></el-option>
                <el-option label="PDF"
                           value="PDF"></el-option>
            </el-select>
            <el-button type="primary"
                       icon="el-icon-upload"
                       @click="exp">导出</el-button>
        </el-row>
        <div class="body-container">
            <div class="body-tit">巡检异常项</div>
            <el-table class="yw-el-table"
                      :data="tableData"
                      border
                      style="width: 100%;"
                      @selection-change="handleSelectionChange">
                <el-table-column prop="idc_type"
                                 label="资源池"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="biz_system"
                                 label="业务系统"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_class"
                                 label="设备类型"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_type"
                                 label="设备子类型"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_name"
                                 label="主机名"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="ip"
                                 label="设备IP"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="biz_group"
                                 label="分类"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="item_name"
                                 label="巡检项"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="result_name"
                                 label="子项"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="item_group"
                                 label="指标分组"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="status"
                                 label="结果状态"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="expression"
                                 label="匹配规则"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="value"
                                 label="巡检值"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <!-- <el-table-column prop="result_desc"
                                 label="结果描述"
                                 align="center">
                </el-table-column> -->
            </el-table>
        </div>

        <div class="body-container">
            <div class="body-tit">巡检无结果项</div>
            <el-table class="yw-el-table"
                      :data="tableData1"
                      border
                      style="width: 100%;"
                      @selection-change="handleSelectionChange">
                <el-table-column prop="idc_type"
                                 label="资源池"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="biz_system"
                                 label="业务系统"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_class"
                                 label="设备类型"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_type"
                                 label="设备子类型"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_name"
                                 label="主机名"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="ip"
                                 label="设备IP"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="biz_group"
                                 label="分类"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="item_name"
                                 label="巡检项"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="result_name"
                                 label="子项"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="item_group"
                                 label="指标分组"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="status"
                                 label="结果状态"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="expression"
                                 label="匹配规则"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="value"
                                 label="巡检值"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <!-- <el-table-column prop="result_desc"
                                 label="结果描述"
                                 align="center">
                </el-table-column>  -->           
              </el-table>
        </div>

        <div class="body-container">
            <div class="body-tit">巡检全量信息</div>
            <el-table class="yw-el-table"
                      :data="tableData2"
                      border
                      style="width: 100%;"
                      @selection-change="handleSelectionChange">
                <el-table-column prop="idc_type"
                                 label="资源池"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="biz_system"
                                 label="业务系统"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_class"
                                 label="设备类型"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_type"
                                 label="设备子类型"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="device_name"
                                 label="主机名"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="ip"
                                 label="设备IP"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="biz_group"
                                 label="分类"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="item_name"
                                 label="巡检项"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="result_name"
                                 label="子项"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="item_group"
                                 label="指标分组"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                
                <el-table-column prop="status"
                                 label="结果状态"
                                 align="center"
                                 :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <status-list :status="scope.row.status"></status-list>
                    </template>
                </el-table-column>
                <el-table-column prop="expression"
                                 label="匹配规则"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="value"
                                 label="巡检值"
                                 align="center"
                                 :show-overflow-tooltip="true">
                </el-table-column>
                <!-- <el-table-column prop="result_desc"
                                 label="结果描述"
                                 align="center">
                </el-table-column> -->
            </el-table>
        </div>
        <!-- <form id="frmHostInfo" name="frmHostInfo" method="post"  fit="true"></form> -->
    </div>
</template>

<script>
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import { formatDate } from 'src/assets/js/utility/rb-filters.js'
    import statusList from 'src/pages/auto_operation/components/inspection-status-list.vue'
    import _ from 'lodash'
    export default {
        data() {
            return {
                obj: {},
                exportTable: '',
                // 多选框模板存放的值
                multipleSelection: [],
                tableData: [],
                tableData1: [],
                tableData2: [],
                reportDetail: {},
                itemGroupList:[]
            }
        },
        components: {
            statusList
        },
        methods: {
            // 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 数据来源
            getTableData() {
                let str = Object.assign(this.$store.state.homeStore.report_id, {})
                rbProjectDataServiceFactory.getReportDetail(str).then((res) => {
                    this.reportDetail.name = res.name
                    this.reportDetail.task_type = res.task_type == '1' ? '手动' : '自动'
                    this.reportDetail.device_num = res.device_num
                    this.reportDetail.create_time = formatDate(res.create_time)
                    this.reportDetail.finish_time = formatDate(res.finish_time)
                    // this.itemGroupList.forEach(group => {
                    //     if (group.key == res.item_group) {
                    //         item.item_group = group.value
                    //     }

                    // })
                    // this.$refs.time.innerHTML = `结束时间：${formatDate(res.finish_time)}`
                    // this.$refs.name.innerHTML = res.name
                    if (res.exception) {
                        _.map(res.exception, item => {
                            // item.ip = item.object_id
                            item.status = '异常'
                            this.itemGroupList.forEach(group => {
                                if (group.key == item.item_group) {
                                    item.item_group = group.value
                                }
                            })
                        })
                        this.tableData = res.exception
                        // this.tableData.status = '异常'
                    }
                    if (res.noResult) {
                        _.map(res.noResult, item => {
                            this.itemGroupList.forEach(group => {
                                if (group.key == item.item_group) {
                                    item.item_group = group.value
                                }
                            })
                            // item.ip = item.object_id
                            // item.status = '无结果'
                        })
                        this.tableData1 = res.noResult
                        // this.tableData1.status = '无结果'
                        
                    }
                    if (res.report_item_list) {
                        _.map(res.report_item_list, item => {
                            this.itemGroupList.forEach(group => {
                                if (group.key == item.item_group) {
                                    item.item_group = group.value
                                }
                            })
                            // item.ip = item.object_id
                            // item.status = item.status_label
                        })
                        this.tableData2 = res.report_item_list
                        // this.tableData1.status = '正常'
                    }
                })
            },
            exp() {
                if (this.exportTable === '') {
                    this.$alert('请先选择一个导出格式如EXCEL', '警告', {
                        confirmButtonText: '确定'
                    })
                } else {
                    let id = this.$store.state.homeStore.report_id
                    let arr = [this.exportTable, id]
                    rbProjectDataServiceFactory.exportResultTable(arr).then((res) => {
                        if (this.exportTable === 'EXCEL') {
                            let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                            let objectUrl = URL.createObjectURL(blob)
                            // window.location.href = objectUrl
                            let downLoadElement = document.createElement('a')
                            downLoadElement.href = objectUrl
                            downLoadElement.download = this.reportDetail.name + '.xls'
                            document.body.appendChild(downLoadElement)
                            downLoadElement.click()
                            document.body.removeChild(downLoadElement)
                            URL.revokeObjectURL(objectUrl)
                        } else {
                            let blob = new Blob([res], { type: 'application/pdf' })
                            // 创建下载链接
                            let objectUrl = URL.createObjectURL(blob)
                            // window.location.href = objectUrl
                            let downLoadElement = document.createElement('a')
                            downLoadElement.href = objectUrl
                            downLoadElement.download = this.reportDetail.name + '.pdf'
                            document.body.appendChild(downLoadElement)
                            downLoadElement.click()
                            document.body.removeChild(downLoadElement)
                            URL.revokeObjectURL(objectUrl)
                        }
                    })
                }
            }
        },
        mounted() {
            let itemGroup = this.$store.state.homeStore.dictObj.item_group
            if (itemGroup) {
              Object.keys(itemGroup).forEach(item => {
                this.itemGroupList.push({'key': item, 'value': itemGroup[item].name})
              })
            }
            this.getTableData()
        }
    }
</script>

<style lang="scss" scoped>
    .component-container {
        table {
            *border-collapse: collapse; /* IE7 and lower */
            border-spacing: 0;
            width: 100%;
            margin-bottom: 10px;
            background: #fff;
        }
        .bordered {
            border: solid $color-border 1px;
            -moz-border-radius: 2px;
            -webkit-border-radius: 2px;
            border-radius: 2px;
        }
        .bordered tr:hover {
            background: $color-bg;
            -o-transition: all 0.1s ease-in-out;
            -webkit-transition: all 0.1s ease-in-out;
            -moz-transition: all 0.1s ease-in-out;
            -ms-transition: all 0.1s ease-in-out;
            transition: all 0.1s ease-in-out;
        }
        .bordered td,
        .bordered th {
            border-left: 1px solid $color-border;
            border-top: 1px solid $color-border;
            padding: 10px;
            text-align: left;
        }
        .bordered tr:first-child td {
            border-top: 0;
        }
        header {
            background: #fff;
            height: 60px;
            border: solid $color-border 1px;
            display: flex;
            align-items: center;
            flex-wrap: wrap;
            .head {
                width: 100%;
                position: relative;
                display: flex;
                align-items: center;
                position: relative;
                div {
                    display: inline-block;
                }
                .head-right {
                    position: absolute;
                    right: 10px;
                    display: flex;
                    align-items: center;
                    .report-details-tit {
                        margin-left: 20px;
                        height: 40px;
                    }
                    .el-select {
                        width: 100px;
                        margin-left: 20px;
                        /deep/ .el-input .el-input__inner {
                            height: 40px !important;
                        }
                    }
                    .creat-time {
                        margin-left: 20px;
                        font-weight: bold;
                    }
                }
                .head-left {
                    position: absolute;
                    left: 10px;
                    div.left-name {
                        font-weight: bold;
                    }
                }
            }
        }
        .body-container {
            .body-tit {
                height: 40px;
                line-height: 40px;
                font-weight: bold;
            }
            border: solid $color-border 1px;
            margin-top: 10px;
            padding: 0 10px 5px 10px;
            background: #fff;
        }
    }
</style>
