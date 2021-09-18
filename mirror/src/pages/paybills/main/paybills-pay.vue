<!-- 缴费管理 -->
<template>
    <div>
        <el-row :gutter="12"
                class="mt10">
            <el-col :span="24">
                <el-card shadow="always"
                         class='ml20 mr20'>
                    <el-form>
                        <el-form-item>
                            <div style="margin: 0 20px">
                                <el-select v-model="value"
                                           placeholder="请选择">
                                    <el-option v-for="item in options"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value">
                                    </el-option>
                                </el-select>
                                <el-select v-model="value"
                                           placeholder="请选择">
                                    <el-option v-for="item in options"
                                               :key="item.value"
                                               :label="item.label"
                                               :value="item.value">
                                    </el-option>
                                </el-select>
                                <el-button type="primary"
                                           @click="queryBtn()">查询</el-button>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <div class="top-wrap">
                                <span class="tit">缴费明细</span>
                                <el-button type="success"
                                           icon="el-icon-plus"
                                           @click="dialogShow = true">新增</el-button>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-table border
                                      stripe
                                      :data="tableData">
                                <el-table-column prop="date"
                                                 label="折扣类型">
                                </el-table-column>
                                <el-table-column prop="name"
                                                 label="折扣系数">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="折扣租户名称">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="业务系统范围">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="资源池范围">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 align="center"
                                                 label="操作">
                                    <template slot-scope="scope">
                                        <el-button type="primary"
                                                   icon="el-icon-edit"
                                                   circle
                                                   @click="editBtn(scope.row)"></el-button>
                                        <el-button type="danger"
                                                   icon="el-icon-delete"
                                                   circle
                                                   @click="editBtn(scope.row)"></el-button>
                                    </template>
                                </el-table-column>
                            </el-table>

                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
        </el-row>
        <el-dialog class="pay-dialog"
                   :visible.sync="dialogShow"
                   :show-close="false"
                   width="500"
                   center>
            <el-form :model="dataParmas">
                <el-form-item label="租户名称"
                              :label-width="formLabelWidth">
                    <el-select v-model="value"
                               placeholder="请选择">
                        <el-option v-for="item in options"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费日期"
                              :label-width="formLabelWidth">
                    <el-select v-model="value"
                               placeholder="请选择">
                        <el-option v-for="item in options"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费类型"
                              :label-width="formLabelWidth">
                    <el-select v-model="value"
                               placeholder="请选择">
                        <el-option v-for="item in options"
                                   :key="item.value"
                                   :label="item.label"
                                   :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="缴费金额"
                              :label-width="formLabelWidth">
                    <el-input v-model="dataParmas.unitInput"
                              autocomplete="off"></el-input>
                </el-form-item>
            </el-form> <span slot="footer"
                  class="dialog-footer">
                <el-button @click="dialogShow = false">取 消</el-button>
                <el-button type="primary"
                           @click="dialogShow = false">确 定</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        data() {
            const item = {
                date: '2016-05-02',
                name: '王小虎',
                address: '上海市普陀区金'
            }
            return {
                tableData: Array(6).fill(item),
                dataParmas: {
                    moneyInput: '2345',
                    unitInput: '元/台/月'
                },
                dialogShow: false,
                formLabelWidth: '200px',
                options: [{
                    value: '选项1',
                    label: '黄金糕'
                }, {
                    value: '选项2',
                    label: '双皮奶'
                }, {
                    value: '选项3',
                    label: '蚵仔煎'
                }, {
                    value: '选项4',
                    label: '龙须面'
                }, {
                    value: '选项5',
                    label: '北京烤鸭'
                }],
                value: ''
            }
        },

        methods: {
            editBtn() { },
            queryBtn() { }
        }
    }

</script>
<style lang='scss' scoped>
    .top-wrap {
        display: flex;
        justify-content: space-between;
        margin: 0 20px;
    }

    .pay-dialog {
        .buttom-wrap {
            height: 100px;
        }

        .el-input {
            width: 200px;

            .el-input__inner {
                height: 38px;
                line-height: 38px;
            }
        }
    }

    .el-button {
        color: #fff;
    }
</style>