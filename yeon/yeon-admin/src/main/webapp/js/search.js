TBoxFinder = jindo.$Class( {
	_completeDiv : null,
	_inputField : null,
	_candiatesTable : null,
	_candiatesTableBody : null,

	_target : null,
	_getDetailLink : null,
	_tagId : null,

	_id : null,

	$init : function(target, tagId, getDetailLink) {
		this._target = target;
		this._tagId = tagId;
		this._getDetailLink = getDetailLink;
		this._id = null;
	},

	getId : function() {
		return this._id;
	},

	findCandidates : function() {
		this._initVar();
		var self = this;
		if (this._inputField.value.length > 0) {
			var ajax = $Ajax("/name/search/searchCandidates.do", {
				onload : function(oResponse) {
					var json = oResponse.json();
					self._setCandidates(json);
				}
			});

			ajax.request( {
				"target" : this._target,
				"keyword" : this._inputField.value
			});
		} else {
			this._clearCandidates();
		}
	},

	_initVar : function() {
		this._inputField = document.getElementById("keyword_" + this._tagId);
		this._candiatesTable = document.getElementById("candidates_table_" + this._tagId);
		this._candiatesTableBody = document.getElementById("candidates_table_body_" + this._tagId);
		this._completeDiv = document.getElementById("popup_" + this._tagId);
	},

	_setCandidates : function(candidate) {
		this._clearCandidates();
		var size = candidate.length;
		this._setOffsets();
		var row, cell, txtNode;
		var self = this;
		for ( var i = 0; i < size; i++) {
			var nextNode = candidate[i];
			row = document.createElement("tr");
			nameCell = document.createElement("td");

			nameCell.setAttribute("id", nextNode.id);
			nameCell.onmouseout = function() {
				this.className = 'mouseOver';
			};
			nameCell.onmouseover = function() {
				this.className = 'mouseOut';
			};
			nameCell.setAttribute("bgcolor", "#FFFAFA");
			nameCell.setAttribute("border", "0");
			nameCell.onclick = function() {
				self._populateName(this);
			};
			nameCell.appendChild(document.createTextNode(nextNode.name));
			row.appendChild(nameCell);

			idCell = document.createElement("td");

			idCell.setAttribute("color", "#FFFAFA");
			idCell.setAttribute("border", "0");
			var link = document.createElement("a");
			link.target = "_blank";
			link.href = self._getDetailLink(nextNode.id);
			link.appendChild(document.createTextNode(nextNode.id));
			idCell.appendChild(link);
			row.appendChild(idCell);

			this._candiatesTableBody.appendChild(row);
		}
	},

	_setOffsets : function() {
		var end = this._inputField.offsetWidth;
		var left = this._calculateOffsetLeft(this._inputField);
		var top = this._calculateOffsetTop(this._inputField) + this._inputField.offsetHeight;
		this._completeDiv.style.border = "black 1px solid";
		this._completeDiv.style.left = left + "px";
		this._completeDiv.style.top = top + "px";
		this._candiatesTable.style.width = end + "px";
	},

	_calculateOffsetLeft : function(field) {
		return this._calculateOffset(field, "offsetLeft");
	},

	_calculateOffsetTop : function(field) {
		return this._calculateOffset(field, "offsetTop");
	},

	_calculateOffset : function(field, attr) {
		var offset = 0;
		while (field) {
			offset += field[attr];
			field = field.offsetParent;
		}
		return offset;
	},

	_populateName : function(cell) {
		this._id = cell.getAttribute("id");
		this._inputField.value = cell.firstChild.nodeValue;
		this._clearCandidates();
	},

	_clearCandidates : function() {
		var ind = this._candiatesTableBody.childNodes.length;
		for ( var i = ind - 1; i >= 0; i--) {
			this._candiatesTableBody.removeChild(this._candiatesTableBody.childNodes[i]);
		}
		this._completeDiv.style.border = "none";
	}
});

var getClassReadLink = function(id) {
	return "/class/read?resourceId=" + id;
};

function createClassFinder(id) {
	return new TBoxFinder("class", id, getClassReadLink);
}

var getPropertyReadLink = function(id) {
	return "/property/read?propertyId=" + id;
};

function createPropertyFinder(id) {
	return new TBoxFinder("property", id, getPropertyReadLink);
} 

var getInstanceReadLink = function(id) {
	return "/instance/read.do?instanceId=" + id;
}; 

function createInstanceFinder(id) {
	return new TBoxFinder("instance", id, getInstanceReadLink);
} 
