<!--  -->
<template>
    <div>
        <section class="yw-dialog-main">
            <el-form class="yw-form components-condition">
                <el-form-item label="脚本参数"
                              prop="script_param">
                    <el-input v-model="script_param"
                              :type="isSensitivity?'password':'text'"
                              clearable></el-input>
                    <el-checkbox v-model="isSensitivity">
                        <el-tooltip class="item"
                                    effect="dark"
                                    :content="tipContent"
                                    placement="right">
                            <i class="el-icon-question"></i>
                        </el-tooltip>
                        *敏感参数
                    </el-checkbox>
                </el-form-item>
                <el-form-item label="自定义参数"
                              prop="is_public">
                    <customParams @custompParameter="custompParameter"
                                  :scriptParamList="scriptParamList"
                                  :showAddBtn="showAddBtn"
                                  :showEdit="showEdit">
                    </customParams>
                </el-form-item>
            </el-form>
        </section>
        <section class="btn-wrap">
            <el-button type="primary"
                       @click="parameterBtn()">确认</el-button>
            <el-button @click="cancel()">返回</el-button>
        </section>

    </div>
</template>

<script>
    import customParams from './custom-parameter.vue'

    export default {
        data() {
            return {
                // 传参
                isSensitivity: false, // 是否敏感参数
                tipContent: '',
                script_param: '',
                custompParameterList: [],
                showAddBtn: '',
                showEdit: true,
                scriptParamList: [],
            }
        },
        props: ['scriptParamList', 'scriptParam'],
        components: { customParams },
        methods: {
            custompParameter(val) {
                this.custompParameterList = val
            },
            parameterBtn() {
                let item_ext = {}
                item_ext.script_param = this.script_param
                item_ext.customize_param = JSON.stringify(this.custompParameterList)
                this.$emit('customParam', item_ext)
                this.$emit('parameterDialog', false)
            },
            cancel() {
                this.$emit('parameterDialog', false)
            }
        },
        created() {
            if (this.scriptParam) {
                this.script_param = this.scriptParam
            }
        },
        watch: {
            scriptParamList: {
                handler(val) {
                    console.log('scriptParamList', val)
                },
                immediate: true,
                deep: true
            },
            script_param: {
                handler(val) {
                    if (val) {
                        let newVal = val.split(' ')
                        let num = 0
                        this.tipContent = ''
                        newVal.forEach(item => {
                            num++
                            this.tipContent += '第' + num + '个参数：' + item + ';'
                        })
                    }
                },
                deep: true,
                immediate: true

            },

        }
    }

</script>
<style lang='scss' scoped>
</style>