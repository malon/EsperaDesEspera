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


int[] baraja(int[] previos) {
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

float [] reparteDatosCarta (int num) {
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
void activaBotones (Carta carta, boolean estado) {
  for (int i=0; i<rotulos.length; i++) {
    carta.botones[i].enabled=estado;
    if (estado) {
      carta.botones[i].release();
    }
  }
}

Carta[] ordenaArray (Carta[] cad, Carta nuevo) {

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

