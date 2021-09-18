<!-- 缴费管理 -->
<template>
    <div class="pay-wrap">
        <div style="margin: 20px">
            <span>月份：</span>
            <el-date-picker v-model="timeParmas"
                            type="month"
                            format="yyyy-MM"
                            value-format="yyyy-MM"
                            placeholder="选择月">
            </el-date-picker>
            <el-button type="primary"
                       @click="queryBtn()">查询</el-button>
        </div>

        <el-row :gutter="12"
                class="mt10">
            <el-col :span="24">
                <el-card shadow="always"
                         class='ml20 mr20'>
                    <el-tabs v-model="activeName"
                             @tab-click="handleClick">
                        <el-tab-pane label="外部租户"
                                     name="first">
                            <el-form>
                                <el-form-item>
                                    <el-table border
                                              class="yw-el-table"
                                              style="cursor:pointer"
                                              stripe
                                              tooltip-effect="dark"
                                              height="calc(100vh - 370px)"
                                              size="samll"
                                              :data="outTableData"
                                              v-loading="loading">
                                        <el-table-column prop="orgName"
                                                         label="租户名称">
                                        </el-table-column>
                                        <el-table-column prop="needPay"
                                                         label="应缴费用">
                                        </el-table-column>
                                        <el-table-column prop="realPay"
                                                         label="实缴费用">
                                        </el-table-column>
                                        <el-table-column prop="balance"
                                                         label="当前账单余额">
                                        </el-table-column>
                                        <el-table-column prop="payTime"
                                                         label="缴费日期(年/月/日)">
                                        </el-table-column>
                                        <el-table-column label="操作">
                                            <template slot-scope="scope">
                                                <el-button type="text"
                                                           title="缴费"
                                                           icon="el-icon-edit"
                                                           @click="goEdit(scope.row)"></el-button>
                                                <el-button type="text"
                                                           title="详情"
                                                           icon="el-icon-s-order"
                                                           @click="detail(scope.row)"></el-button>
                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                        <el-tab-pane label="内部租户"
                                     name="second">
                            <el-form>
                                <el-form-item>
                                    <el-table border
                                              class="yw-el-table"
                                              style="cursor:pointer"
                                              stripe
                                              tooltip-effect="dark"
                                              height="calc(100vh - 370px)"
                                              size="samll"
                                              :data="inTableData"
                                              v-loading="inLoading">
                                        <el-table-column prop="orgName"
                                                         label="租户名称">
                                        </el-table-column>
                                        <el-table-column prop="needPay"
                                                         label="应缴费用">
                                        </el-table-column>
                                        <el-table-column prop="realPay"
                                                         label="实缴费用">
                                        </el-table-column>
                                        <el-table-column prop="balance"
                                                         label="当前账单余额">
                                        </el-table-column>

                                        <el-table-column prop="payTime"
                                                         label="缴费日期(年/月/日)">
                                        </el-table-column>
                                        <el-table-column label="操作">
                                            <template slot-scope="scope">
                                                <el-button type="text"
                                                           title="编辑"
                                                           icon="el-icon-edit"
                                                           @click="goEdit(scope.row)"></el-button>
                                                <el-button type="text"
                                                           title="详情"
                                                           icon="el-icon-s-order"
                                                           @click="detail(scope.row)"></el-button>

                                            </template>
                                        </el-table-column>
                                    </el-table>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                    </el-tabs>

                </el-card>
            </el-col>
        </el-row>

        <el-dialog :visible.sync="dialogShow"
                   class="yw-dialog"
                   title="修改缴费金额"
                   width="600px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form ref="editParmas"
                         class="yw-form">
                    <el-form-item label="租户名称"
                                  :label-width="formLabelWidth">
                        {{orgName}}
                    </el-form-item>
                    <!-- <el-form-item label="月份"
                                  :label-width="formLabelWidth">
                        <el-date-picker v-model="editParmas.chargeTime"
                                        type="month"
                                        format="yyyy-MM"
                                        value-format="yyyy-MM"
                                        :disabled="true"
                                        placeholder="选择月">
                        </el-date-picker>
                    </el-form-item> -->
                    <el-form-item label="当前余额(元)"
                                  :label-width="formLabelWidth">
                        <el-input v-model="editParmas.balance"
                                  style="width:220px;"
                                  type="text"
                                  :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="缴费金额(元)"
                                  :label-width="formLabelWidth">
                        <el-input v-model="editParmas.realPay"
                                  style="width:220px;"
                                  type="text"
                                  :maxlength="10"
                                  oninput="value=value.replace(/[^\d]/g,'')"
                                  @keyup.native="number($event)"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
            </section> <span slot="footer"
                  class="dialog-footer">
                <el-button @click="editCancel">取 消</el-button>
                <el-button type="primary"
                           @click="editBtn">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog :visible.sync="showDetail"
                   class="yw-dialog"
                   :title="orgTitle"
                   width="600px"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-table border
                          class="yw-el-table"
                          style="cursor:pointer"
                          stripe
                          show-summary
                          :summary-method="getSummaries"
                          tooltip-effect="dark"
                          size="samll"
                          :data="detailList.data"
                          v-loading="inLoading">
                    <el-table-column prop="chargeTime"
                                     label="月份">
                    </el-table-column>
                    <el-table-column prop="need_pay"
                                     label="应缴费用">
                    </el-table-column>
                    <el-table-column prop="real_pay"
                                     label="实缴费用">
                    </el-table-column>
                    <el-table-column prop="payTime"
                                     label="缴费日期(年/月/日)">
                    </el-table-column>
                </el-table>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import paybillsFactory from 'src/services/paybills/paybills.js'
    export default {
        data() {
            return {
                outTableData: [],// 外部列表数据
                inTableData: [],// 内部列表数据
                formLabelWidth: '200px',
                timeParmas: '',
                editParmas: {// 修改请求参数
                    departmentId: '',// 部门id
                    chargeTime: '',
                    realPay: ''// 金额
                },
                dialogShow: false, // 弹窗
                orgName: '',
                loading: true,
                inLoading: true,
                activeName: 'first',
                showDetail: false,
                orgTitle: '',
                detailList: []
            }
        },

        methods: {
            // 获取列表数据
            getTableData() {
                // 外部租户
                if (!this.timeParmas) {
                    this.getMonthDate()
                }
                let outParams = {
                    chargeTime: this.timeParmas,
                    type: '外部租户'
                }
                paybillsFactory.getTenantData(outParams).then(res => {
                    let _this = this
                    _this.outTableData = res
                    _this.loading = false
                })
                // 内部租户
                let inParams = {
                    chargeTime: this.timeParmas,
                    type: '内部租户'
                }
                paybillsFactory.getTenantData(inParams).then(res => {
                    let self = this
                    self.inTableData = res
                    self.inLoading = false
                })
            },
            // 取消修改
            editCancel() {
                this.editParmas.realPay = ''
                this.dialogShow = false

            },
            // 查询
            queryBtn() {
                this.getTableData()
            },
            // 弹窗
            goEdit(row) {
                this.editParmas.departmentId = row.department
                this.editParmas.chargeTime = this.timeParmas
                this.editParmas.balance = row.balance
                this.orgName = row.orgName
                this.dialogShow = true
            },
            detail(row) {
                this.orgTitle = row.orgName
                if (!this.timeParmas) {
                    this.getMonthDate()
                }
                paybillsFactory.getTenantDetail({ departmentId: row.department, chargeTime: this.timeParmas }).then(res => {
                    this.detailList = res
                })
                this.showDetail = true
            },
            // 修改
            editBtn() {
                paybillsFactory.editPaybillsService(this.editParmas).then(res => {
                    if (res.resultCode === '0') {
                        this.$message({
                            message: '修改成功',
                            type: 'success'
                        })
                        this.getTableData()
                        this.dialogShow = false
                        this.$refs.editParmas.resetFields()
                        // this.editParmas.realPay = ''
                        // this.editParmas.chargeTime = ''
                    } else {
                        this.$message.error(res.resultDes)
                    }

                })
            },
            handleClick() { },
            getSummaries(param) {
                const { columns } = param
                const sums = []
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '当前余额'
                        return
                    }
                    switch (column.property) {
                        case 'need_pay':
                            sums[index] = this.detailList.balance
                            break
                    }
                })
                return sums
            },
            getMonthDate() {
                // 首次加载为上个月数据
                var now = new Date()
                now.setMonth(now.getMonth() - 1)
                var year = now.getFullYear()
                var month = now.getMonth() + 1
                if (month >= 1 && month <= 9) {
                    month = '0' + month
                }
                this.timeParmas = year + '-' + month
                this.getTableData()
            }
        },
        created() {
            this.getMonthDate()
        }
    }

</script>
<style lang='scss' scoped>
    /deep/.yw-dialog-main {
        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
            -webkit-appearance: none !important;
        }
        input[type="number"] {
            -moz-appearance: textfield;
        }
    }
</style>