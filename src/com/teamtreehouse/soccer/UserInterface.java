package com.teamtreehouse.soccer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Comparator.naturalOrder;

public class UserInterface {

    private Set<Team> league;
    private BufferedReader reader;
    private Map<String, String> mainMenu;
    private List<Player> availablePlayers;

    public UserInterface() {

        Player[] players = Players.load();
        Set<Player> alphaPlayers = new TreeSet<>(Arrays.asList(players));
        availablePlayers = new ArrayList<>(alphaPlayers);
        league = new TreeSet<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        mainMenu = new LinkedHashMap<>();
        mainMenu.put("1", "Create a new team");
        mainMenu.put("2", "Add a player to a team");
        mainMenu.put("3", "Remove a player from a team");
        mainMenu.put("4", "View a report of players grouped by height");
        mainMenu.put("5", "View a report of teams grouped by experience level");
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

                switch(selection) {
                    case "1":
                        // create a new team
                        displayTeamList();
                        createTeamPrompt();
                        pauseProgram();
                        break;

                    case "2":
                        // add a player to a team
                        displayPlayerList(availablePlayers);
                        Player playerToAdd = getAvailablePlayerPrompt();
                        displayTeamList();
                        Team addingTeam = getTeamPrompt();
                        addingTeam.addPlayerToTeam(playerToAdd);
                        availablePlayers.remove(playerToAdd);
                        pauseProgram();
                        break;

                    case "3":
                        // remove a player from a team
                        displayTeamList();
                        Team removingTeam = getTeamPrompt();
                        Set<Player> roster = removingTeam.getTeamRoster();
                        displayPlayerList(roster);
                        Player playerToRemove = getTeamPlayerPrompt(roster);
                        availablePlayers.add(playerToRemove);
                        availablePlayers.sort(naturalOrder());
                        removingTeam.removePlayerFromTeam(playerToRemove);
                        pauseProgram();
                        break;

                    case "4":
                        // display height report
                        displayTeamList();
                        Team teamToReport = getTeamPrompt();
                        displayHeightReport(teamToReport.getTeamRoster());
                        pauseProgram();
                        break;

                    case "5":

                        break;

                    case "6":
                        // view a team roster
                        displayTeamList();
                        Team teamToDisplay = getTeamPrompt();
                        displayPlayerList(teamToDisplay.getTeamRoster());
                        pauseProgram();
                        break;

                    case "7":
                        // exit the program
                        break;
                    default:
                        System.out.println("Unknown entry. Please try again.");
                        break;
                }
            } catch (IOException ioe){
                System.out.println("Invalid entry. Please enter a number between 1 and 7");
            }
        } while (!selection.equals("7"));
    }

    public void pauseProgram() throws IOException {

        System.out.print("\nPress the <Enter> key to continue...");
        reader.readLine();
    }

    public void displayTeamList() {

        System.out.printf("%n%-20s %-20s %-8s%n%n", "Team Name", "Coach Name", "Players");
        if (!league.isEmpty()) {
            int count = 1;
            for (Team team : league) {
                System.out.printf("%d) %s%n", count, String.valueOf(team));
                count++;
            }
        } else {
            System.out.println("No teams currently in the league");
        }
    }

    public void createTeamPrompt() throws IOException {

        System.out.print("\nEnter the new team name: ");
        String newTeamName = reader.readLine();

        System.out.print("Enter the new team coach's name: ");
        String coachName = reader.readLine();

        Team newTeam = new Team(newTeamName, coachName);
        league.add(newTeam);

        System.out.printf("%nTeam: %s coached by %s successfully created and added to the league%n",
                newTeam.getTeamName(), newTeam.getCoachName());
    }

    public Team getTeamPrompt() throws IOException {

        System.out.print("\nEnter the number for the team you would like to select: ");
        int teamNumber = Integer.parseInt(reader.readLine());
        Team[] teamArray = league.toArray(new Team[0]);
        return teamArray[teamNumber - 1];
    }

    public Player getTeamPlayerPrompt(Set<Player> roster) throws IOException {

        System.out.print("\nEnter the number for the player you would like to select: ");
        int playerNumber = Integer.parseInt(reader.readLine());
        Player[] teamPlayerArray = roster.toArray(new Player[0]);
        return teamPlayerArray[playerNumber - 1];
    }

    public void displayPlayerList(Collection<Player> players) {

        System.out.printf("%n%-24s %-10s %-6s%n%n", "Player Name", "Height", "Experience");
        int count = 1;
        for (Player player : players) {
            System.out.printf("%2d) %s%n", count, player);
            count++;
        }
    }

    public Player getAvailablePlayerPrompt() throws IOException {

        System.out.print("\nEnter the number for the player you would like to select: ");
        int playerNumber = Integer.parseInt(reader.readLine());
        return availablePlayers.get(playerNumber - 1);
    }

    private void displayHeightReport(TreeSet<Player> team) {

        List<Player> smallPlayers = new ArrayList<>();
        List<Player> mediumPlayers = new ArrayList<>();
        List<Player> tallPlayers = new ArrayList<>();
        String emptyList = "No players in this height range on this team.";

        for(Player player : team) {
            if(player.getHeightInInches() <= 40) {
                smallPlayers.add(player);
            } else if(player.getHeightInInches() <= 46) {
                mediumPlayers.add(player);
            } else {
                tallPlayers.add(player);
            }
        }

        System.out.println("\nPlayers 35-40 inches in height\n");
        if(smallPlayers.size() == 0) {
            System.out.println("\t" + emptyList);
        } else {
            for(int i = 0; i < smallPlayers.size(); i++) {
                System.out.println("\t" + smallPlayers.get(i));
            }
        }

        System.out.println("\nPlayers 41-46 inches in height\n");
        if(mediumPlayers.size() == 0) {
            System.out.println("\t" + emptyList);
        } else {
            for(int i = 0; i < mediumPlayers.size(); i++) {
                System.out.println("\t" + mediumPlayers.get(i));
            }
        }

        System.out.println("\nPlayers 47-50 inches in height\n");
        if(tallPlayers.size() == 0) {
            System.out.println("\t" + emptyList);
        } else {
            for(int i = 0; i < tallPlayers.size(); i++) {
                System.out.println("\t" + tallPlayers.get(i));
            }
        }
    }

}
