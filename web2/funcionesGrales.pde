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

void setup () {
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
void draw () {
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

void mousePressed() {
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
  void mouseReleased() {
    if (pantalla=="juego") {
      for (int i=0; i<rotulos.length; i++) {
        cartaActiva.botones[i].release();
      }
    }
  }
