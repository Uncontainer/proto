new Function('return defineLazyObject({sType : "PugModule"})')
pug.createModule("nmp.mseller_admin.product.post", {
	_htInstance : {},

	_htElementSelector : {
		"root" : "body",
		"post_top" : "._post_top",
		"file_select" : "._file_select",
		"tpl_post_unit_img" : "._tpl_post_unit_img",
		"tpl_post_unit_text" : "._tpl_post_unit_text",
		"preview_content" : "._preview_content",
		"preview_form" : "._preview_form",
		"flicking" : "._flicking"
	},
	_htElement : {},
	initialize : function() {
		this._initEvent();
		this._createFileUploader();
		this._initFlicking();
	},

	_initEvent : function() {
		pug.event.delegator.attach(this._getElement("root", true), "click", this);
	},
	_initFlicking : function() {
		if (!this._useFlinking) {
			return;
		}
		this._getElement("flicking").css({
			"overflow" : "hidden",
			"height" : "400px"
		})
		if (this._htInstance["flicking"]) {
			this._htInstance["flicking"].destroy();
		}
		var nLen = this._getElement("post_top").queryAll("._post_unit").length;

		if (this._flickType == 0) {
			if (nLen > 1) {
				this._htInstance["flicking"] = new jindo.m.CoverFlicking(this._getElement("flicking", true),{
					nDuration : 500,
					bHorizontal : false
				});
			}
		} else {
			if (nLen > 1) {
				this._htInstance["flicking"] = new jindo.m.CubeFlicking(this._getElement("flicking", true),{
					bHorizontal : false,
					sClassPrefix : 'flick-',
					nDuration : 300
				});
				this._htInstance["flicking"].moveTo(nIndex||nLen-1);
			}
		}


	},
	_createFileUploader : function(elFileSelect) {
		var elFileBrowse = elFileSelect||this._getElement("file_select");
		var sUploadUrl = /local./.test(window.location.href) ? "/upload/photos" : "/p/m/upload/photos";
		this._htInstance["file_upload"] = new jindo.FileUploader(elFileBrowse, {
			sUrl : sUploadUrl,
			htBrowseButton : null,
			sFiletype : '*.jpg;*.gif;*.png;*.jpeg;*.bmp',
			sMsgNotAllowedExt: "지원하지 않는 이미지타입입니다",
			bAutoUpload : false,
			sCallback : "about:blank"
		}).attach({
			"select" : $Fn(this._onImageSelect, this).bind(),
			"success" : $Fn(this._onImageSuccess, this).bind(),
			"error" : $Fn(this._onImageFail, this).bind()
		});
	},
	_onImageSelect : function(weSelect) {
//		nmp.requestLoginCheck({
//			onload : $Fn(function() {
			this._htInstance["file_upload"].upload();
//			}, this).bind()
//		});
	},
	_onImageSuccess : function(weSuccess){
		weSuccess.htResult.url = weSuccess.htResult.url + "?type=o640";
		var sHtml = $Template(this._getElement("tpl_post_unit_img").html()).process(weSuccess.htResult);
		this._getElement("post_top").append(sHtml);
		this._initFlicking();
	},
	_onImageFail : function(weSuccess){
		alert("업로드실패... 다시시도");
	},
	_flickingOrderNumber : function() {
		var ael = this._getElement("post_top").queryAll("._post_unit");
		$A(ael).forEach(function(el, index) {
			$Element(el).attr("index", index);
		})
	},
	addPostText : function() {
		this._getElement("post_top").append(this._getElement("tpl_post_unit_text").html());
		this._initFlicking();
	},
	addPostImg : function() {
		this._getElement("file_select").fireEvent("click");
//		this._getElement("post_top").append(this._getElement("tpl_post_unit_img").html());
	},
	remove : function(wel) {
		var welPostUnit = $Element(wel.element).parent();
		welPostUnit.parent().remove(welPostUnit.$value());
		this._initFlicking();
	},

	/**
	 * FileUpload 리셋 메소드
	 * @param {Number} nIndex
	 */
	_resetImageFileUpload : function(nIndex){
		this._htInstance["file_upload"].reset();
	},

	preview : function() {
		var array = new Array();
		var welList = this._getElement("post_top").queryAll("._post_unit");
		for(var i = 0 ; i < welList.length ; i++) {
			var wel = welList[i];
			if (wel.hasClass("_img_type")) {
				array.push(wel.query("._image_area").html());

			} else {
				array.push(wel.query("._text_area").html());
			}
//			array.add(el)
		}

		this._getElement("preview_content", true).value = array.join("");
		this._getElement("preview_form", true).action = /local./.test(window.location.href) ? "/post/preview" : "/p/m/post/preview";;
		this._getElement("preview_form", true).submit();
	},

	up : function(wel) {
		var postUnit = $Element(wel.element).parent();
		var prevUnit = postUnit.prev();
		if (prevUnit) {
			prevUnit.before(postUnit);
		}
		this._initFlicking();
	},
	down : function(wel) {
		var postUnit = $Element(wel.element).parent();
		var nextUnit = postUnit.next();
		if (nextUnit) {
			postUnit.before(nextUnit);
		}
		this._initFlicking();
	},

	_useFlinking : false,
	_flickType : 0,
	cover : function() {
		this._useFlinking = true;
		this._flickType = 0;
		this._initFlicking();
	},
	cube : function() {
		this._useFlinking = true;
		this._flickType = 1;
		this._initFlicking();
	}
});