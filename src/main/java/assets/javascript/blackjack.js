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
    } else{
       $("#userMoney").html("$" + game.pBank.toString());
       $("#userBetAmount").html("$" + game.pBet.toString());
       $("#dealerBetAmount").html("$" + game.pBet.toString());
    }
}

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