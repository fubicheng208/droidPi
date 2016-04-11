/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年04月10日 星期日 19时23分40秒
	>	File Name: Base64.cpp

***********************************************/

#include "Base64.h"



void Base64::Encode(char *input,char *output){
	
	char encode_table[]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	int head=0,tail=0;
	int tmp[11];
	int ilen = strlen(input);
	int olen = 0;
	int value;
	for(int i=0;i<ilen;i++){
		for(int j=0;j<8;j++){
			if(input[i] & 1<<(8-j-1)){
				tmp[(tail+j)%11]=1;
			}else{
				tmp[(tail+j)%11]=0;
			}
		}	
		tail = (tail+8)%11;
		while((tail-head>7)|(tail<head && tail+11-head>7)){
			
			value = 0;
			for(int i=0;i<6;i++)
				value = value*2 + tmp[(i+head)%11];
			head=(head+6)%11;
			output[olen++]=encode_table[value];
			//printf("output[%d]:%c\n",olen-1,output[olen-1]);
		}
	}
	while((tail-head+11)%6)	tmp[tail++]=0;
	if(tail != head){
		value = 0;
		for(int i=0;i<6;i++)
			value = value*2 + tmp[(i+head)%11];
		head=(head+6)%11;
		output[olen++]=encode_table[value];
		printf("2-output[%d]:%c\n",olen-1,output[olen-1]);
	}
	while(olen%4) output[olen++]='=';
	output[olen]='\0';
}

void Base64::Decode(char *input,char *output){
	char encode_table[]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	int decode_table[256];
	memset(decode_table,-1,sizeof decode_table);
	for(int i=0;i<64;i++){
		decode_table[(int)encode_table[i]]=i;
	}
	
	int ilen = strlen(input);
	int olen = 0;
	int head = 0;
	int tail = 0;
	int tmp[13];
	int value;
	for(int i = 0; i < ilen && input[i]!='='; i++){
		for(int j=0;j<6;j++){
			if(decode_table[(int)input[i]] & 1<<j){
				tmp[(tail+5-j)%13]=1;
			}else{
				tmp[(tail+5-j)%13]=0;
			}
		}
		tail = (tail+6)%13;
		while((tail-head>7)|(tail<head && tail+13-head>7)){
			value = 0;
			for(int i=0;i<8;i++){
				value = value*2 + tmp[(head+i)%13];
			}
			output[olen++]=value;
			printf("Hello %d %c\n",olen-1,value);	
			head = (head+8)%13;
		}
	}
	output[olen]='\0';
}
