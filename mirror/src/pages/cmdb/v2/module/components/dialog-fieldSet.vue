<template>
    <!-- 模型管理:字段信息配置 -->
    <el-dialog class="yw-dialog"
               v-if="dialogMsg.dialogVisible"
               width="750px"
               title="字段信息"
               :visible.sync="dialogMsg.dialogVisible">
        <section class="yw-dialog-main">
            <el-form ref="dialogFieldSet"
                     class="yw-form is-required"
                     label-width="85px"
                     :inline="true"
                     :rules="rules"
                     :model="formData">
                <!-- 字段信息 -->
                <section class="section-set">
                    <el-tabs class="yw-tabs">
                        <el-tab-pane label="字段信息">
                        </el-tab-pane>
                    </el-tabs>
                    <el-form-item label="模型分组">
                        <el-select v-model="formData.moduleGroup"
                                   disabled
                                   placeholder="请选择">
                            <el-option v-for="item in moduleGroupDatas"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="码表名称">
                        <el-input v-model="formData.codeName"
                                  :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="码表编码">
                        <el-input v-model="formData.codeId"
                                  :disabled="true"></el-input>
                    </el-form-item>
                    <el-form-item label="控件类型">
                        <el-select v-model="formData.controlType"
                                   disabled
                                   placeholder="请选择">
                            <el-option v-for="item in controlTypeDatas"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="码表宽度"
                                  prop="codeWidth">
                        <el-radio-group size="mini"
                                        v-model="formData.codeWidth">
                            <el-radio-button :label="tabItem.id"
                                             v-for="(tabItem,tabIndex) in codeWidthDatas"
                                             :key="tabIndex">{{tabItem.name}}</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="字段说明">
                        <el-input v-model="formData.fieldDesc"></el-input>
                    </el-form-item>
                    <el-form-item label="字段长度"
                                  prop="fieldWidth">
                        <el-input v-model="formData.fieldWidth"></el-input>
                    </el-form-item>
                </section>
                <!-- 字段信息 -->

                <!-- 字段验证信息 -->
                <section class="section-set">
                    <el-tabs class="yw-tabs">
                        <el-tab-pane label="字段验证信息">
                        </el-tab-pane>
                    </el-tabs>
                    <el-form-item label="是否必填">
                        <el-radio-group v-model="formData.isRequire">
                            <el-radio label="否">否</el-radio>
                            <el-radio label="是">是</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <br />
                    <el-form-item label="校验规则"
                                  v-if="formData.isRequire=='是'">
                        <el-input v-model="formData.validRules"></el-input>
                    </el-form-item>
                    <el-form-item label="正则表达式"
                                  v-if="formData.isRequire=='是'">
                        <el-input v-model="formData.validRex"
                                  :disabled="disabledDatas.validRex"></el-input>
                        <a>校验</a>
                    </el-form-item>
                </section>
                <!-- 字段验证信息 -->

                <!-- 审核配置信息 -->
                <section class="section-set">
                    <el-tabs class="yw-tabs">
                        <el-tab-pane label="审核配置信息">
                        </el-tab-pane>
                    </el-tabs>
                    <el-form-item label="是否审核">
                        <el-radio-group v-model="formData.isCheck">
                            <el-radio label="否">否</el-radio>
                            <el-radio label="是">是</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <br />
                    <el-form-item label="审核机制"
                                  props='checkMethods'
                                  v-if="formData.isCheck=='是'">
                        <el-select v-model="formData.checkMethods"
                                   placeholder="请选择">
                            <el-option v-for="item in checkMethodsDatas"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="审核备注"
                                  v-if="formData.isCheck=='是'">
                        <el-input v-model="formData.checkDesc"></el-input>
                    </el-form-item>
                </section>
                <!-- 审核配置信息 -->

                <!-- 采集配置信息 -->
                <section class="section-set">
                    <el-tabs class="yw-tabs">
                        <el-tab-pane label="采集配置信息">
                        </el-tab-pane>
                    </el-tabs>
                    <el-form-item label="是否采集">
                        <el-radio-group v-model="formData.isCollect"
                                        style="width: 178px;">
                            <el-radio label="否">否</el-radio>
                            <el-radio label="是">是</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="采集方式"
                                  v-if="formData.isCollect=='是'">
                        <el-select v-model="formData.collectMethods"
                                   placeholder="请选择">
                            <el-option v-for="item in collectMethodsDatas"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="采集脚本"
                                  v-if="formData.isCollect=='是'">
                        <el-select v-model="formData.collectScript"
                                   placeholder="请选择">
                            <el-option v-for="item in collectScriptDatas"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="映射key值"
                                  props="collectKeys"
                                  v-if="formData.isCollect=='是'">
                        <el-input v-model="formData.collectKeys"></el-input>
                    </el-form-item>
                    <el-form-item label="采集频率"
                                  props="collectCycle"
                                  v-if="formData.isCollect=='是'">
                        <el-select v-model="formData.collectCycle"
                                   placeholder="请选择">
                            <el-option v-for="item in collectCycleDatas"
                                       :key="item.id"
                                       :label="item.name"
                                       :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="正则表达式"
                                  v-if="formData.isCollect=='是'">
                        <el-input v-model="formData.collectRex"
                                  :disabled="disabledDatas.collectRex"></el-input>
                        <a>校验</a>
                    </el-form-item>
                </section>
                <!-- 采集配置信息 -->
            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="submit()">保存</el-button>
            <el-button @click="cancel()">取消</el-button>
        </section>
    </el-dialog>

