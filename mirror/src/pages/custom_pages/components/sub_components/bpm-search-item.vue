<template>
    <!-- 服务台： 快速搜索结果 子组件 -->
    <div class="ptb10"
         :class="{'border-bottom' : !isLastOne}">
        <!-- 搜索工单类型 -->
        <template v-if="itemType === 'order'">
            <div class="displaybox">
                <div class="boxflex01 dark-blue f18 underline"
                     @click="gotoDetail(itemData.id, itemData.procDefName)"
                     v-html="handleKeyword(itemData.id)"></div>
                <div class="status-marking bgblue">{{handleStatus(itemData.status)}}</div>
                <!-- <div class="status-marking bgred mleft10">异常</div> -->
            </div>
            <div class="light-grey">{{itemData.subject}}</div>
            <el-row class="light-grey">
                <el-col :span="6">申请人</el-col>
                <el-col :span="18">{{itemData.creator}}</el-col>
            </el-row>
            <el-row class="light-grey">
                <el-col :span="6">创建时间</el-col>
                <el-col :span="18">{{itemData.createTime}}</el-col>
            </el-row>
            <el-row class="light-grey">
                <el-col :span="6">结束时间</el-col>
                <el-col :span="18">{{itemData.endTime}}</el-col>
            </el-row>
            <el-row class="light-grey">
                <el-col :span="6">当前审批人</el-col>
                <el-col :span="18">{{itemData.excutorName}}</el-col>
            </el-row>
            <el-row class="light-grey">
                <el-col :span="6">当前节点</el-col>
                <el-col :span="18">{{itemData.curNodeName}}</el-col>
            </el-row>
        </template>
        <!-- 搜索知识类型 -->
        <template v-if="itemType === 'knowledge'">
            <div @click="showDialog(itemData)">
                <div class="displaybox">
                    <div class="boxflex01 dark-blue f18 underline"
                         v-html="itemData.title"></div>
                </div>
                <div class="light-grey">{{itemData.lastTime}}</div>
                <div v-html="itemData.content"
                     v-if="itemData.content"
                     class="xzword">
                </div>
                <div v-html="handleKeyword(itemData.contentTxt)"
                     v-else
                     class="xzword">
                </div>
            </div>
        </template>
        <!-- 搜索人 -->
        <template v-if="itemType === 'user'">
            <div class="displaybox">
                <div class="dark-blue f18 underline"
                     v-html="handleKeyword(itemData.name)"></div>
                <div class="dark-blue f18 mleft10"
                     v-html="handleKeyword(itemData.code)"></div>
            </div>
            <div class="light-grey">
                <span>{{itemData.no}}</span>
                <span class="mleft10">{{itemData.dept_id}}</span>
                <span class="mleft10">{{itemData.user_type | handleUserType}}</span>
            </div>
            <el-row class="light-grey">
                <el-col :span="4">手机号</el-col>
                <el-col :span="20">{{itemData.mobile}}</el-col>
            </el-row>
            <el-row class="light-grey">
                <el-col :span="4">邮箱</el-col>
                <el-col :span="20">{{itemData.mail}}</el-col>
            </el-row>
        </template>

        <el-dialog title="内容"
                   class="yw-dialog"
                   :visible.sync="dialogVisible"
                   v-if="dialogVisible">
            <section class="yw-dialog-main">
                <div v-html="contentText"
                     class="dialog-content"></div>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbBpmHomeServices from 'src/services/bpm/rb-bpm-home-services.js'

    export default {
        props: {
            itemData: {
                type: Object,
                default() {
                    return {}
                }
            },
            itemType: {
                type: String,
                default: 'order'
            },
            searchWord: {
                type: String,
                default: ''
            },
            isLastOne: {
                type: Boolean,
                default: false
            }
        },
        filters: {
            handleUserType(str) {
                if (str === 1) {
                    return '正式用户'
                } else {
                    return '临时用户'
                }
            }
        },
        components: {
        },
        data() {
            return {
                dialogVisible: false,
                contentText: '',
                title: '网络策略',
                content: '网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述，网络策略内容描述。'
            }
        },
        watch: {
        },
        computed: {
        },
        mounted() {
            // this.showDialog()
        },
        methods: {
            // 搜索关键字变红
            handleKeyword(str = '') {
                let reg = new RegExp('(' + this.searchWord + ')', 'g')
                return str.replace(reg, function (letter) {
                    return `<span class="red">${letter}</span>`
                })
            },
            // 状态处理
            handleStatus(status) {
                if (['manualend', '人工结束'].indexOf(status) > -1) {
                    return '人工结束'
                } else if (['end', '结束'].indexOf(status) > -1) {
                    return '结束'
                } else {
                    return '运行中'
                }
            },
            // 跳转bpm工单详情
            gotoDetail(id, name) {
                this.$router.push({
                    path: '/resource/flow',
                    query: {
                        routerHash: 'inst/' + id,
                        currentTitle: name
                    }
                })
            },
            showDialog(data) {
                rbBpmHomeServices.getKnowledgeDetail(data._id).then(res => {
                    this.contentText = res.row.content
                    this.dialogVisible = true
                })
                // rbBpmHomeServices.getKnowledgeDetail(data._id).then(res => { })
            }
        }

    }
</script>

<style lang="scss" scoped>
    .xzword {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
</style>

<style lang="scss">
    .dialog-content {
        table,
        table tr th,
        table tr td {
            border: 1px solid #eee !important;
        }

        img {
            width: 500px;
            height: 300px;
            border: 1px solid #eee !important;
        }
    }
</style>


