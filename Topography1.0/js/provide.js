function _ClearAddRect () {
  var _addRect = document.getElementsByClassName('_addRect')
  if (_addRect.length > 0) {
    for (let i = 0; i < _addRect.length; i++) {
      var element = _addRect[i];
      element.remove()
    }
  }
  _addRect = document.getElementsByClassName('_addRect')
  if (_addRect.length > 0) {
    _ClearAddRect()
  } else {
    return
  }
}

function flow(graph, key) {
  // console.log(key);
  // 维护交点字段
  // window.intersectionKey = []
  _ClearAddRect()
  var g = document.getElementsByClassName('geDiagramContainer')[0].getElementsByTagName('svg')
  var allPoints = []
  var nodes = graph.getModel().cells
  Object.keys(nodes).forEach(function (key) {
    if (nodes[key].edge) {
      let state = graph.view.getState(nodes[key]);
      if (state && state.style.flow) {
          let style = nodes[key].style.split(';')
          style.push(`class=flow`)
          graph.getModel().beginUpdate();
          try {
              // graph.setCellStyles(mxConstants.STYLE_STROKECOLOR, color, nodes[item.id]);
              graph.getModel().setStyle(nodes[key], style.join(';'))
          } finally {
              graph.getModel().endUpdate();
          }
        // state.shape.node.getElementsByTagName('path')[1].setAttribute('class', 'flow')
      }
      // 屏蔽不规则连线
      if (state.absolutePoints && !state.style.curved && state.style.edgeStyle && state.style.edgeStyle !== "entityRelationEdgeStyle" && state.style.edgeStyle !== "isometricEdgeStyle") {
        _addRect = document.getElementsByClassName('_addRect')
        if (_addRect.length > 0) {
          for (let i = 0; i < _addRect.length; i++) {
            var element = _addRect[i];
            element.remove()
          }
        }
        state.absolutePoints.strokewidth = state.shape.strokewidth
        state.absolutePoints.strokeColor = state.style.strokeColor
        state.absolutePoints.class = state.style.flow
        state.absolutePoints.id = state.cell.id
        allPoints.push(state.absolutePoints)
      }
    }
  })

  if (allPoints.length > 1) {
    const newArr = []
    allPoints.forEach((item, index) => {
      if (!newArr[index]) {
        newArr[index] = []
      }
      for (var i = 0; i < item.length; i++) {
        if (i < item.length - 1) {
          newArr[index].push([item[i], item[i + 1], item.strokeColor, item.class, item.strokewidth, item.id])
        }
      }
    })
    for (var i = 0; i < newArr.length; i++) {
      for (var j = i + 1; j < newArr.length; j++) {
        contrast(g, newArr[i], newArr[j])
      }
    }
  }
}

