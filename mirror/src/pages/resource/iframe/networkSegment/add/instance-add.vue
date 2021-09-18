<template>
    <div :class="['detail'].indexOf(queryParams.state) < 0? 'components-container yw-dashboard' : ''">
        <!-- tab -->
        <section class="yw-tab-section">
            <el-tabs class="yw-tabs"
                     v-if="['detail'].indexOf(queryParams.state) < 0"
                     value="配置字段信息">
                <el-tab-pane label="配置字段信息"
                             name="配置字段信息">
                </el-tab-pane>
            </el-tabs>
            <el-form class="yw-tab-right yw-form">
                <el-button v-if="queryParams.state.indexOf('update') > -1"
                           @click="save">更新</el-button>
                <el-button v-if="queryParams.state.indexOf('add') > -1"
                           @click="save">保存</el-button>
            </el-form>
        </section>
        <!-- tab -->
        <!-- 内容 -->
        <el-collapse class="yw-dashboard-section"
                     v-model="activeCollapseNames">
            <el-collapse-item :name="index+1"
                              v-for="(group, index) in groupList"
                              :key="index">
                <template slot="title">
                    {{group.groupName}}
                </template>
                <el-form class="yw-form form-setting"
                         :inline="true"
                         :model="valueForm"
                         ref="form">
                    <template v-for="(code, codeIndex) in group.codeList">
                        <el-form-item :prop="code.filedCode"
                                      :required="code.isRequire === '是'"
                                      :rules="code.rules"
                                      v-if="!(code.codeSetting.display)"
                                      :key="codeIndex">
                            <!--配置项label-->
                            <el-tooltip :disabled="code.filedName.length <= 7"
                                        :content="code.filedName">
                                <span class="text-ellipse"
                                      style="width: 88px;">
                                    <i v-if="code.isRequire === '是'"
                                       style="color: red">*</i> {{code.filedName}}
                                </span>
                            </el-tooltip>

                            <!-- 详情 -->
                            <el-tooltip v-if="['detail'].indexOf(queryParams.state) > -1"
                                        :disabled="valueForm[code.filedCode] && valueForm[code.filedCode].length <= 16"
                                        :content="valueForm[code.filedCode] ? valueForm[code.filedCode].toString() : ''">
                                <span class="text-ellipse"
                                      style="width: 200px;">
                                    {{valueForm[code.filedCode]}}
                                </span>
                            </el-tooltip>
                            <!-- 详情 -->
                            <!-- 编辑、新增 -->
                            <div v-else
                                 style="display:inline-block;vertical-align:top;width: 178px">
                                <!--输入框-->
                                <el-input v-if="inputList.indexOf(code.controlType.controlCode) > -1"
                                          v-model="valueForm[code.filedCode]"
                                          clearable
                                          @blur="blurValids(code)"
                                          :disabled="disabledFields.indexOf(code.filedCode) >-1"
                                          :placeholder="'请输入' + code.filedName">
                                </el-input>
                                <!--输入框-->
                                <!--下拉框-->
                                <el-select v-if="selectList.indexOf(code.controlType.controlCode) > -1"
                                           v-model="valueForm[code.filedCode]"
                                           clearable
                                           filterable
                                           :disabled="disabledFields.indexOf(code.filedCode) >-1"
                                           :placeholder="'请选择' + code.filedName"
                                           @visible-change.once="initCascader(code)"
                                           @clear="changeCascader(code,true)"
                                           @change="changeCascader(code)">
                                    <el-option v-for="(item, index) in codeOptions[code.filedCode]"
                                               :key="index"
                                               :value="item.id"
                                               :label="item.value">
                                    </el-option>
                                </el-select>
                                <!--下拉框-->
                                <!--日期框-->
                                <el-date-picker v-model="valueForm[code.filedCode]"
                                                value-format="yyyy-MM-dd"
                                                clearable
                                                @blur="blurValids(code)"
                                                v-else-if="dateList.indexOf(code.controlType.controlCode) > -1">
                                </el-date-picker>
                                <!--日期框-->
                                <!-- 校验规则 -->
                                <el-tooltip v-if="valueValids[code.codeId] && valueValids[code.codeId].flag==='error'"
                                            :content="valueValids[code.codeId].msg">
                                    <p class="text-ellipse"
                                       style="font-size:12px;color:red;">{{valueValids[code.codeId].msg}}</p>
                                </el-tooltip>
                                <!-- 校验规则 -->
                            </div>
                            <!-- 编辑、新增 -->
                        </el-form-item>
                    </template>
                </el-form>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script>
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'

    export default {
        name: 'ResourceIframeAdd',
        mixins: [CommonOption],
        props: ['deviceInfo'],
        data() {
            return {
                // 活动面板
                activeCollapseNames: [1, 2, 3],
                // 参数
                queryParams: JSON.parse(this.$route.query.queryParams),
                // 不可修改字段
                disabledFields: ['device_class', 'device_type'],
                // 输入框
                inputList: ['ip', 'singleRowText', 'int'],
                // 下拉框
                selectList: ['listSel', 'cascader'],
                // 时间框
                dateList: ['dateTime'],
                // 字段
                groupList: [],
                // 字段值
                valueForm: {
                    // filedCode值:''
                },
                // 下拉框选项值
                codeOptions: {
                    // filedCode值:[]
                },
                // 被级联列表(用于区分自己数据、被级联数据)
                beCascadeObj: {
                    // filedCode值(被级联):级联code列表
                },
                // 校验规则
                valueValids: {
                    // codeId:{}
                },
            }
        },
        mounted() {
            this.init()
        },
        methods: {
            // blur校验
            blurValids(code) {
                let params = {

                }
                params[code.codeId] = this.valueForm[code.filedCode] || ''
                return rbCmdbModuleService.getCodeValids(params).then((data) => {
                    this.$set(this.valueValids, code.codeId, data[code.codeId])
                    return data
                })

            },
            // 校验规则
            getValids() {
                this.showFullScreenLoading({ text: '正在校验数据, 请稍等...' })
                let validOk = true
                let params = {

                }
                this.groupList.forEach((item) => {
                    item.codeList.forEach((code) => {
                        if (this.disabledFields.indexOf(code.filedCode) < 0 && !code.codeSetting.display) {
                            params[code.codeId] = this.valueForm[code.filedCode] || ''
                        }

                    })
                })

                return rbCmdbModuleService.getCodeValids(params).then((data) => {
                    this.valueValids = data
                    for (let key in this.valueValids) {
                        if (this.valueValids[key].flag === 'error') {
                            validOk = false
                            break
                        }
                    }
                    return validOk

                }).finally(() => {
                    this.closeFullScreenLoading()

                })
            },
            // 获得字段信息
            getFields() {
                this.showFullScreenLoading()
                let params = {
                    moduleId: this.queryParams.moduleId || ''
                }
                rbCmdbModuleService.getModuleDetail(params).then((data) => {
                    this.groupList = data.groupList || []
                    // 新增状态默认值
                    if (this.queryParams.state === 'add') {
                        this.groupList.forEach((groupItem) => {
                            groupItem.codeList.forEach((codeItem) => {
                                this.$set(this.valueForm, codeItem.filedCode, {
                                    filedCode: codeItem.filedCode,
                                    // style: { disabled: false },
                                    value: codeItem.defaultValue
                                })
                            })
                        })
                    }
                    this.getFieldsValues()
                    // 被级联filedCode列表
                    this.getBeCascadeObj()
                    // 下拉框
                    // this.getOptions();
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 获得字段值
            getFieldsValues() {
                this.showFullScreenLoading()
                // 新增状态
                if (!this.queryParams.instanceId) {
                    // this.queryParams['device_class'].id = this.queryParams['device_class'].name
                    // this.queryParams['device_type'].id = this.queryParams['device_type'].name
                    this.$set(this.codeOptions, 'device_class', [this.queryParams['device_class']])
                    this.$set(this.codeOptions, 'device_type', [this.queryParams['device_type']])
                    this.$set(this.valueForm, 'device_class', this.queryParams['device_class'].name)
                    this.$set(this.valueForm, 'device_type', this.queryParams['device_type'].name)
                    return false
                }

                let params = {
                    'instance_id': this.queryParams.instanceId,
                    'module_id': this.queryParams.moduleId,
                }
                rbCmdbServiceFactory.getFullInstance(params).then((data) => {
                    this.valueForm = data
                    this.deviceInfo.ip = data.ip
                    this.deviceInfo.idcType = data.idcType
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 数据字典
            getDictByType(item = {}) {
                let params = {
                    type: item.codeBindSource.dictSource
                }
                return rbCmdbServiceFactory.getDictsByType(params).then((data) => {
                    this.$set(this.codeOptions, item.filedCode, data)
                    return data
                })
            },
            // 数据表
            getDictBySql(filedCode = null, sql = '') {

                let params = {
                    sql: sql
                }
                return rbCmdbServiceFactory.getDictsBySql(params).then((data) => {
                    this.$set(this.codeOptions, filedCode, data)
                    return data
                })
            },

            // 下拉框数据
            async initCascader(item = {}) {
                // return false;
                // 如果是被级联数据,走被级联数据
                if (this.beCascadeObj[item.filedCode]) {
                    this.changeCascader(this.beCascadeObj[item.filedCode])
                    return false
                }

                // 自己数据
                switch (item.codeBindSource.bindSourceType) {
                    case '数据字典':
                        await this.getDictByType(item)
                        break
                    case '数据表':
                        await this.getDictBySql(item.filedCode, item.codeBindSource.tableSql)
                        break
                }

            },
            // 下拉框数据选择切换
            changeCascader(item = {}, clear = false) {

                this.blurValids(item)
                // 非级联
                if (item.controlType.controlCode !== 'cascader') {
                    return false
                }

                // 当前下拉框选中的id
                let currentId = ''
                let value = this.valueForm[item.filedCode]
                this.codeOptions[item.filedCode].some((sub) => {
                    // if ((value === sub.key || value === sub.value || value === sub.name) && !clear) {
                    //     currentId = sub.id
                    //     return true
                    // }
                    if ((value === sub.id) && !clear) {
                        currentId = sub.id
                        return true
                    }
                })

                // 被级联查询
                item.cascadeList.forEach((code) => {
                    if (code.subFiledCode && code.sqlString) {

                        this.$set(this.valueForm, code.subFiledCode, '')
                        if (currentId) {
                            let sqlString = code.sqlString.replace(/\?/g, currentId)
                            this.getDictBySql(code.subFiledCode, sqlString)
                        } else {
                            this.$set(this.valueForm, code.subFiledCode, '')
                            this.$set(this.codeOptions, code.subFiledCode, [])
                        }

                    }
                })
            },

            // 获得下拉框值
            getOptions() {
                let promiseList = []
                this.groupList.forEach((codeItem) => {
                    codeItem.codeList.forEach((item) => {
                        if (this.selectList.indexOf(item.controlType.controlCode) > -1 && item.isBindSource.trim() === '是') {
                            if (item.codeBindSource) {
                                // 自己数据
                                switch (item.codeBindSource.bindSourceType) {
                                    case '数据字典':
                                        promiseList.push(this.getDictByType(item))
                                        break
                                    case '数据表':
                                        promiseList.push(this.getDictBySql(item.filedCode, item.codeBindSource.tableSql))
                                        break
                                }
                                // 被级联数据
                                // if (item.controlType.controlCode === 'cascader') {
                                //   this.changeCascader(item)
                                // }
                            }

                        }
                    })
                })
                Promise.all(promiseList).then(() => {

                })
            },

            // 新增
            add() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let params = Object.assign(this.valueForm, { 'module_id': this.queryParams.moduleId })
                rbCmdbServiceFactory.addInstance(params).then((data) => {
                    if (data.success) {
                        this.$message.success(data.message)
                        this.$router.push({ path: this.queryParams.originRouter })
                        this.$emit('closeTab', this.$route.fullPath)
                    } else {
                        this.$message.error(data.message)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 编辑
            update() {
                this.showFullScreenLoading({ text: '正在更新数据, 请稍等...' })
                let params = Object.assign(this.valueForm, { 'module_id': this.queryParams.moduleId })
                rbCmdbServiceFactory.updateInstance(params, this.queryParams.instanceId).then((data) => {
                    if (data.success) {
                        this.$message.success(data.message)
                        this.$router.push({ path: this.queryParams.originRouter })
                        this.$emit('closeTab', this.$route.fullPath)
                    } else {
                        this.$message.error(data.message)
                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 保存
            async save() {
                let validResult = await this.getValids()
                if (!validResult) {
                    this.$confirm('字段值不符合规范,请检查后再提交', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return false
                }
                this.$confirm('确定提交吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    if (['add'].indexOf(this.queryParams.state) > -1) {
                        this.add()
                    } else if (['update'].indexOf(this.queryParams.state) > -1) {
                        this.update()
                    }

                }).catch(() => {
                })
            },

            // 跳转到设备详情
            toDetail() {
                let queryParams = {
                    instanceId: this.queryParams.instanceId,
                    moduleId: this.queryParams.moduleId,
                    state: 'detail'
                }
                queryParams = JSON.stringify(queryParams)
                this.$router.push({ path: '/resource/iframe/detail', query: { queryParams: queryParams } })
                this.$emit('closeTab', this.$route.fullPath)
            },
            // 获得被级联fileCode
            getBeCascadeObj() {
                this.groupList.forEach((codeItem) => {
                    codeItem.codeList.forEach((item) => {
                        item.cascadeList.forEach((code) => {
                            if (code.subFiledCode) {
                                this.$set(this.beCascadeObj, code.subFiledCode, item)
                            }

                        })
                    })
                })
            },

            async init() {
                this.getFields()
                this.getFieldsValues()
            },
        },

    }
</script>

<style lang="scss" scoped>
    /deep/ .form-setting {
        .el-form-item {
            width: 32%;
        }
    }
</style>
