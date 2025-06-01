import java.util.*;
import java.util.stream.Collectors;

public class MatchSimulator {

    static Player Striker;
    static Player NonStriker;
    static Player Bowler;
    static Player lastBowler = null;

    static Player[] Team1;
    static Player[] Team2;

    static float firstInningScore = 0;
    static int firstInningWicket = 0;
    static int target = 0;
    static float secondInningScore = 0;
    static int secondInningWicket = 0;

    static int balls = 0;
    static int wickets = 0;

    static int batterIndex = 0;
    static final int MAX_OVERS = 20;

    static Map<Player, Integer> bowlerOvers = new HashMap<>();
    static List<Player> eligibleBowlers = new ArrayList<>();

    static List<Player> allBatters = new ArrayList<>(); // For scoreboard

    static Random random = new Random();

    public MatchSimulator(Team team1, Team team2) {
        Team1 = team1.playing11;
        Team2 = team2.playing11;
    }

    void initInnings() {
        Striker = Team1[batterIndex++];
        NonStriker = Team1[batterIndex++];
        allBatters.add(Striker);
        allBatters.add(NonStriker);

        for (int i = Team2.length - 1; i >= Team2.length - 5; i--) {
            eligibleBowlers.add(Team2[i]);
            bowlerOvers.put(Team2[i], 0);
        }

        swapBowlers();
    }

    static void swapBowlers() {
        List<Player> available = eligibleBowlers.stream()
            .filter(p -> bowlerOvers.get(p) < 4 && p != lastBowler)
            .collect(Collectors.toList());
    
        if (available.isEmpty()) {
            // All remaining bowlers have either bowled 4 overs or just bowled last over
            available = eligibleBowlers.stream()
                .filter(p -> bowlerOvers.get(p) < 4)
                .collect(Collectors.toList());
        }
    
        if (available.isEmpty()) {
            System.out.println("⚠️ No bowlers left with overs.");
            return;
        }
    
        Bowler = available.get(random.nextInt(available.size()));
        lastBowler = Bowler;
    }

    static void handleWicket() {
        firstInningWicket++;
        if (batterIndex < Team1.length) {
            Striker = Team1[batterIndex++];
            allBatters.add(Striker);
            System.out.println("🚨 New batsman: " + Striker.name);
        } else {
            System.out.println("🛑 All Out!");
            Striker = null; // Optional: to block further play
        }
    }

    public static int simulateBall(Player batter, Player bowler) {
        float battingSkill = (
            batter.getAttribute("technique") +
            batter.getAttribute("shotSelection") +
            batter.getAttribute("power") +
            batter.getAttribute("strikeRate") +
            batter.getAttribute("injuryResistance") +
            batter.getAttribute("stamina") +
            batter.getAttribute("mentalToughness") +
            batter.getAttribute("matchLuck") +
            batter.getAttribute("recoveryRate")
        ) / 9;

        float bowlingSkill = (
            bowler.getAttribute("accuracy") +
            bowler.getAttribute("pace") +
            bowler.getAttribute("variation") +
            bowler.getAttribute("wicketTaking") +
            bowler.getAttribute("injuryResistance") +
            bowler.getAttribute("stamina") +
            bowler.getAttribute("mentalToughness") +
            bowler.getAttribute("matchLuck") +
            bowler.getAttribute("recoveryRate")
        ) / 9;

        float batterRoll = battingSkill + random.nextFloat() * 20 - 10;
        float bowlerRoll = bowlingSkill + random.nextFloat() * 20 - 10;

        if (batterRoll > bowlerRoll + 20) {
            batter.hit(6);
            bowler.concedeRuns(6);
            bowler.bowl(false);
            return 6;
        } else if (batterRoll > bowlerRoll + 10) {
            batter.hit(4);
            bowler.concedeRuns(4);
            bowler.bowl(false);
            return 4;
        } else if (batterRoll > bowlerRoll) {
            batter.hit(1);
            bowler.concedeRuns(1);
            swapBatters();
            bowler.bowl(false);
            return 1;
        } else if (bowlerRoll > batterRoll + 15) {
            batter.ballsFaced++;
            bowler.bowl(true);
            bowler.takeWicket();
            handleWicket();
            return 0;
        } else {
            batter.hit(0);
            bowler.bowl(true);
            return 0;
        }
    }

    static void swapBatters() {
        Player temp = Striker;
        Striker = NonStriker;
        NonStriker = temp;
    }

    public void simulateOver() {
        Scanner scanner = new Scanner(System.in);

        for (int over = 0; over < MAX_OVERS; over++) {
            if (firstInningWicket >= Team1.length - 1) break;

            System.out.println("\n🎯 Over " + (over + 1) + ": " + Bowler.name + " to " + Striker.name + "\n");

            for (int ball = 0; ball < 6; ball++) {
                if (Striker == null) break;

                System.out.print("Press Enter for next ball...");
                scanner.nextLine();

                firstInningScore += simulateBall(Striker, Bowler);
                balls++;
                displayScoreboard();

                if (firstInningWicket >= Team1.length - 1) break;
            }

            swapBatters();
            swapBowlers();
        }

        System.out.println("\n🏁 Innings Over");
        target = (int) firstInningScore;
    }

    public void displayScoreboard() {
        System.out.println("\n================== 🏏 SCOREBOARD 🏏 ==================");
        for (Player p : allBatters) {
            String mark = (p == Striker) ? "*" : "";
            System.out.printf("%-20s %3.0f (%2.0f balls) %s\n", p.name, p.runs, p.ballsFaced, mark);
        }
    
        System.out.println("------------------------------------------------------");
    
        int overs = balls / 6;
        int ballsInOver = balls % 6;
    
        int bowlerOversDone = (int)(Bowler.ballsBowled / 6);
        int bowlerBalls = (int)(Bowler.ballsBowled % 6);
    
        System.out.printf("Bowler       : %-20s Overs: %d.%d  Runs: %.0f  Wkts: %d  Dots: %.0f\n", 
            Bowler.name, bowlerOversDone, bowlerBalls, Bowler.runsConceded, Bowler.wicketsTaken, Bowler.dotBallsBowled);
    
        System.out.println("------------------------------------------------------");
    
        System.out.printf("TOTAL        : %d/%d  Overs: %d.%d\n", 
            (int) firstInningScore, firstInningWicket, overs, ballsInOver);
    
        System.out.println("======================================================\n");
    }
}