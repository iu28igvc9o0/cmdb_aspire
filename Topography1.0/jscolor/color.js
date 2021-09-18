! function (e) {
  var t = {};

  function r(n) {
    if (t[n])
      return t[n].exports;
    var o = t[n] = {
      i: n,
      l: !1,
      exports: {}
    };
    return e[n].call(o.exports, o, o.exports, r),
      o.l = !0,
      o.exports
  }
  r.m = e,
    r.c = t,
    r.d = function (e, t, n) {
      r.o(e, t) || Object.defineProperty(e, t, {
        enumerable: !0,
        get: n
      })
    },
    r.r = function (e) {
      "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {
          value: "Module"
        }),
        Object.defineProperty(e, "__esModule", {
          value: !0
        })
    },
    r.t = function (e, t) {
      if (1 & t && (e = r(e)),
        8 & t)
        return e;
      if (4 & t && "object" == typeof e && e && e.__esModule)
        return e;
      var n = Object.create(null);
      if (r.r(n),
        Object.defineProperty(n, "default", {
          enumerable: !0,
          value: e
        }),
        2 & t && "string" != typeof e)
        for (var o in e)
          r.d(n, o, function (t) {
              return e[t]
            }
            .bind(null, o));
      return n
    },
    r.n = function (e) {
      var t = e && e.__esModule ? function () {
          return e.default
        } :
        function () {
          return e
        };
      return r.d(t, "a", t),
        t
    },
    r.o = function (e, t) {
      return Object.prototype.hasOwnProperty.call(e, t)
    },
    r.p = "",
    r(r.s = 1)
}([function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
    value: !0
  });
  var n = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
      return typeof e
    } :
    function (e) {
      return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
    };

  function o(e) {
    return "string" == typeof e
  }

  function i(e) {
    return null !== e && "object" === (void 0 === e ? "undefined" : n(e))
  }

  function a(e) {
    return "[object Array]" === Object.prototype.toString.call(e)
  }
  t.isNumber = function (e) {
      return "number" == typeof e
    },
    t.isStr = o,
    t.isUndefined = function (e) {
      return void 0 === e
    },
    t.isNull = function (e) {
      return null === e
    },
    t.isFunction = function (e) {
      return "function" == typeof e
    },
    t.isShallowObject = i,
    t.isDeepObject = function (e) {
      return "[object Object]" === Object.prototype.toString.call(e)
    },
    t.isDeepArray = a,
    t.ewObjToArray = function (e) {
      if (e && e.length)
        return Array.prototype.slice.call(e)
    },
    t.isIB = function (e) {
      return e.indexOf("inline-block") > -1
    },
    t.isStat = function (e) {
      return e.indexOf("static") > -1
    },
    t.isRel = function (e) {
      return e.indexOf("relative") > -1
    },
    t.isAbs = function (e) {
      return e.indexOf("absolute") > -1
    },
    t.isDom = function (e) {
      return "object" === ("undefined" == typeof HTMLElement ? "undefined" : n(HTMLElement)) ? e instanceof HTMLElement : e && "object" === (void 0 === e ? "undefined" : n(e)) && 1 === e.nodeType && "string" == typeof e.nodeName || e instanceof HTMLCollection || e instanceof NodeList
    },
    t.ewAssign = function (e, t) {
      if (null === e)
        return;
      if (Object.assign)
        return Object.assign(e, t);
      for (var r = Object(e), n = 1; n < arguments.length; n++) {
        var o = arguments[n];
        if (o)
          for (var i in o)
            Object.prototype.hasOwnProperty.call(o, i) && (r[i] = o[i])
      }
      return r
    },
    t.ewError = function (e) {
      return new Error(e)
    },
    t.deepCloneObjByJSON = function (e) {
      return JSON.parse(JSON.stringify(e))
    },
    t.cssObjToStr = function (e) {
      if (!i(e))
        return;
      var t = "";
      for (var r in e)
        t += s(r) + ":" + e[r] + ";";
      return t
    },
    t.keba = s,
    t.getCss = function (e, t) {
      var r = e.currentStyle ? function (t) {
          var n = e.currentStyle[t];
          if (n.indexOf("height") > -1 && n.search(/px/i) > -1) {
            var o = e.getBoundingClientRect;
            return o.bottom - o.top - parseInt(r("padding-bottom")) - parseInt(r("padding-top")) + "px"
          }
        } :
        function (t) {
          return window.getComputedStyle(e, null)[t]
        };
      return r(t)
    },
    t.requestAnimationFrame = function () {
      window.requestAnimationFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.msRequestAnimationFrame || window.oRequestAnimationFrame || function (e) {
        return window.setTimeout(e, 1e3 / 60)
      }
    },
    t.createElement = function (e) {
      return document.createElement(e)
    },
    t.addClass = function (e, t) {
      return e.classList.add(t)
    },
    t.removeClass = function (e, t) {
      return e.classList.remove(t)
    },
    t.clone = function (e) {
      return e.cloneNode(!0)
    },
    t.addEvent = function (e, t, r) {
      e.addEventListener ? e.addEventListener(t, r, !1) : e.attachEvent("on" + t, r)
    };
  t.deepCloneObjByRecursion = function e(t) {
    if (i(t)) {
      var r = a(t) ? [] : {};
      for (var n in t)
        r[n] = i(t[n]) ? e(t[n]) : t[n];
      return r
    }
  };

  function s(e) {
    if (o(e))
      return e.replace(/A-Z/g, function (e) {
        return "-" + e.toLowerCase()
      })
  }
  t.getDom = function (e) {
      var t, r = e.slice(0, 1),
        n = e.slice(1);
      return /^[#\.]/.test(r) ? "#" === r ? t = document.getElementById(n) : "." === r && (t = document.getElementsByClassName(n)) : t = document.getElementsByTagName(e),
        t
    },
    t.eventType = navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i) ? ["touchstart", "touchmove", "touchend"] : ["mousedown", "mousemove", "mouseup"]
}, function (e, t, r) {
  "use strict";
  var n, o, i, a = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (e) {
      return typeof e
    } :
    function (e) {
      return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
    },
    s = r(0),
    l = u(r(2)),
    c = u(r(3)),
    d = u(r(4));

  function u(e) {
    return e && e.__esModule ? e : {
      default: e
    }
  }
  i = function () {
      function e(e, t) {
        if (!(0,
            s.isStr)(e))
          throw (0,
            s.ewError)("you should pass a string params,sush as drag,textarea,colorpicker！");
        switch (e) {
          case "drag":
            return new l.default(t);
          case "textarea":
            return new c.default(t);
          case "colorpicker":
            return new d.default(t)
        }
        return this
      }
      return window.ewPlugins || (window.ewPlugins = e),
        e
    },
    "object" === a(t) && void 0 !== e ? e.exports = i() : void 0 === (o = "function" == typeof (n = i) ? n.call(t, r, t, e) : n) || (e.exports = o)
}, function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
    value: !0
  });
  var n = r(0),
    o = [];

  function i(e, t, r) {
    var n = null,
      o = parseInt(e.style[t]),
      i = !isNaN(r) && r > 0 && r <= o ? r : 50;
    e.style.transition = t + " .3s ease-out .1s";
    ! function r() {
      o -= i,
        e.style[t] = o + "px",
        n && o <= 0 ? (e.style[t] = "",
          clearTimeout(n)) : n = setTimeout(r, 100)
    }()
  }

  function a(e) {
    var t, r, i, a, s;
    if ((0,
        n.isStr)(e) || (0,
        n.isDom)(e))
      r = (0,
        n.isStr)(e) ? (0,
        n.getDom)(e) : e,
      t = {
        width: window.innerWidth,
        height: window.innerHeight,
        isWindow: !0
      };
    else {
      if (!(0,
          n.isDeepObject)(e) || !e.el)
        return (0,
          n.ewError)("you should pass a string param or an object param!");
      r = (0,
          n.isStr)(e.el) ? (0,
          n.getDom)(e.el) : e.el,
        i = (0,
          n.isStr)(e.scopeEl) ? (0,
          n.getDom)(e.scopeEl) : e.scopeEl,
        a = (0,
          n.isStr)(e.designEl) ? (0,
          n.getDom)(e.designEl) : e.designEl,
        s = (0,
          n.isStr)(e.disabledButton) ? (0,
          n.getDom)(e.disabledButton) : e.disabledButton,
        (t = {
          width: e.width || window.innerWidth,
          height: e.height || window.innerHeight,
          scopeEl: e.scopeEl || null,
          isWindow: !!(0,
            n.isUndefined)(e.isWindow) || e.isWindow,
          origin: e.origin || !1,
          designEl: a || null,
          startCB: (0,
            n.isFunction)(e.startCB) ? e.startCB : null,
          moveCB: (0,
            n.isFunction)(e.moveCB) ? e.moveCB : null,
          endCB: (0,
            n.isFunction)(e.endCB) ? e.endCB : null,
          dragDisabled: e.dragDisabled || !1,
          disabledButton: s || null,
          grid: e.grid || [],
          ani_transition: (0,
            n.isStr)(e.ani_transition) || (0,
            n.isDeepObject)(e.ani_transition) ? e.ani_transition : null
        }).scopeEl && (t.scopeEl = i.length ? i[0] : i,
          t.width = (0,
            n.getCss)(t.scopeEl, "width"),
          t.height = (0,
            n.getCss)(t.scopeEl, "height")),
        e.axis && (t.axis = e.axis)
    }
    return o.push(t),
      this.config = o,
      this.beforeInit(r, t),
      s && this.clickDisable(s, r, t),
      this
  }
  a.prototype.clickDisable = function (e, t, r) {
      if (e && e.length > 0)
        for (var o = 0, i = e.length; o < i; o++)
          a(this, e[o]);
      else
        a(this, e);

      function a(e, o) {
        o && (o.onclick = function () {
          e.config.length > 1 ? e.config.forEach(function (t) {
            (0,
              n.ewObjToArray)(t.disabledButton).indexOf(o) > -1 && (t.dragDisabled = !t.dragDisabled,
              e.beforeInit(t.el, t))
          }) : t.length > 0 ? (e.config[0].dragDisabled = !e.config[0].dragDisabled,
            (0,
              n.ewObjToArray)(t).forEach(function (t) {
              e.beforeInit(t, e.config[0])
            })) : (r.dragDisabled = !r.dragDisabled,
            e.beforeInit(r.el, r))
        })
      }
    },
    a.prototype.beforeInit = function (e, t) {
      if (e && e.length > 0)
        for (var r = 0, o = e.length; r < o; r++)
          this.init((0,
            n.ewAssign)(t, {
            el: e[r]
          })),
          this.resize(this, (0,
            n.ewAssign)(t, {
            el: e[r]
          }));
      else
        t.el = e,
        this.init(t),
        this.resize(this, t)
    },
    a.prototype.resize = function (e, t) {
      window.onresize = function () {
        t.scopeEl || (t.width = window.innerWidth,
            t.height = window.innerHeight),
          e.init(t)
      }
    },
    a.prototype.init = function (e) {
      e.width = parseInt(e.width) <= parseInt((0,
          n.getCss)(e.el, "width")) ? window.innerWidth : parseInt(e.width),
        e.height = parseInt(e.height) <= parseInt((0,
          n.getCss)(e.el, "height")) ? window.innerHeight : parseInt(e.height),
        this.onMouseDown(e)
    },
    a.prototype.onMouseDown = function (e) {
      if ((0,
          n.isDom)(e.designEl))
        if (e.designEl.length > 0) {
          var t = this;
          (0,
            n.ewObjToArray)(e.designEl).forEach(function (r) {
            t.config.length > 1 ? t.config.map(function (e) {
              e.designEl && (0,
                n.ewObjToArray)(e.designEl).indexOf(r) > -1 && i(t, e.el, r, e)
            }) : r.parentElement ? i(t, r.parentElement, r, t.config[0]) : i(t, e.el, r, t.config[0])
          })
        } else
          i(this, e.el, e.designEl, e);
      else
        i(this, e.el, e.el, e);

      function r() {
        this.style.cursor = "move"
      }

      function o() {
        this.style.cursor = ""
      }

      function i(e, t, i, a) {
        a.dragDisabled ? (i.removeEventListener("mouseenter", r),
          i.removeEventListener("mouseleave", o),
          i["on" + n.eventType[0]] = null,
          document["on" + n.eventType[1]] = document["on" + n.eventType[2]] = null) : (i.addEventListener("mouseenter", r),
          i.addEventListener("mouseleave", o),
          i["on" + n.eventType[0]] = function (r) {
            a.startCB && a.startCB(),
              a.ani_transition && (t.style.cssText += (0,
                n.isStr)(a.ani_transition) && a.ani_transition.indexOf("transition") > -1 ? a.ani_transition : (0,
                n.cssObjToStr)(a.ani_transition).indexOf("transition") > -1 ? (0,
                n.cssObjToStr)(a.ani_transition) : "");
            var o = n.eventType[0].indexOf("touch") > -1 ? r.changedTouches[0].clientX - t.offsetLeft : r.clientX - t.offsetLeft,
              s = n.eventType[0].indexOf("touch") > -1 ? r.changedTouches[0].clientY - t.offsetTop : r.clientY - t.offsetTop;
            e.onMouseMove(t, a, o, s, n.eventType),
              e.onMouseUp(t, i, a, n.eventType)
          }
        )
      }
    },
    a.prototype.onMouseMove = function (e, t, r, o, i) {
      var a = (0,
        n.getCss)(e, "position");
      document["on" + i[1]] = function (s) {
          var l, c, d, u, p = i[0].indexOf("touch") > -1 ? s.changedTouches[0].clientX : s.clientX,
            f = i[0].indexOf("touch") > -1 ? s.changedTouches[0].clientY : s.clientY;
          if (e.style.margin = 0,
            (0,
              n.isAbs)(a) || (e.style.position = "absolute"),
            t.moveCB && t.moveCB(),
            (0,
              n.isDeepArray)(t.grid) && t.grid.length && t.grid.length <= 2) {
            var h = p - r,
              g = parseInt(t.grid[0]),
              b = f - o,
              m = parseInt(t.grid[1]);
            isNaN(g) || (l = g * parseInt(h / g)),
              isNaN(m) || (c = m * parseInt(b / m))
          } else
            l = p - r,
            c = f - o;
          if (d = t.width - e.offsetWidth,
            u = t.height - e.offsetHeight,
            t.axis)
            if (t.axis.toLowerCase().indexOf("x") > -1)
              this.moveLeft(e, t, l, d);
            else {
              if (!(t.axis.toLowerCase().indexOf("y") > -1))
                throw (0,
                  n.ewError)("a Invalid value of axis!");
              this.moveTop(e, t, c, u)
            }
          else
            this.moveLeft(e, t, l, d),
            this.moveTop(e, t, c, u)
        }
        .bind(this)
    },
    a.prototype.moveLeft = function (e, t, r, n) {
      var o = r <= 0 && t.isWindow ? 0 : r >= n && t.isWindow ? n : r;
      e.style.left = o + "px"
    },
    a.prototype.moveTop = function (e, t, r, n) {
      var o = r <= 0 && t.isWindow ? 0 : r >= n && t.isWindow ? n : r;
      e.style.top = o + "px"
    },
    a.prototype.restoreX = function (e, t) {
      i(e, "left", t)
    },
    a.prototype.restoreY = function (e, t) {
      i(e, "top", t)
    },
    a.prototype.onMouseUp = function (e, t, r, o) {
      document["on" + o[2]] = function () {
          if (r.endCB && r.endCB(),
            document["on" + o[1]] = document["on" + o[2]] = null,
            t.style.cursor = "",
            r.origin) {
            if (r.axis)
              if (r.axis.toLowerCase().indexOf("x") > -1)
                this.restoreX(e, r.originSpeed);
              else {
                if (!(r.axis.toLowerCase().indexOf("y") > -1))
                  throw (0,
                    n.ewError)("a Invalid value of axis!");
                this.restoreY(e, r.originSpeed)
              }
            else
              this.restoreX(e, r.originSpeed),
              this.restoreY(e, r.originSpeed);
            e.style.cssText = 'margin:0;position:"";'
          }
        }
        .bind(this)
    },
    t.default = a
}, function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
    value: !0
  });
  var n = r(0),
    o = {
      width: "auto",
      height: "auto",
      border: "1px solid rgb(169, 169, 169)",
      "min-height": "30px",
      font: "400 13.3333px Arial;font-family: monospace",
      "min-width": "157px",
      "max-width": "157px",
      padding: "2px",
      display: "block",
      background: "#fff"
    };

  function i(e) {
    if ((0,
        n.isDeepObject)(e))
      if (e.el) {
        var t = (0,
          n.isStr)(e.el) ? (0,
          n.getDom)(e.el) : e.el;
        this.editextArea(t)
      } else {
        if (this.isModeUndefined(e.mode) && !this.isModeAuto(e.mode) && !this.isModeNotAuto(e.mode))
          throw (0,
            n.ewError)("you should pass a string param that called mode and mode is auto or notAuto!");
        var r = (0,
          n.isStr)(e.container) ? (0,
          n.getDom)(e.container) : (0,
          n.isDom)(e.container) ? e.container : null;
        this.addTextArea(r, this.createEleAuto(e.mode))
      }
    else if ((0,
        n.isUndefined)(e) || (0,
        n.isNull)(e))
      this.addTextArea(null, this.createEleAuto("notAuto"));
    else if (this.isModeAuto(e) || this.isModeNotAuto(e))
      this.addTextArea(null, this.createEleAuto(e));
    else {
      var o = (0,
        n.isStr)(e) ? (0,
        n.getDom)(e) : e;
      this.editextArea(o)
    }
    return this
  }
  i.prototype.editextArea = function (e) {
      if (e.length) {
        var t = !0,
          r = !1,
          n = void 0;
        try {
          for (var o, i = e[Symbol.iterator](); !(t = (o = i.next()).done); t = !0) {
            var a = o.value;
            this.setEleAuto(a)
          }
        } catch (e) {
          r = !0,
            n = e
        } finally {
          try {
            !t && i.return && i.return()
          } finally {
            if (r)
              throw n
          }
        }
      } else
        this.setEleAuto(e)
    },
    i.prototype.addTextArea = function (e, t) {
      var r = this;
      if ((0,
          n.isDom)(e))
        if (e.length) {
          var o = !0,
            i = !1,
            a = void 0;
          try {
            for (var s, l = function () {
                var e = s.value,
                  n = t.cloneNode(!0);
                n.tagName.toLowerCase().indexOf("textarea") > -1 && setTimeout(function () {
                    r.autoTextArea(n)
                  }, 0),
                  e.appendChild(n)
              }, c = e[Symbol.iterator](); !(o = (s = c.next()).done); o = !0)
              l()
          } catch (e) {
            i = !0,
              a = e
          } finally {
            try {
              !o && c.return && c.return()
            } finally {
              if (i)
                throw a
            }
          }
        } else
          e.appendChild(t);
      else
        document.body.appendChild(t)
    },
    i.prototype.isModeUndefined = function (e) {
      return !(0,
        n.isStr)(e)
    },
    i.prototype.isModeAuto = function (e) {
      return e.indexOf("auto") > -1
    },
    i.prototype.isModeNotAuto = function (e) {
      return e.indexOf("notAuto") > -1
    },
    i.prototype.setEleAuto = function (e) {
      e.tagName.toLowerCase().indexOf("textarea") > -1 ? this.autoTextArea(e) : (e.setAttribute("contenteditable", !0),
        e.style.cssText += (0,
          n.cssObjToStr)(o))
    },
    i.prototype.createEleAuto = function (e) {
      var t = null,
        r = function (e) {
          this.autoTextArea(e)
        }
        .bind(this);
      return this.isModeAuto(e) ? (t = document.createElement("textarea"),
          setTimeout(r(t), 0)) : ((t = document.createElement("div")).style.cssText += (0,
            n.cssObjToStr)(o),
          t.classList.add("ew-textarea"),
          t.setAttribute("contenteditable", !0)),
        t
    },
    i.prototype.autoTextArea = function (e, t, r) {
      t = t || 0;
      var o = document.getBoxObjectFor || "mozInnerScreenX" in window,
        i = window.opera && window.opera.toString().indexOf("opera");
      e.style.cssText += "resize:none;";
      var a = parseInt((0,
          n.getCss)(e, "height")),
        s = function () {
          var s, l = 0,
            c = e.style;
          o || i || (l = parseInt((0,
              n.getCss)(e, "padding-bottom")) + parseInt((0,
              n.getCss)(e, "padding-top"))),
            c.height = a + "px",
            e.scrollHeight > a && (r && e.scrollHeight > r ? (s = r - l,
                c.overflowY = "auto") : (s = e.scrollHeight - l,
                c.overflowY = "hidden"),
              c.height = s + t + "px")
        };
      (0,
        n.addEvent)(e, "propertychange", s),
      (0,
        n.addEvent)(e, "focus", s),
      (0,
        n.addEvent)(e, "input", s),
      s()
    },
    t.default = i
}, function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
    value: !0
  });
  var n = r(0),
    o = r(5),
    i = s(r(6)),
    a = s(r(8));

  function s(e) {
    return e && e.__esModule ? e : {
      default: e
    }
  }

  function l(e, t, r) {
    return r ? document.querySelectorAll ? e.querySelectorAll("." + t) : e.getElementsByClassName(t) : document.querySelector ? e.querySelector("." + t) : e.getElementsByClassName(t)[0]
  }

  function c(e, t, r) {
    e.style[t] = r
  }

  function d(e) {
    e.config.openPickerAni.indexOf("height") > -1 ? e.pickerFlag ? a.default.slideDown(e.picker, 400) : a.default.slideUp(e.picker, 400) : e.pickerFlag ? a.default.fadeIn(e.picker, 400) : a.default.fadeOut(e.picker, 400)
  }

  function u(e, t, r, n, o) {
    c(e.pickerCursor, "left", t + "px"),
      c(e.pickerCursor, "top", r + "px");
    var i = parseInt(100 * t / n),
      a = parseInt(100 * (o - r) / o);
    e.hsba.s = i > 100 ? 100 : i < 0 ? 0 : i,
      e.hsba.b = a > 100 ? 100 : a < 0 ? 0 : a,
      p(e)
  }

  function p(e, t) {
    c(e.box, "background", (0,
        o.colorHsbaToRgba)(e.hsba)),
      c(e.arrowRight, "border-top-color", (0,
        o.colorHsbaToRgba)(e.hsba)),
      t || e.config.alpha ? e.pickerInput.value = (0,
        o.colorHsbaToRgba)(e.hsba) : e.pickerInput.value = (0,
        o.colorRgbaToHex)((0,
        o.colorHsbaToRgba)(e.hsba))
  }

  function f(e) {
    if (this.pickerFlag = !1,
      (0,
        n.isStr)(e) || (0,
        n.isDom)(e)) {
      var t = (0,
        n.isDom)(e) ? e : (0,
        n.getDom)(e);
      if (this.config = {
          hue: !0,
          alpha: !1,
          size: "normal",
          predefineColor: [],
          disabled: !1,
          defaultColor: "",
          openPickerAni: "height",
          sure: function () {},
          clear: function () {}
        },
        t.length)
        for (var r = -1; ++r < t.length;)
          this.init(t[r], this.config);
      else
        this.init(t, this.config)
    } else {
      if (!(0,
          n.isDeepObject)(e) || !(0,
          n.isStr)(e.el) && !(0,
          n.isDom)(e.el))
        return (0,
          n.isDeepObject)(e) ? (0,
          n.ewError)("you should pass a param which is el and el must be a string or a dom element!") : (0,
          n.ewError)("you should pass a param that it must be a string or a dom element!");
      var o = (0,
        n.isDom)(e.el) ? e.el : (0,
        n.getDom)(e.el);
      if (this.config = {
          hue: e.hue || !0,
          alpha: e.alpha || !1,
          size: e.size || "normal",
          predefineColor: e.predefineColor || [],
          disabled: e.disabled || !1,
          defaultColor: e.defaultColor || "",
          openPickerAni: e.openPickerAni || "height",
          sure: (0,
            n.isFunction)(e.sure) ? e.sure : null,
          clear: (0,
            n.isFunction)(e.clear) ? e.clear : null
        },
        o.length)
        for (var i = 0; i < o.length;)
          this.init(o[i], this.config),
          i++;
      else
        this.init(o, this.config)
    }
    return e
  }

  function h(e) {
    var t = (0,
      n.deepCloneObjByJSON)(e.hsba);
    t.s = t.b = 100,
      e.alphaBarBg && c(e.alphaBarBg, "background", "linear-gradient(to top," + (0,
        o.colorHsbaToRgba)(t, 0) + " 0%," + (0,
        o.colorHsbaToRgba)(t) + " 100%)")
  }

  function g(e, t, r) {
    e.pickerInput.value = e.config.alpha ? (0,
        o.colorHsbaToRgba)(e.hsba) : (0,
        o.colorRgbaToHex)((0,
        o.colorHsbaToRgba)(e.hsba)),
      e.arrowRight && c(e.arrowRight, "border-top-color", (0,
        o.colorHsbaToRgba)(e.hsba));
    var i = e.hueBar.offsetHeight || 180,
      a = parseInt(e.hsba.s * t / 100),
      s = parseInt(r - e.hsba.b * r / 100),
      l = parseInt(e.hsba.h * i / 360);
    c(e.pickerCursor, "left", a + 4 + "px"),
      c(e.pickerCursor, "top", s + 4 + "px"),
      c(e.hueThumb, "top", l + "px");
    var d = (0,
      n.deepCloneObjByJSON)(e.hsba);
    if (d.s = d.b = 100,
      c(e.pickerPanel, "background", (0,
        o.colorRgbaToHex)((0,
        o.colorHsbaToRgba)(d))),
      e.config.alpha) {
      var u = i - e.hsba.a * i;
      c(e.alphaBarThumb, "top", u + "px")
    }
  }

  function b(e, t) {
    var r = e.hueBar.offsetHeight,
      i = e.hueBar.getBoundingClientRect(),
      a = Math.max(0, Math.min(t - i.y, r));
    c(e.hueThumb, "top", a + "px");
    var s = (0,
      n.deepCloneObjByJSON)(e.hsba);
    s.s = 100,
      s.b = 100,
      e.hsba.h = s.h = parseInt(360 * a / r),
      c(e.pickerPanel, "background", (0,
        o.colorRgbaToHex)((0,
        o.colorHsbaToRgba)(s))),
      p(e),
      h(e)
  }

  function m(e, t) {
    var r = e.hueBar.offsetHeight,
      n = e.hueBar.getBoundingClientRect(),
      o = Math.max(0, Math.min(t - n.y, r));
    c(e.alphaBarThumb, "top", o + "px");
    var i = (r - o <= 0 ? 0 : r - o) / r;
    e.hsba.a = i >= 1 ? 1 : i.toFixed(2),
      p(e, !0)
  }
  f.prototype.init = function (e, t) {
      this.render(e, t),
        document.getElementsByTagName("head")[0].appendChild(i.default)
    },
    f.prototype.render = function (e, t) {
      var r = void 0,
        o = void 0,
        i = "";
      if ((0,
          n.isStr)(t.size))
        switch (t.size) {
          case "normal":
            r = o = "40px";
            break;
          case "medium":
            r = o = "36px";
            break;
          case "small":
            r = o = "20px";
            break;
          case "mini":
            r = o = "20px"
        }
      else {
        if (!(0,
            n.isDeepObject)(t.size))
          return (0,
            n.ewError)("the value must be a string which is one of the normal,medium,small,mini,or must be an object and need to contain width or height property!");
        r = t.size.width && (0,
            n.isNumber)(t.size.width) ? t.size.width + "px" : (0,
            n.isStr)(t.size.width) ? parseInt(t.size.width) + "px" : "40px",
          o = t.size.height && (0,
            n.isNumber)(t.size.height) ? t.size.height + "px" : (0,
            n.isStr)(t.size.height) ? parseInt(t.size.height) : "40px"
      }
      (0,
        n.isDeepArray)(t.predefineColor) && t.predefineColor.length && t.predefineColor.map(function (e) {
        i += '<div class="ew-pre-define-color" style="background:' + e + ';" tabIndex=0></div>'
      });
      var a = t.defaultColor ? '<div class="ew-color-picker-arrow">\n        <div class="ew-color-picker-arrow-left"></div>\n        <div class="ew-color-picker-arrow-right"></div>\n    </div>' : '<div class="ew-color-picker-no"></div>',
        s = t.alpha ? '<div class="ew-alpha-slider-bar">\n    <div class="ew-alpha-slider-wrapper"></div>\n    <div class="ew-alpha-slider-bg"></div>\n    <div class="ew-alpha-slider-thumb"></div>\n    </div>' : "",
        l = i ? '<div class="ew-pre-define-color-container">' + i + "</div>" : "",
        c = '<div class="ew-color-picker-box ' + (t.disabled ? "ew-color-picker-box-disabled" : "") + '" tabindex="0" style="background:' + t.defaultColor + ";width:" + '38px' + ";height:" + '14px' + '">\n                ' + a + '\n            </div>\n            <div class="ew-color-picker">\n                <div class="ew-color-picker-content">\n                    <div class="ew-color-slider">\n                        ' + s + '\n                        <div class="ew-color-slider-bar">\n                            <div class="ew-color-slider-thumb"></div>\n                        </div>\n                    </div>\n                    <div class="ew-color-panel" style="background:red;">\n                        <div class="ew-color-white-panel"></div>\n                        <div class="ew-color-black-panel"></div>\n                        <div class="ew-color-cursor"></div>\n                    </div>\n                </div>\n                <div class="ew-color-dropbtns">\n                    <input type="text" class="ew-color-input">\n                    <div class="ew-color-dropbtngroup">\n                        <button  style="border:0px;color:#4096ef;"     class="ew-color-clear ew-color-dropbtn">清空</button>\n                        <button class="ew-color-sure ew-color-dropbtn">确定</button>\n                    </div>\n                </div>\n                ' + l + "\n            </div>";
      e.innerHTML = c,
        this.startMain(e, t)
    },
    f.prototype.startMain = function (e, t) {
      var r = this;
      this.box = l(e, "ew-color-picker-box"),
        this.arrowRight = l(e, "ew-color-picker-arrow-right"),
        this.pickerPanel = l(e, "ew-color-panel"),
        this.pickerCursor = l(e, "ew-color-cursor"),
        this.pickerInput = l(e, "ew-color-input"),
        this.pickerClear = l(e, "ew-color-clear"),
        this.pickerSure = l(e, "ew-color-sure"),
        this.hueBar = l(e, "ew-color-slider-bar"),
        this.hueThumb = l(e, "ew-color-slider-thumb"),
        this.picker = l(e, "ew-color-picker"),
        this.slider = l(e, "ew-color-slider"),
        this.hsba = this.config.defaultColor ? (0,
          o.colorRgbaToHsba)((0,
          o.colorToRgb)(this.config.defaultColor)) : {
          h: 0,
          s: 100,
          b: 100,
          a: 1
        };
      for (var i = this.panelWidth = parseInt((0,
          n.getCss)(this.pickerPanel, "width")), a = this.panelHeight = parseInt((0,
          n.getCss)(this.pickerPanel, "height")), s = e, c = s.offsetTop, f = s.offsetLeft; s.offsetParent;)
        c += s.offsetParent.offsetTop,
        f += s.offsetParent.offsetLeft,
        s = s.offsetParent;
      this.pancelLeft = f,
        this.pancelTop = c + e.offsetHeight,
        this.preDefineItem = l(e, "ew-pre-define-color", !0),
        this.preDefineItem.length && (0,
          n.ewObjToArray)(this.preDefineItem).map(function (e) {
          e.addEventListener("click", function (e) {
              (0,
                n.ewObjToArray)(this.parentElement.children).forEach(function (e) {
                  (0,
                    n.removeClass)(e, "ew-pre-define-color-active")
                }),
                (0,
                  n.addClass)(e.target, "ew-pre-define-color-active");
              var t = (0,
                o.colorRgbaToHsba)((0,
                n.getCss)(e.target, "background-color"));
              r.hsba = t,
                p(r),
                h(r),
                g(r, i, a)
            }, !1),
            e.addEventListener("blur", function (e) {
              (0,
                n.removeClass)(e.target, "ew-pre-define-color-active")
            }, !1)
        }),
        t.openPickerAni.indexOf("height") > -1 ? this.picker.style.display = "none" : this.picker.style.opacity = 0,
        t.alpha ? (this.alphaBar = l(e, "ew-alpha-slider-bar"),
          this.alphaBarBg = l(e, "ew-alpha-slider-bg"),
          this.alphaBarThumb = l(e, "ew-alpha-slider-thumb"),
          h(this),
          this.bindEvent(this.alphaBarThumb, function (e, t, r, n) {
            m(e, n)
          }, !1),
          this.alphaBar.addEventListener("click", function (e) {
            m(r, e.y)
          }, !1)) : (this.slider.style.width = "14px",
            this.picker.style.minWidth = "300px"),
      this.pickerInput.style.height = '33px'
    this.pickerInput.style.lineHeight = '33px'
    this.pickerInput.style.width = '120px'
        this.pickerInput.addEventListener("blur", function (e) {
          ! function (e, t) {
            var r = (0,
              o.colorRgbaToHsba)((0,
              o.colorToRgb)(t));
            (r.h || r.s || r.h || r.a) && (e.hsba = r,
              g(e, e.panelWidth, e.panelHeight),
              p(e))
          }(r, e.target.value)
        }, !1),
        this.pickerClear.addEventListener("click", function () {
          ! function (e, t) {
            t.config.defaultColor = "",
              t.pickerFlag = !t.pickerFlag,
              t.render(e, t.config),
              d(t),
              t.config.clear(t)
          }(e, r)
        }, !1),
        this.pickerSure.addEventListener("click", function () {
          ! function (e) {
            e.pickerFlag = !1,
              d(e),
              p(e);
            var t = e.config.alpha ? (0,
              o.colorHsbaToRgba)(e.hsba) : (0,
              o.colorRgbaToHex)((0,
              o.colorHsbaToRgba)(e.hsba));
            e.config.sure(t, e)
          }(r)
        }),
        t.disabled || this.box.addEventListener("click", function () {
          ! function (e, t) {
            t.pickerFlag = !t.pickerFlag,
              t.config.defaultColor = t.config.alpha ? (0,
                o.colorHsbaToRgba)(t.hsba) : (0,
                o.colorRgbaToHex)((0,
                o.colorHsbaToRgba)(t.hsba)),
              t.pickerFlag && t.render(e, t.config),
              d(t),
              g(t, t.panelWidth, t.panelHeight)
          }(e, r)
        }, !1),
        this.pickerPanel.addEventListener("click", function (e) {
          ! function (e, t) {
            if (t.target !== e.pickerCursor) {
              var r = t.layerX,
                n = t.layerY,
                o = e.pickerPanel.offsetWidth,
                i = e.pickerPanel.offsetHeight;
              u(e, r >= o - 1 ? o : r <= 0 ? 0 : r, n >= i - 2 ? i : n <= 0 ? 0 : n, o, i)
            }
          }(r, e)
        }, !1),
        this.bindEvent(this.pickerCursor, function (e, t, r, n) {
          u(e, Math.max(0, Math.min(r - e.pancelLeft, i)) + 4, Math.max(0, Math.min(n - e.pancelTop, a)) + 4, i, a)
        }, !1),
        this.hueBar.addEventListener("click", function (e) {
          b(r, e.y)
        }, !1),
        this.bindEvent(this.hueThumb, function (e, t, r, n) {
          b(e, n)
        }, !1)
    },
    f.prototype.bindEvent = function (e, t, r) {
      var o = this;
      e.addEventListener(n.eventType[0], function (i) {
        var a = function (i) {
          var a;
          i.preventDefault(),
            a = i,
            o.moveX = n.eventType[0].indexOf("touch") > -1 ? a.changedTouches[0].clientX : a.clientX,
            o.moveY = n.eventType[0].indexOf("touch") > -1 ? a.changedTouches[0].clientY : a.clientY,
            r ? t(o, o.moveX, o.moveY) : t(o, e, o.moveX, o.moveY)
        };
        document.addEventListener(n.eventType[1], a, {
            capture: !1,
            once: !1,
            passive: !1,
            useCapture: !1,
            wantsUntrusted: !1
          }),
          document.addEventListener(n.eventType[2], function e() {
            document.removeEventListener(n.eventType[1], a, {
                capture: !1,
                once: !1,
                passive: !1,
                useCapture: !1,
                wantsUntrusted: !1
              }),
              document.removeEventListener(n.eventType[2], e, {
                capture: !1,
                once: !1,
                passive: !1,
                useCapture: !1,
                wantsUntrusted: !1
              })
          }, {
            capture: !1,
            once: !1,
            passive: !1,
            useCapture: !1,
            wantsUntrusted: !1
          })
      }, {
        capture: !1,
        once: !1,
        passive: !1,
        useCapture: !1,
        wantsUntrusted: !1
      })
    },
    t.default = f
}, function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
      value: !0
    }),
    t.colorRgbaToHsba = function (e) {
      var t = e.slice(e.indexOf("(") + 1, e.lastIndexOf(")")).split(","),
        r = t.length < 4 ? 1 : Number(t[3]),
        n = Number(t[0]) / 255,
        o = Number(t[1]) / 255,
        i = Number(t[2]) / 255,
        a = void 0,
        s = void 0,
        l = void 0,
        c = Math.min(n, o, i),
        d = l = Math.max(n, o, i),
        u = d - c;
      if (d === c)
        a = 0;
      else {
        switch (d) {
          case n:
            a = (o - i) / u + (o < i ? 6 : 0);
            break;
          case o:
            a = 2 + (i - n) / u;
            break;
          case i:
            a = 4 + (n - o) / u
        }
        a = Math.round(60 * a)
      }
      s = 0 === d ? 0 : 1 - c / d;
      return s = Math.round(100 * s),
        l = Math.round(100 * l), {
          h: a,
          s: s,
          b: l,
          a: r
        }
    },
    t.colorToRgb = function (e) {
      var t = document.createElement("div");
      t.style.backgroundColor = e,
        document.body.appendChild(t);
      var r = window.getComputedStyle(t).backgroundColor;
      return document.body.removeChild(t),
        r
    };
  t.colorHexToRgba = function (e, t) {
      var r = t || 1,
        n = e.toLowerCase(),
        o = e.length,
        i = [];
      if (e && /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/.test(n)) {
        if (4 === o) {
          for (var a = "#", s = 0; s < o; s++) {
            var l = n.slice(s, s + 1);
            a += l.cancat(l)
          }
          n = a
        }
        for (var c = 0, d = n.length; c < d; c++)
          i.push(parseInt("0X" + n.slice(c, c + 2)));
        return "rgba(" + i.join(",") + "," + r + ")"
      }
      return n
    },
    t.colorRgbaToHex = function (e) {
      var t = {
        10: "A",
        11: "B",
        12: "C",
        13: "D",
        14: "E",
        15: "F"
      };
      if (/rgba?/.test(e)) {
        var r = e.replace(/rgba?\(/, "").replace(/\)/, "").replace(/[\s+]/g, "").split(","),
          n = "";
        r[3];
        return r.map(function (e, r) {
            r <= 2 && (n += function (e) {
              e = Math.min(Math.round(e), 255);
              var r = Math.floor(e / 16),
                n = e % 16;
              return "" + (t[r] || r) + (t[n] || n)
            }(e))
          }),
          "#" + n
      }
    },
    t.colorHsbaToRgba = function (e, t) {
      var r, n, o, i = e.a,
        a = Math.round(e.h),
        s = Math.round(255 * e.s / 100),
        l = Math.round(255 * e.b / 100);
      if (0 === s)
        r = n = o = l;
      else {
        var c = (255 - s) * l / 255,
          d = a % 60 * (l - c) / 60;
        360 === a ? (r = l,
          n = o = 0) : a < 60 ? (r = l,
          n = c + d,
          o = c) : a < 120 ? (r = l - d,
          n = l,
          o = c) : a < 180 ? (r = c,
          n = l,
          o = c + d) : a < 240 ? (r = c,
          n = l - d,
          o = l) : a < 300 ? (r = c + d,
          n = c,
          o = l) : a < 360 ? (r = l,
          n = c,
          o = l - d) : r = n = o = 0
      }
      return (t >= 0 || t <= 1) && (i = t),
        "rgba(" + Math.round(r) + "," + Math.round(n) + "," + Math.round(o) + "," + i + ")"
    }
}, function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
    value: !0
  });
  var n, o = r(7),
    i = (n = o) && n.__esModule ? n : {
      default: n
    };
  var a = (0,
    r(0).createElement)("style");
  a.textContent = i.default,
    t.default = a
}, function (e, t, r) {
  "use strict";
  r.r(t),
    t.default = '.ew-color-picker { \r\n background:#fff;\r\n    min-width: 220px;\r\n    position: absolute;\r\n  right:0;\r\n   box-sizing: content-box;\r\n    border: 1px solid #ebeeff;\r\n border-top:0px;\r\n   box-shadow: 0 4px 5px rgba(0, 0, 0, .2);\r\n    border-radius: 5px;\r\n    z-index: 10;\r\n    padding: 7px;\r\n       /* left: 10px;\r\n    top: 10px; */\r\n    display: none;\r\n}\r\n\r\n.ew-color-picker .ew-color-picker-content:after {\r\n    content: "";\r\n    display: table;\r\n    clear: both;\r\n}\r\n\r\n.ew-color-picker-content {\r\n    margin-bottom: 6px;\r\n}\r\n\r\n.ew-color-panel {\r\n    position: relative;\r\n    width: 200px;\r\n    height: 180px;\r\n    cursor: pointer;\r\n}\r\n\r\n.ew-color-white-panel,\r\n.ew-color-black-panel {\r\n    position: absolute;\r\n    left: 0;\r\n    right: 0;\r\n    top: 0;\r\n    bottom: 0;\r\n}\r\n\r\n.ew-color-white-panel {\r\n    background: linear-gradient(90deg, #fff, hsla(0, 0%, 100%, 0));\r\n}\r\n\r\n.ew-color-black-panel {\r\n    background: linear-gradient(0deg, #000, transparent);\r\n}\r\n\r\n.ew-color-slider {\r\n    width: 27px;\r\n right: -8px;\r\n     height: 180px;\r\n    position: relative;\r\n    float: right;\r\n    box-sizing: border-box;\r\n}\r\n.ew-color-slider-bar {\r\n    background: linear-gradient(180deg, #f00 0, #ff0 17%, #0f0 33%, #0ff 50%, #00f 67%, #f0f 83%, #f00);\r\n    margin-left: 3px;\r\n}\r\n.ew-alpha-slider-bar,.ew-color-slider-bar{\r\n    width: 12px;\r\n    height: 100%;\r\n    position: relative;\r\n    float: left;\r\n    cursor: pointer;\r\n}\r\n.ew-alpha-slider-wrapper{\r\n    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAIAAADZF8uwAAAAGUlEQVQYV2M4gwH+YwCGIasIUwhT25BVBADtzYNYrHvv4gAAAABJRU5ErkJggg==");\r\n}\r\n.ew-alpha-slider-bg,.ew-alpha-slider-wrapper{\r\n    position: absolute;\r\n    right: 0;\r\n    top: 0;\r\n    left: 0;\r\n    bottom: 0;\r\n}\r\n/* .ew-alpha-slider-bg{\r\n    background:linear-gradient(to top,rgba(255,0,0,0) 0%,rgba(255,0,0,1) 100%);\r\n} */\r\n.ew-color-slider-thumb,.ew-alpha-slider-thumb {\r\n    position: absolute;\r\n    cursor: pointer;\r\n    box-sizing: border-box;\r\n    left: 0;\r\n    top: 0;\r\n    width: 12px;\r\n    height: 4px;\r\n    border-radius: 1px;\r\n    background: #fff;\r\n    border: 1px solid #f0f0f0;\r\n    box-shadow: 0 0 2px rgba(0, 0, 0, .6);\r\n}\r\n\r\n.ew-color-cursor{\r\n    position: absolute;\r\n    left: 100%;\r\n    top: 0%;\r\n    cursor: default;\r\n    width: 4px;\r\n    height: 4px;\r\n    transform: translate(-2px, -2px);\r\n    border-radius: 50%;\r\n    box-shadow: 0 0 0 3px #fff,\r\n        inset 0 0 2px 2px rgba(0, 0, 0, .4),\r\n        0 0 2px 3px rgba(0, 0, 0, .5);\r\n    transform: translate(-6px,-6px)\r\n}\r\n\r\n.ew-color-dropbtns {\r\n  height: 32px;\r\n   margin-top: 6px;\r\n    position: relative;\r\n}\r\n\r\n.ew-color-input {\r\n    width: 160px;\r\n    height: 28px;\r\n    line-height: 28px;\r\n    border: 1px solid #dcdfe6;\r\n    background-color: #ffffff;\r\n    display: inline-block;\r\n    box-sizing: border-box;\r\n    padding: 0 5px;\r\n    transition: border-color .2s cubic-bezier(0.175, 0.885, 0.32, 1.275);\r\n    border-radius: 5px;\r\n    outline: none;\r\n}\r\n\r\n.ew-color-input:focus {\r\n    border-color: #239fe6;\r\n}\r\n\r\n.ew-color-dropbtn {\r\n    display: inline-block;\r\n    padding: 5px 15px;\r\n    font-size: 12px;\r\n    border-radius: 3px;\r\n    cursor: pointer;\r\n    text-align: center;\r\n    transition: .1s;\r\n    font-weight: 500;\r\n    outline: none;\r\n    box-sizing: border-box;\r\n    margin: 0;\r\n    white-space: nowrap;\r\n    color: #606266;\r\n    border: 1px solid #dcdfe6;\r\n}\r\n\r\n.ew-color-dropbtngroup {\r\n    position: absolute;\r\n    right: 0;\r\n    top: 1px;\r\n}\r\n\r\n.ew-color-clear {\r\n    color: #4096ef;\r\n    border-color: transparent;\r\n    background-color: transparent;\r\n    padding-left: 0;\r\n    padding-right: 0;\r\n}\r\n\r\n.ew-color-clear:hover,\r\n.ew-color-clear:active {\r\n    color: #66b1ff;\r\n}\r\n\r\n.ew-color-sure {\r\n    background-color: #ffffff;\r\n    margin-left: 10px;\r\n}\r\n\r\n.ew-color-sure:hover,\r\n.ew-color-sure:active {\r\n    border-color: #4096ef;\r\n    color: #4096ef;\r\n}\r\n.ew-pre-define-color-container{\r\n    width: 200px;\r\n    font-size: 12px;\r\n    margin-top: 8px;\r\n}\r\n.ew-pre-define-color-container:after{\r\n    content: "";\r\n    visibility: hidden;\r\n    clear: both;\r\n    display: block;\r\n    height: 0;\r\n}\r\n.ew-pre-define-color{\r\n    float: left;\r\n    margin: 0 0 8px 8px;\r\n    width: 20px;\r\n    height: 20px;\r\n    border-radius: 4px;\r\n    cursor: pointer;\r\n    outline: none;\r\n}\r\n.ew-pre-define-color:nth-child(10n+1){\r\n    margin-left: 0;\r\n}\r\n.ew-pre-define-color:hover,\r\n.ew-pre-define-color:active{\r\n    opacity: .8;\r\n}\r\n.ew-pre-define-color-active{\r\n    box-shadow: 0 0 3px 2px #409eff;\r\n}\r\n.ew-color-picker-box{\r\n  position: relative;\r\n  top: 4px;\r\n  left: 4px;\r\n   border: 1px solid black;\r\n    color: #535353;\r\n    outline: none;\r\n    display: inline-block;\r\n    background-color: #ffffff;\r\n    position: relative;\r\n        line-height: 1.5;\r\n    cursor: pointer;\r\n    font-size: 14px;\r\n    transition: border-color  .2s cubic-bezier(0.175, 0.885, 0.32, 1.275);\r\n}\r\n.ew-color-picker-box-disabled{\r\n    background-color: #999999;\r\n    cursor: not-allowed;\r\n}\r\n.ew-color-picker-arrow,.ew-color-picker-no{\r\n border-radius: 4px;\r\n  border: 1px solid #dcdfe6;\r\n    width: 46px;\r\n    height: 22px;\r\n    position: absolute;\r\n    left: -5px;\r\n    top: 0;\r\n    bottom: 0;\r\n    right: 0;\r\n    margin: auto;\r\n    z-index: 3;\r\n}\r\n.ew-color-picker-no{\r\n border-radius: 4px;\r\n   border: 1px solid #dcdfe6;\r\n width: 46px;\r\n    height:22px;\r\n    font-size: 12px;\r\n    text-align: center;\r\n    line-height: 14px;\r\n    color: #5e535f;\r\n    border: 1px solid #dcdfe6;\r\n  }\r\n.ew-color-picker-arrow-left,.ew-color-picker-arrow-right{\r\n    width: 0;\r\n    height: 0;\r\n    position: absolute;\r\n    left: 0;\r\n    top: 0;\r\n    z-index: 5;\r\n    overflow: hidden;\r\n   \r\n}\r\n.ew-color-picker-arrow-left{\r\n    border-top: 10px solid #fff;\r\n}\r\n.ew-color-picker-arrow-right{\r\n    border-top: 10px solid #ff0000;\r\n}'
}, function (e, t, r) {
  "use strict";
  Object.defineProperty(t, "__esModule", {
    value: !0
  });
  var n = r(0),
    o = function () {
      var e = {};

      function t() {
        this.timers = [],
          this.args = [],
          this.isTimerRun = !1
      }

      function r(e, r) {
        if (e.offsetHeight > 0) {
          var n = e.offsetHeight,
            o = n,
            i = n / (r / 10);
          e.style.transition = "height " + r + " ms",
            e.style.overflow = "hidden";
          var a = setInterval(function () {
            o -= i,
              e.style.height = o + "px",
              o <= 0 && (clearInterval(a),
                e.style.display = "none",
                e.style.height = n + "px",
                e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next())
          }, 10)
        } else
          e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next()
      }

      function o(e, r) {
        if (e.offsetHeight <= 0) {
          e.style.display = "block",
            e.style.transition = "height" + r + " ms",
            e.style.overflow = "hidden";
          var n = e.offsetHeight,
            o = 0;
          e.style.height = "0px";
          var i = n / (r / 10),
            a = setInterval(function () {
              o += i,
                e.style.height = o + "px",
                o >= n && (clearInterval(a),
                  e.style.height = n + "px",
                  e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next())
            }, 10)
        } else
          e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next()
      }

      function i(e, r) {
        if (e.style.transition = "opacity" + r + " ms",
          !(0,
            n.getCss)(e, "opactiy") || !parseInt((0,
            n.getCss)(e, "opactiy")) <= 0) {
          e.style.display = "none";
          var o = 0;
          e.style.opacity = 0;
          var i = 100 / (r / 10),
            a = setInterval(function () {
              o += i,
                e.style.display = "block",
                e.style.opacity = (o / 100).toFixed(2),
                o >= 100 && (clearInterval(a),
                  e.style.opacity = 1,
                  e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next())
            }, 10)
        } else
          e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next()
      }

      function a(e, r) {
        if (e.style.transition = "opacity" + r + " ms",
          !(0,
            n.getCss)(e, "opactiy") || !parseInt((0,
            n.getCss)(e, "opactiy")) >= 1) {
          var o = 100;
          e.style.opacity = 1,
            e.style.display = "block";
          var i = 100 / (r / 10),
            a = setInterval(function () {
              o -= i,
                e.style.opacity = (o / 100).toFixed(2),
                o <= 0 && (clearInterval(a),
                  e.style.opacity = 0,
                  e.style.display = "none",
                  e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next())
            }, 10)
        } else
          e.TimerManage && e.TimerManage.constructor === t && e.TimerManage.next()
      }
      return t.makeTimerManage = function (e) {
          e.TimerManage && e.TimerManage.constructor === t || (e.TimerManage = new t)
        },
        t.prototype.add = function (e, t) {
          this.timers.push(e),
            this.args.push(t),
            this.timerRun()
        },
        t.prototype.timerRun = function () {
          if (!this.isTimerRun) {
            var e = this.timers.shift(),
              t = this.args.shift();
            e && t && (this.isTimerRun = !0,
              e(t[0], t[1]))
          }
        },
        t.prototype.next = function () {
          this.isTimerRun = !1,
            this.timerRun()
        },
        e.slideUp = function (e) {
          return t.makeTimerManage(e),
            e.TimerManage.add(r, arguments),
            this
        },
        e.slideDown = function (e) {
          return t.makeTimerManage(e),
            e.TimerManage.add(o, arguments),
            this
        },
        e.fadeIn = function (e) {
          return t.makeTimerManage(e),
            e.TimerManage.add(i, arguments),
            this
        },
        e.fadeOut = function (e) {
          return t.makeTimerManage(e),
            e.TimerManage.add(a, arguments),
            this
        },
        e
    }();
  t.default = o
}]);
