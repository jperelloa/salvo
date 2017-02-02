$(function() {

//capturar parámetro url y JSON
var queryString = location.search;
var searchParam = paramObj(queryString);
var param = searchParam["gp"];
var urlText = "/api/game_view/"+ param +"";
var confirmships = [];
var numships = 0;
var data = $.getJSON(urlText)
    .done(function (data) {
        shipsForPut(data);
        var myPlayer = playersInfo(param, data);
       //dibujar tabla1
        var tableName = "#datos_tabla1";
        makegrid(tableName, data, myPlayer);
        //dibujar tabla2
        var tableName = "#datos_tabla2";
        makegrid(tableName, data);
        putShips (data);
        mySalvoes(data,myPlayer, param);
        playSalvos();
        hitsAnsSinks(data, myPlayer);
        var state = makeState(data);
        console.log("STATE ARRIBA " + state);
        if (state == 1 || state == 3) {
            setInterval(function() { location.reload(); }, 5000);
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
function makegrid(tableName, data , myPlayer) {
        var letras = [" ","A", "B", "C", "D", "E", "F", "G","H", "I" , "J"];
        var numeros = [1, 2, 3 ,4 , 5, 6, 7, 8, 9, 10];
        var table = $(tableName);
        $(".table1").css("pointer-events", "none");
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
                        var cell = letras[i] + numeros[j];
                        var cellClass = shortNameTable + letras[i] + numeros[j];
                        var number = findNumber(letras[i]);
                        var classes = cellClass + " " + "cell";
                        tr += "<td class= '"+classes+"' data-row="+number+" data-col="+numeros[j]+"  data-cell="+cell+"></td>";
                     }
            }
            tr += "</tr>";
            table.append(tr);
        }
        //click en celda
         $("#datos_tabla2 td").click(function () {
             var cell = $(this).attr("data-cell");
             var row = $(this).attr("data-row");
             var col = $(this).attr("data-col");
             if (sessionStorage.getItem("orientacion") != null && sessionStorage.getItem("numcells") != null) {
                var cellarray = makeArrayCells(cell,data,row,col);
                sessionStorage['cellarrayS']= JSON.stringify(cellarray);
             }
            for (var i = 0; i < cellarray.length; i++) {
                var positionOk = yaOcupado(cellarray[i], data);
                if (positionOk == "KO") {
                        $("#confirm").hide();
                        break;
                }
            }
            if (positionOk == "OK") {
                $("#confirm").show();
             }
           });

            $('#confirm').click(function login(evt) {
                                         evt.preventDefault();
                                         var shiparray=JSON.parse(sessionStorage['cellarrayS']);
                                         for (var i=0; i < shiparray.length; i++){
                                                var cellclass = ".T2" + shiparray[i];
                                                $(cellclass).css("background-color", "orangered");
                                                $(cellclass).addClass("prov");
                                                var shipname = sessionStorage.getItem("typeship");
                                                var long = shipname.length;
                                                var cadenanombre = shipname.substring(1,long);
                                                var shipclass = "." + cadenanombre;
                                                $(shipname).hide();
                                                $(shipclass).hide();
                                                $("#confirm").hide();
                                                $(".table2").css("pointer-events", "none");
                                         }
                                         numships = numships + 1;
                                         if (numships % 2 != 0 ){
                                            confirmships.push({
                                                         "shipType" : cadenanombre,
                                                         "shipList"  : shiparray,
                                                         });
                                         }

                                         if (numships == 10) {
                                                      $("#confirm").hide();
                                                      $(".shiplist").hide();
                                                      postships(confirmships);
                                         }
           });

 }  //makegrid


// post de los barcos
function postships(confirmships) {
        $.post({
          url: "api/games/players/"+param+"/ships",
          data: JSON.stringify(confirmships),
          contentType: "application/json"
        })

        location.reload();
}



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

    var playersTitle = local + " (you) vs " + adversario;
    $(".tittle").append('<p class="playersTitle">' +playersTitle+ '</p>');
    return myPlayer;
}

//colocar barcos
function putShips(data) {
    for (var i = 0; i < data.ships.length; i++) {
                for (var j = 0; j < data.ships[i].locations.length; j++) {
                    var casilla = data.ships[i].locations[j];
                    var positionTable = ".T2" + casilla;
                    $(positionTable).addClass("ship");
                }
      }
}


//poner mis salvoes
function mySalvoes(data, myPlayer, param) {
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
         for(var i = 0; i < data.salvoes[myPlayer][turn].length; i++) {
                var positionTable = ".T1" + data.salvoes[myPlayer][turn][i];
                $(positionTable).addClass("water");
                $(positionTable).text(turn);
         }
     }
           // turno contrario
       for (var turn in data.salvoes[playerAdv]){
            for(var i = 0; i < data.salvoes[playerAdv][turn].length; i++) {
                        var positionTable = ".T2" + data.salvoes[playerAdv][turn][i];
                        if ($(positionTable).hasClass("ship")) {
                            $(positionTable).addClass("touched");
                            $(positionTable).text(turn);
                        }
            }
        }

}//mySalvoes



     function  shipsForPut(data) {
        var long = data.ships.length;
        for (var i = 0; i < long; i++){
            var shiptype = data.ships[i].type;
            console.log("type " + shiptype);
            var shipid = "#" + shiptype;
            var shipclass = "." + shiptype;
            $(shipid).hide();
            $(shipclass).hide();
        }
     }



   // click en barco para colocar
   $('#carrier').click(function login(evt) {
                         evt.preventDefault();
                         var shiptype = "#carrier";
                         only1select();
                         selectingShip(shiptype);
    })
    $('#battleship').click(function login(evt) {
                             evt.preventDefault();
                             var shiptype = "#battleship";
                             only1select();
                             selectingShip(shiptype);
    })
    $('#destroyer').click(function login(evt) {
                             evt.preventDefault();
                             var shiptype = "#destroyer";
                             only1select();
                             selectingShip(shiptype);
        })
    $('#submarine').click(function login(evt) {
                                 evt.preventDefault();
                                 var shiptype = "#submarine";
                                 only1select();
                                 selectingShip(shiptype);
    })
    $('#patrol_boat').click(function login(evt) {
                                     evt.preventDefault();
                                     var shiptype = "#patrol_boat";
                                     only1select();
                                     selectingShip(shiptype);
     })


    function selectingShip(shiptype) {
        var longship = $(shiptype).attr("data-cells");
        $(shiptype).css("border", "3px solid red");
        $("#direction").css("display", "block");
        sessionStorage.setItem('numcells', longship);
        sessionStorage.setItem('typeship', shiptype);
    }


    function only1select(shiptype) {
        $(".selectship").css("border", "1px solid black");
        $(shiptype).css("border", "3px solid red");
        $(".table2").css("pointer-events", "visible");
    }

    //traducir letra a número
    function findNumber (letra) {
        if (letra == "A") number=1;
        if (letra == "B") number=2;
        if (letra == "C") number=3;
        if (letra == "D") number=4;
        if (letra == "E") number=5;
        if (letra == "F") number=6;
        if (letra == "G") number=7;
        if (letra == "H") number=8;
        if (letra == "I") number=9;
        if (letra == "J") number=10;
        return number;
    }


    $("input[name=position]").click(function () {
        var position = $('input:radio[name=position]:checked').val();
        sessionStorage.setItem('orientacion', position);
    });


    function makeArrayCells(cell, data, row, col) {
        $("#message").text("");
        $(".cell").css("background-color", "transparent");
        $(".ship").css("background-color", "yellow");
        $(".prov").css("background-color", "orangered");
        var casillaclass = ".T2" + cell;
        $(casillaclass).css("background-color", "orange");
        var cellarray = [];
        cellarray.push(cell);
        var orient = sessionStorage.getItem("orientacion");
        var long = sessionStorage.getItem("numcells");
        var colnumeric =  parseInt(col);
        var rownumeric =  parseInt(row);
        var letter = cell.substr(0,1);
        var newcol = 0;
        if (orient == "H") {
            for (var i = 1; i < long; i++) {
                newcol = colnumeric + i;
                if (newcol > 10) {
                     $("#message").text("OUT OF BOUNDS");
                     $("#confirm").hide();
                     return ;
                }
                var newcell = letter + newcol;
                var casillaclass = ".T2" + newcell;
                $(casillaclass).css("background-color", "orange");
                cellarray.push(newcell);
            }
             return cellarray;
        }
        if (orient == "V") {
            for (var i = 1; i < long; i++) {
                newrow = rownumeric + i;
                var casillaclass = ".T2" + cell;
                $(casillaclass).css("background-color", "orange");
                if (newrow > 10) {
                     $("#message").text("OUT OF BOUNDS");
                     $("#confirm").hide();
                     return;
                }
                var newletter = traduciraletra(newrow);
                var newcell = newletter + col;
                var casillaclass = ".T2" + newcell;
                $(casillaclass).css("background-color", "orange");
                cellarray.push(newcell);
            }
            return cellarray;
        }
   }


    //traducir número a letra
    function traduciraletra(newrow) {
                if (newrow == 2)  newletter = "B";
                if (newrow == 3)  newletter = "C";
                if (newrow == 4)  newletter = "D";
                if (newrow == 5)  newletter = "E";
                if (newrow == 6)  newletter = "F";
                if (newrow == 7)  newletter = "G";
                if (newrow == 8)  newletter = "H";
                if (newrow == 9)  newletter = "I";
                if (newrow == 10)  newletter = "J";
                return   newletter;
     }



    function yaOcupado(cell, data) {
        var casillaclass = ".T2" + cell;
        if ($(casillaclass).hasClass("prov")) {
            $("#message").text("INVALID POSITION");
            return "KO";
        } else{
            return "OK";
        }
    }


    function  playSalvos() {
        $(".table1").css("pointer-events", "visible");
        $("#confirm").hide();
        $("#fire").hide();
        var cell = "";
        var salvoarray=[];
        //click en celda
        $("#datos_tabla1 td").click(function () {
                var cell = $(this).attr("data-cell");
                var positionOk = yaTirado(cell, data);
                if (positionOk == "KO") {
                    $("#fire").hide();
                } else {
                    var long = salvoarray.length;
                    if (salvoarray.length < 5) {
                        $("#message").text("");
                        sessionStorage.setItem('shot', cell);
                        var cellclass = ".T1" + cell;
                        $(cellclass).css("background-color", "orange");
                        $(cellclass).addClass("prov");
                        salvoarray.push(cell);
                        $("#message").text((5 - salvoarray.length) + " SHOTS LEFT");
                     };
                     if (salvoarray.length >= 5) {
                        $("#message").text("CLICK FIRE!!");
                        $("#fire").show();
                        $(".table1").css("pointer-events", "none");
                         $('#fire').click(function login(evt) {
                                     evt.preventDefault();
                                     disparar(salvoarray);
                         });
                    }
                }

        });
    }


    function yaTirado(cell, data) {
            var casillaclass = ".T1" + cell;
            if ($(casillaclass).hasClass("prov") || $(casillaclass).hasClass("water") || $(casillaclass).hasClass("touched")) {
                $("#message").text("REPEATED SHOT");
                return "KO";
            } else{
                 return "OK";
            }
        }

    function  disparar(salvoarray) {
            $.post({
              url: "api/games/players/"+param+"/salvos",
              data: JSON.stringify(salvoarray),
              contentType: "application/json"
            })
            location.reload();
    }


    function hitsAnsSinks(data, myPlayer) {
        console.log("HITS AND SINKS")
        if (data.gamePlayers[0].player.id == myPlayer) {
            var playerAdv = data.gamePlayers[1].player.id;
        } else {
            var playerAdv = data.gamePlayers[0].player.id;
        }
        for (var player in data.history){
                            //por cada ship
                            for (var ship in data.history[player]) {
                              var long = data.history[player][ship].length;
                              var hitslist = [];
                              //por cada location
                              for (var i = 0; i < long; i++) {
                                  console.log("LOC " + data.history[player][ship][i]);
                                  if (data.history[player][ship][i] != "") {
                                      hitslist.push(data.history[player][ship][i]);
                                      if (player == myPlayer) {
                                          var casillaClass = ".T1" + data.history[player][ship][i];
                                      } else {
                                           var casillaClass = ".T2" + data.history[player][ship][i]
                                      }
                                          $(casillaClass).css("background-color", "orangered");
                                          $(casillaClass).css("background-image", "url('fuego.gif'");
                                          $(casillaClass).css("background-position-y", "center");
                                          $(casillaClass).addClass("touched");
                                          $(casillaClass).removeClass("water");

                                  }
                                 if (hitslist.length == data.history[player][ship].length) {
                                    for  (var loc in  hitslist)  {
                                         if (player == myPlayer) {
                                                 var casillaClass = ".T1" + hitslist[loc];
                                         } else {
                                                 var casillaClass = ".T2" + hitslist[loc];
                                         }
                                            $(casillaClass).css("background-color", "pink");
                                            $(casillaClass).removeClass("touched");
                                            $(casillaClass).addClass("sink");
                                            $(casillaClass).css("background-image", "url('calavera.gif'");
                                            $(casillaClass).css("background-position-x", "center");
                                            $(casillaClass).css("background-size", "cover");
                                            $(casillaClass).css("font-size", "0px");
                                     }
                                  }
                               }
                           }
                   //   }
        }
    }


    function makeState(data) {
            var state = data.state;
            console.log("state " + state);
            if (state == 0) {
             $("#message").text("PUT BOATS ON GRILL 2");
             $(".table1").css("pointer-events", "none");
            }
            if (state == 1) {
             $("#message").text("WAIT OPPONENTS' SHIPS");
             $(".table1").css("pointer-events", "none");
            }
            if (state == 2) $("#message").text("ENTER SALVO");
            if (state == 3) {
             $("#message").text("WAIT OPPONENTS' SALVO");
             $(".table1").css("pointer-events", "none");
            }
            if (state == 4) $("#message").text("TIED !!");
            if (state == 5) {
             $("#message").text("YOU WIN !!");
             $(".table1").css("pointer-events", "none");
            }
            if (state == 6) {
            $("#message").text("YOU LOST !!");
            $(".table1").css("pointer-events", "none");
            }
            return state;

    }






})

