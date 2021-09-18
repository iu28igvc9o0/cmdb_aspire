/**
 * Copyright (c) 2006-2012, JGraph Ltd
 */
/**
 * Construcs a new toolbar for the given editor.
 */


function Toolbar(editorUi, container) {
	this.editorUi = editorUi;
	this.container = container;
	this.staticElements = [];
	this.init();

	// Global handler to hide the current menu  全局处理程序隐藏当前菜单
	this.gestureHandler = mxUtils.bind(this, function (evt) {
		if (this.editorUi.currentMenu != null && mxEvent.getSource(evt) != this.editorUi.currentMenu.div) {
			this.hideMenu();
		}
	});

	mxEvent.addGestureListeners(document, this.gestureHandler);
};

/**
 * Image for the dropdown arrow. 下拉箭头的图像。
 */
Toolbar.prototype.dropdownImage = (!mxClient.IS_SVG) ? IMAGE_PATH + '/dropdown.gif' : 'data:image/gif;base64,R0lGODlhDQANAIABAHt7e////yH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IE1hY2ludG9zaCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpCREM1NkJFMjE0NEMxMUU1ODk1Q0M5MjQ0MTA4QjNDMSIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpCREM1NkJFMzE0NEMxMUU1ODk1Q0M5MjQ0MTA4QjNDMSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkQzOUMzMjZCMTQ0QjExRTU4OTVDQzkyNDQxMDhCM0MxIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkQzOUMzMjZDMTQ0QjExRTU4OTVDQzkyNDQxMDhCM0MxIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Af/+/fz7+vn49/b19PPy8fDv7u3s6+rp6Ofm5eTj4uHg397d3Nva2djX1tXU09LR0M/OzczLysnIx8bFxMPCwcC/vr28u7q5uLe2tbSzsrGwr66trKuqqainpqWko6KhoJ+enZybmpmYl5aVlJOSkZCPjo2Mi4qJiIeGhYSDgoGAf359fHt6eXh3dnV0c3JxcG9ubWxramloZ2ZlZGNiYWBfXl1cW1pZWFdWVVRTUlFQT05NTEtKSUhHRkVEQ0JBQD8+PTw7Ojk4NzY1NDMyMTAvLi0sKyopKCcmJSQjIiEgHx4dHBsaGRgXFhUUExIREA8ODQwLCgkIBwYFBAMCAQAAIfkEAQAAAQAsAAAAAA0ADQAAAhGMj6nL3QAjVHIu6azbvPtWAAA7';

/**
 * Image element for the dropdown arrow. 用于下拉箭头的图像元素。
 */
Toolbar.prototype.dropdownImageHtml = '<img border="0" style="position:absolute;right:4px;top:' +
	((!EditorUi.compactUi) ? 8 : 6) + 'px;" src="' + Toolbar.prototype.dropdownImage + '" valign="middle"/>';

/**
 * Defines the background for selected buttons. 定义所选按钮的背景
 */
Toolbar.prototype.selectedBackground = '#d0d0d0';

/**
 * Defines the background for selected buttons. 定义所选按钮的背景
 */
Toolbar.prototype.unselectedBackground = 'none';

/**
 * Array that contains the DOM nodes that should never be removed. 包含不应该删除的DOM节点的数组。
 */
Toolbar.prototype.staticElements = null;

/**
 * Adds the toolbar elements. 添加工具栏元素。
 */
