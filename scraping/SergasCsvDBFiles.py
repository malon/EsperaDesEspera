  
    # This file is part of EsperaDesespera.
    #
    # Copyright (C) 2011 by Alberto Ariza, Juan Elosua & Marta Alonso
    #
    # This program is free software: you can redistribute it and/or modify
    # it under the terms of the GNU Affero General Public License as published by
    # the Free Software Foundation, either version 3 of the License, or
    # (at your option) any later version.
    #
    # This software is provided 'as-is', without any express or implied warranty.
    # In no event will the authors be held liable for any damages arising from the
    # use of this software.
    # 
    # You should have received a copy of the GNU Affero General Public License
    # along with this program.  If not, see <http://www.gnu.org/licenses/>.



#!/usr/bin/env python
# -*- coding: utf-8 -*-

import csv, re

#Diccionarios
dicts = ('dictHosp','dictServ','DictTipo','dictFecha','dictInte')
#Diccionario con los diferentes Hospitales
dictHosp = {}
#Diccionario con los diferentes Tipos de servicio
dictTipo = {}
#Diccionario con los diferentes Servicios
dictServ = {}
#Diccionario con las diferentes Fechas de Analísis
dictFecha = {}
#Diccionario con los diferentes intervalos a tener en cuenta
dictInte = {'0 - 3 m': '1',
						'3 - 6 m' : '2',
						'6 - 12 m' : '3',
						'más 12 m' : '4'}

#marcador de inicio de intervalos
s0a3 = '0 - 3 m'
#marcador de fin de intervalos
total = 'TOTAL'
tempo = 'Tempo medio de espera'
#fIndex = 'prueba.txt'
fIndex = 'CsvfilesIndex.txt'
#Los informes de CEX cambiaron a partir de 2006
CEXServChangeDate = '2006'
#A partir de esta fecha se añadio un informe de totales por hospital
reportChangeDate = 200912

#Funcion para determinar si el campo es numérico
def is_numeric(val):
	try:
		float(val)
	except ValueError, e:
		return False
	return True

#Procesar los csv de hospitales
def procesarHosp(l,tipo):
	try:
		print 'procesarHosp'
		#print 'longitud de la lista: ', len(l)
		#Buscamos la fecha de analisis
		i = l[1].index('/20')
		fechaAnalisis = l[1][i-5:i+5]
		if (not dictFecha.has_key(fechaAnalisis)):
			dictFecha[fechaAnalisis] = str(len(dictFecha)+1)
		if (not dictTipo.has_key(tipo)):
			dictTipo[tipo] = str(len(dictTipo)+1)
		i = l.index(s0a3)
		j = l.index(total)
		"""La longitud de cada fila hay que sumarle 1 para incluir ambos extremos
		y 2 más para la cabecera y tiempo medio"""
		longfila = (j-i+3)
		ifila = j+2
		ffila = ifila+longfila
		while (ffila <= len(l)):
			if (l[ifila] != total):
				if (not dictHosp.has_key(l[ifila])):
					dictHosp[l[ifila]] = str(len(dictHosp)+1)
				wr1.writerow([dictFecha[fechaAnalisis],
				dictHosp[l[ifila]],dictTipo[tipo],l[ffila-2].replace('.',''),l[ffila-1].replace(',','.')])
			ifila = ffila
			ffila += longfila
	except Exception, e:
		print 'Error', e
		return False
	return True

#Procesar los csv de servicios
def procesarServ(l,tipo):
	try:
		print 'procesarServ'
		#print 'longitud de la lista: ', len(l)
		#Buscamos la fecha de analisis
		i = l[1].index('/20')
		fechaAnalisis = l[1][i-5:i+5]
		if (not dictFecha.has_key(fechaAnalisis)):
			dictFecha[fechaAnalisis] = str(len(dictFecha)+1)
		if (not dictTipo.has_key(tipo)):
			dictTipo[tipo] = str(len(dictTipo)+1)
		if (l[1][i+1:i+5] <= CEXServChangeDate and tipo == 'CEX'):
			j = l.index('Pacientes')
			longfila = 3
		else:
			i = l.index(s0a3)
			j = l.index(total)
			longfila = (j-i+3)
		ifila = j+2
		ffila = ifila+longfila
		while (ffila <= len(l)):
			if (l[ifila] != total):
				if (not dictServ.has_key(l[ifila])):
					dictServ[l[ifila]] = str(len(dictServ)+1)
				wr2.writerow([dictFecha[fechaAnalisis],
				dictServ[l[ifila]],dictTipo[tipo],
				l[ffila-2].replace('.',''),l[ffila-1].replace(',','.')])
			else:
				wr3.writerow([dictFecha[fechaAnalisis],dictTipo[tipo],
				l[ffila-2].replace('.',''),l[ffila-1].replace(',','.')])
			ifila = ffila
			ffila += longfila
	except Exception, e:
		print e
		return False
	return True

