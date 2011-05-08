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

String cargaDatos(int numMax){
      
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
    