Toolbar.prototype.init = function () {
	var sw = screen.width;
	var graph = this.editorUi.editor.graph;
	var editor = this.editorUi.editor
	var ui = this.editorUi;
	// Takes into account initial compact mode   考虑初始压缩模式
	sw -= (screen.height > 740) ? 56 : 0;


	if (sw >= 700 && window.systemType === 0) {

		//模式切换
		let a3 = document.createElement("a")
		let div3 = document.createElement("div")
		this.container.appendChild(a3)
		a3.appendChild(div3)
		a3.classList.add("geButton")
		a3.classList.add("toolbarA")
		div3.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NjBGNEE1MzlCNEYxMTFFOUE0NzE4RjAyODg5OTFFRTMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NjBGNEE1MzhCNEYxMTFFOUE0NzE4RjAyODg5OTFFRTMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+OlrOKAAAAJ5JREFUeNrMlG0OQDAMhldxuTmuj79cRnCLUulkkZkWw5s0GdkefbsWIKJ5WplJoCRQQ/a5BHaJgV4pg85Yx1lZHvQKcAP7UHAXBQDuxkBrlh3DnZo2mpo6KzHVvKfbZYoS+9LmhafsnyqPZPCv5s9jbSKs6bezTxm0vC75ORSX+rQSThS+Oqaj/2VFkKYj+wWDtdCezgbt//7PPwswABIhsjaTF6XVAAAAAElFTkSuQmCC)'
		div3.className = "toolbarDiv"
		a3.setAttribute('title', '模式切换')
		a3.addEventListener('click', function (e) {
			sendMessage('warning', '切换运行模式前建议检查配置设备编码!', 300)
			// 调用弹窗
			toast(window.newEditorUi, 2)
			return true;
		})




		// var formatMenu = this.addMenu('',   mxResources.get('file') , true, 'file', null, true);
		// this.addDropDownArrow(formatMenu, 'geSprite-wenjian', 38, 50, -4, -3, 36, -8);
		// this.addSeparator();

		// var formatMenu = this.addMenu('',   mxResources.get('edit') , true, 'edit', null, true);
		// this.addDropDownArrow(formatMenu, 'geSprite-edit', 38, 50, -4, -3, 36, -8);
		// this.addSeparator();

		// var formatMenu = this.addMenu('',   mxResources.get('panTooltip') , true, 'viewPanels', null, true);
		// this.addDropDownArrow(formatMenu, 'geSprite-formatpanel', 38, 50, -4, -3, 36, -8);
		// this.addSeparator();
	}


	if (sw >= 420) {

		var elts = this.addItems(['-', 'undo', 'redo']);
		elts[1].setAttribute('title', mxResources.get('undo') + ' (' + this.editorUi.actions.get('undo').shortcut + ')');
		elts[2].setAttribute('title', mxResources.get('redo') + ' (' + this.editorUi.actions.get('redo').shortcut + ')');

		var elts = this.addItems(['-', 'copy', 'paste']);
		elts[1].setAttribute('title', mxResources.get('copy') + ' (' + this.editorUi.actions.get('copy').shortcut + ')');
		elts[2].setAttribute('title', mxResources.get('paste') + ' (' + this.editorUi.actions.get('paste').shortcut + ')');

		var elts = this.addItems(['delete']);
		elts[0].setAttribute('title', mxResources.get('delete') + ' (' + this.editorUi.actions.get('delete').shortcut + ')');
		// 剪贴板
		let ClipboardA = document.createElement("a")
		let ClipboardDiv = document.createElement("div")
		this.container.appendChild(ClipboardA)
		ClipboardA.appendChild(ClipboardDiv)
		ClipboardA.classList.add("geButton")
		ClipboardA.classList.add("toolbarA")
		ClipboardA.classList.add("Cross_pageCopyData")
		ClipboardA.classList.add("mxDisabled")


		ClipboardDiv.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTA2ODBGQUNCRjIxMTFFOUE1MkVENEExMDQyMDc3M0UiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTA2ODBGQUJCRjIxMTFFOUE1MkVENEExMDQyMDc3M0UiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkFBQTdEQTg1QjRGMjExRTlBMjAwQjM1RDREMTIxMzg3IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkFBQTdEQTg2QjRGMjExRTlBMjAwQjM1RDREMTIxMzg3Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+arZZPAAAARRJREFUeNq0lLENwjAQRWNkpWYCdsgW2QNECUukgA4aOprsAU0iNoEuKRDQpDBn9C0dlp3YiJz0JeS7e/k+YwulVPLvkL6EEKK3sc+MHPior7P3i5NkhJCBWxa2c15rj8LndE2aASYsuEBu1TtwI8QSjq6kzNGSIadrFjbjw3FAp6Qzmu6knAFzrOncCbVBUB0pqURzR5rDVYe1EjVJDNTMrwCEq7D/UjFQ4/jCgBfu8Bcon+0L+pql98AtqHaxJzWkJyA3nHiG33rtQWpRmw5Bd44ZHpihoyO/GYI21n1XWDPhzXOO80ZVVZXUdR30IoXcKNf2twzqzIceVMsPgkGdeRsqQrcW82jLmDGFxiiP9FuAAQCJ9EF0DXP5LQAAAABJRU5ErkJggg==)'
		ClipboardDiv.className = "toolbarDiv"
		ClipboardA.setAttribute('title', '剪贴板')
		ClipboardA.addEventListener('click', function (e) {
			if (localStorage.getItem('Cross_pageCopyDataEdge') || localStorage.getItem('Cross_pageCopyDataEdges')) {
				let dataEdge = JSON.parse(localStorage.getItem('Cross_pageCopyDataEdge'))
				let dataEdges = JSON.parse(localStorage.getItem('Cross_pageCopyDataEdges'))
				dataEdge.forEach(function (item) {
					if (item.edge) {
						// 线对象
						let cell = new mxCell(item.value, new mxGeometry(0, 0, item.geometry.width, item.geometry.height), item.style);
						cell.geometry.setTerminalPoint(new mxPoint(item.sourcePoint.x, item.sourcePoint.y), true);
						cell.geometry.setTerminalPoint(new mxPoint(item.targetPoint.x, item.targetPoint.y), false);
						if (item.points) {
							var points = []
							item.points.forEach(function (item) {
								points.push(
									new mxPoint(item.x, item.y)
								)
							})
						}
						cell.geometry.points = points
						cell.geometry.relative = true;
						cell.edge = true;
						graph.addCell(cell);
					}
				})
				let vertex = []
				let edges = []
				var cellIndex = {}
				dataEdges.forEach(function (item) {
					if (item.vertex) {
						vertex.push(item)
					} else if (item.edges) {
						edges.push(item)
					}
				})
				var parent = graph.getDefaultParent();
				graph.getModel().beginUpdate();
				try {
					vertex.forEach(function (item) {
						var cell = graph.insertVertex(parent, null, item.value, item.geometry.x, item.geometry.y, item.geometry.width, item.geometry.height, item.style);
						cellIndex[item._copyIndex] = cell
					})
					edges.forEach(function (item) {
						if (item.edges === 1) {
							var e1 = graph.insertEdge(parent, null, item.value, cellIndex[item.source], cellIndex[item.target], item.style)
						} else if (item.edges === 2) {
							let cell = new mxCell(item.value, new mxGeometry(0, 0, 0, 0), item.style);
							cell.geometry.setTerminalPoint(new mxPoint(item.targetPoint.x, item.targetPoint.y), false);
							cell.source = cellIndex[item.source]
							cell.geometry.relative = true;
							cell.edge = true;
							graph.addCell(cell);
						} else if (item.edges === 3) {
							let cell = new mxCell(item.value, new mxGeometry(0, 0, 0, 0), item.style);
							cell.geometry.setTerminalPoint(new mxPoint(item.sourcePoint.x, item.sourcePoint.y), true);
							cell.target = cellIndex[item.target]
							cell.geometry.relative = true;
							cell.edge = true;
							graph.addCell(cell);
						}
					})
				} finally {
					graph.getModel().endUpdate();
				}
			}
		})

		this.addSeparator();
		// 左对齐
		let a4 = document.createElement("a")
		let div4 = document.createElement("div")
		this.container.appendChild(a4)
		a4.appendChild(div4)
		a4.classList.add("geButton")
		a4.classList.add("toolbarA")
		a4.classList.add("alignShow")
		a4.classList.add("mxDisabled")
		div4.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MEE5MDQ2RDRCNEYyMTFFOTkyNzFFNzE3MTE0MkJDRjEiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MEE5MDQ2RDNCNEYyMTFFOTkyNzFFNzE3MTE0MkJDRjEiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+Tp2h8wAAAF1JREFUeNpi/P//PwO1ARMDDQBtDWVkZPwPwoPWpSzYBMlxMTDCGenvUmRHE+NIUg39P7gjCjnQh02SYkTyHcPAJSlcYUvIJ3gNJbcsGJxJCugb+riUcchUJwABBgBjjB03wqXVdwAAAABJRU5ErkJggg==)'
		div4.className = "toolbarDiv"
		a4.setAttribute('title', '左对齐')
		a4.addEventListener('click', function (e) {
			graph.alignCells(mxConstants.ALIGN_LEFT);
		})
		// 右对齐
		let a5 = document.createElement("a")
		let div5 = document.createElement("div")
		this.container.appendChild(a5)
		a5.appendChild(div5)
		a5.classList.add("geButton")
		a5.classList.add("toolbarA")
		a5.classList.add("alignShow")
		a5.classList.add("mxDisabled")
		div5.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MUNGQjE5QjVCNEYyMTFFOUI4QUNDRkNGMTUzNDAyRUMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MUNGQjE5QjRCNEYyMTFFOUI4QUNDRkNGMTUzNDAyRUMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+5rMHYgAAAGBJREFUeNrUlcEKACAIQ1v4/7+87NYhMCHNhI7pa24Eku129RZQNZsC4DzhpLJO9F7WJSNNU9nJdALpbcqSlhJL9Hqkaim3rs8tZZJYQREr1/UWtTz5NFW5pPjmOxkCDACLRh00OAYDTwAAAABJRU5ErkJggg==)'
		div5.className = "toolbarDiv"
		a5.setAttribute('title', '右对齐')
		a5.addEventListener('click', function (e) {
			graph.alignCells(mxConstants.ALIGN_RIGHT);
		})

		// 上对齐
		let a6 = document.createElement("a")
		let div6 = document.createElement("div")
		this.container.appendChild(a6)
		a6.appendChild(div6)
		a6.classList.add("geButton")
		a6.classList.add("toolbarA")
		a6.classList.add("alignShow")
		a6.classList.add("mxDisabled")
		div6.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6MzIzNUYxNEZCNEYyMTFFOUIyRTE5OUY5MDA0QkQyM0EiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6MzIzNUYxNEVCNEYyMTFFOUIyRTE5OUY5MDA0QkQyM0EiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+rVwxtQAAAFxJREFUeNpi/P//PwO1ARMDDQBNDGWBMRgZGSkKB2AwMtLUpYy0iCgWDFuwBAPMa/jkBjT2GYmJlKGdTkcNJSPHEMjStC1QiHUkpd7/T2RQgDHNvc84ZKoTgAADAFlXGiztb7HWAAAAAElFTkSuQmCC)'
		div6.className = "toolbarDiv"
		a6.setAttribute('title', '上对齐')
		a6.addEventListener('click', function (e) {
			graph.alignCells(mxConstants.ALIGN_TOP);
		})
		// 下对齐
		let a7 = document.createElement("a")
		let div7 = document.createElement("div")
		this.container.appendChild(a7)
		a7.appendChild(div7)
		a7.classList.add("geButton")
		a7.classList.add("toolbarA")
		a7.classList.add("alignShow")
		a7.classList.add("mxDisabled")
		div7.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NDY4OUFBNjRCNEYyMTFFOTk3NThCQTgzNkQ0RTIzNjAiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NDY4OUFBNjNCNEYyMTFFOTk3NThCQTgzNkQ0RTIzNjAiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+gG9pswAAAGJJREFUeNpi/P//PwO1ARMDDQBNDGVBF2BkZMQID2AQMULlcBqEHIz0cSmyo0EOIGQAtohmIdER/3FYPkhjHxZxQz+djhpKUprEmZVp7lJGWpSnLMR6iZQMMXS8TxOXAgQYAAK/HiNjccknAAAAAElFTkSuQmCC)'
		div7.className = "toolbarDiv"
		a7.setAttribute('title', '下对齐')
		a7.addEventListener('click', function (e) {
			graph.alignCells(mxConstants.ALIGN_BOTTOM);
		})


		// 水平对齐
		let a13 = document.createElement("a")
		let div13 = document.createElement("div")
		this.container.appendChild(a13)
		a13.appendChild(div13)
		a13.classList.add("geButton")
		a13.classList.add("toolbarA")
		a13.classList.add("alignShow")
		a13.classList.add("mxDisabled")
		div13.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NjM0QTE5RTBCNEYyMTFFOTg4RThFNzZDNkVDNDMzNDMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NjM0QTE5REZCNEYyMTFFOTg4RThFNzZDNkVDNDMzNDMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+YAvrrwAAAHJJREFUeNpi/P//PwO1ARMDDcDAGcrIyPgfhHHIUd+l2OKEEasgDldhMZCRbmHKQo5LCPmEhVAEDZokxUhMjoK5GFdw0N+lQBd9BFJ8ZKRVRnwuZadFkmInJuyQsuZ/opMUuUmJmhH1aZgkqUFd8gMEGABe5DIjmiSbBQAAAABJRU5ErkJggg==)'
		div13.className = "toolbarDiv"
		a13.setAttribute('title', '水平对齐')
		a13.addEventListener('click', function (e) {
			graph.alignCells(mxConstants.ALIGN_CENTER);
		})
		// 垂直对齐
		let a12 = document.createElement("a")
		let div12 = document.createElement("div")
		this.container.appendChild(a12)
		a12.appendChild(div12)
		a12.classList.add("geButton")
		a12.classList.add("toolbarA")
		a12.classList.add("alignShow")
		a12.classList.add("mxDisabled")
		div12.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6ODZFQzc2NUVCNEYyMTFFOTk2OUNCOUQ5QkQxOUM0RTYiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6ODZFQzc2NURCNEYyMTFFOTk2OUNCOUQ5QkQxOUM0RTYiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+ZHFp6gAAAHNJREFUeNpi/P//PwO1ARMDDcDQMZQFXYCRkRE5kH+CMDDc+UkxlBE9otAMBQOgGkaKXIpsELoFxFo4eCKKUHDQNvaxhRcpcsiup61LYTbhi+HBGftAVzGQE8b0y/vIwUVudsXm0k+kugw9qBhHdskPEGAAIOU1IKD4WTUAAAAASUVORK5CYII=)'
		div12.className = "toolbarDiv"
		a12.setAttribute('title', '垂直对齐')
		a12.addEventListener('click', function (e) {
			graph.alignCells(mxConstants.ALIGN_MIDDLE);
		})

		this.addSeparator();
		// 水平分布
		let a8 = document.createElement("a")
		let div8 = document.createElement("div")
		this.container.appendChild(a8)
		a8.appendChild(div8)
		a8.classList.add("geButton")
		a8.classList.add("toolbarA")
		a8.classList.add("alignShow")
		a8.classList.add("mxDisabled")
		div8.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OUJEMjFGRjVCNEYyMTFFOTg1QUFERjQwRjU1MzM0NDAiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OUJEMjFGRjRCNEYyMTFFOTg1QUFERjQwRjU1MzM0NDAiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+PmyDBgAAAF1JREFUeNpi/P//PwO1ARMDDQBNDGWBMRgZGcHhAAwORmQFQHEUDejBhU0fCyFbyQlzvIbCXIFNCp9lAxdRoPBCD+vhkaRGDR1qeZ/MnDV4XMo4aMKUcchUJwABBgD8Nh8pb5BFbwAAAABJRU5ErkJggg==)'
		div8.className = "toolbarDiv"
		a8.setAttribute('title', '水平分布')
		a8.addEventListener('click', function (e) {
			graph.distributeCells(true);
		})

		// 垂直分布
		let a9 = document.createElement("a")
		let div9 = document.createElement("div")
		this.container.appendChild(a9)
		a9.appendChild(div9)
		a9.classList.add("geButton")
		a9.classList.add("toolbarA")
		a9.classList.add("alignShow")
		a9.classList.add("mxDisabled")
		div9.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QUFBN0RBODZCNEYyMTFFOUEyMDBCMzVENEQxMjEzODciIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QUFBN0RBODVCNEYyMTFFOUEyMDBCMzVENEQxMjEzODciIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+9iosYwAAAGBJREFUeNrslMEKACAIQ134/79sEQURCoHz1k6dxnzOYGbCVpMClZjqfgBIcRgYUZoUf1FyLGsyzyKkJF1h/KRRRQIje2LKaoJGY2SOgcL0ntBjCmpPS5iyjP8vxVcXYAClOyYq1V10DAAAAABJRU5ErkJggg==)'
		div9.className = "toolbarDiv"
		a9.setAttribute('title', '垂直分布')
		a9.addEventListener('click', function (e) {
			graph.distributeCells(false);
		})


		this.addSeparator();
		var formatMenu = this.addMenu('', mxResources.get('arrange'), true, 'arrange', null, true);
		this.addDropDownArrow(formatMenu, 'geSprite-zuHe', 38, 50, -4, -3, 36, -8);
		this.addSeparator();
		// var formatMenu = this.addMenu('',   mxResources.get('align') , true, 'align', null, true);
		// this.addDropDownArrow(formatMenu, 'geSprite-duiQi', 38, 50, -4, -3, 36, -8);						

		// var formatMenu = this.addMenu('',   mxResources.get('distribute') , true, 'distribute', null, true);
		// this.addDropDownArrow(formatMenu, 'geSprite-fenbu', 38, 50, -4, -3, 36, -8);

	}
	// this.addSeparator();

	// Updates the label if the scale changes  如果比例发生变化，则更新标签




	// if (sw >= 550) {
	// this.addItems(['-', 'toFront', 'toBack']);
	// }
	// if (sw >= 740) {
	// 	// this.addItems(['-', 'fillColor']);

	// 	if (sw >= 780) {
	// 		// this.addItems(['strokeColor']);

	// 		if (sw >= 820) {
	// 			// this.addItems(['shadow']);
	// 		}
	// 	}
	// }

	if (sw >= 440) {
		this.edgeShapeMenu = this.addMenuFunction('', mxResources.get('connection'), false, mxUtils.bind(this, function (menu) {
			this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_SHAPE, 'width'], [null, null], 'geIcon geSprite geSprite-connection', null, true).setAttribute('title', mxResources.get('line'));
			this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_SHAPE, 'width'], ['link', null], 'geIcon geSprite geSprite-linkedge', null, true).setAttribute('title', mxResources.get('link'));
			this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_SHAPE, 'width'], ['flexArrow', null], 'geIcon geSprite geSprite-arrow', null, true).setAttribute('title', mxResources.get('arrow'));
			this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_SHAPE, 'width'], ['arrow', null], 'geIcon geSprite geSprite-simplearrow', null, true).setAttribute('title', mxResources.get('simpleArrow'));
		}));

		this.addDropDownArrow(this.edgeShapeMenu, 'geSprite-connection', 44, 50, 0, 0, 22, -4);
	}

	this.edgeStyleMenu = this.addMenuFunction('geSprite-orthogonal', mxResources.get('orthogonal'), false, mxUtils.bind(this, function (menu) {
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], [null, null, null], 'geIcon geSprite geSprite-straight', null, true).setAttribute('title', mxResources.get('straight'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['orthogonalEdgeStyle', null, null], 'geIcon geSprite geSprite-orthogonal', null, true).setAttribute('title', mxResources.get('orthogonal'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['elbowEdgeStyle', null, null, null], 'geIcon geSprite geSprite-horizontalelbow', null, true).setAttribute('title', mxResources.get('simple'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['elbowEdgeStyle', 'vertical', null, null], 'geIcon geSprite geSprite-verticalelbow', null, true).setAttribute('title', mxResources.get('simple'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['isometricEdgeStyle', null, null, null], 'geIcon geSprite geSprite-horizontalisometric', null, true).setAttribute('title', mxResources.get('isometric'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_ELBOW, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['isometricEdgeStyle', 'vertical', null, null], 'geIcon geSprite geSprite-verticalisometric', null, true).setAttribute('title', mxResources.get('isometric'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['orthogonalEdgeStyle', '1', null], 'geIcon geSprite geSprite-curved', null, true).setAttribute('title', mxResources.get('curved'));
		this.editorUi.menus.edgeStyleChange(menu, '', [mxConstants.STYLE_EDGE, mxConstants.STYLE_CURVED, mxConstants.STYLE_NOEDGESTYLE], ['entityRelationEdgeStyle', null, null], 'geIcon geSprite geSprite-entity', null, true).setAttribute('title', mxResources.get('entityRelation'));
	}));

	this.addDropDownArrow(this.edgeStyleMenu, 'geSprite-orthogonal', 44, 50, 0, 0, 22, -4);

	this.addSeparator();

	var elts = this.addItems(['zoomIn']);
	elts[0].setAttribute('title', mxResources.get('zoomIn') + ' (' + this.editorUi.actions.get('zoomIn').shortcut + ')');


	this.updateZoom = mxUtils.bind(this, function () {
		viewMenu.innerHTML = Math.round(this.editorUi.editor.graph.view.scale * 100) + '%' +
			this.dropdownImageHtml;

		if (EditorUi.compactUi) {
			viewMenu.getElementsByTagName('img')[0].style.right = '1px';
			viewMenu.getElementsByTagName('img')[0].style.top = '5px';
		}
	});

	this.editorUi.editor.graph.view.addListener(mxEvent.EVENT_SCALE, this.updateZoom);
	this.editorUi.editor.addListener('resetGraphView', this.updateZoom);

	var viewMenu = this.addMenu('', mxResources.get('zoom') + ' (Alt+Mousewheel)', true, 'viewZoom', null, true);
	viewMenu.showDisabled = true;
	viewMenu.style.whiteSpace = 'nowrap';
	viewMenu.style.position = 'relative';
	viewMenu.style.overflow = 'hidden';

	if (EditorUi.compactUi) {
		viewMenu.style.width = (mxClient.IS_QUIRKS) ? '58px' : '50px';
	} else {
		viewMenu.style.width = (mxClient.IS_QUIRKS) ? '62px' : '36px';
	}
	var elts = this.addItems(['zoomOut']);
	elts[0].setAttribute('title', mxResources.get('zoomOut') + ' (' + this.editorUi.actions.get('zoomOut').shortcut + ')');


	this.addSeparator();
	// 导入
	let a10 = document.createElement("a")
	let div10 = document.createElement("div")
	this.container.appendChild(a10)
	a10.appendChild(div10)
	a10.classList.add("geButton")
	a10.classList.add("toolbarA")
	div10.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTg3MzIzOEFCNEYzMTFFOThDMzlBNDlENTE2NkYwREMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTg3MzIzODlCNEYzMTFFOThDMzlBNDlENTE2NkYwREMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+pyiB4gAAAL1JREFUeNrUlFEKgzAMhs3q6bzBPNceN0dfPNlgXkKyVBKIIa0KOjTwQ6HJB/mbFBCx2jtu1QFxCLSWAwCU8sSjbJK28TrtXxvakp6kUKhLdy/SPftqZgE6fu23AqOagMB3yLkzzsRyoKkoGrBANTDqbpagHhjNOVp71kAtWCt6fueg2jcNFo9HPgdn23ALVKInPQorPIOCtE67v7jfpX+BOPC34f+amVyrFENu+BsGb4V+Uq3r6ek/lJ8AAwAZ9b3sWCSZPwAAAABJRU5ErkJggg==)'
	div10.className = "toolbarDiv"
	a10.setAttribute('title', '导入')

	var fileImage = document.createElement('input')
	fileImage.id = 'file'
	fileImage.setAttribute('type', 'file')
	fileImage.setAttribute('title', '')
	fileImage.style.width = '100%'
	fileImage.style.display = 'none'
	fileImage.addEventListener('change', function (e) {
		var str = e.path[0].value
		if (str.substring(str.length - 3) === 'xml') {
			var x = new FileReader;
			x.readAsText(e.target.files[0], "UTF-8")
			x.onloadend = function (ev) {
				var xml = x.result
				var xmlData = newEditorUi.editor.graph.zapGremlins(mxUtils.trim(xml));
				newEditorUi.editor.graph.model.beginUpdate();
				try {
					newEditorUi.editor.setGraphXml(mxUtils.parseXml(xmlData).documentElement);
				} catch (e) {
					error = e;
				} finally {
					newEditorUi.editor.graph.model.endUpdate();
				}
			}
		} else {
			alert('必须为xml格式文件')
			return false
		}
	})

	a10.addEventListener('click', function (e) {
		// window.openNew = false;
		// window.openKey = 'import';
		// window.openFile = new OpenFile(mxUtils.bind(this, function () {
		// 	ui.hideDialog();
		// }));

		// window.openFile.setConsumer(mxUtils.bind(this, function (xml, filename) {
		// 	try {
		// 		var doc = mxUtils.parseXml(xml);
		// 		// editor.graph.setSelectionCells(editor.graph.importGraphModel(doc.documentElement));
		// 		editor.setGraphXml(doc.documentElement);
		// 	} catch (e) {
		// 		mxUtils.alert(mxResources.get('invalidOrMissingFile') + ': ' + e.message);
		// 	}
		// }));
		// // Removes openFile if dialog is closed
		// ui.showDialog(new OpenDialog(this).container, 320, 220, true, true, function () {
		// 	window.openFile = null;
		// });
		// 自己写导入
		fileImage.click()

	})
	// 导出
	let a11 = document.createElement("a")
	let div11 = document.createElement("div")
	this.container.appendChild(a11)
	a11.appendChild(div11)
	a11.classList.add("geButton")
	a11.classList.add("toolbarA")
	div11.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NERCQjNFMEFCNEYzMTFFOUE4NkRBODRDNTE3MTNEMkUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NERCQjNFMDlCNEYzMTFFOUE4NkRBODRDNTE3MTNEMkUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+zNkXsQAAALlJREFUeNpi/P//PwO1ARMDDQBNDGUAeZ+IIJgExG3EmAM2i4ChzEA8D6QHiudBxcg2FKR5IZKBMLwQm8HEGIps4AIkAxfgMpiQocxImhdA+TBDsckRZehcLJpghqJbOheboSxYwnIfVEM6EP/FIg8SS4bSe7FFBiPMlYyMjHhTDEw9viQ19HIUE5oXySldMPTRxKUsRKpjHFRh+hwtjIjFIPACV9HnBTWYVEOfgPQi5yjGIVOdAAQYANUauR5Di660AAAAAElFTkSuQmCC)'
	div11.className = "toolbarDiv"
	a11.setAttribute('title', '导出')
	a11.addEventListener('click', function (e) {
		var xml = mxUtils.getXml(editor.getGraphXml());
		download('data.xml', xml)
	})

	function download(filename, text) {
		var element = document.createElement('a');
		element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
		element.setAttribute('download', filename);
		element.style.display = 'none';
		document.body.appendChild(element);
		let blob = new Blob([text]);
		element.href = URL.createObjectURL(blob);
		element.click();
		document.body.removeChild(element);
	}
	// excel
	// let excelA = document.createElement("a")
	// let excelDiv = document.createElement("div")
	// this.container.appendChild(excelA)
	// excelA.appendChild(excelDiv)
	// excelA.style.opacity = '1'
	// excelA.classList.add("geButton")
	// excelA.classList.add("toolbarA")
	// excelDiv.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAD4ElEQVR4Xu2bwa4NQRCGf09AYmGJxM4CsZXgDVha8Qg8AZZWeAPe4N4nwIKVBIk9dhYSJBZ25Eu6TtqYnq6emTPTfc+pZOIe90zfrr//+ruqeuaYdtyOrez/CUlXJV0LF5+vS/q81LyWBqDr8MUeRwHg5VEBwOPwh+DwDUmnAwOaBaDEYZzk+hFWm58Jh6YY4HH4S3AUBw8ih7ssNwDeD3xn7sg4LNWAUodxyitoBsDcTg6OlwNgmw6nGPA8A9oZSbcl/Zb0aCRaNoa6ACzpcAqAnAawZb4IN+cWMIXPZgwGYCsCUf6zb1uKY7iE0qWL4xXB2QH4LomVN/sZiRaTQpSWsNUA+BO8eyrp2YIOVxMCBkAu9rbNgtUZUAsAdzMsRKeehNVAD8bYZgxEsBYGfJN0cow3U+6pCYCvkk5NcWbMvTUBsNcAZzE0ex5QiwbsGVA7A8gW70uiWPFmh2w3j8M9JFlDVj0DLPYoby85a/Z3ob4AtDutA8D8WfkLkh5KepBxiISG1cfOOvoCxgCYMtRDoJQ1MHNzSE1xM0bpNhgr8JBThMunUGR5wIrBHbOdj76nFAD+ECtE+cyKkT73mX2HUhodsL6fRwO8DRHGAtwx9k9DpHQbZHWh6HFJN0OfL55EzJK+36cmXL0IxhO3+O4TRBO+V6HJ4l2hpgBICWKp8MXgNAdAVxCJ81LhaxqAriAieIhjifA1D0AsiOZMifA1DwAOxHH/WtIVr+p1vrfawUjpNtj1LwaAxiqfx1iTAMQZnzntSXv7AGpuF4hFEOF7I+lWJkP0ZIK55mw1DZFuxmenRqkMMRcWzTGgL+MbyhCPFABDGV9JydzkNpgrdb0lc5cRzYSAp9T1lMxdAIw5zTREhjK+XMncpwfUEgjoolbaECkpdUsF8aOk86GBmmuJUXNgizZExsS20fpedKCZWuHqNQBaE59cPO3lMdphNC65cq306gHwODzlO3sAaj8ZmrK6nnv3DNh1Buz8AxI7/4iMaQA5w9CWaSfO6ErqZCqnOZsxSjPB3MBTfr8XwV0XwdUZQPFy6DjHn0LzKnuC3TKUz4iQXVRmHHRu21ZjAFUeq8+ZOU9/pCwGhMnyspPn3N8L3FtJl0OxlSuHZ31CpDtBAAEMtgouXmRKGRPlssfqAcX7ikx3zNUaIp4VigExgHjFLWUGCGDAHE8IWUPEw6xzkn5JInucYgdjXznhj9IfgCExY3IhZID0hZBXA6Y4/N+9UwBITcRCJ/431eszwcV54hqm5U6Gqgegb4JxCBkwqRA6kgCkVs1e1AIUQDKg5txdBhmzjRCYlaLbHuwveDjJ/Pusi2YAAAAASUVORK5CYII=)'
	// excelDiv.className = "toolbarDiv"
	// excelA.setAttribute('title', '导入Excel')
	// excelA.addEventListener('click', function (e) {
	// 	window.parent.postMessage({
	// 		cmd: 'uploadExcel',
	// 		params: {
	// 			success: true
	// 		}
	// 	}, '*');
	// })
	// fullscreen
	let a = document.createElement("a")
	let div = document.createElement("div")
	this.container.appendChild(a)
	a.appendChild(div)
	a.classList.add("geButton")
	a.classList.add("toolbarA")
	div.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABUAAAAVCAYAAACpF6WWAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyFpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTQyIDc5LjE2MDkyNCwgMjAxNy8wNy8xMy0wMTowNjozOSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NkQ2NzA4NzVCNEYzMTFFOUI3RkFFMjZBNUJFOTBDMTMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NkQ2NzA4NzRCNEYzMTFFOUI3RkFFMjZBNUJFOTBDMTMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjBDMTI4NTUzQUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjBDMTI4NTU0QUU4NDExRTk4MzRDRkREQUM5ODc4NDQzIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+EdAARwAAAR1JREFUeNrMlEEOgjAQRanxBMAKNoRTsHEDK72hW88BboBwCndG3XCG2o+dpkKr0Gi0yZDJ7/S1nR/KOOfep8fK+8L4CtTD9WULtiIukBxCcQaWBnUFTqCMjGKMkWNsrnlizdONX/YUGyDCMCzbtuUiBh05NJp3MipN0xP20PeT2uR0NqNUf8Q4JklyiKKIZ1nWNU0z1CCHhjnU6FCbUQoqIBygPM9r3/c51SCHhpxqTNC1zQN8qqra6GLf92xco92O/eSPGnYviqIOgkA1Djm00QmXGSUCkI5qkEMjo2RP+QPzxijK4zje68UIaDTvBBVRksvkODQdZIIaf9Olb5Jso9H96+iUsx8TMW42o3YSvBR6xlrj9f/+5b8LMADRzF+zZy5/9gAAAABJRU5ErkJggg==)'
	div.className = "toolbarDiv"
	a.setAttribute('title', '预览')
	a.addEventListener('click', function (e) {
		// alert('你点击了全屏')
		window.parent.postMessage({
			cmd: 'fullScreen',
			params: {
				success: true
			}
		}, '*');
	})
	// Check the connection

	let CheckConnectionA = document.createElement("a")
	let CheckConnectionDiv = document.createElement("div")
	this.container.appendChild(CheckConnectionA)
	CheckConnectionA.appendChild(CheckConnectionDiv)
	CheckConnectionA.classList.add("geButton")
	CheckConnectionA.classList.add("toolbarA")
	CheckConnectionDiv.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACr0lEQVRYR8WW3VEVQRCFDxEAEQgRqGUASgRoBMIjT2IESAQKCYARIBGAEYgRAA88owFYUp/Vx2qH+dmVqstUbd2tvTPdX5/u6ZkljceKpE1JryXx/iqWXEniuZD0OX7H1ooZS50Va5L2JG1NtArMe0lfJs7/M60FgNOPETHzTsOwI/4hCUAelOF5Eo7PJb2RxJzhqAEcS3obK3G8G1KPjAH9SdJyzAeC9HRHCYCBd7FiWxIwcwY1ggJPA+L5SIkMgIwn4W1D0rOIZg4Ac4FGOSBQAIjmMADkl5FzIifXSI8ic4Z3COtwTl10lTTAh6j4r7HNMMTD9ymDAKib72kd688iBeutVBiAiqV4kJ4cGoBIkLI1rsMwarEjkD+DY+tlTwUAyPU3SRjDCMMAGOz1AWSmVxwk5xmAtUdRE9TYvQGA5ccIkWSAmgI/I7/IjsTIbsgydcy5DZVWWwDe93QxF50N0dXKQiRqoEvnGTzXzu9wXG16fHSenP+WIQdQi9z/1YrX9tmO9xrTCCB3RZxw6BxKupG0U5G0B5AD/LsUAHe/Wgr4jyL1+CXpRac/1ACGKegV4dQ+0EqBi5DC5b26C7wN6X40jFwDtSLMRkhRPi9KBSZtQwzinLZZNqKHKuACzOn9R4WyFbMAiLmtuJYCt2Lkp8FV7wf5MEIF2jGHB9uFAmSvzxk4ojGRFtaS3v3emdI7jiGuFs6ACBUtvQ+35pKyO3lHsOB/LySOHBu03+7VrNYe860IKZGQ9IwGFc/B5AON+awnkMkKeGK+3/GN7cgDCLIycMTO8aXUjrkNkQIutUOI3rWc/KOGL6gjBSg+UuhruXtAF6IHYIeAOErefVfEIfklWpzWbsBDiCkAo8hH/3chFgEAYBNiUQBNiEUCVCEWDVBC7D8GgCE4rLbuAJ63uKGewMWMAAAAAElFTkSuQmCC)'
	CheckConnectionDiv.className = "toolbarDiv"
	CheckConnectionA.setAttribute('title', '检查连接')
	CheckConnectionA.addEventListener('click', CheckConnection)


	// svg-png
	let ax = document.createElement("a")
	let divx = document.createElement("div")
	this.container.appendChild(ax)
	ax.appendChild(divx)
	ax.classList.add("geButton")
	ax.classList.add("toolbarA")
	divx.style.backgroundImage = 'url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAACT0lEQVRYR+2WgTEEQRBFvwgQASJABIgAESACRIAIEAEiQARk4ESACBAB9bamVddc997uca5K6Sp3bqe3+8/v3z0zoynbzJTz608BmJO0KelA0oqkDUn3oxj+CQbWJe1I2q2SXQXPhvCMC2DRJeV/s9uy69PyYF7SWxsLfQGwS2jeckFfJF2Wv+fy/Kb47ZXnKYYuAKinUUydzaCYxFGdAXohaSBpdRwGakFZjEdJZ5LYYSu1ZX22AABIaDUDkaDeHcVpoCA6QPclnZfOSAFkgiIxO5iEUTbatBlEUGmJvKCo79okspeYDft8QOtyQBUoAbAkydT9XTxo67UGYIqlh317GYAundIH2EcNwKPyu40AMGaZA9aCfRKb7xAAFqg3vX4i6bh41gCYBw8uI/0ddYVNxqxsIQDa767UGhawGgDluXYAssOG90jSqDywEAB+IF5wp1hUAmMq621KZOcAY5juolx+aKUAoP6ovIAw+4qwLhFzhIlJXF+OFAC1eyqUcYrxMm3YtQusnQnhtVRXgTzEBvBQcH+KwUIGgJcZs4eFXmOPmJk4Q0HUu/OnGBRGAGhbBAsIasvMt84BEL8zYw2dfFlEr41mvknmfXzyOglj3F9Osg5AW9smzAiAnWIWwHzakuNrnYEfoz0yuzsgSkAMIgC1ms2H3bXtEBHCms2Ulko0S/geZgr3iu7aBZawKwD8r7LgJsZRu/DrNhlhqb4hmx9zxqy5L2YA/AHVFUTrzacEYQjRXbDUnCN96e0KJvMjKcm/RvNvAxgC9g/gE3D/iCHGIVwSAAAAAElFTkSuQmCC)'
	divx.className = "toolbarDiv"
	ax.setAttribute('title', '导出图片')
	ax.addEventListener('click', function (e) {
		SendDialog('请选择导出图片的像素比例!', 270)
	})
	// 保存
	var elts = this.addItems(['save']);
	elts[0].setAttribute('title', mxResources.get('save') + ' (' + this.editorUi.actions.get('save').shortcut + ')');

	var formatMenu = this.addMenu('', mxResources.get('extras'), true, 'extras', null, true);
	this.addDropDownArrow(formatMenu, 'geSprite-qiTa', 38, 50, -4, -3, 36, -8);
	this.addSeparator();

	// 切换面板
	let FormattingPanela = document.createElement("a")
	let FormattingPanelaDiv = document.createElement("div")
	this.container.appendChild(FormattingPanela)
	FormattingPanela.appendChild(FormattingPanelaDiv)
	FormattingPanela.classList.add("geButton")
	FormattingPanela.classList.add("FormattingPanelA")
	FormattingPanela.classList.add("toolbarA")
	FormattingPanelaDiv.className = "FormattingPanel"
	FormattingPanela.setAttribute('title', '展开面板(ctrl+Shift+P)')
	FormattingPanela.addEventListener('click', function (e) {
		ui.toggleFormatPanel()
	})
};

