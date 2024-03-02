function drawThreadsPieChar(destinationContainer, title, dataPoints) {
  var chart = new CanvasJS.Chart(destinationContainer, {
    exportEnabled: true,
    animationEnabled: true,
    title:{
      text: title
    },
    legend:{
      cursor: "pointer"
    },
    data: [{
      type: "pie",
      showInLegend: true,
      toolTipContent: "{name}: <strong>{y}%</strong>",
      indexLabel: "{y}%",
      indexLabelPlacement: "inside",
      dataPoints
    }]
  });
  chart.render();
}
