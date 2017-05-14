package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private static final String USER_NAME = "shtramak@gmail.com";

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        System.out.println(board.toString());

        Point head = board.getHead();
        Point apple = board.getApples().get(0);

        int headX = head.getX();
        int headY = head.getY();

        int appleX = apple.getX();
        int appleY = apple.getY();

        return movePriority(appleX, appleY, headX, headY).toString();
    }

    private Direction movePriority(int appleX, int appleY, int headX, int headY) {

        if ((appleX > headX && appleY < headY) || (appleX > headX && appleY > headY)) return Direction.RIGHT;

        if ((appleX < headX && appleY > headY) || (appleX < headX && appleY < headY)) return Direction.LEFT;

        if (appleX == headX && appleY < headY) return Direction.UP;

        if (appleX > headX && appleY == headY) return Direction.RIGHT;

        if (appleX == headX && appleY > headY) return Direction.DOWN;

        if (appleX < headX && appleY == headY) return Direction.LEFT;

        return Direction.RIGHT;
    }

    public static void main(String[] args) {
//        WebSocketRunner.runOnServer("192.168.1.1:8080", // to use for local server
        WebSocketRunner.run(WebSocketRunner.Host.REMOTE,  // to use for codenjoy.com server
                USER_NAME,
                new YourSolver(new RandomDice()),
                new Board());
    }

}
