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
import com.codenjoy.dojo.services.PointImpl;
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
//        System.out.println(board.toString());

        return getPermittedDirection().toString();
    }

    /**
     * Method which defines the priority for snake direction
     *
     * @return best Direction for snake movement according to the shortest way without any additional verifications
     */

    private Direction directionPriority() {

        Point head = board.getHead();
        Point realApple = board.getApples().get(0);

        int headX = head.getX();
        int headY = head.getY();

        Point checkpoint = appleWayPoint(headX, headY, realApple.getX(), realApple.getY());

        int appleX = checkpoint.getX();
        int appleY = checkpoint.getY();

        if (headX == realApple.getX()) {
            appleX = realApple.getX();
            appleY = realApple.getY();
        }

        if (headY == realApple.getY()) {
            appleX = realApple.getX();
            appleY = realApple.getY();
        }

        if (appleX > headX && appleY < headY) {
            if (board.getSnakeDirection() == Direction.UP) return Direction.UP;

            return Direction.RIGHT;
        }

        if (appleX > headX && appleY > headY) {
            if (board.getSnakeDirection() == Direction.DOWN) return Direction.DOWN;

            return Direction.RIGHT;
        }

        if (appleX < headX && appleY > headY) {
            if (board.getSnakeDirection() == Direction.DOWN) return Direction.DOWN;
            return Direction.LEFT;
        }

        if (appleX < headX && appleY < headY) {
            if (board.getSnakeDirection() == Direction.UP) return Direction.UP;

            return Direction.LEFT;
        }

        if (appleX == headX && appleY < headY) return Direction.UP;

        if (appleX > headX && appleY == headY) return Direction.RIGHT;

        if (appleX == headX && appleY > headY) return Direction.DOWN;

        if (appleX < headX && appleY == headY) return Direction.LEFT;

        return board.getSnakeDirection();
    }

    /**
     * Method for cheking if expected direction is permitted
     *
     * @param snake    current snake direction
     * @param expected expected direction to apple
     * @return true if expected direction is not back direction for current snake direction
     */

    private boolean isPermittedDirection(Direction snake, Direction expected) {

        if (snake == Direction.UP && expected == Direction.DOWN) return false;

        if (snake == Direction.DOWN && expected == Direction.UP) return false;

        if (snake == Direction.RIGHT && expected == Direction.LEFT) return false;

        if (snake == Direction.LEFT && expected == Direction.RIGHT) return false;

        return true;
    }

    /**
     * @return permitted direction for snake with some extra verifications
     */

    private Direction getPermittedDirection() {

        Direction snakeDirection = board.getSnakeDirection();
        if (!isPermittedDirection(snakeDirection, directionPriority())) {

            if (snakeDirection == Direction.RIGHT || snakeDirection == Direction.LEFT) {
                if (board.getHead().getY() > board.getApples().get(0).getY()) {
                    return Direction.UP;
                } else {
                    return Direction.DOWN;
                }
            }
            if (snakeDirection == Direction.UP || snakeDirection == Direction.DOWN) {
                if (board.getHead().getX() > board.getApples().get(0).getX()) {
                    return Direction.RIGHT;
                } else {
                    return Direction.LEFT;
                }
            }
        }

        return directionPriority();
    }

    /**
     * @param headX  x coordinate of the snake head
     * @param headY  y coordinate of the snake head
     * @param appleX x coordinate of the real apple
     * @param appleY y coordinate of the real apple
     * @return the focus point for the snake also in cases when stone and apple are on the same axis
     */

    private Point appleWayPoint(int headX, int headY, int appleX, int appleY) {
        int appleWayX = appleX;
        int appleWayY = appleY;

        Point stone = board.getStones().get(0);

        if (appleX == stone.getX()) {

            appleWayX = getAppleCoordinate(headX, appleX);

        }
        if (appleY == stone.getY()) {

            appleWayY = getAppleCoordinate(headY, appleY);

        }

        return new PointImpl(appleWayX, appleWayY);
    }

    /**
     * @param headCoord  x or y coordinate of the snake head
     * @param appleCoord x or y coordinate of the real apple
     * @return tmp coordinate for snake to focus
     */
    private int getAppleCoordinate(int headCoord, int appleCoord) {
        if (headCoord > appleCoord) {
            return appleCoord + 1;
        } else if (headCoord < appleCoord) {
            return appleCoord - 1;
        } else {
            if (appleCoord != 0) {
                return appleCoord - 1;
            } else {
                return appleCoord + 1;
            }
        }
    }

    public static void main(String[] args) {
//        WebSocketRunner.runOnServer("192.168.1.1:8080", // to use for local server
        WebSocketRunner.run(WebSocketRunner.Host.REMOTE,  // to use for codenjoy.com server
                USER_NAME,
                new YourSolver(new RandomDice()),
                new Board());
    }

}
