<template>
    <div id="content">
        <div style="height:42px;border-bottom:1px solid #d1dbe5;">
           
            <div id="tb" style="float:left;width:100%;">
                <el-tabs v-model="activeName" @tab-click="handleClick">
                    <el-tab-pane label="模型属性" name="modelattr">
                        <div id="form" :class="{disnone:isDisnone}">
                            <el-form ref="form" :model="form" label-width="80px">
                                <el-form-item v-for="item in form.data" :label="item.name" :key="item.id">
                                    <el-input v-if="item.type=='singleRowText'" v-model="item.defaultvalue"></el-input>
                                    <el-select v-else-if="item.type=='listSel'" v-model="form.value" placeholder="请选择">
                                        <el-option v-for="o in item.params.options" :key="o.default" :label="o.name" :value="o.default">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-form>
                        </div>
                        <!--表单控件编辑区域-->
                        
                        <div id="eidtform" :class="{disnone:!isDisnone}">
                            <div class="attredit">
                                <draggable element="span" v-model="list2" :options="dragOptions3" @add="onAdd" :move="onMove" @end="onEnd">
                                    <transition-group name="no" class="list-group ul-group" tag="ul">
                                        <li class="list-group-item" v-for="element in list2" :key="element.name" @click="edit(element.k_id,element.type)">
                                            <el-form label-width="100px" :class="{keyattr:element.keyattr}">
                                                <el-form-item :label="element.name+':'" :required="element.require"  >
                                                    <div v-if="element.type=='groupLine'">
                                                        <div class="group">
                                                            <span style="margin:0px 10px 0px 10px">{{element.name}}</span>
                                                            <i class="el-icon-caret-bottom" style="float:right;margin-right:15px;line-height:30px;"></i>
                                                        </div>
                                                    </div>
                                                    <el-input v-else :disabled="true" size="small" v-model="element.defaultvalue"></el-input>
                                                </el-form-item>
                                                <el-button :disabled="element.builtin==true" type="primary" icon="delete" size="small" @click="deleteAttr(element.k_id)"></el-button>
                                                <i  v-if="element.type=='multiRowText' || element.type=='richText' || element.type=='int' || element.type=='double' || element.type=='table'" class="fa fa-link fa-lg" style="margin-left:10px;" @click="addScript(element.k_id)"></i>
                                                <i  v-if="element.type=='singleRowText' && element.codeflag!='Y_name'" class="fa fa-link fa-lg" style="margin-left:10px;" @click="addScript(element.k_id)"></i>
                                            </el-form>
                                        </li>
                                    </transition-group>
                                </draggable>
                            </div>
                            <div class="attrtype">
                            <div style="width:100%;height:36px;background:#20a0ff;border-radius:4px;text-align:left;line-height:36px;"><span style="color:#fff;font-size:14px;margin-left:10px;">属性类型</span></div>
                                <div class="prompt"><i class="fa fa-exclamation-circle"></i>属性类型可以拖动到左侧编辑区域哦！</div>
                                <draggable class="list-group" element="ul" v-model="list" :options="dragOptions4" :move="onMove" @start="isDragging=true" @end="onEnd" :disabled="true">
                                    <transition-group class="ul" type="transition" :name="'flip-list'">
                                        <li class="list-group-before" v-for="element in list" :class="{groupLine:element.type=='groupLine'}" :key="element.type">
                                            <i :class="element.icon" aria-hidden="true"></i> {{element.name}}
                                        </li>
                                        
                                    </transition-group>
                                </draggable>
                            </div>
                            <el-dialog title="新增脚本" v-model="scriptDialog" size="small" @open="scriptDialogOpen">
                                <el-form :model="formScript" ref="formScript" :rules="formScriptRule" class="form-script">
                                       
                                        <el-form-item label="标签" prop="tag" required style="width:50%">
                                            <el-select v-model="formScript.tag">
                                                <el-option v-for="item in tags" :key="item.id" :label="item.tag" :value="item.id"></el-option>
                                            </el-select>
                                        </el-form-item>
                                        <el-form-item label="脚本类型" prop="language" required style="width:45%">
                                            <el-select v-model="formScript.language" @change = "scriptTypeChange">
                                                <el-option v-for="item in scriptType" :key="item.name" :label="item.name" :value="item.value"></el-option>
                                            </el-select>
                                        </el-form-item>

                                    <el-form-item label=" " prop="script" required>
                                    <codemirror :value="formScript.script" :options="editorOption" ref="myEditor" @change="yourCodeChangeMethod"></codemirror>
                                    </el-form-item>
                                    <el-col style="margin:15px 0px 15px 0px;text-align:center;">
                                        <el-button type="primary" @click="btn_script_submit('formScript')">保存</el-button>
                                        <el-button @click="script_close('formScript')">取消</el-button>
                                    </el-col>
                                </el-form>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="textDialog" size="tiny" :before-close="beforeClose" :modal="false">
                               <textField ref="textForm" :textChildForm="textChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></textField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="textAreaDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <textAreaField ref="textAreaForm" :textAreaChildForm="textAreaChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></textAreaField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="richTextDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <richTextField ref="richTextForm" :richTextChildForm="richTextChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></richTextField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="selectDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <selectField ref="selectForm" :selectChildForm="selectChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></selectField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="singleDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <singleField ref="singleForm" :singleChildForm="singleChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"> </singleField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="multiDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <multiField ref="multiForm" :multiChildForm="multiChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></multiField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="intDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <intField ref="intForm" :intChildForm="intChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></intField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="doubleDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <doubleField ref="doubleForm" :doubleChildForm="doubleChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></doubleField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="fileDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <fileField ref="fileForm" :fileChildForm="fileChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></fileField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="imageDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <imageField ref="imageForm" :imageChildForm="imageChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></imageField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="dateTimeDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <dateTimeField ref="dateTimeForm" :dateTimeChildForm="dateTimeChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></dateTimeField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="dateDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <dateField ref="dateForm" :dateChildForm="dateChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></dateField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="userDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <userField ref="userForm" :userChildForm="userChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></userField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="referDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <referField ref="referForm" :referChildForm="referChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></referField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="tableDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <tableField ref="tableForm" :tableChildForm="tableChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></tableField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="cascaderDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <cascaderField ref="cascaderForm" :cascaderChildForm="cascaderChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></cascaderField>
                            </el-dialog>
                            <el-dialog title="编辑属性" v-model="groupDialog" size="tiny" :before-close="beforeClose" :modal="false">
                                <groupField ref="groupForm" :groupChildForm="groupChildForm" :Data="list2Data" :edit="editIndex" :builtin="mdouleisbuiltin"></groupField>
                            </el-dialog>
                        </div>
                         
                        <!--表单显示区域-->
                       
                        <div id="showform" :class="{disnone:isDisnone}" style="padding:30px 50px 20px 50px">
                            <div>
                                <FormWid ref="Form" :mid="c_mid" :flag="form_flag"></FormWid>
                            </div>
                        </div>
                    </el-tab-pane>
                     
                    <el-tab-pane label="模型关系" name="modelrelation">
                       <ModuleRelation :pid="p_mid"></ModuleRelation>
                    </el-tab-pane>
                </el-tabs>
            </div>
            <div id="eidt" style="float:right;margin-left:-200px;position:relative;">
                <el-button :class="{disnone:isDisnone}" @click="btn_edit">编辑</el-button>
                <el-button type="primary" :loading="btn_submit_loading" :class="{disnone:!isDisnone}" @click="btn_submit">保存</el-button>
                <el-button :class="{disnone:!isDisnone}" @click="btn_cancel">取消</el-button>
            </div>
        </div>
    </div>
