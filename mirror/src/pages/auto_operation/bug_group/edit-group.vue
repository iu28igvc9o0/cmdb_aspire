<template>
    <div class="components-container yw-form">
        <el-form :model="addForm"
                 ref="addForm"
                 class="yw-form form-scene is-required"
                 label-width="85px"
                 :rules="addFormRules">
            <el-form-item label="分组名称"
                          prop="name">
                <el-input v-model="addForm.name"
                          placeholder=""
                          :disabled="readonly"></el-input>
            </el-form-item>
            <el-form-item label="是否生效"
                          prop="is_valid">
                <el-select v-model="addForm.is_valid"
                           clearable
                           :disabled="readonly">
                    <el-option label="是"
                               value="1"></el-option>
                    <el-option label="否"
                               value="0"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="分组描述"
                          prop="vulnerability_group_desc">
                <el-input type="textarea"
                          :rows="2"
                          v-model="addForm.vulnerability_group_desc"
                          placeholder=""
                          :disabled="readonly">
                </el-input>
            </el-form-item>
        </el-form>
        <div class="yw-order-filter"
             v-for="(rows, dataIndex) in ruleDataList"
             :key="dataIndex">
            <div class="data-filter-form">
                <template v-for="(row, rowIndex) in rows">
                    <el-form :model="row"
                             :key="rowIndex"
                             :ref="'rowForm' + dataIndex"
                             :rules="rowFormRules"
                             label-width="80px"
                             label-position="left">
                        <el-row class="data-filter-row"
                                :gutter="0"
                                type="flex"
                                align="middle"
                                justify="start">
                            <el-col :span="2">
                                <img src="/static/img/alert/filter-first.png"
                                     class="data-filter-col-img" />
                                <div class="dotted-line"></div>
                            </el-col>
                            <!-- 规则类型 -->
                            <el-col :span="7">
                                <el-form-item label="规则类型"
                                              prop="filterItemName">
                                    <el-select v-model="row.filterItemName"
                                               placeholder="请选择规则类型"
                                               :disabled="readonly"
                                               clearable
                                               filterable>
                                        <el-option :label="option.label"
                                                   :value="option.value"
                                                   v-for="(option, indexOption) in rulesList"
                                                   :key="indexOption"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <!-- 规则符号 -->
                            <el-col :span="7">
                                <el-form-item label="规则符号"
                                              prop="operate">
                                    <el-select v-model="row.operate"
                                               placeholder="请选择规则符号"
                                               :disabled="readonly"
                                               clearable
                                               filterable>
                                        <el-option :label="option.label"
                                                   :value="option.value"
                                                   v-for="(option, indexOption) in symbolList"
                                                   :key="indexOption"></el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <!-- 规则 -->
                            <el-col :span="7">
                                <el-form-item label="规则"
                                              prop="value">
                                    <el-input v-model="row.value"
                                              placeholder=""
                                              :disabled="readonly"></el-input>
                                </el-form-item>
                            </el-col>
                            <!-- 删除行内规则 -->
                            <el-col :span="1"
                                    v-if="rows.length > 1 && !readonly">
                                <img class="data-filter-col-delete mleft10"
                                     src="/static/img/alert/filter-delete.png"
                                     @click="removeRow(dataIndex, rowIndex)" />
                            </el-col>
                        </el-row>
                        <el-row class="data-filter-row"
                                type="flex"
                                align="middle"
                                justify="start"
                                v-if="rowIndex === rows.length-1 && !readonly">
                            <el-col :span="2">
                                <img src="/static/img/alert/filter-piece-last.png"
                                     class="data-filter-col-img" />
                            </el-col>
                            <el-col :span="22"
                                    v-if="!readonly"
                                    align="left">
                                <!-- 增加行内规则 -->
                                <img src="/static/img/alert/filter-add.png"
                                     class="data-filter-col-add"
                                     @click="addRow(dataIndex)" />
                                <!-- 删除整行规则 -->
                                <span class="pointer blue remove-data-row"
                                      v-if="ruleDataList.length > 1"
                                      @click="removeDataRow(dataIndex)">删除整行</span>
                            </el-col>
                        </el-row>
                    </el-form>
                </template>
            </div>
            <!--激活状态的 或-->
            <div class="filter-or"
                 v-if="(dataIndex === ruleDataList.length - 1) && !readonly">
                <img class="filter-or-img"
                     src="/static/img/alert/filter-or.png"
                     @click="addDataRow(dataIndex)" />
            </div>
            <!--失效状态的 或-->
            <div class="filter-or"
                 v-else-if="dataIndex !== ruleDataList.length - 1">
                <img class="filter-or-img"
                     src="/static/img/alert/filter-or-inactive.png" />
            </div>
        </div>
    </div>
