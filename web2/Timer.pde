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
  void resetea() {
    estado=true;
    episodio =1;
  }

  void dibuja () {

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

        carta1.posX=carta1.posX+(miDestino-carta1.posX)*0.1;
        carta2.posX=carta2.posX+(miDestino-carta2.posX)*0.1;
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
        carta1.posX=carta1.posX+(destino1-carta1.posX)*0.1;
        carta2.posX=carta2.posX+(destino2-carta2.posX)*0.1;

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

