import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class HospitalBattle extends PApplet {


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


//+++++++++++++++
//variables de datos
//++++++++++++++

String [] nombreFotos;
String [] nombres;
String [] rotulos;
float [][] datos;
float[] maximos;

//+++++++++++++++
//variables de apariencia
//+++++++++
// tama\u00f1o del escenario
int ancho=823;
int alto=384;
//los colores son ocre, azul y granate
int colorOcre=color (212, 207, 149);
int colorAzul=color (71, 129, 158);
int colorGranate=color (122, 26, 36);
int colorAzulOscuro=color (41, 51, 54);
//posicion horizontal y vertical del marcador
float marcadorX=20;
float marcadorY=24;
//posiciones de los mazos y los huecos
float mazo1X=240;
float mazo1Y=30;
float mazo2X=130+ancho-mazo1X-200;
float mazo2Y=mazo1Y;
//float desfaseX=12;
float desfaseX=4;
float desfaseY=3;
float [][] mazo1Pos;
float [][] mazo2Pos;
//fotos a cargar
PImage fondo;
PImage icoVacio, icoLleno, reverso, anverso;
//tipograf\u00edas a cargar //12 para los player y 10 para turno y botones
PFont miTipo12; //DIN-Medium-12.vlw 
PFont miTipo10; //DIN-Medium-12.vlw 


//Los tipos de pantalla, son "menu", "juego", "instrucciones", "gameover"
String pantalla="menu";
//**********************************
//variables de juego
//**********************************
//numCartas de los jugadores
int numCartasJugador, numCartasOrdenador, cartasTotales;
Carta [] cartasCPU;
Carta [] cartasJugador;
Carta cartaActiva, cartaOpuesta;
float opcionResaltada, opcionOpuesta;
int turnoActivo;//el 1 es el player y el 2 es el CPU
Timer director;


//************************
//VAriables de interfaz
//***********************
PImage logo;
PImage botOff, botOn; 
int totalBotonesMenu=2;
float [] botMenuX=new float [2];
float [] botMenuY=new float [2];
PImage fondoGameOver, mensajeGameOver;
String  textoGameOver="Has aprendido algo?\n\u00c9chate un par de partidas para tener ua visi\u00f3n m\u00e1s clara de lo que hay que esperar para operarse";
String [] instrucciones;
int insAlfa;
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

class Carta {

  String miNombre;
  float posX, posY;
  float[] misDatos;
  String [] misRotulos;
  String nombreFoto;
  boolean miEstado;
  int miRef;
  int miOrden;
  //internas
  int colorOcre=color (212, 207, 149);
  int colorAzul=color (71, 129, 158);
  int colorGranate=color (122, 26, 36);
  int colorAzulOscuro=color (41, 51, 54);
  int colorBlanco=color(255, 255, 255);
  PFont miTipo12, miTipo10, miTipo20;
  String ruta="fotosHospitales/";
  PImage miFoto;
  float alturaLinea;
  //botones
  MyButton [] botones;
  boolean overGlobal=false;
  float [] alturaBotones;
  //resalte
  boolean resalte=false;
  float resalteAlfa=0;
  float anguloResalte=0;
  int opcionActivada;


  //constructor
  Carta (int _miOrden, int _miRef, String _miNombre, String _nombreFoto, float[] _misDatos, String [] _misRotulos, float _posX, float _posY, boolean _miEstado) {

    miNombre=_miNombre;
    posX=_posX;
    posY=_posY;
    nombreFoto=_nombreFoto;
    misDatos=_misDatos;
    misRotulos=_misRotulos;
    miEstado=_miEstado;
    miRef=_miRef;
    miOrden=_miOrden;
    miTipo12=loadFont ("DIN-Medium-12.vlw");
    miTipo10=loadFont ("DIN-Medium-10.vlw");
    miTipo20=loadFont ("DIN-Medium-20.vlw");
    //cargamos la foto
    miFoto=loadImage(ruta+nombreFoto);
    botones=new MyButton [misDatos.length];
    alturaBotones=new float [misDatos.length];
    float altura=205;
    for (int i=0; i<misDatos.length; i++) {
      /*
      MyButton(int id, String buttonType, String text1, String text2, int pad1, int pad2, int myTextSize, 
       float posX, float posY, float myWidth, float myHeight, 
       color colorOn, color colorOff, color colorPush, color colorStroke, 
       int myCursorMode, int myRectMode, String font)
       */
      botones [i]=new MyButton(i, "CARD", misRotulos[i], misDatos[i]+"", 1, 30, 10, posX+20, altura, miFoto.width, 10, colorBlanco, 
      colorAzul, colorOcre, colorAzulOscuro, HAND, CORNER, "DIN-Medium-10.vlw");
      alturaBotones[i]=altura;

      altura=altura+20;
    }
    stroke (colorAzulOscuro);
  }
  public void updatePos () {
    for (int i=0; i<misDatos.length; i++) {
      botones [i].posX=this.posX+20;

    }
  }
  //dibujamos
  public void dibuja () {
    //BORRAR PINTA TEXTO
    /* fill (255, 0, 0);
     textFont(miTipo12);
     textAlign (RIGHT, TOP);
     String cadena=miOrden+ "   "+miRef;
     text (cadena, posX-20, posY+miOrden*4);
     */
    //FIN BORRAR
    //si miEstado es falso dibujamos el reverso
    if (miEstado==false) {
      image (reverso, posX, posY);
    } 
    else {
      float altura=posY+14;
      //el fondo
      image (anverso, posX, posY);
      // la foto
      image (miFoto, posX+20, altura);
      //hay que poner los botones
      //El nombre
      fill (colorAzulOscuro);
      textFont(miTipo20);
      textSize (14);
      textAlign (LEFT, TOP);
      altura=posY+20+miFoto.height+4;
      if (textWidth (miNombre)>miFoto.width) {
        //multilinea
        textLeading (14);

        text (miNombre, posX+20, altura-5, miFoto.width, 30);
        altura=posY+20+miFoto.height+4+30;
        altura=181;
      } 
      else {
        text (miNombre, posX+20, posY+100);
        altura=posY+20+miFoto.height+4+18;
        altura=165;
      }
      // altura=175;

      // la linea
      strokeWeight (2);
      stroke (colorAzulOscuro);

      line (posX+20, posY+130, posX+20+miFoto.width, posY+130);

      altura=altura+14;

      noStroke ();
      // el resalte
      if (resalte==true) {
        anguloResalte+=0.1f;
        resalteAlfa=sin (anguloResalte)*128+128;
        fill (colorAzulOscuro, resalteAlfa);


        rect (posX+20, alturaBotones[opcionActivada]-3, miFoto.width, 15);
      }

      //los botones
      for (int i=0; i<botones.length; i++) {
        overGlobal=botones[i].update()||overGlobal;
      }
      if (!(overGlobal)) {
        cursor(ARROW);
      }
      for (int i=0; i<botones.length; i++) {
        botones [i].display();
      }
    }
  }
}

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
  int colorOn; // color of the text inside button when mouse is over
  int colorOff; //color of the text of the button when the mouse is not over
  int colorPush; //color of the text of the button when click
  int colorStroke; //color of the stroke of the button if desired, otherwise use 0
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
  int colorOn, int colorOff, int colorPush, int colorStroke, 
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
  public boolean update() {
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
  public void display() {
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
  public void buttonPressed() {
    fill(colorPush);
    //text (text1+" selected!", 100, 400);
    //----------LLAMADA A LA FUNCION QUE CORRESPONDA----------
  }


  /*********************************************************
   *Function called when the cursor is over the button
   *********************************************************/
  public void buttonOvered() {
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
  public void buttonNotOvered() {
    fill(colorOff);
    myAlpha=0;
  }


  /*********************************************************
   *Function called to write text inside the button
   *********************************************************/
  public void buttonText() {
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

  public int press() {
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

  public void release() {
    if (enabled) {
      pressed = false;
    }
  }
  
  public void reset_press() {
    //Called when the card face shows
    if (pressed) {
      pressed = false;
    }
  }
}

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

class Timer {
  int miTiempo;
  int miLimite=120;
  Carta carta1, carta2;
  float mazo1X, mazo2X;
  boolean estado;
  int ganador;
  int episodio;//1: resalta; 2: saca; 3 actualizaMazos, 4 mete;
  float miDestino;
  //COSTRUCTOR

  Timer (Carta _carta1, Carta _carta2, int _ganador, float _mazo1X, float _mazo2X) {
    carta1=_carta1;
    carta2=_carta2;
    mazo1X=_mazo1X;
    mazo2X=_mazo2X;
    ganador=_ganador;
    miTiempo=0;
    estado=true;
    episodio=1;
  }
  public void resetea() {
    estado=true;
    episodio =1;
  }

  public void dibuja () {

    if (estado==true) {

      if (episodio==1) {
        //RESALTAMOS
        miTiempo++;
        if (miTiempo==miLimite) {
          carta1.posX=20;
          carta2.posX=25;
          for (int i=0; i<carta1.botones.length; i++) {
            //We initialize the pressed status
            carta1.botones [i].reset_press();
          }
          for (int i=0; i<carta2.botones.length; i++) {
            //We initialize the pressed status
            carta2.botones [i].reset_press();
          }
          carta1.miEstado=false;
          carta2.miEstado=false;
          episodio=2;
        }
      } 
      else if (episodio==2) {
        //SACAMOS
        if (ganador==1) {
          miDestino=-400;
        } 
        else {
          miDestino=1000;
        }

        carta1.posX=carta1.posX+(miDestino-carta1.posX)*0.1f;
        carta2.posX=carta2.posX+(miDestino-carta2.posX)*0.1f;
        if ( abs(miDestino-carta1.posX)<10 && abs(miDestino-carta2.posX)<10) {

          episodio=3;
        }
      }
      else if (episodio==3) {
        actualizaMazos (ganador);
        episodio=4;
      } 
      else if (episodio==4) {
        float destino1, destino2;
        float [][] arrayDestino;

        if (ganador==1) {
          destino1=mazo1Pos[1][0];
          destino2=mazo1Pos[0][0];
          arrayDestino=mazo1Pos;
        } 
        else {
          destino1=mazo2Pos[0][0];
          destino2=mazo2Pos[1][0];
          arrayDestino=mazo2Pos;
        } 
        //METEMOS
        carta1.posX=carta1.posX+(destino1-carta1.posX)*0.1f;
        carta2.posX=carta2.posX+(destino2-carta2.posX)*0.1f;

        if ( abs(destino1-carta1.posX)<10 && abs(destino2-carta2.posX)<10) {          
          carta1.posX=destino1;
          carta2.posX=destino2;
          if (ganador==1) {
            //para ambos mazos les pasamos nuevas posX y posY;
            for (int i=0; i<=cartasJugador.length-1; i++) {
              cartasJugador[i].posX=mazo1Pos [i][0];
              cartasJugador[i].posY=mazo1Pos [i][1];
              cartasJugador[i].resalte=false;
              cartasJugador[i].miOrden=i;
              cartasJugador[i].updatePos();
            }    
          } 
          else {
            for (int i=0; i<=cartasCPU.length-1; i++) {
              cartasCPU[i].posX=mazo2Pos [i][0];
              cartasCPU[i].posY=mazo2Pos [i][1];
              cartasCPU[i].resalte=false;
              cartasCPU[i].miOrden=i;
              cartasCPU[i].updatePos();
            }
          }
          cambiaTurno (ganador);
          episodio=0;
          estado=false;
        }
      }
    }
  }
}

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

public String cargaDatos(int numMax){
      
    int numHospTot=15;
    cartasTotales=numHospTot;
    String date=null;
    try {  
    String strUrl=  "http://www.cieslog.com/EsperaDesespera/BatallaHosp.php";
    XMLElement eXML = new XMLElement (this,strUrl);
  
    ArrayList nameList = new ArrayList();//Arraylist for hostpitals
    ArrayList photoList = new ArrayList();//Arraylist for photos
    
    int count = eXML.getChildCount();
    int countNom=0;//Hospital counter
    date = eXML.getChild(0).getChild(0).getContent();
    
    //################creating array of services id##################
    ArrayList services=new ArrayList();
    for (int i=0;i<count;i++){
      services.add(eXML.getChild(i).getChild(4).getContent());  
    }
    ArrayList servAux= new ArrayList();
    ArrayList contAux= new ArrayList();
    for (int i=0;i<count;i++){
      if (servAux.indexOf(services.get(i))==-1){
          //new service
          servAux.add(services.get(i));
          contAux.add(1);
      }else{
          //service already in array
          int aux=Integer.parseInt((contAux.get(servAux.indexOf(services.get(i)))).toString());
          contAux.set(servAux.indexOf(services.get(i)),(aux+1));
      }
    }
       
    
    //services to use, zeroes when occurences less than 15
    for(int i=0;i<servAux.size();i++){
      if (Integer.parseInt(contAux.get(i).toString())<numHospTot){
          servAux.set(i,0);
      }
    }
    
    //reducing servAux to the used values
    int x=0;
    while (x<servAux.size()){
      if (Integer.parseInt(servAux.get(x).toString())==0){
        servAux.remove(x);
      }else{
        x++;
      }
    }  
    while (servAux.size()-numMax>0){
      servAux.remove(numMax);
    }  
    servAux.trimToSize();
    
    datos = new float[numMax][numHospTot];
    //##################creating hospital and data array
    for (int i=0;i<count;i++){
      //comparing Hospital id 
      int idHosp=Integer.parseInt(eXML.getChild(i).getChild(1).getContent());
      Object idServ=eXML.getChild(i).getChild(4).getContent();
      int indexServ=servAux.indexOf(idServ);
      String nameHosp=eXML.getChild(i).getChild(2).getContent();        
      String provHosp=eXML.getChild(i).getChild(3).getContent();
      String avgTime=eXML.getChild(i).getChild(9).getContent();
      Float avgFloat=Float.parseFloat(avgTime);
       if (idHosp==1){
        nameHosp="C.H. UNIVERSITARIO A CORUNA";
      }    

      if (idHosp>countNom){
        nameList.add(nameHosp+" "+provHosp);
        photoList.add(eXML.getChild(i).getChild(10).getContent());
        countNom++;
      }
      
      if (indexServ!=-1){
        datos[indexServ][idHosp-1]=avgFloat;
      }                
    }
    
    //hospital names array
    nombres=new String[nameList.size()];
    nombres=(String[])nameList.toArray(nombres);
    //photos names array
    nombreFotos = new String[photoList.size()];
    nombreFotos = (String [])photoList.toArray(nombreFotos);
     
    //##################creating services array
    rotulos=new String[servAux.size()];
    for (int i=0;i<servAux.size();i++){
    for (int j=0;j<count;j++){
      Object idServ=eXML.getChild(j).getChild(4).getContent();
      if (servAux.get(i)==idServ){
        String rot=eXML.getChild(j).getChild(5).getContent();
        if (rot.startsWith("CIRURXIA")){
          rot=rot.replaceAll("CIRURXIA","CIR.");
        }
        rotulos[i]=rot;        
        break;
      }
    }
    }          
          
    //###################### array of max values
       maximos = new float[servAux.size()];
       strUrl= "http://www.cieslog.com/EsperaDesespera/Tiempos.php";
       eXML = new XMLElement (this,strUrl);
       count = eXML.getChildCount();
       
       for (int i=0;i<count;i++){
         Object idServ=eXML.getChild(i).getChild(0).getContent();
         int indexServ=servAux.indexOf(idServ);         
         if (indexServ!=-1){
           maximos[indexServ]=Float.parseFloat(eXML.getChild(i).getChild(1).getContent());
         }
       }              
    
    } catch(Exception e) {
      println("An error has occured");
    } 
    
    return date;
    }
    




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

public void dibujaMarcador ()
{
  //dibujamos letreros
  fill (colorOcre);
  textFont (miTipo12);
  //jugador
  textAlign  (LEFT, CENTER);
  text ("JUGADOR", marcadorX, marcadorY/2+5);
  String textoAux="JUGADOR";
  float posX=marcadorX+textWidth(textoAux)+10;
  float posY=marcadorY/2+5-icoVacio.height/2-2;

  for (int i=0; i<cartasTotales; i++) {
    if (cartasJugador.length==0 || cartasCPU.length==0) {
      pantalla="gameover";
    } 
    if (i<cartasJugador.length) {
      image (icoLleno, posX, posY);
    } 
    else {
      image (icoVacio, posX, posY);
    }
    posX=posX+icoVacio.width+5;
  }
  //cpu
  textAlign (RIGHT, CENTER);
  text ("CPU", width-marcadorX, marcadorY/2+5);
  textoAux=" CPU  ";
  posX=width-marcadorX-textWidth(textoAux)-12;
  posY=marcadorY/2+5-icoVacio.height/2-2;
  for (int i=0; i<cartasTotales; i++) {
    if (i<cartasCPU.length) {
      image (icoLleno, posX, posY);
    } 
    else {
      image (icoVacio, posX, posY);
    }
    posX=posX-icoVacio.width-5;
  }
  //turno
  textAlign (CENTER, CENTER);
  textFont (miTipo12);
  if (turnoActivo==1) {
    text ("Turno JUGADOR", width/2+10, marcadorY/2+5);
  } 
  else {
    text ("Turno CPU", width/2+10, marcadorY/2+5);
  }
}
public void dibujaMazos ()
{
  //los del cpu

  for (int i=0; i<cartasCPU.length; i++) {
    cartasCPU[i].dibuja();
  }
  //los del player
  for (int i=0; i<cartasJugador.length; i++) {
    cartasJugador[i].dibuja();
  }
}
public void dibujaChuleta () {
  fill (colorOcre);
  textFont (miTipo12);
  textAlign (CENTER, BOTTOM);
  text ("DATOS M\u00c1XIMOS", 100, height/2);
  fill (0, 80);
  noStroke ();
  rect (20, height/2, 180, 130);
  fill (colorOcre);
  float posX=30;
  float posY=height/2+10;
  textFont (miTipo10);

  for (int i=0; i<rotulos.length; i++) {
    textAlign (LEFT, TOP);
    String textoAux=rotulos[i];
    text (textoAux, posX, posY);
    textAlign (RIGHT, TOP);
    textoAux=maximos[i]+" ";
    text (textoAux, 190, posY);

    posY+=textAscent()+textDescent ()+5;
  }
}
public void dibujaMenu () {
  background (255);
  botMenuX[0]=width/2-logo.width/2;
  botMenuX[1]=width/2+logo.width/2-botOff.width;
  botMenuY[0]=300;
  botMenuY[1]=300;

  image (logo, width/2-logo.width/2, 30);

  if (     (mouseX>botMenuX[0] && mouseX<(botMenuX[0]+94))      &&      (mouseY>botMenuY[0]-30 && mouseY<(botMenuY[0]+30))    ) {
    //estamos encima
    cursor (HAND);
    image (botOn, botMenuX[0], botMenuY[0]);
    image (botOff, botMenuX[1], botMenuY[1]);
    if (mousePressed) {        
      pantalla="instrucciones";
    }
  }
  else if  ((mouseX>botMenuX[1] && mouseX<(botMenuX[1]+94))      &&      (mouseY>botMenuY[1]-30 && mouseY<(botMenuY[1]+30))    ) {
    //estamos encima
    cursor (HAND);
    image (botOn, botMenuX[1], botMenuY[1]);
    image (botOff, botMenuX[0], botMenuY[0]);
    if (mousePressed) {
      pantalla="juego";
      iniciaPartida ();
    }
  }
  else {
    cursor (ARROW);
    image (botOff, botMenuX[0], botMenuY[0]);
    image (botOff, botMenuX[1], botMenuY[1]);
  }
  textFont (miTipo10);
  textAlign (CENTER, TOP);
  text ("INSTRUCCIONES", botMenuX[0]+botOff.width/2, botMenuY[0]+10);
  text ("JUGAR", botMenuX[1]+botOff.width/2, botMenuY[0]+10);
}

public void dibujaInstrucciones () {
  insAlfa+=5;
  background (255);
  image (logo, width/2-logo.width/2, 30);
  if (insAlfa>150) {
    insAlfa=150;
  }
  fill (0, insAlfa);
  noStroke ();
  rect (80, 0, 325, height);
  String cadena="";

  for (int i=0; i < instrucciones.length; i++) {
    cadena=cadena+instrucciones[i]+"\n";
  }
  fill (255);
  textFont (miTipo10);
  textAlign (LEFT, TOP);
  text (cadena, 100, 50, 300, 300);
}
public void dibujaGameOver () {
  image (fondoGameOver, 0, 0);
  image (mensajeGameOver, width/2-mensajeGameOver.width/2, height/2-mensajeGameOver.height/2);
  textFont (miTipo12);
  textAlign (CENTER, TOP);
  text (textoGameOver, width/2-mensajeGameOver.width/2+120, height/2, 300, 100);
  textSize (12);
}

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

public void setup () {
  size (ancho, alto);
  smooth ();
  //para las fotos de los icos y las cartas hay que saber cuantos datos hay
  cargaDatos (6);
  //cargamos las fotos y las tipos
  fondo=loadImage ("fondo.jpg");
  miTipo12=loadFont ("DIN-Medium-12.vlw");
  miTipo10=loadFont ("DIN-Medium-10.vlw");
  //las fotos
  icoVacio=loadImage ("icoCartaVacia.png");
  icoLleno=loadImage ("icoCartaLlena.png");
  reverso=loadImage ("cartaReverso.png");
  anverso=loadImage ("cartaAnverso.png");
   //INTERFAZ
  logo=loadImage ("logo.png");
  botOff=loadImage ("botonOff.png");
  botOn=loadImage ("botonOn.png");
  fondoGameOver=loadImage ("fondoGameOver.jpg");
  mensajeGameOver=loadImage ("gameOver.png");
  instrucciones=loadStrings ("instrucciones.txt");
  insAlfa=0;
}
public void draw () {
 //se ejecuta continuamente
  //borramos el escenario,

  if (pantalla=="juego") {
    image (fondo, 0, 0);
    dibujaMarcador ();
    dibujaMazos ();
    dibujaChuleta ();
    if (cartaActiva.resalte) {
      director.dibuja ();
    }
  }
  else if (pantalla=="menu") {

    dibujaMenu ();
  } 
  else if (pantalla=="instrucciones") {

    dibujaInstrucciones ();
  } 
  else if (pantalla=="gameover") {
    background (0);
    dibujaGameOver ();
  }
}

public void mousePressed() {
  if (pantalla=="juego") {
    for (int i=0; i<rotulos.length; i++) {
      int opcion=cartaActiva.botones[i].press();
      if (opcion!=-1) {
        activaMe (opcion);
        break;
      }
    }
  } 
  else if (pantalla=="menu") {
    int kk=0;
  } 
  else if (pantalla=="instrucciones") {
    
      pantalla="menu";
      insAlfa=0;
    } 
    else {
      pantalla="menu";
    }
  }
  public void mouseReleased() {
    if (pantalla=="juego") {
      for (int i=0; i<rotulos.length; i++) {
        cartaActiva.botones[i].release();
      }
    }
  }
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


public void iniciaPartida () {
  //barajamos las cartas
  int[] cartasIniciales=new int [cartasTotales];
  for (int i=1; i<cartasTotales; i++) {
    cartasIniciales[i]=i;
  } 
  int [] cartasBarajadas=new int [cartasTotales];
  cartasBarajadas=baraja (cartasIniciales);
  //REPARTIMOS
  //vemos cuantas cartas tiene cada jugador
  float mitadCartas=PApplet.parseFloat(cartasTotales)/2;
  float aux=ceil (mitadCartas);
  numCartasJugador=PApplet.parseInt (aux);
  aux=floor (mitadCartas);
  numCartasOrdenador=PApplet.parseInt(aux);
  
  //PARA EL PLAYER
  int indice=0;
  int miRef=cartasBarajadas[indice];
  //llenamos los arrays de posiciones de los mazos
  mazo1Pos=new float[cartasTotales][2];

  cartasJugador=new Carta[numCartasJugador];
  mazo1Pos[0][0]=mazo1X;
  mazo1Pos[0][1]=mazo1Y;
  //el resto de los datos
  float [] datosCarta=new float [rotulos.length];
  datosCarta=reparteDatosCarta (miRef);
  //Carta (int _miOrden, int _miRef, String _miNombre, float _posX, float _posY, boolean _miEstado) {
  //creamos las cartas del Jugador
  cartasJugador[0]=new Carta (0, miRef, nombres[miRef], nombreFotos[miRef], datosCarta, rotulos, mazo1Pos[0][0], mazo1Pos[0][1], false);
  indice++;
  for (int i=1; i<cartasTotales; i++) {
    //creamos el array de posiciones para todas las cartas
    mazo1Pos[i][0]=mazo1Pos[i-1][0]+desfaseX;
    mazo1Pos[i][1]=mazo1Pos[i-1][1]+desfaseY;

    if ( i<numCartasJugador) {
      //pero solo  creamos la carta para las posiciones ocupadas
      miRef=cartasBarajadas[indice];
      datosCarta=reparteDatosCarta (miRef);
      // y creamos la el objeto carta
      cartasJugador[i]=new Carta (i, miRef, nombres[miRef], nombreFotos[miRef], datosCarta, rotulos, mazo1Pos[i][0], mazo1Pos[i][1], false);
      indice++;
    }
  }
  
  //PARA LA CPU
  mazo2Pos=new float[cartasTotales][2];
  cartasCPU=new Carta[numCartasOrdenador];
  mazo2Pos[0][0]=mazo2X;
  mazo2Pos[0][1]=mazo2Y;
  miRef=cartasBarajadas[indice];
  datosCarta=reparteDatosCarta (miRef);
  cartasCPU[0]=new Carta (0, miRef, nombres[miRef], nombreFotos[miRef], datosCarta, rotulos, mazo2Pos[0][0], mazo2Pos[0][1], false);
  indice++;
  for (int i=1; i<cartasTotales; i++) {
    mazo2Pos[i][0]=mazo2Pos[i-1][0]+desfaseX;
    mazo2Pos[i][1]=mazo2Pos[i-1][1]+desfaseY;
    if (i<numCartasOrdenador) {
      miRef=cartasBarajadas[indice];
      datosCarta=reparteDatosCarta (miRef);
      // y creamos la el objeto carta
      //Carta (int _miOrden, String _miNombre, float _posX, float _posY, int[] _misDatos, boolean _miEstado, int _miNum) 
      cartasCPU[i]=new Carta (i, miRef, nombres[miRef], nombreFotos[miRef], datosCarta, rotulos, mazo2Pos[i][0], mazo2Pos[i][1], false);
      indice++;
    }
  }
  cambiaTurno (1);
}
public void cambiaTurno (int player) {
  turnoActivo=player;
  //el 1 es el player y el 2 es el CPU
  if (player==2) {
    cartaActiva=cartasCPU[cartasCPU.length-1];
    cartaActiva.miEstado=true;
    jugadaMaestra ();
  } 
  else {
    cartaActiva=cartasJugador[cartasJugador.length-1];
    cartaActiva.miEstado=true;
    //activamos sus botones
    activaBotones (cartaActiva, true);
  }
}
public void activaMe (int opcion) {
  cursor (ARROW);
  //anulamos los botones
  activaBotones (cartaActiva, false);
  // vemos la carta resaltada
  if (turnoActivo==2) {
    //si el turno era de la CPU
    cartaOpuesta=cartasJugador[cartasJugador.length-1];
    //le anulamos sus botones y le ponemos el resalte
    activaBotones(cartaOpuesta, false);
    activaResalte (cartaOpuesta, opcion);
  } 
  else {
    cartaOpuesta=cartasCPU[cartasCPU.length-1];
    //mostramos la carta
    cartaOpuesta.miEstado=true;
    //anulamos sus botones
    activaBotones (cartaOpuesta, false);
    activaResalte (cartaOpuesta, opcion);
    activaResalte (cartaActiva, opcion);
  }
  //vemos cual es la opcion resaltada y cual la opuesta
  opcionResaltada=cartaActiva.misDatos [opcion];
  opcionOpuesta=cartaOpuesta.misDatos [opcion];
  //se manda los datos al arbitro y que el dirima
  if (turnoActivo==1) {
    arbitro (opcionResaltada, opcionOpuesta, maximos [opcion]);
  } 
  else {
    arbitro (opcionOpuesta, opcionResaltada, maximos [opcion]);
  }
}
public void activaResalte (Carta carta, int opcion) {
  carta.resalte=true;
  carta.opcionActivada=opcion;
}

public void arbitro (float jugada1, float jugada2, float referencia) {
  int ganador;
  //decidimos quien gana
  if (jugada1<jugada2) {
    ganador=1;
  } 
  else {
    ganador=2;
  }
  turnoActivo=ganador;
  director=new Timer (cartaActiva, cartaOpuesta, ganador, mazo1X, mazo2X);
}
public void actualizaMazos (int ganador) {
  if (ganador==1) {
    //si gano el player
    // me apunto la \u00faltima y la del mazodel CPU
    Carta ultima=cartasCPU[cartasCPU.length-1];
    //muevo la pen\u00faltima hasta una pos nueva

    cartasJugador=ordenaArray (cartasJugador, ultima);
    //Y  para el CPU  le quito la \u00faltima carta
    cartasCPU=(Carta []) shorten (cartasCPU);
  }  
  else {

    // si gana la CPU
    // me apunto la \u00faltima y la del mazo del Jugador
    Carta ultima=cartasJugador[cartasJugador.length-1];
    //muevo la pen\u00faltima hasta una pos nueva
    cartasCPU=ordenaArray (cartasCPU, ultima);

    //Y  para el Jugador  le quito la \u00faltima carta
    cartasJugador=(Carta []) shorten (cartasJugador);
  }
}

public void jugadaMaestra () {
  Carta miCarta=cartasCPU[cartasCPU.length-1];
  cartaActiva=miCarta;
  //damos la vuelta a la \u00faltima carta
  miCarta.miEstado=true;
  activaBotones (miCarta, false);
  //escogemos la opcion guapa
  float minimo=MAX_FLOAT;
  int indiceBuscado=MAX_INT;
  
  for (int i=0; i<miCarta.misDatos.length; i++) {
    if (miCarta.misDatos[i]<minimo) {
      minimo=miCarta.misDatos[i];
      indiceBuscado=i;
    }
  }
  
 // indiceBuscado=round(random(miCarta.misDatos.length-1));
 
  eligeMe (indiceBuscado);
}
public void eligeMe (int opcion) {
  //si el turno era de la CPU
  cartaOpuesta=cartasJugador[cartasJugador.length-1];
  
  //le anulamos sus botones y le ponemos el resalte
  cartaOpuesta.miEstado=true;
  activaBotones(cartaOpuesta, false);
  activaResalte (cartaOpuesta, opcion);
  activaResalte (cartaActiva, opcion);

  //vemos cual es la opcion resaltada y cual la opuesta
  opcionResaltada=cartaActiva.misDatos[opcion];
  opcionOpuesta=cartaOpuesta.misDatos[opcion];
  
  //se manda los datos al arbitro y que el dirima
  arbitro (opcionOpuesta, opcionResaltada, maximos [opcion]);
}

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


public int[] baraja(int[] previos) {
  int total = previos.length;
  int [] escogidas=new int [cartasTotales];
  int orden=0;
  for (int i = 0; i<total; i++) {
    int escogido = ceil(random((previos.length-1)));
    escogidas[orden]=previos[escogido];
    orden++;
    previos[escogido] = previos[previos.length-1];
    previos=shorten (previos);
  }
  return escogidas;
}

public float [] reparteDatosCarta (int num) {
  float [] nueva=new float [rotulos.length];
  for (int i=0; i<rotulos.length; i++) {
    nueva [i]=datos [i][num];
  }
  return nueva;
}

/*
void pinta (int [] dato) {
  for (int i=0; i<dato.length; i++) {
    println (dato[i]);
  }
}
*/
public void activaBotones (Carta carta, boolean estado) {
  for (int i=0; i<rotulos.length; i++) {
    carta.botones[i].enabled=estado;
    if (estado) {
      carta.botones[i].release();
    }
  }
}

public Carta[] ordenaArray (Carta[] cad, Carta nuevo) {

  cad = (Carta []) append(cad, nuevo);
  Carta [] aux= {    
    cad[cad.length-1], cad[cad.length-2]
  };
  for (int i=cad.length-1;i>=0;i--) {
    if (i>=2) {
      cad[i] = cad[i-2];
    }
    else {
      cad[i] = aux[i];
    }
  }
  return cad;
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#c0c0c0", "HospitalBattle" });
  }
}
