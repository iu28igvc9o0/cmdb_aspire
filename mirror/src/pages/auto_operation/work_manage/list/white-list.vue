<template>
    <div>
        <el-form class="yw-form is-required"
                 ref="addForm"
                 :model="addForm">
            <el-form-item label="白名单管理"
                          prop="selectedData">
                <el-button type="primary"
                           @click="switchDialog"
                           v-if="!isPreview">新增白名单对象</el-button>
                <div class="yw-el-table-wrap">
                    <el-table :data="addForm.selectedData"
                              class="yw-el-table"
                              stripe
                              tooltip-effect="dark"
                              border
                              v-loading="loading">
                        <el-table-column prop="command"
                                         label="敏感指令"
                                         width="120">
                        </el-table-column>
                        <el-table-column prop="rule_name"
                                         label="规则名称"
                                         show-overflow-tooltip></el-table-column>
                        <el-table-column prop="object_status"
                                         label="绑定状态"
                                         width="120">
                            <template slot-scope="scope">
                                <span v-if="scope.row.object_status === 2">已解除</span>
                                <span v-if="scope.row.object_status === 1">已绑定</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="update_time"
                                         label="最新操作时间"
                                         width="160"></el-table-column>
                        <el-table-column label="操作"
                                         width="60"
                                         v-if="!isPreview">
                            <template slot-scope="scope">
                                <el-button v-if="scope.row.object_status === 2"
                                           type="text"
                                           @click="updateObjectStatusByWhiteId(scope.row)">绑定</el-button>
                                <el-button v-if="scope.row.object_status === 1"
                                           type="text"
                                           @click="updateObjectStatusByWhiteId(scope.row)">解除</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </el-form-item>

        </el-form>
        <!-- dialog -->

        <el-dialog class="yw-dialog"
                   title="新建白名单对象"
                   :visible.sync="boxShow"
                   width="800px"
                   append-to-body
                   :close-on-click-modal="false">
            <div class="components-container yw-dashboard">
                <el-form class="components-condition yw-form"
                         style="display:flex"
                         label-width="65px">
                    <el-form-item label="敏感指令">
                        <el-input v-model="whiteData.command"
                                  placeholder="请输入敏感指令"></el-input>
                    </el-form-item>
                    <el-form-item label="规则名称">
                        <el-input v-model="whiteData.rule_name"
                                  placeholder="请输入规则名称"></el-input>
                    </el-form-item>
                    <section class="btn-wrap">
                        <el-button type="primary"
                                   @click="getQuerySensitiveRuleList()">查询</el-button>
                        <!-- <el-button @click="reset()">重置</el-button> -->
                    </section>

                </el-form>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="whiteList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection"
                                     width="55">
                    </el-table-column>
                    <el-table-column prop="command"
                                     label="敏感指令"
                                     width="120">
                    </el-table-column>
                    <el-table-column prop="rule_name"
                                     label="规则名称"
                                     width="120">
                    </el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="whiteSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="[5, 10, 50]"
                               :page-size="whiteData.page_size"
                               :page-no="whiteData.page_no"
                               :total="whiteTotal"
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
            </div>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="saveCommand()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>

        </el-dialog>

    </div>
</template>

<script>
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'
    import rbAutoOperationCustomParameterFactory from 'src/services/auto_operation/rb-auto-operation-custom-parameter.factory.js'

    export default {
        props: ['dataList', 'rowData', 'isPreview', 'pipelineId'],
        components: {
        },
        data() {
            return {
                boxShow: false,
                addForm: {
                    selectedData: []
                },
                whiteList: [],
                addWhiteList: [],
                selectList: [],
                whiteData: {
                    command: '',
                    rule_name: '',
                    page_size: 10,
                    page_no: 1

                },
                whiteTotal: 0,
                pageSizes: ['10', '20', '50']

            }
        },
        watch: {
            // 更新目标机器到当前行
            'addForm.selectedData'(newVal) {
                this.$emit('updateCurrentRow', this.rowData, 'white_list', newVal)
            },
            pipelineId(newVal) {
                this.search()
            }
        },
        computed: {
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
            this.getQuerySensitiveRuleList()

        },
        methods: {
            // 获取已有规则下的白名单列表
            search() {
                let req = {
                    object_type: 'pipeline',
                    object_id: this.pipelineId,
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                }
                rbAutoOperationInstructionServicesFactory
                    .querySensitiveRuleWhiteList(req)
                    .then(res => {
                        this.addForm.selectedData = res.dataList
                        this.total = res.totalCount
                    })
            },
            // 规则白名单状态更新
            updateObjectStatusByWhiteId(row) {
                let req = {
                    sensitiveRuleWhiteId: row.sensitive_rule_white_id,
                    status: row.object_status === 1 ? 2 : 1
                }
                this.$confirm('确定更新操作？').then(() => {
                    this.pageLoading = true
                    rbAutoOperationInstructionServicesFactory
                        .updateObjectStatusByWhiteId(req)
                        .then(res => {
                            if (res.flag) {
                                this.$message.success('更新成功！')
                                row.object_status = req.status
                            } else {
                                this.$message.error(res.error_tip)
                            }
                            this.pageLoading = false
                        })
                })
            },
            getQuerySensitiveRuleList() {
                rbAutoOperationInstructionServicesFactory.querySensitiveRuleList(this.whiteData).then(res => {
                    if (res.dataList) {
                        this.whiteList = res.dataList
                        this.whiteTotal = res.totalCount
                    }
                })
            },
            addToSelectedData(data) {
                this.boxShow = false
                this.addForm.selectedData = [].concat(this.addForm.selectedData, data)
            },
            switchDialog() {
                this.boxShow = !this.boxShow
            },
            whiteSizeChange(val) {
                this.whiteData.page_size = val
                this.getQuerySensitiveRuleList()
            },
            handleCurrentChange(val) {
                this.whiteData.page_no = val
                this.getQuerySensitiveRuleList()
            },
            // 勾选动作变化时触发
            handleSelectionChange(val) {
                this.selectList = val

            },
            saveCommand() {
                this.addWhiteList = []
                this.selectList.forEach(item => {
                    let obj = {
                        'object_type': 'pipeline',
                        'object_status': 1,
                        'object_id': this.pipelineId
                    }
                    obj.sensitive_rule_id = item.sensitive_rule_id
                    this.addWhiteList.push(obj)
                })
                console.log('this.addWhiteList===', this.addWhiteList)
                if (this.addWhiteList && this.addWhiteList.length > 0) {
                    this.createSensitiveRuleWhite(this.addWhiteList)
                }
            },
            createSensitiveRuleWhite(list) {
                rbAutoOperationCustomParameterFactory.batchCreateSensitiveRuleWhite({
                    rule_white_list: list
                }).then(res => {
                    if (res.flag === true) {
                        this.search()
                        this.boxShow = !this.boxShow
                    } else {
                        this.$message.error(res.error_tip)
                    }

                })
            }
        }
    }
</script>

<style lang="scss" scoped>
    // 故障自愈
    .border-container {
        border: 1px solid $color-border;
        padding: 10px 5px;
    }
    .ptop0 {
        padding-top: 0;
    }
    .arrow-right {
        width: 30px;
        height: 30px;
        line-height: 30px;
        border: 1px solid rgb(83, 96, 128);
        border-radius: 50%;
        text-align: center;
        cursor: pointer;
        .el-icon-right {
            font-size: 18px;
        }
        &:hover {
            border: 1px solid #46bafe;
            .el-icon-right {
                color: #46bafe;
            }
        }
    }
    .recovery-container .el-input .el-input__icon {
        line-height: 26px;
    }
</style>
