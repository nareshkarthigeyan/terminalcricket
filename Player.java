import java.util.Random;

public class Player {
    String name;
    float totalRuns = 0;
    float totalWickets = 0;
    float totalBallsFaced = 0;
    float totalBallsBowled = 0;
    boolean rightHanded = true;

    float strikeRate = 0;
    float strikeRateLast10Balls = 0;

    float economy = 0;
    float dotBallsBowled = 0;
    float dotBallsFaced = 0;


    float totalDotBallsBowled = 0;
    float totalDotBallsFaced = 0;

    float ballsBowled = 0;
    int oversBowled = 0;

    float ballsFaced = 0;
    float runs = 0;

    float runsConceded;
    float totalRunsConceded;
    int wicketsTaken;
    int totalWicketsTaken;

    int centuries = 0;
    int halfCenturies = 0;
    int boundaries = 0;

    PlayerAttribute[] attributes = new PlayerAttribute[20];
    PlayerAttribute overallRating;

    public void concedeRuns(int runs) {
        this.runsConceded += runs;
    }

    public void takeWicket() {
        this.wicketsTaken++;
    }

    Player(String name, boolean isRightHanded){
        this.name = name;
        this.rightHanded = rightHanded;
        initAttributes();
    }
    
    Player(String name){
        this.name = name;
    }

    private void initAttributes(){
        String[] attributeNames = {
            // Batting
            "technique", "power", "consistency", "shotSelection", "strikeRate",
            // Bowling
            "pace", "spinControl", "accuracy", "variation", "wicketTaking",
            // Fielding
            "throwAccuracy", "catching", "agility", "groundCoverage", "stumpReflexes",
            // Luck/Fitness
            "injuryResistance", "stamina", "mentalToughness", "matchLuck", "recoveryRate"
        };
    
        Random random = new Random();
    
        float battingSum = 0;
        float bowlingSum = 0;
        float otherSum = 0;
    
        for (int i = 0; i < attributeNames.length; i++) {
            float value = 30 + random.nextFloat() * 70; // Random float between 30 and 100
            attributes[i] = new PlayerAttribute(attributeNames[i], value);
    
            if (i < 5) battingSum += value;
            else if (i < 10) bowlingSum += value;
            else otherSum += value;
        }
    
        float maxMainSkill = Math.max(battingSum, bowlingSum);
        float overallValue = (maxMainSkill + otherSum) / 15.0f;
        
        System.out.println("Name: " + this.name + " - Overall Rating: " + overallValue / 10);
        overallRating = new PlayerAttribute("overallRating", overallValue / 10);
    }

    public float getAttribute(String attrName) {
        for (PlayerAttribute attr : attributes) {
            if (attr.name.equals(attrName)) {
                return attr.value;
            }
        }
        return 0;
    }

    public void hit(float runs){
        this.runs += runs;
        this.totalBallsFaced++;
        this.ballsFaced++;

        if(runs == 4 || runs == 6){
            this.boundaries++;
        }

        if(this.runs < 100 && this.runs + runs >= 100){
            this.centuries++;
        }
        if(this.runs < 50 && this.runs + runs >= 50){
            this.halfCenturies++;
        }

        if(runs == 0){
            this.dotBallsFaced++;
            this.totalDotBallsFaced++;
        }
    }

    public void bowl(boolean dot){
        this.ballsBowled++;
        if(dot) 
        {
            this.dotBallsBowled++;
        }
    }



    public void initForMatch(){
        this.ballsBowled = 0;
        this.ballsFaced = 0;
        this.dotBallsBowled = 0;
        this.dotBallsFaced = 0;
        this.oversBowled = 0;
    }

    public void displayStats() {
        System.out.println("┌───────────────────────────────┐");
        System.out.printf ("│ %-29s │\n", name.toUpperCase());
        System.out.println("├───────────────────────────────┤");
    
        // Batting stats
        if (ballsFaced > 0) {
            float strikeRateThisMatch = (runs / ballsFaced) * 100;
            System.out.printf("│ Batting: %-5.0f (%-2.0f balls) SR: %-5.1f │\n", runs, ballsFaced, strikeRateThisMatch);
            System.out.printf("│ Boundaries: %-2d   Dots: %-3.0f           │\n", boundaries, dotBallsFaced);
        } else {
            System.out.println("│ Batting: Yet to bat               │");
        }
    
        // Bowling stats
        if (ballsBowled > 0) {
            float overs = ballsBowled / 6;
            float econ = totalBallsBowled > 0 ? (runs / overs) : 0;
            System.out.printf("│ Bowling: %.1f overs               │\n", overs);
            System.out.printf("│ Dots: %-3.0f   Economy: %-5.2f         │\n", dotBallsBowled, econ);
        } else {
            System.out.println("│ Bowling: Yet to bowl              │");
        }
    
        System.out.println("└───────────────────────────────┘");
    }

}