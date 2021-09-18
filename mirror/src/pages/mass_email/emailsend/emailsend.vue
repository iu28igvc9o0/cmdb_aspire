<template>
    <div id="qf-body">
        <el-container>
            <el-container class="qf-connet">
                <el-header class="qf-header">
                    <p class="qf-title">发短信</p>
                    <p class="qf-record"
                       @click="goRecord">短信发送记录</p>
                </el-header>
                <el-main class="qf-main">
                    <div class="qf-receiver">
                        <div class="qf-people">接收人：</div>
                        <div class="receiver">
                            <div class="qf-tips"
                                 v-if="noContacts">同时发给10人，以逗号隔开！</div>
                            <div class="qf-addrBase"
                                 v-for="(item,index) in mailContacts"
                                 :key="index">
                                <a href="javascript:;">
                                    <b name="addrName">{{item.name}}</b>
                                    <span name="addressInfo">
                                        &lt;{{item.mail}}&gt;
                                    </span>
                                </a>
                                <a href="javascript:;"
                                   @click="removeContacts(index)">
                                    <i class="">x</i>
                                </a>
                            </div>

                            <div class="qf-addrText"
                                 style="width: 10px;">
                                <input type="text"
                                       style="width: 110px;"
                                       class="addrText-input"
                                       v-model="newContacts"
                                       @focus="removeTips"
                                       @blur="useTtps"
                                       @change="changeContacts">
                            </div>

                        </div>
                    </div>
                    <div class="qf-textarea">
                        <textarea v-model="mailConnet">{{mailConnet}}</textarea>
                        <p>还可以输入{{mailNumber}}字，本次将以0条计费</p>
                    </div>
                    <div class="qf-template">
                        <a href="javascript:;"
                           @click="dialogTemplate=true">内容保存到模板</a>
                    </div>
                    <div class="qf-send">
                        <el-button class="qf-sendbtn"
                                   type="primary"
                                   @click="sendMail">发送</el-button>
                    </div>

                </el-main>
            </el-container>
            <el-aside width="300px">
                <el-container class="qf-aside">
                    <el-container>
                        <el-header class="qf-header">
                            <p :class="isMail?'qf-yes':''"
                               @click="switchMail">通讯录</p>
                            <p :class="isTemplate?'qf-yes':''"
                               @click="switchTemplate">短信模板</p>
                        </el-header>
                        <!-- 通讯录 -->
                        <el-main class="qf-main-mail"
                                 v-if="isMail">
                            <el-col :span="24"
                                    class="qf-mail">
                                <el-input class="qf-search"
                                          v-model="searchMail"
                                          suffix-icon="el-icon-search"
                                          placeholder="查找联系人"></el-input>
                                <p>企业通讯录</p>
                                <el-tree :data="data"
                                         :props="defaultProps"
                                         accordions
                                         :filter-node-method="filterNode"
                                         ref="mailTree"
                                         @node-click="handleNodeClick"></el-tree>
                            </el-col>
                        </el-main>

                        <!-- 短信模板 -->
                        <el-main class="qf-main-template"
                                 v-if="isTemplate">
                            <el-col :span="24"
                                    class="qf-mailtemplate">
                                <el-input class="qf-search"
                                          v-model="searchTemplate"
                                          @change="handlerPageNo"
                                          suffix-icon="el-icon-search"
                                          placeholder="关键字"></el-input>
                                <div class="template-label">
                                    <div class="label"
                                         v-for="(item,index) in templateLabel"
                                         :key="index">{{item.label}}</div>
                                    <div class="edit-label"
                                         @click="dialogTemplate = true">编辑模板分类</div>
                                </div>
                                <div class="template-connet">
                                    <ul class="template-ul">
                                        <el-scrollbar style="height:100%">
                                            <li class="template-li"
                                                v-for="(item,index) in templateData"
                                                :key="index"
                                                @click="useTemplate(item.date)">
                                                {{item.date}}
                                            </li>
                                        </el-scrollbar>
                                    </ul>
                                    <el-pagination layout="prev, pager, next"
                                                   :page-size="7"
                                                   :pager-count="4"
                                                   @current-change="handleCurrentChange"
                                                   :total="50">
                                    </el-pagination>
                                </div>

                            </el-col>
                        </el-main>
                    </el-container>
                </el-container>
            </el-aside>
        </el-container>

        <el-dialog title="短信内容保存到模板"
                   :visible.sync="dialogTemplate">
            <div class="lable-title">该短信将保存到珍藏记录，请选择分类：</div>
            <div class="lable-radio">
                <el-radio v-model="radio"
                          v-for="(item,index) in templateLabel"
                          :key="index"
                          :label="index">{{item.label}}</el-radio>
                <el-radio v-model="radio">
                    <el-input v-model="newLabel"
                              placeholder="请输入内容"></el-input>
                </el-radio>
            </div>
            <div slot="footer"
                 class="dialog-footer">
                <el-button @click="dialogTemplate = false">删除</el-button>
                <el-button type="primary"
                           @click="dialogTemplate = false">确 定</el-button>
                <el-button @click="dialogTemplate = false">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import QueryObject from 'src/utils/queryObject.js'

    export default {
        // name: '/logs/device/list',
        mixins: [QueryObject],
        components: {
            // DialogDetails: () => import('../dialog-details.vue'),
        },
        data() {
            return {
                data: [{
                    label: '一级 1',
                    children: [{
                        label: '二级 1-1',
                        children: [{
                            label: '三级 1-1-1'
                        }]
                    }]
                }, {
                    label: '一级 2',
                    children: [{
                        label: '二级 2-1',
                        children: [{
                            label: '三级 2-1-1'
                        }]
                    }, {
                        label: '二级 2-2',
                        children: [{
                            label: '三级 2-2-1'
                        }]
                    }]
                }, {
                    label: '一级 3',
                    children: [{
                        label: '二级 3-1',
                        children: [{
                            label: '三级 3-1-1'
                        }]
                    }, {
                        label: '二级 3-2',
                        children: [{
                            label: '三级 3-2-1'
                        }]
                    }]
                }],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                // 收件人
                mailContacts: [
                    { 'name': '小红', 'mail': 'xiaohong@aspirecn.com', },
                    { 'name': '小明', 'mail': 'xiaoming@aspirecn.com', }
                ],
                noContacts: false,// 联系人提示语
                newContacts: '',// 手动输入联系人
                mailConnet: '',// 短信内容
                mailNumber: 350,// 短信内容
                isMail: false,  // 切换通讯录
                isTemplate: true,  // 切换短信模板
                searchMail: '',  // 通讯录搜索
                searchTemplate: '', // 短信模板搜索
                templateLabel: [
                    { label: '关怀问候' },
                    { label: '节日祝福' },
                    { label: '热榜' },
                    { label: '养生贴士' },
                    { label: '生日祝福' },
                    { label: '发烧友精选' },
                    { label: '精选' },
                    { label: '母亲节精选' },
                ],
                templateData: [
                    { date: '年华似水，光阴似飞，珍惜现在，幻想未来，珍爱生命，放松心情，知足常乐，幸福多多，凡事看开，健康常在，得失看淡，快乐永远。' },
                    { date: '世界上没有爱，就仿佛没有太阳。愿我们爱情永存，让生活充满阳光。' },
                    { date: '还是那株山茶花，芬芳而美丽，那红色是天空的彩霞，是情人脸上的娇羞，是山谷中的胜景，是心里永远的秘密。' },
                    { date: '世界上没有爱，就仿佛没有太阳。愿我们爱情永存，让生活充满阳光。' },
                    { date: '如果我的祝福，能驱除你胸中的烦恼，那么，就让它随着这温馨的春风，吹进你的心谷吧！' },
                    { date: '出门别忘带钥匙，回家别忘多休息，注意健康多微笑，逍遥自在乐呵呵。祝你健健康康，笑呵呵！' },
                    { date: '如果我的祝福，能驱除你胸中的烦恼，那么，就让它随着这温馨的春风，吹进你的心谷吧！' },
                    { date: '如果我的祝福，能驱除你胸中的烦恼，那么，就让它随着这温馨的春风，吹进你的心谷吧！' },
                    { date: '如果我的祝福，能驱除你胸中的烦恼，那么，就让它随着这温馨的春风，吹进你的心谷吧！' },
                    { date: '如果我的祝福，能驱除你胸中的烦恼，那么，就让它随着这温馨的春风，吹进你的心谷吧！' }
                ],
                dialogTemplate: false, // 保存模板
                radio: 'templateLabel',
                newLabel: '', // 新标签
            }
        },
        created() {
            this.query()
        },
        watch: {
            // 通讯录筛选
            searchMail(val) {
                this.$refs.mailTree.filter(val)
            },
            mailConnet(val) {
                console.log(val.length)
                this.mailNumber = Number(350 - val.length)
            }
        },
        methods: {
            // 点击查看记录
            goRecord() {
                this.$router.push({
                    path: '/mass_email/emailrecord',
                    // path: '/system_manage/resource-add',

                })
            },
            // 切换通讯录
            switchMail() {
                this.isMail = true
                this.isTemplate = false
            },
            // 切换短信模板
            switchTemplate() {
                this.isMail = false
                this.isTemplate = true
            },
            // 选择联系人
            handleNodeClick(data) {
                console.log(data)
                let newdata = {
                    'name': '',
                    'mail': data.label
                }
                this.mailContacts.push(newdata)
                let obj = {}
                // 去重
                let peon = this.mailContacts.reduce((cur, next) => {
                    obj[next.mail] ? '' : obj[next.mail] = true && cur.push(next)
                    return cur
                }, [])
                this.mailContacts = peon
                this.useTtps()
            },
            removeTips() {
                this.noContacts = false
            },
            useTtps() {
                if (this.mailContacts.length == 0) {
                    this.noContacts = true
                } else {
                    this.noContacts = false
                }
            },
            // 手动添加联系人
            changeContacts() {
                let data = {
                    'name': '',
                    'mail': this.newContacts
                }
                this.mailContacts.push(data)
                this.newContacts = ''
            },
            removeContacts(index) {
                this.mailContacts.splice(index, 1)
                if (this.mailContacts.length == 0) {
                    this.noContacts = true
                }
            },
            // 监听筛选结果
            filterNode(value, data) {
                if (!value) return true
                return data.label.indexOf(value) !== -1
            },
            // 监听模板搜索
            handlerPageNo() {
                console.log('1111', this.searchTemplate)
            },
            // 监听短信模板分页
            handleCurrentChange(val) {
                console.log('当前', val)
            },
            // 选择模板
            useTemplate(val) {
                this.mailConnet = val
            },
            // 发送短信
            sendMail() {
                this.$confirm('短信已编辑完成, 是否发送?', '提示', {
                    confirmButtonText: '发送',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$message({
                        type: 'success',
                        message: '发送成功!'
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    })
                })
            }

        }
    }
