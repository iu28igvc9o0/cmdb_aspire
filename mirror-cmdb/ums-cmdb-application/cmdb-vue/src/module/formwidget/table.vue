<template>
    <el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
        
        <el-form-item label="表格名称:" prop="name" required>
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
        列名：<el-button size="small" style="margin-left:200px;" @click="addOption">添加</el-button>
        </div>
        <el-form-item v-for="(item,index) in formAttribute.params.fields" :prop="'params.fields['+index+'].name'" :rules="[{required: true, message: '请填写表格列名称', trigger: 'blur'},{validator:checkName,trigger:'blur'}]">
            <el-input v-model="item.name" style="float:left;width:200px;margin-left:20px;"></el-input>
            <i class="el-icon-delete" @click="deleteOption(index)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
    name: 'table',
    props: ['tableChildForm','Data','edit','builtin'],
    data() {
        return {
            formAttribute: {
                name: '表格',
                code: '',
                defaultvalue: '',
                required: false,
                hidden:false,
                params:{
                    fields: [{
                        "key": "col1",
                        "name": "列1",
                    }, {
                        "key": "col2",
                        "name": "列2",
                    }, {
                        "key": "col3",
                        "name": "列3",
                    }],
                },
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'table',
            },
            default: {
                name: '表格',
                code: '',
                defaultvalue: '',
                required: false,
                hidden:false,
                params:{
                    fields: [{
                        "key": "col1",
                        "name": "列1",
                    }, {
                        "key": "col2",
                        "name": "列2",
                    }],
                },
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'table',
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
        if(this.tableChildForm !=null){
            this.formAttribute = this.tableChildForm;  
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('tableChildForm', function(newVal, oldVal) {
            //sconsole.log("---textarea");
            console.log(JSON.stringify(this.tableChildForm));
            this.formAttribute = this.tableChildForm;   
        }, {
            deep: true
        });
    },
    methods: {
        addOption(){
            var len = this.formAttribute.params.fields.length+1;
            this.formAttribute.params.fields.push({"key":'col'+len,"name":'列'+len});
        },
        deleteOption(index){
            if(this.formAttribute.params.fields.length==1){
                this.$notify({
                    title: '提示',
                    message: '表格至少包含一列',
                    type: 'error',
                    duration: 3000
                });
                return false;
            }
            this.formAttribute.params.fields.splice(index,1);
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
        checkName(rule, value, callback){
            let tmp = this.formAttribute.params.fields;
            var index = rule.field.replace(/[^0-9]/ig,"");
            for(let i=0;i<tmp.length;i++){
                if(i==index){
                    continue;
                }
                if(tmp[i].name == $.trim(value)){
                    return callback(new Error('列名已存在'));
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