/**
 * Adds the toolbar elements. 添加工具栏元素。
 */
Toolbar.prototype.addDropDownArrow = function (menu, sprite, width, atlasWidth, left, top, atlasDelta, atlasLeft) {
	atlasDelta = (atlasDelta != null) ? atlasDelta : 32;
	left = (EditorUi.compactUi) ? left : atlasLeft;

	menu.style.whiteSpace = 'nowrap';
	menu.style.overflow = 'hidden';
	menu.style.position = 'relative';
	menu.innerHTML = '<div class="geSprite ' + sprite + '" style="margin-left:' + left + 'px;margin-top:' + top + 'px;"></div>' +
		this.dropdownImageHtml;
	menu.style.width = (mxClient.IS_QUIRKS) ? atlasWidth + 'px' : (atlasWidth - atlasDelta) + 'px';

	if (mxClient.IS_QUIRKS) {
		menu.style.height = (EditorUi.compactUi) ? '24px' : '26px';
	}

	// Fix for item size in kennedy theme
	if (EditorUi.compactUi) {
		menu.getElementsByTagName('img')[0].style.left = '24px';
		menu.getElementsByTagName('img')[0].style.top = '5px';
		menu.style.width = (mxClient.IS_QUIRKS) ? width + 'px' : (width - 10) + 'px';
	}
};

