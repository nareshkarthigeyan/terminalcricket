import java.util.*;
import java.util.stream.Collectors;

public class MatchSimulator {

    static Player Striker;
    static Player NonStriker;
    static Player Bowler;
    static Player lastBowler = null;

    static Team Team1;
    static Team Team2;

    static Player[] Team1eleven;
    static Player[] Team2eleven;

    static int firstInningScore = 0;
    static int firstInningWicket = 0;
    static int target = 0;
    static int secondInningScore = 0;
    static int secondInningWicket = 0;

    static int balls = 0;
    static int wickets = 0;

    static int batterIndex = 0;
    static final int MAX_OVERS = 20;

    static Map<Player, Integer> bowlerOvers = new HashMap<>();
    static List<Player> eligibleBowlers = new ArrayList<>();

    static List<Player> allBatters = new ArrayList<>(); // For scoreboard

    static boolean isFirstInnings = true;
    static Random random = new Random();

    static List<Player> firstInningsBatters = new ArrayList<>();
    static List<Player> secondInningsBatters = new ArrayList<>();

    static int firstInningBalls = 0;
    static int secondInningBalls = 0;

    public MatchSimulator(Team team1, Team team2) {
        Team1 = team1;
        Team2 = team2;
        Team1eleven = team1.playing11;
        Team2eleven = team2.playing11;
    }

    void initInnings(Team team1, Team team2) {
        Team1eleven = team1.playing11;
        Team2eleven = team2.playing11;
        target = isFirstInnings ? 0 : target;
        batterIndex = 0;
        Striker = Team1eleven[batterIndex++];
        NonStriker = Team1eleven[batterIndex++];
        allBatters.add(Striker);
        allBatters.add(NonStriker);

        if (isFirstInnings) {
            firstInningsBatters.clear();
            firstInningsBatters.add(Striker);
            firstInningsBatters.add(NonStriker);
        } else {
            secondInningsBatters.clear();
            secondInningsBatters.add(Striker);
            secondInningsBatters.add(NonStriker);
        }

        for (int i = Team2eleven.length - 1; i >= Team2eleven.length - 5; i--) {
            eligibleBowlers.add(Team2eleven[i]);
            bowlerOvers.put(Team2eleven[i], 0);
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
        if(isFirstInnings){
            firstInningWicket++;
        } else {
            secondInningWicket++;
        }
        
        if (batterIndex < Team1eleven.length) {
            Striker = Team1eleven[batterIndex++];
            if (isFirstInnings) {
                firstInningsBatters.add(Striker);
            } else {
                secondInningsBatters.add(Striker);
            }
            System.out.println("🚨 New batsman: " + Striker.name);
        } else {
            System.out.println("🛑 All Out!");
            Striker = null;
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

    public void simulateInnings(Team battingTeam, Team bowlingTeam) {
        initInnings(battingTeam, bowlingTeam);
        int score = 0;
        for (int over = 0; over < MAX_OVERS; over++) {
            if (isFirstInnings && firstInningWicket >= Team1eleven.length - 1 || !isFirstInnings && secondInningWicket >= Team1eleven.length - 1 || secondInningScore > target ) break;

            Scanner scanner = new Scanner(System.in);

            System.out.println("\n🎯 Over " + (over + 1) + ": " + Bowler.name + " to " + Striker.name + "\n");

            for (int ball = 0; ball < 6; ball++) {
                if (Striker == null) break;

                System.out.print("Press Enter for next ball...");
                scanner.nextLine();

                score += simulateBall(Striker, Bowler);
                // target = isFirstInnings ? score : target;
                if(isFirstInnings){
                    firstInningScore = score;
                    target = score;
                } else {
                    secondInningScore = score;
                    if(secondInningScore > target){
                        System.out.println("Innings over. " + battingTeam.name + " won by " + (10 - battingTeam.wickets) + " wickets (" + (120 - (ball * over)) + " balls left)" );
                        break;
                    }
                }

                if (isFirstInnings) {
                    firstInningBalls++;
                } else {
                    secondInningBalls++;
                }

                displayScoreboard();

                if (isFirstInnings && firstInningWicket >= Team1eleven.length - 1 || !isFirstInnings && secondInningWicket >= Team1eleven.length - 1 || secondInningScore > target ) break;
            }

            swapBatters();
            swapBowlers();
        }

        System.out.println("\n🏁 Innings Over");
        target = isFirstInnings ? firstInningScore : target;
        isFirstInnings = false;
    }

    public void simulateMatch(){
        simulateInnings(Team1, Team2);
        System.out.println("Innigs over: Target: " + target + " to win in 20 overs");
        simulateInnings(Team2, Team1);
    }

    public void displayScoreboard() {
        List<Player> batters = isFirstInnings ? firstInningsBatters : secondInningsBatters;
        int score = isFirstInnings ? firstInningScore : secondInningScore;
        int wickets = isFirstInnings ? firstInningWicket : secondInningWicket;
        int ballsUsed = isFirstInnings ? firstInningBalls : secondInningBalls;
        int overs = ballsUsed / 6;
        int ballsInOver = ballsUsed % 6;

        System.out.println("\n================== 🏏 SCOREBOARD 🏏 ==================");
        for (Player p : batters) {
            String mark = (p == Striker || p == NonStriker) ? "*" : "";
            System.out.printf("%-20s %3.0f (%2.0f balls) %s\n", p.name, p.runs, p.ballsFaced, mark);
        }

        System.out.println("------------------------------------------------------");

        int bowlerOversDone = (int)(Bowler.ballsBowled / 6);
        int bowlerBalls = (int)(Bowler.ballsBowled % 6);

        System.out.printf("Bowler       : %-20s Overs: %d.%d  Runs: %.0f  Wkts: %d  Dots: %.0f\n", 
            Bowler.name, bowlerOversDone, bowlerBalls, Bowler.runsConceded, Bowler.wicketsTaken, Bowler.dotBallsBowled);

        System.out.println("------------------------------------------------------");

        System.out.printf("TOTAL        : %d/%d  Overs: %d.%d\n", score, wickets, overs, ballsInOver);

        if (!isFirstInnings) {
            int targetRuns = firstInningScore + 1;
            int runsRemaining = targetRuns - score;
            int totalBalls = MAX_OVERS * 6;
            int ballsRemaining = totalBalls - secondInningBalls;
            int remainingOvers = ballsRemaining / 6;
            int remainingBalls = ballsRemaining % 6;

            System.out.printf("TARGET       : %d (%d to win)\n", targetRuns, runsRemaining);
            System.out.printf("REQUIRED     : %d off %d balls (%d.%d overs)\n", 
                runsRemaining, ballsRemaining, remainingOvers, remainingBalls);
        }

        System.out.println("======================================================\n");
    }
}