<!--
@author wupeng
@date   19.4.2
@description 账号管理页面
-->
<template>
    <el-container class="components-container">
        <el-aside class="yw-dashboard"
                  width="300px">
            <el-scrollbar class="aside-main"
                          style="height: calc(100% - 60px)">
                <el-form class="yw-form is-required"
                         ref="accountForm"
                         :model="accountForm"
                         :rules="accountRules"
                         label-width="70px">
                    <el-form-item label="账号"
                                  prop="usernames">
                        <el-input v-model="accountForm.usernames"
                                  placeholder="请输入账号"
                                  :disabled="this.disableflag"
                                  maxlength="50"></el-input>
                    </el-form-item>
                    <el-form-item label="用户名称"
                                  prop="name">
                        <el-button @click="handleCheckPerson"
                                   class="mod-btn acc-mange__form-btn"
                                   v-text="accountBtnText"
                                   :disabled="this.disableUserflag"></el-button>
                        <el-input v-model="accountForm.name"
                                  hidden
                                  style="display: none;"></el-input>
                    </el-form-item>
                    <el-form-item label="功能角色"
                                  prop="roleName">
                        <el-select v-model="accountForm.roleName"
                                   closable
                                   multiple
                                   collapse-tags
                                   placeholder="请选择"
                                   size="small">
                            <el-option v-for="item in roleOptions"
                                       :key="item.value"
                                       :label="item.label"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="资源角色"
                                  prop="resoure">
                        <el-tag :key="tag.uuid"
                                v-for="tag in dynamicTags"
                                closable
                                :disable-transitions="false"
                                @close="handleClose(tag)"
                                size="small">
                            {{tag.name}}
                        </el-tag>
                        <el-popover placement="bottom-start"
                                    trigger="click">
                            <comtree :data="resourceData"
                                     :props="resourceTreeDefault"
                                     @node-click="handleTreeClick">
                            </comtree>
                            <el-button slot="reference"
                                       class="mod-btn"
                                       size="small">请选择</el-button>
                        </el-popover>
                    </el-form-item>
                    <el-form-item label="密码"
                                  prop="password">
                        <el-input type="password"
                                  v-model="accountForm.password"
                                  placeholder="请输入密码"
                                  minlength="8"></el-input>
                    </el-form-item>
                    <el-form-item label="确认密码"
                                  prop="checkPass">
                        <el-input type="password"
                                  v-model="accountForm.checkPass"
                                  placeholder="请输入确认密码"></el-input>
                    </el-form-item>
                    <el-form-item label="描述">
                        <el-input type="textarea"
                                  :rows="5"
                                  style="width:210px"
                                  v-model="accountForm.desc"
                                  placeholder="请输入描述"></el-input>
                    </el-form-item>
                </el-form>
                <section class="btn-wrap">
                    <el-button type="primary"
                               @click="handleSubmit">保存</el-button>
                    <el-button @click="cancel_mod">取消修改</el-button>
                    <el-button @click="handleReset">重置</el-button>
                </section>
            </el-scrollbar>
        </el-aside>
        <el-main class="yw-dashboard">
            <div class="yw-search-box">
                <el-input v-model="searchText" :style="{width:isNetwork?'300px':''}"
                          placeholder="请输入账号查询">
                    <i slot="suffix"
                       class="el-input__icon el-icon-search"
                       @click="searchData"></i>
                </el-input>
                <el-button v-if="isNetwork" icon="el-icon-refresh-right" @click="resetSearch">重置</el-button>
                <el-button v-if="isNetwork" type="primary" icon="el-icon-search" @click="chooseOrganization">组织选择</el-button>
                <el-button v-if="isNetwork" type="primary" icon="el-icon-add"  @click="batchAdd">批量新增授权</el-button>
                <el-button v-if="isNetwork" icon="el-icon-delete" @click="batchDelete">批量移除授权</el-button>
            </div>
            <accountList ref="accountList"
                         :searchText="searchText"
                         :isNetwork="isNetwork"
                         @detail-click="handleDetail"
                         @delete-click="handleDelete"
                         @handleSelection="handleSelection"
                         ></accountList>
        </el-main>

        <!-- dialog -->
        <person-dialog :isNull="is_null"
                       :visible="dialogVisible"
                       :dialogStyle="dialogStyle"
                       @dialog-confirm="handlePersonDialog"
                       @dialog-close="handlePersonDialogClose">
        </person-dialog>
        <!-- dialog -->
        <!-- 组织选择弹框 -->
        <el-dialog  class="yw-dialog"
                    :visible.sync="organizationDialog"
                    width="600px"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   title="组织选择">
            <section class="yw-dialog-main">
                <el-tree 
                 v-if="organizationDialog"
                 ref="organizateTree"
                 :props="organizationProps" 
                 :load="loadOrganizationNode"
                 node-key="uuid"
                 lazy
                 show-checkbox>
            </el-tree>
            </section>
            <section class="btn-wrap">
                <el-button type="primary" @click="sureDialog">确定</el-button>
                <el-button @click="cancelDialog">取消</el-button>
            </section>
        </el-dialog>
    </el-container>
    <!-- <div class="account-mange">
    <el-row >
      <el-col :span="5" class="acc-mange__side">
        <el-form class="mod-form"
          ref="accountForm"
          :model="accountForm"
          :rules="accountRules"
          label-width="70px">
          <el-form-item label="账号" prop="usernames">
            <el-input v-model="accountForm.usernames" placeholder="请输入账号" :disabled="this.disableflag"  maxlength="50"></el-input>
          </el-form-item>
          <el-form-item label="用户名称" prop="name">
            <el-button @click="handleCheckPerson" class="mod-btn acc-mange__form-btn" v-text="accountBtnText" :disabled="this.disableflag"></el-button>
            <el-input v-model="accountForm.name" hidden style="display: none;"></el-input>
          </el-form-item>
          <el-form-item label="功能角色" prop="roleName">
            <el-select v-model="accountForm.roleName" closable multiple placeholder="请选择" size="small">
              <el-option
                v-for="item in roleOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="资源角色" prop="resoure">
            <el-tag
            :key="tag.uuid"
            v-for="tag in dynamicTags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)" size="small">
            {{tag.name}}
           </el-tag>
            <el-popover
              placement="bottom-start"
              trigger="click">
              <comtree
                :data="resourceData"
                :props="resourceTreeDefault"
                @node-click="handleTreeClick">
              </comtree>
              <el-button slot="reference" class="mod-btn" size="small">请选择</el-button>
            </el-popover>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="accountForm.password" placeholder="请输入密码" minlength="8"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input type="password" v-model="accountForm.checkPass" placeholder="请输入确认密码"></el-input>
          </el-form-item>
          <el-form-item label="描述">
            <el-input type="textarea" style="font-size:12px;" v-model="accountForm.desc" placeholder="请输入描述"></el-input>
          </el-form-item>
          <div class="acc-mange__form-btn">
            <el-button size="small" type="primary" @click="handleSubmit" class="mod-btn">保存</el-button>
            <el-button size="small" class="mod-btn" @click="cancel_mod">取消修改</el-button>
            <el-button size="small" @click="handleReset" class="mod-btn">重置</el-button>
          </div>
        </el-form>
      </el-col>
      <el-col :span="19" class="acc-mange__mian">
        <div class="acc-mange__search">
          <el-input v-model="searchText"
            placeholder="请输入账号查询">
            <i slot="suffix"
                 class="el-input__icon el-icon-search"
                 @click="searchData"></i>
            </el-input>
        </div>
        <div class="table__main">
          <accountList ref="accountList"
            :searchText="searchText"
            @detail-click="handleDetail"
            @delete-click="handleDelete"
          ></accountList>
        </div>
      </el-col>
    </el-row>
    <person-dialog
      :isNull="is_null"
      :visible="dialogVisible"
      :dialogStyle="dialogStyle"
      @dialog-confirm="handlePersonDialog"
      @dialog-close="handlePersonDialogClose">
    </person-dialog>
  </div> -->
