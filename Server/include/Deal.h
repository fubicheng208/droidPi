/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月09日 星期二 17时02分38秒
	>	File Name: Deal.h

***********************************************/

#include <cstdio>
#include <ctime>
#include "rapidjson/document.h"
#include "rapidjson/writer.h"
#include "rapidjson/stringbuffer.h"
#include "Base64.h"
#include "rapidjson/pointer.h"

using namespace rapidjson;
class Deal{
public:
	static void Perform(char *,char *);
private:
	bool getPorts(Document &,char *);
	bool getPort(Document &,char *);
	bool setPort(Document &,char *);
};
