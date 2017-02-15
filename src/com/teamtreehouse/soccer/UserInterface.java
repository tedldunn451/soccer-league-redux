package com.teamtreehouse.soccer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class UserInterface {

    private Set<Team> league;
    private BufferedReader reader;
    private Map<String, String> mainMenu;
    private List<Player> availablePlayers;

    public UserInterface() {

        Player[] players = Players.load();
        Set<Player> alphaPlayers = new TreeSet<>();
        for (Player player : players) {
            alphaPlayers.add(player);
        }
        availablePlayers = new ArrayList<>(alphaPlayers);
        league = new TreeSet<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        mainMenu = new LinkedHashMap<>();
        mainMenu.put("1", "Create a new team");
        mainMenu.put("2", "Add a player to a team");
        mainMenu.put("3", "Remove a player from a team");
        mainMenu.put("4", "View a report of players grouped by height");
        mainMenu.put("5", "View a report of players grouped by experience level");
        mainMenu.put("6", "View a team roster");
        mainMenu.put("7", "Exit the program");
    }

    public String mainPrompt() throws IOException {

        System.out.println("\nYour available options are listed below:\n");
        for (Map.Entry<String, String> menuItem : mainMenu.entrySet()) {
            System.out.printf("\t%s) %s%n", menuItem.getKey(), menuItem.getValue());
        }
        System.out.print("\nEnter the number for your selection: ");
        String selection = reader.readLine();
        return selection.trim();
    }

    public void run() {

        String selection = "";
        System.out.printf("There are currently %d registered players.%n", availablePlayers.size());
        do {
            try {
                selection = mainPrompt();


            } catch (IOException ioe){

                System.out.println("Invalid entry. Please enter a number between 1 and 7");
            }
        } while (!selection.equals("7"));
    }
}
