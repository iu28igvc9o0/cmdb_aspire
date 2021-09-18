<template>
    <div class="choose-wrap">
        <div class="clearfix">
            <!-- 结果 -->
            <section class="search-right fl">
                <div class="yw-el-table-wrap">
                    <el-table class="yw-el-table"
                              height="290"
                              highlight-current-row
                              ref="singleTable"
                              :data="result"
                              v-loading="loading"
                              @row-click="choose"
                              style="width: 100%">
                        <el-table-column label="设备类型"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.value }}
                            </template>
                        </el-table-column>
                        <el-table-column label="设备分类"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.device_class }}
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </section>
            <!-- 结果 -->
        </div>

        <!-- 按钮 -->
        <section class="btn-wrap">
            <el-button type="primary"
                       size="small"
                       @click="submit()">确定</el-button>
            <el-button size="small"
                       @click="cancel()">取消</el-button>
        </section>
        <!-- 按钮 -->
    </div>

</template>
<script>
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import QueryObject from 'src/utils/queryObject.js'

    export default {
        props: ['currentType', 'dialogMsg'],
        mixins: [QueryObject],

        data() {
            return {
                // 结果
                result: []

            }
        },
        mounted() {
            this.getRefModuleDict()
        },
        methods: {
            // 引用模型
            getRefModuleDict() {
                let params = {codeId: '067f1d8e261944a68c2121cf6f305871'}
                this.loading = true
                return rbCmdbModuleService.getRefModuleDict({ params: params}).then((res) => {
                    if (res) {
                        this.loading = false
                        this.result = res
                        if (this.dialogMsg.data.type) {
                            let val = this.dialogMsg.data.rs
                            let row = this.result.find(function (r) {
                                return r.value == val
                            })
                            this.$refs.singleTable.setCurrentRow(row)
                        }
                    }
                }).catch(error => {
                    this.$message.error(error)
                    this.loading = false
                })
            },

            // 确认
            submit() {
                // 你自己的操作
                let obj = {
                    mulFlag: false,
                    result: this.submitResult,
                    type: this.currentType.id,
                    typeList: this.result
                }
                this.$emit('setData', obj)
            },

            // 取消
            cancel() {
                this.$emit('closeDialog')
            },

            // 选择
            choose(val) {
                this.submitResult = val
                this.$refs.singleTable.setCurrentRow(val)
            },

        }
    }

</script>
<style lang="scss" scoped>
    .btn-wrap {
        margin-top: 20px;
    }
    .choose-wrap {
        display: inline-block;
    }
    .search-right {
        width: 304px;
    }

    .yw-el-table-wrap {
        border: 1px solid rgba(220, 223, 230, 1);
        border-radius: 8px;
        height: 300px;
        padding: 2px;
        /deep/ .el-table__row {
            cursor: pointer;
        }
    }
</style>
