<template>
    <div class="resourceManagement"
         v-loading="loading"
         :element-loading-text="loading_text">
        <div id="TREE"
             class="managementLeft">
            <div class="inputQuery">
                <el-input prefix-icon="el-icon-search"
                          v-model="filterText"
                          placeholder="请输入部门或业务名称查询"></el-input>
            </div>
            <div class="tree">
                <!-- <el-tree class="filter-tree"
                         :data="data2"
                         :props="defaultProps"
                         :filter-node-method="filterNode"
                         node-key="nodeId"
                         highlight-current
                         @node-click="nodeClick"
                         ref="tree2"
                         accordion>
                    <span class="custom-tree-node"
                          slot-scope="{ node, data }">
                        <span>
                            <span>
                                <i :class="data.icon"></i>
                                <span v-if="data.showInfo">{{node.label}} ({{data.showInfo}})</span>
                                <span v-else>{{node.label}}</span>
                            </span>
                        </span>
                    </span>
                </el-tree> -->
                <el-tree class="arrow-show"
                         :filter-node-method="filterNode"
                         :data="data2"
                         v-loading="treeLoading"
                         v-if="isRefresh"
                         :default-expand-all="true"
                         :props="defaultProps"
                         node-key="nodeId"
                         highlight-current
                         ref="tree2"
                         @node-click="changeTree"
                         accordion>
                    <span class="custom-tree-node"
                          slot-scope="{ node, data }">
                        <i class="icon-aow el-icon-caret-right"
                           :class="{'is-leaf':data['is-leaf']}"></i>
                        <i :class="data.icon"></i>
                        <span @click="queryViewsByTree(data)"><span>{{ data.nodeName }}</span><span v-if="data.showInfo">({{data.showInfo}})</span></span>

                    </span>
                </el-tree>
            </div>
        </div>
        <div class="managementRight">
            <div class="networkHead">
                <Tab ref="networkTab"
                     @tabCheck="tabCheck" />
                <Info v-show="!listShow"
                      ref="headerInfor"
                      :segmentAddr="segmentAddr"
                      :ipType="ipType" />
            </div>

            <instance-list :parentParams="bizSystemCondition"
                           v-show="listShow"
                           @tabCheck="tabCheck1"
                           :queryType="queryType"
                           @showLoading="showLoading"
                           @hideLoading="hideLoading"
                           @handleRefresh="handleRefresh"
                           :moduleId="moduleId"
                           :ipSingal="ipSingal"
                           :viewCodeName="viewCodeName"
                           :routerName="routerName"
                           :condicationCode="condicationCode"
                           :moduleType="moduleType"></instance-list>
            <otherinstance-list :parentParams="bizSystemCondition"
                                v-show="!listShow"
                                ref="otherinstance"
                                :queryType="queryType"
                                @resdata="resdata"
                                @showLoading="showLoading"
                                @hideLoading="hideLoading"
                                :moduleId="moduleId1"
                                :ipSingal="ipSingal"
                                :viewCodeName="viewCodeName"
                                :routerName="routerName"
                                :condicationCode="condicationCode1"
                                :moduleType="moduleType"></otherinstance-list>
        </div>
    </div>
