#!/usr/bin/python
import sys

def main():
    i = 0
    f = open(sys.argv[1],'r')
    print 'var movie_ids = new Array();'
    for line in f.readlines():
        array = line.split('\t')
        if (len(array) == 5):
            print 'movie_ids[' + str(i) + '] = "tt' + array[3] + '";'
        i = i + 1

if __name__ == '__main__':
    main()