function contrast(g, arr1, arr2) {
  // debugger
  for (var i = 0; i < arr1.length; i++) {
    for (var j = 0; j < arr2.length; j++) {
      if ((Math.min(arr1[i][0].x, arr1[i][1].x) > Math.max(arr2[j][0].x, arr2[j][1].x)) || (Math.max(arr1[i][0].x, arr1[i][1].x) < Math.min(arr2[j][0].x, arr2[j][1].x)) || (Math.min(arr1[i][0].y, arr1[i][1].y) > Math.max(arr2[j][0].y, arr2[j][1].y)) || (Math.max(arr1[i][0].y, arr1[i][1].y) < Math.min(arr2[j][0].y, arr2[j][1].y))) {
        continue
      }
      if (arr1[i][0].x === arr2[j][0].x || arr1[i][0].x === arr2[j][1].x || arr1[i][1].x === arr2[j][0].x || arr1[i][1].x === arr2[j][1].x || arr1[i][0].y === arr2[j][0].y || arr1[i][0].y === arr2[j][1].y || arr1[i][1].y === arr2[j][0].y || arr1[i][1].y === arr2[j][1].y) {
        continue
      }

      var coordinate = segmentsIntr(arr1[i][0], arr1[i][1], arr2[j][0], arr2[j][1])
      if (coordinate) {
        var fill = '#fff'
        var scale = 1
        var strokeColor = '#00ccff'
        var className = null
        var strokewidth = 1
        var Rtstrokewidth = 1
        var RtstrokeColor = '#00ccff'
        var Radius = '5'
        var flowR_L = 'R'
        var id = null
        if (arr1[i][0].y === arr1[i][1].y || Math.abs(arr1[i][0].y - arr1[i][1].y) < 1) {
          strokeColor = arr1[i][2]
          className = arr1[i][3]
          strokewidth = arr1[i][4]
          id = arr1[i][5]
          if (className && arr1[i][0].x - arr1[i][1].x < 0) {
            flowR_L = 'R'
          } else if (className && arr1[i][0].x - arr1[i][1].x > 0) {
            flowR_L = 'L'
          }
        }

        if (arr2[j][0].y === arr2[j][1].y || Math.abs(arr2[j][0].y - arr2[j][1].y) < 1) {
          strokeColor = arr2[j][2]
          className = arr2[j][3]
          strokewidth = arr2[j][4]
          id = arr2[j][5]
          if (className && arr2[j][0].x - arr2[j][1].x < 0) {
            flowR_L = 'R'
          } else if (className && arr2[j][0].x - arr2[j][1].x > 0) {
            flowR_L = 'L'
          }
        }
        // id flow
        var pathState = window.newEditorUi.editor.graph.view.getState(window.newEditorUi.editor.graph.getModel().getCell(id))
        if (pathState) {
          var paths = pathState.shape.node.getElementsByTagName('path')
          className = paths[1].getAttribute('class')
        }

        // 增加对误差值的判断
        if (arr1[i][0].x === arr1[i][1].x || Math.abs(arr1[i][0].x - arr1[i][1].x) < 1) {
          Rtstrokewidth = arr1[i][4]
          RtstrokeColor = arr1[i][2]
        }
        if (arr2[j][0].x === arr2[j][1].x || Math.abs(arr2[j][0].x - arr2[j][1].x) < 1) {
          Rtstrokewidth = arr2[j][4]
          RtstrokeColor = arr2[j][2]
        }

        if (window.newEditorUi.editor.graph) {
          fill = window.newEditorUi.editor.graph.background
        }
        if (window.newEditorUi.editor.graph.view.scale !== 1) {
          scale = window.newEditorUi.editor.graph.view.scale
        }

        // var createG = document.createElementNS("http://www.w3.org/2000/svg", "g");
        // var createRect = document.createElementNS("http://www.w3.org/2000/svg", "rect");
        // createG.setAttribute('transform', 'translate(0.5,0.5)')
        // createG.style = 'visibility:visible;cursor:move;'
        // createRect.setAttribute('x', coordinate.x - 5)
        // createRect.setAttribute('y', coordinate.y - 5)
        // createRect.setAttribute('width', 10)
        // createRect.setAttribute('height', 10)
        // createRect.setAttribute('rx', 6.28)
        // createRect.setAttribute('fill', fill)
        // createRect.setAttribute('stroke', '#00ccff')
        // createG.setAttribute('class', '_addRect')
        // createG.appendChild(createRect)
        // g[0].children[0].children[1].appendChild(createG)

        // 针对相邻近交点做处理
        // let intersectionKeyFlage = false
        // var new_arr = JSON.parse(JSON.stringify(window.intersectionKey))
        // if (new_arr.length > 0) {
        //   for (let i = 0; i < new_arr.length; i++) {
        //     if (Math.abs(coordinate.x - new_arr[i].x) < 12 * scale) {
        //       intersectionKeyFlage = true
        //     }
        //   }
        // }
        // if (intersectionKeyFlage) {
        //   continue
        // }
        // window.intersectionKey.push(coordinate)

        // 创建交点样式
        var createG = document.createElementNS("http://www.w3.org/2000/svg", "g");
        var createPath = document.createElementNS("http://www.w3.org/2000/svg", "path");
        var createPath2 = document.createElementNS("http://www.w3.org/2000/svg", "path");
        var createPath3 = document.createElementNS("http://www.w3.org/2000/svg", "path");

        createG.setAttribute('transform', 'translate(0.5,0.5)')
        createG.style = 'visibility:visible;cursor:move;'

        createPath.setAttribute('stroke-width', strokewidth * scale)
        createPath.setAttribute('stroke-miterlimit', '10')
        // createPath.setAttribute('d', ' M ' + (coordinate.x - (5 + Rtstrokewidth) * scale) + ' ' + coordinate.y + ' m ' + ((5 + Rtstrokewidth) * scale * 2) + ' ' + (-scale / 2) + ' A ' + ((5 + Rtstrokewidth) + strokewidth - 1) * scale + ' ' + ((5 + Rtstrokewidth) + strokewidth - 1) * scale + ' ' + '0 ' + '1 ' + '0 ' + (coordinate.x - (5 + Rtstrokewidth) * scale) + ' ' + (coordinate.y - scale / 2))
        // 非量变线条
        createPath.setAttribute('d', ' M ' + (coordinate.x - (Radius * 3 * scale)) + ' ' + coordinate.y + ' m ' + (Radius * scale * 2) + ' ' + (strokewidth / 2) + ' a ' + Radius * scale + ' ' + Radius * scale + ' ' + '0 ' + '1 ' + '1 ' + (Radius * 2 * scale) + ' ' + 0)
        createPath.setAttribute('fill', 'none')
        createPath.setAttribute('stroke', strokeColor)
        if (className && flowR_L === 'R') {
          if (className === 'flowV') {
            createPath.setAttribute('class', 'flowV')
          } else if (className === 'checkFlow') {
            createPath.setAttribute('class', 'checkFlow')
          } else {
            createPath.setAttribute('class', 'flowR')
          }
        } else if (className && flowR_L === 'L') {
          if (className === 'flowV') {
            createPath.setAttribute('class', 'flowV')
          } else if (className === 'checkFlow') {
            createPath.setAttribute('class', 'checkFlow')
          } else {
            createPath.setAttribute('class', 'flowL')
          }
        }

        createPath2.setAttribute('stroke-width', strokewidth * scale + 2)
        createPath2.setAttribute('stroke-miterlimit', '10')
        createPath2.setAttribute('d', ' M ' + ((coordinate.x - Radius * scale) + (strokewidth / 2 * scale)) + ' ' + (coordinate.y) + ' L ' + (coordinate.x + Radius * scale - (strokewidth / 2 * scale)) + ' ' + (coordinate.y))
        createPath2.setAttribute('fill', 'none')
        createPath2.setAttribute('stroke', fill)

        createPath3.setAttribute('stroke-width', strokewidth * scale + 3)
        createPath3.setAttribute('stroke-miterlimit', '10')
        createPath3.setAttribute('d', ' M ' + (coordinate.x - (Rtstrokewidth / 2 * scale)) + ' ' + (coordinate.y) + ' L ' + (coordinate.x + (Rtstrokewidth / 2 * scale)) + ' ' + (coordinate.y))
        createPath3.setAttribute('fill', 'none')
        createPath3.setAttribute('stroke', RtstrokeColor)
        if (className === 'checkFlow') {
          createPath3.setAttribute('class', 'checkFlow')
          createG.setAttribute('class', '_addRect')
          createG.appendChild(createPath)
          createG.appendChild(createPath2)
          createG.appendChild(createPath3)
        } else if (className === 'flowV') {
          createG.setAttribute('class', '_addRect')
        } else {
          createG.setAttribute('class', '_addRect')
          createG.appendChild(createPath)
          createG.appendChild(createPath2)
          createG.appendChild(createPath3)
        }
        g[0].children[0].children[1].appendChild(createG)
      }
    }
  }
}

