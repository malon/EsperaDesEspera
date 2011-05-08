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


void iniciaPartida () {
  //barajamos las cartas
  int[] cartasIniciales=new int [cartasTotales];
  for (int i=1; i<cartasTotales; i++) {
    cartasIniciales[i]=i;
  } 
  int [] cartasBarajadas=new int [cartasTotales];
  cartasBarajadas=baraja (cartasIniciales);
  //REPARTIMOS
  //vemos cuantas cartas tiene cada jugador
  float mitadCartas=float(cartasTotales)/2;
  float aux=ceil (mitadCartas);
  numCartasJugador=int (aux);
  aux=floor (mitadCartas);
  numCartasOrdenador=int(aux);
  
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
void cambiaTurno (int player) {
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
void activaMe (int opcion) {
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
void activaResalte (Carta carta, int opcion) {
  carta.resalte=true;
  carta.opcionActivada=opcion;
}

void arbitro (float jugada1, float jugada2, float referencia) {
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
void actualizaMazos (int ganador) {
  if (ganador==1) {
    //si gano el player
    // me apunto la última y la del mazodel CPU
    Carta ultima=cartasCPU[cartasCPU.length-1];
    //muevo la penúltima hasta una pos nueva

    cartasJugador=ordenaArray (cartasJugador, ultima);
    //Y  para el CPU  le quito la última carta
    cartasCPU=(Carta []) shorten (cartasCPU);
  }  
  else {

    // si gana la CPU
    // me apunto la última y la del mazo del Jugador
    Carta ultima=cartasJugador[cartasJugador.length-1];
    //muevo la penúltima hasta una pos nueva
    cartasCPU=ordenaArray (cartasCPU, ultima);

    //Y  para el Jugador  le quito la última carta
    cartasJugador=(Carta []) shorten (cartasJugador);
  }
}

void jugadaMaestra () {
  Carta miCarta=cartasCPU[cartasCPU.length-1];
  cartaActiva=miCarta;
  //damos la vuelta a la última carta
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
void eligeMe (int opcion) {
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

