<template>
    <div class="components-container">
        <div class="run-code-box mtop10">
            <el-form class="yw-form is-required"
                     ref="addForm"
                     :model="addForm"
                     label-width="120px">
                <el-form-item label="白名单管理"
                              prop="selectedData">
                    <el-button type="primary"
                               @click="switchDialog"
                               v-if="!isPreview">新增白名单对象</el-button>
                    <el-input v-model="searchWord"
                              placeholder="请输入搜索内容">
                        <i slot="suffix"
                           class="el-input__icon el-icon-search"
                           @click="searchList"></i>
                    </el-input>
                    <div class="yw-el-table-wrap">
                        <el-table :data="addForm.selectedData"
                                  class="yw-el-table"
                                  stripe
                                  tooltip-effect="dark"
                                  border
                                  v-loading="loading">
                            <el-table-column prop="object_type"
                                             label="对象类型"
                                             width="120">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.object_type === 'pipeline'">作业</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="object_name"
                                             label="对象名称"
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
        </div>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   title="新建白名单对象"
                   :visible.sync="boxShow"
                   width="800px"
                   :close-on-click-modal="false">
            <transfer-whitelist :selectedData="addForm.selectedData"
                                @addToSelectedData="addToSelectedData"
                                @cancel="switchDialog"></transfer-whitelist>
        </el-dialog>
    </div>
</template>

<script>
    import transferWhitelist from './transfer-whitelist'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import rbAutoOperationInstructionServicesFactory from 'src/services/auto_operation/rb-auto-operation-instruction-services.factory.js'

    export default {
        props: ['dataList', 'rowData', 'isPreview'],
        components: {
            transferWhitelist
        },
        data() {
            return {
                boxShow: false,
                addForm: {
                    selectedData: []
                },

                searchWord: '',
            }
        },
        watch: {
            // 更新目标机器到当前行
            'addForm.selectedData'(newVal) {
                this.$emit('updateCurrentRow', this.rowData, 'white_list', newVal)
            }
        },
        computed: {
        },
        mixins: [rbAutoOperationMixin],
        created() {
            this.search()
        },
        methods: {
            // 获取已有规则下的白名单列表
            search(searchWord) {
                if (!this.rowData.sensitive_rule_id) {
                    return
                }
                let req = {
                    sensitive_rule_id: this.rowData.sensitive_rule_id,
                    page_no: this.currentPage,
                    page_size: this.pageSize,
                    objectNameLike: searchWord
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
            addToSelectedData(data) {
                this.boxShow = false
                this.addForm.selectedData = [].concat(this.addForm.selectedData, data)
            },
            switchDialog() {
                this.boxShow = !this.boxShow
            },
            // 过滤
            searchList() {
                this.search(this.searchWord)
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
