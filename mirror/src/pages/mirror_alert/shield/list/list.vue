<template>
    <div class="components-container yw-dashboard">
        <el-form class="yw-form components-condition"
                 label-width="65px"
                 :inline="true"
                 :model="formData">
            <el-form-item label="规则名称">
                <el-input v-model="formData.name"
                          placeholder="规则名称"></el-input>
            </el-form-item>
            <el-form-item label="启用状态">
                <el-select v-model="formData.status"
                           clearable>
                    <el-option v-for="(item, index) in isolate_status"
                               :key="index"
                               :label="item.label"
                               :value="item.value">
                    </el-option>
                </el-select>

            </el-form-item>
            <el-form-item label="生效时间">
                <el-date-picker v-model="formData.effectiveDate"
                                type="daterange"
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd"
                                size="mini"></el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="query()">查询
                </el-button>
                <el-button @click="reset()">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <div class="fr">
                    <el-button type="text"
                               icon=""
                               @click="getLogs()">屏蔽日志
                    </el-button>
                </div>
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="add()">新增
                </el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="delBatch()">删除
                </el-button>
                <el-button type="text"
                           icon="fa fa-unlock-alt"
                           @click="openBatch()">启用
                </el-button>
                <el-button type="text"
                           icon="fa fa-lock"
                           @click="closeBatch()">停用
                </el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table class="yw-el-table"
                          @selection-change="changeCheckbox"
                          v-loading="loading"
                          :data="result"
                          height="calc(100vh - 310px)"
                          stripe
                          border>
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column label="规则名称"
                                     show-overflow-tooltip
                                     width="340">
                        <template slot-scope="scope">
                            <a @click="view(scope.row.id)">{{scope.row.name}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column label="生效时间"
                                     width="220">
                        <template slot-scope="scope">
                            {{scope.row.effectiveDate | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column label="失效时间"
                                     width="220">
                        <template slot-scope="scope">
                            {{scope.row.expireDate | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column label="启用状态"
                                     width="200">
                        <template slot-scope="scope">
                            <span class="text-status"
                                  :class="{error:scope.row.status==='0'}">{{scope.row.status === '1' ? '启用' : '停用'}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="region"
                                     label="操作">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           @click="view(scope.row.id)"
                                           title="查看"
                                           icon="el-icon-view"></el-button>
                                <el-button type="text"
                                           @click="edit(scope.row.id)"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           v-if="scope.row.status==='0' && checkUse(scope.row)"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           @click="del(scope.row.id)"
                                           icon="el-icon-delete"
                                           v-if="scope.row.status==='0' && checkUse(scope.row)"></el-button>
                                <el-button v-if="scope.row.status==='0' && checkUse(scope.row)"
                                           type="text"
                                           @click="open(scope.row.id)"
                                           title="启用"
                                           icon="el-icon-unlock"></el-button>
                                <el-button v-if="scope.row.status==='1' && checkUse(scope.row)"
                                           type="text"
                                           @click="close(scope.row.id)"
                                           title="停用"
                                           icon="el-icon-lock"></el-button>
                                <el-button type="text"
                                           @click="getLogs(scope.row.id)"
                                           title="屏蔽日志"
                                           icon="el-icon-document"></el-button>
                            </div>

                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <YwPagination @handleSizeChange="handleSizeChange"
                          @handleCurrentChange="handleCurrentChange"
                          :current-page="currentPage"
                          :page-sizes="pageSizes"
                          :page-size="pageSize"
                          :total="total"></YwPagination>
        </el-form>

        <!-- dialog -->
        <DialogSheild :dialogMsg="dialogMsg"
                      @closeDialog="closeDialog"
                      v-if="dialogMsg.dialogVisible"></DialogSheild>
        <!-- dialog -->
    </div>
</template>

<script>
    import optionx from '../../alert/config/options.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbAlertIsolateServiceFactory from 'src/services/alert/rb-alert-isolate-service.factory'

    export default {
        name: 'MirrorAlertShield',
        mixins: [QueryObject, optionx],
        components: {
            DialogSheild: () => import('../dialog-shield.vue'),
            YwPagination: () => import('src/components/common/yw-pagination.vue'),
        },
        data() {
            return {
                // 表单数据
                formData: {
                    ip: '',
                    date: ''
                },
                // 查询条件
                queryParams: {},
                // 查询结果
                result: [],
                // dialog
                dialogMsg: {
                    dialogVisible: false,
                    data: {} // 数据
                },
                // 多选框模板存放的值
                multipleSelection: [],
            }
        },
        created() {
            this.query()
        },
        methods: {
            // 设置参数
            setParams(activePagination) {

                if (activePagination) {
                    this.queryParams['pageNum'] = this.currentPage
                    this.queryParams['pageSize'] = this.pageSize
                } else {

                    this.queryParams = {
                        'effectiveDateFrom': this.formData.effectiveDate && this.formData.effectiveDate[0],
                        'effectiveDateTo': this.formData.effectiveDate && this.formData.effectiveDate[1],
                        'name': this.formData.name,
                        'status': this.formData.status,
                        'pageNum': this.initPageChange(),
                        'pageSize': this.pageSize
                    }
                }

            },
            query(activePagination = false) {
                this.loading = true

                this.setParams(activePagination)
                rbAlertIsolateServiceFactory.getIsolateList(this.queryParams).then((data) => {
                    this.result = data.result
                    this.total = data.count
                    this.loading = false
                }).catch(() => {
                    this.loading = false
                })
            },
            reset() {
                this.formData = { 'name': '', 'status': '', 'effectiveDate': [] }
            },
            // 新增
            add() {
                this.dialogMsg.data = {}
                this.dialogMsg.dialogVisible = true
            },

            // 删除
            delBatch() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择屏蔽规则进行删除', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let ids = []
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    let item = this.multipleSelection[i]
                    if (!this.checkUse(item)) {
                        this.$alert('没有操作' + item.name + '的权限', '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                    ids.push(item.id)
                }
                rbAlertIsolateServiceFactory.delete(ids).then(() => {
                    this.query()
                })
            },
            del(id) {
                rbAlertIsolateServiceFactory.delete([id]).then(() => {
                    this.query()
                })
            },

            // 启用
            openBatch() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择屏蔽规则进行启用', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let openFlag = true
                let ids = []
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    let item = this.multipleSelection[i]
                    if (!this.checkUse(item)) {
                        this.$alert('没有操作' + item.name + '的权限', '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                    ids.push(item.id)
                    if (item.status === this.isolate_status[0].value) {
                        openFlag = false
                    }
                }
                if (!openFlag) {
                    this.$alert('已启用屏蔽规则无法再次启用', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.$confirm('确认启用?').then(() => {
                    rbAlertIsolateServiceFactory.start(ids).then(() => {
                        this.query()
                    })
                })
            },
            open(id) {
                this.$confirm('确认启用?').then(() => {
                    rbAlertIsolateServiceFactory.start([id]).then(() => {
                        this.query()
                    })
                })
            },
            // 停用
            closeBatch() {
                if (this.multipleSelection.length < 1) {
                    this.$alert('请选择屏蔽规则进行停用', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                let ids = []
                let closeFlag = true
                for (let i = 0; i < this.multipleSelection.length; i++) {
                    let item = this.multipleSelection[i]
                    if (!this.checkUse(item)) {
                        this.$alert('没有操作' + item.name + '的权限', '警告', {
                            confirmButtonText: '确定'
                        })
                        return
                    }
                    ids.push(item.id)
                    if (item.status === this.isolate_status[1].value) {
                        closeFlag = false
                    }
                }
                if (!closeFlag) {
                    this.$alert('已停用屏蔽规则无法再次停用', '警告', {
                        confirmButtonText: '确定'
                    })
                    return
                }
                this.$confirm('确认停用?').then(() => {
                    rbAlertIsolateServiceFactory.stop(ids).then(() => {
                        this.query()
                    })
                })
            },
            close(id) {
                this.$confirm('确认停用?').then(() => {
                    rbAlertIsolateServiceFactory.stop([id]).then(() => {
                        this.query()
                    })
                })
            },
            // 查看
            view(val) {
                this.$router.push({ path: 'details', query: { id: val } })
            },
            // 编辑
            edit(id) {
                this.dialogMsg.data = { 'id': id }
                this.dialogMsg.dialogVisible = true

            },

            // 屏蔽日志
            // getLogs() {
            //     this.$router.push({path: 'alert'})
            // },
            // 屏蔽日志
            getLogs(id) {
                this.$router.push({ path: 'alert', query: { 'id': id } })
            },
            // checkbox
            changeCheckbox(val) {
                this.multipleSelection = val
            },

            // 关闭弹窗
            closeDialog(type) {
                this.dialogMsg.dialogVisible = false
                if (type === 'update') {
                    this.query()
                }

            },
            checkUse(rowData) {
                let createUser = rowData.creater
                let operateUser = rowData.operateUser
                let userName = sessionStorage.getItem('username')
                let namespace = sessionStorage.getItem('namespace')
                if (userName === createUser || userName === namespace) {
                    return true
                }
                if (operateUser !== null) {
                    let userArray = operateUser.split(',')
                    for (var i = 0; i < userArray.length; i++) {
                        if (userArray[i] === userName) {
                            return true
                        }
                    }
                }
                return false
            },

        }
    }
</script>

<style lang="scss" scoped>
</style>
