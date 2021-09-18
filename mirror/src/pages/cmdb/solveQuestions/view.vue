<template>
    <div class="solveQuestions">
        <div class="common-dashbord">
            <div class="title">
                租户问题及建议表单
            </div>
            <div class="info">
                <div class="info-singal">
                    <div class="inner-info">
                        <span>租户：</span>
                        <span>{{info.user}}</span>
                    </div>
                    <div class="inner-info">
                        <span>提交人：</span>
                        <span>{{info.postUser}}</span>
                    </div>
                </div>
                <div class="info-singal">
                    <div class="inner-info">
                        <span>一级部门：</span>
                        <span>{{info.department1}}</span>
                    </div>
                    <div class="inner-info">
                        <span>提交人电话：</span>
                        <span>{{info.phone}}</span>
                    </div>
                </div>
                <div class="info-singal">
                    <div class="inner-info">
                        <span>二级部门：</span>
                        <span>{{info.department2}}</span>
                    </div>
                    <div class="inner-info">
                        <span>提交人邮箱：</span>
                        <span>{{info.email}}</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="common-dashbord-bottom">
            <div class="form-content">
                <el-form :label-position="labelPosition"
                         label-width="80px"
                         ref="form"
                         :rules="rules"
                         :model="form">
                    <el-form-item label="标题"
                                  prop="name">
                        <el-input v-model="form.name"
                                  :disabled="selectInfo!='process'"></el-input>
                    </el-form-item>
                    <el-form-item label="类型"
                                  prop="type"
                                  style="display:inline-block;margin-right:30px;">
                        <el-select v-model="form.type"
                                   :disabled="selectInfo!='process'"
                                   placeholder="请选择">
                            <el-option v-for="item in typeOptions"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="分类"
                                  prop="class"
                                  style="display:inline-block;margin-right:30px;">
                        <el-select v-model="form.class"
                                   :disabled="selectInfo!='process'"
                                   placeholder="请选择">
                            <el-option v-for="item in classifyOptions"
                                       :key="item.value"
                                       :label="item.name"
                                       :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="时间"
                                  prop="dateTime"
                                  v-if="selectInfo != 'process'"
                                  style="display:inline-block;">
                        <el-date-picker v-model="form.dateTime"
                                        type="datetime"
                                        :disabled="true"
                                        placeholder="选择日期">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="具体描述"
                                  v-if="selectInfo!='process'"
                                  prop="desc">
                        <el-input type="textarea"
                                  autosize
                                  :disabled="selectInfo!='process'"
                                  v-model="form.desc"></el-input>
                    </el-form-item>
                    <el-form-item label="具体描述"
                                  v-if="selectInfo=='process'"
                                  prop="desc">
                        <el-input type="textarea"
                                  :rows="5"
                                  v-model="form.desc"></el-input>
                    </el-form-item>
                    <div v-for="(item,index) in infoArr"
                         :key="index">
                        <el-form-item :label="labelCheck(item.isAdmin)"
                                      :required="true">
                            <div>{{timeCheck(item.createTime)}}</div>
                            <el-input type="textarea"
                                      :disabled="true"
                                      autosize
                                      v-model="item.answerContent"></el-input>
                        </el-form-item>
                    </div>
                    <!-- <div v-for="(item,index) in infoArr"
                         :key="index">
                        <el-form-item label="追加信息"
                                      :required="true"
                                      v-if="selectInfo == 'confirm'">
                            <div>{{timeCheck(item.createTime)}}</div>
                            <el-input type="textarea"
                                      :disabled="true"
                                      autosize
                                      v-model="item.answerContent"></el-input>
                        </el-form-item>
                    </div> -->
                    <el-form-item label="处理信息"
                                  prop="info"
                                  v-if="selectInfo == 'deal'">
                        <el-input type="textarea"
                                  :rows="5"
                                  v-model="form.info"></el-input>
                    </el-form-item>
                    <!-- <el-form-item label="追加信息"
                                  prop="info"
                                  v-if="selectInfo == 'deal' && infoArr.length > 0">
                        <el-input type="textarea"
                                  :rows="5"
                                  v-model="form.info"></el-input>
                    </el-form-item> -->
                    <el-form-item label="追加信息"
                                  prop="elesInfo"
                                  v-if="selectInfo == 'confirm'">
                        <el-input type="textarea"
                                  :rows="5"
                                  v-model="form.elesInfo"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <div class="flex-btn"
                             v-if="selectInfo != 'confirm'">
                            <el-button type="primary"
                                       @click="submitBtn('form')">确认提交</el-button>
                        </div>
                        <div class="flex-btn"
                             v-else>
                            <el-button type="primary"
                                       @click="submitBtn('form')">确认提交</el-button>
                            <!-- <el-button type="primary"
                                       @click="submitBtn('form',1)">完成</el-button> -->
                        </div>
                    </el-form-item>
                </el-form>
            </div>
        </div>

    </div>
