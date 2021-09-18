<template>
    <el-form label-width="100px" style="padding-right:30px;">
        
        <el-form-item v-for="(item,index) in formData" :class="{keyattr:item.keyattr}" :key="item.id" :label="item.name+':'" :required="item.required">
            <!-- 文本框 -->
            <el-input v-if="item.type=='singleRowText'" v-model="item.defaultvalue"></el-input>
            <!-- 多行本文 -->
            <el-input v-if="item.type=='multiRowText'" type="textarea" v-model="item.defaultvalue"></el-input>
            <!-- 富文本 -->
            <vue-editor v-if="item.type=='richText'" v-model="item.defaultvalue"></vue-editor>
            <!-- 下拉 -->
            <el-select v-if="item.type=='listSel'" v-model="item.defaultvalue" placeholder="请选择">
                <el-option v-for="it in item.formOptions" :key="it.name" :label="it.name" :value="it.name">
                </el-option>
            </el-select>
            <!-- 单选 -->
            <el-radio-group v-if="item.type=='singleSel'" v-model="item.defaultvalue">
                <el-radio v-for="it in item.formOptions" :label="it.name">{{it.name}}</el-radio>
            </el-radio-group>
            <!-- 多选 -->
            <el-checkbox-group v-if="item.type=='multiSel'" v-model="item.selectoptions">
                <el-checkbox v-for="it in item.options" :label="it"></el-checkbox>
            </el-checkbox-group>
            <!-- 整数 -->
            <el-input v-if="item.type=='int'" v-model="item.defaultvalue"></el-input>
            <!-- 小数 -->
            <el-input v-if="item.type=='double'" v-model="item.defaultvalue" placeholder="请输入内容"></el-input>
            <!-- 附件 -->
            <el-upload v-if="item.type=='file'" class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
            <!-- 图片 -->
            <el-upload v-if="item.type=='image'" class="upload-demo" action="*" accept="image/jpeg,image/png">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
            </el-upload>
            <!-- 日期时间 -->
            <el-date-picker v-if="item.type=='dateTime' && item.formParams[0].value=='false'" v-model="item.defaultvalue" type="datetime" placeholder="选择日期时间"></el-date-picker>
            <!-- 日期 -->
            <el-date-picker v-if="item.type=='dateTime' && item.formParams[0].value=='true'" v-model="item.defaultvalue" type="date" placeholder="选择日期">
            </el-date-picker>
            <!-- 人员 -->
            <el-select v-if="item.type=='user'" v-model="item.defaultvalue" placeholder="请选择人员">
                <el-option v-for="it in userOptions" :key="it.name" :label="it.name" :value="it.name">
                </el-option>
            </el-select>
            <!-- 级联菜单 -->
            <el-cascader v-if="item.type=='cascader'" :props="prop" :options="cascaderOptions[item.id]" v-model="item.formOptions">
            </el-cascader>
            <!-- 属性分组 -->
            <div v-if="item.type=='groupLine'">
                <div style="height:37px;background:#20a0ff;margin-left:-100px;margin-right:-30px;color:#fff;line-height:37px;border-color:#20a0ff;border-radius:4px">
                    <span style="margin-left:-90px">{{item.name}}</span>
                    <i class="el-icon-caret-bottom" style="float:right;margin-right:15px;line-height:35px;"></i>
                </div>
            </div>
            <!-- 表格 -->
            <el-table v-if="item.type=='table'" :data="tableData3" highlight-current-row @current-change="handleCurrentChange">
                <el-table-column v-for="it in item.formFields" :key="it.id" inline-template :label="it.name">
                    <div>
                        <el-input :value="row.name" size="small" v-model="row[it.key]">
                        </el-input>
                    </div>
                </el-table-column>
                <el-table-column inline-template prop="address" label="操作" width="80">
                    <div>
                        <el-dropdown @command="handleCommand" trigger="click" menu-align="start">
                            <i class="fa fa-cog"></i>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="add_row">添加行</el-dropdown-item>
                                <el-dropdown-item command="del_row">删除行</el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </el-table-column>
            </el-table>
        </el-form-item>
        
    </el-form>
