/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月09日 星期二 00时20分20秒
	>	File Name: Main.cc

***********************************************/

#include "Server.h"
#include <cstdio>
#include <cstdlib>
#include <cctype>

#include "rapidjson/stringbuffer.h"


using namespace std;
int port = 20382;

int main(int argc,char **argv)
{
/*
	Document d;
	char path[30];
	sprintf(path,"/home/superdian/Downloads");
	CBrowse::getAllFiles(d,path);
	StringBuffer buffer;
	Writer<StringBuffer> writer(buffer);
	d.Accept(writer);
	printf("%s\n",buffer.GetString());
//	FileOp::getFile(fileName,readBuf);
//	FileOp::setFileName(fileName,newFileName);
*/
	while(1){
		try{
			printf("Please Input Server Port(default port 20382,enter to default) : ");
			char ch;
			int tmpport = 0;
			while(isdigit(ch = getchar())){
				tmpport *= 10;
				tmpport += ch - '0';
			}
			bool flag = true;
			if(tmpport > 65535) flag = false;
			if (tmpport < 10000)
				flag = false;
			if (flag)
				port = tmpport;
			Server s(port);
			s.Run();
		}
		catch(...)
		{
			printf("Unfortunately System Crashed!\nSystem Will Restart Soon.\n");
		}
	}
	return 0;
}

