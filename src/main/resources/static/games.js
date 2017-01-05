$(function() {

var data = $.getJSON("/api/games")
.done(function (data) {
    createGameList(data);
})
.fail(function() {
    console.log( "error" );
  });

var dataScore = $.getJSON("/api/players")
.done(function (dataScore) {
    makeTableScore(dataScore);
})
.fail(function() {
    console.log( "error" );
  });


  function createGameList(data){
             for(var i = 0; i < data.length; i++) {
                 var arrayPlayers = [];
                 var players = "";
                 var dateMilli = data[i].creationDate;
                 var dateOk = makedate(dateMilli);
                 for (var j = 0; j < data[i].gamePlayers.length; j++){
                     arrayPlayers.push(data[i].gamePlayers[j].player.email);
                 }
                 if (!arrayPlayers[0]) { arrayPlayers[0] = ""};
                 if (!arrayPlayers[1]) { arrayPlayers[1] = ""};

                 players =   " , " +   arrayPlayers[0] + " , " +  arrayPlayers[1]  ;
                 $('#gamelist').append("<li> "+ dateOk +"   "+ players +"   </li>");
             }
    }


    function makedate(dateMilli) {
            var dateOk =  new Date(dateMilli);
            return dateOk;
    }


    function makeTableScore(dataScore) {
      console.log("SCORE " + dataScore.length);
        var table = $('#datos_tabla');
        for(var i = 0; i < dataScore.length; ++i){
                table.append(
                        $('<tr>').append($('<td class="nameColumn">').text(dataScore[i].email))
                                 .append($('<td>').text(dataScore[i].scores.total))
                                 .append($('<td>').text(dataScore[i].scores.won))
                                 .append($('<td>').text(dataScore[i].scores.lost))
                                 .append($('<td>').text(dataScore[i].scores.tied))
                 )
                //var tr = "<tr>";
                /*for(var j = 0; j < 5; ++j){
                            //crea celdas

                            var cellClass = "hola";
                            tr += "<td class= "+cellClass+"></td>";

                         }*/
                   //  $('<tr>').append($('<td>').text(dataScore.email))
                }
                tr += "</tr>";
                table.append(tr);
            }

})