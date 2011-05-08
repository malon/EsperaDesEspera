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
  color colorOcre=color (212, 207, 149);
  color colorAzul=color (71, 129, 158);
  color colorGranate=color (122, 26, 36);
  color colorAzulOscuro=color (41, 51, 54);
  color colorBlanco=color(255, 255, 255);
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
  void updatePos () {
    for (int i=0; i<misDatos.length; i++) {
      botones [i].posX=this.posX+20;

    }
  }
  //dibujamos
  void dibuja () {
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
        anguloResalte+=0.1;
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