function segmentsIntr(a, b, c, d) {

  /** 1 解线性方程组, 求线段交点. **/
  // 如果分母为0 则平行或共线, 不相交
  var denominator = (b.y - a.y) * (d.x - c.x) - (a.x - b.x) * (c.y - d.y);
  if (denominator == 0) {
    return false;
  }

  // 线段所在直线的交点坐标 (x , y)
  var x = ((b.x - a.x) * (d.x - c.x) * (c.y - a.y) +
    (b.y - a.y) * (d.x - c.x) * a.x -
    (d.y - c.y) * (b.x - a.x) * c.x) / denominator;
  var y = -((b.y - a.y) * (d.y - c.y) * (c.x - a.x) +
    (b.x - a.x) * (d.y - c.y) * a.y -
    (d.x - c.x) * (b.y - a.y) * c.y) / denominator;

  /** 2 判断交点是否在两条线段上 **/
  if (
    // 交点在线段1上
    (x - a.x) * (x - b.x) <= 0 && (y - a.y) * (y - b.y) <= 0
    // 且交点也在线段2上
    &&
    (x - c.x) * (x - d.x) <= 0 && (y - c.y) * (y - d.y) <= 0
  ) {

    // 返回交点p
    return {
      x: x,
      y: y
    }
  }
  //否则不相交
  return false
}
// 维护字段
window.equipmentCodeKeys = {
  // 电源设备
  UPS: 'UPS',
  dykg: '开关电源',
  gyzl: '高压直流',
  // 低压配电
  ATS: 'ATS柜',
  byq: '变压器',
  yj: '油机',
  dycxg: '低压出线柜',
  dyjxg: '低压进线柜',
  pdg: '配电柜',
  ltg: '列头柜',
  wmk: '微模块'
}

function sendMessage(type, message, width, time) {
  var Vtime = time || 3000
  var dom = document.getElementsByTagName('body')[0]
  var div = document.createElement('div')
  var icon = document.createElement('span')
  var span = document.createElement('span')
  div.style.left = (document.documentElement.clientWidth - width) / 2 + 'px'
  div.style.width = width + 'px'
  div.classList.add('sendMessage_dom')
  icon.classList.add('sendMessage_icon')
  span.classList.add('sendMessage_span')
  span.innerText = message
  div.appendChild(icon)
  div.appendChild(span)
  setTimeout(() => {
    document.body.removeChild(div);
  }, Vtime);

  if (type === 'success') {
    icon.classList.add('sendMessage_success')
    dom.appendChild(div)
  } else if (type === 'warning') {
    icon.classList.add('sendMessage_warning')
    dom.appendChild(div)
  }
}