/**
 * Sets the current font name.
 */
Toolbar.prototype.setFontName = function (value) {
	if (this.fontMenu != null) {
		this.fontMenu.innerHTML = '<div style="width:60px;overflow:hidden;display:inline-block;">' +
			mxUtils.htmlEntities(value) + '</div>' + this.dropdownImageHtml;
	}
};

/**
 * Sets the current font name.
 */
Toolbar.prototype.setFontSize = function (value) {
	if (this.sizeMenu != null) {
		this.sizeMenu.innerHTML = '<div style="width:24px;overflow:hidden;display:inline-block;">' +
			value + '</div>' + this.dropdownImageHtml;
	}
};

/**
 * Hides the current menu. 隐藏当前菜单。
 */
Toolbar.prototype.createTextToolbar = function () {
	var graph = this.editorUi.editor.graph;

	var styleElt = this.addMenu('', mxResources.get('style'), true, 'formatBlock');
	styleElt.style.position = 'relative';
	styleElt.style.whiteSpace = 'nowrap';
	styleElt.style.overflow = 'hidden';
	styleElt.style.height = '22px'
	styleElt.className += ' toolFont'
	styleElt.innerHTML = mxResources.get('style') + this.dropdownImageHtml;

	if (EditorUi.compactUi) {
		styleElt.style.paddingRight = '18px';
		styleElt.getElementsByTagName('img')[0].style.right = '1px';
		styleElt.getElementsByTagName('img')[0].style.top = '5px';
	}

	this.addSeparator();

	this.fontMenu = this.addMenu('', mxResources.get('fontFamily'), true, 'fontFamily');
	this.fontMenu.style.position = 'relative';
	this.fontMenu.style.whiteSpace = 'nowrap';
	this.fontMenu.style.overflow = 'hidden';
	this.fontMenu.style.width = (mxClient.IS_QUIRKS) ? '80px' : '60px';

	this.setFontName(Menus.prototype.defaultFont);

	if (EditorUi.compactUi) {
		this.fontMenu.style.paddingRight = '18px';
		this.fontMenu.getElementsByTagName('img')[0].style.right = '1px';
		this.fontMenu.getElementsByTagName('img')[0].style.top = '5px';
	}

	this.addSeparator();

	this.sizeMenu = this.addMenu(Menus.prototype.defaultFontSize, mxResources.get('fontSize'), true, 'fontSize');
	this.sizeMenu.style.position = 'relative';
	this.sizeMenu.style.whiteSpace = 'nowrap';
	this.sizeMenu.style.overflow = 'hidden';
	this.sizeMenu.style.width = (mxClient.IS_QUIRKS) ? '44px' : '24px';

	this.setFontSize(Menus.prototype.defaultFontSize);

	if (EditorUi.compactUi) {
		this.sizeMenu.style.paddingRight = '18px';
		this.sizeMenu.getElementsByTagName('img')[0].style.right = '1px';
		this.sizeMenu.getElementsByTagName('img')[0].style.top = '5px';
	}

	var elts = this.addItems(['-', 'undo', 'redo', '-', 'bold', 'italic', 'underline']);
	elts[1].setAttribute('title', mxResources.get('undo') + ' (' + this.editorUi.actions.get('undo').shortcut + ')');
	elts[2].setAttribute('title', mxResources.get('redo') + ' (' + this.editorUi.actions.get('redo').shortcut + ')');
	elts[4].setAttribute('title', mxResources.get('bold') + ' (' + this.editorUi.actions.get('bold').shortcut + ')');
	elts[5].setAttribute('title', mxResources.get('italic') + ' (' + this.editorUi.actions.get('italic').shortcut + ')');
	elts[6].setAttribute('title', mxResources.get('underline') + ' (' + this.editorUi.actions.get('underline').shortcut + ')');

	elts[1].style.height = '22px'
	elts[2].style.height = '22px'
	elts[4].style.height = '22px'
	elts[5].style.height = '22px'
	elts[6].style.height = '22px'

	// KNOWN: Lost focus after click on submenu with text (not icon) in quirks and IE8. This is because the TD seems
	// to catch the focus on click in these browsers. NOTE: Workaround in mxPopupMenu for icon items (without text).
	var alignMenu = this.addMenuFunction('', mxResources.get('align'), false, mxUtils.bind(this, function (menu) {
		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('justifyleft', false, null);
		}), null, 'geIcon geSprite geSprite-left');
		elt.setAttribute('title', mxResources.get('left'));
		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('justifycenter', false, null);
		}), null, 'geIcon geSprite geSprite-center');
		elt.setAttribute('title', mxResources.get('center'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('justifyright', false, null);
		}), null, 'geIcon geSprite geSprite-right');
		elt.setAttribute('title', mxResources.get('right'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('justifyfull', false, null);
		}), null, 'geIcon geSprite geSprite-justifyfull');
		elt.setAttribute('title', mxResources.get('justifyfull'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('insertorderedlist', false, null);
		}), null, 'geIcon geSprite geSprite-orderedlist');
		elt.setAttribute('title', mxResources.get('numberedList'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('insertunorderedlist', false, null);
		}), null, 'geIcon geSprite geSprite-unorderedlist');
		elt.setAttribute('title', mxResources.get('bulletedList'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('outdent', false, null);
		}), null, 'geIcon geSprite geSprite-outdent');
		elt.setAttribute('title', mxResources.get('decreaseIndent'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('indent', false, null);
		}), null, 'geIcon geSprite geSprite-indent');
		elt.setAttribute('title', mxResources.get('increaseIndent'));
	}));

	alignMenu.style.position = 'relative';
	alignMenu.style.whiteSpace = 'nowrap';
	alignMenu.style.overflow = 'hidden';
	alignMenu.innerHTML = '<div class="geSprite geSprite-left" style="margin-left:-2px;"></div>' + this.dropdownImageHtml;
	alignMenu.style.width = (mxClient.IS_QUIRKS) ? '50px' : '30px';
	alignMenu.style.height = '22px'
	if (EditorUi.compactUi) {
		alignMenu.getElementsByTagName('img')[0].style.left = '22px';
		alignMenu.getElementsByTagName('img')[0].style.top = '5px';
	}

	var formatMenu = this.addMenuFunction('', mxResources.get('format'), false, mxUtils.bind(this, function (menu) {
		elt = menu.addItem('', null, this.editorUi.actions.get('subscript').funct,
			null, 'geIcon geSprite geSprite-subscript');
		elt.setAttribute('title', mxResources.get('subscript') + ' (' + Editor.ctrlKey + '+,)');

		elt = menu.addItem('', null, this.editorUi.actions.get('superscript').funct,
			null, 'geIcon geSprite geSprite-superscript');
		elt.setAttribute('title', mxResources.get('superscript') + ' (' + Editor.ctrlKey + '+.)');

		// KNOWN: IE+FF don't return keyboard focus after color dialog (calling focus doesn't help)
		elt = menu.addItem('', null, this.editorUi.actions.get('fontColor').funct,
			null, 'geIcon geSprite geSprite-fontcolor');
		elt.setAttribute('title', mxResources.get('fontColor'));

		elt = menu.addItem('', null, this.editorUi.actions.get('backgroundColor').funct,
			null, 'geIcon geSprite geSprite-fontbackground');
		elt.setAttribute('title', mxResources.get('backgroundColor'));

		elt = menu.addItem('', null, mxUtils.bind(this, function () {
			document.execCommand('removeformat', false, null);
		}), null, 'geIcon geSprite geSprite-removeformat');
		elt.setAttribute('title', mxResources.get('removeFormat'));
	}));

	formatMenu.style.position = 'relative';
	formatMenu.style.whiteSpace = 'nowrap';
	formatMenu.style.overflow = 'hidden';
	formatMenu.innerHTML = '<div class="geSprite geSprite-dots" style="margin-left:-2px;"></div>' +
		this.dropdownImageHtml;
	formatMenu.style.width = (mxClient.IS_QUIRKS) ? '50px' : '30px';
	formatMenu.style.height = '22px'

	if (EditorUi.compactUi) {
		formatMenu.getElementsByTagName('img')[0].style.left = '22px';
		formatMenu.getElementsByTagName('img')[0].style.top = '5px';
	}

	this.addSeparator();

	this.addButton('geIcon geSprite geSprite-code', mxResources.get('html'), function () {
		graph.cellEditor.toggleViewMode();

		if (graph.cellEditor.textarea.innerHTML.length > 0 && (graph.cellEditor.textarea.innerHTML != '&nbsp;' || !graph.cellEditor.clearOnChange)) {
			window.setTimeout(function () {
				document.execCommand('selectAll', false, null);
			});
		}
	});

	this.addSeparator();

	// FIXME: Uses geButton here and geLabel in main menu
	// var insertMenu = this.addMenuFunction('', mxResources.get('insert'), true, mxUtils.bind(this, function (menu) {   // 隐藏文本框插入
	// 	menu.addItem(mxResources.get('insertLink'), null, mxUtils.bind(this, function () {
	// 		this.editorUi.actions.get('link').funct();
	// 	}));

	// 	menu.addItem(mxResources.get('insertImage'), null, mxUtils.bind(this, function () {
	// 		this.editorUi.actions.get('image').funct();
	// 	}));

	// 	menu.addItem(mxResources.get('insertHorizontalRule'), null, mxUtils.bind(this, function () {
	// 		document.execCommand('inserthorizontalrule', false, null);
	// 	}));
	// }));

	// insertMenu.style.whiteSpace = 'nowrap';
	// insertMenu.style.overflow = 'hidden';
	// insertMenu.style.position = 'relative';
	// insertMenu.innerHTML = '<div class="geSprite geSprite-plus" style="margin-left:-4px;margin-top:-3px;"></div>' +
	// 	this.dropdownImageHtml;
	// insertMenu.style.width = (mxClient.IS_QUIRKS) ? '36px' : '16px';
	// insertMenu.style.height = '19px'

	// // Fix for item size in kennedy theme
	// if (EditorUi.compactUi) {
	// 	insertMenu.getElementsByTagName('img')[0].style.left = '24px';
	// 	insertMenu.getElementsByTagName('img')[0].style.top = '5px';
	// 	insertMenu.style.width = (mxClient.IS_QUIRKS) ? '50px' : '30px';
	// }

	// this.addSeparator();

	// KNOWN: All table stuff does not work with undo/redo
	// KNOWN: Lost focus after click on submenu with text (not icon) in quirks and IE8. This is because the TD seems
	// to catch the focus on click in these browsers. NOTE: Workaround in mxPopupMenu for icon items (without text).
	// var elt = this.addMenuFunction('geIcon geSprite geSprite-table', mxResources.get('table'), false, mxUtils.bind(this, function (menu) {  // 隐藏网格
	// 	var elt = graph.getSelectedElement();
	// 	var cell = graph.getParentByName(elt, 'TD', graph.cellEditor.text2);
	// 	var row = graph.getParentByName(elt, 'TR', graph.cellEditor.text2);

	// 	if (row == null) {
	// 		this.editorUi.menus.addInsertTableItem(menu);
	// 	} else {
	// 		var table = graph.getParentByName(row, 'TABLE', graph.cellEditor.text2);

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			try {
	// 				graph.selectNode(graph.insertColumn(table, (cell != null) ? cell.cellIndex : 0));
	// 			} catch (e) {
	// 				mxUtils.alert(mxResources.get('error') + ': ' + e.message);
	// 			}
	// 		}), null, 'geIcon geSprite geSprite-insertcolumnbefore');
	// 		elt.setAttribute('title', mxResources.get('insertColumnBefore'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			try {
	// 				graph.selectNode(graph.insertColumn(table, (cell != null) ? cell.cellIndex + 1 : -1));
	// 			} catch (e) {
	// 				mxUtils.alert(mxResources.get('error') + ': ' + e.message);
	// 			}
	// 		}), null, 'geIcon geSprite geSprite-insertcolumnafter');
	// 		elt.setAttribute('title', mxResources.get('insertColumnAfter'));

	// 		elt = menu.addItem('Delete column', null, mxUtils.bind(this, function () {
	// 			if (cell != null) {
	// 				try {
	// 					graph.deleteColumn(table, cell.cellIndex);
	// 				} catch (e) {
	// 					mxUtils.alert(mxResources.get('error') + ': ' + e.message);
	// 				}
	// 			}
	// 		}), null, 'geIcon geSprite geSprite-deletecolumn');
	// 		elt.setAttribute('title', mxResources.get('deleteColumn'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			try {
	// 				graph.selectNode(graph.insertRow(table, row.sectionRowIndex));
	// 			} catch (e) {
	// 				mxUtils.alert(mxResources.get('error') + ': ' + e.message);
	// 			}
	// 		}), null, 'geIcon geSprite geSprite-insertrowbefore');
	// 		elt.setAttribute('title', mxResources.get('insertRowBefore'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			try {
	// 				graph.selectNode(graph.insertRow(table, row.sectionRowIndex + 1));
	// 			} catch (e) {
	// 				mxUtils.alert(mxResources.get('error') + ': ' + e.message);
	// 			}
	// 		}), null, 'geIcon geSprite geSprite-insertrowafter');
	// 		elt.setAttribute('title', mxResources.get('insertRowAfter'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			try {
	// 				graph.deleteRow(table, row.sectionRowIndex);
	// 			} catch (e) {
	// 				mxUtils.alert(mxResources.get('error') + ': ' + e.message);
	// 			}
	// 		}), null, 'geIcon geSprite geSprite-deleterow');
	// 		elt.setAttribute('title', mxResources.get('deleteRow'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			// Converts rgb(r,g,b) values
	// 			var color = table.style.borderColor.replace(
	// 				/\brgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/g,
	// 				function ($0, $1, $2, $3) {
	// 					return "#" + ("0" + Number($1).toString(16)).substr(-2) + ("0" + Number($2).toString(16)).substr(-2) + ("0" + Number($3).toString(16)).substr(-2);
	// 				});
	// 			this.editorUi.pickColor(color, function (newColor) {
	// 				if (newColor == null || newColor == mxConstants.NONE) {
	// 					table.removeAttribute('border');
	// 					table.style.border = '';
	// 					table.style.borderCollapse = '';
	// 				} else {
	// 					table.setAttribute('border', '1');
	// 					table.style.border = '1px solid ' + newColor;
	// 					table.style.borderCollapse = 'collapse';
	// 				}
	// 			});
	// 		}), null, 'geIcon geSprite geSprite-strokecolor');
	// 		elt.setAttribute('title', mxResources.get('borderColor'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			// Converts rgb(r,g,b) values
	// 			var color = table.style.backgroundColor.replace(
	// 				/\brgb\s*\(\s*(\d+)\s*,\s*(\d+)\s*,\s*(\d+)\s*\)/g,
	// 				function ($0, $1, $2, $3) {
	// 					return "#" + ("0" + Number($1).toString(16)).substr(-2) + ("0" + Number($2).toString(16)).substr(-2) + ("0" + Number($3).toString(16)).substr(-2);
	// 				});
	// 			this.editorUi.pickColor(color, function (newColor) {
	// 				if (newColor == null || newColor == mxConstants.NONE) {
	// 					table.style.backgroundColor = '';
	// 				} else {
	// 					table.style.backgroundColor = newColor;
	// 				}
	// 			});
	// 		}), null, 'geIcon geSprite geSprite-fillcolor');
	// 		elt.setAttribute('title', mxResources.get('backgroundColor'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			var value = table.getAttribute('cellPadding') || 0;

	// 			var dlg = new FilenameDialog(this.editorUi, value, mxResources.get('apply'), mxUtils.bind(this, function (newValue) {
	// 				if (newValue != null && newValue.length > 0) {
	// 					table.setAttribute('cellPadding', newValue);
	// 				} else {
	// 					table.removeAttribute('cellPadding');
	// 				}
	// 			}), mxResources.get('spacing'));
	// 			this.editorUi.showDialog(dlg.container, 300, 80, true, true);
	// 			dlg.init();
	// 		}), null, 'geIcon geSprite geSprite-fit');
	// 		elt.setAttribute('title', mxResources.get('spacing'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			table.setAttribute('align', 'left');
	// 		}), null, 'geIcon geSprite geSprite-left');
	// 		elt.setAttribute('title', mxResources.get('left'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			table.setAttribute('align', 'center');
	// 		}), null, 'geIcon geSprite geSprite-center');
	// 		elt.setAttribute('title', mxResources.get('center'));

	// 		elt = menu.addItem('', null, mxUtils.bind(this, function () {
	// 			table.setAttribute('align', 'right');
	// 		}), null, 'geIcon geSprite geSprite-right');
	// 		elt.setAttribute('title', mxResources.get('right'));
	// 	}
	// }));

	// elt.style.position = 'relative';
	// elt.style.whiteSpace = 'nowrap';
	// elt.style.overflow = 'hidden';
	// elt.innerHTML = '<div class="geSprite geSprite-table" style="margin-left:-2px;"></div>' + this.dropdownImageHtml;
	// elt.style.width = (mxClient.IS_QUIRKS) ? '50px' : '30px';
	// elt.style.height = '19px'
	// Fix for item size in kennedy theme
	// if (EditorUi.compactUi) {
	// 	elt.getElementsByTagName('img')[0].style.left = '22px';
	// 	elt.getElementsByTagName('img')[0].style.top = '5px';
	// }
};

