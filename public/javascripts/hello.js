if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}
$("#SignUpForm").validate({
    rules: {
       name: {
          required: true
             }
       },
       messages: {
          name: "Required Field"
       }
   });

/**
 * 
 * Map canvas starts here
 */
function initialize() {
	var myLatlng = new google.maps.LatLng(28.645,77.17685);
    var mapCanvas = document.getElementById('map-canvas');
    var mapOptions = {
      center: myLatlng,
      zoom: 14,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions);

    var marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        title: 'Knoldus Software LLP'
    });
  }