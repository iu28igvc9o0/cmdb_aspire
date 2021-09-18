<template>
<el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
    <el-form-item label="字段名:" prop="name" required>
        <el-input v-model="formAttribute.name"></el-input>
    </el-form-item>
    <el-form-item label="属性编码" prop="code" required>
        <el-input v-model="formAttribute.code"></el-input>
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
    
</el-form>
</template>
<script>
export default {
    name: 'richtext',
    props: ['richTextChildForm','Data','edit','builtin'],
    data() {
        return {
            formAttribute:{
                name:'富文本',
                code:'',
                defaultvalue:'',
                required:false,
                hidden:false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'richText',
            },
            default:{
                name:'富文本',
                code:'',
                defaultvalue:'',
                required:false,
                hidden:false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'richText',
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
            },
        }
    },
    mounted: function() {
        if(this.richTextChildForm !=null){
            this.formAttribute = this.richTextChildForm;
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('richTextChildForm', function(newVal, oldVal) {
            //sconsole.log("---textarea");
            this.formAttribute = this.richTextChildForm;
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