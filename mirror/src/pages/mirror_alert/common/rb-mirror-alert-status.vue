<template>
    <div v-rubick-status-icon="{status:statusCssMap[status],status_text: statusTranslateMap[status],status_class: statusClass}">
    </div>
</template>
<script>
    import Directives from 'src/utils/directives.js'
    import { ALERT_LEVEL_CSS_MAP, ALERT_LEVEL_MAP } from 'src/pages/mirror_alert/main/rb-alert.constants.js'
    export default {
        mixins: [Directives],
        components: {
        },
        props: [
            'mode', 'status'
        ],
        data () {
            return {
                statusClass: this.mode === 'list' ? 'status' : 'status-detail ',
                statusCssMap: ALERT_LEVEL_CSS_MAP,
                statusTranslateMap: ALERT_LEVEL_MAP
            }
        },
        mounted: function () {
            console.log('status===',this.status)
            console.log('mode===',this.mode)
        },
        methods: {
        }
    }
</script>
<style scoped lang="scss">
.status {
  display: inline-block;
  color: $color-base;
  height: 18px;
  line-height: 15px;
  border-radius: 9px;
  text-align: center;
  width: 50px;
  margin: 0 auto;
  &.detail-status {
    padding: 2px 5px;
    &.status-serious {
      // background-color: $color-tip-red;
      background-color: #F5222D;
    }
    &.status-high {
      // background-color: $color-tip-orange;
      background-color: #FA541C;
    }
    &.status-medium {
      // background-color: $color-tip-yellow;
      background-color: #FFCC00;
    }
    &.status-low {
      // background-color: $color-tip-blue;
      background-color: #1890FF;
    }
    &.status-tip {
      background-color: $color-tip-green;
    }
  }
}
.status-detail {
  display: inline-block;
  color: $color-base;
  height: 18px;
  line-height: 15px;
  border-radius: 9px;
  text-align: center;
  width: 50px;
  /*margin: 0 auto;*/
  &.detail-status {
    padding: 2px 5px;
    &.status-serious {
      background-color: $color-tip-red;
    }
    &.status-high {
      background-color: $color-tip-orange;
    }
    &.status-medium {
      background-color: $color-tip-yellow;
    }
    &.status-low {
      background-color: $color-tip-blue;
    }
    &.status-tip {
      background-color: $color-tip-green;
    }
  }
}
</style>
