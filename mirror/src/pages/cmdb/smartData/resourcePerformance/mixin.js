export const myMixin={
  data(){
    return {
      loading:true,
      hostName:''
    }
  },
  created() {
    if(window.location.hostname==='10.153.1.36'){
      this.hostName='http://10.153.1.52:8090'
    }else{
      this.hostName='http://10.1.203.100:9080'
    }
  },
  mounted() {
    const { iframes } = this.$refs
    // IE和非IE浏览器，监听iframe加载事件不一样，需要兼容
    const that = this
    if (iframes.attachEvent) {
      // IE
      iframes.attachEvent('onload', () => {
        that.stateChange()
      })
    } else {
      // 非IE
      iframes.onload = function () {
        that.stateChange()
      }
    }
  },
  methods: {
    stateChange() {
      this.loading = false
    }
  }
}