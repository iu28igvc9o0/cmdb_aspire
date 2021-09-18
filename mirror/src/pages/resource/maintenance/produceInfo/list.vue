<template>
    <div class="components-container yw-dashboard"
         v-loading="loading">
        <el-form class="components-condition yw-form"
                 :inline="true"
                 label-width="65px">
            <!--<el-form-item label="联系人">-->
            <!--<el-input v-model="search_name"></el-input>-->
            <!--</el-form-item>-->
            <!--<el-form-item label="电话号码">-->
            <!--<el-input v-model="search_phone"></el-input>-->
            <!--</el-form-item>-->
            <!--<el-form-item label="邮箱地址">-->
            <!--<el-input v-model="search_email"></el-input>-->
            <!--</el-form-item>-->
            <el-form-item label="设备厂商">
                <el-input v-model="search_produce"></el-input>
            </el-form-item>
            <el-form-item label="厂商类型">
                <el-select clearable
                           v-model="search_type"
                           placeholder="请选择">
                    <el-option v-for="item in produceTypeList"
                               :key="item.id"
                               :label="item.name"
                               :value="item.name">
                    </el-option>
                </el-select>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="search('search')">查询</el-button>
                <el-button @click="cancel('searchHander')">重置</el-button>
            </section>
        </el-form>

        <el-form class="yw-form form-left">
            <div class="table-operate-wrap clearfix">
                <el-button type="text"
                           icon="el-icon-plus"
                           @click="showAddDialog()">新增</el-button>
                <el-button type="text"
                           icon="el-icon-edit"
                           @click="showUpdateCheck">编辑</el-button>
                <el-button type="text"
                           icon="el-icon-delete"
                           @click="showdeleteDialog()">删除</el-button>
                <el-button type="text"
                           icon="el-icon-download"
                           @click="importData()">导入</el-button>
                <el-button type="text"
                           icon="el-icon-upload2"
                           @click="exportData()">导出</el-button>

            </div>
            <div class="yw-el-table-wrap">
                <el-table :data="tableData"
                          class="yw-el-table"
                          stripe
                          tooltip-effect="dark"
                          border
                          height="calc(100vh - 300px)"
                          @selection-change="handleSelectionChange"
                          row-key="id"
                          @row-click="clickRow"
                          ref="moviesTable"
                          @row-dblclick="selectedRow">
                    <el-table-column type="selection"></el-table-column>
                    <el-table-column prop="produce"
                                     label="厂商"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <el-button type="text"
                                       @click="gotoDetail(scope.row.id)">
                                {{scope.row.produce}}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="logoUrl"
                                     label="厂商图标"
                                     :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <!--<el-image :src="scope.row.logoUrl"></el-image>-->
                            <img v-if="scope.row.logoUrl"
                                 style="width: 117px; height: 43px"
                                 :src="scope.row.logoUrl + '?'+ Math.random()" />
                            <span style="color:#909399"
                                  v-else>未上传logo</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="type"
                                     label="厂商类型"
                                     :show-overflow-tooltip="true"></el-table-column>
                    <el-table-column prop="remark"
                                     label="备注"
                                     :show-overflow-tooltip="true"></el-table-column>
                </el-table>
            </div>
            <div class="yw-page-wrap">
                <el-pagination @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                               :current-page="currentPage"
                               :page-sizes="pageSizes"
                               :page-size="pageSize"
                               :total="total"
                               layout="total, sizes, prev, pager, next, jumper">
                </el-pagination>
            </div>
        </el-form>

        <el-dialog class="yw-dialog"
                   :visible.sync="dialogInfo.show"
                   width="720px"
                   :close-on-click-modal="false"
                   :title="dialogInfo.title">
            <section class="yw-dialog-main">
                <el-form class="yw-form is-required"
                         :model="addForm"
                         :rules="addRules"
                         ref="addForm"
                         label-width="80px">
                    <el-form-item label="厂商"
                                  prop="produce">
                        <el-input v-model="addForm.produce"></el-input>
                    </el-form-item>
                    <el-form-item label="厂商图标"
                                  prop="logoUrl">
                        <el-upload class="avatar-uploader"
                                   ref="uploadLogo"
                                   action="/v1/cmdb/maintenProduce/insertProduce"
                                   name="logo"
                                   list-type="picture"
                                   :headers="headers"
                                   :data="addForm"
                                   :show-file-list="false"
                                   :auto-upload="false"
                                   :on-success="submitSuccess"
                                   :on-error="submitError"
                                   :on-change="imgPreview">
                            <img v-if="addForm.logoUrl"
                                 :src="addForm.logoUrl"
                                 class="avatar" />
                            <i v-else
                               class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="厂商类型"
                                  prop="type">
                        <el-select v-model="addForm.type">
                            <el-option v-for="item in produceTypeList"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.name">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="备注"
                                  prop="remark">
                        <el-input type="textarea"
                                  style="width: 178px"
                                  v-model="addForm.remark"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           class="tem-search1"
                           @click="saveAddForm('addForm')">确 定</el-button>
                <el-button class="tem-search1"
                           @click="cancel('add')">取 消</el-button>
            </section>
        </el-dialog>

        <!--<el-dialog class="yw-dialog"-->
        <!--:visible.sync="updateDialog"-->
        <!--width="720px"-->
        <!--:close-on-click-modal="false"-->
        <!--title="编辑厂商信息">-->
        <!--<section class="yw-dialog-main">-->
        <!--<el-form :model="updateForm"-->
        <!--:rules="updateRules"-->
        <!--ref="updateForm"-->
        <!--label-width="80px"-->
        <!--class="yw-form is-required">-->
        <!--<el-form-item label="厂商"-->
        <!--prop="produce">-->
        <!--<el-input v-model="updateForm.produce"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="厂商图标"-->
        <!--prop="logoUrl">-->
        <!--&lt;!&ndash;<el-upload&ndash;&gt;-->
        <!--&lt;!&ndash;class="avatar-uploader"&ndash;&gt;-->
        <!--&lt;!&ndash;ref="uploadLogo"&ndash;&gt;-->
        <!--&lt;!&ndash;action="/v1/cmdb/maintenProduce/insertProduce"&ndash;&gt;-->
        <!--&lt;!&ndash;name="logo"&ndash;&gt;-->
        <!--&lt;!&ndash;:headers="headers"&ndash;&gt;-->
        <!--&lt;!&ndash;:data="addForm"&ndash;&gt;-->
        <!--&lt;!&ndash;:auto-upload="false"&ndash;&gt;-->
        <!--&lt;!&ndash;:show-file-list="false"&ndash;&gt;-->
        <!--&lt;!&ndash;:on-success="submitSuccess"&ndash;&gt;-->
        <!--&lt;!&ndash;:on-error="submitError">&ndash;&gt;-->
        <!--&lt;!&ndash;<img v-if="addForm.imageUrl" :src="addForm.imageUrl" class="avatar">&ndash;&gt;-->
        <!--&lt;!&ndash;<i v-else class="el-icon-plus avatar-uploader-icon"></i>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-upload>&ndash;&gt;-->
        <!--<el-upload-->
        <!--class="avatar-uploader"-->
        <!--ref="updateLogo"-->
        <!--action="/v1/cmdb/maintenProduce/insertProduce"-->
        <!--name="logo"-->
        <!--list-type="picture"-->
        <!--:headers="headers"-->
        <!--:data="updateForm"-->
        <!--:show-file-list="false"-->
        <!--:auto-upload="false"-->
        <!--:on-success="submitSuccess"-->
        <!--:on-error="submitError"-->
        <!--:on-change="updateImgPreview">-->
        <!--<img v-if="updateForm.logoUrl" :src="updateForm.logoUrl" class="avatar">-->
        <!--<i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
        <!--</el-upload>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="厂商类型"-->
        <!--prop="type">-->
        <!--<el-select v-model="updateForm.type">-->
        <!--<el-option v-for="(item, index) in produceTypeList"-->
        <!--:key="item.id"-->
        <!--:label="item.name"-->
        <!--:value="item.name">-->
        <!--</el-option>-->
        <!--</el-select>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="备注"-->
        <!--prop="remark">-->
        <!--<el-input type="textarea"-->
        <!--v-model="updateForm.remark"-->
        <!--style="width:178px"></el-input>-->
        <!--</el-form-item>-->
        <!--&lt;!&ndash;<el-form-item>&ndash;&gt;-->
        <!--&lt;!&ndash;<div class="tableDate">&ndash;&gt;-->
        <!--&lt;!&ndash;<div class="table">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-table class="yw-el-table"&ndash;&gt;-->
        <!--&lt;!&ndash;:data="updateForm.concatData"&ndash;&gt;-->
        <!--&lt;!&ndash;ref="table"&ndash;&gt;-->
        <!--&lt;!&ndash;tooltip-effect="dark"&ndash;&gt;-->
        <!--&lt;!&ndash;border&ndash;&gt;-->
        <!--&lt;!&ndash;stripe&ndash;&gt;-->
        <!--&lt;!&ndash;style="width: 100%">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-table-column label="联系人"&ndash;&gt;-->
        <!--&lt;!&ndash;min-width="30%">&ndash;&gt;-->
        <!--&lt;!&ndash;<template slot-scope="scope">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-form-item :prop="'concatData.' + scope.$index + '.name'">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-input v-model="scope.row.name"></el-input>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
        <!--&lt;!&ndash;</template>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-table-column>&ndash;&gt;-->
        <!--&lt;!&ndash;<el-table-column label="电话"&ndash;&gt;-->
        <!--&lt;!&ndash;min-width="30%">&ndash;&gt;-->
        <!--&lt;!&ndash;<template slot-scope="scope">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-form-item :prop="'concatData.' + scope.$index + '.phone'"&ndash;&gt;-->
        <!--&lt;!&ndash;:rules="updateRules.phone">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-input v-model="scope.row.phone"></el-input>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
        <!--&lt;!&ndash;</template>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-table-column>&ndash;&gt;-->
        <!--&lt;!&ndash;<el-table-column label="邮箱"&ndash;&gt;-->
        <!--&lt;!&ndash;min-width="30%">&ndash;&gt;-->
        <!--&lt;!&ndash;<template slot-scope="scope">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-form-item :prop="'concatData.' + scope.$index + '.email'"&ndash;&gt;-->
        <!--&lt;!&ndash;:rules="updateRules.email">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-input v-model="scope.row.email"></el-input>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
        <!--&lt;!&ndash;</template>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-table-column>&ndash;&gt;-->
        <!--&lt;!&ndash;<el-table-column label="备注"&ndash;&gt;-->
        <!--&lt;!&ndash;min-width="30%">&ndash;&gt;-->
        <!--&lt;!&ndash;<template slot-scope="scope">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-form-item :prop="'concatData.' + scope.$index + '.remark'">&ndash;&gt;-->
        <!--&lt;!&ndash;<el-input v-model="scope.row.remark"></el-input>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
        <!--&lt;!&ndash;</template>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-table-column>&ndash;&gt;-->
        <!--&lt;!&ndash;<el-table-column min-width="15%">&ndash;&gt;-->
        <!--&lt;!&ndash;<template slot-scope="scope">&ndash;&gt;-->
        <!--&lt;!&ndash;<i class="el-icon-circle-plus-outline"&ndash;&gt;-->
        <!--&lt;!&ndash;style="color: #409eff; font-size:15px;cursor:pointer"&ndash;&gt;-->
        <!--&lt;!&ndash;@click.prevent="addRow('update')"></i>&ndash;&gt;-->
        <!--&lt;!&ndash;<i v-show="updateForm.concatData.length > 1"&ndash;&gt;-->
        <!--&lt;!&ndash;class="el-icon-remove-outline"&ndash;&gt;-->
        <!--&lt;!&ndash;style="color: #409eff; font-size:15px;cursor:pointer"&ndash;&gt;-->
        <!--&lt;!&ndash;@click.prevent="delData(scope.$index, updateForm.concatData)"></i>&ndash;&gt;-->
        <!--&lt;!&ndash;</template>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-table-column>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-table>&ndash;&gt;-->
        <!--&lt;!&ndash;</div>&ndash;&gt;-->
        <!--&lt;!&ndash;</div>&ndash;&gt;-->
        <!--&lt;!&ndash;</el-form-item>&ndash;&gt;-->
        <!--</el-form>-->
        <!--</section>-->
        <!--<section class="btn-wrap">-->
        <!--<el-button type="primary"-->
        <!--class="tem-search1"-->
        <!--@click="saveEditForm('updateForm')">确 定</el-button>-->
        <!--<el-button class="tem-search1"-->
        <!--@click="cancel('update')">取 消</el-button>-->
        <!--</section>-->
        <!--</el-dialog>-->
        <YwImport v-if="display.isImport"
                  :showImport="display.isImport"
                  @setImportDisplay="closeParent"
                  :importType="importType"></YwImport>
    </div>
