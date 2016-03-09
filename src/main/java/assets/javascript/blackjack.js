var game;
$.getJSON("http://localhost:8080/game", function( data ) {
    display(data);
    game = data;
});

function display(game){
    console.log(game);
    //Rebuild user hand now
    //First remove all user card divs from view
    $("#userCards").html("");
    //Iteratively add users cards from game.pHand
    for(i=0;i<game.player.hand.length;i++){
        card = game.player.hand[i];
        cardDiv = "<div><img src='/assets/cards/" + card.value + card.suit.charAt(0).toLowerCase() + ".png'></div>";
        $("#userCards").append(cardDiv);
    }
    //Show player Card Counting Total if greater than zero
    if(game.player.count > 0){
        $("#userCardTotal").html(game.player.count);
    } else{
        $("#userCardTotal").html("");
    }

    //Rebuild Dealer Hand
     $("#dealerCards").html("");
     //Iteratively add users cards from game.dHand
     for(i=0;i<game.dealer.hand.length;i++){
         card = game.dealer.hand[i];
         cardDiv = "<div><img src='/assets/cards/" + card.value + card.suit.charAt(0).toLowerCase() + ".png'></div>";
         $("#dealerCards").append(cardDiv);
     }
     //Show dealer Card counting total if greater than zero
     if(game.dealer.count > 0){
             $("#dealerCardTotal").html(game.dealer.count);
         } else{
             $("#dealerCardTotal").html("");
         }

     //Display Money
     $("#userMoney").html("$" + game.player.bank.toString());
     $("#userBetAmount").html("$" + game.player.bet.toString());
     $("#dealerBetAmount").html("$" + game.dealer.bet.toString());

     //Disable required buttons
     $("#playAgain").prop("disabled",game.againDisabled);
     $("#deal").prop("disabled",game.dealDisabled);
     $("#hit").prop("disabled",game.hitDisabled);
     $("#stand").prop("disabled",game.standDisabled);
     $("#split").prop("disabled",game.splitDisabled);
     $("#doubleDown").prop("disabled",game.doubleDisabled);
     $("#userBetButtons > button").prop("disabled",game.bettingDisabled);

     //Display userMessage as error or in userMsg
     //Depending on whether errorFlag is true
     if(game.errorFlag){
        alert(game.userMessage);
        game.errorFlag = false;
     }else{
        $("#userMsg").html(game.userMessage);
     }
}




$("#deal").click(function(){
            $.ajax({
                    type: "POST",
                    url: "/deal",
                    data: JSON.stringify(game),
                    success: function(data, status){
                    //Display game data
                    display(data);
                    game = data;
                    },
                    contentType:"application/json; charset=utf-8",
                    dataType:"json",
                 });
});
$("#hit").click(function(){
            $.ajax({
               type: "POST",
               url: "/hit",
               data: JSON.stringify(game),
               success: function(data, status){
               //Display game data
               display(data);
               game = data;
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
                  display(data);
                  game = data;
                  },
                  contentType:"application/json; charset=utf-8",
                  dataType:"json",
         });
});


$("#doubleDown").click(function(){
        $.ajax({
                  type: "POST",
                  url: "/doubleDown",
                  data: JSON.stringify(game),
                  success: function(data, status){
                  //Display game data
                  display(data);
                  game = data;
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
        display(data);
        game = data;},
        contentType:"application/json; charset=utf-8",
        dataType:"json",
     });

}