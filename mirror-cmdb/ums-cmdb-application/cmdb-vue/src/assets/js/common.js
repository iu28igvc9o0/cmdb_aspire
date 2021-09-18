  // 对Date的扩展，将 Date 转化为指定格式的String
  // 月(M)、日(d)、小时(H)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
  // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
  // 例子： 
  // (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
  // (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
  Date.prototype.format = function(fmt) { //author: meizz 
      var o = {
          "M+": this.getMonth() + 1, //月份 
          "d+": this.getDate(), //日 
          "H+": this.getHours(), //小时 
          "m+": this.getMinutes(), //分 
          "s+": this.getSeconds(), //秒 
          "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
          "S": this.getMilliseconds() //毫秒 
      };
      if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
      for (var k in o)
          if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      return fmt;
  }
  var utilHelper = {

      getCookie: function(name) {
          var start = document.cookie.indexOf(name + "=");
          var len = start + name.length + 1;
          if ((!start) && (name != document.cookie.substring(0, name.length))) {
              return null;
          }
          if (start == -1)
              return null;

          var end = document.cookie.indexOf(';', len);
          if (end == -1)
              end = document.cookie.length;

          return unescape(document.cookie.substring(len, end));
      },
      getTokenUrl: function(url) {
          var COOKIE_TICKET = 'ticket';
          var vTicket = this.getCookie(COOKIE_TICKET);
          var ckName = "jsessionid";
          var arr = document.cookie.match(new RegExp("(^| )" + ckName + "=([^;]*)(;|$)"));
          if (arr != null) {
              vTicket = unescape(arr[2]);
          } else {
              arr = document.cookie.match(new RegExp("(^| )" + ckName.toUpperCase() + "=([^;]*)(;|$)"));
              if (arr != null) {
                  vTicket = unescape(arr[2]);
              }
          }
          var pn = url;
          var end = pn.indexOf("/", 1);
          var CONTEXT_PATH = pn.substring(0, end);
          var COOKIE_DOMAIN = 'sys_domain';

          var vDomain = this.getCookie(COOKIE_DOMAIN);


          if (vDomain == null || vDomain == undefined) {
              vDomain = CONTEXT_PATH.substring(1);
          }
          if (url.indexOf("?") == -1) {
              return url + "?ticket=" + vTicket + "&domain=" + vDomain;
          } else {
              return url + "&ticket=" + vTicket + "&domain=" + vDomain;
          }
      },
      getRandom:function(m,n){
            
            var num = Math.round(Math.random()*(n-m)+m);
            return num;
      }


  }

  export default utilHelper