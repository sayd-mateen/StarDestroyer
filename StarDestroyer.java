/*
 * Name: Sayd Mateen
 */

import java.util.*;

public class StarDestroyer {
    private static int maxDepth = 7;
    private static long time;
    private static char[][] board = new char[7][7];
    private static Vector<String> humanMoves = new Vector<>();
    private static Vector<String> computerMoves = new Vector<>();
    private static boolean computerMovedSideways = false;
    private static boolean humanMovedSideways = false;
    public static void main(String[] args){
        final String human = "HUMAN";
        final String computer = "COMPUTER";
        Scanner in = new Scanner(System.in);
        System.out.println("Would you like to go First(1) or Second(2)? Please enter the corresponding number.");
        int whoFirst = in.nextInt();
        // Initial Setup
        setUp(board);
        printBoard();
        setUpPossibleMoves(human);
        setUpPossibleMoves(computer);
        // IF Computer goes first
        if(whoFirst == 2){
            time = System.nanoTime();
            makeaMove();
            printBoard();
            setUpPossibleMoves(computer);
            //printPossibleMoves(computer);
        }
        // Loop Until Game is over
        for(;;) {
            // Gets Human Move and Prints Board
            checkGameOver();
            setUpPossibleMoves(human);
            printPossibleMoves(human);
            getaMove(in,humanMoves);
            printBoard();
            // Makes Computer Move and Prints Board
            checkGameOver();
            setUpPossibleMoves(computer);
            printPossibleMoves(computer);
            time = System.nanoTime();
            makeaMove();
            printBoard();
        }
    }
    public static void printPossibleMoves(String who){
        System.out.print("Possible "+ who + " Moves: ");
        if(who.equals("HUMAN")) {
            for (String s : humanMoves) {
                System.out.print(s + ",");
            }
            System.out.println(".");
        }else {
            for (String s : computerMoves) {
                System.out.print(s + ",");
            }
            System.out.println(".");
        }
    }
    public static void checkGameOver(){
        if(board[1][3] != '*' || computerMoves.isEmpty()){
            System.out.println("Human Won! Game Over.");
            System.exit(0);
        }
        if(board[5][3] != '@' || humanMoves.isEmpty()){
            System.out.println("Computer Won! Game Over.");
            System.exit(0);
        }
    }
    public static void makeaMove(){
        int best = -20000, depth = 0, score, yTo = 0, xTo = 0, yFrom = 0, xFrom = 0;
        int min = -20000;
        String savedMove = "";
        boolean sidewaysMove;
        Vector<String> humanCopy = new Vector<>();
        for(String move: computerMoves){
            int acrossFrom = convertToInt(move.charAt(0));
            int acrossTo = convertToInt(move.charAt(2));
            int downFrom = 7 - Character.getNumericValue(move.charAt(1));
            int downTo = 7 - Character.getNumericValue(move.charAt(3));
            //Make move on board
            char pieceFrom = board[downFrom][acrossFrom];
            char pieceTo = board[downTo][acrossTo];
            board[downTo][acrossTo] = pieceFrom;
            board[downFrom][acrossFrom] = '-';
            sidewaysMove = computerMovedSideways;
            computerMovedSideways = checkIfSidewaysMove(move);
            //Calls min() to check Human Counter Move
            setUpPossibleMoves("HUMAN", humanCopy);
            score = min(depth+1, humanCopy, pieceTo, min);
            if(score > best){xTo = acrossTo; yTo = downTo; best = score; savedMove = move; yFrom = downFrom; xFrom = acrossFrom; }
            if(score > min) min = score;
            //Undo Move
            board[downFrom][acrossFrom] = pieceFrom;
            board[downTo][acrossTo] = pieceTo;
            computerMovedSideways = sidewaysMove;
        }
        System.out.println("COMPUTER MOVES: " + savedMove + " [" + getConversionString(savedMove) + "]");
        char chosenMove = board[yFrom][xFrom];
        board[yTo][xTo] = chosenMove;
        board[yFrom][xFrom] = '-';
        if(checkIfSidewaysMove(savedMove)){
            computerMovedSideways = true;
        }else{
            computerMovedSideways = false;
        }
    }
    public static int min(int depth, Vector<String> moves, char capturedPiece, int min){
        int best, score;
        long currentTime;
        boolean sidewaysMove;
        if(checkPiece(capturedPiece) == 5000) return checkPiece(capturedPiece)-depth;
        else best = checkPiece(capturedPiece)-depth;
        if(depth == maxDepth) return -20000;
        Vector<String> computerCopy = new Vector<>();
        for(String move: moves){
            int acrossFrom = convertToInt(move.charAt(0));
            int acrossTo = convertToInt(move.charAt(2));
            int downFrom = 7 - Character.getNumericValue(move.charAt(1));
            int downTo = 7 - Character.getNumericValue(move.charAt(3));
            //Make move on board
            char pieceFrom = board[downFrom][acrossFrom];
            char pieceTo = board[downTo][acrossTo];
            board[downTo][acrossTo] = pieceFrom;
            board[downFrom][acrossFrom] = '-';
            sidewaysMove = humanMovedSideways;
            humanMovedSideways = checkIfSidewaysMove(move);
            //Calls max() to check Computer Counter Move
            setUpPossibleMoves("COMPUTER", computerCopy);
            score = max(depth+1, computerCopy, pieceTo, best);
            if(score < best){ best = score; }
            //Undo Move
            board[downFrom][acrossFrom] = pieceFrom;
            board[downTo][acrossTo] = pieceTo;
            humanMovedSideways = sidewaysMove;
            if (score < min) return min;// Check
            /*currentTime = System.nanoTime() - time;
            if((double)currentTime/ 1000000000.0 >= 4.9){
                System.out.println("Depth reached: "+ depth + " Time: " + (double)currentTime/ 1000000000.0);
                return 0;
            }*/
        }
        return best;
    }
    public static int max(int depth, Vector<String> moves, char capturedPiece, int max) {
        int best, score;
        long currentTime;
        boolean sidewaysMove;
        if(checkPiece(capturedPiece) == -5000) return checkPiece(capturedPiece)+depth;
        else best = checkPiece(capturedPiece)+depth;
        if(depth == maxDepth) return 20000;
        Vector<String> humanCopy = new Vector<>();
        for(String move: moves){
            int acrossFrom = convertToInt(move.charAt(0));
            int acrossTo = convertToInt(move.charAt(2));
            int downFrom = 7 - Character.getNumericValue(move.charAt(1));
            int downTo = 7 - Character.getNumericValue(move.charAt(3));
            //Make move on board
            char pieceFrom = board[downFrom][acrossFrom];
            char pieceTo = board[downTo][acrossTo];
            board[downTo][acrossTo] = pieceFrom;
            board[downFrom][acrossFrom] = '-';
            sidewaysMove = computerMovedSideways;
            computerMovedSideways = checkIfSidewaysMove(move);
            //Calls min() to check Computer Counter Move
            setUpPossibleMoves("HUMAN", humanCopy);
            score = min(depth+1, humanCopy, pieceTo, best);
            if(score > best){ best = score; }
            //Undo Move
            board[downFrom][acrossFrom] = pieceFrom;
            board[downTo][acrossTo] = pieceTo;
            computerMovedSideways = sidewaysMove;
            if (score > max) return max;// Check
            /*currentTime = System.nanoTime() - time;
            if((double)currentTime/ 1000000000.0 >= 4.9){
                System.out.println("Depth reached: "+ depth + " Time: " + (double)currentTime/ 1000000000.0);
                return 0;
            }*/
        }
        return best;
    }
    public static int checkPiece(char capturedPiece){
        switch(capturedPiece){
            case '*':
                return -9999;
            case '@':
                return 10000;
            case 'X':
                return -10;
            case 'x':
                return 10;
            case 'T':
                return -7;
            case 't':
                return 7;
            default:
                return 0;
        }
    }
    public static void getaMove(Scanner in, Vector<String> possibleMoves){
        boolean moveValid = false;
        String move = "";
        while(!moveValid) {
            System.out.print("Enter your move: ");
            move = in.next();
            moveValid = validMove(move, "HUMAN", possibleMoves);
        }
        if(checkIfSidewaysMove(move)){
            humanMovedSideways = true;
        }else{
            humanMovedSideways = false;
        }
    }
    public static boolean checkIfSidewaysMove(String move){
        int acrossTo = convertToInt(move.charAt(2));
        int downFrom = 7 - Character.getNumericValue(move.charAt(1));
        int downTo = 7 - Character.getNumericValue(move.charAt(3));
        if(board[downTo][acrossTo] == 't' || board[downTo][acrossTo] == 'T'){
            if(downTo == downFrom){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    public static boolean validMove(String move, String who, Vector<String> possibleMoves){
        int acrossFrom = convertToInt(move.charAt(0));
        int acrossTo = convertToInt(move.charAt(2));
        int downFrom = 7 - Character.getNumericValue(move.charAt(1));
        int downTo = 7 - Character.getNumericValue(move.charAt(3));
        if(!possibleMoves.contains(move)) {
            System.out.println("Your move is NOT a possible move please try again.");
            return false;
        }
        char pieceFrom = board[downFrom][acrossFrom];
        board[downTo][acrossTo] = pieceFrom;
        board[downFrom][acrossFrom] = '-';
        System.out.println(who + " MOVES: " + move + " [" + getConversionString(move) + "]");
        return true;
    }
    public static void setUpPossibleMoves(String who){
        if(who.equals("COMPUTER")){
            computerMoves.clear();
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board[y].length; x++) {
                    char c = board[y][x];
                    int x2 = 1;
                    int y2 = 1;
                    switch (c) {
                        case 'T':
                            boolean up = true, down = true, left = true, right = true;
                            while (x2 < 7) {
                                if (up && y + y2 < 7 && board[y + y2][x] == '-') {
                                    computerMoves.add(getValidString(y, x, y + y2, x));
                                }else if(up && y + y2 < 7 && (board[y + y2][x] == 'x' || board[y + y2][x] == 't')){
                                    computerMoves.add(getValidString(y, x, y + y2, x));
                                    up = false;
                                }else{
                                    up = false;
                                }
                                if (down && y - y2 >= 0 && (board[y - y2][x] == '@'|| board[y - y2][x] == 'x' || board[y - y2][x] == 't')) {
                                    computerMoves.add(getValidString(y, x, y - y2, x));
                                    down = false;
                                }else {
                                    down = false;
                                }
                                if(!computerMovedSideways) {
                                    if (right && x + x2 < 7 && board[y][x + x2] == '-') {
                                        computerMoves.add(getValidString(y, x, y, x + x2));
                                    } else if (right && x + x2 < 7 && (board[y][x + x2] == 'x' || board[y][x + x2] == 't')) {
                                        computerMoves.add(getValidString(y, x, y, x + x2));
                                        right = false;
                                    } else {
                                        right = false;
                                    }
                                    if (left && x - x2 >= 0 && x - x2 >= 0 && board[y][x - x2] == '-') {
                                        computerMoves.add(getValidString(y, x, y, x - x2));
                                    } else if (left && x - x2 >= 0 && x - x2 >= 0 && (board[y][x - x2] == 'x' || board[y][x - x2] == 't')) {
                                        computerMoves.add(getValidString(y, x, y, x - x2));
                                        left = false;
                                    } else {
                                        left = false;
                                    }
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        case 'X':
                            boolean pp = true, pn = true, np = true, nn = true;
                            while (x2 < 7) {
                                if(pp && y + y2 < 7 && x + x2 < 7 && board[y + y2][x + x2] == '-'){
                                    computerMoves.add(getValidString(y, x, y + y2, x + x2));
                                }else if(pp && y + y2 < 7 && x + x2 < 7 && (board[y + y2][x + x2] == 'x' || board[y + y2][x + x2] == 't')){
                                    computerMoves.add(getValidString(y, x, y + y2, x + x2));
                                    pp = false;
                                }else{
                                    pp = false;
                                }
                                if(pn && y + y2 < 7 && x - x2 >= 0 && board[y + y2][x - x2] == '-' ){
                                    computerMoves.add(getValidString(y, x, y + y2, x - x2));
                                }else if(pn && y + y2 < 7 && x - x2 >= 0 && (board[y + y2][x - x2] == 'x' || board[y + y2][x - x2] == 't')){
                                    computerMoves.add(getValidString(y, x, y + y2, x - x2));
                                    pn = false;
                                }else{
                                    pn = false;
                                }
                                if(np && y - y2 >= 0 && x + x2 < 7 && (board[y - y2][x + x2] == '@'|| board[y - y2][x + x2] == 'x' || board[y - y2][x + x2] == 't')) {
                                    computerMoves.add(getValidString(y, x, y - y2, x + x2));
                                    np = false;
                                }else{
                                    np = false;
                                }
                                if(nn && y - y2 >= 0 && x - x2 >= 0 && (board[y - y2][x - x2] == '@'|| board[y - y2][x - x2] == 'x' || board[y - y2][x - x2] == 't')) {
                                    computerMoves.add(getValidString(y, x, y - y2, x - x2));
                                    nn = false;
                                }else {
                                    nn = false;
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }else {
            humanMoves.clear();
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board[y].length; x++) {
                    char c = board[y][x];
                    int x2 = 1;
                    int y2 = 1;
                    switch (c) {
                        case 't':
                            boolean up = true, down = true, left = true, right = true;
                            while (x2 < 7) {
                                if(up && y - y2 >= 0 && board[y - y2][x] == '-') {
                                    humanMoves.add(getValidString(y, x, y - y2, x));
                                }else if(up && y - y2 >= 0 && (board[y - y2][x] == 'X' || board[y - y2][x] == 'T')){
                                    humanMoves.add(getValidString(y, x, y - y2, x));
                                    up = false;
                                }else{
                                    up = false;
                                }
                                if (down && y + y2 < 7 && (board[y + y2][x] == '*'|| board[y + y2][x] == 'X' || board[y + y2][x] == 'T')) {
                                    humanMoves.add(getValidString(y, x, y + y2, x));
                                    down = false;
                                }else {
                                    down = false;
                                }
                                if(!humanMovedSideways) {
                                    if (right && x + x2 < 7 && board[y][x + x2] == '-') {
                                        humanMoves.add(getValidString(y, x, y, x + x2));
                                    } else if (right && x + x2 < 7 && (board[y][x + x2] == 'X' || board[y][x + x2] == 'T')) {
                                        humanMoves.add(getValidString(y, x, y, x + x2));
                                        right = false;
                                    } else {
                                        right = false;
                                    }
                                    if (left && x - x2 >= 0 && x - x2 >= 0 && board[y][x - x2] == '-') {
                                        humanMoves.add(getValidString(y, x, y, x - x2));
                                    } else if (left && x - x2 >= 0 && x - x2 >= 0 && (board[y][x - x2] == 'X' || board[y][x - x2] == 'T')) {
                                        humanMoves.add(getValidString(y, x, y, x - x2));
                                        left = false;
                                    } else {
                                        left = false;
                                    }
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        case 'x':
                            boolean pp = true, pn = true, np = true, nn = true;
                            while (x2 < 7) {
                                if(pp && y - y2 >= 0 && x + x2 < 7 && board[y - y2][x + x2] == '-'){
                                    humanMoves.add(getValidString(y, x, y - y2, x + x2));
                                }else if(pp && y - y2 >= 0 && x + x2 < 7 && (board[y - y2][x + x2] == 'X' || board[y - y2][x + x2] == 'T')){
                                    humanMoves.add(getValidString(y, x, y - y2, x + x2));
                                    pp = false;
                                }else{
                                    pp = false;
                                }
                                if(pn && y - y2 >= 0 && x - x2 >= 0 && board[y - y2][x - x2] == '-' ){
                                    humanMoves.add(getValidString(y, x, y - y2, x - x2));
                                }else if(pn && y - y2 >= 0 && x - x2 >= 0 && (board[y - y2][x - x2] == 'X' || board[y - y2][x - x2] == 'T')){
                                    humanMoves.add(getValidString(y, x, y - y2, x - x2));
                                    pn = false;
                                }else{
                                    pn = false;
                                }
                                if(np && y + y2 < 7 && x + x2 < 7 && (board[y + y2][x + x2] == '*'|| board[y + y2][x + x2] == 'X' || board[y + y2][x + x2] == 'T')) {
                                    humanMoves.add(getValidString(y, x, y + y2, x + x2));
                                    np = false;
                                }else{
                                    np = false;
                                }
                                if(nn && y + y2 < 7 && x - x2 >= 0 && (board[y + y2][x - x2] == '*'|| board[y + y2][x - x2] == 'X' || board[y + y2][x - x2] == 'T')) {
                                    humanMoves.add(getValidString(y, x, y + y2, x - x2));
                                    nn = false;
                                }else {
                                    nn = false;
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    public static void setUpPossibleMoves(String who, Vector<String> moves){
        if(who.equals("COMPUTER")){
            moves.clear();
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board[y].length; x++) {
                    char c = board[y][x];
                    int x2 = 1;
                    int y2 = 1;
                    switch (c) {
                        case 'T':
                            boolean up = true, down = true, left = true, right = true;
                            while (x2 < 7) {
                                if (up && y + y2 < 7 && board[y + y2][x] == '-') {
                                    moves.add(getValidString(y, x, y + y2, x));
                                }else if(up && y + y2 < 7 && (board[y + y2][x] == 'x' || board[y + y2][x] == 't')){
                                    moves.add(getValidString(y, x, y + y2, x));
                                    up = false;
                                }else{
                                    up = false;
                                }
                                if (down && y - y2 >= 0 && (board[y - y2][x] == '@'|| board[y - y2][x] == 'x' || board[y - y2][x] == 't')) {
                                    moves.add(getValidString(y, x, y - y2, x));
                                    down = false;
                                }else {
                                    down = false;
                                }
                                if(!computerMovedSideways) {
                                    if (right && x + x2 < 7 && board[y][x + x2] == '-') {
                                        moves.add(getValidString(y, x, y, x + x2));
                                    } else if (right && x + x2 < 7 && (board[y][x + x2] == 'x' || board[y][x + x2] == 't')) {
                                        moves.add(getValidString(y, x, y, x + x2));
                                        right = false;
                                    } else {
                                        right = false;
                                    }
                                    if (left && x - x2 >= 0 && x - x2 >= 0 && board[y][x - x2] == '-') {
                                        moves.add(getValidString(y, x, y, x - x2));
                                    } else if (left && x - x2 >= 0 && x - x2 >= 0 && (board[y][x - x2] == 'x' || board[y][x - x2] == 't')) {
                                        moves.add(getValidString(y, x, y, x - x2));
                                        left = false;
                                    } else {
                                        left = false;
                                    }
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        case 'X':
                            boolean pp = true, pn = true, np = true, nn = true;
                            while (x2 < 7) {
                                if(pp && y + y2 < 7 && x + x2 < 7 && board[y + y2][x + x2] == '-'){
                                    moves.add(getValidString(y, x, y + y2, x + x2));
                                }else if(pp && y + y2 < 7 && x + x2 < 7 && (board[y + y2][x + x2] == 'x' || board[y + y2][x + x2] == 't')){
                                    moves.add(getValidString(y, x, y + y2, x + x2));
                                    pp = false;
                                }else{
                                    pp = false;
                                }
                                if(pn && y + y2 < 7 && x - x2 >= 0 && board[y + y2][x - x2] == '-' ){
                                    moves.add(getValidString(y, x, y + y2, x - x2));
                                }else if(pn && y + y2 < 7 && x - x2 >= 0 && (board[y + y2][x - x2] == 'x' || board[y + y2][x - x2] == 't')){
                                    moves.add(getValidString(y, x, y + y2, x - x2));
                                    pn = false;
                                }else{
                                    pn = false;
                                }
                                if(np && y - y2 >= 0 && x + x2 < 7 && (board[y - y2][x + x2] == '@'|| board[y - y2][x + x2] == 'x' || board[y - y2][x + x2] == 't')) {
                                    moves.add(getValidString(y, x, y - y2, x + x2));
                                    np = false;
                                }else{
                                    np = false;
                                }
                                if(nn && y - y2 >= 0 && x - x2 >= 0 && (board[y - y2][x - x2] == '@'|| board[y - y2][x - x2] == 'x' || board[y - y2][x - x2] == 't')) {
                                    moves.add(getValidString(y, x, y - y2, x - x2));
                                    nn = false;
                                }else {
                                    nn = false;
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }else {
            moves.clear();
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board[y].length; x++) {
                    char c = board[y][x];
                    int x2 = 1;
                    int y2 = 1;
                    switch (c) {
                        case 't':
                            boolean up = true, down = true, left = true, right = true;
                            while (x2 < 7) {
                                if(up && y - y2 >= 0 && board[y - y2][x] == '-') {
                                    moves.add(getValidString(y, x, y - y2, x));
                                }else if(up && y - y2 >= 0 && (board[y - y2][x] == 'X' || board[y - y2][x] == 'T')){
                                    moves.add(getValidString(y, x, y - y2, x));
                                    up = false;
                                }else{
                                    up = false;
                                }
                                if (down && y + y2 < 7 && (board[y + y2][x] == '*'|| board[y + y2][x] == 'X' || board[y + y2][x] == 'T')) {
                                    moves.add(getValidString(y, x, y + y2, x));
                                    down = false;
                                }else {
                                    down = false;
                                }
                                if(!humanMovedSideways) {
                                    if (right && x + x2 < 7 && board[y][x + x2] == '-') {
                                        moves.add(getValidString(y, x, y, x + x2));
                                    } else if (right && x + x2 < 7 && (board[y][x + x2] == 'X' || board[y][x + x2] == 'T')) {
                                        moves.add(getValidString(y, x, y, x + x2));
                                        right = false;
                                    } else {
                                        right = false;
                                    }
                                    if (left && x - x2 >= 0 && x - x2 >= 0 && board[y][x - x2] == '-') {
                                        moves.add(getValidString(y, x, y, x - x2));
                                    } else if (left && x - x2 >= 0 && x - x2 >= 0 && (board[y][x - x2] == 'X' || board[y][x - x2] == 'T')) {
                                        moves.add(getValidString(y, x, y, x - x2));
                                        left = false;
                                    } else {
                                        left = false;
                                    }
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        case 'x':
                            boolean pp = true, pn = true, np = true, nn = true;
                            while (x2 < 7) {
                                if(pp && y - y2 >= 0 && x + x2 < 7 && board[y - y2][x + x2] == '-'){
                                    moves.add(getValidString(y, x, y - y2, x + x2));
                                }else if(pp && y - y2 >= 0 && x + x2 < 7 && (board[y - y2][x + x2] == 'X' || board[y - y2][x + x2] == 'T')){
                                    moves.add(getValidString(y, x, y - y2, x + x2));
                                    pp = false;
                                }else{
                                    pp = false;
                                }
                                if(pn && y - y2 >= 0 && x - x2 >= 0 && board[y - y2][x - x2] == '-' ){
                                    moves.add(getValidString(y, x, y - y2, x - x2));
                                }else if(pn && y - y2 >= 0 && x - x2 >= 0 && (board[y - y2][x - x2] == 'X' || board[y - y2][x - x2] == 'T')){
                                    moves.add(getValidString(y, x, y - y2, x - x2));
                                    pn = false;
                                }else{
                                    pn = false;
                                }
                                if(np && y + y2 < 7 && x + x2 < 7 && (board[y + y2][x + x2] == '*'|| board[y + y2][x + x2] == 'X' || board[y + y2][x + x2] == 'T')) {
                                    moves.add(getValidString(y, x, y + y2, x + x2));
                                    np = false;
                                }else{
                                    np = false;
                                }
                                if(nn && y + y2 < 7 && x - x2 >= 0 && (board[y + y2][x - x2] == '*'|| board[y + y2][x - x2] == 'X' || board[y + y2][x - x2] == 'T')) {
                                    moves.add(getValidString(y, x, y + y2, x - x2));
                                    nn = false;
                                }else {
                                    nn = false;
                                }
                                x2++;
                                y2++;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    public static String getValidString(int y1, int x1, int y2, int x2){
        y1 = 7 - y1;
        y2 = 7 - y2;
        return convertToChar(x1) + Integer.toString(y1) + convertToChar(x2) + Integer.toString(y2);
    }
    public static String getConversionString(String move){
        int acrossFrom = convertToInt(move.charAt(0));
        int acrossTo = convertToInt(move.charAt(2));
        int downFrom = 7- Character.getNumericValue(move.charAt(1));
        int downTo = 7- Character.getNumericValue(move.charAt(3));
        return convertToOpponent(acrossFrom) + Integer.toString(downFrom+1) + convertToOpponent(acrossTo) + Integer.toString(downTo+1);
    }
    public static char convertToOpponent(int l){
        switch(l){
            case 0:
                return 'G';
            case 1:
                return 'F';
            case 2:
                return 'E';
            case 3:
                return 'D';
            case 4:
                return 'C';
            case 5:
                return 'B';
            case 6:
                return 'A';
            default:
                System.out.println("Error Covert to char. ");
                return 'H';
        }
    }
    public static char convertToChar(int l){
        switch(l){
            case 0:
                return 'A';
            case 1:
                return 'B';
            case 2:
                return 'C';
            case 3:
                return 'D';
            case 4:
                return 'E';
            case 5:
                return 'F';
            case 6:
                return 'G';
            default:
                System.out.println("Error Covert to char. ");
                return 'H';
        }
    }
    public static int convertToInt(char l){
        switch(l){
            case 'A':
                return 0;
            case 'B':
                return 1;
            case 'C':
                return 2;
            case 'D':
                return 3;
            case 'E':
                return 4;
            case 'F':
                return 5;
            case 'G':
                return 6;
            default:
                System.out.println("Error invalid letter entered. ");
                return 7;
        }
    }
    public static void printBoard(){
        int count = 7;
        System.out.println("*****************************\n");
        for(int i=0; i< board.length; i++){
            System.out.print(count-- + "  ");
            for(int k=0; k< board[i].length; k++){
                System.out.print(board[i][k] + " ");
            }
            if(i==0) System.out.print("  COMPUTER");
            if(i==6) System.out.print("  HUMAN");
            System.out.println();
        }
        System.out.println("   A B C D E F G\n");
    }
    public static void setUp(char[][] arr){
        arr[0][1] = 'T';  arr[6][1] = 't';
        arr[0][2] = 'T';  arr[6][2] = 't';
        arr[0][4] = 'T';  arr[6][4] = 't';
        arr[0][5] = 'T';  arr[6][5] = 't';

        arr[1][2] = '%';  arr[5][2] = '#';
        arr[1][3] = '*';  arr[5][3] = '@';
        arr[1][4] = '%';  arr[5][4] = '#';

        arr[2][0] = 'X';  arr[4][0] = 'x';
        arr[2][1] = 'X';  arr[4][1] = 'x';
        arr[2][5] = 'X';  arr[4][5] = 'x';
        arr[2][6] = 'X';  arr[4][6] = 'x';

        for(int i=0; i< arr.length; i++){
            for (int k = 0; k < arr[i].length; k++){
                if(arr[i][k] == '\u0000'){
                    arr[i][k] = '-';
                }
            }
        }
    }
}