</template>
<script>
    import instanceList from 'src/pages/resource/iframe/networkSegment/list/view.vue'
    // import otherinstanceList from 'src/pages/resource/iframe/networkSegment/list/otherView.vue'
    import otherinstanceList from 'src/pages/resource/iframe/networkSegment/list/view.vue'
    import rbCmdbModuleServiceFactory from 'src/services/cmdb/rb-cmdb-module-service.factory'
    import rbCmdbService from 'src/services/cmdb/rb-cmdb-service.factory'

    export default {
        name: 'ResourceIframeIpNet',
        components: {
            instanceList,
            otherinstanceList,
            Tab: () => import('../networkSegment/tab/view'),
            Info: () => import('../networkSegment/tab/info'),
        },
        data() {
            return {
                ipType: 'ipv6_ip',
                ipSingal: '',
                segmentAddr: '',
                listShow: true,
                moduleType: 'host',
                condicationCode: 'search_ipv6_segment',// 'instance_list','ip_repository_inner'
                condicationCode1: 'search_ipv6',
                loading: false,
                treeLoading: false,
                loading_text: '正在加载数据，请稍等...',
                businessUuid: '',
                moduleId: 'ca0950aa6f844223b9a7f266d9a2bbd1',
                moduleId1: '2ebf3cae36ab43a4bd25921a4ddac514',
                filterText: '',
                department1: '',
                department2: '',
                bizSystem: '',
                deviceType: '',
                bizSystemCondition: {},
                queryType: 'tree',
                data2: [],
                defaultProps: {
                    children: 'subNodeList',
                    label: 'nodeName',
                    isLeaf: 'leaf',
                    subSizeaa: 'subSize'
                },
                viewCodeName: 'ipv6segment_view',
                isRefresh: true,
                routerName: 'ResourceIframeIpNet'
            }
        },
        mounted: function () {
            this.initTree()
        },
        watch: {
            filterText(val) {
                this.$refs.tree2.filter(val)
            }
        },
        methods: {
            // 获得视图树
            queryViewsTree(param = {}, datas = '', rootNode = {}) {
                let params = {
                    viewCode: param.viewCode,
                    nodeList: param.nodeList,
                }
                this.treeLoading = true
                return rbCmdbService.getViewsTree(params).then((res) => {
                    if (!res.nodeList || res.nodeList.length < 1) {
                        datas['is-leaf'] = true
                    }
                    if (datas) {
                        this.$set(datas, 'subNodeList', res.nodeList)
                    } else {
                        this.$set(rootNode, 'treeDatas', res.nodeList)
                    }
                    return datas
                }).finally(() => {
                    this.treeLoading = false
                })
            },
            // 树选择
            changeTree(val) {
                console.log('树')
                console.log(val)
                let nodeList = [{ nodeId: val.nodeId, queryInfo: val.queryInfo, queryToData: val.queryToData }]
                if (!val.nodeId && !val.queryInfo) {
                    nodeList = null
                }
                let params = {
                    viewCode: 'ipv6segment_view',
                    nodeList: nodeList,
                }
                console.log(params)
                if (val.queryInfo) {
                    this.queryViewsTree(params, val)
                    // 右侧查询
                    // 跳转页面查询
                    // this.bizSystemCondition = {
                    //     all: {
                    //         value: val.nodeName,
                    //         key: val.nodeName,
                    //         id: val.nodeId,
                    //         type: val.queryInfo,
                    //         tree: val
                    //         // {                                idcType: 'MM',
                    //         //     inner_segment_sub_type: '存储网',
                    //         //     inner_segment_type: '管理网段',
                    //         //     network_segment_address: '192.168.5.1/28'                            }
                    //     }
                    // }
                }

            },
            // 根据树节点查询视图
            queryViewsByTree(val) {
                console.log('节点')
                // 跳转页面查询
                this.bizSystemCondition = {
                    all: {
                        value: val.nodeName,
                        key: val.nodeName,
                        id: val.nodeId,
                        type: val.queryInfo,
                        tree: val
                    }
                }
            },
            tabCheck(data) {
                console.log(data)
                this.segmentAddr = ''
                if (data) {
                    this.condicationCode = 'search_ipv6_segment'
                    this.moduleId = 'ca0950aa6f844223b9a7f266d9a2bbd1'
                    this.ipSingal = ''
                } else {
                    this.condicationCode1 = 'search_ipv6'
                    this.moduleId1 = '2ebf3cae36ab43a4bd25921a4ddac514'
                    this.ipSingal = 'all'
                }
                this.listShow = data
                if (!data) {
                    this.$nextTick(() => {
                        this.$refs['otherinstance'].getIpStatistics()
                    })

                }
            },
            tabCheck1(data) {
                console.log(data, 'tabCheck1-data')
                this.segmentAddr = data.query.network_segment_address
                if (data.check) {
                    this.condicationCode = 'search_ipv6_segment'
                    this.moduleId = 'ca0950aa6f844223b9a7f266d9a2bbd1'
                    this.ipSingal = ''
                } else {
                    this.condicationCode1 = 'search_ipv6'
                    this.moduleId1 = '2ebf3cae36ab43a4bd25921a4ddac514'
                    this.ipSingal = data.query.network_segment_address
                    if (data.query.ipv6_segment_address) {
                        this.bizSystemCondition = {
                            all: {
                                value: data.query.ipv6_segment_address,
                                key: data.query.ipv6_segment_address,
                                id: data.query.id,
                                type: {
                                    idcType: data.query.idcType_dict_note_name,
                                    ipv6_segment_type: data.query.ipv6_segment_type_dict_note_name,
                                    ipv6_segment_sub_type: data.query.ipv6_segment_sub_type_dict_note_name,
                                    ipv6_segment_address: data.query.ipv6_segment_address
                                },
                                tree: {
                                    queryToData: {
                                        idcType: data.query.idcType,
                                        ipv6_segment_type: data.query.ipv6_segment_type,
                                        ipv6_segment_sub_type: data.query.ipv6_segment_sub_type,
                                        ipv6_segment_address: data.query.ipv6_segment_address
                                    }
                                }
                            }
                        }
                    }
                }
                this.$refs.networkTab.ipBtn()
                this.listShow = data.check
            },
            // router() {
            //     console.log('this.$route.query.name', this.$route.query.name, this.$route.query.seriesName)
            //     // let businessUuid = document.getElementsByClassName('managementLeft')[0]
            //     // document.getElementById('TREE').style.background = 'red'
            //     // .console.log('0-0-0-0-', bbb)
            //     // bbb.style.background = 'red'
            // },
            showLoading(loading_text) {
                this.loading = true
                if (loading_text) {
                    this.loading_text = loading_text
                }
            },
            hideLoading() {
                this.loading = false
            },
            initTree() {
                let _t = this
                // orgManagerService.loadTreeDepBiz().then((data) => {
                //     // _t.data2 = data
                //     console.log(data)
                // }).catch(() => {
                // })
                // _t.data2 = []
                this.rbHttp.sendRequest({
                    method: 'post',
                    data: {
                        viewCode: 'ipv6segment_view'
                    },
                    url: '/v1/cmdb/view/getNextNodeData'
                }).then((res) => {
                    _t.data2 = res.nodeList
                    console.log(res)
                })
            },
            filterNode(value, data) {
                if (!value) return true
                return data.nodeName.indexOf(value) !== -1
            },
            resolveCondition(tree) {
                if (tree.data.type === 'module') {
                    this.moduleId = tree.data.uuid
                    this.deviceType = tree.data.name
                }
                if (tree.data.type === 'biz') {
                    this.bizSystem = tree.data.name
                }
                if (tree.data.type === 'department2') {
                    this.department2 = tree.data.name
                }
                if (tree.data.type === 'department1') {
                    this.department1 = tree.data.name
                    this.businessUuid = tree.data.uuid
                }
                if (tree.level !== 0) {
                    this.resolveCondition(tree.parent)
                }
            },
            nodeClick(node, tree) {
                console.log(tree)
                console.log('业务视图树', node)
                this.department1 = ''
                this.department2 = ''
                this.bizSystem = ''
                this.moduleId = ''
                this.deviceType = ''
                this.businessUuid = ''
                // 判断节点类型
                this.resolveCondition(tree)
                if (node.type && node.type === 'biz') {
                    if (!node.subList || node.subList.length === 0) {
                        // 加载模型数据
                        let _t = this
                        rbCmdbModuleServiceFactory.getModuleTree().then((data) => {
                            _t.$set(node, 'subList', [])
                            data.forEach((item) => {
                                if (item.childModules && item.childModules.length > 0) {
                                    item.childModules.forEach((item2) => {
                                        node.subList.push({ uuid: item2.id, name: item2.name, icon: item2.iconUrl, type: 'module' })
                                    })
                                }
                            })
                        })
                    }
                }
                // 跳转页面查询
                this.bizSystemCondition = {
                    department1: this.department1,
                    department2: this.department2,
                    bizSystem: this.bizSystem,
                    moduleId: this.moduleId,
                    // moduleId: node.uuid,
                    deviceType: this.deviceType,
                    businessUuid: this.businessUuid,
                    all: {
                        value: node.name,
                        key: node.name,
                        id: node.uuid,
                        type: node.type,
                        tree: tree
                    }
                }
            },
            refresh() {
                this.department1 = ''
                this.filterText = ''
                this.initTree()
            },
            resdata(data) {
                if (!this.listShow) {
                    this.$nextTick(() => {
                        if (this.$refs['headerInfor']) {
                            this.$refs['headerInfor'].ipGetRes(data)
                        }
                    })
                }

            },
            handleRefresh() {
                this.isRefresh = false
                setTimeout(() => {
                    this.isRefresh = true
                }, 1000)
            }
        }
    }
</script>
<style lang="stylus" scoped>
    .networkHead {
        padding: 0 10px;
    }

    .resourceManagement {
        display: flex;
        overflow: auto;
        overflow: hidden;
        height: 100%;
        width: 100%;

        .managementLeft {
            box-shadow: 0px 0px 8px 0px rgba(4, 0, 0, 0.13);
            flex: inherit;
            width: 250px;
            height: 97%;
            padding: 10px;
            background: #fff;
            overflow-y: auto;
            overflow-x: auto;

            .inputQuery {
                width: 90%;
                margin: 0 auto;
            }
        }

        .managementRight {
            overflow-y: auto;
            width: calc(100% - 250px);
            height: 100%;
        }
    }
</style>
<style lang="stylus">
    .managementLeft .el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content {
        color: #2089DA;
    }
</style>
