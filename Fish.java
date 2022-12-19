import java.awt.Image; //!!!!!
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

public abstract class Fish {

    protected String colors; // TODO devra etre supprimer car pas utiliser
    private final Image fishImage;
    private int id = 0; // TODO devra etre supprimer car pas utiliser
    protected int pos_x; // coordoneé X du poisson
    protected int pos_y; // coordoneé Y du poisson
    protected int target_x; // coordoneé X de la cible du poisson
    protected int target_y; // coordoneé Y de la cible du poisson
    protected int newTarget_X; // nouvelle coordoneé X de la cible du poisson
    protected int newTarget_Y; // nouvelle coordoneé Y de la cible du poisson
    protected boolean touchOb = false; // boolean qui s'active si on touche un bord
    protected int departureX; // TODO risque d'eter supprimer car non utiliser
    protected int departureY; // TODO risque d'eter supprimer car non utiliser
    protected int speed;
    protected int speedMax=99;
    protected int speedBasic=50; 
    protected int cpt=speed;
    protected int limite=100;
    protected boolean tqt=true;
    static boolean activeInsectivor=false;
    
    

    public Fish(String colors, int speedMax, String nameImage, int id,int speed) {

        ImageIcon iib = new ImageIcon(nameImage);
        fishImage = iib.getImage();
        this.colors = colors;
        this.id = id;
        this.speed =speed;
        pos_x = (int)(1 + (Math.random() * (Aquarium.getTaille()))); // -1 c'est pour eviter que le poisson appraisse directemet dans les bords
        pos_y = (int)(1 + (Math.random() * (Aquarium.getTaille())));

    }

    public Image getFish() {
        return fishImage;
    }

    // TODO devra etre supprimer car pas utiliser
    public String getColors() {
        return colors;
    }

    public int getX() {
        return pos_x;
    }

    public int getY() {
        return pos_y;
    }

    // TODO les deux méthodes setx et setY ne me seront pas utile donc je devrais bientot les supprimer  
    public void setX(int newX) {
        pos_x = newX;
    }

    public void setY(int newY) {
        pos_y = newY;
    }

    // TODO devra etre supprimer car pas utiliser
    public int getId() {
        return id;
    }

    private void move() {

        ArrayList < Integer > x_moveOptions = new ArrayList < Integer > ();
        ArrayList < Integer > y_moveOptions = new ArrayList < Integer > ();
        ArrayList < Double > distances = new ArrayList < Double > ();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int test_pos_x = pos_x + i * Aquarium.getPas();
                int test_pos_y = pos_y + j * Aquarium.getPas();
                if (isValidPosition(test_pos_x, test_pos_y)) {
                    x_moveOptions.add(test_pos_x);
                    y_moveOptions.add(test_pos_y);
                }
            }
        }

        for (int i = 0; i < x_moveOptions.size(); i++) {
            Double distance = getDistance(target_x, target_y, x_moveOptions.get(i), y_moveOptions.get(i));
            distances.add(distance);
        }

        double min = Collections.min(distances);
        int min_index = distances.indexOf(min);

        pos_x = x_moveOptions.get(min_index);
        pos_y = y_moveOptions.get(min_index);
    }

    private boolean isValidPosition(int pos_x, int pos_y) { // savoir si il sera dans le board et donc ici je met la position des o
        // objets qu'il ne doit pas toucher comme les obstacles

        boolean res = true;
        if (pos_y >= Aquarium.getHeights() - 2) {
            res = false;
        }

        if (pos_y < 2) {
            res = false;
        }

        // TODO demander au prof si ce code est de la duplication de code à et vérifier si c'est assez précis et grand comme obstacle
        for (int j = 0; j < Aquarium.listDeco.size(); j++) {

            if (pos_y >= Aquarium.listDeco.get(j).getY() - 15 && pos_y <= Aquarium.listDeco.get(j).getY() + 15 && pos_x == Aquarium.listDeco.get(j).getX() + 15) {
                res = false;
                touchOb = true;
            }

            if (pos_y >= Aquarium.listDeco.get(j).getY() - 15 && pos_y <= Aquarium.listDeco.get(j).getY() + 15 && pos_x == Aquarium.listDeco.get(j).getX() - 15) {
                res = false;
                touchOb = true;
            }

            if (pos_x >= Aquarium.listDeco.get(j).getX() - 15 && pos_x <= Aquarium.listDeco.get(j).getX() + 15 && pos_y == Aquarium.listDeco.get(j).getY() + 15) {
                res = false;
                touchOb = true;
            }

            if (pos_x >= Aquarium.listDeco.get(j).getX() - 15 && pos_x <= Aquarium.listDeco.get(j).getX() + 15 && pos_y == Aquarium.listDeco.get(j).getY() - 15) {
                res = false;
                touchOb = true;
            }

        }

        if (pos_x >= Aquarium.getLenghts() - 2) {
            res = false;
        }

        if (pos_x < 2) {
            res = false;
        }
        return res;
    }

    private double getDistance(int pos_x0, int pos_y0, int pos_x1, int pos_y1) {
        int x_dist = pos_x1 - pos_x0;
        int y_dist = pos_y1 - pos_y0;
        return Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
    }

    public void insectivor(){
        double distanceDepart= Integer.MAX_VALUE;
        for (int i = 0; i < Aquarium.listFish.size(); i++) {
          int x_dist = Aquarium.listFish.get(i).pos_x-this.pos_x;
          int y_dist = Aquarium.listFish.get(i).pos_y-this.pos_y;  
          double distance = Math.sqrt(Math.pow(x_dist, 2)+Math.pow(y_dist, 2)); 
          if(distance < distanceDepart && Aquarium.listFishFriend.get(i).pos_x != this.pos_x ){  
            newTarget_X = Aquarium.listFish.get(i).pos_x;
            newTarget_Y = Aquarium.listFish.get(i).pos_y;
            distanceDepart = distance;
            
          }  
        }
      }

    public void update() {

        if(tqt){
        for (int i = 0; i < Aquarium.listFish.size(); i++) {     
            if(Aquarium.listFish.get(i).cpt>limite){ 
                Aquarium.listFish.get(i).move(); 
                Aquarium.listFish.get(i).cpt=Aquarium.listFish.get(i).speed; 
            }else{
                cpt++;
            }
        }
        }
        if(activeInsectivor){
            insectivor();
        }

        for (int j = 0; j < Aquarium.listFish.size(); j++) {          
                if( this.hashCode()!= Aquarium.listFish.get(j).hashCode() && Aquarium.listFish.get(j).getColors().equals(this.getColors()) && Aquarium.listFish.get(j).pos_x == this.pos_x && Aquarium.listFish.get(j).pos_y == this.pos_y  ){ 
                    Fish fish = this;
                    Aquarium.removeFromListFish(fish);  
                    Aquarium.removeFromListFish(Aquarium.listFish.get(j));   
                    Aquarium.listFish.add((new FishBlue("orange",100, "Images/FishRed.png",0, 10)));  
                    Aquarium.listFish.add((new FishBlue("orange",100, "Images/FishRed.png",0, 10)));
                    Aquarium.listFish.add((new FishBlue("orange",100, "Images/FishRed.png",0, 10)));        
                }
        }
       

    }

}