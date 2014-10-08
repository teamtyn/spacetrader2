package spacetrader;

import java.io.PrintStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class EngineController
  implements Initializable, ControlledScreen
{
  @FXML Slider slider1;
  @FXML Slider slider2;
  @FXML Slider slider3;
  @FXML Label sliderLabel1;
  @FXML Label sliderLabel2;
  @FXML Label sliderLabel3;
  @FXML Button activateSliders;
  @FXML ColorPicker colors;
  @FXML Label colorLabel;
  @FXML Label instruction;
  @FXML ProgressIndicator progress;
  @FXML Button button1;
  @FXML Button button2;
  @FXML Button button3;
  @FXML Button button4;
  @FXML Label failLabel;
  public static final Random random = new Random();
  private ScreensController parentController;
  private Queue<Instruction> instructions;
  private int[] desiredValues;
  private double totalInstructions;
  
  public class Instruction
  {
    public String text;
    private int slide1;
    private int slide2;
    private int slide3;
    private int[] buttonsArray;
    private int buttonsArrayIndex;
    private Color color;
    
    Instruction(String text, int slide1, int slide2, int slide3)
    {
      this.text = (text + slide1 + " " + slide2 + " " + slide3);
      this.slide1 = slide1;
      this.slide2 = slide2;
      this.slide3 = slide3;
      buttonsArray = new int[0];
      buttonsArrayIndex = 0;
    }
    
    Instruction(String text, int[] buttonsArray)
    {
      StringBuilder builder = new StringBuilder("Push buttons in order: \n");
      for (int button : buttonsArray) {
        builder.append(" ").append(button).append(" ");
      }
      text = builder.toString();
      slide1 = 0;
      slide2 = 0;
      slide3 = 0;
      this.buttonsArray = buttonsArray;
      buttonsArrayIndex = 0;
    }
    
    Instruction(String text, Color color)
    {
      this.text = (text + this.slide1 + " " + this.slide2 + " " + this.slide3);
      this.color = color;
    }
    
    public boolean checkButton(int buttonValue)
    {
      boolean success = false;
      if ((buttonsArray.length != 0) && (buttonsArray[buttonsArrayIndex] == buttonValue))
      {
        this.buttonsArrayIndex += 1;
        success = true;
        System.out.println("GOOD");
      }
      else
      {
        buttonsArrayIndex = 0;
        System.out.println("FAIL");
      }
      return success;
    }
    
    public boolean completed(int sliderValue1, int sliderValue2, int sliderValue3)
    {
      boolean complete = true;
      complete = ((slide1 == (int)slider1.getValue()) || (slide1 == 0)) && 
              ((slide2 == (int)slider2.getValue()) || (slide2 == 0)) && 
              ((slide3 == (int)slider3.getValue()) || (slide3 == 0)) && 
              ((buttonsArray.length == 0) || (buttonsArrayIndex == buttonsArray.length));
      
      System.out.println("Complete test");
      System.out.println(buttonsArrayIndex == buttonsArray.length);
      this.color = ((Color)colors.getValue());
      return complete;
    }
  }
  
  public void generateInstructions()
  {
    int[] buttonsToPush = { 1, 2, 3, 4 };
    this.instructions.add(new Instruction("Click buttons in order: ", buttonsToPush));
    int[] buttonsToPush2 = { 1, 2, 3, 4, 4, 3, 2, 1 };
    this.instructions.add(new Instruction("Click buttons in order: ", buttonsToPush2));
    this.instructions.add(new Instruction("Set Sliders: ", random.nextInt(100), random.nextInt(100), random.nextInt(100)));
    this.instructions.add(new Instruction("Set Sliders: ", random.nextInt(100), random.nextInt(100), random.nextInt(100)));
    this.instruction.setText(((Instruction)instructions.peek()).text);
    this.totalInstructions = instructions.size();
  }
  
  public void selectColor()
  {
    this.colorLabel.setText("" + colors.getValue());
  }
  
  public void testForComplete()
  {
    if (((Instruction)instructions.peek()).completed((int)slider1.getValue(), (int)slider2.getValue(), (int)slider3.getValue()))
    {
      System.out.println("YAY");
      instructions.remove();
      if (!instructions.isEmpty())
      {
        instruction.setText(((Instruction)instructions.peek()).text);
        progress.setProgress((totalInstructions - instructions.size()) / totalInstructions);
      }
      else
      {
        instruction.setText("ALL DONE");
        progress.setProgress(1.0D);
      }
    }
    else
    {
      System.out.println("KABOOM");
    }
  }
  
  @FXML
  private void clickButton1(ActionEvent event)
  {
    boolean success = ((Instruction)instructions.peek()).checkButton(1);
    if (success)
    {
      testForComplete();
      failLabel.setText("");
    }
    else
    {
      failLabel.setText("Button sequence out of order");
    }
  }
  
  @FXML
  private void clickButton2(ActionEvent event)
  {
    boolean success = ((Instruction)instructions.peek()).checkButton(2);
    if (success)
    {
      testForComplete();
      failLabel.setText("");
    }
    else
    {
      failLabel.setText("Button sequence out of order");
    }
  }
  
  @FXML
  private void clickButton3(ActionEvent event)
  {
    boolean success = ((Instruction)instructions.peek()).checkButton(3);
    if (success)
    {
      testForComplete();
      failLabel.setText("");
    }
    else
    {
      failLabel.setText("Button sequence out of order");
    }
  }
  
  @FXML
  private void clickButton4(ActionEvent event)
  {
    boolean success = ((Instruction)instructions.peek()).checkButton(4);
    if (success)
    {
      testForComplete();
      failLabel.setText("");
    }
    else
    {
      failLabel.setText("Button sequence out of order");
    }
  }
  
  public void initialize(URL url, ResourceBundle rb)
  {
    instructions = new LinkedList();
    generateInstructions();
    sliderLabel1.setText("" + slider1.getValue());
    sliderLabel2.setText("" + slider2.getValue());
    sliderLabel3.setText("" + slider3.getValue());
    
    this.slider1.valueProperty().addListener(new ChangeListener()
    {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
      {
        EngineController.this.sliderLabel1.setText("" + newValue.intValue());
      }

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    });
    this.slider2.valueProperty().addListener(new ChangeListener()
    {
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
      {
        sliderLabel2.setText("" + newValue.intValue());
      }

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    });
    this.slider3.valueProperty().addListener(new ChangeListener()
    {
        
        public void changed(ObservableValue observable, Number oldValue, Number newValue)
        {
          sliderLabel3.setText("" + newValue.intValue());
        }

        @Override
        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    });
  }
  
  public void setScreenParent(ScreensController parentController)
  {
    this.parentController = parentController;
  }
}

