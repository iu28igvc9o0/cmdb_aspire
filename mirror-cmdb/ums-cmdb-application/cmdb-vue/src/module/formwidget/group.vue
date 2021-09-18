<template>
<el-form :label-position="top" label-width="80px" :model="formAttribute" ref="formAttribute" :rules="fromrules">
    <el-form-item label="分组名称:" required prop="name">
        <el-input v-model="formAttribute.name"></el-input>
    </el-form-item>
</el-form>
</template>
<script>
export default {
    name: 'group',
    props: ['groupChildForm','Data','edit'],
    data() {
        return {
            formAttribute:{
                keyattr: false,
                name:'属性分组',
                code:'group',
                defaultvalue:'',
                required:false,
                hidden:false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'groupLine',
            },
            default:{
                keyattr: false,
                name:'属性分组',
                code:'group',
                defaultvalue:'',
                required:false,
                hidden:false,
                requiredDisable: false,
                rate:'',
                builtin:false,
                type:'groupLine',
            },
            top:'top',
            editIndex:'',
            list2Data: {},
            fromrules: {
                name: [{
                    required: true,
                    message: '请输入名称',
                    trigger: 'blur',
                },{
                    min: 1,
                    max: 16,
                    message: '长度在 1 到 16 个字符',
                    trigger: 'blur',
                }],
            },
        }
    },
    mounted: function() {
        if(this.groupChildForm !=null){
            this.formAttribute = this.groupChildForm;
        }
        //childFormData父类传递的参数值发生变化时调用
        this.$watch('groupChildForm', function(newVal, oldVal) {
            //sconsole.log("---textarea");
            this.formAttribute = this.groupChildForm;
        }, {deep: true});
    },
    methods:{
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