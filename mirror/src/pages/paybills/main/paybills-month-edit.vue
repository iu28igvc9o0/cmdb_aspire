<!-- 月账单管理-修改 -->
<template>
    <div>
        <div class="info-tit">
            <div class="info-tit-left">
                <template> <i class="el-icon-office-building"></i>
                    <span>租户名称&nbsp;&nbsp;&nbsp;信安中心</span></template>
                <template>
                    <i class="el-icon-date"></i>
                    <span>账单月份&nbsp;&nbsp;&nbsp;2020.07</span>
                </template>
            </div>
            <el-button type="primary">提交生成账单</el-button>
        </div>
        <el-row :gutter="12"
                class="mt10">
            <el-col :span="24">
                <el-card shadow="always"
                         class='ml20 mr20'>
                    <el-form>
                        <el-form-item>
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
                            <el-button type="primary">查询</el-button>
                        </el-form-item>
                        <el-form-item>
                            <p>裸金属资源</p>
                            <el-table :data="tableData"
                                      border
                                      style="width: 100%">
                                <el-table-column prop="date"
                                                 label="产品类型">
                                </el-table-column>
                                <el-table-column prop="name"
                                                 label="数量">
                                </el-table-column>
                                <el-table-column label="本月使用天数(天)">
                                    <template slot-scope="scope">
                                        <div class="edit-box">
                                            <template v-if="!editShow ">
                                                <span> {{scope.row.address}}
                                                </span>
                                                <i class="el-icon-edit"
                                                   @click="editBtn(scope.row, scope.$index)">冲销调整</i>
                                            </template>
                                            <template v-else>

                                            </template>
                                        </div>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="本月使用天数(天)">
                                </el-table-column>
                            </el-table>
                        </el-form-item>
                        <el-form-item>
                            <p>裸金属资源</p>
                            <el-table :data="tableData"
                                      border
                                      style="width: 100%">
                                <el-table-column prop="date"
                                                 label="产品类型">
                                </el-table-column>
                                <el-table-column prop="name"
                                                 label="数量">
                                </el-table-column>
                                <el-table-column label="本月使用天数(天)">
                                    <template slot-scope="scope">
                                        <div class="edit-box">
                                            <template v-if="!editShow[scope.$index]">
                                                <span> {{scope.row.address}}
                                                </span>
                                                <i class="el-icon-edit">冲销调整</i>
                                            </template>
                                            <template v-else>

                                            </template>
                                        </div>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="address"
                                                 label="本月使用天数(天)">
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
            return {
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
                tableData: [{
                    date: '2016-05-02',
                    name: '王小虎',
                    address: '2312'
                }, {
                    date: '2016-05-04',
                    name: '王小虎',
                    address: '2312'
                }, {
                    date: '2016-05-01',
                    name: '王小虎',
                    address: '2312'
                }, {
                    date: '2016-05-03',
                    name: '王小虎',
                    address: '2312'
                }],
                value: '',
                editShow: false
            }
        },

        methods: {
            editBtn(row, index) {
                console.log(this.editShow)
                this.editShow = true
                this.$set(this.editShow, index, true)
                console.log('row', row)
                console.log('row', index)
                console.log(this.editShow)
            }
        }
    }

</script>
<style lang='scss' scoped>
    .info-tit {
        display: flex;
        justify-content: space-between;
        padding: 20px;

        .info-tit-left {
            i {
                color: #2f8abf;
            }
            span {
                margin-right: 50px;
                font-weight: 700;
            }
        }
    }

    .edit-box {
        display: flex;
        justify-content: space-between;
        align-items: center;

        i {
            color: #2f8abf;
        }
    }
</style>