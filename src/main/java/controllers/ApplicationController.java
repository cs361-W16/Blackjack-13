/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import ninja.Result;
import ninja.Results;
import ninja.Context;
import models.Game;

import com.google.inject.Singleton;
import ninja.params.PathParam;


@Singleton
public class ApplicationController {

    public Result index() {
        return Results.html();
    }

    public Result blackjack() {

        return Results.html().template("views/blackjack/blackjack.ftl.html");

    }

    public Result gameGet(){
        Game g = new Game();
        g.shuffle();
        return Results.json().render(g);
    }

    public Result betPost(Context context, @PathParam("amount") int amount, Game g){
        g.tryBet(amount);

        return Results.json().render(g);
    }

    public Result hitPost(Context context, Game g){
        g.tryHit();

        return Results.json().render(g);
    }
    public Result splitPost(Context context, Game g){
        g.split();

        return Results.json().render(g);
    }

    public Result hitTwoPost(Context context, Game g){
        g.hitTwo();

        return Results.json().render(g);
    }

    public Result standTwoPost(Context context, Game g){
        g.standTwo();

        return Results.json().render(g);
    }

    public Result doubleDownTwoPost(Context context, Game g){
        g.doubleDownTwo();

        return Results.json().render(g);
    }

    public Result standPost(Context context, Game g){
        g.tryStand();

        return Results.json().render(g);
    }


    public Result dealPost(Context context, Game g){
        g.tryDeal();
        return  Results.json().render(g);
    }

    public Result newHand(Context context, Game g){
        g.newHand();
        g.shuffle();
        return Results.json().render(g);
    }

    public Result doubleDown(Context context, Game g){
        g.doubleDown();
        return  Results.json().render(g);
    }

    public Result helloWorldJson() {
        
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";

        return Results.json().render(simplePojo);

    }
    
    public static class SimplePojo {

        public String content;
        
    }
}
