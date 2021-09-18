<!-- 租户计费冲销 -->
<template>
    <div>
        <el-row :gutter="12"
                class="mt10">
            <el-col :span="24">
                <el-card shadow="always"
                         class='ml20 mr20'>
                    <el-form>
                        <el-form-item label="日期">
                            <el-date-picker v-model="dataParmas.chargeTime"
                                            type="month"
                                            value-format="yyyy-MM"
                                            :change="getTableData">
                            </el-date-picker>
                            <el-button @click="getTableData"
                                       type="primary">查询</el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-table class="yw-el-table"
                                      style="cursor:pointer"
                                      stripe
                                      border
                                      tooltip-effect="dark"
                                      height="calc(100vh - 240px)"
                                      size="samll"
                                      :data="tableData"
                                      v-loading="loading">
                                <!-- <el-table-column prop="bizSystem"
                                                 show-overflow-tooltip
                                                 width="200px"
                                                 label="业务系统名称">
                                </el-table-column>
                                <el-table-column prop="businessConcat"
                                                 label="联系人">
                                </el-table-column>
                                <el-table-column prop="businessConcatPhone"
                                                 width="110px"
                                                 label="联系电话">
                                </el-table-column> -->
                                <el-table-column prop="orgName"
                                                 label="所属租户">
                                </el-table-column>
                                <!-- <el-table-column prop="idcName"
                                                 width="150px"
                                                 label="所属资源池">
                                </el-table-column>
                                <el-table-column prop="podName"
                                                 label="POD名称">
                                </el-table-column> -->
                                <el-table-column prop="needPay"
                                                 label="本月合计(元)">
                                </el-table-column>
                                <el-table-column prop="description"
                                                 label="冲销说明">
                                </el-table-column>
                                <!-- <el-table-column prop="fxxfwq_allocation_amount"
                                                 width="120px"
                                                 label="分析型服务器单价">
                                </el-table-column>
                                <el-table-column prop="fbsfwq_allocation_amount"
                                                 width="120px"
                                                 label="分布式服务器单价">
                                </el-table-column>
                                <el-table-column prop="hcxfwq_allocation_amount"
                                                 width="120px"
                                                 label="缓存型服务器单价">
                                </el-table-column>
                                <el-table-column prop="gdyyfwq_allocation_amount"
                                                 width="130px"
                                                 label="高端应用服务器单价">
                                </el-table-column>
                                <el-table-column prop="djdfwq_allocation_amount"
                                                 width="120px"
                                                 label="多节点服务器单价">
                                </el-table-column>
                                <el-table-column prop="yzj_vcpu_allocation_amount"
                                                 width="140px"
                                                 label="每VCPU单月总价(核)">
                                </el-table-column>
                                <el-table-column prop="yzj_memory_allocation_amount"
                                                 width="140px"
                                                 label="每G内存单月总价(T)">
                                </el-table-column>
                                <el-table-column prop="fcsan_allocation_amount"
                                                 width="120px"
                                                 label="FCSAN单价(T)">
                                </el-table-column>
                                <el-table-column prop="ipsan_allocation_amount"
                                                 width="110px"
                                                 label="IPSAN单价(T)">
                                </el-table-column>

                                <el-table-column prop="kcc_allocation_amount"
                                                 width="110px"
                                                 label="块存储单价(T)">
                                </el-table-column>
                                <el-table-column prop="wjcc_allocation_amount"
                                                 width="110px"
                                                 label="文件存储单价(T)">
                                </el-table-column>
                                <el-table-column prop="dxcc_allocation_amount"
                                                 width="110px"
                                                 label="对象存储单价(T)">
                                </el-table-column>
                                <el-table-column prop="bfcc_allocation_amount"
                                                 width="110px"
                                                 label="备份存储单价(T)">
                                </el-table-column> -->
                                <el-table-column label="计费核销">
                                    <template slot-scope="scope">
                                        <el-button type="text"
                                                   title="编辑"
                                                   icon="el-icon-edit"
                                                   @click="goEdit(scope.row, scope.$index)"></el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
        </el-row>
        <div class="yw-page-wrap clerafix">
            <!-- <el-pagination @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :page-size="dataParmas.pageSize"
                           :page-sizes="[10, 20, 50]"
                           :current-page.sync="dataParmas.pageNo"
                           layout="total, sizes, prev, pager, next, jumper"
                           :total="totalVal">
            </el-pagination> -->
        </div>

        <!-- 修改弹窗 -->
        <el-dialog class="yw-dialog"
                   title="修改价格"
                   :visible.sync="showDialog"
                   width="1000px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form">
                    <!-- <el-form-item label="业务系统名称"
                                  :label-width="formLabelWidth">
                        {{dialogData.bizSystem}}
                    </el-form-item>
                    <el-form-item label="联系人"
                                  :label-width="formLabelWidth">
                        {{dialogData.businessConcat}}
                    </el-form-item>
                    <el-form-item label="联系电话"
                                  :label-width="formLabelWidth">
                        {{dialogData.businessConcatPhone}}
                    </el-form-item> -->
                    <el-form-item label="所属租户"
                                  :label-width="formLabelWidth">
                        {{dialogData.orgName}}
                    </el-form-item>
                    <!-- <el-form-item label="所属资源池名称"
                                  :label-width="formLabelWidth">
                        {{dialogData.idcName}}
                    </el-form-item>
                    <el-form-item label="POD名称"
                                  :label-width="formLabelWidth">
                        {{dialogData.podName}}
                    </el-form-item> -->
                    <el-form-item label="本月合计(元)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="editParmas.needPay"></el-input>
                    </el-form-item>
                    <el-form-item label="冲销说明"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="textarea"
                                  v-model="editParmas.description"></el-input>
                    </el-form-item>

                    <!-- <el-form-item label="分析型服务器单价"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.fxxfwq_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="分布式服务器单价"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.fbsfwq_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="缓存型服务器单价"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.hcxfwq_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="高端应用服务器单价"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.gdyyfwq_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="多节点服务器单价"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.djdfwq_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="每VCPU单月总价(核)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.yzj_vcpu_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="每G内存单月总价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.yzj_memory_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="FCSAN单价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.fcsan_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="IPSAN单价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.ipsan_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="块存储单价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.kcc_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="文件存储单价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.wjcc_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="对象存储单价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.dxcc_allocation_amount"></el-input>
                    </el-form-item>
                    <el-form-item label="备份存储单价(T)"
                                  :label-width="formLabelWidth">
                        <el-input autocomplete="off"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  v-model="dialogData.bfcc_allocation_amount"></el-input>
                    </el-form-item> -->
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button size="small"
                           @click="showDialog = false">取消</el-button>
                <el-button type="primary"
                           size="small"
                           @click="editBtn">确定</el-button>
            </section>

        </el-dialog>
    </div>
