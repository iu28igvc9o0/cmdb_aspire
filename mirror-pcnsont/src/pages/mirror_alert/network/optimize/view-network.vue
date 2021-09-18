<template>
  <div class="iframe-wrap">
    <iframe :src="page"
            v-if="page"
            class="iframe"
            scrolling="no" style="margin-top: -94px; margin-left: -60px" ></iframe>
    <div v-else>没找到可用视图</div>
  </div>
</template>
<script>
  import rbLdapValidCodeFactory from "../../../../services/rbac_role/rb-ldap-validCode.factory";
  export default {
    props: ['idc'],
    data () {
      return {
        page: ''
      }
    },
    created () {
      this.getValidCode()
    },
    methods: {
      getValidCode () {
        rbLdapValidCodeFactory.getValidCode().then(data => {
          if (this.idc) {
            this.page = this.idc + '/network/singleSignOn/login?menuType=01&userName=admin&checkCode=' + data.checkCode
          } else {
            this.page ='/network/singleSignOn/login?menuType=01&userName=admin&checkCode=' + data.checkCode
          }
        })
      }
    }
  }
</script>
<style>
  .iframe {
    width: 100%;
    border-width: 0;
    height: calc(100vh);
  }
</style>