#Procesar los csv de pruebas
def procesarPrueb(l,tipo):
	try:
		print 'procesarPrueb'
		#print 'longitud de la lista: ', len(l)
		#Buscamos la fecha de analisis
		i = l[1].index('/20')
		fechaAnalisis = l[1][i-5:i+5]
		if (not dictFecha.has_key(fechaAnalisis)):
			dictFecha[fechaAnalisis] = str(len(dictFecha)+1)
		if (not dictTipo.has_key(tipo)):
			dictTipo[tipo] = str(len(dictTipo)+1)
		if (fechaAnalisis != '31/03/2009'):
			s = '|'.join(l)
			l2 = s.split('|0 - 3 m|')
			l2=l2[1:]
			for chunk in l2:
				laux=chunk.split('|')
				j = laux.index(total)
				#calculamos la longitud en base a la posición de corte calculada
				longfila = j+4
				#nos posicionamos en la primera fila
				ifila = j+2
				ffila = ifila+longfila
				while (ffila <= len(laux)):
					if (laux[ifila] != total):
						if (not dictServ.has_key(laux[ifila])):
							dictServ[laux[ifila]] = str(len(dictServ)+1)
						wr2.writerow([dictFecha[fechaAnalisis],
						dictServ[laux[ifila]],dictTipo[tipo],
						laux[ffila-2].replace('.',''),laux[ffila-1].replace(',','.')])
					else:
						wr3.writerow([dictFecha[fechaAnalisis],dictTipo[tipo],
						laux[ffila-2].replace('.',''),laux[ffila-1].replace(',','.')])
					ifila = ffila
					ffila += longfila
		else:
			i = l.index(s0a3)
			j = l.index(total)
			longfila = (j-i+4)
			ifila = j+3
			ffila = ifila+longfila
			while (ffila <= len(l)):
				if (l[ifila] != total):
					if (not dictServ.has_key(l[ifila])):
						dictServ[l[ifila]] = str(len(dictServ)+1)
					wr2.writerow([dictFecha[fechaAnalisis],dictServ[l[ifila]],
					dictTipo[tipo],l[ffila-3].replace('.',''),l[ffila-2].replace(',','.')])
				else:
					wr3.writerow([dictFecha[fechaAnalisis],
					dictTipo[tipo],l[ffila-3].replace('.',''),l[ffila-2].replace(',','.')])
				ifila = ffila
				ffila += longfila
	except Exception, e:
		print e
		return False
	return True