</template>
<script>

    export default {
        name: 'EditBugGroup',
        props: {
            currentRowData: {
                type: Object,
                default() {
                    return {}
                }
            },
            showBoxType: ''
        },
        data() {
            return {
                addForm: {
                    name: '',
                    is_valid: '1',
                    vulnerability_group_desc: '',
                    vulnerability_group_rule: '',
                },
                addFormRules: {
                    name: [
                        {
                            required: true,
                            message: '请输入分组名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 500,
                            message: '长度在 2 到 500 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    vulnerability_group_desc: [
                        {
                            required: true,
                            message: '请输入分组描述!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 200,
                            message: '长度在 2 到 200 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    vulnerability_group_rule: [
                        {
                            required: true,
                            message: '请选择分组规则!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },
                rowFormRules: {
                    filterItemName: [
                        {
                            required: true,
                            message: '请选择规则分类!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    operate: [
                        {
                            required: true,
                            message: '请选择规则符号!',
                            trigger: ['blur', 'change']
                        }
                    ],
                    value: [
                        {
                            required: true,
                            message: '请输入自定义规则!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 1000,
                            message: '长度在 2 到 1000 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },

                // 漏洞分组规则
                ruleDataList: [
                    [
                        {
                            value: '',
                            filterItemName: '',
                            operate: '',
                        },
                    ]
                ],
                rowObj: {
                    value: '',
                    filterItemName: '',
                    operate: '',
                },
                // 符号数据
                symbolList: [
                    {
                        label: '模糊匹配',
                        value: 'like'
                    },
                    {
                        label: '等于',
                        value: '='
                    },
                    {
                        label: '不等于',
                        value: '!='
                    },
                    {
                        label: '包含',
                        value: 'in'
                    },
                    {
                        label: '不包含',
                        value: 'not in'
                    }, {
                        label: '存在',
                        value: 'exist'
                    }, {
                        label: '不存在',
                        value: 'not exist'
                    },
                ]
            }
        },
        computed: {
            // 获取规则分类
            rulesList() {
                let rules = this.$store.state.homeStore.dictObj.vulnerability_rule_item
                let arr = []
                for (const key in rules) {
                    let obj = {}
                    if (Object.prototype.hasOwnProperty.call(rules, key)) {
                        obj.value = key
                        obj.label = rules[key].name
                    }
                    arr.push(obj)
                }
                return arr
            },
            readonly() {
                return this.showBoxType === 'viewDetail'
            }
        },
        watch: {
            // 回显数据
            currentRowData: {
                handler(newVal) {
                    this.addForm = newVal || this.addForm
                    this.ruleDataList = (newVal && JSON.parse(newVal.vulnerability_group_rule)) || this.ruleDataList
                },
                deep: true,
                immediate: true
            },
            // 更新编辑框
            addForm: {
                handler(newVal) {
                    this.$emit('updateAddForm', newVal)
                },
                deep: true
            },
            ruleDataList: {
                handler(newVal) {
                    this.addForm.vulnerability_group_rule = JSON.stringify(newVal)
                },
                deep: true
            },
        },
        mounted() {
        },
        methods: {
            // 校验表单
            validForm() {
                let promiseArr = []
                // 基本信息
                const basicFormValid = new Promise((resolve, reject) => {
                    this.$refs.addForm.validate(valid => {
                        if (valid) {
                            resolve()
                        } else {
                            reject()
                        }
                    })
                })
                promiseArr.push(basicFormValid)

                // 规则
                this.ruleDataList.forEach((item, index) => {
                    item.forEach((row, rowIndex) => {
                        const rowFormValid = new Promise((resolve, reject) => {
                            this.$refs['rowForm' + index][rowIndex].validate(valid => {
                                if (valid) {
                                    resolve()
                                } else {
                                    reject()
                                }
                            })
                        })
                        promiseArr.push(rowFormValid)
                    })

                })
                Promise.all(promiseArr)
                    .then(() => {
                        this.$emit('saveFormData')
                    })
                    .catch(() => {
                        this.$message('请先完善信息')
                    })
            },
            // 增加行内规则
            addRow(index) {
                this.ruleDataList[index].push(
                    this.$utils.deepClone(this.rowObj)
                )
            },
            // 删除行内规则
            removeRow(dataIndex, rowIndex) {
                if (this.ruleDataList[dataIndex].length > 1) {
                    this.ruleDataList[dataIndex].splice(rowIndex, 1)
                }
            },
            // 增加【或】规则
            addDataRow() {
                this.ruleDataList.push(
                    [this.$utils.deepClone(this.rowObj)]
                )
            },
            // 删除【或】规则
            removeDataRow(dataIndex) {
                if (this.ruleDataList.length > 1) {
                    this.$confirm('确认删除整行？').then(() => {
                        this.ruleDataList.splice(dataIndex, 1)
                    })
                }
            },
        }
    }
</script>

<style lang="scss" scoped>
    .yw-order-filter {
        .filter-or {
            text-align: center;

            .filter-or-img {
                width: 40px;
                cursor: pointer;
            }
        }

        .data-filter-form {
            border: 1px solid #eeeeee;
            box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.05);
            border-radius: 3px;

            .data-filter-row {
                margin-left: 10px;

                .data-filter-col-img {
                    width: 90px;
                }

                .data-filter-col-delete {
                    width: 20px;
                    height: 20px;
                    cursor: pointer;
                }

                .data-filter-col-add {
                    width: 20px;
                    height: 20px;
                    cursor: pointer;
                }
                .remove-data-row {
                    float: right;
                    margin-right: 20px;
                }
            }
        }
    }
</style>
