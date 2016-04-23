/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月08日 星期一 20时33分43秒
	>	File Name: Server.h

***********************************************/

#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/wait.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <strings.h>
#include <errno.h>
#include <unistd.h>
#include <signal.h>
#include <netinet/in.h>
#include <string.h>
#include "Deal.h"

using namespace std;

class Server{
private:
	int portNumber;

	static void handleSig(int);
public:
	Server(int port);
	void Run();
};
