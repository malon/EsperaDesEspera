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

void dibujaMarcador ()
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
void dibujaMazos ()
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
void dibujaChuleta () {
  fill (colorOcre);
  textFont (miTipo12);
  textAlign (CENTER, BOTTOM);
  text ("DATOS MÁXIMOS", 100, height/2);
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
void dibujaMenu () {
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

void dibujaInstrucciones () {
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
void dibujaGameOver () {
  image (fondoGameOver, 0, 0);
  image (mensajeGameOver, width/2-mensajeGameOver.width/2, height/2-mensajeGameOver.height/2);
  textFont (miTipo12);
  textAlign (CENTER, TOP);
  text (textoGameOver, width/2-mensajeGameOver.width/2+120, height/2, 300, 100);
  textSize (12);
}

