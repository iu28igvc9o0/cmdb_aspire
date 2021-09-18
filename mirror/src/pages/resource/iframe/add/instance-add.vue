<template>
    <!-- 可读状态来源：新增的addReadOnly,编辑的updateReadOnly,数据切换的style.disabled(此会覆盖前面2个) -->
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
            <el-form class="yw-tab-right"
                     style="bottom:15px"
                     v-if="queryParams.state.indexOf('update') > -1 || queryParams.state.indexOf('add') > -1 
                     || queryParams.state.indexOf('unknown') > -1">
                <el-button v-if="queryParams.state.indexOf('update') > -1"
                           @click="save">更新</el-button>
                <el-button v-if="this.queryParams.state === 'add' || this.queryParams.state === 'unknown'"
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
                <el-form class="yw-form form-setting clearfix"
                         :inline="true"
                         :model="valueForm"
                         ref="form">
                    <template v-for="(code, codeIndex) in group.codeList">
                        <el-form-item :prop="code.filedCode"
                                      :style="{width:code.displayStyle ?  Math.floor(100/Number(code.displayStyle))+'%' : '25%'}"
                                      class="fl"
                                      :required="code.isRequire === '是'"
                                      :rules="code.rules"
                                      v-if="!(code.codeSetting.display) && valueForm[code.filedCode]"
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
                                        :disabled="valueForm[code.filedCode].value && valueForm[code.filedCode].value.length <= 16"
                                        :content="valueForm[code.filedCode].value ?  refModuleCode[code.filedCode] ? valueForm[refModuleCode[code.filedCode]].value : valueForm[code.filedCode].value : ''">
                                <span class="text-ellipse"
                                      style="width: 200px;">
                                    {{ refModuleCode[code.filedCode] ? valueForm[refModuleCode[code.filedCode]].value : valueForm[code.filedCode].value}}
                                </span>
                            </el-tooltip>
                            <!-- 详情 -->
                            <!-- 编辑、新增 -->
                            <div v-else
                                 style="display:inline-block;vertical-align:top;width: 178px">
                                <!--输入框-->
                                <el-input v-if="inputList.indexOf(code.controlType.controlCode) > -1"
                                          v-model="valueForm[code.filedCode].value"
                                          clearable
                                          @blur="changeInput(code)"
                                          :disabled="handleDisabled(code)"
                                          :placeholder="'请输入' + code.filedName">
                                </el-input>
                                <!--输入框-->
                                <!--下拉框-->
                                <el-select v-if="selectList.indexOf(code.controlType.controlCode) > -1"
                                           v-model="valueForm[code.filedCode].value"
                                           clearable
                                           filterable
                                           :disabled="handleDisabled(code)"
                                           :placeholder="'请选择' + code.filedName"
                                           @clear="changeCascader(code,true,false)"
                                           @change="changeCascader(code,false,false)">
                                    <el-option v-for="(item, index) in codeOptions[code.filedCode]"
                                               :key="index"
                                               :value="item.id"
                                               :label="item.value">
                                    </el-option>
                                </el-select>
                                <!--下拉框-->
                                <!--日期框-->
                                <el-date-picker v-model="valueForm[code.filedCode].value"
                                                value-format="yyyy-MM-dd"
                                                clearable
                                                :disabled="handleDisabled(code)"
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
    import rbConfigDictService from 'src/services/cmdb/rb-configDict-service.factory'
    import rbCmdbModuleService from 'src/services/cmdb/rb-cmdb-module-service.factory.js'
    import rbCmdbServiceFactory from 'src/services/cmdb/rb-cmdb-service.factory.js'
    import CommonOption from 'src/utils/commonOption.js'

    export default {
        name: 'ResourceIframeAdd',
        mixins: [CommonOption],
        data() {
            return {
                // 活动面板
                activeCollapseNames: [1, 2, 3],
                // 引用模型字段
                refModuleCode: {},
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
                    // filedCode值: {
                    //    filedCode: '',
                    //    style: { disabled: true },
                    //    value: ''
                    // }
                },

                // 关联事件列表
                eventList: {
                    // codeId: [],
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
            handleDisabled(code) {
                return Boolean(this.valueForm[code.filedCode] && this.valueForm[code.filedCode].style ? this.valueForm[code.filedCode].style.disabled : (this.queryParams.state === 'add' && code.addReadOnly) || (this.queryParams.state === 'update' && code.updateReadOnly) || this.disabledFields.indexOf(code.filedCode) > -1)
            },
            // 关联事件赋值
            setEventDatas(item = {}, currentObj = {}) {
                if (this.eventList[item.codeId]) {
                    let eventType = ''
                    this.eventList[item.codeId].some((item) => {
                        // change事件
                        if (item.eventTypeName === 'code_change') {
                            eventType = item.eventType
                            return true
                        }
                    })

                    this.getCodeEventData({ moduleId: this.queryParams.moduleId, codeId: item.codeId, eventType: eventType }, currentObj)
                }
            },
            // 获取配置项事件
            getHaveEventCodeList() {
                this.showFullScreenLoading()
                let params = {
                    moduleId: this.queryParams.moduleId,
                }
                rbConfigDictService.getHaveEventCodeList(params).then((data) => {

                    this.eventList = data

                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 获取配置项事件
            getModuleColumns() {
                let params = {
                    moduleId: this.queryParams.moduleId,
                }
                rbCmdbModuleService.getModuleColumns(params).then((data) => {

                    for (let key in data) {
                        if (data[key].type === 'ref') {
                            this.$set(this.refModuleCode, data[key].filed_code, data[key].ref_name)
                        }
                    }
                    // data.forEach(item => {
                    //     if (item.type === 'ref') {
                    //         this.$set(this.refModuleCode, item.filed_code, item.ref_name)
                    //     }
                    // })

                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 获取配置项事件回调赋值
            getCodeEventData(params = {}, data = {}) {
                this.showFullScreenLoading()
                // let params = {
                //     moduleId: this.queryParams.moduleId,
                //     codeId: obj.codeId,
                //     selectId: obj.selectId,
                //     eventType: obj.eventType,
                // }

                rbConfigDictService.getCodeEventData(params, data).then((res) => {
                    if (res.resultData) {
                        res.resultData.forEach((item) => {
                            this.$set(this.valueForm, item.filedCode, item)
                        })

                    }
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 输入框改变
            changeInput(item = {}) {
                // 校验
                this.blurValids(item)

                // 关联事件
                this.setEventDatas(item, { value: this.valueForm[item.filedCode].value })
            },
            // blur校验
            blurValids(code) {
                let params = {

                }
                params[code.codeId] = this.valueForm[code.filedCode] && this.valueForm[code.filedCode].value || ''
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
                        // code.filedCode = 'test222'
                        if (this.disabledFields.indexOf(code.filedCode) < 0 && !code.codeSetting.display) {
                            if (!this.valueForm[code.filedCode]) {
                                this.$confirm(`filedCode:"${code.filedCode}"字段，配置错误，请检查该字段!`, '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }).then(() => {

                                })
                                validOk = false
                                this.closeFullScreenLoading()

                            } else {
                                params[code.codeId] = this.valueForm[code.filedCode] && this.valueForm[code.filedCode].value || ''
                            }

                        }

                    })
                })

                if (!validOk) {
                    return false
                }

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
                return rbCmdbModuleService.getModuleDetail(params).then((data) => {
                    this.groupList = data.groupList || []
                    // 新增状态默认值
                    let state = this.queryParams.state
                    if (state === 'add' || state === 'unknown') {
                        this.groupList.forEach((groupItem) => {
                            groupItem.codeList.forEach((codeItem) => {
                                this.$set(this.valueForm, codeItem.filedCode, {
                                    filedCode: codeItem.filedCode,
                                    // style: { disabled: false },
                                    value: state === 'unknown' && this.valueForm[codeItem.filedCode] ? this.valueForm[codeItem.filedCode] : codeItem.defaultValue
                                })
                            })
                        })
                    }

                    return data
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
                    this.$set(this.valueForm, 'device_class', { value: this.queryParams['device_class'].name })
                    this.$set(this.valueForm, 'device_type', { value: this.queryParams['device_type'].name })
                    this.$set(this.valueForm, 'ip', { value: this.queryParams['ip'] })
                    this.$set(this.valueForm, 'device_name', { value: this.queryParams['device_name'] })
                    this.$set(this.valueForm, 'idcType', { value: this.queryParams['idcType'] })


                    return false
                }


                let params = {
                    'instance_id': this.queryParams.instanceId,
                    'module_id': this.queryParams.moduleId,
                }
                return rbCmdbServiceFactory.getFullInstance(params).then((data) => {
                    for (let keyItem in data) {
                        this.$set(this.valueForm, keyItem, {
                            filedCode: keyItem,
                            // style: { disabled: false },
                            value: data[keyItem]
                        })
                    }
                    return data
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 设备类型、设备三级分类单独查询
            setOptionBy(filedCode) {
                // 查询设备类型、设备三级分类
                let result = false
                this.groupList.some((codeItem) => {
                    codeItem.codeList.some((item) => {
                        if (filedCode === item.filedCode) {
                            result = true
                            this.changeCascader(item, false, true)
                            return true
                        } else {
                            return false
                        }
                    })
                    return result
                })
            },
            // codeOptions
            setCodeOptions(filedCode, data) {

                if (!Object.keys(this.codeOptions).includes(filedCode)) {
                    this.$set(this.codeOptions, filedCode, data)
                } else {
                    this.codeOptions[filedCode] = data
                }

                // 设备分类、设备类型
                if (['device_class', 'device_type', 'idcType'].indexOf(filedCode) > -1) {

                    let initValue = this.valueForm[filedCode].value
                    this.codeOptions[filedCode].some((item) => {
                        if (item.id === initValue || item.name === initValue || item.value === initValue) {
                            this.valueForm[filedCode].value = item.id
                            return true
                        } else {
                            return false
                        }
                    })

                    this.setOptionBy(filedCode)

                }


            },
            // 数据字典
            getDictByType(item = {}) {
                let params = {
                    type: item.codeBindSource.dictSource
                }
                return rbCmdbServiceFactory.getDictsByType(params).then((data) => {
                    this.setCodeOptions(item.filedCode, data)
                    return data
                })
            },
            // 数据表
            getDictBySql(filedCode = null, sql = '') {

                let params = {
                    sql: sql
                }
                return rbCmdbServiceFactory.getDictsBySql(params).then((data) => {
                    this.setCodeOptions(filedCode, data)
                    return data
                })
            },
            // 引用模型
            getRefModuleDict(code) {
                let filedCode = code.filedCode
                let params = {
                    codeId: code.codeId
                }
                return rbCmdbModuleService.getRefModuleDict({ params: params }).then((data) => {
                    this.setCodeOptions(filedCode, data)
                    return data
                })
            },

            // 下拉框数据
            initCascader(item = {}) {

                // 如果是被级联数据,走被级联数据
                if (this.beCascadeObj[item.filedCode]) {
                    this.changeCascader(this.beCascadeObj[item.filedCode], false, true)
                    return false
                }

                // 自己数据
                switch (item.codeBindSource && item.codeBindSource.bindSourceType) {
                    case '数据字典':
                        this.getDictByType(item)
                        break
                    case '数据表':
                        this.getDictBySql(item.filedCode, item.codeBindSource.tableSql)
                        break
                    case '引用模型':
                        this.getRefModuleDict(item)
                        break
                }

            },
            // 下拉框数据选择切换(当前code、是否清空、是否初始化)
            changeCascader(item = {}, clear = false, init = false) {
                // 1.判断是否绑定数据源（isBindSource）
                // 2.值为否则无数据不需要做任何操作
                // if (item.isBindSource === '否') {
                //     return
                // }

                this.blurValids(item)

                // 当前下拉框选中的id、对象
                let currentId = ''
                let currentObj = {}
                let value = this.valueForm[item.filedCode].value
                this.codeOptions[item.filedCode] && this.codeOptions[item.filedCode].some((sub) => {
                    // if ((value === sub.key || value === sub.value || value === sub.name) && !clear) {
                    //     currentId = sub.id
                    //     currentObj = sub
                    //     return true
                    // }
                    if ((value === sub.id || value === sub.name) && !clear) {
                        currentId = sub.id
                        currentObj = sub
                        return true
                    }
                })

                // 级联查询
                if (item.controlType.controlCode === 'cascader') {
                    item.cascadeList.forEach((code) => {
                        if (code.subFiledCode && code.sqlString) {
                            // let that45 = this
                            if (!init) {
                                this.valueForm[code.subFiledCode].value = ''
                            }

                            // 选择资源池，同时清空机房位置、机柜
                            // if (code.subFiledCode === 'pod_name') {
                            //     this.valueForm['roomId'].value = ''
                            //     this.valueForm['idc_cabinet'].value = ''
                            // }
                            // 选择pod池，同时清空机房位置、机柜
                            // if (code.subFiledCode === 'roomId') {
                            //     this.valueForm['idc_cabinet'].value = ''
                            // }

                            let sqlString = code.sqlString.replace(/\?/g, currentId || value)
                            // 没有数据或非初始化触发的情况下，查询getDictBySql
                            // if (!this.codeOptions[code.subFiledCode] || !this.codeOptions[code.subFiledCode].length || !init) {
                            this.getDictBySql(code.subFiledCode, sqlString)
                            // }

                        }
                    })
                }

                // 关联事件
                if (!init) {
                    this.setEventDatas(item, currentObj)
                }


            },

            // 获得下拉框值
            getOptions() {
                let promiseList = []
                this.groupList.forEach((codeItem) => {
                    codeItem.codeList.forEach((item) => {
                        this.initCascader(item)
                    })
                })
                Promise.all(promiseList).then(() => {

                })
            },

            // 新增
            add() {
                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let params = Object.assign({}, this.valueForm, { 'module_id': { value: this.queryParams.moduleId } })
                for (let keys in params) {
                    params[keys] = params[keys].value
                }
                rbCmdbServiceFactory.addInstance(params).then((data) => {
                    if (data.success) {
                        this.$message.success(data.message)
                        let queryDatas = {
                            type: 'CMDB_VIEW',
                            key: this.$route.query.viewCodeName
                        }
                        rbCmdbServiceFactory.refreshViewsTree(queryDatas).then((res) => {
                            if (res.success) {
                                if (this.$route.query.routerName) {
                                    this.$store.commit('deleteIncludePagesName', this.$route.query.routerName)
                                }
                                setTimeout(() => {
                                    this.$router.push({ path: this.queryParams.originRouter })
                                    this.$emit('closeTab', this.$route.fullPath)
                                }, 1000)

                            } else {
                                this.$message.error(res.message)
                            }
                        })

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
                let params = Object.assign({}, this.valueForm, { 'module_id': { value: this.queryParams.moduleId } })
                for (let keys in params) {
                    params[keys] = params[keys].value
                }
                rbCmdbServiceFactory.updateInstance(params, this.queryParams.instanceId).then((data) => {
                    if (data.success) {
                        this.$message.success(data.message)
                        let queryDatas = {
                            type: 'CMDB_VIEW',
                            key: this.$route.query.viewCodeName
                        }
                        rbCmdbServiceFactory.refreshViewsTree(queryDatas).then((res) => {
                            if (res.success) {
                                if (this.$route.query.routerName) {
                                    this.$store.commit('deleteIncludePagesName', this.$route.query.routerName)
                                }
                                setTimeout(() => {
                                    this.$router.push({ path: this.queryParams.originRouter })
                                    this.$emit('closeTab', this.$route.fullPath)
                                }, 1000)

                            } else {
                                this.$message.error(res.message)
                            }
                        })

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
                    if (['add', 'unknown'].indexOf(this.queryParams.state) > -1) {
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
                await this.getFields()
                await this.getFieldsValues()

                // 被级联filedCode列表
                this.getBeCascadeObj()
                // 下拉框
                this.getOptions()
                this.getHaveEventCodeList()
                if (this.queryParams.state == 'detail') {
                    this.getModuleColumns()
                }
            },
        },

    }
</script>

<style lang="scss" scoped>
    /deep/ .form-setting {
        display: block;
        padding: 0;
        .el-form-item {
            margin-right: 0;
        }
    }
</style>
