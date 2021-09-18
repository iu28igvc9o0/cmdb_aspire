<template>
    <el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
        <el-form-item label="">
            <div>
                关键属性:
                <el-tooltip content="唯一KEY作为入库配置的重要识别码，由一个或多个关键属性构成" placement="bottom">
                    <i style="margin-left:20px;" class="fa fa-question-circle-o" aria-hidden="true"></i>
                </el-tooltip>&nbsp&nbsp&nbsp
                <el-checkbox v-if="builtin=='false'" v-model="formAttribute.keyattr"  @click.native="keyAttrClick"></el-checkbox>
            </div>
        </el-form-item>
        <el-form-item label="字段名称:" prop="name" required>
            <el-input v-model="formAttribute.name" :disabled="formAttribute.builtin"></el-input>
        </el-form-item>
        <el-form-item label="属性编码:" prop="code" required >
            <el-input v-model="formAttribute.code" :disabled="formAttribute.builtin"></el-input>
        </el-form-item>
        <el-form-item label="默认值:">
            <el-input v-model="formAttribute.defaultvalue"></el-input>
        </el-form-item>
        <el-form-item label="字段采集频次:">
            <el-select v-model="formAttribute.rate" placeholder="请选择">
                <el-option label="高频" value="3"></el-option>
                <el-option label="中频" value="2"></el-option>
                <el-option label="低频" value="1"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item v-if="builtin=='false'">
            <el-checkbox v-model="formAttribute.required" @click.native="requiredClick" :disabled="formAttribute.requiredDisable">必填</el-checkbox>
            <el-checkbox v-model="formAttribute.hidden" @click.native="hiddenClick" :disabled="formAttribute.requiredDisable">隐藏</el-checkbox>
        </el-form-item>
        <el-form-item v-if="builtin=='true'">
            <el-checkbox v-model="formAttribute.required" @click.native="requiredClick" :disabled="true">必填</el-checkbox>
            <el-checkbox v-model="formAttribute.hidden" @click.native="hiddenClick" :disabled="true">隐藏</el-checkbox>
        </el-form-item>
        <el-form-item label="校验规则:">
            <el-select v-model="formAttribute.params.validation" placeholder="请选择">
                <el-option label="无校验" value="0"></el-option>
                <el-option v-for="item in validationOptions" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="文本最小长度:" required prop="params.minLength">
            <el-input-number v-model="formAttribute.params.minLength" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="文本最大长度:" required prop="params.maxLength">
            <el-input-number v-model="formAttribute.params.maxLength" :min="1"></el-input-number>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
    name: 'text',
    props: ['textChildForm', 'Data', 'edit','builtin'],

    data() {
        return {
            formAttribute: {
                name: '单行文本',
                code: '',
                defaultvalue: '',
                required: false,
                hidden: false,
                params: {
                    validation: '',
                    minLength: 1,
                    maxLength: 100,
                },
                requiredDisable: false,
                keyattr: false,
                rate:'',

                builtin: false,
                type: 'singleRowText',

            },
            default: {
                name: '单行文本',
                code: '',
                defaultvalue: '',
                required: false,
                hidden: false,
                params: {
                    validation: '',
                    minLength: 1,
                    maxLength: 100,
                },
                requiredDisable: false,
                keyattr: false,
                rate:'',

                builtin: false,
                type: 'singleRowText',

            },
            top: 'top',
            validationOptions: [],

            editIndex: '',
            list2Data: {},
            isbuiltin:null,
            fromrules: {
                name: [{
                    required: true,
                    message: '请输入名称',
                    trigger: 'blur',
                }, {
                    min: 1,
                    max: 16,
                    message: '长度在 1 到 16 个字符',
                    trigger: 'blur',
                }],
                code: [{
                    required: true,
                    message: '请输入编码',
                    trigger: 'blur',
                }, {
                    min: 1,
                    max: 16,
                    message: '长度在 1 到 16 个字符',
                    trigger: 'blur',
                }, {
                    validator: this.checkCode,
                    trigger: 'blur',
                }],
                'params.maxLength': [{
                    validator: this.checkMax,
                    trigger: 'blur',
                }],
            },
        }
    },
    /*computed: {
        formAttribute () {
            return this.textChildForm
        }
    },*/
    mounted: function() {
        this.getRule();
        console.log(this.builtin);
        if (this.textChildForm != null) {
            this.formAttribute = this.parse(this.textChildForm);
        }
        //childFormData父类传递的参数值发生变化时调用
         this.$watch('textChildForm', function(newVal, oldVal) {
             console.log("--text");
             this.formAttribute = this.parse(this.textChildForm);
         },{deep: true});
    },
    methods: {
        parse(data){
            if(data.keyattr==true){
                data['requiredDisable'] = true;
            }else{
                data['requiredDisable'] = false;
            }
            return data;
        },
        keyAttrClick() {
            if (this.formAttribute.keyattr == true) {
                this.formAttribute.required = true;
                this.formAttribute.hidden = false;
                this.formAttribute.requiredDisable = true;
            } else {
                this.formAttribute.required = false;
                this.formAttribute.hidden = false;
                this.formAttribute.requiredDisable = false;
            }

        },
        requiredClick() {
            if (this.formAttribute.required == true) {
                this.formAttribute.required = false;
            } else {
                this.formAttribute.required = true;
                this.formAttribute.hidden = false;
            }
        },
        hiddenClick() {
            if (this.formAttribute.hidden == true) {
                this.formAttribute.hidden = false;
            } else {
                this.formAttribute.hidden = true;
                this.formAttribute.required = false;
            }
        },
        //校验编码方法
        checkCode(rule, value, callback) {
            for (var p in this.Data) {
                if (this.edit == p) {
                    continue;
                }
                if ($.trim(value) == this.Data[p]['code']) {
                    return callback(new Error('抱歉，该编码已被占用!'));
                }
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if (!c.test(value)) {
                return callback(new Error('编码只可输入字母、数字、下划线'));
            }
            callback();
        },
        checkMax(rule, value, callback) {
            if (value <= this.formAttribute.params.minLength) {
                return callback(new Error('抱歉，最大长度必须大于最小长度!'));
            }
            callback();
        },
        objDeepCopy(source) {
            var sourceCopy = source instanceof Array ? [] : {};
            for (var item in source) {
                sourceCopy[item] = typeof source[item] === 'object' ? this.objDeepCopy(source[item]) : source[item];
            }
            return sourceCopy;
        },
        validation() {
            var flag = false;
            this.$refs.formAttribute.validate((valid) => {
                if (valid) {
                    flag = true;
                }
            });
            return flag;
        },
        getRule() {
            $.ajax({
                url: '/cmdb/form/getFormRule',
                type: 'get',
                dataType: 'json',
            }).done(function(data) {
                this.validationOptions = data;
            }.bind(this));
        },
        setDefault(){
            this.formAttribute = JSON.parse(JSON.stringify(this.default));
        },
        validation(){
            let vaildData = this.formAttribute;
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
        }
    }
}

</script>
