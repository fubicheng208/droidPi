#include <cstdio>
#include <cstring>
#include <algorithm>
#include <iostream>
#include <wiringPi.h>

class Piinfo{
private:
	int value[32];
	int pin[32]={11,12,13,15,16,18,22,7,3,5,24,26,19,21,23,8,10,-1,-1,-1,-1,29,31,33,35,37,32,36,38,40,27,28};
	int depin[44];
public:
	Piinfo();
	int readPinStatus(int number);
	bool setPinStatus(int number,int value);
};
