<template>
    <div class="wp100"
         v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <div class="wp100 displaybox">
            <div class="content-right boxflex01 p10">
                <div class="wp100">
                    <el-form class="components-condition yw-form"
                             :model="formSearch"
                             @keyup.enter.native="search(1)"
                             ref="formSearch"
                             :inline="true"
                             label-width="65px">
                        <el-form-item label="文件类型"
                                      prop="fileType">
                            <el-select v-model="formSearch.fileType"
                                       placeholder="请选择文件类型"
                                       filterable
                                       clearable>
                                <el-option v-for="val in fileTypeList"
                                           :key="val.id"
                                           :label="val.name"
                                           :value="val.name"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="文件名称"
                                      prop="fileName">
                            <el-input v-model="formSearch.fileName"
                                      placeholder="请输入文件名称"
                                      clearable></el-input>
                        </el-form-item>
                        <section class="btn-wrap">
                            <el-button type="primary"
                                       @click="search(1)">查询</el-button>
                            <el-button @click="reset()">重置</el-button>
                        </section>
                    </el-form>
                </div>

                <div class="table-operate-wrap clearfix"
                     style="margin:10px 0">
                    <el-button type="text"
                               icon="el-icon-plus"
                               @click="showBox('add')">新增</el-button>
                </div>
                <div class="yw-el-table-wrap">
                    <el-table :data="dataList"
                              class="yw-el-table"
                              :header-cell-style="{background:'#E8F0FC',color:'#3A4154',height:'19px'}"
                              stripe
                              tooltip-effect="dark"
                              border
                              height="calc(100vh - 275px)"
                              v-loading="loading">
                        <el-table-column sortable
                                         prop="fileName"
                                         label="文件名称"
                                         min-width="100"></el-table-column>
                        <el-table-column sortable
                                         prop="fileType"
                                         label="文件类型"
                                         min-width="80"></el-table-column>
                        <el-table-column sortable
                                         prop="fileObject"
                                         label="文件对象"
                                         min-width="200"></el-table-column>
                        <el-table-column sortable
                                         prop="fileNameAlias"
                                         label="文件别名"
                                         min-width="100"></el-table-column>
                        <el-table-column label="操作"
                                         width="180">
                            <template slot-scope="scope">

                                <div class="table-operate-wrap">
                                    <el-button type="text"
                                               icon="el-icon-download"
                                               @click="download(scope.row)">下载</el-button>
                                    <el-button type="text"
                                               icon="el-icon-edit"
                                               @click="showBox('edit', scope.row)">修改</el-button>
                                    <el-button type="text"
                                               icon="el-icon-delete"
                                               @click="remove(scope.row.id)">删除</el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <div class="yw-page-wrap">
                    <el-pagination @size-change="handleSizeChange"
                                   @current-change="handleCurrentChange"
                                   :current-page="currentPage"
                                   :page-sizes="pageSizes"
                                   :page-size="pageSize"
                                   :total="total"
                                   layout="total, sizes, prev, pager, next, jumper"></el-pagination>
                </div>
            </div>
        </div>

        <!-- 新建、编辑文件记录 -->
        <el-dialog class="yw-dialog"
                   :title="dialogName"
                   :visible.sync="boxShow"
                   width="500">
            <section class="yw-dialog-main middle-size-form">
                <el-form class="yw-form is-required"
                         ref="fileForm"
                         :model="submitInfo"
                         :rules="formRules"
                         label-width="100px">
                    <el-form-item label="文件类型"
                                  prop="fileType">
                        <el-select v-model="submitInfo.fileTypeIndex"
                                   placeholder="请选择文件类型"
                                   filterable
                                   clearable>
                            <el-option v-for="(val,index) in fileTypeList"
                                       :key="val.id"
                                       :label="val.name"
                                       :value="index"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文件对象"
                                  prop="fileObject">
                        <el-select v-model="submitInfo.fileObject"
                                   placeholder="请选择文件对象"
                                   filterable
                                   clearable>
                            <el-option v-for="val in fileObjectList"
                                       :key="val.id"
                                       :label="val.name"
                                       :value="val.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="文件别名"
                                  prop="fileNameAlias">
                        <el-input v-model="submitInfo.fileNameAlias"
                                  placeholder="请输入文件别名"
                                  :readonly="isReadonly"
                                  :clearable="!isReadonly"></el-input>
                    </el-form-item>
                    <el-form-item label="文件"
                                  prop="fileName">
                        <el-input class="middle-size-input"
                                  v-model="submitInfo.fileName"
                                  placeholder="请选取文件"
                                  readonly></el-input>
                        <el-upload v-show="!isReadonly"
                                   class="upload-demo"
                                   :show-file-list="false"
                                   :http-request="UploadFile">
                            <el-button v-if="eventType === 'add'"
                                       size="small"
                                       type="primary">选取文件</el-button>
                        </el-upload>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap"
                     v-show="!isReadonly">
                <el-button type="primary"
                           @click="save()">保存</el-button>
                <el-button @click="addCancel()">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbDocManageServicesFactory from 'src/services/doc_manage/rb-doc-manage-services.factory.js'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    export default {
        name: 'DocManageList',
        components: {},
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                fileList: [],
                // yum源文件
                eventType: 'add', // yum触发事件
                currentData: {},
                submitInfo: {
                    fileName: '',
                    fileTypeIndex: '',
                    fileType: '',
                    fileObject: '',
                    fileNameAlias: '',
                    fileData: ''
                },
                formRules: {
                    fileName: [
                        {
                            required: true,
                            message: '请上传文件!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    fileType: [
                        {
                            required: true,
                            message: '请选择文件类型!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    fileObject: [
                        {
                            required: true,
                            message: '请选择文件对象!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    fileNameAlias: [
                        {
                            required: true,
                            message: '请输入文件别名!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 50,
                            message: '长度在 2 到 50 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },
                boxShow: false,
                dialogName: '新增文件记录',
                defaultProps: {
                    expandTrigger: 'hover',
                    children: 'children',
                    label: 'groupName',
                    value: 'groupId',
                    checkStrictly: true
                },

                filterText: '',
                defaultId: 1000,
                treeData: [],

                // 文件列表
                dataList: [],

                // 查询条件
                formSearch: {
                    fileType: '',
                    fileName: '',
                },
                // 文件类型
                fileTypeList: [],
                // 文件对象
                fileObjectList: [],
            }
        },
        mixins: [rbAutoOperationMixin],
        mounted() {
            this.search()
            this.getDictsByType()
        },
        watch: {
            'submitInfo.fileTypeIndex'(val) {
                if (this.fileTypeList[val]) {
                    this.submitInfo.fileType = this.fileTypeList[val].name
                    this.queryFileObj()
                } else {
                    this.submitInfo.fileType = ''
                    this.submitInfo.fileObject = ''
                    this.fileObjectList = []
                }
            }
        },
        computed: {
            isReadonly() {
                return this.eventType === 'viewDetail'
            }
        },
        methods: {
            // 上传yum源文件
            UploadFile(param) {
                this.submitInfo.fileName = param.file.name
                this.submitInfo.fileData = param.file
            },
            // 下载源文件
            download(row) {
                this.pageLoading = true
                rbDocManageServicesFactory.downloadFile(row.id).then(res => {
                    this.$message.success('下载完成')
                    this.createDownloadFile(res, row.fileName)
                    this.pageLoading = false
                })
            },
            createDownloadFile(res, filename) {
                var data = new Blob([res])
                let url = window.URL.createObjectURL(data)

                let element = document.createElement('a')
                element.setAttribute('href', url)
                element.setAttribute('download', filename)
                element.style.display = 'none'
                document.body.appendChild(element)
                element.click()
                document.body.removeChild(element)
            },
            // 提交文件
            saveFile(req, type) {
                this.pageLoading = true
                rbDocManageServicesFactory[type](req).then(res => {
                    if (res.flag === 'success') {
                        this.$message.success('保存成功')
                        this.search()
                        this.closeBox()
                        this.pageLoading = false
                    } else {
                        this.$message.error(res.error_tip)
                        this.pageLoading = false
                    }
                })
            },
            // 保存文件
            save() {
                this.$refs.fileForm.validate(valid => {
                    if (!valid) {
                        // this.$message("请先完善信息");
                        return
                    }
                    let req = this.submitInfo
                    if (this.eventType === 'edit') {
                        req.id = this.currentData.id
                        this.saveFile(req, 'updateFile')
                    } else {
                        this.saveFile(req, 'saveFile')
                    }
                })
            },
            // 删除文件
            remove(id) {
                this.$confirm('您确定要删除该文件吗？').then(() => {
                    this.loading = true
                    rbDocManageServicesFactory.deleteFile({ id: id }).then(res => {
                        if (res.flag === 'success') {
                            this.$message.success('删除成功')
                            this.search()
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                })
            },
            handleFileTypeIndex(fileType) {
                let handleFileTypeIndex
                this.fileTypeList.forEach((item, index) => {
                    if (item.name === fileType) {
                        handleFileTypeIndex = index
                    }
                })
                return handleFileTypeIndex
            },
            getFileObjectId(fileObjectName) {
                let fileObjectId
                this.fileObjectList.forEach((item) => {
                    if (item.name === fileObjectName) {
                        fileObjectId = item.id
                    }
                })
                return fileObjectId
            },
            // 查看文件
            viewDetail(row) {
                if (JSON.stringify(row) !== '{}') {
                    this.submitInfo = {
                        fileName: row.fileName,
                        fileType: row.fileType,
                        fileTypeIndex: this.handleFileTypeIndex(row.fileType),
                        fileObject: this.getFileObjectId(row.fileObject),
                        fileNameAlias: row.fileNameAlias
                    }
                } else {
                    this.submitInfo = {
                        fileName: '',
                        fileTypeIndex: '',
                        fileType: '',
                        fileObject: '',
                        fileNameAlias: '',
                        fileData: ''
                    }
                }
            },
            // 新建文件
            showBox(type, row) {
                this.eventType = type
                if (row) {
                    this.currentData = row
                } else {
                    this.currentData = {}
                }
                if (type === 'add') {
                    this.dialogName = '新增文件记录'
                    this.viewDetail(this.currentData)
                } else if (type === 'edit') {
                    this.dialogName = '编辑文件记录'
                    this.viewDetail(this.currentData)
                }
                this.boxShow = true
            },
            closeBox() {
                this.boxShow = false
            },
            gotoDocManage() {
                this.$router.push({
                    path: 'docManage'
                })
            },
            // 关闭弹框
            addCancel() {
                this.boxShow = false
            },

            // 查询文件列表
            search(pageNum) {
                this.currentPage = pageNum ? pageNum : this.currentPage
                let req = {
                    pageNo: this.currentPage,
                    pageSize: this.pageSize,
                }
                req = Object.assign(req, this.formSearch)
                this.loading = true
                rbDocManageServicesFactory
                    .queryFileList(req)
                    .then(res => {
                        this.loading = false
                        this.total = res.totalSize
                        this.dataList = res.data
                    })
                    .catch(() => {
                        this.loading = false
                    })
            },
            // 获取文档类型
            getDictsByType() {
                let req = {
                    type: 'file_object_type'
                }
                rbDocManageServicesFactory
                    .getDictsByType(req)
                    .then(res => {
                        this.fileTypeList = res
                    })
            },
            // 获取文档对象
            queryFileObj() {
                let req = {
                    fileType: this.fileTypeList[this.submitInfo.fileTypeIndex].value
                }
                rbDocManageServicesFactory
                    .queryFileObj(req)
                    .then(res => {
                        this.fileObjectList = res
                    })
            },
            reset() {
                this.$refs['formSearch'].resetFields()
            },

        }
    }
</script>

<style lang="scss" scoped>
    @import "../../auto_operation/assets/global.scss";
</style>
