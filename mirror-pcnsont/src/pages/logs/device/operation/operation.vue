<template>
  <div class="components-container yw-dashboard">
    <list :ruleType="'operation'"></list>
  </div>
</template>

<script>
  import list from 'src/pages/logs/device/list/list.vue'
  export default {
    mixins: [],
    components: {
      list
    },
    data () {
      return {
      }
    },
    created () {
    },
    methods: {
    }
  }
</script>
<style lang="scss" scoped>
</style>
