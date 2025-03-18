package justinrojas.battleshipgame;
public abstract class Ship {
    protected String name;
    protected int size;
    protected boolean placed;
    
    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.placed = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isPlaced() {
        return placed;
    }
    
    public void placeShip() {
        this.placed = true;
    }
    public void placeShip(boolean placed) {
        this.placed = placed;
    }
    public void setPlaced(boolean placed) {
        this.placed = placed;
    }


}