</template>
<script>
import {
    VueEditor
} from 'vue2-editor'
export default {
    components: {
        VueEditor,
    },
    props: ['mid', 'flag'],
    data() {
        return {
            formData: [],
            rediovlaue: '',
            tableData3: [{
                id: 1,
                col1: '',
                col2: '',
                col3: ''
            }, {
                id: 2,
                col1: '',
                col2: '',
                col3: '',
            }],
            input: '',
            tableCol: [],
            currentRow: null, //表格当前选定的行
            userOptions: [],
            cascaderOptions: null,
            prop: {
                value: 'name',
                label: 'name',
                children: 'children',
            }
        }
    },
    mounted: function() {
        /* this.$watch('mid', function(newVal, oldVal) {
             
         }, {
             deep: true
         });*/
    },
    watch: {
        mid: function(newVal, oldVal) {
            this.formData = [];
            this.getFormWidget(this.mid);
            //处理表格数据
            //this.handleTableData(this.formData);
        },
        flag: function(newVal, oldVal) {
            this.formData = [];
            this.getFormWidget(this.mid);
        }
    },

    methods: {
        onSubmit() {
            console.log('submit!');
        },
        //获取当前模块的表单信息
        getFormWidget(id) {
            $.ajax({
                url: '/cmdb/form/getForms',
                type: 'POST',
                data: {
                    "id": id
                },
                dataType: 'json',
            }).done(function(data) {
                if (data.formData != undefined) {
                    this.cascaderOptions = data.cssops;
                    this.parseData(data.formData);
                }
            }.bind(this));
        },
        handleTableData(data) {
            let tmpObj = {}
            for (var i = 0; i < data.length; i++) {
                if (data[i].type == 'table') {
                    let col = data[i].formFields;
                    for (var j = 0; j < col.length; j++) {
                        this.tableCol.push(col.key);
                    }
                }
            }
        },

        handleCommand(val) {
            if (val == 'add_row') { //增加行
                let tmp = {
                    id: this.uuid(),
                    col1: "",
                    col2: "",
                    col3: "",
                }
                this.tableData3.push(tmp);
            } else { //删除行
                console.log("当前删除行:" + this.currentRow.id);
                for (var i = 0; i < this.tableData3.length; i++) {
                    if (this.tableData3[i].id == this.currentRow.id) {
                        this.tableData3.splice(i, 1);
                        break;
                    }
                }
            }
        },
        //生成唯一的UUID
        uuid() {
            function S4() {
                return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
            }
            return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
        },
        handleCurrentChange(row) {
            console.log("当前行：" + JSON.stringify(row));
            this.currentRow = row;
        },
        //获取人员相关信息
        getUserOptions() {

        },
        parseData(data) {
            for (var i = 0; i < data.length; i++) {
                if (data[i].type == 'multiSel') {
                    var tmp = {
                        "moduleid": data[i].moduleid,
                        "name": data[i].name,
                        "code": data[i].code,
                        "type": data[i].type,
                        "defaultvalue": data[i].value,
                        "required": data[i].required == 'true' ? true : false,
                        "hidden": data[i].hidden == 'true' ? true : false,
                        "keyattr": data[i].keyattr == 'true' ? true : false,
                        "rate": data[i].rate,
                        "unit": data[i].unit,
                        "sortindex": data[i].sortindex,
                    }
                    let options = data[i].formOptions;
                    let allOptions = [];
                    let seleOp = [];
                    for (var j = 0; j < options.length; j++) {
                        allOptions.push(options[j].name);
                        if (options[j].isdefault == "true") {
                            seleOp.push(options[j].name);
                        }
                    }
                    tmp['options'] = allOptions;
                    tmp['selectoptions'] = seleOp;
                    this.formData.push(tmp);
                } else if(data[i].type == 'singleSel'){
                    var tmp = {
                        "moduleid": data[i].moduleid,
                        "name": data[i].name,
                        "code": data[i].code,
                        "type": data[i].type,
                        "defaultvalue": data[i].value,
                        "required": data[i].required == 'true' ? true : false,
                        "hidden": data[i].hidden == 'true' ? true : false,
                        "keyattr": data[i].keyattr == 'true' ? true : false,
                        "rate": data[i].rate,
                        "unit": data[i].unit,
                        "sortindex": data[i].sortindex,
                        "formOptions":data[i].formOptions,
                    }
                    let options = data[i].formOptions;
                    for (var j = 0; j < options.length; j++) {
                        if (options[j].isdefault == "true") {
                            tmp.defaultvalue = options[j].name;
                            break;
                        }
                    }
                    this.formData.push(tmp);
                } else {
                    data[i].required = data[i].required == 'true' ? true : false;
                    data[i].hidden = data[i].hidden == 'true' ? true : false;
                    data[i].keyattr = data[i].keyattr == 'true' ? true : false;
                    data[i].builtin = data[i].builtin == 'true' ? true : false;
                    let tmp = JSON.parse(JSON.stringify(data[i]));
                    this.formData.push(tmp);
                }
            }
            this.deleteEmptyProperty(this.cascaderOptions)
        },
        isEmpty(object) {
            for (var name in object) {
                return false;
            }
            return true;
        },
        //删除对象中空对象，空属性
        deleteEmptyProperty(object) {
            for (var i in object) {
                var value = object[i];
                // sodino.com
                // console.log('typeof object[' + i + ']', (typeof value));
                if (typeof value === 'object') {
                    if (Array.isArray(value)) {
                        if (value.length == 0) {
                            delete object[i];
                            continue;
                        }
                    }
                    this.deleteEmptyProperty(value);
                    if (this.isEmpty(value)) {
                        delete object[i];
                    }
                } else {
                    if (value === '' || value === null || value === undefined) {
                        delete object[i];
                    } else {
                    }
                }
            }
        }
    }
}

</script>
<style>
.el-form-item.is-required.keyattr .el-form-item__label:before {
    content: '\F084 *';
    color: #ff4949;
    margin-right: 4px;
    font: normal normal normal 14px/1 FontAwesome;
    display: inline-block;
}
</style>
