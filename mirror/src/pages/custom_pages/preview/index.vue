<template>
    <div v-loading.fullscreen.lock="pageLoading"
         :element-loading-text="loading_text">
        <draggable class="drag-box"
                   draggable=".item-draggable"
                   :options="Contentoptions"
                   v-model="currentPageData">
            <div v-for="(row, rowIndex) in currentPageData"
                 :key="rowIndex"
                 class="drag-row"
                 :class="{
                     'DragHover': (hover == row.name && !readonly), 
                     'item-draggable': !readonly, 
                     'page-readonly': readonly, 
                     'no-top-radius': !row.childrenHasTitle
                     }"
                 style="position:relative">

                <el-tooltip class="item"
                            v-if="!readonly"
                            effect="dark"
                            content="拖动整行"
                            placement="bottom"
                            :hide-after=1000>
                    <div class="topDrag"
                         @mouseenter="Itemhover(row.name)"
                         @mouseleave="Itemhover(-1)">
                        <i class="iconfont iconyidonghuabu"></i>
                        <span class="light-blue pointer t-right absolute delete-row-btn"
                              @click="removeRow(rowIndex)">移除整行</span>
                    </div>
                </el-tooltip>

                <!-- 行模块标题和边框 -->
                <div class="content-chart no-content"
                     v-if="!row.childrenHasTitle">
                    <section class="chart-title-wrap clearfix">
                        <svg class="svg-icon svg-icon-24"
                             aria-hidden="true">
                            <use xlink:href="#icongaojing"></use>
                        </svg>
                        <el-input class="chart-title"
                                  v-if="false"
                                  v-model="row.name"
                                  clearable
                                  placeholder="请输入模块名称"></el-input>
                        <span v-else
                              class="chart-title">{{row.name}}</span>
                    </section>
                </div>
                <draggable :list="row.children"
                           group="viewInfo"
                           class="content"
                           draggable=".item-draggable"
                           :class="{'highLight': flag && rowIndex != currentModuleIndex, 'bgwhite' : curPageType === 'light'}"
                           :style="{minHeight: row.minHeight}">
                    <div v-for="(item, index) in row.children"
                         class="content-item relative"
                         :class="{'has-no-title': !row.childrenHasTitle, 'no-border-high-light': readonly, 'item-draggable mtop10': !readonly}"
                         :style="handleWidth(row.children.length, item)"
                         :key="index"
                         @click="itemClick(item)"
                         @dragenter="(e) => dragover(e, item, index)"
                         @dragleave="dragleave">
                        <div class="light-blue pointer t-right absolute delete-module-btn"
                             v-if="!readonly"
                             alt="移除模块"
                             @click="removeModule(rowIndex, item, index)">移除模块</div>
                        <component :is="item && componentsRequire[item.name] && componentsRequire[item.name].default"
                                   :conditionParams="conditionParams"
                                   :isCustomPage="true"
                                   :key="index"></component>
                    </div>
                </draggable>
            </div>
        </draggable>
        <!-- dialog -->
        <el-dialog class="yw-dialog"
                   v-if="dialogBoxShow"
                   title="新增模块"
                   :visible.sync="dialogBoxShow"
                   :close-on-click-modal="false"
                   :destroy-on-close="true"
                   width="500px">
            <section class="yw-dialog-main">
                <el-form class="yw-form components-condition"
                         ref="addForm"
                         :model="addForm"
                         :rules="addFormRules"
                         label-width="140px">
                    <el-form-item label="是否显示模块名称:"
                                  prop="showName">
                        <el-radio-group v-model="addForm.showName">
                            <el-radio :label="1"
                                      name="type">是</el-radio>
                            <el-radio :label="0"
                                      name="type">否</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="模块名称:"
                                  v-if="addForm.showName === 1"
                                  prop="name">
                        <el-input v-model="addForm.name"
                                  placeholder="请输入模块名称"></el-input>
                    </el-form-item>
                </el-form>
            </section>
            <section class="btn-wrap">
                <el-button type="primary"
                           @click="checkModuleName()">确定</el-button>
                <el-button @click="dialogBoxShow = false">取消</el-button>
            </section>
        </el-dialog>
    </div>
</template>