/**
 * Hides the current menu.
 */
Toolbar.prototype.hideMenu = function () {
	this.editorUi.hideCurrentMenu();
};

/**
 * Adds a label to the toolbar.  // 增加一个图标到工具栏
 */
Toolbar.prototype.addMenu = function (label, tooltip, showLabels, name, c, showAll) {
	var menu = this.editorUi.menus.get(name);
	var elt = this.addMenuFunction(label, tooltip, showLabels, function () {
		menu.funct.apply(menu, arguments);
	}, c, showAll);

	menu.addListener('stateChanged', function () {
		elt.setEnabled(menu.enabled);
	});

	return elt;
};

/**
 * Adds a label to the toolbar.
 */
Toolbar.prototype.addMenuFunction = function (label, tooltip, showLabels, funct, c, showAll) {
	return this.addMenuFunctionInContainer((c != null) ? c : this.container, label, tooltip, showLabels, funct, showAll);
};

/**
 * Adds a label to the toolbar.
 */
Toolbar.prototype.addMenuFunctionInContainer = function (container, label, tooltip, showLabels, funct, showAll) {
	var elt = (showLabels) ? this.createLabel(label) : this.createButton(label);
	this.initElement(elt, tooltip);
	this.addMenuHandler(elt, showLabels, funct, showAll);
	container.appendChild(elt);
	return elt;
};

