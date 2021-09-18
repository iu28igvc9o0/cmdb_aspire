<!-- 月账单管理 -->
<template>
    <div>
        <div style="margin: 20px">
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

        <el-row :gutter="12"
                class="mt10">
            <el-col :span="24">
                <el-card shadow="always"
                         class='ml20 mr20'>
                    <el-form>
                        <el-form-item>
                        </el-form-item>
                        <el-form-item>
                            <div class="top-wrap">
                                <span class="tit">月账单</span>
                                <div class="btn-box">
                                    <el-button type="primary"
                                               @click="goAdd()">全选</el-button>
                                    <el-button type="primary"
                                               @click="goAdd()">批量下载账单</el-button>
                                </div>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-table border
                                      stripe
                                      :data="tableData">
                                <el-table-column prop="date"
                                                 type="selection"
                                                 label="月份">
                                </el-table-column>
                                <el-table-column prop="name"
                                                 label="租户名称">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="月度费用合计(元)">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 align="center"
                                                 label="操作">
                                    <template slot-scope="scope">
                                        <el-button type="info"
                                                   icon="el-icon-edit"
                                                   circle
                                                   @click="editBtn(scope.row)"></el-button>
                                        <el-button type="info"
                                                   icon="el-icon-s-order"
                                                   circle
                                                   @click="editBtn(scope.row)"></el-button>
                                        <el-button type="danger"
                                                   icon="el-icon-download"
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