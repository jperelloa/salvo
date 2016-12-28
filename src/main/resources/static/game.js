$(function() {
//dibujar tabla1
var tableName = "#datos_tabla1";
makegrid(tableName);
//dibujar tabla2
var tableName = "#datos_tabla2";
makegrid(tableName);




//capturar parámetro url y JSON
var queryString = location.search;
var searchParam = paramObj(queryString);
var param = searchParam["gp"];
var urlText = "/api/game_view/"+ param +"";
var data = $.getJSON(urlText)
    .done(function (data) {
        playersInfo(param, data);
        for (var i = 0; i < data.ships.length; i++) {
            console.log("cuantas casillas " + data.ships[i].locations.length);

            for (var j = 0; j < data.ships[i].locations.length; j++) {
                console.log("aa " + data.ships[i].locations[j]);
                var casilla = data.ships[i].locations[j];
                var positionTable = ".T2" + casilla;
                console.log("AAAA " + positionTable);
                $(positionTable).addClass("ship");
                }
          }

      })
    .fail(function( jqXHR, textStatus ) {
          showOutput( "Failed: " + textStatus );
        });




    //capturar todo el parametro
function paramObj(search) {
  var obj = {};
  var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

  search.replace(reg, function(match, param, val) {
    obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
  });
  return obj;

}



//Hacer Grid
function makegrid(tableName) {
        var letras = [" ","A", "B", "C", "D", "E", "F", "G","H", "I" , "J"];
        var numeros = [1, 2, 3 ,4 , 5, 6, 7, 8, 9, 10];
        var table = $(tableName);
        for(var i = 0; i < 11; ++i){
            var tr = "<tr>";
            for(var j = 0; j < 10; ++j){
                     if (j == 0){
                        tr += "<td class='marker_row'>"+letras[i]+ "</td>";
                      }

                     if (i == 0){
                         tr += "<td class='marker_col'>"+numeros[j]+ "</td>";
                     } else {
                        //crea celdas poniendo clase según tabla y celda
                        if (tableName ==  "#datos_tabla1") {
                            shortNameTable = "T1";
                        } else {
                            shortNameTable = "T2";
                        }
                        var cellClass = shortNameTable + letras[i] + numeros[j];
                        tr += "<td class= "+cellClass+"></td>";
                     }
            }
            tr += "</tr>";
            table.append(tr);
        }

 }  //makegrid

//encuentra el jugador y el contrincante
function playersInfo(param, data) {
    console.log("QQQQQQQQ");
    console.log(data.gamePlayers[0].id);
    console.log(data.gamePlayers[1].id);
    console.log(data.gamePlayers[0].player.email);
    console.log(data.gamePlayers[1].player.email);
    if (data.gamePlayers[0].id == param) {
       var local = data.gamePlayers[0].player.email;
       var adversario = data.gamePlayers[1].player.email;
       } else {
            var local = data.gamePlayers[1].player.email;
            var adversario = data.gamePlayers[0].player.email;
       }

    console.log("LOCAL " + local);
    console.log("Adversario " + adversario);
    var playersTitle = local + " (you) vs " + adversario;
    $(".tittle").append('<p class="playersTitle">' +playersTitle+ '</p>');
}

})