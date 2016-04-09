/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月08日 星期一 15时49分47秒
	>	File Name: JsonFile.h

***********************************************/

#include "rapidjson/filereadstream.h"
#include "rapidjson/filewritestream.h"
#include "rapidjson/document.h"
#include "rapidjson/writer.h"
#include "rapidjson/prettywriter.h"
#include "rapidjson/stringbuffer.h"
#include "rapidjson/pointer.h"
#include <cstdio>
#include <cstring>

using namespace rapidjson;
class JsonFile{
public:
	static void jsonRead(char *,Document &);
	static void jsonRead(char *,char *);
	static bool jsonWrite(char *,Document &);
	static bool jsonPrettyWrite(char *,Document &);
	static bool jsonWrite(char *,char *);
	static bool jsonPrettyWrite(char *,char *);
	static void jsonReadDebug(char *);
	static void jsonWriteDebug();
};