function SendDialog(message, width) {
  if (document.getElementById('_SendDialog')) {
    document.body.removeChild(document.getElementById('_SendDialog'))
  }
  var div = document.createElement('div')
  var span = document.createElement('span')
  var input = document.createElement('input')

  div.id = '_SendDialog'
  div.style.left = (document.documentElement.clientWidth - width) / 2 + 'px'
  div.style.top = '30px'
  div.style.width = width + 'px'
  div.classList.add('sendMessage_dom')
  div.style.display = 'block'
  div.style.height = '160px'
  span.innerText = message
  span.style.height = '30px'
  span.style.width = '100%'
  span.style.lineHeight = '30px'
  span.style.marginTop = '15px'
  span.style.display = 'block'

  input.value = '1.0'
  input.className = 'eleInput'
  input.style.display = 'inline-block'
  input.style.height = '30px'
  input.style.lineHeight = '30px'
  input.style.width = '130px'
  input.style.verticalAlign = 'middle'
  input.style.borderRadius = '0'
  input.style.textAlign = 'center'
  input.setAttribute('readonly', 'readonly')

  var plusButton = document.createElement('div')
  var reduceButton = document.createElement('div')
  var bandInput = document.createElement('div')
  plusButton.classList.add('plusButton')
  reduceButton.classList.add('reduceButton')
  bandInput.style.height = '30px'
  bandInput.style.width = '100%'
  bandInput.style.textAlign = 'center'

  bandInput.appendChild(reduceButton)
  bandInput.appendChild(input)
  bandInput.appendChild(plusButton)

  var DivBottom = document.createElement('div')
  var DivBottomButton1 = document.createElement('div')
  var DivBottomButton2 = document.createElement('div')
  DivBottom.appendChild(DivBottomButton1)
  DivBottom.appendChild(DivBottomButton2)
  DivBottom.classList.add('DivBottom')
  DivBottomButton1.classList.add('toastDivBottomButton')
  DivBottomButton2.classList.add('toastDivBottomButton', 'toastDivBottomButton2')
  DivBottomButton1.innerText = '取消'
  DivBottomButton2.innerText = '确定'
  DivBottom.style.marginTop = '10px'
  DivBottomButton1.style.marginLeft = '140px'

  var warningSpan = document.createElement('span')
  warningSpan.style.fontSize = '12px'
  warningSpan.innerText = '根据个人的设备性能,建议像素倍数不超过3倍!'
  warningSpan.style.display = 'block'
  warningSpan.style.height = '20px'
  warningSpan.style.lineHeight = '20px'
  warningSpan.style.paddingLeft = '25px'
  warningSpan.style.color = 'rgb(187, 187, 187)'
  warningSpan.style.marginTop = '10px'

  div.appendChild(span)
  div.appendChild(bandInput)
  div.appendChild(warningSpan)

  div.appendChild(DivBottom)
  document.body.appendChild(div)

  // 处理事件
  plusButton.addEventListener('click', function (e) {
    if (Math.floor((parseFloat(input.value) + 0.1) * 10) / 10 >= 4) {
      input.value = '4.0'
    } else {
      input.value = Math.floor((parseFloat(input.value) + 0.1) * 10) / 10
    }
  })
  reduceButton.addEventListener('click', function (e) {
    if (Math.floor((parseFloat(input.value) - 0.1) * 10) / 10 <= 1) {
      input.value = '1.0'
    } else {
      input.value = Math.floor((parseFloat(input.value) - 0.1) * 10) / 10
    }
  })
  bandInput.addEventListener('mousemove', function (e) {
    plusButton.style.border = '1px solid rgb(64, 158, 255)'
    reduceButton.style.border = '1px solid rgb(64, 158, 255)'
    input.style.border = '1px solid rgb(64, 158, 255)'
    reduceButton.style.borderRight = '1px solid #dcdfe6'
    plusButton.style.borderLeft = '1px solid #dcdfe6'
    input.style.borderRight = '0px'
    input.style.borderLeft = '0px'
  })
  bandInput.addEventListener('mouseleave', function (e) {
    plusButton.style.border = '1px solid #dcdfe6'
    reduceButton.style.border = '1px solid #dcdfe6'
    input.style.border = '1px solid #dcdfe6'
  })
  // 关闭
  DivBottomButton1.addEventListener('click', function (e) {
    document.body.removeChild(div);
  }, false)
  // 确定
  DivBottomButton2.addEventListener('click', function () {
    input.value = parseFloat(input.value)
    document.body.removeChild(div);
    _exportPNG(input.value)
  }, false)
}

