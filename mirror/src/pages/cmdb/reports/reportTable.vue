<template>
    <div style="width:100%;overflow-x:hidden;">
        <el-form :inline="true"
                 class="yw-form components-condition">
            <el-form-item label="月份">
                <el-date-picker v-model="date"
                                @change="changeDate"
                                type="month"
                                value-format="yyyy-MM"
                                :clearable="false"
                                :disabled="params.link === 'external'"
                                placeholder="选择月">
                </el-date-picker>
            </el-form-item>
            <section class="btn-wrap">
                <el-button type="primary"
                           v-if="params.link !== 'external'"
                           @click="query()">查询</el-button>
                <el-button type="primary"
                           v-if="['update'].indexOf(params.type)>-1"
                           @click="save()">保存</el-button>
            </section>
        </el-form>
        <el-card class="box-card"
                 v-for="(value1,key1,index1) in tableTitles"
                 :key="index1"
                 style="margin-bottom: 5px;">
            <div slot="header">
                <span style="font-weight: bold">{{key1}}</span>
                <div class="clearfix fr">
                    <el-button class="btn-icons-wrap"
                               v-if="['update'].indexOf(params.type)>-1 && noIdcTypeAdd(key1)"
                               type="text"
                               icon="el-icon-plus"
                               @click="add(key1)">新增</el-button>
                    <el-button type="text"
                               v-if="['update'].indexOf(params.type)>-1"
                               icon="el-icon-download"
                               @click="importIn(key1)">导入</el-button>
                </div>
            </div>
            <el-table class="yw-el-table"
                      style="width:100%;min-height:300px;"
                      :data="tableDatas[key1]"
                      max-height="400"
                      stripe
                      border>
                <el-table-column :label="key2"
                                 min-width="200"
                                 align="center"
                                 v-for="(value2,key2) in value1"
                                 :key="key2">
                    <!-- 有子列 -->
                    <el-table-column v-for="(value3) in value2"
                                     min-width="200"
                                     :key="value3.id"
                                     align="center"
                                     :label="value3.resourceType">
                        <template slot-scope="scope">
                            <!--日期框-->
                            <el-date-picker v-model="scope.row[value3.id]"
                                            style="width:175px"
                                            v-if="['update'].indexOf(params.type)>-1 && dateList.indexOf(value3.resourceControlType)>-1"
                                            value-format="yyyy-MM-dd HH:mm:ss"
                                            type="datetime"
                                            @change="valid(scope.row,value3,value1)"
                                            clearable>
                            </el-date-picker>
                            <!--日期框-->
                            <!-- 数字框 -->
                            <el-input-number v-model="scope.row[value3.id]"
                                             v-else-if="['update'].indexOf(params.type)>-1 && numList.indexOf(value3.resourceControlType)>-1"
                                             :min="value3.valueValid && value3.valueValid.start || 0"
                                             :max="value3.valueValid && value3.valueValid.end || 999999999"
                                             style="width:160px"
                                             @change="valid(scope.row,value3,value1)"
                                             label="描述文字"></el-input-number>
                            <!-- 数字框 -->
                            <!-- 浮点数框 -->
                            <el-input-number v-model="scope.row[value3.id]"
                                             v-else-if="['update'].indexOf(params.type)>-1 && floatList.indexOf(value3.resourceControlType)>-1"
                                             :precision="2"
                                             :step="0.1"
                                             :min="value3.valueValid && value3.valueValid.start || 0"
                                             :max="value3.valueValid && value3.valueValid.end || 999999999"
                                             style="width:160px"
                                             @change="valid(scope.row,value3,value1)"
                                             label="描述文字"></el-input-number>
                            <!-- 浮点数框 -->
                            <!-- 输入框(省份通过左侧树名称匹配，其他字段id匹配) -->
                            <input class="edit-input"
                                   type="text"
                                   :disabled="['省份名称'].indexOf(value3.resourceType)>-1"
                                   v-else-if="['update'].indexOf(params.type)>-1"
                                   v-model="scope.row[value3.id]"
                                   @change="valid(scope.row,value3,value1)"
                                   placeholder="" />
                            <!-- 输入框 -->
                            <!-- 文本 -->
                            <span v-else>{{scope.row[value3.id]}}</span>
                            <!-- 文本 -->
                            <!-- 校验提示 -->
                            <div class="text-ellipse"
                                 v-if="['update'].indexOf(params.type)>-1 && scope.row['rules'] && scope.row['rules'][value3.resourceType] && !(scope.row['rules'][value3.resourceType]['flag'])"
                                 style="font-size:12px;color:red;">
                                {{scope.row['rules'][value3.resourceType].msg}}
                            </div>
                            <!-- 校验提示 -->
                        </template>
                    </el-table-column>
                    <!-- 有子列 -->
                </el-table-column>
                <el-table-column label="操作"
                                 fixed="right"
                                 v-if="['update'].indexOf(params.type)>-1 && noIdcType.indexOf(key1)<0"
                                 align="center"
                                 width="100">
                    <template slot-scope="scope">
                        <div class="yw-table-operator">
                            <el-button type="text"
                                       title="添加"
                                       icon="el-icon-circle-plus-outline"
                                       @click="add(key1)"></el-button>
                            <el-button type="text"
                                       title="删除"
                                       icon="el-icon-delete"
                                       @click="del(scope.row,key1,scope.$index)"></el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>

        <!-- dialog -->
        <YwUpload v-if="uploadData.show"
                  :uploadData="uploadData"
                  @uploadEvent="uploadEvent"></YwUpload>
        <!-- dialog -->
    </div>
