
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
// tamaño del escenario
int ancho=823;
int alto=384;
//los colores son ocre, azul y granate
color colorOcre=color (212, 207, 149);
color colorAzul=color (71, 129, 158);
color colorGranate=color (122, 26, 36);
color colorAzulOscuro=color (41, 51, 54);
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
//tipografías a cargar //12 para los player y 10 para turno y botones
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
String  textoGameOver="Has aprendido algo?\nÉchate un par de partidas para tener ua visión más clara de lo que hay que esperar para operarse";
String [] instrucciones;
int insAlfa;