/**
 * Adds a separator to the separator.  --添加分隔符
 */
Toolbar.prototype.addSeparator = function (c) {
	c = (c != null) ? c : this.container;
	var elt = document.createElement('div');
	elt.className = 'geSeparator';
	c.appendChild(elt);

	return elt;
};

/**
 * Adds given action item --添加给定的操作项
 */
Toolbar.prototype.addItems = function (keys, c, ignoreDisabled) {
	var items = [];
	for (var i = 0; i < keys.length; i++) {
		var key = keys[i];

		if (key == '-') {
			items.push(this.addSeparator(c));
		} else {
			items.push(this.addItem('geSprite-' + key.toLowerCase(), key, c, ignoreDisabled));
		}
	}


	return items;
};

/**
 * Adds given action item --添加给定的操作项
 */
Toolbar.prototype.addItem = function (sprite, key, c, ignoreDisabled) {
	var action = this.editorUi.actions.get(key);
	var elt = null;

	if (action != null) {
		var tooltip = action.label;

		if (action.shortcut != null) {
			tooltip += ' (' + action.shortcut + ')';
		}

		elt = this.addButton(sprite, tooltip, action.funct, c);

		if (!ignoreDisabled) {
			elt.setEnabled(action.enabled);

			action.addListener('stateChanged', function () {
				elt.setEnabled(action.enabled);
			});
		}
	}

	return elt;
};

