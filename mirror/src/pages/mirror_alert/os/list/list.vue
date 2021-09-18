<template>
    <div>
        <div class="managementRight">
            <instance-view :parentParams="bizSystemCondition"
                           :queryType="queryType"
                           @showLoading="showLoading"
                           @hideLoading="hideLoading"></instance-view>
        </div>
    </div>
</template>
<script>
    import instanceView from 'src/pages/mirror_alert/os/instance/instance-view.vue'
    export default {
        name: 'ResourceIframeResourceManagement',
        components: {
            instanceView
        },
        data() {
            return {
                loading: false,
                loading_text: '正在加载数据...',
                moduleId: '',
                filterText: '',
                department1: '',
                department2: '',
                bizSystem: '',
                bizSystemCondition: {},
                queryType: 'normal',
                data2: [],
                defaultProps: {
                    children: 'subList',
                    label: 'name',
                    isLeaf: 'leaf',
                    subSizeaa: 'subSize'
                }
            }
        },
        mounted: function() {
        },
        watch: {
        },
        methods: {
            showLoading(loading_text) {
                this.loading = true
                if (loading_text) {
                    this.loading_text = loading_text
                }
            },
            hideLoading() {
                this.loading = false
            }
        }
    }
</script>
<style lang="stylus" scoped>
  .resourceManagement {
    height:100%;
    width:100%;
    .managementLeft {
      float:left;
      width:250px;
      height:100%;
      padding:10px;
      background:#fff;
      overflow-y:auto;
      .inputQuery {
        width:90%;
        margin:0 auto;
      }
    }
    .managementRight {
      float:right;
      width:calc(100%- 260px);
      height:100%;
    }
  }
</style>