<script>
    import rbCustomServices from 'src/services/custom_pages/rb-services.js'
    import draggable from 'vuedraggable'
    import drag from '../mixin/drag'
    import rbAutoOperationMixin from 'src/services/auto_operation/rb-auto-operation-mixin.js'
    import modluesData from '../mixin/modules'

    export default {
        name: 'CustomPagesPreview',
        props: {
            readonly: {
                type: Boolean,
                default: true
            },
            pageCode: {
                type: String,
                default: 'index'
            },
            pageType: {
                type: String,
                default: 'dark'
            },
            viewsData: {
                type: Array,
                default() {
                    return []
                }
            },
        },
        mixins: [drag, rbAutoOperationMixin],
        components: {
            draggable,
        },
        data() {
            return {
                pageLoading: false,
                loading_text: '请稍候...',
                value: 'index',
                currentPageData: [],
                resPageType: '',    // 接口返回的页面类型
                // 查询条件
                conditionParams: {
                    // 日期范围
                    dateRange: [],

                    // 资源
                    poolActive: '',
                    poolList: [{ value: '', name: '全部' }]
                },
                componentNames: [],
                dialogBoxShow: false,
                addForm: {
                    showName: '',
                    name: '',
                },
                addFormRules: {
                    showName: [
                        {
                            required: true,
                            message: '请选择是否显示模块名称!',
                            trigger: ['blur', 'change']
                        },
                    ],
                    name: [
                        {
                            required: true,
                            message: '请输入模块名称!',
                            trigger: ['blur', 'change']
                        },
                        {
                            min: 2,
                            max: 10,
                            message: '长度在 2 到 10 个字符!',
                            trigger: ['blur', 'change']
                        }
                    ],
                },

            }
        },
        computed: {
            // 动态引入组件
            componentsRequire() {
                let componentObj = {}
                this.componentNames.forEach(name => {
                    componentObj[name] = require(`src/pages/custom_pages/components/${name}.vue`)
                })
                return componentObj
            },
            // 页面源数据
            originPageData() {
                // 筛选出相同pageType的模版
                const theSamePageTypeModules = modluesData.filter(item => {
                    return this.pageType === item.pageType
                })
                // 从相同pageType的模版里面选择一个默认模版
                let singleModule = theSamePageTypeModules.find(item => {
                    return item.code === this.pageCode
                })
                // 如果没有当前pageCode的模版，则默认第一个
                singleModule = singleModule || theSamePageTypeModules[0]
                return this.$utils.deepClone(singleModule.data) || []
            },
            // 当前行视图内容
            storeRowContent() {
                return this.$store.state.homeStore.currentRowContent
            },
            username() {
                return sessionStorage.getItem('username')
            },
            curPageType() {
                return this.resPageType || this.pageType
            }
        },
        watch: {
            currentPageData: {
                handler(newVal) {
                    // 编辑状态，触发更新
                    if (!this.readonly) {
                        this.$emit('updatecurrentPageData', newVal)
                    }
                    this.componentNames = this.handleComponentNames(newVal)
                },
                deep: true,
                immediate: true
            },
            // 只读状态，获取最新定制化数据
            readonly: {
                handler(newVal) {
                    if (newVal && this.$route.fullPath.includes('/custom_pages/main')) {
                        this.getPageViewByLdapId()
                    }
                },
                immediate: true
            },
            viewsData: {
                handler(newVal) {
                    // viewsData有值，设置为定制化页面视图内容；否则使用默认页面数据 this.currentPageData
                    if (newVal.length && !this.readonly) {
                        this.currentPageData = this.viewsData
                    } else if (!this.readonly) {
                        this.currentPageData = this.originPageData
                    }
                },
                immediate: true,
                deep: true
            }
        },
        methods: {
            // 获取组件名称
            handleComponentNames(data) {
                let arr = []
                data.forEach(item => {
                    item.children.forEach(child => {
                        arr.push(child.name)
                    })
                })
                return arr
            },
            // 模块名称
            checkModuleName() {
                this.$refs.addForm.validate((valid) => {
                    if (!valid) {
                        this.$message.warning('请填写模块名称')
                        return
                    }
                    this.dialogBoxShow = false
                    this.addModule()
                })
            },
            // 新增模块
            addModule() {
                let isDuplication = false
                this.currentPageData.forEach(row => {
                    if (this.addForm.showName === 1 && row.name === this.addForm.name) {
                        this.$message.warning('模块名称重复！')
                        isDuplication = true
                    }
                })
                if (isDuplication) {
                    return
                }
                this.currentPageData.push({
                    name: this.addForm.name,
                    type: 'row',
                    childrenHasTitle: this.addForm.showName === 0,
                    children: []
                })
                this.$emit('scrollToBottom')
            },
            // 移除整行
            removeRow(rowIndex) {
                this.$confirm('确定移除该行？').then(() => {
                    this.$bus.emit('updateMenuList', this.currentPageData[rowIndex].children)
                    this.currentPageData.splice(rowIndex, 1)
                })
            },
            // 移除模块
            removeModule(rowIndex, item, index) {
                this.$confirm('确定移除该模块？').then(() => {
                    this.$bus.emit('updateMenuList', item)
                    this.currentPageData[rowIndex].children.splice(index, 1)
                })
            },
            // 还原模块
            resetModule() {
                this.currentPageData = this.originPageData
            },
            handleWidth(length, item) {
                let style = {
                    // width: (100 / length - 0.5) + '%',
                    minWidth: item && item.minWidth,
                    maxWidth: item && item.maxWidth,
                    // flex: 'none'
                }
                if (item.minWidth === '100%') {
                    style.marginLeft = '0'
                }
                if (item.height) {
                    style.height = item.height
                }
                return style
            },
            // 获取定制化内容
            getPageViewByLdapId() {
                let req = {
                    ldapId: this.username
                }
                this.pageLoading = true

                // 同一个接口，swagger可以访问，5566报500，须加到5566的网关路由里
                rbCustomServices
                    .getPageViewByLdapId(req)
                    .then(res => {
                        this.pageLoading = false
                        this.currentPageData = JSON.parse(this.getCurrentViewData(res))
                    })
                    .catch(error => {
                        this.pageLoading = false
                        this.showErrorTip(error)
                    })
            },
            // 根据pageCode获取对应页面内容
            getCurrentViewData(data) {
                let curData = data.find(item => {
                    return item.pageCode === this.pageCode
                })
                if (!curData) {
                    curData = data.find(item => {
                        return item.pageCode === 'index'
                    })
                }
                this.resPageType = curData.pageType
                this.$emit('updatePageType', this.curPageType)
                return curData.pageCustomConfig
            }
        },
        destroyed() {
            this.$bus.off('updateMenuList')
        }
    }
</script>

<style lang="scss" scoped>
    @import "../mixin/drag.scss";
</style>