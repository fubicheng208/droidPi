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

	Document doc;
	doc.Parse(recv);
	time_t tt = time(NULL);
	tm* t = localtime(&tt);
	if(!doc.HasMember("method"))
	{
//		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		sprintf(send,"{\"method\":\"unknown_command\"}");
//		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}else if(strcmp(doc["method"].GetString(),"debug")==0 && doc.HasMember("encode")){
		if(strcmp(doc["encode"].GetString(),"plain")==0){
			sprintf(send,"{\"method\":\"plain\"}");
		}else if(strcmp(doc["encode"].GetString(),"base64")==0){
			sprintf(send,"{\"method\":\"base64\"}");
		}else{
			sprintf(send,"{\"method\":\"Unknow_encode\"}");
		}
	}else{
		sprintf(send,"{\"method\":\"Unknow_command\"}");
	}

}
