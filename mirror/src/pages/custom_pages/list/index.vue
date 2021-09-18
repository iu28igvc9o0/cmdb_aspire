<template>
    <div class="components-container yw-dashboard"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">

        <el-form class="yw-form">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="showAddDialog">新建</el-button>
            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="pagesList"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 170px)"
                          @selection-change="handleSelectionChange"
                          v-loading="loading">
                    <el-table-column prop="name"
                                     label="页面名称"
                                     min-width="110"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="describe"
                                     label="描述"
                                     min-width="110"
                                     show-overflow-tooltip></el-table-column>
                    <el-table-column prop="createTime"
                                     label="创建时间"
                                     width="180">
                        <template slot-scope="scope">
                            {{scope.row.createTime | formatDate}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="user_id"
                                     label="创建人"
                                     width="120"></el-table-column>
                    <el-table-column label="操作"
                                     width="120">
                        <template slot-scope="scope">
                            <div class="yw-table-operator">
                                <el-button type="text"
                                           title="编辑"
                                           icon="el-icon-edit"
                                           @click="updateRow(scope.row)"></el-button>
                                <el-button type="text"
                                           title="设计页面"
                                           icon="el-icon-brush"
                                           @click="designPage(scope.row)"></el-button>
                                <el-button type="text"
                                           title="删除"
                                           icon="el-icon-delete"
                                           @click="deletePageView(scope.row.id)"></el-button>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <!-- <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper"></el-pagination>
            </div> -->
        </el-form>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   v-if="dialogBoxShow"
                   :title="dialogName"
                   :visible.sync="dialogBoxShow"
                   :close-on-click-modal="false"
                   :destroy-on-close="true"
                   width="700px">
            <section class="yw-dialog-main middle-size-form">
                <el-form class="yw-form is-required"
                         ref="addForm"
                         :model="addForm"
                         :rules="addFormRules"
                         label-width="100px">
                    <el-form-item label="首页名称"
                                  prop="name">
                        <el-input v-model="addForm.name"
                                  placeholder="请输入首页名称"></el-input>
                    </el-form-item>
                    <el-form-item label="页面描述"
                                  prop="describe">
                        <el-input type="textarea"
                                  rows="4"
                                  v-model="addForm.describe"
                                  placeholder="请输入页面描述"></el-input>
                    </el-form-item>
                    <el-form-item label="页面样式:"
                                  prop="pageType">
                        <el-radio-group v-model="addForm.pageType">
                            <el-radio label="dark"
                                      name="type">深色模式</el-radio>
                            <el-radio label="light"
                                      name="type">浅色模式</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="addPageView()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbCustomServices from 'src/services/custom_pages/rb-services.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        name: 'CustomPagesList',
        components: {
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                pagesList: [], // 脚本列表
                formSearch: {
                    codeName: '',
                    creater: '',
                    createTimeStart: '',
                    createTimeEnd: '',
                    workName: ''
                },
                codeCloneList: [],  // 脚本克隆列表
                codeInfo: {}, // 脚本弹框最新内容
                currentCodeInfo: {},  // 当前行脚本内容
                dialogName: '新建脚本',  // 弹框title

                dialogBoxShow: false,
                addForm: {
                    name: '',
                    describe: '',
                    pageType: '',
                },
                addFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请输入文件名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    describe: [
                        {
                            required: true,
                            message: '请输入页面描述!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 100,
                            message: '长度在 2 到 100 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    pageType: [
                        {
                            required: true,
                            message: '请选择页面样式!',
                            trigger: ['blur', 'change']
                        },
                    ],
                },
            }
        },
        mixins: [rbAutoOperationMixin],
        computed: {
            username() {
                return sessionStorage.getItem('username')
            }
        },
        watch: {
            $route(to) {
                if (to.query.fullPath === '/custom_pages/list') {
                    this.search()
                }
            }
        },
        mounted() {
            this.search()
        },
        methods: {
            // 查询列表
            search() {
                let req = {
                    user_id: this.username,
                }
                this.loading = true
                rbCustomServices
                    .getPagesList(req)
                    .then(res => {
                        this.loading = false
                        this.pagesList = res
                    })
                    .catch(error => {
                        this.loading = false
                        this.showErrorTip(error)
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },
            // 保存
            addPageView() {
                this.$refs.addForm.validate((valid) => {
                    if (!valid) {
                        this.$message.warning('请先完善信息')
                        return
                    }
                    let saveType = this.dialogName === '新建视图' ? 'addPageView' : 'updatePageView'

                    this.pageLoading = true

                    let req = Object.assign(this.addForm, {
                        user_id: this.username,
                    })
                    rbCustomServices[saveType](req)
                        .then(res => {
                            if (res === 'success') {
                                this.$message.success('保存成功')
                                this.search()
                                this.dialogBoxShow = false
                            } else {
                                this.$message.error(res)
                            }
                            this.pageLoading = false
                        })
                        .catch(error => {
                            this.pageLoading = false
                            this.showErrorTip(error)
                        })
                })
            },
            // 取消关闭弹框
            addCancel() {
                this.dialogBoxShow = false
            },
            // 显示新建弹框
            showAddDialog() {
                this.dialogName = '新建视图'
                this.addForm = {
                    name: '',
                    describe: ''
                }
                this.dialogBoxShow = true

                // 测试-流程工单跳转
                // this.$router.push({
                //     path: '/resource/flow',
                //     query: {
                //         routerHash: 'inst/2009081657333959123'
                //     }
                // })
            },
            // 编辑
            updateRow(row) {
                this.dialogName = '编辑视图'
                this.addForm = this.$utils.deepClone(row)
                this.dialogBoxShow = true
            },
            // 设计页面
            designPage(row) {
                let rowContent = row.content || '[]'
                this.$store.commit('setCurrentRowContent', JSON.parse(rowContent))
                this.$router.push({
                    path: '/custom_pages/main',
                    query: {
                        id: row.id,
                        currentTitle: row.name,
                        useOneTab: 1,
                        pageType: row.name.includes('通用首页') ? 'light' : row.pageType
                    }
                })
            },
            // 删除行
            deletePageView(id) {
                this.$confirm('确认删除?').then(() => {
                    rbCustomServices.deletePageView(id || this.multipleSelection).then(res => {
                        if (res === 'success') {
                            this.$message.success('删除成功!')
                            this.search()
                        } else {
                            this.showErrorTip(res)
                        }
                    })
                        .catch(error => {
                            this.showErrorTip(error)
                        })
                })
            },
        }
    }
</script>

<style lang="scss" scoped>
</style>
