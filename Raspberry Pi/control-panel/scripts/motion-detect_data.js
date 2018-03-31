$(function() {

  function requestData(chart){
    $.ajax({
      type: "GET",
      url: "scripts/get-movements-frequency.php" 
    })
    .done(function( data ) {
		 console.log(JSON.parse(data));
      chart.setData(JSON.parse(data));
      console.log("success");
    })
    .fail(function() {
      alert( "error occured" );
    });
  }

  var chart = Morris.Bar({
    element: 'morris-bar-chart',
    data: [0,0],
      xkey: 'time',
        ykeys: ['count'],
        labels: ['Frequency'],
        hideHover: 'auto',
        resize: true
  });

  requestData(chart);

});