/**
 * Adds a button to the toolbar.  --添加按钮
 */
Toolbar.prototype.addButton = function (classname, tooltip, funct, c) {
	var elt = this.createButton(classname);
	c = (c != null) ? c : this.container;

	this.initElement(elt, tooltip);
	this.addClickHandler(elt, funct);
	c.appendChild(elt);

	return elt;
};

/**
 * Initializes the given toolbar element. 初始化给定的工具栏元素
 */
Toolbar.prototype.initElement = function (elt, tooltip) {
	// Adds tooltip
	if (tooltip != null) {
		elt.setAttribute('title', tooltip);
	}

	this.addEnabledState(elt);
};

/**
 * Adds enabled state with setter to DOM node (avoids JS wrapper).  使用setter向DOM节点添加启用状态(避免JS包装器)
 */
Toolbar.prototype.addEnabledState = function (elt) {
	var classname = elt.className;

	elt.setEnabled = function (value) {
		elt.enabled = value;

		if (value) {
			elt.className = classname;
		} else {
			elt.className = classname + ' mxDisabled';
		}
	};

	elt.setEnabled(true);
};

/**
 * Adds enabled state with setter to DOM node (avoids JS wrapper).
 */
Toolbar.prototype.addClickHandler = function (elt, funct) {
	if (funct != null) {
		mxEvent.addListener(elt, 'click', function (evt) {
			if (elt.enabled) {
				funct(evt);
			}

			mxEvent.consume(evt);
		});

		if (document.documentMode != null && document.documentMode >= 9) {
			// Prevents focus
			mxEvent.addListener(elt, 'mousedown', function (evt) {
				evt.preventDefault();
			});
		}
	}
};

