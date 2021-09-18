<template>
    <el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
        <el-form-item>
            <div>
                关键属性:
                <el-tooltip content="唯一KEY作为入库配置的重要识别码，由一个或多个关键属性构成" placement="bottom">
                    <i style="margin-left:20px;" class="fa fa-question-circle-o" aria-hidden="true"></i>
                </el-tooltip>&nbsp&nbsp&nbsp
                <el-checkbox v-model="formAttribute.keyattr" v-if="builtin=='false'" @click.native="keyattrClick"></el-checkbox>
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
            值：
            <el-button size="small" style="margin-left:200px;" @click="addOption">添加</el-button>
        </div>
        <el-form-item v-for="(item,index) in formAttribute.params.options" :key="index" :prop="'params.options['+index+'].name'" :rules="[{required: true, message: '请填写选项名称！', trigger: 'blur'},{validator:checkOptionName,trigger: 'blur'}]">
            <div>
                <el-input size="small" v-model="item.name" style="float:left;width:180px;margin-left:20px;"></el-input>
                <el-input size="small" v-model="item.value" style="float:left;width:50px;margin-left:20px;"></el-input>
                <i class="fa fa-plus-square-o" @click="addChildOption(index)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
                <i class="el-icon-delete" @click="deleteOption(index)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
            </div>
            <div v-for="(it,ins) in formAttribute.params.options[index].children" :key="index+'-'+ins">
                <div>
                    <el-input size="small" v-model="it.name" style="float:left;width:160px;margin-left:40px;"></el-input>
                    <el-input size="small" v-model="it.value" style="float:left;width:50px;margin-left:20px;"></el-input>
                    <i class="fa fa-plus-square-o" @click="addChild2Option(index,ins)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
                    <i class="el-icon-delete" @click="deleteOption2(index,ins)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
                </div>
                <div v-for="(it3,ins3) in it.children" :key="ins+'-'+ins3">
                    <el-input size="small" v-model="it3.name" style="float:left;width:140px;margin-left:60px;"></el-input>
                    <el-input size="small" v-model="it3.value" style="float:left;width:50px;margin-left:20px;"></el-input>
                    <i class="el-icon-delete" @click="deleteOption3(index,ins,ins3)" style="float:left;margin-left:20px;line-height:35px;cursor:pointer"></i>
                </div>
            </div>
            </div>
        </el-form-item>
    </el-form>
</template>
<script>
export default {
    name: 'cascader',
    props: ['cascaderChildForm', 'Data', 'edit','builtin'],
    data() {
        return {
            formAttribute: {
                name: '级联选择',
                code: '',
                defaultvalue: '',
                required: false,
                params: {
                    options: [{
                            "name": "",
                            "value":"",
                        },
                        {
                            "name": "",
                            "value":"",
                        }
                    ],
                },
                hidden: false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin: false,
                type: 'cascader',
            },
            default: {
                name: '级联选择',
                code: '',
                defaultvalue: '',
                required: false,
                params: {
                    options: [{
                            "name": "",
                            "value":"",
                        },
                    ],
                },
                hidden: false,
                keyattr: false,
                requiredDisable: false,
                rate:'',
                builtin: false,
                type: 'cascader',
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
            },
        }
    },
    mounted: function() {
        if (this.cascaderChildForm != null) {
            this.formAttribute = this.parse(this.cascaderChildForm);
        }
    },
    watch: {
        cascaderChildForm: {
            handler: function(val, oldVal) {
                console.log("cascader  change")
                this.formAttribute = this.parse(this.cascaderChildForm);
            },
            deep: true,
        }
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
        addOption() {
            this.formAttribute.params.options.push({ "default": false, "name": '' });
        },
        deleteOption(index) {
            if (this.formAttribute.params.options.length == 1) {
                this.$notify({
                    title: '提示',
                    message: '下拉列表至少包含一个选项',
                    type: 'error',
                    duration: 3000
                });
                return false;
            }
            this.formAttribute.params.options.splice(index, 1);
        },
        deleteOption2(index1, index2) {
            this.formAttribute.params.options[index1].children.splice(index2, 1);
        },
        deleteOption3(index1, index2, index3) {
            this.formAttribute.params.options[index1].children[index2].children.splice(index3, 1);
        },
        addChildOption(index1) {
            if (this.formAttribute.params.options[index1]['children'] == undefined) {
                this.$set(this.formAttribute.params.options[index1],'children',[{ "name": "" }]);
            } else {
                this.formAttribute.params.options[index1].children.push({ "name": "" });
            }
            console.log(JSON.stringify(this.formAttribute.params.options));

        },
        addChild2Option(index1, index2) {
            if (this.formAttribute.params.options[index1].children[index2]['children'] == undefined ) {
                this.$set(this.formAttribute.params.options[index1].children[index2],'children',[{ "name": "" }]);
            } else {
                this.formAttribute.params.options[index1].children[index2].children.push({ "name": "" });
            }
            console.log(JSON.stringify(this.formAttribute.params.options));
        },
        check(index) {
            for (var i = 0; i < this.formAttribute.params.options.length; i++) {
                if (i == index) {
                    continue;
                }
                this.formAttribute.params.options[i]['default'] = false;
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
        checkOptionName(rule, value, callback){
            var index = rule.field.replace(/[^0-9]/ig,"");
            let tmp = this.formAttribute.params.options;
            let curOption = this.formAttribute.params.options[index]
            if(curOption.value == ''){
                return callback(new Error('请填写选项值!'));
            }else{
                if(curOption.children!=undefined){
                    let cur2 = curOption.children;
                    for(let n=0;n<cur2.length;n++){
                        if(cur2[n].name == '' || cur2[n].value == ''){
                            return callback(new Error('请填写选项信息!'));
                        }else{
                            if(cur2[n].children !=undefined){
                                let cur3 = cur2[n].children;
                                for(let m=0;m<cur3.length;m++){
                                    if(cur3[m].name == ''|| cur3.value == ''){
                                        return callback(new Error('请填写选项信息!'));
                                    }
                                }
                            }
                        }
                    }
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
<style>


</style>
