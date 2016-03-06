var game;
$.getJSON("http://localhost:8080/game", function( data ) {
    display(data);
    game = data;
});

function display(game){
    console.log(game);

}

function displayMoney(game){
    //First try to update bets
    if(game.betError == true){
       alert("You cannot bet more than you have in the bank");
    }else{
       $("#userMoney").html("$" + game.pBank.toString());
       $("#userBetAmount").html("$" + game.pBet.toString());
       $("#dealerBetAmount").html("$" + game.pBet.toString());
    }
}

function displayUserCards(game){
        //Rebuild user hand now
        //First remove all user card divs from view
        $("#userCards").html("");
        //Iteratively add users cards from game.pHand
        for(i=0;i<game.pHand.length;i++){
            card = game.pHand[i];
            cardDiv = "<div><img src='/assets/cards/" + card.value + card.suit.charAt(0).toLowerCase() + ".png'></div>";
            $("#userCards").append(cardDiv);
        }
        //Show Card Counting Total
        $("#userCardTotal").html(game.pCCount);
}

function dealerTurn(game){
     $("#dalerCards").html("");
     //Iteratively add users cards from game.pHand
     for(i=0;i<game.dHand.length;i++){
         card = game.dHand[i];
         cardDiv = "<div><img src='/assets/cards/" + card.value + card.suit.charAt(0).toLowerCase() + ".png'></div>";
         $("#dealerCards").append(cardDiv);
     }
     if(game.playerWin == true){
        $("#userMsg").html("You Won!");
     } else{
        $("#userMsg").html("You Lost");
     }

     $("#dealerCardTotal").html(game.dCCount);
}


$("#deal").click(function(){
        if (game.stillBet == true){
            $.ajax({
                    type: "POST",
                    url: "/deal",
                    data: JSON.stringify(game),
                    success: function(data, status){
                    //Display game data
                    displayUserCards(data);
                    game = data;
                    if(game.stillBet == false){
                            if(game.canSplit == true){
                                $("#userMsg").html("Hit, Double Down, Stand, or Split");
                                $("#split").prop("disabled", false);
                            } else{
                                $("#userMsg").html("Hit, Double Down, or Stand");
                            }
                            $("#doubleDown").prop("disabled", false);
                            $("#hit").prop("disabled", false);
                            $("#deal").prop("disabled", true);
                            $("#playAgain").prop("disabled", true);
                            $("#userBetButtons > button").prop("disabled", true);
                     } else{
                        alert("You must bet at least $2");
                     }
                    },
                    contentType:"application/json; charset=utf-8",
                    dataType:"json",
                 });
            } else{
                alert("You have already been dealt your hand");
}
});
$("#hit").click(function(){
            $.ajax({
               type: "POST",
               url: "/hit",
               data: JSON.stringify(game),
               success: function(data, status){
               //Display game data
               displayUserCards(data);
               game = data;
               $("#userMsg").html("Hit or Stand");
               $("#doubleDown").prop("disabled", true);
               $("#split").prop("disabled", true);
               if(game.bust == true){
                 $("#userMsg").html("You bust");
                 $("#hit").prop("disabled", true);
                 $("#playAgain").prop("disabled", false);
                 displayMoney(game);
               }
               },
               contentType:"application/json; charset=utf-8",
               dataType:"json",
            });
});

$("#playAgain").click(function(){
        $.ajax({
                  type: "POST",
                  url: "/newHand",
                  data: JSON.stringify(game),
                  success: function(data, status){
                  //Display game data
                  displayMoney(data);
                  game = data;
                  playAgain();
                  },
                  contentType:"application/json; charset=utf-8",
                  dataType:"json",
         });
});

function playAgain(){
    $("#userCards").html("");
    $("#dealerCards").html("");
    $("#deal").prop("disabled", false);
    $("#userCardTotal").html("");
    $("#dealerCardTotal").html("");
    $("#userBetButtons > button").prop("disabled",false);
    $("#userMsg").html("Place your bet");
    $("#doubleDown").prop("disabled",true);
    $("#split").prop("disabled",true);
    $("#hit").prop("disabled",true);
}

$("#doubleDown").click(function(){
        $.ajax({
                  type: "POST",
                  url: "/doubleDown",
                  data: JSON.stringify(game),
                  success: function(data, status){
                  //Display game data
                  displayMoney(data);
                  game = data;
                  displayUserCards(game);
                  if(game.bust == true){
                    $("#userMsg").html("You bust");
                  } else{
                  dealerTurn(game);
                  }
                  $("#hit").prop("disabled", true);
                  $("#stand").prop("disabled", true);
                  $("#split").prop("disabled", true);
                  $("#doubleDown").prop("disabled", true);
                  $("#playAgain").prop("disabled", false);
                  },
                  contentType:"application/json; charset=utf-8",
                  dataType:"json",
         });
});


//Bet button clicks:
function userBetFun(bet){
    //Send game data to /bet/{amount}
    $.ajax({
        type: "POST",
        url: "/bet/" + bet,
        data: JSON.stringify(game),
        success: function(data, status){
        console.log("Data: " + data + "\nStatus: " + status);
        //Display game data
        displayMoney(data);
        game = data;},
        contentType:"application/json; charset=utf-8",
        dataType:"json",
     });

}