</template>
<script>
//表单空间数据
const message = [ {
    name: '属性分组',
    type: 'groupLine',
    icon: 'fa fa-clone'
},{
    name: '单行文本',
    type: 'singleRowText',
    icon: 'fa fa-text-height'
}, {
    name: '多行文本',
    type: 'multiRowText',
    icon: 'fa fa-list'
}, {
    name: '富文本',
    type: 'richText',
    icon: 'fa fa-pencil-square-o'
}, {
    name: '下拉菜单',
    type: 'listSel',
    icon: 'fa fa-chevron-circle-down'
}, {
    name: '单选',
    type: 'singleSel',
    icon: 'fa fa-dot-circle-o'
}, {
    name: '多选',
    type: 'multiSel',
    icon: 'fa fa-check-square-o'
}, {
    name: '整数',
    type: 'int',
    icon: 'fa fa-align-justify'
}, {
    name: '小数',
    type: 'double',
    icon: 'fa fa-align-center'
}, {
    name: '附件',
    type: 'file',
    icon: 'fa fa-paperclip'
}, {
    name: '日期时间',
    type: 'dateTime',
    icon: 'fa fa-clock-o'
},/* {
    name: '日期',
    type: 'date',
    icon: 'fa fa-calendar-minus-o'
}, {//人员下版本实现
    name: '人员',
    type: 'user',
    icon: 'fa fa-user-circle-o'
},*/ {
    name: '图片',
    type: 'image',
    icon: 'fa fa-picture-o'
}, {
    name: '表格',
    type: 'table',
    icon: 'fa fa-table'
}, {
    name: '级联选择',
    type: 'cascader',
    icon: 'fa fa-caret-square-o-down'
}]
//编辑器
import { codemirror } from 'vue-codemirror-lite'
require('codemirror/mode/javascript/javascript')
require('codemirror/mode/shell/shell')
require('codemirror/mode/ruby/ruby')
require('codemirror/mode/python/python')
require('codemirror/addon/selection/active-line.js')

require('codemirror/addon/hint/show-hint.js')
require('codemirror/addon/hint/show-hint.css')
require('codemirror/addon/hint/javascript-hint.js')
require('codemirror/addon/hint/anyword-hint.js')

require('codemirror/theme/base16-dark.css')
require('codemirror/mode/clike/clike.js')
require('codemirror/addon/edit/matchbrackets.js')
require('codemirror/addon/comment/comment.js')
require('codemirror/addon/dialog/dialog.js')
require('codemirror/addon/dialog/dialog.css')
require('codemirror/addon/search/searchcursor.js')
require('codemirror/addon/search/search.js')
require('codemirror/keymap/sublime.js')

import draggable from 'vuedraggable';
import FormWid from './form.vue';

