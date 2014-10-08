package spacetrader;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import spacetrader.items.CargoBay;
import spacetrader.market.TradeGood;
import spacetrader.player.Player;

/**
 * FXML Controller class
 *
 * @author David Purcell
 */
public class CargoController implements Initializable, ControlledScreen {
    @FXML Pane cargoBay;
    @FXML Pane loadingBayWater;
    @FXML Pane loadingBayFurs;
    @FXML AnchorPane anchorPane;
    private ScreensController parentController;
    public final ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
    public final ArrayList<Rectangle> waterRectangles = new ArrayList<Rectangle>();
    public final ArrayList<Rectangle> furRectangles = new ArrayList<Rectangle>();
    private int resetX;
    private int resetY;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final Random random = new Random(); 
        Player tempPlayer = new Player();
        CargoBay tempCargoBay = new CargoBay(10); 
        Rectangle rect1 = new Rectangle(25,10,50,50);
        rect1.setFill(Color.BLUE);
        Rectangle rect2 = new Rectangle(25,70,100,50);
        rect2.setFill(Color.BLUE);
        Rectangle rect3 = new Rectangle(25,130,50,100);
        rect3.setFill(Color.BLUE);
        Rectangle rect4 = new Rectangle(25,240,100,100);
        rect4.setFill(Color.BLUE);
        waterRectangles.add(rect1);
        waterRectangles.add(rect2);
        waterRectangles.add(rect3);
        waterRectangles.add(rect4);
        Rectangle rect5 = new Rectangle(25,10,50,50);
        rect5.setFill(Color.BROWN);
        Rectangle rect6 = new Rectangle(25,70,100,50);
        rect6.setFill(Color.BROWN);
        Rectangle rect7 = new Rectangle(25,130,50,100);
        rect7.setFill(Color.BROWN);
        Rectangle rect8 = new Rectangle(25,240,100,100);
        rect8.setFill(Color.BROWN);
        furRectangles.add(rect5);
        furRectangles.add(rect6);
        furRectangles.add(rect7);
        furRectangles.add(rect8);

        for(Rectangle waterRectangle: waterRectangles){
            waterRectangle.setOnDragDetected(new EventHandler <MouseEvent>() {
                public void handle(MouseEvent event) {
                    Dragboard db = waterRectangle.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    Rectangle newRectangle = new Rectangle(waterRectangle.getWidth(),waterRectangle.getHeight(),waterRectangle.getFill());
                    newRectangle.setOpacity(.2);
                    content.put(DataFormat.RTF, newRectangle);
                    db.setContent(content);

                    event.consume();
                }
            });
            loadingBayWater.getChildren().add(waterRectangle);
        }
        for(Rectangle furRectangle: furRectangles){
            furRectangle.setOnDragDetected(new EventHandler <MouseEvent>() {
                public void handle(MouseEvent event) {
                    Dragboard db = furRectangle.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    Rectangle newRectangle = new Rectangle(furRectangle.getWidth(),furRectangle.getHeight(),furRectangle.getFill());
                    newRectangle.setOpacity(.2);
                    content.put(DataFormat.RTF, newRectangle);
                    db.setContent(content);

                    event.consume();
                }
            });
            loadingBayFurs.getChildren().add(furRectangle);
        }
        cargoBay.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                Rectangle rect = (Rectangle)event.getDragboard().getContent(DataFormat.RTF);
                
                if(!cargoBay.getChildren().contains(rect)){
                    cargoBay.getChildren().add(rect);
                    rect.setOnDragDetected(new EventHandler <MouseEvent>() {
                        public void handle(MouseEvent event) {
                            resetX = (int) rect.getX();
                            resetY = (int) rect.getY();
                            Dragboard db = rect.startDragAndDrop(TransferMode.ANY);
                            ClipboardContent content = new ClipboardContent();
                            content.put(DataFormat.RTF, rect);
                            db.setContent(content);

                            event.consume();
                        }
                    });
                }
                if((event.getSceneX() - rect.getWidth()/2 > cargoBay.getLayoutX()) &&
                   (event.getSceneX() + rect.getWidth()/2) < cargoBay.getLayoutX() + cargoBay.getPrefWidth() &&
                   (event.getSceneY() - rect.getHeight()/2 > cargoBay.getLayoutY()) &&
                   (event.getSceneY() + rect.getHeight()/2) < cargoBay.getLayoutY() + cargoBay.getPrefHeight()){
                    final double x = event.getSceneX() - rect.getWidth()/2 - 270;
                    final double y = event.getSceneY() - rect.getHeight()/2;
                    rect.setX((int) (Math.ceil(x / 5d) * 5));
                    rect.setY((int) (Math.ceil(y / 5d) * 5));
                }
                event.consume();
            }
        });
        cargoBay.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                Rectangle rect = (Rectangle)event.getDragboard().getContent(DataFormat.RTF);
                if(overlap(rect) || outsideCargo(rect)){
                    System.out.println("can't go there");
                    if(resetX != 0 && resetY != 0){
                        rect.setX(resetX);
                        rect.setY(resetY);
                    } else {
                        cargoBay.getChildren().remove(rect);
                    }
                } else {
                    rect.setOpacity(1);
                    rectangles.add(rect);
                }
                
                event.consume();
            }
        });
    }    
    
    public boolean overlap(Rectangle rect){
        boolean overlap = false;
        for(Rectangle rectangle: rectangles){
            if(!rect.equals(rectangle) && rect.getBoundsInLocal().intersects(rectangle.getBoundsInLocal())){
                overlap = true;
            }
        }
        return overlap;
    }
    
    public boolean outsideCargo(Rectangle rect){
        return cargoBay.getBoundsInParent().contains(rect.getX(), rect.getY(), rect.getHeight(), rect.getWidth());
    }

  @Override
  public void setScreenParent(ScreensController parentController)
  {
    this.parentController = parentController;
  }   
}
