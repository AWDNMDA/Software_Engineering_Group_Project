package Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardSaver {
    static List<Board> boards = new ArrayList<>();
    static List<String> name = new ArrayList<>();
    Scanner scanner= new Scanner(System.in);
    public static void saveBoard(Board board,String n) {
        name.add(n);
        boards.add(board);
    }
    public static void displayBoards(){
        for(int i = 0; i < boards.size();i++){
            System.out.printf("%d. %s\n",i+1,name.get(i));
        }
    }
    public static Board getBoard(int index){
        if(index<1||index>boards.size()){
            System.out.println("The choice is invalid, we give you a default board");
            return new Board();
        }
        return boards.get(index-1);
    }
    public static Board loadBoard(Scanner scanner) {
        Board board;
        System.out.println("Which one do you want to load?");
        BoardSaver.displayBoards();
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        board = BoardSaver.getBoard(choice);
        return board;
    }
}