import textField from './formwidget/text.vue';
import textAreaField from './formwidget/textarea.vue';
import selectField from './formwidget/select.vue';
import richTextField from './formwidget/richtext.vue';
import singleField from './formwidget/single.vue';
import multiField from './formwidget/multi.vue';
import intField from './formwidget/int.vue';
import doubleField from './formwidget/double.vue';
import fileField from './formwidget/file.vue';
import imageField from './formwidget/image.vue';
import dateTimeField from './formwidget/datetime.vue';
import dateField from './formwidget/date.vue';
import userField from './formwidget/user.vue';
import referField from './formwidget/refer.vue';
import tableField from './formwidget/table.vue';
import cascaderField from './formwidget/cascader.vue';
import groupField from './formwidget/group.vue';
import ModuleRelation from '../maintain/moduleRelation.vue';
export default {
    name: 'content',
    components: {
        draggable,
        codemirror,
        FormWid,

        textField,
        textAreaField,
        selectField,
        richTextField,
        singleField,
        multiField,
        intField,
        doubleField,
        fileField,
        imageField,
        dateTimeField,
        dateField,
        userField,
        referField,
        tableField,
        cascaderField,
        groupField,
        ModuleRelation,
    },
    props: ['emid','amid','mdouleisbuiltin'], //module.vue传递过来的参数
    data() {
        return {
            activeName: "modelattr",
            form: {
                data: [],
                value: '',
            },
            isDisnone: false,
            //表单控件参数
            list: message.map((name, index) => {
                return {
                    name: message[index].name,
                    type: message[index].type,
                    icon: message[index].icon,
                    fixed: false,
                    order: index + 1
                };
            }),
            list2: [], //存储主页面表单主键的基本信息（tpye_id(表单主键的类型),k_id(唯一标识该主键),icon(图标))
            editable: true,
            isDragging: false,
            delayedDragging: false,
            scriptDialog: false,
            btn_submit_loading:false,//保存表单按钮loading状态

            currentIndex: '', //存放当前空间的k_id（唯一标识控件）
            editIndex: '', //当前编辑的k_id
            editFlg:'',
            currentDialog: '',
            //textChildForm: '',
            //textAreaChildForm:'',
            ChildForm: '', //传递给子Dialog的参数
            list2Data: {}, //key_id为改表单插件的ID,存储表单数据
            list2Script: {}, //key_id为改表单插件的ID，存储表单脚本数据
            listTemp: [],
            c_mid: '', //当前点击的模型ID
            p_mid:'',
            isbuiltin:'',
            form_flag: false, //标记表单是否从新加载
            //表单控件参数信息
            textDialog: false,
            textAreaDialog: false,
            selectDialog: false,
            richTextDialog: false,
            singleDialog: false,
            multiDialog: false,
            intDialog: false,
            doubleDialog: false,
            fileDialog: false,
            imageDialog: false,
            dateTimeDialog: false,
            dateDialog: false,
            userDialog: false,
            referDialog: false,
            tableDialog: false,
            cascaderDialog: false,
            groupDialog: false,
            //控件数据
            textChildForm: null,
            textAreaChildForm: null,
            richTextChildForm: null,
            selectChildForm: null,
            singleChildForm: null,
            multiChildForm: null,
            intChildForm: null,
            doubleChildForm: null,
            fileChildForm: null,
            imageChildForm: null,
            dateTimeChildForm: null,
            dateChildForm: null,
            userChildForm: null,
            referChildForm: null,
            tableChildForm: null,
            cascaderChildForm: null,
            groupChildForm: null,
            //表单脚本
            scriptType: [{
                name: "Python",
                value:"python"
            }, {
                name: "Shell",
                value:"shell"
            }],//脚本类型
            tags: [],//模型标签选项
            formScript: {
                tag: '',
                language: '',
                script: '',
            },
            defaultFormScript:{
                tag: '',
                language: '',
                script: '',
            },
            formScriptRule: {
                tag: [{
                    required: true,
                    message: '请选择标签',
                    trigger: 'change',
                }],
                language: [{
                    required: true,
                    message: '请选择脚本类型',
                    trigger: 'change',
                }],
                script: [{
                    required: true,
                    message: '请输入脚本内容',
                    trigger: 'blur',
                }],
            },
            //编辑器
            code:"#/usr/bin/python",
            editorOption: {
                tabSize: 4,
                lineNumbers: true,
                mode: "text/x-python",
                theme:'base16-dark',
                styleActiveLine: true,
                autoCloseBrackets:true,
                extraKeys: { 'Ctrl-Space': 'autocomplete' },
            },
        }
    },
    mounted: function() {
        //编辑模型是传递过来的moduleId
        this.$watch('emid', function(newVal, oldVal) {
            this.c_mid = this.emid;
            this.p_mid = this.emid;
            this.isDisnone = false;
            this.activeName="modelattr";
            this.getModuleTags(this.emid); //加载该模块的标签
        });
        //新增模型传递过来的moduleId
        this.$watch('amid', function(newVal, oldVal) {
            this.c_mid = this.amid;
            this.p_mid = this.amid;
            this.btn_edit();
            this.isDisnone = true;
            this.getModuleTags(this.amid); //加载该模块的标签
        });
        //表示该module是否内置
        this.$watch('mdouleisbuiltin', function(newVal, oldVal) {
            this.isbuiltin = this.mdouleisbuiltin;
        });
        //模型属性才会呈现编辑按钮
        this.$watch('activeName', function(newVal, oldVal) {
            if (this.activeName == 'modelattr') {
                $("#eidt").css("display", "block");
            } else {
                $("#eidt").css("display", "none");
            }
        }, {
            deep: true
        });
        this.$watch('isDisnone', function(newVal, oldVal) {
            if (newVal == false) {
                this.getFormWidget(this.c_mid)
            }
        });
    },
    created:function(){
        //编辑模型成功后，重新加载数据
        this.$root.Bus.$on('flushForm', value => {
            this.getModuleTags(value);
        })    
    },
    methods: {
        handleClick(tab) {
            if (tab.$props.name == 'modelattr') {
                this.isDisnone = false;
            } else {
                this.isDisnone = true;
            }
        },
        //表单控件的方法
        orderList() {
            this.list = this.list.sort((one, two) => {
                return one.order - two.order;
            })
        },
        onAdd(evt) {
            //this.formAttribute.fieldName=$.trim(evt.clone.innerText);
            //this.formAttribute.fieldName=this.list[evt.oldIndex].name;
            //根据表单类型选择弹出框
            
            if (this.list[evt.oldIndex].type == 'singleRowText') {
                this.textDialog = true;
            } else if (this.list[evt.oldIndex].type == 'multiRowText') {
                this.textAreaDialog = true;
            } else if (this.list[evt.oldIndex].type == 'listSel') {
                this.selectDialog = true;
            } else if (this.list[evt.oldIndex].type == 'richText') {
                this.richTextDialog = true;
            } else if (this.list[evt.oldIndex].type == 'singleSel') {
                this.singleDialog = true;
            } else if (this.list[evt.oldIndex].type == 'multiSel') {
                this.multiDialog = true;
            } else if (this.list[evt.oldIndex].type == 'int') {
                this.intDialog = true;
            } else if (this.list[evt.oldIndex].type == 'double') {
                this.doubleDialog = true;
            } else if (this.list[evt.oldIndex].type == 'file') {
                this.fileDialog = true;
            } else if (this.list[evt.oldIndex].type == 'image') {
                this.imageDialog = true;
            } else if (this.list[evt.oldIndex].type == 'dateTime') {
                this.dateTimeDialog = true;
            } else if (this.list[evt.oldIndex].type == 'date') {
                this.dateDialog = true;
            } else if (this.list[evt.oldIndex].type == 'user') {
                this.userDialog = true;
            } else if (this.list[evt.oldIndex].type == 'reference') {
                this.referDialog = true;
            } else if (this.list[evt.oldIndex].type == 'table') {
                this.tableDialog = true;
            } else if (this.list[evt.oldIndex].type == 'cascader') {
                this.cascaderDialog = true;
            } else if (this.list[evt.oldIndex].type == 'groupLine') {
                this.groupDialog = true;
            }
            this.currentIndex = this.guid(); //生成唯一的一个ID
           
            this.list2 = this.objDeepCopy(this.list2);

            this.list2[evt.newIndex].k_id = this.currentIndex;
            this.list2[evt.newIndex].require = false;
            this.list2[evt.newIndex].defaultvalue = "";
            this.list2[evt.newIndex].keyattr = false;
            this.list2[evt.newIndex].builtin = false; //是否内置
            this.list2[evt.newIndex].codeflag = ""; //是否内置
            //设置当前编辑的表单控件k_id 为空
            this.editIndex = '';
            /*this.list2 = this.list2.map((name, index) => {
                return {
                    name: this.list2[index].name,
                    type: this.list2[index].type,
                    icon: this.list2[index].icon,
                    fixed: this.list2[index].fixed,
                    k_id: index,
                    require: this.list2[index].require,
                    defaultValue: this.list2[index].defaultValue,
                };
            })*/

        },
        onEnd(evt) {
            // / console.log(JSON.stringify(this.formAttribute));
            //this.formAttribute.fieldName = this.list[evt.oldIndex].name;
            //this.dialogVisible = true;
            //this.list2[]
            //console.log(evt);
        },
        deleteAttr(k_id) {
            event.stopPropagation(); //阻止事件的传播
            //删除表单元素

            for (var i = 0; i < this.list2.length; i++) {
                if (this.list2[i].k_id == k_id) {
                    this.list2.splice(i, 1);
                    break;
                }
            }
            delete this.list2Data[k_id];
            delete this.list2Script[k_id];
        },
        edit(k_id, type) {
            //this.childFormData
            let tmpData = this.list2Data[k_id];
            this.editIndex = k_id;
            this.currentIndex = k_id;

            //根据表单类型选择弹出框
            if (type == 'singleRowText') {
                this.textDialog = true;
                this.textChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'multiRowText') {
                this.textAreaDialog = true;
                this.textAreaChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'listSel') {
                this.selectDialog = true;
                this.selectChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'richText') {
                this.richTextDialog = true;
                this.richTextChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'singleSel') {
                this.singleDialog = true;
                this.singleChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'multiSel') {
                this.multiDialog = true;
                this.multiChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'int') {
                this.intDialog = true;
                this.intChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'double') {
                this.doubleDialog = true;
                this.doubleChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'file') {
                this.fileDialog = true;
                this.fileChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'image') {
                this.imageDialog = true;
                this.imageChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'dateTime') {
                this.dateTimeDialog = true;
                this.dateTimeChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'date') {
                this.dateDialog = true;
                this.dateChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'user') {
                this.userDialog = true;
                this.userChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'reference') {
                this.referDialog = true;
                this.referChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'table') {
                this.tableDialog = true;
                this.tableChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'cascader') {
                this.cascaderDialog = true;
                this.cascaderChildForm = JSON.parse(JSON.stringify(tmpData));
            } else if (type == 'groupLine') {
                this.groupDialog = true;
                this.groupChildForm = JSON.parse(JSON.stringify(tmpData));
            }

        },
        onMove({
            relatedContext,
            draggedContext
        }) {

            const relatedElement = relatedContext.element;
            const draggedElement = draggedContext.element;
            return (!relatedElement || !relatedElement.fixed) && !draggedElement.fixed
        },
        //关闭前的回调
        beforeClose(done) {
            var type = null;
            var index = null;
            for (var i = 0; i < this.list2.length; i++) {
                if (this.list2[i].k_id == this.currentIndex) {
                    index = i;
                    type = this.list2[i].type;
                    break;
                }
            }
            var form = ""; //表单域的值
            let tmpop = "";
            switch (type) {
                case 'singleRowText':
                    form = this.$refs.textForm.formAttribute;
                    break;
                case 'multiRowText':
                    form = this.$refs.textAreaForm.formAttribute;
                    break;
                case 'listSel':
                    form = this.$refs.selectForm.formAttribute;
                    //设置用户选择的默认值
                    form.defaultvalue = "";
                    tmpop = form.params.options;
                    for(let i=0;i<tmpop.length;i++){
                        if(tmpop[i].isdefault == true){
                            form.defaultvalue = tmpop[i].name;
                            break;
                        }
                    }
                    break;
                case 'richText':
                    form = this.$refs.richTextForm.formAttribute;
                    break;
                case 'singleSel':
                    form = this.$refs.singleForm.formAttribute;
                    //设置用户选择的默认值
                    form.defaultvalue = "";
                    tmpop = form.params.options;
                    for(let i=0;i<tmpop.length;i++){
                        if(tmpop[i].isdefault == true){
                            form.defaultvalue = tmpop[i].name;
                            break;
                        }
                    }
                    break;
                case 'multiSel':
                    form = this.$refs.multiForm.formAttribute;
                    //设置用户选择的默认值
                    form.defaultvalue = "";
                    tmpop = form.params.options;
                    for(let i=0;i<tmpop.length;i++){
                        if(tmpop[i].isdefault == true){
                            if(form.defaultvalue == ""){
                                form.defaultvalue = form.defaultvalue+tmpop[i].name;
                            }else{
                                form.defaultvalue = form.defaultvalue+","+tmpop[i].name;
                            }
                        }
                    }
                    break;
                case 'int':
                    form = this.$refs.intForm.formAttribute;
                    break;
                case 'double':
                    form = this.$refs.doubleForm.formAttribute;
                    break;
                case 'file':
                    form = this.$refs.fileForm.formAttribute;
                    break;
                case 'image':
                    form = this.$refs.imageForm.formAttribute;
                    break;
                case 'dateTime':
                    form = this.$refs.dateTimeForm.formAttribute;
                    break;
                case 'date':
                    form = this.$refs.dateForm.formAttribute;
                    break;
                case 'user':
                    form = this.$refs.userForm.formAttribute;
                    break;
                case 'reference':
                    form = this.$refs.referForm.formAttribute;
                    break;
                case 'table':
                    form = this.$refs.tableForm.formAttribute;
                    break;
                case 'cascader':
                    form = this.$refs.cascaderForm.formAttribute;
                    break;
                case 'groupLine':
                    form = this.$refs.groupForm.formAttribute;
                    break;
            }
            //表单校验不成功，则不能关闭Dialog
           /* if (!re) {
                return false;
            }*/
            this.list2[index].name = $.trim(form.name);
            if (form.required == true) {
                this.list2[index]['require'] = true;
            } else {
                this.list2[index]['require'] = false;
            }
            if (form.keyattr == true) {
                this.list2[index]['keyattr'] = true;
            } else {
                this.list2[index]['keyattr'] = false;
            }
            this.list2[index]['defaultvalue'] = form.defaultvalue;
            this.list2[index]['codeflag'] = form.code;
            var a = this.currentIndex;
            //深拷贝，否则两个会指向同一个地址

            this.list2Data[a] = this.objDeepCopy(form);
            //操作完成，关闭Dialog
            done();
            this.resetDialog(type);//设置各个Dialog表单为默认

        },
        resetDialog(type){
            switch (type) {
                case 'singleRowText':
                    this.$refs.textForm.setDefault();
                    break;
                case 'multiRowText':
                    this.$refs.textAreaForm.setDefault();
                    break;
                case 'listSel':
                    this.$refs.selectForm.setDefault();
                    break;
                case 'richText':
                    this.$refs.richTextForm.setDefault();
                    break;
                case 'singleSel':
                    this.$refs.singleForm.setDefault();
                    break;
                case 'multiSel':
                    this.$refs.multiForm.setDefault();
                    break;
                case 'int':
                    this.$refs.intForm.setDefault();
                    break;
                case 'double':
                    this.$refs.doubleForm.setDefault();
                    break;
                case 'file':
                    this.$refs.fileForm.setDefault();
                    break;
                case 'image':
                    this.$refs.imageForm.setDefault();
                    break;
                case 'dateTime':
                    this.$refs.dateTimeForm.setDefault();
                    break;
                case 'date':
                    this.$refs.dateForm.setDefault();
                    break;
                case 'user':
                    this.$refs.userForm.setDefault();
                    break;
                case 'reference':
                    this.$refs.referForm.setDefault();
                    break;
                case 'table':
                    this.$refs.tableForm.setDefault();
                    break;
                case 'cascader':
                    this.$refs.cascaderForm.setDefault();
                    break;
                case 'groupLine':
                    this.$refs.groupForm.setDefault();
                    break;
            }
        },
        //数组深拷贝的方法
        deepCopy(source) {
            var result = {};
            for (var key in source) {
                result[key] = typeof source[key] === 'object' ? this.deepCoyp(source[key]) : source[key];
            }
            return result;
        },
        //对象数组深拷贝
        objDeepCopy(source) {
            var sourceCopy = source instanceof Array ? [] : {};
            for (var item in source) {
                sourceCopy[item] = typeof source[item] === 'object' ? this.objDeepCopy(source[item]) : source[item];
            }
            return sourceCopy;
        },
        //生成唯一的UUID
        guid() {
            function S4() {
                return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
            }
            return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
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
                this.list2Data = {};
                this.list2 = [];
                this.list2Script = {};
                this.editFormWidget(data);
            }.bind(this));
        },
        //编辑表单控件
        editFormWidget(val) {
            if (val.formData == undefined) {
                return;
            }
            var data = val.formData;
            for (var i = 0; i < data.length; i++) {
                var tmp = {
                    "name": data[i].name,
                    "type": data[i].type,
                    "order": i + 1,
                    "k_id": data[i].id,
                    "require": data[i].required == 'true' ? true : false,
                    "defaultvalue": data[i].defaultvalue == null ? "" : data[i].defaultvalue,
                    "keyattr": data[i].keyattr == 'true' ? true : false,
                    "builtin": data[i].builtin == 'true' ? true : false,
                    "codeflag":data[i].code,
                }
                this.list2.push(tmp);
                var dataTmp = {
                    "id": data[i].id,
                    "name": data[i].name,
                    "type": data[i].type,
                    "code": data[i].code,
                    "defaultvalue": data[i].defaultvalue,
                    "keyattr": data[i].keyattr == 'true' ? true : false,
                    "required": data[i].required == 'true' ? true : false,
                    "hidden": data[i].hidden == 'true' ? true : false,
                    "builtin": data[i].builtin == 'true' ? true : false,
                    "rate":data[i].rate,
                }
                if (data[i].type == 'multiRowText'|| data[i].type == 'dateTime' || data[i].type == 'singleRowText' || data[i].type == 'double' || data[i].type == 'int') {
                    let tmpparam = data[i].formParams;
                    let params = {};
                    for (var j = 0; j < tmpparam.length; j++) {
                        let key = tmpparam[j].key
                        if(key == 'formatDate'){
                            params[key] = tmpparam[j].value == 'true' ? true : false;
                        }else{
                            params[key] = tmpparam[j].value;
                        }
                    }
                    dataTmp['params'] = params;
                } else if (data[i].type == 'singleSel' || data[i].type == 'multiSel' || data[i].type == 'listSel' || data[i].type == 'listSel') {
                    let tmpoptions = data[i].formOptions;
                    let params = {};
                    params['options'] = tmpoptions;
                    dataTmp['params'] = params;
                } else if (data[i].type == 'table') {
                    let tmpfield = data[i].formFields;
                    let params = {};
                    params['fields'] = tmpfield;
                    dataTmp['params'] = params;
                } else if (data[i].type == 'cascader') {
                    let params = {};
                    params['options'] = val.cssops[data[i].id];
                    dataTmp['params'] = params;
                }
                this.list2Data[data[i].id] = dataTmp;
                this.list2Script[data[i].id] = data[i].formScript;
            }

        },
        btn_edit() {
            this.isDisnone = true;
            this.getFormWidget(this.c_mid);
        },
        //取消编辑的表单信息
        btn_cancel(){
        	this.$confirm('是否确认取消？', '警告', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.isDisnone=!this.isDisnone;
            }).catch(() => {
                this.$notify({
                    title: '提示',
                    message: '已取消退出',
                    type: 'info',
                    duration:3000
                });
            });
        },
        //保存选择的表单信息
        btn_submit() {
            this.form_flag = false; //更新表单之前，将标志 表单是否从新加载的变量 置为false，
            var forms = [];
            let re = this.valid();
            if(re!=true){
                return;
            }
            this.btn_submit_loading = true;//按钮变为loading
            for (var i = 0; i < this.list2.length; i++) {
                let key = this.list2[i].k_id;
                this.list2Data[key]['sortindex'] = forms.length + 1;
                this.list2Data[key]['script'] = this.list2Script[key]
                forms.push(this.list2Data[key]);
            }
            var params = {
                "mid": this.p_mid,
                "forms": forms
            };
            $.ajax({
                url: '/cmdb/form/updateForm',
                type: 'POST',
                cache: false,
                async: true,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(params),
                success: function(data) {
                    this.btn_submit_loading = false;
                    if (data.success == true) {
                        this.$notify({
                            title: '提示',
                            message: '保存成功!',
                            type: 'success',
                            duration: 3000
                        });
                        //添加成功切换到表单显示页面
                        this.isDisnone = false;
                        this.c_mid = this.p_mid;
                        this.form_flag = true;
                    }else{
                        this.$notify({
                            title: '提示',
                            message: data.message,
                            type: 'error',
                            duration: 3000
                        });
                    }
                }.bind(this),
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    this.btn_submit_loading = false;
                    this.$notify({
                        title: '提示',
                        message: '保存失败',
                        type: 'error',
                        duration: 3000
                    });
                }.bind(this),
            });
        },
        valid(){
            let re = "";
            for(var key in this.list2Data){
                var tmp_form_data=this.list2Data[key];
                let type = tmp_form_data.type;
                var allcode= this.get_allCode(key);
                switch (type) {
                    case 'singleRowText':
                        re = this.textValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'multiRowText':
                        re = this.textAreaValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'listSel':
                        re = this.listValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'richText':
                        re = this.richValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'singleSel':
                        re = this.singleValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'multiSel':
                        re = this.multiValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'int':
                        re = this.intValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'double':
                        re = this.doubleValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'file':
                        re = this.fileValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'image':
                        re = this.imageValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'dateTime':
                        re = this.dateValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'date':
                        re = this.dateValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'user':
                        re = this.userValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'table':
                        re = this.tableValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'cascader':
                        re = this.cascaderValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                    case 'groupLine':
                        re = this.groupValid(tmp_form_data,allcode);
                        if(re!=true){
                            this.edit(key,type);
                            this.$notify({title: '提示',message: re,type: 'warning',duration: 3000});
                        }
                        break;
                }
                if(re!=true){
                    break;
                }else{
                    continue;
                }
            }   
            return re;
        },
        get_allCode(k_id){
            let tmp = this.list2Data;

            
            let result = [];
            for(var key in tmp){
                if(key == k_id){
                    continue;
                }
                result.push(tmp[key].code);
            }
            return result;
        },
        handleOrder() {
            return tmpArray;
        },
        //表单脚本
        addScript(k_id) {
            event.stopPropagation();
            this.editIndex = k_id;
            if(this.list2Script[k_id] != undefined){
                this.formScript = this.list2Script[k_id];
               /* $.ajax({
                    url: '/cmdb/form/getScriptByformId',
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        formId:k_id,
                    }
                }).done(function(data) {
                   this.formScript =data;
                }.bind(this));*/
            }else{
                this.formScript = JSON.parse(JSON.stringify(this.defaultFormScript));
            }
            this.scriptDialog = true;
        },
        getModuleTags(mid) {
            $.ajax({
                url: '/cmdb/module/getModuleTags',
                type: 'POST',
                dataType: 'json',
                data: {
                    moduleId: mid,
                }
            }).done(function(data) {
                this.tags = data;
            }.bind(this));
        },
        btn_script_submit(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    let a = this.editIndex;
                    this.list2Script[a] = JSON.parse(JSON.stringify(this.formScript));
                    this.scriptDialog = false;
                } else {
                    return false;
                }
            });
        },
        script_close(formName){
            this.scriptDialog = false;
        },
        scriptTypeChange(val){
            if(val=='python'){
                this.editorOption.mode = 'text/x-python';
            }else if(val == 'shell'){
                this.editorOption.mode = 'text/x-sh';
            }
        },
        scriptDialogOpen(){
            this.getModuleTags(this.p_mid);
        },
        yourCodeChangeMethod(val) {
            this.formScript.script = val;
        },
        flush_form(){
            //this.form_flag = !this.form_flag;
            //this.getFormWidget(this.c_mid);
            this.isDisnone=false;
            this.activeName="modelattr";
        },
        textValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            if(vaildData.params.minLength>vaildData.params.maxLength){
                return "最小长度必须小与最大长度!";
            }
            if($.trim(vaildData.defaultvalue).length>vaildData.params.maxLength){
                return "默认值必须小于最大长度";
            }
            return true;
        },
        textAreaValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            if(vaildData.params.minLength > vaildData.params.maxLength){
                return "最小长度必须小与最大长度!";
            }
            if($.trim(vaildData.defaultvalue).length > vaildData.params.maxLength){
                return "默认值必须小于最大长度";
            }
            return true;
        },
        userValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            return true;
        },
        tableValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            let fields = vaildData.params.fields;
            for(let i=0;i<fields.length;i++){
                if($.trim(fields[i].name)==''){
                   return "请填写列名信息";
                   break; 
                }
                if($.trim(fields[i].name).length<1 || $.trim(fields[i].name).length>16){
                    return "列名长度必须大于1小于16";
                    break;
                }
            }
            return true;
        },
        singleValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            let fields = vaildData.params.options;
            for(let i=0;i<fields.length;i++){
                if($.trim(fields[i].name)==''){
                   return "请填写选项名称";
                   break; 
                }
                if($.trim(fields[i].value)==''){
                   return "请填写选项值";
                   break; 
                }
                if($.trim(fields[i].name).length<1 || $.trim(fields[i].name).length>16){
                    return "选项名称长度必须大于1小于16";
                    break;
                }
                if($.trim(fields[i].value).length<1 || $.trim(fields[i].value).length>16){
                    return "选项值长度必须大于1小于16";
                    break;
                }
            }
            return true;
        },
        listValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            let fields = vaildData.params.options;
            for(let i=0;i<fields.length;i++){
                if($.trim(fields[i].name)==''){
                   return "请填写选项名称";
                   break; 
                }
                if($.trim(fields[i].value)==''){
                   return "请填写选项值";
                   break; 
                }
                if($.trim(fields[i].name).length<1 || $.trim(fields[i].name).length>16){
                    return "选项名称长度必须大于1小于16";
                    break;
                }
                if($.trim(fields[i].value).length<1 || $.trim(fields[i].value).length>16){
                    return "选项值长度必须大于1小于16";
                    break;
                }
            }
            return true;
        },
        richValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            return true;
        },
        multiValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            let fields = vaildData.params.options;
            for(let i=0;i<fields.length;i++){
                if($.trim(fields[i].name)==''){
                   return "请填写选项名称";
                   break; 
                }
                if($.trim(fields[i].value)==''){
                   return "请填写选项值";
                   break; 
                }
                if($.trim(fields[i].name).length<1 || $.trim(fields[i].name).length>16){
                    return "选项名称长度必须大于1小于16";
                    break;
                }
                if($.trim(fields[i].value).length<1 || $.trim(fields[i].value).length>16){
                    return "选项值长度必须大于1小于16";
                    break;
                }
            }
            return true;
        },
        intValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            var g=/^-?\d+$/;
            if($.trim(vaildData.defaultvalue)!='' && !g.test(vaildData.defaultvalue)){
                return "整数的默认值必须为整数!";
            }
            if(vaildData.params.min>vaildData.params.max){
                return "最小值必须小与最大值!";
            }
            if(parseInt($.trim(vaildData.defaultvalue))>vaildData.params.max){
                return "默认值必须小于最大值";
            }
            return true;
        },
        groupValid(vaildData,allcode){
            if($.trim(vaildData.name)==""){
                return "名称必须填写";
            }
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "分组名称必须大于1个字符，小于16个字符";
            }
            return true;
        },
        fileValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            return true;
        },
        imageValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            return true;
        },
        doubleValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            if(vaildData.params.min>vaildData.params.max){
                return "最小值必须小与最大值!";
            }
            var c =new RegExp("^-?\\d+\\.?\\d{0,"+parseInt(vaildData.params.precision)+"}$");
            if($.trim(vaildData.defaultvalue) !='' && !c.test($.trim(vaildData.defaultvalue))){
                return "默认值必须是有效的数,且至少保留"+vaildData.params.precision+"位小数!";  
            }
            if(parseInt($.trim(vaildData.defaultvalue))>vaildData.params.max){
                return "默认值必须小于最大值";
            }
            return true;
        },
        dateValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            return true;
        },
        cascaderValid(vaildData,allcode){
            if($.trim(vaildData.name).length<1 || $.trim(vaildData.name).length>16){
                return "名称必须大于1个字符，小于16个字符";
            }
            if($.trim(vaildData.code).length<1 || $.trim(vaildData.code).length>16){
                return "编码必须大于1个字符，小于16个字符";
            }
            var c = /^[a-zA-Z_0-9]+$/;
            if(!c.test(vaildData.code)){
                return "编码只可输入字母、数字、下划线";
            }
            if(allcode.indexOf(vaildData.code)!=-1){
                return "该编码已被占用!";
            }
            let fields = vaildData.params.options;
            for(let i=0;i<fields.length;i++){
                if($.trim(fields[i].name)==''){
                   return "请填写选项名称";
                   break; 
                }
                if($.trim(fields[i].value)==''){
                   return "请填写选项值";
                   break; 
                }
                if($.trim(fields[i].name).length<1 || $.trim(fields[i].name).length>16){
                    return "选项名称长度必须大于1小于16";
                    break;
                }
                if($.trim(fields[i].value).length<1 || $.trim(fields[i].value).length>16){
                    return "选项值长度必须大于1小于16";
                    break;
                }
                if(fields[i].children!=undefined){
                    let ch2 = fields[i].children;
                    for(let j=0;j<ch2.length;j++){
                        if($.trim(ch2[j].name)==''){
                           return "请填写选项名称";
                           break; 
                        }
                        if($.trim(ch2[j].value)==''){
                           return "请填写选项值";
                           break; 
                        }
                        if($.trim(ch2[j].name).length<1 || $.trim(ch2[j].name).length>16){
                            return "选项名称长度必须大于1小于16";
                            break;
                        }
                        if($.trim(ch2[j].value).length<1 || $.trim(ch2[j].value).length>16){
                            return "选项值长度必须大于1小于16";
                            break;
                        }
                        if(ch2[j].children!=undefined){
                            let ch3 = ch2[j].children;
                            for(let k=0;k<ch3.length;k++){
                                if($.trim(ch3[k].name)==''){
                                   return "请填写选项名称";
                                   break; 
                                }
                                if($.trim(ch3[k].value)==''){
                                   return "请填写选项值";
                                   break; 
                                }
                                if($.trim(ch3[k].name).length<1 || $.trim(ch3[k].name).length>16){
                                    return "选项名称长度必须大于1小于16";
                                    break;
                                }
                                if($.trim(ch3[k].value).length<1 || $.trim(ch3[k].value).length>16){
                                    return "选项值长度必须大于1小于16";
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return true; 
        }


    },
    computed: {
        dragOptions3() {
            return {
                animation: 0,
                group: {
                    name: 'mjy',
                    pull: 'true',

                },
                disabled: !this.editable,
                ghostClass: 'ghost',
            };
        },
        dragOptions4() {
            return {
                animation: 0,
                group: {
                    name: 'mjy',
                    pull: 'clone',
                    put: 'false',
                },
                disabled: !this.editable,
                ghostClass: 'ghost',
                sort: false
            };
        },
    },
    watch: {
        isDragging(newValue) {
            if (newValue) {
                this.delayedDragging = true
                return
            }
            this.$nextTick(() => {
                this.delayedDragging = false
            })
        }
    }
}

</script>
<style>
.editStyle{border:1px solid red;}
.keyattr .el-form-item.is-required .el-form-item__label:before {
    content: '\F084 *';
    color: #ff4949;
    margin-right: 4px;
    font: normal normal normal 14px/1 FontAwesome;
    display: inline-block;
}
#tb .el-tabs__header {
    border: 0px;
    margin-bottom: 0px;
}

.attredit .el-form-item {
    width: 80%;
    float: left;
    margin-bottom: 0px;
}
.list-group .el-input .el-input__inner{cursor: move;}
.list-group-item *{cursor: move;}

.ul-group{padding-bottom: 60px;}

#showform .el-form-item__label{
    overflow:hidden;
    height: 36px;
}

/*设置表单控件name超出宽度显示省略号*/

.attredit .el-form-item__label {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.disnone {
    display: none;
}

.container {}

.attrtype {
    width: 30%;
    padding: 30px 10px 0px 40px;
    border-left: 1px solid #d1dbe5;
    float: right;
    font-size: 12px;
    color: #48576a;
}

.attredit {
    width: 60%;
    float: left;
    padding-top: 30px;
    font-size: 12px;
}

.prompt {
    margin: 30px 0px 20px 0px;
    width: 100%;
    height: 25px;
    border: 1px dashed #d1dbe5;
    font-size: 12px;
    color: #48576a;
    text-align: center;
    line-height: 25px;
}







/*拖动到控件区域未放下时li的样式*/

.attredit .list-group-before {
    list-style-type: none;
    border: 1px dashed #d1dbe5;
    background: #fff;
    width: 90%;
    margin: 0 auto;
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
    cursor: move;
}







/*li*/

.attrtype .list-group-before {
    width: 43%;
    height: 25px;
    list-style-type: none;
    border: 1px solid #C2F0FF;
    margin-top: 10px;
    text-align: left;
    padding-left: 10px;
    line-height: 25px;
    float: left;
    cursor: move;
}







/*ul*/

.attrtype .ul {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    width: 100%;
}

#tb .el-tabs__content {
    padding-top: 1px;
}

.list-group-item {
    cursor: move;
    list-style-type: none;
    height: 40px;
    padding: 0px 0px 0px 10px;
    line-height: 35px;
    border: 0px;
}

.attredit .list-group-item button {
    margin-left: 10px;
}

.list-group-item:after {
    content: ".";
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
}

.flip-list-move {
    transition: transform 0.5s;
}

.no-move {
    transition: transform 0s;
}

.ghost {
    opacity: .5;
    background: #C8EBFB;
}

.list-group {
    min-height: 40px;
}

.list-group-item i {
    cursor: pointer;
}

.field-name {
    float: left;
    margin-right: 20px;
}

.field-name-inner {
    float: left;
    width: 80%;
}

.field-text {
    width: 70%;
    line-height: 40px;
    border: 1px solid #ddd;
    display: inline-block;
}
.groupLine{width:300px;}
/*编辑器*/
.group{
   height:30px;
   background:#20a0ff;
   margin-top:3px;
   color:#fff;
   line-height:30px;
   margin-left:-100px;
   border-radius:4px;
   overflow: hidden;
text-overflow: ellipsis;
white-space: nowrap;
}
.form-script .el-form-item__content{line-height: 23px;font-size: 16px;}
</style>
