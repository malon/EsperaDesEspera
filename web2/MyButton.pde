   /*
    * This file is part of EsperaDesespera.
    *
    * Copyright (C) 2011 by Alberto Ariza, Juan Elosua & Marta Alonso
    *
    * This program is free software: you can redistribute it and/or modify
    * it under the terms of the GNU Affero General Public License as published by
    * the Free Software Foundation, either version 3 of the License, or
    * (at your option) any later version.
    *
    * This software is provided 'as-is', without any express or implied warranty.
    * In no event will the authors be held liable for any damages arising from the
    * use of this software.
    * 
    * You should have received a copy of the GNU Affero General Public License
    * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */

/**
 *class My Button
 *
 */

class MyButton {

  // Parameters
  int id; //number of option
  String buttonType;//MENU or CARD by the moment
  String text1; // first text to write (speciality) or menu content
  String text2; // second text to write (average waiting days)
  int pad1; //separation between posX and text1
  int pad2; //separation between posX and text2
  float posX; // position X of the button
  float posY; // position Y of the button
  float myTextSize; //text size
  float myWidth; // width of button
  float myHeight;// height of button
  color colorOn; // color of the text inside button when mouse is over
  color colorOff; //color of the text of the button when the mouse is not over
  color colorPush; //color of the text of the button when click
  color colorStroke; //color of the stroke of the button if desired, otherwise use 0
  // boolean myStroke=false;//stroke? or nostroke? defaults to false
  int myCursorMode; //cursor mode when mouse is over
  int myRectMode; //rect mode to draw the buttons
  String font; // text font


    //Initialized variables
  boolean over=false; 
  boolean pressed = false;   
  int myAlpha=0;
  boolean enabled=true;


  //constructor
  MyButton(int id, String buttonType, String text1, String text2, int pad1, int pad2, int myTextSize, 
  float posX, float posY, float myWidth, float myHeight, 
  color colorOn, color colorOff, color colorPush, color colorStroke, 
  int myCursorMode, int myRectMode, String font) {
    this.id=id;
    this.buttonType=buttonType;
    this.text1=text1;
    this.text2=text2;
    this.pad1=pad1;
    this.pad2=pad2;
    this.posX=posX;
    this.posY=posY;
    this.myTextSize=myTextSize;
    this.myWidth=myWidth;
    this.myHeight=myHeight;
    this.colorOn=colorOn;
    this.colorOff=colorOff; 
    this.colorPush=colorPush;
    this.colorStroke=colorStroke;

    this.myCursorMode=myCursorMode;
    this.myRectMode=myRectMode;
    this.font=font;
  }

  /************************************************          
   *Function to update the status of the variable: over
   @return over
   *************************************************/
  boolean update() {
    if (enabled) {
      //inside the button
      if ((mouseX>posX && mouseX<(posX+myWidth)) &&(mouseY>posY-myHeight && mouseY<(posY+myHeight))) {
        over = true;
        cursor(myCursorMode);
      }
      //outside the button
      else {        
        over = false;
      }
      return over;
    } 
    else {
      return false;
    }
  }


  /**********************************************
   *Function to display the buttons
   ***********************************************/
  void display() {
    //pressed
      
    if (pressed == true) {
      buttonPressed();
    }
    //not pressed but over t he button
    else if (over == true) {
      buttonOvered();
    }
    //not over the button
    else {
      buttonNotOvered();
    }
    buttonText();
  }      


  /*********************************************************
   *Function called when the button is pressed
   *********************************************************/
  void buttonPressed() {
    fill(colorPush);
    //text (text1+" selected!", 100, 400);
    //----------LLAMADA A LA FUNCION QUE CORRESPONDA----------
  }


  /*********************************************************
   *Function called when the cursor is over the button
   *********************************************************/
  void buttonOvered() {
    if (enabled) {
      fill (colorOff, myAlpha);
      myAlpha+=10;
      if (myAlpha>255) {
        myAlpha=255;
      }
      rectMode (myRectMode);
      rect (posX-pad1, posY-pad1, myWidth+pad1*2, myHeight+pad1*2);

      fill(colorOn);
    }
  }


  /*********************************************************
   *Function called when the cursor is not over the button
   *neither is clicked
   *********************************************************/
  void buttonNotOvered() {
    fill(colorOff);
    myAlpha=0;
  }


  /*********************************************************
   *Function called to write text inside the button
   *********************************************************/
  void buttonText() {
    textSize(myTextSize);
    if (buttonType=="MENU") {
      text (text1, posX+pad1, posY);
    }
    else if (buttonType=="CARD") {                
      text (text1, posX+pad1, posY);
      textAlign (RIGHT, TOP);
      text (text2, posX+myWidth, posY);
      textAlign (LEFT, TOP);
    }
  }



  /*********************************************************
   *Two functions used to inform the button that a click is made
   *somewhere over the screen, or that the click was released.
   **********************************************************/

  int press() {
    if (enabled) {
      if (over == true) {
        pressed = true;
        return this.id;
      }                
      else {
        return -1;
      }
    } 
    else {  
      return -1;
    }
  }

  void release() {
    if (enabled) {
      pressed = false;
    }
  }
  
  void reset_press() {
    //Called when the card face shows
    if (pressed) {
      pressed = false;
    }
  }
}