</template>

<script>
    import rbMainServiceFactory from 'src/services/cmdb/rb-maintenance-services.factory'
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory'

    export default {
        name: 'ResourceMaintenanceProduceInfo',
        components: {
            YwImport: () => import('src/components/common/yw-import.vue')
        },
        data() {
            return {
                display: {
                    isImport: false
                },
                importType: 'produce',
                multipleSelection: [],
                // concatData: [],
                currentPage: 1, // 当前页
                pageSize: 50, // 分页每页多少行数据
                pageSizes: [10, 20, 50, 100], // 每页多少行数组
                total: 0, // 总共多少行数据
                search_produce: '',
                search_type: '',
                search_name: '',
                search_phone: '',
                search_email: '',
                tableData: [],
                parent_biz: [],
                addDialog: false,
                loading: false,
                addForm: {
                    // id: '',
                    // produce: '',
                    // type: '',
                    // remark: '',
                    // logoUrl: '',
                    // concatData: []
                },
                headers: {
                    'head_isAdmin': true,
                    'head_orgAccount': 'alauda',
                    'head_userName': 'alauda',
                    Authorization: 'Bearer ' + sessionStorage.getItem('token')
                },
                addRules: {
                    produce: [
                        { required: true, message: '厂商必填', trigger: 'blur' }
                    ],
                    type: [
                        { required: true, message: '厂商类型必填', trigger: 'blur' }
                    ],
                    logoUrl: [
                        { required: true, message: '厂商图标', trigger: 'blur' }
                    ]
                },
                updateDialog: false,
                dialogInfo: {
                    show: false,
                    title: '',
                    type: ''
                },
                updateRules: {
                    produce: [
                        { required: true, message: '厂商必填', trigger: 'blur' }
                    ],
                    type: [
                        { required: true, message: '厂商类型必填', trigger: 'blur' }
                    ],
                    phone: [
                        { pattern: /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/, message: '电话格式不正确', trigger: ['blur', 'change'] }
                    ],
                    email: [
                        { pattern: /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/, message: '邮箱格式不正确', trigger: ['blur', 'change'] }
                    ]
                },
                concatRules: {
                    phone: [
                        { pattern: /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/, message: '电话格式不正确', trigger: ['blur', 'change'] }
                    ],
                    email: [
                        { pattern: /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/, message: '邮箱格式不正确', trigger: ['blur', 'change'] }
                    ]
                },
                dynamicModel: {},
                selectedObj: {},
                produceTypeList: [],
            }
        },
        mounted() {
            this.getProduceTypeList()
            this.search()
        },
        methods: {
            gotoDetail(row) {
                this.$router.push({
                    path: '/resource/maintenance/produceDetail',
                    query: { id: row }
                })
            },
            clickRow(row) {
                this.$refs.moviesTable.toggleRowSelection(row)
            },
            delData(index, list) {
                list.splice(index, 1)
            },
            showdeleteDialog() {
                if (this.multipleSelection.length < 1) {
                    this.$message.error('请至少选择一条数据进行删除')
                } else {
                    this.$confirm('此操作将删除厂商信息，确认删除吗？', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let ids = []
                        this.multipleSelection.forEach((item) => {
                            ids.push(item.id)
                        })
                        let param = { 'produceId': ids.toString() }
                        rbMainServiceFactory.deleteProduceById(param).then((res) => {
                            if (res.success) {
                                this.$message({
                                    message: res.message,
                                    type: 'success'
                                })
                                this.search('search')
                            } else {
                                this.$message.error(res.message)
                            }
                            return res
                        })

                    }).catch(() => {
                        // this.$message({
                        //   type: 'info',
                        //   message: '已取消删除'
                        // })
                    })
                }
            },
            // element相关方法 多选框
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            // 分页改变尺寸
            handleSizeChange(val) {
                this.pageSize = val
                this.search('page')
            },
            // 分页改变当前页
            handleCurrentChange(val) {
                this.currentPage = val
                this.search('page')
            },
            // 字典表查询厂商类型
            getProduceTypeList() {
                let obj = {
                    'type': 'produceType',
                    'pid': '',
                    'pValue': '',
                    'pType': ''
                }
                rbConfigDictServiceFactory.getDictsByType(obj).then((res) => {
                    this.produceTypeList = res
                    return res
                })
            },
            search(type) {
                this.loading = true
                this.concatData = []
                let param = {
                    'pageNum': type === 'search' ? 1 : this.currentPage,
                    'pageSize': this.pageSize,
                    'produce': this.search_produce,
                    'type': this.search_type,
                    'name': this.search_name,
                    'phone': this.search_phone,
                    'email': this.search_email
                }
                rbMainServiceFactory.listProduceByPage(param).then((res) => {
                    this.loading = false
                    this.total = res.count
                    this.tableData = res.data
                    return res
                }).finally(() => {
                    this.loading = false
                })

            },
            cancel(val) {
                if (val === 'add') {
                    this.dialogInfo.show = false
                    this.addForm = {}
                    // this.$refs['addForm'].resetFields();
                    // this.$refs['uploadLogo'].clearFiles();
                } else if (val === 'searchHander') {
                    this.search_produce = ''
                    this.search_type = ''
                    this.search_name = ''
                    this.search_phone = ''
                    this.search_email = ''
                }

                // this.search('search')
            },
            showAddDialog() {
                this.dialogInfo.show = true
                this.dialogInfo.type = 'add'
                this.dialogInfo.title = '新增厂商信息'
            },
            imgPreview(file) {
                let _t = this
                let fileName = file.name
                let regex = /(.jpg|.jpeg|.gif|.png|.bmp)$/
                if (regex.test(fileName.toLowerCase())) {
                    // this.logoUrl = file.url
                    _t.$set(_t.addForm, 'logoUrl', file.url)
                } else {
                    _t.$message.error('请选择图片文件')
                }
            },
            beforeAvatarUpload() {
            },
            saveAddForm(formName) {
                let _t = this
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        _t.$refs.uploadLogo.submit()
                        // _t.$refs.uploadLogo.clearFiles();
                    } else {
                        return false
                    }
                })
            },
            submitSuccess(resp) {
                if (resp.success) {
                    this.dialogInfo.show = false
                    this.addForm = {}
                    this.$message.success(resp.msg || resp.message)
                    this.search('search')
                } else {
                    this.$message.error(resp.msg || resp.message)
                }
            },
            submitError(err) {
                this.$refs['uploadLogo'].clearFiles()
                this.$message.error(err)
            },
            showUpdateCheck() {
                if (this.multipleSelection.length !== 1) {
                    this.$message.error('请选择一条数据进行修改')
                } else {
                    this.dialogInfo.show = true
                    this.dialogInfo.type = 'update'
                    this.dialogInfo.title = '编辑厂商信息'
                    let item = this.multipleSelection[0]
                    this.addForm = {
                        id: item.id,
                        produce: item.produce,
                        type: item.type,
                        logoUrl: item.logoUrl,
                        remark: item.remark
                    }
                }
            },
            p(key) {
                let _t = this
                if (_t.dynamicModel[key]['showSize'] + 1 <= _t.dynamicModel[key]['default'].length) {
                    _t.$set(_t.dynamicModel[key], 'showSize', _t.dynamicModel[key]['showSize'] + 1)
                }
            },
            s(key, value) {
                let _t = this
                if (_t.dynamicModel[key]['showSize'] - 1 > 0) {
                    _t.$set(_t.dynamicModel[key], 'showSize', _t.dynamicModel[key]['showSize'] - 1)
                    // 改下拉框已经选过值 则替换值
                    let indexInArray = this.findSelectTypeByRowIndex(key, value.split('_')[0])
                    if (indexInArray !== undefined) {
                        _t.dynamicModel[key]['select_type'].splice(indexInArray, 1) // 移除
                    }
                    _t.dynamicModel[key]['row_' + value.split('_')[0]] = ''
                    this.filter(key, value)
                }
            },
            exportData() {
                this.loading = true
                let params = {
                    'produce': this.search_produce,
                    'type': this.search_type,
                    'name': this.search_name,
                    'phone': this.search_phone,
                    'email': this.search_email
                }
                rbMainServiceFactory.exportProdceList(params).then((res) => {
                    this.loading = false
                    let blob = new Blob([res], { type: 'application/vnd.ms-excel' })
                    let objectUrl = URL.createObjectURL(blob)
                    // window.location.href = objectUrl
                    let downLoadElement = document.createElement('a')
                    downLoadElement.href = objectUrl
                    downLoadElement.download = '维保厂商信息列表.xls'
                    document.body.appendChild(downLoadElement)
                    downLoadElement.click()
                    document.body.removeChild(downLoadElement)
                    URL.revokeObjectURL(objectUrl)
                    return res
                }).finally(() => {
                    this.loading = false
                })
            },
            importData() {
                this.display.isImport = true
            },
            closeParent(val) {
                this.display.isImport = val
            }
        }
    }
</script>

<style lang="scss" scoped>
    /deep/ .yw-el-table .el-input__inner {
        width: 100px;
    }
    .form-left {
        display: inline-block;
        width: 100%;
    }
    .form-right {
        display: inline-block;
        vertical-align: top;
        width: 38%;
        margin-left: 1%;
        margin-top: 26px;
    }

    .avatar-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }
    .el-upload:hover {
        border-color: #409eff;
    }
    .avatar-uploader-icon {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
        color: #8c939d;
        width: 113px;
        height: 47px;
        line-height: 47px;
        text-align: center;
    }
    .avatar {
        width: 113px;
        height: 47px;
        display: block;
    }
</style>
