<template>
    <div class="automatic-configuration">
        <el-form :model="valueForm"
                 class="yw-form"
                 :inline="true"
                 label-width="160px"
                 label-position="right"
                 ref="automaticForm">
            <el-row>
                <div v-for="(item,index) of columnsData"
                     :key="index">
                    <template v-if="item.type=='text'">
                        <el-col :span="8"
                                v-for="(itemValue,i) of item.column"
                                :key="i">
                            <el-form-item :label="itemValue.filed_name+'：'"
                                          v-if="['update_time','update_person','id','module_id','insert_person','insert_time'].indexOf(itemValue.filed_code)<0">
                                <span class="item-value">{{valueForm[itemValue.filed_code]}}</span>
                            </el-form-item>
                        </el-col>
                    </template>
                    <el-col :span="24"
                            v-else>
                        <el-form-item :label="item.name+'：'"
                                      class="table-detail">
                            <el-table :data="valueForm[item.code]"
                                      border
                                      size="small"
                                      style="width:100%">
                                <el-table-column :prop="tableValue.filed_code"
                                                 v-for="(tableValue,j) of item.column"
                                                 :key="j"
                                                 :label="tableValue.filed_name"
                                                 min-width="200">
                                </el-table-column>
                            </el-table>
                        </el-form-item>
                    </el-col>
                </div>
                <!-- <el-col :span="8">
                    <el-form-item label="IP：">
                        <span class="item-value">172.168.0.0.1</span>
                    </el-form-item>
                </el-col>
               <el-col :span="8">
                    <el-form-item label="主机名：">
                        <span class="item-value">host2</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="agent心跳：">
                        <span class="item-value">host2</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="物理地址：">
                        <span class="item-value">005：3443：34344343</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="uuid：">
                        <span class="item-value">3434-5dfdf-34343</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="agent版本：">
                        <span class="item-value">1.2323</span>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label="CPU信息："
                                  class="table-detail">
                        <el-table :data="valueForm.tableData"
                                  border
                                  size="small"
                                  style="width:100%">
                            <el-table-column prop="date"
                                             label="型号"
                                             width="200">
                            </el-table-column>
                            <el-table-column prop="name"
                                             label="架构"
                                             width="180">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="频率">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="物理颗粒度">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="总物理核心数">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="逻辑CPU数">
                            </el-table-column>
                        </el-table>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="物理地址：">
                        <span class="item-value">005：3443：34344343</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="uuid：">
                        <span class="item-value">3434-5dfdf-34343</span>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="agent版本：">
                        <span class="item-value">1.2323</span>
                    </el-form-item>
                </el-col> 
                <el-col :span="24">
                    <el-form-item label="CPU信息："
                                  class="table-detail">
                        <el-table :data="valueForm.tableData"
                                  border
                                  size="small"
                                  style="width:100%">
                            <el-table-column prop="date"
                                             label="型号"
                                             width="200">
                            </el-table-column>
                            <el-table-column prop="name"
                                             label="架构"
                                             width="180">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="频率">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="物理颗粒度">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="总物理核心数">
                            </el-table-column>
                            <el-table-column prop="address"
                                             label="逻辑CPU数">
                            </el-table-column>
                        </el-table>
                    </el-form-item>
                </el-col>-->
            </el-row>
        </el-form>

    </div>
</template>

<script>
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        data() {
            return {
                valueForm: {

                },
                columnsData: []
            }
        },
        created() {
            this.getAutomateColumns()

        },
        methods: {
            getAutomateColumns() {
                rbCmdbServiceFactory.getAutomateColumns().then((res) => {
                    this.columnsData = res
                }).finally(() => {
                    this.getAutomateHostDetail()
                })
            },
            getAutomateHostDetail() {
                let queryData = {
                    ip: JSON.parse(this.$route.query.queryParams).ip
                }
                rbCmdbServiceFactory.getAutomateHostDetail(queryData).then((res) => {
                    this.valueForm = res.data || {}
                })
            }
        }
    }
</script>

<style scoped lang="scss">
    .yw-form /deep/ .el-form-item__label {
        text-align: right;
    }
    .table-detail {
        display: flex;
        /deep/ .el-form-item__content {
            width: 85%;
            margin-top: 10px;
        }
    }
</style>
