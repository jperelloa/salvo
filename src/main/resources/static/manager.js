$(function() {

var data = $.getJSON("/api/manager")
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

/*$('.newGame').click(function(){
        alert("JJJJJJAAAAAAAIIIIIIIIGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
        alert(data.games.length);
       })*/


  function createGameList(data){
             for(var i = 0; i < data.games.length; i++) {
                 var arrayPlayers = [];
                 var players = "";
                 var dateMilli = data.games[i].creationDate;
                 var dateOk = makedate(dateMilli);
                 for (var j = 0; j < data.games[i].gamePlayers.length; j++){
                     arrayPlayers.push(data.games[i].gamePlayers[j].player.email);
                 }
                 if (!arrayPlayers[0]) { arrayPlayers[0] = ""};
                 if (!arrayPlayers[1]) { arrayPlayers[1] = ""};

                 players =   " , " +   arrayPlayers[0] + " , " +  arrayPlayers[1]  ;
                 if (!arrayPlayers[1] && arrayPlayers[0] != data.player.email) {
                      var num_game = data.games[i].id;
                    //  var joinButton = '<a class="join">' + "JOIN" + "</a>"
                       var joinButton = '<a class="join"' + " " + "data-game=" + num_game  + '>' + "JOIN" + "</a>"
                   }else {
                          joinButton = "";
                  }
                  console.log(joinButton);
             //Si game con usuario logado y 2 jugadores
             if ((arrayPlayers[0] === data.player.email || arrayPlayers[1] === data.player.email) &&  arrayPlayers[1] != "" ) {

                    var user1Id  = data.games[i].gamePlayers[0].player.id;
                    var user2Id  = data.games[i].gamePlayers[1].player.id;
                    //averiguar gameplayer(gp)
                    if (user1Id == data.player.id) {
                        var gp = data.games[i].gamePlayers[0].id;
                    } else {
                        gp = data.games[i].gamePlayers[1].id;
                    }
                   var newUrl = "game.html?gp=" + gp ;
                   console.log(newUrl);
                   var players = players.link(newUrl);
                }

                 $('#gamelist').append("<li> "+ dateOk +"   "+ players + " "  + joinButton + " " + "</li>");
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

        }
    }

})