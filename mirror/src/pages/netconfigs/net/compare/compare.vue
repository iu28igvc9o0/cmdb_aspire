<template>
    <!-- 内容对比 -->
    <div class="compare-wrap">

        <div class="back-icon-wrap">
            <i class="el-icon-back"
               @click="goBack()"></i>
        </div>
        <div id="view"
             class="compare-content"
             v-loading="loading"></div>
        <!-- <div v-loading="loading">
            <el-pagination v-if="resList.length > 1000 || newList.length > 1000"
                           background
                           layout="prev, pager, next"
                           :page-size="1000"
                           :total='total'
                           @current-change="handleCurrentChange">
            </el-pagination>
            <vueCodeDiff :old-string="oldStr"
                         :new-string="newStr"
                         :context="1000"
                         :outputFormat="outputFormat" />
        </div> -->

    </div>

</template>
<script>
    import CommonOption from 'src/utils/commonOption.js'
    import CodeMirror from 'codemirror'
    import 'codemirror/lib/codemirror.css'
    import 'codemirror/addon/merge/merge.js'
    import 'codemirror/addon/merge/merge.css'
    import DiffMatchPatch from 'diff-match-patch'
    window.diff_match_patch = DiffMatchPatch
    window.DIFF_DELETE = -1
    window.DIFF_INSERT = 1
    window.DIFF_EQUAL = 0

    export default {
        mixins: [CommonOption],
        props: ['resultSelected'],
        data() {
            return {
                resourse: [],
                oldStr: '第一条数据：',
                newStr: '第二条数据：',
                loading: false,
            }
        },
        mounted() {
            this.init()
        },
        methods: {
            // 初始化
            async init() {
                this.showLoading()
                Promise.all([
                    this.getDatas(this.resultSelected[0], 1),
                    this.getDatas(this.resultSelected[1], 2),
                ]).then(() => {
                    this.closeLoading()
                    this.compare(this.oldStr, this.newStr)
                })
            },
            // 对比
            compare(oldData, newData) {
                if (oldData == null) return
                let target = document.getElementById('view')
                target.innerHTML = ''
                CodeMirror.MergeView(target, {
                    value: oldData,// 上次内容
                    origLeft: null,
                    orig: newData,// 本次内容
                    lineNumbers: true,// 显示行号
                    mode: 'text/html',
                    highlightDifferences: true,
                    connect: 'align',
                    readOnly: true,// 只读 不可修改
                })
            },
            // 返回
            goBack() {
                this.$emit('changeCompare', false)
                this.$router.push({ path: '/netconfigs/net/list' })
            },
            // 获得数据
            getDatas(resourse = {}, index = 1) {
                return this.rbHttp.sendRequest({
                    method: 'GET',
                    url: `/v1/config/getConfigById/${resourse.index}/${resourse.id}`
                }).then((res) => {
                    // 数据行数限制(最多1000行)
                    let resList = res.massage.split(/\n/g)
                    let limitList = []
                    let lineNumber = resList.length > 1000 ? 1000 : resList.length
                    for (let i = 0; i < lineNumber; i++) {
                        limitList.push(resList[i])
                    }
                    let limitStr = limitList.join('/\n/g')

                    if (index === 1) {
                        this.oldStr = `第一条数据：\n${limitStr}`
                    } else {
                        this.newStr = `第二条数据：\n${limitStr}`
                    }

                    return res.massage
                })
            },
        }
    }
</script>
<style lang="scss" scoped>
    .compare-wrap {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        background: #fff;
        z-index: 2;
        height: calc(100% - 20px);
        overflow-y: hidden;
        margin-bottom: 20px;
        .compare-content {
            height: calc(100% - 50px);
            overflow: auto;
            /deep/.CodeMirror-merge {
                height: 100%;
                .CodeMirror {
                    height: 100%;
                }
            }
        }
    }
</style>
