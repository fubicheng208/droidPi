/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年04月11日 星期一 09时41分58秒
	>	File Name: src/Piinfo.cpp

***********************************************/

#include "Piinfo.h"

Piinfo::Piinfo(){
	memset(value,-1,sizeof(value));
	wiringPiSetup();
	for(int i=0;i<32;i++){
		if(pin[i]>0){
			pinMode();
		}
	}
}
int Piinfo::readPiStatus(int number){
	
}

bool setPinStatus(int number, int value){

}
