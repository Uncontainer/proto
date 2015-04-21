(function(jQuery) {
	jQuery.fn.localeValueEditor = function() {
		var resourceId = jQuery(this).attr("data-resource-id");
		var valueType = jQuery(this).attr("data-value-type");
		if(!resourceId || !valueType) {
			throw {
				name: 'TargetError',
				message: 'needs locale value editor form'
			};
		}
		
		var rootElementId = '#resource_' + valueType + '_' + resourceId;
		var urlPrefix = '/lang/value/locale/' + valueType + '/';
		
		jQuery.template( "entryTemplate", jQuery(rootElementId + ' ._entry_template').html() );
	
		// Add locale value
		jQuery(rootElementId + ' ._add_button').bind('click', function() {
			var addForm = jQuery(this).closest('form._resource_locale_value_create_form');
			
			jQuery.post(urlPrefix + 'create', 
				addForm.serialize(),
				function(response) {
					if(response.success || false) {
						jQuery(rootElementId + ' tr').last()
							.before($('<tr/>').css({'vertical-align':'top'}))
							.prev()
							.append(jQuery.tmpl("entryTemplate", response.value).show());
						
						addForm.find('._add_area [name=locale]').val("");
						addForm.find('._add_area [name=value]').val("");
					} else {
						alert('에러 발생');
					}
				});
				
			return false;
		});
		
		// Modify locale value
		jQuery(rootElementId + ' ._modify_button').live('click', function() {
			var modifyButton = jQuery(this);
			var row = modifyButton.closest('tr');
			var elLocale = row.children('._locale').eq(0);
			var elValue = row.children('._value').eq(0);
			var removeButton = row.find('._remove_button').eq(0);
			
			if(elLocale.children('input[name="locale"]').length > 0) {
				var newLocale = elLocale.children('[name="locale"]').eq(0).val();
				var newValue = elValue.children('[name="value"]').eq(0).val();
					
				jQuery.post(urlPrefix + 'modify', 
					{
						id: jQuery(this).attr('data-entry-id'),
						locale: newLocale,
						value: newValue
					},
					function(response) {
						if(response.success || false) {
							elLocale.text(newLocale);
							elValue.text(newValue);
							modifyButton.text('수정');
							removeButton.text('삭제');
						} else {
							alert('에러 발생');
						}
					});
			} else {
				var locale = elLocale.text();
				elLocale.html('').append($(rootElementId + ' ._add_area [name="locale"]').clone().val(locale));
				
				var value = elValue.text();
				elValue.html('').append($(rootElementId + ' ._add_area [name="value"]').clone().val(value));
				
				modifyButton.text('확인');
				removeButton.text('취소');
			}
							
			return false;
		});
		
		// Remove locale value
		jQuery(rootElementId + ' ._remove_button').live('click', function() {
			var row = jQuery(this).closest('tr');
			var elLocale = row.children('._locale').eq(0);
			var elValue = row.children('._value').eq(0);
			
			if('취소' === jQuery(this).text()) {
				elLocale.html(elLocale.children('[name="locale"]').eq(0).val());
				elValue.html(elValue.children('[name="value"]').eq(0).val());
				
				row.find('._modify_button').text('수정');
				row.find('._remove_button').text('삭제');
				
				return;
			}
			
			
			var locale = elLocale.text();
			var value = elValue.text();
			
			if(!confirm("'" + locale + ":" + value + "'을(를) 삭제하시겠습니까?")) {
				return false;
			}
			
			jQuery.post(urlPrefix + 'remove',
				{ id : jQuery(this).attr('data-entry-id') }, 
				function(response) {
					if(response.success || false) {
						row.remove();
					} else {
						alert('에러 발생');
					}
				});
				
			return false;
		});
		
		// Initialize locale value area
		jQuery.get(urlPrefix + resourceId, 
			{}, 
			function(response) {
				if (response.success || false) {
					for(var i=0; i<response.value.length; i++) {
						var entry =response.value[i];
						jQuery(rootElementId + ' tr').last()
							.before($('<tr/>').css({'vertical-align':'top'}))
							.prev()
							.append(jQuery.tmpl("entryTemplate", entry).show());
					}
				} else {
					if (response.message) {
						alert(response.message);
					} else {
						alert('에러 발생');
					}
				}
			});
		
		return this;
	};
	
	jQuery.fn.nameEditor = function() {
		var valueType = jQuery(this).attr("data-value-type");
		if('name' !== valueType) {
			throw {
				name: 'TargetError',
				message: 'needs locale value editor form'
			};
		}
		
		jQuery(this).localeValueEditor();
	};
	
	jQuery.fn.descEditor = function() {
		jQuery(this).descriptionEditor();
	};
	
	jQuery.fn.descriptionEditor = function() {
		var valueType = jQuery(this).attr("data-value-type");
		if('description' !== valueType && 'desc' !== valueType) {
			throw {
				name: 'TargetError',
				message: 'needs locale value editor form'
			};
		}
		
		jQuery(this).localeValueEditor();
	};
})(jQuery);