

public class Team {
    String name;
    Player[] allPlayers;
    Player[] playing11;
    int wins;
    int losses;
    int ties;
    int points;
    int runs;
    int wickets;
    float oversPlayed;


    public Team(String name, Player[] players) {
        this.name = name;
        this.playing11 = players;
        //TODO: HANDLE PLAYER GEM / ETC.
    }

    
}
