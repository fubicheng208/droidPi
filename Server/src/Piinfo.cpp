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
	memset(depin,-1,sizeof(depin));
	for(int i=0;i<32;i++){
		if(pin[i]>0){
			depin[pin[i]] = i;
			pinMode(pin[i],INPUT);
			value[i]=-1;
		}
	}
}
int Piinfo::readPinStatus(int number){
				
	return value[depin[number]];
}

bool Piinfo::setPinStatus(int number, int v){
	try{
		if(v==-1){
			pinMode(pin[depin[number]],INPUT);
			value[depin[number]]=-1;
		}else if(v<2){
			pinMode(pin[depin[number]],OUTPUT);
			digitalWrite(pin[depin[number]],v);
			value[depin[number]]=1;
		}else{
			return false;
		}
	}catch(...){
		return false;
	}
	return true;
}

int Piinfo::getValue(int number){

	return value[number];

}