#Procesar los csv de hospitales y servicios
def procesarHospServ(l, tipo):
	try:
		print 'procesarHospServ'
		#Buscamos la fecha de analisis
		i = l[1].index('/20')
		fechaAnalisis = l[1][i-5:i+5]
		if (not dictFecha.has_key(fechaAnalisis)):
			dictFecha[fechaAnalisis] = str(len(dictFecha)+1)
		if (not dictTipo.has_key(tipo)):
			dictTipo[tipo] = str(len(dictTipo)+1)
		s = '|'.join(l)
		if (l[1][i+1:i+5] <= CEXServChangeDate and tipo == 'CEX'):
			l2 = s.split('|Pacientes|')
			hospital = ''
			for chunk in l2:
				laux=chunk.split('|')
				#almacenamos el nombre del hospital de la siguiente tabla
				if (laux.count(tempo) == 0):
					hospital = laux[len(laux)-2]
					if (not dictHosp.has_key(hospital)):
						dictHosp[hospital] = str(len(dictHosp)+1)
					continue
				j = laux.index(tempo)
				#calculamos la longitud en base a la posición de corte calculada
				longfila = j+3
				#nos posicionamos en la primera fila
				ifila = j+1
				ffila = ifila+longfila
				while (ffila <= len(laux)):
					if (laux[ifila] != total):
						if (not dictServ.has_key(laux[ifila])):
							dictServ[laux[ifila]] = str(len(dictServ)+1)
						#Solamente cuenta con totales y no con valores individuales por intervalo
						wr4.writerow([dictFecha[fechaAnalisis],dictHosp[hospital],dictServ[laux[ifila]],
						dictTipo[tipo],laux[ffila-2].replace('.',''),laux[ffila-1].replace(',','.')])
					else:
						wr1.writerow([dictFecha[fechaAnalisis],dictHosp[hospital],
						dictTipo[tipo],laux[ffila-2].replace('.',''),laux[ffila-1].replace(',','.')])
					ifila = ffila
					ffila += longfila
				hospital = laux[len(laux)-2]
				if (not chunk == l2[len(l2)-1]):
					if (not dictHosp.has_key(hospital)):
						dictHosp[hospital] = str(len(dictHosp)+1)
		else:
			l2 = s.split('|0 - 3 m|')
			hospital = ''
			for chunk in l2:
				laux=chunk.split('|')
				#almacenamos el nombre del hospital de la siguiente tabla
				if (laux.count(total) == 0):
					hospital = laux[len(laux)-2]
					if (not dictHosp.has_key(hospital)):
						dictHosp[hospital] = str(len(dictHosp)+1)
					continue
				j = laux.index(total)
				#calculamos la longitud en base a la posición de corte calculada
				longfila = j+4
				#nos posicionamos en la primera fila
				ifila = j+2
				ffila = ifila+longfila
				while (ffila <= len(laux)):
					if (laux[ifila] != total):
						if (not dictServ.has_key(laux[ifila])):
							dictServ[laux[ifila]] = str(len(dictServ)+1)
						#tenemos que quitar la primera columna y las 2 ultimas
						longdatos = longfila-3
						for i in range(longdatos):
							wr5.writerow([dictFecha[fechaAnalisis],
							str(i+1),dictHosp[hospital],dictServ[laux[ifila]],dictTipo[tipo],laux[ifila+1+i].replace('.','')])
						wr4.writerow([dictFecha[fechaAnalisis],
						dictHosp[hospital],dictServ[laux[ifila]],dictTipo[tipo],
						laux[ffila-2].replace('.',''),laux[ffila-1].replace(',','.')])
					else:
						if(int(fechaAnalisis[6:10]+fechaAnalisis[3:5]) < reportChangeDate):
							wr1.writerow([dictFecha[fechaAnalisis],dictHosp[hospital],
							dictTipo[tipo],laux[ffila-2].replace('.',''),laux[ffila-1].replace(',','.')])
					ifila = ffila
					ffila += longfila
				hospital = laux[len(laux)-2]
				if (not chunk == l2[len(l2)-1]):
					if (not dictHosp.has_key(hospital)):
						dictHosp[hospital] = str(len(dictHosp)+1)
	except Exception, e:
		print e
		return False
	return True

#Procesar los csv de hospitales y pruebas
def procesarHospPrueb(l, tipo):
	try:
		print 'procesarHospPrueb'
		#Buscamos la fecha de analisis
		i = l[1].index('/20')
		fechaAnalisis = l[1][i-5:i+5]
		if (not dictFecha.has_key(fechaAnalisis)):
			dictFecha[fechaAnalisis] = str(len(dictFecha)+1)
		if (not dictTipo.has_key(tipo)):
			dictTipo[tipo] = str(len(dictTipo)+1)
		if (fechaAnalisis == '31/03/2009'):
			offset = 1
		else:
			offset = 0
		s = '|'.join(l)
		l2 = s.split('|0 - 3 m|')
		hospital = ''
		for chunk in l2:
			laux=chunk.split('|')
			#almacenamos el nombre del hospital de la siguiente tabla
			if (laux.count(total) == 0):
				hospital = laux[len(laux)-3+offset]
				if (not dictHosp.has_key(hospital)):
					dictHosp[hospital] = str(len(dictHosp)+1)
				continue
			j = laux.index(total)
			#calculamos la longitud en base a la posición de corte calculada
			longfila = j+4+offset
			#nos posicionamos en el principio de la fila
			ifila = j+2+offset
			ffila = ifila+longfila
			while (ffila <= len(laux)):
				if (laux[ifila] != total):
					if (not dictServ.has_key(laux[ifila])):
						dictServ[laux[ifila]] = str(len(dictServ)+1)
					#tenemos que quitar la primera columna y las 2 ultimas
					longdatos = longfila-3
					for i in range(longdatos):
						wr5.writerow([dictFecha[fechaAnalisis],str(i+1),
						dictHosp[hospital],dictServ[laux[ifila]],dictTipo[tipo],laux[ifila+1+i].replace('.','')])
					wr4.writerow([dictFecha[fechaAnalisis],dictHosp[hospital],dictServ[laux[ifila]],
					dictTipo[tipo],laux[ffila-(2+offset)].replace('.',''),laux[ffila-(1+offset)].replace(',','.')])
				ifila = ffila
				ffila += longfila
			h =laux[len(laux)-3+offset].replace(',','.')
			if (not is_numeric(h)):
				hospital = laux[len(laux)-3+offset]
				if (not chunk == l2[len(l2)-1]):
					if (not dictHosp.has_key(hospital)):
						dictHosp[hospital] = str(len(dictHosp)+1)
	except Exception, e:
		print e
		return False
	return True

