public class Main {
    public static void main(String[] args) {

        Player[] CSK = {
            new Player("Ruturaj Gaikwad", true),
            new Player("Devon Conway", true),
            new Player("Moeen Ali", true),
            new Player("Ben Stokes", true),
            new Player("Shivam Dube", true),
            new Player("Ravindra Jadeja", true),
            new Player("MS Dhoni", true),
            new Player("Deepak Chahar", true),
            new Player("Maheesh Theekshana", true),
            new Player("Matheesha Pathirana", true),
            new Player("Tushar Deshpande", true)
        };
        
        Player[] MI = {
            new Player("Rohit Sharma", true),
            new Player("Ishan Kishan", true),
            new Player("Suryakumar Yadav", true),
            new Player("Tilak Varma", true),
            new Player("Cameron Green", true),
            new Player("Hardik Pandya", true),
            new Player("Tim David", true),
            new Player("Krunal Pandya", true),
            new Player("Kagiso Rabada", true),
            new Player("Jasprit Bumrah", true),
            new Player("Tim Boult", true)
        };
        // virat
        Team csk = new Team("CSK", CSK);
        Team mi = new Team("MI", MI);
        MatchSimulator match = new MatchSimulator(csk, mi);
        match.initInnings();
        match.simulateOver();
        // System.out.println(result);
    }
}