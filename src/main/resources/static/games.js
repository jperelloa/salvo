$(function() {

var data = $.getJSON("/api/games")
.done(function (data) {
    console.log(data);
    console.log(data[0]);
    console.log(data[0].gamePlayers[0].player.email);
    createGameList(data);
})
.fail(function() {
    console.log( "error" );
  })
;





  function createGameList(data){
             for(var i = 0; i < data.length; i++) {
                 var arrayPlayers = [];
                 var players = "";
                 var dateMilli = data[i].creationDate;
                 var dateOk = makedate(dateMilli);
                 console.log ("AAA " + data[i].gamePlayers.length);
                 for (var j = 0; j < data[i].gamePlayers.length; j++){
                     arrayPlayers.push(data[i].gamePlayers[j].player.email);
                 }
                  if (!arrayPlayers[0]) { arrayPlayers[0] = ""};
                  if (!arrayPlayers[1]) { arrayPlayers[1] = ""};

                 players =   " , " +   arrayPlayers[0] + " , " +  arrayPlayers[1]  ;
                 console.log("PLAYERS " + players);
                // $('#gamelist').append("<li> "+ dateOk +"  "+ players +"  </li>");
                $('#gamelist').append("<li> "+ dateOk +"   "+ players +"   </li>");
             }
    }


    function makedate(dateMilli) {
            var dateOk =  new Date(dateMilli);
            return dateOk;
    }


})