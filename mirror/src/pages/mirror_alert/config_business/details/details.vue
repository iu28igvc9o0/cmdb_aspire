<template>
    <div class="components-container yw-dashboard">
        <el-collapse class="yw-dashboard-section"
                     v-model="activeCollapseNames">
            <el-collapse-item name="1">
                <template slot="title">
                    业务告警规则
                </template>
                <el-form class="yw-form components-condition"
                         label-width="75px"
                         :model="formData"
                         :inline="true">
                    <el-form-item label="规则名称">
                        <el-input v-model="formData.name"
                                  placeholder=""
                                  disabled></el-input>
                    </el-form-item>
                    <br />
                    <el-form-item label="规则描述">
                        <el-input type="textarea"
                                  :rows="2"
                                  placeholder=""
                                  v-model="formData.description"
                                  disabled></el-input>
                    </el-form-item>
                    <br />
                    <el-form-item label="维护用户">
                        <el-checkbox label="当前用户"
                                     v-model="currentUserFlag"
                                     disabled></el-checkbox>
                        <el-checkbox label="其他用户"
                                     v-model="otherUserFlag"
                                     disabled></el-checkbox>
                        <el-input v-model="formData.operateUser"
                                  placeholder=""
                                  :disabled="true"></el-input>
                    </el-form-item>
                </el-form>
            </el-collapse-item>
            <el-collapse-item name="2">
                <template slot="title">
                    业务告警条件
                </template>
                <YwFilterOrder :dataList="optionData"
                               :detailFlag="detailFlag"
                               v-if="showFilterFlag"></YwFilterOrder>
            </el-collapse-item>
            <el-collapse-item name="3">
                <template slot="title">
                    操作记录
                </template>
                <el-form class="yw-form">
                    <div class="yw-el-table-wrap">
                        <el-table :data="result"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  height="300px">
                            <el-table-column prop="receiver"
                                             label="操作人">
                                <template slot-scope="scope">
                                    {{scope.row.operater}}
                                </template>
                            </el-table-column>
                            <el-table-column prop="mail_server"
                                             label="操作类型">
                                <template slot-scope="scope">
                                    {{scope.row.operateTypeDesc}}
                                </template>
                            </el-table-column>
                            <el-table-column prop="receive_port"
                                             label="操作时间">
                                <template slot-scope="scope">
                                    {{scope.row.operateTime | formatDate}}
                                </template>
                            </el-table-column>
                            <el-table-column prop="active"
                                             label="操作内容">
                                <template slot-scope="scope">
                                    {{scope.row.operateContent}}
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <YwPagination @handleSizeChange="handleSizeChange"
                                  @handleCurrentChange="handleCurrentChange"
                                  :current-page="currentPage"
                                  :page-sizes="pageSizes"
                                  :page-size="pageSize"
                                  :total="total"></YwPagination>
                </el-form>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertConfigExceptionServiceFactory from 'src/services/alert/rb-alert-config-business-service.factory'
    import rbAlertIsolateServiceFactory from 'src/services/alert/rb-alert-isolate-service.factory'
    export default {
        name: 'MirrorAlertShield',
        mixins: [QueryObject],
        components: {
            YwFilterOrder: () => import('src/components/common/yw-filter-order.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                id: '',
                activeCollapseNames: ['1', '2', '3'],
                // 表单数据
                formData: {
                    name: '*****业务告警规则',
                    user: ['当前用户'],
                    startTime: '2019-08-13',
                    endTime: '2019-08-13',
                },
                optionData: {},
                detailFlag: true,
                showFilterFlag: false,
                // 结果
                result: [],
                currentUserFlag: false,
                otherUserFlag: false,
            }
        },
        created() {
            this.id = this.$route.query.id
            this.queryDetail()
            this.query()
        },
        methods: {
            queryDetail() {
                rbAlertConfigExceptionServiceFactory.getDetail(this.id).then((date) => {
                    this.formData = date
                    let myuser = sessionStorage.getItem('username')
                    let users = this.formData.operateUser.split(',')
                    users.forEach((user) => {
                        if (user === myuser) {
                            this.currentUserFlag = true
                        } else {
                            this.otherUserFlag = true
                        }
                    })
                    this.optionData = JSON.parse(date.optionCondition)
                    this.showFilterFlag = true
                }).catch(() => {
                    this.showFilterFlag = true
                })

                // 工单数据
            },
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['pageNum'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {
                    this.queryParams = {
                        'relationId': this.id,
                        'operateModel': 'alert_business',
                        'pageNum': this.initPageChange(),
                        'pageSize': this.pageSize
                    }
                }

            },
            query(activePagination = false) {
                this.setParams(activePagination)
                rbAlertIsolateServiceFactory.getOperateLogList(this.queryParams).then((data) => {
                    this.result = data.result
                    this.total = data.count
                }).catch(() => {
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
