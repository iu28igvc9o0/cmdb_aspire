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
        <el-form-item label="默认值:">
            <el-date-picker v-if="formAttribute.params.formatDate==false" v-model="formAttribute.defaultvalue" type="datetime" format="yyyy-MM-dd HH:mm:ss" @change="dateChange">
            </el-date-picker>
            <el-date-picker v-if="formAttribute.params.formatDate==true" v-model="formAttribute.defaultvalue" type="date" format="yyyy-MM-dd" @change="dateChange">
            </el-date-picker>
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
        <el-form-item>
            <label>仅日期:</label><el-checkbox style="float:right;margin-right:140px" v-model="formAttribute.params.formatDate" @click.native="onlyDateClick"></el-checkbox>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
    name: 'datetime',
    props: ['dateTimeChildForm','Data','edit','builtin'],
    data() {
        return {
            formAttribute: {
                name: '时间',
                code: '',
                defaultvalue: '',
                required: false,
                hidden:false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                params:{
                    formatDate:false,
                },
                builtin:false,
                type:'dateTime',
            },
            default: {
                name: '时间',
                code: '',
                defaultvalue: '',
                required: false,
                hidden:false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                params:{
                    formatDate:false,
                },
                builtin:false,
                type:'dateTime',
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
        if(this.dateTimeChildForm != null){
            this.formAttribute = this.parse(this.dateTimeChildForm);
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('dateTimeChildForm', function(newVal, oldVal) {
            console.log("---dateTimeChildForm");
            this.formAttribute = this.parse(this.dateTimeChildForm);
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
        dateChange(val){
            this.formAttribute.defaultvalue = val;
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
        onlyDateClick(){
            console.log(JSON.stringify(this.formAttribute));
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