async function _exportPNG(value) {
  var editor = window.newEditorUi.editor
  var graph = editor.graph

  // 获取节点信息
  graph.zoomTo(1)
  var dom = document.getElementsByClassName('geDiagramContainer')[0].getElementsByTagName('svg')[0];

  var svgImage = dom.getElementsByTagName('image')
  var Gscale = value;

  // 离线版本 / 在线版本
  if (window.location.protocol === 'file:') {
    for (var i = 0; i < svgImage.length; i++) {
      if (svgImage[i].href.baseVal.indexOf('data:image') === -1) {
        var key = svgImage[i].href.baseVal.substring(svgImage[i].href.baseVal.indexOf(STENCIL_PATH))
        svgImage[i].setAttribute('xlink:href', staticImageBase64[key])
      }
    }
  } else {
    for (let i = 0; i < svgImage.length; i++) {
      // 将非base64的图片替换成base64
      if (svgImage[i].href.baseVal.indexOf('data:image') === -1) {
        var image = new Image();
        image.src = svgImage[i].href.baseVal
        image.crossOrigin = "anonymous";

        var height = image.height
        var width = image.width
        var canvasWidth = svgImage[i].width.baseVal.value;
        var canvasHeight = svgImage[i].height.baseVal.value;

        var canvas = document.createElement("canvas")
        canvas.width = canvasWidth * Gscale
        canvas.height = canvasHeight * Gscale
        var ctx = canvas.getContext("2d")
        ctx.scale(Gscale, Gscale)
        var scale = 1
        // 计算缩放比列
        if (height > width) {
          scale = width / height
          ctx.drawImage(image, (canvasWidth - canvasHeight * scale) / 2, 0, canvasHeight * scale, canvasHeight);
        } else if (width > height) {
          scale = height / width
          ctx.drawImage(image, 0, (canvasHeight - canvasWidth * scale) / 2, canvasWidth, canvasWidth * scale);
        } else {
          ctx.drawImage(image, 0, 0, canvasWidth, canvasHeight);
        }
        var ext = image.src.substring(image.src.lastIndexOf(".") + 1).toLowerCase();
        var dataURL = canvas.toDataURL("image/" + ext);
        svgImage[i].setAttribute('xlink:href', dataURL)
      }
    }
  }
  var pageFormatWidth = graph.pageFormat.width
  var pageFormatHeight = graph.pageFormat.height
  // 生成图片
  var _getSvg = await html2canvas(dom, {
    width: graph.view.graphBounds.width + pageFormatWidth * 2,
    height: graph.view.graphBounds.height + pageFormatHeight * 2,
    scrollX: -710,
    scrollY: -460,
    useCORS: true,
    scale: Gscale,
    backgroundColor: graph.background
  }).then(canvas => {
    return canvas.toDataURL("image/png");
  })
  // 生成链接
  var dlLink = document.createElement('a');
  dlLink.download = 'svg.png';
  if (_getSvg === 'data:,') {
    sendMessage('warning', '未检测到图片或者图片过大!', 220, 3000)
    return
  }
  dlLink.href = URL.createObjectURL(base64ToBlob(_getSvg))

  document.body.appendChild(dlLink);
  dlLink.click();
  document.body.removeChild(dlLink);

  graph.refresh()
  sendMessage('success', '导出图片完成!', 220, 3000)
}
async function toast(ui, type) {
  var editor = ui.editor
  var graph = editor.graph
  if (document.getElementsByClassName('toastDiv')[0]) {
    document.body.removeChild(document.getElementsByClassName('toastDiv')[0])
  }
  // 总样式
  var toastDiv = document.createElement('div')
  // 顶部的样式
  var toastDivTop = document.createElement('div')
  var toastDivTopSpan = document.createElement('div')
  var toastDivTopIcon = document.createElement('span')
  var toastDivTopContent = document.createElement('div')
  // 中间样式
  // 查询
  var toastDivContent = document.createElement('div')
  var toastDivContentQuery = document.createElement('div')
  var toastDivContentQueryInput = document.createElement('input')
  var toastDivContentQueryInputIcon = document.createElement('span')
  toastDivContentQueryInput.classList.add('eleInput')
  toastDivContentQueryInput.setAttribute('placeholder', '请输入设备名称查询')
  toastDivContentQuery.appendChild(toastDivContentQueryInput)
  toastDivContentQuery.appendChild(toastDivContentQueryInputIcon)
  toastDivContent.appendChild(toastDivContentQuery)


  // 初始化处理表单数据
  var toastTableData = await _getData(graph)
  var toastDivContentTabel = await _createTabel({
    hasValueData: toastTableData.hasValueData,
    noValueData: toastTableData.noValueData
  })
  // 填充表单
  toastDivContent.appendChild(toastDivContentTabel)
  // class
  toastDivContent.classList.add('toastDivContent')
  toastDivContentQuery.classList.add('toastDivContentQuery')
  toastDivContentQueryInput.classList.add('toastDivContentQueryInput')
  toastDivContentQueryInputIcon.classList.add('toastDivContentQueryInputIcon')
  // 中间整体样式
  toastDivContent.style.height = (document.documentElement.clientHeight - 60 - 37 - 90 - 20 - 30) + 'px'
  toastDivContentQueryInput.style.height = '30px';
  toastDivContentQueryInput.style.width = '298px'
  toastDivContentQueryInput.style.borderRight = '0px'
  toastDivContentQueryInput.style.borderRadius = '4px 0 0 4px'

  // 底部的样式
  var toastDivBottom = document.createElement('div')
  var toastDivBottomButton1 = document.createElement('div')
  var toastDivBottomButton2 = document.createElement('div')
  toastDivBottom.appendChild(toastDivBottomButton1)
  toastDivBottom.appendChild(toastDivBottomButton2)

  toastDiv.style.position = 'absolute'
  toastDiv.style.width = '330px'
  toastDiv.style.height = (document.documentElement.clientHeight - 90) + 'px'
  toastDiv.style.top = '40px'
  toastDiv.style.right = 250 + 'px'
  // 全局样式
  toastDiv.classList.add('toastDiv')
  // 底部样式
  toastDivBottom.classList.add('toastDivBottom')
  toastDivBottomButton1.classList.add('toastDivBottomButton')
  toastDivBottomButton2.classList.add('toastDivBottomButton', 'toastDivBottomButton2')
  toastDivBottomButton1.innerText = '取消'
  toastDivBottomButton2.innerText = '确定'
  // 顶部的样式
  toastDivTop.classList.add('toastDivTop')
  toastDivTop.style.width = '100%'
  toastDivTop.style.borderBottom = '1px solid #ebeef5'
  toastDivTopSpan.style.padding = '10px'
  toastDivTopSpan.innerText = '配置设备编码'
  toastDivTopSpan.style.fontWeight = 'bold'
  toastDivTopSpan.style.fontSize = '18px'
  toastDivTopSpan.style.display = 'inline-block'
  toastDivTopIcon.style.display = 'inline-block'
  toastDivTopIcon.classList.add('toastDivTopIcon')
  toastDivTopContent.innerText = '设备编码格式参考《中国移动动环命名及编码指导意见》中的命名格式要求'
  toastDivTopContent.style.padding = '0 10px 10px 10px'
  toastDivTop.appendChild(toastDivTopSpan)
  toastDivTop.appendChild(toastDivTopIcon)
  toastDivTop.appendChild(toastDivTopContent)
  // 顶部
  toastDiv.appendChild(toastDivTop)
  // 中间
  toastDiv.appendChild(toastDivContent)
  // 底部取消确定
  toastDiv.appendChild(toastDivBottom)
  var dom = document.getElementsByTagName('body')[0]
  dom.appendChild(toastDiv)

  // 事件处理
  // 关闭
  toastDivTopIcon.addEventListener('click', function (e) {
    window.newEditorUi.editor.graph.zoomTo(1)
    window.newEditorUi.resetScrollbars()
    dom.removeChild(toastDiv);
  }, false)
  // 取消
  toastDivBottomButton1.addEventListener('click', function (e) {
    window.newEditorUi.editor.graph.zoomTo(1)
    window.newEditorUi.resetScrollbars()
    dom.removeChild(toastDiv);
  }, false)
  // 确定
  toastDivBottomButton2.addEventListener('click', function (e) {
    // 根据type调用跳转或者保存方法
    _JumpAction(e, type)
  }, false)
  // 搜索
  toastDivContentQueryInputIcon.addEventListener('click', function (e) {
    _search(e, toastDivContentQueryInput.value)
  }, false)
  toastDivContentQueryInput.addEventListener('change', function (e) {
    _search(e, toastDivContentQueryInput.value)
  }, false)
  // 移动事件
  _dropToast()
}

