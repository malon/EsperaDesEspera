  
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

from BeautifulSoup import BeautifulSoup
import urllib2, csv, re

fLinks = 'SergasFinalLinks.txt'
fIndex = 'CsvfilesIndex.txt'
#reports changed after 200809
reportChangeDate = 200809
#Exclusiones por diferencias en formatos y coherencia de datos
reportMinimumDate = 200603
#To scrape data from reports
newTag = 'td'
newFilter = {'class': re.compile('s*')}
#To scrape data from old fashion reports
oldTag = 'font'
oldFilter = {'size':'1','face':'Verdana'}

inputFile = open(fLinks,'r')
indexFile = open(fIndex,'w')
sinpuntos = re.compile('([0-9]+)\.([0-9]+)\.([0-9]+)')
for line in inputFile.readlines():
	#print 'Link: ', line
	try:
		html = urllib2.urlopen(line).read()
		#Nuevas lineas
		aux = line.partition('ListasEspera/')
		aux2 = aux[2].split('/')
		if (aux2[0] == 'PDT'):
			aux3 = 'csv/'+aux2[0]+'_'+aux2[1]+'_'+aux2[3]+'.csv'
			reportDate = int(aux2[1])
			html = html.replace('&nbsp;','0')
		else:
			aux3 = 'csv/'+aux2[0]+'_'+aux2[2]+'_'+aux2[4]+'.csv'
			reportDate = int(aux2[2])
			if (reportDate > reportChangeDate):
				html = html.replace('&nbsp;','0')
			else:
				html = html.replace('&nbsp;','<FONT SIZE=1 FACE="Verdana">0</FONT>')
			if ((aux2[4].find('neros') != -1) or reportDate < reportMinimumDate):
				continue
		soup = BeautifulSoup(html)
		output = open(aux3,'wb')
		wr = csv.writer(output, quoting=csv.QUOTE_ALL)
		if (reportDate > reportChangeDate):
			print 'hola'
			wr.writerow([data.string for data in soup.findAll(newTag, newFilter)])
		else:
			print 'hola2'
			wr.writerow([data.string for data in soup.findAll(oldTag, oldFilter)])
		output.close()
		indexFile.write(aux3+u'\n')
	except Exception, e:
		print e
inputFile.close()
indexFile.close()
