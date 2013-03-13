#!/usr/bin/python
import sys

def main():
    f = open(sys.argv[1],'r')
    for line in f.readlines():
        array = line.split('\t')
        print array[2] + "\t" + array[3]

if __name__ == '__main__':
    main()