function _getData(graph) {

  var hasValueData = []
  var noValueData = []

  var nodes = graph.getModel().cells;
  Object.keys(nodes).forEach(function (key) {
    if (!nodes[key].edge && nodes[key].vertex) {
      var state = graph.view.getState(nodes[key]);
      if (equipmentCodeKeys[state.style.type] && state.style.precinctId) {
        hasValueData.push({
          id: nodes[key].id,
          precinctId: state.style.precinctId,
          value: equipmentCodeKeys[state.style.type]
        })
      } else if (equipmentCodeKeys[state.style.type]) {
        noValueData.push({
          id: nodes[key].id,
          precinctId: null,
          value: equipmentCodeKeys[state.style.type]
        })
      }
    }
  })
  return {
    hasValueData: hasValueData,
    noValueData: noValueData,
    allData: noValueData.concat(hasValueData)
  }
}


function _createTabel(data) {
  // 数据栏
  var div = document.createElement('div')
  div.id = '_tableData'
  var toastDivContentTabel = document.createElement('div')
  var toastDivContentTabelKey = document.createElement('span')
  var toastDivContentTabelValue = document.createElement('span')
  toastDivContentTabel.appendChild(toastDivContentTabelKey)
  toastDivContentTabel.appendChild(toastDivContentTabelValue)
  // 填充数据
  toastDivContentTabelKey.innerText = '设备名称'
  toastDivContentTabelValue.innerText = '设备编码'
  // 增加class
  toastDivContentTabel.classList.add('toastDivContentTabelTitle', 'toastDivContentTabelTitle')
  toastDivContentTabelKey.classList.add('toastDivContentTabelKeyTitle')
  toastDivContentTabelValue.classList.add('toastDivContentTabelValueTitle')
  div.appendChild(toastDivContentTabel)
  if (data.noValueData) {
    for (let i = 0; i < data.noValueData.length; i++) {
      var toastDivContentTabel = document.createElement('div')
      var toastDivContentTabelKey = document.createElement('span')
      var toastDivContentTabelValue = document.createElement('input')
      toastDivContentTabel.appendChild(toastDivContentTabelKey)
      toastDivContentTabel.appendChild(toastDivContentTabelValue)
      div.appendChild(toastDivContentTabel)
      // 填充数据
      toastDivContentTabelKey.innerText = data.noValueData[i].value
      toastDivContentTabelValue.value = data.noValueData[i].precinctId
      // 增加class
      if (i % 2 === 0) {
        toastDivContentTabel.style.background = '#fff'
      }
      toastDivContentTabelValue.setAttribute('id', data.noValueData[i].id)
      toastDivContentTabel.classList.add('toastDivContentTabel')
      toastDivContentTabelKey.classList.add('toastDivContentTabelKey')
      toastDivContentTabelValue.classList.add('toastDivContentTabelValue', 'eleInput')
      // 处理事件
      toastDivContentTabelValue.addEventListener('change', _setprecinctId, false)
      toastDivContentTabelValue.addEventListener('mouseover', _setSelectionCell, false)
      toastDivContentTabelValue.addEventListener('mouseleave', _removeSelectionCell, false)
    }
  }
  if (data.hasValueData) {
    for (let i = 0; i < data.hasValueData.length; i++) {
      var toastDivContentTabel = document.createElement('div')
      var toastDivContentTabelKey = document.createElement('span')
      var toastDivContentTabelValue = document.createElement('input')
      toastDivContentTabel.appendChild(toastDivContentTabelKey)
      toastDivContentTabel.appendChild(toastDivContentTabelValue)
      div.appendChild(toastDivContentTabel)
      // 填充数据
      toastDivContentTabelKey.innerText = data.hasValueData[i].value
      toastDivContentTabelValue.value = data.hasValueData[i].precinctId
      if (data.noValueData.length % 2 === 0) {
        if (i % 2 === 0) {
          toastDivContentTabel.style.background = '#fff'
        }
      } else {
        if (i % 2 === 1) {
          toastDivContentTabel.style.background = '#fff'
        }
      }
      // 增加class
      toastDivContentTabelValue.setAttribute('id', data.hasValueData[i].id)
      toastDivContentTabel.classList.add('toastDivContentTabel')
      toastDivContentTabelKey.classList.add('toastDivContentTabelKey')
      toastDivContentTabelValue.classList.add('toastDivContentTabelValue', 'eleInput')
      // 处理事件
      toastDivContentTabelValue.addEventListener('change', _setprecinctId, false)
      toastDivContentTabelValue.addEventListener('mouseover', _setSelectionCell, false)
      toastDivContentTabelValue.addEventListener('mouseleave', _removeSelectionCell, false)
    }
  }
  if (data.allData) {
    if (document.getElementById('_tableData')) {
      document.getElementsByClassName('toastDivContent')[0].removeChild(document.getElementById('_tableData'))
    }
    for (let i = 0; i < data.allData.length; i++) {
      var toastDivContentTabel = document.createElement('div')
      var toastDivContentTabelKey = document.createElement('span')
      var toastDivContentTabelValue = document.createElement('input')
      toastDivContentTabel.appendChild(toastDivContentTabelKey)
      toastDivContentTabel.appendChild(toastDivContentTabelValue)
      div.appendChild(toastDivContentTabel)
      // 填充数据
      toastDivContentTabelKey.innerText = data.allData[i].value
      toastDivContentTabelValue.value = data.allData[i].precinctId
      // 增加class
      if (i % 2 === 0) {
        toastDivContentTabel.style.background = '#fff'
      }
      toastDivContentTabelValue.setAttribute('id', data.allData[i].id)
      toastDivContentTabel.classList.add('toastDivContentTabel')
      toastDivContentTabelKey.classList.add('toastDivContentTabelKey')
      toastDivContentTabelValue.classList.add('toastDivContentTabelValue', 'eleInput')
      // 处理事件
      toastDivContentTabelValue.addEventListener('change', _setprecinctId, false)
      toastDivContentTabelValue.addEventListener('mouseover', _setSelectionCell, false)
      toastDivContentTabelValue.addEventListener('mouseleave', _removeSelectionCell, false)
    }
    document.getElementsByClassName('toastDivContent')[0].appendChild(div)
  }
  return div
}

