<template>
    <div>
        <el-form class="yw-form is-required"
                 ref="yumForm"
                 :model="addForm"
                 label-width="100px">
            <el-form-item label="资源池名称"
                          prop="poolName">
                <YwCodeFrame ref="poolSelect"
                             :frameDatas="frameDatas"
                             :frameOptions="frameDatas.frameOptions"
                             @changeSelect="changeSelect"></YwCodeFrame>
            </el-form-item>
            <el-form-item label="报告类型"
                          prop="name">
                <el-select v-model="addForm.reportType"
                           filterable>
                    <el-option v-for="item in reportTypeList"
                               :key="item.label"
                               :label="item.value"
                               :value="item.label"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="扫描周期"
                          prop="scanCycle">
                <el-select v-model="addForm.scanCycle"
                           filterable>
                    <el-option v-for="item in scanCycleList"
                               :key="item.scanCycle"
                               :label="item.scanCycle"
                               :value="item.scanCycle"></el-option>
                </el-select>
                <el-button size="small"
                           type="primary"
                           @click="addDate = true">新增</el-button>

            </el-form-item>

            <el-form-item label="报告文件"
                          prop="filename">
                <el-input class="middle-size-input"
                          v-model="filename"
                          placeholder="上传报告文件"
                          readonly></el-input>
                <el-upload class="upload-demo"
                           action=""
                           :show-file-list="false"
                           :http-request="UploadFile">
                    <el-button size="small"
                               type="primary">上传</el-button>
                </el-upload>
            </el-form-item>
        </el-form>
        <el-dialog width="50%"
                   class="yw-dialog"
                   title="新增漏洞扫描周期"
                   :visible.sync="addDate"
                   v-if="addDate"
                   :append-to-body="true"
                   :destroy-on-close="true"
                   :close-on-click-modal="false">
            <section class="yw-dialog-main">
                <el-form class="yw-form">
                    <el-form-item label="漏洞扫描周期："
                                  label-width="100px">
                        <el-input v-model="scanCycleVal"
                                  placeholder="请输入漏洞扫描周期"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="confirmSubmit">确定
                </el-button>
                <el-button @click="hideediting">取消</el-button>
            </section>
        </el-dialog>
    </div>

</template>

<script>
    import bugManageService from 'src/services/auto_operation/rb-auto-operation-bug-manage-services.js'
    import YwCodeFrame from 'src/components/common/yw-code-frame/yw-code-frame.vue'
    import { validFile } from 'src/utils/utils.js'
    export default {
        name: 'AutoOperationCodeStatusList',
        components: {
            YwCodeFrame
        },
        data() {
            return {
                addDate: false,
                scanCycleList: [],
                filename: '',
                poolList: [],
                scanCycleVal: '',
                addForm: {
                    poolName: '',
                    reportType: '',
                    scanCycle: ''
                },
                reportTypeList: [
                    {
                        label: 'lvmeng',
                        value: '绿盟'
                    },
                    {
                        label: 'qiming',
                        value: '启明星辰'
                    },
                ],
                frameDatas: {
                    codeObj: {
                        filedCode: 'idcType',
                    },
                    frameOptions: {
                        type: 'select',
                        clearable: false
                    }
                },
            }
        },
        created() {
            this.addForm.reportType = this.reportTypeList[0].label
            this.getScanCycleList()
        },
        methods: {
            hideediting() {
                this.addDate = false
            },
            // 获取扫描周期列表
            getScanCycleList() {
                bugManageService
                    .getScanCycleList()
                    .then(res => {
                        this.scanCycleList = res
                        this.addForm.scanCycle = res[0].scanCycle
                        this.$store.commit('setScanCycleList', res)
                    })
            },
            confirmSubmit() {
                bugManageService.saveVulScanCycle({ scanCycle: this.scanCycleVal }).then(res => {
                    if (res.flag === true) {
                        this.$message.success('添加成功')
                        this.getScanCycleList()
                        this.addForm.scanCycle = this.scanCycleVal
                        this.hideediting()
                    }
                })
            },
            // 下拉框选中事件
            // eslint-disable-next-line no-unused-vars
            changeSelect(frameDatas, frameOptions, select, codeObj) {
                if (typeof select === 'object') {
                    this.addForm.poolName = select.value
                }
            },
            UploadFile(param) {
                const formData = new FormData()
                // 条件组件未完善，暂时用该方式获取第一项默认值
                const defaultPool = this.$refs.poolSelect.selectList[0].value
                console.log('param.file', param.file)
                this.filename = param.file.name
                if (!validFile(this, param.file, 'zip')) {
                    return
                }
                this.$message('请稍候')
                formData.append('file', param.file)
                formData.append('idcType', this.addForm.poolName || defaultPool)
                formData.append('reportType', this.addForm.reportType)
                formData.append('scanCycle', this.addForm.scanCycle)
                let obj = {
                    params: {},

                }
                obj.payload = param.file
                obj.params.idcType = this.addForm.poolName || defaultPool
                obj.params.reportType = this.addForm.reportType
                obj.params.scanCycle = this.addForm.scanCycle
                console.log('formData', formData)
                // {
                //         params: {
                //             file: param.file,
                //             idcType: this.addForm.poolName || defaultPool,
                //             reportType: this.addForm.reportType,
                //             scanCycle: this.addForm.scanCycle
                //         }
                //     }
                console.log('obj', obj)
                bugManageService
                    .importVulnerabilityReport(formData)
                    .then(res => {
                        if (res.flag) {
                            this.$message.success(res.error_tip)
                            this.$emit('uploadSuccess')
                        } else {
                            this.$message.error(res.error_tip)
                        }
                    })
                    .catch(err => {
                        this.$message.error(err.data)
                    })
            },
        }
    }
</script>
