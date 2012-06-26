<html> 
<head> 
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/> 
<title>Raleigh Retold</title> 
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script> 
<script type="text/javascript"> 
  var map;
  function initialize() {
    var latitude =  35.7844;
    var longitude = -78.6649;
    if (window.android){
      latitude = window.android.getLatitude();
      longitude = window.android.getLongitude();
    }

    var myLatlng = new google.maps.LatLng(latitude,longitude);
    var myOptions = {
      zoom: 12,
	  streetViewControl: false,
	  mapTypeControl: false,
      center: myLatlng,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }

    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
		
	var testStyles =
	[
	  {
		stylers: [
		  { invert_lightness: true },
		  { hue: "#003bff" },
		  { lightness: 31 },
		  { saturation: -22 },
		  { gamma: 0.61 }
		]
	  }
	]
	// http://gmaps-samples-v3.googlecode.com/svn/trunk/styledmaps/wizard/index.html
	map.setOptions({styles: testStyles});
	
	// Dump Youtube data directly for testing purposes. Comment out for release.	
	//var georssLayer = new google.maps.KmlLayer('https://gdata.youtube.com/feeds/api/users/Udae7Y9ZC1T9M7cTA7NyYA/uploads');
	//georssLayer.setMap(map);
	
	// Initialize icons
	// Icons created using http://mapicons.nicolasmollet.com/
	var personIcon = 'person.png';
	var rIcon = 'fastfood.png';
	var bIcon = 'bar.png';
	var hIcon = 'historical.png';
	var eIcon = 'theater.png';
	var sIcon = 'university.png';
	
	// Set user location marker
	var personMarker = new google.maps.Marker({position: myLatlng, icon:personIcon});
	personMarker.setMap(map);
	personMarker.setAnimation(google.maps.Animation.BOUNCE);
	var infoWindowPerson = new google.maps.InfoWindow({
		content: 'You are here. Click another point to view multimedia about that location!',
		maxWidth: 100
	});
	google.maps.event.addListener(personMarker, 'click', function() {
		infoWindowPerson.open(map,personMarker);
	});
    //FIXME: need to add a listener to update location
	
	// Set story marker locations
	
	// Loaded from XML parser
	// TODO: stick these in an array
	
	var placeMarker1 = new google.maps.Marker({position: new google.maps.LatLng(35.7862, -78.6616), icon: bIcon});
	placeMarker1.setMap(map);
	google.maps.event.addListener(placeMarker1, 'click', function() {infoWindow1.open(map,placeMarker1);});
	var contentString1 = '<center><b>Player\'s Retreat</b><br />Player\'s Retreat is a bar and restaurant near NCSU campus with a long history of interesting people and stories.<a href="vnd.youtube://zpbI__S-tSo"><img src="http://i.ytimg.com/vi/zpbI__S-tSo/0.jpg" height=180 width=240></a></center>';
	var infoWindow1 = new google.maps.InfoWindow({content: contentString1, maxWidth: 270});
	
	var placeMarker2 = new google.maps.Marker({position: new google.maps.LatLng(35.776977, -78.644366), icon: eIcon});
	placeMarker2.setMap(map);
	google.maps.event.addListener(placeMarker2, 'click', function() {infoWindow2.open(map,placeMarker2);});
	var contentString2 = '<center><b>Paper Plant</b><br />The old paper plant was an employment center for many a Raleigh citizen.<a href="vnd.youtube://XFComlY1Hgs"><img src="http://i.ytimg.com/vi/XFComlY1Hgs/0.jpg" height=180 width=240></a></center>';
	var infoWindow2 = new google.maps.InfoWindow({content: contentString2, maxWidth: 270});
	
	var placeMarker3 = new google.maps.Marker({position: new google.maps.LatLng(35.776861,-78.649632), icon: rIcon});
	placeMarker3.setMap(map);
	google.maps.event.addListener(placeMarker3, 'click', function() {infoWindow3.open(map,placeMarker3);});
	var contentString3 = '<center><b>Thrifty Food Mart</b><br />The Thrifty Food Mart has a great story to tell!<a href="vnd.youtube://GsX3rFyG3f8"><img src="http://i.ytimg.com/vi/ozJeXP-1rFs/0.jpg" height=180 width=240></a></center>';
	var infoWindow3 = new google.maps.InfoWindow({content: contentString3, maxWidth: 270});
	
	var placeMarker4 = new google.maps.Marker({position: new google.maps.LatLng(35.776927,-78.639284), icon: hIcon});
	placeMarker4.setMap(map);
	google.maps.event.addListener(placeMarker4, 'click', function() {infoWindow4.open(map,placeMarker4);});
	var contentString4 = '<center><b>Raleigh Skyline</b><br />The Raleigh skyline has changed a great deal through the years as buildings have come and go.<a href="vnd.youtube://y7jPWyydyPM"><img src="http://i.ytimg.com/vi/y7jPWyydyPM/0.jpg" height=180 width=240></a></center>';
	var infoWindow4 = new google.maps.InfoWindow({content: contentString4, maxWidth: 270});
	
	var placeMarker5 = new google.maps.Marker({position: new google.maps.LatLng(35.778530,-78.649117), icon: bIcon});
	placeMarker5.setMap(map);
	google.maps.event.addListener(placeMarker5, 'click', function() {infoWindow5.open(map,placeMarker5);});
	var contentString5 = '<center><b>Boylan Bridge Brew Pub</b><br />Alliterative historic places are always awesome.<a href="vnd.youtube://GsX3rFyG3f8"><img src="http://i.ytimg.com/vi/ozJeXP-1rFs/0.jpg" height=180 width=240></a></center>';
	var infoWindow5 = new google.maps.InfoWindow({content: contentString5, maxWidth: 270});
	
	var placeMarker6 = new google.maps.Marker({position: new google.maps.LatLng(35.777777,-78.638120), icon: rIcon});
	placeMarker6.setMap(map);
	google.maps.event.addListener(placeMarker6, 'click', function() {infoWindow6.open(map,placeMarker6);});
	var contentString6 = '<center><b>Raleigh Sandwich Shop</b><br />Get two pieces of bread and put some history in the middle.<a href="vnd.youtube://GsX3rFyG3f8"><img src="http://i.ytimg.com/vi/ozJeXP-1rFs/0.jpg" height=180 width=240></a></center>';
	var infoWindow6 = new google.maps.InfoWindow({content: contentString6, maxWidth: 270});
	
	var placeMarker7 = new google.maps.Marker({position: new google.maps.LatLng(35.77157, -78.6358), icon: sIcon});
	placeMarker7.setMap(map);
	google.maps.event.addListener(placeMarker7, 'click', function() {infoWindow7.open(map,placeMarker7);});
	var contentString7 = '<center><b>Shaw University</b><br />A private university affiliated with the Baptist church, which is the oldest historically Black college in the South.<a href="vnd.youtube://_AhJCcFRwmk"><img src="http://i.ytimg.com/vi/_AhJCcFRwmk/0.jpg" height=180 width=240></a></center>';
	var infoWindow7 = new google.maps.InfoWindow({content: contentString7, maxWidth: 270});
	
	var placeMarker8 = new google.maps.Marker({position: new google.maps.LatLng(35.77758, -78.63939), icon: hIcon});
	placeMarker8.setMap(map);
	google.maps.event.addListener(placeMarker8, 'click', function() {infoWindow8.open(map,placeMarker8);});
	var contentString8 = '<center><b>Briggs Hardware</b><br />Currently the building is home to the Raleigh City Museum, and is owned by a joint group of non-profit organizations.<a href="vnd.youtube://GsX3rFyG3f"><img src="http://i.ytimg.com/vi/GsX3rFyG3f/0.jpg" height=180 width=240></a></center>';
	var infoWindow8 = new google.maps.InfoWindow({content: contentString8, maxWidth: 270});
	
	var placeMarker9 = new google.maps.Marker({position: new google.maps.LatLng(35.77941, -78.63914), icon: hIcon});
	placeMarker9.setMap(map);
	google.maps.event.addListener(placeMarker9, 'click', function() {infoWindow9.open(map,placeMarker9);});
	var contentString9 = '<center><b>Fayetteville Street</b><br />A major street in downtown Raleigh that connects the State Capitol to the Raleigh Convention Center.<a href="vnd.youtube://m7ck4VEu9Tg"><img src="http://i.ytimg.com/vi/m7ck4VEu9Tg/0.jpg" height=180 width=240></a></center>';
	var infoWindow9 = new google.maps.InfoWindow({content: contentString9, maxWidth: 270});
	
	var placeMarker10 = new google.maps.Marker({position: new google.maps.LatLng(35.77819, -78.63859), icon: hIcon});
	placeMarker10.setMap(map);
	google.maps.event.addListener(placeMarker10, 'click', function() {infoWindow10.open(map,placeMarker10);});
	var contentString10 = '<center><b>Raleigh Times</b><br />It was a newspaper, now it is a bar!<a href="vnd.youtube://qrAHiwp4X98"><img src="http://i.ytimg.com/vi/qrAHiwp4X98/0.jpg" height=180 width=240></a></center>';
	var infoWindow10 = new google.maps.InfoWindow({content: contentString10, maxWidth: 270});
	
	var placeMarker11 = new google.maps.Marker({position: new google.maps.LatLng(35.77823, -78.63825), icon: hIcon});
	placeMarker11.setMap(map);
	google.maps.event.addListener(placeMarker11, 'click', function() {infoWindow11.open(map,placeMarker11);});
	var contentString11 = '<center><b>Hargett Street</b><br />Home of many places to visit both new and old.<a href="vnd.youtube://Q8xPL92vfiU"><img src="http://i.ytimg.com/vi/Q8xPL92vfiU/0.jpg" height=180 width=240></a></center>';
	var infoWindow11 = new google.maps.InfoWindow({content: contentString11, maxWidth: 270});
	
	var placeMarker12 = new google.maps.Marker({position: new google.maps.LatLng(35.77529, -78.63943), icon: hIcon});
	placeMarker12.setMap(map);
	google.maps.event.addListener(placeMarker12, 'click', function() {infoWindow12.open(map,placeMarker12);});
	var contentString12 = '<center><b>Sir Walter Raleigh Hotel</b><br />The oldest surviving hotel building in Raleigh, North Carolina<a href="vnd.youtube://W8p4ROd3Qrc"><img src="http://i.ytimg.com/vi/W8p4ROd3Qrc/0.jpg" height=180 width=240></a></center>';
	var infoWindow12 = new google.maps.InfoWindow({content: contentString12, maxWidth: 270});
	
	var placeMarker13 = new google.maps.Marker({position: new google.maps.LatLng(35.7765, -78.63599), icon: hIcon});
	placeMarker13.setMap(map);
	google.maps.event.addListener(placeMarker13, 'click', function() {infoWindow13.open(map,placeMarker13);});
	var contentString13 = '<center><b>City Market</b><br />A market located in Raleigh which was first founded in October, 1914.<a href="vnd.youtube://IVHO6ZOedZs"><img src="http://i.ytimg.com/vi/IVHO6ZOedZs/0.jpg" height=180 width=240></a></center>';
	var infoWindow13 = new google.maps.InfoWindow({content: contentString13, maxWidth: 270});
	
	var placeMarker14 = new google.maps.Marker({position: new google.maps.LatLng(35.7765, -78.64614), icon: hIcon});
	placeMarker14.setMap(map);
	google.maps.event.addListener(placeMarker14, 'click', function() {infoWindow14.open(map,placeMarker14);});
	var contentString14 = '<center><b>Warehouse District</b><br />Named after its historical function, the warehouse district is taking on a new life of its own.<a href="vnd.youtube://Y-7IqK0dvKE"><img src="http://i.ytimg.com/vi/Y-7IqK0dvKE/0.jpg" height=180 width=240></a></center>';
	var infoWindow14 = new google.maps.InfoWindow({content: contentString14, maxWidth: 270});
	
	var placeMarker15 = new google.maps.Marker({position: new google.maps.LatLng(35.78045, -78.63909), icon: hIcon});
	placeMarker15.setMap(map);
	google.maps.event.addListener(placeMarker15, 'click', function() {infoWindow15.open(map,placeMarker15);});
	var contentString15 = '<center><b>Capitol Building</b><br />No longer used by the General Assembly, this building is one of the best known landmarks in Raleigh.<a href="vnd.youtube://2daoaldrLTQ"><img src="http://i.ytimg.com/vi/2daoaldrLTQ/0.jpg" height=180 width=240></a></center>';
	var infoWindow15 = new google.maps.InfoWindow({content: contentString15, maxWidth: 270});
	
	var placeMarker16 = new google.maps.Marker({position: new google.maps.LatLng(35.7759, -78.63926), icon: hIcon});
	placeMarker16.setMap(map);
	google.maps.event.addListener(placeMarker16, 'click', function() {infoWindow16.open(map,placeMarker16);});
	var contentString16 = '<center><b>Hudson Belk Building</b><br />The building was originally built in the 1930s and for many years served as the main department store in downtown Raleigh.<a href="vnd.youtube://ozJeXP-1rFs"><img src="http://i.ytimg.com/vi/ozJeXP-1rFs/0.jpg" height=180 width=240></a></center>';
	var infoWindow16 = new google.maps.InfoWindow({content: contentString16, maxWidth: 270});
	
	var placeMarker17 = new google.maps.Marker({position: new google.maps.LatLng(35.789342, -78.660221), icon: eIcon});
	placeMarker17.setMap(map);
	google.maps.event.addListener(placeMarker17, 'click', function() {infoWindow17.open(map,placeMarker17);});
	var contentString17 = '<center><b>The Underground</b><br />A music scene which included many well known artists in their early days.<a href="vnd.youtube://5b1Wkx0bC1g"><img src="http://i.ytimg.com/vi/5b1Wkx0bC1g/0.jpg" height=180 width=240></a></center>';
	var infoWindow17 = new google.maps.InfoWindow({content: contentString17, maxWidth: 270});
	
	var placeMarker18 = new google.maps.Marker({position: new google.maps.LatLng(35.789477, -78.676448), icon: eIcon});
	placeMarker18.setMap(map);
	google.maps.event.addListener(placeMarker18, 'click', function() {infoWindow18.open(map,placeMarker18);});
	var contentString18 = '<center><b>The Brewery / PC Goodtimes</b><br />Another great piece of Raleigh history.<a href="vnd.youtube://GsX3rFyG3f8"><img src="http://i.ytimg.com/vi/ozJeXP-1rFs/0.jpg" height=180 width=240></a></center>';
	var infoWindow18 = new google.maps.InfoWindow({content: contentString18, maxWidth: 270});
	
	var placeMarker19 = new google.maps.Marker({position: new google.maps.LatLng(35.776948, -78.644015), icon: eIcon});
	placeMarker19.setMap(map);
	google.maps.event.addListener(placeMarker19, 'click', function() {infoWindow19.open(map,placeMarker19);});
	var contentString19 = '<center><b>DesignBox</b><br />An influential spot where a group of diverse independent creative professionals collaborate and work.<a href="vnd.youtube://GsX3rFyG3f8"><img src="http://i.ytimg.com/vi/ozJeXP-1rFs/0.jpg" height=180 width=240></a></center>';
	var infoWindow19 = new google.maps.InfoWindow({content: contentString19, maxWidth: 270});
	
	map.setZoom(16);
	map.setCenter(myLatLng);
  }

  function centerAt(latitude, longitude){
    myLatlng = new google.maps.LatLng(latitude,longitude);
    map.panTo(myLatlng);

    

  }

</script> 

</head> 

<body style="margin:0px; padding:0px;" onload="initialize()"> 

  <div id="map_canvas" style="width:100%; height:100%"></div> 

</body> 

</html> 