function _setprecinctId(e) {
  var graph = window.newEditorUi.editor.graph
  var id = e.target.id
  var precinctId = e.target.value
  graph.getModel().beginUpdate();
  try {
    var cell = graph.getModel().getCell(id)
    graph.getModel().setStyle(cell, cell.style + ';precinctId=' + precinctId)
    e.target.blur()
    sendMessage('success', '设置编码成功!', 120, 2000)
  } finally {
    graph.getModel().endUpdate();
  }
}

async function _search(e, v) {
  var getData = await _getData(window.newEditorUi.editor.graph)
  if (v) {
    var data = getData.allData.filter(function (el) {
      return el.value.toLowerCase().indexOf(v.toLowerCase()) >= 0
    })
    _createTabel({
      allData: data
    })
    e.target.blur()
  } else {
    _createTabel({
      allData: getData.allData
    })
    e.target.blur()
  }
}

function _setSelectionCell(e) {
  var graph = window.newEditorUi.editor.graph
  var cell = graph.getModel().getCell(e.target.id)
  var state = graph.view.getState(cell);
  graph.setSelectionCell(cell)
  // 画布大小
  var pageFormat = graph.pageFormat
  // 定位滚动条位置
  document.getElementsByClassName('geDiagramContainer')[0].scrollTo(state.x - (pageFormat.width - state.width * 2) / 2, state.y - (pageFormat.height - state.height * 2) / 2)
}

