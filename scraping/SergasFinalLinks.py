  
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

from BeautifulSoup import BeautifulSoup 
import urllib2

#Intermediate file with links to the pages we need to scrape.
fDocLinks = 'SergasDocLinks.txt'
#Output file where the final links are written.
fLinks = 'SergasFinalLinks.txt'

inputFile = open(fDocLinks,'r')
outputFile = open(fLinks,'w')
for line in inputFile.readlines():
	#print 'DocLink: ', line
	try: 		
		html = urllib2.urlopen(line).read()
		soup = BeautifulSoup(html)		
		aux = line.rpartition('/')
		uri = aux[0]+aux[1]				
		for link in soup.findAll('a'):
			#print 'href: ' , link['href']	
			if (link['href'].find('definic') == -1):			
				outputFile.write(uri+link['href']+u'\n')		
	except Exception, e:
		print e	
inputFile.close()
outputFile.close()