</template>
<script>
    import rbConfigDictServiceFactory from 'src/services/cmdb/rb-configDict-service.factory.js'
    export default {
        name: 'SolveQuestions',
        props: ['selectInfo'],
        computed: {
            // eslint-disable-next-line no-unused-vars
            timeCheck(value) {
                return function (value) {
                    let num = value.indexOf('.')
                    return value.substring(0, num)
                }
            },
            // eslint-disable-next-line no-unused-vars
            labelCheck(value) {
                return function (value) {
                    if (value == 'true') {
                        return '处理信息'
                    } else {
                        return '追加信息'
                    }
                }
            }
        },
        data() {
            return {
                loginName: '',
                userName: '',
                infoArr: [],
                info: {
                    user: '--',
                    department1: '--',
                    department2: '--',
                    postUser: '--',
                    phone: '--',
                    email: '--'
                },
                rules: {
                    name: [
                        { required: true, message: '请输入名称' }
                    ],
                    desc: [
                        { required: true, message: '请输入具体描述' }
                    ],
                    type: [
                        { required: true, message: '请选择类型', trigger: 'change' }
                    ],
                    class: [
                        { required: true, message: '请选择分类', trigger: 'change' }
                    ],
                    // dateTime: [
                    //     { required: true, message: '请选择具体日期', trigger: 'change' }
                    // ],
                    info: [
                        { required: true, message: '请输入处理信息' }
                    ],
                    elesInfo: [
                        { required: true, message: '请输入追加信息' }
                    ]
                },
                labelPosition: 'left',
                form: {
                    name: '',
                    desc: '',
                    type: '',
                    class: '',
                    // dateTime: '',
                    info: '',
                    elesInfo: ''
                },
                typeOptions: [],
                classifyOptions: []
            }
        },
        mounted() {

            if (this.selectInfo != 'process') {
                // this.loginName = sessionStorage.getItem('usernameScreen') || sessionStorage.getItem('username')
                // this.info.department1 = sessionStorage.getItem('department1Screen') || sessionStorage.getItem('userDepartmentOne')
                // this.info.department2 = sessionStorage.getItem('department2Screen') || sessionStorage.getItem('userDepartmentTwo')
                this.loginName = sessionStorage.getItem('username')
                // this.info.department1 = sessionStorage.getItem('userDepartmentOne')
                // this.info.department2 = sessionStorage.getItem('userDepartmentTwo')
                // if (this.info.department1 == null) {
                //     this.info.department1 = '--'
                // }
                // if (this.info.department2 == null) {
                //     this.info.department2 = '--'
                // }
                // this.info.user = this.info.department2 || this.info.department1 || '--'
                // console.log(this.info.department1)
                // console.log(this.$route.query.queryParams)
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/user/findByLdapId/{ldap_id}',
                    params: {
                        ldapId: this.loginName
                    }
                }).then((userInfo) => {
                    console.log(userInfo)
                    // this.info.postUser = userInfo.name
                    this.userName = userInfo.name// sessionStorage.getItem('username')
                    // this.info.phone = userInfo.mobile
                    // this.info.email = userInfo.mail
                    this.initHttpRes(this.$route.query.queryParams)
                })
                // this.initHttpRes(this.$route.query.queryParams)
                // this.form.name = JSON.parse(this.$route.query.queryParams).title
                // this.form.desc = JSON.parse(this.$route.query.queryParams).problemDesc
                // this.form.dateTime = JSON.parse(this.$route.query.queryParams).createTime
            } else {
                this.loginName = sessionStorage.getItem('username')
                if (sessionStorage.getItem('usernameScreen')) {
                    this.info.department1 = sessionStorage.getItem('department1Screen') || sessionStorage.getItem('userDepartmentOne')
                    this.info.department2 = sessionStorage.getItem('department2Screen') || sessionStorage.getItem('userDepartmentTwo')
                } else {
                    this.info.department1 = sessionStorage.getItem('userDepartmentOne') || sessionStorage.getItem('department1Screen')
                    this.info.department2 = sessionStorage.getItem('userDepartmentTwo') || sessionStorage.getItem('department2Screen')
                }

                console.log(sessionStorage.getItem('userDepartmentOne'))
                console.log(sessionStorage.getItem('userDepartmentTwo'))
                console.log(sessionStorage.getItem('department1Screen'))
                console.log(sessionStorage.getItem('department2Screen'))
                console.log(this.info.department1)
                console.log(this.loginName)
                this.info.user = this.info.department2 || this.info.department1 || '--'
                this.getTypeRes()
                this.getClassRes()
                this.initInfo()
            }
        },
        methods: {
            // 提交
            submitBtn(form, data) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        console.log('submit!')
                        if (this.selectInfo == 'deal') {
                            this.submitRes()
                        } else if (this.selectInfo == 'process') {
                            this.processSubmitRes()
                        } else if (this.selectInfo == 'confirm') {
                            if (data == 1) {
                                // this.confirmUrl()
                                this.confirmSubmitRes()
                            } else {
                                // this.confirmSubmitRes()
                                this.confirmUrl()
                            }

                        }

                    } else {
                        console.log('error submit!!')
                        return false
                    }
                })
            },
            confirmSubmitRes() {
                this.rbHttp.sendRequest({
                    method: 'PUT',
                    url: '/v1/v3/cmdb/screen/problem/update',
                    data: { id: this.$route.query.queryParams }
                }).then((res) => {
                    console.log(res)
                    if (res.flag) {
                        this.$message.success('提交成功！')
                        setTimeout(() => {
                            this.$router.push({ path: '/cmdb/solveQuestions/userList' })
                        }, 3000)
                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            confirmUrl() {
                // this.$router.push({ path: '/cmdb/solveQuestions/userList', query: { queryParams: 1 } })
                let data = {}
                data.loginName = this.loginName
                data.userName = this.userName
                data.department1 = this.info.department1
                data.department2 = this.info.department2
                data.mobile = this.info.phone
                data.email = this.info.email
                data.problemId = this.$route.query.queryParams
                data.answerContent = this.form.elesInfo
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/v3/cmdb/screen/answer/save?isAdmin=false',
                    data: data
                }).then((res) => {
                    console.log(res)
                    if (res.flag) {
                        this.$message.success('提交成功！')
                        setTimeout(() => {
                            this.$router.push({ path: '/cmdb/solveQuestions/userList' })
                        }, 3000)

                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            // 新增
            processSubmitRes() {
                let data = {}
                data.loginName = this.loginName
                data.userName = this.userName
                data.department1 = this.info.department1
                data.department2 = this.info.department2
                data.mobile = this.info.phone
                data.email = this.info.email
                data.title = this.form.name
                data.type = this.form.type
                data.classify = this.form.class
                data.problemDesc = this.form.desc
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/v3/cmdb/screen/problem/save',
                    data: data
                }).then((res) => {
                    console.log(res)
                    if (res.flag) {
                        this.$message.success('提交成功！')
                        setTimeout(() => {
                            this.$router.push({ path: '/cmdb/solveQuestions/userList' })
                        }, 3000)

                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            // 问题回答---管理者信息
            submitRes() {
                let data = {}
                data.loginName = this.loginName
                data.userName = this.userName
                data.department1 = this.info.department1
                data.department2 = this.info.department2
                data.mobile = this.info.phone
                data.email = this.info.email
                data.problemId = this.$route.query.queryParams
                data.answerContent = this.form.info
                this.rbHttp.sendRequest({
                    method: 'POST',
                    url: '/v1/v3/cmdb/screen/answer/save?isAdmin=true',
                    data: data
                }).then((res) => {
                    console.log(res)
                    if (res.flag) {
                        this.$message.success('提交成功！')
                        setTimeout(() => {
                            this.$router.push({ path: '/cmdb/solveQuestions/list' })
                        }, 3000)

                    } else {
                        this.$message.error(res.message)
                    }
                })
            },
            initInfo() {
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/user/findByLdapId/{ldap_id}',
                    params: {
                        ldapId: this.loginName
                    }
                }).then((userInfo) => {
                    console.log(userInfo)
                    // this.info.department1 = '中移信息公司'
                    // this.info.department2 = '基础平台部'
                    // this.info.user = this.info.department2 || this.info.department1 || '--'
                    this.info.postUser = userInfo.name
                    this.userName = userInfo.name// sessionStorage.getItem('username')
                    this.info.phone = userInfo.mobile
                    this.info.email = userInfo.mail
                })
            },
            getTypeRes(type) {
                let obj = {
                    type: 'cmdb_bbs_type'
                }
                // eslint-disable-next-line no-unused-vars
                rbConfigDictServiceFactory.getDictsByType(obj).then(res => {
                    this.typeOptions = res
                    if (this.selectInfo != 'process') {
                        // this.form.type = JSON.parse(this.$route.query.queryParams).type
                        this.form.type = type
                    }
                })

            },
            getClassRes(type) {
                let obj = {
                    type: 'cmdb_bbs_classify'
                }
                // eslint-disable-next-line no-unused-vars
                rbConfigDictServiceFactory.getDictsByType(obj).then(res => {
                    this.classifyOptions = res
                    if (this.selectInfo != 'process') {
                        // this.form.class = JSON.parse(this.$route.query.queryParams).classify
                        this.form.class = type
                    }
                })

            },
            initHttpRes(id) {
                // let id = 'f8d09c5986e0402d9632cc55da137bfb'
                this.rbHttp.sendRequest({
                    method: 'GET',
                    url: '/v1/v3/cmdb/screen/problem/list/' + id,
                }).then((userInfo) => {
                    console.log(userInfo)
                    this.getTypeRes(userInfo.type)
                    this.getClassRes(userInfo.classify)
                    this.form.name = userInfo.title
                    this.form.desc = userInfo.problemDesc
                    this.form.dateTime = userInfo.createTime
                    this.infoArr = userInfo.answerInfoList

                    this.info.postUser = userInfo.userName
                    // this.userName = userInfo.userName// sessionStorage.getItem('username')
                    this.info.phone = userInfo.mobile || '--'
                    this.info.email = userInfo.email || '--'
                    // this.loginName = userInfo.loginName
                    this.info.department1 = userInfo.department1 || '--'
                    this.info.department2 = userInfo.department2 || '--'
                    if (this.info.department1 == null) {
                        this.info.department1 = '--'
                    }
                    if (this.info.department2 == null) {
                        this.info.department2 = '--'
                    }
                    this.info.user = this.info.department2 || this.info.department1 || '--'
                    // this.info.department1 = userInfo.department1 || '--'
                    // this.info.department2 = userInfo.department2 || '--'
                    // this.info.user = userInfo.department2 || userInfo.department1 || '--'
                    // this.info.postUser = userInfo.userName
                    // this.info.phone = userInfo.moblie
                    // this.info.email = userInfo.email
                })
            }
        }
    }
</script>
<style lang="scss" scoped>
    .solveQuestions {
        display: flex;
        flex-direction: column;
        height: 100%;
        box-sizing: border-box;
        padding-bottom: 20px;
        width: 100%;
        .common-dashbord {
            background: white;
            box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.05);
            border-radius: 16px;
            margin: 10px;
            box-sizing: border-box;
        }
        .common-dashbord-bottom {
            flex: 1;
            background: white;
            box-shadow: 0px 5px 5px 0px rgba(0, 0, 0, 0.05);
            border-radius: 16px;
            margin: 10px;
            box-sizing: border-box;
        }
        .title {
            width: 100%;
            text-align: center;
            line-height: 50px;
            font-weight: bold;
            font-size: 16px;
        }
        .info {
            display: flex;
            justify-content: space-around;
            width: 100%;
            padding: 20px 0;
            box-sizing: border-box;
            // border-bottom: 1px solid #666;

            .info-singal {
                // width: 25%;
                // line-height: 50px;
                .inner-info {
                    line-height: 50px;
                    span:nth-child(1) {
                        font-weight: bold;
                    }
                }
            }
        }
        .form-content {
            width: 100%;
            padding: 0 5%;
            padding-top: 20px;
            box-sizing: border-box;
        }
        .flex-btn {
            margin-top: 20px;
            display: flex;
            justify-content: center;
        }
    }
</style>