</script>

<style lang="scss" scoped>
    #qf-body {
        background: white;
        font-size: 18px;
    }
    .qf-main {
        height: calc(100vh - 160px);
    }
    .qf-connet {
        .qf-header {
            border-bottom: 1px solid silver;
            display: flex;
            justify-content: space-between;
            line-height: 60px;
            p {
                text-align: center;
                color: blue;
            }
            .qf-title {
                border-bottom: 2px solid blue;
                font-weight: 700;
            }
            .qf-record {
                font-size: 15px;
                // margin-right: 80px;
                line-height: 80px;
            }
        }
        .qf-main {
            .qf-receiver {
                border-bottom: 1px solid silver;
                display: flex;
                margin-bottom: 20px;
                padding-bottom: 10px;
                .qf-people {
                    flex-shrink: 0;
                }
                .receiver {
                    float: left;
                    display: flex;
                    justify-content: start;
                    flex-wrap: wrap;
                    .qf-tips {
                        color: rgb(204, 204, 204);
                        left: 3px;
                        // display: none;
                    }
                    .qf-addrBase {
                        border: 1px solid silver;
                        border-radius: 35px;
                        padding: 0 6px;
                        line-height: 21px;
                        margin: 0 5px 5px 0;
                        a {
                            color: #333;
                            font-weight: 400;
                            font-size: 14px;
                        }
                    }
                    .qf-addrText {
                        border: none;
                        display: inline;
                        // margin-top: 5px;
                        .addrText-input {
                            background: none;
                            border: 0;
                            outline: none;
                            -webkit-appearance: none;
                            width: 90%;
                            float: left;
                            padding: 0;
                            padding-left: 4px;
                            height: 25px;
                            line-height: 21px;
                            color: #222;
                            font-weight: bold;
                        }
                    }
                    .qf-addrBase:hover {
                        border: 1px solid blue;
                    }
                }
            }
            .qf-textarea {
                border: 1px solid silver;
                border-radius: 10px;
                padding: 5px;
                textarea {
                    width: 100%;
                    height: 300px;
                    border: none;
                    outline: none;
                    resize: none;
                    border-bottom: 1px solid silver;
                    font-size: 18px;
                }
            }
            .qf-template {
                padding: 10px 0 0 0;
                font-size: 16px;
                a {
                    color: #367cb7;
                    border-bottom: 1px solid #367cb7;
                }
            }
            .qf-send {
                margin-top: 40px;
                display: flex;
                justify-content: center;
                .qf-sendbtn {
                    font-size: 18px;
                    padding: 8px 25px;
                }
            }
        }
    }
    .qf-aside {
        // border-bottom: 1px solid silver;
        border-left: 1px solid silver;
        .qf-header {
            display: flex;
            padding: 0;
            line-height: 60px;
            p {
                width: 50%;
                text-align: center;
                border-bottom: 1px solid silver;
                cursor: pointer;
            }
            .qf-yes {
                color: blue;
                border-bottom: 3px solid blue;
                font-weight: 700;
            }
        }
        .qf-main-mail {
            height: calc(100vh - 160px);
            background: #fcfcfc;
            .qf-mail {
                .qf-search {
                    font-size: 16px;
                    height: 34px;
                    line-height: 34px;
                    .el-icon-search {
                        margin-top: 5px;
                    }
                }
                p {
                    font-size: 16px;
                    padding: 8px 0;
                }
                .el-tree {
                    background: #fcfcfc;
                    span {
                        font-size: 16px;
                    }
                }
            }
        }
        .qf-main-template {
            height: calc(100vh - 160px);
            // overflow: hidden;
            .qf-mailtemplate {
                .qf-search {
                    font-size: 16px;
                    height: 34px;
                    line-height: 34px;
                    .el-icon-search {
                        margin-top: 5px;
                    }
                }
                .template-label {
                    display: flex;
                    justify-content: start;
                    flex-wrap: wrap;
                    font-size: 16px;
                    .label {
                        padding: 5px;
                    }
                    .label:hover {
                        cursor: pointer;
                        color: #367cb7;
                    }
                    .edit-label {
                        font-size: 14px;
                        padding: 3px;
                        border: 1px solid silver;
                        line-height: 24px;
                    }
                    .edit-label:hover {
                        cursor: pointer;
                        color: #367cb7;
                        border: 1px solid #367cb7;
                    }
                }
                .template-connet {
                    .template-ul {
                        width: 260px;
                        overflow: hidden;
                        .template-li {
                            width: 260px;
                            overflow: hidden;
                            word-wrap: break-word;
                            padding: 10px 0;
                            color: #333;
                        }
                        .template-li:hover {
                            cursor: pointer;
                            color: #367cb7;
                        }
                    }
                    .el-pagination {
                        padding: 2px 0;
                        .button {
                            min-width: 0;
                        }
                        ul {
                            li {
                                min-width: 30px;
                            }
                        }
                    }
                }
            }
        }
    }
    .lable-title {
        font-size: 18px;
        margin-bottom: 20px;
    }
    .lable-radio {
        padding-bottom: 80px;
        .el-radio {
            padding: 7px 0;
            .el-radio__label {
                font-size: 18px;
            }
        }
    }
    .dialog-footer {
        .el-button .el-button--default {
            font-size: 16px;
        }
    }
</style>
