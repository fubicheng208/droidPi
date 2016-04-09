/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月08日 星期一 15时54分22秒
	>	File Name: JsonFile.cc

***********************************************/

#include "JsonFile.h"


using namespace rapidjson;

void JsonFile::jsonRead(char *fileName,Document &d)
{
	try
	{
	FILE *fp = fopen(fileName,"r");

	char readbuffer[65536];
	FileReadStream is(fp,readbuffer,sizeof(readbuffer));
	d.ParseStream(is);
	fclose(fp);
	}
	catch(...)
	{
	
	}
}
void JsonFile::jsonRead(char *fileName,char *buffer)
{
	FILE *fp = fopen(fileName,"r");
	char c;
	int len = 0;
	while((c=fgetc(fp))!=EOF)
		if(isgraph(c)||c==' ')
			buffer[len++]=c;
	buffer[len]='\0';
	fclose(fp);
}
bool JsonFile::jsonWrite(char *fileName,Document &d)
{
	try
	{
		FILE *fp = fopen(fileName,"w");
		char writebuffer[65536];
		FileWriteStream os(fp,writebuffer,sizeof(writebuffer));
		Writer<FileWriteStream> writer(os);
		d.Accept(writer);
		fclose(fp);
	}
	catch(...)
	{
		return false;
	}
	return true;
}

bool JsonFile::jsonPrettyWrite(char *fileName,Document &d)
{
	try
	{
		FILE *fp = fopen(fileName,"w");
		char writebuffer[65536];
		FileWriteStream os(fp,writebuffer,sizeof(writebuffer));
		PrettyWriter<FileWriteStream> writer(os);
		d.Accept(writer);
		fclose(fp);
	}
	catch(...)
	{
		return false;
	}
	return true;
}
bool JsonFile::jsonWrite(char *fileName,char *buffer)
{
	try
	{	
		Document d;
		d.Parse(buffer);
		FILE *fp = fopen(fileName,"w");
		char writebuffer[65536];
		FileWriteStream os(fp,writebuffer,sizeof(writebuffer));
		Writer<FileWriteStream> writer(os);
		d.Accept(writer);
		fclose(fp);
	}
	catch(...)
	{
		return false;
	}
	return true;
}
bool JsonFile::jsonPrettyWrite(char *fileName,char *buffer)
{
	try
	{

		Document d;
		d.Parse(buffer);
		FILE *fp = fopen(fileName,"w");
		char writebuffer[65536];
		FileWriteStream os(fp,writebuffer,sizeof(writebuffer));

		PrettyWriter<FileWriteStream> writer(os);
		d.Accept(writer);
		fclose(fp);
	}
	catch(...)
	{
		return false;
	}
	return true;
}
void JsonFile::jsonReadDebug(char *filename)
{
	char json[10000];
	char tmp[1000];
	FILE *fp = fopen(filename,"r");
	while(fgets(tmp,sizeof(tmp),fp)!=NULL)
		strcat(json,tmp);
	printf("%s\n",json);
	fclose(fp);
}
void JsonFile::jsonWriteDebug()
{
	char json[100]="{\"hello\":\"world\",\"a\":[1,3,4,5]}";
	Document d;
	d.Parse(json);
	
	FILE *fp = fopen("output.json","w");
	char writeBuffer[65536];
	FileWriteStream os(fp,writeBuffer,sizeof(writeBuffer));

	PrettyWriter<FileWriteStream> writer(os);
	d.Accept(writer);

	fclose(fp);
}