</template>
<script>
    export default {
        props: ['dialogMsg'],
        components: {

        },
        data () {
            return {
                // 表单数据
                formData: {
                    moduleGroup: '',// 模型分组
                    codeName: '',// 码表名称
                    codeId: '',// 码表编码
                    controlType: '',// 控件类型
                    codeWidth: '',// 码表宽度
                    fieldDesc: '',// 字段说明
                    fieldWidth: '',// 字段长度
                    isRequire: '否',// 是否必填
                    validRules: '',// 校验规则
                    validRex: '',// 验证正则表达式
                    isCheck: '否',// 是否审核
                    checkMethods: '',// 审核机制
                    checkDesc: '',// 审核备注
                    isCollect: '否',// 是否采集
                    collectMethods: '',// 采集方式
                    collectScript: '',// 采集脚本
                    collectKeys: '',// 映射key值
                    collectCycle: '',// 采集频率
                    collectRex: '',// 采集正则表达式
                },

                // 下拉框数据
                moduleGroupDatas: [],// 模型分组下拉框数据
                controlTypeDatas: [],// 控件类型下拉框数据
                codeWidthDatas: [],// 码表宽度下拉框数据
                checkMethodsDatas: [],// 审核机制下拉框数据
                collectMethodsDatas: [],// 采集方式下拉框数据
                collectScriptDatas: [],// 采集脚本下拉框数据
                collectCycleDatas: [],// 采集频率下拉框数据

                // disabled是否不可编辑
                disabledDatas: {
                    validRex: true,
                    collectRex: true
                },

                // 校验
                rules: {
                    codeWidth: [
                        { required: true, message: '不能为空', trigger: 'change' }
                    ],
                    fieldWidth: [
                        { required: true, message: '不能为空', trigger: 'blur' }
                    ],
                    checkMethods: [
                        { required: true, message: '不能为空', trigger: 'change' }
                    ],
                    collectKeys: [
                        { required: true, message: '不能为空', trigger: 'blur' }
                    ],
                    collectCycle: [
                        { required: true, message: '不能为空', trigger: 'change' }
                    ],

                },
            }
        },
        methods: {
            query () {
                this.getCodeWidthDatas()
            },
            // 获得码表宽度数据
            getCodeWidthDatas () {
                this.codeWidthDatas = [
                    {
                        id: '1',
                        name: '整行'
                    },
                    {
                        id: '2',
                        name: '1/2行'
                    },
                    {
                        id: '3',
                        name: '1/3行'
                    },
                    {
                        id: '4',
                        name: '1/4行'
                    }
                ]
            },

            // 保存
            submit () {
                this.$emit('closeDialog')
            },
            // 取消
            cancel () {
                this.$emit('closeDialog')
            }


        },
        mounted () {
            this.query()
        }
    }

</script>
<style lang="scss" scoped>
/deep/.el-radio-button--mini {
  .el-radio-button__inner {
    padding: 7px;
  }
}
.section-set {
  margin-bottom: 20px;
}
</style>
