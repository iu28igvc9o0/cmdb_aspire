<template>
  <div id="module" class="module" style="height:100%;background:#fff;padding-bottom:30px;">
    <el-row>
      <el-col :span="4" style="border-right:1px solid #d1dbe5;height:100%;">
        <el-button type="primary" style="display:block;margin:0 auto;margin-top:20px;" @click="btn_addmodule"><i class="el-icon-plus">&nbsp</i>创建模型</el-button>
        <el-menu :default-active="activeIndex" ref="moduleMenu" :default-openeds="defaultOpenNav" :model="parentNav" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" theme="light" style="background:#fff;margin-top:20px;">
          <el-submenu v-for="(item,index) in parentNav" :index="item.id" :key="item.id">
            <template slot="title">{{item.name}}</template>
            <el-menu-item v-for="(child,ci) in item.childModules" :index="child.id" :key="child.id" @click="changeRouter(child.id,child.builtin)"><span class="menuitemName">{{child.name}}</span>
              <el-dropdown @command="handleCommand" style="float:right;">
                <i class="fa fa-cog" aria-hidden="true"></i>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="'1='+ci+'='+index">编辑</el-dropdown-item>
                  <el-dropdown-item v-if="child.builtin=='false'" :command="'2='+child.id">删除</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-menu-item>
          </el-submenu>
          <!-- s -->
        </el-menu>
      </el-col>
      <el-col :span="19" style="height:100%;padding:10px 20px 0px 20px;">
        <!--表单控件页面-->
        <div :class="{dis:display}">
          <DataCom ref="content" :emid="emid" :amid="amid" :addflag="addflag" :mdouleisbuiltin="builtinFlag"></DataCom>
        </div>
        <!--新增模型页面-->
        <div :class="{dis:!display}">
          <addModule v-on:addModuleData="getAddModuleData"></addModule>
        </div>
      </el-col>
    </el-row>
    <router-view></router-view>
    <el-dialog title="编辑模型" :visible.sync="eidtModuleDialog">
      <el-form :model="formmodule" :rules="formRules" ref="formmodule" label-width="85px">
        <el-form-item label="类型名称:" prop="name" required>
          <el-input v-model="formmodule.name" placeholder="请输入类型名称（中、英文）"></el-input>
        </el-form-item>
        <el-form-item label="模型图标:" prop="iconurl" required>
          <div class="btnIcon" @click="selectIcon" v-model="formmodule.iconurl"><span :class="{dis:icondisplay}">选择图标</span>
            <div :class="{dis:!icondisplay}" style="background:#1d6aa7;border-radius:3px;">
              <img width="30" height="30" :src="formmodule.iconurl" />
              <div style="position: absolute;padding: 0 7px;height:20px;line-height:20px;top:43px;">修改图标</div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="模型标签:" required>
          <label v-for="(item,index) in moduletags">
            <el-tag :key="item.tag" v-if="!item.edit" :closable="true" :close-transition="false" @dblclick.native="tagClick(index)" @close="handleCloseTag(index)">{{item.tag}}</el-tag>
            <el-input class="input-new-tag" v-else v-model="editValue" ref="editTagInput" size="mini" @keyup.enter.native="editInputConfirm(index)" @blur="editInputConfirm(index)"></el-input>
          </label>
          <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="mini" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 点击新增</el-button>
        </el-form-item>
        <el-form-item label="禁用:" prop="checked">
          <el-checkbox v-model="formmodule.checked"></el-checkbox>&nbsp
          <span v-if="formmodule.checked" style="color:red;">禁用后会清除该类下所有配置项</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="eidtSubmit('formmodule')">保存</el-button>
          <el-button @click="editCancel('formmodule')">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="iconDialog" class="iconDialog" @close="iconDialogClose">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="系统图标库" name="sysicon">
          <div class="sysicon" v-model="sysicons">
            <li v-for="(item,index) in sysicons" class="iconlist" :class="{liactive:activeIcon==item.id}" @click="iconClick(item)"><img width="30" height="30" :src="item.iconUrl" />
              <i :class="{active:activeIcon==item.id,nactive:activeIcon!=item.id}" class="fa fa-check-circle"></i></li>
            <!--解决浮动之后无法撑开外层div的问题-->
            <div style="clear:both;"></div>
          </div>
          <el-pagination style="margin-top:20px" @current-change="sysCurrentChange" :current-page.sync="syspage.currPage" layout="prev, pager, next, jumper" :total="syspage.total">
          </el-pagination>
          <div style="text-align:center;margin-top:20px;">
            <el-button type="primary" @click="iconcommit" :disabled="!btn_active">确定</el-button>
            <el-button @click="iconCancel">取消</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="自定义图标库" name="cusicon">
          <div class="uploadicon">
            <el-upload class="upload-demo" action="/cmdb/icon/uploadIcon" accept="image/jpeg,image/png" :on-error="uploadError" :on-success="uploadSuccess">
              <el-button size="small" type="primary">上传图标</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过200kb</div>
            </el-upload>
          </div>
          <div class="sysicon" v-model="customicons">
            <li v-for="item in customicons" class="iconlist" :class="{liactive:activeIcon==item.id}" @click="iconClick(item)"><img width="30" height="30" :src="item.iconUrl" />
              <i :class="{active:activeIcon==item.id,nactive:activeIcon!=item.id}" class="fa fa-check-circle"></i></li>
            <!--解决浮动之后无法撑开外层div的问题-->
            <div style="clear:both;"></div>
          </div>
          <el-pagination style="clear:both;margin-top:20px" @current-change="cusCurrentChange" :current-page.sync="cuspage.currPage" :page-size="100" layout="prev, pager, next, jumper" :total="cuspage.total">
          </el-pagination>
          <div style="text-align:center;margin-top:20px;">
            <el-button type="primary" @click="iconcommit" :disabled="!btn_active">确定</el-button>
            <el-button @click="iconCancel">取消</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>
