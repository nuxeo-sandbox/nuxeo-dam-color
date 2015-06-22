function init_color_picker(color_picker) {

  var client = new nuxeo.Client();

  client.request('directory/colors')
    .get(function(error, data) {
      if (error) {
        // something went wrong
        throw error;
      }
      for (i = 0; i < data.entries.length; i++) {
          var entry = data.entries[i];
          display_color(color_picker, entry.properties.id);
      }
      init_selection(color_picker);
      console.log(JSON.stringify(data.entries, null, 2))
    });
};

function display_color(color_picker, colorHex) {
    var root = jQuery('#' + color_picker + " .color_palette").first();
    var div = jQuery('<div>').
      attr('id','colors' + Math.random()).
      attr('name',colorHex).
      addClass('not_selected_color').
      css('background-color','#'+colorHex);
    div.appendTo(root);
    div.click(function(event){
      var element = jQuery(event.target),
        picker = color_picker;
      if (element.hasClass("not_selected_color")) {
        element.removeClass("not_selected_color");
        element.addClass("selected_color")
        add_color_to_selection(picker, element.attr('name'))
      } else {
        element.removeClass("selected_color");
        element.addClass("not_selected_color")
        remove_color_from_selection(picker, element.attr('name'))
      }
    });
};


function add_color_to_selection(picker, colorHex) {
    var input = jQuery("#" + picker + " .color_input :input").first();
    var value = input.attr('value');
    value.length > 0 ? value = value.concat(','+colorHex) : value = value.concat(''+colorHex);
    input.attr('value',value);
};

function remove_color_from_selection(picker, inputColor) {
    var input = jQuery("#" + picker + " .color_input :input").first();
    var colorArray = input.attr('value').split(',');
    colorArray.splice(colorArray.indexOf(inputColor),1);
    var output = '';
    for (color of colorArray) {
      output.length > 0 ?
        output = output.concat(','+color) :
        output = output.concat(''+color);
    }
    input.attr('value',output);
};

function init_selection(picker) {
    var input = jQuery("#" + picker + " .color_input :input").first();
    var value = input.attr('value');
    value.length > 0 ? value = value.split(',') : value = value;
    for (i = 0; i < value.length; i++) {
        var color = jQuery("#" + picker + ' div[name='+value[i]+']');
        color.removeClass("not_selected_color");
        color.addClass("selected_color")
    }
};