#Fichero de entrada
fileInput = open(fIndex,'r')
#Ficheros de salida de datos
fileOutput1 = open('TotHosp.csv','wb')
wr1 = csv.writer(fileOutput1, quoting=csv.QUOTE_ALL)
fileOutput2 = open('TotServ.csv','wb')
wr2 = csv.writer(fileOutput2, quoting=csv.QUOTE_ALL)
fileOutput3 = open('TotTipoServ.csv','wb')
wr3 = csv.writer(fileOutput3, quoting=csv.QUOTE_ALL)
fileOutput4 = open('TotHospServ.csv','wb')
wr4 = csv.writer(fileOutput4, quoting=csv.QUOTE_ALL)
fileOutput5 = open('DatosPacien.csv','wb')
wr5 = csv.writer(fileOutput5, quoting=csv.QUOTE_ALL)
#Ficheros de salida de definición
fileOutput6 = open('DefHosp.csv','wb')
wr6 = csv.writer(fileOutput6, quoting=csv.QUOTE_ALL)
fileOutput7 = open('DefServ.csv','wb')
wr7 = csv.writer(fileOutput7, quoting=csv.QUOTE_ALL)
fileOutput8 = open('DefTipoServ.csv','wb')
wr8 = csv.writer(fileOutput8, quoting=csv.QUOTE_ALL)
fileOutput9 = open('DefFecha.csv','wb')
wr9 = csv.writer(fileOutput9, quoting=csv.QUOTE_ALL)
fileOutput10 = open('DefInte.csv','wb')
wr10 = csv.writer(fileOutput10, quoting=csv.QUOTE_ALL)

for line in fileInput.readlines():
	try:
		tipo = line[4:7]
		ifile  = open(line.rstrip(), "rb")
		reader = csv.reader(ifile)
		for row in reader:
			if (row[0].find('por hospitais e servizos') != -1):
				print line.rstrip()
				procesarHospServ(row,tipo)
			elif (row[0].find('por hospitais e probas') != -1 or
			row[0].find('probas diagnósticas por hospitais') != -1):
				print line.rstrip()
				procesarHospPrueb(row,tipo)
			elif (row[0].find('por servizos') != -1):
				print line.rstrip()
				procesarServ(row,tipo)
			elif (row[0].find('probas diagnósticas') != -1):
				print line.rstrip()
				procesarPrueb(row,tipo)
			elif (row[0].find('por hospitais') != -1):
				print line.rstrip()
				procesarHosp(row,tipo)
			else:
				print 'Categoria no esperada ', row[0]
		ifile.close()
	except Exception, e:
		print 'Error: ', e

try:
	l	= dictHosp.keys()
	for data in l:
		wr6.writerow([dictHosp[data],data,data])
	l	= dictServ.keys()
	for data in l:
		wr7.writerow([dictServ[data],data,data])
	l	= dictTipo.keys()
	for data in l:
		wr8.writerow([dictTipo[data],data,data,data])
	l	= dictFecha.keys()
	for data in l:
		wr9.writerow([dictFecha[data],data,data,' '])
	l	= dictInte.keys()
	for data in l:
		wr10.writerow([dictInte[data],data,data])
except Exception, e:
	print e

#print 'diccionario de fechas: ', dictFecha
#print 'diccionario de hospitales: ', dictHosp
#print 'diccionario de servicios: ', dictServ
#print 'diccionario de tipos: ', dictTipo
#print 'diccionario de intervalos: ', dictInte
fileOutput1.close()
fileOutput2.close()
fileOutput3.close()
fileOutput4.close()
fileOutput5.close()
fileOutput6.close()
fileOutput7.close()
fileOutput8.close()
fileOutput9.close()
fileOutput10.close()
fileInput.close()

