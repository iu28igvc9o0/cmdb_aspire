/**
 * Copyright (c) 2006-2015, JGraph Ltd
 * Copyright (c) 2006-2015, Gaudenz Alder
 */
/**
 * Class: mxPolyline
 *
 * Extends <mxShape> to implement a polyline (a line with multiple points).
 * This shape is registered under <mxConstants.SHAPE_POLYLINE> in
 * <mxCellRenderer>.
 *
 * Constructor: mxPolyline
 *
 * Constructs a new polyline shape.
 *
 * Parameters:
 *
 * points - Array of <mxPoints> that define the points. This is stored in
 * <mxShape.points>.
 * stroke - String that defines the stroke color. Default is 'black'. This is
 * stored in <stroke>.
 * strokewidth - Optional integer that defines the stroke width. Default is
 * 1. This is stored in <strokewidth>.
 */
function mxPolyline(points, stroke, strokewidth)
{
	mxShape.call(this);
	this.points = points;
	this.stroke = stroke;
	this.strokewidth = (strokewidth != null) ? strokewidth : 1;
	
};

/**
 * Extends mxShape.
 */
mxUtils.extend(mxPolyline, mxShape);

/**
 * Function: getRotation
 *
 * Returns 0.
 */
mxPolyline.prototype.getRotation = function()
{
	return 0;
};

/**
 * Function: getShapeRotation
 *
 * Returns 0.
 */
mxPolyline.prototype.getShapeRotation = function()
{
	return 0;
};

/**
 * Function: isPaintBoundsInverted
 *
 * Returns false.
 */
mxPolyline.prototype.isPaintBoundsInverted = function()
{
	return false;
};

/**
 * Function: paintEdgeShape
 *
 * Paints the line shape.
 */
mxPolyline.prototype.paintEdgeShape = function(c, pts)
{
	if (this.style == null || this.style[mxConstants.STYLE_CURVED] != 1)
	{
		this.paintLine(c, pts, this.isRounded);
	}
	else
	{
		this.paintCurvedLine(c, pts);
	}
};

/**
 * Function: paintLine
 *
 * Paints the line shape.
 */
mxPolyline.prototype.paintLine = function(c, pts, rounded)
{
	// console.log('------------------------开始')
	var graph = window.newEditorUi.editor.graph
	var cells = graph.getModel().cells
	var allPoints = []
	var intersectionArr = []
	Object.keys(cells).forEach(function (key) {
		if (cells[key].edge) {
			let state = graph.view.getState(cells[key]);
			if (state.absolutePoints) {
				allPoints.push(state.absolutePoints)
			}
		}
	})
	if (allPoints.length > 1) {
		// for (var i = 0; i < allPoints.length; i++) {
		// 	var line1 = allPoints[i];
		// 	for (var j = i + 1; j < allPoints.length; j++) {
		// 		var line2 = allPoints[j]
		// 		console.log(line1, line2)
		// 		this.loopLines(c, line1, line2);
		// 	}
		// }
		var newArr = []

		allPoints.forEach((item, index) => {
			if (!newArr[index]) {
				newArr[index] = []
			}
			for (var i = 0; i < item.length; i ++) {
				if (i < item.length - 1) {
					newArr[index].push([item[i], item[i + 1]])
				}
			}
		})
		for (var i = 0; i < newArr.length; i ++) {
			for (var j = i + 1; j < newArr.length; j ++) {
				if (this.contrast(c, newArr[i], newArr[j])) {
					intersectionArr.push(this.contrast(c, newArr[i], newArr[j]))
				}
			}
		}
		console.log(intersectionArr)
	}

	var arcSize = mxUtils.getValue(this.style, mxConstants.STYLE_ARCSIZE, mxConstants.LINE_ARCSIZE) / 2;
	c.begin();
	this.addPointsNew(intersectionArr, c, pts, rounded, arcSize, false);
	c.stroke();


};
// 对比
mxPolyline.prototype.contrast = function (c, arr1, arr2) {
	for (var i = 0; i < arr1.length; i ++) {
		for (var j = 0; j < arr2.length; j ++) {
			var coordinate = segmentsIntr(arr1[i][0], arr1[i][1], arr2[j][0], arr2[j][1])
			if (coordinate) {
				// console.log(coordinate)
				return coordinate
				// console.log(c)
				// c.begin();
				// c.roundrect(coordinate.x-5, coordinate.y-5, 10, 10, 2 * Math.PI);
				// c.setStrokeColor('red')
				// c.stroke();
				this.paintCurvedLine(c, [
					{
						x: coordinate.x - 10,
						y: coordinate.y
					},
					{
						x: coordinate.x - 10,
						y: coordinate.y - 10
					},
					{
						x: coordinate.x + 10,
						y: coordinate.y - 10
					},
					{
						x: coordinate.x + 10,
						y: coordinate.y
					}
				])
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
	var x = ((b.x - a.x) * (d.x - c.x) * (c.y - a.y)
		+ (b.y - a.y) * (d.x - c.x) * a.x
		- (d.y - c.y) * (b.x - a.x) * c.x) / denominator;
	var y = -((b.y - a.y) * (d.y - c.y) * (c.x - a.x)
		+ (b.x - a.x) * (d.y - c.y) * a.y
		- (d.x - c.x) * (b.y - a.y) * c.y) / denominator;

	/** 2 判断交点是否在两条线段上 **/
	if (
		// 交点在线段1上
		(x - a.x) * (x - b.x) <= 0 && (y - a.y) * (y - b.y) <= 0
		// 且交点也在线段2上
		&& (x - c.x) * (x - d.x) <= 0 && (y - c.y) * (y - d.y) <= 0
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


// mxPolyline.prototype.loopLines = function (c, line1, line2) {
// 	var addLine1,addLine2
// 		for (var i = 0; i < line1.length - 1; i++) {
// 			var addLine1 = [line1[i], line1[i+1]];
// 			for (let j = 0; j < line2.length - 1; j++) {
// 				var addLine2 = [line2[j], line2[j + 1]];
// 				this.addMarkPoint(c, addLine1, addLine2)
// 			}
// 		}
// }
// mxPolyline.prototype.addMarkPoint = function (c, l1, l2) {
// 	// console.log(c, l1, l2);
// 	  var p1 = l1[0],
// 				p2 = l1[1],
// 				p3 = l2[0],
// 				p4 = l2[1];
// 	var denominator = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);
// 	var ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)) / denominator;
// 	var ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)) / denominator;
// 	var x = p1.x + ua * (p2.x - p1.x);
// 	var y = p1.y + ua * (p2.y - p1.y);
//
// 	  if (ua > 0 && ub > 0) {
// 			c.begin();
// 			c.roundrect(x-5, y-5, 10, 10, 2 * Math.PI);
// 			c.setStrokeColor('blue')
// 			c.stroke();
// 	  }
// }
/**
 * Function: paintLine
 *
 * Paints the line shape.
 */
mxPolyline.prototype.paintCurvedLine = function(c, pts)
{
	c.begin();

	var pt = pts[0];
	var n = pts.length;

	c.moveTo(pt.x, pt.y);

	for (var i = 1; i < n - 2; i++)
	{
		var p0 = pts[i];
		var p1 = pts[i + 1];
		var ix = (p0.x + p1.x) / 2;
		var iy = (p0.y + p1.y) / 2;

		c.quadTo(p0.x, p0.y, ix, iy);
	}

	var p0 = pts[n - 2];
	var p1 = pts[n - 1];

	c.quadTo(p0.x, p0.y, p1.x, p1.y);
	c.stroke();
};
