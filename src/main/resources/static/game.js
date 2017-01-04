$(function() {

//capturar parámetro url y JSON
var queryString = location.search;
var searchParam = paramObj(queryString);
var param = searchParam["gp"];
var urlText = "/api/game_view/"+ param +"";
var data = $.getJSON(urlText)
    .done(function (data) {
        var myPlayer = playersInfo(param, data);
        //dibujar tabla1
        var tableName = "#datos_tabla1";
        makegrid(tableName, data, myPlayer);
        //dibujar tabla2
        var tableName = "#datos_tabla2";
        makegrid(tableName);
        putShips (data);
        mySalvoes(data,myPlayer, param);






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
function makegrid(tableName, data , myPlayer) {
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
                            var cell = letras[i] + numeros[j]
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



//encuentra el jugador y el contrincante y devuelve mi jugador
function playersInfo(param, data) {
    if (data.gamePlayers[0].id == param) {
       var local = data.gamePlayers[0].player.email;
       var adversario = data.gamePlayers[1].player.email;
       var myPlayer = data.gamePlayers[0].player.id;
       } else {
            var local = data.gamePlayers[1].player.email;
            var adversario = data.gamePlayers[0].player.email;
            var myPlayer = data.gamePlayers[1].player.id;
   }

    console.log("LOCAL " + local);
    console.log("Adversario " + adversario);
    var playersTitle = local + " (you) vs " + adversario;
    $(".tittle").append('<p class="playersTitle">' +playersTitle+ '</p>');
    return myPlayer;
}

//colocar barcos
function putShips(data) {
    for (var i = 0; i < data.ships.length; i++) {
              //  console.log("cuantas casillas " + data.ships[i].locations.length);
                for (var j = 0; j < data.ships[i].locations.length; j++) {
                //    console.log("aa " + data.ships[i].locations[j]);
                    var casilla = data.ships[i].locations[j];
                    var positionTable = ".T2" + casilla;
                  //  console.log("AAAA " + positionTable);
                    $(positionTable).addClass("ship");
                }
      }
}

//poner mis salvoes
function mySalvoes(data, myPlayer, param) {
    console.log("myplayer " + myPlayer);
    var paramNumber = Number(param);
    for (var i = 0; i < 2; i++) {                           //averigüo gp del contrario
            if (paramNumber !== data.gamePlayers[i].id) {   //para cuando hagamos tocados
              var gpAdversary =  data.gamePlayers[i].id;
             }
            if (myPlayer !== data.gamePlayers[i].player.id) { //averigüo player contrario
              var playerAdv =  data.gamePlayers[i].player.id;
           }
      }

    for (var turn in data.salvoes[myPlayer]){
         console.log("TURN " + turn);
         console.log("ZZZZZZZZ " + data.salvoes[myPlayer][turn]);
         console.log("player " + myPlayer);
        for(var i = 0; i < data.salvoes[myPlayer][turn].length; i++) {
             console.log("mochi " + data.salvoes[myPlayer][turn][i]);
            // checkTouched(gpAdversary, data.salvoes[myPlayer][turn][i]);
             var positionTable = ".T1" + data.salvoes[myPlayer][turn][i];
                                    console.log("CELDA " + positionTable);
                                    $(positionTable).addClass("water");
                                    $(positionTable).text(turn);
       }
           // turno contrario
        for(var i = 0; i < data.salvoes[playerAdv][turn].length; i++) {
                     console.log("tiros contrario " + data.salvoes[playerAdv][turn][i]);
                    var positionTable = ".T2" + data.salvoes[playerAdv][turn][i];
                    if ($(positionTable).hasClass("ship")) {
                     //var positionTable = ".T2" + data.salvoes[playerAdv][turn][i];
                                            console.log("CELDASHIP " + positionTable);
                                            $(positionTable).addClass("touched");
                                            $(positionTable).text(turn);
                     }
               }
    }


    }//mySalvoes


    //chequear tocados
  /*  function checkTouched(gpAdversary, cell) {
      console.log("GPADVERSARYYYYYYYYY " + gpAdversary);
      console.log("CEEEEELL " + cell);
      var urlText2 = "/api/game_view/"+ gpAdversary +"";
      var dataAdv = $.getJSON(urlText2);
          .done(function (data) {
             for (var i = 0; i < dataAdv.ships.length; i++) {
                            for (var j = 0; j < dataAdv.ships[i].locations.length; j++) {
                             //    console.log("aa " + data.ships[i].locations[j]);
                                 var casilla = dataAdv.ships[i].locations[j];
                                 if (cell == casilla) {
                                 var positionTable = ".T1" + casilla;
                               //  console.log("TTTTT " + positionTable);
                                 $(positionTable).addClass("touched");
                                 }
                             }
                   }
          }
    }*/


});


