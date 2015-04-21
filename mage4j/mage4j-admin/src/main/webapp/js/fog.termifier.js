(function($){
	$.fn.termifier = function(actionURL, options) {
		var settings = $.extend({
			origin: {top:0, left:0},
			paramName: 'term',
			addCladd: null,
			actionURL: actionURL
		}, options||{});
		
		this.click(function(event) {
			$('div.termifier').remove();
			
			$('div')
				.addClass('termifier' + (settings.addClass ? (' ') + settings.addClass : ''))
				.css({
					position: 'absolute',
					top: event.pageY - setting.origin.top,
					left: event.pageX - setting.origin.left,
					display: 'none'
				})
				.click(function(event) {
					$(this).fadeOut('slow');
				})
				.appendTo('body')
				.append(
					$('div').load(
						setting.actonURL,
						encodeURIComponent(settings.paramName) + '=' + encodeURIComponent($(event.target).text()),
						function() {
							$(this).closet('.termifier').fadeIn('slow');
						}
					)
				);
		});
		
		this.addClass('termifier');
		
		return this;
	};
})(jQuery);