<style>
.dis {
  display: none;
}

</style>
<script>
import DataCom from './content.vue'
import addModule from './addModule.vue'
export default {
  components: {
    DataCom,
    addModule,
  },
  data() {
    var checkName = (rule, val, callback) => {
      if ($.trim(val) == this.editModule.name) {
        callback();
      }
      $.ajax({
        url: '/cmdb/module/getModuleSelective',
        type: 'POST',
        dataType: 'json',
        data: {
          "name": $.trim(val)
        }
      }).done(function(data) {
        if (data.length > 0) {
          return callback(new Error('抱歉，该类型名称已被占用!'));
        }
        callback();
      });

    };
    return {
      parentNav: [], //父导航
      emid: "",
      amid: "",
      builtinFlag: false, //模型是否内置
      display: false, //设置新增模型与表单模块的显示，
      defaultOpenNav: [], //默认展开的Nav,为index属性的值
      defaultActive: null, //默认选中的菜单，为index属性的值
      activeIndex: null,
      eidtModuleDialog: false,
      btn_active: false,
      iconDialog: false,
      formmodule: {
        id: '',
        name: '',
        checked: false,
        iconurl: '',
      },
      activeName: 'sysicon',
      syspage: {
        currPage: 1,
        pageSize: 27,
        total: 0,
      },
      cuspage: {
        currPage: 1,
        pageSize: 27,
        total: 0,
      },
      moduletags: [],
      inputVisible: false,
      inputValue: '',
      editValue: 'fuck',
      icondisplay: false, //设置是否显示修改图标的div
      sysicons: [], //系统图标库
      customicons: [], //自定义图标库
      currSelectIcon: "", //当前选中的图标
      editModule: {},
      addflag: null, //标识新增的模型，传递到content.vue页面
      formRules: {
        name: [{
          min: 1,
          max: 16,
          message: '长度在 1 到 16 个字符',
          trigger: 'blur',
        }, {
          required: true,
          message: '请输入类型名称',
          trigger: 'blur'
        }, {
          validator: checkName,
          trigger: 'blur'
        }],
      },
      activeIcon: '',
    }
  },
  watch: {
    defaultActive: function(newVal, oldVal) {
      let tmp = this.defaultActive;
      this.$refs.moduleMenu.activedIndex = tmp;
    }
  },
  mounted: function() {
    this.getModule();
  },
  methods: {
    getModule(id) {
      $.ajax({
        url: '/cmdb/module/getModule',
        type: 'POST',
        dataType: 'json',
      }).done(function(data) {
        this.parentNav = data;
        if (this.parentNav != null && this.parentNav.length > 0) {
          for (var i = 0; i < this.parentNav.length; i++) {
            if (this.parentNav[i].childModules.length <= 0) {
              continue;
            } else {
              if (id == undefined) {
                this.emid = this.parentNav[i].childModules[0].id;
                this.builtinFlag = this.parentNav[i].childModules[0].builtin;
                this.defaultActive = this.emid;
              } else {
                //this.id = id;
                this.builtinFlag = false;
                this.defaultActive = id;
              }
              this.defaultOpenNav.push(this.parentNav[0].id);
              break;
            }
          }
        }

      }.bind(this));
    },
    //新增模型
    btn_addmodule() {
      this.display = true;
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    changeRouter(id, builtin) {
      //alert("aa");
      this.display = false; //设置表单叶显示
      if (this.emid == id) {
        this.$refs.content.flush_form();
      }
      this.emid = id;
      this.builtinFlag = builtin;
    },
    addModule(pid, id) {

    },
    //接收子组件传递过来的参数，存放在data，表示新增模块添加成功
    getAddModuleData(data) {
      this.getModule(data[0].id); //从新获取菜单
      this.amid = data[0].id
      //设置当前展开和选中的菜单
      this.defaultOpenNav = [];
      this.defaultOpenNav.push(data[0].parentId);
      this.defaultActive = data[0].id;; //设置当前选定的菜单
      this.display = false; //设置表单页面显示；
    },
    handleCommand(val) {
      var re = val.split("=")

      if (re[0] == '1') {
        //修改
        this.eidtModuleDialog = true;
        if (re[1] != "" && re[1] != null && re[2] != "" && re[2] != null) {
          var cindex = re[1];
          var pindex = re[2];
          var child = this.parentNav[pindex].childModules;
          this.getModuleTags(child[cindex].id);
          this.editModule = child[cindex];
          this.formmodule.id = child[cindex].id;
          this.formmodule.name = child[cindex].name;
          this.formmodule.iconurl = child[cindex].iconurl;
          this.formmodule.checked = child[cindex].disabled == 'true' ? true : false;
          this.icondisplay = true;
        }
      } else {
        //删除
        if (re[1] != "" && re[1] != null) {

          this.$confirm('确认删除该模型?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            $.ajax({
              url: '/cmdb/module/deleteModule',
              type: 'POST',
              cache: false,
              async: true,
              traditional: true,
              dataType: 'json',
              data: {
                "id": re[1]
              },
              success: function(data) {
                if (data.success == true) {
                  this.$notify({
                    title: '提示',
                    message: '删除成功!',
                    type: 'success',
                    duration: 3000
                  });
                  this.getModule();
                }

              }.bind(this),
              error: function(XMLHttpRequest, textStatus, errorThrown) {
                this.$notify({
                  title: '提示',
                  message: '删除失败',
                  type: 'error',
                  duration: 3000
                });
              }.bind(this),
            });
          }).catch(() => {
            console.log("已取消删除");
          });
        }
      }
    },
    getModuleTags(mid) {
      $.ajax({
        url: '/cmdb/module/getModuleTags',
        type: 'POST',
        data: {
          moduleId: mid
        },
        dataType: 'json',
      }).done(function(data) {
        this.moduletags = [];
        this.moduletags = data;
        var len = data.length;
        for (var i = 0; i < len; i++) {
          this.moduletags[i]['edit'] = false;
        }
      }.bind(this));
    },
    handleClick() {

    },
    //点击选择图标
    selectIcon() {
      this.iconDialog = true;
      this.getSysIcons();
      this.getCustomIcons();
    },
    //获取系统图标
    getSysIcons() {
      $.ajax({
        url: '/cmdb/icon/getIcons',
        type: 'POST',
        data: {
          "pageNumber": this.syspage.currPage,
          "pageSize": this.syspage.pageSize,
          "iconcategory": 0, //获取系统图标
        },
        dataType: 'json',
      }).done(function(data) {
        this.sysicons = data.dataList;
        this.syspage.currPage = data.pageNo;
        this.syspage.total = data.total;
      }.bind(this));
    },
    //获取自定义图标
    getCustomIcons() {
      $.ajax({
        url: '/cmdb/icon/getIcons',
        type: 'POST',
        data: {
          "pageNumber": this.cuspage.currPage,
          "pageSize": this.cuspage.pageSize,
          "iconcategory": 1, //获取自定义图标
        },
        dataType: 'json',
      }).done(function(data) {
        this.customicons = data.dataList;
        this.cuspage.currPage = data.pageNo;
        this.cuspage.total = data.total;
      }.bind(this));
    },
    //自定义图标上传成功
    uploadError(err, file, fileList) {
      this.$notify({
        title: '提示',
        message: '图标上传失败',
        type: 'error',
        duration: 3000
      });
    },
    uploadSuccess(response, file, fileList) {
      if (response.success) {
        this.getCustomIcons();
        this.$notify({
          title: '提示',
          message: '图标上传成功',
          type: 'success',
          duration: 3000
        });
      } else {
        this.$notify({
          title: '提示',
          message: 'response.message',
          type: 'error',
          duration: 3000
        });
      }
    },
    //选中icon
    iconClick(item) {
      this.activeIcon = item.id;
      this.currSelectIcon = item.iconUrl;
      this.btn_active = true;
    },
    //确定选择的图标
    iconcommit() {
      this.formmodule.iconurl = this.currSelectIcon;
      this.iconDialog = false;
      this.icondisplay = true;
    },
    //图标选择Dialog 取消按钮
    iconCancel() {
      this.iconDialog = false;
    },
    iconDialogClose() {
      this.currSelectIcon = "";
      this.syspage.currPage = 1;
      this.cuspage.currPage = 1;
      this.activeIcon = null;
      this.btn_active = false;
    },
    //系统图标分页
    sysCurrentChange(val) {
      this.syspage.currPage = val;
      this.getSysIcons();
    },
    //自定义图标分页
    cusCurrentChange(val) {
      this.cuspage.currPage = val;
      this.getCustomIcons();
    },

    //重置模型编辑框表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.icondisplay = false;
      this.currSelectIcon = "";
    },
    editCancel(formName) {
      this.resetForm(formName);
      this.eidtModuleDialog = false;
    },
    //提交编辑
    eidtSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var data = {
            "id": this.formmodule.id,
            "name": this.formmodule.name,
            "iconurl": this.formmodule.iconurl,
            "disabled": this.formmodule.checked == true ? 'true' : 'false',
            "tags": this.moduletags,
          };
          $.ajax({
            url: '/cmdb/module/updateModule/',
            type: 'POST',
            cache: false,
            async: true,
            traditional: true,
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data, textStatus) {
              if (data.success == true) {
                this.$notify({
                  title: '提示',
                  message: '修改成功!',
                  type: 'success',
                  duration: 3000
                });
                this.getModule(this.formmodule.id);//从新获取模型信息
                this.emid = this.formmodule.id;
                this.eidtModuleDialog = false;
                this.$root.Bus.$emit('flushForm',this.formmodule.id);//传递到content组件更新表单信息
              } else {
                this.$notify({
                  title: '提示',
                  message: '修改失败!',
                  type: 'error',
                  duration: 3000
                });
              }

            }.bind(this),
            error: function(XMLHttpRequest, textStatus, errorThrown) {
              this.$notify({
                title: '提示',
                message: '修改失败',
                type: 'error',
                duration: 3000
              });
            }.bind(this),
          });
        }
      });
    },
    handleCloseTag(index) {
      if (this.moduletags[index].id != undefined) {
        $.ajax({
          url: '/cmdb/module/getScriptByTagId/',
          type: 'POST',
          cache: false,
          async: true,
          traditional: true,
          data: { "tagId": this.moduletags[index].id },
          success: function(data, textStatus) {
            if (null != data && data.length > 0) {
              this.$confirm('该标签已设置脚本，确认删除？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                //用户确认删除
                this.moduletags.splice(index, 1);
              }).catch(() => {
                console.log("已取消删除");
              });
            } else {
              this.moduletags.splice(index, 1);
            }
          }.bind(this),
          error: function(XMLHttpRequest, textStatus, errorThrown) {
            this.$notify({
              title: '提示',
              message: '修改失败',
              type: 'error',
              duration: 3000
            });
          }.bind(this),
        });
      } else {
        //用户刚新增的，直接删除
        this.moduletags.splice(index, 1)
      }
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    tagClick(index) {
      this.editValue = this.moduletags[index].tag;
      let tmp = JSON.parse(JSON.stringify(this.moduletags[index]));
      tmp['edit'] = true;
      this.$set(this.moduletags, index, tmp);
      this.$nextTick(_ => {
        this.$refs.editTagInput[0].$refs.input.focus();
      });
    },
    editInputConfirm(index) {
      let editValue = $.trim(this.editValue);
      let flag = false;
      if (editValue == '') {
        let tmp = JSON.parse(JSON.stringify(this.moduletags[index]));
        tmp['edit'] = false;
        this.$set(this.moduletags, index, tmp);
        return;
      }
      if (editValue.length > 20) {
        this.$notify({
          title: '提示',
          message: '标签名称必须小于20个字符',
          type: 'warning',
          duration: 3000
        });
        return;
      }
      for (var i = 0; i < this.moduletags.length; i++) {
        if (i != index && editValue == this.moduletags[i].tag) {
          this.$notify({
            title: '提示',
            message: '标签名称已经存在',
            type: 'warning',
            duration: 3000
          });
          return;
        } else {
          flag = true;
        }
      }
      if (flag) {
        let tmp = JSON.parse(JSON.stringify(this.moduletags[index]));
        tmp['tag'] = editValue;
        tmp['edit'] = false;
        this.$set(this.moduletags, index, tmp);
      } else {
        let tmp = JSON.parse(JSON.stringify(this.moduletags[index]));
        tmp['edit'] = false;
        this.$set(this.moduletags, index, tmp);
      }
    },
    handleInputConfirm() {
      let inputValue = $.trim(this.inputValue);
      let flag = false;
      if ($.trim(inputValue).length > 20) {
        this.$notify({
          title: '提示',
          message: '标签名称必须小于20个字符',
          type: 'warning',
          duration: 3000
        });
        return;
      }
      if (inputValue) {
        for (var i = 0; i < this.moduletags.length; i++) {
          if (inputValue == this.moduletags[i].tag) {
            this.$notify({
              title: '提示',
              message: '标签名称已经存在',
              type: 'warning',
              duration: 3000
            });
            return;
          } else {
            flag = true;
          }
        }
        if (flag == true || this.moduletags.length == 0) {
          let tmp = {}
          tmp['tag'] = $.trim(inputValue);
          tmp['edit'] = false;
          this.moduletags.push(tmp);
        }
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
  }
}

</script>
<style>
html,
body {
  height: 100%;
  margin: 0;
}

#module .el-tag {
  margin-left: 10px;
}

.sysicon .active {
  display: block;
  color: #6adc71;
  float: right;
}

.liactive {
  background: #1681c4;
  border-radius: 10px;
}

.nactive {
  display: none;
}

.module .el-submenu__title:hover {
  background-color: #fff;
}

.module .el-submenu .el-menu-item:hover {
  background: #20a0ff;
}

.module .el-menu-item.is-active {
  color: #48576a;
  background-color: #20a0ff;
}

.module .el-submenu .el-menu-item {
  min-width: 0px;
}

.menuitemName {
  display: inline-block;
  width: 85%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.iconlist {
  float: left;
  width: 40px;
  height: 40px;
  list-style-type: none;
  margin: 10px 0px 0px 10px;
}

.module .el-submenu__title {
  border-bottom: 1px solid #dcdcdc;
}

.module .el-menu-item {
  border-bottom: 1px solid #dcdcdc;
}

.module .el-submenu.is-active .el-submenu__title {
  border-bottom-color: #dcdcdc;
}

</style>
