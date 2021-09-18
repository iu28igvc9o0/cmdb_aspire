import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-default/index.css'; // Ĭ������
// import '../static/css/theme-green/index.css';       // ǳ��ɫ����
import util from './assets/js/common';
import 'font-awesome/css/font-awesome.css'//引入icon图标库

Vue.config.productionTip = false;
Vue.use(ElementUI);

new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App },
  data:{
  	Bus: new Vue()//非父子组件通信使用
  }
})
