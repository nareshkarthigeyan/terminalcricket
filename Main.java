public class Main {
    public static void main(String[] args) {

        Player[] CSK = {
            new Player("MS Dhoni", true),
            new Player("Ruturaj Gaikwad", true),
            new Player("Devon Conway", true),
            new Player("Moeen Ali", true),
            new Player("Shivam Dube", true),
            new Player("Ravindra Jadeja", true),
            new Player("Ben Stokes", true),
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
            new Player("Piyush Chawla", true),
            new Player("Jasprit Bumrah", true),
            new Player("Jason Behrendorff", true),
            new Player("Arjun Tendulkar", true)
        };
        // virat
        MatchSimulator match = new MatchSimulator(CSK, MI);
        match.initInnings();
        match.simulateOver();
        // System.out.println(result);
    }
}