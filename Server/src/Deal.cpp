/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月09日 星期二 17时02分43秒
	>	File Name: Deal.cc

***********************************************/
#include "Deal.h"


void Deal::Perform(char *recv,char *send)
{

	Document doc;
	doc.Parse(recv);
	time_t tt = time(NULL);
	tm* t = localtime(&tt);
	if(!doc.HasMember("method"))
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		sprintf(send,"{\"method\":\"unknown_command\"}");
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"SayHi")==0)
	{
	}
	else if(strcmp(doc["method"].GetString(),"import_student")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"import_room")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"delete_room")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"delete_student")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"show_student")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"show_room")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"search_student")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"get_room")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("Server: %s\n",send);
	}
	else if(strcmp(doc["method"].GetString(),"get_student")==0)
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}
	else
	{
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Client: %s\n",recv);
		sprintf(send,"{\"method\":\"unknown_command\"}");
		printf("[%02d:%02d:%02d]:",t->tm_hour,t->tm_min,t->tm_sec);
		printf("Server: %s\n",send);
	}

}
