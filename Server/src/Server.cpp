/***********************************************


	>	Author: SuperDian
	>	Mail: tech-sky@outlook.com
	>	Create Time: 2016年02月08日 星期一 23时27分39秒
	>	File Name: Server.cc

***********************************************/

#include "Server.h"

void Server::handleSig(int signum)
{
	while(waitpid(-1, NULL, WNOHANG) > 0);
}
Server::Server(int port=23456)
{
	portNumber = port;
}
void Server::Run()
{
	printf("Run at port: %d\n",portNumber);
	int MAXHOSTNAME = 256;
	struct sockaddr_in socketInfo;
	char sysHost[MAXHOSTNAME+1];
	struct hostent *hPtr;
	int socketHandle;

	signal(SIGCHLD,Server::handleSig);

	bzero(&socketInfo, sizeof(sockaddr_in));
	gethostname(sysHost,MAXHOSTNAME);

	if((hPtr = gethostbyname(sysHost)) == NULL)
	{
		cerr << "System hostname misconfigured." << endl;
		exit(EXIT_FAILURE);
	}
	
	if((socketHandle = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		close(socketHandle);
		exit(EXIT_FAILURE);
	}

	socketInfo.sin_family = AF_INET;
	socketInfo.sin_addr.s_addr = INADDR_ANY;
	socketInfo.sin_port = htons(portNumber);

	if(bind(socketHandle, (struct sockaddr *)&socketInfo, sizeof(struct sockaddr_in)) < 0)
	{
		close(socketHandle);
		perror("bind");
		exit(EXIT_FAILURE);
	}
	listen(socketHandle, 1);
	int socketConnection;

	while(true)
	{
		if((socketConnection = accept(socketHandle, NULL, NULL)) < 0)
		{
			close(socketHandle);
			if(errno == EINTR) continue;
			perror("accept");
			exit(EXIT_FAILURE);
		}
		switch(fork())
		{
			case -1:
				printf("case: -1\n");
				perror("fork");
				close(socketHandle);
				close(socketConnection);
				exit(EXIT_FAILURE);
			case 0:
				close(socketHandle);
				char readbuffer[1000],writebuffer[80000];
				if(recv(socketConnection, readbuffer, sizeof(readbuffer), 0) < 0)
				{
					printf("Read perform faild!\n");
					break;
				}
				Deal::Perform(readbuffer,writebuffer);
				sprintf(writebuffer,"%s\n",writebuffer);
				if(send(socketConnection,writebuffer,sizeof(writebuffer), 0) < 0 )
				{
					printf("Write perform faild!\n");
					break;
				}
				exit(0);
			default:
				close(socketConnection);
				continue;
		}
	}
}

