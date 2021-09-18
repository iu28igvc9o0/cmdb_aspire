<template>
    <div class="SolveQuestionsCommon">
        <div class="common-dashbord">
            <div class="form-content">
                <el-form :label-position="labelPosition"
                         label-width="80px"
                         :model="formLabelAlign">
                    <el-form-item label="标题">
                        <el-input v-model="formLabelAlign.title"></el-input>
                    </el-form-item>
                    <el-form-item label="类型"
                                  style="display:inline-block;">
                        <el-select v-model="formLabelAlign.type"
                                   clearable
                                   placeholder="请选择">
                            <el-option v-for="item in typeOptions"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="分类"
                                  style="display:inline-block;">
                        <el-select v-model="formLabelAlign.classify"
                                   clearable
                                   placeholder="请选择">
                            <el-option v-for="item in classifyOptions"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="时间"
                                  style="display:inline-block;">
                        <el-date-picker v-model="formLabelAlign.createTime"
                                        clearable
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item style="display:inline-block;">
                        <div class="flex-btn">
                            <el-button type="primary"
                                       @click="search">搜索</el-button>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
        </div>
        <div class="common-dashbord-bottom">
            <div class="tabCover">
                <el-button type="primary"
                           v-if="!userType"
                           class="create"
                           @click="create">新建</el-button>
                <el-table :data="tableData"
                          border
                          stripe
                          style="width: 100%;margin-bottom:10px;">
                    <el-table-column label="序号"
                                     type="index"
                                     width="50"
                                     align="center">
                    </el-table-column>
                    <el-table-column prop="department2"
                                     label="租户"
                                     align="center"
                                     width="180">
                    </el-table-column>
                    <el-table-column label="类型"
                                     align="center"
                                     width="180">
                        <template slot-scope="scope">
                            <!-- <span>{{checkClass(scope.row.class)}}</span> -->
                            <span>{{scope.row.type}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column align="center"
                                     width="180"
                                     label="分类">
                        <template slot-scope="scope">
                            <!-- <span>{{checkType(scope.row.type)}}</span> -->
                            <span>{{scope.row.classify}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="title"
                                     align="center"
                                     label="标题">
                    </el-table-column>
                    <el-table-column align="center"
                                     width="80"
                                     label="状态">
                        <template slot-scope="scope">
                            <!-- <span>{{checkStatus(scope.row.status)}}</span> -->
                            <span>{{scope.row.status}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="处理操作"
                                     align='center'
                                     width="200">
                        <template slot-scope="scope">
                            <!-- <el-button size="mini"
                                       v-if="!scope.row.status"
                                       icon="el-icon-upload"
                                       type="primary"
                                       @click="handleClick(scope.row)">提交</el-button> -->
                            <el-button size="mini"
                                       icon="el-icon-edit"
                                       v-if="scope.row.status"
                                       type="primary"
                                       @click="handleClick(scope.row)">处理</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <YwPagination @handleSizeChange="handleSizeChange"
                              @handleCurrentChange="handleCurrentChange"
                              :current-page="currentPage"
                              :page-sizes="pageSizes"
                              :page-size="pageSize"
                              :total="total"></YwPagination>
            </div>
        </div>
    </div>
</template>
<script>
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    import QueryObject from 'src/utils/queryObject.js'
    export default {
        name: 'SolveQuestionsCommon',
        mixins: [QueryObject],
        props: ['userType'],
        data() {
            return {
                loginName: '',
                class: ['问题', '建议'],
                types: ['资源管理', '工单服务', '故障处理'],
                status: ['提交', '处理'],
                tableData: [],
                labelPosition: 'right',
                formLabelAlign: {
                    title: '',
                    type: '',
                    classify: '',
                    createTime: ''
                },
                typeOptions: [],
                classifyOptions: []
            }
        },
        computed: {
            // eslint-disable-next-line no-unused-vars
            checkStatus(val) {
                return function (val) {
                    return this.status[val]
                }
            },
            // eslint-disable-next-line no-unused-vars
            checkType(val) {
                return function (val) {
                    return this.types[val]
                }
            },
            // eslint-disable-next-line no-unused-vars
            checkClass(val) {
                return function (val) {
                    return this.class[val]
                }
            },
        },
        components: {
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        created() {
            this.pageSize = 10
            this.loginName = sessionStorage.getItem('username')
            console.log(this.userType)
            this.getTypeRes()
            this.getClassRes()
            this.query()
        },
        methods: {
            // 搜索
            search() {
                this.currentPage = 1
                this.query()
            },
            // 新建方法
            create() {
                this.$router.push({ path: '/cmdb/solveQuestions/process', query: { queryParams: 1 } })
            },
            // 操作按钮事件
            handleClick(row) {
                console.log(row)
                if (!this.userType) {
                    // this.$router.push({ path: '/cmdb/solveQuestions/confirm', query: { queryParams: JSON.stringify(row) } })
                    this.$router.push({ path: '/cmdb/solveQuestions/confirm', query: { queryParams: row.id } })
                } else {
                    // this.$router.push({ path: '/cmdb/solveQuestions/deal', query: { queryParams: JSON.stringify(row) } })
                    this.$router.push({ path: '/cmdb/solveQuestions/deal', query: { queryParams: row.id } })
                }
            },
            query() {
                let paramsData = { ...this.formLabelAlign, loginName: this.loginName, isAdmin: this.userType, pageNo: this.currentPage, pageSize: this.pageSize }
                this.rbHttp.sendRequest({
                    method: 'POST',
                    data: paramsData,
                    url: '/v1/v3/cmdb/screen/problem/list'
                }).then((res) => {
                    console.log(res)
                    this.tableData = res.data
                    this.total = res.totalSize
                })
            },
            getTypeRes() {
                let obj = {
                    type: 'cmdb_bbs_type'
                }
                // eslint-disable-next-line no-unused-vars
                rbConfigDictServiceFactory.getDictsByType(obj).then(res => {
                    this.typeOptions = res
                })

            },
            getClassRes() {
                let obj = {
                    type: 'cmdb_bbs_classify'
                }
                // eslint-disable-next-line no-unused-vars
                rbConfigDictServiceFactory.getDictsByType(obj).then(res => {
                    this.classifyOptions = res
                })

            }
        }
    }
</script>
<style lang="scss" scoped>
    .SolveQuestionsCommon {
        display: flex;
        flex-direction: column;
        height: 100%;
        box-sizing: border-box;
        padding-bottom: 20px;
        width: 100%;
    }
    .common-dashbord {
        background: white;
        box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.05);
        border-radius: 16px;
        margin: 10px;
        box-sizing: border-box;
    }
    .form-content {
        width: 100%;
        padding: 0 5%;
        padding-top: 40px;
        box-sizing: border-box;
    }
    .common-dashbord-bottom {
        flex: 1;
        background: white;
        box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.05);
        border-radius: 16px;
        margin: 10px;
        box-sizing: border-box;
    }
    .tabCover {
        width: 100%;
        box-sizing: border-box;
        padding: 20px 40px;
    }
    .create {
        margin-bottom: 20px;
    }
</style>