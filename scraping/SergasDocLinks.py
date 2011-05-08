  
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
# -*- coding: UTF-8 -*-

from BeautifulSoup import BeautifulSoup, SoupStrainer 
import urllib, urllib2, re

#Homepage where the sergas waiting lists are hosted
home  = 'http://www.sergas.es/MostrarContidos_N2_T01.aspx?IdPaxina=40026'
#Site to include in case of relative paths
site = 'http://www.sergas.es'
#Bottom link page that allows to navigate to the different data for a waitingList
docLinks = 'DocLinks.htm'
#Exceptions to the automation process developed
skipped1 = 'http://www.sergas.es/Docs/ListasEspera/CIR/HTML/201003/WEB_CI~1/DocLinks.htm'
skipped2 = 'http://www.sergas.es/Docs/ListasEspera/CIR/HTML/200506/web_cir_2005-06_archivos/tabstrip.htm'

try: 
	html = urllib2.urlopen(home).read()
	findDivs = SoupStrainer('div', {'class':'list_nivel2'})
	soup = BeautifulSoup(html, parseOnlyThese=findDivs)
	output = open('SergasAuxLinks.txt','w')
	for link in soup.findAll('a', href=re.compile('MostrarContidos_')):
		uri = link['href'].partition('uri=')
		uriaux = uri[2]		
		if not re.match('^http',uriaux):
			uriaux = site+uriaux		
		ruta = uriaux.partition('.htm')
		rutaFinal = ruta[0]+'/'+docLinks
		output.write(rutaFinal+u'\n')
	#We manually enter this two skipped links that fall out of the automation
	output.write(skipped1+u'\n')
	output.write(skipped2+u'\n')	
	output.close()		
except Exception, e:
	print e	
