package com.sherkhonov.app;

import com.github.freva.asciitable.AsciiTable;

import java.util.*;

import static java.util.Arrays.asList;

public class Game {
    Scanner scanner = new Scanner(System.in);
    SecureChooseMove secureChooseMove = new SecureChooseMove();

    public void startGame(String[] args) {
        do {
            if (Boolean.TRUE.equals(checkInputArgs(args))) {
                ArrayList<String> gameMoves = setListOfArgs(args);
                showMenu(gameMoves);
                try {
                    int botMove = secureChooseMove.getSecureMove(gameMoves);
                    System.out.print("Enter your move: ");
                    String command = scanner.nextLine();
                    if (command.equals("?")) showHint(args);
                    else if (command.equals("0")) System.exit(0);
                    else if (isInteger(command) && Integer.parseInt(command) <= gameMoves.size()) {
                        checkWinner(botMove, Integer.parseInt(command) - 1, args);
                        System.out.println("HMAC key: " + secureChooseMove.getKey());
                        System.out.println("==============");
                    } else {
                        System.out.println("Wrong command! try again!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println(String.format("Err: Count of ur args is [%s]. " +
                        "Count of args must be equals or more that 3, ODD and no repeats, please try again!", args.length));
                args = scanner.nextLine().split(" ");
            }
        } while (true);
    }

    public static boolean isInteger(final String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private ArrayList<String> setListOfArgs(String[] args) {
        return new ArrayList<>(asList(args));
    }

    private void showMenu(ArrayList<String> gameMoves) {
        for (int i = 0, j = 1; i < gameMoves.size(); i++, j++) {
            System.out.println(String.format("%s - %s", j, gameMoves.get(i)));
        }
        System.out.println("0 - Exit");
        System.out.println("? - Help");
    }

    private List<String> getWinLoseLine(String[] gm) {
        List<String> winLoseArray = new ArrayList<>();
        for (int i = 0; i < gm.length; i++) {
            if (i == 0) winLoseArray.add("Draw");
            else if (i <= gm.length / 2) winLoseArray.add("Lose");
            else winLoseArray.add("Win");
        }
        return winLoseArray;
    }

    private void showHint(String[] gm) {
        List<String> winLoseList = getWinLoseLine(gm);
        String[] headers = new String[gm.length + 1];
        headers[0] = String.format("You%nComp");
        String[][] tableData = new String[winLoseList.size()][];
        for (int i = 0, j = 1; i < winLoseList.size(); i++, j++) {
            List<String> temp = new ArrayList<>();
            temp.add(gm[i]);
            temp.addAll(winLoseList);
            tableData[i] = temp.toArray(new String[0]);
            Collections.rotate(winLoseList, 1);
            headers[j] = gm[i];
        }
        System.out.println(AsciiTable.getTable(headers, tableData));
    }

    private Boolean checkInputArgs(String[] gameMoves) {
        return (gameMoves.length >= 3 && gameMoves.length % 2 != 0 && checkRepeats(gameMoves));
    }

    private Boolean checkRepeats(String[] gameMoves) {
        HashSet<String> clearList = new HashSet<>(Arrays.asList(gameMoves));
        return clearList.size() == gameMoves.length;
    }

    private void checkWinner(int a, int b, String[] gm) {
        List<String> winLoseList = getWinLoseLine(gm);
        Collections.rotate(winLoseList, b);
        System.out.println(winLoseList.get(a));
        System.out.println("Computer move was: " + gm[a]);
    }

}