</template>

<script>
    import CommonOption from 'src/utils/commonOption.js'
    import QueryObject from 'src/utils/queryObject.js'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory.js'
    export default {
        mixins: [CommonOption, QueryObject],
        props: ['params'],
        components: {
            YwUpload: () => import('src/components/common/yw-upload.vue'),
        },
        data() {
            return {
                // 时间框
                date: '',
                /** 多表操作 */
                // 查询表头
                tableTitles: {},
                // 查询数据
                tableDatas: {},
                // 提交数据
                submitDatas: {},
                // 表头key-value格式(提交时,携带表头校验等信息)
                titlesObj: {
                    // 每个表名:{"存储服务器数量(台)":{id:'',calcExp:'',...},}
                },
                // 输入框
                inputList: ['string'],
                // 下拉框
                selectList: ['listSel'],
                // 时间框
                dateList: ['datetime'],
                // 数字框
                numList: ['int'],
                // 浮点框
                floatList: ['double'],
                // 没有资源池的表格
                noIdcType: ['资源工单', '运维工单'],

                // 当前表格
                activeTable: {
                    name: ''
                },
                // 上传弹窗
                uploadData: {
                    show: false,// 是否显示弹出窗
                    actionUrl: '',// 上传地址
                    actionParams: '',// 上传参数
                },
            }
        },
        computed: {
            noIdcTypeAdd() {
                return function (key1) {
                    let noIdcTypeTrue = this.noIdcType.indexOf(key1) > -1
                    let tableLengthTrue = this.tableDatas[key1] && this.tableDatas[key1].length > 0

                    return (!tableLengthTrue && noIdcTypeTrue) || !noIdcTypeTrue
                }

            }
        },

        methods: {
            // 切换时间
            changeDate(val) {
                if (this.params.type === 'query') {
                    this.$emit('changeDate', val)
                }

            },
            // 上传事件
            uploadEvent(obj = {}) {
                switch (obj.eventType) {
                    // 关闭弹窗
                    case 'closeDialog':
                        this.uploadData.show = false
                        this.query()
                        break
                    // 下载模板
                    case 'downLoadTemp': {
                        let params = {
                            resourceOwner: this.params.source,
                            resourceClass: this.activeTable.name
                        }
                        rbCmdbService.downloadReportTable(params).then((data) => {
                            this.exportFiles({
                                data: data,
                                fileType: 'application/msword',
                                fileName: '导入模板.xlsx'
                            })
                        })
                        break
                    }

                    default:
                        break
                }
            },

            // 导入
            importIn(val) {
                this.activeTable.name = val
                this.uploadData.show = true
                this.uploadData.actionParams = {
                    importType: '31_province_report',
                    province: this.params.province,
                    resourceOwner: this.params.source,
                    monthlyTime: this.date,
                    operatorUser: this.params.updatePerson,
                    resourceClass: this.activeTable.name,
                }
            },
            // 校验字段
            valid(row, rule, tableTitles) {

                let curValue = row[rule.resourceType]// 当前值
                let compareValue = ''// 比较值
                let calcValid = JSON.parse(rule.calcValid)// 比较判断对象
                let valueValid = JSON.parse(rule.valueValid)// 区间判断对象
                // 错误提示
                let errorTip = {
                    flag: true,// 校验通过否
                    msg: ''
                }
                // 获得比较值
                for (let keys in tableTitles) {
                    tableTitles[keys].some((item) => {
                        if (calcValid && calcValid.anotherId === item.id) {
                            compareValue = row[item.resourceType]
                            return true
                        }
                    })
                }


                // 区间判断
                if (curValue && valueValid && !(valueValid.start <= curValue <= valueValid.end)) {
                    errorTip.flag = false
                    errorTip.msg = valueValid.tip
                }
                // 比较判断
                if (this.dateList.indexOf(rule.resourceControlType) > -1) {
                    // 时间框
                    if (curValue && calcValid && compareValue && !eval(Date.parse(curValue) + calcValid.operator + Date.parse(compareValue))) {
                        errorTip.flag = false
                        errorTip.msg = calcValid.tip
                    }

                } else {
                    if (curValue && calcValid && !eval(curValue + calcValid.operator + compareValue)) {
                        errorTip.flag = false
                        errorTip.msg = calcValid.tip
                    }
                }
                // 校验规则绑定在行数据上
                let obj = {

                }
                this.$set(obj, rule.resourceType, errorTip)
                this.$set(row, 'rules', {})
                row.rules = Object.assign({}, row.rules, obj)
            },
            // 获得提交数据
            getSubmitDatas() {
                for (let tableKey in this.tableDatas) {
                    this.$set(this.submitDatas, tableKey, [])// 每个表格
                    this.tableDatas[tableKey].forEach((item, index) => {
                        let obj = {}// 每行数据
                        obj['provinceReport'] = {
                            id: item.id,
                            resourceOwner: this.params.source,
                            provinceName: item['省份名称'],
                            submitMonth: item['月份'],
                            updatePerson: this.params.updatePerson
                        }
                        obj['valueList'] = []

                        for (let rowKey in item) {
                            if (['rules', '月份', '省份名称', 'id'].indexOf(rowKey) < 0 && this.titlesObj[tableKey][rowKey]) {
                                // 每个字段
                                obj['valueList'].push({
                                    'settingId': this.titlesObj[tableKey][rowKey].id,// 表头id
                                    'calcExp': this.titlesObj[tableKey][rowKey].calcExp, // 校验规则
                                    'reportValue': item[rowKey],// 字段值
                                })
                            }

                        }
                        this.submitDatas[tableKey].push(obj)
                    })

                }

                return this.submitDatas
            },
            // 获得titlesObj
            getTitlesObj() {
                this.titlesObj = {}
                for (let titleKey in this.tableTitles) {
                    // 每个表格
                    this.titlesObj[titleKey] = {}
                    for (let subKey in this.tableTitles[titleKey]) {
                        // 每个字段
                        if (this.tableTitles[titleKey][subKey] && this.tableTitles[titleKey][subKey].length > 0) {
                            this.tableTitles[titleKey][subKey].forEach((leafItem) => {
                                this.$set(this.titlesObj[titleKey], leafItem.id, leafItem)
                            })
                        } else {
                            this.$set(this.titlesObj[titleKey], subKey, {})
                        }
                    }

                }
                return this.titlesObj
            },
            // 获得表头
            getTitles() {
                let params = {
                    resourceOwner: this.params.source,
                    provinceName: this.params.province,
                    submitMonth: this.date,
                    type: this.params.type,
                }
                return rbCmdbService.getReportTitles(params).then((data) => {
                    this.tableTitles = data
                    this.getTitlesObj()
                    return data
                })
            },
            // 获得表内容
            getDatas() {
                this.showFullScreenLoading({ text: '正在查询数据, 请稍等...' })
                let params = {
                    resourceOwner: this.params.source,
                    provinceName: this.params.province,
                    submitMonth: this.date,
                    type: this.params.type,
                }
                return rbCmdbService.getReportResource(params).then((data) => {
                    this.tableDatas = data
                    return data
                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },

            // 添加
            add(key) {
                let rowDatas = {}
                for (let subKey in this.tableTitles[key]) {
                    this.$set(rowDatas, '月份', this.date)
                    this.$set(rowDatas, '省份名称', this.params.province)
                    this.$set(rowDatas, '月份', this.date)

                    if (this.tableTitles[key][subKey] && this.tableTitles[key][subKey].length > 0) {
                        this.tableTitles[key][subKey].forEach((item) => {
                            if (item.resourceType === '省份名称') {
                                rowDatas[item.id] = this.params.province
                            } else {
                                this.$set(rowDatas, item.id, '')
                            }

                        })

                    } else {
                        this.$set(rowDatas, subKey, '')
                    }
                }
                this.$set(rowDatas, 'id', '')

                if (!this.tableDatas[key]) {
                    this.$set(this.tableDatas, key, [])
                }
                this.tableDatas[key].push(rowDatas)
            },
            // 删除
            del(row, key, index) {
                this.tableDatas[key].splice(index, 1)
            },
            // 校验规则
            getValids() {
                let result = false
                for (let key1 in this.tableDatas) {
                    result = this.tableDatas[key1].some((item) => {
                        for (let key2 in item.rules) {
                            if (item.rules[key2].flag === false) {
                                return true
                            } else {
                                return false
                            }
                        }
                    })
                    if (result) {
                        break
                    }
                }
                return !result
            },
            // 更新数据
            updateDatas() {

                let validResult = this.getValids()
                if (!validResult) {
                    this.$confirm('字段值不符合规范,请检查后再提交', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    })
                    return false
                }

                this.showFullScreenLoading({ text: '正在保存数据, 请稍等...' })
                let params = {
                    provinceName: this.params.province,
                    resourceOwner: this.params.source,
                    submitMonth: this.date,
                    data: this.getSubmitDatas()
                }
                rbCmdbService.saveReportResource(params).then((data) => {
                    if (data.success === 'true') {
                        this.$message.success('保存成功！')
                        this.getDatas()
                    } else {
                        this.$message.error(data.message)
                    }

                }).finally(() => {
                    this.closeFullScreenLoading()
                })
            },
            // 保存
            save() {
                if (!this.date) {// 如果为空
                    this.$confirm('月份不能为空', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {

                    })
                    return false
                }
                this.$confirm('确定保存吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.updateDatas()
                })
            },
            // 查询数据
            async query() {
                await this.getTitles()
                this.getDatas()

                if (this.params.type === 'query') {
                    this.$emit('queryStatus', { submitMonth: this.date })
                }

                // Promise.all([promise1, promise2]).then((res) => {
                //     //如果为空,默认展示一条空数据
                //     if (['update'].indexOf(this.params.type) > -1) {
                //         for (let title in this.tableTitles) {
                //             if (Object.keys(this.tableDatas).length < 1 || !this.tableDatas[title]) {

                //                 this.$set(this.tableDatas, title, []);
                //                 this.add(title);
                //             }

                //         }

                //     }

                // })
            },
            async init() {
                this.date = this.params.date
                await this.getTitles()
                this.getDatas()
            },
        },
        mounted() {
            this.init()
        },
    }
</script>

<style lang="scss" scoped>
</style>
