
import resourceDataService from 'src/services/sys/reource-services.js'
export default {
    data() {
        return {
            isSecondInited: false,
            // 自动化资源
            pageLoading: false,
            loading_text: '请稍候...',
            activeName: 'first',
            treeTitle1: '',
            treeTitle2: '',
            treeTitle3: '',

            // 分组资源
            groupSourceData: [],
            groupSourceDataExpanded: [],
            groupSourceDataChecked: [],
            groupSourceDataDefault: {
                label: 'group_name',
                children: 'sub_group_list',
                // eslint-disable-next-line no-unused-vars
                disabled: (data, node) => {
                    if (this.$route.path.includes('resource-add')) {
                        return false
                    } else {
                        return true
                    }
                }
            },
            // 作业、脚本、yum资源
            workSourceData: [],
            scriptSourceData: [],
            yumSourceData: [],

            workSourceDataExpanded: [],
            scriptSourceDataExpanded: [],
            yumSourceDataExpanded: [],

            workSourceDataChecked: [],
            scriptSourceDataChecked: [],
            yumSourceDataChecked: [],

            otherSourceDataDefault: {
                label: 'resource_name',
                children: 'sub_resource_list',
                // eslint-disable-next-line no-unused-vars
                disabled: (data, node) => {
                    if (this.$route.path.includes('resource-add')) {
                        return false
                    } else {
                        return true
                    }
                }
            },

            ROOT_ID: '000000'
        }
    },
    watch: {
        activeName(val) {
            if (val === 'first') {
                this.treeTitle1 = this.treeTitle2 = this.treeTitle3 = ''
            } else {
                this.treeTitle1 = '作业资源'
                this.treeTitle2 = '脚本资源'
                this.treeTitle3 = 'YUM资源'
            }
        }
    },
    methods: {
        /**
         * 自动化资源
         * 20200316
         */
        // 自动化资源详情
        handleAutoOperationDetailData() { },
        // 获取分组资源
        querGroupTree() {
            resourceDataService.querGroupTree().then(res => {
                this.groupSourceData = res
                this.groupSourceDataExpanded.push(res[0].group_id)
            })
        },
        // 获取作业、脚本、yum资源
        querOpsResourceTree(req) {
            resourceDataService.querOpsResourceTree(req).then(res => {
                switch (req.objectType) {
                    case 'pipeline':
                        this.workSourceData = res
                        this.workSourceDataExpanded.push(res[0].resource_id)
                        break
                    case 'script':
                        this.scriptSourceData = res
                        this.scriptSourceDataExpanded.push(res[0].resource_id)
                        break
                    case 'yum':
                        this.yumSourceData = res
                        this.yumSourceDataExpanded.push(res[0].resource_id)
                        break

                }
            })
        },
        // 切换tab
        // eslint-disable-next-line no-unused-vars
        handleClick(tab, event) {
            this.activeName = tab.name

            if (this.activeName === 'second' && !this.isSecondInited) {
                this.initAutoOperation()
            }
        },
        // 初始化设备资源
        async initDevice(data) {
            await this.getAreaData()
            // 判断是否是cdn
            if (data != 'cdn') {
                await this.getBizSysData()
                await this.getDevicetypeList()
            }

            this.$nextTick(() => {
                if (this.$route.query.id) {
                    this.getResourceDetail(this.$route.query.id)
                }
            })
        },
        // 初始化自动化资源
        initAutoOperation() {
            this.querGroupTree()
            this.querOpsResourceTree({ objectType: 'pipeline' })
            this.querOpsResourceTree({ objectType: 'script' })
            this.querOpsResourceTree({ objectType: 'yum' })
            this.isSecondInited = true
        }
    }
}