/**
 * Creates and returns a new button. 创建并返回一个新按钮
 */
Toolbar.prototype.createButton = function (classname) {
	var elt = document.createElement('a');
	elt.setAttribute('href', 'javascript:void(0);');
	elt.className = 'geButton';

	var inner = document.createElement('div');

	if (classname != null) {
		inner.className = 'geSprite ' + classname;
	}

	elt.appendChild(inner);

	return elt;
};

/**
 * Creates and returns a new button. 创建并返回一个新按钮
 */
Toolbar.prototype.createLabel = function (label, tooltip) {
	var elt = document.createElement('a');
	elt.setAttribute('href', 'javascript:void(0);');
	elt.className = 'geLabel';
	mxUtils.write(elt, label);

	return elt;
};

/**
 * Adds a handler for showing a menu in the given element. 添加用于在给定元素中显示菜单的处理程序。
 */
Toolbar.prototype.addMenuHandler = function (elt, showLabels, funct, showAll) {
	if (funct != null) {
		var graph = this.editorUi.editor.graph;
		var menu = null;
		var show = true;

		mxEvent.addListener(elt, 'click', mxUtils.bind(this, function (evt) {
			if (show && (elt.enabled == null || elt.enabled)) {
				graph.popupMenuHandler.hideMenu();
				menu = new mxPopupMenu(funct);
				menu.div.className += ' geToolbarMenu';
				menu.showDisabled = showAll;
				menu.labels = showLabels;
				menu.autoExpand = true;

				var offset = mxUtils.getOffset(elt);
				menu.popup(offset.x, offset.y + elt.offsetHeight, null, evt);
				this.editorUi.setCurrentMenu(menu, elt);

				// Workaround for scrollbar hiding menu items 解决滚动条隐藏菜单项的问题
				if (!showLabels && menu.div.scrollHeight > menu.div.clientHeight) {
					menu.div.style.width = '40px';
				}

				menu.hideMenu = mxUtils.bind(this, function () {
					mxPopupMenu.prototype.hideMenu.apply(menu, arguments);
					this.editorUi.resetCurrentMenu();
					menu.destroy();
				});

				// Extends destroy to reset global state   扩展destroy以重置全局状态
				menu.addListener(mxEvent.EVENT_HIDE, mxUtils.bind(this, function () {
					this.currentElt = null;
				}));
			}

			show = true;
			mxEvent.consume(evt);
		}));

		// Hides menu if already showing 隐藏菜单，如果已经显示
		mxEvent.addListener(elt, 'mousedown', mxUtils.bind(this, function (evt) {
			show = this.currentElt != elt;

			// Prevents focus
			if (document.documentMode != null && document.documentMode >= 9) {
				evt.preventDefault();
			}
		}));
	}
};

/**
 * Adds a handler for showing a menu in the given element. 添加用于在给定元素中显示菜单的处理程序。
 */
Toolbar.prototype.destroy = function () {
	if (this.gestureHandler != null) {
		mxEvent.removeGestureListeners(document, this.gestureHandler);
		this.gestureHandler = null;
	}
};