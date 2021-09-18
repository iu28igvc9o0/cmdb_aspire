var language="ch";



function expandSection(titleObj){
	var titleId = titleObj.id;
	var contentId = titleId.replace("title", "content");
	var contentObj = document.getElementById(contentId);

	if (contentObj){
		  if(contentObj.style.display == "none"){
		  	contentObj.style.display = "";
		  } else{
		  	contentObj.style.display = "none";
		  }
	}

	if (contentObj){
		if (titleObj.className == "VSectionTitle"){
			titleObj.className = "VSectionTitleClose";
		}else{
			titleObj.className = "VSectionTitle";
		}
	}

}

function expandTable(titleObj){
	var titleId = titleObj.id;
	var tableId = titleId.replace("title", "content");
	var tableObj = document.getElementById(tableId);
	if (tableObj){
		  if(tableObj.style.display == "none"){
		  	tableObj.style.display = "";
		  } else{
		  	tableObj.style.display = "none";
		  }
	}
	if (titleObj){
		if (titleObj.className == "VTableExpand"){
			titleObj.className = "VTableShrink";
		}else{
			titleObj.className = "VTableExpand";
		}
	}
}

function expandSubTable(cellObj){
	var tableObj = getParent(cellObj, "table");
	if (tableObj){
		var curStyle = "";
		for (i=1;i < tableObj.rows.length ; i++)
		{
			if (i == 1){
				if(tableObj.rows[i].cells[0].className == "VCellShrink"){
				  	tableObj.rows[i].cells[0].className = "VCellExpand";
				  	curStyle = "none";

				}else{
					tableObj.rows[i].cells[0].className = "VCellShrink";
				}
			}
			else{
				for(j=0; j <tableObj.rows[i].cells.length;j++)
				{
					tableObj.rows[i].cells[j].style.display = curStyle;
				}
				tableObj.rows[i].style.display = curStyle;
			}
		}

	}
}

function getParent(el, pTagName) {
	if (el == null)
		return null;
	 else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())
	 	return el;
	 else
	 	return getParent(el.parentNode, pTagName);

}

var dataip;
function goRefUrl(param){
	if (null != dataip){
		window.open("http://" + dataip + param);
	}else{
		window.open(param);
	}
}
function closeSubTable(){
	var tableObjs = $(".VPropTable");
	tableObjs.each(function(m){
		if (tableObjs[m]){
			var curStyle = "none";
			for (i=1;i < tableObjs[m].rows.length ; i++)
			{
					if (i == 1){
						if(tableObjs[m].rows[i].cells[0].className == "VCellShrink"){
						  	tableObjs[m].rows[i].cells[0].className = "VCellExpand";
						}
					}
					else{
						for(j=0; j <tableObjs[m].rows[i].cells.length;j++)
						{
							tableObjs[m].rows[i].cells[j].style.display = curStyle;
						}
						tableObjs[m].rows[i].style.display = curStyle;
					}

			}

		}
	});
}

function queryVulnDetail(url,id){

	if(null!=url){
		closeSubTable();
		window.open(url,'_self');
		var tdObj =$("#"+id).parents("td").eq(0);
		tdObj.each(function(i){
			if(null!=tdObj[i] && "undefined"!=tdObj[i]){
				expandSubTable(tdObj[i]);
			}
		});
	}
}