function _removeSelectionCell(e) {
  var graph = window.newEditorUi.editor.graph
  var cell = graph.getModel().getCell(e.target.id)
  graph.removeSelectionCell(cell)
}

function _JumpAction(e, type) {
  var editor = window.newEditorUi.editor
  var graph = editor.graph
  window.newEditorUi.editor.graph.zoomTo(1)
  window.newEditorUi.resetScrollbars()
  if (type === 1) {
    window.newEditorUi.save(window.newEditorUi.editor.getOrCreateFilename())
    document.body.removeChild(document.getElementsByClassName('toastDiv')[0]);
    return
  }
  if (type === 2) {
    if (graph.isEditing()) {
      graph.stopEditing();
    }
    var xml = mxUtils.getXml(editor.getGraphXml());
    var nodes = graph.getModel().cells;
    // 告警图标显示
    var arr = []
    Object.keys(nodes).forEach(function (key) {
      var state = graph.view.getState(nodes[key]);
      var style = state.style;
      if (nodes[key].geometry && !nodes[key].edge && state.style.id) {
        arr.push({
          deviceId: style.deviceId || null,
          deviceKind: style.deviceKind || null,
          deviceModel: style.deviceModel || null,
          deviceType: style.deviceType || null,
          id: style.id || null,
          isParent: style.isParent || null,
          isPrecinct: style.isPrecinct || null,
          precinctId: style.precinctId || null,
          precinctKind: style.precinctKind || null,
        })
      }
    })
    // 向父vue页面发送信息
    window.parent.postMessage({
      cmd: 'modeSwitch',
      params: {
        success: true,
        data: JSON.stringify(xml),
        comTrees: JSON.stringify(arr),
        name: sessionStorage.getItem('viewTitle'),
        pictureType: sessionStorage.getItem('pictureType')
      }
    }, '*');
    document.body.removeChild(document.getElementsByClassName('toastDiv')[0]);
    return
  }
}

function _dropToast() {
  var box = document.getElementsByClassName("toastDiv")[0];
  var x, y;
  var isDrop = false;
  box.onmousedown = function (e) {
    var e = e || window.event
    x = e.clientX - box.offsetLeft;
    y = e.clientY - box.offsetTop;
    isDrop = true;
    box.classList.add('toastDivMove');
  }

  document.onmousemove = function (e) {
    if (isDrop) {
      var e = e || window.event;
      var moveX = e.clientX - x;
      var moveY = e.clientY - y;

      var maxX = document.documentElement.clientWidth - box.offsetWidth;
      var maxY = document.documentElement.clientHeight - box.offsetHeight;

      moveX = Math.min(maxX, Math.max(0, moveX));
      moveY = Math.min(maxY, Math.max(0, moveY));
      box.style.left = moveX + "px";
      box.style.top = moveY + "px";

    } else {
      return;
    }
  }

  document.onmouseup = function () {
    isDrop = false;
    box.classList.remove('toastDivMove');
  }
}

function CheckConnection() {
  var editor = window.newEditorUi.editor
  var graph = editor.graph
  var cells = graph.getModel().cells
  Object.keys(cells).forEach(function (key) {
    if (cells[key].edge && (!cells[key].source || !cells[key].target)) {
      let state = graph.view.getState(cells[key])
      if (state) {
        var path = state.shape.node.getElementsByTagName('path')
        for (let i = 1; i < path.length; i++) {
          if (i === 1) {
            path[i].setAttribute('class', 'checkFlow');
          } else {
            path[i].setAttribute('class', 'checkFlow');
            path[i].setAttribute('fill', 'none');
          }
        }
      }
    }
  })
  flow(graph)
}

function base64ToBlob(code) {
  var parts = code.split(';base64,');
  var contentType = parts[0].split(':')[1];
  var raw = window.atob(parts[1]);
  var rawLength = raw.length;

  var uInt8Array = new Uint8Array(rawLength);

  for (var i = 0; i < rawLength; ++i) {
    uInt8Array[i] = raw.charCodeAt(i);
  }
  return new Blob([uInt8Array], {
    type: contentType
  });
}

function geDiagramContainerSc(type) {
  var geDiagramContainer = document.getElementsByClassName('geDiagramContainer ')[0]
  if (type == 1) {
    geDiagramContainer.style.overflow = 'auto'
  } else if (type == 2) {
    geDiagramContainer.style.overflow = 'hidden'
  } else {
    if (geDiagramContainer.style.overflow === 'hidden') {
      geDiagramContainer.style.overflow = 'auto'
    } else {
      geDiagramContainer.style.overflow = 'hidden'
    }
  }
}