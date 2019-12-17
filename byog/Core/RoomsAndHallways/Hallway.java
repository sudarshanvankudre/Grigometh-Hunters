package byog.Core.RoomsAndHallways;

public class Hallway extends Room {
    public Hallway(int x, int y, int length, String direction) {
        super(x, y, 3, 3);
        if (direction.equals("right")) {
            this.width = length;
        } else if (direction.equals("up")) {
            this.height = length;
        }
    }
}
