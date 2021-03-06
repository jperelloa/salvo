$(function() {
    if (sessionStorage.getItem("nombre")) {
         var data = $.getJSON("/api/manager")
        .done(function (data) {
             $("#login-form").hide();
             $("#newgame").show();
             $("#logout").show();
             console.log("login " + data.player.email);
             $("#usuariologado").text(data.player.email);
             createGameListLogin(data);
             // si clic sobre botón JOIN
              $(".join").on('click', function login(evt) {
                           evt.preventDefault();
                           var nameuser = sessionStorage.getItem("nombre");
                           var gamenumber = $("#joingame").attr("data-game");
                           console.log("GAMENUMBER " + gamenumber);
                           var url = "/api/games/" + gamenumber + "/players";
                           console.log("URL " + url);
                           $.post(url)
                               .done(function (response) {
                                     var newgp = response.gpid;
                                     console.log("newgp " + newgp);
                                     var url = " game.html?gp=" + newgp;
                                     console.log("URL " + url);
                                     location.href = url;
                                 });
             })
         })
         .fail(function() {
               console.log( "error" );
         });
    }else { console.log("NOOO");
          var data = $.getJSON("/api/games")
           .done(function (data) {
                  $("#newgame").hide();
                  $("#logout").hide();
                  $("#login-form").show();
                  createGameList(data);
            })
            .fail(function() {
                  console.log( "error" );
            });
    }



//TABLA PUNTUACIÖN
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


    function createGameListLogin(data){
                  console.log("gamelislogin");
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
                     //Si hay un solo jugador y no es el logado poner botón join
                     if (!arrayPlayers[1] && arrayPlayers[0] != data.player.email) {
                          var num_game = data.games[i].id;
                          var joinButton = '<input type="submit" name="join" value="JOIN" class="join" id="joingame"' + " " + "data-game=" + num_game  + '>'
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

    $('#login').click(function login(evt){
              evt.preventDefault();
              var form = evt.target.form;
              console.log("name" + form["username"].value);
              console.log("pass" + form["password"].value);
                    $.post("/api/login",
                   //  $.post("/login",
                     { name: form["username"].value,
                       pwd: form["password"].value })
                    .done(function (response) {
                       $("login-form").hide();
                       sessionStorage.setItem('nombre', form["username"].value);
                       location.reload();
                     })
                    .fail(function(response) {
                        if (response.status = 401) {
                              var errormsg = "Username or password incorrect";
                                    $("#error").text(errormsg)
                                               .addClass("errorMessage")
                                               .removeClass("okMessage")
                                               .show();
                         }
                    });
    })

     $('#logout').click(function login(evt){
                  evt.preventDefault();
                  $.post("/logout")
                         .done(function (response) {
                         console.log("log out");
                         sessionStorage.clear();
                         location.reload();
                           });
                   });



    //click en signup
    $('#signup').click(function login(evt){
                     evt.preventDefault();
                    var form = evt.target.form;
                    $.post("/api/players",
                         { name: form["username"].value,
                           pwd: form["password"].value })
                        .done(function (response) {
                            var errormsg = "Registered user. Login to enter"
                            $("#error").text(errormsg)
                                       .addClass("okMessage")
                                       .removeClass("errorMessage")
                                       .show();
                        })
                        .fail(function(response) {
                            var errormsg = response.responseText;
                            $("#error").text(errormsg)
                                       .addClass("errorMessage")
                                       .removeClass("okMessage")
                                       .show();
                        });
        })

        //click en newgame
       $('#newgame').click(function login(evt){
                      evt.preventDefault();
                      var nameuser = sessionStorage.getItem("nombre");
                      $.post("/api/games",
                             { name: nameuser })
                            .done(function (response) {
                                console.log("name" + nameuser);
                                location.reload();
                       });
        })


