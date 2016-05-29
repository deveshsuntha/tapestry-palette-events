define(["t5/core/zone"], function(zoneManager) {
  T5.extendInitializers({
    observe: function(spec) {
      var element = $(spec.id).observe(spec.event, function() {
        var url = spec.url;
        var selected = "";
        $(spec.id).childElements().each(function(element) {
          if (!selected) {
            selected += element.value;
          } else {
            selected += "," + element.value;
          }
        });
        url = appendQueryStringParameter(url, "selected", selected);
        zoneManager.deferredZoneUpdate(spec.zone, url);
      });
    }
  });
  function appendQueryStringParameter(url, name, value) {
    if (url.indexOf('?') < 0) {
      url += '?'
    } else {
      url += '&';
    }
    value = escape(value);
    url += name + '=' + value;
    return url;
  }
});