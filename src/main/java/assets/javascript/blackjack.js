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
    if (game.stillBet == true){
        alert ("You must bet a minimum of $2");
    } else{
        //Rebuild user hand now
        //First remove all user card divs from view
        $("#userCards").html("");
        //Iteratively add users cards from game.pHand
        for(i=0;i<game.pHand.length;i++){
            card = game.pHand[i];
            cardDiv = "<div><img src='/assets/cards/" + card.value + card.suit.charAt(0).toLowerCase() + ".png'></div>";
            $("#userCards").append(cardDiv);
        }
        $("#dealHit").html("Hit");
    }
}

$("#dealHit").click(function(){
    state = $("#dealHit").text();
    if(state == "Deal"){
        if (game.stillBet == true){
            $.ajax({
                    type: "POST",
                    url: "/deal",
                    data: JSON.stringify(game),
                    success: function(data, status){
                    //Display game data
                    displayUserCards(data);
                    game = data;
                    },
                    contentType:"application/json; charset=utf-8",
                    dataType:"json",
                 });
            } else{
                alert("You have already been dealt your hand");
            }
    }else if(state == "Hit"){
    //Put your code here tanner
    }
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