/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月09日 星期二 17时02分43秒
	>	File Name: Deal.cc

***********************************************/
#include "Deal.h"

using namespace rapidjson;
void Deal::Perform(char *recv,char *send)
{
	char decode_recv[10000];
	char decode_send[10000];
	Base64::Decode(recv,decode_recv);

	Document doc;
	doc.Parse(decode_recv);
	time_t tt = time(NULL);
	tm* t = localtime(&tt);
	if(!doc.HasMember("method")){
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",decode_recv);
		sprintf(decode_send,"{\"method\":\"unknown_command\"}");
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",decode_send);
	}else if(strcmp(doc["method"].GetString(),"debug")==0 && doc.HasMember("encode")){
		if(strcmp(doc["encode"].GetString(),"plain")==0){
			sprintf(decode_send,"{\"method\":\"plain\"}");
		}else if(strcmp(doc["encode"].GetString(),"base64")==0){
			sprintf(decode_send,"{\"method\":\"base64\"}");
		}else{
			sprintf(decode_send,"{\"method\":\"Unknow_encode\"}");
		}
	}else{
		sprintf(decode_send,"{\"method\":\"Unknow_command\"}");
	}
	Base64::Encode(decode_send,send);
}
bool Deal::getPort(Document &doc,char *output){
	try{
		Document d;
		Pointer("/method").Set(d,"result");
		Pointer("/encode").Set(d,"Base64");
		int Port = Pointer("/Port").Get(doc)->GetInt();
		if(Pi.getValue(Port)==-1){
			Pointer("/result").Set(d,"in");
		}else if(Pi.getValue(Port)==0){
			Pointer("/result").Set(d,"out_low");
		}else if(Pi.getValue(Port)==1){
			Pointer("/result").Set(d,"out_high");
		}
		StringBuffer buffer;
		Writer<StringBuffer> writer(buffer);
		d.Accept(writer);
		strcpy(output,buffer.GetString());

	}catch(...){
		return false;
	}
	return true;
}

bool Deal::setPort(Document &doc,char *output){
	
	Document d;
	Pointer("/method").Set(d,"result");
	Pointer("/encode").Set(d,"Base64");
	int Port = Pointer("/Port").Get(doc)->GetInt();
	if(doc["value"]=="in"){
		pinMode(Port,INPUT);
	}else if(doc["value"]=="out_high"){
		pinMode(Port,OUTPUT);
		digitalWrite(Port,HIGH);
	}else if(doc["value"]=="out_low"){
		pinMode(Port,OUTPUT);
		digitalWrite(Port,LOW);
	}
	Pointer("/result").Set(d,"succeed");
	StringBuffer buffer;
	Writer<StringBuffer> writer(buffer);
	d.Accept(writer);
	strcpy(output,buffer.GetString());
	return true;
}


