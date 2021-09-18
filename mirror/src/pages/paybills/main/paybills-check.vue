<!-- 缴费管理 -->
<template>
    <div class="pay-wrap">
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
                            <div class="info-wrap">
                                <img src="../../../assets/img/bg.png"
                                     alt="">
                                <div class="info-box">
                                    <h4>信安中心</h4>
                                    <p>截至本月累计已缴费(元)<span>384832</span></p>
                                    <p>截至本月账户余额(元)<span>384832</span></p>
                                </div>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <div class="top-wrap">
                                <span class="tit">账户明细</span>
                            </div>
                        </el-form-item>
                        <el-form-item>
                            <el-table border
                                      stripe
                                      :data="tableData">
                                <el-table-column prop="date"
                                                 label="日期">
                                </el-table-column>
                                <el-table-column prop="name"
                                                 label="类型">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="金额(元)">
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="账户余额(元)">
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
            queryBtn() { }
        }
    }

</script>
<style lang='scss' scoped>
    .pay-wrap {
        .info-wrap {
            display: flex;
            img {
                width: 100px;
                height: 100px;
            }

            .info-box {
                margin-left: 24px;
                p {
                    font-size: 12px;
                    line-height: 32px;

                    span {
                        color: #015eca;
                        padding-left: 20px;
                    }
                }

                p:nth-of-type(2) {
                    span {
                        padding-left: 32px;
                    }
                }
            }
        }
        .top-wrap {
            display: flex;
            justify-content: space-between;
            margin: 0 20px;
        }
    }

    .el-button {
        color: #fff;
    }
</style>