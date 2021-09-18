<template>
<el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
    <el-form-item label="字段名:" required prop="name">
        <el-input v-model="formAttribute.name"></el-input>
    </el-form-item>
    <el-form-item label="属性编码" required prop="code">
        <el-input v-model="formAttribute.code"></el-input>
    </el-form-item>
    <el-form-item label="默认值">
        <el-input type="textarea" :rows="2" v-model="formAttribute.defaultvalue"></el-input>
    </el-form-item>
    <el-form-item label="字段采集频次:">
        <el-select v-model="formAttribute.rate" placeholder="请选择">
            <el-option label="高频" value="3"></el-option>
            <el-option label="中频" value="2"></el-option>
            <el-option label="低频" value="1"></el-option>
        </el-select>
    </el-form-item>
    <el-form-item>
        <el-checkbox v-model="formAttribute.required" @click.native="requiredClick" :disabled="formAttribute.requiredDisable">必填</el-checkbox>
            <el-checkbox v-model="formAttribute.hidden" @click.native="hiddenClick" :disabled="formAttribute.requiredDisable">隐藏</el-checkbox>
    </el-form-item>
    <el-form-item label="文本最小长度:" required prop="params.minLength">
        <el-input-number v-model="formAttribute.params.minLength" :min="0"></el-input-number>
    </el-form-item>
    <el-form-item label="文本最大长度:" required prop="params.maxLength">
        <el-input-number v-model="formAttribute.params.maxLength" :min="1"></el-input-number>
    </el-form-item>
</el-form>
</template>
<script>
export default {
    name: 'textarea',
    props: ['textAreaChildForm','Data','edit','builtin'],
    data() {
        return {
            formAttribute:{
                keyattr: false,
                name:'多行文本',
                code:'',
                defaultvalue:'',
                required:false,
                hidden:false,
                params:{
                    minLength:1,
                    maxLength:200,
                },
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'multiRowText',
            },
            default:{
                keyattr: false,
                name:'多行文本',
                code:'',
                defaultvalue:'',
                required:false,
                hidden:false,
                params:{
                    minLength:1,
                    maxLength:200,
                },
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'multiRowText',
            },
            top:'top',
            editIndex:'',
            list2Data: {},
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
                    validator:this.checkMax,
                    trigger: 'blur',
                }],

            },
        }
    },
    mounted: function() {
        if(this.textAreaChildForm !=null){
            this.formAttribute = this.textAreaChildForm;
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('textAreaChildForm', function(newVal, oldVal) {
            //sconsole.log("---textarea");
            this.formAttribute = this.textAreaChildForm;
        }, {deep: true});
    },
    methods:{
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
                if(this.edit == p){
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
        checkMax(rule, value, callback){
            if(value<=this.formAttribute.params.minLength){
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
            var flag=false;
            this.$refs['formAttribute'].validate((valid) => {
                if (valid) {
                    flag = true;
                }
            });
            return flag; 
        },
        setDefault(){
            this.formAttribute = JSON.parse(JSON.stringify(this.default));
        }
    }
}
</script>