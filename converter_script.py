# -*- coding: utf-8 -*-
"""
Created on Sun Jul 17 14:59:33 2016

@author: Ben
"""
#script to convert .muse file type to .csv file type
import subprocess

for i in range(31,39):
    call_fn = "muse-player"
    arg0 = "-f"
    arg1 = "-C"    
    path_from = "pathfrom" + "{0:03}".format(i)   + ".muse"
    path_to = "pathto" + "{0:03}".format(i)   + ".csv"
    subprocess.call([call_fn, arg0, path_from, arg1, path_to])
