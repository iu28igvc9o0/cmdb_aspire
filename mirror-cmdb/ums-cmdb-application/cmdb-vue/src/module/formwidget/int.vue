<template>
    <el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
        <div style="margin-bottom:15px;">
            关键属性:
            <el-tooltip content="唯一KEY作为入库配置的重要识别码，由一个或多个关键属性构成" placement="bottom">
                <i style="margin-left:20px;" class="fa fa-question-circle-o" aria-hidden="true"></i>
            </el-tooltip>&nbsp&nbsp&nbsp
            <el-checkbox v-if="builtin=='false'" v-model="formAttribute.keyattr" @click.native="keyattrClick"></el-checkbox>
        </div>
        <el-form-item label="字段名称:" prop="name" required>
            <el-input v-model="formAttribute.name"></el-input>
        </el-form-item>
        <el-form-item label="属性编码:" prop="code" required>
            <el-input v-model="formAttribute.code"></el-input>
        </el-form-item>
        <el-form-item label="默认值:" prop="defaultvalue">
            <el-input v-model="formAttribute.defaultvalue"></el-input>
        </el-form-item>
        <el-form-item label="单位:">
            <el-input v-model="formAttribute.unit"></el-input>
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
        <el-form-item label="最小值:" required prop="params.min">
            <el-input-number v-model="formAttribute.params.min" :min="1"></el-input-number>
        </el-form-item>
        <el-form-item label="最大值:" required prop="params.max">
            <el-input-number v-model="formAttribute.params.max" :min="3"></el-input-number>
        </el-form-item>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
    name: 'int',
    props: ['intChildForm', 'Data', 'edit','builtin'],
    data() {
        return {
            formAttribute: {
                name: '整数',
                code: '',
                defaultvalue: '',
                required: false,
                unit: '',
                params: {
                    min: 1,
                    max: 200,
                },
                hidden: false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'int',
                
            },
            default: {
                name: '整数',
                code: '',
                defaultvalue: '',
                required: false,
                unit: '',
                params: {
                    min: 1,
                    max: 200,
                },
                hidden: false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'int',
                
            },
            top: 'top',
            editIndex: '',
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
                'params.max':[{
                    validator:this.checkMax,
                    trigger: 'blur',
                }],
                defaultvalue:[{
                    validator:this.checkInt,
                    trigger: 'blur',
                }],
            },
        }
    },
    mounted: function() {
        if(this.intChildForm !=null){
            this.formAttribute = this.parse(this.intChildForm);
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('intChildForm', function(newVal, oldVal) {
            console.log("---int");
            this.formAttribute = this.parse(this.intChildForm);
        }, {
            deep: true
        });
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
        keyattrClick() {
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
        checkMax(rule, value, callback){
            if(value<=this.formAttribute.params.min){
                return callback(new Error('抱歉，最大值必须大于最小值!'));
            }
            callback();
        },
        checkInt(rule, value, callback){
            var c=/^-?\d+$/;
            if($.trim(value)!='' && !c.test(value)){
                return callback(new Error('抱歉，整数的默认值必须为整数!'));
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
