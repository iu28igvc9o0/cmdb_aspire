<template>
    <el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
        <el-form-item>
            <div>
                关键属性:
                <el-tooltip content="唯一KEY作为入库配置的重要识别码，由一个或多个关键属性构成" placement="bottom">
                    <i style="margin-left:20px;" class="fa fa-question-circle-o" aria-hidden="true"></i>
                </el-tooltip>&nbsp&nbsp&nbsp
                <el-checkbox v-if="builtin=='false'" v-model="formAttribute.keyattr" @click.native="keyattrClick"></el-checkbox>
            </div>
        </el-form-item>
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
        <div style="margin-bottom:20px;">
        值：<el-button size="small" style="margin-left:200px;" @click="addOption">添加</el-button>
        </div>
        <el-form-item v-for="(item,index) in formAttribute.params.options" :prop="'params.options['+index+'].name'" :rules="[{required: true, message: '请填写选项的名称', trigger: 'blur'},{validator:checkOptionName,trigger: 'blur'}]">
            <el-checkbox v-model="item.isdefault" @click.native="check(index)" style="float:left;"></el-checkbox>
            <el-input v-model="item.name" style="float:left;width:180px;margin-left:20px;"></el-input>
            <el-input v-model="item.value" style="float:left;width:70px;margin-left:20px;"></el-input>
            <i class="el-icon-delete" @click="deleteOption(index)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
    name: 'single',
    props: ['singleChildForm','Data','edit','builtin'],
    data() {
        return {
            formAttribute: {
                name: '单选',
                code: '',
                defaultvalue: '',
                required: false,
                params:{
                    options: [{
                        "isdefault": false,
                        "name": "单选1",
                        "value":"",
                    }, {
                        "isdefault": false,
                        "name": "单选2",
                        "value":"",
                    }, {
                        "isdefault": false,
                        "name": "单选3",
                        "value":"",
                    }],
                },
                hidden:false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'singleSel',

            },
            default: {
                name: '单选',
                code: '',
                defaultvalue: '',
                required: false,
                params:{
                    options: [{
                        "isdefault": false,
                        "name": "单选1",
                        "value":"",
                    }, {
                        "isdefault": false,
                        "name": "单选2",
                        "value":"",
                    }, {
                        "isdefault": false,
                        "name": "单选3",
                        "value":"",
                    }],
                },
                hidden:false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'singleSel',

            },
            top: 'top',

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
        if(this.singleChildForm !=null){
            this.formAttribute = this.parse(this.singleChildForm);
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('singleChildForm', function(newVal, oldVal) {
            //sconsole.log("---textarea");
            this.formAttribute = this.parse(this.singleChildForm);
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
        addOption(){
            this.formAttribute.params.options.push({"isdefault":false,"name":''});
        },
        deleteOption(index){
            if(this.formAttribute.params.options.length==1){
                this.$notify({
                    title: '提示',
                    message: '单选至少包含一个选项',
                    type: 'error',
                    duration: 3000
                });
                return false;
            }
            this.formAttribute.params.options.splice(index,1);
        },
        check(index){
            for(var i=0;i<this.formAttribute.params.options.length;i++){
                if(i==index){
                    continue;
                }
                this.formAttribute.params.options[i]['isdefault']=false;
            }
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
        //校验选项名称
        checkOptionName(rule, value, callback){
            let tmp = this.formAttribute.params.options;
            var index = rule.field.replace(/[^0-9]/ig,"");
            let tmpvalue = this.formAttribute.params.options[index].value;
            if($.trim(tmpvalue)==''){
                return callback(new Error('请填写选项值！'));
            }
            for(let i=0;i<tmp.length;i++){
                if(i==index){
                    continue;
                }
                if(tmp[i].name == $.trim(value)){
                    return callback(new Error('选项名称已经存在！'));
                }
                if(tmpvalue == tmp[i].value){
                    return callback(new Error('选项值已经存在！'));
                }
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
<style>

</style>