</template>

<script>
    import paybillsFactory from 'src/services/paybills/paybills.js'
    export default {
        data() {
            return {
                showDialog: false,// 弹窗是否显示
                tableData: [],  // 请求列表
                totalVal: 0,// 列表总条数
                formLabelWidth: '200px',
                dataParmas: {  // 请求列表传参
                    chargeTime: '', // 选择日期
                    // pageNo: 1,
                    // pageSize: 20
                },
                editParmas: {// 修改传参
                    needPay: '',
                    departmentId: '',
                    description: '',
                    orgName: ''
                    // department: '', // 部门id
                    // businessSystem: '',// 业务系统id 
                    // idc: '',// 资源池id
                    // pod: '',// pod池id
                    // deviceTypes: {
                    // }
                },
                dialogData: [],
                loading: true
            }
        },

        methods: {
            // 获取列表数据
            getTableData() {
                paybillsFactory.getPaybillsData(this.dataParmas).then(res => {
                    let _this = this
                    // _this.totalVal = res.count
                    _this.tableData = res
                    _this.loading = false

                })
            },
            // 修改弹窗
            goEdit(row) {
                // 弹窗各项绑定值
                let self = this
                self.dialogData = row
                self.editParmas.needPay = row.needPay
                self.editParmas.description = row.description
                self.editParmas.departmentId = row.departmentId
                self.editParmas.orgName = row.orgName
                self.editParmas.chargeTime = self.dataParmas.chargeTime
                // self.editParmas.businessSystem = row.businessSystemId
                // self.editParmas.idc = row.idcId
                // self.editParmas.pod = row.podId
                self.showDialog = true
            },
            // 保存修改
            editBtn() {
                // id过滤加入对象
                // const arr = Object.keys(this.dialogData).filter(i => i.indexOf('_id') === -1)
                // arr.forEach(i => {
                //     if (this.dialogData[`${i}_id`]) {
                //         this.editParmas.deviceTypes[this.dialogData[`${i}_id`]] = this.dialogData[i]
                //     }
                // })
                // 修改请求
                paybillsFactory.updatePaybillsService(this.editParmas).then(res => {
                    if (res.flag == true) {
                        this.$message({
                            message: res.message,
                            type: 'success'
                        })
                        this.getTableData()
                        this.showDialog = false
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            handleSizeChange(val) {
                this.dataParmas.pageSize = val
                this.getTableData()
            },
            handleCurrentChange(val) {
                this.dataParmas.pageNo = val
                this.getTableData()
            },
        },
        created() {
            var now = new Date()
            now.setMonth(now.getMonth() - 1)
            var year = now.getFullYear()
            var month = now.getMonth() + 1
            if (month >= 1 && month <= 9) {
                month = '0' + month
            }
            this.dataParmas.chargeTime = year + '-' + month
            this.getTableData()
        }
    }

</script>
<style lang='scss' scoped>
    .yw-dialog {
        /deep/.yw-dialog-main {
            input::-webkit-outer-spin-button,
            input::-webkit-inner-spin-button {
                -webkit-appearance: none !important;
            }
            input[type="number"] {
                -moz-appearance: textfield;
            }
        }

        .btn-wrap {
            left: 40%;
        }
    }
</style>