</template>
<script>
    import systemAccountService from 'src/services/sys/account-services.js'
    import accountList from 'src/pages/system_manage/components/accountList.vue'
    import personDialog from 'src/pages/system_manage/components/personListDialog.vue'
    import systemDataService from 'src/services/sys/role-services.js'
    import resourceDataService from 'src/services/sys/reource-services.js'
    import comtree from 'src/pages/system_manage/components/tree.vue'
    import _ from 'lodash'

    export default {
        name: 'Account',
        components: {
            personDialog,
            accountList,
            comtree
        },
        data() {
            var validateName = (rule, value, callback) => {
                var reg = new RegExp('^[a-zA-Z]\\w{1,50}$')
                if (!reg.test(value)) {
                    callback(new Error('请1-50数字或字母'))
                } else {
                    callback()
                }
            }
            var validatePass = (rule, value, callback) => {
                var reg = new RegExp('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$')
                if (!reg.test(value)) {
                    callback(new Error('请输入大小写及数字组合的密码'))
                } else {
                    if (this.accountForm.checkPass !== '') {
                        this.$refs.accountForm.validateField('checkPass')
                    }
                    callback()
                }
            }
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'))
                } else if (value !== this.accountForm.password) {
                    callback(new Error('两次输入密码不一致!'))
                } else {
                    callback()
                }
            }
            // var resouresCheck = (rule, value, callback) => {
            //   if (value === '') {
            //     callback(new Error('请选择资源'))
            //   } else {
            //     callback()
            //   }
            // }
            return {
                dynamicTags: [],
                accountForm: {
                    desc: ''
                },
                accountRules: {
                    usernames: [
                        { required: true, message: '请输入账户', trigger: 'blur, change' },
                        { validator: validateName, trigger: 'blur' }
                    ],
                    name: [
                        { required: true, message: '请选择用户名称', trigger: 'blur, change' }
                    ],
                    roleName: [
                        { required: true, message: '请选择功能角色', trigger: 'blur, change' }
                    ],
                    // resoure: [
                    //   { required: true, message: '请选择资源', trigger: 'blur' },
                    //   { validator: resouresCheck, trigger: 'blur' }
                    // ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { required: true, validator: validatePass2, trigger: 'blur' }
                    ]
                },
                roleOptions: [],
                selectAccount: '',
                searchText: '',
                accountBtnText: '请点击查询人员',
                resourceBtnText: {},
                dialogVisible: false,
                dialogList: [],
                resourceData: [],
                resourceTreeDefault: {
                    label: 'name',
                    children: 'childList'
                },
                dialogStyle: {},
                disableflag: false,
                disableUserflag: false,
                org_name: '',
                is_null: 'is_null',
                isNetwork:window.projectName=='集中网管'?true:false,
                selectionAccout:[],
                organizationDialog:false,
                organizationProps:{
                   label:'name',
                   children:'zones',
                   isLeaf: 'leaf'
                }
            }
        },
        methods: {
            handleClose(tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1)
            },
            // 保存数据
            cancel_mod() {
                this.accountForm.roleName = []
                this.accountForm = {}
                this.accountBtnText = '请点击查询人员'
                this.dynamicTags = []
                this.disableflag = false
                this.disableUserflag = false
                this.$forceUpdate()
            },
            handleSubmit() {
                let data = this.accountForm
                let user_id = data.name
                if (!this.disableflag) {
                    this.$refs['accountForm'].validate((valid) => {
                        if (valid) {
                            // 需要对接后台参数格式
                            let params = {
                                usernames: [data.usernames],
                                roles: [
                                    // {
                                    //   uuid: data.roleName
                                    // }
                                    // ,
                                    // {
                                    //   uuid: data.resoures
                                    // }
                                ],
                                password: data.password,
                                description: data.desc
                            }
                            // 功能角色解析
                            if (data.roleName && data.roleName !== '') {
                                data.roleName.forEach(function (item) {
                                    params.roles.push({ uuid: item })
                                })
                            }
                            // 资源解析
                            this.dynamicTags.forEach(function (item) {
                                params.roles.push({ uuid: item.uuid })
                            })
                            systemAccountService.addAccount(params, user_id).then(() => {
                                // return systemAccountService.assignAccount(params1, data.usernames, this.org_name).then((res) => {
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                })
                                this.getAccountList()
                                // this.accountForm = {}
                                // this.accountBtnText = '请点击查询人员'
                                this.handleReset()
                                // this.resourceBtnText = '请选择资源'
                            }).catch((res) => {
                                var message
                                if (res.data && res.data.errors && res.data.errors[0] && res.data.errors[0].code === 'invalid_args' && res.data.errors[0].fields[0]['0.username']) {
                                    message = res.data.errors[0].fields[0]['0.username'][0]
                                    if (message.indexOf('already exist') !== -1) {
                                        message = '账号已经存在'
                                    }
                                }
                                this.$message({
                                    message: message ? message.toString() : '提交失败',
                                    type: 'error'
                                })
                            })
                            // })
                        } else {
                            return false
                        }
                    })
                } else {
                    let params1 = {
                        roles: [
                            // {
                            //   uuid: data.roleName
                            // }
                            // ,
                            // {
                            //   uuid: data.resoures
                            // }
                        ],
                        description: data.desc
                    }
                    if (data.roleName && data.roleName !== '') {
                        data.roleName.forEach(function (item) {
                            params1.roles.push({ uuid: item })
                        })
                    }
                    // 资源解析
                    this.dynamicTags.forEach(function (item) {
                        params1.roles.push({ uuid: item.uuid })
                    })
                    if (data.password !== '') {
                        params1.newPassword = data.password
                    }
                    systemAccountService.updateAccount(params1, data.name, data.usernames).then(() => {
                        this.$message({
                            message: '更新成功',
                            type: 'success'
                        })
                        this.getAccountList()
                    }).catch(() => {
                        this.$message({
                            message: '更新失败',
                            type: 'error'
                        })
                    })
                }
            },
            // 重置按钮
            handleReset() {
                this.accountForm.roleName = []
                this.accountForm = {}
                this.disableflag = false
                this.disableUserflag = false
                this.accountBtnText = '请点击查询人员'
                this.dynamicTags = []
                this.$forceUpdate()
                // this.resourceBtnText = '请选择资源'
            },
            // 人员详情
            handleDetail(data) {
                this.disableflag = true
                this.selectAccount = data
                systemAccountService.getAccountList(undefined, undefined, undefined, data).then((res) => {
                    const data = res.results[0]
                    this.accountForm.usernames = data.username
                    if (data.userInfo) {
                        this.disableUserflag = true
                        this.accountForm.name = data.userInfo.uuid
                        this.accountBtnText = data.userInfo.name
                    }
                    if (data.roles) {
                        let roleName = []
                        let resource = []
                        if (data.roles) {
                            data.roles.forEach(function (item) {
                                if (item.role_type === 0) {
                                    roleName.push(item.uuid)
                                } else {
                                    resource.push({ 'name': item.name, 'uuid': item.uuid })
                                }
                            })
                        }

                        this.accountForm.roleName = roleName
                        this.dynamicTags = resource
                    }
                    // this.accountForm.roleName = data.roles[0].uuid
                    // this.accountForm.resoures = data.roles[1] !== undefined ? data.roles[1].uuid : ''
                    // this.resourceBtnText = data.roles[1] !== undefined ? data.roles[1].name : ''
                    this.accountForm.desc = data.description
                })
            },
            // 人员删除
            handleDelete(data) {
                this.$confirm('此操作将删除用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    systemAccountService.deleteAccount(data).then(() => {
                        this.$message({
                            message: '删除成功',
                            type: 'success'
                        })
                        this.getAccountList()
                        this.accountForm = []
                        this.disableflag = false
                        this.disableUserflag = false
                    }).catch(() => {
                        this.$message({
                            message: '删除失败',
                            type: 'error'
                        })
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消操作'
                    })
                })
            },
            // 选择人员
            handleCheckPerson(event) {
                let offsetTop = event.clientY
                let offsetLeft = event.clientX
                if (event.layerX) {
                    offsetLeft -= event.layerX
                }
                if (event.layerY) {
                    offsetTop -= event.layerY
                }
                this.dialogStyle = {
                    top: `${offsetTop + 30}px`, // 往下偏移 30
                    left: `${offsetLeft - 10}px` // 往右偏移 10
                }
                this.dialogVisible = true
            },
            // 关闭弹窗
            handlePersonDialogClose() {
                this.dialogVisible = false
            },
            // 确认人员弹窗
            handlePersonDialog(data) {
                // console.log('handlePersonDialog', data)
                // TODO 确认字段
                this.accountBtnText = data.name
                this.accountForm.name = data.uuid
                this.dialogVisible = false
            },
            // 确认资源弹窗
            handleTreeClick(data) {
                // this.accountForm.resoures = data.uuid
                // this.resourceBtnText.name = data.name
                // this.resourceBtnText.uuid = data.uuid
                if (_.map(this.dynamicTags, 'uuid').indexOf(data.uuid) === -1) {
                    this.dynamicTags.push({ 'name': data.name, 'uuid': data.uuid })
                }
                this.treeVisible = false
            },
            // 获取账户列表
            getAccountList() {
                this.$refs['accountList'].getAccountList()
            },
            // 获取资源
            getResourceList() {
                resourceDataService.getResourceList().then((res) => {
                    if (res) {
                        this.needAddRoot = res.length === 0
                    }
                    this.resourceData = res
                })
            },
            // 获取角色列表
            getRoleList() {
                systemDataService.getRoleList().then((res) => {
                    if (res && res.results.length > 0) {
                        res.results.map((item) => {
                            this.roleOptions.push({
                                value: item.uuid,
                                label: item.name
                            })
                        })
                    }
                })
            },
            // 模糊查询资源角色方法
            searchData() {
                this.$refs['accountList'].searchData()
            },
            handleSelection(val){
             this.selectionAccout=val
            },
            resetSearch(){
              this.searchText=''
              setTimeout(()=>{
              this.$refs['accountList'].searchData()
              },100)
            },
            // 组织选择
            chooseOrganization(){
              this.organizationDialog=true
            },
            // 获取树图数据
            getTreeDatas(deptId,resolve){
              systemAccountService.deptUserTree(deptId).then((res)=>{
                  if(res.deptList||res.userList){
                  if(res.deptList){
                    resolve(res.deptList)
                   }else{
                   resolve(res.userList)
                   }
                  }else{
                   return resolve([])
                  }  
              })
            },
            // 加载组织树图
            loadOrganizationNode(node, resolve){
             // console.log(node,'node')
            if (node.level === 0) {
             this.getTreeDatas('',resolve)
            }else{
             this.getTreeDatas(node.data.uuid,resolve)
             }
            },
            // 确认组织操作
            sureDialog(){
              if(this.$refs.organizateTree.getCheckedNodes().length>0){
              let userList=this.$refs.organizateTree.getCheckedNodes().filter(item=>item.type==='user')
              let userCode=userList.map(item=>item.code)
               this.searchText=userCode.join(',')
               this.organizationDialog=false
              setTimeout(()=>{
              this.$refs['accountList'].searchData()
              },100)
              }else{
                this.$message({type:'warning',message:'请勾选账号'})
              }
              
            },
            // 取消组织操作
            cancelDialog(){
            this.organizationDialog=false
            },
            // 批量新增授权
            batchAdd(){
             if(this.accountForm.roleName.length>0){
              if(this.selectionAccout.length>0){
                this.$confirm('是否执行批量授权操作?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                 }).then(() => {
                   let roles=this.accountForm.roleName.map((item)=>{
                     return {uuid:item}
                   })
                   let userNames=this.selectionAccout.map(item=>item.account)
                   let queryData={
                     roles:roles,
                     userNames:userNames
                   }
                   systemAccountService.batchAssign(queryData).then((res)=>{
                     this.$message({type:'success',message:'批量授权成功'})
                     this.searchData()
                   })
                })         
               }else{
               this.$message({type:'warning',message:'请勾选需要授权的账号'})
             }
             }else{
               this.$message({type:'warning',message:'请选择功能角色'})
             }
            },
            // 批量移除授权
            batchDelete(){
             if(this.accountForm.roleName.length>0){
              if(this.selectionAccout.length>0){
                this.$confirm('是否执行批量移除授权操作?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                 }).then(() => {
                   let roles=this.accountForm.roleName.map((item)=>{
                     return {uuid:item}
                   })
                   let userNames=this.selectionAccout.map(item=>item.account)
                   let queryData={
                     roles:roles,
                     userNames:userNames
                   }
                   systemAccountService.batchRevoke(queryData).then((res)=>{
                     this.$message({type:'success',message:'批量移除授权成功'})
                     this.searchData()
                   })
                })         
               }else{
               this.$message({type:'warning',message:'请勾选需要移除授权的账号'})
             }
             }else{
               this.$message({type:'warning',message:'请选择功能角色'})
             }
            }
        },
        mounted() {
            this.getAccountList()
            this.getRoleList()
            this.getResourceList()
        }
    }
</script>
<style lang="scss" scoped>
    .com-tree {
        height: 300px;
        overflow: scroll;
    }
</style>
