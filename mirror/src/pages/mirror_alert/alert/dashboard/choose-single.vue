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
                              v-loading="loading"
                              :data="result"
                              @row-click="choose"
                              style="width: 100%">
                        <el-table-column label="业务名称"
                                         align="left">
                            <template slot-scope="scope">
                                {{ scope.row.value }}
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
    import QueryObject from 'src/utils/queryObject.js'
    import rbProjectDataServiceFactory from 'src/services/template/rb-template-services.factory.js'
    import configDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'

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
            this.query()
        },
        methods: {
            // 查询数据
            query() {
                this.loading = true
                if (this.currentType.id == 1) {
                    let data = {
                    }
                    configDictServiceFactory.getBizSystem(data).then((res) => {
                        if (res) {
                            this.loading = false
                            const list = []
                            for (let i in res) {
                                let obj = { value: res[i].bizSystem }
                                list.push(obj)
                            }
                            this.result = list
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
                } else {
                    let obj = {
                        'type': 'bizSystem',
                        'pid': ''
                    }

                    if (this.currentType.id == 2) {
                        obj.type = 'idcType'
                    } else if (this.currentType.id == 3) {
                        obj.type = 'roomId'
                    } else if (this.currentType.id == 4) {
                        obj.type = 'device_class'
                    } else if (this.currentType.id == 5) {
                        obj.type = 'device_type'
                    }
                    rbProjectDataServiceFactory.getConfigDictByType(obj).then((res) => {
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
                    })
                }
            },

            // 确认
            submit() {
                // 你自己的操作
                let obj = {
                    mulFlag: false,
                    result: this.submitResult,
                    type: this.currentType.id
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
